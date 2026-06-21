---
title: "A reorganized Settings window, with built-in editors"
description: "The Settings sidebar is grouped into five sections, and macros, snippets, templates, and your personal dictionary are now managed in the GUI."
date: 2026-06-25
---

Settings got a cleanup and a set of in-app editors, so more of Editora is
configurable without touching a file by hand. Every setting is preserved, only
the organization and presentation changed.

**Grouped sidebar.** The flat list is now five sections, General, Editor,
Languages & Tools, Version Control, and System, with headers and indented items.
The overloaded *Editor* page was split (Code Completion, TODO, and Markdown are
their own pages), *Application* became **Interface** (window chrome, Simple/Zen
modes) and **Workspace** (projects, notes, local history), and HTTP Client and
HTML Preview merged into **Web**. Search now also hides empty group headers.

**Manage your stuff in the GUI.** New pages let you edit things that used to mean
hand-editing JSON:

- **Macros**: rename a saved macro, edit/reorder/remove its steps, and assign a
  keybinding inline.
- **Snippets** and **Templates**: edit the bundled (shipped) ones too, tagged
  read-only, with an edit writing a user override; the body is now a real code
  editor with **syntax highlighting and Emacs caret keys**.
- **Personal dictionary**: list, add, and remove your accepted spell-check words,
  with an enable toggle.

Palette entry points: *Snippets: Manage Snippets…*, *Templates: Manage File
Templates…*, *Spell: Manage Personal Dictionary…*, and the Macros page from
Settings. See [Keymaps](/docs/keymaps) and [Configuration](/docs/configuration).
