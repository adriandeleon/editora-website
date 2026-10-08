---
title: "Editora 0.20.0: more Git, crash recovery, and settings sync"
description: "Editora 0.20.0 handles merges, rebases, branches, remotes, tags, stashes and patches from inside the editor, keeps unsaved edits through a crash, syncs snippets and templates between computers, and reworks printing and PDF export."
date: 2026-10-08
version: "0.20.0"
---

**Editora 0.20.0** is out, two days after 0.19.0. It is a large release: the Git
integration covers most of what used to need a terminal, unsaved edits survive
a crash, a new Settings sync keeps snippets and templates the same on every
computer, and printing and PDF export were reworked after a review. Download it
from the [0.20.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.20.0).

## Git

A merge, rebase, cherry-pick or revert in progress is shown in the status bar
(`main · MERGING`) and as a banner in the Commit window with Continue, Skip and
Abort. Conflicted files get their own group, with the three-way resolver and
keep-one-side actions. A Git command that stops on conflicts opens the Commit
window instead of an error dialog.

Pull has a mode setting: fast-forward only (still the default), rebase, or
merge. A pull that cannot fast-forward offers Rebase or Merge. A rejected push
offers Pull-then-Push or Force-with-Lease, and there are commands for Force
Push (always with lease), Push to another remote, and Push Tags.

The branch dropdown manages branches: create from any branch or tag, rename,
merge, rebase onto, set upstream, compare with the current branch, and delete
locally or on the remote. There are also dialogs for remotes, worktrees, and
stashes, and commands for tags and patches.

In the editor, `Git: Next Change` and `Git: Previous Change` move between a
file's changes. Clicking a gutter change bar opens a card with the old and new
lines, where you can revert or stage that one hunk. Tabs are tinted by Git
status.

The Git Log loads history a page at a time instead of stopping at 200 commits.
It has an all-branches view with a commit graph, a details area, and a search
over the whole history by message, `author:`, `content:`, date, and `path:`.
Enter on a commit opens everything it changed as one review.

The Commit window adds Amend, Commit and Push, Sign off, Undo Last Commit,
recent messages, and a length guide for the subject and body. The Emacs keymap
moves its Git chords under `C-x v`. See the [Git guide](/docs/git).

## Crash recovery

Unsaved edits, including untitled buffers, are kept in the config folder while
you work. If Editora did not close normally, the next launch offers them back
as unsaved tabs; it never writes to your files. This is on by default. See
[Crash recovery](/docs/undo-history#crash-recovery).

This came out of a review of every path that writes, deletes or replaces user
data, which found 58 issues. Among the fixes: deleting a file from the Project
tree moves it to the trash on Linux and macOS, a file with mixed line endings
is not rewritten by a save that changed nothing, two Editora processes on one
config directory do not revert each other's settings, and an agent or language
server edit keeps a Local History copy of what it replaces. Four items are not
fully fixed and are listed in the changelog.

## Settings sync (Beta)

Settings sync keeps your snippets, abbreviations, templates and personal
dictionary the same on every computer, through a private Git repository you
own. Changes merge entry by entry, so two computers that each added a snippet
keep both. It uses your own Git credentials and stores none. Preferences,
keymaps, macros and themes are not synced. It is off by default; set it up in
**Settings → Sync**. See [Settings sync](/docs/settings-sync).

## Print and PDF

Print… and Export to PDF… are in the File menu, the tab menu and the editor's
right-click menu, with Print Selection… and Export Selection to PDF… beside
them. The Print Preview has zoom, Page Setup, a page number you can type, and
keyboard paging, and shows the sheet at paper size.

Exported PDFs have clickable links, bookmarks from the headings, and a page
footer. Orientation, margins and the code font size are settings. Printed
Markdown and CSV pages are always light, tables split between rows and repeat
their header, and long code blocks print every line. See
[Print and PDF](/docs/print-pdf).

## Java

With the Java language server, the Code Actions menu carries out Move, Extract
Interface and Change Signature. The signature is edited as one line, such as
`public String greet(Helper helper, int n)`. Generate Getters and Setters and
Generate Delegate Methods let you choose the fields or methods. Code lenses
show reference and implementation counts after a declaration (off by default),
and tests inside JUnit `@Nested` classes run from the gutter.

## Also in this release

- **Project Map**: Print and PDF output the whole map, large folders load in
  chunks instead of truncating, and the history, fit and sibling keys work in
  every keymap.
- **GitHub**: the tool window filters pull requests and issues by state, loads
  more on demand, and shows the repository, branch and account. Create Pull
  Request starts from the branch's commits and the repository's template.
- **Log viewer**: the controls are a bar above the log, a filter keeps or hides
  whole records so an exception stays with its stack trace, Follow survives log
  rotation, and more file names and formats are recognised.
- **Spell check** is set per file type, with commands for next and previous
  misspelling and for correcting the word at the caret.
- **Snippets** outline their fields while running and ship for seven more
  languages; **macros** can record text typed into prompts.
- **Templates**: New Project From Template asks for a name and location and
  opens the new folder as a project.
- Launching Editora again goes to the running editor instead of starting a
  second process; `--new-instance` starts a separate one.

This release has no experimental Native Image archives. The regular installers
are unaffected.

The complete list is on the [What's New](/whats-new) page.
