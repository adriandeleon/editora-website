---
title: "Read-only / View mode"
group: "Workspace & files"
order: 7
beta: false
summary: "Toggle a buffer read-only to browse without editing, with a View Mode banner and pager-style <kbd>Space</kbd>/<kbd>Backspace</kbd> paging."
description: "Toggle a buffer read-only with a View mode banner and pager keys. Files of 200,000 lines or 16 MB, very long lines and minified code open in View mode."
alt: "A Java file in View mode, with an amber banner and an Enable Editing button"
---

Read a file without the risk of changing it by accident. In View mode typing and edit commands are blocked, while highlighting, folding, search and copy keep working.

- **Toggle it** with `C-x C-q` in the Emacs keymap, or **View: Toggle Read-Only (View Mode)** in the palette.
- **A banner** above the editor says the file is for viewing, with an **Enable Editing** button when the file is writable.
- **Page like a pager**: Space pages down and Backspace pages up.
- **Files you cannot write** open read-only, and Save asks before replacing one.
- **Huge files open in View mode**: 200,000 lines or 16 MB or more, a line of a million characters, or minified code. The banner says why.
- **Remembered per file**, so a file you set read-only stays that way.

See the [Read-only and View mode guide](/docs/editing/#read-only-and-view-mode).
