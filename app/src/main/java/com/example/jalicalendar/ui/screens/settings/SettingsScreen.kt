package com.example.jalicalendar.ui.screens.settings

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.LanguageMode
import com.example.jalicalendar.domain.model.ThemeMode
import com.example.jalicalendar.domain.model.WeekStart
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: SettingsRepository,
    calendarRepo: CalendarRepository,
) : ViewModel() {
    val settings = repo.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings.DEFAULT)

    val calendars = calendarRepo.observeCalendars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun update(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch { repo.update(transform) }
    }
}

private val TIME_ZONES = listOf(
    "Asia/Tehran", "UTC", "Europe/Istanbul", "Asia/Dubai", "Asia/Karachi",
    "Asia/Kolkata", "Europe/London", "Europe/Berlin", "America/New_York",
    "Asia/Tokyo", "Australia/Sydney",
)

private val REMINDER_CHOICES = listOf(0, 5, 10, 15, 30, 60)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val strings = com.example.jalicalendar.ui.LocalStrings.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.settings) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.cancel)
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            SectionTitle(strings.general)

            val calendarOptions by viewModel.calendars.collectAsStateWithLifecycle()
            val calendarFilter = calendarOptions
                .filter { it.type == com.example.jalicalendar.domain.model.Calendar.TYPE_CALENDAR }
            DropdownSetting(
                label = strings.defaultCalendar,
                value = calendarFilter.firstOrNull { it.id == settings.defaultCalendarId }?.title
                    ?: strings.localCalendar,
                options = calendarFilter.map { it.id to it.title } +
                    ("" to strings.localCalendar),
                onSelect = { key -> viewModel.update { it.copy(defaultCalendarId = key.ifBlank { null }) } },
            )

            DropdownSetting(
                label = strings.defaultReminder,
                value = if (settings.defaultReminderMinutes == 0) strings.reminderAtStart
                else String.format(java.util.Locale.US, strings.reminderMinutesBefore, settings.defaultReminderMinutes),
                options = REMINDER_CHOICES.map { m ->
                    m.toString() to if (m == 0) strings.reminderAtStart
                    else String.format(java.util.Locale.US, strings.reminderMinutesBefore, m)
                },
                onSelect = { key -> key.toIntOrNull()?.let { m -> viewModel.update { it.copy(defaultReminderMinutes = m) } } },
            )

            DropdownSetting(
                label = strings.timeZone,
                value = settings.timeZone,
                options = TIME_ZONES.map { it to it },
                onSelect = { key -> viewModel.update { it.copy(timeZone = key) } },
            )

            DropdownSetting(
                label = strings.weekStart,
                value = when (settings.weekStart) {
                    WeekStart.SATURDAY -> "شنبه / Sat"
                    WeekStart.SUNDAY -> "یکشنبه / Sun"
                    WeekStart.MONDAY -> "دوشنبه / Mon"
                },
                options = listOf(
                    "SATURDAY" to "شنبه / Sat",
                    "SUNDAY" to "یکشنبه / Sun",
                    "MONDAY" to "دوشنبه / Mon",
                ),
                onSelect = { key -> key.let { v -> viewModel.update { s -> s.copy(weekStart = WeekStart.valueOf(v)) } } },
            )

            SwitchSetting(strings.hourFormat24, settings.use24HourFormat) { v ->
                viewModel.update { it.copy(use24HourFormat = v) }
            }
            SwitchSetting(strings.persianDigits, settings.persianDigits) { v ->
                viewModel.update { it.copy(persianDigits = v) }
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            SectionTitle(strings.appearance)

            DropdownSetting(
                label = strings.theme,
                value = when (settings.themeMode) {
                    ThemeMode.SYSTEM -> strings.themeSystem
                    ThemeMode.LIGHT -> strings.themeLight
                    ThemeMode.DARK -> strings.themeDark
                },
                options = listOf(
                    "SYSTEM" to strings.themeSystem,
                    "LIGHT" to strings.themeLight,
                    "DARK" to strings.themeDark,
                ),
                onSelect = { key -> viewModel.update { it.copy(themeMode = ThemeMode.valueOf(key)) } },
            )

            SwitchSetting(strings.dynamicColor, settings.dynamicColor) { v ->
                viewModel.update { it.copy(dynamicColor = v) }
            }

            DropdownSetting(
                label = strings.language,
                value = when (settings.language) {
                    LanguageMode.SYSTEM -> strings.languageSystem
                    LanguageMode.PERSIAN -> strings.languagePersian
                    LanguageMode.ENGLISH -> strings.languageEnglish
                },
                options = listOf(
                    "SYSTEM" to strings.languageSystem,
                    "PERSIAN" to strings.languagePersian,
                    "ENGLISH" to strings.languageEnglish,
                ),
                onSelect = { key -> viewModel.update { it.copy(language = LanguageMode.valueOf(key)) } },
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

@Composable
private fun DropdownSetting(
    label: String,
    value: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(onClick = { expanded = true }) { Text("▾") }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { (key, label2) ->
                    DropdownMenuItem(text = { Text(label2) }, onClick = { onSelect(key); expanded = false })
                }
            }
        }
    }
}

@Composable
private fun SwitchSetting(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
