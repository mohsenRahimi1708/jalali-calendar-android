package com.example.jalicalendar.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response as RetrofitResponse

/**
 * Google Calendar API v3. Both full and incremental variants exist because
 * incremental sync REQUIRES a syncToken query param and never pages forward.
 * `410 Gone` on the sync-token variant means "delete the cursor and resync" —
 * surfaced as [retrofit2.HttpException] to [com.example.jalicalendar.sync.SyncEngine].
 */
interface CalendarApi {

    @GET("users/me/calendarList")
    suspend fun listCalendarsFull(
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 250,
        @Query("showHidden") showHidden: Boolean = true,
    ): CalendarListResponseDto

    @GET("users/me/calendarList")
    suspend fun listCalendarsIncremental(
        @Query("syncToken") syncToken: String,
        @Query("maxResults") maxResults: Int = 250,
    ): CalendarListResponseDto

    @POST("users/me/calendarList")
    suspend fun insertCalendar(@Body entry: CalendarListEntryDto): CalendarListEntryDto

    @PATCH("users/me/calendarList/{id}")
    suspend fun updateCalendar(@Path("id") id: String, @Body entry: CalendarListEntryDto): CalendarListEntryDto

    @DELETE("users/me/calendarList/{id}")
    suspend fun deleteCalendar(@Path("id") id: String): RetrofitResponse<Unit>

    @GET("calendars/{calendarId}/events")
    suspend fun listEventsFull(
        @Path("calendarId") calendarId: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 2500,
        @Query("showDeleted") showDeleted: Boolean = true,
        @Query("showHidden") showHidden: Boolean = true,
    ): EventsResponseDto

    @GET("calendars/{calendarId}/events")
    suspend fun listEventsIncremental(
        @Path("calendarId") calendarId: String,
        @Query("syncToken") syncToken: String,
        @Query("maxResults") maxResults: Int = 2500,
        @Query("showDeleted") showDeleted: Boolean = true,
    ): EventsResponseDto

    @GET("calendars/{calendarId}/events/{eventId}")
    suspend fun getEvent(
        @Path("calendarId") calendarId: String,
        @Path("eventId") eventId: String,
    ): EventDto

    @POST("calendars/{calendarId}/events")
    suspend fun insertEvent(@Path("calendarId") calendarId: String, @Body event: EventDto): EventDto

    @PATCH("calendars/{calendarId}/events/{eventId}")
    suspend fun updateEvent(
        @Path("calendarId") calendarId: String,
        @Path("eventId") eventId: String,
        @Body event: EventDto,
    ): EventDto

    @DELETE("calendars/{calendarId}/events/{eventId}")
    suspend fun deleteEvent(
        @Path("calendarId") calendarId: String,
        @Path("eventId") eventId: String,
    ): RetrofitResponse<Unit>

    /** Same endpoint, different params: occurrence list for a recurring event. */
    @GET("calendars/{calendarId}/events")
    suspend fun listOccurrences(
        @Path("calendarId") calendarId: String,
        @Query("recurringEventId") recurringEventId: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 2500,
    ): EventsResponseDto

    @GET("calendars/{calendarId}/acl")
    suspend fun listAcl(
        @Path("calendarId") calendarId: String,
        @Query("pageToken") pageToken: String? = null,
    ): AclResponseDto

    @POST("calendars/{calendarId}/watch")
    suspend fun watchEvents(
        @Path("calendarId") calendarId: String,
        @Body request: WatchRequestDto,
    ): WatchResponseDto

    @POST("users/me/calendarList/watch")
    suspend fun watchCalendarList(@Body request: WatchRequestDto): WatchResponseDto

    @GET("users/me/settings")
    suspend fun getSettings(): SettingsResponseDto
}

/** Google Tasks API v1 — separate host, same auth. */
interface TasksApi {

    @GET("users/@me/lists")
    suspend fun listTaskLists(@Query("pageToken") pageToken: String? = null): TaskListsResponseDto

    @GET("users/@me/lists/{tasklist}/tasks")
    suspend fun listTasks(
        @Path("tasklist") taskListId: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int = 100,
        @Query("showCompleted") showCompleted: Boolean = true,
        @Query("showDeleted") showDeleted: Boolean = true,
        @Query("showHidden") showHidden: Boolean = true,
    ): TasksResponseDto

    @POST("users/@me/lists/{tasklist}/tasks")
    suspend fun insertTask(@Path("tasklist") taskListId: String, @Body task: TaskDto): TaskDto

    @PATCH("users/@me/lists/{tasklist}/tasks/{taskId}")
    suspend fun updateTask(
        @Path("tasklist") taskListId: String,
        @Path("taskId") taskId: String,
        @Body task: TaskDto,
    ): TaskDto

    @DELETE("users/@me/lists/{tasklist}/tasks/{taskId}")
    suspend fun deleteTask(
        @Path("tasklist") taskListId: String,
        @Path("taskId") taskId: String,
    ): RetrofitResponse<Unit>
}
