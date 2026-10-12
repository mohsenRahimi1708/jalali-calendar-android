package com.example.jalicalendar.data.repo;

import com.example.jalicalendar.data.local.dao.EventDao;
import com.example.jalicalendar.data.local.dao.ReminderDao;
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
public final class EventRepositoryImpl_Factory implements Factory<EventRepositoryImpl> {
  private final Provider<EventDao> eventDaoProvider;

  private final Provider<ReminderDao> reminderDaoProvider;

  public EventRepositoryImpl_Factory(Provider<EventDao> eventDaoProvider,
      Provider<ReminderDao> reminderDaoProvider) {
    this.eventDaoProvider = eventDaoProvider;
    this.reminderDaoProvider = reminderDaoProvider;
  }

  @Override
  public EventRepositoryImpl get() {
    return newInstance(eventDaoProvider.get(), reminderDaoProvider.get());
  }

  public static EventRepositoryImpl_Factory create(Provider<EventDao> eventDaoProvider,
      Provider<ReminderDao> reminderDaoProvider) {
    return new EventRepositoryImpl_Factory(eventDaoProvider, reminderDaoProvider);
  }

  public static EventRepositoryImpl newInstance(EventDao eventDao, ReminderDao reminderDao) {
    return new EventRepositoryImpl(eventDao, reminderDao);
  }
}
