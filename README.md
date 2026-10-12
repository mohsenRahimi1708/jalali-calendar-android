# Jalali Calendar (تقویم جلالی)

A modern Android calendar built with **Jetpack Compose + Material 3**, featuring a
first-class **Jalali (Persian/Solar Hijri) calendar**, Google Calendar sync, offline
support, and reminders. All UI in Compose (no XML layouts), full RTL, Vazirmatn font.

## Features

- **Jalali + Gregorian** side by side (month/week/day/agenda views).
- **Full offline**: view, create, edit, delete — no network required. Writes are
  queued locally and pushed on the next sync.
- **Google Calendar & Tasks sync** (Calendar API v3 + Tasks API v1):
  - OAuth 2.0 authorization-code flow with **PKCE** (AppAuth, system browser).
  - **Incremental sync** via `syncToken`, with automatic full-resync on `410 Gone`.
  - **Conflict resolution**: last-write-wins (default) or prompt the user.
  - **Push** (Calendar `watch` channels) — optional; requires a webhook relay (below).
- **Background sync**: WorkManager, periodic (15 min) + sync-on-open + sync-now.
- **Reminders**: exact-time alarms with local notifications; Jalali-aware text.
- **Settings**: default calendar/reminder, time zone (default `Asia/Tehran`),
  week start (Saturday), 24-hour, Persian digits, theme (system/light/dark),
  Material You dynamic color, language (system/Persian/English), conflict policy.
- **Accessibility**: TalkBack content descriptions, ≥48dp touch targets.

## Requirements

- Android Studio Ladybug+ (AGP 8.7.3), JDK 17.
- `minSdk 26`, `targetSdk/compileSdk 35`.
- A Google account + OAuth client ID (for sync). The app runs fully offline without one.

## Project structure

```
app/src/main/java/com/example/jalicalendar/
├── core/time/       Jalali conversion, RFC 5545 recurrence, UTC helpers, digits
├── data/
│   ├── local/       Room (Event, Calendar, Reminder, Task, Account) + DAOs
│   ├── remote/      Retrofit clients (Calendar v3, Tasks v1) + DTOs + auth
│   ├── prefs/       DataStore settings
│   └── repo/        Local-first repository implementations
├── domain/          Pure-Kotlin models + repository interfaces
├── auth/            AppAuth OAuth (PKCE) + encrypted token store
├── sync/            SyncEngine, WorkManager workers, conflicts, watch channels
├── reminders/       Notification helper, alarm scheduler, receiver
├── di/              Hilt modules (network, database, repositories)
└── ui/              Compose theme, navigation, screens, components
```

Layering: `ui → domain(repo interfaces) → data(local | remote | prefs)`.

## Build & run

```bash
./gradlew :app:assembleDebug      # APK -> app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:testDebugUnitTest  # 40 unit + integration tests
```

> Builds are tuned for low-memory containers (`gradle.properties`: single JVM,
> in-process Kotlin/KSP, SerialGC). On a normal machine you can raise `-Xmx`.

## Google OAuth setup (for sync)

1. **Enable the APIs** — in the [Google Cloud console](https://console.cloud.google.com/),
   enable **Google Calendar API** and **Google Tasks API** for your project.
2. **OAuth consent screen** — configure it (External is fine for testing). Add your
   Google account as a test user.
3. **Create the client ID** — Credentials → Create credentials → **OAuth client ID** →
   Application type **Android**:
   - Package name: `com.example.jalicalendar`
   - SHA-1: from your debug keystore (below).
4. **Get the SHA-1**:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -storepass android -alias androiddebugkey | grep SHA1
   ```
   (Windows: `%USERPROFILE%\.android\debug.keystore`.)
5. **Pass the client ID to Gradle** — put it in `~/.gradle/gradle.properties`
   (keep it out of version control):
   ```properties
   googleClientId=YOUR_CLIENT_ID.apps.googleusercontent.com
   ```
6. **Redirect URI** — already registered in the manifest as
   `com.example.jalicalendar:/oauth2callback`. When creating a **Web** type client
   (only needed if you use the push relay), add that URI as an authorized redirect.

Scopes requested: `calendar`, `tasks`, `openid`, `email`, `profile`.

Without a client ID the app still builds and runs; the Calendars screen shows a
setup hint instead of the sign-in button.

## Mirror setup (networks where dl.google.com is blocked)

If Gradle/AGP can't reach `dl.google.com` / `maven.google.com`, swap in the
**Aliyun** mirrors. Edit `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        mavenCentral()
    }
}
```

For the Gradle **distribution** itself, point `distributionUrl` in
`gradle/wrapper/gradle-wrapper.properties` at a mirror:
```properties
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.11.1-bin.zip
```
Tencent and Huawei also mirror Maven Central and Google:
`https://mirrors.cloud.tencent.com/nexus/repository/maven-public/`,
`https://repo.huaweicloud.com/repository/maven/`.

