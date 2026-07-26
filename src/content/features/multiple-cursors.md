---
title: "Multiple cursors"
group: "Keyboard & commands"
order: 4
beta: false
summary: "Add a caret at the next occurrence, above/below, or on every occurrence at once, or <kbd>Alt</kbd>-drag a column/box selection to edit many places at once, VS Code-style."
---

Edit many places at once, VS Code-style. Add a caret at the **next occurrence** of the selection, or **above / below** the current line, or **Alt-drag** a column/box selection. Type, and the edit fans out to every caret; `Esc` collapses back to one.

**Select all occurrences** (`Ctrl+Shift+L` in the VS Code and Sublime keymaps) puts a cursor on every occurrence of the selection, or of the word under the caret, in one step. From the Find bar, **Alt+Enter** does the same for every match of the current query, so the query's case, regex and whole-word toggles decide what gets a cursor.

It's powered by Editora's RichTextFX fork, which adds multiple cursors and column selection as a layered input map that's completely transparent when there's a single caret.

Movement chords fan out too: `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and `M-b` move every caret, like the arrow keys. Document, paragraph, sentence and page motions stay on the primary caret.
