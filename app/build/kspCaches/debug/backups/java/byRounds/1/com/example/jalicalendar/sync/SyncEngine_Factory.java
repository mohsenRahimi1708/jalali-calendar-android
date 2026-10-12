package com.example.jalicalendar.sync;

import com.example.jalicalendar.auth.AuthManager;
import com.example.jalicalendar.data.remote.CalendarApi;
import com.example.jalicalendar.data.remote.TasksApi;
import com.example.jalicalendar.domain.repo.AccountRepository;
import com.example.jalicalendar.domain.repo.CalendarRepository;
import com.example.jalicalendar.domain.repo.EventRepository;
import com.example.jalicalendar.domain.repo.SettingsRepository;
import com.example.jalicalendar.domain.repo.TaskRepository;
import com.example.jalicalendar.reminders.ReminderScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SyncEngine_Factory implements Factory<SyncEngine> {
  private final Provider<AuthManager> authManagerProvider;

  private final Provider<AccountRepository> accountRepoProvider;

  private final Provider<CalendarRepository> calendarRepoProvider;

  private final Provider<EventRepository> eventRepoProvider;

  private final Provider<TaskRepository> taskRepoProvider;

  private final Provider<SettingsRepository> settingsRepoProvider;

  private final Provider<CalendarApi> calendarApiProvider;

  private final Provider<TasksApi> tasksApiProvider;

  private final Provider<ConflictStore> conflictsProvider;

  private final Provider<SyncCursorStore> cursorStoreProvider;

  private final Provider<SyncStatusStore> statusStoreProvider;

  private final Provider<WatchManager> watchManagerProvider;

  private final Provider<ReminderScheduler> reminderSchedulerProvider;

  public SyncEngine_Factory(Provider<AuthManager> authManagerProvider,
      Provider<AccountRepository> accountRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider,
      Provider<EventRepository> eventRepoProvider, Provider<TaskRepository> taskRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider, Provider<CalendarApi> calendarApiProvider,
      Provider<TasksApi> tasksApiProvider, Provider<ConflictStore> conflictsProvider,
      Provider<SyncCursorStore> cursorStoreProvider, Provider<SyncStatusStore> statusStoreProvider,
      Provider<WatchManager> watchManagerProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    this.authManagerProvider = authManagerProvider;
    this.accountRepoProvider = accountRepoProvider;
    this.calendarRepoProvider = calendarRepoProvider;
    this.eventRepoProvider = eventRepoProvider;
    this.taskRepoProvider = taskRepoProvider;
    this.settingsRepoProvider = settingsRepoProvider;
    this.calendarApiProvider = calendarApiProvider;
    this.tasksApiProvider = tasksApiProvider;
    this.conflictsProvider = conflictsProvider;
    this.cursorStoreProvider = cursorStoreProvider;
    this.statusStoreProvider = statusStoreProvider;
    this.watchManagerProvider = watchManagerProvider;
    this.reminderSchedulerProvider = reminderSchedulerProvider;
  }

  @Override
  public SyncEngine get() {
    return newInstance(authManagerProvider.get(), accountRepoProvider.get(), calendarRepoProvider.get(), eventRepoProvider.get(), taskRepoProvider.get(), settingsRepoProvider.get(), calendarApiProvider.get(), tasksApiProvider.get(), conflictsProvider.get(), cursorStoreProvider.get(), statusStoreProvider.get(), watchManagerProvider.get(), reminderSchedulerProvider.get());
  }

  public static SyncEngine_Factory create(Provider<AuthManager> authManagerProvider,
      Provider<AccountRepository> accountRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider,
      Provider<EventRepository> eventRepoProvider, Provider<TaskRepository> taskRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider, Provider<CalendarApi> calendarApiProvider,
      Provider<TasksApi> tasksApiProvider, Provider<ConflictStore> conflictsProvider,
      Provider<SyncCursorStore> cursorStoreProvider, Provider<SyncStatusStore> statusStoreProvider,
      Provider<WatchManager> watchManagerProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    return new SyncEngine_Factory(authManagerProvider, accountRepoProvider, calendarRepoProvider, eventRepoProvider, taskRepoProvider, settingsRepoProvider, calendarApiProvider, tasksApiProvider, conflictsProvider, cursorStoreProvider, statusStoreProvider, watchManagerProvider, reminderSchedulerProvider);
  }

  public static SyncEngine newInstance(AuthManager authManager, AccountRepository accountRepo,
      CalendarRepository calendarRepo, EventRepository eventRepo, TaskRepository taskRepo,
      SettingsRepository settingsRepo, CalendarApi calendarApi, TasksApi tasksApi,
      ConflictStore conflicts, SyncCursorStore cursorStore, SyncStatusStore statusStore,
      WatchManager watchManager, ReminderScheduler reminderScheduler) {
    return new SyncEngine(authManager, accountRepo, calendarRepo, eventRepo, taskRepo, settingsRepo, calendarApi, tasksApi, conflicts, cursorStore, statusStore, watchManager, reminderScheduler);
  }
}
