package com.example.jalicalendar.core.time

import com.example.jalicalendar.domain.model.Event
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Expansion correctness. The table-driven cases are cross-checked against
 * python-dateutil (see RecurrenceTestData); DST cases are hand-verified
 * against Asia/Tehran (DST until 2022) and Europe/Berlin transitions.
 */
class RecurrenceExpanderTest {

    private val utc = ZoneId.of("UTC")

    private fun expandWide(rrule: String, startUtcMillis: Long, zone: ZoneId = utc): List<Long> =
        RecurrenceExpander.expand(
            rule = RecurrenceRuleParser.parse(rrule),
            startUtcMillis = startUtcMillis,
            zone = zone,
            rangeStartUtcMillis = startUtcMillis - 86_400_000L,
            rangeEndUtcMillis = startUtcMillis + 10L * 365 * 86_400_000L,
        )

    @Test
    fun `matches python-dateutil for all generated cases`() {
        for (case in RecurrenceTestData.CASES) {
            val actual = expandWide(case.rrule, case.startUtcMillis, ZoneId.of(case.zone))
            assertThat(actual).isEqualTo(case.expected)
        }
    }

    @Test
    fun `results are limited to the requested range`() {
        val start = RecurrenceTestData.CASES[0].startUtcMillis
        val all = expandWide("FREQ=DAILY;INTERVAL=3;COUNT=6", start)
        val windowStart = all[2]
        val windowEnd = all[4]
        val filtered = RecurrenceExpander.expand(
            rule = RecurrenceRuleParser.parse("FREQ=DAILY;INTERVAL=3;COUNT=6"),
            startUtcMillis = start,
            zone = utc,
            rangeStartUtcMillis = windowStart,
            rangeEndUtcMillis = windowEnd,
        )
        assertThat(filtered).isEqualTo(all.filter { it in windowStart until windowEnd })
        assertThat(filtered).containsExactly(all[2], all[3]).inOrder()
    }

    @Test
    fun `count is consumed from DTSTART even when the range starts later`() {
        val start = RecurrenceTestData.CASES[0].startUtcMillis
        val all = expandWide("FREQ=DAILY;INTERVAL=3;COUNT=6", start)
        val late = RecurrenceExpander.expand(
            rule = RecurrenceRuleParser.parse("FREQ=DAILY;INTERVAL=3;COUNT=6"),
            startUtcMillis = start,
            zone = utc,
            rangeStartUtcMillis = all[4],
            rangeEndUtcMillis = start + 10L * 365 * 86_400_000L,
        )
        // occurrences #5 and #6 remain — later occurrences are not renumbered
        assertThat(late).isEqualTo(all.subList(4, 6))
    }

    @Test
    fun `invalid rule yields DTSTART only`() {
        val rule = RecurrenceRule(freq = RecurrenceRule.Freq.DAILY, interval = 0)
        // interval is coerced to >=1 by the parser; construct a pathological count directly
        val expanded = RecurrenceExpander.expand(
            rule = rule.copy(count = 0), // count<=1 → DTSTART only
            startUtcMillis = 0L,
            zone = utc,
            rangeStartUtcMillis = -1L,
            rangeEndUtcMillis = Long.MAX_VALUE / 2,
        )
        assertThat(expanded).containsExactly(0L)
    }

    @Test
    fun `Tehran DST keeps wall-clock time and shifts UTC offset`() {
        val tehran = ZoneId.of("Asia/Tehran")
        // DST in Iran 2021: Mar 21 → Sep 21 (UTC+3:30 → UTC+4:30)
        val summer = expandWide("FREQ=DAILY;COUNT=4", toUtc(2021, 6, 1, 8, 0, tehran), tehran)
        for (instant in summer) {
            assertThat(TimeUtils.localTimeOf(instant, tehran)).isEqualTo(LocalTime.of(8, 0))
            assertThat(TimeUtils.zoneOffsetMinutes(instant, tehran)).isEqualTo(270)
        }

        val winter = expandWide("FREQ=DAILY;COUNT=4", toUtc(2021, 1, 10, 8, 0, tehran), tehran)
        for (instant in winter) {
            assertThat(TimeUtils.localTimeOf(instant, tehran)).isEqualTo(LocalTime.of(8, 0))
            assertThat(TimeUtils.zoneOffsetMinutes(instant, tehran)).isEqualTo(210)
        }
    }

