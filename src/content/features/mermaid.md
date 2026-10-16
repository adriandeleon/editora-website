---
title: "Mermaid diagrams"
group: "Docs & diagrams"
order: 2
beta: false
summary: "Render Mermaid diagrams inline in Markdown and in standalone <code>.mmd</code> files, with live linting and export to SVG / PNG / PDF."
---

Mermaid diagrams render inline. A fenced ` ```mermaid ` block in Markdown becomes a diagram in the preview, and standalone `.mmd` files get the same 3-mode preview.

Rendering uses the `mmdc` CLI (rasterized faithfully and cached per diagram), with **live linting** via `maid` that underlines errors with precise line/column messages as you type. Export a diagram to **SVG / PNG / PDF**. Inside a Mermaid fence, completion offers Mermaid keywords, and diagrams are drawn for the preview's own light or dark theme. Exported HTML includes the drawn diagrams when `mmdc` is available.

On by default, and inert until `mmdc` is found. Configure it under Settings → Mermaid (point it at your `mmdc`/`maid`, or use `npx`).
