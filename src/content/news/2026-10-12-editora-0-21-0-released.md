---
title: "Editora 0.21.0: a faster editor and a reworked Local History"
description: "Editora 0.21.0 does less work per keystroke, per tab switch and at startup, uses less memory per open file, reworks Local History, makes code lenses clickable, and confines the MCP server's open_file tool to the project."
date: 2026-10-12
version: "0.21.0"
---

**Editora 0.21.0** is out, four days after 0.20.0. Most of it is a performance
pass over the whole editor and a rework of Local History. It also makes code
lenses clickable, folds the branch dropdown's sections, and closes a gap in the
MCP server. Download it from the
[0.21.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.21.0).

## Performance

Nothing here changes what the editor does, only how much work it does and when.

- **Typing** re-measures only the line you edited instead of every visible
  line, and the toolbar no longer compares the document on each keystroke.
- **Switching tabs** does not scan the document for fold regions on the UI
  thread. A plain buffer builds its overlays, right-click menu and popups on
  first use, and a tab left in the background for three minutes releases the
  extra copies it holds.
- **Opening files**: session restore, background opens and Reload from Disk
  split the file into lines on a worker thread. A reload rewrites only the part
  of the document that changed, so folds and highlighting elsewhere stay put.
- **Session restore** builds an editor only for the file you were looking at
  before the first frame. The other tabs show their name and icon at once and
  are built afterwards, or when you click them.
- **Startup** waits for the first painted frame before probing for `rg`, `gh`
  and the login-shell PATH, building the spell dictionary, and checking for a
  new release. Stylesheets ship precompiled.
- **Memory**: a large plain-text file takes about one file-size less heap (a
  49 MB file went from 13.3 to 11.5 times its size), a modified tab no longer
  keeps a second copy of its document, and the undo history of all open
  documents shares one budget, with the least recently edited document giving
  up its oldest steps first.
- The **minimap** draws one row per pixel row on long documents and keeps a
  background tab's pixels, so switching back does not re-render.

## Local History

The tool window that lists a file's saved revisions is called **Local History**
everywhere, and Git's per-file log is **Show Git History**. Both have a
shortcut in every bundled keymap, and the menu bar has a
**VCS ▸ Local History** submenu.

The panel was reworked. Rows are compact, with day captions, and the newest
revision is tagged *Current* only when it matches the editor's text. The diff
follows edits you make in the editor, row actions work from the keyboard, and
a folder's history shows a diff for the selected revision.

Restoring a revision of an open file goes into its tab as an edit you can
undo, instead of rewriting the file on disk underneath it.

What gets recorded changed too. One large file cannot push other files'
history out: over the project's size limit, a file above its fair share loses
its own older revisions first. Auto-saves a few minutes apart collapse into one
revision, an unchanged save adds nothing, and the content a file had before
its first save is kept as *Before first save*. A file's history is the same in
every window.

Several ways Local History could lose revisions without saying so are fixed,
among them a second Editora process deleting the first one's revision content
after it exited. Export Configuration no longer includes the text of every
file revision. See [Local History](/docs/workspace#local-file-history).

## Git

In a file's Git history, Enter opens what the commit changed in that file, and
the header is a chip whose ✕ (or Escape) returns to the branch log. A file
history used to stop at its first 200 commits whenever other commits lay
between the file's own; Load More now continues, and a search finds commits
from before a rename.

The branch dropdown's *Actions*, *Local* and *Remote* sections fold, and a
folded header shows how many rows it hides. The dropdown is larger, lists the
actions above the branches, and opens with the current branch selected. See
the [Git guide](/docs/git).

## Code lenses and references

A code lens reacts to the mouse and a click opens what it counts. A references
lens always opens the References tool window, including for a single reference
and for "0 references". The References tool window colors each reference's
line like the editor does, in the active color theme. See the
[language features guide](/docs/lsp).

## Security: MCP `open_file`

An MCP client could name any absolute path, have the editor open it, and then
read or rewrite it through the buffer tools. `open_file` now opens a file only
when it is inside the project of the window that answers, or already open
there; a window with no project opens nothing new for a client. `edit_buffer`
and `save_buffer` refuse a buffer in the editor's configuration directory or
in version-control metadata. Files you open yourself stay available to the
client. If you run the MCP server, update. See the [MCP guide](/docs/mcp).

## Also in this release

- **Snippets**: a trigger typed out in full is expanded by Tab instead of being
  taken over by a dictionary, language-server or AI suggestion.
- **Settings sync** keeps working when you switch GitHub CLI accounts with
  `gh auth switch`, and no longer starts over on every sync when a Git
  `insteadOf` rule rewrites the repository URL.
- **Language servers and debug adapters**: a failed or incomplete download no
  longer replaces the installed version.
- **Bookmarks**: setting a mnemonic in a narrowed buffer no longer deletes the
  project's bookmarks.
- **Project tool window**: Enter in the filter opens the first match, and a
  folder you close and reopen lists what it holds now.
- **Test Results**: class rows have a right-click menu with Go to Test Class,
  Rerun This Class and Debug This Class.
- **macOS**: a save Editora made itself is no longer taken for a change by
  another program, and Enter reloads in the "modified outside Editora" prompt.

The complete list is on the [What's New](/whats-new) page.
