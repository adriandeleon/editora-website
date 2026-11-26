---
title: "Multiple cursors"
group: "Keyboard & commands"
order: 5
beta: false
summary: "Add a caret at the next occurrence, above/below, on every occurrence or at every selected line's end, split a selection into lines, or <kbd>Alt</kbd>-drag a column/box selection to edit many places at once."
description: "Add carets at the next or every occurrence, above or below, or at line ends, split a selection into lines, or Alt-drag a column selection."
alt: "Five carets in a column of Java lines, the same word typed at each"
---

Change ten places at once instead of making the same edit ten times. Put a caret everywhere the edit belongs, type once, and the edit lands at every caret; `Esc` collapses back to one.

## Add carets

- **Alt-drag** draws a column or box selection with a caret on every line, in every keymap.
- **Edit: Add Caret at Next Occurrence** adds a caret at the next match of the selection.
- **Edit: Add Caret Above** and **Edit: Add Caret Below** stack carets on the neighbouring lines.
- **Edit: Select All Occurrences** puts a caret on every occurrence of the selection, or of the word under the caret.
- **Alt+Enter in the find bar** puts a caret on every match, so the query's case, regex and whole-word toggles decide what gets one.
- **Edit: Add Carets to Line Ends** puts a caret at the end of every selected line.
- **Edit: Split Selection into Lines** turns a selection into one selection per line it covers.

## Shape the set

- **Edit: Skip Occurrence** moves the newest selection on to the next match instead of adding one.
- **Edit: Remove Last Added Caret** takes back the most recent caret, one at a time.

## Edit at every caret

- **Typing, Tab and Shift+Tab** happen at every caret, and the [line commands](/features/line-and-text-tools/) run at each one as a single undo step.
- **Movement fans out**: the arrow keys and `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and `M-b` move every caret. Page and document motions move the main caret only.

In the Emacs keymap the caret commands are in the palette. The VS Code, Sublime Text and IntelliJ IDEA keymaps bind them as those editors do, such as `Ctrl+D` for the next occurrence in VS Code and Sublime Text (`Cmd+D` on macOS); see the [keybindings](/keybindings/).

**On by default.** Turn it off in **Settings → Editor**. See the [Editing guide](/docs/editing/#multiple-cursors).
