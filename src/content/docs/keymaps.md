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

None of them is modal: they're just different chord-to-command
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

## Rebinding commands

The **Settings → Keymaps** page lists every command with its current chord and a
filter. For any command:

- **Record** captures a new chord (multi-key sequences like `C-x C-s` are
  supported; Enter saves it and Esc cancels the capture).
- **Reset** restores that command's default.
- **Reset all shortcuts** clears every override.

Changes apply live across all windows. Your overrides are saved in
`settings.json` (the `keymap` name plus per-command entries) and layer on top of
the active keymap, so you only store what you change. Overrides are kept **per
keymap**: switching keymap does not unbind unrelated keys, and switching back
restores the rebinds you made there. Overrides are also stored **per platform**, since a chord is
modifier-specific (⌘ on macOS, Ctrl elsewhere), so a config synced between a Mac
and a Windows or Linux machine binds each OS's own chord instead of both.

## The reference pages

- The [Commands](/commands) page lists every command grouped by area, with its
  description and default chord per keymap.
- The [Keybindings](/keybindings) page shows each keymap's bindings grouped by
  area, with a tab per keymap.

## Limitations

The recorder allows a modifier-less chord, which can shadow plain typing, so use
care there. Modal **Vim** is deferred: the flat chord-to-command resolver can't
express a normal/insert/visual state machine yet.
