---
title: "Undo History"
group: "Editing"
order: 7
beta: false
summary: "A timeline of the file as you edit it, one checkpoint per burst of typing. Jump back to any recent state from a filterable popup, without pressing undo forty times."
description: "A timeline of in-session checkpoints, one per typing burst. Jump back to any earlier state of the buffer from a popup or a tool window."
alt: "The Undo History window beside a Python file, listing each edit with its time"
---

Get back to how the file looked a few minutes ago without pressing undo over and over. Editora keeps a checkpoint for each burst of typing, so you can pick the state you want from a list.

- **Pick a checkpoint** with **Undo History: Pick Checkpoint…** (`M-g v` in the Emacs keymap). Each entry shows the caret line and the time, and the list filters as you type.
- **Undo the jump** if you picked the wrong one: restoring a checkpoint is a single undoable step.
- **Keep it docked** in the **Undo History** tool window (`M-g u` in the Emacs keymap), where double-click or Enter jumps back. Its side-stripe button is off until you turn it on in **Settings → Tool Windows**.
- **Up to 50 checkpoints** per file, within a shared memory budget, kept for the session.

Undo History is finer-grained than [Local History](/features/local-file-history/), which keeps saved versions across sessions. It is off for very large files. The other keymaps run both commands from the palette.

See the [Undo History guide](/docs/undo-history/#the-popup-recommended).
