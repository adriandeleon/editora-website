---
title: "Editora 0.9.1 is here"
description: "The first tagged release, with native installers for macOS, Windows, and Linux, and the whole feature set built so far, including new AI assistance."
date: 2026-07-06
version: "0.9.1"
---

**Editora 0.9.1** is the first tagged release. Grab a native installer for your
platform from the [releases page](https://github.com/adriandeleon/Editora/releases/latest):
`.dmg` for macOS (Intel & Apple Silicon), `.msi` for Windows (x64), and `.deb`
for Linux (x64 & arm64). Each bundles its own Java runtime.

It ships everything built so far: a keyboard-driven, command-first editor with
your choice of keymap (Emacs, CUA, Sublime Text, VS Code, IntelliJ), TextMate
syntax highlighting, language servers, debugging, Git, a Markdown preview with
Mermaid and math, CSV tooling, snippets and templates, macros, and much more. See
the full [What's New](/whats-new) for the complete list.

New in this release: **AI assistance**. One-shot AI actions (explain, rewrite,
generate a commit message, inline completion) and an embedded coding agent over
the Agent Client Protocol, using the Anthropic API or a local model. It's off by
default; read the [AI guide](/docs/ai).

Installers are currently unsigned, so on first launch you may need to right-click
→ *Open* (macOS) or "More info → Run anyway" (Windows). Read the
[0.9.1 release notes](/blog/editora-0-9-1) for the story behind the version
number.
