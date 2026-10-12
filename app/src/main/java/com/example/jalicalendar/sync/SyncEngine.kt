package com.example.jalicalendar.sync

import com.example.jalicalendar.auth.AuthManager
import com.example.jalicalendar.core.time.Rfc5545
import com.example.jalicalendar.data.remote.CalendarApi
import com.example.jalicalendar.data.remote.EventDto
import com.example.jalicalendar.data.remote.TaskDto
import com.example.jalicalendar.data.remote.TasksApi
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.model.ConflictPolicy
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Task
import com.example.jalicalendar.domain.repo.AccountRepository
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import com.example.jalicalendar.domain.repo.TaskRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * One full sync pass, run from WorkManager (periodic 15 min / one-time on
 * demand / on app open). Algorithm per calendar:
 *
 * 1. Push local dirty rows (upserts then deletes); 404 on delete = already gone.
 * 2. Pull with the stored syncToken; on `410 Gone` (token expired/rejected)
 *    fall back to one full listing and rebase the cursor.
 * 3. Merge: remote rows land via [EventRepository.upsertSynced] — unless the
 *    local row is dirty, in which case [ConflictPolicy] decides (LWW compares
 *    localUpdatedAt vs remote `updated`; PROMPT keeps the local copy and
 *    raises a [ConflictStore] record for the user).
 *
 * Everything is idempotent: re-running a pass (or replaying a page) converges.
 */
