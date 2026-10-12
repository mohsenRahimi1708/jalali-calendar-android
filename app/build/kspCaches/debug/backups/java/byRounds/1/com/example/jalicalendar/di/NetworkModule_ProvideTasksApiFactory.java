package com.example.jalicalendar.di;

import com.example.jalicalendar.data.remote.TasksApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideTasksApiFactory implements Factory<TasksApi> {
  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Json> jsonProvider;

  public NetworkModule_ProvideTasksApiFactory(Provider<OkHttpClient> clientProvider,
      Provider<Json> jsonProvider) {
    this.clientProvider = clientProvider;
    this.jsonProvider = jsonProvider;
  }

  @Override
  public TasksApi get() {
    return provideTasksApi(clientProvider.get(), jsonProvider.get());
  }

  public static NetworkModule_ProvideTasksApiFactory create(Provider<OkHttpClient> clientProvider,
      Provider<Json> jsonProvider) {
    return new NetworkModule_ProvideTasksApiFactory(clientProvider, jsonProvider);
  }

  public static TasksApi provideTasksApi(OkHttpClient client, Json json) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideTasksApi(client, json));
  }
}
