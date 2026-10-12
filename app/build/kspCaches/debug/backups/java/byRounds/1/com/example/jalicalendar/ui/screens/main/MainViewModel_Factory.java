package com.example.jalicalendar.ui.screens.main;

import com.example.jalicalendar.domain.repo.CalendarRepository;
import com.example.jalicalendar.domain.repo.EventRepository;
import com.example.jalicalendar.domain.repo.SettingsRepository;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<SettingsRepository> settingsRepoProvider;

  private final Provider<EventRepository> eventRepoProvider;

  private final Provider<CalendarRepository> calendarRepoProvider;

  public MainViewModel_Factory(Provider<SettingsRepository> settingsRepoProvider,
      Provider<EventRepository> eventRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider) {
    this.settingsRepoProvider = settingsRepoProvider;
    this.eventRepoProvider = eventRepoProvider;
    this.calendarRepoProvider = calendarRepoProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(settingsRepoProvider.get(), eventRepoProvider.get(), calendarRepoProvider.get());
  }

  public static MainViewModel_Factory create(Provider<SettingsRepository> settingsRepoProvider,
      Provider<EventRepository> eventRepoProvider,
      Provider<CalendarRepository> calendarRepoProvider) {
    return new MainViewModel_Factory(settingsRepoProvider, eventRepoProvider, calendarRepoProvider);
  }

  public static MainViewModel newInstance(SettingsRepository settingsRepo,
      EventRepository eventRepo, CalendarRepository calendarRepo) {
    return new MainViewModel(settingsRepo, eventRepo, calendarRepo);
  }
}
