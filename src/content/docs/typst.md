---
title: Typst
description: A live 3-mode preview for .typ files, a tinymist language server, Markdown-style editing, and export to PDF, PNG, or SVG.
category: Editing
order: 7
---

Standalone `.typ` ([Typst](https://typst.app)) files get the same 3-mode view
(Editor / Split / Preview) as Markdown, rendered off-thread by the external
**`typst`** CLI as a **multi-page** stack, one image per page.

The preview is steady while you work: the last good render stays on screen as you
edit (no flicker), and a compile error keeps the pages visible under a small
banner. Zoom resizes the pages.

## Editing

Typst files get Markdown-style ergonomics:

- Enter continues a `-` / `+` / `N.` list.
- Selecting text pops a **format bar** (bold `*`, emphasis `_`, raw `` ` ``,
  link, bullet, heading), with matching right-click and palette actions
  (`typst.bold`, `typst.emph`, `typst.raw`, `typst.link`, `typst.bulletList`,
  `typst.headingPromote` / `Demote`).
- Insert a table, image, or table of contents (`typst.insertTable`,
  `typst.insertImage`, `typst.outline`).
- Bundled snippets cover figures, tables, and more.

Code intelligence comes from the **tinymist** language server (enable the
[LSP feature](/docs/lsp) and install tinymist).

Editing actions are grouped under a single **Typst** submenu in the editor's
right-click menu, rather than spliced flat into it — eight entries at the top
level pushed cut, copy, paste and the spelling suggestions far enough down to
hunt for. Run and Debug deliberately stay at the top level, being the two
entries reached without reading the menu.

## Structure and folding

A `.typ` file **folds and outlines by section**. Headings (`=`, `==`, …) drive
both, so the Structure window reads as the document's own table of contents and
a section collapses like one. Folding at `#align` / `#table` brackets still
works alongside it, and a long embedded listing collapses at its ` ``` ` fence —
usually the thing you most want out of the way.

`#let` and `#show` bindings join the outline in document order, each nested
under the section it is written in (one above the first heading stays at the
root). A template file can be almost entirely bindings, and outlining headings
alone left exactly those files with an empty Structure window.

Three forms are deliberately left out, because each would be noise rather than
navigation:

- **`#set`**, which configures the document rather than defining anything — a
  run of `#set page` / `#set text` would top most files.
- **An indented `#let`**, inside a function body, naming something that means
  nothing outside it.
- **Destructuring and `#show:` forms**, which name nothing you could jump to.

Telling a section marker from every other `=` in a Typst file — an assignment in
`#let x = 1`, an equality inside math, a rule of `=` characters — and from
anything written inside a raw block, a line comment or one of Typst's nesting
block comments, is the whole difficulty. Folding and the outline share one
parser, so the two surfaces cannot disagree about where a section begins.

## Export

Export the document with `typst.export`: **PDF** (a native single file), **PNG**,
or **SVG** (also `typst.exportPng` / `typst.exportSvg`). Printing paginates the
pages.

## Enabling

It's **on by default**, self-gating on detection, so it stays inert until the
`typst` CLI is found. Install it with your package manager
(`brew install typst`, `cargo install typst-cli`) or the in-app **Install…**
button (`install.typstCli`). The toggle and tool path live under **Settings →
Languages & Tools → Typst**.
