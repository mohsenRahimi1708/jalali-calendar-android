package com.example.jalicalendar.ui.screens.settings;

import com.example.jalicalendar.domain.repo.CalendarRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> repoProvider;

  private final Provider<CalendarRepository> calendarRepoProvider;

  public SettingsViewModel_Factory(Provider<SettingsRepository> repoProvider,
      Provider<CalendarRepository> calendarRepoProvider) {
    this.repoProvider = repoProvider;
    this.calendarRepoProvider = calendarRepoProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(repoProvider.get(), calendarRepoProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<SettingsRepository> repoProvider,
      Provider<CalendarRepository> calendarRepoProvider) {
    return new SettingsViewModel_Factory(repoProvider, calendarRepoProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository repo,
      CalendarRepository calendarRepo) {
    return new SettingsViewModel(repo, calendarRepo);
  }
}
