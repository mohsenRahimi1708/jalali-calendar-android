package com.example.jalicalendar.ui.screens.search;

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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<EventRepository> eventRepoProvider;

  private final Provider<SettingsRepository> settingsRepoProvider;

  public SearchViewModel_Factory(Provider<EventRepository> eventRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider) {
    this.eventRepoProvider = eventRepoProvider;
    this.settingsRepoProvider = settingsRepoProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(eventRepoProvider.get(), settingsRepoProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<EventRepository> eventRepoProvider,
      Provider<SettingsRepository> settingsRepoProvider) {
    return new SearchViewModel_Factory(eventRepoProvider, settingsRepoProvider);
  }

  public static SearchViewModel newInstance(EventRepository eventRepo,
      SettingsRepository settingsRepo) {
    return new SearchViewModel(eventRepo, settingsRepo);
  }
}
