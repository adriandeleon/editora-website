---
title: "EditorConfig"
group: "Editing"
order: 6
beta: false
summary: "Honors a project's <code>.editorconfig</code>: indent style/size, tab width, end-of-line, charset, max line length, and on-save trim / final-newline. On by default."
---

Editora reads a project's `.editorconfig` so your files follow the project's conventions automatically. On opening a file it resolves the nearest config, walking up the directory tree to a `root = true` file, with the closest directory winning.

It honors the common keys:

- **`indent_style`** / **`indent_size`** / **`tab_width`**: Tab and Enter follow them.
- **`end_of_line`** and **`charset`** (utf-8, utf-8-bom, latin1, utf-16le/be), round-tripped on read and save.
- **`max_line_length`**, which drives the column ruler.
- On save, **`trim_trailing_whitespace`** and **`insert_final_newline`**.

It's **on by default**; toggle it in Settings → Editor or with *View: Toggle EditorConfig*. Without an `.editorconfig`, a global **Indent style** preference (Detect / Spaces / Tabs) still applies.
