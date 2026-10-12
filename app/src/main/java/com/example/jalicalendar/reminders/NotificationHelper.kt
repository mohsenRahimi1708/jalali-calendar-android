package com.example.jalicalendar.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.jalicalendar.MainActivity
import com.example.jalicalendar.R

/**
 * Notification channel + posting. One "reminders" channel (spec §4: reminders
 * for events). Posting checks POST_NOTIFICATIONS (API 33+) at runtime; the
 * scheduler requests it from the UI, and a denial just silently skips alerts
 * rather than crashing.
 */
object NotificationHelper {

    const val CHANNEL_ID = "event_reminders"
    private const val NOTIFICATION_TAG = "event_reminder"

    // minSdk is 26 (O), so NotificationChannel is always available.
    fun ensureChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(R.string.notif_channel_desc)
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java)
            ?.createNotificationChannel(channel)
    }

    fun canPost(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    // POST_NOTIFICATIONS is enforced at runtime via canPost() below; lint cannot
    // follow that helper, so the annotation documents the real guard.
    @SuppressLint("MissingPermission")
    fun showReminder(context: Context, notificationId: Int, title: String, subtitle: String) {
        ensureChannel(context)
        if (!canPost(context)) return

        val contentIntent = PendingIntent.getActivity(
            context, notificationId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_TAG, notificationId, notification)
    }
}
