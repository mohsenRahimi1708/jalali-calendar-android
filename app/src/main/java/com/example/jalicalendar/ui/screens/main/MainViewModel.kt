package com.example.jalicalendar.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jalicalendar.core.time.OccurrenceExpander
import com.example.jalicalendar.core.time.TimeUtils
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/** One expanded occurrence of an event in the visible window. */
data class Occurrence(
    val event: Event,
    val startUtcMillis: Long,
    val endUtcMillis: Long,
)

enum class ViewMode { MONTH, WEEK, DAY }

/**
 * Backs the main calendar screen: selected civil date, view mode, visible
 * range, expanded occurrences (recurring included) and calendar visibility.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepo: SettingsRepository,
    private val eventRepo: EventRepository,
    private val calendarRepo: CalendarRepository,
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings.DEFAULT)

    private val _selectedDate = MutableStateFlow<LocalDate?>(null) // null = today
    private val _viewMode = MutableStateFlow(ViewMode.MONTH)

    val viewMode: StateFlow<ViewMode> = _viewMode

    /** Selected civil date resolved against the configured display zone. */
    val selectedDate: StateFlow<LocalDate> = combine(settings, _selectedDate) { s, explicit ->
        explicit ?: LocalDate.now(ZoneId.of(s.timeZone))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LocalDate.now())

    val calendars: StateFlow<List<Calendar>> = calendarRepo.observeCalendars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private data class Range(val start: Long, val end: Long) // [start, end)

    /** Visible window depends on view mode + selection + week start. */
    private val range: StateFlow<Range> = combine(settings, selectedDate, _viewMode) { s, date, mode ->
        val zone = ZoneId.of(s.timeZone)
        when (mode) {
            ViewMode.MONTH -> {
                val first = date.withDayOfMonth(1)
                val gridStart = first.minusDays(
                    // align to week start
                    ((first.dayOfWeek.value - weekStartOffset(s)) % 7 + 7) % 7L,
                )
                Range(
                    TimeUtils.localStartOfDay(gridStart, zone),
                    TimeUtils.localStartOfDay(gridStart.plusDays(42), zone),
                )
            }

            ViewMode.WEEK -> {
                val offset = ((date.dayOfWeek.value - weekStartOffset(s)) % 7 + 7) % 7L
                val start = date.minusDays(offset)
                Range(
                    TimeUtils.localStartOfDay(start, zone),
                    TimeUtils.localStartOfDay(start.plusDays(7), zone),
                )
            }

            ViewMode.DAY -> Range(
                TimeUtils.localStartOfDay(date, zone),
                TimeUtils.localStartOfDay(date.plusDays(1), zone),
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Range(0, 0))

    /** ISO day-of-week offset: week start per settings (SAT=6, SUN=7, MON=1). */
    private fun weekStartOffset(s: AppSettings) = when (s.weekStart) {
        com.example.jalicalendar.domain.model.WeekStart.SATURDAY -> 6
        com.example.jalicalendar.domain.model.WeekStart.SUNDAY -> 7
        com.example.jalicalendar.domain.model.WeekStart.MONDAY -> 1
    }

    /** Expanded occurrences (RRULE applied) inside the visible window. */
    val occurrences: StateFlow<List<Occurrence>> = combine(range, settings) { r, s -> r to s }
        .flatMapLatest { (r, s) ->
            if (r.end <= r.start) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                eventRepo.observeBetween(r.start, r.end).map { events ->
                    val zone = ZoneId.of(s.timeZone)
                    events.flatMap { event ->
                        val duration = OccurrenceExpander.durationMillis(event)
                        OccurrenceExpander.occurrences(event, r.start, r.end, zone).map { start ->
                            Occurrence(event, start, start + duration)
                        }
                    }.sortedBy { it.startUtcMillis }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** calendarId → color for chips/bars. */
    val calendarColors: StateFlow<Map<String, Int>> = calendars
        .map { list -> list.associate { it.id to it.colorArgb } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    val visibleCalendarIds: StateFlow<Set<String>> = calendars
        .map { list -> list.filter { it.selected && !it.hidden }.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val visibleOccurrences: StateFlow<List<Occurrence>> =
        combine(occurrences, visibleCalendarIds) { occs, ids -> occs.filter { it.event.calendarId in ids } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ---------------------------------------------------------------- actions

    fun setViewMode(mode: ViewMode) = _viewMode.update { mode }

    fun selectDate(date: LocalDate) = _selectedDate.update { date }

    fun goToToday() {
        _selectedDate.update { null }
    }


    fun nextPeriod() = shiftPeriod(+1)

    fun previousPeriod() = shiftPeriod(-1)

    /** Month view moves by calendar month (java.time clamps Jan 31 → Feb 28). */
    private fun shiftPeriod(direction: Int) {
        val base = selectedDate.value
        _selectedDate.update {
            when (_viewMode.value) {
                ViewMode.MONTH -> base.plusMonths(direction.toLong())
                ViewMode.WEEK -> base.plusDays(7L * direction)
                ViewMode.DAY -> base.plusDays(direction.toLong())
            }
        }
    }

    fun toggleCalendarVisible(calendar: Calendar) {
        viewModelScope.launch { calendarRepo.setSelected(calendar.id, !calendar.selected) }
    }

    fun occurrencesOn(date: LocalDate): List<Occurrence> {
        val zone = ZoneId.of(settings.value.timeZone)
        val start = TimeUtils.localStartOfDay(date, zone)
        val end = TimeUtils.localStartOfDay(date.plusDays(1), zone)
        return visibleOccurrences.value.filter { it.startUtcMillis in start until end }
    }
}
