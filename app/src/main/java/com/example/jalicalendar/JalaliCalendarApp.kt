package com.example.jalicalendar

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.sync.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Application entry point.
 *
 * WorkManager is initialized manually (its default androidx.startup initializer is
 * removed in the manifest) so that workers can use Hilt-injected dependencies.
 */
@HiltAndroidApp
class JalaliCalendarApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var calendarRepo: CalendarRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // 15-minute periodic sync (spec §6). Safe to call repeatedly (KEEP).
        SyncWorker.schedulePeriodic(this)
        // Sync-on-open: appends a one-shot pass so fresh data appears fast.
        SyncWorker.requestOneShot(this)
        // Fresh installs have no calendars until one is created; seed the Local
        // calendar on first launch so every screen has a valid target.
        appScope.launch { runCatching { calendarRepo.seedLocalCalendar() } }
    }
}
