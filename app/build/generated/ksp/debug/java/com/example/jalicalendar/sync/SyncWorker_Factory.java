package com.example.jalicalendar.sync;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
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
public final class SyncWorker_Factory {
  private final Provider<SyncEngine> engineProvider;

  public SyncWorker_Factory(Provider<SyncEngine> engineProvider) {
    this.engineProvider = engineProvider;
  }

  public SyncWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, engineProvider.get());
  }

  public static SyncWorker_Factory create(Provider<SyncEngine> engineProvider) {
    return new SyncWorker_Factory(engineProvider);
  }

  public static SyncWorker newInstance(Context appContext, WorkerParameters params,
      SyncEngine engine) {
    return new SyncWorker(appContext, params, engine);
  }
}
