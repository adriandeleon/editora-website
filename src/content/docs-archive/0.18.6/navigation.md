---
title: Navigation & search
description: Jump pickers, the file finder, the tab switcher, in-file find, project-wide search, and AceJump.
category: Navigation
order: 1
---

Keyboard-first navigation: fuzzy pickers and search that get you anywhere
without the mouse.

For moving around *code* in particular — symbols, definitions, the scope you're
in — see [Code navigation](/docs/code-navigation).

## Search Everywhere

**Search Everywhere** (`search.everywhere`) is one picker over **commands,
project files and symbols**, so you can type the *name* of the thing instead of
first deciding which finder it lives in. Editora had five pickers behind five
chords; this one asks for the name.

A leading sigil restricts it to a single source when you already know:

| Prefix | Searches |
| --- | --- |
| (none) | commands, files and symbols together |
| `>` | commands only |
| `#` | files only |
| `@` | symbols only |

Those are VS Code's sigils rather than invented ones, since the muscle memory
already exists.

Results stay **grouped by source** rather than interleaved on raw score. The
sources differ in size by orders of magnitude — tens of thousands of symbols,
thousands of files, a few hundred commands — so a flat merge hands the whole
list to whichever is biggest and the other two effectively disappear. Each
source gets a guaranteed share (8 rows, 24 overall), the groups compete on their
*best* result rather than their bulk, and the overall cap trims a group's tail
instead of dropping a source outright.

**A single-source query is not capped.** When a sigil restricts the picker to
one source — or when the query is empty, which lists commands — there is nothing
for a large source to drown out, so you get the whole list. Before 0.14.0 a `>`
search returned at most eight commands, which made the scoped mode strictly
worse than the picker it stands in for.

An **empty query lists every command** and touches no other corpus, so opening
it is a browsable list rather than a blank box. A bare sigil is a *scope* rather
than an empty query: it names what it will search and walks nothing.

A single-line selection seeds the query, the same way the find bar and Find in
Files do.

