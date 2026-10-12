package com.example.jalicalendar.data.repo;

import com.example.jalicalendar.data.local.dao.CalendarDao;
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
public final class CalendarRepositoryImpl_Factory implements Factory<CalendarRepositoryImpl> {
  private final Provider<CalendarDao> calendarDaoProvider;

  public CalendarRepositoryImpl_Factory(Provider<CalendarDao> calendarDaoProvider) {
    this.calendarDaoProvider = calendarDaoProvider;
  }

  @Override
  public CalendarRepositoryImpl get() {
    return newInstance(calendarDaoProvider.get());
  }

  public static CalendarRepositoryImpl_Factory create(Provider<CalendarDao> calendarDaoProvider) {
    return new CalendarRepositoryImpl_Factory(calendarDaoProvider);
  }

  public static CalendarRepositoryImpl newInstance(CalendarDao calendarDao) {
    return new CalendarRepositoryImpl(calendarDao);
  }
}
