---
title: "Editora 0.18.5: fluid Java completion and safer editing"
description: "Editora 0.18.5 sharpens continuous Java completion, adds selectable Maven JDK toolchains, keeps Run output in reach, and strengthens save, LSP, Git, diff, and merge safety."
date: 2026-09-19T13:15:00-06:00
version: "0.18.5"
---

**Editora 0.18.5** is out. This release makes Java editing feel continuous,
lets each Maven project run on the right JDK, and hardens the asynchronous paths
that protect work while files, language servers, Git, and diff views are all
changing at once. Download it from the
[0.18.5 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.5).

## Java completion that keeps up

Member completion now appears immediately, while ordinary identifier completion
waits for a short pause and filters complete result lists locally. Stale requests
are cancelled as you type. Enter and Tab belong to the completion popup when it
is open, overloads stay distinct, commit characters and server ranges survive
the full trip, and completed methods reuse parentheses already in the file.

Auto-imports can follow safe continued typing and undo with the completion as a
single action. Signature help stays useful across multiline calls, supports
overload navigation, and no longer appears for finished zero-argument calls or
method references.

## Pick the JDK for Maven work

A new global Maven JDK selector discovers installations in standard platform
locations and through SDKMAN, asdf, mise, Jabba, and JetBrains-managed JDKs.
Run configurations can override it per project. Run, Debug, Maven classpath
resolution, Maven tasks, and before-launch steps all use the selected toolchain.

Running a file or configuration now opens and focuses the Run window every time.
If a process is already active, pressing Run brings its existing console back
into view. Console action labels also stay readable beside long command lines.

## More protection for work in flight

Save, Save As, close, reload, and Git working-tree operations now revalidate the
exact document and disk state before committing a change. LSP saves, renames,
resource edits, diagnostics, and navigation retain their version and session
identity, so stale asynchronous work is refused.

Diff and merge Result drafts participate in close protection. Degraded binary
comparisons stay read-only, stale Git hunks are rejected, and a later merge
choice cannot erase a Result you edited by hand. Git also treats paths
literally and keeps conflicted entries visible.

The full list, including the JavaFX 27 runtime update and dependency refresh in
0.18.4, is on [What's New](/whats-new).
