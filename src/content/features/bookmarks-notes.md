---
title: "Bookmarks & notes"
group: "Workspace & files"
order: 4
beta: false
summary: "Bookmarks to come back to and Markdown Personal Notes on code, both kept outside your source files and listed across a project."
description: "Line bookmarks with a gutter marker, and Markdown personal notes on words, lines, ranges, files and folders, kept outside your source files."
alt: "The Personal Note dialog over a Java file, with bookmark markers in the gutter"
---

Mark the places you want to come back to, and keep notes on code without writing them into the file. Both follow the code as it changes and belong to the [project](/features/projects/) you are in.

## Bookmarks

- **Toggle** a bookmark on a line with `C-c m` in the Emacs keymap, or **Add / Remove Bookmark** in the editor's right-click menu. A gutter marker shows it, and it can carry a short note.
- **Jump** with `C-c ]` and `C-c [` within a file, or `M-g b` to pick one from any file. The other keymaps have **Bookmarks: Jump…** in the palette.
- **Mnemonics**: **Bookmarks: Set Mnemonic** gives a bookmark one character, and each digit has a command you can bind to a single key.
- **The Bookmarks window** lists them across files in an order you choose, by drag or Alt+Up and Alt+Down.

## Personal Notes

- **Add a note** to a word, a line, a range, a file or a folder with `C-c n` in the Emacs keymap, or **Add Personal Note** in the right-click menu.
- **Outside the file**, so notes work on read-only, generated or shared code, and on context that does not belong in source.
- **Markdown**: a noted span is tinted, and hovering it shows the note rendered.
- **Follow the code** through edits, renames and outside changes.
- **Find them again** with `M-g n` in the Emacs keymap, **Search Notes**, or the Personal Notes window. Folder notes show as tooltips in the Project tree.
- **Resolve** a note with **Toggle Note Resolved** when it is done, or reopen it the same way.
- **On the [Project Map](/features/project-map/)**, notes sit beside the card lines they belong to.

**On by default.** Turn Personal Notes off in **Settings → Workspace → Features** (**Enable Personal Notes**). See the [Bookmarks & notes guide](/docs/bookmarks-notes/).
