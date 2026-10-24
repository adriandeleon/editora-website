---
title: Running & debugging
description: Run files from the gutter, run and debug a project's main class, save run configurations, and debug Java, Python, JavaScript, Groovy, Kotlin, Clojure and Scala.
category: Run & debug
order: 1
---

## Running files

You can run compact Java files, Python scripts, Node.js scripts, shell scripts,
Groovy scripts, Kotlin files, Clojure files and Scala files with
[language services](/docs/lsp) disabled. A green ▶ appears in the gutter of a
runnable file:

- a **Java compact source file** (JEP 512, a `.java` with a top-level
  `void main`) or an extensionless Java file with a `java --source 25+`
  shebang,
- a **Python** script (the ▶ sits on the `if __name__ == "__main__"` guard,
  written either way round, with either quote or in parentheses; see
  [Python scripts](#python-scripts)),
- a **JavaScript or TypeScript** file (`.js`, `.mjs`, `.cjs`, `.ts`, `.mts`,
  `.cts`) or a script whose shebang names `node` (see
  [Node.js scripts](#nodejs-scripts)), or
- a **shell** script: `.sh`, `.bash`, `.zsh`, `.ksh` and the other shell
  types, or a file whose shebang names a shell (see
  [Shell scripts](#shell-scripts)). Startup files that a shell reads rather
  than runs, such as `.bashrc`, `.profile`, `.zshrc`, `.bash_logout`,
  `.xinitrc` and `.envrc`, get no ▶.
- a **Groovy** script or class (`.groovy`): the ▶ sits on its
  `static void main` when it has one, else on the first line. See
  [Groovy scripts](#groovy-scripts).
- a **Kotlin** script (`.kts`, `.main.kts`), with the ▶ on its first line, or a
  `.kt` file with a top-level `fun main`, with the ▶ on that line. See
  [Kotlin files](#kotlin-files).
- a **Clojure** file (`.clj`, `.cljc`, `.bb`, or a script with a `clojure` or
  `bb` shebang): the ▶ sits on its `(defn -main` when it has one, else on the
  first line. EDN files (`deps.edn` and the like), `project.clj` and
  ClojureScript (`.cljs`) get no ▶. See [Clojure files](#clojure-files).
- a **Scala** program: the ▶ sits on an `@main def`, an `object … extends App`
  or an object's `def main(args: Array[String])`, and on the first line of a
  `.sc` script. A build definition (`build.sbt`, `build.mill`, `build.sc`) gets
  none. See [Scala files](#scala-files).

Click the ▶, use the right-click *Run File*, or bind `file.run` (`C-c r`). Output
streams to the **Run** tool window (`M-9`), which accepts **stdin** so
console-style programs work. Double-click a **stack-trace line** to jump to the
file and line: JVM frames from Java, Kotlin, Groovy, Scala and Clojure code
(and Clojure's own `at my-app.core/f (core.clj:4)`), Python and Node frames, and
shell errors such as `deploy.sh: line 12: …`. A Node frame or a `tsc` error opens
at its column too, also for an ES module's `file://` path and a path with spaces.
For Python that covers traceback frames of any file (also a script with no `.py`
extension), `app.py:12:` locations in warnings and in pytest, mypy or Ruff
output, and pdb's `> app.py(12)` lines; `<stdin>` and `<frozen …>` frames have
no file to open.

The console's first line, drawn muted, is the command that started a script or
a test run: which interpreter ran, with which options, on which file.

The console reads the terminal codes programs print: colours are drawn in the
theme's colours, a carriage return (`\r`) rewrites the current line so a
progress bar stays on one line, and `clear` empties the console. Other cursor
codes are dropped. Stop ends the program and everything it started, and the
console then says **Stopped**. On macOS and Linux, a Python or Node.js program
(and npm, pnpm, yarn or bun) started on this computer first gets Ctrl-C
(SIGINT), so it can clean up (a Python program's `finally` blocks and `atexit`
handlers run), and is ended only if it is still running a moment later.

The console keeps the newest output. When a program prints faster than the
console can show, some lines are skipped (the program is never slowed down),
but the last screenful is always complete, and the console says that output
was skipped. When the console reaches its size limit, it drops the oldest
output and says so at the top. **End Input** in the console's header, or
`Ctrl+D` in the empty input field, closes the program's input, so a loop that
reads lines until the end of input can finish (*Run: End Program Input*,
`run.endInput`).

While a program runs, a small **spinner** turns beside the Run window's status,
from the launch (including a configuration's before-launch command) until the
program exits, is stopped, or fails to start, so a script that prints little
does not look stalled. The Run button on the tool-window stripe badges the same
spinner on its icon, which shows a run is alive with the Run window closed or
another window open in its place.

### Which runtime a program runs under

Beside the status, the Run window names the runtime the program runs under:
**Java 25.0.4 (Temurin)**, **Python 3.13.5**, **Node.js 24.18.1**,
**Groovy 5.1.3 (Java 25.0.4)**, **Kotlin 2.4.21 (Java 25.0.4)**, **Clojure CLI 1.12.6.1673**,
**Leiningen 2.13.0**, **Babashka 1.13.226**, **Scala CLI 1.18.0 (Scala 3.9.0)**, **Bash 5.2.37** or **GNU Make 4.4.1**. A Java runtime also shows its
distribution (Temurin, Corretto, Zulu, GraalVM CE and others) when the JDK
names one. Hover the name for the runtime's own version text and, for Java,
the JDK's folder.

The name comes from the very program the run starts, asked in the run's folder
and with its environment. A run configuration's JDK, a virtual environment's
`python`, a version picked by pyenv and a remote host's or container's own
runtime are therefore what it shows, rather than whatever is first on your
PATH. It appears a moment after the launch (the program does not wait for
it), stays after the program exits, and is replaced by the next run. A plain
`sh` script and a compiled program show no runtime, and neither does a
configuration's before-launch command.

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

### Node.js scripts

A `.js`, `.mjs`, `.cjs`, `.ts`, `.mts` or `.cts` file runs under Node.js, and
so does a script whose shebang names `node`. A type declaration file (`.d.ts`)
has no code, so it gets no ▶. JSX and TSX files need a bundler, so Run does not
start them.

**Which Node.js runs.** Run, run configurations, Debug, npm scripts and the
TypeScript language server all use the same Node.js. The first of these wins:

1. a run configuration's **Interpreter** field;
2. the **Node.js** field under **Settings → Debugging → JavaScript**;
3. the version that a `.nvmrc` or `.node-version` file names, when nvm has that
   version installed (the file is looked for from the script's folder up to its
   package folder). A version such as `20` or `20.11` picks the newest matching
   install; an alias such as `lts/*` is not followed;
4. `node` on your `PATH` (the login shell's `PATH`, so the default of nvm, fnm
   or Volta counts).

[Doctor](/docs/troubleshooting#doctor) has a **Node.js** row that names the
Node.js in use and why it was chosen, for example *Node.js setting* or
*.nvmrc, via nvm*.

**Where it runs.** A script runs in its package folder: the nearest folder above
it with a `package.json`, never one inside `node_modules` and never your home
folder. Without a `package.json`, it runs in the script's own folder.

**How it runs.** Editora adds `--enable-source-maps`, so a stack trace from
compiled code names the original source, and sets `FORCE_COLOR=1`, so the Run
console shows the program's colours (unless you set `NO_COLOR`). A `node`
shebang keeps its flags, such as `#!/usr/bin/env -S node --no-warnings`, but
runs under the Node.js chosen above. A shebang that names `deno`, `bun`, `tsx`
or `ts-node` runs with that program, as written.

**TypeScript** runs on plain Node.js 22.18 or newer, which removes the types
itself; no `tsx` or build step is needed. On an older Node.js, Run says that
the file needs 22.18 or newer instead of showing a syntax error.

**Test files.** The ▶ of a test file runs `node --test` and shows the results
in [Test Results](/docs/build-tools#test-runner). A test file is a
`*.test.*` or `*.spec.*` file, or any script under a `test/`, `tests/` or
`__tests__/` folder. **Rerun Failed** and **Rerun This Test** work on these
results.

While the program runs, the Run window names its runtime, such as
**Node.js 24.18.1** (see
[Which runtime a program runs under](#which-runtime-a-program-runs-under)).

### Groovy scripts

A Groovy file runs with `groovy` on the JDK chosen in Settings for builds and
runs (the one Maven, Gradle and Java use), which Editora passes as
`JAVA_HOME`. Editora finds `groovy` through `GROOVY_HOME`, then your `PATH`,
then SDKMAN. When none of them has it, Run says Groovy is not installed instead
of starting anything. A Gradle build script (`build.gradle`) and a
`Jenkinsfile` are Groovy too, but they get no ▶: nobody runs those as a
program.

### Kotlin files

Kotlin runs with the Kotlin compiler, `kotlinc`, on the JDK chosen in Settings
for builds and runs (the one Maven, Gradle and Java use). Editora finds
`kotlinc` through `KOTLIN_HOME`, then your `PATH`, then SDKMAN (`sdk install
kotlin`). When none of them has it, Run says Kotlin is not installed instead of
starting anything. Only `kotlinc` is used: JetBrains' newer Kotlin toolchain
command is also called `kotlin` and is not the compiler.

- A **script** (`.kts`, `.main.kts`) runs with `kotlinc -script`, from the top.
- A **program** (a `.kt` file with a top-level `fun main`) is compiled first,
  together with the Kotlin standard library, into a jar that Editora keeps
  in its config folder, and then runs with `java -jar`. The compile shows in the
  Run window, and Stop ends it. Running the file again without changing it
  skips the compile; after an edit the file is compiled again and the old jar
  is deleted. The file is compiled on its own, so a `.kt` file that uses other
  files of a Gradle project runs through the build tool instead.

A Gradle build script (`build.gradle.kts`, `settings.gradle.kts`) gets no ▶. A
Kotlin program in a [remote folder](/docs/remote) is not compiled from here;
a Kotlin script there runs on its host.

### Clojure files

A Clojure file runs with one of three tools, picked from the file and its
project, the nearest folder above it with a `deps.edn`, `project.clj` or
`bb.edn`:

| The file | Runs as |
| --- | --- |
| a namespace with `-main` in a `deps.edn` project | `clojure -M -m my-app.core`, in the project folder |
| a namespace with `-main` in a `project.clj` project | `lein run -m my-app.core`, in the project folder |
| a namespace with `-main` in no project | `clojure -M -m hello`, with the file's folder on the classpath |
| any other `.clj` or `.cljc` file | a script: `clojure -M <file>` (or `lein run -m clojure.main <file>`) |
| a `.bb` file, a `#!/usr/bin/env bb` script, or any file of a project with only a `bb.edn` | `bb <file>` |

A namespace runs through `-m` only when its name matches its path, such as
`my-app.core` in `src/my_app/core.clj`, so the tool can find it on the
classpath. A namespace outside `src` is added to it for the run. A project with
both a `deps.edn` and a `project.clj` runs with the Clojure CLI.

The Clojure CLI and Leiningen run on the JDK chosen in Settings for builds and
runs. Since Clojure 1.10.1 the CLI writes an uncaught exception's stack trace
to a temporary file and prints only a summary; Run sets
`clojure.main.report=stderr`, so the whole report is in the Run console and its
frames are links.

Editora finds `clojure`, `lein` and `bb` on your `PATH`. When the one a file
needs is missing, Run says so instead of starting anything. On Windows the
Clojure CLI is best used under WSL; see
[Running in a container or WSL](#running-in-a-container-or-wsl).

### Scala files

How a Scala file runs depends on the build it belongs to:

- A file of an **sbt** build (a `build.sbt` above it) runs its main class
  through sbt, in the sbt console of the [Output window](/docs/build-tools):
  `sbt "runMain demo.hello"` with your arguments, from the build's root (the
  highest folder with a `build.sbt`).
- A file of a **Mill** build runs through Mill the same way:
  `./mill app.runMain demo.hello`, where `app` is the module whose `src` folder
  holds the file. The project's own `./mill` launcher is used when it has one,
  after the same [trust](/docs/build-tools) question as for `./mvnw`.
- A file of a **Gradle** or **Maven** build is run from that build's tool
  window; Run says so instead of running the file on its own.
- Any other file, and a `.sc` script, runs with **Scala CLI**:
  `scala-cli run Main.scala -- args`. In a Scala CLI project (a folder with
  `project.scala`), the whole folder runs, with the file's main class. Scala
  CLI reads the `//> using` directives for dependencies and the Scala version.

Scala CLI runs on the JDK chosen in Settings for builds and runs, which Editora
passes as `JAVA_HOME`. Editora finds it as `scala-cli` on your `PATH`, then
`scala` (the `scala` command is Scala CLI since Scala 3.5), then SDKMAN's
`scalacli` and `scala`. When there is none, Run says Scala CLI is not installed
instead of starting anything. The first run of a Scala version downloads its
compiler, so it takes longer.

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

A project on this machine can run on a host too: set a configuration's **Run
on** to a [deployment](/docs/deployments), and Editora copies what changed in the
project to a folder on the host before each run, then runs the program there.

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
  classpath (`compile dependency:build-classpath`) and runs the file's
  `main`, and Gradle delegates to its `run` task, or `bootRun` in a Spring Boot
  project. In a multi-module Maven build the fallback finds the module's
  reactor through the poms' `<modules>` (an aggregator one level down, or a
  `modules/` folder without a pom of its own, counts; the search never leaves
  the open project) and, when the root lists the module, runs from the root
  with `-pl <module> -am`, so a submodule that depends on an uninstalled sibling
  still runs. A module the reactor does not hold builds alone, and a build that
  fails with "Could not find the selected project in the reactor" is retried
  for the module alone; the Run console says which mode ran. The checked
  profiles and flags of the Maven window are appended (its *Reactor* toggle is
  not: the fallback decides that itself). The Maven it runs is the one the
  Maven window runs (`./mvnw` once trusted, the command override, else `mvn` on
  your `PATH`), in the Run console: its output streams there, Stop ends it, and
  a second Run is refused while it runs. When it fails, the status quotes
  Maven's first `[ERROR]` line. The program's classpath carries the compile and
  runtime scopes only; a class under `src/test` gets the test classpath. The
  program runs on the Default JDK's `java`; with none selected, on
  `$JAVA_HOME/bin/java`, the one Maven compiled with, and only without a
  `JAVA_HOME` on the `java` of your `PATH`.

The fallback compiles as part of resolving the classpath, and in a Maven or
Gradle project on this computer the language-server path **builds first** too:
Maven's `compile` (`test-compile` for a class under `src/test`) or Gradle's
`compileJava` (`compileTestJava`) runs in the Run or Debug console before the
launch, through the project's wrapper once trusted, the command override, or
the tool on your `PATH`. The Maven compile builds from the reactor root with
`-pl <module> -am` when the root lists the module, else the module alone, and
carries the Maven window's checked profiles and flags, as the fallback does.
A failed build aborts the launch, so a fresh clone no
longer dies with `ClassNotFoundException` and an edited file no longer runs
the previous binary. **Settings → Languages & Tools → Build Tools → Build
before Run and Debug** (on) turns it off; a run configuration with its own
[before-launch step](#a-step-before-the-launch) keeps that step and is not
built twice. A launch that ran without the build prints a hint to build when
the JVM cannot find the main class.

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

**Run on** picks where the configuration runs: **This machine**, or one of the
project's [deployments](/docs/deployments), a folder on an SSH host that the
project is copied to before the run. **Deployments…** beside it opens the list.

### What a configuration can launch

Pick a **Type** in the project's **Run Configurations** window:

| Type | Launches |
| --- | --- |
| Java main class | a `main` in the project, via the language server or the build tool |
| Python script | a `.py` file with the project's Python (see [Python scripts](#python-scripts)) |
| Python module | a module by its name, as `python -m app.main` does, from the project root |
| Shell script | a script with the interpreter its shebang names (or `bash`), unless **Interpreter** is set |
| Node.js script | a `.js` or `.ts` file under Node.js |
| Groovy script | a `.groovy` file with `groovy`, on the configuration's JDK; its VM arguments go into `JAVA_OPTS` |
| Kotlin file | a `.kts` script with `kotlinc -script`, or a `.kt` program compiled and run with `java -jar`, on the configuration's JDK |
| Clojure script | a Clojure file with the tool [Run File picks](#clojure-files), on the configuration's JDK; its VM arguments go to the program's JVM (`-J` arguments for the Clojure CLI, `JVM_OPTS` for Leiningen) |
| Scala (Scala CLI) | a `.scala` or `.sc` file with Scala CLI, on the configuration's JDK; its VM arguments are passed as `--java-opt` |
| Make target | a target in a makefile |
| npm script | a named `package.json` script, run with the project's package manager |

The form **shows only the fields the selected type uses**. A Java main class
has no Script / target field; a Python, Node.js, shell, Make or npm
configuration has no Main class, Module, VM arguments or JDK, and a Groovy or
Clojure or Scala one keeps only VM arguments and JDK of those. A hidden field
keeps its value, so switching the type and back loses nothing. A Python,
Node.js, shell, Groovy, Clojure or Scala file can be picked with **Browse…**
beside Script / target;
a script inside the project is saved relative to the project root. A Clojure
configuration with no working directory runs in its project's folder. For an
npm configuration, **Browse…** lists the scripts in the `package.json` instead.

A Java main class configuration has **Browse…** beside **Main class**: pick a
`.java` file and its fully qualified class name is filled in, so
`src/main/java/test/App.java` gives test.App. The name comes from a `main`
method the file declares, or else from the file's package and name. A
configuration still named "New Configuration", or named after the class it
launched before, is renamed after the new class; a name you typed is kept.

A **Node.js script** configuration has the script, **Node.js arguments** (such
as `--max-old-space-size=4096`), **Interpreter**, program arguments and
environment. Left blank, **Interpreter** uses the Node.js that Run uses (see
[Node.js scripts](#nodejs-scripts)), and a blank working directory is the
package folder, the nearest one with a `package.json`.

An **npm script** configuration runs `<manager> run <name>`, with its arguments
after `--`. The manager is the one the project uses (npm, pnpm, yarn or bun), as
in the [Package Scripts](/docs/build-tools#npm-and-other-package-managers)
window, so a pnpm workspace member runs pnpm too. The npm command set in
Settings → Build Tools applies only when it names that same manager, or is a
wrapper script.

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
Java, Groovy, Kotlin, Clojure, Scala, Node.js script and npm script configurations can be debugged. A Node.js
or npm configuration is debugged the way Run starts it, with the same Node.js,
package manager, folder and environment (see
[Debugging JavaScript and TypeScript](#debugging-javascript-and-typescript)).
Debugging a Node.js or npm configuration of a remote project is not supported
yet. Debug on any other type says so, and for a shell configuration it points to
[Run with Trace](#run-with-trace).

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

The step may name a build tool instead of a program: `maven:compile` or
`gradle:compileJava` runs that goal or task through the project's own Maven or
Gradle (its wrapper once trusted, the command override, else the tool on your
`PATH`), resolved at run time, so the saved configuration does not freeze a
command that goes stale when your Maven moves. A project generated by
[New Maven Project](/docs/build-tools#new-maven-project) stores `maven:compile`
and leaves the working directory blank, which means the project root. A saved
Java configuration builds and runs in the module its project name (the pom's
`artifactId`) or its main class's source file belongs to, whatever file is
active when you run it.
A Java configuration with a step of its own runs that step instead of the
automatic build before a launch.

### Running in a Terminal tab

A configuration's **Console** option, **Run in a Terminal tab**, runs its
program in the [Terminal](/docs/terminal#run-and-debug-programs-in-a-terminal)
tool window instead of the Run console. Use it for programs that draw on the
screen, read single keys, or ask for a password, which need a real terminal.
It works for every type, on this computer, on the project's host, and in its
container or WSL distribution.

- **The step before the launch** still runs in the Run console first.
- **The tab** is named after the configuration. When the program ends, it
  stays, with the program's exit code in the corner, and the status bar gives
  the exit code too. Closing the tab while the program runs asks first.
- **The Run console's own tools don't apply.** Stack-trace links, the runtime
  chip, End Input and Stop belong to the console. Rerun repeats the last run
  that used the console, so after a terminal run it is unavailable until the
  next console run.

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
an active Python, Node.js or shell script it adds a configuration of that type
for the script, named after the file. For a Java file it
prefills the main class from the active Java file (or the one your Gradle build
declares as `mainClass`, `mainClass.set(…)`, or the legacy `mainClassName`), names
the configuration after that class, and puts the cursor in whichever field still
needs you. Adding twice from the same file gets you "App" and "App (2)" rather
than two entries sharing a name.

**Run: Save Run Configuration…** saves the active Java file's main class, or the
active Python, Node.js or shell script, with the file's program arguments. For
a script, Add and Save write the folder its gutter ▶ runs it in into **Working
directory**, so the configuration runs exactly as ▶ does. It is left blank when
that folder is the project root, and for a Node.js script, which runs from its
package folder.

Running an **incomplete** configuration opens its form at the field you need to
fill in, rather than naming the problem and leaving you to find it. VM arguments
and environment variables apply to both Run and Debug. Before a script
configuration runs, Editora saves that script if it has unsaved edits in any
tab.

A saved configuration does not depend on which tab is in front: any open Java
file in the project serves, and it reports an error only when there isn't one.

## Debugging

<span class="beta-pill">Beta</span>

Editora debugs **Java**, **Python**, **JavaScript and TypeScript (Node.js)**,
**Groovy**, **Kotlin**, **Clojure** and **Scala** through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window (`M-g d`). Debugging
is **off by default**; enable it in **Settings → Debugging**. Shell scripts
can't be debugged; [Run with Trace](#run-with-trace) shows each line of a
script as it runs.

| Action | Command | Default key |
| --- | --- | --- |
| Start / continue | `debug.start` | `C-c C-d d` |
| Continue | `debug.continue` | `C-c C-d c` |
| Pause | `debug.pause` | `C-c C-d p` |
| Stop | `debug.stop` | `C-c C-d k` |
| Restart | `debug.restart` | `C-c C-d r` |
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
| Attach to a running JVM or Node.js | `debug.attach` | `C-c C-d a` |
| Turn the Python or JavaScript adapter on or off | `debug.toggleAdapter` | (palette) |
| Set an adapter's path | `debug.setAdapterPath` | (palette) |

The keys are the Emacs keymap's; the other keymaps' keys are listed under
[Debugger keys](/docs/keymaps#debugger-keys). The step commands are also
buttons in the Debug window, and the **Run** menu lists every debugger action.

**Jump to Line** moves the program to another line without running the code in
between. It needs a debugger that can do that: Python's debugpy can. The Java
and JavaScript debuggers cannot, and Jump to Line then says it is not supported.

The Debug window has a threads and call-stack view, a lazy variables tree with
**set-value**, **watches**, and an evaluate console. While suspended, **inline
values** appear after each line and hovering a variable shows its value.
Hovering an attribute shows the whole expression: the `x` of `p.x` shows
`p.x`, not a variable named `x`. Hovering stops at a call or an index, so it
never runs code.

In Variables and watches, each row's name, value and type are coloured like
code in the current editor theme: strings, numbers, constants such as `true`,
`null` and `None`, and type names, including each element of a list or dict
value. A watch that cannot be evaluated shows the debugger's message in red
italics instead.

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

Beside that status, the Debug window names the runtime the debugged program
runs under, as the [Run window does](#which-runtime-a-program-runs-under): the
JDK of a Java launch, the Python that debugpy runs the script with, or the
Node.js of a JavaScript launch. The name clears when the session ends. A
session started with *Debug → Attach* shows none, because the program was
already running.

When a file cannot be debugged, the status bar says why and what to do: Python
or JavaScript debugging is turned off, debugpy was not found for the Python
interpreter, or vscode-js-debug or Node.js was not found.

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
| Groovy, Kotlin, Clojure, Scala | Caught Exceptions, Uncaught Exceptions | Uncaught Exceptions |

A session starts with the filters its debugger turns on by default, so a
Python program stops where an uncaught exception is raised, with the
exception's type and message in the Debug window and the status bar. In
Python, `sys.exit()` with a non-zero code also counts as an uncaught exception;
Continue then ends the program. Editora keeps your choice for each language and
each project, as it does for **Break on Caught Exceptions** and **Break on
Uncaught Exceptions** (see [Managing breakpoints](#managing-breakpoints)).

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
- The editor's right-click menu on a test has **Debug This Test** (or **Debug
  This Class**).
- In [Test Results](/docs/build-tools#test-runner), a Python test's
  right-click menu has **Debug This Test** (a class row: **Debug This Class**).

The test runs as `python -m pytest` with its node id, from the project root (the
file's folder when there is no project), under the debugger. When the
interpreter has no pytest, a `unittest.TestCase` test runs under
`python -m unittest` instead; for any other test the status bar names the
interpreter that lacks pytest. A Python
[run configuration](#run-configurations) runs but cannot be debugged; debug its
file instead.

### Program input in the Debug console

A debugged **Java**, **Python** or **JavaScript** program can read its standard
input from the Debug console, so a Python program that calls `input()` waits
for a line typed there.
The field does one of two things, and its prompt says which:

- While the program **runs**, Enter sends the line to the program (an empty
  line counts).
- While it is **paused**, Enter evaluates an expression.

End the input with `Ctrl+D` in the field, the console's right-click menu, or
*Debug: End Program Input (EOF)* (`debug.endProgramInput`).

In this mode Editora starts the program itself rather than leaving it to
the debugger, and stops it on Stop, Restart, a new launch, a failed launch, or
closing the window. The session ends when the program exits, after its last
output, and the console reports the exit code. A Python program's output is
unbuffered, so a prompt shows before the program waits. Lines written to stdout
and stderr at almost the same moment can appear in either order.

It is **on by default**. The switch, **Run the debugged program in the Debug
console (program input)**, is under **Settings → Debugging → Java** (or use
*Debug: Toggle Program Input in the Debug Console*,
`debug.toggleProgramConsole`) and takes effect at the next launch. It applies
to a launched Java program, and to a Python or JavaScript program started on
this computer; a Python or JavaScript file on a remote host, in WSL or in a
container is unaffected, and so are attached sessions. It has not yet been
confirmed on Windows.

**Run the program in a Terminal tab instead**, just below it, runs that program
in a tab of the [Terminal](/docs/terminal#run-and-debug-programs-in-a-terminal)
tool window rather than the Debug console. Use it for a program that draws on
the screen, reads single keys or asks for a password. Its output and input are
then in that tab, which is titled *Debug: <file>* and stays when the program
ends. The program's exit still ends the session, and Stop ends the program. It
is off by default and needs the program console switch on.

With the switch off, the debugger starts a Java program itself. The console
still reports the program's exit code on Linux and macOS when the program runs
on this machine. On Windows, and on a remote host, in WSL or in a container, it
says "The program ended." instead. A JavaScript program's output, including
`process.stdout.write`, still reaches the console, but the program cannot read
input.

### Debugging Java

Compact `.java` sources and extensionless Java `--source 25+` shebang files can
be debugged with breakpoints, stepping, and local variables. Editora maps
breakpoints and stack frames to the original file and removes its temporary
compiled files after the session. Java debugging needs the Java language server
and debug adapter described below.

JUnit tests are debugged from their gutter icon or with *Debug Test at Caret*,
including tests inside `@Nested` classes; see the
[test runner](/docs/build-tools#test-runner).

### Starting a Java session

**Debug** on a Java file debugs that file's own `main`. When the file has no
`main` and its folder has a single main class, Debug runs that class, and the
Debug console says which program runs. The gutter's Debug ▶ and Debug on a file
use the saved [run configuration](#run-configurations) that names the same
class, so its arguments, VM options, environment, working directory and JDK
apply, as they do for Run ▶. Before a Java launch, Editora saves every unsaved
Java file of the project.

In a Maven or Gradle project, Debug builds first, so the program runs the code
you see. With **Build before Run and Debug** on (the default), the build tool
compiles in the Debug console (Maven `compile`, or `test-compile` for a class
under `src/test`; Gradle `compileJava` or `compileTestJava`), through the same
Maven or Gradle the tasks tree runs, and a failed build aborts the launch. With
the setting off, Debug asks the Java language server to build the project
instead: if that build finds compile errors, nothing is launched, the Debug
console lists the errors as links to their lines, and the status bar gives
their number; if it cannot run, the program starts anyway, and the console
warns you when a class file is missing or older than its source. Stop during
either build cancels the launch. A run configuration with a
[before-launch step](#a-step-before-the-launch) uses that step as its build
instead. A Java configuration's step runs in the project root, whatever the
configuration's working directory.

A class with a `package` line in a folder that has no `pom.xml` or Gradle build
can be debugged too, and such a class has **Debug File** in the editor's
right-click menu. In a project window for such a folder, *Debug Main Class* and
Debug on a saved Java configuration work, and the program runs in the project
root.

Debug in another Java tab while a session runs asks before it stops that
session. When Java debugging can't start, the status bar names the missing
piece: LSP is off, the Java language server is turned off, or jdtls or
java-debug was not found. The Java card of **Settings → Debugging** has an
**Install…** button for what is missing.

### Stepping through Java code

Step Into skips JDK classes (`java.*`, `javax.*`, `jdk.*`, `sun.*`, `com.sun.*`
and a few more) and synthetic methods, and a frame's variables include its
class's static fields. You can turn either off on the Java card of
**Settings → Debugging**: **Step Into skips JDK classes and synthetic methods**
and **Show static fields in Variables**.

When you step into JDK or library code anyway, or click such a frame in the
call stack, its source opens in a read-only tab. A stop at a breakpoint, a
Pause or an exception still selects the nearest frame of your own code.

**Debug: Smart Step Into…** (`C-c C-d s`, also in the Run menu) lists the
calls on the current line, for example `add(twice(1), twice(2))`, `twice(1)`
and `twice(2)`. Pick one to step into it. On a line with a single call it
steps straight in.

**Debug: Restart Frame** (`C-c C-d f`, the call stack's right-click menu, or
the Run menu) runs the method of the selected frame again from its first line
and pauses there. It is greyed out on the bottom frame.

If the connection to the debugger is lost, Editora ends the program at once and
says so in the Debug console and the status bar.

### Editing during a session

The running program keeps the code it was started with. When you edit a file
during a session, Editora matches breakpoints and the stopped line to the code
the program runs, so they stay on the right lines. At a stop in an edited file,
the status bar says that the program still runs the code it started with. A
breakpoint on a line you added during the session is kept, and its hover says
that it takes effect in the next session. To run the new code, restart the
session, or let [hot code replace](#hot-code-replace) load a change to a
method body.

### Hot code replace

During a Java session, saving a `.java` file loads the change into the running
program without restarting it. Editora builds the project with the Java
language server and sends the changed classes to the program. The Debug
console and the status bar say which classes were replaced, or why nothing
was:

- the project has compile errors: fix them and save again;
- the program refused the change, with its reason. A change to a method's body
  can be loaded; a change such as adding or removing a method or a field needs
  a restart, and the console tells you so;
- the Java language server could not build the project;
- the program already runs this code.

Choose when this happens with **Apply code changes to a running Java program**
on the Java card of **Settings → Debugging**:

| Choice | What a save does |
| --- | --- |
| **On save** (default) | loads the change when you save a file yourself; autosave does not |
| **Only with Debug: Apply Code Changes** | shows a hint in the status bar; run **Debug: Apply Code Changes** (`C-c C-d h`, Run menu) to load the saved changes |
| **Never** | nothing; Debug: Apply Code Changes says that it is off |

Debug: Apply Code Changes loads only what is saved. When the active Java tab
has unsaved edits, the console says so.

Hot code replace also runs in an attached session and in Debug Test and Debug
via Build Tool sessions. There the program runs the classes your build tool
compiled, so a change can be refused that a session Editora started would
accept; the console then tells you to restart.

### Debugging JavaScript and TypeScript

**Debug** on a JavaScript or TypeScript file (`.js`, `.mjs`, `.cjs`, `.ts`,
`.mts`, `.cts`, or a `node` shebang script) runs it under vscode-js-debug with
the same Node.js, folder and flags as Run (see
[Node.js scripts](#nodejs-scripts)). Choose that Node.js in the **Node.js**
field under **Settings → Debugging → JavaScript**; left blank, Editora uses the
version a project's `.nvmrc` or `.node-version` names (when nvm has it), else
`node` on your `PATH`. The card's **Detected** line names that Node.js version
and where vscode-js-debug was found.

- **TypeScript** files are debugged directly on Node.js 22.18 or newer. With an
  older Node.js, Debug says so. In a project that compiles its TypeScript, a
  breakpoint in a `.ts` source stops there when the compiled JavaScript runs,
  through its source maps.
- **JSX and TSX** files can't be run by Node.js. Debug the compiled JavaScript
  instead; a breakpoint in the `.jsx` or `.tsx` file still stops there through
  its source map.
- **Output and input.** All output reaches the Debug console, including
  `process.stdout.write` and `readline` prompts, and the program can read lines
  you type there (see
  [Program input in the Debug console](#program-input-in-the-debug-console)).
- Node.js's own modules are skipped, so they don't fill the call stack and Step
  Into doesn't enter them.
- A breakpoint condition that isn't valid JavaScript marks the breakpoint as
  rejected, and the console says why, once.
- Child processes and worker threads show all their threads.
- **Debug Test at Caret** (`test.debugAtCaret`) on a `node:test` test runs that
  test under `node --test` and stops at its breakpoints.
- **Node.js script** and **npm script** [run configurations](#run-configurations)
  can be debugged, started the way Run starts them.

**Attach to a running program.** Start Node.js with `--inspect` (for example
`node --inspect server.js`), then run **Debug: Attach to JVM…** (`debug.attach`)
on a JavaScript or TypeScript file. It asks for the inspector's `host:port`,
such as `localhost:9229`. A program started with `--inspect-brk` waits before its
first line; attaching does not start it, so press Continue.

vscode-js-debug itself needs Node.js 18 or newer to run. When it can't start,
or the Node.js is too old, Debug says so instead of stopping without a word.
**Install…** on the JavaScript card installs vscode-js-debug v1.117.0, a version
tested with Editora.

### Managing breakpoints

A breakpoint's glyph shows its kind by shape as well as colour: a dot for a
plain breakpoint, a diamond for a logpoint, two "=" bars for a breakpoint with
a condition or a hit count, and grey for a disabled one. Its hover names the
condition, log message and hit count, and says when it is disabled. A hit
count stops on that hit in Java too: `3` stops the third time the line runs.

Right-click a glyph for **Edit…**, **Enable Breakpoint** or **Disable
Breakpoint**, **Remove Breakpoint** and **Breakpoints…**. The editor's
right-click menu has Toggle Breakpoint and Edit Breakpoint, and while the
program is paused also Run to Cursor, Show Value at Caret and Add to Watches.

A breakpoint can't be set on a blank line or on a line that holds only a
comment; the status bar says so. Removing one is always possible. A line with
only a closing `}` takes a breakpoint.

**Debug: Breakpoints…** (`C-c C-d l`) lists every breakpoint. Type to filter
it; Enter jumps to the selected breakpoint, Alt+E enables or disables it,
Alt+D removes it, and Alt+M mutes them all. **Debug: Mute Breakpoints**
(`C-c C-d m`) turns every breakpoint off without removing it, until you run it
again. While breakpoints are muted, every glyph is faded, a glyph's hover says
that none of them stops the program, Mute Breakpoints is checked in the Run
menu, and the Debug window's state line says "breakpoints muted". Editora keeps
the choice for each project, so a muted project is still muted when you open it
again. **Debug: Enable / Disable
Breakpoint** (`C-c C-d t`) switches the breakpoint on the caret line. These
commands are in the Run menu's **Breakpoints** submenu.

**Debug: Break on Caught Exceptions** stops wherever an exception is thrown,
even one the program catches. **Debug: Break on Uncaught Exceptions** stops on
an exception that nothing catches. Both are in the Run menu, checked when on,
and are the **Caught** and **Uncaught** buttons in the Debug window's toolbar.
They act on the running session's language, or else on the active file's, and
Editora keeps your choice for each project. To stop only on some Java exception
classes, list them, separated by commas, in **Exception breakpoints: only these
classes** on the Java card of **Settings → Debugging** (for example
java.lang.IllegalStateException). Left blank, Caught and Uncaught stop on every
exception.

At an exception stop, the line that threw is drawn red, and the exception's
type and message are the top row of Variables.

### Inspecting values

The line the program is stopped on is amber, with an arrow in the gutter, and
it is scrolled into view below any sticky header. Inline values appear only on
the lines up to the stopped line. Hovering a name shows its value once the
mouse rests; names in comments and strings are skipped. **Debug: Show Value at
Caret** (`C-c C-d v`) does the same from the keyboard.

A plain value, such as a number, shows on one line. An object, array or
collection shows as a tree that opens with its first level unfolded; unfold a
row to see its fields or elements. A Java `String` is an object to the
debugger, so it shows as a tree too: its value, then its fields. A big array comes a page at a time: its
last row is "Show N more…". Right-click a row for **Copy Value** and **Add to
Watches**. The popup closes with Esc or when the mouse leaves it. When you open
it with Show Value at Caret, the keyboard works it while it is open:

| Key | Action |
| --- | --- |
| Up / Down | Move between rows |
| Right | Unfold the row |
| Left | Fold the row, or move to its parent |
| Enter | Unfold or fold the row, or load the next page on "Show N more…" |
| Shift+Enter | Add the row's expression to Watches |
| Ctrl+C (⌘C on macOS) | Copy the row's value |
| Esc | Close the popup |

Any other key closes the popup and does its usual thing in the editor. When the
popup was opened by the mouse, any key closes it.

In the Variables tree, the right-click menu has **Copy Value** (a string is
copied without its quotes and escapes), **Copy Name** and **Add to Watches**,
which adds the row's whole expression, such as `order.items`. Ctrl+C (⌘C on
macOS) copies the selected value, and Delete removes the selected watch. In
the evaluate field, Up and Down bring back the expressions you typed before.

While the program is paused, **completion** works in the Debug console's
evaluate field, the Add Watch and Edit Watch prompts, and a breakpoint's
condition and log message. Press Ctrl+Space, or type `.`, to list what the
debugger can complete in the stopped frame. Keep typing to narrow the list, use
Up and Down to move in it, Enter or Tab to insert the selected item, and Esc to
close it. In the evaluate field, Enter inserts the item and does not evaluate
yet. In a log message, only the `{expression}` part at the caret is completed.
When the program is not paused, Ctrl+Space says under the field that
completion needs a pause. A breakpoint's condition is completed against the
frame the program is stopped in, not the breakpoint's own line.

Emoji and other characters outside the Basic Multilingual Plane show as
written in Java string values: in Variables, watches, the hover popup and
inline values. One limit remains, from a bug in the Java language server's
debugger: a string that an evaluation builds, such as `name + "!"`, and the
text inside a `toString()` result can still show such characters garbled.

The thread list leaves out the JVM's own housekeeping threads and puts the
stopped thread first, marked "(paused)". After the program ends, the state line
keeps "Exited with code N".

### Keys in the Debug window

Starting a session or stopping at a breakpoint never moves the keyboard focus
into the Debug window: focus stays in the editor, with the caret on the
stopped line. A stop opens the Debug window again if you closed it.

When you click into the Debug window, single keys drive the session, as in
gdb:

| Key | Action |
| --- | --- |
| `c` | Start or continue |
| `p` | Pause |
| `n` | Step over |
| `s` | Step into |
| `f` | Step out (finish) |
| `u` | Run to cursor |
| Shift+K | Stop |
| Shift+R | Restart |

Stop and Restart need Shift, so a stray `k` or `r` typed in a list does nothing
to the program. The keys don't apply while you type in the evaluate field.
Stepping keeps the focus in the Debug window, so you can step again and again.
A toolbar button's tooltip shows both its key in your keymap and its key in the
Debug window, for example "Step Over (F10 · in this window: N)".

### Debug via the build tool

Some applications are meant to be started by the build tool rather than by a bare
`java` command. **Debug: Debug via Build Tool** (`debug.viaBuild`) launches the
app under a suspended JVM through Gradle (`run` or `bootRun`) or, for a Spring
Boot Maven project, `spring-boot:run` with a JDWP agent. Editora
watches the build output for the JDWP banner and attaches the debugger when the
app is listening. It complements *Debug Main Class*, which uses the language
server; a plain Maven `main` has no uniform build-tool debug mechanism, so use
*Debug Main Class* there. Two cases are called out in the status bar: a
`spring-boot-maven-plugin` whose configuration sets its own `<jvmArguments>`
replaces the debug agent Editora passes, so the launch warns up front, and a
build still running after 45 seconds with no banner says the app may have
started without the agent and points at *Debug Main Class* or the plugin's
configuration.

For [Debug Test](/docs/build-tools#test-runner) and Debug via Build Tool, in
Maven and in Gradle, every JVM the build starts listens on a free port, so a
busy port 5005 does not get in the way. Gradle and `spring-boot:run` print the
JDWP banner, which Editora reads for the port. A Maven test JVM is different:
Surefire 3 prints that banner only after the test has run, so Editora picks
the port itself, passes it in `-Dmaven.surefire.debug` and attaches as soon as
the fork listens; the one "Debugger failed to attach: handshake failed" line
the JVM prints is Editora's port check, not an error. For Gradle, Editora
passes an init script that adds the debug agent to
the `test`, `run` or `bootRun` task. A Gradle build that runs on another
machine uses `--debug-jvm` and its fixed port 5005 instead. When a run starts
several JVMs, for example forked test JVMs, Editora attaches to each in turn,
as the session before it ends. **Stop** on
such a session also ends the test or build run that started the JVM, with any
JVMs still waiting.

### Debugging Groovy

Groovy is debugged by a debugger built into Editora, so there is nothing to
install. Debug on a Groovy file runs it as [Run does](#groovy-scripts), and
Debug on a Groovy script run configuration uses that configuration's script,
arguments, folder, environment, JDK and VM arguments.

- **Breakpoints** work in scripts, classes and closures.
- **Stepping** goes from line to line of your code and steps over Groovy's own
  runtime, which sits between nearly every two calls a script makes.
- **Variables** show the frame's locals, `this`, and the fields of any object or
  the elements of an array. A closure's frame is named after the method it is
  in, such as `Report.run { closure }`.
- **Hover and Watch** read a variable or a field path such as `order.total`.
  They never call a method, so looking cannot change the program.
- **Exceptions**: an exception that leaves your code uncaught stops where it was
  thrown. This is on by default for Groovy; the **Caught** toggle also stops on
  exceptions your own code catches. Exceptions Groovy throws and catches inside
  its runtime never stop.

Not supported yet: conditions, hit counts and log messages on a breakpoint (such
a breakpoint shows hollow and says so), evaluating expressions or method calls,
hot code replace, and debugging on a remote host or in a container.

### Debugging Kotlin

Kotlin is debugged by a debugger built into Editora, so there is nothing to
install beyond the Kotlin compiler. Debug on a Kotlin file runs it as
[Run does](#kotlin-files), compiling a program first, and Debug on a Kotlin run
configuration uses that configuration's file, arguments, folder, environment,
JDK and VM arguments.

- **Breakpoints** work in programs, scripts and lambdas, whether or not the
  file sits in a folder named after its package.
- **Frames** are named as Kotlin names them: `Hello.main` for a top-level
  function in `Hello.kt`, and `Hello.main { lambda }` for a lambda inside it.
- **Stepping** goes from line to line of your code and steps over the Kotlin
  standard library and the coroutines machinery.
- **Variables** show the frame's locals, `this`, and the fields of any object or
  the elements of an array. **Hover and Watch** read a variable or a field
  path; they never call a method.
- **Exceptions**: an exception that leaves your code uncaught stops where it is
  thrown, on by default; the **Caught** toggle also stops on exceptions your
  code catches.

For a Gradle project, open a Kotlin file and use
[Debug via Build Tool](#debug-via-the-build-tool): Gradle starts the app under
a suspended JVM and Editora's Kotlin debugger attaches to it, so breakpoints in
`.kt` files bind. **Debug: Attach to JVM…** (`debug.attach`) from a Kotlin file attaches the same
way to any JVM listening for a debugger.

Not supported yet: conditions, hit counts and log messages on a breakpoint (such
a breakpoint shows hollow and says so), evaluating expressions or method calls,
hot code replace, and debugging on a remote host or in a container.

### Debugging Clojure

Clojure is debugged by the same built-in debugger as Groovy, with nothing to
install. Debug on a Clojure file runs it as [Run does](#clojure-files), under
the Clojure CLI or Leiningen, and Debug on a Clojure script run configuration
uses that configuration's file, arguments, folder, environment, JDK and VM
arguments.

- **Breakpoints** work in a namespace's functions and in a script's top-level
  forms. Clojure compiles `src/my_app/core.clj` to classes that name
  `core.clj` as their source, which is how a breakpoint finds them.
- **Stepping** goes from line to line of your code and steps over Clojure's
  runtime and `clojure.core`.
- **Frames** are named as Clojure names the function, such as
  `my-app.core/greet` (not the class `my_app.core$greet`).
- **Variables** show a frame's locals, kept alive for the whole function during
  a session (`clojure.compiler.disable-locals-clearing`), so a local still reads
  after its last use. A value shows as its class, such as `PersistentVector`;
  expand it for its fields. Nothing in the program is called to print it.
- **Exceptions**: an exception that leaves your code uncaught stops where it was
  thrown, on by default as for Groovy.

Babashka scripts can't be debugged: `bb` is a native program, with no JVM for
the debugger to attach to. Debug says so and runs nothing; run the script, or
debug the same code under the Clojure CLI.

The debugger sees the classes your files were compiled to when the program
loaded them. A function you redefine from a REPL is compiled again to a new
class, so a breakpoint set before the redefinition does not stop in it, and
code evaluated in a REPL has no file to stop in. Breakpoint conditions and log
messages, evaluating expressions, and debugging on a remote host are not
supported yet, as for Groovy.

### Debugging Scala

A file of an sbt, Mill, Gradle or Maven build is debugged by
[Metals](/docs/lsp#scala), which has imported the build: Debug asks Metals to
compile the file's build target and start its debug adapter, which runs the
program on the build's classpath. Metals needs to be installed, running for the
file, and done importing the build; otherwise Debug says so. The first session
waits for the compile.

A `.sc` script, a file outside any build, and a Scala CLI project are debugged
by a debugger built into Editora, so there is nothing to install: the program
runs with Scala CLI as Run runs it, with the debugger's agent passed as a
`--java-opt`. Breakpoints bind by file name, since a Scala file's package does
not have to match its folder. Stepping skips the standard library
(`scala.runtime`, the collections, `Predef`). A lambda's frame reads
`Main { lambda }`, a top-level definition's `Main.greet`, and an object's
methods `Util.twice`. Locals, `this` and fields are shown without running any
code in the program. Conditional breakpoints and logpoints are not supported
there yet.

### Debugging on a remote host

A file in a [remote folder](/docs/remote) is debugged on its host, when that
host [may run commands](/docs/remote#allowing-a-host-to-run-commands), with
breakpoints, stepping, variables and the call stack as for a local file.
Breakpoints of a remote file are kept across reconnects.

- **Python** runs debugpy on the host, which needs its own
  (`pip install debugpy`). The program itself runs as Run runs it there: under
  the project's virtual environment on the host, else the host's **Python for
  Run and tests**, with the project root and `src/` on `PYTHONPATH`. debugpy
  runs under the host's **Python for debugging**. The program's output reaches
  the Debug console, but it can't read typed input there.
- **JavaScript** runs vscode-js-debug and `node` on the host. The program's
  output reaches the Debug console, but it can't read typed input there.
- **Java** in a Maven or Gradle project goes through the host's `jdtls`: Debug,
  Debug Main Class, Attach (where `localhost` is the host), Debug Test, and a
  saved configuration. Editora copies its java-debug plugin to
  `~/.cache/editora` on the host. A Java file outside a project is compiled on
  the host for the debugger.

The adapters' locations can be set per host in Settings → Remote (**Python for
debugging**, **JavaScript debugger (vscode-js-debug)**, **Node.js**,
**java-debug plugin jar**). Other
languages cannot be debugged on a host yet.

A Java configuration that runs on a [deployment](/docs/deployments#debugging-on-a-deployment)
is debugged on its host too: the program starts there and the debugger attaches
through a tunnel, with breakpoints in the local files.

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

Adapters are not bundled. Each language's card on **Settings → Debugging** has
an **Install…** button that downloads what is missing, the adapter together with
its language server, into your config folder. The same installs are three
palette commands:

| Command | Installs | Needs |
| --- | --- | --- |
| **Install: Java Language Support** (`install.javaSupport`) | jdtls and the Microsoft java-debug plugin | `tar`, and a JDK to run `jdtls` |
| **Install: Python Language Support** (`install.pythonSupport`) | Pyright and debugpy | Python with `pip` for debugpy; Node.js with `npm` for Pyright |
| **Install: TypeScript / JavaScript (Node.js) Language Support** (`install.jsSupport`) | `typescript-language-server` with TypeScript 6, and vscode-js-debug v1.117.0 | Node.js with `npm`, and `tar` |

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

Each card on **Settings → Debugging** has the adapter's path field with
**Browse…** and a **Detected** line, for example *debugpy: found
(/usr/bin/python3)* (the interpreter that has debugpy) or *js-debug: not
found*, also while that language's debugging is switched off. While **Enable
debugging (DAP)** is off, the cards are greyed and a note says to turn it on.
Python's path field is the **Python interpreter (fallback for Run, Debug and
code intelligence)**: the Python used when a project has no interpreter of its
own, and the one that runs debugpy when the project's interpreter cannot import
it.

For sending HTTP requests from `.http` files, see the
[HTTP client](/docs/http-client). For Maven, npm, Cargo, Go, and Gradle tasks,
see [Build tools](/docs/build-tools). A build task that would run a project's
own `./mvnw` or `./gradlew` asks you to
[trust the folder](/docs/workspace#trusted-folders) first.
