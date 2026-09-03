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

The four non-Emacs maps are non-modal: they're just different chord-to-command
maps over the same command ids, so switching changes accelerators without
stranding any feature. Switching is **live, with no restart**, and every chord
hint updates with it: toolbar tooltips, the command palette, tool-window
tooltips, and the Welcome shortcuts.

On macOS the non-Emacs keymaps use ⌘ wherever the
[keybindings reference](/keybindings) shows Ctrl. Emacs uses Control on every
platform.

## Rebinding commands

The **Settings → Keymaps** page lists every command with its current chord and a
filter. For any command:

- **Record** captures a new chord (multi-key sequences like `C-x C-s` are
  supported; Esc cancels the capture).
- **Reset** restores that command's default.
- **Reset all** clears every override.

Changes apply live across all windows. Your overrides are saved in
`settings.toml` (the `keymap` name plus per-command entries) and layer on top of
the active keymap, so you only store what you change. Switching keymaps keeps
your overrides on top. Overrides are stored **per platform**, since a chord is
modifier-specific (⌘ on macOS, Ctrl elsewhere), so a config synced between a Mac
and a Windows or Linux machine binds each OS's own chord instead of both.

## Reading and writing the reference

- The [Commands](/commands) page lists every command grouped by area, with its
  description and default chord per keymap.
- The [Keybindings](/keybindings) page shows each keymap's bindings grouped by
  area, with a tab per keymap.

## Notes

The recorder allows a modifier-less chord, which can shadow plain typing, so use
care there. Modal **Vim** is deferred: the flat chord-to-command resolver can't
express a normal/insert/visual state machine yet.
