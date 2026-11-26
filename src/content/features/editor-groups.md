---
title: "Editor groups"
group: "Workspace & files"
order: 3
beta: false
summary: "Split the editor into independent groups with their own tabs to see two files at once, nest the splits, drag a tab between them, and get the whole arrangement back on the next launch."
description: "Split the editor into groups with their own tabs, nest the splits, drag tabs between them, and get the same layout back on the next launch."
alt: "Two editor groups side by side: a CSV file on the left and a Markdown file on the right"
---

---
---
Put two or more files side by side, each group with its own tabs, so a header sits beside its implementation or a test beside what it tests.

- **Split** with **View: Split Editor Group Right** or **View: Split Editor Group Down**, which move the current file into a new group.
- **Nest** the splits: a side-by-side pair can hold a stacked pair. Splitting the same way twice gives three even columns.
- **Drag a tab** onto another group to move it there, or onto a group's edge to split that group; a highlight shows where it will land.
- **Move between them** with **View: Move File to Next Editor Group** and **View: Focus Next Editor Group**, and **View: Merge Editor Groups** puts everything back in one.
- **No empty panes**: closing a group's last file closes the group.
- **Restored on launch**: the layout is saved with the [project](/features/projects/) session.

All five commands are in the palette and can be bound to keys. To see two parts of the *same* file, **View: Split Editor — Side by Side** (`C-x 3` in the Emacs keymap) splits one file into two views, and it combines with groups.

See the [Editor groups guide](/docs/workspace/#editor-groups-two-files-at-once).
