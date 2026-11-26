---
title: "A menu bar, over the same commands"
group: "Keyboard & commands"
order: 8
beta: false
summary: "<strong>File / Edit / Find / View / Navigate / Code / Run / Git / Tools / Window / Help</strong> menus, built over the command registry, so every item shows its live keybinding. Hide it in one keystroke."
description: "File to Help menus built over the command registry, each item with its live keybinding. On Windows and Linux the menu bar shares the title bar by default."
alt: "The Git menu open, listing its commands with their keybindings and submenus such as Branches and Stash"
---

The command palette finds anything you can name, but it is hard to browse. The menu bar is for seeing what the editor can do: eleven menus, from **File** to **Help**, each item a registered [command](/features/command-driven-core/), so a menu never names an action that does not exist.

## Browse what Editora can do

- **Live keys**: each item shows its current keybinding and updates when you [switch keymaps](/features/keymaps/).
- **A stable map**: a command whose feature is switched off is greyed rather than removed, so the menus never rearrange themselves.
- **The same icons everywhere**: an item carries the glyph that action has in right-click menus, so Save looks the same wherever you reach it.
- **Short menus**: daily items stay at the top and the rest sit one level down. Git groups its commands by what they act on, Run keeps the debugger in four submenus, and **File ▸ Export** holds the export formats.
- **A curated subset**: Editora has over 1,600 commands, so the menus hold the useful ones and the palette stays the complete index.

## Keep it out of the way

- **From the keyboard**: **View: Focus Menu Bar** (`F10` in the Emacs keymap, `Ctrl+Shift+F10` in CUA, VS Code and Sublime Text) opens the first menu.
- **One row of chrome**: on Linux and Windows the menus share the window's title bar, giving the editor a full row back. Turn off **Merge the menu bar into the title bar** in **Settings → Interface** for the system title bar; it applies after a restart.
- **On macOS** the menus sit in the system menu bar.
- **Hide it** with **View: Toggle Menu Bar**. It hides itself in Zen and Expert mode, and [Simple mode](/features/simple-ui-mode/) keeps a reduced menu bar, because a first-time user is the person most likely to browse.

**On by default.** Turn it off in **Settings → Interface → Window chrome**. See the [Workspace guide](/docs/workspace/#window-chrome).
