package com.example.jalicalendar.di;

import com.example.jalicalendar.data.remote.AuthInterceptor;
import com.example.jalicalendar.data.remote.TokenAuthenticator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class NetworkModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<AuthInterceptor> authInterceptorProvider;

  private final Provider<TokenAuthenticator> authenticatorProvider;

  public NetworkModule_ProvideOkHttpFactory(Provider<AuthInterceptor> authInterceptorProvider,
      Provider<TokenAuthenticator> authenticatorProvider) {
    this.authInterceptorProvider = authInterceptorProvider;
    this.authenticatorProvider = authenticatorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(authInterceptorProvider.get(), authenticatorProvider.get());
  }

  public static NetworkModule_ProvideOkHttpFactory create(
      Provider<AuthInterceptor> authInterceptorProvider,
      Provider<TokenAuthenticator> authenticatorProvider) {
    return new NetworkModule_ProvideOkHttpFactory(authInterceptorProvider, authenticatorProvider);
  }

  public static OkHttpClient provideOkHttp(AuthInterceptor authInterceptor,
      TokenAuthenticator authenticator) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttp(authInterceptor, authenticator));
  }
}
