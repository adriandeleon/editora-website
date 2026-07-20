---
title: Running & debugging
description: Run files from the gutter, and debug Java, Python, and JavaScript.
category: Run & debug
order: 1
---

## Running files

With the [LSP feature](/docs/lsp) enabled, a green ▶ appears in the gutter of a
runnable file:

- a **Java compact source file** (JEP 512, a `.java` with a top-level
  `void main`),
- a **Python** script (the ▶ sits on the `if __name__ == "__main__"` guard), or
- a **shell** script (only when the Bash language server is enabled).

Click the ▶, use the right-click *Run File*, or bind `file.run` (`C-c r`). Output
streams to the **Run** tool window (`M-9`), which accepts **stdin** so
console-style programs work, and **stack-trace lines are clickable** (Java,
Python, and Node frames) to jump to the file and line.

Pass per-file **program arguments** with `file.runWithArgs` (remembered across
runs and reused by the debugger), and repeat the last run with `run.rerun`.
Running a Java file needs JDK 25 on your `PATH`; Editora preflights this and
reports a clear message if it finds an older Java.

## Debugging

<span class="beta-pill">Beta</span>

Editora debugs **Java**, **Python**, and **JavaScript (Node)** through the Debug
Adapter Protocol, with an IntelliJ-style Debug tool window (`M-g d`). Debugging
is **off by default**; enable it in **Settings → Debugging**.

| Action | Command | Default key |
| --- | --- | --- |
| Start / continue | `debug.start` | `C-c C-d d` |
| Toggle breakpoint | `debug.toggleBreakpoint` | `C-c C-b` |
| Pause | `debug.pause` | `C-c C-d p` |
| Run to cursor | `debug.runToCursor` | `C-c C-d u` |
| Jump to line | `debug.jumpToLine` | `C-c C-d j` |
| Step over / into / out | `debug.stepOver` … | (Debug window) |

The Debug window has a threads and call-stack view, a lazy variables tree with
**set-value**, **watches**, and an evaluate console. While suspended, **inline
values** appear after each line and hovering a variable shows its value.
Breakpoints live in a leftmost gutter strip and are saved per project.
**Edit Breakpoint** is a form for all three: a **condition**, a **log message**
(which turns it into a logpoint that logs and never suspends), and an
**enabled** toggle. Breakpoints in closed files are honored too.

When the Debug panel has the keyboard (it shows an active-focus highlight),
**gdb-style single keys** drive the session: `n` to step over, `s` to step into,
`f` to finish (step out), and so on. Stepping keeps focus in the panel, so you
can step repeatedly without your key presses landing in the editor. A fresh
breakpoint hit while you're editing still takes you to the code.

### Installing adapters

Adapters are user-installed, not bundled. Helper scripts in the Editora repo
fetch each one:

- **Java**: the Microsoft java-debug plugin (layered onto `jdtls`),
  `scripts/install-java-debug.sh`.
- **Python**: `debugpy`, `scripts/install-debugpy.sh`.
- **JavaScript**: vscode-js-debug, `scripts/install-js-debug.sh`.

The Settings → Debugging page shows a live found/not-found status for each.

For sending HTTP requests from `.http` files, see the
[HTTP client](/docs/http-client). For Maven, npm, Cargo, Go, and Gradle tasks,
see [Build tools](/docs/build-tools).
