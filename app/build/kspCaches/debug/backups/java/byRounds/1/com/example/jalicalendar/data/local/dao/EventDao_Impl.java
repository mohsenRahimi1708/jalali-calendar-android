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
import com.example.jalicalendar.data.local.entity.EventEntity;
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
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfHardDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForCalendar;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfPurgeSyncedDeleted;

  private final SharedSQLiteStatement __preparedStmtOfClearSyncFlags;

  private final EntityUpsertionAdapter<EventEntity> __upsertionAdapterOfEventEntity;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfHardDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM events WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForCalendar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM events WHERE calendarId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE events SET remoteId = ?, etag = ?,\n"
                + "            remoteUpdatedAtMillis = ?,\n"
                + "            dirty = 0, deleted = 0, calendarId = ifnull(?, calendarId)\n"
                + "        WHERE id = ?\n"
                + "        ";
        return _query;
      }
    };
    this.__preparedStmtOfPurgeSyncedDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM events WHERE id = ? AND deleted = 1";
        return _query;
      }
    };
    this.__preparedStmtOfClearSyncFlags = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE events SET dirty = 0, deleted = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfEventEntity = new EntityUpsertionAdapter<EventEntity>(new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `events` (`id`,`calendarId`,`remoteId`,`title`,`description`,`location`,`startUtcMillis`,`endUtcMillis`,`allDay`,`timeZone`,`rrule`,`rdate`,`exdate`,`recurringEventId`,`originalStartUtcMillis`,`status`,`attendeesJson`,`organizer`,`colorArgb`,`visibility`,`transparency`,`etag`,`remoteUpdatedAtMillis`,`localUpdatedAtMillis`,`dirty`,`deleted`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCalendarId());
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocation());
        }
        statement.bindLong(7, entity.getStartUtcMillis());
        statement.bindLong(8, entity.getEndUtcMillis());
        final int _tmp = entity.getAllDay() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getTimeZone() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTimeZone());
        }
        if (entity.getRrule() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRrule());
        }
        if (entity.getRdate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getRdate());
        }
        if (entity.getExdate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExdate());
        }
        if (entity.getRecurringEventId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getRecurringEventId());
        }
        if (entity.getOriginalStartUtcMillis() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getOriginalStartUtcMillis());
        }
        statement.bindString(16, entity.getStatus());
        if (entity.getAttendeesJson() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getAttendeesJson());
        }
        if (entity.getOrganizer() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getOrganizer());
        }
        if (entity.getColorArgb() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getColorArgb());
        }
        if (entity.getVisibility() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getVisibility());
        }
        if (entity.getTransparency() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTransparency());
        }
        if (entity.getEtag() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getEtag());
        }
        if (entity.getRemoteUpdatedAtMillis() == null) {
          statement.bindNull(23);
        } else {
          statement.bindLong(23, entity.getRemoteUpdatedAtMillis());
        }
        statement.bindLong(24, entity.getLocalUpdatedAtMillis());
        final int _tmp_1 = entity.getDirty() ? 1 : 0;
        statement.bindLong(25, _tmp_1);
        final int _tmp_2 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(26, _tmp_2);
      }
    }, new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `events` SET `id` = ?,`calendarId` = ?,`remoteId` = ?,`title` = ?,`description` = ?,`location` = ?,`startUtcMillis` = ?,`endUtcMillis` = ?,`allDay` = ?,`timeZone` = ?,`rrule` = ?,`rdate` = ?,`exdate` = ?,`recurringEventId` = ?,`originalStartUtcMillis` = ?,`status` = ?,`attendeesJson` = ?,`organizer` = ?,`colorArgb` = ?,`visibility` = ?,`transparency` = ?,`etag` = ?,`remoteUpdatedAtMillis` = ?,`localUpdatedAtMillis` = ?,`dirty` = ?,`deleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCalendarId());
        if (entity.getRemoteId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRemoteId());
        }
        statement.bindString(4, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocation());
        }
        statement.bindLong(7, entity.getStartUtcMillis());
        statement.bindLong(8, entity.getEndUtcMillis());
        final int _tmp = entity.getAllDay() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getTimeZone() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTimeZone());
        }
        if (entity.getRrule() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRrule());
        }
        if (entity.getRdate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getRdate());
        }
        if (entity.getExdate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExdate());
        }
        if (entity.getRecurringEventId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getRecurringEventId());
        }
        if (entity.getOriginalStartUtcMillis() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getOriginalStartUtcMillis());
        }
        statement.bindString(16, entity.getStatus());
        if (entity.getAttendeesJson() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getAttendeesJson());
        }
        if (entity.getOrganizer() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getOrganizer());
        }
        if (entity.getColorArgb() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getColorArgb());
        }
        if (entity.getVisibility() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getVisibility());
        }
        if (entity.getTransparency() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTransparency());
        }
        if (entity.getEtag() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getEtag());
        }
        if (entity.getRemoteUpdatedAtMillis() == null) {
          statement.bindNull(23);
        } else {
          statement.bindLong(23, entity.getRemoteUpdatedAtMillis());
        }
        statement.bindLong(24, entity.getLocalUpdatedAtMillis());
        final int _tmp_1 = entity.getDirty() ? 1 : 0;
        statement.bindLong(25, _tmp_1);
        final int _tmp_2 = entity.getDeleted() ? 1 : 0;
        statement.bindLong(26, _tmp_2);
        statement.bindString(27, entity.getId());
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
  public Object deleteForCalendar(final String calendarId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForCalendar.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, calendarId);
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
          __preparedStmtOfDeleteForCalendar.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markSynced(final String id, final String remoteId, final String etag,
      final Long remoteUpdatedAtMillis, final String calendarId,
      final Continuation<? super Unit> $completion) {
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
        if (calendarId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, calendarId);
        }
        _argIndex = 5;
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
  public Object upsert(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfEventEntity.upsert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> observeBetween(final long startInclusive,
      final long endExclusive) {
    final String _sql = "\n"
            + "        SELECT * FROM events\n"
            + "        WHERE deleted = 0 AND startUtcMillis < ? AND endUtcMillis > ?\n"
            + "        ORDER BY startUtcMillis ASC, endUtcMillis ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, endExclusive);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startInclusive);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _item = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Flow<EventEntity> observeById(final String id) {
    final String _sql = "SELECT * FROM events WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _result = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
          } else {
            _result = null;
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
  public Object getById(final String id, final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM events WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _result = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object getByRemoteId(final String calendarId, final String remoteId,
      final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM events WHERE calendarId = ? AND remoteId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, calendarId);
    _argIndex = 2;
    _statement.bindString(_argIndex, remoteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _result = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object search(final String query,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM events\n"
            + "        WHERE deleted = 0 AND (\n"
            + "            title LIKE '%' || ? || '%'\n"
            + "            OR ifnull(description, '') LIKE '%' || ? || '%'\n"
            + "            OR ifnull(location, '') LIKE '%' || ? || '%'\n"
            + "        )\n"
            + "        ORDER BY startUtcMillis ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _item = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object pendingChanges(final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM events WHERE dirty = 1 OR deleted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _item = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
      final Continuation<? super EventEntity> $completion) {
    final String _sql = "SELECT * FROM events WHERE remoteId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, remoteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EventEntity>() {
      @Override
      @Nullable
      public EventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final EventEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _result = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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
  public Object upcomingWithReminders(final long now,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT DISTINCT e.* FROM events e\n"
            + "        INNER JOIN reminders r ON r.eventId = e.id\n"
            + "        WHERE e.deleted = 0 AND e.startUtcMillis >= ?\n"
            + "        ORDER BY e.startUtcMillis ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, now);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarId");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startUtcMillis");
          final int _cursorIndexOfEndUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endUtcMillis");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfTimeZone = CursorUtil.getColumnIndexOrThrow(_cursor, "timeZone");
          final int _cursorIndexOfRrule = CursorUtil.getColumnIndexOrThrow(_cursor, "rrule");
          final int _cursorIndexOfRdate = CursorUtil.getColumnIndexOrThrow(_cursor, "rdate");
          final int _cursorIndexOfExdate = CursorUtil.getColumnIndexOrThrow(_cursor, "exdate");
          final int _cursorIndexOfRecurringEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringEventId");
          final int _cursorIndexOfOriginalStartUtcMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "originalStartUtcMillis");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfAttendeesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "attendeesJson");
          final int _cursorIndexOfOrganizer = CursorUtil.getColumnIndexOrThrow(_cursor, "organizer");
          final int _cursorIndexOfColorArgb = CursorUtil.getColumnIndexOrThrow(_cursor, "colorArgb");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfTransparency = CursorUtil.getColumnIndexOrThrow(_cursor, "transparency");
          final int _cursorIndexOfEtag = CursorUtil.getColumnIndexOrThrow(_cursor, "etag");
          final int _cursorIndexOfRemoteUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAtMillis");
          final int _cursorIndexOfLocalUpdatedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "localUpdatedAtMillis");
          final int _cursorIndexOfDirty = CursorUtil.getColumnIndexOrThrow(_cursor, "dirty");
          final int _cursorIndexOfDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "deleted");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCalendarId;
            _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final long _tmpStartUtcMillis;
            _tmpStartUtcMillis = _cursor.getLong(_cursorIndexOfStartUtcMillis);
            final long _tmpEndUtcMillis;
            _tmpEndUtcMillis = _cursor.getLong(_cursorIndexOfEndUtcMillis);
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpTimeZone;
            if (_cursor.isNull(_cursorIndexOfTimeZone)) {
              _tmpTimeZone = null;
            } else {
              _tmpTimeZone = _cursor.getString(_cursorIndexOfTimeZone);
            }
            final String _tmpRrule;
            if (_cursor.isNull(_cursorIndexOfRrule)) {
              _tmpRrule = null;
            } else {
              _tmpRrule = _cursor.getString(_cursorIndexOfRrule);
            }
            final String _tmpRdate;
            if (_cursor.isNull(_cursorIndexOfRdate)) {
              _tmpRdate = null;
            } else {
              _tmpRdate = _cursor.getString(_cursorIndexOfRdate);
            }
            final String _tmpExdate;
            if (_cursor.isNull(_cursorIndexOfExdate)) {
              _tmpExdate = null;
            } else {
              _tmpExdate = _cursor.getString(_cursorIndexOfExdate);
            }
            final String _tmpRecurringEventId;
            if (_cursor.isNull(_cursorIndexOfRecurringEventId)) {
              _tmpRecurringEventId = null;
            } else {
              _tmpRecurringEventId = _cursor.getString(_cursorIndexOfRecurringEventId);
            }
            final Long _tmpOriginalStartUtcMillis;
            if (_cursor.isNull(_cursorIndexOfOriginalStartUtcMillis)) {
              _tmpOriginalStartUtcMillis = null;
            } else {
              _tmpOriginalStartUtcMillis = _cursor.getLong(_cursorIndexOfOriginalStartUtcMillis);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpAttendeesJson;
            if (_cursor.isNull(_cursorIndexOfAttendeesJson)) {
              _tmpAttendeesJson = null;
            } else {
              _tmpAttendeesJson = _cursor.getString(_cursorIndexOfAttendeesJson);
            }
            final String _tmpOrganizer;
            if (_cursor.isNull(_cursorIndexOfOrganizer)) {
              _tmpOrganizer = null;
            } else {
              _tmpOrganizer = _cursor.getString(_cursorIndexOfOrganizer);
            }
            final Integer _tmpColorArgb;
            if (_cursor.isNull(_cursorIndexOfColorArgb)) {
              _tmpColorArgb = null;
            } else {
              _tmpColorArgb = _cursor.getInt(_cursorIndexOfColorArgb);
            }
            final String _tmpVisibility;
            if (_cursor.isNull(_cursorIndexOfVisibility)) {
              _tmpVisibility = null;
            } else {
              _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            }
            final String _tmpTransparency;
            if (_cursor.isNull(_cursorIndexOfTransparency)) {
              _tmpTransparency = null;
            } else {
              _tmpTransparency = _cursor.getString(_cursorIndexOfTransparency);
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
            _item = new EventEntity(_tmpId,_tmpCalendarId,_tmpRemoteId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpStartUtcMillis,_tmpEndUtcMillis,_tmpAllDay,_tmpTimeZone,_tmpRrule,_tmpRdate,_tmpExdate,_tmpRecurringEventId,_tmpOriginalStartUtcMillis,_tmpStatus,_tmpAttendeesJson,_tmpOrganizer,_tmpColorArgb,_tmpVisibility,_tmpTransparency,_tmpEtag,_tmpRemoteUpdatedAtMillis,_tmpLocalUpdatedAtMillis,_tmpDirty,_tmpDeleted);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
