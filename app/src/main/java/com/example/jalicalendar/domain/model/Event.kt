package com.example.jalicalendar.domain.model

/**
 * A calendar event.
 *
 * Contract (see constraint §6/§9):
 * - All instants are UTC epoch millis ([startUtcMillis], [endUtcMillis], ...).
 * - For all-day events the UTC fields store UTC-midnight of the civil date and
 *   [allDay] is true; consumers format them as dates, never as clock times.
 * - [timeZone] is the IANA zone the event is *defined* in (used for recurrence
 *   expansion across DST); display uses the user's configured zone.
 * - [rrule]/[rdate]/[exdate] are RFC 5545 strings; expanded occurrences carry
 *   [recurringEventId] + [originalStartUtcMillis] (Google's single-instance override model).
 * - Sync state: [dirty] = local change pending push, [deleted] = soft-deleted
 *   pending remote deletion (last-write-wins resolution happens in the sync step).
 */
data class Event(
    val id: String, // local UUID — primary key, never changes
    val calendarId: String,
    val remoteId: String? = null, // Google event id; null until first successful push/pull
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startUtcMillis: Long,
    val endUtcMillis: Long,
    val allDay: Boolean = false,
    val timeZone: String? = null,
    val rrule: String? = null,
    val rdate: String? = null,
    val exdate: String? = null,
    val recurringEventId: String? = null,
    val originalStartUtcMillis: Long? = null,
    val status: String = "confirmed", // confirmed | tentative | cancelled
    val attendeesJson: String? = null,
    val organizer: String? = null,
    val colorArgb: Int? = null,
    val visibility: String? = null, // default | public | private
    val transparency: String? = null, // opaque | transparent
    val etag: String? = null,
    val remoteUpdatedAtMillis: Long? = null,
    val localUpdatedAtMillis: Long = 0L,
    val dirty: Boolean = false,
    val deleted: Boolean = false,
)
