package com.example.jalicalendar.data.remote

import com.example.jalicalendar.auth.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/** Attaches `Authorization: Bearer …` to every Google request. */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStore.session.value?.accessToken
        val request: Request = if (token != null) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}

/**
 * Single 401 retry after a synchronous token refresh. AuthManager owns the
 * refresh logic (and its mutex-free design is safe here: OkHttp serializes
 * authenticator calls per client).
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    private val refresher: TokenRefresher,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Only retry once per request to avoid loops on revoked tokens.
        if (responseCount(response) >= 2) return null
        val current = tokenStore.session.value ?: return null
        if (current.isExpired(System.currentTimeMillis()).not() &&
            response.request.header("Authorization") != null
        ) {
            // Token looked fresh but the server disagreed; force a refresh anyway.
        }
        val refreshed = runBlocking { refresher.refresh() } ?: return null
        return response.request.newBuilder()
            .header("Authorization", "Bearer $refreshed")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}

/**
 * Thin seam so the OkHttp authenticator (blocking) can call AuthManager's
 * suspend refresh without a Hilt cycle: AuthManager → repos → (this module).
 */
@Singleton
class TokenRefresher @Inject constructor(
    private val authManagerProvider: dagger.Lazy<com.example.jalicalendar.auth.AuthManager>,
    private val tokenStore: TokenStore,
) {
    /** Returns the fresh access token, or null when refresh failed. */
    suspend fun refresh(): String? {
        val ok = authManagerProvider.get().refresh()
        return if (ok) tokenStore.session.value?.accessToken else null
    }
}
