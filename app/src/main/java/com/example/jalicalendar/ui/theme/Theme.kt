package com.example.jalicalendar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.jalicalendar.domain.model.AppSettings
import com.example.jalicalendar.domain.model.LanguageMode
import com.example.jalicalendar.domain.model.ThemeMode

/**
 * App theme: honors the settings for theme mode (system/light/dark),
 * dynamic color (Material You on Android 12+) and RTL when the language is
 * Persian (or SYSTEM-resolved device locale is Persian).
 *
 * Note: [AppSettings.persianDigits] and 24-hour format are applied by the
 * formatting layer, not by the theme.
 */
@Composable
fun JalaliCalendarTheme(
    settings: AppSettings = AppSettings.DEFAULT,
    isSystemInDark: Boolean = isSystemInDarkTheme(),
    deviceIsPersian: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dark = when (settings.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        settings.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)

        else -> if (dark) DarkColors else LightColors
    }

    val direction = when (settings.language) {
        LanguageMode.PERSIAN -> LayoutDirection.Rtl
        LanguageMode.ENGLISH -> LayoutDirection.Ltr
        LanguageMode.SYSTEM -> if (deviceIsPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}
