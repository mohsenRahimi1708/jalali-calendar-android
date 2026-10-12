package com.example.jalicalendar.core.time

import com.example.jalicalendar.domain.model.Event
import java.time.ZoneId

/**
 * Turns an [Event] into concrete occurrence instants inside a range,
 * applying its RRULE (+ INTERVAL semantics), RDATE list and EXDATE exclusions.
 * Invalid rules degrade to a single occurrence instead of crashing the UI.
 */
object OccurrenceExpander {

    /**
     * @param zone display/user zone for timed events; all-day events always
     *             expand on the UTC date axis
     * @return ascending, de-duplicated occurrence start instants within
     *         [rangeStartUtcMillis, rangeEndUtcMillis)
     */
    fun occurrences(
        event: Event,
        rangeStartUtcMillis: Long,
        rangeEndUtcMillis: Long,
        zone: ZoneId,
    ): List<Long> {
        if (rangeEndUtcMillis <= rangeStartUtcMillis) return emptyList()
        val expansionZone = if (event.allDay) ZoneId.of("UTC") else zone

        val base: List<Long> = if (event.rrule.isNullOrBlank()) {
            listOf(event.startUtcMillis)
        } else {
            runCatching {
                RecurrenceExpander.expand(
                    rule = RecurrenceRuleParser.parse(event.rrule!!),
                    startUtcMillis = event.startUtcMillis,
                    zone = expansionZone,
                    rangeStartUtcMillis = rangeStartUtcMillis,
                    rangeEndUtcMillis = rangeEndUtcMillis,
                )
            }.getOrElse { listOf(event.startUtcMillis) }
        }

        val rdates = Rfc5545.parseDateTimeList(event.rdate)
        val exdates = Rfc5545.parseDateTimeList(event.exdate)

        return (base + rdates)
            .distinct()
            .filter { it !in exdates }
            .filter { it >= rangeStartUtcMillis && it < rangeEndUtcMillis }
            .sorted()
    }

    /** Inclusive length of one occurrence (defaults to 1 h when malformed). */
    fun durationMillis(event: Event): Long {
        val d = event.endUtcMillis - event.startUtcMillis
        return if (d > 0) d else 3_600_000L
    }
}
