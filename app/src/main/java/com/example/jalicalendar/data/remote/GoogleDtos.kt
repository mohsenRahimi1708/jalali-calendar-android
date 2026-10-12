package com.example.jalicalendar.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Wire models for Google Calendar API v3 + Tasks API v1. Field names mirror
 * the JSON; everything is nullable-with-defaults because the API omits unset
 * fields, and [ignoreUnknownKeys] drops new server fields (spec §9).
 */

@Serializable
data class ApiErrorDto(
    val error: ApiErrorBody? = null,
)

@Serializable
data class ApiErrorBody(
    val code: Int? = null,
    val message: String? = null,
    val errors: List<ApiErrorItem>? = null,
)

@Serializable
data class ApiErrorItem(
    val reason: String? = null,
    val domain: String? = null,
)

// --- Calendar list ----------------------------------------------------------

@Serializable
data class CalendarListResponseDto(
    val items: List<CalendarListEntryDto>? = null,
    val nextPageToken: String? = null,
    val nextSyncToken: String? = null,
)

@Serializable
data class CalendarListEntryDto(
    val id: String? = null,
    val summary: String? = null,
    val description: String? = null,
    val primary: Boolean? = null,
    val accessRole: String? = null,
    val hidden: Boolean? = null,
    val selected: Boolean? = null,
    val timeZone: String? = null,
    val colorId: String? = null,
    val etag: String? = null,
    val reminders: CalendarRemindersDto? = null,
)

@Serializable
data class CalendarRemindersDto(
    val useDefault: Boolean? = null,
    val overrides: List<ReminderOverrideDto>? = null,
)

@Serializable
data class ReminderOverrideDto(
    val method: String? = null, // alert | email
    val minutes: Int? = null,
)

// --- Events -----------------------------------------------------------------

@Serializable
data class EventsResponseDto(
    val items: List<EventDto>? = null,
    val nextPageToken: String? = null,
    val nextSyncToken: String? = null,
)

@Serializable
data class EventDto(
    val id: String? = null,
    val etag: String? = null,
    val status: String? = null, // confirmed | tentative | cancelled
    val summary: String? = null,
    val description: String? = null,
    val location: String? = null,
    val colorId: String? = null,
    val start: EventDateTimeDto? = null,
    val end: EventDateTimeDto? = null,
    val recurrence: List<String>? = null,
    val recurringEventId: String? = null,
    val originalStartTime: EventDateTimeDto? = null,
    val created: String? = null,
    val updated: String? = null,
    val sequence: Int? = null,
    val transparency: String? = null,
    val visibility: String? = null,
    val organizer: OrganizerDto? = null,
    val attendees: List<AttendeeDto>? = null,
    val reminders: EventRemindersDto? = null,
)

@Serializable
data class EventDateTimeDto(
    /** All-day: yyyy-MM-dd (no time). Mutually exclusive with [dateTime]. */
    val date: String? = null,
    /** Timed: RFC 3339 with offset, e.g. 2026-01-05T09:00:00+03:30 or ...Z. */
    @JsonNames("dateTime") val dateTime: String? = null,
    val timeZone: String? = null,
)

@Serializable
data class OrganizerDto(
    val email: String? = null,
    val displayName: String? = null,
    val self: Boolean? = null,
)

@Serializable
data class AttendeeDto(
    val email: String? = null,
    val displayName: String? = null,
    val responseStatus: String? = null,
    val self: Boolean? = null,
)

@Serializable
data class EventRemindersDto(
    val useDefault: Boolean? = null,
    val overrides: List<ReminderOverrideDto>? = null,
)

// --- Settings / ACL ---------------------------------------------------------

@Serializable
data class SettingsResponseDto(
    val items: Map<String, String>? = null,
    val etag: String? = null,
)

@Serializable
data class AclResponseDto(
    val items: List<AclRuleDto>? = null,
    val nextPageToken: String? = null,
    val nextSyncToken: String? = null,
)

@Serializable
data class AclRuleDto(
    val id: String? = null,
    val etag: String? = null,
    val role: String? = null, // none | freeBusyReader | reader | writer | owner
    val scope: AclScopeDto? = null,
)

@Serializable
data class AclScopeDto(
    val type: String? = null, // default | user | group | domain | anyone
    val value: String? = null,
)

// --- Watch channels ---------------------------------------------------------

@Serializable
data class WatchRequestDto(
    val id: String,          // channel id (client-generated UUID)
    val type: String = "web_hook",
    val address: String,     // public HTTPS webhook
    val token: String? = null,
    val expiration: Long? = null,
)

@Serializable
data class WatchResponseDto(
    val id: String? = null,
    val resourceId: String? = null,
    val resourceUri: String? = null,
    val token: String? = null,
    val expiration: String? = null, // epoch millis as string
)

// --- Tasks API --------------------------------------------------------------

@Serializable
data class TaskListsResponseDto(
    val items: List<TaskListDto>? = null,
    val nextPageToken: String? = null,
)

@Serializable
data class TaskListDto(
    val id: String? = null,
    val etag: String? = null,
    val title: String? = null,
    val updated: String? = null,
)

@Serializable
data class TasksResponseDto(
    val items: List<TaskDto>? = null,
    val nextPageToken: String? = null,
)

@Serializable
data class TaskDto(
    val id: String? = null,
    val etag: String? = null,
    val title: String? = null,
    val notes: String? = null,
    val status: String? = null, // needsAction | completed
    val due: String? = null,    // RFC 3339 (date-only for all-day due)
    val completed: String? = null,
    val updated: String? = null,
    val position: String? = null,
    val deleted: Boolean? = null,
)
