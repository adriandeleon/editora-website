---
title: "TODO highlighting and tool window"
description: "Configurable regex patterns (TODO, FIXME, and your own) are highlighted in the editor and collected in a new TODO tool window, with scrollbar and minimap stripes."
date: 2026-06-22
---

Editora now highlights **TODO / FIXME-style patterns** everywhere they appear,
IntelliJ-style, and collects them in a new **TODO** tool window (`M-g o`).

- Matches are highlighted in the editor and listed in the tool window, grouped
  by file. It scans the open project's tree when a project is open, else the
  open files. Double-click a result to jump to it.
- Matches also show as **overview stripes** over the scrollbar and on the
  minimap edge, each in its pattern's color. Click a stripe to jump, hover for
  the line.
- Patterns are fully configurable in **Settings → Editor → TODO Highlighting**:
  name, regex, a color picker, case sensitivity, and enabled. TODO and FIXME
  ship by default, each in its own color.

It's on by default. Highlighting runs off the UI thread and is debounced, and
the project scan is lazy (only when the tool window is open or refreshed).
Commands: `tool.todo`, `todo.refresh`, `todo.addPattern`, and
`view.toggleTodoHighlight`.
