---
title: "Git integration"
group: "Git & diff"
order: 1
beta: false
summary: "Native Git: status-bar branch, gutter change bars vs HEAD, a Commit tool window, merges and rebases with a conflict resolver, branches, remotes, tags, stashes and patches, a paged log with a commit graph, and blame."
description: "Git through your own git: branch in the status bar, gutter change bars, a Commit window, merge conflict resolver, paged log with a graph, and blame."
alt: "The Commit window beside a Java file: one changed file, two untracked, and a commit message typed"
---

Do your everyday Git work without leaving the editor. Editora runs your own installed `git`, so it behaves exactly like the command line, with your own config and credentials.

## See what changed

- **The branch in the status bar**, with ahead and behind counts and a menu to switch or create branches, pull, fetch and push.
- **Change bars in the gutter** mark added, changed and deleted lines. Click one to see the old lines and revert or stage that hunk.
- **The Project tree is coloured by status**: added, modified, deleted, renamed and untracked files, with changed folders tinted.

## Commit

- **The Commit window** (`M-4` in the Emacs keymap) groups staged, changed and untracked files, with stage, unstage, discard and a message box.
- **Commit options**: Amend, Commit and Push, Sign off, and Undo Last Commit.

## Merge, rebase and sync

- **Operations in progress**, such as a merge, rebase, cherry-pick or revert, show in the status bar and the Commit window with Continue, Skip and Abort.
- **Conflicts** get their own group and open in the [three-way resolver](/features/diff-merge/). Finishing a resolution stages the file.
- **Pull your way**, fast-forward only, rebase or merge, and push with lease or to another remote.
- **Manage branches** from the status-bar menu: rename, merge, rebase onto, set upstream, compare and delete.
- **Remotes, worktrees, tags, stashes and patches** each have their own commands.

## Explore history

- **The Git Log** (`C-x v S-l` in the Emacs keymap) pages through the whole history with a commit graph. Search by message, author, content, date or path, and press Enter on a commit to review everything it changed.
- **Blame** (`C-x v g` in the Emacs keymap) shows who last changed each line. It can ignore whitespace, follow moved lines, and walk a line back through earlier revisions.

## Know what it ran

- **A Git transcript** in the Output console lists every command you asked for, with its output, exit code and duration. Clone, fetch, pull and push show progress there, with a Stop button.
- **Remote folders work too.** In a [remote folder](/features/remote-sftp/), the host's own `git` runs over the same SSH connection.

Nothing appears until `git` is found. The other keymaps' chords are on the [keybindings](/keybindings/) page.

**On by default.** Turn it off in **Settings → Version Control → Git**. See the [Git guide](/docs/git/#git-integration).
