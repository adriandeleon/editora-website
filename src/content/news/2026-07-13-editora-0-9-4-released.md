---
title: "Editora 0.9.4 is here"
description: "Expert mode, a lighter focus mode than Zen, plus build-tool tasks tool windows, folder-targeted file templates, and Project-tree improvements."
date: 2026-07-13
version: "0.9.4"
---

**Editora 0.9.4** is out. Grab a native installer from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

The headline is **Expert mode**, a lighter focus mode than Zen. It strips the
window chrome (toolbar, tab bar, breadcrumb, tool stripes) but **keeps the full
editor view**, line numbers, status bar, minimap, column ruler, and current-line
highlight, so you get a focused surface that still shows where you are. Toggle it
with `C-c C-e`, the palette, Settings → Interface → Modes, the floating **"E"**
button, or the `--expert` flag. See [Focus modes](/docs/workspace#focus-modes).

Also in this release:

- **Build tools** (Maven, npm, Cargo, Go, Gradle) now have IntelliJ-style
  **tasks tool windows**, a browsable tree of goals/scripts/targets with a mini
  toolbar and a streaming console, instead of main-toolbar icons.
  See [Build tools](/docs/build-tools).
- **File Templates** can write into a chosen folder (a new Folder field in the
  wizard), pre-filled when you invoke *New From Template…* from a project folder.
- The **Project tool window** gained *New Folder…* and a right-click menu on the
  project root.

Plus fixes: the Cargo/Go/Gradle tool-window icons no longer render blank, and
installing the Typst language server (tinymist) from the banner now actually
activates it. The full list is on the [What's New](/whats-new) page, and there's
a [0.9.4 blog post](/blog/editora-0-9-4).
