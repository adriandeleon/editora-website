---
title: "Code navigation"
group: "Code intelligence"
order: 3
beta: false
summary: "Go to symbol <strong>with no language server</strong>, sticky scroll, Peek Definition, preview tabs, and related-file jumps. Move around code without losing your place."
---

Navigation is two questions: can the thing be **found**, and can you jump **without losing your place**.

**Go to Symbol in Project** answers the first with Editora's own declaration scanner across sixteen language ids, so it works on a first run and in every language that ships a grammar but has no server. The index is built lazily on first use — walking every file the moment a project opens spends real work for someone who may never ask it anything — and is incremental after that, rescanning exactly the file you saved. It under-reports on purpose: a missing declaration costs you one fallback to search, an invented one teaches you not to trust the feature. A running [language server](/features/lsp) is still the better answer and takes precedence.

The rest is flow:

- **Sticky scroll** pins the enclosing scope headers above the viewport, so deep in a long method you can still see what it belongs to.
- **Peek Definition** shows a definition over the editor and leaves your place, scroll and tab count where they were. Enter commits to the real jump.
- **Preview tabs** make browsing cost one tab, not one per glance — the reused slot's title is italic, and editing or explicitly opening the file promotes it.
- **Recent Locations** lists the session's trail with the line you were on, and previews as you arrow through it.
- **Go to Related File** pairs a file with its counterpart: a test and its subject, a header and its implementation, a component and its stylesheet.

See [Code navigation](/docs/code-navigation).
