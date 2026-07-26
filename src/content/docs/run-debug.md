---
title: Running & debugging
description: Run files from the gutter, run and debug a project's main class, save run configurations, and debug Java, Python, and JavaScript.
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

## Running a project's main class

Beyond single files, **Run: Run Main Class…** (`run.mainClass`) and
**Debug: Debug Main Class…** (`debug.mainClass`) pick any main class in the
active file's **Maven or Gradle project** and run it in the Run console or debug
it with breakpoints. A green ▶ also appears in the gutter next to every
`public static void main`, and the editor right-click menu offers
*Run '….main()'* and *Debug '….main()'*.

There are two paths under that, picked automatically:

- With the **Java language server** set up, Editora asks it for the project's
  main classes and resolved classpath. This is the fast, project-wide path, and
  it is the only one that can debug.
- Without it, plain **Run** falls back to the **build tool**: Maven resolves the
  classpath (`mvn compile dependency:build-classpath`) and runs the file's
  `main`, and Gradle delegates to its `run` task, or `bootRun` in a Spring Boot
  project. In a multi-module Maven build the fallback runs from the reactor root
  with `-pl <module> -am`, so a submodule that depends on an uninstalled sibling
  still runs.

### Run configurations

Save a configuration per project with **Run: Save Run Configuration…**: a name,
the main class and module, **program arguments**, **VM arguments**,
**environment variables** (`KEY=value`, quoting values that contain spaces) and a
working directory. Re-run one with **Run: Run Configuration…** or remove it with
**Run: Delete Run Configuration…**.

**Settings → Run Configurations** is a master-detail editor for the same list, so
you can create, rename and edit them in place instead of only through palette
prompts. VM arguments and environment variables apply to both Run and Debug. In a
Gradle project, saving a configuration pre-fills the main class the build itself
declares (`mainClass`, `mainClass.set(…)`, or the legacy `mainClassName`), so the
configuration matches what `gradle run` would launch rather than whichever file
happens to be open.

## Debugging

<span class="beta-pill">Beta</span>

Editora debugs **Java**, **Python**, and **JavaScript (Node)** through the Debug
Adapter Protocol, with an IntelliJ-style Debug tool window (`M-g d`). Debugging
is **off by default**; enable it in **Settings → Debugging**.

| Action | Command | Default key |
| --- | --- | --- |
| Start / continue | `debug.start` | `C-c C-d d` |
| Debug a project main class | `debug.mainClass` | (palette) |
| Debug via build tool | `debug.viaBuild` | (palette) |
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

### Debug via the build tool

Some applications are meant to be started by the build tool rather than by a bare
`java` command. **Debug: Debug via Build Tool** (`debug.viaBuild`) launches the
app under a suspended JVM through Gradle (`run` or `bootRun` with `--debug-jvm`)
or, for a Spring Boot Maven project, `spring-boot:run` with a JDWP agent. Editora
watches the build output for the JDWP banner and attaches the debugger when the
app is listening. It complements *Debug Main Class*, which uses the language
server; a plain Maven `main` has no uniform build-tool debug mechanism, so use
*Debug Main Class* there.

### Installing adapters

Adapters are user-installed, not bundled. Helper scripts in the Editora repo
fetch each one:

- **Java**: the Microsoft java-debug plugin (layered onto `jdtls`),
  `scripts/install-java-debug.sh`. If your `jdtls` distribution already bundles
  and loads java-debug itself (Homebrew's does), Editora detects that and needs
  no second copy.
- **Python**: `debugpy`, `scripts/install-debugpy.sh`.
- **JavaScript**: vscode-js-debug, `scripts/install-js-debug.sh`.

The Settings → Debugging page shows a live found/not-found status for each.

For sending HTTP requests from `.http` files, see the
[HTTP client](/docs/http-client). For Maven, npm, Cargo, Go, and Gradle tasks,
see [Build tools](/docs/build-tools).
