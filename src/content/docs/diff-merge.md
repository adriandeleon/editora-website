---
title: Diff & merge
description: The side-by-side and unified diff viewer, hunk apply, and the merge-conflict resolver.
category: Version control
order: 2
---

Editora has a built-in diff viewer and a merge-conflict resolver. The Git-backed
comparisons need [Git](/docs/git) enabled; comparing two arbitrary files does
not.

## Diff viewer

Compare files in a dedicated tab, **side by side** or **unified**, with per-line
backgrounds and intra-line word-level highlights.

| Compare | Command | Default key |
| --- | --- | --- |
| Against HEAD | `diff.vsHead` | `C-x v =` |
| Against a commit | `diff.vsCommit` | (palette) |
| With another file | `diff.compareWith` | (palette) |

When a file is open, its live (possibly unsaved) text is used as the working
side. Open diffs **refresh live** when the underlying files change on disk or
after a Git mutation such as a commit, stage, or checkout.

## Applying changes

On the editable side, gutter arrows let you **apply changes hunk by hunk**: a
single chevron on each changed row, and a double chevron at the start of a whole
hunk. The toolbar's **Apply all** replaces the editable file with the other side
wholesale (after a confirmation).

Applies route through an undoable buffer, so the toolbar's **Undo** and **Save**
act on them and nothing is committed to disk until you save. Which side is
editable depends on the comparison (the working file for vs-HEAD and vs-commit,
your local file for compare-with).

## Merge conflicts

When a file has Git conflict markers, `merge.resolve` opens the **merge
resolver**. It lists each conflict with **Accept Ours**, **Accept Theirs**, or
**Accept Both**, and a resolved count in the toolbar. Save writes the resolved
lines back to the buffer (undoable).
