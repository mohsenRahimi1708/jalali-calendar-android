package com.example.jalicalendar.core.time

import java.time.DayOfWeek
import java.time.format.DateTimeParseException

/**
 * Parsed RFC 5545 RRULE. Supported parts: FREQ, INTERVAL, COUNT, UNTIL,
 * BYDAY (with ordinals like `1MO`, `-1FR`), BYMONTHDAY, BYMONTH, WKST.
 * Unsupported parts (BYYEARDAY, BYSETPOS, …) are ignored — Google Calendar
 * does not emit them for everyday events.
 */
data class RecurrenceRule(
    val freq: Freq,
    val interval: Int = 1,
    val count: Int? = null,
    val untilUtcMillis: Long? = null,
    val byDay: List<ByDay> = emptyList(),
    val byMonthDay: List<Int> = emptyList(),
    val byMonth: List<Int> = emptyList(),
    val wkst: DayOfWeek = DayOfWeek.MONDAY,
) {
    enum class Freq { DAILY, WEEKLY, MONTHLY, YEARLY }

    /** BYDAY entry: a weekday plus an optional ordinal (MONTHLY/YEARLY only). */
    data class ByDay(val ordinal: Int?, val day: DayOfWeek)

    companion object {
        /** `true` when [rrule] can be parsed. */
        fun isValid(rrule: String?): Boolean = !rrule.isNullOrBlank() &&
            runCatching { RecurrenceRuleParser.parse(rrule) }.isSuccess
    }
}

object RecurrenceRuleParser {

    private val DAY_CODES = mapOf(
        "MO" to DayOfWeek.MONDAY, "TU" to DayOfWeek.TUESDAY, "WE" to DayOfWeek.WEDNESDAY,
        "TH" to DayOfWeek.THURSDAY, "FR" to DayOfWeek.FRIDAY, "SA" to DayOfWeek.SATURDAY,
        "SU" to DayOfWeek.SUNDAY,
    )

    private val BY_DAY = Regex("""^(-?\d+)?(MO|TU|WE|TH|FR|SA|SU)$""")

    /**
     * Parses an RRULE string (`FREQ=…;INTERVAL=…`, optionally `RRULE:`-prefixed).
     * @throws IllegalArgumentException on a missing/unknown FREQ or malformed values.
     */
    fun parse(rrule: String): RecurrenceRule {
        var text = rrule.trim()
        if (text.startsWith("RRULE:", ignoreCase = true)) text = text.substring(6)
        if (text.startsWith("RRULE;", ignoreCase = true)) text = text.substring(6)

        var freq: RecurrenceRule.Freq? = null
        var interval = 1
        var count: Int? = null
        var until: Long? = null
        val byDay = mutableListOf<RecurrenceRule.ByDay>()
        val byMonthDay = mutableListOf<Int>()
        val byMonth = mutableListOf<Int>()
        var wkst = DayOfWeek.MONDAY

        for (part in text.split(';')) {
            if (part.isBlank()) continue
            val eq = part.indexOf('=')
            require(eq > 0) { "Malformed RRULE part: $part" }
            val key = part.substring(0, eq).trim().uppercase()
            val value = part.substring(eq + 1).trim()
            when (key) {
                "FREQ" -> freq = RecurrenceRule.Freq.valueOf(value.uppercase())
                "INTERVAL" -> interval = value.toInt().coerceAtLeast(1)
                "COUNT" -> count = value.toInt().coerceAtLeast(1)
                "UNTIL" -> until = Rfc5545.parseDateTime(value)
                    ?: throw DateTimeParseException("UNTIL", value, 0)
                "BYDAY" -> value.split(',').filter { it.isNotBlank() }.forEach { token ->
                    val m = BY_DAY.matchEntire(token.trim().uppercase())
                        ?: throw IllegalArgumentException("Bad BYDAY: $token")
                    val ordinal = m.groupValues[1].takeIf { it.isNotEmpty() }?.toInt()
                    byDay += RecurrenceRule.ByDay(ordinal, DAY_CODES.getValue(m.groupValues[2]))
                }
                "BYMONTHDAY" -> value.split(',').filter { it.isNotBlank() }
                    .forEach { byMonthDay += it.trim().toInt() }
                "BYMONTH" -> value.split(',').filter { it.isNotBlank() }
                    .forEach { byMonth += it.trim().toInt() }
                "WKST" -> wkst = DAY_CODES[value.uppercase()] ?: DayOfWeek.MONDAY
                else -> { /* unsupported part — ignored by design */ }
            }
        }
        requireNotNull(freq) { "RRULE missing FREQ: $rrule" }
        require(count == null || until == null) { "RRULE cannot have both COUNT and UNTIL" }
        return RecurrenceRule(
            freq = freq, interval = interval, count = count, untilUtcMillis = until,
            byDay = byDay, byMonthDay = byMonthDay, byMonth = byMonth, wkst = wkst,
        )
    }
}
