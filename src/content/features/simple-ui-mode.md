---
title: "Simple UI mode"
group: "Customization & extensibility"
order: 3
beta: false
summary: "One toggle strips the editor to the essentials, hiding the extra toolbar groups, tool-window stripe, breadcrumb, gutter, and minimap for a calm, minimal surface."
---

One toggle strips the editor to the essentials (hiding the extra toolbar groups, the tool-window stripe, the breadcrumb, the **entire gutter** (line numbers, fold chevrons, and all markers), and the minimap) and turns off the heavier features (LSP, debugging, Git, multiple cursors) for a calm, minimal surface.

The [menu bar](/features/menu-bar) stays, **simplified rather than hidden** — File, Edit, Find, View, Help. The menus that go are exactly the ones the mode switches off, which would otherwise sit there entirely greyed out. Toggling Simple mode stays in that reduced View menu, so it is never a one-way door for anyone who entered it from there.

Toggle it from Settings → Interface → Modes, the toolbar, the palette, or the `--simple` CLI flag (session-only). Toggling off restores everything exactly.
