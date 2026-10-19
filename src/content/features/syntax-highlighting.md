---
title: "Syntax highlighting"
group: "Code intelligence"
order: 1
beta: false
summary: "TextMate grammars (via tm4e) for Java, Astro, Python, Rust, Go, Kotlin, C/C++, C#, Ruby, SQL, Markdown, and more."
---

Highlighting uses **TextMate grammars** (via tm4e) for Java, Astro, XML, shell, PowerShell, DOS batch, Python, Groovy, Kotlin, Scala, Clojure, Ruby, C, C++, Rust, Go, C#, Markdown, JSON, CSS, HTML, YAML, INI, and SQL, plus TypeScript/JavaScript, PHP, Lua, Dockerfile, Terraform, TOML, and more added alongside their language servers. Astro uses the official grammar for mixed frontmatter, HTML, and CSS.

A file whose name gives no language takes it from a shebang (`#!/usr/bin/env bash`, `#!/usr/bin/env -S uv run --script` for Python, or `java --source 25` for Java) or a vim or emacs modeline (`# vim: ft=sh`, `-*- mode: sh -*-`), and the status-bar language picker sets it by hand; the language server, Run and the tab's file-type icon follow either way. Shell scripts are also recognised by their startup-file names (`.bashrc`, `.zshrc`, direnv's `.envrc`, `/etc/profile`, …), and a command's bare arguments are plain text, so commands, variables and strings stand out. Python's grammar covers f-strings, t-strings, `match`/`case`, type parameters (`def f[T]`) and `type` aliases, and a Python project's other files open with a format: pip `requirements.txt` files get their own highlighting, `Pipfile` and the Poetry, uv and PDM lock files open as TOML, and `Pipfile.lock` and `.ipynb` notebooks as JSON. See [Languages & highlighting](/docs/languages).

Tokenization is **stateful** (it carries grammar state across lines, so block comments and heredocs highlight correctly) and **incremental**: an edit re-tokenizes only from the changed line, off the UI thread. Token colors are themed per editor theme.

## Bracket-pair colorization

Each `()`, `[]` and `{}` is tinted by how deeply it is nested, so "how far in am I?" is readable without counting. The matching-bracket highlight answers a different question, "where does *this* one close?", and the two combine on the same character.

Brackets inside strings and comments are skipped, as is the `)` that ends a shell `case` pattern, because a stray `{` in a string would otherwise shift the colour of every bracket below it. The depth pass runs as part of tokenizing, so it costs no extra repaint. On by default; Settings → Editor turns it off.
