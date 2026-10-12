package com.example.jalicalendar.auth;

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
public final class TokenStore_Factory implements Factory<TokenStore> {
  private final Provider<Context> contextProvider;

  public TokenStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TokenStore get() {
    return newInstance(contextProvider.get());
  }

  public static TokenStore_Factory create(Provider<Context> contextProvider) {
    return new TokenStore_Factory(contextProvider);
  }

  public static TokenStore newInstance(Context context) {
    return new TokenStore(context);
  }
}
