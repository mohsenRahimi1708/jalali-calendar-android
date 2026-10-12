package com.example.jalicalendar.data.remote;

import com.example.jalicalendar.auth.AuthManager;
import com.example.jalicalendar.auth.TokenStore;
import dagger.Lazy;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
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
public final class TokenRefresher_Factory implements Factory<TokenRefresher> {
  private final Provider<AuthManager> authManagerProvider;

  private final Provider<TokenStore> tokenStoreProvider;

  public TokenRefresher_Factory(Provider<AuthManager> authManagerProvider,
      Provider<TokenStore> tokenStoreProvider) {
    this.authManagerProvider = authManagerProvider;
    this.tokenStoreProvider = tokenStoreProvider;
  }

  @Override
  public TokenRefresher get() {
    return newInstance(DoubleCheck.lazy(authManagerProvider), tokenStoreProvider.get());
  }

  public static TokenRefresher_Factory create(Provider<AuthManager> authManagerProvider,
      Provider<TokenStore> tokenStoreProvider) {
    return new TokenRefresher_Factory(authManagerProvider, tokenStoreProvider);
  }

  public static TokenRefresher newInstance(Lazy<AuthManager> authManagerProvider,
      TokenStore tokenStore) {
    return new TokenRefresher(authManagerProvider, tokenStore);
  }
}
