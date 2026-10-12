package com.example.jalicalendar.data.repo

import com.example.jalicalendar.data.local.dao.EventDao
import com.example.jalicalendar.data.local.dao.ReminderDao
import com.example.jalicalendar.data.local.toDomain
import com.example.jalicalendar.data.local.toEntity
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Reminder
import com.example.jalicalendar.domain.repo.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local-first event store. All writes go through here so sync bookkeeping
 * (dirty flags, localUpdatedAt) stays consistent; remote I/O lives in the sync layer.
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val reminderDao: ReminderDao,
) : EventRepository {

    override fun observeBetween(startUtcMillis: Long, endUtcMillis: Long): Flow<List<Event>> =
        eventDao.observeBetween(startUtcMillis, endUtcMillis).map { rows -> rows.map { it.toDomain() } }

    override fun observeById(id: String): Flow<Event?> =
        eventDao.observeById(id).map { it?.toDomain() }

    override suspend fun getEvent(id: String): Event? = eventDao.getById(id)?.toDomain()

    override suspend fun search(query: String): List<Event> =
        eventDao.search(query.trim()).map { it.toDomain() }

    override suspend fun upsert(event: Event): String {
        val id = event.id.ifBlank { UUID.randomUUID().toString() }
        // A local edit re-opens the push queue; merging happens in the sync step.
        eventDao.upsert(
            event.copy(
                id = id,
                localUpdatedAtMillis = System.currentTimeMillis(),
                dirty = true,
                deleted = false,
            ).toEntity(),
        )
        return id
    }

    override suspend fun delete(event: Event) {
        if (event.remoteId == null) {
            reminderDao.deleteForEvent(event.id)
            eventDao.hardDelete(event.id)
        } else {
            eventDao.upsert(
                event.copy(
                    deleted = true,
                    dirty = true,
                    localUpdatedAtMillis = System.currentTimeMillis(),
                ).toEntity(),
            )
        }
    }

    override suspend fun upcomingWithReminders(nowMillis: Long): List<com.example.jalicalendar.domain.model.ScheduledReminder> =
        eventDao.upcomingWithReminders(nowMillis).flatMap { entity ->
            val event = entity.toDomain()
            reminderDao.forEvent(entity.id)
                .filter { it.enabled }
                .map { com.example.jalicalendar.domain.model.ScheduledReminder(event, it.toDomain()) }
        }

    override suspend fun remindersFor(eventId: String): List<Reminder> =
        reminderDao.forEvent(eventId).map { it.toDomain() }

    override suspend fun observeReminders(eventId: String): Flow<List<Reminder>> =
        reminderDao.observeForEvent(eventId).map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsertReminders(reminders: List<Reminder>) =
        reminders.forEach { reminderDao.upsert(it.toEntity()) }

    override suspend fun deleteReminder(id: String) = reminderDao.delete(id)

    override suspend fun findLocalByRemoteId(remoteId: String): Event? =
        eventDao.findByRemoteId(remoteId)?.toDomain()

    override suspend fun pendingChanges(): List<Event> =
        eventDao.pendingChanges().map { it.toDomain() }

    override suspend fun markSynced(
        id: String,
        remoteId: String?,
        etag: String?,
        remoteUpdatedAtMillis: Long?,
        calendarId: String?,
    ) = eventDao.markSynced(id, remoteId, etag, remoteUpdatedAtMillis, calendarId)

    override suspend fun purgeSyncedDeleted(id: String) = eventDao.purgeSyncedDeleted(id)

    override suspend fun upsertSynced(event: Event) {
        // Pull path: keep server truth, do not mark the row dirty.
        eventDao.upsert(event.toEntity())
    }
}
