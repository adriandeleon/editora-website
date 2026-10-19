---
title: "Simple UI mode"
group: "Customization & extensibility"
order: 5
beta: false
summary: "One toggle strips the editor to the essentials, hiding the extra toolbar groups, tool-window stripe, breadcrumb, gutter, and minimap for a calm, minimal surface."
---

One toggle strips the editor to the essentials (hiding the extra toolbar groups, the tool-window stripe, the breadcrumb, the **entire gutter** (line numbers, fold chevrons, and all markers), and the minimap) and turns off the heavier features (LSP, debugging, Git, multiple cursors) for a calm, minimal surface.

The [menu bar](/features/menu-bar) stays, **simplified rather than hidden**, with File, Edit, Find, View and Help. The menus that go are the ones for features the mode switches off, which would otherwise be entirely greyed out. The Simple mode toggle stays in that reduced View menu, so anyone who entered the mode from there can leave it the same way. The right-click menus are trimmed the same way: the tab, editor, Project tree and preview menus keep saving, editing, printing, closing and file management, and leave out Git, Local History, Run, Debug and the language-server, build and AI items. Turning the mode off brings every entry back.

Toggle it from Settings → Interface → Modes, the toolbar, the palette, or the `--simple` CLI flag (session-only). Toggling off restores everything exactly.

The other three ways to run the editor are [Zen mode](/features/zen-mode), [Expert mode](/features/expert-mode), and the [full IDE](/features/full-ide).
