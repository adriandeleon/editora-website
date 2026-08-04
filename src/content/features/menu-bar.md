---
title: "A menu bar, over the same commands"
group: "Keyboard & commands"
order: 7
beta: false
summary: "Prefer to browse rather than recall? <strong>File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help</strong>, built over the command registry, so every item shows its live keybinding. Hide it in one keystroke."
---

The command palette is complete but unbrowsable: it answers "what is this called?" and not "what can this thing do?". The menu bar answers the second question.

**File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help.** Every item names a registered [command](/features/command-driven-core) — the same objects the palette lists and the keymap binds — so nothing in it can drift out of step with what Editora can actually do.

- Each entry shows its **current keybinding**, and updates when you [switch keymaps](/features/keymaps).
- A command whose feature is switched off appears **greyed rather than vanishing**, so the menu stays a stable map instead of rearranging itself as you toggle features.
- On **macOS** it sits in the system menu bar, where it belongs.

It is deliberately a curated subset. Editora registers over six hundred commands and a menu that listed all of them would be a worse palette; the palette remains the complete index.

Hide it from **Settings → Interface** or with **View: Toggle Menu Bar**, and it hides itself in Zen, Expert and [Simple](/features/simple-ui-mode) modes.
