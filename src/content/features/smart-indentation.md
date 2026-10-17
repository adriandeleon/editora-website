---
title: "Smart indentation"
group: "Editing"
order: 2
beta: false
summary: "Per-language auto-indent on <kbd>Enter</kbd> (block openers, matching-pair stanzas, closer re-alignment), plus smart backspace that clears the indent in one press (one level at a time in Python and YAML)."
---

Enter follows each language's rules: it keeps the current line's indent, adds a level after a block opener (braces, `:` in Python/YAML, `do`/`then` in shell, an open tag in XML/HTML…), and splits a matching pair into an indented stanza with the closer dropped below. Typing a closer (`)]}` or a keyword like `end`/`fi`/`done`) re-aligns the line to its opener. In shell scripts the indent follows the script's structure: a `case` pattern indents its body, `;;` stays at the body's level (as shfmt lays it out), `fi`, `done` and `esac` line up with the block they close, and a here-document's body is left as it is while its closing delimiter is unindented so bash recognises it. In Python, typing the `:` of `else`, `elif`, `except`, `finally` or `case` lines the clause up with its `if` or `try`, Enter after `return`, `pass`, `raise`, `break` or `continue` steps out a level, the line after a wrapped statement goes back to the statement's indent, and inside an open bracket the next line hangs one level in or lines up with the first argument, as PEP 8 shows.

**Smart backspace** clears a line's indentation in one press, and on a blank, auto-indented line, a single Backspace jumps back to the end of the previous line. In Python and YAML, where indentation is syntax, it removes one indent level instead and never joins the line above. The indent unit (tabs vs spaces) is inferred from the file; in Python only lines that start a statement count, so PEP 8 hanging indents do not change it.
