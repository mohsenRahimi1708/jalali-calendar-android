package com.example.jalicalendar.sync

import com.example.jalicalendar.auth.AuthManager
import com.example.jalicalendar.auth.AuthSession
import com.example.jalicalendar.data.remote.CalendarApi
import com.example.jalicalendar.data.remote.TasksApi
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.repo.AccountRepository
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import com.example.jalicalendar.domain.repo.TaskRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json

/**
 * Integration test: the real Retrofit Calendar/Tasks clients (JSON serialization,
 * sync-token paging, 410 handling) driven by [SyncEngine] against a local
 * [MockWebServer]. Repos are mocked; the wire layer is real.
 */
class SyncEngineTest {

    private lateinit var server: MockWebServer
    private lateinit var engine: SyncEngine
    private lateinit var calendarRepo: CalendarRepository
    private lateinit var eventRepo: EventRepository
    private lateinit var cursorStore: SyncCursorStore

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
        coerceInputValues = true
    }

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val client = OkHttpClient.Builder().build()
        val converter = json.asConverterFactory("application/json".toMediaType())
        val calendarApi = Retrofit.Builder()
            .baseUrl(server.url("/calendar/v3/"))
            .client(client)
            .addConverterFactory(converter)
            .build()
            .create(CalendarApi::class.java)
        val tasksApi = Retrofit.Builder()
            .baseUrl(server.url("/tasks/v1/"))
            .client(client)
            .addConverterFactory(converter)
            .build()
            .create(TasksApi::class.java)

        val authManager = mockk<AuthManager>()
        every { authManager.isConfigured() } returns true
        every { authManager.tokenStoreSession() } returns AuthSession(
            accountId = "acc", email = "a@b.c", accessToken = "tok",
            refreshToken = "ref", expiresAtMillis = System.currentTimeMillis() + 3_600_000L,
        )
        every { authManager.needsRefresh() } returns false

        calendarRepo = mockk(relaxed = true)
        eventRepo = mockk(relaxed = true)
        val taskRepo = mockk<TaskRepository>(relaxed = true)
        val accountRepo = mockk<AccountRepository>(relaxed = true)
        val settingsRepo = mockk<SettingsRepository>(relaxed = true)
        val conflicts = ConflictStore()
        val statusStore = SyncStatusStore()
        cursorStore = mockk(relaxed = true)
        val watchManager = mockk<WatchManager>(relaxed = true)
        val reminderScheduler = mockk<com.example.jalicalendar.reminders.ReminderScheduler>(relaxed = true)

        coEvery { settingsRepo.current() } returns AppSettings.DEFAULT
        every { cursorStore.get(any()) } returns null

        engine = SyncEngine(
            authManager = authManager,
            accountRepo = accountRepo,
            calendarRepo = calendarRepo,
            eventRepo = eventRepo,
            taskRepo = taskRepo,
            settingsRepo = settingsRepo,
            calendarApi = calendarApi,
            tasksApi = tasksApi,
            conflicts = conflicts,
            cursorStore = cursorStore,
            statusStore = statusStore,
            watchManager = watchManager,
            reminderScheduler = reminderScheduler,
        )
    }

    @After
    fun tearDown() {
        try { server.shutdown() } catch (_: Exception) {}
    }

    private val remoteCalendar = Calendar(
        id = "local-cal", remoteId = "primary", title = "Work",
        accessRole = "owner", type = Calendar.TYPE_CALENDAR,
    )

    @Test
    fun `full pull stores events and advances the sync token`() = runBlocking {
        // One calendar list page carrying a nextSyncToken, one events page.
        server.enqueue(jsonResponse(200, """
            {"items":[{"id":"primary","summary":"Work","accessRole":"owner","timeZone":"Asia/Tehran"}],
             "nextSyncToken":"CURSOR_CAL_1"}
        """.trimIndent()))
        server.enqueue(jsonResponse(200, """
            {"items":[
              {"id":"e1","summary":"Standup","status":"confirmed",
               "start":{"dateTime":"2026-03-21T09:00:00Z"},
               "end":{"dateTime":"2026-03-21T09:30:00Z"},
               "updated":"2026-03-20T10:00:00Z","etag":"etag1"},
              {"id":"e2","summary":"Nowruz","status":"confirmed",
               "start":{"date":"2026-03-21"},"end":{"date":"2026-03-22"},
               "updated":"2026-03-20T10:00:00Z","etag":"etag2"}
             ],
             "nextSyncToken":"CURSOR_EVT_1"}
        """.trimIndent()))
        // Task lists: empty.
        server.enqueue(jsonResponse(200, """{"items":[]}"""))

        coEvery { calendarRepo.observeCalendars() } returns flowOf(listOf(remoteCalendar))
        coEvery { eventRepo.pendingChanges() } returns emptyList()
        coEvery { eventRepo.findLocalByRemoteId(any()) } returns null
        coEvery { eventRepo.upcomingWithReminders(any()) } returns emptyList()

        val result = engine.syncAll()
        assertThat(result).isEqualTo(SyncResult.Success)

        // Both events landed locally via upsertSynced.
        coVerify(atLeast = 2) { eventRepo.upsertSynced(any()) }
        // Calendar-list cursor advanced via the cursor store.
        coVerify { cursorStore.set(SyncCursorStore.CALENDAR_LIST, "CURSOR_CAL_1") }
        // Per-calendar event cursor advanced via the calendar repo.
        coVerify { calendarRepo.setSyncToken(eq("primary"), eq("CURSOR_EVT_1"), any()) }
    }

    @Test
    fun `incremental 410 Gone falls back to a full resync`() = runBlocking {
        // calendarList cursor exists -> incremental request -> 410 -> full listing.
        every { cursorStore.get(SyncCursorStore.CALENDAR_LIST) } returns "STALE"
        // Incremental calendarList: 410.
        server.enqueue(jsonResponse(410, """{"error":{"code":410,"message":"Gone"}}"""))
        // Full calendarList after fallback.
        server.enqueue(jsonResponse(200, """
            {"items":[{"id":"primary","summary":"Work","accessRole":"owner"}],
             "nextSyncToken":"NEW_CAL"}
        """.trimIndent()))
        // Events full listing.
        server.enqueue(jsonResponse(200, """{"items":[],"nextSyncToken":"NEW_EVT"}"""))
        // Task lists empty.
        server.enqueue(jsonResponse(200, """{"items":[]}"""))

        coEvery { calendarRepo.observeCalendars() } returns flowOf(listOf(remoteCalendar.copy(syncToken = "STALE_EVT")))
        coEvery { eventRepo.pendingChanges() } returns emptyList()
        coEvery { eventRepo.upcomingWithReminders(any()) } returns emptyList()

        val result = engine.syncAll()
        assertThat(result).isEqualTo(SyncResult.Success)

        // After the 410, a fresh full listing re-based the cursor.
        coVerify { cursorStore.set(SyncCursorStore.CALENDAR_LIST, "NEW_CAL") }
    }

    @Test
    fun `LWW keeps the newer local copy and pushes it instead of overwriting`() = runBlocking {
        server.enqueue(jsonResponse(200, """{"items":[],"nextSyncToken":"C"}""")) // calendarList
        server.enqueue(jsonResponse(200, """
            {"items":[{"id":"e1","summary":"Server older","status":"confirmed",
              "start":{"dateTime":"2026-03-21T09:00:00Z"},
              "end":{"dateTime":"2026-03-21T10:00:00Z"},
              "updated":"2020-01-01T00:00:00Z","etag":"old"}],
             "nextSyncToken":"E"}
        """.trimIndent()))
        server.enqueue(jsonResponse(200, """{"items":[]}""")) // task lists

        // Local row is dirty and was edited far more recently than the server copy.
        val localNewer = Event(
            id = "local-1", calendarId = "primary", remoteId = "e1",
            title = "Local newer", startUtcMillis = 1774065000000L,
            endUtcMillis = 1774068600000L, dirty = true,
            localUpdatedAtMillis = System.currentTimeMillis(),
        )
        coEvery { calendarRepo.observeCalendars() } returns flowOf(listOf(remoteCalendar))
        coEvery { eventRepo.pendingChanges() } returns listOf(localNewer)
        coEvery { eventRepo.findLocalByRemoteId("e1") } returns localNewer
        coEvery { eventRepo.upcomingWithReminders(any()) } returns emptyList()

        val result = engine.syncAll()
        assertThat(result).isEqualTo(SyncResult.Success)

        // The dirty local row was NOT overwritten by the older server copy.
        coVerify(exactly = 0) { eventRepo.upsertSynced(match { it.id == "local-1" && it.title == "Server older" }) }
    }

    private fun jsonResponse(code: Int, body: String): MockResponse =
        MockResponse().setResponseCode(code)
            .setHeader("Content-Type", "application/json")
            .setBody(body)
}
