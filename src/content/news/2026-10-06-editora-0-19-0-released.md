---
title: "Editora 0.19.0: faster on large files, safer with untrusted ones"
description: "Editora 0.19.0 makes highlighting incremental, opens large files without freezing, lets a debugged Java program read its input, and hardens what an opened file, repository, or link is allowed to do."
date: 2026-10-06T15:12:35-06:00
version: "0.19.0"
---

**Editora 0.19.0** is out. This release comes out of a review of the whole
editor: it is faster where large files and busy consoles used to stall it,
stricter about what a file you merely opened is allowed to do, and it fixes a
long list of editing, saving, and debugging bugs. Download it from the
[0.19.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.19.0).

## Faster where it used to stall

Highlighting after an edit now re-tokenizes only the lines the edit affects. In
a 79,000-line Java file, an edit near the top settled in about half a second
instead of five to seven.

Large files open without freezing the window. A 49 MB file used to block the
interface for about three seconds and now stays under a second, and a loaded
file keeps one copy of its text in memory instead of two. Find in a very large
file runs in the background.

A build or program that prints quickly no longer monopolizes the interface:
200,000 build lines that froze the window for about a minute now stream in about
13 seconds with the window responsive. Restoring a session lays out only the tab
you are looking at, which in testing took about a fifth of the interface-thread
time and 140 MB less memory for a 30-file session.

## Opening a file does less on its own

Opening a file no longer lets its folder's `.git/config` run programs. The
status, diff, log, and blame lookups Editora runs by itself ignore hooks,
`core.fsmonitor`, and external diff drivers; the commands you start still run
your hooks.

Links now go through one check. Only `http`, `https`, and `mailto` are handed to
the system, a relative link opens inside the editor when it points into the
project, and anything else is refused. Agent replies, language-server hovers,
and pull-request text no longer load remote images. PlantUML previews run
sandboxed, the HTML Live Preview server answers only its own browser tab, and a
project's language-server commands apply only once you trust the folder.

Jackson and Apache MINA SSHD were updated for three published CVEs.

## Debugging Java programs that read input

The Debug console now feeds the running program's standard input for Java
launches, so a debugged program that reads `System.in` no longer waits forever.
While the program runs, Enter sends the line to it; while it is paused, Enter
evaluates an expression as before. See
[Program input in the Debug console](/docs/run-debug#program-input-in-the-debug-console).

A breakpoint the debugger has not verified is drawn as a hollow ring with the
reason on hover, and the call stack, variables, and console sit side by side
when the panel is wide enough.

## Editing and saving

- Both panes of a split view share one undo history.
- A right-click outside the selection moves the caret to the click, so the menu
  acts on the place you clicked.
- Move Line and Duplicate Line act on every line of a selection, and Fill
  Paragraph in code fills comments only.
- A read-only file is never replaced silently: Save asks first.
- Save As on a remote (SFTP) tab writes a local copy, even after the connection
  is gone.
- Key-binding overrides are kept per keymap, and shortcuts are shown in the
  notation of the active keymap and platform.
- Local File History follows a renamed or moved file, and two new commands
  delete a file's or a project's history for good.

## Installers

Installers and archives now carry the license texts. The Windows MSI no longer
registers script types such as `.bat`, `.ps1`, `.py`, and `.sh`, so a
double-click keeps running the script instead of opening Editora. On Linux, the
`.deb` no longer discards other applications' defaults, and the tarball's
`install.sh` leaves a root-owned install.

This release's experimental Native Image archives cover Linux x64 and macOS x64.
The regular installers remain the recommended downloads.

The complete list, which is long, is on the [What's New](/whats-new) page.
