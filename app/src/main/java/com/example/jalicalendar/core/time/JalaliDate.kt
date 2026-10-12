package com.example.jalicalendar.core.time

import java.util.Locale

/**
 * A civil date in the Persian (Solar Hijri) calendar: year, month (1–12), day.
 * Validated on construction — impossible dates like 1404-12-30 throw.
 */
data class JalaliDate(val year: Int, val month: Int, val day: Int) : Comparable<JalaliDate> {

    init {
        require(JalaliConverter.isValidDate(year, month, day)) {
            "Invalid Jalali date: $year-$month-$day"
        }
    }

    val isLeapYear: Boolean get() = JalaliConverter.isLeapYear(year)

    fun lengthOfMonth(): Int = JalaliConverter.monthLength(year, month)

    fun toEpochDay(): Long = JalaliConverter.toEpochDay(this)

    fun plusDays(days: Long): JalaliDate = JalaliConverter.fromEpochDay(toEpochDay() + days)

    /** Day of week of this date (ISO-8601: Monday=1 … Sunday=7). */
    fun dayOfWeek(): java.time.DayOfWeek =
        java.time.LocalDate.ofEpochDay(toEpochDay()).dayOfWeek

    /** Stable machine-readable form: `1404-07-18` (always ASCII digits). */
    fun formatIso(): String = String.format(Locale.US, "%04d-%02d-%02d", year, month, day)

    override fun compareTo(other: JalaliDate): Int =
        compareValuesBy(this, other, { it.year }, { it.month }, { it.day })

    companion object {
        val MONTH_NAMES_FA = listOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند",
        )

        val MONTH_NAMES_EN = listOf(
            "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar",
            "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand",
        )

        fun monthName(month: Int, persian: Boolean): String {
            val names = if (persian) MONTH_NAMES_FA else MONTH_NAMES_EN
            return names.getOrElse(month - 1) { "" }
        }

        fun today(zone: java.time.ZoneId = java.time.ZoneId.of("Asia/Tehran")): JalaliDate =
            JalaliConverter.toJalali(java.time.LocalDate.now(zone))
    }
}
