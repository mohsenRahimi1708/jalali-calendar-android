package com.example.jalicalendar.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.ConflictPolicy
import com.example.jalicalendar.domain.model.LanguageMode
import com.example.jalicalendar.domain.model.ThemeMode
import com.example.jalicalendar.domain.model.WeekStart
import com.example.jalicalendar.domain.repo.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Preferences DataStore backing [AppSettings]. Missing/unknown values fall back
 * to [AppSettings.DEFAULT], so an unknown enum string from a future version
 * degrades to the default instead of crashing.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    private object Keys {
        val DEFAULT_CALENDAR_ID = stringPreferencesKey("default_calendar_id")
        val DEFAULT_REMINDER_MINUTES = intPreferencesKey("default_reminder_minutes")
        val TIME_ZONE = stringPreferencesKey("time_zone")
        val WEEK_START = stringPreferencesKey("week_start")
        val USE_24_HOUR = booleanPreferencesKey("use_24_hour")
        val PERSIAN_DIGITS = booleanPreferencesKey("persian_digits")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val LANGUAGE = stringPreferencesKey("language")
        val CONFLICT_POLICY = stringPreferencesKey("conflict_policy")
    }

    override val settings: Flow<AppSettings> = context.settingsDataStore.data.map { it.toAppSettings() }

    override suspend fun current(): AppSettings = settings.first()

    override suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.settingsDataStore.edit { prefs ->
            val s = transform(prefs.toAppSettings())
            s.defaultCalendarId?.let { prefs[Keys.DEFAULT_CALENDAR_ID] = it } ?: prefs.remove(Keys.DEFAULT_CALENDAR_ID)
            prefs[Keys.DEFAULT_REMINDER_MINUTES] = s.defaultReminderMinutes
            prefs[Keys.TIME_ZONE] = s.timeZone
            prefs[Keys.WEEK_START] = s.weekStart.name
            prefs[Keys.USE_24_HOUR] = s.use24HourFormat
            prefs[Keys.PERSIAN_DIGITS] = s.persianDigits
            prefs[Keys.THEME_MODE] = s.themeMode.name
            prefs[Keys.DYNAMIC_COLOR] = s.dynamicColor
            prefs[Keys.LANGUAGE] = s.language.name
            prefs[Keys.CONFLICT_POLICY] = s.conflictPolicy.name
        }
    }

    private fun Preferences.toAppSettings(): AppSettings {
        val d = AppSettings.DEFAULT
        return AppSettings(
            defaultCalendarId = this[Keys.DEFAULT_CALENDAR_ID],
            defaultReminderMinutes = this[Keys.DEFAULT_REMINDER_MINUTES] ?: d.defaultReminderMinutes,
            timeZone = this[Keys.TIME_ZONE] ?: d.timeZone,
            weekStart = enumOr(this[Keys.WEEK_START], d.weekStart),
            use24HourFormat = this[Keys.USE_24_HOUR] ?: d.use24HourFormat,
            persianDigits = this[Keys.PERSIAN_DIGITS] ?: d.persianDigits,
            themeMode = enumOr(this[Keys.THEME_MODE], d.themeMode),
            dynamicColor = this[Keys.DYNAMIC_COLOR] ?: d.dynamicColor,
            language = enumOr(this[Keys.LANGUAGE], d.language),
            conflictPolicy = enumOr(this[Keys.CONFLICT_POLICY], d.conflictPolicy),
        )
    }

    private inline fun <reified T : Enum<T>> enumOr(name: String?, default: T): T =
        enumValues<T>().firstOrNull { it.name == name } ?: default
}
