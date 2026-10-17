---
title: Running & debugging
description: Run files from the gutter, run and debug a project's main class, save run configurations, and debug Java, Python, and JavaScript.
category: Run & debug
order: 1
---

## Running files

You can run compact Java files, Python scripts and shell scripts with
[language services](/docs/lsp) disabled. A green ▶ appears in the gutter of a
runnable file:

- a **Java compact source file** (JEP 512, a `.java` with a top-level
  `void main`) or an extensionless Java file with a `java --source 25+`
  shebang,
- a **Python** script (the ▶ sits on the `if __name__ == "__main__"` guard,
  written either way round, with either quote or in parentheses; see
  [Python scripts](#python-scripts)), or
- a **shell** script: `.sh`, `.bash`, `.zsh`, `.ksh` and the other shell
  types, or a file whose shebang names a shell (see
  [Shell scripts](#shell-scripts)). Startup files that a shell reads rather
  than runs, such as `.bashrc`, `.profile`, `.zshrc`, `.bash_logout`,
  `.xinitrc` and `.envrc`, get no ▶.

Click the ▶, use the right-click *Run File*, or bind `file.run` (`C-c r`). Output
streams to the **Run** tool window (`M-9`), which accepts **stdin** so
console-style programs work. Double-click a **stack-trace line** to jump to the
file and line: Java, Python and Node frames, and shell errors such as
`deploy.sh: line 12: …`. For Python that covers traceback frames of any file
(also a script with no `.py` extension), `app.py:12:` locations in warnings and
in pytest, mypy or Ruff output, and pdb's `> app.py(12)` lines; `<stdin>` and
`<frozen …>` frames have no file to open.

The console's first line, drawn muted, is the command that started a script or
a test run: which interpreter ran, with which options, on which file.

The console reads the terminal codes programs print: colours are drawn in the
theme's colours, a carriage return (`\r`) rewrites the current line so a
progress bar stays on one line, and `clear` empties the console. Other cursor
codes are dropped. Stop ends the program and everything it started, and the
console then says **Stopped**. A Python program on macOS or Linux gets Ctrl-C
first, so its `finally` blocks and `atexit` handlers run; whatever still runs a
moment later is ended. **End Input** in the console's header, or
`Ctrl+D` in the empty input field, closes the program's input, so a loop that
reads lines until the end of input can finish (*Run: End Program Input*,
`run.endInput`).

While a program runs, a small **spinner** turns beside the Run window's status,
from the launch (including a configuration's before-launch command) until the
program exits, is stopped, or fails to start, so a script that prints little
does not look stalled. The Run button on the tool-window stripe badges the same
spinner on its icon, which shows a run is alive with the Run window closed or
another window open in its place.

A clicked **Java** frame in the Run, Test or Build console is resolved by the
language server, so a frame inside a dependency or the JDK opens its source
instead of reporting "not found". Frames in your own code open as usual, and
filename matching handles anything the server can't place, plus every
non-Java trace.

Pass per-file **program arguments** with `file.runWithArgs` (remembered across
runs and reused by the debugger), and repeat the last run with `run.rerun`.
Rerun first saves the file it runs, even when another tab is in front.
Arguments are split the way a shell splits them: quote an argument that
contains spaces (`"two words"` or `'two words'`), and put a backslash before a
space, quote or backslash to keep it (`O\'Brien`). Inside double quotes, `\"`
and `\\` are escapes; a backslash before any other character stays, so a
Windows path such as `C:\tools\x.exe` is kept as typed. An unclosed quote
stops the run with a message instead of joining the rest of the line into one
argument. `$NAME` in an argument is passed as written.
Running a Java file needs JDK 25 or newer. Editora uses the selected Default JDK
when one is configured, or `java` on your `PATH`, and checks that exact launcher
before running. An extensionless shebang can request a newer source release.

For Maven projects, **Settings → Languages & Tools → Build Tools → Default
JDK** selects the toolchain used by Run, Debug, Maven classpath resolution,
Maven tasks, and before-launch steps. Installed JDKs are discovered from the
usual platform locations and common version managers. A run configuration's
**JDK** field can inherit that choice or override it for one configuration. The
Default JDK also applies to standalone Java Run and Debug.

### Python scripts

Run, Python [run configurations](#run-configurations), the
[test runner](/docs/build-tools#test-runner) and the debugger all use the same
Python. The status-bar item on a Python file shows which one and why, for
example `Python 3.13.5 (.venv)`; click it, or use **Python: Select
Interpreter** (`python.selectInterpreter`), to pick another for the project
(see [The Python interpreter](/docs/lsp#the-python-interpreter)). The first of
these wins:

1. a run configuration's **Interpreter**;
2. the interpreter picked for the project;
3. an environment that was active when Editora started (`$VIRTUAL_ENV`, then
   `$CONDA_PREFIX`);
4. a `.venv`, `venv`, `env` or `.env` folder holding a `pyvenv.cfg`, looked for
   from the script's folder up to the project root;
5. **Settings → Debugging → Python interpreter**;
6. `python3`, then `python`, on your `PATH`. On Windows: the `py` launcher
   (`py -3`), then `python`, never the Microsoft Store shortcut.

A **shebang** that asks for a particular Python is followed, as `./script`
would follow it. One that names plain `python` or `python3` asks for no
particular Python, so the project's runs the script, with the shebang's options:

| First line | Runs |
| --- | --- |
| `#!/usr/bin/env python3` | the project's Python |
| `#!/usr/bin/python3 -O` | the project's Python with `-O` |
| `#!/usr/bin/env python3.12` | `python3.12` |
| `#!/usr/bin/env -S uv run --script` | `uv run --script script` |

`pypy`, `pipx`, `poetry` and `pdm` shebangs are followed the same way. When the
interpreter a shebang asks for is not installed, the project's Python runs the
script, and the console and the status line say so. A configuration's
**Interpreter** wins over any shebang.

The script runs in **its own folder**. In a project, the project root, and its
`src/` folder when there is one, go in front of `PYTHONPATH` (anything you set
there stays after them), so `from mypackage import x` works in a file deep
inside the package. With no project open, `PYTHONPATH` is left as it is.
Output appears as the program prints it, not in one block at the end
(`PYTHONUNBUFFERED=1`), and in UTF-8 on every platform (`PYTHONIOENCODING`), so
`print("✓")` works on Windows too. A configuration's **Environment** can set
either variable differently.

A `.pyi` stub gets no ▶, and neither do `__init__.py` and `conftest.py` unless
they have a `__main__` guard. A guard inside a string or docstring does not
count. A test file (`test_*.py`, `*_test.py`) gets a ▶ for each test instead,
and Run on it runs its tests: see [Test runner](/docs/build-tools#test-runner).

### Shell scripts

A shell script runs under the interpreter its **shebang** names, as `./script`
would in a terminal, with the shebang's options:

| First line | Runs |
| --- | --- |
| `#!/bin/bash -eu` | `/bin/bash -eu script` |
| `#!/usr/bin/env -S bash -e` | `bash -e script` |
| `#!/bin/sh` | `/bin/sh script` |
| `#!/usr/bin/env zsh` | `zsh script` |
| `#!/usr/bin/env python3` | `python3 script` |
| none | `bash script`, or `zsh` for a `.zsh` file |

The `env` forms are read as `env` reads them: `env -u NAME` runs the script
without `NAME` set, and `env -C DIR` runs it in that folder (Run reports a
folder that does not exist). `#!/bin/busybox sh` runs busybox's shell. When an
interpreter path in the shebang does not exist on this machine, Run uses the
command of that name on your `PATH` instead. A
[run configuration](#run-configurations) can set its own interpreter.

A shell run gets `TERM=xterm-256color`, so scripts that ask `tput` for colours
get them. When a bash script asks a question with `read -p "Name: "`, the
prompt shows in the console, although bash prints such prompts only to a
terminal; type the answer in the input field.

If the script has Windows (CRLF) line endings or starts with a byte-order mark,
Run warns first, because bash then fails with errors that never mention the
cause. **Convert and Run** saves the file with LF line endings (and UTF-8
without a byte-order mark) and runs it; **Run Anyway** runs it as it is. When
`.editorconfig` requires CRLF for the file, only Run Anyway is offered.

On **Windows**, `bash` and `sh` come from **Git Bash**: the one beside the
`git.exe` on your `PATH`, then a standard Git for Windows install, then any other
`bash` on your `PATH` (MSYS2, Cygwin). The WSL launcher in `System32` is never
used, because it cannot open a Windows path. Lines typed into the console
reach the script with Unix line endings.

Shell scripts can't be debugged; use [Run with Trace](#run-with-trace) to see
each line as it runs.

### Run with Trace

**Run: Run with Trace** (`run.trace`, also in the Run menu and the editor's
right-click menu on a shell script) runs the active shell script with `-x`,
keeping the shebang's interpreter. Under bash, each command is printed before
it runs with its file and line:

```
+/home/me/deploy.sh:12: tar czf site.tgz public
```

Double-click a trace line to open that line. Trace lines are drawn muted, not
as errors. A script run by `sh` or `dash` is traced too, but its trace lines
carry no file and line, because those shells can't print them. zsh scripts and
scripts in other languages can't be traced.

To trace a [run configuration](#run-configurations), put `-x` in its
**Interpreter options**; the trace lines link to their source the same way.

### Running on a remote host

A file in a [remote folder](/docs/remote) runs on its host, when that host
[may run commands](/docs/remote#allowing-a-host-to-run-commands); otherwise
Run says so when you launch it. That covers Run on a file, a Java project's main
class (the gutter ▶, *Run Main Class*, or a saved configuration, with the class
path the host's `jdtls` or Maven gives), and script, make and npm
configurations of a remote project. A shell script runs under the interpreter
its shebang names, found on the host, and Run with Trace works there too; the
`read -p` prompts described above show only for a local run. Output, input and
Stop work as for a local run, over the same SSH connection, and a makefile at a remote project's root
shows the run-configuration toolbar as it does locally. A configuration's
working directory is a folder on the host.

The host's own `java` is used, or the **JDK home** set for that host in
Settings → Remote. Your local Default JDK and a configuration's JDK override
are paths on this machine, so they are not sent to the host.

A Python script or configuration runs under the configuration's
**Interpreter**, the interpreter picked for the project, or the project's venv
on the host (a `.venv`, `venv`, `env` or `.env` folder there). Without one, it
uses the host's **Python for Run and tests** from Settings → Remote → This Host
(see [Remote](/docs/remote#what-runs-on-the-host)), then
**Settings → Debugging → Python interpreter** when that is a plain command name
rather than a path on this machine, then the host's `python3`. `PYTHONPATH` gets
the project root and its `src/` folder; the host's own `PYTHONPATH` is not
kept, because Editora does not know it. Stop ends a remote program straight
away, without the Ctrl-C a local Python program gets first.

### Running in a container or WSL

A project whose tools [run in a container](/docs/remote#tools-in-a-container)
or, on Windows, [in a WSL distribution](/docs/remote#tools-in-wsl-windows) runs
its programs there: Run on a file, a main class, and every kind of run
configuration, with the `java`, interpreters and environment the container or
distribution has. Output, input and Stop work as for a local run, and Stop ends
what the program started inside. The files are handed over by the names they
have inside, and a configuration's absolute working directory is read as a
folder there.

A Python script picks its interpreter as on a remote host: the configuration's
**Interpreter**, the project's pick, or the project's venv (one created inside
the container or distribution, named by its path there), else the `python3`
found inside. Stop ends it straight away, as on a remote host.

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

A run configuration saves how something is launched: a name, what to
launch, **program arguments**, **VM arguments**, **environment variables**, an
optional **JDK override**, and a working directory.

Run configurations belong to a [project](/docs/workspace#projects): each
project window keeps its own list. The No Project window has none; there, a
script or a compact Java file runs from its gutter ▶ or **File: Run**
(`file.run`), as described in [Running files](#running-files).

**Environment** takes `KEY=value` pairs, quoted and escaped like
[program arguments](#running-files). `$NAME` and `${NAME}` in a value expand
to that variable: one set earlier in the same field, or else the environment
the program would get anyway, so `PATH=/opt/tools/bin:$PATH` adds a folder to
the real `PATH`. Single quotes keep `$` as it is. In a remote project, a
variable that is not set earlier in the field is passed to the host as written.

A relative **Working directory** is relative to the project root, and a blank
one means the project root, except for a Python script: that runs in the
script's own folder, as its gutter ▶ does. A relative script path is relative
to the project root too, whatever the working directory.
Run names a working directory or script that does not exist, and opens the
configuration at the field to fix.

### What a configuration can launch

Pick a **Type** in the project's **Run Configurations** window:

| Type | Launches |
| --- | --- |
| Java main class | a `main` in the project, via the language server or the build tool |
| Python script | a `.py` file with the project's Python (see [Python scripts](#python-scripts)) |
| Python module | a module by its name, as `python -m app.main` does, from the project root |
| Shell script | a script with the interpreter its shebang names (or `bash`), unless **Interpreter** is set |
| Make target | a target in a makefile |
| NPM script | a named `package.json` script via `npm run` |

The form **shows only the fields the selected type uses**. A Java main class
has no Script / target field; a Python, shell, Make or NPM configuration has no
Main class, Module, VM arguments or JDK. A hidden field keeps its value, so
switching the type and back loses nothing. A Python or shell script can be
picked with **Browse…** beside Script / target; a script inside the project is
saved relative to the project root.

A shell configuration also has **Interpreter** and **Interpreter options**.
Left blank, the script's shebang decides (see [Shell scripts](#shell-scripts));
set, they replace it, for example `/opt/homebrew/bin/bash` with `-eu`. Options
alone keep the shebang's interpreter and replace its options.

A Python script or module configuration has them too. **Interpreter** names the
Python for this configuration (a path relative to the project root, such as
`.venv-311/bin/python`, works); left blank, the project's Python runs it, chosen
as described in [Python scripts](#python-scripts). **Interpreter options** go
between the interpreter and the script, for example `-X dev -W error`. A Python
module configuration takes the module name in Script / target, the part after
`python -m` (for example app.main).

Script and make configurations need **no language server**.
Only Java configurations can be debugged; Debug on any other type says so, and
for a shell configuration it points to [Run with Trace](#run-with-trace).

### The toolbar selector

The toolbar carries a configuration dropdown with **Run**, **Debug** and **Stop**
beside it; your choice is remembered across restarts. The dropdown ends with
**Edit Configurations…**, which opens the separate Run Configurations window
with the configuration you had selected already picked out. The same window is
available as **Run: Edit Run Configurations…** (`run.editConfigs`).

Starting a file or configuration always opens and focuses the **Run** tool
window. If that process is already active, Run brings its existing console back
instead of hiding the useful output behind a busy message.

The group only appears in a project window where you could launch something: a
Maven, Gradle, npm, Cargo or Go build file *inside* the project, or a makefile
at its root. Anything you have already saved keeps the group visible
regardless, because a configuration only needs a `main` method and a plain Java
folder can hold one. The palette commands work in any project either way. The
No Project window never shows the group, and its run configuration commands say
to open a project.

Each configuration also **becomes a real command**, so it appears in the palette
by name and can be given its own keyboard shortcut in Settings → Keymaps, the
same way saved [macros](/docs/macros) and
[external tools](/docs/external-tools) work.

### A step before the launch

A configuration can name a command to run first, such as a build or a codegen
step. A
**non-zero exit aborts the launch**, so a stale binary is never run by accident.
The step runs in the configuration's working directory; for a Python script
with a blank one, it runs at the project root.

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

The **Run Configurations** window (`run.editConfigs`, or **Edit
Configurations…** in the toolbar dropdown) is a master-detail editor for the
list, and
**Run: Save Run Configuration…** creates one from the palette;
**Run: Run Configuration…** runs one and **Run: Delete Run Configuration…**
removes one.

**Add** starts from the file you are looking at rather than a blank entry. For
an active Python or shell script it adds a configuration of that type for the
script, named after the file. For a Java file it
prefills the main class from the active Java file (or the one your Gradle build
declares as `mainClass`, `mainClass.set(…)`, or the legacy `mainClassName`), names
the configuration after that class, and puts the cursor in whichever field still
needs you. Adding twice from the same file gets you "App" and "App (2)" rather
than two entries sharing a name.

**Run: Save Run Configuration…** saves the active Java file's main class, or the
active Python or shell script, with the file's program arguments. For a script,
Add and Save write the folder its gutter ▶ runs it in into **Working
directory** (left blank when that is the project root), so the configuration
runs exactly as ▶ does.

Running an **incomplete** configuration opens its form at the field you need to
fill in, rather than naming the problem and leaving you to find it. VM arguments
and environment variables apply to both Run and Debug. Before a script
configuration runs, Editora saves that script if it has unsaved edits in any
tab.

A saved configuration does not depend on which tab is in front: any open Java
file in the project serves, and it reports an error only when there isn't one.

## Debugging

<span class="beta-pill">Beta</span>

Editora debugs **Java**, **Python**, and **JavaScript (Node)** through the Debug
Adapter Protocol, with an IntelliJ-style Debug tool window (`M-g d`). Debugging
is **off by default**; enable it in **Settings → Debugging**. Shell scripts
can't be debugged; [Run with Trace](#run-with-trace) shows each line of a
script as it runs.

| Action | Command | Default key |
| --- | --- | --- |
| Start / continue | `debug.start` | `C-c C-d d` |
| Continue | `debug.continue` | `C-c C-d c` |
| Pause | `debug.pause` | `C-c C-d p` |
| Stop | `debug.stop` | `C-c C-d k` |
| Restart | `debug.restart` | (palette) |
| Step over | `debug.stepOver` | `C-c C-d n` |
| Step into | `debug.stepInto` | `C-c C-d i` |
| Step out | `debug.stepOut` | `C-c C-d o` |
| Run to cursor | `debug.runToCursor` | `C-c C-d u` |
| Jump to line | `debug.jumpToLine` | `C-c C-d j` |
| Toggle breakpoint | `debug.toggleBreakpoint` | `C-c C-b` |
| Edit breakpoint | `debug.editBreakpoint` | `C-c C-d e` |
| Choose exception breakpoints | `debug.toggleExceptionBreakpoints` | (palette) |
| Debug a project main class | `debug.mainClass` | (palette) |
| Debug a saved run configuration | `debug.config` | (palette) |
| Debug via build tool | `debug.viaBuild` | (palette) |
| Attach to a running JVM | `debug.attach` | `C-c C-d a` |
| Turn the Python or JavaScript adapter on or off | `debug.toggleAdapter` | (palette) |
| Set an adapter's path | `debug.setAdapterPath` | (palette) |

The keys are the Emacs keymap's; the step commands are also buttons in the
Debug window.

The Debug window has a threads and call-stack view, a lazy variables tree with
**set-value**, **watches**, and an evaluate console. While suspended, **inline
values** appear after each line and hovering a variable shows its value.
Hovering an attribute shows the whole expression: the `x` of `p.x` shows
`p.x`, not a variable named `x`. Hovering stops at a call or an index, so it
never runs code.
Breakpoints live in a leftmost gutter strip and are saved per project.
**Edit Breakpoint** is a form with a **condition**, a **hit count**, a **log
message** (which turns it into a logpoint that logs and never suspends), and an
**enabled** toggle. The hit count makes it stop only on that hit: `5` stops on
the fifth, and Python also takes forms such as `>= 3` or `% 2`. A breakpoint
with a condition or a hit count is drawn as conditional. Breakpoints in closed
files are honored too.

The debugger may set a breakpoint on another line than the one you chose: in
Python, a breakpoint on a blank or comment line goes to a nearby line with code.
The breakpoint then moves to that line, and the status bar says so. If that line
already has a breakpoint, the moved one is removed and the one already there,
with its condition, is kept.

While a session runs, a breakpoint the debugger has **not verified** (its code
is not loaded yet, or the line has no code) is drawn as a hollow ring, with the
debugger's explanation on hover. Every breakpoint is hollow from the start of
the session until the debugger answers, and the markers return to normal when
the session ends.

When the panel is wide enough, the call stack, variables, and console sit side
by side; they stack when it is narrow. The call stack lists the innermost 1,000
frames when the adapter supports partial stacks. The console is cleared at each
launch and kept after the session ends, and its right-click menu has **Clear
Console**. **Restart** is disabled for a debugger attached to a test or build
run (its tooltip says why); a session started with *Debug → Attach* can still be
restarted.

A spinner beside the Debug window's status turns while the before-launch build,
the session start, or the program is running, and stops while the program is
paused (at a breakpoint, a step, or Pause).

When a file cannot be debugged, the status bar says why: Python debugging is
turned off, or debugpy was not found for the Python interpreter (with the
command that installs it), or the same for JavaScript and vscode-js-debug.

### Exception breakpoints

**Debug: Exception Breakpoints…** (`debug.toggleExceptionBreakpoints`) lists
the exception filters of the debugger for the current session's language (or
the active file's, when no session runs). A checkmark marks the ones that are
on; choosing one turns it on or off, also during a session.

| Language | Filters | On at the start |
| --- | --- | --- |
| Python | Raised Exceptions, Uncaught Exceptions, User Uncaught Exceptions | Uncaught Exceptions |
| Java | Caught Exceptions, Uncaught Exceptions | none |
| JavaScript | Caught Exceptions, Uncaught Exceptions | none |

A session starts with the filters its debugger turns on by default, so a
Python program stops where an uncaught exception is raised, with the
exception's type and message in the Debug window and the status bar. In
Python, `sys.exit()` with a non-zero code also counts as an uncaught exception;
Continue then ends the program. Your choice holds for that language's sessions
until Editora closes.

### Debugging Python

Debug a Python file with **Debug: Start / Continue** (`debug.start`), or with
**Debug File** in the editor's right-click menu, next to **Run File**. The
program runs the way [Run](#python-scripts) runs it: under the same
interpreter (the project's virtual environment, the one picked for the project,
or the shebang's), in the script's own folder, with the project root and its
`src/` folder at the front of `PYTHONPATH`, and with the arguments set by
**File: Run with Arguments…** (`file.runWithArgs`). The Debug console's first
line shows the command that runs. When the program ends, the console shows its
exit code.

A script whose shebang hands it to a runner (`uv run --script`, `pipx run`,
`poetry run python`, `pdm run python`) is debugged under the project's Python:
the debugger needs an interpreter, not a runner.

The debugger itself (debugpy) runs under the same interpreter when that
interpreter can import debugpy, else under the **Python interpreter** set in
Settings → Debugging. A program that starts child Python processes, for example
with `multiprocessing`, runs them without the debugger: breakpoints stop in the
main process only.

A pytest test is debugged from its test file:

- **Debug Test at Caret** (`test.debugAtCaret`) debugs the test at the caret
  (the whole file outside any test).
- The right-click menu of a test's gutter ▶ line has **Debug This Test** (or
  **Debug This Class**).
- In [Test Results](/docs/build-tools#test-runner), a Python test's
  right-click menu has **Debug This Test**.

The test runs as `python -m pytest` with its node id, from the project root (the
file's folder when there is no project), under the debugger. When the
interpreter has no pytest, a `unittest.TestCase` test runs under
`python -m unittest` instead; for any other test the status bar names the
interpreter that lacks pytest. A Python
[run configuration](#run-configurations) runs but cannot be debugged; debug its
file instead.

### Program input in the Debug console

A debugged **Java** or **Python** program can read its standard input from the
Debug console, so a Python program that calls `input()` waits for a line typed
there. The field does one of two things, and its prompt says which:

- While the program **runs**, Enter sends the line to the program (an empty
  line counts).
- While it is **paused**, Enter evaluates an expression.

End the input with `Ctrl+D` in the field, the console's right-click menu, or
*Debug: End Program Input (EOF)* (`debug.endProgramInput`).

In this mode Editora starts the program itself rather than leaving it to the
debugger, and stops it on Stop, Restart, a new launch, a failed launch, or
closing the window. The session ends when the program exits, after its last
output, and the console reports the exit code. A Python program's output is
unbuffered, so a prompt shows before the program waits. Lines written to stdout
and stderr at almost the same moment can appear in either order.

It is **on by default**. The switch, **Run the debugged program in the Debug
console (program input)**, is on the Java card of **Settings → Debugging** and
applies to Python too (or use *Debug: Toggle Program Input in the Debug
Console*, `debug.toggleProgramConsole`). It takes effect at the next launch.
Attached sessions, JavaScript, and Python files on a remote host, in WSL or in a
container are unaffected. It has not yet been confirmed on Windows.

Compact `.java` sources and extensionless Java `--source 25+` shebang files can
be debugged with breakpoints, stepping, and local variables. Editora maps
breakpoints and stack frames to the original file and removes its temporary
compiled files after the session. Java debugging needs the Java language server
and debug adapter described below.

JUnit tests are debugged from their gutter icon or with *Debug Test at Caret*,
including tests inside `@Nested` classes; see the
[test runner](/docs/build-tools#test-runner).

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

### Debugging on a remote host

A file in a [remote folder](/docs/remote) is debugged on its host, when that
host [may run commands](/docs/remote#allowing-a-host-to-run-commands), with
breakpoints, stepping, variables and the call stack as for a local file.
Breakpoints of a remote file are kept across reconnects.

- **Python** runs debugpy on the host, which needs its own
  (`pip install debugpy`). The program itself runs as Run runs it there: under
  the project's virtual environment on the host, else the host's **Python for
  Run and tests**, with the project root and `src/` on `PYTHONPATH`. debugpy
  runs under the host's **Python for debugging**.
- **JavaScript** runs vscode-js-debug and `node` on the host.
- **Java** in a Maven or Gradle project goes through the host's `jdtls`: Debug,
  Debug Main Class, Attach (where `localhost` is the host), Debug Test, and a
  saved configuration. Editora copies its java-debug plugin to
  `~/.cache/editora` on the host. A Java file outside a project is compiled on
  the host for the debugger.

The adapters' locations can be set per host in Settings → Remote (**Python for
debugging**, **js-debug server**, **Node.js**, **java-debug plugin jar**). Other
languages cannot be debugged on a host yet.

### Debugging in a container or WSL

A project whose tools [run in a container](/docs/remote#tools-in-a-container)
or [in a WSL distribution](/docs/remote#tools-in-wsl-windows) is debugged there,
as on a remote host: Python with the debugpy installed there (the program under
the project's virtual environment there, as Run uses it), JavaScript with
vscode-js-debug and `node` there, and Java through the jdtls running there, with
Editora's java-debug plugin copied into `~/.cache/editora`. A Java file outside
a project is compiled there for the debugger. The debugger's port inside is
reached from here whatever the container's network, through a relay that needs
`nc`, `bash` or `python3` inside (see
[Ports and debuggers](/docs/remote#ports-and-debuggers)).

### Installing adapters

Adapters are not bundled. Three palette commands download each language's
adapter, together with its language server, into your config folder:

| Command | Installs | Needs |
| --- | --- | --- |
| **Install: Java Language Support** (`install.javaSupport`) | jdtls and the Microsoft java-debug plugin | `tar`, and a JDK to run `jdtls` |
| **Install: Python Language Support** (`install.pythonSupport`) | Pyright and debugpy | Python with `pip` for debugpy; Node.js with `npm` for Pyright |
| **Install: JavaScript / Node Language Support** (`install.jsSupport`) | `typescript-language-server` and vscode-js-debug | Node.js with `npm`, and `tar` |

Each language's card on the **Settings → Debugging** page has the same
**Install…** button. An install fetches only what is missing. When one tool's
runtime is missing, the others still install and the status bar names the one
that waits (debugpy installs without Node.js, for example). When Python has no
`pip`, the error says so. After a debugger installs while debugging is off, the
status bar says where to turn it on.

Editora does not install those runtimes for you. A downloaded server or adapter
is unpacked beside the installed version and replaces it only once it is
complete, so a failed install leaves the working one in place.

To install an adapter by hand instead, the Editora repository has a helper
script for each:

- **Java**: the Microsoft java-debug plugin (layered onto `jdtls`),
  `scripts/install-java-debug.sh`. If your `jdtls` distribution already bundles
  and loads java-debug itself (Homebrew's does), Editora detects that and needs
  no second copy.
- **Python**: `debugpy`, `scripts/install-debugpy.sh`.
- **JavaScript**: vscode-js-debug, `scripts/install-js-debug.sh`.

The Settings → Debugging page shows a live found/not-found status for each,
also while that language's debugging is switched off. Python's card also has
the **Python interpreter (fallback for Run, Debug and code intelligence)**
field: the Python used when a project has no interpreter of its own, and the
one that runs debugpy when the project's interpreter cannot import it.

For sending HTTP requests from `.http` files, see the
[HTTP client](/docs/http-client). For Maven, npm, Cargo, Go, and Gradle tasks,
see [Build tools](/docs/build-tools). A build task that would run a project's
own `./mvnw` or `./gradlew` asks you to
[trust the folder](/docs/workspace#trusted-folders) first.
