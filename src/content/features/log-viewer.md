---
title: "Server log viewer"
group: "Workspace & files"
order: 11
beta: false
summary: "Open a log file, a <code>.gz</code> log or a live stream from Docker, Kubernetes, journald or SSH for severity highlighting, Follow, filters by level, pattern and time, a summary, and logs merged by time."
alt: "A .log file in View mode with levels coloured, a filter box, and an error's stack trace highlighted"
---

`.log` files open in a dedicated log mode built for reading server output. So do rotated logs (`app.log.1`), `access_log`, `syslog`, `catalina.out`, `nohup.out`, and files such as `server.out` whose content looks like a log.

- **Severity highlighting**: FATAL / ERROR / WARN / INFO / DEBUG / TRACE, both inline and as a left-edge bar that works even on huge logs. It recognizes Logback/Log4j, `java.util.logging`, syslog, nginx, structured/JSON, zerolog, .NET, klog, pino/bunyan, and access logs.
- **Follow** (`tail -f`): a Follow button in the bar above the log streams new lines as the file grows and auto-scrolls. It keeps going when the log is rotated. Very large logs **open at the tail** (read-only at the end).
- **Live filtering**: filter as you type by a level floor and a regex (or a literal substring when it isn't valid regex), and a second field **hides** the records you don't want, such as health checks or framework stack frames. A filter keeps or hides whole records, so a matched line brings its stack trace, lines keep their real numbers, and the bar shows how many are showing ("65 of 185 lines").
- **Times**: each line's timestamp is read, so **Go to Time…** jumps to a moment and **Filter by Time…** shows the records between two times.
- **Warnings and errors on the scrollbar**, in amber and red; hover to read one, click to go there.
- **Live logs from other places**: **Open Log Stream…** follows `docker logs -f`, `kubectl logs -f`, `journalctl -f`, `ssh host tail -F` or any command, in a log tab with the same filters.
- **Summarize** writes a report of a log's levels, records over time, most frequent messages and longest silences; **Merge Logs…** opens several logs as one, in the order their records were written.
- **Compressed logs** (`app.log.2.gz`) open directly, and a log opens with the filters you left on it.

Logs open in **View mode** (read-only with an *Enable Editing* banner) by default, and follow keeps streaming while read-only. On by default (Settings → Editor → Logs). Commands: `log.toggleFollow`, `log.setLevelFilter`, `log.setRegexFilter`, `log.setHideFilter`, `log.clearFilter`, `log.focusFilter`, `log.goToTime`, `log.setTimeRange`, `log.openStream`, `log.summarize`, `log.mergeLogs`, `log.viewAsLog`, and `view.toggleLogViewer`. See the [log viewer guide](/docs/log-viewer).
