package com.example.jalicalendar.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jalicalendar.core.time.JalaliConverter
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.WeekStart
import com.example.jalicalendar.ui.LocalPersianLanguage
import com.example.jalicalendar.ui.LocalStrings
import com.example.jalicalendar.ui.components.EventChip
import com.example.jalicalendar.ui.format.Formatting
import java.time.LocalDate

/**
 * Week view: horizontally scrollable 7-day column strip with that day's
 * events listed under a day header; whole week in one LazyColumn grouped
 * by day so TalkBack reads chronologically.
 */
@Composable
fun WeekView(
    weekStartDate: LocalDate,
    settings: AppSettings,
    occurrences: List<Occurrence>,
    today: LocalDate,
    selected: LocalDate,
    calendarColors: Map<String, Int>,
    onDateSelected: (LocalDate) -> Unit,
    onEventClick: (Occurrence) -> Unit,
) {
    val strings = LocalStrings.current
    val days = (0 until 7).map { weekStartDate.plusDays(it.toLong()) }

    Column(Modifier.fillMaxSize()) {
        // Day header strip
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
        ) {
            days.forEach { date ->
                DayHeader(
                    date = date,
                    settings = settings,
                    isToday = date == today,
                    isSelected = date == selected,
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        LazyColumn(Modifier.fillMaxSize()) {
            days.forEach { date ->
                item(key = "h_$date") {
                    Text(
                        text = Formatting.dayLabel(date, today, strings, settings),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                val dayOccs = occurrences.filter {
                    Formatting.localDate(it.startUtcMillis, settings) == date
                }
                if (dayOccs.isEmpty()) {
                    item(key = "e_$date") {
                        Text(
                            text = strings.noEvents,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                    }
                } else {
                    items(dayOccs, key = { "${it.event.id}_${it.startUtcMillis}" }) { occ ->
                        OccurrenceRow(occ, settings, calendarColors, onEventClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun DayHeader(
    date: LocalDate,
    settings: AppSettings,
    isToday: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val j = JalaliConverter.toJalali(date)
    Column(
        modifier = modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            )
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp)
            .semantics {
                contentDescription = Formatting.fullDate(j, settings, persianWeekday = true)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val headers = Formatting.weekdayHeaders(WeekStart.SATURDAY, persian = LocalPersianLanguage.current)
        Text(
            text = headers[((j.dayOfWeek().value + 1) % 7)],
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .width(28.dp)
                .clip(CircleShape)
                .background(if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = Formatting.dayNumber(j.day, settings),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/**
 * Day view: all-day events first, then timed events with their start time.
 */
@Composable
fun DayView(
    date: LocalDate,
    settings: AppSettings,
    occurrences: List<Occurrence>,
    calendarColors: Map<String, Int>,
    onEventClick: (Occurrence) -> Unit,
) {
    val strings = LocalStrings.current
    val dayOccs = occurrences.filter { Formatting.localDate(it.startUtcMillis, settings) == date }
    val allDay = dayOccs.filter { it.event.allDay }
    val timed = dayOccs.filterNot { it.event.allDay }

    if (dayOccs.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(strings.noEvents, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        if (allDay.isNotEmpty()) {
            item {
                Text(
                    text = strings.allDay,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            items(allDay, key = { "${it.event.id}_${it.startUtcMillis}_ad" }) { occ ->
                OccurrenceRow(occ, settings, calendarColors, onEventClick)
            }
        }
        timed.forEach { occ ->
            item(key = "${occ.event.id}_${occ.startUtcMillis}") {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = Formatting.time(occ.startUtcMillis, settings),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .width(64.dp)
                            .padding(top = 10.dp),
                    )
                    OccurrenceRow(
                        occ, settings, calendarColors, onEventClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

/** Time + title row used by week/day/agenda lists. */
@Composable
fun OccurrenceRow(
    occurrence: Occurrence,
    settings: AppSettings,
    calendarColors: Map<String, Int>,
    onEventClick: (Occurrence) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val timeLabel = if (occurrence.event.allDay) {
        strings.allDay
    } else {
        Formatting.time(occurrence.startUtcMillis, settings)
    }
    val fullDate = Formatting.fullDate(Formatting.jalaliDate(occurrence.startUtcMillis, settings), settings, true)
    EventChip(
        timeLabel = timeLabel,
        title = occurrence.event.title,
        calendarColor = calendarColors[occurrence.event.calendarId] ?: 0xFF006750.toInt(),
        contentDescription = "$fullDate، $timeLabel، ${occurrence.event.title}",
        onClick = { onEventClick(occurrence) },
        modifier = modifier.padding(vertical = 2.dp),
    )
}
