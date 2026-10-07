---
title: "Syntax highlighting"
group: "Code intelligence"
order: 1
beta: false
summary: "TextMate grammars (via tm4e) for Java, Astro, Python, Rust, Go, Kotlin, C/C++, C#, Ruby, SQL, Markdown, and more."
---

Highlighting uses **TextMate grammars** (via tm4e) for Java, Astro, XML, shell, PowerShell, DOS batch, Python, Groovy, Kotlin, Ruby, C, C++, Rust, Go, C#, Markdown, JSON, CSS, HTML, YAML, INI, and SQL, plus TypeScript/JavaScript, PHP, Lua, Dockerfile, Terraform, TOML, and more added alongside their language servers. Astro uses the official grammar for mixed frontmatter, HTML, and CSS.

Tokenization is **stateful** (it carries grammar state across lines, so block comments and heredocs highlight correctly) and **incremental**: an edit re-tokenizes only from the changed line, off the UI thread. Token colors are themed per editor theme.

## Bracket-pair colorization

Each `()`, `[]` and `{}` is tinted by how deeply it is nested, so "how far in am I?" is readable without counting. The matching-bracket highlight answers a different question, "where does *this* one close?", and the two combine on the same character.

Brackets inside strings and comments are skipped, because a stray `{` in a string would otherwise shift the colour of every bracket below it. The depth pass runs as part of tokenizing, so it costs no extra repaint. On by default; Settings → Editor turns it off.
