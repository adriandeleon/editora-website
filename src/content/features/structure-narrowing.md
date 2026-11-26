---
title: "Structure narrowing"
group: "Code intelligence"
order: 5
beta: false
summary: "Walk a file one function at a time: a toggle in the Structure tool window narrows the editor to the symbol you click, and a click on the next one moves there."
description: "Read a file one declaration at a time: the Structure window narrows the editor to the symbol you pick, Emacs-style, with a Widen banner."
alt: "A Java method narrowed: an amber banner says lines 39 to 62 of 74 are in reach, with Widen, and the Structure window beside it"
---

A long file is easier to read one function at a time. Turn on narrowing in the **Structure** tool window, and each symbol you pick fills the editor on its own, with the rest of the file out of the way until you widen.

## Walk a file

- **The toggle**: **Narrow to fold region**, at the end of the Structure window's sort row, narrows the editor to the picked symbol's block.
- **Next symbol**: while the toggle is on, a click or Enter on another symbol narrows to that one instead of only scrolling there.
- **Markdown** narrows a heading to its section.
- **From the keyboard**: `f` flips the toggle while the outline has focus, and **Structure: Toggle Narrow to Fold Region** can take a key in any keymap.
- **From the [Project Map](/features/project-map/)**: a declaration card's **Open** opens its file narrowed to that declaration.

## What narrowing means

- **Real narrowing, as in Emacs**: search, replace, macros and Select All see only the region, and saving still writes the whole file.
- **You always know**: an amber banner names the lines shown, such as *Narrowed to lines 32–36 of 81*, with a **Widen** button.
- **The file's own line numbers** stay in the gutter, the status bar and the outline, and the outline mutes the rows outside the region.
- **Widen any way** to end it: the banner, the status-bar badge or `C-x n w` in the Emacs keymap. The `C-x n` chords from [Emacs heritage](/features/emacs-heritage/) narrow too.
- **While narrowed**, language-server features pause, and bookmarks, notes and breakpoints are not saved until you widen.

See the [Emacs editing model guide](/docs/editing/#the-emacs-editing-model).
