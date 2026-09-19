---
title: Diff & merge
description: File, folder, patch, and Git reviews; editable results; hunk actions; and three-way merge resolution.
category: Version control
order: 2
beta: true
---

Editora has a built-in diff viewer and a merge-conflict resolver. The Git-backed
comparisons need [Git](/docs/git) enabled; comparing two arbitrary files does
not.

## Diff viewer

Compare files in a dedicated tab, **side by side** or **unified**, with per-line
backgrounds, intra-line word emphasis, change ribbons between related hunks,
and a right-edge overview of the whole document.

| Compare | Command | Default key |
| --- | --- | --- |
| Against HEAD | `diff.vsHead` | `C-x v =` |
| Against a commit | `diff.vsCommit` | (palette) |
| With another file | `diff.compareWith` | (palette) |
| Clipboard or empty text with the active file | `diff.compareClipboard` / `diff.compareBlank` | (palette) |
| Two folders recursively | `diff.compareDirectories` | (palette) |
| All staged changes | `diff.reviewStaged` | (palette / Commit window) |
| All unstaged and untracked changes | `diff.reviewUnstaged` | (palette / Commit window) |
| Open a `.patch`/`.diff` in the viewer | `diff.openPatchFile` | (palette) |

When a file is open, its live (possibly unsaved) text is used as the working
side. A unified `.patch`/`.diff` file opens as one navigable multi-file review
with per-file status and statistics. Git reviews provide the same navigation
across every staged or working-tree file. Open diffs **refresh live** when the
underlying files change on disk or after a Git mutation such as a commit, stage,
or checkout, while preserving the selected change, scroll positions, focused
side, and file-list divider.

Comparison controls can ignore whitespace or case, use smart line alignment,
collapse unchanged context, wrap long lines, switch layout, swap sides, and
export a patch. Binary and very large inputs degrade to safe metadata or bounded
line comparisons instead of being decoded or rendered as ordinary text.

## Folder comparisons

Right-click a folder in the Project tree to compare it recursively with HEAD, a
branch, tag, or chosen revision. You can also compare any two directories with
`editora --diff-ui DIR DIR`. The scan runs in the background, respects Git
ignore rules on both sides, excludes `.git`, and lists only changed, left-only,
or right-only files in a lazy review.

`editora --diff-ui LEFT RIGHT` starts an isolated comparison workspace without
restoring a normal Editora session. Use the Editora-mark button in its toolbar
to restore the full interface without closing or reloading the comparison.

## Applying changes

On the editable side, gutter arrows let you **apply changes hunk by hunk**: a
single chevron on each changed row, and a double chevron at the start of a whole
hunk. The toolbar's **Apply all** replaces the editable file with the other side
wholesale (after a confirmation). Git comparisons also expose Stage, Unstage,
and Revert actions for the selected hunk or line, plus Copy Hunk and Open Changed
Line.

Applies route through an undoable buffer, so the toolbar's **Undo** and **Save**
act on them and nothing is committed to disk until you save. Which side is
editable depends on the comparison (the working file for vs-HEAD and vs-commit,
your local file for compare-with). Line endings and final-newline state are
preserved, and stale local content is rejected rather than overwritten.

For a local comparison, **Edit Local Result** opens a syntax-highlighted Result
draft below the diff. Editing the draft re-diffs after a short pause; applying
it changes the real editor as one guarded, undoable operation. It is never saved
implicitly.

## Merge conflicts

When Git has unmerged index stages, `merge.resolve` opens a true three-way
resolver using the common ancestor, ours, and theirs. Independent and identical
edits combine automatically; divergent regions offer Base, Ours, and Theirs
choices above an editable Result. Applying is undoable and protected against a
buffer that changed while the resolver was open.
