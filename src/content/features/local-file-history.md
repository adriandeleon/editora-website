---
title: "Local History"
group: "Workspace & files"
order: 9
beta: false
summary: "Snapshots of your files over time, taken on save, auto-save, and before an external reload, so you can diff or restore an earlier version with no Git required."
description: "Snapshots of your files on save, auto-save and external reloads, to diff or restore an earlier version, with labels and deleted-file recovery. No Git needed."
alt: "The Local History window: a Java file's saved versions, with one compared against the current text"
---

---
---
Editora keeps earlier versions of your files on this machine, so a bad edit, a lost afternoon or an accidental delete can be undone even in a folder that is not in Git.

## What it keeps

- **Every save and auto-save** becomes a revision, and the version you started the session from is kept too.
- **Before anything replaces your text**: an outside change, a revert, Replace in Files, an AI agent's write or a Git discard each keep a copy first.
- **Deleted files**: deleting a file in Editora snapshots it first, so it can come back.

## Look back and restore

- **The Local History window** opens with `M-g l` in the Emacs keymap, `Alt+Shift+H` in the others (`Ctrl+Cmd+H` on macOS), and lists the file's revisions with when and why each was recorded.
- **Compare** any revision with the current file side by side, and the diff follows your edits.
- **Restore** the whole revision, or copy back single changes with the arrows in the diff. Both are undoable, and nothing is written until you save.
- **Labels**: **Local History: Put Label…** names a moment, such as "before the refactor", and labelled versions are kept longer.
- **Local History: Recent Changes…** lists the project's latest revisions across files.
- **A folder's history**: right-click a folder in the Project tree to see every file under it, deleted ones struck through, and bring one back.

History follows a file when you rename or move it, and old revisions are pruned by limits you set.

**On by default.** Turn it off in **Settings → Workspace → Local History**. See the [Local History guide](/docs/workspace/#local-file-history).
