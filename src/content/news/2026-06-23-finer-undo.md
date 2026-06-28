---
title: "Finer-grained undo, and an Undo History window"
description: "One Ctrl-Z now undoes a word or line instead of a whole typing burst, and a new Undo History tool window lets you jump back to any recent checkpoint."
date: 2026-06-23
---

Undo got smarter, the first step in a planned undo arc.

**Word- and line-level undo.** A single `C-z` no longer collapses an entire
typing burst into one step. The undo manager starts a new group at word,
whitespace, and newline boundaries, and after a short typing pause, so undo
behaves like VS Code and IntelliJ: one press steps back a word or a line.

**Undo History** (`M-g u` tool window, `M-g v` popup). An in-session timeline of
document checkpoints, captured roughly one per typing burst. The tool window
lists them; the keyboard-driven **popup** (`undoHistory.jump`, `M-g v`) shows the
same checkpoints as a filterable list with a caret-line preview and capture time.
Either way, picking one jumps back as a single undoable restore. It's
finer-grained than the save-based [Local History](/docs/workspace#local-file-history),
session-only, and disabled for very large files. See the
[Undo History guide](/docs/undo-history).

An undo tree and a richer history panel are next.
