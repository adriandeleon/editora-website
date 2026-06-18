---
title: "Markdown gets linting, math, image paste, and HTML export"
description: "A broad set of Markdown upgrades: more CommonMark extensions, a heading outline, linting, LaTeX math, image paste and drag-drop, table editing, and Export to HTML."
date: 2026-06-20
---

The Markdown experience got a wide round of improvements.

- **More CommonMark extensions** in the preview: YAML front matter (as a
  metadata block), footnotes, heading anchors, and `++inserted++` text, on top
  of the existing GFM tables, task lists, strikethrough, and autolinks.
- **Heading outline**: the Structure tool window now shows a document's
  `#`…`######` tree (ATX and Setext), so long documents are navigable. Click a
  heading to jump.
- **Markdown linting**: high-confidence rules (trailing whitespace, blank-line
  runs, heading-marker spacing, multiple H1, fenced blocks without a language,
  missing final newline, broken reference links) show as inline squiggles with
  hover messages and in a new **Markdown Lint** tool window. On by default;
  toggle with *View: Toggle Markdown Lint*.
- **Image paste and drag-drop**: paste an image from the clipboard, or drop
  image files onto a saved Markdown buffer, and the file is written into a
  sibling `assets/` folder with the `![](…)` link inserted.
- **Smart link paste**: paste a URL over a selection to wrap it as
  `[selection](url)`.
- **Table editing**: Tab and Shift-Tab move between cells, and Enter on the last
  row adds one, reflowing the table as you go.
- **LaTeX math**: render inline `$…$` and display `$$…$$` math in the preview
  (and in PDF export) via JLaTeXMath, with GitHub-style delimiter rules so prose
  dollar amounts are left alone. Off by default (Settings → Editor → *Render
  LaTeX math*).
- **Export to HTML**: export the rendered preview to a standalone,
  self-contained `.html` file (embedded stylesheet, heading anchors, math as
  images) via *Preview: Export to HTML*.

See the [Markdown guide](/docs/markdown) for the full picture.
