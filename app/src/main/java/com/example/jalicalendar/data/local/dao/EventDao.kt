package com.example.jalicalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.jalicalendar.data.local.entity.EventEntity
import com.example.jalicalendar.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    /** Events overlapping [start, end): start < end of range AND end > start of range. */
    @Query(
        """
        SELECT * FROM events
        WHERE deleted = 0 AND startUtcMillis < :endExclusive AND endUtcMillis > :startInclusive
        ORDER BY startUtcMillis ASC, endUtcMillis ASC
        """,
    )
    fun observeBetween(startInclusive: Long, endExclusive: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun observeById(id: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE calendarId = :calendarId AND remoteId = :remoteId LIMIT 1")
    suspend fun getByRemoteId(calendarId: String, remoteId: String): EventEntity?

    @Query(
        """
        SELECT * FROM events
        WHERE deleted = 0 AND (
            title LIKE '%' || :query || '%'
            OR ifnull(description, '') LIKE '%' || :query || '%'
            OR ifnull(location, '') LIKE '%' || :query || '%'
        )
        ORDER BY startUtcMillis ASC
        """,
    )
    suspend fun search(query: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE dirty = 1 OR deleted = 1")
    suspend fun pendingChanges(): List<EventEntity>

    @Upsert
    suspend fun upsert(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Query("DELETE FROM events WHERE calendarId = :calendarId")
    suspend fun deleteForCalendar(calendarId: String)

    @Query(
        """
        UPDATE events SET remoteId = :remoteId, etag = :etag,
            remoteUpdatedAtMillis = :remoteUpdatedAtMillis,
            dirty = 0, deleted = 0, calendarId = ifnull(:calendarId, calendarId)
        WHERE id = :id
        """,
    )
    suspend fun markSynced(
        id: String,
        remoteId: String?,
        etag: String?,
        remoteUpdatedAtMillis: Long?,
        calendarId: String?,
    )

    @Query("DELETE FROM events WHERE id = :id AND deleted = 1")
    suspend fun purgeSyncedDeleted(id: String)

    @Query("UPDATE events SET dirty = 0, deleted = 0 WHERE id = :id")
    suspend fun clearSyncFlags(id: String)

    @Query("SELECT * FROM events WHERE remoteId = :remoteId LIMIT 1")
    suspend fun findByRemoteId(remoteId: String): EventEntity?

    @Query(
        """
        SELECT DISTINCT e.* FROM events e
        INNER JOIN reminders r ON r.eventId = e.id
        WHERE e.deleted = 0 AND e.startUtcMillis >= :now
        ORDER BY e.startUtcMillis ASC
        """,
    )
    suspend fun upcomingWithReminders(now: Long): List<EventEntity>
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE eventId = :eventId ORDER BY minutesBefore DESC")
    suspend fun forEvent(eventId: String): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE eventId = :eventId ORDER BY minutesBefore DESC")
    fun observeForEvent(eventId: String): Flow<List<ReminderEntity>>

    @Upsert
    suspend fun upsert(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM reminders WHERE eventId = :eventId")
    suspend fun deleteForEvent(eventId: String)

}
