package com.example.jalicalendar.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.jalicalendar.data.local.dao.AccountDao;
import com.example.jalicalendar.data.local.dao.AccountDao_Impl;
import com.example.jalicalendar.data.local.dao.CalendarDao;
import com.example.jalicalendar.data.local.dao.CalendarDao_Impl;
import com.example.jalicalendar.data.local.dao.EventDao;
import com.example.jalicalendar.data.local.dao.EventDao_Impl;
import com.example.jalicalendar.data.local.dao.ReminderDao;
import com.example.jalicalendar.data.local.dao.ReminderDao_Impl;
import com.example.jalicalendar.data.local.dao.TaskDao;
import com.example.jalicalendar.data.local.dao.TaskDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JalaliDatabase_Impl extends JalaliDatabase {
  private volatile EventDao _eventDao;

  private volatile CalendarDao _calendarDao;

  private volatile ReminderDao _reminderDao;

  private volatile TaskDao _taskDao;

  private volatile AccountDao _accountDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` TEXT NOT NULL, `calendarId` TEXT NOT NULL, `remoteId` TEXT, `title` TEXT NOT NULL, `description` TEXT, `location` TEXT, `startUtcMillis` INTEGER NOT NULL, `endUtcMillis` INTEGER NOT NULL, `allDay` INTEGER NOT NULL, `timeZone` TEXT, `rrule` TEXT, `rdate` TEXT, `exdate` TEXT, `recurringEventId` TEXT, `originalStartUtcMillis` INTEGER, `status` TEXT NOT NULL, `attendeesJson` TEXT, `organizer` TEXT, `colorArgb` INTEGER, `visibility` TEXT, `transparency` TEXT, `etag` TEXT, `remoteUpdatedAtMillis` INTEGER, `localUpdatedAtMillis` INTEGER NOT NULL, `dirty` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_events_calendarId_startUtcMillis` ON `events` (`calendarId`, `startUtcMillis`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_events_calendarId_remoteId` ON `events` (`calendarId`, `remoteId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_events_dirty` ON `events` (`dirty`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_events_recurringEventId` ON `events` (`recurringEventId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `calendars` (`id` TEXT NOT NULL, `accountId` TEXT, `remoteId` TEXT, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `colorArgb` INTEGER NOT NULL, `timeZone` TEXT NOT NULL, `isPrimary` INTEGER NOT NULL, `accessRole` TEXT NOT NULL, `hidden` INTEGER NOT NULL, `selected` INTEGER NOT NULL, `syncToken` TEXT, `lastSyncedAtMillis` INTEGER, `localUpdatedAtMillis` INTEGER NOT NULL, `dirty` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_calendars_type_remoteId` ON `calendars` (`type`, `remoteId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_calendars_dirty` ON `calendars` (`dirty`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` TEXT NOT NULL, `eventId` TEXT NOT NULL, `minutesBefore` INTEGER NOT NULL, `enabled` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reminders_eventId` ON `reminders` (`eventId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` TEXT NOT NULL, `listId` TEXT NOT NULL, `remoteId` TEXT, `title` TEXT NOT NULL, `notes` TEXT, `dueDateMillis` INTEGER, `completed` INTEGER NOT NULL, `completedAtMillis` INTEGER, `status` TEXT NOT NULL, `position` TEXT, `etag` TEXT, `remoteUpdatedAtMillis` INTEGER, `localUpdatedAtMillis` INTEGER NOT NULL, `dirty` INTEGER NOT NULL, `deleted` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tasks_listId_remoteId` ON `tasks` (`listId`, `remoteId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_listId_position` ON `tasks` (`listId`, `position`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_dirty` ON `tasks` (`dirty`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` TEXT NOT NULL, `email` TEXT NOT NULL, `displayName` TEXT, `photoUrl` TEXT, `provider` TEXT NOT NULL, `scopes` TEXT, `addedAtMillis` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4b8a5759ae8d08e34addcd427d48d985')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `events`");
        db.execSQL("DROP TABLE IF EXISTS `calendars`");
        db.execSQL("DROP TABLE IF EXISTS `reminders`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsEvents = new HashMap<String, TableInfo.Column>(26);
        _columnsEvents.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("calendarId", new TableInfo.Column("calendarId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("remoteId", new TableInfo.Column("remoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("startUtcMillis", new TableInfo.Column("startUtcMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("endUtcMillis", new TableInfo.Column("endUtcMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("allDay", new TableInfo.Column("allDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("timeZone", new TableInfo.Column("timeZone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("rrule", new TableInfo.Column("rrule", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("rdate", new TableInfo.Column("rdate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("exdate", new TableInfo.Column("exdate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("recurringEventId", new TableInfo.Column("recurringEventId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("originalStartUtcMillis", new TableInfo.Column("originalStartUtcMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("attendeesJson", new TableInfo.Column("attendeesJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("organizer", new TableInfo.Column("organizer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("colorArgb", new TableInfo.Column("colorArgb", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("visibility", new TableInfo.Column("visibility", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("transparency", new TableInfo.Column("transparency", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("etag", new TableInfo.Column("etag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("remoteUpdatedAtMillis", new TableInfo.Column("remoteUpdatedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("localUpdatedAtMillis", new TableInfo.Column("localUpdatedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("dirty", new TableInfo.Column("dirty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("deleted", new TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEvents = new HashSet<TableInfo.Index>(4);
        _indicesEvents.add(new TableInfo.Index("index_events_calendarId_startUtcMillis", false, Arrays.asList("calendarId", "startUtcMillis"), Arrays.asList("ASC", "ASC")));
        _indicesEvents.add(new TableInfo.Index("index_events_calendarId_remoteId", true, Arrays.asList("calendarId", "remoteId"), Arrays.asList("ASC", "ASC")));
        _indicesEvents.add(new TableInfo.Index("index_events_dirty", false, Arrays.asList("dirty"), Arrays.asList("ASC")));
        _indicesEvents.add(new TableInfo.Index("index_events_recurringEventId", false, Arrays.asList("recurringEventId"), Arrays.asList("ASC")));
        final TableInfo _infoEvents = new TableInfo("events", _columnsEvents, _foreignKeysEvents, _indicesEvents);
        final TableInfo _existingEvents = TableInfo.read(db, "events");
        if (!_infoEvents.equals(_existingEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "events(com.example.jalicalendar.data.local.entity.EventEntity).\n"
                  + " Expected:\n" + _infoEvents + "\n"
                  + " Found:\n" + _existingEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsCalendars = new HashMap<String, TableInfo.Column>(17);
        _columnsCalendars.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("accountId", new TableInfo.Column("accountId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("remoteId", new TableInfo.Column("remoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("colorArgb", new TableInfo.Column("colorArgb", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("timeZone", new TableInfo.Column("timeZone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("isPrimary", new TableInfo.Column("isPrimary", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("accessRole", new TableInfo.Column("accessRole", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("hidden", new TableInfo.Column("hidden", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("selected", new TableInfo.Column("selected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("syncToken", new TableInfo.Column("syncToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("lastSyncedAtMillis", new TableInfo.Column("lastSyncedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("localUpdatedAtMillis", new TableInfo.Column("localUpdatedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("dirty", new TableInfo.Column("dirty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendars.put("deleted", new TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCalendars = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCalendars = new HashSet<TableInfo.Index>(2);
        _indicesCalendars.add(new TableInfo.Index("index_calendars_type_remoteId", true, Arrays.asList("type", "remoteId"), Arrays.asList("ASC", "ASC")));
        _indicesCalendars.add(new TableInfo.Index("index_calendars_dirty", false, Arrays.asList("dirty"), Arrays.asList("ASC")));
        final TableInfo _infoCalendars = new TableInfo("calendars", _columnsCalendars, _foreignKeysCalendars, _indicesCalendars);
        final TableInfo _existingCalendars = TableInfo.read(db, "calendars");
        if (!_infoCalendars.equals(_existingCalendars)) {
          return new RoomOpenHelper.ValidationResult(false, "calendars(com.example.jalicalendar.data.local.entity.CalendarEntity).\n"
                  + " Expected:\n" + _infoCalendars + "\n"
                  + " Found:\n" + _existingCalendars);
        }
        final HashMap<String, TableInfo.Column> _columnsReminders = new HashMap<String, TableInfo.Column>(4);
        _columnsReminders.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("eventId", new TableInfo.Column("eventId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("minutesBefore", new TableInfo.Column("minutesBefore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminders = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReminders.add(new TableInfo.ForeignKey("events", "CASCADE", "NO ACTION", Arrays.asList("eventId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReminders = new HashSet<TableInfo.Index>(1);
        _indicesReminders.add(new TableInfo.Index("index_reminders_eventId", false, Arrays.asList("eventId"), Arrays.asList("ASC")));
        final TableInfo _infoReminders = new TableInfo("reminders", _columnsReminders, _foreignKeysReminders, _indicesReminders);
        final TableInfo _existingReminders = TableInfo.read(db, "reminders");
        if (!_infoReminders.equals(_existingReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "reminders(com.example.jalicalendar.data.local.entity.ReminderEntity).\n"
                  + " Expected:\n" + _infoReminders + "\n"
                  + " Found:\n" + _existingReminders);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(15);
        _columnsTasks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("listId", new TableInfo.Column("listId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("remoteId", new TableInfo.Column("remoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueDateMillis", new TableInfo.Column("dueDateMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("completedAtMillis", new TableInfo.Column("completedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("position", new TableInfo.Column("position", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("etag", new TableInfo.Column("etag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("remoteUpdatedAtMillis", new TableInfo.Column("remoteUpdatedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("localUpdatedAtMillis", new TableInfo.Column("localUpdatedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dirty", new TableInfo.Column("dirty", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("deleted", new TableInfo.Column("deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(3);
        _indicesTasks.add(new TableInfo.Index("index_tasks_listId_remoteId", true, Arrays.asList("listId", "remoteId"), Arrays.asList("ASC", "ASC")));
        _indicesTasks.add(new TableInfo.Index("index_tasks_listId_position", false, Arrays.asList("listId", "position"), Arrays.asList("ASC", "ASC")));
        _indicesTasks.add(new TableInfo.Index("index_tasks_dirty", false, Arrays.asList("dirty"), Arrays.asList("ASC")));
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.example.jalicalendar.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(7);
        _columnsAccounts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("photoUrl", new TableInfo.Column("photoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("provider", new TableInfo.Column("provider", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("scopes", new TableInfo.Column("scopes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("addedAtMillis", new TableInfo.Column("addedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(com.example.jalicalendar.data.local.entity.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4b8a5759ae8d08e34addcd427d48d985", "28bcd05e2c2e3135578a8698722d4002");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "events","calendars","reminders","tasks","accounts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `events`");
      _db.execSQL("DELETE FROM `calendars`");
      _db.execSQL("DELETE FROM `reminders`");
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `accounts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CalendarDao.class, CalendarDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReminderDao.class, ReminderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AccountDao.class, AccountDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }

  @Override
  public CalendarDao calendarDao() {
    if (_calendarDao != null) {
      return _calendarDao;
    } else {
      synchronized(this) {
        if(_calendarDao == null) {
          _calendarDao = new CalendarDao_Impl(this);
        }
        return _calendarDao;
      }
    }
  }

  @Override
  public ReminderDao reminderDao() {
    if (_reminderDao != null) {
      return _reminderDao;
    } else {
      synchronized(this) {
        if(_reminderDao == null) {
          _reminderDao = new ReminderDao_Impl(this);
        }
        return _reminderDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public AccountDao accountDao() {
    if (_accountDao != null) {
      return _accountDao;
    } else {
      synchronized(this) {
        if(_accountDao == null) {
          _accountDao = new AccountDao_Impl(this);
        }
        return _accountDao;
      }
    }
  }
}
