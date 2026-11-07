---
title: "Structure narrowing"
group: "Code intelligence"
order: 5
beta: false
summary: "Walk a file one function at a time: a toggle in the Structure tool window narrows the editor to the symbol you click, and a click on the next one moves there."
---

The **Structure** tool window (`tool.structure`) lists a file's declarations. Its **Narrow to fold region** toggle, at the end of the sort row, turns that list into a way to read a file one piece at a time. A click narrows the editor to the picked symbol's own block (on a method, the method itself), and while the toggle is on, a click or Enter on another symbol narrows to that one instead of only scrolling there. In Markdown it narrows a heading to its section.

Narrowing restricts the buffer itself, as in Emacs: search, replace, macros and Select All see only the region, and saving still writes the whole file. An amber banner names the lines shown (*Narrowed to lines 32–36 of 81*) and has a **Widen** button, and the gutter, the status bar's **Ln** and the outline keep the file's own line numbers. The outline stays the whole file's, with the rows outside the region muted.

- `f` flips the toggle while the outline has focus, and `structure.narrowFold` can be bound in any keymap.
- Widening any way, from the banner, the status-bar badge or `C-x n w`, ends the mode.
- The `C-x n` chords from [Emacs heritage](/features/emacs-heritage) narrow too, and the toggle shows when one of them did.
- On the [Project Map](/features/project-map), a declaration card's **Open** opens its file narrowed to the declaration.

While a buffer is narrowed, language-server support is suspended, and bookmarks, notes and breakpoints stop being saved until you widen. See [The Emacs editing model](/docs/editing#the-emacs-editing-model).
