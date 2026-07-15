---
title: "Editora 0.9.5: safer by default"
description: "A release that's mostly fixes, the unglamorous kind that keep your files, your config, and your machine safe, plus a mini file manager in the Project tree."
date: 2026-07-15
author: Adrian De Leon
tags: [release]
---

Not every release is about new features. 0.9.5 is mostly the other kind of work:
making sure the editor never loses your data, never leaks anything, and cleans up
after itself. Here's what changed and why it matters.

## Don't lose the file

Saving used to truncate the file before writing the new contents, which means a
crash or a power loss at the wrong moment could leave you with an empty or
half-written file. Saves are now **atomic**: the new content is written to a temp
file and moved into place, so an interruption leaves the original intact.

Auto-save got two guardrails. It can no longer **undo a manual save** (hitting
Ctrl/Cmd-S just as a queued auto-save fired could race), and it no longer
**overwrites a file that changed on disk** in a background tab. Save-As now
applies the destination folder's `.editorconfig` rather than the old location's,
and if your file's encoding can't represent a character you typed, Editora tells
you instead of silently writing a `?`.

## Don't lose the config

The same atomic-write treatment now protects your bookmarks, notes, breakpoints,
and session, a crash or a full disk while saving them can't wipe them out. A
couple of sharper edges are gone too: running an older Editora after a newer one
can't clobber the newer config, a deleted project can't quietly come back from a
queued save, and quitting no longer discards the unsaved work and session of
every window except the one you closed.

## Don't leak, don't freeze

A handful of security fixes. Previewing an **untrusted Markdown file** can no
longer be used to probe your internal network or leak credentials through a
crafted image or link. A **malicious file template** can't write outside the
folder you targeted. And a **catastrophic regex** in Find, the kind that takes
exponential time on the wrong input, no longer freezes the editor.

## Clean up after yourself

Closing a window now shuts down everything it started, the diff, external-tool,
and HTTP-client workers, plus any language servers and debug adapters. And
stopping (or quitting during) a run or a build now kills the **whole process
tree**, so a `npm run dev` or `mvn` invocation doesn't leave orphaned processes
behind.

## The one visible change

The **Project tool window** grew up into a small file manager. You can
multi-select files and folders with Ctrl/Cmd- and Shift-click, then drag them
onto a folder to move them, with your open tabs following to the new path, and
Delete now acts on the whole selection at once. It also shows a **single-letter
Git status** (M / A / D / R / U) on each changed file, matching the Commit
window, so the two read the same way.

## Get it

Download from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The
full changelog, including the couple dozen fixes not called out here, is on the
[What's New](/whats-new) page.
