package com.example.jalicalendar.di;

import com.example.jalicalendar.data.local.JalaliDatabase;
import com.example.jalicalendar.data.local.dao.EventDao;
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
public final class DatabaseModule_ProvideEventDaoFactory implements Factory<EventDao> {
  private final Provider<JalaliDatabase> dbProvider;

  public DatabaseModule_ProvideEventDaoFactory(Provider<JalaliDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EventDao get() {
    return provideEventDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEventDaoFactory create(Provider<JalaliDatabase> dbProvider) {
    return new DatabaseModule_ProvideEventDaoFactory(dbProvider);
  }

  public static EventDao provideEventDao(JalaliDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEventDao(db));
  }
}
