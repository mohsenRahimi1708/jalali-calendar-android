package com.example.jalicalendar.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import com.example.jalicalendar.BuildConfig
import com.example.jalicalendar.domain.model.Account
import com.example.jalicalendar.domain.repo.AccountRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.NoClientAuthentication
import net.openid.appauth.CodeVerifierUtil
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Sign-in state for the UI. [Error] is cleared by the next [beginAuthorization]. */
sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Working : AuthUiState
    data class SignedIn(val email: String) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

/**
 * Google OAuth 2.0 via AppAuth (authorization code + PKCE, system browser
 * Custom Tab). Client ID comes from BuildConfig.GOOGLE_CLIENT_ID (see README:
 * Google Cloud console → OAuth client ID, Android, with the debug SHA-1).
 *
 * Endpoints are the fixed Google URLs rather than fetched from the discovery
 * document: they change essentially never, and hardcoding them keeps sign-in
 * deterministic (no extra round trip that can fail offline).
 */
@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenStore: TokenStore,
    private val accountRepo: AccountRepository,
) {

    companion object {
        private val AUTH_ENDPOINT = Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
        private val TOKEN_ENDPOINT = Uri.parse("https://oauth2.googleapis.com/token")
        private val REDIRECT_URI = Uri.parse("com.example.jalicalendar:/oauth2callback")
        val CLIENT_ID: String get() = BuildConfig.GOOGLE_CLIENT_ID

        val SCOPES = listOf(
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/tasks",
            "openid", "email", "profile",
        )
    }

    private val serviceConfig = AuthorizationServiceConfiguration(AUTH_ENDPOINT, TOKEN_ENDPOINT)

    private val _uiState = MutableStateFlow<AuthUiState>(
        if (tokenStore.session.value != null) AuthUiState.Idle else AuthUiState.Idle
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val signedIn: Boolean get() = tokenStore.session.value != null

    /** True while a session exists; flips on sign-in/out. */
    fun signedInFlow(): Flow<Boolean> = tokenStore.session.map { it != null }

    /**
     * The pending auth request must outlive the browser round trip so the code
     * verifier matches on exchange; held in memory (process death → user simply
     * restarts the flow).
     */
    private val pendingRequest = AtomicReference<AuthorizationRequest?>(null)

    /** True when a client id has been configured; false → UI must explain setup. */
    fun isConfigured(): Boolean = CLIENT_ID.isNotBlank()

    /** Build the Custom Tab intent the UI launches via StartIntentSenderForResult. */
    suspend fun beginAuthorization(): PendingIntent = withContext(Dispatchers.IO) {
        _uiState.value = AuthUiState.Working
        val request = AuthorizationRequest.Builder(
            serviceConfig,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            REDIRECT_URI,
        )
            .setScopes(SCOPES)
            .setState(UUID.randomUUID().toString())
            .setCodeVerifier(CodeVerifierUtil.generateRandomCodeVerifier())
            .build()
        pendingRequest.set(request)
        val intent = AuthorizationService(context).getAuthorizationRequestIntent(request)
        PendingIntent.getActivity(
            context, 0, intent,
            // Mutable: the system fills in the response data; required for IntentSender.
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
    }

    /**
     * Called with the activity result. Exchanges the code, stores tokens,
     * and records the account row used by sync.
     */
    suspend fun handleAuthResponse(data: Intent?) {
        if (data == null) {
            _uiState.value = AuthUiState.Idle
            return
        }
        val exception = AuthorizationException.fromIntent(data)
        val response = net.openid.appauth.AuthorizationResponse.fromIntent(data)
        if (exception != null || response == null) {
            _uiState.value = AuthUiState.Error(exception?.errorDescription ?: "Authorization failed")
            return
        }
        val request = pendingRequest.getAndSet(null)
            ?: run {
                _uiState.value = AuthUiState.Error("Lost authorization request; please retry")
                return
            }
        _uiState.value = AuthUiState.Working
        try {
            exchangeCode(request, response.authorizationCode!!)
        } catch (e: Exception) {
            _uiState.value = AuthUiState.Error(e.message ?: "Token exchange failed")
        }
    }

    private suspend fun exchangeCode(request: AuthorizationRequest, code: String) {
        val tokenRequest = TokenRequest.Builder(serviceConfig, CLIENT_ID)
            .setGrantType(GrantTypeValues.AUTHORIZATION_CODE)
            .setAuthorizationCode(code)
            .setRedirectUri(REDIRECT_URI)
            .setCodeVerifier(request.codeVerifier)
            .build()
        val tokenResponse = performTokenRequest(tokenRequest)
        persist(tokenResponse)
    }

    /** Refresh the access token; false when the refresh token itself is dead (re-auth needed). */
    suspend fun refresh(): Boolean = withContext(Dispatchers.IO) {
        val session = tokenStore.session.value ?: return@withContext false
        val refresh = session.refreshToken ?: return@withContext false
        try {
            val tokenRequest = TokenRequest.Builder(serviceConfig, CLIENT_ID)
                .setGrantType(GrantTypeValues.REFRESH_TOKEN)
                .setRefreshToken(refresh)
                .build()
            persist(performTokenRequest(tokenRequest))
            true
        } catch (e: Exception) {
            false
        }
    }

    /** Current session accessor for the sync engine. */
    fun tokenStoreSession(): AuthSession? = tokenStore.session.value

    /** True when the access token expires within the skew window. */
    fun needsRefresh(): Boolean {
        val s = tokenStore.session.value ?: return false
        return s.isExpired(System.currentTimeMillis())
    }

    suspend fun signOut() {
        tokenStore.clear()
        _uiState.value = AuthUiState.Idle
    }

    // -- internals ----------------------------------------------------------

    private suspend fun persist(response: net.openid.appauth.TokenResponse) {
        val identity = response.idToken?.let(::parseIdToken)
        val session = AuthSession(
            accountId = identity?.optString("sub") ?: tokenStore.session.value?.accountId ?: UUID.randomUUID().toString(),
            email = identity?.optString("email") ?: tokenStore.session.value?.email ?: "",
            displayName = identity?.optString("name")?.takeIf { it.isNotEmpty() }
                ?: tokenStore.session.value?.displayName,
            photoUrl = identity?.optString("picture")?.takeIf { it.isNotEmpty() }
                ?: tokenStore.session.value?.photoUrl,
            accessToken = response.accessToken ?: return,
            refreshToken = response.refreshToken,
            expiresAtMillis = response.accessTokenExpirationTime ?: 0L,
        )
        tokenStore.save(session)
        if (session.email.isNotEmpty()) {
            accountRepo.upsert(
                Account(
                    id = session.accountId,
                    email = session.email,
                    displayName = session.displayName,
                    photoUrl = session.photoUrl,
                    scopes = SCOPES.joinToString(" "),
                    addedAtMillis = System.currentTimeMillis(),
                )
            )
        }
        _uiState.value = session.email.takeIf { it.isNotEmpty() }
            ?.let { AuthUiState.SignedIn(it) } ?: AuthUiState.Idle
    }

    private suspend fun performTokenRequest(request: TokenRequest) =
        suspendCancellableCoroutine<net.openid.appauth.TokenResponse> { cont ->
            // Public client (PKCE, no secret): an empty Basic header is what Google expects.
            AuthorizationService(context).performTokenRequest(request, NoClientAuthentication.INSTANCE) { resp, ex ->
                when {
                    ex != null && cont.isActive -> cont.resumeWithException(
                        Exception(ex.errorDescription ?: ex.code.toString()),
                    )
                    resp != null && cont.isActive -> cont.resume(resp)
                }
            }
        }

    /** Extracts claims from the ID token payload without a userinfo round trip. */
    private fun parseIdToken(idToken: String): JSONObject? = try {
        val payload = idToken.split(".")[1]
        JSONObject(String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)))
    } catch (e: Exception) {
        null
    }
}
