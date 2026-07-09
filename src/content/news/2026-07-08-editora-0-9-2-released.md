---
title: "Editora 0.9.2 released"
description: "Maven support, a dedicated AI settings group with a master switch, clickable Markdown-preview links, and a critical Windows launch fix."
date: 2026-07-08
version: "0.9.2"
---

**Editora 0.9.2** is out. Grab a native installer for your platform from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest): `.dmg`
for macOS (Intel & Apple Silicon), `.msi` for Windows (x64), and `.deb` for Linux
(x64 & arm64). Each bundles its own Java runtime.

New in this release:

- **[Maven support](/docs/maven)**: a toolbar button (shown when a `pom.xml` is
  detected) opens a searchable popup that reads the pom directly, listing
  lifecycle phases, profiles, and each plugin's bound goals, and streams runs to
  a Maven console. It prefers your `./mvnw` wrapper and needs no external
  effective-pom step. On by default.
- **A dedicated AI settings group with a master switch.** The AI Agent and AI
  Actions pages now live under one **AI** group with a single Enable/Disable AI
  toggle (off by default). Turning it off disables every AI feature at once.
- **Clickable links in the Markdown preview**, opening in your system browser.
- **A loading spinner** while an AI explanation streams into the preview.

This release also fixes a **critical Windows launch failure**: since the AOT
cache landed, every Windows install failed to start with "Failed to find JVM in
...\runtime directory", because the build stripped the runtime's `bin/` (which,
on Windows only, holds the JVM itself). The strip is now guarded to non-Windows.

See the full [What's New](/whats-new) for the complete list, or the
[0.9.2 release notes](/blog/editora-0-9-2).
