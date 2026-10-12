package com.example.jalicalendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jalicalendar.data.local.dao.AccountDao
import com.example.jalicalendar.data.local.dao.CalendarDao
import com.example.jalicalendar.data.local.dao.EventDao
import com.example.jalicalendar.data.local.dao.ReminderDao
import com.example.jalicalendar.data.local.dao.TaskDao
import com.example.jalicalendar.data.local.entity.AccountEntity
import com.example.jalicalendar.data.local.entity.CalendarEntity
import com.example.jalicalendar.data.local.entity.EventEntity
import com.example.jalicalendar.data.local.entity.ReminderEntity
import com.example.jalicalendar.data.local.entity.TaskEntity

/**
 * Room database. Schemas are exported to app/schemas (see ksp args in
 * build.gradle.kts) so future migrations can use room.schemaLocation + AutoMigration.
 */
@Database(
    entities = [
        EventEntity::class,
        CalendarEntity::class,
        ReminderEntity::class,
        TaskEntity::class,
        AccountEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class JalaliDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun calendarDao(): CalendarDao
    abstract fun reminderDao(): ReminderDao
    abstract fun taskDao(): TaskDao
    abstract fun accountDao(): AccountDao

    companion object {
        const val NAME = "jalali_calendar.db"
    }
}
