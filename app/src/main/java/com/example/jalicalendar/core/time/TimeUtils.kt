package com.example.jalicalendar.core.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * UTC/civil-date bridge utilities. Invariant per spec §6/§9: the app stores
 * instants as UTC epoch millis and converts to a display zone only at the edge.
 * All-day events live on the pure date axis and are anchored at UTC midnight.
 */
object TimeUtils {

    fun localDateOf(epochMillis: Long, zone: ZoneId): LocalDate =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()

    fun localTimeOf(epochMillis: Long, zone: ZoneId): LocalTime =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalTime()

    /** First instant of the local day containing [epochMillis]. */
    fun startOfLocalDay(epochMillis: Long, zone: ZoneId): Long =
        localDateOf(epochMillis, zone).atStartOfDay(zone).toInstant().toEpochMilli()

    /** First instant of the next local day (exclusive end of the day). */
    fun endOfLocalDay(epochMillis: Long, zone: ZoneId): Long =
        localDateOf(epochMillis, zone).plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

    /** UTC midnight of a civil date — the anchor for all-day events. */
    fun utcStartOfDay(date: LocalDate): Long =
        date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    /** Exclusive: UTC midnight of the following day. */
    fun utcEndOfDay(date: LocalDate): Long = utcStartOfDay(date.plusDays(1))

    /** First instant of a local day in [zone] (DST-aware; gap handled by ZonedDateTime). */
    fun localStartOfDay(date: LocalDate, zone: ZoneId): Long =
        date.atStartOfDay(zone).toInstant().toEpochMilli()

    /** Combine a civil date + wall-clock time in [zone] into a UTC instant. */
    fun toUtcMillis(date: LocalDate, time: LocalTime, zone: ZoneId): Long =
        ZonedDateTime.of(date, time, zone).toInstant().toEpochMilli()

    /** UTC-offset in minutes of [zone] at [epochMillis] (e.g. Tehran: 210 or 270 during DST). */
    fun zoneOffsetMinutes(epochMillis: Long, zone: ZoneId): Int =
        zone.getRules().getOffset(Instant.ofEpochMilli(epochMillis)).totalSeconds / 60
}
