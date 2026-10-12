package com.example.jalicalendar.core.time

import java.time.LocalDate

/**
 * Persian (Jalali / Solar Hijri) ↔ Gregorian conversion.
 *
 * Port of the Borkowski 33-year-cycle algorithm with a historical leap-cycle
 * breaks table, taken verbatim from jalaali-js v2.0.1
 * (https://github.com/jalaali/jalaali-js, MIT license) — validated there
 * against the official Iranian calendar.
 *
 * Supported Jalali years: [-61, 3177]. All conversions go through Julian Day
 * Numbers, so round-trips are exact for the whole supported range.
 */
object JalaliConverter {

    /** Years that begin a new leap cycle (jalaali-js BREAKS table). */
    private val BREAKS = intArrayOf(
        -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181,
        1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178,
    )

    const val MIN_YEAR = -61
    const val MAX_YEAR = 3177

    /** Julian Day Number of 1970-01-01 (Unix epoch date). */
    private const val EPOCH_JDN = 2440588

    /** Truncating division — JavaScript's `~~(a / b)`. */
    private fun div(a: Int, b: Int): Int = a / b

    /** Mathematical modulo (always non-negative for positive b). */
    private fun mod(a: Int, b: Int): Int = a - (a / b) * b

    private data class JalCalCore(val gy: Int, val march: Int, val jump: Int, val n: Int)

    private data class JalCal(val leap: Int, val gy: Int, val march: Int)

    private fun requireYear(jy: Int) =
        require(jy in MIN_YEAR..MAX_YEAR) { "Invalid Jalaali year $jy: must be in [$MIN_YEAR, $MAX_YEAR]" }

    /**
     * Core of the Borkowski algorithm: locate [jy] inside the cycle table and
     * compute the Gregorian date of Farvardin 1 of that year.
     */
    private fun jalCalCore(jy: Int): JalCalCore {
        requireYear(jy)
        val gy = jy + 621
        var leapJ = -14
        var jp = BREAKS[0]
        var jm = 0
        var jump = 0
        for (i in 1 until BREAKS.size) {
            jm = BREAKS[i]
            jump = jm - jp
            if (jy < jm) break
            leapJ = leapJ + div(jump, 33) * 8 + div(mod(jump, 33), 4)
            jp = jm
        }
        val n = jy - jp
        leapJ += div(n, 33) * 8 + div(mod(n, 33) + 3, 4)
        if (mod(jump, 33) == 4 && jump - n == 4) leapJ += 1
        val leapG = div(gy, 4) - div((div(gy, 100) + 1) * 3, 4) - 150
        return JalCalCore(gy, 20 + leapJ - leapG, jump, n)
    }

    private fun jalCal(jy: Int): JalCal {
        val core = jalCalCore(jy)
        return JalCal(leap = leapFromCycle(core.jump, core.n), gy = core.gy, march = core.march)
    }

    /**
     * Years since the last leap year within the 33-year cycle (0..4);
     * 0 means [jy] is a leap year.
     */
    private fun leapFromCycle(jump: Int, n: Int): Int {
        var adjusted = n
        if (jump - n < 6) adjusted = n - jump + div(jump + 4, 33) * 33
        var leap = mod(mod(adjusted + 1, 33) - 1, 4)
        if (leap == -1) leap = 4
        return leap
    }

    private fun jalCalLeap(jy: Int): Int {
        requireYear(jy)
        var jp = BREAKS[0]
        var jm = 0
        var jump = 0
        for (i in 1 until BREAKS.size) {
            jm = BREAKS[i]
            jump = jm - jp
            if (jy < jm) break
            jp = jm
        }
        return leapFromCycle(jump, jy - jp)
    }

    /** True when the Jalali year has 366 days (Esfand has 30 days). */
    fun isLeapYear(jy: Int): Boolean = jalCalLeap(jy) == 0

    /** Days in a Jalali month: 1–6 → 31, 7–11 → 30, 12 → 29/30 (leap). */
    fun monthLength(jy: Int, jm: Int): Int = when {
        jm <= 6 -> 31
        jm <= 11 -> 30
        else -> if (isLeapYear(jy)) 30 else 29
    }

    fun isValidDate(jy: Int, jm: Int, jd: Int): Boolean =
        jy in MIN_YEAR..MAX_YEAR && jm in 1..12 && jd in 1..monthLength(jy, jm)

    /** Gregorian date → Julian Day Number. */
    private fun g2d(gy: Int, gm: Int, gd: Int): Int {
        var d = div((gy + div(gm - 8, 6) + 100100) * 1461, 4) +
            div(153 * mod(gm + 9, 12) + 2, 5) + gd - 34840408
        d -= div(div(gy + 100100 + div(gm - 8, 6), 100) * 3, 4) - 752
        return d
    }

    /** Julian Day Number → Gregorian (y, m, d). */
    private fun d2g(jdn: Int): Triple<Int, Int, Int> {
        var j = 4 * jdn + 139361631
        j += div(div(4 * jdn + 183187720, 146097) * 3, 4) * 4 - 3908
        val i = div(mod(j, 1461), 4) * 5 + 308
        val gd = div(mod(i, 153), 5) + 1
        val gm = mod(div(i, 153), 12) + 1
        val gy = div(j, 1461) - 100100 + div(8 - gm, 6)
        return Triple(gy, gm, gd)
    }

    /** Jalali date → Julian Day Number. */
    private fun j2d(jy: Int, jm: Int, jd: Int): Int {
        val r = jalCalCore(jy)
        return g2d(r.gy, 3, r.march) + (jm - 1) * 31 - div(jm, 7) * (jm - 7) + jd - 1
    }

    private val FIRST_JDN = j2d(MIN_YEAR, 1, 1)
    private val LAST_JDN = j2d(MAX_YEAR, 12, monthLength(MAX_YEAR, 12))

    /** Julian Day Number → Jalali date. */
    private fun d2j(jdn: Int): JalaliDate {
        require(jdn in FIRST_JDN..LAST_JDN) { "Julian Day $jdn outside supported range" }
        val gy = d2g(jdn).first
        var jy = minOf(gy - 621, MAX_YEAR)
        val r = jalCal(jy)
        var k = jdn - g2d(r.gy, 3, r.march)
        if (k >= 0) {
            if (k <= 185) return JalaliDate(jy, 1 + div(k, 31), mod(k, 31) + 1)
            k -= 186
        } else {
            jy -= 1
            k += 179
            if (r.leap == 1) k += 1
        }
        return JalaliDate(jy, 7 + div(k, 30), mod(k, 30) + 1)
    }

    // ------------------------------------------------------------------ public

    fun toJalali(gy: Int, gm: Int, gd: Int): JalaliDate = d2j(g2d(gy, gm, gd))

    fun toJalali(date: LocalDate): JalaliDate = d2j((date.toEpochDay() + EPOCH_JDN).toInt())

    fun toGregorian(jy: Int, jm: Int, jd: Int): LocalDate {
        require(isValidDate(jy, jm, jd)) { "Invalid Jalali date: $jy-$jm-$jd" }
        return LocalDate.ofEpochDay(j2d(jy, jm, jd).toLong() - EPOCH_JDN)
    }

    fun toGregorian(date: JalaliDate): LocalDate = toGregorian(date.year, date.month, date.day)

    /** Days since 1970-01-01 for this Jalali date. */
    fun toEpochDay(date: JalaliDate): Long = j2d(date.year, date.month, date.day).toLong() - EPOCH_JDN

    fun fromEpochDay(epochDay: Long): JalaliDate = d2j((epochDay + EPOCH_JDN).toInt())
}
