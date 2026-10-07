---
title: Git
description: "The built-in Git integration: branch info, change bars, commits, log, blame, and stash."
category: Version control
order: 1
---

Editora's Git support shells out to your installed `git`, with no bundled
library. It's **on by default**; turn it off in **Settings → Version Control →
Git** or with `view.toggleGit`. If `git` isn't installed, the integration stays
inert.

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

In a file's history, double-click a revision or press Enter to compare it with
the editable working file. The full diff viewer opens, including line, hunk,
whole-file, Result, Undo, and Save controls. The repository-wide Git Log keeps
its parent-to-commit view; use **Compare with Working Tree** on a changed file
when you want the editable comparison instead.

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
Branch switches, discard, and stash never silently replace a dirty open copy;
deleted and edited buffers remain available for recovery. Git paths are passed
literally, including names that look like options or contain unusual
characters, and conflicted entries remain visible while a merge is unresolved.
History mutations and refreshes also revalidate the active file and working-tree
state before applying their result.

Editora uses the `git` on your `PATH`; **Git: Set Git Command**
(`git.setCommand`) points it somewhere else, and a blank value goes back to the
`PATH` one.

## What runs automatically

Some Git commands run without you asking: status, the gutter diff, log, blame
and file-content lookups happen as you open files and switch tabs. Editora runs
those background reads so that a repository's own configuration cannot start a
program through them. `core.fsmonitor` and hooks are overridden, external diff
and textconv drivers are switched off, and Git never waits on a terminal prompt.
A partial clone is also told not to fetch missing objects on demand; that needs
Git 2.45 or newer (or a 2.39.4–2.44.1 maintenance release), and older versions
ignore it.

One thing is not covered: filter drivers selected through `.gitattributes`
(Git LFS, for example) still run when Git has to re-hash a modified file.

Commands you start yourself (commit, checkout, pull, push and the rest) are
not restricted, so your hooks run as they do in a terminal. A revision name
that starts with `-` is refused rather than passed to Git, where it would be
read as an option.

## Seeing what it ran

The **Output** console (`tool.buildOutput`) has a **Git** tab holding a
transcript of what Editora ran on your behalf: the command line, its output, and
its exit code and duration.

It logs the commands you *asked for* — commit, push, pull, fetch, checkout,
stash, clone — and deliberately not the `status` and `diff` reads it re-runs on
every tab switch, focus change and save, which would bury them. It never steals
focus, either: nothing jumps in front of what you were doing, and the transcript
is simply waiting when you open the window.

For comparing files and resolving merge conflicts, see
[Diff & merge](/docs/diff-merge). For pull requests, reviews, and CI runs, see
[GitHub](/docs/github).
