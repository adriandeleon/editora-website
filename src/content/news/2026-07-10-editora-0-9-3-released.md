---
title: "Editora 0.9.3 is here"
description: "A big release: Typst, diagram-as-code and smart previews for data and config files, build-tool integration for Maven/npm/Cargo/Go/Gradle, and more."
date: 2026-07-10
version: "0.9.3"
---

**Editora 0.9.3** is out (0.9.2 shipped a few days earlier). Grab a native
installer from the [releases page](https://github.com/adriandeleon/Editora/releases/latest),
now including a portable Linux `.tar.gz` alongside the `.deb`.

The headline additions:

- **[Typst](/docs/typst)**: a live 3-mode preview for `.typ` files via the
  `typst` CLI, a tinymist language server, Markdown-style editing, and export to
  PDF / PNG / SVG.
- **[Diagrams as code](/docs/diagrams)**: the same preview for Graphviz DOT and
  PlantUML.
- **[Smart file previews](/docs/previews)**: JSON / YAML / TOML / XML render as a
  data tree (OpenAPI specs as browsable API docs), and systemd, ssh config,
  Dockerfile, fstab, crontab, and GitHub Actions files decode into plain English.
  Plus a PDF page viewer and an SVG image preview. Every preview exports to PDF.
- **[Build tools](/docs/build-tools)**: Maven, npm, Cargo, Go, and Gradle each
  get a toolbar icon with an actions popup and a streaming console.

There's more in the full [What's New](/whats-new), and the story of the release
is in the [0.9.3 blog post](/blog/editora-0-9-3). Installers are still unsigned,
so on first launch you may need a right-click → *Open* (macOS) or "More info →
Run anyway" (Windows).
