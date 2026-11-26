---
title: "Comment toggling"
group: "Editing"
order: 4
beta: false
summary: "One key comments or uncomments the line or selection in the language's own syntax: <code>//</code>, <code>#</code>, <code>&lt;!-- --&gt;</code>, <code>--</code> and the rest."
description: "One key comments or uncomments the line or selection in the language's own syntax, keeps the indentation, and never touches a shebang line."
alt: "A Java method commented out as one block comment, its lines highlighted"
---

Comment out a line or a block with one key, in whatever language you are in, and press it again to bring the code back.

- **One key** for every language: **Edit: Toggle Comment** is `M-;` in the Emacs keymap and `Ctrl+/` in the other keymaps (`Cmd+/` on macOS).
- **The right syntax**: `//` and `/* */` in Java and C-like languages, `#` in Python, shell and YAML, `<!-- -->` in HTML, XML and Markdown, `--` in SQL, and so on.
- **Lines or blocks.** A single line gets a line comment and a multi-line selection a block comment, or whichever form the language has.
- **Tidy markers.** Indentation is kept, and the markers of several lines line up in one column.
- **Scripts stay runnable.** A `#!` shebang line is never commented, and a shell here-document is left alone unless you select the line that opens it.
- **Follows the file.** In an INI file such as `setup.cfg`, the comment character is the one the file uses.

See the [Comments guide](/docs/editing/#comments).
