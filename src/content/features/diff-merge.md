---
title: "Diff & merge"
group: "Git & diff"
order: 3
beta: false
summary: "Compare files, folders, patches and Git changes side by side or unified, apply changes hunk by hunk, and resolve merge conflicts three ways."
description: "Side-by-side and unified diffs of files, folders, patches and Git changes, with word emphasis, hunk actions and a three-way merge resolver."
alt: "A Java file side by side with its HEAD version, the changed line and words highlighted"
---

See exactly what changed, take the parts you want, and settle merge conflicts without hand-editing conflict markers.

## Compare anything

- **A file against Git**: **Diff: Compare with HEAD** (`C-x v =` in the Emacs keymap), or against a commit, branch or tag. Unsaved edits count.
- **Two files or two folders**, or a file against the clipboard or empty text.
- **A whole change set**: every staged or working-tree change, or a `.patch` file, opens as one review you step through file by file.

## Read the changes

- **Side by side or unified**, with the changed words highlighted inside each line and an overview of the whole file at the right edge.
- **Cut the noise**: ignore whitespace or case, collapse unchanged lines, wrap long lines, or swap sides.
- **Line up related lines** with smart alignment, so a changed line sits beside its old version even when lines were inserted or removed around it.

## Apply what you want

- **Take a change** into your file with the gutter actions, each one undoable, or edit a result draft and save it.
- **Work with Git** from the diff: stage, unstage or revert a hunk or a single line, copy a hunk, or open the changed line in the editor.
- **Export** the comparison as a patch.

## Resolve conflicts

**Merge: Resolve Conflicts** opens a three-way resolver from Git's base, ours and theirs. Compatible edits combine on their own, each remaining conflict offers Ours, Theirs, Both or Base, and a complete resolution saves and stages the file. Edits on neighbouring lines count as one conflict, as in Git, so nothing is merged out of sight.

See the [Diff and merge guide](/docs/diff-merge/).
