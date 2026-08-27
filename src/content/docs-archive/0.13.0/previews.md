---
title: Smart file previews
description: Open a data or config file and get a rendered or plain-English preview, from JSON/YAML/TOML trees and OpenAPI docs to decoded systemd, ssh, Dockerfile, fstab, crontab, and GitHub Actions files.
category: Editing
order: 9
---

Many file types get the same 3-mode preview (Editor / Split / Preview) as
Markdown, turning raw data and config into something readable. Each is **on by
default**, and each preview type has its own checkbox under **Settings →
Editor**.

Whatever the file, the same two commands switch the view: **`view.togglePreview`**
flips between the editor and the full preview, and **`view.toggleSplitPreview`**
flips between the editor and a split editor-plus-preview. They act on whatever
the active file previews as, so there's nothing per-type to remember. A floating
3-mode toggle also appears at the top right of any previewable file.

## Structured data and API docs

- `.json`, `.yaml`, and `.toml` render a collapsible, type-colored **data tree**.
- `.xml` (xsd, xsl, fxml, wsdl, rss, and so on) renders a faithful **DOM
  tree** of tags, attributes, and text. A `pom.xml` gets [its own
  rendering](#maven-poms) instead.
- A JSON or YAML file recognized as an **OpenAPI 3 / Swagger 2** spec instead
  renders as browsable **API docs**: endpoints with colored method badges,
  params, responses, and schemas. Toggle between the tree and the docs with
  `structured.toggleView`.

## Maven poms

A `pom.xml` previews as a **summary** rather than as the generic XML tree, which
spreads every dependency over four nested rows. You get coordinates and parent,
then modules, properties, dependencies, managed dependencies, plugins, managed
plugins, and each profile's own set — with the artifact name and its version in
aligned columns, so the versions read as a column rather than a hunt.

The point is the two indirections that otherwise send you back up the file:

- A **`${property}` version is resolved**, with the reference kept beside it
  (`5.10.2  ${junit.version}`), including properties defined in terms of other
  properties and the `project.*` built-ins.
- A **blank version is filled in** from the file's own `<dependencyManagement>`
  or `<pluginManagement>` and tagged *managed* (`2.0.17  managed`).

What the file cannot answer, it says so about. **No parent pom is read**, so a
version inherited from a parent reads *inherited* rather than a number Editora
would be inventing, and an unresolvable `${…}` reports itself. This is a reading
of one file, not an effective pom.

Any file named `pom.xml` or `*.pom` qualifies, plus anything that sniffs as a
pom, so a `pom-template.xml` or an `effective-pom.xml` gets the same view.
**`pom.toggleView`** — or **Show as XML tree** in the preview's right-click menu
— switches to the standard XML rendering and back. Settings → Editor → File
previews turns the summary off entirely.

Poms also carry a [Maven submenu](/docs/build-tools#the-maven-submenu) on their
right-click menu.

## Config files, decoded to plain English

- **crontab** (`crontab`, `*.cron`, `cron.d/*`): each schedule in plain English
  (`30 2 * * 1-5` becomes "At 02:30, Monday through Friday"), the next fire times,
  and the `@reboot` / `@daily` macros. Malformed lines are flagged with the field
  error.
- **fstab**: each mount line decoded, the device spec (UUID / LABEL / path / CIFS
  / NFS), mount point, filesystem, and the options.
- **systemd** units (`.service` / `.timer` / `.socket` / …): each directive
  glossed, with a `.timer`'s `OnCalendar=` decoded to English plus its next
  triggers.
- **SSH config**: a one-line connection summary per `Host` block plus option
  glosses.
- **Dockerfile**: a per-build-stage digest (base image, ports, workdir, user,
  entrypoint, health check).
- **GitHub Actions** workflows (detected by content): a plain-English digest of
  the triggers (with `schedule:` crons decoded) and each job's runner and steps.

## Viewers

- `.pdf` files open in a read-only **page viewer** (rasterized via PDFBox).
- `.svg` files stay editable XML but gain a live rendered-image preview.
- Binary files open as a read-only [hex dump](/docs/editing#hex-viewer).

Every preview can **export to PDF** from its right-click menu.
