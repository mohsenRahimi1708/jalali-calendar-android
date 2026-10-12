package com.example.jalicalendar.domain.model

/**
 * A Google Task. Lives in a task-list container (a [Calendar] with
 * type == TYPE_TASK_LIST). Due dates are civil dates stored as UTC midnight.
 */
data class Task(
    val id: String,
    val listId: String,
    val remoteId: String? = null,
    val title: String,
    val notes: String? = null,
    val dueDateMillis: Long? = null,
    val completed: Boolean = false,
    val completedAtMillis: Long? = null,
    val status: String = "needsAction", // needsAction | completed
    val position: String? = null,
    val etag: String? = null,
    val remoteUpdatedAtMillis: Long? = null,
    val localUpdatedAtMillis: Long = 0L,
    val dirty: Boolean = false,
    val deleted: Boolean = false,
)
