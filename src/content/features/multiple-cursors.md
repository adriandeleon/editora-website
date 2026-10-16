---
title: "Multiple cursors"
group: "Keyboard & commands"
order: 5
beta: false
summary: "Add a caret at the next occurrence, above/below, on every occurrence or at every selected line's end, split a selection into lines, or <kbd>Alt</kbd>-drag a column/box selection to edit many places at once."
---

Edit many places at once, VS Code-style. Add a caret at the **next occurrence** of the selection, or **above / below** the current line, or **Alt-drag** a column/box selection. Type, and the edit fans out to every caret; `Esc` collapses back to one.

**Select all occurrences** (`Ctrl+Shift+L` in the VS Code keymap, `Alt+F3` in Sublime's) puts a cursor on every occurrence of the selection, or of the word under the caret, in one step. From the Find bar, **Alt+Enter** does the same for every match of the current query, so the query's case, regex and whole-word toggles decide what gets a cursor.

Shaping the set of cursors:

- **Split Selection into Lines** (`Ctrl+Shift+L` in the Sublime keymap) turns every selection into one selection per line it covers, each holding that line's selected part.
- **Add Carets to Line Ends** (`Shift+Alt+I` in the VS Code and CUA keymaps, `Alt+Shift+G` in IntelliJ's) puts a cursor at the end of every selected line.
- **Skip Occurrence** (`Ctrl+K Ctrl+D` in VS Code and Sublime) moves the newest selection on to the next match instead of adding one, wrapping round to the start.
- **Remove Last Added Caret** (`Ctrl+U` in VS Code and Sublime, `Alt+Shift+J` in IntelliJ) takes back the cursor added last, one at a time; the main cursor stays.

The [line commands](/features/line-and-text-tools) and Tab/Shift-Tab run at every cursor as one undo step.

It's powered by Editora's RichTextFX fork, which adds multiple cursors and column selection as a layered input map that does nothing when there's a single caret.

Movement chords fan out too: `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and `M-b` move every caret, like the arrow keys. Document, paragraph, sentence and page motions stay on the primary caret.
