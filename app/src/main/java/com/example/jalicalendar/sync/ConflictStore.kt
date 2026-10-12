package com.example.jalicalendar.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class ConflictSource { PUSH, PULL }

/** A user-visible edit collision awaiting a decision. */
data class ConflictItem(
    val eventId: String,
    val title: String,
    val source: ConflictSource,
    val raisedAtMillis: Long = System.currentTimeMillis(),
)

/**
 * Pending conflicts for PROMPT policy. In-memory + observed by the UI dialog;
 * a conflict that is never resolved simply keeps the local copy as-is on the
 * next pass (pull path) or retries the push (push path) — no data is lost.
 */
@Singleton
class ConflictStore @Inject constructor() {

    private val _conflicts = MutableStateFlow<List<ConflictItem>>(emptyList())
    val conflicts: StateFlow<List<ConflictItem>> = _conflicts.asStateFlow()

    fun record(eventId: String, title: String, source: ConflictSource) {
        val item = ConflictItem(eventId, title, source)
        _conflicts.value = (_conflicts.value.filterNot { it.eventId == eventId } + item)
    }

    fun resolve(eventId: String) {
        _conflicts.value = _conflicts.value.filterNot { it.eventId == eventId }
    }

    fun clear() { _conflicts.value = emptyList() }
}
