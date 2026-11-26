---
title: "Command-driven core"
group: "Keyboard & commands"
order: 1
beta: false
summary: "Every action is a registered command, bound to a chord or one <kbd>M-x</kbd> search away. There are 1,600+ commands and no hidden actions."
description: "Every Editora action is a registered command with an id, title and description: 1,600+ of them, one M-x search away, bindable to any key."
alt: "The command palette filtered by \"git\", listing Git commands with the highlighted one's description"
---

Editora has no hidden actions. Every capability (save, toggle a bookmark, start the debugger, switch a theme) is a registered command with an id and a title. Four things are built on that registry:

- The **command palette** (`M-x`) fuzzy-searches all 1,600+ commands, each with a one-line description. A command is also found by the first part of its id, so "markdown table" finds the `Table: …` commands.
- **Keybindings** are a map from a chord to a command id, so anything can be bound, or rebound.
- **Toolbar buttons** dispatch the same commands, so the UI and the keyboard never drift apart.
- The **[menu bar](/features/menu-bar/)** is a curated view of the same registry, so each item shows its live keybinding and can never name an action that doesn't exist.

If you can describe it, you can find it by typing a few letters. In the palette, the docs key (`C-h` in the Emacs keymap; the hint line names it in the others) opens the highlighted command's page in the user guide that ships with Editora, the one **Help ▸ Documentation** opens: the guide for the version you run, readable offline. Browse the full [command list](/commands/).

See the [Navigation guide](/docs/navigation/#command-palette).
