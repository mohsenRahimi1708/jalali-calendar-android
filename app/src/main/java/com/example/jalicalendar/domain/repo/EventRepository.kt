package com.example.jalicalendar.domain.repo

import com.example.jalicalendar.domain.model.Event
import com.example.jalicalendar.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    /** Live events overlapping [startUtcMillis, endUtcMillis), non-deleted, ordered by start. */
    fun observeBetween(startUtcMillis: Long, endUtcMillis: Long): Flow<List<Event>>

    fun observeById(id: String): Flow<Event?>
    suspend fun getEvent(id: String): Event?
    suspend fun search(query: String): List<Event>

    /** Insert or update; bumps localUpdatedAt and marks dirty. Returns the row id. */
    suspend fun upsert(event: Event): String

    /**
     * Delete locally: hard-deletes events that were never synced, otherwise
     * soft-deletes (deleted + dirty) so the sync layer removes the server copy.
     */
    suspend fun delete(event: Event)

    suspend fun remindersFor(eventId: String): List<Reminder>

    /** Future events (>= [nowMillis]) that have enabled reminders scheduled. */
    suspend fun upcomingWithReminders(nowMillis: Long): List<com.example.jalicalendar.domain.model.ScheduledReminder>
    suspend fun observeReminders(eventId: String): Flow<List<Reminder>>
    suspend fun upsertReminders(reminders: List<Reminder>)
    suspend fun deleteReminder(id: String)

    /** Find the local row mirroring a Google event id (pull merge). */
    suspend fun findLocalByRemoteId(remoteId: String): Event?

    /** Rows with local changes to push (dirty or soft-deleted). */
    suspend fun pendingChanges(): List<Event>

    /** Called by the sync layer after a successful push/pull merge. */
    suspend fun markSynced(
        id: String,
        remoteId: String?,
        etag: String?,
        remoteUpdatedAtMillis: Long?,
        calendarId: String? = null,
    )

    suspend fun purgeSyncedDeleted(id: String)
    suspend fun upsertSynced(event: Event) // pull path: no dirty flag, no localUpdatedAt bump
}
