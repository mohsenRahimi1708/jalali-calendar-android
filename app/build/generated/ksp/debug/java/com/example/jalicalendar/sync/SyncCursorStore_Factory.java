package com.example.jalicalendar.sync;

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
public final class SyncCursorStore_Factory implements Factory<SyncCursorStore> {
  private final Provider<Context> contextProvider;

  public SyncCursorStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SyncCursorStore get() {
    return newInstance(contextProvider.get());
  }

  public static SyncCursorStore_Factory create(Provider<Context> contextProvider) {
    return new SyncCursorStore_Factory(contextProvider);
  }

  public static SyncCursorStore newInstance(Context context) {
    return new SyncCursorStore(context);
  }
}
