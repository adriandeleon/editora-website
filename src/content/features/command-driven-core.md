---
title: "Command-driven core"
group: "Keyboard & commands"
order: 1
beta: false
summary: "Every action is a registered <code>Command</code>, bound to a chord or one <kbd>M-x</kbd> search away. There are 700+ commands and no hidden actions."
---

Editora has no hidden actions. Every capability (save, toggle a bookmark, start the debugger, switch a theme) is a registered `Command` with an id and a title. Four things are built on that registry:

- The **command palette** (`M-x`) fuzzy-searches all 700+ commands, each with a one-line description.
- **Keybindings** are a map from a chord to a command id, so anything can be bound, or rebound.
- **Toolbar buttons** dispatch the same commands, so the UI and the keyboard never drift apart.
- The **[menu bar](/features/menu-bar)** is a curated view of the same registry, so each item shows its live keybinding and can never name an action that doesn't exist.

If you can describe it, you can find it by typing a few letters. Browse the full [command list](/commands).
