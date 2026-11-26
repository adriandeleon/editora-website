---
title: "Expert mode"
group: "Customization & extensibility"
order: 4
beta: false
summary: "A lighter focus mode than Zen: the menu bar, toolbar, tabs, breadcrumb, and tool stripes go, while line numbers, the minimap, and the status bar stay."
description: "A lighter focus mode: the menu bar, toolbar, tabs, breadcrumb and stripes go, while line numbers, minimap and status bar stay."
alt: "Expert mode: a Java file with only line numbers, the minimap and the status bar around it, and the E button that leaves the mode"
---

Expert mode removes the window chrome around the editor and keeps the editor itself complete. The [menu bar](/features/menu-bar), toolbar, tab bar, breadcrumb, tool-window stripes, and whitespace guides are hidden; where the menus share the title bar, the title and the window buttons stay. Line numbers, the minimap, the rulers, the indent guides, the current-line highlight, and the status bar stay.

It suits people who drive the editor from the keyboard and want the room back without losing their bearings in the file. Nothing is switched off, so language servers, Git, and every command keep working.

- Toggle it with `C-c C-e` in the Emacs keymap, with **View: Toggle Expert Mode** from the palette, from Settings → Interface → Modes, or start in it with the `--expert` flag.
- A small floating **E** button in the corner turns it off again.
- It applies to one window and never changes your saved settings.
- Expert and [Zen mode](/features/zen-mode) are mutually exclusive: turning one on turns the other off.

The other three ways to run the editor are [Zen mode](/features/zen-mode), [Simple UI mode](/features/simple-ui-mode), and the [full IDE](/features/full-ide). See also [Focus modes](/docs/workspace#focus-modes) in the docs.
