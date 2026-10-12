package com.example.jalicalendar.sync;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SyncStatusStore_Factory implements Factory<SyncStatusStore> {
  @Override
  public SyncStatusStore get() {
    return newInstance();
  }

  public static SyncStatusStore_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SyncStatusStore newInstance() {
    return new SyncStatusStore();
  }

  private static final class InstanceHolder {
    private static final SyncStatusStore_Factory INSTANCE = new SyncStatusStore_Factory();
  }
}
