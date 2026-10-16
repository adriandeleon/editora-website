---
title: "EditorConfig"
group: "Editing"
order: 6
beta: false
summary: "Honors a project's <code>.editorconfig</code>: indent style/size, tab width, end-of-line, charset, max line length, and on-save trim / final-newline. On by default."
---

Editora reads a project's `.editorconfig` so your files follow the project's conventions automatically. On opening a file it resolves the nearest config, walking up the directory tree to a `root = true` file, with the closest directory winning.

It honors the common keys:

- **`indent_style`** / **`indent_size`** / **`tab_width`**: Tab and Enter follow them, and so do new files from templates.
- **`end_of_line`** and **`charset`** (utf-8, utf-8-bom, latin1, utf-16le/be), round-tripped on read and save. An encoding you choose for a file yourself ([Reopen or Save with Encoding](/features/file-encodings)) takes precedence.
- **`max_line_length`**, which sets the first [ruler](/features/rulers-indent-guides) (`off` hides them all).
- On save, **`trim_trailing_whitespace`** and **`insert_final_newline`**.

Shell scripts keep LF line endings and no byte-order mark: `end_of_line = crlf` and `charset = utf-8-bom` are not applied to them, because bash cannot run such a script.

It's **on by default**; toggle it in Settings → Editor or with *View: Toggle EditorConfig*. Without an `.editorconfig`, a global **Indent style** preference (Detect / Spaces / Tabs) still applies.
