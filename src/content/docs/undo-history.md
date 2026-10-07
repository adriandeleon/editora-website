---
title: Undo History
description: Jump back to any recent in-session editing checkpoint from a filterable popup or a tool window.
category: Editing
order: 6
---

Editora keeps an in-session **timeline of checkpoints** as you edit, one per
typing burst. It's finer-grained than save-based
[local file history](/docs/workspace#local-file-history), and session-only
(it's not persisted, and it's off in large-file mode and for documents over a
million characters).

Up to **50** checkpoints are kept, within a memory budget, whichever limit is
reached first. A count of 50 whole-document snapshots does not bound memory when
documents differ in size by three orders of magnitude, so an ordinary file
gets all 50 while a big one keeps fewer. The budget
is shared by **all open files together** (64 million characters), and the oldest
checkpoints go first. Closing a tab releases its checkpoints straight away.

Ordinary undo is bounded the same way: a document keeps up to 300 undo steps,
and no more than 64 million characters of undo text.

## The popup (recommended)

`undoHistory.jump` (`M-g v` in the Emacs keymap) opens the active buffer's checkpoints as a
filterable popup, each with a caret-line preview and capture time. Type to
filter, then pick one to jump back to that state. The jump is a single undoable
restore, so you can undo it (`edit.undo`).

## The tool window

The **Undo History** tool window (`M-g u`) shows the same timeline; double-click
or Enter on a checkpoint to jump back. Its side-stripe button is off by default
(the popup is the primary entry point); enable it in **Settings → Tool Windows**
or with `tool.undoHistory`.

## Related

This complements **word/line-level undo**, where one undo removes a word or line
rather than a whole contiguous burst. For longer-term recovery across sessions,
see [local file history](/docs/workspace#local-file-history).
