package com.example.jalicalendar.di

import com.example.jalicalendar.data.prefs.SettingsRepositoryImpl
import com.example.jalicalendar.data.repo.AccountRepositoryImpl
import com.example.jalicalendar.data.repo.CalendarRepositoryImpl
import com.example.jalicalendar.data.repo.EventRepositoryImpl
import com.example.jalicalendar.data.repo.TaskRepositoryImpl
import com.example.jalicalendar.domain.repo.AccountRepository
import com.example.jalicalendar.domain.repo.CalendarRepository
import com.example.jalicalendar.domain.repo.EventRepository
import com.example.jalicalendar.domain.repo.SettingsRepository
import com.example.jalicalendar.domain.repo.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
    @Binds abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
    @Binds abstract fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository
    @Binds abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
    @Binds abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository
}
