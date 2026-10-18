---
title: "Smart indentation"
group: "Editing"
order: 2
beta: false
summary: "Per-language auto-indent on <kbd>Enter</kbd> (block openers, matching-pair stanzas, closer re-alignment), plus smart backspace that clears a whole indent level in one press."
---

Enter follows each language's rules: it keeps the current line's indent, adds a level after a block opener (braces, `:` in Python/YAML, `do`/`then` in shell, an open tag in XML/HTML…), and splits a matching pair into an indented stanza with the closer dropped below. Typing a closer (`)]}` or a keyword like `end`/`fi`/`done`) re-aligns the line to its opener. In shell scripts the indent follows the script's structure: a `case` pattern indents its body, `;;` stays at the body's level (as shfmt lays it out), `fi`, `done` and `esac` line up with the block they close, and a here-document's body is left as it is while its closing delimiter is unindented so bash recognises it.

In brace languages such as JavaScript, TypeScript and Java, a `case` label indents its body, the one statement under a brace-less `if`, `for`, `while` or `else` is indented and the next line steps back, an open argument list (`foo(a,`) indents the next argument, a line starting with `.` continues a call chain, and Enter inside a `/** … */` comment continues it with ` * `.

**Smart backspace** removes a whole indent level in one press, and on a blank, auto-indented line, a single Backspace jumps back to the end of the previous line. The indent unit (tabs vs spaces) is inferred from the file.
