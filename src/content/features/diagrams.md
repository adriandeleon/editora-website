---
title: "Diagrams as code"
group: "Docs & diagrams"
order: 6
beta: false
summary: "A 3-mode preview for Graphviz DOT (<code>.dot</code>/<code>.gv</code>) and PlantUML (<code>.puml</code>) files via the <code>dot</code> / <code>plantuml</code> CLIs, with export to SVG / PNG / PDF."
---

Standalone `.dot`/`.gv` (Graphviz) and `.puml`/`.plantuml` files get the same 3-mode preview as Markdown and Mermaid, rendered off-thread via the external **`dot`** and **`plantuml`** CLIs. Both rasterize to PNG natively, so there's no headless browser, and results are cached by source hash. Zoom resizes the image, and you can export a diagram to **SVG / PNG / PDF** (`diagram.export`).

**On by default**, self-gating on detection, so it's inert until the tool is found (install via your package manager, e.g. `brew install graphviz plantuml`). Tool paths live under Settings → Languages & Tools → Diagrams.
