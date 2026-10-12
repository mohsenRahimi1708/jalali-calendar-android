package com.example.jalicalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import com.example.jalicalendar.domain.model.LanguageMode

/**
 * App strings live in code (not resources) because the language is driven by
 * the in-app setting rather than the device locale: SYSTEM resolves from the
 * device configuration once, PERSIAN/ENGLISH override it immediately.
 */
data class Strings(
    val appName: String,
    // Navigation / top level
    val search: String, val settings: String, val calendars: String,
    val addEvent: String, val syncNow: String, val synced: String, val offline: String,
    val syncing: String, val syncFailed: String,
    // Views
    val month: String, val week: String, val day: String, val today: String,
    val tomorrow: String, val yesterday: String,
    val viewMonth: String, val viewWeek: String, val viewDay: String,
    val noEvents: String, val moreEvents: String,
    // Event editor
    val newEvent: String, val editEvent: String,
    val title: String, val description: String, val location: String,
    val calendar: String, val allDay: String, val startDate: String, val endDate: String,
    val startTime: String, val endTime: String, val repeat: String, val reminder: String,
    val save: String, val delete: String, val cancel: String,
    val titleRequired: String, val endAfterStart: String, val eventSaved: String, val eventDeleted: String,
    // Repeat / reminder presets
    val repeatNone: String, val repeatDaily: String, val repeatWeekly: String,
    val repeatMonthly: String, val repeatYearly: String,
    val reminderNone: String, val reminderAtStart: String,
    val reminderMinutesBefore: String, val reminderDayBefore: String,
    // Search
    val searchHint: String, val searchResults: String, val noResults: String,
    // Settings
    val general: String, val appearance: String,
    val defaultCalendar: String, val defaultReminder: String, val timeZone: String,
    val weekStart: String, val hourFormat24: String, val persianDigits: String,
    val theme: String, val themeSystem: String, val themeLight: String, val themeDark: String,
    val dynamicColor: String, val language: String,
    val languageSystem: String, val languagePersian: String, val languageEnglish: String,
    val minutes: String,
    // Calendars
    val calendarsSubtitle: String, val localCalendar: String, val noAccountYet: String,
    val signInToSync: String, val showCalendar: String,
    val signIn: String, val signOut: String, val clientIdHint: String,
    // Misc
    val unknown: String, val discardChanges: String,
)

val faStrings = Strings(
    appName = "تقویم جلالی",
    search = "جستجو", settings = "تنظیمات", calendars = "تقویم‌ها",
    addEvent = "افزودن رویداد", syncNow = "همگام‌سازی", synced = "همگام شد", offline = "آفلاین",
    syncing = "در حال همگام‌سازی…", syncFailed = "همگام‌سازی ناموفق",
    month = "ماه", week = "هفته", day = "روز", today = "امروز", tomorrow = "فردا", yesterday = "دیروز",
    viewMonth = "نمای ماه", viewWeek = "نمای هفته", viewDay = "نمای روز",
    noEvents = "رویدادی نیست", moreEvents = "…",
    newEvent = "رویداد جدید", editEvent = "ویرایش رویداد",
    title = "عنوان", description = "توضیحات", location = "مکان",
    calendar = "تقویم", allDay = "تمام‌روز", startDate = "تاریخ شروع", endDate = "تاریخ پایان",
    startTime = "ساعت شروع", endTime = "ساعت پایان", repeat = "تکرار", reminder = "یادآوری",
    save = "ذخیره", delete = "حذف", cancel = "انصراف",
    titleRequired = "عنوان الزامی است", endAfterStart = "پایان باید بعد از شروع باشد",
    eventSaved = "رویداد ذخیره شد", eventDeleted = "رویداد حذف شد",
    repeatNone = "بدون تکرار", repeatDaily = "روزانه", repeatWeekly = "هفتگی",
    repeatMonthly = "ماهانه", repeatYearly = "سالانه",
    reminderNone = "بدون یادآوری", reminderAtStart = "در شروع رویداد",
    reminderMinutesBefore = "%d دقیقه قبل", reminderDayBefore = "یک روز قبل",
    searchHint = "جستجوی رویدادها…", searchResults = "نتایج", noResults = "موردی یافت نشد",
    general = "عمومی", appearance = "ظاهر",
    defaultCalendar = "تقویم پیش‌فرض", defaultReminder = "یادآوری پیش‌فرض", timeZone = "منطقه زمانی",
    weekStart = "شروع هفته", hourFormat24 = "قالب ۲۴ ساعته", persianDigits = "ارقام فارسی",
    theme = "پوسته", themeSystem = "سیستم", themeLight = "روشن", themeDark = "تیره",
    dynamicColor = "رنگ پویا (Material You)", language = "زبان",
    languageSystem = "سیستم", languagePersian = "فارسی", languageEnglish = "English",
    minutes = "دقیقه",
    calendarsSubtitle = "تقویم‌های نمایش‌داده‌شده", localCalendar = "محلی",
    noAccountYet = "حسابی متصل نیست", signInToSync = "برای همگام‌سازی وارد شوید",
    showCalendar = "نمایش تقویم",
    signIn = "ورود با گوگل", signOut = "خروج",
    clientIdHint = "کلید OAuth تنظیم نشده است؛ README را ببینید.",
    unknown = "نامشخص", discardChanges = "تغییرات ذخیره نشده است",
)

