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
  (`tool.todo`, `M-g o`), grouped by file. Double-click a result to jump to it.
- In a project window, the tool window scans the project's tree, plus any open
  files outside it.
- In the **No Project** window, the tool window lists only the current tab and
  keeps up as you edit it. It is hidden, stripe button included, while the tab
  has no TODOs; an unsaved new file counts as having none. If it was open when
  it was hidden, it comes back on the next tab or edit that has TODOs.
- Matches also show as **overview stripes** over the scrollbar and on the
  minimap edge, each in its pattern's color. Click a stripe to jump, hover for
  the line.

## Patterns

Configure patterns in **Settings → TODO → TODO Highlighting**: a name, a
regex, a color picker, case sensitivity, and an enabled flag. Six ship by
default, each in its own color: **TODO**, **FIXME**, **HACK**, **NOTE**,
**XXX**, and **DONE**. Add a quick one from the palette with **TODO: Add
Highlight Pattern…** (`todo.addPattern`).

## Structured TODOs

A match can carry a tag and a priority after its keyword:

```
// TODO [auth] (high) fix token refresh
```

The form is `KEYWORD [tag] (priority) description`. Only the keyword is
required; the tag and the priority are optional and come in that order. A colon
straight after the keyword is fine (`TODO: fix it`). The priority is one of
`critical`, `high`, `medium`, or `low`, in any letter case. Each part gets its
own color, set in **Settings → TODO → TODO Part Colors** or with
`todo.setPartColor`.

The tool window's **Group by** selector groups matches by **File**,
**Priority**, **Tag**, or **Keyword**. Right-click a match to edit it in your
source: **Mark Done** rewrites its keyword to `DONE` (**Reopen** reverses it),
**Priority** sets or clears the priority, and **Edit Description…** changes the
text.

## Commands

| Action | Command | Emacs key |
| --- | --- | --- |
| Toggle the TODO tool window | `tool.todo` | `M-g o` |
| Refresh the scan | `todo.refresh` | (palette) |
| Add a pattern | `todo.addPattern` | (palette) |
| Next / previous TODO | `todo.next` / `todo.previous` | `M-g ]` / `M-g [` |
| Set a part color | `todo.setPartColor` | (palette) |

Highlighting runs off the UI thread and is debounced, and the project scan is
lazy (only when the tool window is open or refreshed), so it stays cheap.
Without a project, `tool.todo` and `todo.refresh` on a tab with no TODOs say so
in the status bar instead of opening an empty window. Use
*View: Toggle TODO Highlighting* to turn it off.
