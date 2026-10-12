package com.example.jalicalendar.di;

import com.example.jalicalendar.data.local.JalaliDatabase;
import com.example.jalicalendar.data.local.dao.CalendarDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCalendarDaoFactory implements Factory<CalendarDao> {
  private final Provider<JalaliDatabase> dbProvider;

  public DatabaseModule_ProvideCalendarDaoFactory(Provider<JalaliDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CalendarDao get() {
    return provideCalendarDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCalendarDaoFactory create(
      Provider<JalaliDatabase> dbProvider) {
    return new DatabaseModule_ProvideCalendarDaoFactory(dbProvider);
  }

  public static CalendarDao provideCalendarDao(JalaliDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCalendarDao(db));
  }
}