val enStrings = Strings(
    appName = "Jalali Calendar",
    search = "Search", settings = "Settings", calendars = "Calendars",
    addEvent = "Add event", syncNow = "Sync now", synced = "Synced", offline = "Offline",
    syncing = "Syncing…", syncFailed = "Sync failed",
    month = "Month", week = "Week", day = "Day", today = "Today", tomorrow = "Tomorrow", yesterday = "Yesterday",
    viewMonth = "Month view", viewWeek = "Week view", viewDay = "Day view",
    noEvents = "No events", moreEvents = "…",
    newEvent = "New event", editEvent = "Edit event",
    title = "Title", description = "Description", location = "Location",
    calendar = "Calendar", allDay = "All day", startDate = "Start date", endDate = "End date",
    startTime = "Start time", endTime = "End time", repeat = "Repeat", reminder = "Reminder",
    save = "Save", delete = "Delete", cancel = "Cancel",
    titleRequired = "Title is required", endAfterStart = "End must be after start",
    eventSaved = "Event saved", eventDeleted = "Event deleted",
    repeatNone = "Does not repeat", repeatDaily = "Daily", repeatWeekly = "Weekly",
    repeatMonthly = "Monthly", repeatYearly = "Yearly",
    reminderNone = "No reminder", reminderAtStart = "At start",
    reminderMinutesBefore = "%d minutes before", reminderDayBefore = "1 day before",
    searchHint = "Search events…", searchResults = "Results", noResults = "No results",
    general = "General", appearance = "Appearance",
    defaultCalendar = "Default calendar", defaultReminder = "Default reminder", timeZone = "Time zone",
    weekStart = "Week starts on", hourFormat24 = "24-hour format", persianDigits = "Persian digits",
    theme = "Theme", themeSystem = "System", themeLight = "Light", themeDark = "Dark",
    dynamicColor = "Dynamic color (Material You)", language = "Language",
    languageSystem = "System", languagePersian = "فارسی", languageEnglish = "English",
    minutes = "min",
    calendarsSubtitle = "Visible calendars", localCalendar = "Local",
    noAccountYet = "No account connected", signInToSync = "Sign in to sync",
    showCalendar = "Show calendar",
    signIn = "Sign in with Google", signOut = "Sign out",
    clientIdHint = "OAuth client ID not configured; see README.",
    unknown = "Unknown", discardChanges = "Unsaved changes",
)

val LocalStrings = staticCompositionLocalOf { enStrings }

/** True when the effective UI language renders Arabic-script (Persian) labels. */
val LocalPersianLanguage = staticCompositionLocalOf { false }

/** Resolves the effective strings for the current language setting. */
@Composable
fun ProvideStrings(language: LanguageMode, content: @Composable () -> Unit) {
    val deviceIsPersian = LocalConfiguration.current.locales[0].language == "fa"
    val strings = when (language) {
        LanguageMode.PERSIAN -> faStrings
        LanguageMode.ENGLISH -> enStrings
        LanguageMode.SYSTEM -> if (deviceIsPersian) faStrings else enStrings
    }
    CompositionLocalProvider(
        LocalStrings provides strings,
        LocalPersianLanguage provides (strings === faStrings),
        content = content,
    )
}

/** Readable access inside composables: `LocalStrings.current`. */
object StringsAccess {
    val current: Strings
        @Composable @ReadOnlyComposable get() = LocalStrings.current
}
