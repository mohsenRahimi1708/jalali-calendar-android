# HANDOFF.md — Jalali Calendar (تقویم جلالی) for Android

> Complete technical handoff for **github.com/mohsenRahimi1708/jalali-calendar-android**.
> Everything a new maintainer needs: architecture, every module, design decisions,
> verification evidence, known gaps, and the roadmap. Companion to `README.md`
> (which covers user-facing setup: OAuth, mirrors, building).

**Status:** v1.0.0 complete and verified · 6,678 LOC main source (59 Kotlin files) ·
40/40 tests green · Android lint 0 errors · debug APK builds and is signed for release.

---

## 1. What this app is

A standalone Android calendar with a first-class **Jalali (Solar Hijri / شمسی)** calendar,
Google Calendar & Tasks sync, offline-first storage, and local reminders.

| Capability | Detail |
|---|---|
| Calendars | Jalali + Gregorian together; month / week / day / agenda views |
| Offline | Full CRUD with zero network; writes queue locally and push on next sync |
| Sync | Google Calendar API v3 + Tasks API v1, OAuth 2.0 + PKCE, incremental sync tokens |
| Conflicts | Last-write-wins (default) or prompt-the-user policy |
| Push | Calendar `watch` channels implemented; off by default (needs a webhook relay — §8) |
| Reminders | AlarmManager exact alarms + local notifications, Jalali-aware text |
| Localization | English + Persian (fa), full RTL, Vazirmatn font, Persian digits option |
| Accessibility | TalkBack content descriptions, ≥48 dp touch targets |
| minSdk / target | 26 (Android 8.0) / 35 (Android 15) |

## 2. Tech stack (exact versions)

`gradle/libs.versions.toml` is the single source of truth.

| Layer | Choice |
|---|---|
| Language | Kotlin 2.0.21 (JVM target 17) |
| Build | AGP 8.7.3, Gradle 8.11.1, KSP 2.0.21-1.0.28 |
| UI | Jetpack Compose (BOM 2024.12.01), Material 3, Navigation-Compose 2.8.5 — **no XML layouts** |
| DI | Hilt 2.52 |
| DB | Room 2.6.1 (schema exported at `app/schemas/`) |
| Prefs | DataStore 1.1.1 (Preferences) |
| Background | WorkManager 2.10.0 |
| Network | Retrofit 2.11.0 + OkHttp 4.12.0 + kotlinx-serialization 1.7.3 |
| OAuth | AppAuth 0.11.1 (net.openid), EncryptedSharedPreferences 1.1.0-alpha06 |
| Tests | JUnit4, Truth 1.4.4, MockK 1.13.13, MockWebServer (OkHttp), coroutines-test |

`applicationId`: `com.example.jalicalendar` · versionCode 1 · versionName 1.0.0.

## 3. Architecture

```
ui (Compose + ViewModels, Hilt)
   └─▶ domain (pure-Kotlin models + repository interfaces)      ← the seam
          └─▶ data/local (Room, DataStore)  ⇄  data/remote (Retrofit)
                     ▲
        sync (SyncEngine over both), auth (AppAuth), reminders (alarms)
```

- **Local-first:** every read comes from Room; the network only reconciles. UI never
  touches Retrofit. ViewModels observe repository flows.
- **Single-direction data flow for writes:** write Room (+ queue if unsynced) →
  `SyncWorker` pushes → server IDs/tokens backfill into the same rows.
- All instants are **UTC epoch millis** in storage; the IANA timezone is applied only
  at the display edge (`ui/format/Formatting.kt`).

### Package map (every file)

