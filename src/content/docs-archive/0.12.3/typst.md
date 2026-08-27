---
title: Typst
description: A live 3-mode preview for .typ files, a tinymist language server, Markdown-style editing, and export to PDF, PNG, or SVG.
category: Editing
order: 7
---

Standalone `.typ` ([Typst](https://typst.app)) files get the same 3-mode view
(Editor / Split / Preview) as Markdown, rendered off-thread by the external
**`typst`** CLI as a **multi-page** stack, one image per page.

The preview is steady while you work: the last good render stays on screen as you
edit (no flicker), and a compile error keeps the pages visible under a small
banner. Zoom resizes the pages.

## Editing

Typst files get Markdown-style ergonomics:

- Enter continues a `-` / `+` / `N.` list.
- Selecting text pops a **format bar** (bold `*`, emphasis `_`, raw `` ` ``,
  link, bullet, heading), with matching right-click and palette actions
  (`typst.bold`, `typst.emph`, `typst.raw`, `typst.link`, `typst.bulletList`,
  `typst.headingPromote` / `Demote`).
- Insert a table, image, or table of contents (`typst.insertTable`,
  `typst.insertImage`, `typst.outline`).
- Bundled snippets cover figures, tables, and more.

Code intelligence comes from the **tinymist** language server (enable the
[LSP feature](/docs/lsp) and install tinymist).

## Export

Export the document with `typst.export`: **PDF** (a native single file), **PNG**,
or **SVG** (also `typst.exportPng` / `typst.exportSvg`). Printing paginates the
pages.

## Enabling

It's **on by default**, self-gating on detection, so it stays inert until the
`typst` CLI is found. Install it with your package manager
(`brew install typst`, `cargo install typst-cli`) or the in-app **Install…**
button (`install.typstCli`). The toggle and tool path live under **Settings →
Languages & Tools → Typst**.
