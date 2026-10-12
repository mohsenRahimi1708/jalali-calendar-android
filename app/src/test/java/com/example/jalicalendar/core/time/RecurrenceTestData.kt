package com.example.jalicalendar.core.time

/**
 * Expansion expectations generated from python-dateutil `rrulestr`
 * (DAILY/WEEKLY/MONTHLY/YEARLY, INTERVAL, COUNT, UNTIL, BYDAY, BYMONTHDAY).
 * Regenerate with tools/gen_ref_data.py if the expander changes.
 */
object RecurrenceTestData {
    data class Case(
        val rrule: String,
        val startUtcMillis: Long,
        val zone: String,
        val expected: List<Long>,
    )

    val CASES: List<Case> = listOf(
        Case(
            rrule = "FREQ=DAILY;INTERVAL=3;COUNT=6",
            startUtcMillis = 1767603600000L,
            zone = "UTC",
            expected = listOf(1767603600000L, 1767862800000L, 1768122000000L, 1768381200000L, 1768640400000L, 1768899600000L),
        ),
        Case(
            rrule = "FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR;COUNT=8",
            startUtcMillis = 1767607200000L,
            zone = "UTC",
            expected = listOf(1767607200000L, 1767780000000L, 1767952800000L, 1768816800000L, 1768989600000L, 1769162400000L, 1770026400000L, 1770199200000L),
        ),
        Case(
            rrule = "FREQ=MONTHLY;COUNT=5",
            startUtcMillis = 1769846400000L,
            zone = "UTC",
            expected = listOf(1769846400000L, 1774944000000L, 1780214400000L, 1785484800000L, 1788163200000L),
        ),
        Case(
            rrule = "FREQ=MONTHLY;BYDAY=1FR;COUNT=4",
            startUtcMillis = 1767333600000L,
            zone = "UTC",
            expected = listOf(1767333600000L, 1770357600000L, 1772776800000L, 1775196000000L),
        ),
        Case(
            rrule = "FREQ=MONTHLY;INTERVAL=2;BYMONTHDAY=15,-1;COUNT=6",
            startUtcMillis = 1768478400000L,
            zone = "UTC",
            expected = listOf(1768478400000L, 1769860800000L, 1773576000000L, 1774958400000L, 1778846400000L, 1780228800000L),
        ),
        Case(
            rrule = "FREQ=YEARLY;BYMONTH=3;BYMONTHDAY=20;COUNT=3",
            startUtcMillis = 1773964800000L,
            zone = "UTC",
            expected = listOf(1773964800000L, 1805500800000L, 1837123200000L),
        ),
        Case(
            rrule = "FREQ=WEEKLY;BYDAY=TU;UNTIL=20260210T235959Z",
            startUtcMillis = 1767690000000L,
            zone = "UTC",
            expected = listOf(1767690000000L, 1768294800000L, 1768899600000L, 1769504400000L, 1770109200000L, 1770714000000L),
        ),
        Case(
            rrule = "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;COUNT=10",
            startUtcMillis = 1767250800000L,
            zone = "UTC",
            expected = listOf(1767250800000L, 1767337200000L, 1767596400000L, 1767682800000L, 1767769200000L, 1767855600000L, 1767942000000L, 1768201200000L, 1768287600000L, 1768374000000L),
        ),
        Case(
            rrule = "FREQ=DAILY;BYDAY=MO,SA;COUNT=8",
            startUtcMillis = 1770499800000L,
            zone = "UTC",
            expected = listOf(1770499800000L, 1770672600000L, 1771104600000L, 1771277400000L, 1771709400000L, 1771882200000L, 1772314200000L, 1772487000000L),
        ),
        Case(
            rrule = "FREQ=YEARLY;COUNT=4",
            startUtcMillis = 1718477100000L,
            zone = "UTC",
            expected = listOf(1718477100000L, 1750013100000L, 1781549100000L, 1813085100000L),
        ),
    )
}