```
app/src/main/java/com/example/jalicalendar/
├── JalaliCalendarApp.kt        @HiltAndroidApp; schedules periodic sync on start
├── MainActivity.kt             Single-activity Compose host; splash; POST_NOTIFICATIONS request
├── core/time/                  ── pure Kotlin, zero Android deps (unit-testable on JVM) ──
│   ├── JalaliConverter.kt      Jalali⇄Gregorian via Julian Day Number (Borkowski/jalaali-js port)
│   ├── JalaliDate.kt           Validated value type; rejects 1404-12-30-style invalid dates
│   ├── Rfc5545.kt              "20260105T090000Z" / RFC3339 / date-only → UTC millis
│   ├── RecurrenceRule.kt       RRULE parser: FREQ INTERVAL COUNT UNTIL BYDAY BYMONTHDAY BYMONTH WKST
│   ├── RecurrenceExpander.kt   Zone-aware occurrence generation (DST wall-clock preserving)
│   ├── OccurrenceExpander.kt   Event-level: RRULE + RDATE − EXDATE, malformed-rule fallback
│   ├── TimeUtils.kt            UTC↔civil-date bridge, DST-aware day boundaries
│   └── PersianDigits.kt        ۰-۹ ⇄ 0-9 + localize()
├── domain/
│   ├── model/                  Event, Calendar, Reminder, Task, Account, AppSettings, ConflictPolicy
│   └── repo/                   5 repository interfaces + SettingsRepository
├── data/
│   ├── local/entity/Entities.kt    5 Room entities (§4)
│   ├── local/dao/                  EventDao (+ReminderDao), CalendarDao, TaskDao
│   ├── local/JalaliDatabase.kt     Room DB, version 1, schema exported
│   ├── local/Mappers.kt            entity⇄domain
│   ├── prefs/SettingsRepositoryImpl.kt  10 DataStore keys (§4.2)
│   ├── remote/GoogleApis.kt        Retrofit interfaces (§7 endpoints)
│   ├── remote/GoogleDtos.kt        Calendar v3 + Tasks v1 DTOs (kotlinx.serialization)
│   ├── remote/AuthInterceptor.kt   Bearer injection + 401→refresh authenticator
│   └── repo/                       4 repository impls (local-first + sync queue)
├── auth/
│   ├── AuthManager.kt          AppAuth: PKCE authorize, code exchange, refresh, sign-out
│   └── TokenStore.kt           EncryptedSharedPreferences token persistence
├── sync/
│   ├── SyncEngine.kt           The core: pull/push per calendar, tokens, 410, conflicts (§7)
│   ├── GoogleMappers.kt        API DTO ⇄ domain (incl. RRULE: prefix, reminders, recurrence)
│   ├── SyncWorker.kt           Hilt worker; one-shot + unique periodic (§7.4)
│   ├── SyncCursorStore.kt      Per-calendar syncToken + calendarListCursor storage
│   ├── ConflictStore.kt        Pending-conflict records for the PROMPT policy
│   ├── SyncStatusStore.kt      In-memory sync state → UI (idle/syncing/error)
│   └── WatchManager.kt         Google watch-channel register/renew (optional, §8)
├── reminders/
│   ├── NotificationHelper.kt   Channel + posting (runtime POST_NOTIFICATIONS guard)
│   ├── ReminderScheduler.kt    AlarmManager per (event, reminder); stable request codes
│   └── ReminderReceiver.kt     Builds Jalali-aware subtitle, posts notification
├── di/                         DatabaseModule, NetworkModule, RepositoryModule
└── ui/
    ├── JalaliCalendarRoot.kt   NavHost + scaffold + snackbars
    ├── Strings.kt              Compose-side i18n (en/fa), keyed strings
    ├── navigation/AppNavHost.kt Routes: main, search, calendars, settings, event/{id}
    ├── theme/                  Color.kt, Theme.kt (dynamic color), Type.kt (Vazirmatn)
    ├── format/Formatting.kt    Jalali/Greg date-time formatting, Persian digits
    ├── components/             MonthGrid, EventChip, JalaliDatePickerDialog
    └── screens/
        ├── main/               MainCalendarScreen, MainViewModel, WeekDayViews
        ├── eventedit/          EventEditScreen + EventEditViewModel (create & edit)
        ├── search/             SearchScreen (events by title/location)
        ├── calendars/          CalendarsScreen (list, visibility, Google sign-in/out)
        └── settings/           SettingsScreen (all 10 switches, §4.2)
```

Tests mirror this: `app/src/test/java/.../core/time/*Test.kt` (6 classes) and
`sync/GoogleMappersTest.kt`, `sync/SyncEngineTest.kt`. `core/time/RecurrenceTestData.kt`
is **generated** (§10).

## 4. Data layer

### 4.1 Room (5 tables, version 1, schema in `app/schemas/`)

| Table | Purpose | Key columns |
|---|---|---|
| `events` | Calendar events | `id` (local UUID PK), `calendarId`, `remoteId?`, `title`, `startUtcMillis`, `endUtcMillis`, `allDay`, `rrule?`, `rdateJson`, `exdateJson`, `location?`, `description?`, `localUpdatedAt`, `remoteUpdated?`, `deleted` (soft), `syncState?` |
| `calendars` | Google calendars **and** task lists | `id` PK, `remoteId?`, `type` (`calendar`/`tasklist`), `name`, `color`, `visible`, `syncToken?` |
| `reminders` | Per-event reminders (min 0/1 per event) | `id` PK, `eventId` FK, `minutesBefore`, `remoteId?` |
| `tasks` | Google Tasks items | `id` PK, `taskListId`, `remoteId?`, `title`, `notes?`, `dueUtcMillis?`, `status`, `deleted` |
| `accounts` | Signed-in Google account record | `id`, `email`, `name` |