## Push notifications (optional)

Google Calendar `watch` channels can push change notifications, but they require a
**public HTTPS webhook** that the app controls — a pure client app doesn't have one.
This is implemented and **off by default**. To enable it, run a tiny relay that
receives Google's POST and forwards a "sync now" signal to the device (e.g. via FCM),
then pass:

```properties
watchWebhookUrl=https://your-relay.example.com/google-watch
watchWebhookSecret=optional-shared-secret
```

The primary sync mechanism remains WorkManager (15-min periodic + sync-on-open),
which is fully functional without any relay.

## Testing

- **Unit** — Jalali conversion (1,383-row cross-check vs Python `jdatetime`),
  RFC 5545 recurrence expansion (cross-checked vs `dateutil`), Persian digits,
  UTC/DST helpers, DTO mapping.
- **Integration** — `SyncEngineTest` drives the real Retrofit clients against a
  local **MockWebServer**: full pull + sync-token advance, `410 Gone` full-resync
  fallback, and last-write-wins conflict handling.

Regenerate the reference data with `tools/gen_ref_data.py` if the date logic changes (needs Python 3 with `pip install jdatetime python-dateutil`; output is deterministic — seed 1404 — so unchanged output also confirms stability).

## Sample data

See `sample-data/` — an `.ics` file covering all-day, recurring, and timed events,
and a raw Calendar API JSON response for exercising the sync parser.

## Design decisions & assumptions

- **Time**: all instants stored as **UTC epoch millis** in Room; the IANA zone is
  applied only at the display edge. All-day events are anchored at UTC midnight.
- **Jalali conversion**: own algorithm + leap-year table (`core/time/JalaliConverter`),
  a Kotlin port of the MIT-licensed `jalaali-js` (Borkowski algorithm), validated
  against the official Iranian calendar. Years −61…3177.
- **Recurrence**: RFC 5545 RRULE (FREQ/INTERVAL/COUNT/UNTIL/BYDAY/BYMONTHDAY/BYMONTH/WKST),
  RDATE/EXDATE. DST-safe (wall-clock preserved across transitions). When DTSTART
  doesn't match its own rule (RFC calls this undefined) we **keep DTSTART** as the
  first occurrence so a locally saved event is never hidden.
- **IDs**: local UUID primary keys, so rows exist before their first server push.
  Unique `(calendarId, remoteId)` prevents duplicate rows on repeated pulls.
- **Deletes**: never-synced rows are hard-deleted; synced rows are soft-deleted and
  the server copy is removed on the next sync (offline-delete-safe).
- **Task lists** reuse the Calendar entity with `type = tasklist` (avoids a 6th table
  while keeping Google Tasks lists intact).
- **Conflicts**: last-write-wins by comparing local `localUpdatedAt` vs remote
  `updated`; the PROMPT policy surfaces a conflict instead of overwriting.

## Third-party licenses

- **Vazirmatn** font — SIL Open Font License 1.1 (`LICENSE-OFL-Vazirmatn.txt`).
- **jalaali-js** algorithm — MIT (ported; see `core/time/JalaliConverter.kt`).
- No copyrighted Google Calendar assets are used; icons are Material/origina
