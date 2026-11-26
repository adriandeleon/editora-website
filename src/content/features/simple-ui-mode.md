---
title: "Simple UI mode"
group: "Customization & extensibility"
order: 5
beta: false
summary: "One switch turns Editora into a plain editor: no gutter, minimap or extra toolbars, and language servers, debugging, Git and AI off until you leave it."
description: "One switch hides the gutter, minimap and extra toolbars and turns off language servers, debugging, Git and AI for a calm, minimal editor."
alt: "Simple UI mode: a Java file with a short menu bar, a small toolbar, and no gutter or tool windows"
---

Simple UI mode keeps only what you need to open, write and save files. It suits writing, and anyone starting out with code editors who wants a calm window.

- **Less on screen.** It hides the extra toolbar groups, the project selector, the tool-window stripe, the breadcrumb, the whole gutter, the minimap and most status-bar segments.
- **Fewer features running.** Language servers, debugging, Git, AI, multiple cursors, external tools and plugins are off while the mode is on.
- **A short menu bar.** The [menu bar](/features/menu-bar/) keeps File, Edit, Find, View and Help. The menus for switched-off features go, rather than sitting greyed out.
- **Trimmed right-click menus.** The tab, editor, Project tree and preview menus keep saving, editing, printing, closing and file management.
- **An easy way back.** The reduced View menu keeps the toggle, and turning the mode off restores everything exactly as it was.

Toggle it with **View: Toggle Simple UI Mode** in the palette, the toolbar button beside Settings, or **Settings → Interface → Modes**. The `--simple` flag starts one session in it.

The other three ways to run the editor are [Zen mode](/features/zen-mode/), [Expert mode](/features/expert-mode/) and the [full IDE](/features/full-ide/).

**Off by default.** Turn it on in **Settings → Interface → Modes**. See the [Focus modes guide](/docs/workspace/#focus-modes).
