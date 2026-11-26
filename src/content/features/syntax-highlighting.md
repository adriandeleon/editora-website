---
title: "Syntax highlighting"
group: "Code intelligence"
order: 1
beta: false
summary: "TextMate grammars for dozens of languages, from Java, Python, Rust and Go to Astro, SQL and Markdown, with bracket-pair colours and language detection from a shebang."
description: "Accurate highlighting for dozens of languages that never makes typing wait, with the language found from a shebang or modeline, and bracket-pair colours."
alt: "A Java class in the Editora Light theme, with keywords, types, strings and comments in their own colours"
---

Accurate colours make code faster to read and mistakes easier to spot. Editora highlights dozens of languages with TextMate grammars, the format VS Code and Sublime Text use, and keeps highlighting in step with every keystroke without slowing your typing.

## The right language for every file

- **Dozens of languages**: Java, Python, Rust, Go, C and C++, C#, Kotlin, Ruby, PHP, TypeScript, Elixir, Dart, SQL, Markdown and many more, plus config formats such as YAML, TOML, INI and Dockerfile.
- **Mixed files** highlight each part in its own language: Astro's frontmatter, HTML and CSS use the official Astro grammar.
- **A file with no telling name** takes its language from a shebang such as `#!/usr/bin/env bash` or a vim or Emacs modeline.
- **The status-bar language picker** sets it by hand, and the language server, Run and the tab's icon follow.
- **Familiar file names** open in the right language without an extension, such as `.bashrc`, `.envrc`, `requirements.txt` and Bazel's `BUILD` files.

## Correct and fast

- **Stateful**: grammar state carries from line to line, so block comments, heredocs and multi-line strings colour correctly.
- **Incremental**: an edit re-tokenizes only from the changed line, in the background, so a large file stays responsive.
- **Themed**: token colours follow the editor theme you pick.

## Bracket-pair colours

Each `()`, `[]` and `{}` is tinted by how deeply it is nested, so "how far in am I?" is readable without counting. The matching-bracket highlight answers "where does *this* one close?", and the two combine on the same character. Brackets inside strings and comments are skipped, so a stray `{` in a string does not shift the colour of every bracket below it.

**On by default.** Turn bracket-pair colours off in **Settings → Editor**. See the [Languages guide](/docs/languages/#syntax-highlighting).
