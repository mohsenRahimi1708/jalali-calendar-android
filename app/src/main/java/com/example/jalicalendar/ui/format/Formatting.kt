package com.example.jalicalendar.ui.format

import com.example.jalicalendar.core.time.JalaliConverter
import com.example.jalicalendar.core.time.JalaliDate
import com.example.jalicalendar.core.time.PersianDigits
import com.example.jalicalendar.core.time.TimeUtils
import com.example.jalicalendar.domain.model.AppSettings
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Display formatting. Every function takes the current [AppSettings] so
 * digits (Persian/Latin), clock format (24h/12h) and the display time zone
 * apply consistently; parsing paths always stay ASCII.
 */
object Formatting {

    private val hhmm24 = DateTimeFormatter.ofPattern("HH:mm", Locale.US)
    private val weekdayFa = listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه")
    private val weekdayEn = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")

    /** Seven weekday headers starting at the configured week start. */
    fun weekdayHeaders(weekStart: com.example.jalicalendar.domain.model.WeekStart, persian: Boolean): List<String> {
        val startIdx = when (weekStart) {
            com.example.jalicalendar.domain.model.WeekStart.SATURDAY -> 0
            com.example.jalicalendar.domain.model.WeekStart.SUNDAY -> 1
            com.example.jalicalendar.domain.model.WeekStart.MONDAY -> 2
        }
        val base = if (persian) weekdayFa else weekdayEn
        return (0 until 7).map { base[(startIdx + it) % 7] }
    }

    /** Monday-first index of [date] within a week starting at [weekStart]. */
    fun weekIndexOf(date: java.time.LocalDate, weekStart: com.example.jalicalendar.domain.model.WeekStart): Int {
        val startDow = when (weekStart) {
            com.example.jalicalendar.domain.model.WeekStart.SATURDAY -> java.time.DayOfWeek.SATURDAY
            com.example.jalicalendar.domain.model.WeekStart.SUNDAY -> java.time.DayOfWeek.SUNDAY
            com.example.jalicalendar.domain.model.WeekStart.MONDAY -> java.time.DayOfWeek.MONDAY
        }
        return ((date.dayOfWeek.value - startDow.value) % 7 + 7) % 7
    }
    private val hmm12 = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    /** `18:30` / `6:30 PM` — localized to Persian digits when enabled. */
    fun time(epochMillis: Long, settings: AppSettings): String {
        val zone = ZoneId.of(settings.timeZone)
        val local = Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalTime()
        val raw = when {
            settings.use24HourFormat -> local.format(hhmm24)
            else -> local.format(hmm12).replace("AM", "ق.ظ").replace("PM", "ب.ظ")
        }
        return PersianDigits.localize(raw, settings.persianDigits)
    }

    /** Civil date of an instant in the display zone. */
    fun localDate(epochMillis: Long, settings: AppSettings): LocalDate =
        TimeUtils.localDateOf(epochMillis, ZoneId.of(settings.timeZone))

    /** Jalali date of an instant in the display zone. */
    fun jalaliDate(epochMillis: Long, settings: AppSettings): JalaliDate =
        JalaliConverter.toJalali(localDate(epochMillis, settings))

    /** Jalali day-of-month number. */
    fun dayNumber(jalaliDay: Int, settings: AppSettings): String =
        PersianDigits.localizeNumber(jalaliDay, settings.persianDigits)

    /** `مهر ۱۴۰۵` / `Mehr 1405`. */
    fun monthYear(jy: Int, jm: Int, settings: AppSettings): String {
        val name = JalaliDate.monthName(jm, settings.persianDigits)
        val year = PersianDigits.localizeNumber(jy, settings.persianDigits)
        return if (settings.persianDigits) "$name $year" else "$name $year"
    }

    /** `شنبه ۱۸ مهر ۱۴۰۵` / `Sat 18 Mehr 1405` — used in headers and a11y. */
    fun fullDate(jalali: JalaliDate, settings: AppSettings, persianWeekday: Boolean): String {
        val day = PersianDigits.localizeNumber(jalali.day, settings.persianDigits)
        val month = JalaliDate.monthName(jalali.month, settings.persianDigits)
        val year = PersianDigits.localizeNumber(jalali.year, settings.persianDigits)
        val dow = if (persianWeekday) {
            when (jalali.dayOfWeek()) {
                java.time.DayOfWeek.SATURDAY -> "شنبه"
                java.time.DayOfWeek.SUNDAY -> "یکشنبه"
                java.time.DayOfWeek.MONDAY -> "دوشنبه"
                java.time.DayOfWeek.TUESDAY -> "سه‌شنبه"
                java.time.DayOfWeek.WEDNESDAY -> "چهارشنبه"
                java.time.DayOfWeek.THURSDAY -> "پنجشنبه"
                java.time.DayOfWeek.FRIDAY -> "جمعه"
            }
        } else {
            jalali.dayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, Locale.US)
        }
        return "$dow $day $month $year"
    }

    /** Relative day label: Today / Tomorrow / Yesterday / `18 Mehr`. */
    fun dayLabel(
        target: LocalDate,
        today: LocalDate,
        strings: com.example.jalicalendar.ui.Strings,
        settings: AppSettings,
    ): String = when (target) {
        today -> strings.today
        today.plusDays(1) -> strings.tomorrow
        today.minusDays(1) -> strings.yesterday
        else -> {
            val j = JalaliConverter.toJalali(target)
            val day = PersianDigits.localizeNumber(j.day, settings.persianDigits)
            val month = JalaliDate.monthName(j.month, settings.persianDigits)
            "$day $month"
        }
    }
}
