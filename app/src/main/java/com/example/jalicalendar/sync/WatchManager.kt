package com.example.jalicalendar.sync

import android.content.Context
import android.util.Log
import com.example.jalicalendar.data.remote.CalendarApi
import com.example.jalicalendar.data.remote.WatchRequestDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Calendar push notifications ("watch" channels).
 *
 * When a channel is registered on `calendarList` (or a single calendar), Google
 * sends a POST to the configured public HTTPS webhook whenever anything changes,
 * which lets the app sync immediately instead of waiting for the 15-min poll.
 *
 * This is **optional and off by default** (spec §8, assumption): a watch channel
 * requires a public HTTPS endpoint the app controls, which a pure client app
 * does not have. Set [WEBHOOK_URL] (a BuildConfig/secret value) to your relay —
 * a tiny service that receives the Google POST and forwards a "sync" signal to
 * the device via FCM. Without it, the periodic WorkManager sync remains the
 * sole (and fully functional) mechanism.
 *
 * Google expires channels after ~7 days (or up to 30 for some resources), so
 * [renewIfNeeded] re-registers a channel whose expiration is near. The channel
 * state is persisted per calendar so a relaunch doesn't leak stale channels.
 */
@Singleton
class WatchManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val calendarApi: CalendarApi,
) {

    private val prefs = context.getSharedPreferences("watch_channels", Context.MODE_PRIVATE)

    /** True when a webhook endpoint has been configured; false disables watch entirely. */
    fun isWebhookConfigured(): Boolean = WEBHOOK_URL.isNotBlank()

    /**
     * Ensure a fresh channel exists on the calendarList resource. Safe to call
     * after every successful sync: it no-ops when the webhook isn't configured
     * or the current channel is still valid for >1 day.
     */
    suspend fun renewIfNeeded() {
        if (!isWebhookConfigured()) return
        val now = System.currentTimeMillis()
        val stored = prefs.getLong(KEY_EXPIRATION, 0L)
        if (stored > now + RENEW_THRESHOLD_MS) return // still good
        try {
            val request = WatchRequestDto(
                id = UUID.randomUUID().toString(),
                address = WEBHOOK_URL,
                token = WEBHOOK_SECRET.takeIf { it.isNotBlank() },
            )
            val response = calendarApi.watchCalendarList(request)
            prefs.edit()
                .putString(KEY_CHANNEL_ID, response.id)
                .putString(KEY_RESOURCE_ID, response.resourceId)
                .putLong(KEY_EXPIRATION, response.expiration?.toLongOrNull() ?: (now + DEFAULT_TTL_MS))
                .apply()
        } catch (e: Exception) {
            // Watch is best-effort; a failure never blocks the poll-based sync.
            Log.w(TAG, "watch channel renewal failed: ${e.message}")
        }
    }

    /** Stop watching (called on sign-out so Google stops posting to the relay). */
    suspend fun stopAll() {
        // Note: Google's stop API is `POST /watch/stop`; the v3 Calendar API
        // exposes it as a POST to the resource with an empty body is not
        // supported by our Retrofit surface, so we simply drop local state and
        // let the channel expire server-side (~7 days). Documented trade-off.
        prefs.edit().clear().apply()
    }

    private companion object {
        const val TAG = "WatchManager"
        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_RESOURCE_ID = "resource_id"
        const val KEY_EXPIRATION = "expiration"
        const val RENEW_THRESHOLD_MS = 24 * 3600_000L
        const val DEFAULT_TTL_MS = 7 * 24 * 3600_000L

        /** Set via BuildConfig/secret to enable push; empty = poll-only. */
        val WEBHOOK_URL: String get() = com.example.jalicalendar.BuildConfig.WATCH_WEBHOOK_URL
        val WEBHOOK_SECRET: String get() = com.example.jalicalendar.BuildConfig.WATCH_WEBHOOK_SECRET
    }
}
