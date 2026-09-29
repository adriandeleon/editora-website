---
title: Keyboard macros
description: Record a sequence of edits and replay it, name and save macros, and bind them to keys.
category: Editing
order: 5
---

Editora records and replays **keyboard macros**, Emacs-style. Recording captures
the faithful interleaved stream of invoked commands and literally typed text, and
replay reproduces the exact sequence, so replayed typing runs through the same
auto-close and auto-indent assists as live typing.

## Record and replay

| Action | Command | Default key |
| --- | --- | --- |
| Start recording | `macro.startRecording` | `F3` |
| Stop recording | `macro.stopRecording` | `F4` |
| Replay the last macro | `macro.replayLast` | `C-x e` |
| Replay N times | `macro.replayLastN` | (palette) |

## Saving and reusing

| Action | Command |
| --- | --- |
| Name and save the last macro | `macro.nameAndSave` |
| Run a saved macro | `macro.runSaved` |
| Delete a saved macro | `macro.deleteSaved` |

Saved macros persist across sessions (in `macros.json` in your
[config folder](/docs/configuration)). Each saved macro becomes its own palette
command, so you can **bind it to a shortcut** in
[Settings → Keymaps](/docs/keymaps) like any other command.

## Notes

The recording hooks are inert when you're not recording, so there's no idle
cost. One limitation: a recorded command that opens a modal picker or prompt
pauses replay to wait for input rather than scripting it.
