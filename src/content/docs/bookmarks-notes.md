---
title: Bookmarks & notes
description: Bookmarks and Personal Notes attached to code or project folders, with cross-file navigation.
category: Workspace
order: 2
---

Two ways to mark up code. Both are scoped per project and stored outside your
files.

## Bookmarks

Toggle a bookmark on a line with `C-c m`, or from the editor's right-click menu
(**Add / Remove Bookmark**, acting on the line you clicked). It shows a gutter
marker and can carry a short note. The marker itself is display-only. Clicking
the gutter does not toggle a bookmark, because the breakpoint strip and the Run ▶
are narrow targets inside that same gutter and a slightly-off click would add a
stray bookmark instead of hitting what you aimed at.

The Project Tree and Map menus can add a bookmark to a file's first line without
opening it first, or attach a bookmark directly to a folder. Both surfaces show
a compact bookmark indicator after the file or folder name.

| Action | Command | Emacs key |
| --- | --- | --- |
| Toggle bookmark | `bookmarks.toggle` | `C-c m` |
| Next / previous in file | `bookmarks.next` / `previous` | `C-c ]` / `C-c [` |
| Jump (cross-file) | `bookmarks.jump` | `M-g b` |
| Set a mnemonic | `bookmarks.setMnemonic` | (palette) |

### Mnemonics

A bookmark can carry a **single character**, and that character becomes a
shortcut that jumps to it from anywhere in the project. Set one with
`bookmarks.setMnemonic` on the bookmarked line; an empty answer clears it.

A mnemonic is **unique within a project**, so `3` means one place in the
project and not one place per file. That way its chord always jumps to a single
location. Assigning one that's already taken moves it, with no error message.

Each of the ten digits gets its own command, `bookmarks.gotoMnemonic0` through
`bookmarks.gotoMnemonic9`, so you can bind each to a single chord in
**Settings → Keymaps** and jump with one keystroke and no prompt.
Letters work as a mnemonic too and are reachable from the bookmarks picker; only
the digits have a bindable command of their own.

The panel shows the mnemonic **first on the row**, because it's the only part
of a bookmark that is otherwise invisible.

### The Bookmarks window

The **Bookmarks** tool window (`M-2`) lists them across files in an order you
control: reorder a bookmark or a whole file group with Alt+Up/Down, a right-click
Move, or drag and drop. It groups by project, always showing the **General**
(no-project) group plus the **current project**, with a **Show all projects**
toggle to reveal and jump to every other project's bookmarks. Bookmarks follow
their content through edits, and they're re-anchored to the saved line text when
a file changes outside the editor, so they survive external edits. They live in
`bookmarks.json`.

## Personal notes

Personal Notes attach an annotation to a **word, line, range, or project
folder**, stored *outside* the file. They're built for read-only, generated, or
shared code, and for project context that does not belong in a source file.
Notes are **on by default**; toggle them in **Settings → Workspace → Features**
(*Enable Personal Notes*).

| Action | Command | Emacs key |
| --- | --- | --- |
| Add a note | `notes.add` | `C-c n` |
| Edit the note at the caret | `notes.editNote` | `C-c e` |
| Resolve / reopen the note | `notes.toggleResolved` | (palette) |
| Next / previous | `notes.next` / `previous` | (palette) |
| Jump (cross-file) | `notes.jump` | `M-g n` |
| Full-text search | `notes.search` | `M-g s` |
| Delete note on the line | `notes.delete` | (palette) |
| Export notes to JSON | `notes.export` | (palette) |

Add one from the editor right-click menu (*Add Personal Note* / *Add Note to
Selection*) or `C-c n`. A noted span gets a soft highlight and an amber start
marker you can **click to edit**, and hovering it shows the note body rendered as
Markdown. Note bodies are edited in a multi-line dialog (Ctrl/Cmd+Enter saves)
that honors your keymap's caret-movement keys.

You can also add a first-line note to a file, or a note directly to a folder,
from the Project Tree or Map menu. An indicator after the name makes noted paths
visible while navigating. Folder notes appear as tooltips in the Project tree;
activating a folder entry in the Notes window opens the Project explorer with
that folder selected.

Notes track their anchor through edits and re-locate themselves by surrounding
text when a file changes externally, marking themselves orphaned only if the
anchor disappears. The **Notes** tool window (`M-5`) groups them per file
with a filter, and like Bookmarks it groups by project, General plus the current
one, with a **Show all projects** toggle. File and folder rows use matching
icons so their targets remain clear. They live in `notes.json`. A second
toggle, *Show note indicators* (**Settings → Editor → Display**), hides just the
gutter glyph and highlight while keeping notes on.

Source references in the Notes window use the same compact, subdued line-number
style as Structure and Bookmarks, keeping the note text visually primary.
