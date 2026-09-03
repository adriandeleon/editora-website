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

A clicked **Java** frame in the Run, Test or Build console is resolved by the
language server, so a frame inside a dependency or the JDK opens its source
instead of reporting "not found". Frames in your own code behave as before, and
filename matching still handles anything the server can't place, plus every
non-Java trace.

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

## Run configurations

A run configuration is a saved answer to "how is this launched": a name, what to
launch, **program arguments**, **VM arguments**, **environment variables**
(`KEY=value`, quoting values that contain spaces) and a working directory.

### What a configuration can launch

Pick a **Type** in Settings → Run Configurations:

| Type | Launches |
| --- | --- |
| Java main class | a `main` in the project, via the language server or the build tool |
| Python script | a `.py` file with your `python3` |
| Shell script | a script with `bash` |
| Make target | a target in a makefile |

Script and make configurations need **no project and no language server**.
Debugging remains Java-only, and says so rather than reporting a confusing Java
error.

### The toolbar selector

The toolbar carries a configuration dropdown with **Run**, **Debug** and **Stop**
beside it; your choice is remembered across restarts. The dropdown ends with
**Edit Configurations…**, which opens Settings on the Run Configurations page
with the configuration you had selected already picked out — also available as
**Run: Edit Run Configurations…** (`run.editConfigs`).

The group only appears where you could actually launch something: with a project
open and a Maven, Gradle, npm, Cargo or Go build file *inside* it, or a makefile
at its root; with no project open, any detected build does. Anything you have
already saved keeps the group visible regardless, because a configuration only
needs a `main` method and a plain Java folder can hold one. The palette commands
work anywhere either way.

Each configuration also **becomes a real command**, so it appears in the palette
by name and can be given its own keyboard shortcut in Settings → Keymaps — the
same way saved [macros](/docs/macros) and
[external tools](/docs/external-tools) already work.

### A step before the launch

A configuration can name a command to run first — a build, a codegen step. A
**non-zero exit aborts the launch**, so a stale binary is never run by accident.

### Sharing them with your team

| Action | Command |
| --- | --- |
| Write them into the project | `run.exportConfigs` |
| Merge the project's back in | `run.importConfigs` |

**Export Configurations to Project** writes them to
`.editora/run-configurations.json` inside the project, where they can be
committed alongside the project's [committed settings](/docs/workspace).
**Import** merges them back **by name**, so importing twice doesn't duplicate and
a colleague's edit updates a configuration rather than doubling it.

### Creating and editing them

**Settings → Run Configurations** is a master-detail editor for the list, and
**Run: Save Run Configuration…** creates one from the palette;
**Run: Run Configuration…** runs one and **Run: Delete Run Configuration…**
removes one.

**Add** starts from the file you are looking at rather than a blank entry: it
prefills the main class from the active Java file (or the one your Gradle build
declares — `mainClass`, `mainClass.set(…)`, or the legacy `mainClassName`), names
the configuration after that class, and puts the cursor in whichever field still
needs you. Adding twice from the same file gets you "App" and "App (2)" rather
than two entries sharing a name.

Running an **incomplete** configuration opens its form at the field you need to
fill in, rather than naming the problem and leaving you to find it. VM arguments
and environment variables apply to both Run and Debug.

A saved configuration no longer depends on which tab is in front: any open Java
file in the project serves, and it only complains when there genuinely isn't one.

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
