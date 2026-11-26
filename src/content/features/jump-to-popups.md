---
title: "Jump-to popups"
group: "Keyboard & commands"
order: 4
beta: false
summary: "Fuzzy-jump to recent files, symbols, open tabs, and tool windows, plus an Emacs <code>find-file</code>-style path finder."
description: "Fuzzy pickers for recent files, symbols, open tabs, tool windows, bookmarks and notes, plus an Emacs find-file style path finder."
alt: "The Jump to Structure picker listing a Java class and its methods"
---

Get anywhere in a project without touching the mouse. Each picker opens on a chord, filters as you type, and shows a footer legend of its keys.

- **Recent Files: Jump…** (`C-x C-r` in the Emacs keymap) reopens a file you had open.
- **Structure: Jump…** (`M-g i`) lists the current file's symbols.
- **Open Files: Jump…** (`C-x b`) switches between open tabs.
- **Tool Windows: Jump…** (`M-g t`) opens any tool window by name.
- **Bookmarks: Jump…** (`M-g b`) and **Jump to Note…** (`M-g n`) reach your bookmarks and notes across files.
- **File: Find File…** (`C-x C-f`) is an Emacs-style path finder: Tab completes, Enter goes into a folder or opens a file, creating it if needed, and `C-d` opens a folder as a project.

**Smart ranking**: every picker scores what you typed by contiguity, word and camelCase boundaries and exact case, and bolds the matched characters, so `mcon` finds `MainController` first.

The other keymaps bind several of these in their own editors' style; see the [keybindings](/keybindings/). When you don't know which picker holds something, use [Search Everywhere](/features/search-everywhere/).

See the [Navigation guide](/docs/navigation/#jump-pickers).
