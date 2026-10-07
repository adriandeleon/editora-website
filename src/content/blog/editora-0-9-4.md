---
title: "Editora 0.9.4: Expert mode"
description: "A lighter focus mode than Zen that keeps the full editor view, plus build-tool tasks tool windows and a few workspace conveniences."
date: 2026-07-13
author: Adrián De León
tags: [release]
---

0.9.4 is a smaller release. The main addition is a focus mode that hides the
window chrome but keeps the editor view.

## Expert mode

Editora already had [Zen mode](/docs/workspace#focus-modes) for distraction-free
writing, which hides essentially everything. That's great for prose, but for
coding you often still want the line numbers, the minimap, and the status bar,
which tell you *where* you are.

**Expert mode** is the middle ground. It strips only the surrounding window
chrome (the toolbar, tab bar, breadcrumb, tool stripes, and whitespace guides)
but keeps the whole editor view: line numbers, status bar, minimap, column ruler,
and the current-line highlight.

You can toggle it with `C-c C-e`, the command palette (*View: Toggle Expert
Mode*), Settings → Interface → Modes, the `--expert` launch flag, or the floating
**"E"** button in the top-right (mirroring Zen's "Z"). Expert and Zen are mutually
exclusive, and like Zen it's per-window and never touches your saved settings, so
leaving it restores everything exactly.

## Build tools become tool windows

The build-tool integration (Maven, npm, Cargo, Go, Gradle) moved from
main-toolbar icons to **IntelliJ-style tasks tool windows**. Each detected tool
gets a stripe whose panel is a browsable tree of its goals, scripts, or targets,
with a mini toolbar to run, reload, stop, or run a custom task, and the output
streams to a separate per-tool console. The searchable actions popup is still a
palette command away. Detection and execution are unchanged; the tasks have more
room than a toolbar button gave them. See [Build tools](/docs/build-tools).

## Workspace conveniences

- **File Templates** can now write straight into a folder you pick (a new Folder
  field in the wizard), and invoking *New From Template…* from a project folder
  pre-fills it.
- The **Project tool window** gained a *New Folder…* action and a right-click
  menu on the project root (create, reveal, terminal, local history, Git
  stage/revert), with rename left off the root so you can't move the whole
  project by accident.

## Fixes

The Cargo, Go, and Gradle tool-window icons had been rendering blank because
their vendored SVG logos used a compact arc syntax JavaFX's SVG parser can't
read, so they're now Material glyphs, with a test guarding against un-parseable
paths. And installing the Typst language server (tinymist) from the banner now
activates it; the extracted binary's path wasn't being saved, so
detection kept failing right after a "successful" install.

## Get it

Download from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The
full changelog is on the [What's New](/whats-new) page.
