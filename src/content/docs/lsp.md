---
title: Language servers (LSP)
description: Enable language servers for go-to-definition, references, hover, diagnostics, and completion.
category: Code intelligence
order: 2
---

Editora speaks the **Language Server Protocol**, so with a server installed you
get real language smarts. LSP is **off by default**; turn it on in
**Settings → LSP**.

## What you get

| Feature | Command | Default key |
| --- | --- | --- |
| Go to definition | `lsp.gotoDefinition` | `M-.` |
| Find references | `lsp.findReferences` | `M-?` |
| Hover docs | `lsp.hover` | `C-c h` |
| Restart servers | `lsp.restartServers` | (palette) |

The first three are also in the editor right-click menu while a server is
active. Diagnostics appear as inline squiggles, in the **Problems** tool window
(`M-8`), and as marks on the minimap and scrollbar. Completion is merged into the
[autocomplete](/docs/languages#autocomplete) popup. The status bar shows an
**LSP: \<server\>** segment for managed files, with a loading bar while a server
starts.

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

What's deferred for now: formatting, rename, code actions and quick fixes,
document symbols from the server, incremental sync, and a dedicated references
tool window. Diagnostics for files that aren't open are dropped to keep the
Problems window focused on what you're editing.
