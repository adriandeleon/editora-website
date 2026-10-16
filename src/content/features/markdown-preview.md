---
title: "Markdown preview"
group: "Docs & diagrams"
order: 1
beta: false
summary: "A 3-mode view rendered natively with CommonMark + GFM: task lists, alerts, images, LaTeX math, selectable text, wiki links and backlinks, linting, formatting, and export to PDF, HTML, Word, and ODF. Live and theme-matched."
---

An IntelliJ-style 3-mode view (**Editor**, **Editor + Preview** (split), and **Preview**) via a floating control at the top-right of any Markdown file.

It renders **natively** (no WebView) from CommonMark + GFM, GitHub-style: real task-list checkboxes, inline-code pills, underlined headings, tables, and **images** (local and remote, including SVG badges). It updates live as you type, follows the active theme (or its own light/dark toggle), and remembers its mode per file. It can also open in a tab of its own, beside the file, following the active document or locked to one. It also renders front matter (as a table), GitHub alerts, footnotes, emoji shortcodes, `==highlight==`, superscript and subscript, definition lists, heading anchors, `++inserted++` text and a safe subset of HTML. A ```` ```console ```` or ```` ```shell-session ```` block shows as a terminal session: a muted prompt, the command highlighted as shell, and plain output. Text in the preview can be selected and copied as formatted text, task boxes clicked to tick the source, and images and diagrams enlarged.

Markdown files get a full editing kit:

- **Linting** with 43 markdownlint rules plus checks for broken links, shown as inline squiggles, scrollbar/minimap stripes, a status-bar count and a Markdown Lint tool window, with **auto-fix** (also on save), per-rule severity, project-wide lint, inline directives, and `.markdownlint` / `.markdownlint-cli2` config discovery.
- **LaTeX math**: inline `$…$` and display `$$…$$` (on by default).
- **Image paste & drag-drop** into an assets folder you choose (unused pasted images go to the trash on save), **Paste as Markdown** for HTML and spreadsheet cells, and **smart link paste** to wrap a selection.
- **Table editing**: insert a table, add/delete rows and columns, move and sort them, align columns, Tab between cells, and reflow; convert to and from **CSV** or export the table to Excel/ODF.
- **Table of contents** and **task-list** insertion, plus a **heading outline** in the Structure tool window.
- **Links and wiki links**: follow `#heading`, other-file and reference links; `[[wiki links]]` with completion; a **Backlinks** tool window; and links updated when a file is renamed or moved.
- **Format Document**, heading numbering, and completion of paths, heading anchors, emoji and fence languages.
- A **word count** and reading time in the status bar, and **typewriter mode**.
- **Export** the preview to PDF, **self-contained HTML**, **Word (`.docx`)**, or **ODF (`.odt`)**.

Zoom with the −/+ control or Ctrl+wheel; right-click to copy, export, or print. See the [Markdown guide](/docs/markdown).
