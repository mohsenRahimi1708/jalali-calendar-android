package com.example.jalicalendar.di

import android.content.Context
import androidx.room.Room
import com.example.jalicalendar.data.local.JalaliDatabase
import com.example.jalicalendar.data.local.dao.AccountDao
import com.example.jalicalendar.data.local.dao.CalendarDao
import com.example.jalicalendar.data.local.dao.EventDao
import com.example.jalicalendar.data.local.dao.ReminderDao
import com.example.jalicalendar.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JalaliDatabase =
        Room.databaseBuilder(context, JalaliDatabase::class.java, JalaliDatabase.NAME)
            // Destructive migration is acceptable only pre-release; replace with
            // explicit migrations once the schema ships (schema exported to app/schemas).
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides fun provideEventDao(db: JalaliDatabase): EventDao = db.eventDao()
    @Provides fun provideCalendarDao(db: JalaliDatabase): CalendarDao = db.calendarDao()
    @Provides fun provideReminderDao(db: JalaliDatabase): ReminderDao = db.reminderDao()
    @Provides fun provideTaskDao(db: JalaliDatabase): TaskDao = db.taskDao()
    @Provides fun provideAccountDao(db: JalaliDatabase): AccountDao = db.accountDao()
}
