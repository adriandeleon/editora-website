---
title: Server log viewer
description: .log files get severity highlighting, a tail -f Follow toggle, open-at-the-tail for huge logs, and live level + regex filtering.
category: Workspace
order: 5
---

Open a `.log` file and Editora switches into a dedicated log mode built for
reading server output. It's **on by default** (Settings → Editor → Logs; toggle
with *View: Toggle Log Viewer*).

## Severity highlighting

Lines are colored by level (ERROR / WARN / INFO / DEBUG / TRACE), both inline and
as a **left-edge bar** that works even on huge logs. It recognizes common
formats: Logback / Log4j, `java.util.logging`, syslog, nginx, structured / JSON,
zerolog, and access logs.

## Follow and large logs

- **Follow** (`tail -f`): a floating toggle streams new lines as the file grows
  and auto-scrolls to the bottom. It keeps streaming even while the buffer is
  read-only.
- **Open at the tail**: very large logs open read-only at the end, so you're not
  waiting on a multi-gigabyte file to load from the top.

Logs open in **View mode** (read-only with an *Enable Editing* banner) by
default, since they're for reading.

## Live filtering

Filter as you type by a **level floor** and a **regex** (or a literal substring
when the query isn't valid regex). A stack trace inherits its record's level, so
an exception stays visible when you filter to `WARN` and above.

## Commands

| Action | Command |
| --- | --- |
| Toggle Follow (tail -f) | `log.toggleFollow` |
| Filter by level | `log.setLevelFilter` |
| Filter by pattern | `log.setRegexFilter` |
| Clear the filter | `log.clearFilter` |
| Next / previous error | `log.nextError` / `log.previousError` |
| Treat the file as a log | `log.viewAsLog` |
| Enable/disable the feature | `view.toggleLogViewer` |
