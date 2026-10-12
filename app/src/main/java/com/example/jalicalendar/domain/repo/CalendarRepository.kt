package com.example.jalicalendar.domain.repo

import com.example.jalicalendar.domain.model.Calendar
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun observeCalendars(): Flow<List<Calendar>>
    fun observeSelectedCalendars(): Flow<List<Calendar>>
    suspend fun getCalendar(id: String): Calendar?
    suspend fun upsert(calendar: Calendar): String
    suspend fun delete(id: String)

    /** Toggle visibility without creating a pending sync change. */
    suspend fun setSelected(id: String, selected: Boolean)

    /** Calendars needing a push (local edits / deletions not yet on the server). */
    suspend fun pendingChanges(): List<Calendar>

    suspend fun getSyncToken(calendarId: String): String?
    suspend fun setSyncToken(calendarId: String, token: String, syncedAtMillis: Long)
    suspend fun seedLocalCalendar(): String // creates/returns the on-device calendar
}
