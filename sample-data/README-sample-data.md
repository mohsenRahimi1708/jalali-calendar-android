# Sample data

- `sample_events.ics` — 6 events exercising every shape the app supports:
  all-day (Nowruz), weekly recurring with COUNT, yearly recurring, a single
  timed event with a location + reminder, and a weekly event with UNTIL, plus a TZID=Asia/Tehran anchored weekly event.
  Import into a Google Calendar (Settings → Import & export) to test sync, or
  open it in any calendar client to eyeball the recurrence rules.

- `sample_google_events.json` — a raw `GET /calendars/{id}/events` response
  body, including a `cancelled` row and a `nextSyncToken`. This is the shape
  the sync layer parses; pair it with a MockWebServer (see
  `SyncEngineTest`) to reproduce pull/merge/410 behavior locally.
