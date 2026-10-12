package com.example.jalicalendar.domain.repo

import com.example.jalicalendar.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(listId: String): Flow<List<Task>>
    suspend fun upsert(task: Task): String
    suspend fun delete(task: Task)
    suspend fun findByRemoteId(remoteId: String): Task?
    suspend fun upsertSynced(task: Task) // pull path: no dirty flag, no local bump
    suspend fun pendingChanges(): List<Task>
    suspend fun markSynced(id: String, remoteId: String?, etag: String?, remoteUpdatedAtMillis: Long?)
    suspend fun purgeSyncedDeleted(id: String)
}
