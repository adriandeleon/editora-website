---
title: Markdown, diagrams & preview
description: The live Markdown preview, the format bar, linting, LaTeX math, Mermaid, HTML preview, and export to PDF or HTML.
category: Editing
order: 3
---

Editora renders Markdown natively (no embedded browser) and gives Markdown files
a few editing aids on top.

## Live preview

Markdown files get an IntelliJ-style 3-mode view from a floating control at the
top-right of the editor:

- **Editor**: just the source.
- **Split**: source on the left, preview on the right.
- **Preview**: the rendered page only.

The preview is built from CommonMark + GFM and aims to match GitHub: real
task-list checkboxes, inline-code pills, underlined headings, tables, and images
(local and remote, including SVG badges). It updates live as you type, follows
the active theme or its own light/dark toggle, and remembers its mode per file.

Beyond GFM it also renders **YAML front matter** (as a metadata block),
**footnotes**, **heading anchors**, and **`++inserted++`** text, and the
Structure tool window shows the document's `#`…`######` heading outline for quick
navigation.

Zoom with the `−` / `+` control or Ctrl+wheel. In Preview mode, **Space** /
**PageDown** and **Backspace** / **PageUp** scroll the page, and a right-click
menu offers Select All, Copy, Export to PDF, and Print.

## The format bar

Select text in a Markdown file and a small format bar appears above the
selection with bold, italic, strikethrough, inline code, link, list, and a
heading level picker. The same actions are commands with Emacs `markdown-mode`
chords:

| Action | Command | Default key |
| --- | --- | --- |
| Bold / italic / strike / code | `markdown.bold` … | `C-c C-s b/i/s/c` |
| Insert link | `markdown.link` | `C-c C-a l` |
| Promote / demote heading | `markdown.headingPromote` / `Demote` | `C-c C-h p` / `d` |
| Open link under caret | `markdown.openLink` | `C-c C-o` |
| Reflow table | `markdown.reflowTable` | `C-c C-s t` |

List and quote continuation happens automatically on Enter. Ctrl/Cmd-click opens
a link. The format bar can be turned off in **Settings → Editor**.

## Linting, math, and editing aids

- **Linting**: high-confidence rules (trailing whitespace, blank-line runs,
  heading-marker spacing, multiple H1, fenced blocks without a language, missing
  final newline, broken reference links) show as inline squiggles with hover
  messages and in a **Markdown Lint** tool window. On by default; toggle with
  *View: Toggle Markdown Lint*.
- **LaTeX math**: render inline `$…$` and display `$$…$$` math in the preview
  (and in PDF export) via JLaTeXMath, with GitHub-style delimiter rules so prose
  dollar amounts are left alone. **Off by default** (Settings → Editor → *Render
  LaTeX math*).
- **Image paste and drag-drop**: paste an image from the clipboard, or drop image
  files onto a saved Markdown buffer, and the file is written into a sibling
  `assets/` folder with the `![](…)` link inserted.
- **Smart link paste**: paste a URL over a selection to wrap it as
  `[selection](url)`.
- **Table editing**: **Tab** / **Shift-Tab** move between pipe-table cells, and
  **Enter** on the last row adds a row, reflowing as you go.

## Mermaid diagrams

Mermaid renders inline. A fenced ` ```mermaid ` block in Markdown becomes a
diagram in the preview, and standalone `.mmd` files get the same 3-mode preview.

Mermaid is **off by default**. Enable it in **Settings → Mermaid** and point it
at the `mmdc` (mermaid-cli) command, with optional `maid` for linting (a blank
field uses `npx`). Rendering uses `mmdc` and is cached per diagram; `.mmd` files
get live linting that underlines errors with precise line and column messages.
Export a diagram to SVG, PNG, or PDF with `mermaid.export`.

## HTML live preview

<span class="beta-pill">Beta</span>

On any `.html` / `.htm` / `.xhtml` file, a floating globe button opens it in a
detected browser (Safari, Chrome, Firefox, Edge, or the system default), served
over a tiny loopback-only web server with live reload as you type. Sibling CSS,
JS, and images load from disk.

It's **off by default**; enable it in **Settings → HTML Preview**. The file must
be saved so its assets resolve, and remote (SFTP) files are excluded. Commands:
`htmlPreview.open` and `htmlPreview.openIn` (pick a browser).

## Export and print

Export code to a syntax-highlighted PDF, or the Markdown / Mermaid preview to a
richly formatted PDF with headings, lists, tables, code blocks, and images as
native vector text. You can also export a Markdown file's preview to a
**standalone, self-contained `.html` file** (embedded stylesheet, heading
anchors, math as images) with *Preview: Export to HTML*. Or print either, with a
page-by-page preview first (what you preview is what prints). Output is always
light-themed and generated off the UI thread.

Commands: `editor.exportPdf`, `preview.exportPdf`, `preview.exportHtml`,
`editor.print`, `preview.print`. Line numbers, syntax highlighting, and page size
live in **Settings → Editor → Export & Print**.
