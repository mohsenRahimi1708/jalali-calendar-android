package com.example.jalicalendar.sync

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Account-level sync cursors that don't belong to a calendar row — currently
 * just the calendarList incremental token. Plain SharedPreferences: written
 * once per pass, tiny, and survives process death like Room does.
 */
@Singleton
class SyncCursorStore @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("sync_cursors", Context.MODE_PRIVATE)

    fun get(key: String): String? = prefs.getString(key, null)?.takeIf { it.isNotEmpty() }

    fun set(key: String, token: String?) {
        prefs.edit().apply {
            if (token.isNullOrEmpty()) remove(key) else putString(key, token)
        }.apply()
    }

    companion object {
        const val CALENDAR_LIST = "calendarlist_sync_token"
    }
}
