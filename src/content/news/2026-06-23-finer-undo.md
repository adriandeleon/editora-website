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

**Undo History tool window** (`M-g u`). An in-session timeline of document
checkpoints, captured roughly one per typing burst. Double-click or press Enter
on any entry to jump back to that state as a single undoable restore. It's
finer-grained than the save-based [Local History](/docs/workspace#local-file-history),
session-only, and disabled for very large files.

An undo tree and a richer history panel are next.
