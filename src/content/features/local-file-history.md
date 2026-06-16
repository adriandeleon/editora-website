---
title: "Local file history"
group: "Workspace & files"
order: 7
beta: false
summary: "IntelliJ-style snapshots of your files over time, on save, auto-save, and before an external reload, so you can diff or restore an earlier version with no Git required."
---

Editora quietly snapshots your local files over time, on save, on auto-save, and before it reloads a file that changed outside the editor. It's independent of any version control, so you get a safety net even on files that aren't in Git.

Open a file's timeline from the **File History** tool window (`M-g l`). Each revision shows its date, the reason it was taken, and its size, with the latest tagged *Current*. Double-click one for a read-only diff against the current file, or restore it (an undoable whole-file replace).

Snapshots are deduped by content and stored gzip-compressed under your config folder, pruned by configurable limits (revisions per file, age, size per project). On by default, local-only, and off in Simple UI mode.
