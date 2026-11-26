---
title: "Typst"
group: "Docs & diagrams"
order: 5
beta: false
summary: "A 3-mode preview for <code>.typ</code> files rendered by the Typst CLI, a tinymist language server, Markdown-style editing, and export to PDF / PNG / SVG."
description: "A multi-page preview for .typ files rendered by the Typst CLI, the tinymist language server, list and format helpers, and export to PDF, PNG or SVG."
alt: "A Typst document in Split view, with the typeset page, headings and maths beside the source"
---

Write Typst documents with the typeset pages beside your source, and the editing comforts of Markdown. The preview keeps its last good render while you type, so it never flickers, and a compile error shows as a small banner over pages that stay visible.

- **See every page.** `.typ` files get the Editor, Split and Preview views, rendered by the `typst` CLI as a stack of pages.
- **Edit like Markdown.** Enter continues a list, and selecting text pops a format bar for bold, emphasis, raw, link, bullet and heading, also on the right-click menu.
- **Insert the usual blocks**: tables, images and a table of contents, plus bundled snippets for figures and more.
- **Navigate by section.** Headings drive the outline and the folding, and `#let` and `#show` bindings appear in the Structure window under their section.
- **Code intelligence** comes from the tinymist language server, which Editora can install for you.
- **Export** to PDF, PNG or SVG with **Typst: Export (PDF/PNG/SVG)**, or print the pages.

Nothing appears until `typst` is found. Install it with your package manager, such as `brew install typst` or `cargo install typst-cli`, or with the **Install…** button in **Settings → Typst**.

**On by default.** Turn it off in **Settings → Typst**. See the [Typst guide](/docs/typst/).
