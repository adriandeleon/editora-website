---
title: "Emacs heritage"
group: "Keyboard & commands"
order: 6
beta: false
summary: "The Emacs editing <em>model</em>, not just its keybindings: a kill ring and a mark ring, <code>C-x r</code> rectangles, narrowing, query-replace, <kbd>C-u</kbd> prefix arguments, and structural sexp motion. Emacs is the default keymap."
---

Plenty of editors ship an "Emacs keymap" that maps a handful of chords onto their own commands. Editora implements the model underneath: the concepts an Emacs user reaches for without thinking, built as real features rather than shortcut aliases. Emacs is the default keymap, and everything here works in the other four too, through the palette or a binding of your own.

## Killing and yanking

A real kill ring, not a clipboard with extra steps. It holds the last 120 kills (Emacs' own `kill-ring-max`), consecutive kills accumulate into one entry, and the ring is shared with the system clipboard in both directions, so text copied in another application is yankable and a kill is pasteable elsewhere.

- **C-k** kill line, **M-d** kill word, **M-DEL** backward kill word, **C-S-DEL** kill whole line
- **M-z** zap to char, **C-M-k** kill sexp
- **C-y** yank, **M-y** yank-pop to cycle back through the ring

## The mark

- **C-SPC** sets the mark, **C-x C-SPC** pops back through the ring, **C-x h** marks the whole buffer.
- The ring is per buffer, as in Emacs, and marks are tracked through edits: type above a mark and it stays on its text rather than its old offset.

## Rectangles

The full **C-x r** family over the columns between point and mark: kill, copy, yank, delete, clear, open, string-rectangle, and number-lines. Each is a single undo step. Note these are character columns, so a tab counts as one, and rectangles are distinct from the Alt-drag multiple-cursor selection.

## Narrowing

**C-x n** narrows to the region, the current defun, or a fold region, and widening restores everything. It is true narrowing: search, replace, Select All and macros all see only the region, which is the point of it. Writing the file still writes the whole file.

## Search, replace, and motion

- **C-s** / **C-r** incremental search, repeating the chord to cycle matches.
- **M-%** query-replace and **C-M-%** query-replace-regexp, with the usual `y`, `n`, `!`, `.` and `q` answers.
- **M-s o** occur, listing every match in the buffer.
- **C-M-f** / **C-M-b** move by sexp, **C-M-SPC** marks one, and **C-M-a** / **C-M-e** jump to the start and end of a defun.
- **M-g j** and **M-g L** jump to any visible character or line by typing its label, in the spirit of avy.

## Prefix arguments

**C-u** takes a numeric argument the way Emacs does: `C-u 5 C-n`, `C-u 40 -` to type a row of dashes, `C-u C-SPC` to pop the mark. It is a repeat model rather than a full numeric argument, so a negative argument does not reverse motion and `C-u 0` specials are not honoured.

## The rest of the muscle memory

Transpose (**C-t**, **M-t**, **C-x C-t**), fill paragraph (**M-q**) with a fill column and optional auto-fill, case commands (**M-u**, **M-l**, **M-c**), whitespace and line surgery (**M-\**, **M-SPC**, **C-x C-o**, **C-o**, **M-^**), comment-dwim (**M-;**), abbreviations (**C-x a e**, **C-x a g**), keyboard macros (**F3**, **F4**, **C-x e**), **M-x** for anything else, and **C-g** to back out.

## What isn't here

Registers, a global cross-buffer mark ring, and dabbrev are not implemented yet. Neither is Elisp: Editora is extended through [plugins](/features/plugins) and its [command system](/features/command-driven-core), not a built-in Lisp, so nothing here replaces a tuned Emacs configuration. Browse the [full command list](/commands) or the [keybindings reference](/keybindings).
