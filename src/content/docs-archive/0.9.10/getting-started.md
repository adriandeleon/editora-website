---
title: Getting Started
description: Install Editora, open your first file, and learn the keyboard-first essentials.
category: Getting started
order: 2
---

Install Editora, open a file, and learn the handful of keys that make it feel
fast.

## Install

Grab a native installer for your platform from the
[releases page](https://github.com/adriandeleon/Editora/releases) (or use the
OS-detected button on the [home page](/)):

- **macOS**: `.dmg` (Intel & Apple Silicon)
- **Windows**: `.msi` (x64)
- **Linux**: `.deb` (x64 & arm64)

Installers bundle their own Java runtime, so there's nothing else to install.
They're currently **unsigned**, so on first launch macOS Gatekeeper
(right-click → *Open*) or Windows SmartScreen will warn you once. See
[Troubleshooting](/docs/troubleshooting) if a launcher is blocked.

### Or run from source

Requires JDK 25+. The Maven wrapper is bundled.

```bash
git clone https://github.com/adriandeleon/Editora.git
cd Editora
./mvnw javafx:run           # run the app
./mvnw -Pfatjar package     # build a self-contained jar
java -jar target/Editora-*.jar
```

## First steps

Editora is keyboard-first. These get you moving (the chords shown are the
default Emacs keymap; pick another in Settings):

| Do this | Key |
| --- | --- |
| Open the command palette (everything is here) | `M-x` |
| Find or open a file by path | `C-x C-f` |
| Save | `C-x C-s` |
| Jump to a symbol in the file | `M-g i` |
| Switch between open files | `C-x b` |
| Find in the current file | `C-s` |
| Toggle a bookmark | `C-c m` |

Don't memorize these. Open the palette with `M-x`, type a few letters of what
you want, and run it. Each entry shows its key, so you pick up shortcuts as you
go. The full list lives on the [Commands](/commands) and
[Keybindings](/keybindings) pages.

A command that can't run right now stays listed and dimmed rather than
disappearing, so you can still discover it. That covers both reasons it might
not apply: its **feature is switched off** (a Git command with Git disabled), or
there's **nothing to act on** (a Markdown command outside a Markdown file, a
debugger step with nothing suspended, a Git command outside a repository). Hover
a dimmed row and it tells you which, and names the command that would fix it.
The toggle that switches a feature back on is never dimmed itself.

## Make it yours

- **Themes & fonts**: open Settings (the gear, or "Settings" in the palette) and
  go to Appearance. Six editor themes, five bundled fonts. See
  [Themes & fonts](/docs/themes-fonts).
- **Keymap**: prefer VS Code or IntelliJ chords? Switch live in
  **Settings → Keymaps**, or rebind individual commands. See
  [Keymaps & keybindings](/docs/keymaps).
- **Projects**: off by default. Enable them in Settings, then `C-x C-p` opens a
  folder as a workspace. See [Projects, windows & files](/docs/workspace).

## Turning on the bigger features

Several capabilities are off by default because they reach outside the editor or
add chrome. Enable the ones you want in Settings:

- **[Language servers](/docs/lsp)** for definitions, references, and diagnostics.
- **[Git](/docs/git)** for branch info, change bars, and commits.
- **[Debugging](/docs/run-debug)**, the **[HTTP client](/docs/http-client)**,
  **[Mermaid](/docs/markdown#mermaid-diagrams)**, **[HTML preview](/docs/markdown#html-live-preview)**,
  **[Personal notes](/docs/bookmarks-notes)**, **[Remote files](/docs/remote)**,
  and **[Plugins](/docs/plugins)**.
