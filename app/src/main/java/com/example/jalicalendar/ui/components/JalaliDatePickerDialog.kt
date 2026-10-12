package com.example.jalicalendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jalicalendar.core.time.JalaliConverter
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.ui.LocalPersianLanguage
import com.example.jalicalendar.ui.LocalStrings
import com.example.jalicalendar.ui.format.Formatting
import java.time.LocalDate
import java.time.ZoneId

/**
 * Jalali date picker: year ±1, month navigation and the shared month grid.
 * Returns a Gregorian [LocalDate] (the app's civil-date axis).
 */
@Composable
fun JalaliDatePickerDialog(
    initialDate: LocalDate,
    settings: AppSettings,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    val strings = LocalStrings.current
    var anchor by remember { mutableStateOf(initialDate.withDayOfMonth(1)) }
    var selected by remember { mutableStateOf(initialDate) }
    val jAnchor = JalaliConverter.toJalali(anchor)
    val today = LocalDate.now(ZoneId.of(settings.timeZone))

    fun goto(jy: Int, jm: Int) {
        val clampedYear = jy.coerceIn(JalaliConverter.MIN_YEAR, JalaliConverter.MAX_YEAR)
        val clampedMonth = jm.coerceIn(1, 12)
        anchor = JalaliConverter.toGregorian(clampedYear, clampedMonth, 1)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(Formatting.monthYear(jAnchor.year, jAnchor.month, settings))
        },
        text = {
            androidx.compose.foundation.layout.Column(Modifier.fillMaxWidth()) {
                // Year controls
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { goto(jAnchor.year - 1, jAnchor.month) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                    }
                    Text(
                        text = com.example.jalicalendar.core.time.PersianDigits.localizeNumber(
                            jAnchor.year, settings.persianDigits,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = { goto(jAnchor.year + 1, jAnchor.month) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }
                // Month controls
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            if (jAnchor.month == 1) goto(jAnchor.year - 1, 12)
                            else goto(jAnchor.year, jAnchor.month - 1)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                    }
                    Text(
                        text = com.example.jalicalendar.core.time.JalaliDate.monthName(
                            jAnchor.month, settings.persianDigits,
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    IconButton(
                        onClick = {
                            if (jAnchor.month == 12) goto(jAnchor.year + 1, 1)
                            else goto(jAnchor.year, jAnchor.month + 1)
                        },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }

                MonthGrid(
                    monthAnchor = anchor,
                    settings = settings,
                    weekStart = settings.weekStart,
                    selectedDate = selected,
                    todayDate = today,
                    eventsByDay = emptyMap(),
                    onDateSelected = { selected = it },
                    persianLanguage = LocalPersianLanguage.current,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(strings.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.cancel) }
        },
    )
}
