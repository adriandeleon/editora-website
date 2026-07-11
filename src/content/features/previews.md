---
title: "Smart file previews"
group: "Docs & diagrams"
order: 7
beta: false
summary: "Open a data or config file and get a rendered or plain-English preview: JSON/YAML/TOML/XML trees, OpenAPI docs, and decoded systemd, ssh, Dockerfile, fstab, crontab, and GitHub Actions files."
---

Many file types get the same 3-mode preview as Markdown, turning raw config and data into something readable.

- **Structured data**: `.json` / `.yaml` / `.toml` render a collapsible, type-colored **data tree**, and `.xml` renders a faithful DOM tree. A JSON/YAML file recognized as an **OpenAPI 3 / Swagger 2** spec instead renders as browsable **API docs** (endpoints, method badges, params, responses, schemas), with a tree ⇄ docs toggle.
- **Config files, decoded to plain English**: **crontab** (`30 2 * * 1-5` becomes "At 02:30, Monday through Friday", with next fire times), **fstab** mounts, **systemd** units (with `OnCalendar=` next triggers), **SSH config** (a one-line connection summary per host), **Dockerfile** (a per-stage digest), and **GitHub Actions** workflows (triggers and jobs). Malformed lines are flagged.
- **Viewers**: `.pdf` files open in a read-only page viewer, `.svg` files stay editable XML but gain a live rendered-image preview, and binaries open as a hex dump.

All on by default, each toggled under Settings → Editor. See the [previews guide](/docs/previews).
