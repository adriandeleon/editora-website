---
title: "Language servers (LSP)"
group: "Code intelligence"
order: 2
beta: false
summary: "Go-to-definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics and completions via 29 language servers (Java, Astro, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled."
description: "Definitions, references, rename, code actions, hover, inlay hints and diagnostics from 29 language servers that Editora finds on your PATH or installs."
alt: "Quick fixes for an unresolved ArrayList in a Java file, above the Problems window listing two errors"
---

A language server is a language's own analysis engine, the one its community maintains. Editora works with 29 of them, so definitions, fixes, refactorings and errors come from the same tools you would run on the command line, in every language you use.

## Read code

- **Go to Definition** (`M-.` in the Emacs keymap, or Ctrl-click, Cmd-click on macOS) jumps to a symbol's definition, also from inside an opened JDK or library source tab.
- **Find References** (`M-S-/`) lists every use in the **References** tool window, each line with its syntax highlighting.
- **Go to implementation, type definition and declaration** appear wherever the server supports them.
- **Call and type hierarchy** opens as a tree in the **Hierarchy** tool window, each level fetched as you expand it.
- **Go to Symbol in Workspace** (`M-g s`) searches every symbol in the project and jumps to it.
- **Hover docs** (`C-c h`), **signature help** as you type `(` or `,`, occurrence highlighting and inlay hints explain the code in front of you.
- **Library sources** open with their real source when a Maven repository has it, or decompiled, with **Download Sources** in the tab.

## Change code

- **Code actions and quick fixes** (`C-c l a`, or `Ctrl+.` in the other keymaps, `Cmd+.` on macOS) open at the caret with the server's preferred fix first.
- **Organize imports, extract and inline** come from the same menu. For Python, Editora adds fixes Pyright lacks: import a missing name, remove an unused import, ignore on this line.
- **Rename Symbol** (`C-c l r`, `F2` in most other keymaps) renames across the workspace and lists every affected file first, so you can untick any.
- **Format Document** reformats the file through the server and keeps the file's own indentation. Python files use Ruff or Black, also with language servers off.
- **Java code generation** fills in toString, equals and hashCode, constructors, getters and setters, delegates and overrides, each from a checkbox picker.
- **Java refactorings** that need an answer ask for it: Move, Extract Interface and Change Signature.
- **Pasted Java code imports itself**, and a `;` typed mid-expression moves to the end of the statement.

## See problems

- **Inline diagnostics** underline errors, fade unused code and strike through deprecated calls, with marks on the scrollbar and minimap.
- **The Problems tool window** shows each problem's rule code, such as `[SC2086]`, and opens that rule's documentation. **LSP: Build Project (refresh all problems)** checks the whole project.
- **Code lenses** show reference and implementation counts after a declaration; click one to open what it counts.
- **Project tools join in**: a trusted JavaScript or TypeScript project's own ESLint, Prettier or Biome check and format its files, and shell scripts get ShellCheck and shfmt.

## Servers

Editora supports servers for Java, Astro, TypeScript and JavaScript, Python, Go, Rust, C and C++, C#, Ruby, PHP, Kotlin, Groovy, Clojure, Scala, HTML, CSS, XML, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, Dockerfile, Typst, PowerShell, Elixir and Dart.

- **Found, never bundled**: Editora uses the server on your PATH, and each server's card in **Settings → LSP** shows whether its program is found.
- **One-click install**: an **Install…** button per server, a banner when a file's server is missing, or **Install: Language Server…** fetches it, and it starts with no restart.
- **Python** uses Pyright with the project's own interpreter, shown in the status bar and changed with **Python: Select Interpreter**.
- **Remote folders** run the server on the host, found on the host's PATH or set per host.
- **Per project**: a [project](/features/projects/) can commit which server it uses and whether it runs.
- **Safe in a folder you have not trusted**: a server that would run the project's build waits until you choose **Trust Folder**.
- **Looks after itself**: a crashed server restarts, and an idle one shuts down a few minutes after its last file closes.

**On by default.** Turn it off in **Settings → LSP**; each server starts once it is installed. See the [Language servers guide](/docs/lsp/#servers-are-auto-detected-never-bundled).
