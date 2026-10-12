package com.example.jalicalendar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jalicalendar.ui.screens.calendars.CalendarsScreen
import com.example.jalicalendar.ui.screens.eventedit.EventEditScreen
import com.example.jalicalendar.ui.screens.main.MainCalendarScreen
import com.example.jalicalendar.ui.screens.search.SearchScreen
import com.example.jalicalendar.ui.screens.settings.SettingsScreen

/** Navigation graph. `eventId=null` in the `event` route = create mode. */
object Routes {
    const val MAIN = "main"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val CALENDARS = "calendars"
    const val EVENT_NEW = "event/new"
    const val EVENT_EDIT = "event/{eventId}"
    fun eventEdit(eventId: String) = "event/$eventId"
    const val ARG_EVENT_ID = "eventId"

    /** Key on MAIN's SavedStateHandle where editors post a toast message. */
    const val RESULT_TOAST = "toast"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN) { MainCalendarScreen(navController) }
        composable(
            Routes.EVENT_NEW,
            arguments = listOf(
                navArgument("startMillis") { type = NavType.LongType; defaultValue = -1L },
                navArgument("allDayDate") { type = NavType.StringType; nullable = true; defaultValue = null },
            ),
        ) { EventEditScreen(navController, eventId = null) }
        composable(
            Routes.EVENT_EDIT,
            arguments = listOf(navArgument(Routes.ARG_EVENT_ID) { type = NavType.StringType }),
        ) { entry -> EventEditScreen(navController, entry.arguments?.getString(Routes.ARG_EVENT_ID)) }
        composable(Routes.SEARCH) { SearchScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(Routes.CALENDARS) { CalendarsScreen(navController) }
    }
}
