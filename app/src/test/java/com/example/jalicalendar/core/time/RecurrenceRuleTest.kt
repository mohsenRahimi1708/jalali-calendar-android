package com.example.jalicalendar.core.time

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class RecurrenceRuleTest {

    @Test
    fun `parses a full rule`() {
        val r = RecurrenceRuleParser.parse(
            "RRULE:FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,2FR;COUNT=10;WKST=SU",
        )
        assertThat(r.freq).isEqualTo(RecurrenceRule.Freq.WEEKLY)
        assertThat(r.interval).isEqualTo(2)
        assertThat(r.count).isEqualTo(10)
        assertThat(r.untilUtcMillis).isNull()
        assertThat(r.wkst).isEqualTo(DayOfWeek.SUNDAY)
        assertThat(r.byDay).containsExactly(
            RecurrenceRule.ByDay(null, DayOfWeek.MONDAY),
            RecurrenceRule.ByDay(null, DayOfWeek.WEDNESDAY),
            RecurrenceRule.ByDay(2, DayOfWeek.FRIDAY),
        ).inOrder()
    }

    @Test
    fun `parses UNTIL in all supported formats`() {
        val compact = RecurrenceRuleParser.parse("FREQ=DAILY;UNTIL=20260210T235959Z").untilUtcMillis!!
        val iso = RecurrenceRuleParser.parse("FREQ=DAILY;UNTIL=2026-02-10T23:59:59Z").untilUtcMillis!!
        assertThat(compact).isEqualTo(iso)

        val dateOnly = RecurrenceRuleParser.parse("FREQ=DAILY;UNTIL=20260210").untilUtcMillis!!
        assertThat(dateOnly).isEqualTo(TimeUtils.utcStartOfDay(LocalDate.of(2026, 2, 10)))
    }

    @Test
    fun `parses BYMONTHDAY with negative values and BYMONTH lists`() {
        val r = RecurrenceRuleParser.parse("FREQ=MONTHLY;BYMONTHDAY=15,-1;BYMONTH=1,7")
        assertThat(r.byMonthDay).containsExactly(15, -1).inOrder()
        assertThat(r.byMonth).containsExactly(1, 7).inOrder()
    }

    @Test
    fun `defaults follow RFC 5545`() {
        val r = RecurrenceRuleParser.parse("FREQ=DAILY")
        assertThat(r.interval).isEqualTo(1)
        assertThat(r.count).isNull()
        assertThat(r.untilUtcMillis).isNull()
        assertThat(r.byDay).isEmpty()
        assertThat(r.wkst).isEqualTo(DayOfWeek.MONDAY)
    }

    @Test
    fun `invalid rules are rejected and flagged`() {
        assertThrows(IllegalArgumentException::class.java) { RecurrenceRuleParser.parse("INTERVAL=2") }
        assertThrows(IllegalArgumentException::class.java) { RecurrenceRuleParser.parse("FREQ=SOMETIMES") }
        assertThrows(IllegalArgumentException::class.java) {
            RecurrenceRuleParser.parse("FREQ=DAILY;COUNT=5;UNTIL=20260101T000000Z")
        }
        assertThat(RecurrenceRule.isValid(null)).isFalse()
        assertThat(RecurrenceRule.isValid("")).isFalse()
        assertThat(RecurrenceRule.isValid("FREQ=WEEKLY;BYDAY=MO")).isTrue()
    }

    @Test
    fun `unsupported parts are ignored instead of failing`() {
        val r = RecurrenceRuleParser.parse("FREQ=MONTHLY;BYSETPOS=-1;BYHOUR=9;BYDAY=SU")
        assertThat(r.freq).isEqualTo(RecurrenceRule.Freq.MONTHLY)
        assertThat(r.byDay).containsExactly(RecurrenceRule.ByDay(null, DayOfWeek.SUNDAY))
    }
}
