---
title: "Server log viewer"
group: "Workspace & files"
order: 9
beta: false
summary: "Open a <code>.log</code> file for severity highlighting, a <code>tail -f</code> Follow toggle, open-at-the-tail for huge logs, and live level + regex filtering."
---

`.log` files open in a dedicated log mode built for reading server output.

- **Severity highlighting**: ERROR / WARN / INFO / DEBUG / TRACE, both inline and as a left-edge bar that works even on huge logs. It recognizes Logback/Log4j, `java.util.logging`, syslog, nginx, structured/JSON, zerolog, and access logs.
- **Follow** (`tail -f`): a floating toggle streams new lines as the file grows and auto-scrolls. Very large logs **open at the tail** (read-only at the end).
- **Live filtering**: filter as you type by a level floor and a regex (or a literal substring when it isn't valid regex). A stack trace inherits its record's level so it stays visible.

Logs open in **View mode** (read-only with an *Enable Editing* banner) by default, and follow keeps streaming while read-only. On by default (Settings → Editor → Logs). Commands: `log.toggleFollow`, `log.setLevelFilter`, `log.setRegexFilter`, `log.clearFilter`, `log.viewAsLog`, and `view.toggleLogViewer`. See the [log viewer guide](/docs/log-viewer).
