package com.example.jalicalendar.data.repo

import com.example.jalicalendar.data.local.dao.TaskDao
import com.example.jalicalendar.data.local.toDomain
import com.example.jalicalendar.data.local.toEntity
import com.example.jalicalendar.domain.model.Task
import com.example.jalicalendar.domain.repo.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
) : TaskRepository {

    override fun observeTasks(listId: String): Flow<List<Task>> =
        taskDao.observeForList(listId).map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsert(task: Task): String {
        val id = task.id.ifBlank { UUID.randomUUID().toString() }
        taskDao.upsert(
            task.copy(
                id = id,
                localUpdatedAtMillis = System.currentTimeMillis(),
                dirty = true,
                deleted = false,
            ).toEntity(),
        )
        return id
    }

    override suspend fun delete(task: Task) {
        if (task.remoteId == null) {
            taskDao.hardDelete(task.id)
        } else {
            taskDao.upsert(
                task.copy(deleted = true, dirty = true, localUpdatedAtMillis = System.currentTimeMillis()).toEntity(),
            )
        }
    }

    override suspend fun findByRemoteId(remoteId: String): Task? =
        taskDao.findByRemoteId(remoteId)?.toDomain()

    override suspend fun upsertSynced(task: Task) {
        taskDao.upsert(task.copy(dirty = false, deleted = task.deleted).toEntity())
    }

    override suspend fun pendingChanges(): List<Task> =
        taskDao.pendingChanges().map { it.toDomain() }

    override suspend fun markSynced(id: String, remoteId: String?, etag: String?, remoteUpdatedAtMillis: Long?) =
        taskDao.markSynced(id, remoteId, etag, remoteUpdatedAtMillis)

    override suspend fun purgeSyncedDeleted(id: String) = taskDao.purgeSyncedDeleted(id)
}
