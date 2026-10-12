package com.example.jalicalendar.domain.model

/** A reminder attached to an event; deleted automatically when the event is deleted. */
data class Reminder(
    val id: String,
    val eventId: String,
    val minutesBefore: Int, // 0 = at start of event
    val enabled: Boolean = true,
)

/** A future reminder paired with its event — the scheduler's input. */
data class ScheduledReminder(
    val event: com.example.jalicalendar.domain.model.Event,
    val reminder: Reminder,
)