    @Test
    fun `Berlin spring-forward gap shifts the nonexistent local time forward`() {
        val berlin = ZoneId.of("Europe/Berlin")
        // 2026-03-29: 02:00 → 03:00. Daily 02:30 occurrences:
        // Mar 27 02:30 CET, Mar 28 02:30 CET, Mar 29 02:30 doesn't exist → 03:30 CEST, Mar 30 02:30 CEST
        val start = toUtc(2026, 3, 27, 2, 30, berlin)
        val occ = expandWide("FREQ=DAILY;COUNT=4", start, berlin)
        val localTimes = occ.map { TimeUtils.localTimeOf(it, berlin) }
        assertThat(localTimes).containsExactly(
            LocalTime.of(2, 30), LocalTime.of(2, 30), LocalTime.of(3, 30), LocalTime.of(2, 30),
        ).inOrder()
    }

    @Test
    fun `exdate removes and rdate adds occurrences`() {
        val start = toUtc(2026, 1, 5, 9, 0, utc)
        val event = Event(
            id = "e1", calendarId = "c1", title = "Standup",
            startUtcMillis = start, endUtcMillis = start + 1_800_000L,
            rrule = "FREQ=DAILY;COUNT=5",
            rdate = "20260120T090000Z",
            exdate = "20260107T090000Z,2026-01-08T09:00:00Z",
        )
        val occ = OccurrenceExpander.occurrences(
            event,
            rangeStartUtcMillis = start - 86_400_000L,
            rangeEndUtcMillis = start + 60L * 86_400_000L,
            zone = utc,
        )
        val base = expandWide("FREQ=DAILY;COUNT=5", start)
        val expected = (base.filterNot {
            it == base[2] || it == base[3] // 7 and 8 Jan excluded
        } + (TimeUtils.utcStartOfDay(LocalDate.of(2026, 1, 20)) + 9 * 3_600_000L)).sorted()
        assertThat(occ).isEqualTo(expected)
        assertThat(occ).hasSize(4) // 5 base − 2 excluded + 1 rdate
    }

    @Test
    fun `unparseable rule degrades to a single occurrence`() {
        val event = Event(
            id = "e2", calendarId = "c1", title = "Broken",
            startUtcMillis = 1_000L, endUtcMillis = 2_000L,
            rrule = "FREQ=WHENEVER;BOGUS",
        )
        val occ = OccurrenceExpander.occurrences(event, 0L, Long.MAX_VALUE / 2, utc)
        assertThat(occ).containsExactly(1_000L)
    }

    @Test
    fun `non-recurring event yields itself only`() {
        val event = Event(
            id = "e3", calendarId = "c1", title = "Once",
            startUtcMillis = 5_000L, endUtcMillis = 6_000L,
        )
        assertThat(OccurrenceExpander.occurrences(event, 0L, 10_000L, utc)).containsExactly(5_000L)
        assertThat(OccurrenceExpander.occurrences(event, 6_000L, 10_000L, utc)).isEmpty()
    }

    @Test
    fun `DTSTART not matching the rule still yields the saved event first`() {
        // RFC 5545 leaves this combination undefined; we keep DTSTART as the
        // first occurrence so a saved event is never hidden (documented choice).
        val start = toUtc(2026, 2, 3, 21, 30, utc) // Tuesday, rule says MO,SA
        val occ = expandWide("FREQ=DAILY;BYDAY=MO,SA;COUNT=4", start)
        assertThat(occ.first()).isEqualTo(start)
        assertThat(occ).hasSize(4)
        // Feb 4 is Wednesday; the Monday (Feb 2) is before DTSTART, so the
        // next matches are Sat Feb 7, Mon Feb 9, Sat Feb 14.
        val days = occ.drop(1).map { TimeUtils.localDateOf(it, utc).dayOfWeek }
        assertThat(days).containsExactly(
            java.time.DayOfWeek.SATURDAY, java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.SATURDAY,
        ).inOrder()
    }

    private fun toUtc(y: Int, m: Int, d: Int, h: Int, min: Int, zone: ZoneId): Long =
        TimeUtils.toUtcMillis(LocalDate.of(y, m, d), LocalTime.of(h, min), zone)
}
