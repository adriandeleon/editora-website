---
title: "Search Everywhere"
group: "Keyboard & commands"
order: 3
beta: false
summary: "One picker over <strong>commands, files and symbols</strong>. Type the name of the thing instead of first choosing which finder it lives in. A <code>&gt;</code>, <code>#</code> or <code>@</code> prefix narrows it when you already know."
description: "One picker over commands, project files and symbols, grouped by source. A > (commands), # (files) or @ (symbols) prefix narrows it to one kind."
alt: "Search Everywhere for \"theme\", with matching files and symbols in separate groups"
---

Every other picker asks you to decide what kind of thing you want before you can type its name. Search Everywhere skips that step: type the name, and commands, project files and symbols are searched together.

- **Open it**: `M-S-x` in the Emacs keymap, `Ctrl+Shift+E` in the others (`Cmd+Shift+E` on macOS).
- **Narrow when you know**: start with `>` for commands only, `#` for files only, or `@` for symbols only.
- **Grouped results**: commands, files and symbols each keep their own group and a fair share of rows, so thousands of symbols never push the one command you wanted off the list.
- **Best group first**: groups are ordered by their best match, not by how many matches they have.
- **Teaches like the palette**: an empty query lists every command, a switched-off feature's command is greyed with the setting that enables it, and the highlighted row's description sits under the list.
- **No server needed**: symbols come from Editora's own [project symbol index](/features/code-navigation/), so they work with no language server installed.
- **One chord for all of it**: **Palette shortcut opens Search Everywhere** in **Settings → Interface → Pickers** gives the palette's chord to Search Everywhere.

In a [remote project](/features/remote-sftp/) it lists files from the host and has no symbols.

See the [Navigation guide](/docs/navigation/#search-everywhere).
