---
title: "Editora 0.28.0: a debugger for eight more languages, and the Project Map as a code browser"
seoTitle: "Editora 0.28.0: debugging for 8 more languages"
description: "Debug Go, Rust, C/C++, C#, Ruby, PHP, Bash and browser JavaScript; Step Back and several sessions at once; a code-browsing Project Map; a whole-UI zoom."
date: 2026-11-18
version: "0.28.0"
---

**Editora 0.28.0** is out. It is the biggest release so far. The debugger
reaches eight more languages and gains the tools of the big IDEs. The Project
Map becomes a place to read, follow and review code. And every row and button
of the interface can be reached from the keyboard and the Command Palette.
Download it from the [0.28.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.28.0).

## The debugger

- **More languages.** Debug Go (Delve), Rust (CodeLLDB), C and C++ (CodeLLDB
  or GDB), C#, Ruby, PHP and Bash scripts, and JavaScript in Chrome, Edge or
  another Chromium browser, with breakpoints in your TypeScript source through
  source maps. VS Code's `launch.json` configurations run as they are, and a
  debug adapter of your own can be added.
- **Several programs at once.** Debug a second file while one runs, or start a
  compound of several together. Each session has its own tab, console, call
  stack and variables.
- **Going back.** Debug History keeps every stop so you can return to an
  earlier one, recording breakpoints collect stops without staying stopped,
  and on GDB **Step Back** and **Reverse Continue** replay what was recorded.
- **Seeing the program.** Attach to Process, the Threads view, Thread Dump,
  Parallel Stacks, an exception card, Evaluate as IntelliJ does it, pinned
  values, View as Table, View as DataFrame for Python, type renderers, Show
  Value As, and Disassembly and Memory views.
- **Breakpoints that fit the problem:** conditions on exception types,
  triggered breakpoints, thread and caller filters, groups you can export and
  import, method and field breakpoints and lambda breakpoints in Java,
  JavaScript and TypeScript, and logpoints in one step.

[More about debugging](/docs/debugging).

## Project Map

- **Navigate code on the Map.** Go to Definition, Find Usages, the call and
  type hierarchies and the rest of the editor's navigation answer with cards
  beside the one you asked from, joined by labelled edges. Back and Forward
  step through them, and the Map follows the code into libraries.
- **Symbol Graph** puts a method or class in the middle of a card, with what
  calls it, what it calls and its types around it. Walk it with Enter and
  Backspace.
- **Review a branch or your changes** on the Map: changed files are striped,
  and *Compare with base* opens the diff against where the branch began.
- **Code metrics and hotspots** list a folder's files with their size,
  complexity and recent commits, and mark the code that is both complex and
  often changed.
- **Cards read like the editor**, with its gutter, problems and usage counts,
  and the Map follows the debugger to each stop.

[More about the Project Map](/docs/project-map).

## Every command on the keyboard

Editora now follows [Pauta](https://pauta-project.dev/), the guidelines its
family of apps shares, and says how far it has come: *Help → Pauta
Conformance* lists every rule it does not meet yet.

- **Every right-click row and button is a command**, and every setting can be
  changed from the Command Palette. Right-click menus show each row's
  keyboard shortcut, and a grayed row says why it cannot run.
- **Moving around without a mouse:** go between the editor and the tool
  windows both ways in every keymap, Focus Menu Bar, and the Menu key or
  Shift+F10 on any list. After a prefix key such as `C-x`, a card lists the
  keys that can follow.
- **A zoom for the whole interface**, from 80% to 200%, apart from the
  editor's text zoom.
- **Fewer surprises:** Editora asks before more removals and names what goes,
  with **Undo Last Removal** for the ones it doesn't. Questions and dialogs
  look and read the same everywhere, and a screen reader hears which row of
  the palette or a picker is selected, and why a grayed row cannot run.

[More about keymaps](/docs/keymaps).

## Faster on large files and projects

Typing, the pickers, the Command Palette and the editor's commands no longer
wait on work done across a whole file or project: that work runs in the
background and lands a moment later. Long Markdown previews and long lists
build only what is on screen.

## Safer with code you just cloned

Language servers are on by default. In a folder you have not trusted they
start without running its build, and Run, Debug and every build tool ask
before running a folder's code.

## And more

- A terminal can be an editor tab beside your files.
- **Git: Copy Permalink** copies a link to the selected lines on GitHub,
  GitLab, Bitbucket and other hosts.
- The mouse wheel zooms a PDF, Markdown code blocks have a copy button, and
  JSON Lines files are recognised.

The full list is in [What's New](/whats-new/0.28/#0280---2026-11-18).
