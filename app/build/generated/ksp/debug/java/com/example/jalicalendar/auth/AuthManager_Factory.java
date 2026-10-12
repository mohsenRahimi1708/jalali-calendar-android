package com.example.jalicalendar.auth;

import android.content.Context;
import com.example.jalicalendar.domain.repo.AccountRepository;
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
public final class AuthManager_Factory implements Factory<AuthManager> {
  private final Provider<Context> contextProvider;

  private final Provider<TokenStore> tokenStoreProvider;

  private final Provider<AccountRepository> accountRepoProvider;

  public AuthManager_Factory(Provider<Context> contextProvider,
      Provider<TokenStore> tokenStoreProvider, Provider<AccountRepository> accountRepoProvider) {
    this.contextProvider = contextProvider;
    this.tokenStoreProvider = tokenStoreProvider;
    this.accountRepoProvider = accountRepoProvider;
  }

  @Override
  public AuthManager get() {
    return newInstance(contextProvider.get(), tokenStoreProvider.get(), accountRepoProvider.get());
  }

  public static AuthManager_Factory create(Provider<Context> contextProvider,
      Provider<TokenStore> tokenStoreProvider, Provider<AccountRepository> accountRepoProvider) {
    return new AuthManager_Factory(contextProvider, tokenStoreProvider, accountRepoProvider);
  }

  public static AuthManager newInstance(Context context, TokenStore tokenStore,
      AccountRepository accountRepo) {
    return new AuthManager(context, tokenStore, accountRepo);
  }
}
