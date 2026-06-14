---
title: Navigation & search
description: Jump pickers, the file finder, the tab switcher, in-file find, project-wide search, and AceJump.
category: Navigation
order: 1
---

Keyboard-first navigation: fuzzy pickers and search that get you anywhere
without the mouse.

## Jump pickers

Each of these opens an in-scene picker with a footer legend of its keys. Type to
filter; Enter opens.

| Jump to | Command | Default key |
| --- | --- | --- |
| Recent files | `recent.open` | `C-x C-r` |
| Symbols / file structure | `tool.structure` | `M-g i` |
| Open tabs | `buffer.jump` | `C-x b` |
| Tool windows | (picker) | `M-g t` |
| Bookmarks (cross-file) | `bookmarks.jump` | `M-g b` |
| Notes (cross-file) | `notes.jump` | `M-g n` |

The pickers share styling and dirty-file markers, so an unsaved buffer shows the
same dot you see on its tab.

## The file finder

`C-x C-f` opens an Emacs `find-file`-style path finder with prefix
autocomplete. Type and press Tab to complete, Enter to descend into a folder or
open (or create) a file. There's a matching folder finder for picking a
directory.

## The tab switcher

`Ctrl-Tab` opens an IntelliJ-style switcher over the most-recently-used tabs.
Hold and press again to move down the list; release to switch.

## Find in the current file

`C-s` / `C-r` open the Find/Replace bar at the top of the editor. It searches as
you type (incremental), highlights every match, and shows a "{n} of {total}"
count. A repeated `C-s` / `C-r` cycles to the next or previous match, and `C-g`
closes the bar. Case, regex, and whole-word toggles are on the bar. Open replace
with `M-S-5`.

## Find in files

`C-S-f` opens the **Find in Files** tool window and focuses it. It searches the
active project (skipping dot-dirs, oversize, and binary files) plus the
in-memory text of open buffers, so unsaved edits are included. Results are
grouped by file; Enter or double-click jumps to a match. Case, regex, and
whole-word apply here too.

**Replace in files** rewrites matches across the project after a confirmation:
open buffers are edited in-memory (undoable) and closed files are rewritten on
disk, preserving their line endings. The search re-runs afterward to refresh the
panel.

## AceJump

`M-g j` labels every visible occurrence of the next character you type. Type a
label to jump the caret there. **Esc** or `C-g` cancels. It's the fastest way to
move the caret to something you can see.

## Closing tool windows

`M-g` closes a focused tool window (any of them) and returns focus to the
editor.
