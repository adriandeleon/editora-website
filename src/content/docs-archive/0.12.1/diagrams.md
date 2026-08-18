---
title: Diagrams as code
description: A live 3-mode preview for Graphviz DOT and PlantUML files, with export to SVG, PNG, or PDF.
category: Editing
order: 8
---

Standalone **Graphviz DOT** (`.dot` / `.gv`) and **PlantUML** (`.puml` /
`.plantuml`) files get the same 3-mode preview (Editor / Split / Preview) as
Markdown and Mermaid.

Rendering runs off-thread via the external **`dot`** and **`plantuml`** CLIs.
Both rasterize to PNG natively, so there's no headless browser, and results are
cached by source hash so an unchanged diagram isn't re-rendered. Zoom resizes the
image.

## Export

Export a diagram to **SVG**, **PNG**, or **PDF** with `diagram.export` (also on
the preview's right-click menu).

## Enabling

It's **on by default**, self-gating on detection, so it stays inert until the
tool is found. Install with your package manager, for example
`brew install graphviz plantuml`. The toggles and tool paths live under
**Settings → Languages & Tools → Diagrams**.

For diagrams embedded in Markdown, see [Mermaid](/docs/markdown#mermaid-diagrams).
