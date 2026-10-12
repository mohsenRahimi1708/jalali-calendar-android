package com.example.jalicalendar.sync

import com.example.jalicalendar.core.time.Rfc5545
import com.example.jalicalendar.data.remote.EventDateTimeDto
import com.example.jalicalendar.data.remote.EventDto
import com.example.jalicalendar.data.remote.TaskDto
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Domain ⇄ wire mapping. The domain keeps UTC epoch millis (spec §6: UTC
 * internally); Google carries either a `date` (all-day, anchored at UTC
 * midnight) or a `dateTime` RFC 3339 instant, plus an optional IANA zone.
 */

fun Event.toDto(): EventDto = EventDto(
    id = remoteId,
    status = if (deleted) "cancelled" else status,
    summary = title,
    description = description,
    location = location,
    start = if (allDay) {
        EventDateTimeDto(date = Instant.ofEpochMilli(startUtcMillis).atZone(ZoneOffset.UTC).toLocalDate().toString())
    } else {
        EventDateTimeDto(
            dateTime = Instant.ofEpochMilli(startUtcMillis).atZone(ZoneOffset.UTC)
                .toString(), // RFC 3339, UTC 'Z'
            timeZone = timeZone ?: "UTC",
        )
    },
    end = if (allDay) {
        EventDateTimeDto(date = Instant.ofEpochMilli(endUtcMillis).atZone(ZoneOffset.UTC).toLocalDate().toString())
    } else {
        EventDateTimeDto(
            dateTime = Instant.ofEpochMilli(endUtcMillis).atZone(ZoneOffset.UTC).toString(),
            timeZone = timeZone ?: "UTC",
        )
    },
    recurrence = rrule?.takeIf { it.isNotBlank() }?.let {
        // RFC 5545 values on the wire require the RRULE: prefix.
        listOf(if (it.startsWith("RRULE:") || it.startsWith("EXRULE:")) it else "RRULE:$it")
    },
    recurringEventId = recurringEventId,
    originalStartTime = originalStartUtcMillis?.let { millis ->
        if (allDay) EventDateTimeDto(date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toString())
        else EventDateTimeDto(dateTime = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toString())
    },
    transparency = transparency,
    visibility = visibility,
    etag = etag,
)

/**
 * Wire → domain. [localId] is the Room primary key; when null a fresh UUID is
 * minted (pull path). Recurring masters keep their RRULE; individual
 * occurrences carry `recurringEventId` + `originalStartTime`.
 */
fun EventDto.toDomain(calendarId: String, localId: String?): Event {
    val allDay = start?.date != null
    val startMillis = if (allDay) {
        LocalDate.parse(start!!.date!!).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    } else {
        start?.dateTime?.let(Rfc5545::parseDateTime) ?: 0L
    }
    val endMillis = when {
        end?.date != null -> LocalDate.parse(end.date).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        end?.dateTime != null -> Rfc5545.parseDateTime(end.dateTime) ?: startMillis
        allDay -> startMillis + 24 * 3600_000L
        else -> startMillis + 3600_000L
    }
    return Event(
        id = localId ?: java.util.UUID.randomUUID().toString(),
        calendarId = calendarId,
        remoteId = id,
        title = summary ?: "(untitled)",
        description = description,
        location = location,
        startUtcMillis = startMillis,
        endUtcMillis = endMillis,
        allDay = allDay,
        timeZone = if (allDay) null else start?.timeZone,
        rrule = recurrence?.firstOrNull()?.takeIf { it.startsWith("RRULE:") }?.removePrefix("RRULE:")
            ?: recurrence?.firstOrNull(),
        recurringEventId = recurringEventId,
        originalStartUtcMillis = originalStartTime?.let {
            if (it.date != null) LocalDate.parse(it.date).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            else it.dateTime?.let(Rfc5545::parseDateTime)
        },
        status = status ?: "confirmed",
        transparency = transparency,
        visibility = visibility,
        etag = etag,
        remoteUpdatedAtMillis = updated?.let(Rfc5545::parseDateTime),
        dirty = false,
        deleted = status == "cancelled",
    )
}

fun Task.toDto(): TaskDto = TaskDto(
    id = remoteId,
    title = title,
    notes = notes,
    status = if (completed) "completed" else "needsAction",
    due = dueDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toString() },
    completed = completedAtMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toString() },
    position = position,
    etag = etag,
    deleted = deleted.takeIf { it },
)

fun TaskDto.toDomain(listId: String, localId: String?): Task = Task(
    id = localId ?: java.util.UUID.randomUUID().toString(),
    listId = listId,
    remoteId = id,
    title = title ?: "(untitled)",
    notes = notes,
    dueDateMillis = due?.let { Rfc5545.parseDateTime(it) },
    completed = status == "completed",
    completedAtMillis = completed?.let { Rfc5545.parseDateTime(it) },
    status = status ?: "needsAction",
    position = position,
    etag = etag,
    remoteUpdatedAtMillis = updated?.let(Rfc5545::parseDateTime),
    dirty = false,
    deleted = deleted == true,
)
