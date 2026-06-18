---
title: "Keyboard macros"
description: "Record a sequence of edits and replay it: F3 to start, F4 to stop, C-x e to replay. Name and save macros, and bind them to keys."
date: 2026-06-21
---

Editora now records and replays **keyboard macros**, Emacs-style. Recording
captures the faithful interleaved stream of invoked commands and literally typed
text, and replay reproduces the exact sequence, so replayed typing runs through
the same auto-close and auto-indent assists as live typing.

- **F3** starts recording, **F4** stops, and **C-x e** replays the last macro.
- The palette adds **Replay Last N Times**, **Name and Save Last**, **Run
  Saved**, and **Delete Saved**.
- Saved macros persist across sessions (in `macros.json`), and each one becomes
  its own palette command, so you can **bind it to a shortcut** in
  Settings → Keymaps like any other command.

The recording hooks are inert when you're not recording, so there's no idle
cost. One limitation: a recorded command that opens a modal picker or prompt
pauses replay for input rather than scripting it. Browse the macro commands on
the [commands page](/commands).
