---
title: "Typst comes to Editora"
description: "A live 3-mode preview for .typ files via the Typst CLI, a tinymist language server, Markdown-style editing, and export to PDF, PNG, or SVG."
date: 2026-07-09
---

Editora now has first-class [Typst](https://typst.app) support. Standalone `.typ`
files get the same 3-mode view (Editor / Split / Preview) as Markdown, rendered
off-thread by the `typst` CLI as a **multi-page** stack, one image per page.

The preview stays steady while you work: the last good render remains on screen
as you edit, and a compile error keeps the pages visible under a small banner.

Editing has Markdown-style ergonomics. Enter continues a `-` / `+` / `N.` list,
and selecting text pops a format bar (bold, emphasis, raw, link, bullet,
heading) with matching right-click and palette actions, plus commands to insert
a table, image, or table of contents. Code intelligence comes from the
**tinymist** language server, and bundled snippets cover figures and tables.

Export the document to **PDF** (a native single file), **PNG**, or **SVG**
(`typst.export`); printing paginates the pages.

It's on by default and self-gating, so it stays inert until the `typst` CLI is
found. Install it with your package manager (`brew install typst`,
`cargo install typst-cli`) or the in-app **Install…** button. See the
[Typst guide](/docs/typst).
