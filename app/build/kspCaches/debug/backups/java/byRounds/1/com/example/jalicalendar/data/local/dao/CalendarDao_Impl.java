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
import com.example.jalicalendar.data.local.entity.CalendarEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class CalendarDao_Impl implements CalendarDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfSetSelected;

  private final SharedSQLiteStatement __preparedStmtOfSetSyncToken;

  private final SharedSQLiteStatement __preparedStmtOfHardDelete;

  private final SharedSQLiteStatement __preparedStmtOfClearSyncFlags;

  private final EntityUpsertionAdapter<CalendarEntity> __upsertionAdapterOfCalendarEntity;

  public CalendarDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfSetSelected = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET selected = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetSyncToken = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET syncToken = ?, lastSyncedAtMillis = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfHardDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM calendars WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearSyncFlags = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE calendars SET dirty = 0, deleted = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfCalendarEntity = new EntityUpsertionAdapter<CalendarEntity>(new EntityInsertionAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `calendars` (`id`,`accountId`,`remoteId`,`type`,`title`,`description`,`colorArgb`,`timeZone`,`isPrimary`,`accessRole`,`hidden`,`selected`,`syncToken`,`lastSyncedAtMillis`,`localUpdatedAtMillis`,`dirty`,`deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getAccountId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAccountId());
        }
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getColorArgb());
        statement.bindString(8, entity.getTimeZone());
        final int _tmp = entity.isPrimary() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindString(10, entity.getAccessRole());
        final int _tmp_1 = entity.getHidden() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.getSelected() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        if (entity.getSyncToken() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSyncToken());
        }
        if (entity.getLastSyncedAtMillis() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastSyncedAtMillis());
        }
        statement.bindLong(15, entity.getLocalUpdatedAtMillis());
        final int _tmp_3 = entity.getDirty() ? 1 : 0;
        statement.bindLong(16, _tmp_3);
        final int _tmp_4 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(17, _tmp_4);
      }
    }, new EntityDeletionOrUpdateAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `calendars` SET `id` = ?,`accountId` = ?,`remoteId` = ?,`type` = ?,`title` = ?,`description` = ?,`colorArgb` = ?,`timeZone` = ?,`isPrimary` = ?,`accessRole` = ?,`hidden` = ?,`selected` = ?,`syncToken` = ?,`lastSyncedAtMillis` = ?,`localUpdatedAtMillis` = ?,`dirty` = ?,`deleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getAccountId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAccountId());
        }
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindLong(7, entity.getColorArgb());
        statement.bindString(8, entity.getTimeZone());
        final int _tmp = entity.isPrimary() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindString(10, entity.getAccessRole());
        final int _tmp_1 = entity.getHidden() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.getSelected() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        if (entity.getSyncToken() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSyncToken());
        }
        if (entity.getLastSyncedAtMillis() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastSyncedAtMillis());
        }
        statement.bindLong(15, entity.getLocalUpdatedAtMillis());
        final int _tmp_3 = entity.getDirty() ? 1 : 0;
        statement.bindLong(16, _tmp_3);
        final int _tmp_4 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(17, _tmp_4);
        statement.bindString(18, entity.getId());
      }
    });
  }

  @Override
  public Object setSelected(final String id, final boolean selected,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetSelected.acquire();
        int _argIndex = 1;
        final int _tmp = selected ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfSetSelected.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setSyncToken(final String id, final String token, final long syncedAtMillis,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetSyncToken.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, token);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, syncedAtMillis);
        _argIndex = 3;
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
          __preparedStmtOfSetSyncToken.release(_stmt);
        }
      }
    }, $completion);
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
  public Object clearSyncFlags(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSyncFlags.acquire();
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
          __preparedStmtOfClearSyncFlags.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsert(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfCalendarEntity.upsert(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEntity>> observeAll() {
    final String _sql = "SELECT * FROM calendars WHERE deleted = 0 ORDER BY isPrimary DESC, title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfAccessRole = CursorUtil.getColumnIndexOrThrow(_cursor, "accessRole");
          final int _cursorIndexOfHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "hidden");
          final int _cursorIndexOfSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "selected");
          final int _cursorIndexOfSyncToken = CursorUtil.getColumnIndexOrThrow(_cursor, "syncToken");
          final int _cursorIndexOfLastSyncedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final String _tmpTimeZone;
            _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            final boolean _tmpIsPrimary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp != 0;
            final String _tmpAccessRole;
            _tmpAccessRole = _cursor.getString(_cursorIndexOfAccessRole);
            final boolean _tmpHidden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHidden);
            _tmpHidden = _tmp_1 != 0;
            final boolean _tmpSelected;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_2 != 0;
            final String _tmpSyncToken;
            if (_cursor.isNull(_cursorIndexOfSyncToken)) {
              _tmpSyncToken = null;
            } else {
              _tmpSyncToken = _cursor.getString(_cursorIndexOfSyncToken);
            }
            final Long _tmpLastSyncedAtMillis;
            if (_cursor.isNull(_cursorIndexOfLastSyncedAtMillis)) {
              _tmpLastSyncedAtMillis = null;
            } else {
              _tmpLastSyncedAtMillis = _cursor.getLong(_cursorIndexOfLastSyncedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_3 != 0;
            final boolean _tmpDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_4 != 0;
            _item = new CalendarEntity(_tmpId,_tmpAccountId,_tmpRemoteId,_tmpType,_tmpTitle,_tmpDescription,_tmpColorArgb,_tmpTimeZone,_tmpIsPrimary,_tmpAccessRole,_tmpHidden,_tmpSelected,_tmpSyncToken,_tmpLastSyncedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Flow<List<CalendarEntity>> observeSelected() {
    final String _sql = "SELECT * FROM calendars WHERE deleted = 0 AND selected = 1 ORDER BY isPrimary DESC, title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfAccessRole = CursorUtil.getColumnIndexOrThrow(_cursor, "accessRole");
          final int _cursorIndexOfHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "hidden");
          final int _cursorIndexOfSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "selected");
          final int _cursorIndexOfSyncToken = CursorUtil.getColumnIndexOrThrow(_cursor, "syncToken");
          final int _cursorIndexOfLastSyncedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final String _tmpTimeZone;
            _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            final boolean _tmpIsPrimary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp != 0;
            final String _tmpAccessRole;
            _tmpAccessRole = _cursor.getString(_cursorIndexOfAccessRole);
            final boolean _tmpHidden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHidden);
            _tmpHidden = _tmp_1 != 0;
            final boolean _tmpSelected;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_2 != 0;
            final String _tmpSyncToken;
            if (_cursor.isNull(_cursorIndexOfSyncToken)) {
              _tmpSyncToken = null;
            } else {
              _tmpSyncToken = _cursor.getString(_cursorIndexOfSyncToken);
            }
            final Long _tmpLastSyncedAtMillis;
            if (_cursor.isNull(_cursorIndexOfLastSyncedAtMillis)) {
              _tmpLastSyncedAtMillis = null;
            } else {
              _tmpLastSyncedAtMillis = _cursor.getLong(_cursorIndexOfLastSyncedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_3 != 0;
            final boolean _tmpDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_4 != 0;
            _item = new CalendarEntity(_tmpId,_tmpAccountId,_tmpRemoteId,_tmpType,_tmpTitle,_tmpDescription,_tmpColorArgb,_tmpTimeZone,_tmpIsPrimary,_tmpAccessRole,_tmpHidden,_tmpSelected,_tmpSyncToken,_tmpLastSyncedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object getById(final String id, final Continuation<? super CalendarEntity> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntity>() {
      @Override
      @Nullable
      public CalendarEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfAccessRole = CursorUtil.getColumnIndexOrThrow(_cursor, "accessRole");
          final int _cursorIndexOfHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "hidden");
          final int _cursorIndexOfSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "selected");
          final int _cursorIndexOfSyncToken = CursorUtil.getColumnIndexOrThrow(_cursor, "syncToken");
          final int _cursorIndexOfLastSyncedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final CalendarEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final String _tmpTimeZone;
            _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            final boolean _tmpIsPrimary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp != 0;
            final String _tmpAccessRole;
            _tmpAccessRole = _cursor.getString(_cursorIndexOfAccessRole);
            final boolean _tmpHidden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHidden);
            _tmpHidden = _tmp_1 != 0;
            final boolean _tmpSelected;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_2 != 0;
            final String _tmpSyncToken;
            if (_cursor.isNull(_cursorIndexOfSyncToken)) {
              _tmpSyncToken = null;
            } else {
              _tmpSyncToken = _cursor.getString(_cursorIndexOfSyncToken);
            }
            final Long _tmpLastSyncedAtMillis;
            if (_cursor.isNull(_cursorIndexOfLastSyncedAtMillis)) {
              _tmpLastSyncedAtMillis = null;
            } else {
              _tmpLastSyncedAtMillis = _cursor.getLong(_cursorIndexOfLastSyncedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_3 != 0;
            final boolean _tmpDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_4 != 0;
            _result = new CalendarEntity(_tmpId,_tmpAccountId,_tmpRemoteId,_tmpType,_tmpTitle,_tmpDescription,_tmpColorArgb,_tmpTimeZone,_tmpIsPrimary,_tmpAccessRole,_tmpHidden,_tmpSelected,_tmpSyncToken,_tmpLastSyncedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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

  @Override
  public Object getByType(final String type,
      final Continuation<? super List<CalendarEntity>> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE type = ? AND deleted = 0 ORDER BY isPrimary DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfAccessRole = CursorUtil.getColumnIndexOrThrow(_cursor, "accessRole");
          final int _cursorIndexOfHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "hidden");
          final int _cursorIndexOfSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "selected");
          final int _cursorIndexOfSyncToken = CursorUtil.getColumnIndexOrThrow(_cursor, "syncToken");
          final int _cursorIndexOfLastSyncedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final String _tmpTimeZone;
            _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            final boolean _tmpIsPrimary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp != 0;
            final String _tmpAccessRole;
            _tmpAccessRole = _cursor.getString(_cursorIndexOfAccessRole);
            final boolean _tmpHidden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHidden);
            _tmpHidden = _tmp_1 != 0;
            final boolean _tmpSelected;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_2 != 0;
            final String _tmpSyncToken;
            if (_cursor.isNull(_cursorIndexOfSyncToken)) {
              _tmpSyncToken = null;
            } else {
              _tmpSyncToken = _cursor.getString(_cursorIndexOfSyncToken);
            }
            final Long _tmpLastSyncedAtMillis;
            if (_cursor.isNull(_cursorIndexOfLastSyncedAtMillis)) {
              _tmpLastSyncedAtMillis = null;
            } else {
              _tmpLastSyncedAtMillis = _cursor.getLong(_cursorIndexOfLastSyncedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_3 != 0;
            final boolean _tmpDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_4 != 0;
            _item = new CalendarEntity(_tmpId,_tmpAccountId,_tmpRemoteId,_tmpType,_tmpTitle,_tmpDescription,_tmpColorArgb,_tmpTimeZone,_tmpIsPrimary,_tmpAccessRole,_tmpHidden,_tmpSelected,_tmpSyncToken,_tmpLastSyncedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object pendingChanges(final Continuation<? super List<CalendarEntity>> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE dirty = 1 OR deleted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "accountId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfAccessRole = CursorUtil.getColumnIndexOrThrow(_cursor, "accessRole");
          final int _cursorIndexOfHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "hidden");
          final int _cursorIndexOfSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "selected");
          final int _cursorIndexOfSyncToken = CursorUtil.getColumnIndexOrThrow(_cursor, "syncToken");
          final int _cursorIndexOfLastSyncedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getString(_cursorIndexOfAccountId);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpColorArgb;
            _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            final String _tmpTimeZone;
            _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            final boolean _tmpIsPrimary;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp != 0;
            final String _tmpAccessRole;
            _tmpAccessRole = _cursor.getString(_cursorIndexOfAccessRole);
            final boolean _tmpHidden;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHidden);
            _tmpHidden = _tmp_1 != 0;
            final boolean _tmpSelected;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSelected);
            _tmpSelected = _tmp_2 != 0;
            final String _tmpSyncToken;
            if (_cursor.isNull(_cursorIndexOfSyncToken)) {
              _tmpSyncToken = null;
            } else {
              _tmpSyncToken = _cursor.getString(_cursorIndexOfSyncToken);
            }
            final Long _tmpLastSyncedAtMillis;
            if (_cursor.isNull(_cursorIndexOfLastSyncedAtMillis)) {
              _tmpLastSyncedAtMillis = null;
            } else {
              _tmpLastSyncedAtMillis = _cursor.getLong(_cursorIndexOfLastSyncedAtMillis);
            }
            final long _tmpLocalUpdatedAtMillis;
            _tmpLocalUpdatedAtMillis = _cursor.getLong(_cursorIndexOfLocalUpdatedAtMillis);
            final boolean _tmpDirty;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfDirty);
            _tmpDirty = _tmp_3 != 0;
            final boolean _tmpDeleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDeleted);
            _tmpDeleted = _tmp_4 != 0;
            _item = new CalendarEntity(_tmpId,_tmpAccountId,_tmpRemoteId,_tmpType,_tmpTitle,_tmpDescription,_tmpColorArgb,_tmpTimeZone,_tmpIsPrimary,_tmpAccessRole,_tmpHidden,_tmpSelected,_tmpSyncToken,_tmpLastSyncedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object getSyncToken(final String id, final Continuation<? super String> $completion) {
    final String _sql = "SELECT syncToken FROM calendars WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
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

  @Override
  public Object countCalendars(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM calendars WHERE deleted = 0 AND type = 'calendar'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
