package com.example.jalicalendar.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jalicalendar.R
import com.example.jalicalendar.core.time.JalaliConverter
import com.example.jalicalendar.core.time.TimeUtils
import com.example.jalicalendar.ui.LocalStrings
import com.example.jalicalendar.ui.components.MonthGrid
import com.example.jalicalendar.ui.format.Formatting
import com.example.jalicalendar.ui.navigation.Routes
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Main calendar host: top bar with month/year + period navigation, view-mode
 * tabs (Month/Week/Day), FAB to create, and a toast channel fed by editors
 * via [Routes.RESULT_TOAST].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCalendarScreen(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    val strings = LocalStrings.current
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
    val occurrences by viewModel.visibleOccurrences.collectAsStateWithLifecycle()
    val calendarColors by viewModel.calendarColors.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var menuOpen by remember { mutableStateOf(false) }

    // Toast results posted by editor screens
    val currentEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentEntry) {
        val handle = currentEntry?.savedStateHandle ?: return@LaunchedEffect
        handle.getStateFlow<String?>(Routes.RESULT_TOAST, null).collect { msg ->
            if (msg != null) {
                snackbarHostState.showSnackbar(msg)
                handle.remove<String>(Routes.RESULT_TOAST)
            }
        }
    }

    val jSelected = JalaliConverter.toJalali(selectedDate)
    val today = LocalDate.now(ZoneId.of(settings.timeZone))

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(Formatting.monthYear(jSelected.year, jSelected.month, settings))
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.previousPeriod() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.goToToday() }) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = strings.today)
                    }
                    IconButton(onClick = { viewModel.nextPeriod() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                    IconButton(onClick = { navController.navigate(Routes.SEARCH) }) {
                        Icon(Icons.Filled.Search, contentDescription = strings.search)
                    }
                    Box {
                        IconButton(onClick = { menuOpen = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = strings.settings)
                        }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(
                                text = { Text(strings.calendars) },
                                onClick = { menuOpen = false; navController.navigate(Routes.CALENDARS) },
                                leadingIcon = { Icon(Icons.Filled.CalendarMonth, null) },
                            )
                            DropdownMenuItem(
                                text = { Text(strings.settings) },
                                onClick = { menuOpen = false; navController.navigate(Routes.SETTINGS) },
                                leadingIcon = { Icon(Icons.Filled.Settings, null) },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val zone = ZoneId.of(settings.timeZone)
                    val start = TimeUtils.toUtcMillis(selectedDate, LocalTime.of(9, 0), zone)
                    navController.navigate("${Routes.EVENT_NEW}?startMillis=$start")
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = strings.addEvent)
            }
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // View mode tabs
            TabRow(selectedTabIndex = viewMode.ordinal) {
                ViewMode.entries.forEach { mode ->
                    val label = when (mode) {
                        ViewMode.MONTH -> strings.viewMonth
                        ViewMode.WEEK -> strings.viewWeek
                        ViewMode.DAY -> strings.viewDay
                    }
                    Tab(
                        selected = viewMode == mode,
                        onClick = { viewModel.setViewMode(mode) },
                        text = { Text(label) },
                    )
                }
            }

            when (viewMode) {
                ViewMode.MONTH -> MonthContent(
                    viewModel = viewModel,
                    selectedDate = selectedDate,
                    today = today,
                    occurrences = occurrences,
                    calendarColors = calendarColors,
                    onOpenEvent = { navController.navigate(Routes.eventEdit(it.event.id)) },
                )

                ViewMode.WEEK -> {
                    val zone = ZoneId.of(settings.timeZone)
                    val weekStart = selectedDate.minusDays(
                        Formatting.weekIndexOf(selectedDate, settings.weekStart).toLong(),
                    )
                    WeekView(
                        weekStartDate = weekStart,
                        settings = settings,
                        occurrences = occurrences,
                        today = today,
                        selected = selectedDate,
                        calendarColors = calendarColors,
                        onDateSelected = { viewModel.selectDate(it) },
                        onEventClick = { navController.navigate(Routes.eventEdit(it.event.id)) },
                    )
                }

                ViewMode.DAY -> DayView(
                    date = selectedDate,
                    settings = settings,
                    occurrences = occurrences,
                    calendarColors = calendarColors,
                    onEventClick = { navController.navigate(Routes.eventEdit(it.event.id)) },
                )
            }
        }
    }
}

@Composable
private fun MonthContent(
    viewModel: MainViewModel,
    selectedDate: LocalDate,
    today: LocalDate,
    occurrences: List<Occurrence>,
    calendarColors: Map<String, Int>,
    onOpenEvent: (Occurrence) -> Unit,
) {
    val settings = viewModel.settings.collectAsStateWithLifecycle().value
    val strings = LocalStrings.current

    // Group occurrences by civil day for the grid bars
    val eventsByDay: Map<LocalDate, List<Int>> = occurrences.groupBy {
        Formatting.localDate(it.startUtcMillis, settings)
    }.mapValues { (_, list) -> list.map { calendarColors[it.event.calendarId] ?: 0xFF006750.toInt() } }

    val dayOccurrences = occurrences.filter {
        Formatting.localDate(it.startUtcMillis, settings) == selectedDate
    }

    Column(Modifier.fillMaxSize()) {
        MonthGrid(
            monthAnchor = selectedDate,
            settings = settings,
            weekStart = settings.weekStart,
            selectedDate = selectedDate,
            todayDate = today,
            eventsByDay = eventsByDay,
            onDateSelected = { viewModel.selectDate(it) },
            persianLanguage = com.example.jalicalendar.ui.LocalPersianLanguage.current,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        )
        HorizontalDivider()
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = Formatting.dayLabel(selectedDate, today, strings, settings),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        if (dayOccurrences.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    strings.noEvents,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(dayOccurrences, key = { "${it.event.id}_${it.startUtcMillis}" }) { occ ->
                    OccurrenceRow(occ, settings, calendarColors, onOpenEvent)
                }
            }
        }
    }
}