The symbol half is backed by Editora's own
[project symbol index](/docs/code-navigation#go-to-symbol-in-project), so it
works with no language server installed.

### Commands you can't run yet

A command whose feature is switched off is **listed, greyed, with an explanation
naming the setting that would enable it** — the same way the
[command palette](/docs/keymaps) treats it. Omitting it is tidier in a short
mixed list, which is how this shipped in 0.13.0, but it also means you never
learn the command exists. Greyed rows sort after everything you can actually
run, and both the cursor and the mouse step over them.

The highlighted row's **description** appears under the list, and **`C-h`**
opens that command's documentation in your browser — both matching the palette
exactly. The hint line along the bottom names them, alongside `C-n`/`C-p`.

### Opening it

| Keymap | Chord |
| --- | --- |
| Emacs | `M-S-x` |
| CUA, Sublime, VS Code, IntelliJ | `Ctrl`/`Cmd`+`Shift`+`E` |

`M-S-x` sits beside `M-x`, where the mnemonic explains itself. (Through 0.13.0
the command had no chord in any bundled keymap, so the only way to reach it was
the palette.)

It can also **take over the command palette's shortcut**: switch on *Palette
shortcut opens Search Everywhere* under **Settings → Interface → Pickers**, or
run `view.togglePaletteSearchEverywhere`. It is off by default — which picker a
chord opens is muscle memory, so this is something you turn on rather than
something a release does to you. Nothing is lost by switching: an empty query
lists every command exactly as the palette does, and typing also reaches project
files and symbols. Search Everywhere keeps its own chord either way.

## How matching works

Every picker now scores what you typed rather than just testing whether it
matched, and emboldens the characters actually responsible for the match.

- **Contiguity, boundaries and case.** A character earns a bonus for landing on
  a word boundary (the start, after a separator, a camelCase hump, a
  letter→digit transition) or for continuing a run. Spread-out matches are
  penalized. So an exact prefix beats an acronym beats a scattered subsequence.
- **The best alignment, not the first.** The match is computed by dynamic
  programming rather than greedily left to right, which is the difference
  between `mc` meaning the two humps of `MainController` and meaning whichever
  `c` happens to come first.
- **Several terms, any order.** Whitespace splits the query into terms that must
  all match, in any order — `toggle git` finds *Git: Toggle Blame*.
- **Paths know their basename.** The Project tree's filter scores the whole
  relative path, so a query can name a directory as well as a file.

Grouped and structural lists — build actions, branches, the Structure tree —
take the same matcher but keep their own order, because a tree that reorders
under a filter is worse, not better.

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
| Fold the selection | `view.createFoldFromSelection` | (palette) |
| Remove your manual folds | `view.removeManualFolds` | (palette) |
| Fold all block comments | `view.foldAllBlockComments` | (palette) |
| Fold / unfold all `#region` markers | `view.foldAllMarkerRegions` | (palette) |
| Fold / unfold all except the caret | `view.foldAllExcept` / `view.unfoldAllExcept` | (palette) |

**Fold Level *n*** collapses everything at that nesting depth, like VS Code's
`Ctrl+K Ctrl+1..7`. **Fold Recursively** collapses the region at the caret *and*
everything nested inside it, and unfolding recursively reveals the whole subtree.
The three **Go to … Fold** commands move the caret to a fold header, revealing it
first if it is hidden. None of these has a default key; bind them from
Settings → Keymap.

### Folds of your own

**Fold the selection** creates a range with no syntactic basis at all — whatever
lines you picked. Unlike a detected region, which is recomputed from the text as
you type, a manual range is tracked through your edits, and it **persists across
restarts** with the rest of your session. **Remove Manual Fold Ranges** clears
them for the file.

**Fold All Block Comments** and **Fold All `#region` Markers** fold exactly
those. The marker families recognized are `//#region`, `//region`, `#region`,
`# region`, `#pragma region`, `<!-- #region -->` and `--region`; Markdown and
Markwhen are deliberately excluded, since `# region` there is a legal heading.
**Fold All Except Caret** collapses everything but the block you are in.

All of these **merge** with the folds Editora detects, and with any a language
server reports, rather than replacing them.

Where a [language server](/docs/lsp#folding-and-selection) is running, the fold
regions come from the server rather than from brace and indent scanning, so
import blocks and javadoc fold as single regions. Collapsed regions are saved per
file and restored with your session.

## Back and forward

Jump around and step through your navigation history with **Go: Back**
(`nav.back`) and **Go: Forward** (`nav.forward`), like a browser's back/forward
for the caret.

### Recent Locations

**Go: Recent Locations…** (`nav.recentLocations`) lists the session's trail
newest-first, each row showing **the line you were on** — which is the thing you
actually lost when a jump took you elsewhere. `Foo.java:214` says nothing about
why you were there; the line usually says it at a glance.

It reads the same trail Back walks rather than keeping a second log, so the
picker and Back can never disagree about where you have been.

The picker also **previews**: the highlighted row is shown in the editor as the
selection moves, and dismissing the picker puts everything back where it was.
The undo is half the feature — without it, arrowing through a list and pressing
Escape leaves you wherever the cursor stopped, which is worse than not
previewing at all. Preview navigates without taking focus and without recording
history, so browsing a picker can't itself become something to navigate back
through, and a location whose file isn't open lands in the
[preview tab](/docs/code-navigation#preview-tabs), so a list of twenty locations
costs one tab rather than twenty.

## AceJump

`M-g j` labels every visible occurrence of the next character you type. Type a
label to jump the caret there. **Esc** or `C-g` cancels. It's the fastest way to
move the caret to something you can see. **Line mode** (`nav.aceJumpLine`,
`M-g L`) labels every visible line so you can jump straight to one.

## Closing tool windows

`M-g` closes a focused tool window (any of them) and returns focus to the
editor.
