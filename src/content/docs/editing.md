---
title: Editing
description: Multiple cursors, auto-indent, bracket matching, comments, and the small text operations.
category: Editing
order: 1
---

The text surface is a RichTextFX `CodeArea` with line numbers, a fold gutter, a
minimap, and a current-line highlight. Most of the editing help is automatic;
the rest is a command away. See the full list on the [Commands](/commands) page.

## Multiple cursors

Edit many places at once, VS Code style.

- **Add a caret at the next occurrence** of the selection, or **above** / **below**
  the current line, from the palette (`edit.addCaret…`).
- **Alt-drag** to make a column or box selection.
- Type, and the edit fans out to every caret. **Esc** collapses back to one.

Movement chords act on the primary caret and don't fan out, so use the arrow
keys to move several carets together. Multiple cursors can be turned off in
Settings, and they're disabled in Simple UI mode.

## Auto-indent

**Enter** does the right thing per language: it keeps the current line's indent,
adds a level after a block opener (braces, a trailing `:` in Python or YAML,
`do` / `then` in shell, an open tag in XML or HTML), and splits a matching pair
into an indented stanza with the closer dropped below. Typing a closer such as
`)`, `]`, `}`, or a keyword like `end` / `fi` / `done` re-aligns the line to its
opener.

**Smart backspace** removes a whole indent level in one press. On a blank,
auto-indented line, a single Backspace jumps back to the end of the previous
line. The indent unit (tabs or spaces) is inferred from the file.

## Brackets and quotes

Typing `(`, `[`, `{`, `"`, `'`, or `` ` `` inserts the matching closer and keeps
the caret between them. Type the closer when it's already next to the caret and
Editora types over it. Type an opener with a selection and it wraps the
selection. Backspace inside an empty pair deletes both halves. Quotes aren't
auto-paired next to a word character, so the apostrophe in `don't` is left
alone. Whenever the caret sits next to a bracket, it and its match are
highlighted.

## Comments

`M-;` (Emacs comment-dwim) comments or uncomments using the language's own
syntax. A single line toggles a line comment; a multi-line selection toggles a
block or region comment. It covers `//` and `/* */`, `#`, `<!-- -->`, `--`, and
the rest, preserving indentation, and toggles back off when the text is already
commented.

## Small operations

| Operation | Command | Default key |
| --- | --- | --- |
| Transpose characters | `edit.transposeChars` | `C-t` |
| Transpose words | `edit.transposeWords` | `M-t` |
| Transpose lines | `edit.transposeLines` | `C-x C-t` |
| Duplicate line | `edit.duplicateLine` | (keymap-specific) |
| Move line up / down | `edit.moveLineUp` / `Down` | (keymap-specific) |
| Select all | `edit.selectAll` | (keymap-specific) |
| Fill paragraph | `edit.fillParagraph` | `M-q` |
| Set fill column | `edit.setFillColumn` | `C-x f` |

Fill re-wraps a paragraph to the fill column (70 by default), repeating any
comment or quote prefix on each wrapped line.

## Read-only and View mode

Toggle a buffer read-only with `C-x C-q` so it can't be edited by accident.
Typing and edit commands are blocked, while highlighting, folding, search, and
copy keep working. A file that isn't writable on disk opens read-only
automatically. A Word-style **View Mode banner** docks above the editor with an
*Enable Editing* button when the file is writable, and while read-only,
**Space** pages down and **Backspace** pages up like a pager.

## Whitespace and the column ruler

View options in Settings toggle whitespace markers, a configurable column ruler,
the minimap, and line numbers. These render as lightweight overlays drawn only
for the visible lines, so they stay cheap on large files.
