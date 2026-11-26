---
title: "Autocomplete"
group: "Code intelligence"
order: 4
beta: false
summary: "As-you-type completion: a popup for code (LSP + snippets) and inline ghost text for prose. Trigger with <kbd>C-M-i</kbd> / <kbd>M-/</kbd>."
description: "A completion popup that merges language-server results and snippets for code, and inline ghost-text suggestions for prose."
alt: "A stand-in illustration of an editor, with faint lines of code and the word Autocomplete"
---

Completion saves keystrokes and the trip to the docs: Editora suggests what comes next as you type, from your language server and snippets in code, and from a word list in prose.

- **One popup in code** merges language-server results and snippets; Enter or Tab accepts.
- **The whole list**: everything the server offers is listed, so `os.` in Python shows all of the module's members.
- **Docs beside the list** show the highlighted item's signature and documentation, widening to fit a long signature.
- **Snippets** start a tab-stop session, so Tab moves through the fields to fill in.
- **Imports come along**: accepting a Java item adds its import, and one undo takes back both.
- **A slow server says so**: a row such as "Waiting for Pyright…" shows until its answer arrives.
- **Ghost text in prose** finishes the word you are typing in plain text and Markdown as a muted suffix; Tab accepts it.

Completion appears as you type. To ask for it, press `C-M-i` or `M-/` in the Emacs keymap, or `Ctrl+Space` in the others.

**On by default.** Turn it off in **Settings → Code Completion**, where words, snippets and Mermaid keywords each have a switch of their own. See the [Languages guide](/docs/languages/#autocomplete).
