package com.example.jalicalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.jalicalendar.ui.JalaliCalendarRoot
import com.example.jalicalendar.ui.theme.JalaliCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity host. The whole UI is built with Jetpack Compose — no XML layouts.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JalaliCalendarTheme {
                // Screen content is wired up in the UI step.
                JalaliCalendarRoot()
            }
        }
    }
}
