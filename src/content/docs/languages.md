---
title: Languages & highlighting
description: TextMate grammars, the supported languages, autocomplete, and spell checking.
category: Code intelligence
order: 1
---

## Syntax highlighting

Highlighting uses **TextMate grammars** through tm4e. When you open a file,
Editora maps its extension (or, for a `Dockerfile`, its name) to a bundled
grammar and tokenizes the document line by line. Tokenization is **stateful**,
so block comments and heredocs highlight correctly across lines, and
**incremental**, so an edit re-tokenizes only from the changed line, off the UI
thread. Token colors come from the active editor theme.

Files without a bundled grammar are left unstyled rather than guessed at.

### Supported languages

Grammars ship for Java, XML, shell, PowerShell, DOS batch, Python, Groovy,
Kotlin, Ruby, C, C++, Rust, Go, C#, Markdown, JSON, CSS, HTML, YAML, INI, SQL,
TypeScript, JavaScript, PHP, Lua, Dockerfile, Terraform, TOML, Mermaid, and the
HTTP request format. The TypeScript grammar also covers plain JavaScript.

Folding, comment syntax, and auto-indent rules are wired per language alongside
the grammar. Many extension-less dotfiles and named config files (e.g.
`.editorconfig`, `.gitignore` and other `.*ignore` files) are matched by name, so
they get highlighting too (and the matching language server when enabled).

## Autocomplete

Completion appears as you type, debounced and kept off the hot path. It won't
trigger below a two-character prefix.

- **Code buffers** get a popup that merges sources and ranks them: language-server
  results (when [LSP](/docs/lsp) is on), then snippets, then nothing buried.
  Press **Enter** or **Tab** to accept. Accepting a snippet starts a tab-stop
  session; accepting an LSP item can auto-add its import.
- **Prose buffers** (plain text and Markdown) get inline **ghost text**, a muted
  suffix you accept with **Tab**.

Trigger manually with `C-M-i` or `M-/`. Completion also fires on a language
server's advertised trigger characters, so `<` in HTML or `:` in CSS pops the
popup. In the popup, **Up** / **Down** and `C-n` / `C-p` move the selection, and
**Esc** or `C-g` dismisses it.

The code popup is IntelliJ-style: per-kind icons (class, method, keyword,
snippet, and so on), the matched characters highlighted, deprecated items struck
through, and a **documentation popup** beside the list. The docs popup shows
automatically and toggles with `C-q` (*Edit: Toggle Completion Documentation*).

Toggles live in **Settings → Editor**: a master switch plus per-source switches
for words (prose), snippets, and Mermaid keywords. Palette equivalents are
`view.toggleAutocomplete` and the per-source variants.

## Spell checking

Misspelled words get a red wavy underline. Right-click for **suggestions** (pick
one to replace), **Add to Dictionary**, or **Ignore**. In source files only
comments and string literals are checked, so identifiers aren't flagged; plain
text and Markdown are checked in full.

It uses Apache Lucene's pure-Java **Hunspell** engine, with no native
dependency. English (en_US, en_GB), Spanish, and French ship in the app. Pick a
dictionary per file with **Spell Check: Set Language…**, set a default in
Settings, or toggle checking with `view.toggleSpellCheck`. Words you add live in
`dictionary.txt` in your config folder.

A bundled **technical-terms dictionary** (`config`, `async`, `middleware`,
`kubernetes`, and the like) keeps code-adjacent words from being flagged; toggle
it in Settings or with `view.toggleTechnicalDictionary`.
