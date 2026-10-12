package com.example.jalicalendar.di;

import com.example.jalicalendar.data.local.JalaliDatabase;
import com.example.jalicalendar.data.local.dao.TaskDao;
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
public final class DatabaseModule_ProvideTaskDaoFactory implements Factory<TaskDao> {
  private final Provider<JalaliDatabase> dbProvider;

  public DatabaseModule_ProvideTaskDaoFactory(Provider<JalaliDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TaskDao get() {
    return provideTaskDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTaskDaoFactory create(Provider<JalaliDatabase> dbProvider) {
    return new DatabaseModule_ProvideTaskDaoFactory(dbProvider);
  }

  public static TaskDao provideTaskDao(JalaliDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTaskDao(db));
  }
}
