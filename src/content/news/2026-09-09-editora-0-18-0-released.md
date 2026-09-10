---
title: "Editora 0.18.0: Astro support and notes on the map"
description: "Astro syntax and language-server support, folder bookmarks and notes, connected Project Map cards, and a broad reliability pass arrive in Editora 0.18.0."
date: 2026-09-09
version: "0.18.0"
---

**Editora 0.18.0** is out. This release adds first-class Astro support, makes
bookmarks and Personal Notes useful at the folder level, and turns Project Map
notes into editable cards that stay visually connected to their source.
Download it from the
[0.18.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.0).

## Astro from highlighting through code intelligence

Open an `.astro` file and Editora now recognizes it as Astro, highlights its
frontmatter, HTML, and CSS with the official TextMate grammar, and extracts
symbols for the Structure window.

The configurable `astro-ls` integration brings completion, diagnostics,
navigation, and the rest of Editora's language-server workflow. Editora detects
the server automatically, offers one-click npm installation when it is missing,
and locates the TypeScript SDK that Astro's server needs—even when the SDK is
hoisted in the workspace or installed alongside the server.

## Put context on folders, not only files

Bookmarks and Personal Notes can now be attached directly to folders from the
Project tree. Their indicators sit after the file or folder name, where they do
not compete with the path itself.

Folder notes appear as lightweight tooltips while browsing. In the Bookmarks
and Notes windows, folder entries use folder icons and activate the Project
explorer with the owning folder selected, so project-level context leads back to
the right place.

## Personal Notes become part of the Project Map

Personal Notes badges on the canvas now open independent, editable cards for
files and folders. A note can stay open beside the same file's code preview,
with a live connector back to its source row while you pan, zoom, move, or resize
the card. A default-off **Hide all open Personal Notes** toggle clears the canvas
temporarily without closing anything.

The map can also filter its working set to bookmarked paths or paths with
Personal Notes, making annotations useful as a focused way through a large
project.

## Reliability and interface polish

The Welcome page keeps long Recent lists compact behind an accessible
disclosure control, the Quit confirmation makes its destructive action visually
clear, and Personal Notes previews share a soft paper-like color across the
editor and Project tree.

Underneath, save, history, replace, search, preview, process, and shutdown paths
received a broad concurrency and lifecycle pass. Editor overlays now respect
HiDPI texture limits, and the HTML **Open in Browser** control no longer overlaps
the minimap.

The complete list is on the [What's New](/whats-new) page.
