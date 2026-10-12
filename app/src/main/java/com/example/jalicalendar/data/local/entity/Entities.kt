package com.example.jalicalendar.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room layer. Entities mirror the domain models 1:1 (mapped in Mappers.kt) but
 * use primitives/strings only. Invariants:
 * - instants are UTC epoch millis,
 * - sync bookkeeping lives here: remoteId/etag/syncToken/dirty/deleted,
 * - unique (calendarId, remoteId) prevents duplicate rows on repeated pulls
 *   (SQLite treats NULLs as distinct, so local-only rows don't collide).
 */

@Entity(
    tableName = "events",
    indices = [
        Index(value = ["calendarId", "startUtcMillis"]),
        Index(value = ["calendarId", "remoteId"], unique = true),
        Index(value = ["dirty"]),
        Index(value = ["recurringEventId"]),
    ],
)
data class EventEntity(
    @PrimaryKey val id: String,
    val calendarId: String,
    val remoteId: String?,
    val title: String,
    val description: String?,
    val location: String?,
    val startUtcMillis: Long,
    val endUtcMillis: Long,
    val allDay: Boolean,
    val timeZone: String?,
    val rrule: String?,
    val rdate: String?,
    val exdate: String?,
    val recurringEventId: String?,
    val originalStartUtcMillis: Long?,
    val status: String,
    val attendeesJson: String?,
    val organizer: String?,
    val colorArgb: Int?,
    val visibility: String?,
    val transparency: String?,
    val etag: String?,
    val remoteUpdatedAtMillis: Long?,
    val localUpdatedAtMillis: Long,
    val dirty: Boolean,
    val deleted: Boolean,
)

@Entity(
    tableName = "calendars",
    indices = [
        Index(value = ["type", "remoteId"], unique = true),
        Index(value = ["dirty"]),
    ],
)
data class CalendarEntity(
    @PrimaryKey val id: String,
    val accountId: String?,
    val remoteId: String?,
    val type: String,
    val title: String,
    val description: String?,
    val colorArgb: Int,
    val timeZone: String,
    val isPrimary: Boolean,
    val accessRole: String,
    val hidden: Boolean,
    val selected: Boolean,
    val syncToken: String?,
    val lastSyncedAtMillis: Long?,
    val localUpdatedAtMillis: Long,
    val dirty: Boolean,
    val deleted: Boolean,
)

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["eventId"])],
)
data class ReminderEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val minutesBefore: Int,
    val enabled: Boolean,
)

@Entity(
    tableName = "tasks",
    indices = [
        Index(value = ["listId", "remoteId"], unique = true),
        Index(value = ["listId", "position"]),
        Index(value = ["dirty"]),
    ],
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val listId: String,
    val remoteId: String?,
    val title: String,
    val notes: String?,
    val dueDateMillis: Long?,
    val completed: Boolean,
    val completedAtMillis: Long?,
    val status: String,
    val position: String?,
    val etag: String?,
    val remoteUpdatedAtMillis: Long?,
    val localUpdatedAtMillis: Long,
    val dirty: Boolean,
    val deleted: Boolean,
)

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val provider: String,
    val scopes: String?,
    val addedAtMillis: Long,
)
