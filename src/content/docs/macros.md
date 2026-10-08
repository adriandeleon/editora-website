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

| Action | Command | Emacs key |
| --- | --- | --- |
| Start recording | `macro.startRecording` | `F3` |
| Stop recording | `macro.stopRecording` | `F4` |
| Start or stop recording | `macro.toggleRecording` | (palette) |
| Cancel the recording | `macro.cancelRecording` | (palette) |
| Replay the last macro | `macro.replayLast` | `C-x e` |
| Replay N times | `macro.replayLastN` | (palette) |

The other keymaps bind a smaller set by default: **Ctrl+Shift+R** starts or
stops a recording in the CUA keymap, and **Ctrl+Shift+Q** replays the last macro
in the Sublime Text keymap. Every macro command is in the **Tools → Macros**
submenu and the command palette, and can be given a key in
[Settings → Keymaps](/docs/keymaps).

While you record, the status bar shows **● REC**; click it to stop. **Esc** (or
`C-g` in the Emacs keymap) with the focus in the document cancels the recording
and keeps the previous macro. A recording with nothing in it also leaves the
previous macro in place.

### What is recorded

- Commands you invoke, by key, menu or palette.
- Text you type, including typing at several carets and text from an input
  method.
- Keys that act rather than type: Enter, Tab, Shift+Tab, Backspace, the arrows,
  Escape. They replay through the same path as the key, so a replayed Tab
  expands a snippet or indents exactly as it does live.
- **Prompts.** Text and keys typed into the find bar, an overlay prompt, or a
  picker are recorded and replayed into that prompt. Recording `C-s`, `foo`,
  Enter, Esc replays as a search for *foo*. Esc inside a prompt closes the
  prompt and is recorded; it does not cancel the recording.
- A numeric prefix: `C-u 5` followed by a key is recorded as typed.

Mouse clicks are not recorded, so move the caret with the keyboard. A command
that opens a blocking dialog cannot be scripted: what you do in the dialog is
not recorded, and a replay waits at that step for you. Both cases show a hint in
the status bar while you record.

### Replaying

One replay is **one undo step**, and so is a replay with a count: a single Undo
takes back everything it did. Give a count with **Macro: Replay Last N Times**
or with a prefix argument (`C-u 50 C-x e`); the count is capped at 10,000.

A long replay runs in slices so the window stays responsive. The status bar
shows the progress (*Replaying macro… 120 of 500*), and **Esc** stops it. When
steps could not run, the status bar says why: typing is skipped in a read-only
buffer, and a step whose command does not exist is skipped and named.

## Saving and reusing

| Action | Command |
| --- | --- |
| Name and save the last macro | `macro.nameAndSave` |
| Run a saved macro | `macro.runSaved` |
| Delete a saved macro | `macro.deleteSaved` |

Saved macros persist across sessions (in `macros.json` in your
[config folder](/docs/configuration)). Each saved macro becomes its own palette
command, listed as **Macro: ‹name›**, so you can **bind it to a shortcut** in
[Settings → Keymaps](/docs/keymaps) like any other command. Saving under a name
that is already taken asks before replacing it, and deleting a saved macro asks
first and removes its key binding.

**Settings → Macros** lists your saved macros, where you can rename one, edit
its steps, delete it, or assign its keybinding. Steps are edited by kind: a
command step with a command picker, a text step as multi-line text, and a key
step by pressing the key. A step can be marked as typed into a prompt rather
than the document.

A macro can run another saved macro as one of its steps. A macro that would run
itself, directly or through another, stops with a message.

`macros.json` is a versioned file (version 2). Each macro is stored with an id
that its key binding refers to, so renaming a macro keeps its shortcut. A file
written by an earlier Editora is migrated when it is first read, and existing
key bindings are kept.

## Notes

The recording hooks are inert when you're not recording, so there's no idle
cost. Macros and their key bindings are not part of
[settings sync](/docs/settings-sync).
