package com.example.jalicalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.jalicalendar.ui.format.Formatting
import java.time.LocalDate

/**
 * Fixed 6×7 Jalali month grid. Cells are ≥48dp tall (touch target),
 * day numbers follow the Persian-digits setting, and up to three event
 * bars are drawn per day with the calendar colors.
 *
 * @param monthAnchor any date inside the displayed month
 * @param eventsByDay day → (calendarId to color) bars for that day
 */
@Composable
fun MonthGrid(
    monthAnchor: LocalDate,
    settings: AppSettings,
    weekStart: WeekStart,
    selectedDate: LocalDate,
    todayDate: LocalDate,
    eventsByDay: Map<LocalDate, List<Int>>, // day → calendar colors (one entry per occurrence)
    onDateSelected: (LocalDate) -> Unit,
    persianLanguage: Boolean,
    modifier: Modifier = Modifier,
) {
    val first = monthAnchor.withDayOfMonth(1)
    val jalaliMonth = JalaliConverter.toJalali(first).month
    val offset = Formatting.weekIndexOf(first, weekStart)
    val gridStart = first.minusDays(offset.toLong())

    Column(modifier = modifier.fillMaxWidth()) {
        // Weekday header
        Row(Modifier.fillMaxWidth()) {
            Formatting.weekdayHeaders(weekStart, persianLanguage).forEach { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                )
            }
        }

        // 6 weeks × 7 days
        repeat(6) { week ->
            Row(Modifier.fillMaxWidth()) {
                repeat(7) { day ->
                    val date = gridStart.plusDays((week * 7 + day).toLong())
                    DayCell(
                        date = date,
                        jalaliMonth = jalaliMonth,
                        settings = settings,
                        selected = date == selectedDate,
                        today = date == todayDate,
                        eventColors = eventsByDay[date] ?: emptyList(),
                        onClick = { onDateSelected(date) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    jalaliMonth: Int,
    settings: AppSettings,
    selected: Boolean,
    today: Boolean,
    eventColors: List<Int>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val jalali = JalaliConverter.toJalali(date)
    val inMonth = jalali.month == jalaliMonth
    val dayNum = Formatting.dayNumber(jalali.day, settings)
    val desc = Formatting.fullDate(jalali, settings, persianWeekday = true) +
        if (eventColors.isEmpty()) "" else ", ${eventColors.size}"

    Box(
        modifier = modifier
            .height(56.dp)
            .padding(1.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    selected -> MaterialTheme.colorScheme.primaryContainer
                    else -> Color.Transparent
                },
            )
            .semantics { contentDescription = desc }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            today && !selected -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = dayNum,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (today) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        today && !selected -> MaterialTheme.colorScheme.onPrimary
                        selected -> MaterialTheme.colorScheme.onPrimaryContainer
                        !inMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                )
            }
            // Event bars (max 3)
            Row(modifier = Modifier.padding(top = 3.dp)) {
                if (eventColors.isEmpty()) {
                    Box(
                        Modifier
                            .height(4.dp)
                            .width(4.dp),
                    )
                } else {
                    eventColors.take(3).forEach { color ->
                        Box(
                            Modifier
                                .padding(horizontal = 1.dp)
                                .height(4.dp)
                                .width(7.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(color)),
                        )
                    }
                }
            }
        }
    }
}
