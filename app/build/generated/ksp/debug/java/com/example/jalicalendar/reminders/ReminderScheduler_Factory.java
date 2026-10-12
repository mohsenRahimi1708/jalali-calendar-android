package com.example.jalicalendar.reminders;

import android.content.Context;
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
public final class ReminderScheduler_Factory implements Factory<ReminderScheduler> {
  private final Provider<Context> contextProvider;

  public ReminderScheduler_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ReminderScheduler get() {
    return newInstance(contextProvider.get());
  }

  public static ReminderScheduler_Factory create(Provider<Context> contextProvider) {
    return new ReminderScheduler_Factory(contextProvider);
  }

  public static ReminderScheduler newInstance(Context context) {
    return new ReminderScheduler(context);
  }
}
