---
title: "Language servers (LSP)"
group: "Code intelligence"
order: 2
beta: true
summary: "Go-to-definition, find references, hover docs, diagnostics, and completions via 20+ language servers (Java, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled."
---

Editora speaks the **Language Server Protocol**, so you get real language smarts:

- **Go to definition**: `M-.`
- **Find references**: `M-?`
- **Hover docs**: `C-c h`
- **Format Document**: reformat the whole file via the server (palette or right-click)
- Inline **diagnostics** (with a Problems tool window and minimap/scrollbar marks) and **completions**

Over 20 servers are supported, Java, TypeScript/JavaScript, Python, Go, Rust, C/C++, C#, Ruby, PHP, Kotlin, HTML, CSS, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, and more. Servers are **auto-detected on your PATH, never bundled** (and configurable in Settings → LSP).

**One-click install** covers all 21 servers: an **Install…** button per server in Settings, an in-editor banner when a file's server is missing, and the **Install: Language Server…** picker. Editora fetches each via the right channel (npm, the language's own toolchain, or a per-OS binary release), and the server activates without a restart.

Off by default. Enable it under Settings → LSP.
