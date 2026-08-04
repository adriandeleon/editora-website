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
- **Select all occurrences** (`edit.selectAllOccurrences`, `Ctrl+Shift+L` in the
  VS Code and Sublime keymaps) turns every occurrence of the selection, or of the
  word under the caret, into a cursor at once. Matching is literal and
  case-sensitive.
- From the Find bar, **Alt+Enter** (`find.selectAllMatches`) does the same for
  every match of the current query and closes the bar, so the query's case,
  regex and whole-word toggles decide what gets a cursor.
- **Alt-drag** to make a column or box selection.
- Type, and the edit fans out to every caret. **Esc** collapses back to one.

Movement chords fan out too: `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and
`M-b` move every caret, like the arrow keys. Document, paragraph, sentence and
page motions stay on the primary caret. Multiple cursors can be turned off in
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

**Go: Matching Bracket** (`nav.matchingBracket`, `Ctrl+Shift+\` in the VS Code
keymap) jumps the caret to the bracket paired with the one beside it, and press
it again to jump back. **Edit: Select to Bracket** selects everything between a
pair, both brackets included. Matching is text-based, like the highlight, so a
bracket inside a string or comment can pair with the wrong one.

## Expand and shrink selection

**Edit: Expand Selection** grows the selection outward through syntactic levels:
word, then the enclosing brackets or string, the line, the enclosing definition,
the paragraph, the whole document. **Edit: Shrink Selection** retraces the exact
same steps back. `Shift+Alt+Right` / `Left` in the VS Code, CUA and Sublime
keymaps; `Ctrl+W` / `Ctrl+Shift+W` in the IntelliJ keymap (`Option+Up` / `Down`
on macOS); palette-discoverable everywhere.

Where a [language server](/docs/lsp#folding-and-selection) is running, the ladder
comes from the server's own parse, so it respects strings and comments. Without
one it falls back to text-based bracket and string matching, which is the
always-available baseline.

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
| Forward / backward subword | `nav.subwordForward` / `Backward` | (palette) |
| Delete subword forward / backward | `edit.deleteSubword…` | (palette) |
| Convert indentation to spaces / tabs | `edit.indentationTo…` | (palette) |
| Tabify / untabify region | `edit.tabify` / `edit.untabify` | (palette) |
| Align regexp | `edit.alignRegexp` | (palette) |
| Copy with syntax highlighting | `edit.copyWithHighlighting` | (palette) |

Fill re-wraps a paragraph to the fill column (70 by default), repeating any
comment or quote prefix on each wrapped line. **Auto Fill mode**
(`view.toggleAutoFill`, off by default) does it as you type, breaking the line
when it passes the fill column. It is prose-only, so it never wraps code, and
each wrapped line keeps the paragraph's indent.

**Subword navigation** moves and deletes by camelCase and snake_case parts:
stepping through `getUserName` lands on `get`, `User`, `Name`, and acronyms split
correctly (`HTMLParser` gives `HTML` and `Parser`). These have no default key, so
bind them from Settings → Keymap if you want VS Code's `Ctrl+Alt+←/→`.

**Convert indentation** rewrites the whole file's *leading* whitespace between
tabs and spaces. Alignment inside a line and string contents are left alone, and
each direction reverses the other. **Tabify / untabify** work the other way
round, converting whitespace runs anywhere in the selection (or the buffer) using
the tab width. **Align regexp** pads a selection's lines with spaces so a regular
expression lines up in a column, which is how you align a block of `=`
assignments; it only adds spaces, so re-running it is a no-op.

**Copy with syntax highlighting** puts a colored HTML flavor on the clipboard
alongside the plain text, so code pasted into Slack, an email or a document keeps
its highlighting. It is on by default for ordinary copies (Settings → Editor, or
`view.toggleCopyWithHighlighting`) and uses a light GitHub-style palette, since
pasted code usually lands on a light background. The explicit command ignores
both the setting and the size cap. Plain text is always on the clipboard too, so
pasting into a plain-text field is unchanged.

## Abbreviations

A text-replacement dictionary: define a short abbreviation that expands to longer
text. Expand the word before the caret on demand with `C-x a e`, or turn on
**Abbrev Mode** (`view.toggleAbbrevMode`) to expand automatically when you type a
space or punctuation. Define a new one with `C-x a g`, and manage the whole
dictionary in **Settings → Editor → Abbreviations** (`abbrev.manage`).

Your typed case is carried onto the expansion, so `btw` gives *by the way*, `Btw`
gives *By the way*, and `BTW` gives *BY THE WAY*; a lowercase abbreviation keeps
any capitals inside its expansion.

## The Emacs editing model

Beyond the keymap, Editora implements the concepts an Emacs user reaches for.
See [Emacs heritage](/features/emacs-heritage) for the full tour.

| Feature | Keys |
| --- | --- |
| Kill ring, yank, yank-pop | `C-k` / `M-d` / `M-DEL` …, `C-y`, `M-y` |
| Mark ring, pop mark | `C-SPC`, `C-x C-SPC` |
| Narrowing / widen | `C-x n n` / `d` / `f`, `C-x n w` |
| Rectangles | `C-x r k` / `M-w` / `y` / `d` / `c` / `o` / `t` / `N` |
| Query-replace, regexp | `M-%`, `C-M-%` |
| Occur | `M-s o` |
| Universal argument | `C-u` |

The **kill ring** holds the last 120 kills, and consecutive kills accumulate into
one entry, so `C-k C-k C-k` then `C-y` restores all three lines. Kills are still
written to the system clipboard, and text copied in another application wins over
a stale ring entry, so `C-y` never surprises you. *Edit: Yank from Kill Ring…*
picks an older entry directly. The ring is per window and lives for the session.

The **mark ring** is per buffer. `C-SPC` records the spot, `C-x C-SPC` pops back
through older marks and around to where you started, and marks follow their text
as you edit above them.

**Narrowing** is real, not a display filter: search, replace, macros and Select
All see only the region, which is the point of it. An amber **Narrowed** badge in
the status bar shows the state and widens on click. Saving always writes the
whole file. Language-server support is suspended while narrowed, bookmarks, notes
and breakpoints stop persisting until you widen, and undo history is dropped at
the narrowing boundary (edits made while narrowed undo normally).

**Rectangles** act on the columns between point and mark, and each command is one
undo step. They are character columns, so a tab counts as one, and the rectangle
is remembered separately from the kill ring, as in Emacs.

**Query-replace** stops on each match and waits: `y` or Space replaces, `n` or
Backspace skips, `!` replaces all the rest, `.` replaces this one and stops, and
`q`, Enter or Escape quits. Regexp mode expands `$1` group references. In the
Emacs keymap `M-%` runs query-replace rather than opening the find bar's replace
mode; the other keymaps are unchanged.

The **`C-u` prefix argument** gives the next command a count: `C-u 5 C-n` moves
down five lines, `C-u 3 C-k` kills three, `C-u 40 -` inserts forty dashes. A bare
`C-u` is 4 and repeating it multiplies by four, digits after it are that number,
and `C-g` cancels a half-typed argument. `C-u C-SPC` is special-cased to
pop-mark. Only the Emacs keymap binds `C-u`.

## String manipulation

Convert the **case style** of the selection or the identifier at the caret
(camelCase, PascalCase, snake_case, SCREAMING_SNAKE_CASE, kebab-case, dot.case),
with a *Cycle Case Style* that steps a token through the styles on repeated
presses (`edit.case.cycle`) and a *Swap Case* (`edit.case.swap`). Whole-line
transforms work on the selection or the whole file: sort ascending/descending
(numeric-aware, case-insensitive), sort by length, reverse, shuffle, remove
duplicate or empty lines, and trim trailing whitespace. Each is a palette
command, or reach them all from one filterable picker, *Edit: String
Manipulation…* (`C-c x`).

## Hex viewer

Opening a **binary** file (an executable, archive, `.class`, `.pdf`, and so on)
shows a read-only `offset | hex | ASCII` dump instead of garbage text. Binaries
are detected by content, and large files show their first slice with a
truncation note. *View: Open as Hex* (`view.openAsHex`) force-opens any file's
bytes as hex, and *View: Open as Text* switches back.

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
