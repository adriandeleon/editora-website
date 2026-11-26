---
title: "Diagrams as code"
group: "Docs & diagrams"
order: 6
beta: false
summary: "A 3-mode preview for Graphviz DOT (<code>.dot</code>/<code>.gv</code>) and PlantUML (<code>.puml</code>) files via the <code>dot</code> / <code>plantuml</code> CLIs, with export to SVG / PNG / PDF."
description: "Graphviz DOT and PlantUML files get an Editor, Split and Preview view rendered by dot and plantuml, with export to SVG, PNG and PDF."
alt: "A Graphviz DOT file in Split view, with the rendered graph beside its source"
---

Keep your architecture and UML diagrams as plain text in the repository, and see them drawn beside the source as you edit.

- **Graphviz and PlantUML.** `.dot` and `.gv` files and `.puml` and `.plantuml` files get the Editor, Split and Preview views, drawn by the `dot` and `plantuml` tools.
- **Quick to redraw.** An unchanged diagram is not drawn again, and zoom resizes the image.
- **Export** a diagram to SVG, PNG or PDF with **Diagram: Export (SVG/PNG/PDF)**, or print it.
- **Safe to open.** PlantUML runs sandboxed, so a diagram cannot pull in local files or URLs.

Nothing appears until the tools are found. Install them with your package manager, such as `brew install graphviz plantuml`; **Settings → Diagrams** shows whether each one is found.

**On by default.** Turn it off in **Settings → Diagrams**. See the [Diagrams guide](/docs/diagrams/).
