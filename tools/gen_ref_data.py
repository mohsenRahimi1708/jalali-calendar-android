import random
from datetime import date, timedelta, datetime, timezone
import jdatetime
from dateutil.rrule import rrulestr, DAILY, WEEKLY, MONTHLY, YEARLY
from dateutil.tz import UTC

random.seed(1404)

rows = []
seen = set()
def add(g: date):
    key = (g.year, g.month, g.day)
    if key in seen:
        return
    seen.add(key)
    j = jdatetime.date.fromgregorian(date=g)
    # sanity: jdatetime round-trips itself
    back = j.togregorian()
    assert back == g, (g, j, back)
    rows.append((j.year, j.month, j.day, g.year, g.month, g.day))

# dense: full year 1990 + full leap year 1992 + Jan-Mar 2024 (around Nowruz)
def rng(a, b):
    d = a
    while d < b:
        add(d)
        d += timedelta(days=1)
rng(date(1990, 1, 1), date(1991, 1, 1))
rng(date(1992, 1, 1), date(1993, 1, 1))
rng(date(2024, 3, 1), date(2024, 4, 1))

# boundaries: around leap Feb 29 and year ends, spread across centuries
for y in range(1900, 2201, 11):
    for m, dd in [(2, 28), (3, 1), (12, 31), (1, 1)]:
        try:
            add(date(y, m, dd))
        except ValueError:
            pass
    # Feb 29 when leap
    try:
        add(date(y, 2, 29))
    except ValueError:
        pass

# 500 random days 1850..2250
for _ in range(500):
    y = random.randint(1850, 2250)
    d = date(y, random.randint(1, 12), 1) + timedelta(days=random.randint(0, 27))
    add(d)

# documented anchors
for g in [date(1970, 1, 1), date(1979, 2, 11), date(2000, 3, 20),
          date(2024, 3, 20), date(2016, 4, 11), date(2026, 10, 10),
          date(2021, 3, 21), date(1971, 3, 21)]:
    add(g)

with open('app/src/test/resources/jalali_crosscheck.csv', 'w') as f:
    for r in sorted(rows, key=lambda r: (r[3], r[4], r[5])):
        f.write(','.join(map(str, r)) + '\n')
print('jalali rows:', len(rows))

# ---- recurrence cases vs dateutil ----
def millis(dt):
    return int(dt.timestamp() * 1000)

def case(rrule, start_iso, years=6):
    st = datetime.fromisoformat(start_iso).replace(tzinfo=UTC)
    rule = rrulestr(rrule, dtstart=st)
    end = st.replace(year=st.year + years)
    occ = [dt for dt in rule if dt < end]
    return st, [millis(dt) for dt in occ]

cases = [
    ("FREQ=DAILY;INTERVAL=3;COUNT=6",              "2026-01-05T09:00:00"),
    ("FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR;COUNT=8", "2026-01-05T10:00:00"),
    ("FREQ=MONTHLY;COUNT=5",                       "2026-01-31T08:00:00"),
    ("FREQ=MONTHLY;BYDAY=1FR;COUNT=4",             "2026-01-02T06:00:00"),
    ("FREQ=MONTHLY;INTERVAL=2;BYMONTHDAY=15,-1;COUNT=6", "2026-01-15T12:00:00"),
    ("FREQ=YEARLY;BYMONTH=3;BYMONTHDAY=20;COUNT=3", "2026-03-20T00:00:00"),
    ("FREQ=WEEKLY;BYDAY=TU;UNTIL=20260210T235959Z", "2026-01-06T09:00:00"),
    ("FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;COUNT=10", "2026-01-01T07:00:00"),
    ("FREQ=DAILY;BYDAY=MO,SA;COUNT=8",             "2026-02-07T21:30:00"),
    ("FREQ=YEARLY;COUNT=4",                        "2024-06-15T18:45:00"),
]

lines = []
for rrule, start in cases:
    st, expected = case(rrule, start)
    exp = ', '.join(f'{m}L' for m in expected)
    lines.append(
        f'        Case(\n'
        f'            rrule = "{rrule}",\n'
        f'            startUtcMillis = {millis(st)}L,\n'
        f'            zone = "UTC",\n'
        f'            expected = listOf({exp}),\n'
        f'        ),'
    )
    # print a preview
    print(rrule, '->', len(expected), 'occ')

kotlin = (
    'package com.example.jalicalendar.core.time\n\n'
    '/**\n'
    ' * Expansion expectations generated from python-dateutil `rrulestr`\n'
    ' * (DAILY/WEEKLY/MONTHLY/YEARLY, INTERVAL, COUNT, UNTIL, BYDAY, BYMONTHDAY).\n'
    ' * Regenerate with tools/gen_ref_data.py if the expander changes.\n'
    ' */\n'
    'object RecurrenceTestData {\n'
    '    data class Case(\n'
    '        val rrule: String,\n'
    '        val startUtcMillis: Long,\n'
    '        val zone: String,\n'
    '        val expected: List<Long>,\n'
    '    )\n\n'
    '    val CASES: List<Case> = listOf(\n'
    + '\n'.join(lines) + '\n    )\n}\n'
)
import os
os.makedirs('app/src/test/java/com/example/jalicalendar/core/time', exist_ok=True)
with open('app/src/test/java/com/example/jalicalendar/core/time/RecurrenceTestData.kt', 'w') as f:
    f.write(kotlin)
print('recurrence cases written:', len(cases))
