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

Grammars ship for Java, Astro, XML, shell, PowerShell, DOS batch, Python, Groovy,
Kotlin, Ruby, C, C++, Rust, Go, C#, Markdown, JSON, CSS, HTML, YAML, INI, SQL,
TypeScript, JavaScript, PHP, Lua, Dockerfile, Terraform, TOML, Mermaid, and the
HTTP request format. The TypeScript grammar also covers plain JavaScript.

Astro files use the official Astro TextMate grammar, including mixed
frontmatter, HTML, and CSS regions. The same language integration supplies
Structure symbols and can connect to `astro-ls` for code intelligence.

Folding, comment syntax, and auto-indent rules are wired per language alongside
the grammar. Many extension-less dotfiles and named config files (e.g.
`.editorconfig`, `.gitignore` and other `.*ignore` files) are matched by name, so
they get highlighting too (and the matching language server when enabled).

## Autocomplete

Completion appears as you type, debounced and kept off the hot path. Java member
completion triggers immediately after `.`, while identifier completion begins
after a short pause. Other word completion won't trigger below a two-character
prefix.

- **Code buffers** get a popup that merges sources and ranks them: language-server
  results (when [LSP](/docs/lsp) is on), then snippets.
  Press **Enter** or **Tab** to accept. Complete result lists filter locally and
  obsolete server requests are cancelled as you keep typing. Accepting a snippet
  starts a tab-stop session; accepting a Java LSP item can safely add its import
  after continued typing, grouped with the completion as one undo/redo action.
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

Toggles live in **Settings → Code Completion**: a master switch plus per-source switches
for words (prose), snippets, and Mermaid keywords. Palette equivalents are
`view.toggleAutocomplete` and the per-source variants.

## Spell checking

Misspelled words get a red wavy underline. Right-click for **suggestions** (pick
one to replace), **Add to Dictionary**, or **Ignore**. **Ignore** stops flagging
the word in every open file until Editora is closed; **Add to Dictionary** keeps
it for good.

What is checked depends on the file type:

- **Plain text and Markdown** are checked in full, apart from inline code, links,
  and Markdown fenced code blocks.
- **HTML and Typst** have the document's text checked, plus comments. Tags,
  attributes, `<script>`, `<style>` and `<pre>` blocks, Typst math and Typst
  lines that open with a `#` instruction are left alone.
- **Source code** has only comments and string literals checked, so identifiers
  aren't flagged.
- **Data and configuration formats** are off by default: JSON, YAML, TOML, XML,
  CSV, INI, properties, `.env`, Git and SSH configuration, system configuration
  files, logs, and diffs.

**Settings → Spell Check → File Types** has a tick box per file type, and **Spell
Check: Toggle for This File Type** (`spell.toggleForLanguage`) flips the active
file's type. `view.toggleSpellCheck` turns checking on or off everywhere.

### Spell-check commands

Everything on the right-click menu is also a command, so you can work through a
file from the keyboard.

| Action | Command | Emacs key |
| --- | --- | --- |
| Next misspelling | `spell.nextMisspelling` | (palette) |
| Previous misspelling | `spell.previousMisspelling` | (palette) |
| Correct the word at the caret | `spell.correctWord` | `M-S-4` (`M-$`) |
| Add the word at the caret to the dictionary | `spell.addWord` | (palette) |
| Ignore the word at the caret | `spell.ignoreWord` | (palette) |
| Set the file's dictionary | `spell.setLanguage` | (palette) |
| Reload the personal dictionary | `spell.reloadDictionary` | (palette) |

**Correct Word at Caret** opens a list of suggestions to filter and pick from.
The other keymaps bind the navigation keys: in **CUA**, `F7` and `Shift+F7` go to
the next and previous misspelling; in **Sublime Text**, `F6` toggles spell check
and `Ctrl+F6` / `Ctrl+Shift+F6` go to the next and previous misspelling.

### Dictionaries

Spell check uses Apache Lucene's pure-Java **Hunspell** engine, with no native
dependency. English (US and UK), Spanish (Spain and Mexico), and French ship in
the app. The default dictionary is set in **Settings → Spell Check**. The status
bar shows the dictionary the active file is checked with; click it, or run
**Spell Check: Set Language…**, to give the file another one. The picker lists
the languages by name, marks the current one, and has an entry that returns the
file to the default.

Words you add live in `dictionary.txt` in your config folder. **Settings → Spell
Check** has an editor for the list (`spell.manageDictionary`) and a switch that
turns the personal dictionary off. After editing `dictionary.txt` outside
Settings, run **Spell Check: Reload Personal Dictionary**.

A bundled **technical-terms dictionary** (`config`, `async`, `middleware`,
`kubernetes`, and the like) keeps code-adjacent words from being flagged in any
language; toggle it in Settings or with `view.toggleTechnicalDictionary`.
