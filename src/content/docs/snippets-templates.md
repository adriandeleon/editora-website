---
title: Snippets & templates
description: Expand boilerplate with tab-stop snippets, and scaffold new files from templates.
category: Editing
order: 2
---

Editora has two related ways to stop retyping boilerplate: **snippets** for
inline expansions, and **file templates** for whole new files. Both use the same
VS Code / TextMate placeholder syntax.

## Snippets

Type a prefix and press **Tab**, or pick from the **Snippet: Insert…** list
(`C-c i`). Once expanded:

- Placeholders are pre-selected so you can overtype them.
- **Tab** / **Shift-Tab** cycle the fields, and mirrors update live.
- A choice field shows a dropdown.
- `$0` is where the caret lands when you finish.

Bodies use the standard syntax: `$1`, `${1:default}`, mirrors (a repeated
number), `${1|a,b|}` choices, and variables like `$TM_FILENAME`, `$CLIPBOARD`,
the current selection, and date/time. Snippets ship for all 21 highlighted
languages.

### Your own snippets

User snippets live in your config folder under `snippets/<language>.json` (or
`global.json`), and override the bundled ones by prefix. Two commands manage
them:

- **Snippet: Edit User Snippets…** opens the file for the current language.
- **Snippet: Reload Snippets** picks up changes without a restart.

The format is the VS Code snippet JSON, and the reader is lenient about
real-world files (an array `prefix` registers every trigger, and an unknown
`scope` field is ignored).

## File templates

<span class="beta-pill">Beta</span>

**New File From Template** (`C-c C-n`, also a toolbar button) scaffolds a file,
or a whole set of files, from a reusable template. A picker chooses the
template, then a small wizard prompts for any variables it declares (author,
date, file name, package, and so on).

Templates use the same `${var}` / `${var:default}` / `$0` syntax as snippets,
plus a `${cursor}` marker for the final caret. Bundled templates cover a Java
class, an HTML page, an HTML bundle (multiple files), a Markdown doc, and a
Python script.

There are two flavors:

- **Untitled** (`template.new`): creates an unsaved buffer with tab stops live,
  so you fill the placeholders before saving.
- **Into a folder** (`template.newInFolder`, also the Project tree's
  *New From Template…*): writes the file or files to disk, refusing to overwrite,
  and opens the primary one.

### Your own templates

Drop template JSON files in your config folder under `templates/`; a user
template overrides a bundled one with the same id. The author name used by
`${author}` comes from **Settings → Application → Templates** (it defaults to
your OS user). **Template: Reload Templates** picks up changes.

## Autocomplete overlap

Snippet prefixes also surface in [autocomplete](/docs/languages#autocomplete):
accepting a snippet from the completion popup starts a full tab-stop session
rather than inserting plain text.
