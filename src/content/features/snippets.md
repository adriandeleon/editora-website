---
title: "Snippets"
group: "Editing"
order: 1
beta: false
summary: "Expand VS Code / TextMate templates with tab stops, mirrors, choices, and variables, all from a prefix + <kbd>Tab</kbd>. Ships for 30 languages."
---

Expand boilerplate with interactive templates. Type a prefix and press **Tab**, or pick from the **Snippet: Insert…** list (`C-c i`).

Placeholders are pre-selected to overtype, **Tab / Shift-Tab** cycle the fields, mirrors update live, choice fields show a dropdown, and `$0` is the final caret. Bodies use the standard VS Code / TextMate syntax, `$1`, `${1:default}`, mirrors, `${1|a,b|}` choices, and variables (`$TM_FILENAME`, `$CLIPBOARD`, date/time, the selection…).

While a snippet runs its fields are outlined and the status bar shows where you are ("Snippet 2/3"). Tab does not expand inside comments or strings. A trigger typed out in full is expanded by Tab ahead of a dictionary or AI inline suggestion, and leads the completion list when a language server is running. In Markdown and plain text, a trigger that is an ordinary word expands only when it is alone on its line, and a block snippet (heading, table, alert…) only at the start of a line; mid-sentence, the completion popup still offers it. Shell snippets follow the script's `#!` line: under `sh` or `dash` they write `[ ]` tests instead of bash's `[[ ]]`. Python snippets cover functions, methods, classes, `if`/`for`/`while`/`with`, `try`/`except` blocks, `match`/`case` and the `__main__` guard, and `#env` writes a `python3` shebang. In shell and Python, Enter after a keyword ends the line; Tab expands its snippet. In a shell script, Enter on a completion that only repeats the word just typed (`else`, `case`…) starts a new line; Tab still expands the snippet.

Snippets ship for 30 languages, and a bundled snippet can be disabled; add your own in `~/.editora/snippets/<language>.json` (user snippets override the bundled ones).
