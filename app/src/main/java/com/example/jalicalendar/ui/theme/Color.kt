package com.example.jalicalendar.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Original brand palette ("deep teal" — chosen to avoid any Google Calendar
 * trade dress). Dynamic color (Material You) overrides these on Android 12+.
 */
// Light
private val LightPrimary = Color(0xFF006750)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFF9DF0D5)
private val LightOnPrimaryContainer = Color(0xFF00201A)
private val LightSecondary = Color(0xFF4C635D)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFCEE8E0)
private val LightOnSecondaryContainer = Color(0xFF09201B)
private val LightTertiary = Color(0xFF4A6078)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFFD2E4FF)
private val LightOnTertiaryContainer = Color(0xFF041C32)
private val LightBackground = Color(0xFFF6FBF7)
private val LightOnBackground = Color(0xFF171D1B)
private val LightSurface = Color(0xFFF6FBF7)
private val LightOnSurface = Color(0xFF171D1B)
private val LightSurfaceVariant = Color(0xFFDBE5E0)
private val LightOnSurfaceVariant = Color(0xFF404945)
private val LightError = Color(0xFFBA1A1A)

// Dark
private val DarkPrimary = Color(0xFF81D4C5)
private val DarkOnPrimary = Color(0xFF00382E)
private val DarkPrimaryContainer = Color(0xFF005145)
private val DarkOnPrimaryContainer = Color(0xFF9DF0D5)
private val DarkSecondary = Color(0xFFB3CCC4)
private val DarkOnSecondary = Color(0xFF1E352F)
private val DarkSecondaryContainer = Color(0xFF344B45)
private val DarkOnSecondaryContainer = Color(0xFFCEE8E0)
private val DarkTertiary = Color(0xFFB2C8E2)
private val DarkOnTertiary = Color(0xFF173248)
private val DarkTertiaryContainer = Color(0xFF2F4A61)
private val DarkOnTertiaryContainer = Color(0xFFD2E4FF)
private val DarkBackground = Color(0xFF0F1513)
private val DarkOnBackground = Color(0xFFDFE4E1)
private val DarkSurface = Color(0xFF0F1513)
private val DarkOnSurface = Color(0xFFDFE4E1)
private val DarkSurfaceVariant = Color(0xFF404945)
private val DarkOnSurfaceVariant = Color(0xFFBFC9C4)
private val DarkError = Color(0xFFFFB4AB)

internal val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = LightError,
)

internal val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = DarkError,
)
