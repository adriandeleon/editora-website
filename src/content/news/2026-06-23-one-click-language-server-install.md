---
title: "One-click install for every language server"
description: "All 21 language servers now install from inside Editora: a button per server, an in-editor banner, or the Install picker. No more hand-running scripts."
date: 2026-06-23
---

Setting up language support used to mean hunting down each server and running an
install script. Now Editora installs them for you, and the last batch just
landed, so **all 21 supported language servers** are covered.

There are three ways in:

- An **Install…** button next to each server in **Settings → Language Servers**
  (it flips to *Installed* once detected).
- An in-editor **banner** when you open a file whose server is missing (turn the
  nudge off with `view.toggleInstallPrompts`).
- The **Install: Language Server…** picker (`install.languageServer`).

Editora fetches each server through the right channel for it:

- **npm** packages (JSON/HTML/CSS, Bash, YAML, Dockerfile, TOML, TypeScript,
  Pyright), needing Node.
- The language's own **toolchain** (Go, Ruby, C#, Rust, PHP), used only when that
  toolchain is already on your `PATH`.
- Per-OS **binary** releases (clangd, lemminx, Kotlin, terraform-ls, Lua),
  downloaded and extracted into your config folder.

After installing, the server is auto-detected and activates without a restart.
Editora never installs the underlying runtimes (Node, Python, Go, …); if one is
missing it tells you which to install first. See the
[LSP guide](/docs/lsp#one-click-install).
