package com.example.jalicalendar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.jalicalendar.R

/**
 * Vazirmatn (SIL OFL) as the app font family for both scripts — it renders
 * Latin and Arabic-script digits well and supports weights 400–700.
 */
val Vazirmatn = FontFamily(
    Font(R.font.vazirmatn_regular, FontWeight.Normal),
    Font(R.font.vazirmatn_medium, FontWeight.Medium),
    Font(R.font.vazirmatn_semibold, FontWeight.SemiBold),
    Font(R.font.vazirmatn_bold, FontWeight.Bold),
)

private fun TextStyle.vazir() = copy(fontFamily = Vazirmatn)

/** Material 3 type scale with Vazirmatn applied to every style. */
val AppTypography = Typography().let { t ->
    Typography(
        displayLarge = t.displayLarge.vazir(),
        displayMedium = t.displayMedium.vazir(),
        displaySmall = t.displaySmall.vazir(),
        headlineLarge = t.headlineLarge.vazir(),
        headlineMedium = t.headlineMedium.vazir(),
        headlineSmall = t.headlineSmall.vazir(),
        titleLarge = t.titleLarge.vazir(),
        titleMedium = t.titleMedium.vazir(),
        titleSmall = t.titleSmall.vazir(),
        bodyLarge = t.bodyLarge.vazir(),
        bodyMedium = t.bodyMedium.vazir(),
        bodySmall = t.bodySmall.vazir(),
        labelLarge = t.labelLarge.vazir(),
        labelMedium = t.labelMedium.vazir(),
        labelSmall = t.labelSmall.vazir(),
    )
}

/** Compact numeric style used inside calendar day cells. */
val DayNumberStyle = TextStyle(fontFamily = Vazirmatn, fontWeight = FontWeight.Medium, fontSize = 14.sp)
