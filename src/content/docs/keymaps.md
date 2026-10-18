---
title: Keymaps & keybindings
description: Switch between five built-in keymaps and rebind any command.
category: Customization
order: 2
---

Editora is keyboard-first, and every action is a command, so the keyboard layer
is entirely yours to change.

## The five keymaps

Editora ships five complete keymaps, selectable in **Settings → Keymaps** or with
`keymap.select`:

- **Emacs** (default)
- **CUA**
- **Sublime Text**
- **VS Code**
- **IntelliJ IDEA**

None of them is modal: they're different chord-to-command
maps over the same command ids, so switching changes accelerators without
stranding any feature. Switching is **live, with no restart**, and every chord
hint updates with it: toolbar tooltips, the command palette, tool-window
tooltips, and the Welcome shortcuts. Hints are written in the notation of the
active keymap and platform: `Ctrl+Shift+P`, `⇧⌘P`, or Emacs notation in the
Emacs keymap.

Undo and toggle-comment also have chords that can be typed on keyboards where
`/` is a shifted key: `C-x u`, `C-_`, and Ctrl+Shift+7 undo in the Emacs keymap,
and Ctrl+Shift+7 or the numpad slash toggle a comment in the other keymaps.

On macOS the non-Emacs keymaps mostly use ⌘ where the
[keybindings reference](/keybindings) shows Ctrl. A few chords differ beyond the
modifier, where macOS reserves the obvious one: Replace is ⌥⌘F rather than
Ctrl+H in the CUA, Sublime Text and VS Code keymaps, for example. The command
palette always shows the chord that applies on your machine. Emacs uses Control
on every platform.

Emacs notation reads `C-` as Ctrl, `M-` as Alt (Option on macOS) and `S-` as
Shift, and a space separates the steps of a sequence: `C-x C-s` is Ctrl+X, then
Ctrl+S.

## Debugger keys

Every keymap binds the debugger's commands. The Emacs keymap's are listed under
[Debugging](/docs/run-debug#debugging); besides those, `C-c C-d` followed by
`r` restarts, `v` shows the value at the caret, `x` evaluates, `w` adds a
watch, `l` lists the breakpoints, `m` mutes them, `t` enables or disables the
caret line's breakpoint, `s` smart-steps into, `f` restarts the frame and `h`
applies code changes.

The CUA and Sublime Text keymaps use VS Code's function keys:

| Key | Action |
| --- | --- |
| F5 | Start or continue |
| Shift+F5 | Stop |
| Ctrl+Shift+F5 | Restart |
| F6 | Pause (CUA only: in Sublime Text, F6 is spell checking) |
| F9 | Toggle breakpoint |
| F10 | Step over |
| F11 | Step into while the program is paused, otherwise Full Screen |
| Shift+F11 | Step out |

Because F11 steps into only while a program is paused, Full Screen also has
Ctrl+Alt+F in these two keymaps (Ctrl+⌘F on macOS). The VS Code keymap has the
same keys, with F11 always Step Into.

The IntelliJ IDEA keymap adds Ctrl+F5 Restart, Alt+F8 Evaluate, Ctrl+Alt+F8
Show Value at Caret, Ctrl+Shift+F9 Breakpoints, Shift+F7 Smart Step Into and
Ctrl+F9 Apply Code Changes to its usual debugger keys.

Some debugger keys are the same in every keymap. While the program is paused,
Ctrl+Space completes an expression in the evaluate field, a watch prompt and a
breakpoint's condition and log message, and typing `.` opens the same list.
In the value popup of Show Value at Caret, the arrow keys move and unfold,
Enter unfolds a row or loads the next page, Shift+Enter adds the row to
Watches, Ctrl+C (⌘C on macOS) copies its value and Esc closes the popup. See
[Inspecting values](/docs/run-debug#inspecting-values).

## Rebinding commands

The **Settings → Keymaps** page lists every command with its current chord.
Click a row, or move to it with the arrow keys and press Enter, to show its
buttons:

- **Record** captures a new chord. Multi-key sequences like `C-x C-s` work.
  The field shows the chord in the active keymap's notation (`Ctrl+Shift+K`,
  `⇧⌘K`, or `C-S-k` in the Emacs keymap). Enter saves it and Esc cancels.
- **Reset** restores that command's default chord. If another command has taken
  that chord, Editora asks first, because the other command loses it.
- **Remove** unbinds the command. Delete or Backspace on a row does the same.
- **Reset all shortcuts** clears every override of the keymap in use. Overrides
  made under another keymap stay with that keymap.

A chord cannot start with a key that types a character, such as a letter, a
digit, punctuation or Space with no modifier other than Shift. Bound, that key
would run the command everywhere you type, so Editora refuses it and asks you to
add Ctrl, Alt or another modifier. When a new chord collides with existing
bindings, Editora lists them and asks before binding it.

The **Filter commands…** field matches every word you type, in any order, so
`save file` finds **File: Save**. You can also type a chord in any notation:
`Ctrl+S`, `C-s`, `⇧⌘S` or a sequence like `Ctrl+K Ctrl+W` lists the commands on
that chord, written the way the active keymap writes it.

Changes apply live across all windows. Your overrides are saved in
`settings.json` (the `keymap` name plus per-command entries) and layer on top of
the active keymap, so you only store what you change. Overrides are kept **per
keymap**: switching keymap does not unbind unrelated keys, and switching back
restores the rebinds you made there. Shortcuts of commands that no keymap binds
by default (saved macros, external tools and run configurations) follow you to
the new keymap instead. When the new keymap already uses that chord, or the
chord would clash with one of its chords, the shortcut stays with the keymap it
was made in and comes back when you switch back. Overrides are also stored **per platform**, since a chord is
modifier-specific (⌘ on macOS, Ctrl elsewhere), so a config synced between a Mac
and a Windows or Linux machine binds each OS's own chord instead of both.

## The reference pages

- The [Commands](/commands) page lists every command grouped by area, with its
  description and default chord per keymap.
- The [Keybindings](/keybindings) page shows each keymap's bindings grouped by
  area, with a tab per keymap.

## Limitations

Modal **Vim** is deferred: the flat chord-to-command resolver can't
express a normal/insert/visual state machine yet.
