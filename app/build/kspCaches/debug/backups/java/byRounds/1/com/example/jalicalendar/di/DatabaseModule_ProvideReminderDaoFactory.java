package com.example.jalicalendar.di;

import com.example.jalicalendar.data.local.JalaliDatabase;
import com.example.jalicalendar.data.local.dao.ReminderDao;
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
public final class DatabaseModule_ProvideReminderDaoFactory implements Factory<ReminderDao> {
  private final Provider<JalaliDatabase> dbProvider;

  public DatabaseModule_ProvideReminderDaoFactory(Provider<JalaliDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ReminderDao get() {
    return provideReminderDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideReminderDaoFactory create(
      Provider<JalaliDatabase> dbProvider) {
    return new DatabaseModule_ProvideReminderDaoFactory(dbProvider);
  }

  public static ReminderDao provideReminderDao(JalaliDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReminderDao(db));
  }
}
