---
title: "Keyboard macros"
group: "Keyboard & commands"
order: 6
beta: false
summary: "Record a sequence of edits and replay it: in the Emacs keymap, <kbd>F3</kbd> to start, <kbd>F4</kbd> to stop, <kbd>C-x e</kbd> to replay. Name and save macros, and bind them to keys."
description: "Record keyboard macros of commands and typed text, replay them as one undo step, and save named macros that become bindable commands."
alt: "The command palette filtered by \"macro\", listing the macro commands with F3, F4 and C-x e"
---

When the same edit has to happen forty times, do it once and let Editora repeat it. A macro records the commands you run and the text you type, and replays them exactly, auto-indent and auto-close included.

## Record and replay

- **Record**: `F3` starts recording and `F4` stops in the Emacs keymap; `Esc` cancels and keeps the previous macro.
- **Replay**: `C-x e` replays the last macro, and **Macro: Replay Last N Times** repeats it as often as you ask.
- **Everything you do counts**: text typed into the find bar, a prompt or a picker is recorded too, so a search replays as a search.
- **One undo step**: a whole replay undoes at once.

## Keep the good ones

- **Macro: Name and Save Last** keeps a macro across sessions.
- **Each saved macro is a command** of its own, so it appears in the palette and takes a shortcut in **Settings → Keymaps**.
- **Macro: Run Saved** and **Macro: Delete Saved** pick from your saved macros.

In the CUA and Sublime Text keymaps `F3` is Find, so CUA starts and stops recording with `Ctrl+Shift+R` (`Cmd+Shift+R` on macOS) and Sublime Text replays with `Ctrl+Shift+Q`. In every keymap the macro commands are in the palette.

See the [Macros guide](/docs/macros/#record-and-replay).
