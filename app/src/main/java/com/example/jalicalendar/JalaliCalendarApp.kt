package com.example.jalicalendar

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import com.example.jalicalendar.sync.SyncWorker
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
    }
}
