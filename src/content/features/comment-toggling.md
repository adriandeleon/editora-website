---
title: "Comment toggling"
group: "Editing"
order: 4
beta: false
summary: "<kbd>M-;</kbd> comments or uncomments the line or selection using the language's syntax (<code>//</code>, <code>#</code>, <code>&lt;!-- --&gt;</code>, <code>/* */</code>, <code>--</code>, …)."
---

`M-;` (Emacs comment-dwim) toggles comments using the language's own syntax. A single line toggles a line comment; a multi-line selection toggles a block/region comment: `//` and `/* */` for Java and C-likes, `#` for Python/shell/YAML, `<!-- -->` for XML/HTML/Markdown, `--` for SQL, and so on.

It preserves indentation, lines the markers of several commented lines up in one column, uses whichever form exists in line-only or block-only languages, and is a no-op for languages without comments. A `#!` shebang line is never toggled, in any language (`// #!/usr/bin/env node` would break a Node.js script), and in a shell script the lines of a here-document are left alone unless the selection includes the line that opens it.