- **IDs are local UUIDs** so rows exist before their first server push.
- **Deletes:** never-synced → hard delete; synced → soft-delete flag + server DELETE on
  next sync (offline-safe).
- Uniqueness on `(calendarId, remoteId)` prevents duplicate rows on repeated pulls.
- **Task lists reuse the `calendars` table** with `type = 'tasklist'` — deliberate
  simplification, keeps Google list semantics without a 6th table.

### 4.2 DataStore settings (file `settings`)

| Key | Values (default bold) |
|---|---|
| `theme_mode` | **system** / light / dark |
| `dynamic_color` | **on** / off (Material You) |
| `time_zone` | **Asia/Tehran** / any IANA id |
| `week_start` | **saturday** / sunday / monday |
| `use_24_hour` | **on** (boolean, default true) / off — 24h vs 12h clock |
| `persian_digits` | **auto** / on / off |
| `language` | **system** / persian / english |
| `default_calendar_id` | first visible calendar |
| `default_reminder_minutes` | **0** (none) / 5 / 10 / 15 / 30 / 60 |
| `conflict_policy` | **lww** / prompt |

## 5. Core time engine (the crown jewel — most-tested module)

- **Jalali conversion** (`JalaliConverter.kt`): faithful Kotlin port of the Borkowski
  algorithm with leap-cycle breaks table from **jalaali-js v2.0.1 (MIT)**. Works via
  Julian Day Numbers both directions, valid for years −61…3177. *Why not a library?*
  No maintained Kotlin/JVM equivalent with the same accuracy guarantees; the port is
  1,383-row cross-validated (§10).
- **Recurrence** (`RecurrenceRule`/`RecurrenceExpander`/`OccurrenceExpander`): RFC 5545
  subset — FREQ, INTERVAL, COUNT, UNTIL, BYDAY (`1MO`, `-1FR`), BYMONTHDAY, BYMONTH,
  WKST; RDATE/EXDATE; unknown RRULE parts ignored **per spec**. Generation is
  zone-aware: wall-clock time is preserved across DST (Asia/Tehran +3:30⇄+4:30 tested;
  nonexistent local times in spring-forward gaps shift forward, never crash).
- **Documented design choice:** when DTSTART doesn't match its own RRULE (RFC calls
  this *undefined*; python-dateutil drops DTSTART), **we keep DTSTART as occurrence #1**
  — a locally saved event must never be invisible. Tested in `RecurrenceExpanderTest`.
