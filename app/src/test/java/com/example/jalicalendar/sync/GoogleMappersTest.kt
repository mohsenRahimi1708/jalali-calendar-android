package com.example.jalicalendar.sync

import com.example.jalicalendar.data.remote.EventDateTimeDto
import com.example.jalicalendar.data.remote.EventDto
import com.example.jalicalendar.data.remote.TaskDto
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Task
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Domain to/from wire mapping. Verifies all-day vs timed handling, the RFC 5545
 * RRULE prefix, and UTC anchoring.
 */
class GoogleMappersTest {

    @Test
    fun `all-day event maps to date-only and back`() {
        val start = LocalDate.of(2026, 3, 21).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val end = LocalDate.of(2026, 3, 22).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val event = Event(
            id = "local-1", calendarId = "cal", title = "Nowruz",
            startUtcMillis = start, endUtcMillis = end, allDay = true,
        )
        val dto = event.toDto()
        assertThat(dto.start?.date).isEqualTo("2026-03-21")
        assertThat(dto.start?.dateTime).isNull()
        assertThat(dto.end?.date).isEqualTo("2026-03-22")

        val back = dto.toDomain(calendarId = "cal", localId = "local-1")
        assertThat(back.allDay).isTrue()
        assertThat(back.startUtcMillis).isEqualTo(start)
        assertThat(back.endUtcMillis).isEqualTo(end)
        assertThat(back.title).isEqualTo("Nowruz")
    }

    @Test
    fun `timed event maps to RFC3339 UTC and back`() {
        val start = 1774065000000L // 2026-03-21T05:10:00Z
        val event = Event(
            id = "local-2", calendarId = "cal", title = "Standup",
            startUtcMillis = start, endUtcMillis = start + 3600_000L,
            allDay = false, timeZone = "Asia/Tehran",
        )
        val dto = event.toDto()
        assertThat(dto.start?.date).isNull()
        assertThat(dto.start?.dateTime).contains("T")
        assertThat(dto.start?.timeZone).isEqualTo("Asia/Tehran")

        val back = dto.toDomain(calendarId = "cal", localId = "local-2")
        assertThat(back.allDay).isFalse()
        assertThat(back.startUtcMillis).isEqualTo(start)
        assertThat(back.endUtcMillis).isEqualTo(start + 3600_000L)
        assertThat(back.timeZone).isEqualTo("Asia/Tehran")
    }

    @Test
    fun `rrule gains RRULE prefix on the wire and is stripped on the way back`() {
        val event = Event(
            id = "local-3", calendarId = "cal", title = "Daily",
            startUtcMillis = 1774065000000L, endUtcMillis = 1774068600000L,
            rrule = "FREQ=DAILY;COUNT=5",
        )
        val dto = event.toDto()
        assertThat(dto.recurrence).containsExactly("RRULE:FREQ=DAILY;COUNT=5")

        val back = dto.toDomain(calendarId = "cal", localId = "local-3")
        assertThat(back.rrule).isEqualTo("FREQ=DAILY;COUNT=5")
    }

    @Test
    fun `cancelled status becomes a deleted local row`() {
        val dto = EventDto(
            id = "g1", summary = "Gone", status = "cancelled",
            start = EventDateTimeDto(dateTime = "2026-03-21T09:00:00Z"),
            end = EventDateTimeDto(dateTime = "2026-03-21T10:00:00Z"),
        )
        val back = dto.toDomain(calendarId = "cal", localId = null)
        assertThat(back.deleted).isTrue()
        assertThat(back.remoteId).isEqualTo("g1")
    }

    @Test
    fun `task due date round-trips as UTC millis`() {
        val task = Task(
            id = "t1", listId = "list", title = "Buy milk",
            dueDateMillis = 1774065000000L, completed = false,
        )
        val dto = task.toDto()
        assertThat(dto.status).isEqualTo("needsAction")
        val back = dto.toDomain(listId = "list", localId = "t1")
        assertThat(back.dueDateMillis).isEqualTo(1774065000000L)
        assertThat(back.completed).isFalse()
    }

    @Test
    fun `task marked completed maps status and completed timestamp`() {
        val dto = TaskDto(
            id = "t2", title = "Done", status = "completed",
            updated = "2026-03-21T10:00:00Z",
        )
        val back = dto.toDomain(listId = "list", localId = null)
        assertThat(back.completed).isTrue()
        assertThat(back.status).isEqualTo("completed")
        assertThat(back.remoteUpdatedAtMillis).isNotNull()
    }
}
