---
title: Projects, windows & files
description: Projects and multi-window, tabs and splits, Zen and Simple UI modes, local file history, and external-change detection.
category: Workspace
order: 1
---

## Projects

A project is a VS Code-style single-folder workspace: a root folder plus its
**own saved session** (open files with carets and pins, the active tab, folds,
and tool-window layout). Projects are **off by default**; enable them in
Settings.

| Action | Command | Default key |
| --- | --- | --- |
| Open a folder as a project | `project.open` | `C-x C-p` |
| Switch project | `project.switch` | `C-x p` |

The **Project** tool window shows the folder tree with keyboard navigation, a
filter that runs a bounded project-wide filename search, per-file-type icons, and
right-click actions: new file, **new folder**, new from template, rename, reveal,
open terminal, local history, and Git stage/revert. The **project root has its
own menu** too (with rename omitted so you can't move the whole project).
Bookmarks and notes are scoped per project. Closing a project returns you to the
global, no-project session.

With no project open, the Project tool window doesn't sit empty: it becomes a
**Current Folder** explorer rooted at the active file's parent directory, and
follows the focused tab as you switch files.

## Multiple windows

When projects are enabled, **each project opens in its own window**, with its own
tabs, tool windows, and session. The window's project picker acts as a window
switcher: choosing a project focuses or opens that window. The set of open
windows is remembered and restored on the next launch. With projects disabled,
Editora stays a single window.

## Tabs and splits

Tabs are draggable to reorder and can be **pinned**. The tab strip, the
switcher, and the Open Files picker all show the same unsaved-file marker. Close
the last tab and the editor is left empty (it doesn't recreate an Untitled
buffer).

Split the editor into two panes:

| Split | Command | Default key |
| --- | --- | --- |
| Side by side | `view.splitVertical` | `C-x 3` |
| Stacked | `view.splitHorizontal` | `C-x 2` |
| Unsplit | `view.unsplit` | (palette) |

## The Welcome page

With no session to restore, Editora opens a **Welcome** page (a real tab) with
New / Open / Open Folder / Clone actions (each labeled with its shortcut), your
recent files, and version and license info. Reopen it with `view.welcome`.

## Focus modes

- **Zen mode** hides the chrome for distraction-free writing, with a small
  floating "Z" to exit. Toggle it from the palette or start with the `--zen`
  flag.
- **Expert mode** is a lighter focus mode than Zen: it strips only the window
  chrome (toolbar, tab bar, breadcrumb, tool stripes, whitespace guides) but
  **keeps the full editor view**, line numbers, status bar, minimap, column
  ruler, and current-line highlight, so you stay oriented. Toggle it from the
  palette (`view.toggleExpert`), `C-c C-e`, Settings → Interface → Modes, the
  floating "E" button, or the `--expert` flag. Expert and Zen are mutually
  exclusive, and like Zen it's per-window and never changes your saved settings.
- **Simple UI mode** strips the editor to the essentials: it hides the extra
  toolbar groups, the tool-window stripe, the breadcrumb, the gutter, and the
  minimap, and turns off the heavier features (LSP, debugging, Git, multiple
  cursors) for a calm surface. Toggle it from Settings, the toolbar, the palette
  (`view.toggleSimpleMode`), or the `--simple` flag (session-only). Toggling off
  restores everything.

You can also hide the toolbar, the tool stripe, the breadcrumb, and the minimap
individually in Settings.

## Local file history

Editora snapshots your local files over time, independent of any version
control, so you have a safety net even outside Git. A snapshot is taken on save,
on auto-save, and before a file is reloaded after an external change.

The **File History** tool window (`M-g l`) lists each revision with its date,
the reason it was taken, and its size (the latest tagged *Current*).
Double-click a revision for a diff against the current file, then restore the
whole revision or use the **apply-chevrons to copy individual fragments** back in
(undoable).

It mirrors more of IntelliJ's Local History:

- **Named snapshots** with *Put Label* (`history.putLabel`), shown bold in the
  list even when the content is unchanged.
- A **filter** over the revision list, plus a project-wide **Recent Changes**
  picker (`history.recentChanges`).
- A **folder view**: right-click a folder in the Project tree to list every file
  under it that has history, with **deleted files badged**; restore a revision to
  recreate the file. Deleting a file in Editora snapshots it first, so an
  accidental delete is recoverable (for files Editora had opened or edited).

Snapshots are deduped by content and stored gzip-compressed in your config
folder, pruned by configurable limits. It's on by default, local-only, and off in
Simple UI mode.

## External-change detection

When a file changes on disk under you, Editora notices on window focus and tab
switch and prompts to reload or keep your version. The Project tree also
re-scans on focus so files added or removed outside the editor show up, keeping
your expanded folders and selection.
