---
title: "Editora 0.9.5: a hardening release"
description: "A big round of data-safety and security fixes, crash-safe saves, protected auto-save, and config integrity, plus the Project tree becomes a mini file manager."
date: 2026-07-15
version: "0.9.5"
---

**Editora 0.9.5** is mostly about making the editor safer with your files and
your data. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**Your files.** Saving is now crash-safe (written atomically, so an interruption
can't leave a truncated file), auto-save can no longer undo a manual save or
overwrite a file that changed on disk, Save-As applies the destination's
`.editorconfig`, and a character your file's encoding can't represent is surfaced
instead of silently becoming `?`.

**Your config.** A crash or full disk while saving can no longer wipe out your
bookmarks, notes, breakpoints, or session, running an older Editora can't destroy
a newer config, and a deleted project can't come back.

**Security.** Previewing an untrusted Markdown file can no longer probe your
internal network or leak credentials, a malicious file template can't write
outside its target folder, and a catastrophic regex in Find no longer freezes the
editor.

**Robustness.** Quitting or closing a window now shuts down everything it
started, and stopping a run or build kills the whole process tree, so no more
orphaned `npm run dev` or `mvn` processes.

Two things you'll actually see: the **Project tool window is now a mini file
manager** (multi-select, drag files onto a folder to move them, bulk delete), and
it shows a **single-letter Git status** (M / A / D / R / U) on each changed file,
matching the Commit window. See [Projects](/docs/workspace#projects) and
[Git](/docs/git).

The complete list is on the [What's New](/whats-new) page, and there's a
[0.9.5 blog post](/blog/editora-0-9-5).
