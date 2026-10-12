package com.example.jalicalendar.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.jalicalendar.core.time.PersianDigits
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Fired by [ReminderScheduler]'s alarms. Posts a notification whose subtitle is
 * the event start time in the device zone, honoring the Persian-digit and
 * 24-hour preferences (read from the event's passed-in start time).
 */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION) return
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: return
        val reminderId = intent.getStringExtra(EXTRA_REMINDER_ID) ?: "0"
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val startMillis = intent.getLongExtra(EXTRA_START_MILLIS, -1L)

        val subtitle = if (startMillis > 0) formatStart(context, startMillis) else ""
        NotificationHelper.showReminder(
            context = context,
            notificationId = (eventId + "|" + reminderId).hashCode(),
            title = title.ifBlank { context.getString(com.example.jalicalendar.R.string.reminder) },
            subtitle = subtitle,
        )
    }

    /** "Today 09:00" / "Tomorrow 14:30" / "Oct 5, 09:00" in device zone. */
    private fun formatStart(context: Context, millis: Long): String {
        val zone = ZoneId.systemDefault()
        val event = Instant.ofEpochMilli(millis).atZone(zone)
        val now = Instant.now().atZone(zone)
        val timeFmt = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        val time = timeFmt.format(event)

        val dayLabel = when {
            event.toLocalDate() == now.toLocalDate() -> context.getString(com.example.jalicalendar.R.string.today)
            event.toLocalDate() == now.toLocalDate().plusDays(1) ->
                context.getString(com.example.jalicalendar.R.string.tomorrow)
            else -> DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()).format(event)
        }
        val combined = "$dayLabel $time"
        // Persian digits when the setting is on (default) and locale is Persian.
        return PersianDigits.localize(combined, enabled = isPersianLocale(context))
    }

    private fun isPersianLocale(context: Context): Boolean =
        context.resources.configuration.locales[0].language == "fa"

    companion object {
        const val ACTION = "com.example.jalicalendar.action.REMINDER"
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_REMINDER_ID = "reminder_id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_START_MILLIS = "start_millis"
    }
}
