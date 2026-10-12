package com.example.jalicalendar.core.time

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Generates occurrence start instants from a [RecurrenceRule].
 *
 * Semantics (RFC 5545):
 * - DTSTART is always occurrence #1 and counts toward COUNT even when it does
 *   not match the BY* rules.
 * - Occurrences keep the DTSTART wall-clock time in [zone]; DST gaps shift the
 *   instant forward (02:30 → 03:30), overlaps resolve to the earlier offset.
 * - Month/day combinations that don't exist (e.g. Jan 31 in February) are
 *   skipped, never clamped.
 */
object RecurrenceExpander {

    /** Safety cap on generated candidates (bounds pathological rules). */
    private const val MAX_CANDIDATES = 100_000

    /**
     * @param startUtcMillis DTSTART as UTC instant
     * @param zone zone the rule's wall-clock times are defined in
     *             (ZoneOffset.UTC for all-day events)
     * @return occurrence starts in [rangeStartUtcMillis, rangeEndUtcMillis),
     *         ascending and de-duplicated, capped at [maxOccurrences]
     */
    fun expand(
        rule: RecurrenceRule,
        startUtcMillis: Long,
        zone: ZoneId,
        rangeStartUtcMillis: Long,
        rangeEndUtcMillis: Long,
        maxOccurrences: Int = 1000,
    ): List<Long> {
        if (rangeEndUtcMillis <= rangeStartUtcMillis) return emptyList()

        val results = LinkedHashSet<Long>()

        // DTSTART is always the first occurrence (counts toward COUNT).
        if (startUtcMillis >= rangeStartUtcMillis && startUtcMillis < rangeEndUtcMillis) {
            results += startUtcMillis
        }
        if (rule.count != null && rule.count <= 1) return results.toList()

        val startDate = TimeUtils.localDateOf(startUtcMillis, zone)
        val startTime = TimeUtils.localTimeOf(startUtcMillis, zone)

        var seriesCount = 1 // DTSTART consumed
        var candidates = 0

        for (candidateDate in candidateDates(rule, startDate)) {
            if (++candidates > MAX_CANDIDATES) break
            if (candidateDate == startDate) continue // DTSTART already handled

            val instant = TimeUtils.toUtcMillis(candidateDate, startTime, zone)

            val until = rule.untilUtcMillis
            if (until != null && instant > until) break
            seriesCount++
            if (rule.count != null && seriesCount > rule.count) break
            if (instant >= rangeEndUtcMillis) break

            if (instant >= rangeStartUtcMillis) results += instant
            if (results.size >= maxOccurrences) break
        }
        return results.sorted()
    }

    /** Infinite ascending sequence of candidate civil dates after DTSTART. */
    private fun candidateDates(rule: RecurrenceRule, startDate: LocalDate): Sequence<LocalDate> =
        sequence {
            when (rule.freq) {
                RecurrenceRule.Freq.DAILY -> {
                    val weekdays = rule.byDay.map { it.day }.toSet() // BYDAY narrows DAILY (RFC 5545)
                    var d = startDate
                    while (true) {
                        if (weekdays.isEmpty() || d.dayOfWeek in weekdays) yield(d)
                        d = d.plusDays(rule.interval.toLong())
                    }
                }

                RecurrenceRule.Freq.WEEKLY -> {
                    val days = if (rule.byDay.isEmpty()) {
                        listOf(startDate.dayOfWeek)
                    } else {
                        rule.byDay.map { it.day }.distinct().sorted()
                    }
                    var weekStart = startDate.with(TemporalAdjusters.previousOrSame(rule.wkst))
                    while (true) {
                        for (day in days) {
                            val offset = (day.value - rule.wkst.value + 7) % 7
                            val c = weekStart.plusDays(offset.toLong())
                            if (c > startDate) yield(c)
                        }
                        weekStart = weekStart.plusWeeks(rule.interval.toLong())
                    }
                }

                RecurrenceRule.Freq.MONTHLY -> {
                    var ym = YearMonth.from(startDate)
                    while (true) {
                        for (d in monthlyCandidates(rule, ym, startDate)) {
                            if (d > startDate) yield(d)
                        }
                        ym = ym.plusMonths(rule.interval.toLong())
                    }
                }

                RecurrenceRule.Freq.YEARLY -> {
                    var year = startDate.year
                    val months = if (rule.byMonth.isEmpty()) listOf(startDate.monthValue)
                                 else rule.byMonth.distinct().sorted()
                    while (true) {
                        for (month in months) {
                            if (month !in 1..12) continue
                            val ym = YearMonth.of(year, month)
                            for (d in monthlyCandidates(rule, ym, startDate, fromDtstartDay = rule.byMonth.isEmpty())) {
                                if (d > startDate) yield(d)
                            }
                        }
                        year += rule.interval
                    }
                }
            }
        }

    /**
     * Matching days of [ym] for MONTHLY (and YEARLY) rules, ascending.
     * [fromDtstartDay] selects the default day-of-month source: DTSTART's day
     * when no BY* rules narrow the month (default behaviour of both FREQs).
     */
    private fun monthlyCandidates(
        rule: RecurrenceRule,
        ym: YearMonth,
        startDate: LocalDate,
        fromDtstartDay: Boolean = true,
    ): List<LocalDate> {
        val len = ym.lengthOfMonth()
        val days = mutableSetOf<LocalDate>()

        when {
            rule.byDay.isNotEmpty() -> {
                for (byDay in rule.byDay) {
                    val matched = (1..len).map { ym.atDay(it) }
                        .filter { it.dayOfWeek == byDay.day }
                    val ord = byDay.ordinal
                    when {
                        ord == null -> days.addAll(matched)
                        ord > 0 -> matched.getOrNull(ord - 1)?.let { days += it }
                        else -> matched.getOrNull(matched.size + ord)?.let { days += it }
                    }
                }
            }

            rule.byMonthDay.isNotEmpty() -> {
                for (n in rule.byMonthDay) {
                    val day = if (n > 0) n else len + n + 1
                    if (day in 1..len) days += ym.atDay(day)
                }
            }

            fromDtstartDay -> {
                val day = startDate.dayOfMonth
                if (day <= len) days += ym.atDay(day)
            }
        }
        return days.sorted()
    }
}
