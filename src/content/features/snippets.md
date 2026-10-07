---
title: "Snippets"
group: "Editing"
order: 1
beta: false
summary: "Expand VS Code / TextMate templates with tab stops, mirrors, choices, and variables, all from a prefix + <kbd>Tab</kbd>. Ships for 23 languages."
---

Expand boilerplate with interactive templates. Type a prefix and press **Tab**, or pick from the **Snippet: Insert…** list (`C-c i`).

Placeholders are pre-selected to overtype, **Tab / Shift-Tab** cycle the fields, mirrors update live, choice fields show a dropdown, and `$0` is the final caret. Bodies use the standard VS Code / TextMate syntax, `$1`, `${1:default}`, mirrors, `${1|a,b|}` choices, and variables (`$TM_FILENAME`, `$CLIPBOARD`, date/time, the selection…).

Snippets ship for 23 languages; add your own in `~/.editora/snippets/<language>.json` (user snippets override the bundled ones).
