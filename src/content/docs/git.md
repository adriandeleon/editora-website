---
title: Git, diff & merge
description: The built-in Git integration, the diff viewer, and the merge-conflict resolver.
category: Version control
order: 1
---

Editora's Git support shells out to your installed `git`, with no bundled
library. It's **off by default**; turn it on in **Settings → Git**. If `git`
isn't installed, the integration stays inert.

## Git integration

- The **status bar** shows the current branch with ahead/behind counts. Click it
  for a dropdown to switch or create branches, pull, fetch, and push. Outside a
  repo it reads "No VCS" and offers to clone.
- **Gutter change bars** mark added, modified, and deleted lines against HEAD.
  Hover a bar for that hunk's diff.
- The **Commit** tool window (`M-4`) groups staged, changed, and untracked files
  with stage, unstage, discard, and a commit box (Ctrl/Cmd+Enter to commit).
- A **Git Log** tool window (`M-g h`), the active file's history
  (`git.fileHistory`), **inline blame** (GitLens-style, current line; toggle
  with `git.toggleBlame`), and **stash** (`git.stash`, pop, drop).

| Action | Command | Default key |
| --- | --- | --- |
| Commit (open the Commit window) | `git.commit` | `C-x g` |
| Switch branch (dropdown) | `git.switchBranch` | (status bar) |
| Fetch / pull / push | `git.fetch` / `git.pull` / `git.push` | (palette) |
| Clone a repository | `git.clone` | (palette) |
| Refresh | `git.refresh` | (palette) |

Cloning asks for a URL and a destination, then opens a file from the clone (its
README if present) so Git activates without creating a project. Branch switch,
pull, and push reload unmodified open buffers whose files changed on disk.

## Diff viewer

Compare files in a dedicated tab, **side by side** or **unified**, with per-line
backgrounds and intra-line word-level highlights.

| Compare | Command | Default key |
| --- | --- | --- |
| Against HEAD | `diff.vsHead` | `C-x v =` |
| Against a commit | `diff.vsCommit` | (palette) |
| With another file | `diff.compareWith` | (palette) |

The Git-backed comparisons need Git on; comparing two files does not. When a
file is open, its live (possibly unsaved) text is used as the working side. Open
diffs **refresh live** when the underlying files change on disk or after a Git
mutation.

**Apply changes hunk by hunk** with the gutter arrows on the editable side (a
single chevron per changed row, a double chevron for a whole hunk), or
**Apply all** from the toolbar. Applies route through an undoable buffer, so the
toolbar's Undo and Save act on them and you can revert.

## Merge conflicts

When a file has Git conflict markers, `merge.resolve` opens the **merge
resolver**, which lists each conflict with **Accept Ours / Theirs / Both**. Save
writes the resolved result back to the buffer (undoable).
