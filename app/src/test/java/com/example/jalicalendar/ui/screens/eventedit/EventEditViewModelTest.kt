package com.example.jalicalendar.ui.screens.eventedit

import androidx.lifecycle.SavedStateHandle
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import com.example.jalicalendar.reminders.ReminderScheduler
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Regression tests for the "Title is required" false error.
 *
 * On a fresh install there are no calendars (the Local calendar used to be
 * seeded by nobody), so `save()` hit the blank-calendarId branch and reported
 * the failure as `error = "title"` even with a valid title. The editor must
 * seed the Local calendar instead and never masquerade a calendar problem
 * as a title error.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EventEditViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val calendarFlow = MutableStateFlow<List<Calendar>>(emptyList())
    private lateinit var calendarRepo: CalendarRepository
    private lateinit var eventRepo: EventRepository
    private lateinit var settingsRepo: SettingsRepository
    private lateinit var reminderScheduler: ReminderScheduler

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        calendarFlow.value = emptyList()
        calendarRepo = mockk {
            coEvery { seedLocalCalendar() } answers {
                val local = Calendar(
                    id = "local_test",
                    type = Calendar.TYPE_CALENDAR,
                    title = "Local",
                    selected = true,
                )
                calendarFlow.value = listOf(local)
                "local_test"
            }
            every { observeCalendars() } returns calendarFlow
        }
        eventRepo = mockk(relaxed = true) {
            coEvery { upsert(any()) } returns "saved"
            coEvery { remindersFor(any()) } returns emptyList()
        }
        settingsRepo = mockk {
            every { settings } returns flowOf(AppSettings.DEFAULT)
            coEvery { current() } returns AppSettings.DEFAULT
        }
        reminderScheduler = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): EventEditViewModel = EventEditViewModel(
        savedStateHandle = SavedStateHandle(),
        eventRepo = eventRepo,
        calendarRepo = calendarRepo,
        settingsRepo = settingsRepo,
        reminderScheduler = reminderScheduler,
    )

    @Test
    fun `fresh install - editor seeds local calendar instead of leaving id blank`() {
        val vm = createViewModel()
        dispatcher.scheduler.advanceUntilIdle()

        // The Local calendar exists after init and is selected.
        assertThat(calendarFlow.value).hasSize(1)
        assertThat(vm.state.value.calendarId).isEqualTo("local_test")
        assertThat(vm.state.value.loading).isFalse()
    }

    @Test
    fun `save with valid title on empty-calendar install succeeds - no title error`() {
        val vm = createViewModel()
        dispatcher.scheduler.advanceUntilIdle()

        vm.setTitle("apoitment")
        vm.save()
        dispatcher.scheduler.advanceUntilIdle()

        // Regression: this used to set error="title" ("Title is required").
        assertThat(vm.state.value.error).isNull()
        assertThat(vm.state.value.finished).isTrue()
        coVerify {
            eventRepo.upsert(match { it.title == "apoitment" && it.calendarId == "local_test" })
        }
    }

    @Test
    fun `stale default calendar id falls back to the seeded local calendar`() {
        val staleSettings = mockk<SettingsRepository> {
            every { settings } returns flowOf(AppSettings.DEFAULT.copy(defaultCalendarId = "deleted_calendar"))
            coEvery { current() } returns AppSettings.DEFAULT.copy(defaultCalendarId = "deleted_calendar")
        }
        val vm = EventEditViewModel(
            savedStateHandle = SavedStateHandle(),
            eventRepo = eventRepo,
            calendarRepo = calendarRepo,
            settingsRepo = staleSettings,
            reminderScheduler = reminderScheduler,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(vm.state.value.calendarId).isEqualTo("local_test")

        vm.setTitle("stale default")
        vm.save()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(vm.state.value.error).isNull()
    }
}
