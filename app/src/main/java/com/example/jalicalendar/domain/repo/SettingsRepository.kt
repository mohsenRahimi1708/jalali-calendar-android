package com.example.jalicalendar.domain.repo

import com.example.jalicalendar.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

/** Typed access to user settings; writes go through [update] so partial updates stay atomic. */
interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun current(): AppSettings
    suspend fun update(transform: (AppSettings) -> AppSettings)
}
