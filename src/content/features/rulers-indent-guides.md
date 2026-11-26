---
title: "Rulers and indent guides"
group: "Editing"
order: 9
beta: false
summary: "Rulers at any columns, with their own set per language, and faint guides at every indentation level, carried through blank lines."
description: "Column rulers set globally or per language, and indent guides at every level that run through blank lines and stay aligned in any font."
alt: "A Rust file with rulers at columns 60 and 80 and indent guides down each nested block"
---

See your line-length limit and which block a line belongs to at a glance, in any file, font or tab size.

## Rulers

- **Any columns.** One ruler at column 80 by default, or as many as you like, such as `80, 120`, set in **Settings → Editor → Display** or with **Editor: Set Rulers…**.
- **Per language.** Give a file type its own columns, such as `java: 100; python: 79, 88`, with **Editor: Set Rulers for This Language…**. A language with no columns shows no rulers.
- **From the project.** An [`.editorconfig`](/features/editorconfig/) `max_line_length` replaces the first ruler, and `off` hides them all.

## Indent guides

- **A line at each level** of indentation, so nested blocks are easy to follow. The guides run through blank lines inside a block, follow the theme's colours, and skip folded lines.
- **Always on their columns**, measured from the laid-out text, so they stay aligned with tabs at any width, in a proportional font, and when the line numbers widen.
- **Cheap at any size.** Their cost depends on the lines on screen, not on the length of the file.

Both hide in [Zen mode](/features/zen-mode/) and [Simple UI mode](/features/simple-ui-mode/), and stay in [Expert mode](/features/expert-mode/). Show or hide them with **View: Toggle Rulers** and **View: Toggle Indent Guides**.

**On by default.** Turn them off in **Settings → Editor → Display**. See the [Rulers and indent guides guide](/docs/configuration/#rulers-and-indent-guides).
