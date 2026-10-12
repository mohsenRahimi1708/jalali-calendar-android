package com.example.jalicalendar.ui.screens.eventedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.jalicalendar.core.time.TimeUtils
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.ui.LocalStrings
import com.example.jalicalendar.ui.components.JalaliDatePickerDialog
import com.example.jalicalendar.ui.format.Formatting
import com.example.jalicalendar.ui.navigation.Routes
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** Create or edit an event. `eventId == null` → create mode. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditScreen(navController: NavHostController, eventId: String?) {
    val viewModel: EventEditViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val calendars by viewModel.calendars.collectAsStateWithLifecycle()
    val strings = LocalStrings.current

    val settings by viewModel.settings.collectAsStateWithLifecycle()

    // Post result toast to the main screen, then pop
    LaunchedEffect(state.finished) {
        if (state.finished) {
            val msg = if (state.finishedDeleted) strings.eventDeleted else strings.eventSaved
            navController.previousBackStackEntry?.savedStateHandle?.set(Routes.RESULT_TOAST, msg)
            navController.popBackStack()
        }
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    if (state.loading) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(Modifier.padding(top = 48.dp))
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) strings.newEvent else strings.editEvent) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.cancel)
                    }
                },
                actions = {
                    if (!state.isNew) {
                        IconButton(onClick = { viewModel.delete() }) {
                            Icon(Icons.Filled.Delete, contentDescription = strings.delete)
                        }
                    }
                    IconButton(onClick = { viewModel.save() }) {
                        Icon(Icons.Filled.Done, contentDescription = strings.save)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::setTitle,
                label = { Text(strings.title) },
                isError = state.error == "title",
                supportingText = if (state.error == "title") {
                    { Text(strings.titleRequired) }
                } else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Calendar picker (stable DropdownMenu anchored to a button)
            LabeledValueRow(
                label = strings.calendar,
                content = {
                    DropdownMenuRow(
                        value = calendars.firstOrNull { it.id == state.calendarId }?.title
                            ?: strings.localCalendar,
                        options = calendars.map { it.id to it.title },
                        onSelect = viewModel::setCalendar,
                    )
                },
            )

            if (state.error == "calendar") {
                Text(strings.noCalendar, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            // All-day switch
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(strings.allDay, style = MaterialTheme.typography.bodyLarge)
                Switch(checked = state.allDay, onCheckedChange = viewModel::setAllDay)
            }

            // Dates
            PickerRow(
                label = strings.startDate,
                value = formatDate(state.startMillis, settings, state.allDay),
                onClick = { showStartDatePicker = true },
            )
            PickerRow(
                label = strings.endDate,
                value = formatDate(state.endMillis - if (state.allDay) 1 else 0, settings, state.allDay),
                onClick = { showEndDatePicker = true },
            )
            if (state.error == "end") {
                Text(strings.endAfterStart, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            // Times (timed events only)
            if (!state.allDay) {
                PickerRow(
                    label = strings.startTime,
                    value = Formatting.time(state.startMillis, settings),
                    onClick = { showStartTimePicker = true },
                )
                PickerRow(
                    label = strings.endTime,
                    value = Formatting.time(state.endMillis, settings),
                    onClick = { showEndTimePicker = true },
                )
            }

            HorizontalDivider()

            // Repeat
            LabeledValueRow(
                label = strings.repeat,
                content = {
                    DropdownMenuRow(
                        value = repeatLabel(state.rrule, strings),
                        options = listOf(
                            "" to strings.repeatNone,
                            "FREQ=DAILY" to strings.repeatDaily,
                            "FREQ=WEEKLY" to strings.repeatWeekly,
                            "FREQ=MONTHLY" to strings.repeatMonthly,
                            "FREQ=YEARLY" to strings.repeatYearly,
                        ),
                        onSelect = { key -> viewModel.setRrule(key.ifBlank { null }) },
                    )
                },
            )

            // Reminder
            LabeledValueRow(
                label = strings.reminder,
                content = {
                    DropdownMenuRow(
                        value = reminderLabel(state.reminderMinutes, strings),
                        options = listOf(
                            "" to strings.reminderNone,
                            "0" to strings.reminderAtStart,
                            "5" to reminderText(5, strings),
                            "10" to reminderText(10, strings),
                            "15" to reminderText(15, strings),
                            "30" to reminderText(30, strings),
                            "60" to reminderText(60, strings),
                            "1440" to strings.reminderDayBefore,
                        ),
                        onSelect = { key ->
                            viewModel.setReminder(key.ifBlank { null }?.toIntOrNull())
                        },
                    )
                },
            )

            OutlinedTextField(
                value = state.location,
                onValueChange = viewModel::setLocation,
                label = { Text(strings.location) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::setDescription,
                label = { Text(strings.description) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    // Dialogs
    if (showStartDatePicker) {
        JalaliDatePickerDialog(
            initialDate = civilDate(state.startMillis, settings),
            settings = settings,
            onDismiss = { showStartDatePicker = false },
            onConfirm = { date -> viewModel.setStartDate(date); showStartDatePicker = false },
        )
    }
    if (showEndDatePicker) {
        JalaliDatePickerDialog(
            initialDate = civilDate(state.endMillis - if (state.allDay) 1 else 0, settings),
            settings = settings,
            onDismiss = { showEndDatePicker = false },
            onConfirm = { date -> viewModel.setEndDate(date); showEndDatePicker = false },
        )
    }
    if (showStartTimePicker) {
        val time = Instant.ofEpochMilli(state.startMillis).atZone(ZoneId.of(settings.timeZone)).toLocalTime()
        TimePickerDialogWrapper(
            initial = time,
            is24h = settings.use24HourFormat,
            onDismiss = { showStartTimePicker = false },
            onConfirm = { h, m -> viewModel.setStartTime(h, m); showStartTimePicker = false },
        )
    }
    if (showEndTimePicker) {
        val time = Instant.ofEpochMilli(state.endMillis).atZone(ZoneId.of(settings.timeZone)).toLocalTime()
        TimePickerDialogWrapper(
            initial = time,
            is24h = settings.use24HourFormat,
            onDismiss = { showEndTimePicker = false },
            onConfirm = { h, m -> viewModel.setEndTime(h, m); showEndTimePicker = false },
        )
    }
}

// --------------------------------------------------------------------- pieces

@Composable
private fun PickerRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun LabeledValueRow(label: String, content: @Composable () -> Unit) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        content()
    }
}

@Composable
private fun DropdownMenuRow(
    value: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row {
        TextButton(onClick = { expanded = true }) {
            Text(value, color = MaterialTheme.colorScheme.primary)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (key, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = { onSelect(key); expanded = false },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialogWrapper(
    initial: LocalTime,
    is24h: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val state = rememberTimePickerState(initialHour = initial.hour, initialMinute = initial.minute, is24Hour = is24h)
    // material3 1.3.x ships no TimePickerDialog; AlertDialog hosts the picker.
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                Text(LocalStrings.current.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(LocalStrings.current.cancel) }
        },
        text = { TimePicker(state = state) },
    )
}

private fun civilDate(epochMillis: Long, settings: AppSettings): LocalDate =
    TimeUtils.localDateOf(epochMillis, ZoneId.of(settings.timeZone))

private fun formatDate(epochMillis: Long, settings: AppSettings, allDay: Boolean): String {
    val j = Formatting.jalaliDate(epochMillis, settings)
    return Formatting.fullDate(j, settings, persianWeekday = false)
}

private fun repeatLabel(rrule: String?, strings: com.example.jalicalendar.ui.Strings): String = when (rrule) {
    null, "" -> strings.repeatNone
    "FREQ=DAILY" -> strings.repeatDaily
    "FREQ=WEEKLY" -> strings.repeatWeekly
    "FREQ=MONTHLY" -> strings.repeatMonthly
    "FREQ=YEARLY" -> strings.repeatYearly
    else -> rrule
}

private fun reminderText(minutes: Int, strings: com.example.jalicalendar.ui.Strings): String =
    if (minutes >= 1440) strings.reminderDayBefore
    else String.format(java.util.Locale.US, strings.reminderMinutesBefore, minutes)

private fun reminderLabel(minutes: Int?, strings: com.example.jalicalendar.ui.Strings): String =
    when (minutes) {
        null -> strings.reminderNone
        0 -> strings.reminderAtStart
        1440 -> strings.reminderDayBefore
        else -> String.format(java.util.Locale.US, strings.reminderMinutesBefore, minutes)
    }
