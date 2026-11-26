---
title: "Server log viewer"
group: "Workspace & files"
order: 11
beta: false
summary: "Open a log file, a <code>.gz</code> log or a live stream from Docker, Kubernetes, journald or SSH for severity highlighting, Follow, filters by level, pattern and time, a summary, and logs merged by time."
description: "A log mode with severity colours, Follow, filters by level, pattern and time, summaries, merged logs, and live streams from Docker, Kubernetes or SSH."
alt: "A .log file in View mode with levels coloured, a filter box, and an error's stack trace highlighted"
---

---
---
Editora reads server logs as records, not lines: each level in its own colour, new lines streaming in as they are written, and filters that cut a long log down to the part that matters. `.log` files open this way, and so do rotated logs, `catalina.out`, `syslog` and files whose content looks like a log.

## Read it

- **Severity colours** mark FATAL, ERROR, WARN, INFO, DEBUG and TRACE inline and in a bar down the left edge, for the common Java, syslog, web-server, .NET and JSON formats.
- **Warnings and errors on the scrollbar**, in amber and red; hover to read one, click to go there.
- **Compressed logs** such as `app.log.2.gz` open directly.

## Follow it live

- **Follow** streams new lines as the file grows, like `tail -f`, and keeps going when the log is rotated. Toggle it with `C-c C-l t` in the Emacs keymap, `Ctrl+Alt+Shift+L` in the others (`Cmd+Option+Shift+L` on macOS).
- **Very large logs** open at the tail, so the newest lines are there at once.
- **Log: Open Log Stream…** follows `docker logs -f`, `kubectl logs -f`, `journalctl -f`, a file over SSH or any command, with the same filters.

## Narrow it down

- **Level and pattern** filters keep only what you want, as you type. `C-c C-l f` in the Emacs keymap focuses them, `Ctrl+Alt+Shift+F` in the others (`Cmd+Option+Shift+F` on macOS).
- **Hide** drops records you never want to see, such as health checks.
- **Whole records**: a matched line brings its stack trace, lines keep their real numbers, and the bar counts what is showing.
- **Times**: **Log: Go to Time…** jumps to a moment, and **Log: Filter by Time…** keeps the records between two times.
- **Remembered filters**: a log opens with the filters you left on it.

## Make sense of it

- **Log: Summarize** reports the levels, records over time, the most frequent messages and the longest silences.
- **Log: Merge Logs…** opens several logs as one, in the order their records were written.

Logs open in View mode, read-only with an **Enable Editing** banner, and Follow keeps streaming.

**On by default.** Turn it off in **Settings → Editor → Logs**. See the [Server log viewer guide](/docs/log-viewer/).
