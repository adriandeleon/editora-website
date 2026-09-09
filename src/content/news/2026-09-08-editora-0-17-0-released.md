---
title: "Editora 0.17.0: a serious diff workspace"
description: "Multi-file Git reviews, recursive folder comparisons, editable result drafts, smart alignment, and true three-way merge resolution turn Editora's diff viewer into a complete review workspace."
date: 2026-09-08
version: "0.17.0"
---

**Editora 0.17.0** is out. This release turns the diff viewer from a useful
two-file comparison into a complete review workspace. Download it from the
[0.17.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.17.0).

## Review a change, not one file at a time

Open every staged file or every unstaged and untracked file as one navigable
review, directly from the Commit tool window or command palette. Multi-file
patches get the same treatment: a resizable file list, status and line counts,
and one consistent set of next/previous-change controls.

Git reviews now act on the selected hunk or line. **Stage**, **Unstage**, and
**Revert** sit alongside **Copy Hunk** and **Open Changed Line**, so reviewing and
preparing a commit can happen in the same place.

## Compare whole folders

Right-click a Project-tree folder to compare it recursively with HEAD, a branch,
tag, or chosen revision. You can also compare any two directories. Editora scans
in the background, respects both roots' Git ignore rules, skips `.git`, and opens
only changed, left-only, or right-only files in a lazy review.

The CLI version, `editora --diff-ui LEFT RIGHT`, now starts a clean,
session-free comparison workspace for either files or directories. Its Editora
button restores the normal application chrome without throwing the comparison
away.

## A diff that helps you read

Side-by-side views draw change ribbons across the center seam and add a compact
overview track at the right edge. Smart alignment pairs related lines around an
insertion or removal instead of cascading mismatches down the page. You can also
ignore case, choose whitespace rules, collapse context, wrap long lines, swap
sides, emphasize changed words, and export the result as a patch.

Refreshes keep your selected change, scroll positions, focused side, and divider
position. Highlighting happens off the UI thread, large and binary inputs
degrade safely, and CRLF and final-newline state survive both display and apply.

## Edit the result—and resolve real merges

Local comparisons can open a syntax-highlighted **Result** draft below the
diff. As you edit, the comparison updates after a short pause. Applying the
draft is one guarded, undoable editor change; it is never saved behind your
back.

Merge resolution now reads Git's base, ours, and theirs index stages. Compatible
edits combine automatically, while divergent regions offer Base, Ours, and
Theirs choices above an editable result.

## Smaller improvements around the editor

Run configurations have moved out of application-wide Settings into their own
project/session-scoped window and now support named npm scripts. The Run console
colors log levels, makes web links clickable, and includes the normal copy menu.

The Project Map previews bitmap images, keeps separate text and image zoom, and
reveals the ancestor columns for filename-search matches. Project Tree and Map
menus can add first-line bookmarks and Personal Notes, with indicators on the
file. Context menus now use consistent typography throughout the application.

The complete list is on the [What's New](/whats-new) page.
