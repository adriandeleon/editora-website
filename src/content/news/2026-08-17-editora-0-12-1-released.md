---
title: "Editora 0.12.1: one editor, and 225 ms off the startup"
description: "Opening a file from your file manager now reuses the editor you already have open instead of starting a second one, and the first frame that shows your file arrives about 225 ms sooner."
date: 2026-08-17
version: "0.12.1"
---

**Editora 0.12.1** is out. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

A small release, but both halves are things you feel on every launch.

**Opening a file from your file manager reuses the running Editora.** On Linux
and Windows a file manager delivers the path as a command-line argument, which
by definition means a new process — so clicking a file while Editora was already
open paid a full cold start *and* left a second editor resident. Measured, that
was two JVMs at 670 MB and 1707 MB, for what looks like one application. macOS
never had the problem, because Finder delivers an event that reaches the running
app; this is the cross-platform equivalent, and it hands the launch to that same
code path.

A second launch now passes its files to the running editor and exits — about
735 ms, which is JVM startup and unavoidable for any new process, against ~1.5 s
*and* a permanently resident second editor. The files open in the focused
window, and a focus mode the launcher asked for is applied, so the "Editora
Expert Mode" desktop entry still means Expert Mode.

It is deliberately narrow. Only a launch that is purely "open these files" is
handed over. `--project`, `--new-file`, `--config-dir` and `--dev` shape how a
*process* starts and have no honest reading inside a window that is already
running, so those still get their own editor — as does `editora` with no files
at all. The new **`--new-instance`** forces a separate editor in any case. An
instance is scoped to its config directory, so a `--dev` launch can never hand
off to your real editor. If anything about the handoff fails, the launch simply
starts its own editor, exactly as before.

**Startup is about 225 ms faster to the first frame that shows your file** — a
median of 1702 ms down to 1477 ms on a packaged Linux build, roughly 13%. Four
pieces of work were running on the UI thread *before* the editor had painted,
and the first frame needs none of them.

The largest was the **minimap**, at 135–230 ms. Its render walks every paragraph
and then takes a snapshot, which forces a full layout of the scene, and at
startup its triggers arrive in a burst — theme colours, tab size, the content
settling — so the whole sequence ran three times over before any text appeared.
Renders now coalesce to one per frame, and the first is held until the editor
has painted. It is a secondary navigation aid; nothing about it needs to precede
the text you are waiting for. Typing is unaffected.

The other three: the **debug adapter locations** for Python and JavaScript were
being resolved on the UI thread at startup, walking directories for values not
read until a debug session actually launches — and resolved *again*, off-thread,
moments later. The **rest of the session restore** now yields a couple of frames
before continuing, since the file you asked for is already front-loaded and the
remaining tabs were being filled in the very frames that render it. And **fold
recompute no longer forces a layout pass to discover it has nothing to do** —
which also helps while typing, where it runs on every pause and most edits leave
the fold structure untouched.

Both of the big costs read as "rendering is slow" from the call graph, and
neither was. If you ever suspect a startup regression, measure it rather than
reason about it: `scripts/measure-startup.sh` reports the phase breakdown, and
`EDITORA_PERF_SAMPLE=1` now adds a per-phase sample of what is holding the UI
thread.

The complete list is on the [What's New](/whats-new) page.
