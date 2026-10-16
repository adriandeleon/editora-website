---
title: "Rulers and indent guides"
group: "Editing"
order: 9
beta: false
summary: "Rulers at any columns, with their own set per language, and faint guides at every indentation level, carried through blank lines."
---

**Rulers** mark line lengths: one at column 80 by default, or as many as you like. Set them in Settings → Editor (**Rulers**, such as `80, 120`), or with *Editor: Set Rulers…* from the palette.

A language can have **its own rulers** instead (**Rulers by language**, such as `java: 100; python: 79, 88`), set from the palette for the current file's language with *Editor: Set Rulers for This Language…*. A language with nothing after its colon has none. A project's [`.editorconfig`](/features/editorconfig) `max_line_length` replaces the first ruler, and `off` hides them all. *View: Toggle Rulers* shows or hides them.

**Indent guides** are faint vertical lines at each indentation level of the visible text, so you can see which block a line belongs to. They run through blank lines inside a block, follow the theme's colours, and skip folded lines. Each guide stands where its level's text starts, measured from the text as it is laid out, so the guides stay on their columns when the line numbers get wider, in a tab-indented file at any tab size, and in a proportional font; scrolled sideways, they are not drawn over the line numbers. They are on by default; turn them off with *View: Toggle Indent Guides* or in Settings → Editor.

Both are hidden in [Zen mode](/features/zen-mode) and [Simple UI mode](/features/simple-ui-mode), and kept in [Expert mode](/features/expert-mode). The guides are also off in large-file mode. Typing inside a line costs them next to nothing: the rulers are placed from one measurement, and the guides check where the text starts and redraw only when something they show has changed. Their cost depends on how many lines are on screen, not on the length of the file.
