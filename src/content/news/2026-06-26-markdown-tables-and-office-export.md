---
title: "Markdown table editing, and export to Word and ODF"
description: "Insert and edit Markdown tables, convert to and from CSV, and export the preview to MS Word (.docx) and OpenDocument (.odt) alongside PDF and HTML."
date: 2026-06-26
---

Two rounds of Markdown improvements landed together.

**Table editing.** The right-click **Table** submenu (and the palette) now
inserts a table and adds or deletes rows and columns, on top of Tab-between-cells
and reflow. Tables also interoperate with data files: convert **to and from CSV**,
or export the table under the cursor to a **CSV**, **Excel (`.xlsx`)**, or **ODF
(`.ods`)** file. You can also insert a **table of contents** and a **task list**.

**Export to Word and ODF.** Beyond PDF and standalone HTML, the Markdown preview
now exports to **MS Word (`.docx`)** and **OpenDocument Text (`.odt`)** from the
preview right-click menu (`preview.exportDocx` / `preview.exportOdt`). Headings,
text styling, lists, quotes, code, GFM tables, math, Mermaid diagrams, and images
(including SVG badges) all carry over.

See the [Markdown guide](/docs/markdown#tables) and, for editing data files
directly, [CSV & TSV](/docs/csv).
