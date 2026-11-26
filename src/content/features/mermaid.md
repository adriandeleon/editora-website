---
title: "Mermaid diagrams"
group: "Docs & diagrams"
order: 2
beta: false
summary: "Render Mermaid diagrams inline in Markdown and in standalone <code>.mmd</code> files, with live linting and export to SVG / PNG / PDF."
description: "Mermaid diagrams render in Markdown previews and .mmd files through mmdc, with live linting by maid and export to SVG, PNG and PDF."
alt: "A Mermaid flowchart in Split view, with the rendered diagram beside its source"
---

Draw flowcharts, sequence diagrams and charts as text, and see them rendered as you write, in Markdown documents and in their own files.

- **In Markdown**, a ` ```mermaid ` fence becomes a diagram in the [preview](/features/markdown-preview/), drawn for the preview's light or dark theme.
- **In `.mmd` files**, the diagram gets the Editor, Split and Preview views.
- **Live linting** underlines mistakes as you type, with the exact line and column, when the `maid` linter is installed.
- **Completion** offers Mermaid keywords inside a fence.
- **Export** a diagram to SVG, PNG or PDF with **Mermaid: Export Diagram (SVG/PNG/PDF)**. HTML exported from Markdown includes the drawn diagrams.

Diagrams are drawn by the `mmdc` command, and nothing appears until it is found. Install it with the **Install…** button in **Settings → Mermaid**, which shows whether each tool is found, and add the linter with `npm i -g @probelabs/maid`.

**On by default.** Turn it off in **Settings → Mermaid**. See the [Mermaid guide](/docs/markdown/#mermaid-diagrams).
