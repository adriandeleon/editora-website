---
title: EditorConfig
description: Editora honors a project's .editorconfig for indent, line endings, charset, and on-save fixups.
category: Editing
order: 4
---

Editora reads a project's [`.editorconfig`](https://editorconfig.org) so your
files follow the project's conventions without per-file fiddling. It's **on by
default**; toggle it in **Settings → Editor** or with *View: Toggle
EditorConfig*.

## How resolution works

When you open a file, Editora finds the nearest `.editorconfig` by walking up the
directory tree until it hits a file with `root = true`. Closer directories win,
so a nested config overrides a parent.

## Supported keys

| Key | Effect |
| --- | --- |
| `indent_style`, `indent_size`, `tab_width` | Tab and Enter indentation |
| `end_of_line` | LF / CRLF / CR, round-tripped on save |
| `charset` | utf-8, utf-8-bom, latin1, utf-16le/be, round-tripped on read and save |
| `max_line_length` | Drives the column ruler |
| `trim_trailing_whitespace` | Trim on save |
| `insert_final_newline` | Ensure a trailing newline on save |

The on-save fixups (trim, final newline) and the encoding round-trip apply when
the file is written, so what's on disk matches the config.

## Without an .editorconfig

The indent unit is normally inferred per file. You can force it with a global
**Indent style** preference (Detect / Spaces / Tabs) in **Settings → Editor**, or
the *Editor: Set Indent Style…* command. With an `.editorconfig` present, its
`indent_style` takes precedence over the global preference.

The *EditorConfig: Open Active File* command (`editorConfig.openActive`, also the
status-bar EditorConfig segment) opens the `.editorconfig` governing the current
file.
