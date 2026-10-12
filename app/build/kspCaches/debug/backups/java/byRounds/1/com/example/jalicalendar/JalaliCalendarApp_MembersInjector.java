package com.example.jalicalendar;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class JalaliCalendarApp_MembersInjector implements MembersInjector<JalaliCalendarApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public JalaliCalendarApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<JalaliCalendarApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new JalaliCalendarApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(JalaliCalendarApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.example.jalicalendar.JalaliCalendarApp.workerFactory")
  public static void injectWorkerFactory(JalaliCalendarApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
