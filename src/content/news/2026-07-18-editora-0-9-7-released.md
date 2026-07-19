---
title: "Editora 0.9.7: performance and polish"
description: "Work that was quietly happening on the UI thread moves off it, so typing, scrolling, and zooming stay smooth, plus colored consoles, a project-grouped Bookmarks and Notes, and a palette that shows what it's hiding."
date: 2026-07-18
version: "0.9.7"
---

**Editora 0.9.7** is about how the editor *feels*: the release chases down the
places where routine work was happening on the UI thread and moves it off, and
adds a round of visual polish on top. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**Performance.** Opening the first file of a language no longer freezes the
editor while its language server starts up, that work moved to a background
thread. Text zoom is much smoother (a `Ctrl`+wheel notch used to re-run the
entire settings cascade and re-detect every language server; now it only touches
the fonts). Typing and arrow-keying in a large file are lighter, since the status
bar no longer re-scans the whole document on every keystroke. And the Structure
outline and File Information panel do nothing at all while their tool windows are
closed, instead of rebuilding on every edit.

**Polish.** The Run, External Tools, and Debug consoles now **tint error output**
(a Python traceback stands out from normal output). The **Git Log** window is
colored and uses the real file-type icons, matching the Commit window and Project
tree. The command palette now **shows commands that can't run right now, grayed
out** rather than hiding them, so you can see a feature exists. The build-tool
task panels open on the **right** by default, and the gutter fold arrow is 35%
larger.

**Bookmarks and Notes group by project.** Both panels always show the **General**
(no-project) group plus your **current project**, with a **Show all projects**
toggle that reveals and jumps to every other project's marks, so they no longer
appear and vanish as you switch projects. See
[Bookmarks & notes](/docs/bookmarks-notes).

**Fixes** include per-platform keybindings (a config synced between a Mac and a
PC no longer double-binds), plugin class loaders that no longer leak jar handles
(and no longer block uninstall on Windows), two SFTP windows that no longer
strand each other's remote files, text zoom that now works in diff views and on
the Welcome page, and one Ctrl-Z that fully reverts a repeated snippet field. The
full list is on the [What's New](/whats-new) page.

There's a [0.9.7 blog post](/blog/editora-0-9-7) on the performance work, and the
complete changelog is on [What's New](/whats-new).
