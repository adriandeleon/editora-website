---
title: "Spell checking"
group: "Editing"
order: 5
beta: false
summary: "Red wavy underlines in prose, comments and strings, never in your identifiers, with suggestions a right-click away. English, Spanish and French."
description: "Spell checking for prose, comments and strings in English, Spanish and French, with suggestions, a personal dictionary and a technical dictionary."
alt: "A Markdown paragraph with four misspelled words underlined in red"
---

Typos get a red wavy underline wherever people will read the text, and your code's identifiers are left alone, so the underlines that remain are worth fixing.

## What is checked

- **Prose** in plain text, Markdown, HTML and Typst is checked in full, apart from code, link targets and markup.
- **Source code** has only its comments and string literals checked.
- **Data and configuration formats** such as JSON, YAML and TOML are off by default. Tick any file type on or off in **Settings → Spell Check → File Types**.

## Fix a word

- **Right-click** a flagged word for suggestions, **Add to Dictionary** or **Ignore**.
- **Work from the keyboard** with **Spell Check: Next Misspelling** and **Spell Check: Correct Word at Caret…** (`M-S-4` in the Emacs keymap). The add and ignore commands are in the palette too.

## Dictionaries

- **English** (US and UK), **Spanish** and **French** ship with the app, with no native library to install. Pick one per file with **Spell Check: Set Language…**, or set the default in **Settings → Spell Check**.
- **Your words** live in `dictionary.txt`, kept across sessions.
- **A technical dictionary** of over 2,000 terms, such as `async`, `kubernetes` and `worktree`, keeps programming words from being flagged, plurals and possessives included.

**On by default.** Turn it off in **Settings → Spell Check**. See the [Spell checking guide](/docs/languages/#spell-checking).
