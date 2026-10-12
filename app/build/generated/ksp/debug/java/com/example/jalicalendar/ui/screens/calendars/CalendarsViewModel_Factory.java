package com.example.jalicalendar.ui.screens.calendars;

import android.content.Context;
import com.example.jalicalendar.auth.AuthManager;
import com.example.jalicalendar.domain.repo.AccountRepository;
import com.example.jalicalendar.domain.repo.CalendarRepository;
import com.example.jalicalendar.sync.SyncStatusStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class CalendarsViewModel_Factory implements Factory<CalendarsViewModel> {
  private final Provider<CalendarRepository> calendarRepoProvider;

  private final Provider<AccountRepository> accountRepoProvider;

  private final Provider<AuthManager> authManagerProvider;

  private final Provider<SyncStatusStore> statusStoreProvider;

  private final Provider<Context> contextProvider;

  public CalendarsViewModel_Factory(Provider<CalendarRepository> calendarRepoProvider,
      Provider<AccountRepository> accountRepoProvider, Provider<AuthManager> authManagerProvider,
      Provider<SyncStatusStore> statusStoreProvider, Provider<Context> contextProvider) {
    this.calendarRepoProvider = calendarRepoProvider;
    this.accountRepoProvider = accountRepoProvider;
    this.authManagerProvider = authManagerProvider;
    this.statusStoreProvider = statusStoreProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public CalendarsViewModel get() {
    return newInstance(calendarRepoProvider.get(), accountRepoProvider.get(), authManagerProvider.get(), statusStoreProvider.get(), contextProvider.get());
  }

  public static CalendarsViewModel_Factory create(Provider<CalendarRepository> calendarRepoProvider,
      Provider<AccountRepository> accountRepoProvider, Provider<AuthManager> authManagerProvider,
      Provider<SyncStatusStore> statusStoreProvider, Provider<Context> contextProvider) {
    return new CalendarsViewModel_Factory(calendarRepoProvider, accountRepoProvider, authManagerProvider, statusStoreProvider, contextProvider);
  }

  public static CalendarsViewModel newInstance(CalendarRepository calendarRepo,
      AccountRepository accountRepo, AuthManager authManager, SyncStatusStore statusStore,
      Context context) {
    return new CalendarsViewModel(calendarRepo, accountRepo, authManager, statusStore, context);
  }
}
