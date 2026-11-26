---
title: "CSV & TSV support"
group: "Workspace & files"
order: 12
beta: false
summary: "Rainbow per-column coloring, a field readout, and an editable CSV grid with sort/filter, print, and export to PDF/Excel/ODF, plus align/shrink and Markdown-table interop."
description: "Rainbow CSV and TSV columns, a field readout, an editable grid with sort and filter, column alignment, and export to PDF, Excel and ODF."
alt: "A CSV file shown as a table in Preview, with a filter box and a first-row-is-header switch"
---

---
---
Work with CSV and TSV files as a table when you want to read or edit the data, or as text with every column in its own colour when you want to see the raw file.

## In the editor

- **Rainbow columns** give each column its own colour, cycling through eight, so rows line up at a glance.
- **Field readout**: the status bar says which field the caret is in, as *Field N of M*.
- **CSV: Align Columns** pads fields so the delimiters line up, and **CSV: Shrink Columns** takes the padding out again. Quoted fields are kept intact.

## As a grid

- **The CSV Grid** shows the file as a spreadsheet in its preview, beside the text or instead of it.
- **Edit cells and headers**, and the changes go straight back to the file.
- **Sort and filter** by column, and spot rows with the wrong number of fields, which are highlighted.

## Print, export and Markdown

- **Print, PDF, Excel and ODF**: the grid's right-click menu outputs exactly what it shows, the filtered rows in the sorted order.
- **CSV: Copy as Markdown Table** copies the file as a table for a README or an issue, and a Markdown table converts back to CSV.

**On by default.** Turn rainbow columns and the grid off in **Settings → Editor → CSV**. See the [CSV & TSV guide](/docs/csv/).
