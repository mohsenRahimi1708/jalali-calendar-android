package com.example.jalicalendar.core.time

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PersianDigitsTest {

    @Test
    fun `converts ascii to persian digits`() {
        assertThat(PersianDigits.toPersian("0123456789")).isEqualTo("۰۱۲۳۴۵۶۷۸۹")
        assertThat(PersianDigits.toPersian("1404-07-18")).isEqualTo("۱۴۰۴-۰۷-۱۸")
    }

    @Test
    fun `converts persian digits back to ascii for parsing`() {
        assertThat(PersianDigits.toLatin("۰۱۲۳۴۵۶۷۸۹")).isEqualTo("0123456789")
        assertThat(PersianDigits.toLatin("ساعت ۱۲:۳۰")).isEqualTo("ساعت 12:30")
    }

    @Test
    fun `round-trips mixed content`() {
        val s = "Event 42 at 18:30"
        assertThat(PersianDigits.toLatin(PersianDigits.toPersian(s))).isEqualTo(s)
        // non-digits untouched
        assertThat(PersianDigits.toPersian("abc/=- ")).isEqualTo("abc/=- ")
    }

    @Test
    fun `localize respects the toggle`() {
        assertThat(PersianDigits.localize("42", enabled = true)).isEqualTo("۴۲")
        assertThat(PersianDigits.localize("42", enabled = false)).isEqualTo("42")
        assertThat(PersianDigits.localizeNumber(1404, enabled = true)).isEqualTo("۱۴۰۴")
        assertThat(PersianDigits.localizeNumber(1404L, enabled = false)).isEqualTo("1404")
    }
}
