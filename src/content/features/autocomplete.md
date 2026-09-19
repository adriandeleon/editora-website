---
title: "Autocomplete"
group: "Code intelligence"
order: 4
beta: false
summary: "As-you-type completion: a popup for code (LSP + snippets) and inline ghost text for prose. Trigger with <kbd>C-M-i</kbd> / <kbd>M-/</kbd>."
---

Completion appears as you type, debounced and off the hot path.

**Code** buffers get a popup that merges LSP results and snippets, Enter or Tab to accept. Java member completion appears immediately; identifier completion waits for a short pause, filters complete lists locally, and cancels obsolete requests as you keep typing. Overloads stay distinct, method calls reuse existing parentheses, and commit characters behave as the server intended.

Accepting a snippet starts a full tab-stop session. An LSP item can auto-add its import even after safe continued typing; completion plus import undo and redo as one action, while later typing remains separate.

**Prose** buffers get inline **ghost text**, a muted suffix you accept with Tab.

Trigger manually with `C-M-i` or `M-/`. Per-source toggles (words, snippets) live in Settings → Editor.
