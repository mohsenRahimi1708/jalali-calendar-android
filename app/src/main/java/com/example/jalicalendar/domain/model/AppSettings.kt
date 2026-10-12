package com.example.jalicalendar.domain.model

/** First day of the week. Default: Saturday (Iranian convention). */
enum class WeekStart { SATURDAY, SUNDAY, MONDAY }

/** App theme mode. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** UI language; SYSTEM follows the device locale. */
enum class LanguageMode { SYSTEM, PERSIAN, ENGLISH }

/** How a local-vs-remote edit collision is resolved during sync. */
enum class ConflictPolicy { LAST_WRITE_WINS, PROMPT }

/**
 * User settings (DataStore-backed). Defaults per spec §4:
 * Asia/Tehran, week starts Saturday, 24-hour clock, Persian digits on.
 */
data class AppSettings(
    val defaultCalendarId: String? = null,
    val defaultReminderMinutes: Int = 10,
    val timeZone: String = "Asia/Tehran",
    val weekStart: WeekStart = WeekStart.SATURDAY,
    val use24HourFormat: Boolean = true,
    val persianDigits: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val language: LanguageMode = LanguageMode.SYSTEM,
    val conflictPolicy: ConflictPolicy = ConflictPolicy.LAST_WRITE_WINS,
) {
    companion object {
        val DEFAULT = AppSettings()
    }
}
