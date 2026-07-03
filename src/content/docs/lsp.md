---
title: Language servers (LSP)
description: Enable language servers for go-to-definition, references, hover, diagnostics, and completion.
category: Code intelligence
order: 2
beta: true
---

Editora speaks the **Language Server Protocol**, so with a server installed you
get real language smarts. LSP is **off by default**; turn it on in
**Settings → LSP**.

## What you get

| Feature | Command | Default key |
| --- | --- | --- |
| Go to definition | `lsp.gotoDefinition` | `M-.` |
| Find references | `lsp.findReferences` | `M-?` |
| Go to Symbol in Workspace | `lsp.gotoSymbol` | (palette) |
| Hover docs | `lsp.hover` | `C-c h` |
| Format Document | `lsp.formatDocument` | (palette) |
| Restart servers | `lsp.restartServers` | (palette) |

The goto/references/hover commands are also in the editor right-click menu while
a server is active, and you can **Ctrl/Cmd-click** a symbol to jump to its
definition. **Find references** lists results in a browsable **References** tool
window (`tool.references`), grouped by file with a line and preview. **Go to
Symbol in Workspace** opens a live search over every symbol in the project.
**Format Document** reformats the whole file through the server when it
advertises formatting (undoable; palette or the right-click menu). Diagnostics
appear as inline squiggles, in the **Problems** tool window (`M-8`), and as marks
on the minimap and scrollbar. Completion is merged into the
[autocomplete](/docs/languages#autocomplete) popup. The status bar shows an
**LSP: \<server\>** segment for managed files, with a loading bar while a server
starts.

Two more things ride on a server when it's active. The **Structure** tool window
builds its outline from the server's document symbols, so you get a precise
hierarchy with real kinds, per-kind icons, and method signatures (it falls back
to a fold/TextMate heuristic for non-LSP files). And in a language whose server
supports range formatting, **Tab** snaps the current line's indentation to the
server's convention.

## Servers are auto-detected, never bundled

Editora doesn't ship language servers. It looks for each one on your `PATH` (a
Settings field can override the command per server) and uses it if present.
Around twenty servers are supported:

- **Java** (`jdtls`), **TypeScript / JavaScript** (`typescript-language-server`),
  **Python** (`pyright-langserver`), **Go** (`gopls`), **Rust**
  (`rust-analyzer`), **C / C++** (`clangd`), **C#** (`csharp-ls`)
- **Ruby** (`ruby-lsp`), **PHP** (`phpactor`), **Kotlin**
  (`kotlin-language-server`), **Lua** (`lua-language-server`)
- **HTML**, **CSS**, **JSON**, **YAML**, **XML** (`lemminx`), **Bash**
  (`bash-language-server`), **Dockerfile**, **SQL** (`sqls`), **Terraform**
  (`terraform-ls`), **TOML** (`taplo`)

Each server has its own enable checkbox and a live found/not-found status on the
**Settings → LSP** page, plus a per-server command field with a Browse button.

## One-click install

If a server isn't installed, Editora can fetch it for you. **All 21 servers** are
covered through three channels, picked per server:

- **npm** packages (JSON/HTML/CSS, Bash, YAML, Dockerfile, TOML, TypeScript,
  Python/Pyright), needing Node.
- **Toolchain** installs that use the language's own package manager (Go, Ruby,
  C#, Rust, PHP), which run only when that toolchain is already on your `PATH`.
- **Binary** releases downloaded per OS/architecture (clangd, lemminx, Kotlin,
  terraform-ls, Lua), extracted into your config folder.

Three entry points: an **Install…** button per server in **Settings → LSP**, an
in-editor **banner** when you open a file whose server is missing (turn the nudge
off with `view.toggleInstallPrompts`), and the **Install: Language Server…**
picker (`install.languageServer`). After installing, the server is auto-detected
and activates without a restart. Editora never installs the underlying runtimes
(Node, Python, Go, …); if one is missing it tells you which to install first.

## Workspace roots

A server runs once per workspace root. The root is the active project, else the
nearest build marker (`pom.xml`, `package.json`, `go.mod`, `Cargo.toml`, and so
on), else the file's directory. Files of the same language in one project share a
single server.

## Finding the server's binary

A GUI-launched app inherits a stripped `PATH` that often misses Homebrew, npm,
and version-manager directories. Editora rebuilds the `PATH` from your login
shell plus the usual install locations before launching a server, which recovers
things like nvm's per-version `node` bin. If a server still isn't found, set its
absolute path in Settings. See [Troubleshooting](/docs/troubleshooting#external-tools-arent-found)
for details.

## Notes and limits

What's deferred for now: format-on-save, rename, code actions and quick fixes,
document symbols from the server, incremental sync, and a dedicated references
tool window. (Whole-file Format Document is supported.) Diagnostics for files
that aren't open are dropped to keep the Problems window focused on what you're
editing.
