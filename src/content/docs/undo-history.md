---
title: Undo History
description: Jump back to any recent in-session editing checkpoint from a filterable popup or a tool window.
category: Editing
order: 6
---

Editora keeps an in-session **timeline of checkpoints** as you edit, one per
typing burst. It's finer-grained than save-based
[local file history](/docs/workspace#local-file-history), and session-only
(it's not persisted, and it's disabled for very large files).

## The popup (recommended)

`undoHistory.jump` (`M-g v`) opens the active buffer's checkpoints as a
filterable popup, each with a caret-line preview and capture time. Type to
filter, then pick one to jump back to that state. The jump is a single undoable
restore, so you can `C-z` it.

## The tool window

The **Undo History** tool window (`M-g u`) shows the same timeline; double-click
or Enter on a checkpoint to jump back. Its side-stripe button is off by default
(the popup is the primary entry point); enable it in **Settings → Tool Windows**
or with `tool.undoHistory`.

## Related

This complements **word/line-level undo**, where one `C-z` undoes a word or line
rather than a whole contiguous burst. For longer-term recovery across sessions,
see [local file history](/docs/workspace#local-file-history).
