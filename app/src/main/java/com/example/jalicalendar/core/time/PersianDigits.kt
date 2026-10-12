package com.example.jalicalendar.core.time

/**
 * Digit localization for the "Persian digits" setting.
 * All formatters in the app produce ASCII digits first, then run through
 * [localize] when the setting is on — keeping parsing/math ASCII-only.
 */
object PersianDigits {
    private const val FA_DIGITS = "۰۱۲۳۴۵۶۷۸۹"
    private const val EN_DIGITS = "0123456789"

    fun toPersian(text: String): String = buildString(text.length) {
        for (c in text) {
            val i = c - '0'
            append(if (c in '0'..'9') FA_DIGITS[i] else c)
        }
    }

    fun toLatin(text: String): String = buildString(text.length) {
        for (c in text) {
            val i = FA_DIGITS.indexOf(c)
            append(if (i >= 0) EN_DIGITS[i] else c)
        }
    }

    /** Returns Persian digits when [enabled], ASCII otherwise. */
    fun localize(text: String, enabled: Boolean): String = if (enabled) toPersian(text) else text

    fun localizeNumber(value: Long, enabled: Boolean): String = localize(value.toString(), enabled)

    fun localizeNumber(value: Int, enabled: Boolean): String = localize(value.toString(), enabled)
}
