package com.example.jalicalendar.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class SyncPhase { IDLE, RUNNING }

/** Observable sync state for the UI (status pill / snackbar). */
@Singleton
class SyncStatusStore @Inject constructor() {

    private val _phase = MutableStateFlow(SyncPhase.IDLE)
    val phase: StateFlow<SyncPhase> = _phase.asStateFlow()

    private val _lastSyncAtMillis = MutableStateFlow<Long?>(null)
    val lastSyncAtMillis: StateFlow<Long?> = _lastSyncAtMillis.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    fun setRunning() { _phase.value = SyncPhase.RUNNING; _lastError.value = null }
    fun setSucceeded() { _phase.value = SyncPhase.IDLE; _lastSyncAtMillis.value = System.currentTimeMillis() }
    fun setFailed(message: String) { _phase.value = SyncPhase.IDLE; _lastError.value = message }
}
