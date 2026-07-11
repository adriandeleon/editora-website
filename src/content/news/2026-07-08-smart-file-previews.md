---
title: "Instant previews for data, config, and diagram files"
description: "JSON/YAML/TOML/XML trees, OpenAPI docs, decoded systemd/ssh/Dockerfile/fstab/crontab/GitHub-Actions files, DOT and PlantUML diagrams, and PDF/SVG viewers."
date: 2026-07-08
---

A whole family of file types now gets the same 3-mode preview (Editor / Split /
Preview) as Markdown, turning raw data and config into something readable. All on
by default.

**Structured data.** `.json`, `.yaml`, and `.toml` render a collapsible,
type-colored data tree, and `.xml` renders a faithful DOM tree. A JSON or YAML
file recognized as an **OpenAPI 3 / Swagger 2** spec instead becomes browsable
API docs, with a tree ⇄ docs toggle.

**Config, decoded to plain English.** A **crontab** turns `30 2 * * 1-5` into "At
02:30, Monday through Friday" and lists the next fire times. **fstab**,
**systemd** units (with `OnCalendar=` next triggers), **SSH config** (a one-line
connection summary per host), **Dockerfile** (a per-stage digest), and **GitHub
Actions** workflows all get the same treatment, with malformed lines flagged.

**Diagrams as code.** Standalone Graphviz **DOT** (`.dot`/`.gv`) and **PlantUML**
(`.puml`) files preview via the `dot` / `plantuml` CLIs, with export to SVG / PNG
/ PDF.

**Viewers.** `.pdf` files open in a read-only page viewer, and `.svg` files stay
editable XML but gain a live rendered-image preview. Every preview exports to
PDF from its right-click menu. See the [previews guide](/docs/previews) and
[diagrams](/docs/diagrams).
