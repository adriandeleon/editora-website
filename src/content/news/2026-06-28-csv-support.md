---
title: "CSV and TSV get first-class support"
description: "Rainbow columns, a field readout, and an editable CSV Grid with sort, filter, and export, plus align/shrink and Markdown-table interop."
date: 2026-06-28
---

`.csv` and `.tsv` files now open with real, spreadsheet-style tooling on top of
proper CSV/TSV highlighting.

**In the editor.** Each column is colored distinctly (**Rainbow CSV**, cycling
every eight), the status bar shows *Field N of M* for the caret's column, and two
commands reformat the file: *CSV: Align Columns* pads fields so the delimiters
line up, and *CSV: Shrink Columns* reverses it. Both preserve quoted fields.

**The CSV Grid.** A tool window shows the file as a spreadsheet, with content-fit
columns, a filter box, column sort, inconsistent-row highlighting, and
**editable** cells and headers written straight back to the file. Right-click to
export it to **PDF, Print, Excel (`.xlsx`), or ODF (`.ods`)**.

**Markdown interop.** Copy a CSV file as a Markdown table, or convert a Markdown
table to and from CSV and export it to Excel or ODF.

Rainbow columns and the grid are on by default (Settings → Editor → CSV). See the
[CSV guide](/docs/csv).
