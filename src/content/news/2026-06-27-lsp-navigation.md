---
title: "Better code navigation: symbols, references, and Ctrl-click"
description: "Go to Symbol in Workspace, a browsable References tool window, and Ctrl/Cmd-click to go to a definition round out the LSP navigation."
date: 2026-06-27
---

Getting around a codebase with a language server running just got easier.

- **Go to Symbol in Workspace** (`lsp.gotoSymbol`) opens a live search box that
  finds any symbol (class, function, method, and so on) across the whole project
  by name, and jumps to it.
- **References tool window.** *Find References* (`M-?`) now shows its results in a
  dedicated, browsable **References** window, grouped by file with a line and
  preview for each hit, instead of a one-shot list.
- **Ctrl/Cmd-click to go to definition.** In a code file with a running server,
  holding Ctrl (or ⌘ on macOS) and clicking a symbol jumps to its definition, the
  same gesture you know from other editors.

These join go-to-definition, hover, diagnostics, completion, and Format Document.
See the [LSP guide](/docs/lsp). Language servers are off by default; enable them
in Settings → LSP.
