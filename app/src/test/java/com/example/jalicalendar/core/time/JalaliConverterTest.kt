package com.example.jalicalendar.core.time

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class JalaliConverterTest {

    private fun crossCheckRows(): List<Pair<JalaliDate, LocalDate>> =
        checkNotNull(javaClass.classLoader.getResourceAsStream("jalali_crosscheck.csv")).use { stream ->
            stream.bufferedReader().readLines().filter { it.isNotBlank() }.map { line ->
                val v = line.split(',').map { it.toInt() }
                JalaliDate(v[0], v[1], v[2]) to LocalDate.of(v[3], v[4], v[5])
            }
        }

    @Test
    fun `documented anchor dates convert correctly`() {
        // Epoch anchor: Unix epoch = 11 Dey 1348
        assertThat(JalaliConverter.toJalali(LocalDate.of(1970, 1, 1)))
            .isEqualTo(JalaliDate(1348, 10, 11))
        // 22 Bahman 1357 = 11 Feb 1979
        assertThat(JalaliConverter.toJalali(LocalDate.of(1979, 2, 11)))
            .isEqualTo(JalaliDate(1357, 11, 22))
        // Nowruz 1403 = 20 Mar 2024 ; Nowruz 1379 = 20 Mar 2000
        assertThat(JalaliConverter.toJalali(LocalDate.of(2024, 3, 20)))
            .isEqualTo(JalaliDate(1403, 1, 1))
        assertThat(JalaliConverter.toJalali(LocalDate.of(2000, 3, 20)))
            .isEqualTo(JalaliDate(1379, 1, 1))
        // jalaali-js documented example
        assertThat(JalaliConverter.toJalali(LocalDate.of(2016, 4, 11)))
            .isEqualTo(JalaliDate(1395, 1, 23))
    }

    @Test
    fun `matches jdatetime reference table in both directions`() {
        val rows = crossCheckRows()
        assertThat(rows.size).isAtLeast(1000)
        for ((jalali, gregorian) in rows) {
            assertThat(JalaliConverter.toGregorian(jalali)).isEqualTo(gregorian)
            assertThat(JalaliConverter.toJalali(gregorian)).isEqualTo(jalali)
        }
    }

    @Test
    fun `every day of dense years round-trips and advances by exactly one day`() {
        for (startYear in listOf(1990, 1992, 2024)) {
            var date = LocalDate.of(startYear, 1, 1)
            val end = LocalDate.of(startYear + 1, 1, 1)
            var prevEpoch = JalaliConverter.toJalali(date).toEpochDay() - 1
            while (date < end) {
                val j = JalaliConverter.toJalali(date)
                assertThat(JalaliConverter.toGregorian(j)).isEqualTo(date)
                assertThat(j.toEpochDay()).isEqualTo(prevEpoch + 1)
                prevEpoch = j.toEpochDay()
                date = date.plusDays(1)
            }
        }
    }

    @Test
    fun `leap years follow the 33-year cycle`() {
        // Known Jalali leap years
        for (y in listOf(1375, 1379, 1383, 1387, 1391, 1395, 1399, 1403)) {
            assertThat(JalaliConverter.isLeapYear(y)).isTrue()
            assertThat(JalaliConverter.monthLength(y, 12)).isEqualTo(30)
        }
        for (y in listOf(1394, 1396, 1400, 1402, 1404, 1405)) {
            assertThat(JalaliConverter.isLeapYear(y)).isFalse()
            assertThat(JalaliConverter.monthLength(y, 12)).isEqualTo(29)
        }
    }

    @Test
    fun `month lengths are 31-31-31-31-31-31-30-30-30-30-30-x`() {
        val expected = listOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30)
        for (m in 1..11) {
            assertThat(JalaliConverter.monthLength(1404, m)).isEqualTo(expected[m - 1])
        }
    }

    @Test
    fun `invalid dates are rejected`() {
        assertThrows(IllegalArgumentException::class.java) { JalaliDate(1404, 12, 30) }
        assertThrows(IllegalArgumentException::class.java) { JalaliDate(1404, 13, 1) }
        assertThrows(IllegalArgumentException::class.java) { JalaliDate(1404, 0, 1) }
        assertThat(JalaliConverter.isValidDate(1403, 12, 30)).isTrue()
        assertThat(JalaliConverter.isValidDate(1404, 12, 30)).isFalse()
    }

    @Test
    fun `jalali date helpers work`() {
        val d = JalaliDate(1405, 7, 18) // = 2026-10-10
        assertThat(d.formatIso()).isEqualTo("1405-07-18")
        assertThat(d.plusDays(1)).isEqualTo(JalaliDate(1405, 7, 19))
        assertThat(d.plusDays(40)).isEqualTo(JalaliDate(1405, 8, 28))
        assertThat(JalaliDate.monthName(1, persian = true)).isEqualTo("فروردین")
        assertThat(JalaliDate.monthName(12, persian = false)).isEqualTo("Esfand")
        assertThat(JalaliDate.today(java.time.ZoneId.of("Asia/Tehran")).year).isAtLeast(1404)
    }
}
