---
title: Snippets & templates
description: Expand boilerplate with tab-stop snippets, and scaffold new files from templates.
category: Editing
order: 2
---

Editora has two related ways to stop retyping boilerplate: **snippets** for
inline expansions, and **file templates** for whole new files. The two have
different syntaxes: a snippet body uses the VS Code / TextMate tab-stop syntax,
and a template body is a file with named `${variable}` placeholders.

## Snippets

Type a trigger and press **Tab**, or pick from the **Snippet: Insert…** list
(`C-c i`). Once expanded:

- Placeholders are pre-selected so you can overtype them.
- **Tab** / **Shift-Tab** cycle the fields, and mirrors update live.
- A choice field shows a dropdown.
- `$0` is where the caret lands when you finish.
- Every field is outlined, mirrors are underlined, and the final caret position
  is marked. The status bar shows where you are, as in **Snippet 2/3**.
- **Esc**, a click on that status-bar segment, or **Snippet: Leave Tab Stops**
  (`snippets.endSession`) leaves the fields and keeps the text. Moving the caret
  out of the fields does the same, so Tab indents again.

Bodies use the standard syntax: `$1`, `${1:default}`, mirrors (a repeated
number), `${1|a,b|}` choices, and [variables](#snippet-variables). Snippets ship
for 30 languages, plus a global set that applies to every file. The TypeScript
set also applies to JSX and TSX files.

### Expanding with Tab

A trigger can be a word (`fori`), a token with punctuation (`#include`), or up to
three words (`else if`). Tab does not expand a trigger inside a comment or a
string, or in a CSV/TSV file; there it indents as usual. The bundled `date` and
`time` snippets are offered in the completion popup and the **Snippet: Insert…**
list only, so Tab after those words is an ordinary Tab.

**Settings → Snippets → Expand snippets with Tab** (on by default, also **View:
Toggle Snippet Expansion on Tab**, `view.toggleSnippetTabExpansion`) turns Tab
expansion off altogether. Snippets then come from the completion popup and
**Snippet: Insert…** only.

### Snippet variables

A variable is written `$NAME` or `${NAME}`, and `${NAME:default}` supplies a
value for when it is empty. A `$name` Editora does not know stays in the text as
typed.

| Variables | Value |
| --- | --- |
| `TM_FILENAME`, `TM_FILENAME_BASE`, `TM_DIRECTORY`, `TM_FILEPATH` | The file's name, name without extension, folder, and full path |
| `RELATIVE_FILEPATH` | The file's path relative to the project |
| `WORKSPACE_NAME`, `WORKSPACE_FOLDER` | The project's name and folder |
| `TM_SELECTED_TEXT`, `SELECTION` | The selection when the snippet was inserted |
| `TM_CURRENT_LINE`, `TM_CURRENT_WORD` | The line and the word at the caret |
| `TM_LINE_INDEX`, `TM_LINE_NUMBER` | The caret's line, counted from 0 and from 1 |
| `CLIPBOARD` | The clipboard text |
| `CURRENT_YEAR`, `CURRENT_YEAR_SHORT`, `CURRENT_MONTH`, `CURRENT_MONTH_NAME`, `CURRENT_MONTH_NAME_SHORT`, `CURRENT_DATE`, `CURRENT_DAY_NAME`, `CURRENT_DAY_NAME_SHORT` | Parts of today's date |
| `CURRENT_HOUR`, `CURRENT_MINUTE`, `CURRENT_SECOND`, `CURRENT_SECONDS_UNIX`, `CURRENT_TIMEZONE_OFFSET` | Parts of the current time |
| `UUID`, `RANDOM`, `RANDOM_HEX` | A random UUID, six random digits, six random hex digits |
| `LINE_COMMENT`, `BLOCK_COMMENT_START`, `BLOCK_COMMENT_END` | The comment markers of the file's language |

### Your own snippets

User snippets live in your config folder under `snippets/<language>.json` (or
`global.json`). Snippets are merged by name in the order bundled, plugin, user,
so your snippet replaces a bundled or plugin one with the same name. When two
snippets share a trigger, both stay in the completion popup and the picker, and
Tab expands yours before a plugin's or a bundled one. Three commands manage
them:

- **Snippet: Manage…** opens **Settings → Snippets**, where you add, edit, and
  remove them without touching the JSON. The list has a filter and a trigger
  column.
- **Snippet: Edit User Snippets…** opens the file for the current language. It
  reloads when you save.
- **Snippet: Reload Snippets** picks up changes without a restart.

The format is the VS Code snippet JSON, and the reader is lenient about
real-world files. A snippet can have several triggers: an array `prefix` in the
JSON, or a comma-separated list in the **Triggers** field in Settings. A `scope`
field (a comma-separated list of language ids) limits a snippet to those
languages. A file with a mistake is reported with its name, the reason and the
line, and only the bad entry is skipped.

### Changing or disabling a bundled snippet

Bundled snippets appear in **Settings → Snippets** alongside yours. Editing one
saves your own version under the same name, which replaces the bundled snippet
under all its triggers; **Remove** brings the bundled one back. **Disable**
switches a snippet off without deleting anything, and **Enable** restores it. In
the JSON, a disabled bundled snippet is an entry with that snippet's name and
`"disabled": true`.

## File templates

**New File From Template** (`C-c C-n`, also a toolbar button) scaffolds a file,
or a whole set of files, from a reusable template. A picker chooses the
template and shows where each one comes from (bundled, yours, or a plugin), then
a small wizard prompts for any variables it declares that Editora can't fill in
itself.

Bundled templates cover a Java class, a Java compact source file, an HTML page,
an HTML bundle (multiple files), a Markdown doc, a Python script, a Python
project (multiple files), a shell script, and a Zsh script.

### Template syntax

A template body is a file, not a snippet. Exactly three forms are special, in a
body, a file name, or a path:

| Form | Meaning |
| --- | --- |
| `${variable}` | A variable: a built-in, or one the wizard asks for |
| `${variable:default}` | The same, with the value used when nothing supplies one. The wizard pre-fills it. |
| `${cursor}` | Where the caret lands when the file opens. It is removed from the text. |

Everything else is written as it is: `$1`, `$HOME`, `$@`, `${arr[0]}`, `$(date)`
and backslashes are literal, so shell and awk content needs no escaping. To
write a literal `${name}`, double the dollar: `$${name}`.

These variables are filled in for you rather than asked for: `${author}`,
`${projectName}`, `${packageName}`, `${packageDeclaration}`, `${fileName}`,
`${baseName}`, `${extension}`, `${date}`, `${year}`, and `${time}`.
`${packageDeclaration}` is a whole `package x.y;` line followed by a blank line,
or nothing when the file is outside a package. The snippet variables that start
with `TM_` or `CURRENT_` work in templates too.

### The wizard

The wizard asks for every variable that is not built in, plus the built-ins it
cannot work out. That includes the template's main name (the class name, the
file's base name, or the package) when the file name depends on it, and the
project name when no project is open. Fields have readable labels: a template
can supply its own, and otherwise a name like `issueTitle` is shown as *Issue
title*.

The wizard has a **Folder** field (with a Browse button). For a single-file
template it is optional: leave it blank to create an unsaved buffer, or pick a
folder to write the file to disk. A multi-file template requires a folder.
Invoking **New → From Template…** from a project folder's right-click menu
pre-fills that folder. The two flows are also the `template.new` and
`template.newInFolder` commands.

Before anything is written, the wizard lists the files that already exist in the
folder. They are kept as they are; pressing **Create** again creates only the
missing ones. A template never overwrites a file. When it is done, the file that
holds `${cursor}` opens with the caret there.

Created files end with a line break and follow the project's `.editorconfig`
line endings. A file that starts with `#!`, such as the shell and Zsh scripts,
is created executable on Linux and macOS.

### Java packages

A Java template created in a folder gets the package of that folder. Editora
looks for a `src/main/java` or `src/test/java` folder above the file, or a plain
`src` directly under the project root, and reads the package from the path below
it. The search stops at the project root, and folders such as
`src/main/resources` are not source roots. The Java Class template writes the
matching `package` line, or none outside a package.

### Your own templates

Drop template JSON files in your config folder under `templates/`. The file name
without `.json` is the template's id, and templates override each other by id in
the order bundled, plugin, user. The author name used by `${author}` comes from
**Settings → Templates → Author** (it defaults to your OS user).

- **Template: Manage…** opens **Settings → Templates** to add, edit, or remove
  single-file templates, including the ones plugins provide. Editing a bundled
  or plugin template saves a copy in your `templates/` folder that overrides it;
  **Remove** asks first, deletes your copy, and the original comes back.
- **Template: Edit User Templates…** (`template.editUser`) opens a list: pick
  one of your templates to open as JSON, choose **New template…** for a starter
  file, or choose **Customize** on a bundled or plugin template to copy it to
  your folder. Multi-file templates are edited this way.
- **Template: Reload Templates** re-reads the folder. Saving a template file in
  Editora applies it at once.

A single-file template looks like this:

```json
{
  "name": "Java Class",
  "description": "A public Java class with a main method",
  "language": "java",
  "fileName": "${className:Main}.java",
  "labels": { "className": "Class name" },
  "body": ["${packageDeclaration}public class ${className:Main} {", "    ${cursor}", "}"]
}
```

- `name` is required. `body` is a string or an array of lines.
- `fileName` is the name of the created file and may contain variables.
- `language` sets the grammar of the created file when its name alone does not
  select one.
- `labels` maps a variable to the label its wizard field shows.
- A multi-file template has `"files": [{ "path": "...", "body": "..." }]`
  instead of `fileName` and `body`. Paths are relative to the chosen folder, may
  contain variables, and cannot lead outside that folder.

A file that is not this shape is skipped. The picker and **Template: Reload
Templates** name the file, the reason, and the line of a JSON syntax error.

### Starting a project

**New Project From Template** (`project.newFromTemplate`) uses a multi-file
template to start a project. Pick the template, then enter a **Project name**
and a **Location**; the wizard shows the folder it creates. Editora creates
`<location>/<name>`, writes the template's files there, and opens the folder as
a project in its own window on the template's main file. The location defaults
to the folder beside the active project, or your home folder. A folder that
already exists and is not empty is refused. The name you enter is
`${projectName}` and, in a form valid for a package (`my-app` becomes `my_app`),
the default for `${packageName}`.

## New files by type

**File: New File of Type…** (`file.newFileOfType`, also **New** in the Project
tree's right-click menu) creates an empty or minimal file of a chosen kind
(a Java class, interface, record or enum, a Markdown file, a Python script,
YAML, and so on) in the current folder. It asks only for a name.

- The type's extension is added unless the name already ends in a known one, so
  `release-1.2` as a Markdown file becomes `release-1.2.md`.
- A Java type gets the `package` line of its folder, as described under
  [Java packages](#java-packages).
- A name that cannot work is refused with the reason: a name Windows reserves
  (`CON`, `NUL`, `COM1`), one of the characters `< > : " | ? *`, a trailing dot
  or space, or a name that starts with `~`. For Java types, a keyword or a name
  with another extension (`Foo.txt`) is refused as well.

## Autocomplete overlap

Snippet prefixes also surface in [autocomplete](/docs/languages#autocomplete):
accepting a snippet from the completion popup starts a full tab-stop session
rather than inserting plain text.
