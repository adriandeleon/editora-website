---
title: External tools
description: Define your own CLI commands and run them on the current file or buffer, with macros, stdin piping, and flexible output.
category: Customization
order: 6
---

Define your own command-line tools in **Settings → External Tools** and run them
on the current file or buffer, IntelliJ-style. It's available by default (the
list starts empty until you add one) and off in Simple UI mode.

## Macros

A tool's command and arguments expand `$Name$` macros from the active file:

| Macro | Value |
| --- | --- |
| `$FilePath$` | Full path of the file |
| `$FileDir$` | The file's directory |
| `$FileName$` | The file name |
| `$FileNameWithoutExtension$` | The file name without its extension |
| `$SelectedText$` | The current selection |
| `$LineNumber$` / `$ColumnNumber$` | Caret position |
| `$ProjectFileDir$` | The project root |

## Input and output

A tool can pipe the **selection** or the **whole buffer** to the command's
stdin. It then chooses what to do with the output:

- show it in a read-only **console** tool window,
- **replace the selection**,
- **replace the whole buffer** (undoable), or
- **insert at the caret**.

That covers both "run a command and read the output" and text transforms with
filters like `jq`, `sort`, or `sed`.

## Running

Every tool you define becomes its own palette command (bindable to a key in
[Settings → Keymaps](/docs/keymaps)). There's also **External Tools: Run…**
(`externalTool.run`, a picker) and **Rerun Last** (`externalTool.rerunLast`).
Tools run off the UI thread with a timeout, and console output appears in the
External Tools tool window (`tool.externalTools`).
