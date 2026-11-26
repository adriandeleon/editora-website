---
title: "Snippets"
group: "Editing"
order: 1
beta: false
summary: "Type a short prefix, press <kbd>Tab</kbd>, and fill in the blanks. VS Code and TextMate style templates, bundled for 35 languages."
description: "VS Code and TextMate style snippets with tab stops, mirrors, choices and variables, bundled for 35 languages, plus your own in Settings."
alt: "The Insert Snippet picker listing Java snippets such as main, class, record, enum and method"
---

Boilerplate takes a few keystrokes: type a short trigger such as `fori` or `main`, press **Tab**, and fill in the blanks the snippet leaves for you.

## Use a snippet

- **Expand** a trigger with **Tab**, or pick one by name from **Snippet: Insert…** (`C-c i` in the Emacs keymap, `Ctrl+J` in the IntelliJ keymap, `Cmd+J` on macOS).
- **Fill the fields.** Placeholders are selected so you can type over them, **Tab** and **Shift+Tab** move between fields, and repeated fields update together.
- **Choose from a list** where a field offers choices, and land on the final caret position when you finish.
- **Know where you are.** Fields are outlined, and the status bar shows "Snippet 2/3".

## Expands only where you mean it

- **Not in comments or strings**, and not on a member name after a `.`, so Tab indents or completes as usual there.
- **Not mid-sentence in prose.** In Markdown and plain text a word-like trigger expands only alone on its line; the completion popup still offers it.
- **Ahead of other suggestions.** A trigger typed in full wins over a dictionary or AI inline suggestion, and leads the language server's completion list.

## What ships

Snippets ship for 35 languages, plus a set for every file. They use each language's idioms: shell snippets write `[ ]` tests under `sh` or `dash` and `[[ ]]` under bash, and the Java set covers declarations named after the file, loggers, streams, text blocks and JUnit 5 tests.

## Your own snippets

- **Add or change snippets** for any language in **Settings → Snippets**, or in `~/.editora/snippets/<language>.json`. Your snippet replaces a bundled one with the same trigger.
- **Disable** a bundled snippet you never want.

See the [Snippets guide](/docs/snippets-templates/#snippets).
