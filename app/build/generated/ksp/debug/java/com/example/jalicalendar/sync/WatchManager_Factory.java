package com.example.jalicalendar.sync;

import android.content.Context;
import com.example.jalicalendar.data.remote.CalendarApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class WatchManager_Factory implements Factory<WatchManager> {
  private final Provider<Context> contextProvider;

  private final Provider<CalendarApi> calendarApiProvider;

  public WatchManager_Factory(Provider<Context> contextProvider,
      Provider<CalendarApi> calendarApiProvider) {
    this.contextProvider = contextProvider;
    this.calendarApiProvider = calendarApiProvider;
  }

  @Override
  public WatchManager get() {
    return newInstance(contextProvider.get(), calendarApiProvider.get());
  }

  public static WatchManager_Factory create(Provider<Context> contextProvider,
      Provider<CalendarApi> calendarApiProvider) {
    return new WatchManager_Factory(contextProvider, calendarApiProvider);
  }

  public static WatchManager newInstance(Context context, CalendarApi calendarApi) {
    return new WatchManager(context, calendarApi);
  }
}
