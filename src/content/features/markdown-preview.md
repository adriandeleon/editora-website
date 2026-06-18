---
title: "Markdown preview"
group: "Docs & diagrams"
order: 1
beta: false
summary: "IntelliJ-style 3-mode view rendered natively with CommonMark + GFM: task lists, code pills, images, LaTeX math, a heading outline, linting, and Export to HTML. Live and theme-matched."
---

An IntelliJ-style 3-mode view (**Editor**, **Editor + Preview** (split), and **Preview**) via a floating control at the top-right of any Markdown file.

It renders **natively** (no WebView) from CommonMark + GFM, GitHub-style: real task-list checkboxes, inline-code pills, underlined headings, tables, and **images** (local and remote, including SVG badges). It updates live as you type, follows the active theme (or its own light/dark toggle), and remembers its mode per file. Extra CommonMark extensions render too: YAML front matter, footnotes, heading anchors, and `++inserted++` text.

Markdown files get a full editing kit:

- **Linting** with high-confidence rules, shown as inline squiggles and in a Markdown Lint tool window.
- **LaTeX math**: inline `$…$` and display `$$…$$` (off by default).
- **Image paste & drag-drop** into a sibling `assets/` folder, and **smart link paste** to wrap a selection.
- **Table editing**: Tab between cells, Enter adds a row, with reflow.
- A **heading outline** in the Structure tool window.

Zoom with the −/+ control or Ctrl+wheel; right-click to copy, or **export to PDF, HTML, or print**. See the [Markdown guide](/docs/markdown).
