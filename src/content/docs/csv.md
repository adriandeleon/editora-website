---
title: CSV & TSV
description: Rainbow columns, a field readout, and an editable CSV Grid preview with sort, filter, and export, plus align/shrink and Markdown-table interop.
category: Workspace
order: 6
---

`.csv` and `.tsv` files open with spreadsheet-style tooling on top of proper
CSV/TSV syntax highlighting.

## In the editor

- **Rainbow columns**: each column is colored distinctly (cycling every eight),
  so rows line up at a glance. On by default; toggle in **Settings → Editor →
  CSV** or with *View: Toggle Rainbow CSV Columns*.
- **Field readout**: the status bar shows *Field N of M* for the caret's column.
- **Align / shrink**: *CSV: Align Columns* (`csv.align`) pads fields with spaces
  so the delimiters line up in a monospace editor; *CSV: Shrink Columns*
  (`csv.shrink`) removes that padding. Both are reversible and preserve quoted
  fields.

## The CSV Grid

The **CSV Grid** shows the active file as a spreadsheet. It isn't a tool
window: it's the file's preview, embedded in the editor with the same 3-mode
view (Editor / Split / Preview) as Markdown. Switch modes from the floating
control at the top right of the editor, or with `view.togglePreview` and
`view.toggleSplitPreview`.

- Content-fit column widths, a **filter box**, **column sort**, and
  inconsistent-row highlighting.
- **Editable** cells and headers, written straight back to the file.
- Right-click to **Export to PDF / Print / Excel (`.xlsx`) / ODF (`.ods`)**.

It's on by default for CSV/TSV files (**Settings → Editor → CSV**, or *View:
Toggle CSV Grid Preview*).

## Markdown-table interop

- *CSV: Copy as Markdown Table* (`csv.copyAsMarkdownTable`) copies the whole file
  as a GFM table.
- From a Markdown table, convert to and from CSV (`markdown.tableFromCsv` /
  `markdown.tableToCsv`) or export it to CSV, Excel, or ODF (see the
  [Markdown guide](/docs/markdown#tables)).
