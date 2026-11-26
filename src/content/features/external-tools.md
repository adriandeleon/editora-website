---
title: "External tools"
group: "Customization & extensibility"
order: 9
beta: false
summary: "Define your own CLI commands and run them on the current file or buffer, with <code>$Name$</code> macros, stdin piping, and output to a console or back into the text."
description: "Run your own command-line tools on the current file or selection, with $Name$ macros, stdin piping, and output to a console or back into the text."
alt: "The External Tools: Run picker listing three tools and what each does with its output"
---

Turn any command-line tool into an Editora command: run a checker on the current file, or pipe the selection through `jq`, `sort` or `sed` and put the result back.

- **Define a tool** in **Settings → External Tools** with its command, arguments, input and output. The list starts empty.
- **Fill in the context** with macros such as `$FilePath$`, `$SelectedText$` and `$LineNumber$`, eight in all, for the current file, selection, caret and project.
- **Send it text.** A tool can pipe the selection or the whole buffer to the command's standard input.
- **Choose where the output goes:** a read-only console, the selection, the whole buffer as one undoable edit, or the caret.
- **Run it like any command.** Each tool is its own palette command that you can bind to a key, and **External Tools: Run…** and **External Tools: Rerun Last** pick or repeat one.

Tools run in the background with a time limit, so a slow one never freezes the editor. External tools are off in [Simple UI mode](/features/simple-ui-mode/).

See the [External tools guide](/docs/external-tools/#macros).
