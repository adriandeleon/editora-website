---
title: "Smart indentation"
group: "Editing"
order: 2
beta: false
summary: "<kbd>Enter</kbd> puts the next line where the language expects it, closers line up with their openers, and Backspace clears an indent in one press."
description: "Per-language indentation on Enter and on typing a closer, PEP 8 hanging indents for Python, and a Backspace that clears the indent in one press."
alt: "A Python file whose new line under a for statement is indented automatically"
---

You spend less time fixing whitespace: each new line starts at the right depth for the language you are writing, and Backspace takes an indent away in one press.

- **Enter keeps the indent** of the current line, and adds a level after a block opener such as `{`, a trailing `:` in Python or YAML, `do` or `then` in shell, or an open tag.
- **Pairs split into a stanza.** Enter between `{` and `}` puts the caret on an indented line with the closer dropped below.
- **Closers line up.** Typing `)`, `]`, `}` or a closing keyword such as `end`, `fi` or `done` moves the line back to its opener.

## In each language

- **Python** follows PEP 8: `else`, `except` and `finally` line up with their `if` or `try`, Enter after `return` or `pass` steps out a level, and an open bracket gives a hanging indent. **Tab** gives a line the indent Enter would.
- **Shell scripts** indent the way shfmt lays them out: `case` bodies indent, `fi`, `done` and `esac` line up, and a here-document's closing word stays at the margin so bash still finds it.
- **Brace languages** such as Java, JavaScript and TypeScript indent a `case` body, the one statement under a brace-less `if`, the next argument of an open call, and a `.` call chain. Enter inside `/** … */` continues it with ` * `.

## Backspace and indent style

- **Smart Backspace** clears a line's whole indent in one press, and on a blank auto-indented line jumps back to the end of the line above. In Python and YAML, where indentation is syntax, it removes one level at a time.
- **Tabs or spaces** are read from the file. A Python file that mixes them the way Python rejects says "mixed indent" in the status bar.

See the [Editing guide](/docs/editing/#auto-indent).
