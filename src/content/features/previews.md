---
title: "Smart file previews"
group: "Docs & diagrams"
order: 7
beta: false
summary: "Open a data or config file and read it at a glance: JSON, YAML, TOML and XML as trees, Maven poms as summaries, OpenAPI as API docs, Markwhen as timelines, and crontab, systemd or Dockerfile in plain English."
description: "Readable previews of JSON, YAML, TOML and XML trees, Maven poms, OpenAPI docs, Markwhen timelines, and crontab, systemd or Dockerfile in plain English."
alt: "A YAML config file in Split view: the source on the left and its data tree on the right, with the name, version, features and authors keys and their typed values"
---

Raw data and config files are hard to read. Editora gives each one a preview that shows what the file means, in the same Editor, Split and Preview views as Markdown.

## Data and specs

- **Data trees** for JSON, YAML and TOML, collapsible and coloured by type, and an element tree for XML.
- **Maven poms as a summary**: coordinates, dependencies, plugins, properties and profiles in aligned columns, instead of the generic XML tree. **Maven: Toggle POM Summary / XML View** switches back.
- **API docs** for an OpenAPI 3 or Swagger 2 file: endpoints, method badges, parameters, responses and schemas, with a toggle back to the tree.
- **Markwhen timelines** for `.mw` and `.markwhen` files, with a month calendar view and JSON export.

## Config files in plain English

- **crontab**: `30 2 * * 1-5` reads "At 02:30, Monday through Friday", with the next run times.
- **systemd units**, with the next triggers of an `OnCalendar=` timer.
- **SSH config**, one connection summary per host, and **fstab** mounts.
- **Dockerfile**, a digest per build stage, and **GitHub Actions** workflows, their triggers and jobs.
- **Malformed lines are flagged**, so the preview also catches mistakes.

## Viewers

- **PDF files** open in a read-only page viewer.
- **SVG files** stay editable and gain a live rendered preview.
- **Binary files** open as a hex dump.

**On by default.** Turn the data, pom, SVG and config previews off one by one in **Settings → Editor → File previews**. See the [Previews guide](/docs/previews/).
