---
title: "Editora 0.18.3: steadier Project Map navigation"
description: "Editora 0.18.3 adds independent Project Map zoom and column-focus options, improves C-x 1 for tool windows, and keeps navigation targets clear of sticky headers."
date: 2026-09-11T20:25:38-06:00
version: "0.18.3"
---

**Editora 0.18.3** is out. This patch release makes spatial project navigation
more predictable and removes two small sources of friction when moving around
the editor. Download it from the
[0.18.3 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.3).

## Keep your Project Map view

The Project Map now has two independent, default-on session options:

- **Keep current zoom** preserves your scale when you open a folder.
- **Focus new column** centers the column that was just created.

Turn either option off without changing the other. Disabling zoom preservation
restores the previous fit-to-content behavior as the map expands.

## Smoother keyboard navigation

In Emacs mode, `C-x 1` now closes every open tool window when the editor is
already unsplit, including floating tool windows.

Structure, Bookmark, and Personal Note navigation also keeps its destination
visible below sticky-scroll headers, instead of allowing the selected line to
land underneath the overlay.

The complete list is on the [What's New](/whats-new) page.
