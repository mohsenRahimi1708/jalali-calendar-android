package com.example.jalicalendar.ui.screens.eventedit;

import androidx.lifecycle.SavedStateHandle;
import com.example.jalicalendar.domain.repo.CalendarRepository;
import com.example.jalicalendar.domain.repo.EventRepository;
import com.example.jalicalendar.domain.repo.SettingsRepository;
import com.example.jalicalendar.reminders.ReminderScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class EventEditViewModel_Factory implements Factory<EventEditViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<EventRepository> eventRepoProvider;

  private final Provider<CalendarRepository> calendarRepoProvider;

  private final Provider<SettingsRepository> settingsRepoProvider;

  private final Provider<ReminderScheduler> reminderSchedulerProvider;

  public EventEditViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<EventRepository> eventRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.eventRepoProvider = eventRepoProvider;
    this.calendarRepoProvider = calendarRepoProvider;
    this.settingsRepoProvider = settingsRepoProvider;
    this.reminderSchedulerProvider = reminderSchedulerProvider;
  }

  @Override
  public EventEditViewModel get() {
    return newInstance(savedStateHandleProvider.get(), eventRepoProvider.get(), calendarRepoProvider.get(), settingsRepoProvider.get(), reminderSchedulerProvider.get());
  }

  public static EventEditViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<EventRepository> eventRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider,
      Provider<ReminderScheduler> reminderSchedulerProvider) {
    return new EventEditViewModel_Factory(savedStateHandleProvider, eventRepoProvider, calendarRepoProvider, settingsRepoProvider, reminderSchedulerProvider);
  }

  public static EventEditViewModel newInstance(SavedStateHandle savedStateHandle,
      EventRepository eventRepo, CalendarRepository calendarRepo, SettingsRepository settingsRepo,
      ReminderScheduler reminderScheduler) {
    return new EventEditViewModel(savedStateHandle, eventRepo, calendarRepo, settingsRepo, reminderScheduler);
  }
}
