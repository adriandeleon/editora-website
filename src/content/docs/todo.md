---
title: TODO highlighting
description: Highlight TODO, FIXME, and your own regex patterns in the editor and collect them in a tool window.
category: Workspace
order: 4
---

Editora highlights **TODO / FIXME-style patterns** everywhere they appear,
IntelliJ-style, and collects them in a **TODO** tool window. It's on by default.

## In the editor and the tool window

- Matches are highlighted inline and listed in the **TODO** tool window
  (`tool.todo`, `M-g o`), grouped by file. It scans the open project's tree when
  a project is open, else the open files. Double-click a result to jump to it.
- Matches also show as **overview stripes** over the scrollbar and on the
  minimap edge, each in its pattern's color. Click a stripe to jump, hover for
  the line.

## Patterns

Configure patterns in **Settings → Editor → TODO Highlighting**: a name, a
regex, a color picker, case sensitivity, and an enabled flag. **TODO** and
**FIXME** ship by default, each in its own color. Add a quick one from the
palette with **TODO: Add Highlight Pattern…** (`todo.addPattern`).

## Commands

| Action | Command | Default key |
| --- | --- | --- |
| Toggle the TODO tool window | `tool.todo` | `M-g o` |
| Refresh the scan | `todo.refresh` | (palette) |
| Add a pattern | `todo.addPattern` | (palette) |

Highlighting runs off the UI thread and is debounced, and the project scan is
lazy (only when the tool window is open or refreshed), so it stays cheap. Use
*View: Toggle TODO Highlighting* to turn it off.
