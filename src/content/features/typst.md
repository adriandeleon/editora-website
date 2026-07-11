---
title: "Typst"
group: "Docs & diagrams"
order: 5
beta: false
summary: "A 3-mode preview for <code>.typ</code> files rendered by the Typst CLI, a tinymist language server, Markdown-style editing, and export to PDF / PNG / SVG."
---

Standalone `.typ` files get the same 3-mode view (Editor / Split / Preview) as Markdown, rendered off-thread by the external **`typst`** CLI as a **multi-page** stack. The last good render stays on screen while you edit (no flicker), and a compile error keeps the pages visible under a small banner.

- **Editing** has Markdown-style ergonomics: Enter continues a `-` / `+` / `N.` list, and selecting text pops a format bar (bold, emphasis, raw, link, bullet, heading) with matching right-click and palette actions. Bundled snippets cover figures, tables, and more.
- **Code intelligence** comes from the **tinymist** language server.
- **Export** to PDF (a native single file), PNG, or SVG (`typst.export`); print paginates the pages.

**On by default**, self-gating on detection, so it's inert until `typst` is found. Install it with your package manager (`brew install typst`, `cargo install typst-cli`) or the in-app **Install…** button. See the [Typst guide](/docs/typst).