- **All-day events** anchor at UTC midnight and render as date-only in every zone.
- `FREQ=DAILY;BYDAY=MO,SA` filters weekdays (dateutil does too; naive expansion doesn't).

## 6. UI layer

- **Screens:** month grid (Jalali primary, Gregorian sub-labels, out-of-month dimming,
  up to 3 event chips/day + "+N"), week view (hour grid, RTL-safe), day view, agenda
  list, event create/edit (all-day toggle, recurrence presets + custom RRULE, reminder
  picker, calendar picker, Jalali date picker + time picker), search, calendar list
  (visibility toggles, color dots, Google account section), settings.
- **Navigation:** single-activity Compose; routes `main`, `search`, `calendars`,
  `settings`, `event/{id}` (`new` sentinel for creation).
- **RTL:** entire app mirrors under `fa` (Compose handles layout direction);
  week/day time gutters mirror correctly.
- **i18n:** `Strings.kt` (Compose) + `res/values/strings.xml` + `values-fa/` (system
  resources for notifications). Persian digits honor the `persian_digits` setting.
- **A11y:** content descriptions on all interactive components; ≥48 dp targets.
- **Theme:** M3 with dynamic color (Material You), Vazirmatn for both scripts.

## 7. Sync engine (highest-risk module — read before touching)

### 7.1 OAuth 2.0 authorization-code + PKCE (AppAuth)

1. `AuthManager.signIn()` opens the system browser with a generated code verifier/challenge.
2. Redirect → `com.example.jalicalendar:/oauth2callback` (manifest intent-filter).
3. Code exchanged for access + refresh + ID token; identity parsed from the ID token.
4. Tokens persist in `TokenStore` (EncryptedSharedPreferences). `AuthInterceptor`
   attaches `Bearer`; a 401 triggers the OkHttp authenticator → refresh → retry once.
5. Scopes: `calendar`, `tasks`, `openid`, `email`, `profile`.

### 7.2 Endpoints used (all in `GoogleApis.kt`)

```
GET/PATCH/DELETE  users/me/calendarList[/{id}]     POST users/me/calendarList
GET/POST/PATCH/DELETE  calendars/{id}/events[/{eventId}]   (with ?syncToken / ?maxResults / ?pageToken / ?singleEvents / ?showDeleted)
GET calendars/{id}/acl        POST calendars/{id}/watch      POST users/me/calendarList/watch
GET users/me/settings
GET users/@me/lists[/{tasklist}/tasks[/{taskId}]]
```

### 7.3 Pull / push algorithm (`SyncEngine.kt`)

**Pull (per calendar):**
```
if syncToken exists  → GET events?syncToken=…
else                 → GET events?showDeleted=true&singleEvents=true (first page)
loop pages via nextPageToken; map DTO→domain; upsert by (calendarId, remoteId)
cancelled rows       → hard-delete local row
on HTTP 410          → wipe syncToken + local rows of that calendar → full resync
advance syncToken only after the whole page set succeeds
```
**Push:** local rows flagged unsynced → POST/PATCH/DELETE to the server, then backfill
`remoteId`/timestamps. All-day, recurrence (`RRULE:` prefix on the wire — `GoogleMappers`
adds it; a missing prefix was a real bug caught by tests), reminders, and tasks included.

**Conflicts:** compare local `localUpdatedAt` vs remote `updated`:
- `lww` (default): newer side wins.
- `prompt`: record in `ConflictStore`, surface in UI; user chooses.

**Calendar-list sync** uses the same engine with a synthetic cursor stored in
`SyncCursorStore` (a dedicated store, because the calendars-table `setSyncToken` is an
UPDATE that would silently no-op for a non-existent row — subtle bug found in review).

### 7.4 WorkManager scheduling

- Unique periodic work `jalical_sync_periodic`, interval **15 min** (platform minimum),
  `KEEP` policy, network-constrained, battery-not-low.
- One-shot expedited runs: app start, pull-to-refresh, "Sync now", post-write.
- Worker is `@HiltWorker`; scheduling starts in `JalaliCalendarApp.onCreate`.

## 8. Watch channels (push) — implemented, off by default

Google can POST to a webhook when events change, but that requires a **public HTTPS
endpoint the app controls** — impossible for a pure client without a relay. `WatchManager`
implements registration (`watch` on calendarList + each calendar), renewal before
`expiration`, and channel-id bookkeeping. Disabled unless `watchWebhookUrl` /
`watchWebhookSecret` are supplied (Gradle properties → BuildConfig). WorkManager remains
the primary sync trigger. To enable: run a tiny relay that receives Google's POST and
forwards a "sync now" signal (e.g. FCM data message) to the device.

## 9. Reminders

- One alarm per (event, reminder) via `ReminderScheduler`; **stable request codes**
  derived from (eventId, reminderId) so alarms are distinct and cancellable.
- `setExactAndAllowWhileIdle` on API 31+ **only when** `SCHEDULE_EXACT_ALARM` is granted
  (not granted by default on 12+); otherwise inexact `set()` — reminders still fire,
  just imprecisely. Never crashes on permission denial.
- `ReminderReceiver` → `NotificationHelper` (one `event_reminders` channel,
  IMPORTANCE_HIGH, BigTextStyle, runtime `POST_NOTIFICATIONS` guard; a denial silently
  skips alerts). Jalali-aware subtitle: "Today 09:30" / "Tomorrow …" / "Oct 5 …".
- Editor hooks: save schedules, edit replaces, delete cancels. **Sync pull also
  re-schedules** reminders for pulled events (§12 gap closed).

## 10. Verification evidence (what "done" means here)

- **40/40 unit + integration tests**, all green in the final run:
  `JalaliConverterTest`(7) `RecurrenceExpanderTest`(10) `RecurrenceRuleTest`(6)
  `PersianDigitsTest`(4) `TimeUtilsTest`(4) `GoogleMappersTest`(6) `SyncEngineTest`(3).
- **Cross-validation vs Python references** (`tools/gen_ref_data.py`, deterministic seed 1404):
  - `jdatetime`: 1,383-row conversion table (1850–2250, dense 1990/1992/2024), both
    directions + every-day round-trip + +1-day monotonicity.
  - `dateutil`: 10 generated RRULE cases (DAILY/WEEKLY/MONTHLY/YEARLY × INTERVAL/COUNT/
    UNTIL/BYDAY/BYMONTHDAY). Regeneration reproduced byte-identical output → stability proof.
- **DST:** Asia/Tehran historical DST keeps 08:00 local; Europe/Berlin spring-forward
  gap shifts 02:30→03:30.
- **Anchors:** 1348-10-11 = 1970-01-01; 1357-11-22 = 1979-02-11; Nowruz 1379/1403/1405;
  leap years 1395/1399/1403 (not 1404/1405); month lengths 31×6/30×5/29–30.
- **Integration:** `SyncEngineTest` drives the real Retrofit stack against MockWebServer:
  full pull + syncToken advance, `410` full-resync fallback, LWW conflict.
- **Android lint:** 0 errors (remaining: dependency-version notices +1 intentional
  `mipmap-anydpi-v26` warning, see §12).
- **APK:** `aapt2 dump badging` verified — package, label, all 7 permissions,
  launchable activity. Debug-signed, installable.

## 11. Building & testing (quick reference)

```bash
./gradlew :app:assembleDebug            # APK → app/build/outputs/apk/debug/
./gradlew :app:testDebugUnitTest        # all 40 tests
./gradlew :app:lintDebug                # must stay at 0 errors
# needs: JDK 17, Android SDK 35; low-memory tuning lives in gradle.properties
# network-blocked regions: Aliyun/Tencent mirrors (README §Mirror setup)
# reference data: pip install jdatetime python-dateutil && python3 tools/gen_ref_data.py
```

`gradle.properties` is tuned for a 4–8 GB container: single JVM (Gradle+Kotlin+KSP
in-process), SerialGC, metaspace-capped, no daemon. Raise `-Xmx` freely on a normal
machine. The `.toolchain/` dir (JDK + SDK) is disposable and git-ignored.

## 12. Known gaps / deliberate trade-offs (be honest to yourself and users)

1. **No emulator/KVM in the build container** → UI was verified by compilation + APK
   packaging, *not* visually. First-run visual QA on a real device is the top pending task.
2. **Real Google sign-in untested** (no OAuth client ID in CI) → the flow is spec-correct
   and the code compiles, but device testing with a real client ID is required (README §OAuth).
3. **`mipmap-anydpi-v26` warning kept:** lint suggests merging into `mipmap-anydpi`,
   but AAPT2 failed to resolve the launcher icon from the plain folder; reverted.
   Investigate before "fixing" this warning.
4. **Watch push needs a relay** (§8) — implemented but dormant; primary sync is periodic.
5. **Recurrence subset:** no BYSETPOS/BYEASTER/complex BYWEEKNO; sufficient for Google
   Calendar's common rules but not the full RFC grammar.
6. **Tasks UI is minimal** — data layer + sync are complete; the UI shows task lists but
   there is no dedicated task board screen.
7. **Room schema at version 1 with no migration tests yet** — add before schema changes.

## 13. Roadmap (suggested order)

1. Device QA pass (visual, RTL, TalkBack, reminders on Android 12/14/15).
2. OAuth client ID + real-account sync test; fix whatever surfaces.
3. Compose UI test suite (`createAndroidComposeRule` — deps already wired) for month
   grid, editor validation, settings persistence.
4. Room migration test harness before any schema change.
5. FCM relay for instant push (replaces polling where possible).
6. Task board screen; widgets (month + next-event); export/import `.ics` locally.

## 14. Troubleshooting

| Symptom | Cause / fix |
|---|---|
| `JAVA_HOME is not set` | Restore JDK 17 (e.g. Temurin tarball) and export `JAVA_HOME` |
| Gradle can't download from `dl.google.com` | Apply Aliyun/Tencent mirrors (README) |
| `Metaspace OOM` during KSP/Hilt | Use the shipped `gradle.properties` JVM tuning |
| Sync silently does nothing | No client ID / not signed in (README §OAuth); check account section |
| Reminders don't fire | Android 12+ exact-alarm permission; app falls back to inexact — check Settings → Alarms |
| Tests fail after date-logic edits | Regenerate refs (§10) and update expectations deliberately — diffs are spec decisions |

## 15. Licensing & credits

- App code: MIT recommended (add a `LICENSE` file — pending).
- **Vazirmatn** font — SIL OFL 1.1 (`LICENSE-OFL-Vazirmatn.txt`, kept in repo).
- **jalaali-js** algorithm — MIT (ported; credited in `JalaliConverter.kt`).
- No Google-owned calendar assets, icons, or branding are used or implied.

---
*Generated as part of the v1.0.0 handoff. Repo: github.com/mohsenRahimi1708/jalali-calendar-android ·
APK: Releases → v1.0.0.*
