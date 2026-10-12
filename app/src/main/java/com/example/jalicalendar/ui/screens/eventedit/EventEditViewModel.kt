package com.example.jalicalendar.ui.screens.eventedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jalicalendar.core.time.TimeUtils
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Reminder
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

/** Everything the editor screen needs; one flat state object keeps save logic simple. */
data class EventEditState(
    val loading: Boolean = true,
    val isNew: Boolean = true,
    val eventId: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val calendarId: String = "",
    val allDay: Boolean = false,
    val startMillis: Long = 0L,
    val endMillis: Long = 0L,
    val rrule: String? = null,
    val reminderMinutes: Int? = null,
    val originalRemoteId: String? = null,
    val originalEtag: String? = null,
    val originalRemoteUpdated: Long? = null,
    /** Validation error key: "title" | "end" — null when valid. */
    val error: String? = null,
    val finished: Boolean = false,
    val finishedDeleted: Boolean = false,
)

@HiltViewModel
class EventEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepo: EventRepository,
    private val calendarRepo: CalendarRepository,
    settingsRepo: SettingsRepository,
    private val reminderScheduler: com.example.jalicalendar.reminders.ReminderScheduler,
) : ViewModel() {

    private val navEventId: String? = savedStateHandle["eventId"]
    private val presetStart: Long = savedStateHandle.get<Long>("startMillis") ?: -1L
    private val presetAllDayDate: String? = savedStateHandle["allDayDate"]

    val settings: StateFlow<AppSettings> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings.DEFAULT)

    val calendars: StateFlow<List<Calendar>> = calendarRepo.observeCalendars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _state = MutableStateFlow(EventEditState())
    val state: StateFlow<EventEditState> = _state

    private fun zone(): String = settings.value.timeZone

    init {
        viewModelScope.launch {
            if (navEventId != null) {
                val existing = eventRepo.getEvent(navEventId)
                if (existing == null) {
                    _state.update { it.copy(loading = false, finished = true) }
                } else {
                    val reminders = eventRepo.remindersFor(navEventId)
                    _state.update {
                        it.copy(
                            loading = false,
                            isNew = false,
                            eventId = existing.id,
                            title = existing.title,
                            description = existing.description.orEmpty(),
                            location = existing.location.orEmpty(),
                            calendarId = existing.calendarId,
                            allDay = existing.allDay,
                            startMillis = existing.startUtcMillis,
                            endMillis = existing.endUtcMillis,
                            rrule = existing.rrule,
                            reminderMinutes = reminders.minOfOrNull { r -> r.minutesBefore },
                            originalRemoteId = existing.remoteId,
                            originalEtag = existing.etag,
                            originalRemoteUpdated = existing.remoteUpdatedAtMillis,
                        )
                    }
                }
            } else {
                val zoneId = ZoneId.of(zone())
                val start = when {
                    presetAllDayDate != null -> TimeUtils.utcStartOfDay(LocalDate.parse(presetAllDayDate))
                    presetStart > 0L -> presetStart
                    else -> {
                        val todayStart = TimeUtils.startOfLocalDay(System.currentTimeMillis(), zoneId)
                        todayStart + 9 * 3_600_000L // default 09:00 local
                    }
                }
                val isAllDay = presetAllDayDate != null
                // Fresh installs have no calendars at all (the Local calendar is not
                // guaranteed to exist yet), so seed one before deriving the id —
                // otherwise calendarId stays blank and save() fails downstream.
                val localId = calendarRepo.seedLocalCalendar()
                val storedDefault = settings.value.defaultCalendarId
                val chosen = storedDefault
                    ?.takeIf { id -> calendarRepo.observeCalendars().first().any { it.id == id } }
                _state.update {
                    it.copy(
                        loading = false,
                        isNew = true,
                        startMillis = start,
                        endMillis = if (isAllDay) start + 86_400_000L else start + 3_600_000L,
                        allDay = isAllDay,
                        calendarId = chosen ?: localId,
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------------ edits

    fun setTitle(v: String) = _state.update { it.copy(title = v, error = null) }
    fun setDescription(v: String) = _state.update { it.copy(description = v) }
    fun setLocation(v: String) = _state.update { it.copy(location = v) }
    fun setCalendar(v: String) = _state.update { it.copy(calendarId = v) }
    fun setRrule(v: String?) = _state.update { it.copy(rrule = v) }
    fun setReminder(v: Int?) = _state.update { it.copy(reminderMinutes = v) }

    fun setAllDay(v: Boolean) = _state.update {
        if (v && it.endMillis - it.startMillis < 86_400_000L) {
            it.copy(allDay = true, endMillis = it.startMillis + 86_400_000L)
        } else {
            it.copy(allDay = v)
        }
    }

    fun setStartDate(date: LocalDate) = _state.update { s ->
        val zoneId = ZoneId.of(zone())
        if (s.allDay) {
            val newStart = TimeUtils.utcStartOfDay(date)
            val length = (s.endMillis - s.startMillis).coerceAtLeast(86_400_000L)
            s.copy(startMillis = newStart, endMillis = newStart + length)
        } else {
            val time = TimeUtils.localTimeOf(s.startMillis, zoneId)
            val newStart = TimeUtils.toUtcMillis(date, time, zoneId)
            val duration = (s.endMillis - s.startMillis).coerceAtLeast(3_600_000L)
            s.copy(startMillis = newStart, endMillis = newStart + duration)
        }
    }

    fun setEndDate(date: LocalDate) = _state.update { s ->
        val zoneId = ZoneId.of(zone())
        if (s.allDay) {
            s.copy(endMillis = TimeUtils.utcEndOfDay(date))
        } else {
            val time = TimeUtils.localTimeOf(s.endMillis, zoneId)
            s.copy(endMillis = TimeUtils.toUtcMillis(date, time, zoneId))
        }
    }

    /** Start wall-clock time change; keeps duration when possible. */
    fun setStartTime(hour: Int, minute: Int) = _state.update { s ->
        val zoneId = ZoneId.of(zone())
        val date = TimeUtils.localDateOf(s.startMillis, zoneId)
        val newStart = TimeUtils.toUtcMillis(date, LocalTime.of(hour, minute), zoneId)
        val duration = (s.endMillis - s.startMillis).coerceAtLeast(3_600_000L)
        s.copy(startMillis = newStart, endMillis = newStart + duration)
    }

    /** End wall-clock time change (same civil day as the end date). */
    fun setEndTime(hour: Int, minute: Int) = _state.update { s ->
        val zoneId = ZoneId.of(zone())
        val date = TimeUtils.localDateOf(s.endMillis, zoneId)
        s.copy(endMillis = TimeUtils.toUtcMillis(date, LocalTime.of(hour, minute), zoneId))
    }

    // ------------------------------------------------------------------- save

    fun save() {
        val s = _state.value
        when {
            s.title.isBlank() -> { _state.update { it.copy(error = "title") }; return }
            s.endMillis <= s.startMillis -> { _state.update { it.copy(error = "end") }; return }
        }
        viewModelScope.launch {
            // Resolve a valid target calendar: the chosen id (if it still exists) →
            // any calendar-type row → seed the Local calendar. A calendar problem
            // must never be reported as "Title is required".
            val calendarId = s.calendarId.takeIf { id -> calendars.value.any { it.id == id } }
                ?: calendars.value.firstOrNull { c -> c.type == Calendar.TYPE_CALENDAR }?.id
                ?: runCatching { calendarRepo.seedLocalCalendar() }.getOrNull()
            if (calendarId.isNullOrBlank()) {
                _state.update { it.copy(error = "calendar") }
                return@launch
            }
            val endMillis = if (s.allDay && s.endMillis <= s.startMillis) {
                s.startMillis + 86_400_000L
            } else {
                s.endMillis
            }
            val event = Event(
                id = s.eventId,
                calendarId = calendarId,
                remoteId = s.originalRemoteId,
                title = s.title.trim(),
                description = s.description.trim().ifBlank { null },
                location = s.location.trim().ifBlank { null },
                startUtcMillis = s.startMillis,
                endUtcMillis = endMillis,
                allDay = s.allDay,
                timeZone = zone(),
                rrule = s.rrule,
                etag = s.originalEtag,
                remoteUpdatedAtMillis = s.originalRemoteUpdated,
            )
            eventRepo.upsert(event)

            // The editor manages a single reminder: cancel old alarms first
            // (the ids are needed for the request code), then replace rows.
            eventRepo.remindersFor(event.id).forEach {
                reminderScheduler.cancel(event.id, it.id)
                eventRepo.deleteReminder(it.id)
            }
            val newReminders = s.reminderMinutes?.let { minutes ->
                val r = Reminder(
                    id = UUID.randomUUID().toString(),
                    eventId = event.id,
                    minutesBefore = minutes,
                )
                eventRepo.upsertReminders(listOf(r))
                listOf(r)
            }.orEmpty()
            // Schedule (or clear, when no reminder) the alarm for this event.
            reminderScheduler.scheduleForEvent(event, newReminders)
            _state.update { it.copy(finished = true) }
        }
    }

    fun delete() {
        val s = _state.value
        if (s.isNew) {
            _state.update { it.copy(finished = true, finishedDeleted = true) }
            return
        }
        viewModelScope.launch {
            val existing = eventRepo.getEvent(s.eventId) ?: run {
                _state.update { it.copy(finished = true, finishedDeleted = true) }
                return@launch
            }
            eventRepo.delete(existing)
            eventRepo.remindersFor(existing.id).forEach {
                reminderScheduler.cancel(existing.id, it.id)
                eventRepo.deleteReminder(it.id)
            }
            _state.update { it.copy(finished = true, finishedDeleted = true) }
        }
    }
}
