---
title: "Editora 0.9.3: Typst, diagrams, and previews for everything"
description: "The 0.9.2 and 0.9.3 releases add Typst, diagram-as-code, smart previews for data and config files, and build-tool integration."
date: 2026-07-10
author: Adrian De Leon
tags: [release]
---

Two releases have landed since 0.9.1: **0.9.2** and **0.9.3**. The theme this
time is turning more of the files you already open into something you can read,
plus a couple of larger additions. Here's the tour.

## Typst

If you write [Typst](https://typst.app), Editora now treats `.typ` files as a
first-class document format. You get the same three-mode view as Markdown, Editor
/ Split / Preview, with the document rendered off-thread by the `typst` CLI as a
multi-page stack. The nice touch is that the preview doesn't flicker: the last
good render stays on screen while you type, and a compile error just adds a small
banner over the pages instead of blanking them.

Editing feels like Markdown. Enter continues a list, a format bar pops when you
select text, and there are commands to insert a table, image, or table of
contents. Code intelligence comes from the tinymist language server, and you can
export to PDF, PNG, or SVG. It's on by default and self-gating, so it does
nothing until you have `typst` installed, which you can do from an in-app
Install button.

## Previews for data, config, and diagrams

A big chunk of this release is a family of previews that share the Markdown
three-mode view. Open the file, flip to the preview, and see it rendered or
decoded:

- **Structured data**: JSON, YAML, and TOML render as a collapsible, type-colored
  tree, and XML renders a real DOM tree. A JSON or YAML file that's actually an
  OpenAPI or Swagger spec renders as browsable API docs instead, with method
  badges, params, responses, and schemas.
- **Config, in plain English**: a crontab line like `30 2 * * 1-5` becomes "At
  02:30, Monday through Friday" with the next fire times; fstab, systemd units,
  SSH config, Dockerfiles, and GitHub Actions workflows all get the same
  decoding, and malformed lines are flagged.
- **Diagrams as code**: Graphviz DOT and PlantUML files preview through the `dot`
  and `plantuml` CLIs, with export to SVG, PNG, or PDF.
- **Viewers**: PDFs open in a read-only page viewer, and SVGs stay editable XML
  but gain a live rendered image.

Every one of these can export to PDF from its right-click menu.

## Build tools

Editora now integrates with **Maven, npm, Cargo, Go, and Gradle**. Each detected
tool gets a toolbar icon, shown only when its marker file is present, that opens
an actions popup and streams the chosen task to its own console. Maven reads your
pom's lifecycle, profiles, and plugin goals; npm lists your `scripts`; Cargo,
Go, and Gradle expose their standard tasks. Discovery parses the marker file
directly, so it's instant and offline, and Maven and Gradle prefer your project's
own wrapper.

## AI, refined

The AI features from 0.9.1 got some polish: a dedicated AI group in Settings with
a master on/off switch, the ability to resume an agent session and pick which ACP
client to use, and a loading indicator while an explanation streams into the
Markdown preview.

## Get it

Download an installer from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). Linux
now has a portable `.tar.gz` in addition to the `.deb`. Installers are unsigned
for now, so macOS wants a right-click → *Open* and Windows wants "More info → Run
anyway" the first time.

The complete list is on the [What's New](/whats-new) page. Editora is built in the
open, and with the help of AI coding tools.
