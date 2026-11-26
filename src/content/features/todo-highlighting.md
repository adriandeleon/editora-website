---
title: "TODO highlighting"
group: "Workspace & files"
order: 10
beta: false
summary: "Configurable regex patterns (TODO, FIXME, and your own) are highlighted in the editor and collected in a TODO tool window, with scrollbar and minimap stripes."
description: "TODO, FIXME and your own regex patterns are highlighted in the editor, listed in a TODO window, and marked on the scrollbar and minimap."
alt: "TODO comments highlighted in a Java file, with the TODO window counting 1,309 matches in 163 files"
---

---
---
Every TODO, FIXME and pattern of your own stands out where it is written and is collected in one list, so the notes you leave in code get picked up again.

## See them

- **Highlighted inline**, each pattern in its own colour.
- **Stripes** on the scrollbar and the minimap edge show where they are; click one to jump.
- **The TODO window** opens with `M-g o` in the Emacs keymap, or **Tool Window: TODO** in the palette, and lists every match in the project, grouped by file.
- **Next and previous** match in the file with `M-g ]` and `M-g [` in the Emacs keymap, or **TODO: Next Match** in the palette.

## Organize them

- **Tags and priorities**: write `TODO [auth] (high) fix token refresh`, and the TODO window groups matches by file, priority, tag or keyword.
- **Edit from the list**: right-click a match to mark it done, set its priority or change its text, and the source changes to match.

## Choose the patterns

**Settings → TODO → TODO highlighting** sets each pattern's name, regex, colour and case. Six ship with Editora: TODO, FIXME, HACK, NOTE, XXX and DONE. A new pattern's regex fills in from its name.

**On by default.** Turn it off in **Settings → TODO → TODO highlighting** (**Highlight TODO/FIXME patterns**). See the [TODO highlighting guide](/docs/todo/).
