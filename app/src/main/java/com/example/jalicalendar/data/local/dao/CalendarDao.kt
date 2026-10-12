package com.example.jalicalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.jalicalendar.data.local.entity.AccountEntity
import com.example.jalicalendar.data.local.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendars WHERE deleted = 0 ORDER BY isPrimary DESC, title ASC")
    fun observeAll(): Flow<List<CalendarEntity>>

    @Query("SELECT * FROM calendars WHERE deleted = 0 AND selected = 1 ORDER BY isPrimary DESC, title ASC")
    fun observeSelected(): Flow<List<CalendarEntity>>

    @Query("SELECT * FROM calendars WHERE id = :id")
    suspend fun getById(id: String): CalendarEntity?

    @Query("SELECT * FROM calendars WHERE type = :type AND deleted = 0 ORDER BY isPrimary DESC")
    suspend fun getByType(type: String): List<CalendarEntity>

    @Query("SELECT * FROM calendars WHERE dirty = 1 OR deleted = 1")
    suspend fun pendingChanges(): List<CalendarEntity>

    @Upsert
    suspend fun upsert(calendar: CalendarEntity)

    /** Local-only display preference: never touches sync bookkeeping. */
    @Query("UPDATE calendars SET selected = :selected WHERE id = :id")
    suspend fun setSelected(id: String, selected: Boolean)

    @Query("UPDATE calendars SET syncToken = :token, lastSyncedAtMillis = :syncedAtMillis WHERE id = :id")
    suspend fun setSyncToken(id: String, token: String, syncedAtMillis: Long)

    @Query("SELECT syncToken FROM calendars WHERE id = :id")
    suspend fun getSyncToken(id: String): String?

    @Query("DELETE FROM calendars WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Query("SELECT COUNT(*) FROM calendars WHERE deleted = 0 AND type = 'calendar'")
    suspend fun countCalendars(): Int

    @Query("UPDATE calendars SET dirty = 0, deleted = 0 WHERE id = :id")
    suspend fun clearSyncFlags(id: String)
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY addedAtMillis ASC")
    fun observeAll(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getById(id: String): AccountEntity?

    @Query("SELECT * FROM accounts ORDER BY addedAtMillis ASC LIMIT 1")
    suspend fun primary(): AccountEntity?

    @Upsert
    suspend fun upsert(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun delete(id: String)
}
