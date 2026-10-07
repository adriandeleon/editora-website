---
title: Getting Started
description: Install Editora, open your first file, and learn the keyboard-first essentials.
category: Getting started
order: 2
---

Install Editora, open a folder or a file, and learn the handful of keys that
make it feel fast.

## Install

Grab the package for your platform from the
[releases page](https://github.com/adriandeleon/Editora/releases) (or use the
OS-detected button on the [home page](/)). The native packages bundle their own
Java runtime, so there's nothing else to install.

### macOS

Download the `.dmg` for your Mac (`macos-arm64` for Apple Silicon, `macos-x64`
for Intel), open it, and drag Editora to Applications.

### Windows

Run the `.msi` (x64). The installer lets you choose the folder and adds a Start
Menu group and a desktop shortcut.

### Linux

Pick the format that suits your system. `.deb`, `.rpm`, and `.AppImage` are x64
only; the `.tar.gz` comes for x64 and arm64.

```bash
# Debian / Ubuntu: adds an `editora` command and a menu entry
sudo apt install ./Editora-<version>-linux-x64.deb

# Fedora / RHEL / openSUSE: adds a menu entry; the launcher is /opt/editora/bin/Editora
sudo dnf install ./Editora-<version>-linux-x64.rpm

# AppImage: no install step
chmod +x Editora-<version>-linux-x64.AppImage
./Editora-<version>-linux-x64.AppImage

# Tarball: adds an `editora` command and a menu entry
tar xzf Editora-<version>-linux-x64.tar.gz && cd editora-x86_64
./install.sh          # per-user  -> ~/.local/editora  (+ ~/.local/bin/editora)
sudo ./install.sh     # system    -> /opt/editora       (+ /usr/local/bin/editora)
./install.sh --uninstall   # remove it again
```

The `.deb` also makes *Editora Expert Mode* the system-wide default for text and
source files; your own per-user choice still wins, and the previous defaults are
put back when you uninstall the package. See the
[command-line reference](/docs/cli) for where the launcher lives in each
package.

### First launch

The installers are currently **unsigned**, so macOS Gatekeeper and Windows
SmartScreen stop the first launch until you allow it. The steps are in
[Troubleshooting](/docs/troubleshooting#a-launcher-is-blocked-on-first-run).

### Or run from source

Requires JDK 25+. The Maven wrapper is bundled (use `mvnw.cmd` on Windows).

```bash
git clone https://github.com/adriandeleon/Editora.git
cd Editora
./mvnw javafx:run           # run the app
./mvnw -Pfatjar package     # build a self-contained jar
java -jar target/Editora-*.jar
```

## Open a folder or a file

A fresh install opens on the **Welcome** page. Its buttons are the quickest way
in:

- **Open Folder as a Project…** opens a folder with its file tree in the
  Project tool window. This is the usual way to work on a codebase; each project
  gets its own window and remembers its open files.
- **Open File…** opens a single file.
- **New File** starts an empty buffer, and **Clone Git Repository…** fetches a
  repository and opens it.

Every one of these is also a command in the palette. Reopen the page any time
with *View: Welcome Page*.

## Choose your keymap

Editora ships five keymaps: **Emacs** (the default), **CUA**, **Sublime Text**,
**VS Code**, and **IntelliJ IDEA**. If Emacs chords aren't your muscle memory,
switch before you learn anything else: open Settings (the gear in the toolbar,
or "Settings" in the palette), go to **Keymaps**, and pick one. The change is
live, and every shortcut shown in menus, tooltips, and the palette follows it.
See [Keymaps & keybindings](/docs/keymaps).

### Reading the key notation

These docs write the Emacs keymap's chords the Emacs way:

- `C-` is **Ctrl**, `M-` is **Alt** (**Option** on macOS), and `S-` is
  **Shift**. So `M-x` is Alt+X and `C-S-f` is Ctrl+Shift+F.
- A space separates the steps of a sequence: `C-x C-f` is Ctrl+X, then Ctrl+F.
- `C-g` cancels a half-typed sequence.

## First steps

Editora is keyboard-first. These get you moving:

| Do this | Emacs keymap | VS Code keymap |
| --- | --- | --- |
| Open the command palette (everything is here) | `M-x` | `Ctrl+Shift+P` or `F1` |
| Find or open a file by path | `C-x C-f` | `Ctrl+P` |
| Save | `C-x C-s` | `Ctrl+S` |
| Jump to a symbol in the file | `M-g i` | `Ctrl+Shift+O` |
| Switch between open files | `C-x b` | (palette) |
| Find in the current file | `C-s` | `Ctrl+F` |
| Search Everywhere | `M-S-x` | `Ctrl+Shift+E` |
| Toggle a bookmark | `C-c m` | (palette) |
| Open Settings | (palette) | `Ctrl+,` |

On macOS the VS Code keymap uses `Cmd` where the table shows `Ctrl`. The CUA,
Sublime Text, and IntelliJ IDEA keymaps differ in places; the
[Keybindings](/keybindings) page has a tab for each.

Don't memorize these. Open the palette, type a few letters of what you want,
and run it. Each entry shows its key, so you pick up shortcuts as you go. The
full list lives on the [Commands](/commands) and [Keybindings](/keybindings)
pages.

If you don't yet know *which* picker holds the command, file or symbol you
want, run **Search Everywhere** and type its name. See
[Navigation & search](/docs/navigation#search-everywhere).

If you'd rather browse than recall, there is also a **menu bar** (File / Edit /
Find / View / Navigate / Code / Run / VCS / Tools / Window / Help) built over
the same commands, with each item showing its current keybinding. Its **Help**
menu links to the documentation for the version you are running.

A command that can't run right now stays listed and dimmed rather than
disappearing, whether its feature is switched off or there is nothing for it to
act on. Hover a dimmed row and it tells you which, and names the command that
would fix it.

## Make it yours

- **Themes & fonts**: open Settings and go to Appearance. Editora Light and Dark
  are the default pair, with 26 more themes and five bundled fonts. See
  [Themes & fonts](/docs/themes-fonts).
- **Keymap**: switch keymaps live in **Settings → Keymaps**, or rebind
  individual commands. See [Keymaps & keybindings](/docs/keymaps).
- **Projects**: on by default. *Project: Open Folder…* (`C-x C-p` in the Emacs
  keymap) opens a folder as a workspace, each in its own window, and **New
  Project From Template** scaffolds one from scratch. See
  [Projects, windows & files](/docs/workspace).

## Turning on the bigger features

**Off until you turn them on** are the features that run code or reach the
network. Enable the ones you want in Settings:

- **[Language servers](/docs/lsp)** for definitions, references, and diagnostics.
- **[Debugging](/docs/run-debug)**.
- **[Plugins](/docs/plugins)**.
- **[AI assistance](/docs/ai)** and the **[MCP server](/docs/mcp)**.

**On, but dormant until their tool is installed** is the larger group:
**[Git](/docs/git)**, **[GitHub](/docs/github)**,
**[Mermaid](/docs/markdown#mermaid-diagrams)**,
**[diagrams](/docs/diagrams)**, and **[Typst](/docs/typst)** are enabled by
default, and their buttons and panels appear once Editora finds the command they
need (`git`, `gh`, `mmdc`, and so on). Run *View: Doctor* to see what was found;
see [Troubleshooting](/docs/troubleshooting#doctor).

Projects, the **[HTTP client](/docs/http-client)**,
**[HTML preview](/docs/markdown#html-live-preview)**, and
**[Personal notes](/docs/bookmarks-notes)** are on from the start and need
nothing else.
