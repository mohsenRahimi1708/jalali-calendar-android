package com.example.jalicalendar.core.time

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class TimeUtilsTest {

    private val tehran = ZoneId.of("Asia/Tehran")

    @Test
    fun `all-day anchors are utc midnight and span exactly one day`() {
        val d = LocalDate.of(2026, 1, 5)
        val start = TimeUtils.utcStartOfDay(d)
        val end = TimeUtils.utcEndOfDay(d)
        assertThat(start).isEqualTo(TimeUtils.toUtcMillis(d, LocalTime.MIDNIGHT, ZoneOffset.UTC))
        assertThat(end - start).isEqualTo(86_400_000L)
        assertThat(TimeUtils.utcStartOfDay(LocalDate.of(1970, 1, 1))).isEqualTo(0L)
        // Round-trip on the date axis
        assertThat(TimeUtils.localDateOf(start, ZoneOffset.UTC)).isEqualTo(d)
    }

    @Test
    fun `local day boundaries respect the zone offset`() {
        val instant = TimeUtils.toUtcMillis(LocalDate.of(2026, 1, 5), LocalTime.of(9, 0), tehran)
        assertThat(TimeUtils.localDateOf(instant, tehran)).isEqualTo(LocalDate.of(2026, 1, 5))
        // Tehran is UTC+3:30 → 09:00 local = 05:30 UTC
        assertThat(instant % 86_400_000L).isEqualTo(5L * 3_600_000L + 30 * 60_000L)
        val dayStart = TimeUtils.startOfLocalDay(instant, tehran)
        val dayEnd = TimeUtils.endOfLocalDay(instant, tehran)
        assertThat(dayEnd - dayStart).isEqualTo(86_400_000L)
        assertThat(TimeUtils.localDateOf(dayStart, tehran)).isEqualTo(LocalDate.of(2026, 1, 5))
    }

    @Test
    fun `zone offsets capture historical DST in Tehran`() {
        // Standard time
        assertThat(TimeUtils.zoneOffsetMinutes(TimeUtils.toUtcMillis(LocalDate.of(2021, 1, 15), LocalTime.NOON, tehran), tehran)).isEqualTo(210)
        // DST (+4:30)
        assertThat(TimeUtils.zoneOffsetMinutes(TimeUtils.toUtcMillis(LocalDate.of(2021, 7, 15), LocalTime.NOON, tehran), tehran)).isEqualTo(270)
        // DST abolished since 2022
        assertThat(TimeUtils.zoneOffsetMinutes(TimeUtils.toUtcMillis(LocalDate.of(2025, 7, 15), LocalTime.NOON, tehran), tehran)).isEqualTo(210)
    }

    @Test
    fun `jalali date bridging stays consistent with jdatetime anchors`() {
        // 2026-10-10 = 1405-07-18 (Nowruz 1405 = 2026-03-21; equinox 18:16 Tehran)
        val d = LocalDate.of(2026, 10, 10)
        val j = JalaliConverter.toJalali(d)
        assertThat(j).isEqualTo(JalaliDate(1405, 7, 18))
        assertThat(JalaliConverter.toGregorian(j)).isEqualTo(d)
        assertThat(JalaliConverter.toEpochDay(j)).isEqualTo(d.toEpochDay())
    }
}
