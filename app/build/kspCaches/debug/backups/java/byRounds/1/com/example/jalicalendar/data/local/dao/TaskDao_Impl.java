package com.example.jalicalendar.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.jalicalendar.data.local.entity.TaskEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfHardDelete;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfPurgeSyncedDeleted;

  private final EntityUpsertionAdapter<TaskEntity> __upsertionAdapterOfTaskEntity;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfHardDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE tasks SET remoteId = ?, etag = ?,\n"
                + "            remoteUpdatedAtMillis = ?, dirty = 0, deleted = 0\n"
                + "        WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfPurgeSyncedDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ? AND deleted = 1";
        return _query;
      }
    };
    this.__upsertionAdapterOfTaskEntity = new EntityUpsertionAdapter<TaskEntity>(new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `tasks` (`id`,`listId`,`remoteId`,`title`,`notes`,`dueDateMillis`,`completed`,`completedAtMillis`,`status`,`position`,`etag`,`remoteUpdatedAtMillis`,`localUpdatedAtMillis`,`dirty`,`deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getListId());
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getTitle());
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        if (entity.getDueDateMillis() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDateMillis());
        }
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getCompletedAtMillis() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCompletedAtMillis());
        }
        statement.bindString(9, entity.getStatus());
        if (entity.getPosition() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPosition());
        }
        if (entity.getEtag() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEtag());
        }
        if (entity.getRemoteUpdatedAtMillis() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getRemoteUpdatedAtMillis());
        }
        statement.bindLong(13, entity.getLocalUpdatedAtMillis());
        final int _tmp_1 = entity.getDirty() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        final int _tmp_2 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(15, _tmp_2);
      }
    }, new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `tasks` SET `id` = ?,`listId` = ?,`remoteId` = ?,`title` = ?,`notes` = ?,`dueDateMillis` = ?,`completed` = ?,`completedAtMillis` = ?,`status` = ?,`position` = ?,`etag` = ?,`remoteUpdatedAtMillis` = ?,`localUpdatedAtMillis` = ?,`dirty` = ?,`deleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getListId());
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getTitle());
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        if (entity.getDueDateMillis() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueDateMillis());
        }
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getCompletedAtMillis() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCompletedAtMillis());
        }
        statement.bindString(9, entity.getStatus());
        if (entity.getPosition() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPosition());
        }
        if (entity.getEtag() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEtag());
        }
        if (entity.getRemoteUpdatedAtMillis() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getRemoteUpdatedAtMillis());
        }
        statement.bindLong(13, entity.getLocalUpdatedAtMillis());
        final int _tmp_1 = entity.getDirty() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        final int _tmp_2 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(15, _tmp_2);
        statement.bindString(16, entity.getId());
      }
    });
  }

  @Override
  public Object hardDelete(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfHardDelete.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfHardDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markSynced(final String id, final String remoteId, final String etag,
      final Long remoteUpdatedAtMillis, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
        int _argIndex = 1;
        if (remoteId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, remoteId);
        }
        _argIndex = 2;
        if (etag == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, etag);
        }
        _argIndex = 3;
        if (remoteUpdatedAtMillis == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, remoteUpdatedAtMillis);
        }
        _argIndex = 4;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object purgeSyncedDeleted(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfPurgeSyncedDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfPurgeSyncedDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsert(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfTaskEntity.upsert(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TaskEntity>> observeForList(final String listId) {
    final String _sql = "\n"
            + "        SELECT * FROM tasks\n"
            + "        WHERE listId = ? AND deleted = 0\n"
            + "        ORDER BY (status = 'completed') ASC, ifnull(position, '') ASC, ifnull(dueDateMillis, 0) ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, listId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfListId = CursorUtil.getColumnIndexOrThrow(_cursor, "listId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfDueDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateMillis");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpListId;
            _tmpListId = _cursor.getString(_cursorIndexOfListId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpDueDateMillis;
            if (_cursor.isNull(_cursorIndexOfDueDateMillis)) {
              _tmpDueDateMillis = null;
            } else {
              _tmpDueDateMillis = _cursor.getLong(_cursorIndexOfDueDateMillis);
            }
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final Long _tmpCompletedAtMillis;
            if (_cursor.isNull(_cursorIndexOfCompletedAtMillis)) {
              _tmpCompletedAtMillis = null;
            } else {
              _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPosition;
            if (_cursor.isNull(_cursorIndexOfPosition)) {
              _tmpPosition = null;
            } else {
              _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            }
            final String _tmpEtag;
            if (_cursor.isNull(_cursorIndexOfEtag)) {
              _tmpEtag = null;
            } else {
              _tmpEtag = _cursor.getString(_cursorIndexOfEtag);
            }
            final Long _tmpRemoteUpdatedAtMillis;
            if (_cursor.isNull(_cursorIndexOfRemoteUpdatedAtMillis)) {
              _tmpRemoteUpdatedAtMillis = null;
            } else {
              _tmpRemoteUpdatedAtMillis = _cursor.getLong(_cursorIndexOfRemoteUpdatedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_1 != 0;
            final boolean _tmpDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_2 != 0;
            _item = new TaskEntity(_tmpId,_tmpListId,_tmpRemoteId,_tmpTitle,_tmpNotes,_tmpDueDateMillis,_tmpCompleted,_tmpCompletedAtMillis,_tmpStatus,_tmpPosition,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object pendingChanges(final Continuation<? super List<TaskEntity>> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE dirty = 1 OR deleted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfListId = CursorUtil.getColumnIndexOrThrow(_cursor, "listId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfDueDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateMillis");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpListId;
            _tmpListId = _cursor.getString(_cursorIndexOfListId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpDueDateMillis;
            if (_cursor.isNull(_cursorIndexOfDueDateMillis)) {
              _tmpDueDateMillis = null;
            } else {
              _tmpDueDateMillis = _cursor.getLong(_cursorIndexOfDueDateMillis);
            }
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final Long _tmpCompletedAtMillis;
            if (_cursor.isNull(_cursorIndexOfCompletedAtMillis)) {
              _tmpCompletedAtMillis = null;
            } else {
              _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPosition;
            if (_cursor.isNull(_cursorIndexOfPosition)) {
              _tmpPosition = null;
            } else {
              _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            }
            final String _tmpEtag;
            if (_cursor.isNull(_cursorIndexOfEtag)) {
              _tmpEtag = null;
            } else {
              _tmpEtag = _cursor.getString(_cursorIndexOfEtag);
            }
            final Long _tmpRemoteUpdatedAtMillis;
            if (_cursor.isNull(_cursorIndexOfRemoteUpdatedAtMillis)) {
              _tmpRemoteUpdatedAtMillis = null;
            } else {
              _tmpRemoteUpdatedAtMillis = _cursor.getLong(_cursorIndexOfRemoteUpdatedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_1 != 0;
            final boolean _tmpDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_2 != 0;
            _item = new TaskEntity(_tmpId,_tmpListId,_tmpRemoteId,_tmpTitle,_tmpNotes,_tmpDueDateMillis,_tmpCompleted,_tmpCompletedAtMillis,_tmpStatus,_tmpPosition,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByRemoteId(final String remoteId,
      final Continuation<? super TaskEntity> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE remoteId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, remoteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TaskEntity>() {
      @Override
      @Nullable
      public TaskEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfListId = CursorUtil.getColumnIndexOrThrow(_cursor, "listId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfDueDateMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateMillis");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final TaskEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpListId;
            _tmpListId = _cursor.getString(_cursorIndexOfListId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Long _tmpDueDateMillis;
            if (_cursor.isNull(_cursorIndexOfDueDateMillis)) {
              _tmpDueDateMillis = null;
            } else {
              _tmpDueDateMillis = _cursor.getLong(_cursorIndexOfDueDateMillis);
            }
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final Long _tmpCompletedAtMillis;
            if (_cursor.isNull(_cursorIndexOfCompletedAtMillis)) {
              _tmpCompletedAtMillis = null;
            } else {
              _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPosition;
            if (_cursor.isNull(_cursorIndexOfPosition)) {
              _tmpPosition = null;
            } else {
              _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            }
            final String _tmpEtag;
            if (_cursor.isNull(_cursorIndexOfEtag)) {
              _tmpEtag = null;
            } else {
              _tmpEtag = _cursor.getString(_cursorIndexOfEtag);
            }
            final Long _tmpRemoteUpdatedAtMillis;
            if (_cursor.isNull(_cursorIndexOfRemoteUpdatedAtMillis)) {
              _tmpRemoteUpdatedAtMillis = null;
            } else {
              _tmpRemoteUpdatedAtMillis = _cursor.getLong(_cursorIndexOfRemoteUpdatedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_1 != 0;
            final boolean _tmpDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_2 != 0;
            _result = new TaskEntity(_tmpId,_tmpListId,_tmpRemoteId,_tmpTitle,_tmpNotes,_tmpDueDateMillis,_tmpCompleted,_tmpCompletedAtMillis,_tmpStatus,_tmpPosition,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
