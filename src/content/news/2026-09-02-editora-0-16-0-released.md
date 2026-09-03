---
title: "Editora 0.16.0: navigate your project on a canvas"
description: "A new Project Map turns the file hierarchy into a focused, zoomable canvas with directional keyboard navigation, live filters, and a floating code preview."
date: 2026-09-02
version: "0.16.0"
---

**Editora 0.16.0** is out. Its headline feature is the **Project Map**, a new
canvas navigator for moving through a codebase spatially without giving up the
traditional file tree. Download it from the
[releases page](https://github.com/adriandeleon/Editora/releases/tag/v0.16.0).

## The file tree gets a second view

The Project tool window now has a **Tree / Map** switch. Tree is the familiar
hierarchical explorer. Map lays the current path across a canvas as focused
Miller-style columns: open a directory and its children appear at the next
depth, while the ancestors that got you there stay in view.

That single-branch rule matters in a real repository. Expanding a sibling
replaces the branch below the previous sibling instead of folding unrelated
subtrees into a dense graph of crossing lines. The result reads as a path, not
as a diagram you have to untangle.

The layout can flow **left to right, right to left, top to bottom, or bottom to
top**. The connectors, column placement, and arrow-key semantics rotate
together, so navigation still feels natural whichever shape fits beside the
editor. Pan by dragging the canvas, zoom around the pointer with the wheel, or
use Fit, Center selection, Reset, and the overview to get your bearings.

## Preview before opening

Single-click a file and a movable, resizable, syntax-highlighted preview floats
over the map. It uses the current unsaved buffer when that file is already open;
otherwise it loads in the background. Double-click the node or choose **Open**
to promote it to a normal editor tab.

Folders expand with one click. File and folder nodes use the same themed icons
as the tree, mark files already open in an editor, and show path, type, size,
modification time, and editor or Git status in their tooltips. Right-clicking a
node opens the same file-management menu as the tree, including New, Maven,
rename, delete, reveal, terminal, Local History, and Git actions where they
apply.

## Filter the view, keep the context

The Project tool window's search box becomes the map's global fuzzy filter.
Status chips narrow the canvas to files that are open, modified, or changed in
Git, and a type selector focuses source, markup, configuration, or other files.
Every non-root column has a fuzzy name filter and a hidden-file toggle of its
own.

Global filtering fades unrelated nodes rather than erasing all spatial context.
Columns size themselves to their loaded names, can be dragged independently,
and can be locked against accidental movement.

## Made for the keyboard too

Arrow keys move along the selected flow and among siblings. `Ctrl-N` /
`Ctrl-P` step through a column, `Page Down` / `Page Up` jump by ten,
`Backspace` selects the parent, and `Home` returns to the project root.
`Alt-Left` / `Alt-Right` walk selection history, `/` focuses the current
column's filter, and `Escape` fits the visible map.

The map is deliberately an additional view over the same project—not a second
file-management model. It shares the project root, search, filesystem watcher,
Git and editor state, sorting, icons, and actions with the tree.

## Faster work behind the canvas

0.16.0 also changes how Editora handles background editor work. Highlighting,
previews, completion, TODO scans, LSP synchronization, Undo History, and lint
now share one settled-edit timer sequence per buffer and one whole-document
snapshot per revision. Text files perform disk I/O off the UI thread, while CSV
styling and slow external preview renders coalesce stale work instead of
processing it after the document has moved on.

Preferences now live in `settings.json`, with project toolchain overrides in
`.editora/settings.json`; existing TOML settings migrate automatically. Find in
Files also has one consistent toolbar toggle, and recent files now include their
project names.

The complete list is on the [What's New](/whats-new) page.