@Singleton
class SyncEngine @Inject constructor(
    private val authManager: AuthManager,
    private val accountRepo: AccountRepository,
    private val calendarRepo: CalendarRepository,
    private val eventRepo: EventRepository,
    private val taskRepo: TaskRepository,
    private val settingsRepo: SettingsRepository,
    private val calendarApi: CalendarApi,
    private val tasksApi: TasksApi,
    private val conflicts: ConflictStore,
    private val cursorStore: SyncCursorStore,
    private val statusStore: SyncStatusStore,
    private val watchManager: WatchManager,
    private val reminderScheduler: com.example.jalicalendar.reminders.ReminderScheduler,
) {

    /** Runs one pass; never throws — the result is folded into [SyncStatusStore]. */
    suspend fun syncAll(): SyncResult {
        if (!authManager.isConfigured() || authManager.tokenStoreSession() == null) {
            return SyncResult.Skipped("not signed in")
        }
        statusStore.setRunning()
        return try {
            if (authManager.needsRefresh()) {
                if (!authManager.refresh()) {
                    statusStore.setFailed("Token refresh failed — sign in again")
                    return SyncResult.Failed("refresh failed")
                }
            }
            syncCalendarList()
            syncEvents()
            syncTasks()
            // Re-arm reminder alarms for (possibly new/changed) synced events.
            reminderScheduler.scheduleAll(eventRepo.upcomingWithReminders(System.currentTimeMillis()))
            // Best-effort: renew the push channel (no-op unless a webhook is set).
            watchManager.renewIfNeeded()
            statusStore.setSucceeded()
            SyncResult.Success
        } catch (e: Exception) {
            val message = e.message ?: e::class.simpleName ?: "sync failed"
            statusStore.setFailed(message)
            SyncResult.Failed(message)
        }
    }

    // -- calendar list -------------------------------------------------------

    private suspend fun syncCalendarList() {
        val account = accountRepo.primaryAccount() ?: return
        val existing = calendarRepo.observeCalendars()
            .first()
            .associateBy { it.remoteId ?: it.id }

        val cursor = cursorStore.get(SyncCursorStore.CALENDAR_LIST)
        val pages: List<com.example.jalicalendar.data.remote.CalendarListEntryDto> =
            if (cursor == null) fetchAllCalendars() else fetchCalendarsIncremental(cursor)

        for (entry in pages) {
            val id = entry.id ?: continue
            val local = existing[id]
            // `cancelled` semantics don't exist on calendarList; a missing summary
            // after incremental sync means the calendar was removed remotely.
            if (entry.summary == null && cursor != null) {
                local?.let { calendarRepo.delete(it.id) }
                continue
            }
            val merged = mergeCalendar(local, entry, account.id)
            calendarRepo.upsert(merged)
        }
    }

    private suspend fun fetchAllCalendars(): List<com.example.jalicalendar.data.remote.CalendarListEntryDto> {
        val out = mutableListOf<com.example.jalicalendar.data.remote.CalendarListEntryDto>()
        var page: String? = null
        var syncToken: String? = null
        do {
            val resp = calendarApi.listCalendarsFull(pageToken = page)
            out += resp.items.orEmpty()
            page = resp.nextPageToken
            syncToken = resp.nextSyncToken
        } while (page != null)
        syncToken?.let { cursorStore.set(SyncCursorStore.CALENDAR_LIST, it) }
        return out
    }

    private suspend fun fetchCalendarsIncremental(token: String): List<com.example.jalicalendar.data.remote.CalendarListEntryDto> =
        try {
            val out = mutableListOf<com.example.jalicalendar.data.remote.CalendarListEntryDto>()
            var page: String? = null
            var syncToken: String? = null
            do {
                val resp = calendarApi.listCalendarsIncremental(token)
                out += resp.items.orEmpty()
                page = resp.nextPageToken
                syncToken = resp.nextSyncToken
            } while (page != null)
            syncToken?.let { cursorStore.set(SyncCursorStore.CALENDAR_LIST, it) }
            out
        } catch (e: HttpException) {
            if (e.code() == 410) {
                // Cursor rejected: full resync, one time.
                cursorStore.set(SyncCursorStore.CALENDAR_LIST, null)
                fetchAllCalendars()
            } else throw e
        }

    private fun mergeCalendar(
        local: Calendar?,
        entry: com.example.jalicalendar.data.remote.CalendarListEntryDto,
        accountId: String,
    ): Calendar = Calendar(
        id = local?.id ?: entry.id ?: UUID.randomUUID().toString(),
        accountId = accountId,
        remoteId = entry.id,
        type = Calendar.TYPE_CALENDAR,
        title = entry.summary ?: local?.title ?: entry.id.orEmpty(),
        description = entry.description ?: local?.description,
        colorArgb = local?.colorArgb ?: Calendar.DEFAULT_COLOR,
        timeZone = entry.timeZone ?: local?.timeZone ?: "Asia/Tehran",
        isPrimary = entry.primary == true,
        accessRole = entry.accessRole ?: local?.accessRole ?: "reader",
        hidden = entry.hidden ?: false,
        selected = local?.selected ?: (entry.selected ?: true),
        syncToken = local?.syncToken,
        lastSyncedAtMillis = System.currentTimeMillis(),
        localUpdatedAtMillis = local?.localUpdatedAtMillis ?: 0L,
        dirty = false,
        deleted = false,
    )

    // -- events --------------------------------------------------------------

    private suspend fun syncEvents() {
        val calendars = calendarRepo.observeCalendars().first()
            .filter { it.type == Calendar.TYPE_CALENDAR && it.remoteId != null }
        for (calendar in calendars) {
            syncOneCalendar(calendar)
        }
    }

    private suspend fun syncOneCalendar(calendar: Calendar) {
        val remoteId = calendar.remoteId ?: return
        pushEvents(calendar)
        pullEvents(calendar, remoteId)
    }

    private suspend fun pushEvents(calendar: Calendar) {
        if (!calendar.canWrite) return
        val remoteId = calendar.remoteId ?: return
        val pending = eventRepo.pendingChanges().filter { it.calendarId == calendar.id }
        for (event in pending) {
            try {
                if (event.deleted) {
                    if (event.remoteId != null) {
                        calendarApi.deleteEvent(remoteId, event.remoteId!!)
                    }
                    eventRepo.purgeSyncedDeleted(event.id)
                } else {
                    val dto = event.toDto()
                    val saved = if (event.remoteId == null) {
                        calendarApi.insertEvent(remoteId, dto)
                    } else {
                        calendarApi.updateEvent(remoteId, event.remoteId!!, dto)
                    }
                    eventRepo.markSynced(
                        id = event.id,
                        remoteId = saved.id,
                        etag = saved.etag,
                        remoteUpdatedAtMillis = saved.updated?.let(Rfc5545::parseDateTime),
                    )
                }
            } catch (e: HttpException) {
                if (e.code() == 404 && event.deleted) {
                    eventRepo.purgeSyncedDeleted(event.id)
                } else if (e.code() == 409 || e.code() == 412) {
                    // ETag mismatch: someone else changed the server copy.
                    handlePushConflict(calendar, event, remoteId)
                } else {
                    throw e
                }
            }
        }
    }

    private suspend fun handlePushConflict(calendar: Calendar, event: Event, remoteId: String) {
        val settings = settingsRepo.current()
        if (settings.conflictPolicy == ConflictPolicy.LAST_WRITE_WINS) {
            // Force push with the latest server etag.
            val server = calendarApi.getEvent(remoteId, event.remoteId!!)
            val forced = calendarApi.updateEvent(
                remoteId, event.remoteId!!, event.toDto().copy(etag = server.etag),
            )
            eventRepo.markSynced(
                id = event.id,
                remoteId = forced.id,
                etag = forced.etag,
                remoteUpdatedAtMillis = forced.updated?.let(Rfc5545::parseDateTime),
            )
        } else {
            conflicts.record(event.id, event.title, ConflictSource.PUSH)
        }
    }

    private suspend fun pullEvents(calendar: Calendar, remoteId: String) {
        val cursor = calendar.syncToken
        val items = try {
            if (cursor.isNullOrEmpty()) fetchAllEvents(remoteId) else fetchEventsIncremental(remoteId, cursor)
        } catch (e: HttpException) {
            if (e.code() == 410) fetchAllEvents(remoteId) else throw e
        }
        for (dto in items) {
            mergeRemoteEvent(calendar, dto)
        }
    }

    private suspend fun fetchAllEvents(remoteId: String): List<EventDto> {
        val out = mutableListOf<EventDto>()
        var page: String? = null
        var syncToken: String? = null
        do {
            val resp = calendarApi.listEventsFull(remoteId, pageToken = page)
            out += resp.items.orEmpty()
            page = resp.nextPageToken
            syncToken = resp.nextSyncToken
        } while (page != null)
        syncToken?.let { calendarRepo.setSyncToken(remoteId, it, System.currentTimeMillis()) }
        return out
    }

    private suspend fun fetchEventsIncremental(remoteId: String, token: String): List<EventDto> {
        val out = mutableListOf<EventDto>()
        var page: String? = null
        var syncToken: String? = null
        do {
            val resp = calendarApi.listEventsIncremental(remoteId, token)
            out += resp.items.orEmpty()
            page = resp.nextPageToken
            syncToken = resp.nextSyncToken
        } while (page != null)
        syncToken?.let { calendarRepo.setSyncToken(remoteId, it, System.currentTimeMillis()) }
        return out
    }

    private suspend fun mergeRemoteEvent(calendar: Calendar, dto: EventDto) {
        val remoteId = dto.id ?: return
        val incoming = dto.toDomain(calendarId = calendar.id, localId = null)
        val local = eventRepo.findLocalByRemoteId(remoteId)
        when {
            dto.status == "cancelled" -> {
                if (local != null && local.dirty) {
                    // local edit wins over remote cancellation per policy
                    applyConflictPolicy(calendar, local, incoming)
                } else if (local != null) {
                    eventRepo.delete(local)
                }
                // never-synced cancelled rows: ignore
            }
            local == null -> eventRepo.upsertSynced(incoming)
            local.dirty -> applyConflictPolicy(calendar, local, incoming)
            else -> eventRepo.upsertSynced(incoming.copy(id = local.id))
        }
    }

    private suspend fun applyConflictPolicy(calendar: Calendar, local: Event, incoming: Event) {
        val settings = settingsRepo.current()
        if (settings.conflictPolicy == ConflictPolicy.LAST_WRITE_WINS) {
            val localTime = local.localUpdatedAtMillis
            val remoteTime = incoming.remoteUpdatedAtMillis ?: 0L
            if (localTime >= remoteTime) {
                // Local copy is newer: leave it dirty, next pass pushes it.
                return
            }
            eventRepo.upsertSynced(incoming.copy(id = local.id))
        } else {
            conflicts.record(local.id, local.title, ConflictSource.PULL)
        }
    }

    // -- tasks ---------------------------------------------------------------

    private suspend fun syncTasks() {
        val taskLists = fetchTaskLists()
        for (list in taskLists) {
            val listId = list.id ?: continue
            pushTasks(listId)
            pullTasks(listId)
        }
    }

    private suspend fun fetchTaskLists(): List<com.example.jalicalendar.data.remote.TaskListDto> {
        val out = mutableListOf<com.example.jalicalendar.data.remote.TaskListDto>()
        var page: String? = null
        do {
            val resp = tasksApi.listTaskLists(pageToken = page)
            out += resp.items.orEmpty()
            page = resp.nextPageToken
        } while (page != null)
        return out
    }

    private suspend fun pushTasks(listId: String) {
        for (task in taskRepo.pendingChanges().filter { it.listId == listId }) {
            try {
                if (task.deleted) {
                    if (task.remoteId != null) tasksApi.deleteTask(listId, task.remoteId!!)
                    taskRepo.purgeSyncedDeleted(task.id)
                } else {
                    val saved = if (task.remoteId == null) {
                        tasksApi.insertTask(listId, task.toDto())
                    } else {
                        tasksApi.updateTask(listId, task.remoteId!!, task.toDto())
                    }
                    taskRepo.markSynced(
                        id = task.id,
                        remoteId = saved.id,
                        etag = saved.etag,
                        remoteUpdatedAtMillis = saved.updated?.let(Rfc5545::parseDateTime),
                    )
                }
            } catch (e: HttpException) {
                if (e.code() == 404 && task.deleted) taskRepo.purgeSyncedDeleted(task.id) else throw e
            }
        }
    }

    private suspend fun pullTasks(listId: String) {
        val out = mutableListOf<TaskDto>()
        var page: String? = null
        do {
            val resp = tasksApi.listTasks(listId, pageToken = page)
            out += resp.items.orEmpty()
            page = resp.nextPageToken
        } while (page != null)
        for (dto in out) {
            val remoteId = dto.id ?: continue
            val incoming = dto.toDomain(listId = listId, localId = null)
            val local = taskRepo.findByRemoteId(remoteId)
            when {
                local == null -> taskRepo.upsertSynced(incoming)
                local.dirty -> { /* keep local; pushed next pass */ }
                else -> taskRepo.upsertSynced(incoming.copy(id = local.id))
            }
        }
    }
}

sealed interface SyncResult {
    data object Success : SyncResult
    data class Skipped(val reason: String) : SyncResult
    data class Failed(val message: String) : SyncResult
}
