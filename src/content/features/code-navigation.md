---
title: "Code navigation"
group: "Code intelligence"
order: 3
beta: false
summary: "Go to symbol <strong>with no language server</strong>, sticky scroll, Peek Definition, preview tabs, and related-file jumps. Move around code without losing your place."
description: "Go to Symbol and Go to Definition without a language server, Ctrl or Cmd hover links, sticky scroll, Peek Definition, preview tabs and related files."
alt: "The Go to Symbol in Project picker over a Java file, listing classes with their files and lines"
---

Navigation needs two things: a symbol has to be **findable**, and you have to be able to jump to it **without losing your place**.

## Find it

- **Go to Symbol in Project** searches every declaration in the project with Editora's own index, so it works on a first run and in 19 languages with no server installed.
- **Go to Definition** uses the same index when no server is running: it looks for the name at the caret in the file, then in the project.
- **A running [language server](/features/lsp/)** gives better answers and takes over when it starts.
- **Trustworthy results**: the index leaves out what it is unsure of, so it never sends you to a place that does not exist.

## Keep your place

- **Sticky scroll** pins the enclosing scope headers above the code, so deep in a long method you can still see what it belongs to.
- **Ctrl-hover** (Cmd on macOS) underlines the symbol under the mouse when Go to Definition can follow it; click to go there.
- **LSP: Peek Definition** shows a definition over the editor and leaves your place, scroll and tabs as they were. Enter makes the real jump; it needs a language server.
- **Preview tabs** reuse one tab while you browse instead of opening one per file. Its title is italic, and editing the file or opening it for real makes it a normal tab.
- **Go: Recent Locations…** lists the session's trail with the line you were on, and previews each as you arrow through it.
- **Go: Related File** (`C-c S-t` in the Emacs keymap) jumps between a test and its subject, a header and its implementation, or a component and its stylesheet.

See the [Code navigation guide](/docs/code-navigation/#go-to-symbol-in-project).
