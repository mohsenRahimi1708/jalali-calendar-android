package com.example.jalicalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.jalicalendar.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query(
        """
        SELECT * FROM tasks
        WHERE listId = :listId AND deleted = 0
        ORDER BY (status = 'completed') ASC, ifnull(position, '') ASC, ifnull(dueDateMillis, 0) ASC
        """,
    )
    fun observeForList(listId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dirty = 1 OR deleted = 1")
    suspend fun pendingChanges(): List<TaskEntity>

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Query(
        """
        UPDATE tasks SET remoteId = :remoteId, etag = :etag,
            remoteUpdatedAtMillis = :remoteUpdatedAtMillis, dirty = 0, deleted = 0
        WHERE id = :id
        """,
    )
    suspend fun markSynced(id: String, remoteId: String?, etag: String?, remoteUpdatedAtMillis: Long?)

    @Query("DELETE FROM tasks WHERE id = :id AND deleted = 1")
    suspend fun purgeSyncedDeleted(id: String)


    @Query("SELECT * FROM tasks WHERE remoteId = :remoteId LIMIT 1")
    suspend fun findByRemoteId(remoteId: String): TaskEntity?
}
