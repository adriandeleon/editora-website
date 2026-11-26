---
title: "Keyboard macros"
group: "Keyboard & commands"
order: 6
beta: false
summary: "Record a sequence of edits and replay it: in the Emacs keymap, <kbd>F3</kbd> to start, <kbd>F4</kbd> to stop, <kbd>C-x e</kbd> to replay. Name and save macros, and bind them to keys."
description: "Record keyboard macros of commands and typed text, replay them as one undo step, and save named macros that become bindable commands."
alt: "The command palette filtered by \"macro\", listing the macro commands with F3, F4 and C-x e"
---

Record a sequence of editor actions and replay it, Emacs-style. Recording captures the interleaved stream of invoked commands and literally typed text, and replay reproduces the exact sequence, so replayed typing runs through the same auto-close and auto-indent assists as live typing.

- In the Emacs keymap, **F3** starts recording, **F4** stops, and **C-x e** replays the last macro. In the CUA and Sublime Text keymaps F3 is Find: there, CUA's `Ctrl+Shift+R` starts and stops recording, Sublime Text's `Ctrl+Shift+Q` replays, and every macro command is in the palette. **Esc** cancels a recording and keeps the previous macro.
- Text and keys typed into the find bar, a prompt or a picker are recorded too, so a search replays as a search.
- One replay is one undo step.
- The palette adds **Replay Last N Times**, **Name and Save Last**, **Run Saved**, and **Delete Saved**.
- Saved macros persist across sessions, and each becomes its own palette command, so you can bind it to a shortcut in Settings → Keymaps like any other command.

The recording hooks are inert when you're not recording, so there's no idle cost. See the [macros guide](/docs/macros).
