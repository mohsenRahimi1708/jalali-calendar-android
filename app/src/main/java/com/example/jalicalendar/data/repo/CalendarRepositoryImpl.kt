package com.example.jalicalendar.data.repo

import com.example.jalicalendar.data.local.dao.CalendarDao
import com.example.jalicalendar.data.local.toDomain
import com.example.jalicalendar.data.local.toEntity
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.repo.CalendarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepositoryImpl @Inject constructor(
    private val calendarDao: CalendarDao,
) : CalendarRepository {

    override fun observeCalendars(): Flow<List<Calendar>> =
        calendarDao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeSelectedCalendars(): Flow<List<Calendar>> =
        calendarDao.observeSelected().map { rows -> rows.map { it.toDomain() } }

    override suspend fun getCalendar(id: String): Calendar? = calendarDao.getById(id)?.toDomain()

    override suspend fun upsert(calendar: Calendar): String {
        val id = calendar.id.ifBlank { UUID.randomUUID().toString() }
        calendarDao.upsert(
            calendar.copy(
                id = id,
                localUpdatedAtMillis = System.currentTimeMillis(),
                dirty = calendar.remoteId != null, // local-only calendars have nothing to push
            ).toEntity(),
        )
        return id
    }

    override suspend fun delete(id: String) {
        val c = calendarDao.getById(id) ?: return
        if (c.remoteId == null) {
            calendarDao.hardDelete(id)
        } else {
            // Soft delete; the sync layer deletes remotely, then purges the row.
            calendarDao.upsert(c.copy(deleted = true, dirty = true, localUpdatedAtMillis = System.currentTimeMillis()))
        }
    }

    override suspend fun setSelected(id: String, selected: Boolean) =
        calendarDao.setSelected(id, selected)

    override suspend fun pendingChanges(): List<Calendar> =
        calendarDao.pendingChanges().map { it.toDomain() }

    override suspend fun getSyncToken(calendarId: String): String? =
        calendarDao.getSyncToken(calendarId)

    override suspend fun setSyncToken(calendarId: String, token: String, syncedAtMillis: Long) =
        calendarDao.setSyncToken(calendarId, token, syncedAtMillis)

    override suspend fun seedLocalCalendar(): String {
        val existing = calendarDao.getByType(Calendar.TYPE_CALENDAR).firstOrNull { it.id.startsWith(Calendar.LOCAL_ID_PREFIX) }
        if (existing != null) return existing.id
        val local = Calendar(
            id = Calendar.LOCAL_ID_PREFIX + UUID.randomUUID().toString(),
            accountId = null,
            remoteId = null,
            type = Calendar.TYPE_CALENDAR,
            title = "Local", // R.string title resolved by the UI layer if needed
            colorArgb = 0xFF6750A4.toInt(),
            timeZone = "Asia/Tehran",
            isPrimary = true,
            accessRole = "owner",
            selected = true,
            dirty = false,
        )
        calendarDao.upsert(local.toEntity())
        return local.id
    }
}
