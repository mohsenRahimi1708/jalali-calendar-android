package com.example.jalicalendar.data.remote;

import com.example.jalicalendar.auth.TokenStore;
import dagger.internal.DaggerGenerated;
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
public final class TokenAuthenticator_Factory implements Factory<TokenAuthenticator> {
  private final Provider<TokenStore> tokenStoreProvider;

  private final Provider<TokenRefresher> refresherProvider;

  public TokenAuthenticator_Factory(Provider<TokenStore> tokenStoreProvider,
      Provider<TokenRefresher> refresherProvider) {
    this.tokenStoreProvider = tokenStoreProvider;
    this.refresherProvider = refresherProvider;
  }

  @Override
  public TokenAuthenticator get() {
    return newInstance(tokenStoreProvider.get(), refresherProvider.get());
  }

  public static TokenAuthenticator_Factory create(Provider<TokenStore> tokenStoreProvider,
      Provider<TokenRefresher> refresherProvider) {
    return new TokenAuthenticator_Factory(tokenStoreProvider, refresherProvider);
  }

  public static TokenAuthenticator newInstance(TokenStore tokenStore, TokenRefresher refresher) {
    return new TokenAuthenticator(tokenStore, refresher);
  }
}
