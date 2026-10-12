package com.example.jalicalendar.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OAuth tokens for the single Google account, at rest in
 * [EncryptedSharedPreferences] (AES-256, excluded from device backup — the
 * master key dies with the key store on restore, so a backed-up token blob is
 * useless).
 */
data class AuthSession(
    val accountId: String,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val accessToken: String,
    val refreshToken: String?,
    val expiresAtMillis: Long,
) {
    fun isExpired(nowMillis: Long, skewMillis: Long = 60_000): Boolean =
        nowMillis >= expiresAtMillis - skewMillis
}

@Singleton
class TokenStore @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "auth_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private val _session = MutableStateFlow(read())
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    @Synchronized
    fun save(session: AuthSession) {
        prefs.edit()
            .putString(KEY_ACCOUNT_ID, session.accountId)
            .putString(KEY_EMAIL, session.email)
            .putString(KEY_NAME, session.displayName)
            .putString(KEY_PHOTO, session.photoUrl)
            .putString(KEY_ACCESS, b64(session.accessToken))
            .putString(KEY_REFRESH, session.refreshToken?.let(::b64))
            .putLong(KEY_EXPIRES, session.expiresAtMillis)
            .apply()
        _session.value = session
    }

    /** Replace tokens/identity fields, keeping everything else (refresh after sign-in). */
    @Synchronized
    fun updateTokens(accessToken: String, expiresAtMillis: Long, refreshToken: String?) {
        val current = _session.value ?: return
        save(current.copy(accessToken = accessToken, expiresAtMillis = expiresAtMillis,
            refreshToken = refreshToken ?: current.refreshToken))
    }

    @Synchronized
    fun clear() {
        prefs.edit().clear().apply()
        _session.value = null
    }

    private fun read(): AuthSession? {
        val access = prefs.getString(KEY_ACCESS, null)?.let(::unb64) ?: return null
        return AuthSession(
            accountId = prefs.getString(KEY_ACCOUNT_ID, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            displayName = prefs.getString(KEY_NAME, null),
            photoUrl = prefs.getString(KEY_PHOTO, null),
            accessToken = access,
            refreshToken = prefs.getString(KEY_REFRESH, null)?.let(::unb64),
            expiresAtMillis = prefs.getLong(KEY_EXPIRES, 0L),
        )
    }

    private fun b64(s: String) = Base64.encodeToString(s.toByteArray(), Base64.NO_WRAP)
    private fun unb64(s: String) = String(Base64.decode(s, Base64.NO_WRAP))

    private companion object {
        const val KEY_ACCOUNT_ID = "account_id"
        const val KEY_EMAIL = "email"
        const val KEY_NAME = "display_name"
        const val KEY_PHOTO = "photo_url"
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_EXPIRES = "expires_at"
    }
}
