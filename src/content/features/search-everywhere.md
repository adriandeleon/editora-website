---
title: "Search Everywhere"
group: "Keyboard & commands"
order: 3
beta: false
summary: "One picker over <strong>commands, files and symbols</strong>. Type the name of the thing instead of first choosing which finder it lives in — <code>&gt;</code>, <code>#</code> or <code>@</code> narrows it when you already know."
---

Editora had five pickers behind five chords, each asking you to decide *what kind* of thing you wanted before you could start typing its name. **Search Everywhere** asks for the name.

- No prefix: commands, project files and symbols together
- `>` commands only, `#` files only, `@` symbols only — VS Code's sigils, because the muscle memory already exists

Results stay **grouped by source** rather than interleaved on raw score. The sources differ in size by orders of magnitude (tens of thousands of symbols, thousands of files, a few hundred commands), so a flat merge hands the whole list to whichever is biggest and the other two disappear. Each source gets a guaranteed share, and the groups compete on their *best* result rather than their bulk.

The symbol half is backed by Editora's own [project symbol index](/features/code-navigation), so it works with no language server installed. See [Navigation & search](/docs/navigation#search-everywhere).
