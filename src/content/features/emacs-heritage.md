---
title: "Emacs heritage"
group: "Keyboard & commands"
order: 7
beta: false
summary: "The Emacs editing <em>model</em>, not just its keybindings: a kill ring and a mark ring, registers, <code>C-x r</code> rectangles, narrowing, query-replace, <kbd>C-u</kbd> prefix arguments, and structural sexp motion. Emacs is the default keymap."
description: "The Emacs editing model in Editora: kill ring, mark ring, registers, C-x r rectangles, narrowing, query-replace, C-u prefix arguments and sexp motion."
alt: "The command palette filtered by \"kill\", listing the Emacs kill commands with their chords"
---

Plenty of editors ship an "Emacs keymap" that maps a handful of chords onto their own commands. Editora builds the model underneath, so the concepts an Emacs user reaches for without thinking are real features, not shortcut aliases. Every chord below is in the Emacs keymap, the default; the same commands are in the palette and bindable in the other four keymaps.

## Kill and yank

The kill ring holds the last 120 kills, consecutive kills join into one entry, and the ring is shared with the system clipboard both ways.

- **Kill**: `C-k` line, `M-d` word, `M-backspace` word backward, `C-S-backspace` whole line, `M-z` zap to char, `C-M-k` sexp.
- **Yank**: `C-y` yanks, and `M-y` cycles back through the ring.

## Mark, rectangles and registers

- **The mark**: `C-space` sets it, `C-x C-space` pops back through the ring, `C-x h` marks the whole buffer. The ring is per buffer, and marks stay on their text as you edit.
- **Rectangles**: the full `C-x r` family works on the columns between point and mark: kill, copy, yank, delete, clear, open, string-rectangle and number-lines, each one undo step.
- **Text registers**: `C-x r s` copies the region into a one-character register, and `C-x r i` inserts it.
- **Position registers**: `C-x r space` saves point, and `C-x r j` jumps back, opening the file if it is closed. The position follows edits to its file.
- **Edit: List Registers** shows them all. Registers last for the session, one set per window.

## Narrow, search and move

- **Narrowing**: `C-x n n`, `C-x n d` and `C-x n f` narrow to the region, the defun or a fold region, and `C-x n w` widens. Search, replace and macros see only the narrowed part; saving writes the whole file. See [Structure narrowing](/features/structure-narrowing/).
- **Incremental search**: `C-s` and `C-r`, repeating the chord to step through matches.
- **Query-replace**: `M-S-5` (Emacs' `M-%`) and `C-M-S-5` for the regexp form, with the usual `y`, `n`, `!`, `.` and `q` answers.
- **Occur**: `M-s o` lists every matching line in the buffer.
- **Sexps and defuns**: `C-M-f` and `C-M-b` move by sexp, `C-M-space` marks one, `C-M-a` and `C-M-e` jump to a defun's start and end.
- **Jump by label**: `M-g j` and `M-g S-l` jump to any visible character or line by typing its label, in the spirit of avy.

## Repeat and record

- **Prefix arguments**: `C-u` repeats the next command, as in `C-u 5 C-n`, or `C-u 40 -` for a row of dashes.
- **Keyboard macros**: `F3` records, `F4` stops, `C-x e` replays, and a saved macro becomes a command you can bind. See [Keyboard macros](/features/macros/).
- **M-x** finds any command by name, with its description and key, and a greyed row says why a command cannot run. See the [command-driven core](/features/command-driven-core/).

## The rest of the muscle memory

- **Transpose**: `C-t`, `M-t` and `C-x C-t`.
- **Fill**: `M-q` fills a paragraph to the fill column, with optional auto-fill.
- **Case**: `M-u`, `M-l` and `M-c`.
- **Whitespace and lines**: `M-\`, `M-space`, `C-x C-o`, `C-o` and `M-S-6` (Emacs' `M-^`).
- **Comments and abbreviations**: `M-;` comments, `C-x a g` defines an abbreviation and `C-x a e` expands it.
- **Back out**: `C-g` cancels anything.

## What Editora does differently

- **No Elisp.** Editora answers "can it do X?" by [running the tools you have installed](/features/doctor/): your `git`, your `ripgrep`, your language servers. You extend it with [plugins](/features/plugins/) and the command system, so it does not replace a tuned Emacs configuration.
- **Discoverable by default.** Every action has a title and a description, Settings is a real window with checkboxes, and you should not need the manual to find a feature.
- **A default, not a requirement.** Four other complete [keymaps](/features/keymaps/) ship, and any command can be rebound.
- **Modern windows.** Tabs, tool windows, a project tree, drag and drop and a working mouse replace the Emacs buffer and window model.

## Limits of the port

- `C-u` repeats rather than passing a true numeric argument, so a negative argument does not reverse a motion.
- Rectangles count character columns, so a tab counts as one.
- Number, rectangle and window-configuration registers, a global cross-buffer mark ring and dabbrev are not supported; `M-/` opens completion instead.

Browse the [command list](/commands/) or the [keybindings](/keybindings/). See the [Emacs editing model guide](/docs/editing/#the-emacs-editing-model).
