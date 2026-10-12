package com.example.jalicalendar.data.local

import com.example.jalicalendar.data.local.entity.AccountEntity
import com.example.jalicalendar.data.local.entity.CalendarEntity
import com.example.jalicalendar.data.local.entity.EventEntity
import com.example.jalicalendar.data.local.entity.ReminderEntity
import com.example.jalicalendar.data.local.entity.TaskEntity
import com.example.jalicalendar.domain.model.Account
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Reminder
import com.example.jalicalendar.domain.model.Task

/** Entity → domain. */
fun EventEntity.toDomain() = Event(
    id, calendarId, remoteId, title, description, location,
    startUtcMillis, endUtcMillis, allDay, timeZone, rrule, rdate, exdate,
    recurringEventId, originalStartUtcMillis, status, attendeesJson, organizer,
    colorArgb, visibility, transparency, etag, remoteUpdatedAtMillis,
    localUpdatedAtMillis, dirty, deleted,
)

fun CalendarEntity.toDomain() = Calendar(
    id, accountId, remoteId, type, title, description, colorArgb, timeZone,
    isPrimary, accessRole, hidden, selected, syncToken, lastSyncedAtMillis,
    localUpdatedAtMillis, dirty, deleted,
)

fun ReminderEntity.toDomain() = Reminder(id, eventId, minutesBefore, enabled)

fun TaskEntity.toDomain() = Task(
    id, listId, remoteId, title, notes, dueDateMillis, completed, completedAtMillis,
    status, position, etag, remoteUpdatedAtMillis, localUpdatedAtMillis, dirty, deleted,
)

fun AccountEntity.toDomain() = Account(id, email, displayName, photoUrl, provider, scopes, addedAtMillis)

/** Domain → entity. */
fun Event.toEntity() = EventEntity(
    id, calendarId, remoteId, title, description, location,
    startUtcMillis, endUtcMillis, allDay, timeZone, rrule, rdate, exdate,
    recurringEventId, originalStartUtcMillis, status, attendeesJson, organizer,
    colorArgb, visibility, transparency, etag, remoteUpdatedAtMillis,
    localUpdatedAtMillis, dirty, deleted,
)

fun Calendar.toEntity() = CalendarEntity(
    id, accountId, remoteId, type, title, description, colorArgb, timeZone,
    isPrimary, accessRole, hidden, selected, syncToken, lastSyncedAtMillis,
    localUpdatedAtMillis, dirty, deleted,
)

fun Reminder.toEntity() = ReminderEntity(id, eventId, minutesBefore, enabled)

fun Task.toEntity() = TaskEntity(
    id, listId, remoteId, title, notes, dueDateMillis, completed, completedAtMillis,
    status, position, etag, remoteUpdatedAtMillis, localUpdatedAtMillis, dirty, deleted,
)

fun Account.toEntity() = AccountEntity(id, email, displayName, photoUrl, provider, scopes, addedAtMillis)
