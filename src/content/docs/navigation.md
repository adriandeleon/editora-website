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
closes the bar. Case, regex, and whole-word toggles are on the bar, along with
**`AB`** (preserve case) and **`Sel`** (find in selection); a clear (`✕`) button
empties a field in one click.

**Preserve case** recases each replacement after the text it replaces, so
replacing `foo` with `bar` rewrites `FOO` as `BAR` and `Foo` as `Bar` instead of
flattening everything to lowercase. `snake_case` and `kebab-case` are cased per
segment. **Find in selection** is picked up automatically when you open the bar
with a *multi-line* selection, scoping both search and Replace All to it and
following the region as you edit; a single-line selection seeds the query
instead, as before.

Replace understands **regex capture groups**: search `(\w+)_(\w+)` and replace
with `$2-$1`. A replacement naming a group the pattern doesn't have reports the
error and leaves the buffer untouched. In literal (non-regex) mode `$` is
inserted verbatim. **Alt+Enter** puts a cursor on every match at once (see
[Multiple cursors](/docs/editing#multiple-cursors)).

Open replace with `M-S-5`. In the **Emacs keymap** that chord runs
[query-replace](/docs/editing#the-emacs-editing-model) instead, the
replace-with-confirmation loop; the find bar's own replace is still on the bar
and in the palette.

## Occur

**Edit: Occur…** (`M-s o`) lists every line in the current buffer matching a
regular expression in a keyboard picker, and picking one jumps to it. It is the
buffer-scoped counterpart to Find in Files, and the picker's own filter narrows
the list further as you type.

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

There's also a lightweight **Find in Files popup** (`search.inFilesPopup`) if you
prefer a quick, keyboard-first search without opening the tool window.

## Folding

The gutter carries a fold chevron beside every foldable region. Beyond clicking
it:

| Action | Command | Default key |
| --- | --- | --- |
| Fold / unfold at the caret | `view.fold` | `C-c C-f` |
| Fold all / unfold all | `view.foldAll` | `C-c f` |
| Fold by nesting level (1 to 7) | `view.foldLevel1` … `7` | (palette) |
| Fold / unfold recursively | `view.foldRecursively` / `view.unfoldRecursively` | (palette) |
| Go to parent / next / previous fold | `view.gotoParentFold` … | (palette) |

**Fold Level *n*** collapses everything at that nesting depth, like VS Code's
`Ctrl+K Ctrl+1..7`. **Fold Recursively** collapses the region at the caret *and*
everything nested inside it, and unfolding recursively reveals the whole subtree.
The three **Go to … Fold** commands move the caret to a fold header, revealing it
first if it is hidden. None of these has a default key; bind them from
Settings → Keymap.

Where a [language server](/docs/lsp#folding-and-selection) is running, the fold
regions come from the server rather than from brace and indent scanning, so
import blocks and javadoc fold as single regions. Collapsed regions are saved per
file and restored with your session.

## Back and forward

Jump around and step through your navigation history with **Go: Back**
(`nav.back`) and **Go: Forward** (`nav.forward`), like a browser's back/forward
for the caret.

## AceJump

`M-g j` labels every visible occurrence of the next character you type. Type a
label to jump the caret there. **Esc** or `C-g` cancels. It's the fastest way to
move the caret to something you can see. **Line mode** (`nav.aceJumpLine`,
`M-g L`) labels every visible line so you can jump straight to one.

## Closing tool windows

`M-g` closes a focused tool window (any of them) and returns focus to the
editor.
