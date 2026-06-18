---
title: "Keyboard macros"
group: "Keyboard & commands"
order: 5
beta: false
summary: "Record a sequence of edits and replay it: <kbd>F3</kbd> to start, <kbd>F4</kbd> to stop, <kbd>C-x e</kbd> to replay. Name and save macros, and bind them to keys."
---

Record a sequence of editor actions and replay it, Emacs-style. Recording captures the faithful interleaved stream of invoked commands and literally typed text, and replay reproduces the exact sequence, so replayed typing runs through the same auto-close and auto-indent assists as live typing.

- **F3** starts recording, **F4** stops, and **C-x e** replays the last macro.
- The palette adds **Replay Last N Times**, **Name and Save Last**, **Run Saved**, and **Delete Saved**.
- Saved macros persist across sessions, and each becomes its own palette command, so you can bind it to a shortcut in Settings → Keymaps like any other command.

The recording hooks are inert when you're not recording, so there's no idle cost. See the [macros guide](/docs/macros).
