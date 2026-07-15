---
title: Git
description: "The built-in Git integration: branch info, change bars, commits, log, blame, and stash."
category: Version control
order: 1
beta: true
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
- The **Project tree marks files by Git status**, IntelliJ-style: a single-letter
  prefix (M / A / D / R / U) and a color, added (green), modified (blue), deleted
  (gray), renamed (violet), untracked (olive), with changed folders tinted. The
  Commit window's rows use the same letters and colors, so the two read
  identically. It updates as you edit, stage, commit, or switch branches.
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

For comparing files and resolving merge conflicts, see
[Diff & merge](/docs/diff-merge).
