package com.example.jalicalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.jalicalendar.ui.navigation.AppNavHost
import com.example.jalicalendar.ui.screens.main.MainViewModel
import com.example.jalicalendar.ui.theme.JalaliCalendarTheme

/**
 * App root: resolves settings once, applies theme (mode/dynamic color/RTL)
 * and language (strings), then hosts navigation. `MainViewModel` is shared
 * with the main screen via the activity/navigation scope.
 */
@Composable
fun JalaliCalendarRoot(viewModel: MainViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val deviceIsPersian = LocalConfiguration.current.locales[0].language == "fa"

    JalaliCalendarTheme(
        settings = settings,
        deviceIsPersian = deviceIsPersian,
    ) {
        ProvideStrings(language = settings.language) {
            val navController = rememberNavController()
            AppNavHost(navController)
        }
    }
}
