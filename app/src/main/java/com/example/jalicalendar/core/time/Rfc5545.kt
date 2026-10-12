package com.example.jalicalendar.core.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Parses RFC 5545 / RFC 3339 date-time values as Google Calendar emits them
 * in RRULE UNTIL, RDATE and EXDATE lists:
 * - `20260105T090000Z`   (RFC 5545 compact UTC)
 * - `2026-01-05T09:00:00Z` / `…+03:30` (RFC 3339 / ISO-8601)
 * - `20260105` / `2026-01-05` (date-only → UTC midnight, for all-day events)
 *
 * All results are UTC epoch millis.
 */
object Rfc5545 {

    private val COMPACT_UTC = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    private val COMPACT_DATE = DateTimeFormatter.ofPattern("yyyyMMdd")

    /** Returns UTC millis, or null when [value] matches none of the formats. */
    fun parseDateTime(value: String): Long? {
        val v = value.trim()
        if (v.isEmpty()) return null
        return try {
            when {
                // 20260105T090000Z (RFC 5545 compact, UTC)
                v.length == 16 && v.endsWith("Z") && v[8] == 'T' ->
                    LocalDateTime.parse(v.substring(0, 15), COMPACT_UTC)
                        .toInstant(ZoneOffset.UTC).toEpochMilli()

                // 20260105T090000 (RFC 5545 compact, no zone → UTC)
                v.length == 15 && v[8] == 'T' ->
                    LocalDateTime.parse(v, COMPACT_UTC).toInstant(ZoneOffset.UTC).toEpochMilli()

                // 20260105 (date-only)
                v.length == 8 && v.all { it.isDigit() } ->
                    TimeUtils.utcStartOfDay(LocalDate.parse(v, COMPACT_DATE))

                // ISO-8601 date-only
                v.length == 10 && v[4] == '-' ->
                    TimeUtils.utcStartOfDay(LocalDate.parse(v))

                // RFC 3339 with offset or Z
                else ->
                    OffsetDateTime.parse(v).toInstant().toEpochMilli()
            }
        } catch (e: DateTimeParseException) {
            null
        }
    }

    /** Parses a comma-separated RDATE/EXDATE list; silently drops bad entries. */
    fun parseDateTimeList(value: String?): List<Long> =
        value.orEmpty().split(',')
            .mapNotNull { parseDateTime(it) }
            .distinct()
            .sorted()
}
