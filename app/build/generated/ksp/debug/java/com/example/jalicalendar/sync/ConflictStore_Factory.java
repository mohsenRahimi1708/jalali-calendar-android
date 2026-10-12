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
public final class ConflictStore_Factory implements Factory<ConflictStore> {
  @Override
  public ConflictStore get() {
    return newInstance();
  }

  public static ConflictStore_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ConflictStore newInstance() {
    return new ConflictStore();
  }

  private static final class InstanceHolder {
    private static final ConflictStore_Factory INSTANCE = new ConflictStore_Factory();
  }
}
