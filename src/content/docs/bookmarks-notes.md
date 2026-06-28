---
title: Bookmarks & notes
description: Line bookmarks with cross-file jump, and Personal Notes attached to a word, line, or range.
category: Workspace
order: 2
---

Two ways to mark up code. Both are scoped per project and stored outside your
files.

## Bookmarks

Toggle a bookmark on a line with `C-c m`. It shows a gutter marker and can carry
a short note.

| Action | Command | Default key |
| --- | --- | --- |
| Toggle bookmark | `bookmarks.toggle` | `C-c m` |
| Next / previous in file | `bookmarks.next` / `previous` | `C-c ]` / `C-c [` |
| Jump (cross-file) | `bookmarks.jump` | `M-g b` |

The **Bookmarks** tool window (`M-2`) lists them across files in an order you
control: reorder a bookmark or a whole file group with Alt+Up/Down, a right-click
Move, or drag and drop. Bookmarks follow their content through edits, and they're
re-anchored to the saved line text when a file changes outside the editor, so
they survive external edits. They live in `bookmarks.json`.

## Personal notes

Personal Notes attach an annotation to a **word, line, or range**, stored
*outside* the file. They're built for read-only, generated, or shared code where
you can't leave a comment in the source. Notes are **off by default**; enable
them in **Settings → Application**.

| Action | Command | Default key |
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

Notes track their anchor through edits and re-locate themselves by surrounding
text when a file changes externally, marking themselves orphaned only if the
anchor truly disappears. The **Notes** tool window (`M-5`) groups them per file
with a filter. They live in `notes.json`. A second toggle, *Show note
indicators*, hides just the gutter glyph and highlight while keeping notes on.
