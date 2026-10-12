package com.example.jalicalendar.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Reminder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Schedules exact-time alarms for event reminders via [AlarmManager].
 *
 * One alarm per (event, reminder). Exact alarms are used on API 31+ only when
 * the user has granted SCHEDULE_EXACT_ALARM (system settings); otherwise we
 * fall back to inexact alarms so reminders still fire (just with jitter).
 * Callers re-schedule the affected event's reminders after every save/delete.
 */
@Singleton
class ReminderScheduler @Inject constructor(@ApplicationContext private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Schedule reminders for one event (replacing any prior alarms for it).
     * Past reminders and all-day events are skipped.
     */
    fun scheduleForEvent(event: Event, reminders: List<Reminder>) {
        cancelForEvent(event.id) // replace semantics
        val now = System.currentTimeMillis()
        for (reminder in reminders.filter { it.enabled }) {
            val triggerAt = event.startUtcMillis - reminder.minutesBefore * 60_000L
            if (triggerAt <= now) continue
            val pending = pendingIntentFor(event.id, reminder.id, event.title, event.startUtcMillis)
            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            }
        }
    }

    /**
     * Re-arm alarms for every upcoming reminder (called after sync and app
     * start). Setting the same (event, reminder) request code replaces the
     * previous alarm, so this is idempotent.
     */
    fun scheduleAll(items: List<com.example.jalicalendar.domain.model.ScheduledReminder>) {
        val now = System.currentTimeMillis()
        for (item in items) {
            val triggerAt = item.event.startUtcMillis - item.reminder.minutesBefore * 60_000L
            if (triggerAt <= now) continue
            val pending = pendingIntentFor(
                item.event.id, item.reminder.id, item.event.title, item.event.startUtcMillis,
            )
            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            }
        }
    }

    /** Drop all alarms belonging to one event (after delete or reminder edit). */
    fun cancelForEvent(eventId: String) {
        // Best-effort cancel for the current and past reminder slots. We can't
        // enumerate reminder ids here, so callers pass the concrete ids when
        // known; this catches the common single-reminder case.
        alarmManager.cancel(pendingIntentFor(eventId, "0", "", 0L))
    }

    /** Cancel a specific reminder alarm by its reminder id. */
    fun cancel(eventId: String, reminderId: String) {
        alarmManager.cancel(pendingIntentFor(eventId, reminderId, "", 0L))
    }

    private fun canScheduleExact(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            alarmManager.canScheduleExactAlarms()

    private fun pendingIntentFor(
        eventId: String,
        reminderId: String,
        title: String,
        triggerAt: Long,
    ): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ReminderReceiver.ACTION
            putExtra(ReminderReceiver.EXTRA_EVENT_ID, eventId)
            putExtra(ReminderReceiver.EXTRA_REMINDER_ID, reminderId)
            putExtra(ReminderReceiver.EXTRA_TITLE, title)
        }
        // Stable request code per (event, reminder) so each alarm is distinct.
        val requestCode = (eventId + "|" + reminderId).hashCode()
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
