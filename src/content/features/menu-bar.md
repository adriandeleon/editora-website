---
title: "A menu bar, over the same commands"
group: "Keyboard & commands"
order: 8
beta: false
summary: "Prefer to browse rather than recall? <strong>File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help</strong>, built over the command registry, so every item shows its live keybinding. Hide it in one keystroke."
---

The command palette is complete but unbrowsable: it answers "what is this called?" and not "what can this thing do?". The menu bar answers the second question.

**File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help.** Every item names a registered [command](/features/command-driven-core) — the same objects the palette lists and the keymap binds — so nothing in it can drift out of step with what Editora can actually do.

- Each entry shows its **current keybinding**, and updates when you [switch keymaps](/features/keymaps).
- A command whose feature is switched off appears **greyed rather than vanishing**, so the menu stays a stable map instead of rearranging itself as you toggle features.
- Almost every item that can carry an **icon** does — the same glyph you see for that action in a right-click menu, so Save looks like Save wherever you reach it from. About a third are deliberately left blank rather than given an invented glyph; the icon column is reserved either way, so the titles still line up.
- On **macOS** it sits in the system menu bar, where it belongs.

It is deliberately a curated subset. Editora registers over six hundred commands and a menu that listed all of them would be a worse palette; the palette remains the complete index.

On Linux and Windows it can **share the window's title bar** — menus, title and system buttons on one row, a full bar of vertical space back for the editor. Experimental, off by default, under **Settings → Interface**.

Hide it from **Settings → Interface** or with **View: Toggle Menu Bar**, and it hides itself in Zen and Expert modes. [Simple mode](/features/simple-ui-mode) keeps a reduced one — File, Edit, Find, View, Help — because the mode aimed at someone new to the editor is the one that needs a browsable map most.
