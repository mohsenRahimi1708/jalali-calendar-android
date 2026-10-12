package com.example.jalicalendar.domain.model

/**
 * A calendar collection. Also doubles as a Google Tasks task list — see [type].
 * ([type] == [TYPE_TASK_LIST] rows are task lists, not event calendars.)
 */
data class Calendar(
    val id: String,
    val accountId: String? = null,
    val remoteId: String? = null,
    val type: String = TYPE_CALENDAR,
    val title: String,
    val description: String? = null,
    val colorArgb: Int = 0xFF006750.toInt(),
    val timeZone: String = "Asia/Tehran",
    val isPrimary: Boolean = false,
    val accessRole: String = "owner", // owner | writer | reader
    val hidden: Boolean = false,
    val selected: Boolean = true, // visible in the UI
    val syncToken: String? = null, // incremental sync cursor (Calendar API)
    val lastSyncedAtMillis: Long? = null,
    val localUpdatedAtMillis: Long = 0L,
    val dirty: Boolean = false,
    val deleted: Boolean = false,
) {
    val canWrite: Boolean get() = accessRole == "owner" || accessRole == "writer"

    companion object {
        const val TYPE_CALENDAR = "calendar"
        const val TYPE_TASK_LIST = "tasklist"
        const val LOCAL_ID_PREFIX = "local_"
        const val DEFAULT_COLOR = 0xFF006750.toInt()
    }
}
