---
title: "Editora 0.9.1: the first release"
description: "The first tagged build of a keyboard-driven, cross-platform programmer's editor, plus the story behind why it's 0.9.1 and not 0.9.0."
date: 2026-07-06
author: Adrian De Leon
tags: [release]
---

Editora has its first tagged release: **0.9.1**, with native installers for
macOS, Windows, and Linux. This post is a tour of what's in it, and a short story
about the version number.

## What Editora is

Editora is a fast, keyboard-driven programmer's text editor, cross-platform and
open source under the MIT License. The organizing idea is that every action is a
registered command: the same command powers the palette (`M-x`), a keybinding,
and any toolbar button, so nothing is buried in a menu you can't reach from the
keyboard. You pick the keymap that fits your hands, Emacs, CUA, Sublime Text, VS
Code, or IntelliJ, and switch it live.

## What's in 0.9.1

Because this is the first release, it carries everything built so far. A few of
the areas:

- **Editing**: multiple cursors and column selection, smart auto-indent,
  auto-close brackets, comment toggling, the full set of Emacs editing and
  movement commands, string manipulation, and finer-grained undo with an Undo
  History timeline.
- **Code intelligence**: TextMate syntax highlighting for 20-plus languages,
  language servers (with one-click install for all 21), autocomplete, and a
  Structure outline.
- **Run and debug**: run a file from a gutter play button, a Debug Adapter
  Protocol debugger for Java, Python, and JavaScript, and an HTTP client for
  `.http` files.
- **Version control**: native Git, a side-by-side and unified diff viewer, a
  merge resolver, and IntelliJ-style local file history.
- **Docs and data**: a native Markdown preview with Mermaid diagrams and LaTeX
  math, export to PDF, HTML, Word, and ODF, a server log viewer, and
  first-class CSV and TSV tooling.
- **Workspace**: projects and multi-window, bookmarks, personal notes, remote
  editing over SFTP, and a TODO tool window.

The full list lives on the [What's New](/whats-new) page, and each feature has
its own page linked from the [home grid](/) and the [docs](/docs).

## New this release: AI, on your terms

0.9.1 also introduces optional **AI assistance**, off by default. There are
one-shot actions, explain a selection, rewrite it to an instruction, generate a
commit message from the staged diff, and an inline ghost completion, plus a full
embedded coding **agent** over the Agent Client Protocol whose edits land as
undoable buffer changes you review and save. You can point it at the Anthropic
API or at a local OpenAI-compatible model like LM Studio or Ollama, so it works
without sending anything to a cloud if you'd rather it didn't. The
[AI guide](/docs/ai) has the details.

## Why 0.9.1 and not 0.9.0

The honest version: `v0.9.0` was cut first, but a JReleaser misconfiguration
published the GitHub release as *immutable* before the installer uploads
finished. GitHub permanently reserves a tag once it has backed an immutable
release, even after you delete the release and the tag, so `v0.9.0` can never be
reused on the repository. The fix shipped as 0.9.1, along with a few related
release-engineering repairs: jpackage rejects a macOS `--app-version` whose first
number is zero, so a `0.x.y` build needed a placeholder version that gets
rewritten back to the true one before the DMG is wrapped; and JReleaser needed to
be told the default branch is `master`, not `main`, and to create the release as
a draft before uploading assets.

None of that changes the app. It's the kind of thing you only hit the first time
you ship.

## Get it

Download an installer from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). They're
unsigned for now, so macOS wants a right-click → *Open* and Windows wants "More
info → Run anyway" the first time. Signing and notarization are on the list.

Editora is built in the open, and with the help of AI coding tools. Issues and
discussion are on [GitHub](https://github.com/adriandeleon/Editora).
