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

![hello.py with a green run arrow in the gutter on its __main__ guard, and the Run tool window below showing the Python 3.13 runtime chip, Process finished with exit code 0, the muted command line and the script's output](/screenshots/docs/run-file.png "A Python script after a run: the ▶ in the gutter, the runtime chip, and the command line above the output.")

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

For Maven projects, **Settings → Build Tools → Default JDK** selects the toolchain used by Run, Debug, Maven classpath resolution,
Maven tasks, and before-launch steps. Installed JDKs are discovered from the
usual platform locations and common version managers. A run configuration's
**JDK** field can inherit that choice or override it for one configuration. The
Default JDK also applies to standalone Java Run and Debug. A Java file or folder
without Maven or Gradle runs on the Default JDK, else on the `java` on your
`PATH`, and the Java language server builds it for that same JDK. A changed
Default JDK reaches a running language server at once, with no restart.

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
5. the **Python interpreter** on the Python card of **Settings → LSP**;
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

1. a run configuration's **Node.js** field;
2. the version that a `.nvmrc` or `.node-version` file names, when nvm has that
   version installed. The file is looked for from the script's folder up to the
   project root, so a monorepo's root `.nvmrc` counts for each of its packages
   (with no project open, the search stops at the package folder). nvm's
   versions are looked for in `$NVM_DIR` (or `$XDG_CONFIG_HOME/nvm`), then in
   `~/.nvm`. A version such as
   `20` or `20.11` picks the newest matching install; an alias such as `lts/*`
   is not followed;
3. the **Node.js** field in the **Node.js** card under **Settings → Build
   Tools**;
4. `node` on your `PATH` (the login shell's `PATH`, so the default of nvm, fnm
   or Volta counts).

When a `.nvmrc` asks for a version that nvm does not have, the run goes on with
the next Node.js in the list, and the Run console says so first, for example
*.nvmrc asks for Node.js 18, which nvm does not have: using the Node.js setting
instead*.

On a remote host, in WSL or in a container, Run, a test file's ▶ and Debug
choose in the same order, with that host's answers. The `.nvmrc` or
`.node-version` is read on the host, from the script's folder up to the project
(or the package folder, with no project). The version comes from the host's
nvm (`$NVM_DIR`, else `$XDG_CONFIG_HOME/nvm`, then `~/.nvm` there). Step 3 is
the **Node.js** set for that host in Settings → Remote → This Host, and step 4
the host's `node`. A version the host's nvm does not have, or an alias such as
`lts/*`, falls back to those two, and the Run console says so.

Editora's other npm language servers (HTML, CSS, JSON, YAML, Bash, Pyright…)
skip step 2: they run on the **Node.js** setting, else `node` on your `PATH`, so
an old Node.js a project pins cannot break them.

The tooltip of the Run window's runtime chip ends with where that Node.js was
chosen from, such as *Chosen from: .nvmrc, via nvm*, on a host too.
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

**TypeScript** runs the first way that works for the file:

1. **The project's tsx.** When `node_modules/tsx` is in the file's folder or a
   folder above it (up to the project root), Node.js runs the file with
   `--import tsx`. It is still the Node.js chosen above, with its flags.
2. **The compiled file.** When the nearest `tsconfig.json` has an `outDir` and
   the file it compiles to exists (`src/index.ts` → `dist/index.js`), that file
   runs, with source maps. The console's first line says so: *Running the
   compiled dist/index.js (built from src/index.ts)*. When the compiled file is
   older than its source, the line says to build again to run your latest
   changes.
3. **Plain Node.js** 22.18 or newer, which removes the types itself. On an
   older Node.js, Run says that the file needs 22.18 or newer (or tsx) instead
   of showing a syntax error. Plain Node.js cannot map an import of `./greet.js`
   to `greet.ts`, which is how a `"module": "NodeNext"` project writes its
   imports; when that import fails, a hint follows the error: add tsx to the
   project (`npm install -D tsx`), or build it so Run uses the compiled file.

A TypeScript test file's ▶ takes the same route.

**Test files.** The ▶ of a test file runs `node --test` and shows the results
in [Test Results](/docs/build-tools#test-runner). A test file is a
`*.test.*` or `*.spec.*` file, or any script under a `test/`, `tests/` or
`__tests__/` folder. The Run console shows node's own readable report, while
Test Results reads a TAP report of the same run. A test file that fails to
load, for example on a missing module, is a failed row named after the file,
with the error and its stack. A skipped or todo test shows as skipped under its
own name, with or without a reason. **Rerun Failed** and **Rerun This Test**
work on these results; Rerun Failed on a file that failed to load runs the
whole file again.

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
  holds the file. The project's own `./mill` launcher is used when it has one;
  unlike `./mvnw`, it runs without a [trust](/docs/build-tools) question.
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
Run says so when you launch it. Remote is in Beta. That covers Run on a file, a Java project's main
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
(see [Remote](/docs/remote#what-runs-on-the-host)), then the **Python
interpreter** on **Settings → LSP → Python** when that is a plain command name
rather than a path on this machine, then the host's `python3`. `PYTHONPATH` gets
the project root and its `src/` folder; the host's own `PYTHONPATH` is not
kept, because Editora does not know it. Stop ends a remote program straight
away, without the Ctrl-C a local Python program gets first.

A Node.js script runs from its package folder there, the nearest folder above
it on the host with a `package.json`, as it does locally. This holds in a
container and in WSL too. A `.nvmrc` or `.node-version` on the host picks its
Node.js from the host's nvm (see [Node.js scripts](#nodejs-scripts)).

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
folder there. Containers and WSL are part of [Remote](/docs/remote), which is in
Beta.

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

**Run: Run Context at Caret** (`run.context`; Ctrl+Shift+F10, or ⌃⇧R on
macOS, in the IntelliJ keymaps) runs whatever the caret is in: the test at the
caret (the whole test class when the caret is above its declaration), else the
class's `main` as its ▶ does, else the file. A Java class with neither says so.

There are two paths under that, picked automatically:

- With the **Java language server** set up, Editora asks it for the project's
  main classes and resolved classpath. This is the fast, project-wide path, and
  it is the only one that can debug. In a folder you have not trusted the
  server runs without the build, so it knows only the JDK's classes and the
  project's sources (see
  [Servers that load a project's build](/docs/lsp#servers-that-load-a-projects-build)).
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
A failed build aborts the launch, so a fresh clone does
not stop with `ClassNotFoundException` and an edited file never runs the
previous binary. **Settings → Build Tools → Build before Run and Debug** (on)
turns it off; a run configuration with its own
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

An entry `@path`, with no `=`, loads a **`.env` file**, as VS Code's `envFile`
does: `@.env`, or `"@config/dev env/.env"` in quotes when the path has spaces.
A relative path is relative to the configuration's working directory. The file
holds one `NAME=value` per line, read the way dotenv tools read it:

- blank lines and lines starting with `#` are skipped, and an `export` before
  the name is dropped;
- a value in single quotes is taken as written;
- a value in double quotes expands `$NAME` and `${NAME}`, and reads `\n`, `\t`,
  `\"` and `\\` as escapes;
- an unquoted value ends at a ` #` comment, is trimmed, and expands `$NAME` and
  `${NAME}`.

A name expands to a variable set earlier in the file or the field, else to the
environment the program would get anyway. The file's variables are set where
the entry stands, so a later `NAME=value` overrides one of them and can use it
as `$NAME`: `@.env DEBUG=1` keeps the file's values but turns `DEBUG` on. When
the file cannot be read, Run and Debug stop before launching, and the status
bar names the configuration and the file, such as *Run configuration "API":
cannot read the environment file /home/me/api/.env*.

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

A **Node.js script** configuration has **Script**, **Node.js options** (such
as `--max-old-space-size=4096`), **Node.js**, program arguments and
environment. Left blank, **Node.js** is picked the way Run picks it (see
[Node.js scripts](#nodejs-scripts)), and a blank working directory is the
package folder, the nearest one with a `package.json`. A **Node.js** path that
does not exist stops the run with a message that names it, and the
configuration opens so you can fix it. **Before launch** can run a command
first, such as `npm run build`.

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
with a blank one, it runs at the project root. A Java configuration's step
always runs in its project root, whatever the working directory, because it is
usually a build.

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

#### What the file holds

Export writes every configuration of the window, and the project's
[deployments](/docs/deployments#sharing-them), replacing the file. It is plain
JSON, one object per configuration:

```json
{
  "configurations": [
    {
      "name": "App",
      "type": "java",
      "mainClass": "com.example.App",
      "args": "--port 8080",
      "vmArgs": "-Xmx512m",
      "env": "LOG_LEVEL=debug",
      "beforeLaunch": "maven:compile"
    },
    {
      "name": "serve.py",
      "type": "python",
      "target": "scripts/serve.py",
      "workingDir": ""
    }
  ]
}
```

`type` is `java`, `python`, `python-module`, `shell`, `make`, `npm`, `node`,
`groovy`, `kotlin`, `scala` or `clojure`. A field left out is blank, and a
blank field means the default described above. Relative paths in `target` and
`workingDir` are relative to the project root.

Before you commit it:

- **Machine paths travel too.** A JDK (`jdkHome`), an interpreter or Node.js
  path (`interpreter`), or a deployment's key file is written as you set it,
  so a path that exists only on your computer fails on a colleague's. Leave
  these blank to use each machine's default. A Python interpreter can also be
  a path relative to the project root, such as `.venv/bin/python`.
- **Environment values are written as typed.** Keep secrets out of a
  configuration you share.
- **A file that cannot be read counts as empty.** A hand edit that breaks the
  JSON makes Import say there are no shared configurations, rather than
  naming the error. Unknown keys are ignored.

### Creating and editing them

The **Run Configurations** window (`run.editConfigs`, or **Edit
Configurations…** in the toolbar dropdown) is a master-detail editor for the
list, and
**Run: Save Run Configuration…** creates one from the palette;
**Run: Run Configuration…** runs one and **Run: Delete Run Configuration…**
removes one.

![The Run Configurations window with a hello.py entry in the list and its form: Name, Type (Python script), Script, Interpreter, Interpreter options, Program arguments, Working directory, Environment, Before launch, Console and Run on](/screenshots/docs/run-configurations.png "Add starts from the open file: here a Python script configuration, prefilled from hello.py.")

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

Editora debugs **Java**, **Python**, **JavaScript and TypeScript (Node.js)**,
**Groovy**, **Kotlin**, **Clojure**, **Scala**, [**Go**](#debugging-go),
[**Rust**](#debugging-rust), [**C and C++**](#debugging-c-and-c),
[**C#**](#debugging-c), [**Ruby**](#debugging-ruby) and
[**PHP**](#debugging-php) through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window (`M-g d`). Debugging
is **on by default**, and each language's debugger starts once its adapter is
found ([Installing adapters](#installing-adapters)); the switch is **Enable
debugging (DAP)** in **Settings → Debugging**. A project's
`.vscode/launch.json` configurations start as VS Code starts them
([launch.json configurations](#launchjson-configurations)), and any other
debugger that speaks the protocol can be added
([Debug adapters of your own](#debug-adapters-of-your-own)). Shell scripts
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
| Step over / into one machine instruction ([Disassembly](#disassembly-and-memory)) | `debug.stepOverInstruction`, `debug.stepIntoInstruction` | (palette) |
| Run to cursor | `debug.runToCursor` | `C-c C-d u` |
| Run to cursor past every breakpoint | `debug.forceRunToCursor` | `C-c C-d S-u` |
| Jump to line | `debug.jumpToLine` | `C-c C-d j` |
| Toggle breakpoint | `debug.toggleBreakpoint` | `C-c C-b` |
| Toggle temporary breakpoint | `debug.toggleTemporaryBreakpoint` | `C-c C-d 1` |
| Toggle [recording breakpoint](#recording-breakpoints) | `debug.toggleRecordingBreakpoint` | (palette) |
| Toggle method breakpoint (Java) | `debug.toggleMethodBreakpoint` | `C-c C-d S-m` |
| Toggle field watchpoint (Java) | `debug.toggleFieldWatchpoint` | `C-c C-d S-w` |
| Toggle [lambda breakpoint](#lambda-breakpoints) (Java) | `debug.toggleLambdaBreakpoint` | `C-c C-d S-l` |
| Edit breakpoint | `debug.editBreakpoint` | `C-c C-d e` |
| Choose exception breakpoints | `debug.toggleExceptionBreakpoints` | (palette) |
| Show the [exception card](#exception-card) again | `debug.showException` | (palette) |
| Debug a project main class | `debug.mainClass` | (palette) |
| Debug a saved run configuration | `debug.config` | (palette) |
| Start a [launch.json configuration](#launchjson-configurations) | `debug.startConfiguration` | (palette) |
| Open [`debug-adapters.json`](#debug-adapters-of-your-own) | `debug.editCustomAdapters` | (palette) |
| Debug via build tool | `debug.viaBuild` | (palette) |
| Attach to a running JVM or Node.js | `debug.attach` | `C-c C-d a` |
| Attach to a program on this machine ([Attach to Process](#attach-to-process)) | `debug.attachToProcess` | (palette) |
| Attach again to the last address | `debug.reattach` | `C-c C-d S-a` |
| Pin a value to the caret's line ([pinned values](#pinned-values)) | `debug.pinValue` | (palette) |
| Unpin the values on the caret's line | `debug.unpinLine` | (palette) |
| Show numbers as hexadecimal | `debug.toggleHex` | (palette) |
| Show every thread ([Threads view](#threads-view)) | `debug.threads` | (palette) |
| Go back or forward through earlier stops ([Debug History](#debug-history)) | `debug.historyBack`, `debug.historyForward` | (palette) |
| List the stops of the session | `debug.showHistory` | (palette) |
| Show the [Disassembly or Memory view](#disassembly-and-memory) | `debug.showDisassembly`, `debug.showMemory` | (palette) |
| Turn the Python, JavaScript, Go, Rust, C and C++, C#, Ruby or PHP debugger on or off | `debug.toggleAdapter` | (palette) |
| Set an adapter's path | `debug.setAdapterPath` | (palette) |

![A Java program paused at a breakpoint in Shelf.java with the Debug tool window below: the call stack, a variables tree with the items list unfolded, and the console](/screenshots/features/debugging.png "Paused at a breakpoint: call stack, variables and console in the Debug window.")

The keys are the Emacs keymap's; the other keymaps' keys are listed under
[Debugger keys](/docs/keymaps#debugger-keys). The step commands are also
buttons in the Debug window, and most debugger actions are in the **Run** menu.
Its top level has Start / Continue, Debug Main Class, Attach, Attach to
Process, Reattach, Stop, Restart and Toggle Breakpoint. Start Configuration,
stepping (by line or by instruction), evaluate, watches, pinned values,
hexadecimal values, Debug History, breakpoints, the Threads, Disassembly and
Memory views, Edit Debug Adapters, exception breakpoints and Show Exception are
in its **Debugger** submenu (**Run ▸ Debugger**).

**Jump to Line** moves the program to another line without running the code in
between. It needs a debugger that can do that: Python's debugpy can. The Java
and JavaScript debuggers cannot, and Jump to Line then says it is not supported.

**Force Run to Cursor** runs to the caret line as Run to Cursor does, but no
breakpoint or logpoint on the way stops it, in any file. Exception breakpoints
still stop the program. The breakpoints are back at the next stop, wherever
that is: the caret line, an exception, or Pause. Its keys are `C-c C-d S-u` in
the Emacs keymap and Ctrl+Alt+F9 (⌥⌘F9 on macOS) in the IntelliJ IDEA keymap;
it is also in **Run ▸ Debugger** and, while the program is paused, in the
editor's right-click menu.

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
message** (which turns it into a logpoint that logs and never suspends), an
**Enabled** toggle, **Remove once hit**, which makes it a
[temporary breakpoint](#temporary-breakpoints), and **Record and continue**,
which makes it a [recording breakpoint](#recording-breakpoints). Its title names the file and line, such as *Edit
Breakpoint — Main.java:27*, and the line's code is shown above the fields, since
the form can cover that line. The hit count makes it stop only on that hit: `5`
stops on the fifth. Python also takes forms such as `>= 3` or `% 2`; the other
debuggers take only a number, and the field's hint says which. A breakpoint
with a condition or a hit count is drawn as conditional. Breakpoints in closed
files are honored too.

![The Edit Breakpoint form for Shelf.java line 50 over the editor: the line's code above the fields, a Condition field with item.price() > 1.0 typed, an empty Log message, a Hit count field whose hint reads Every hit, or a number: 5 stops on the fifth hit, Enabled ticked, Remove once hit, and Cancel and OK buttons](/screenshots/docs/edit-breakpoint.png "Edit Breakpoint: a condition, a hit count, or a log message that turns it into a logpoint.")

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
frames when the adapter supports partial stacks. The console wraps long lines.
It is cleared at each launch and kept after the session ends, and its
right-click menu has **Clear Console**. It keeps the last 200,000 characters;
when older output was dropped, the Debug window's state line says so. **Restart** is disabled for a debugger attached to a test or build
run (its tooltip says why); a session started with *Debug → Attach* can still be
restarted.

A spinner beside the Debug window's status turns while the before-launch build,
the session start, or the program is running, and stops while the program is
paused (at a breakpoint, a step, or Pause). Pause works only on a running
program, so it is greyed out while the program is paused, starting or being
built. In Java, Pause stops every thread of the program, and Continue resumes
them all (see [Threads in a Java program](#threads-in-a-java-program)).

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
| Rust | Rust: on panic | Rust: on panic |
| C and C++ (CodeLLDB) | C++: on throw, C++: on catch | C++: on throw |
| C# | C#: user-unhandled exceptions, C#: all exceptions | C#: user-unhandled exceptions |
| Ruby | Ruby: any raised exception, Ruby: RuntimeError | none |
| PHP | PHP: exceptions, errors, warnings, notices, deprecations, everything | none |

Go has none to choose: Delve stops on a panic by itself.

A session starts with the filters its debugger turns on by default, so a
Python program stops where an uncaught exception is raised, with the
exception's type and message in the Debug window and the status bar. In
Python, `sys.exit()` with a non-zero code also counts as an uncaught exception;
Continue then ends the program. Editora keeps your choice for each language and
each project, as it does for **Break on Caught Exceptions** and **Break on
Uncaught Exceptions** (see [Managing breakpoints](#managing-breakpoints)).

### Exception card

When the program stops on an exception, a card opens under the line that threw
it, once per stop. Its title says what kind of stop it is:

- **Uncaught exception**, in red, for an exception that nothing catches;
- **Exception (caught)** for one the program catches;
- **Exception** when the debugger doesn't say which. The Java debugger says
  which.

Below the title are the exception's type and its message. When the debugger
sends them, the card also lists the exceptions that caused it, each on a
**Caused by** line, and the stack trace. The Python and JavaScript debuggers
send them; the Java debugger sends only the type and the message.

- **Copy** copies the type, the message, the causes and the stack trace.
- **Don’t Stop on Caught Exceptions** appears on a caught exception while
  **Break on Caught Exceptions** is on, and turns that off, so the program no
  longer stops on exceptions it catches.

The card never takes focus, so typing stays in the editor. Esc in the editor,
the card's ✕ or a click anywhere else closes it, and so do Continue, a step,
picking another frame in the call stack, or the session ending. It follows its
line when you scroll, and closes when the line leaves the screen.

**Debug: Show Exception** (`debug.showException`, in **Run ▸ Debugger** while
the program is paused) opens the card again. If another frame is shown, it goes
back to the frame that threw first. When the program did not stop on an
exception, the status bar says so.

### Attach to Process

**Debug: Attach to Process…** (`debug.attachToProcess`, in the **Run** menu
under Attach) lists the Java, Node.js, Python, Go, Rust, C and C++ programs
running on this machine under your user, so you can attach to one without typing its address.
It is also the last row, *A program on this machine…*, of the
[Attach list](#starting-a-java-session) a Java file shows once the project has
attached before. Type to filter the list. Programs listening for a
debugger come first:

- a **JVM** started with the JDWP agent as a server on a fixed port, such as
  `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005` (or
  `-Xrunjdwp:…`). A JVM started with `server=n` or `address=0` can't be
  attached to;
- a **Node.js** started with `--inspect`, `--inspect-brk` or `--inspect-wait`
  (port 9229 unless it names another);
- a **Python** program started under debugpy with
  `python -m debugpy --listen 5678 app.py`;
- any **Go** program, on Linux and macOS. Delve attaches to it by its process
  id, so it needs no flag; Editora tells a Go program by reading its
  executable;
- a **Rust**, **C** or **C++** program built with debug information, on
  Linux. CodeLLDB attaches to it by its process id. Editora tells a Rust
  program from a C or C++ one, listed as *Native (C, C++)*, by reading its
  executable. A program built without debug information isn't listed.

Each row names the main class, jar, script or program and the process id, and
below them the kind of program and where it listens, such as *Java · listening
on localhost:5005*, *Go · Delve attaches by process id* or *Native (C, C++) ·
CodeLLDB attaches by process id*. A program of these kinds that was started without debugging is
listed too, with what to start it with instead: *not started for debugging
(start it with --inspect)*. Picking such a row says the same in the status bar
and attaches nothing. With none of these programs running, the list says so.

![The Attach to Process list over Main.java: com.example.inventory.InventoryServer (pid 11), Java · listening on localhost:5005; server.js (pid 13), Node.js · listening on localhost:9229; worker.py (pid 15), Python · listening on localhost:5678; then com.example.inventory.ReportJob (pid 12) and watch.js (pid 14), not started for debugging, each with what to start it with](/screenshots/docs/debug-attach-to-process.png "Attach to Process: programs listening for a debugger come first, then those started without debugging.")

Picking a listening program attaches the right debugger to it and opens the
Debug window. **Stop** detaches: the program keeps running, and you can attach
again.

- **Java** attaches the way [Attach to Running Program](#starting-a-java-session)
  does, through a Java file of the program's project: the active tab if it is
  one, else the first open Java tab. With no Java file open, the status bar
  asks you to open one, since the debugger finds the program's classes through
  it. The address is added to the project's Attach list, so
  **Debug: Reattach** goes back to it.
- **Node.js** attaches through vscode-js-debug, as
  [Attach to a running program](#debugging-javascript-and-typescript) does.
- **Python** connects to debugpy inside the program, so Editora doesn't need
  a debugpy of its own. A program listening only on another network address of
  this machine, not on `localhost`, can't be attached to yet.
- **Go** attaches [Delve](#debugging-go) to the process, with the breakpoints
  of the project's Go files. On Linux, a debugger may attach to a program it
  didn't start only when `kernel.yama.ptrace_scope` is 0, which many
  distributions don't make the default; `sudo sysctl kernel.yama.ptrace_scope=0`
  sets it until the next restart. Not on Windows.
- **Rust, C and C++** attach [CodeLLDB](#debugging-rust) to the process by
  its process id. The same `ptrace_scope` setting applies. A macOS program keeps its debug information
  outside the executable, so it isn't listed there.

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
interpreter can import debugpy, else under the **Python interpreter** set on
the Python card of Settings → LSP. A program that starts child Python processes, for example
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

A debugged **Java**, **Python**, **JavaScript** or **Rust** program can read its
standard input from the Debug console, so a Python program that calls `input()` waits
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
console (program input)**, is on the **Debugged programs** card of
**Settings → Debugging** (or use
*Debug: Toggle Program Input in the Debug Console*,
`debug.toggleProgramConsole`) and takes effect at the next launch. It applies
to a launched Java or Rust program, and to a Python or JavaScript program
started on this computer; a Python or JavaScript file on a remote host, in WSL or in a
container is unaffected, and so are attached sessions.

**Run the program in a Terminal tab instead**, just below it, runs that program
in a tab of the [Terminal](/docs/terminal#run-and-debug-programs-in-a-terminal)
tool window rather than the Debug console. Use it for a program that draws on
the screen, reads single keys or asks for a password. Its output and input are
then in that tab, which is titled *Debug: <file>* and stays when the program
ends. The Debug console's first line names the tab. The program's exit still
ends the session, and Stop ends the program. It is off by default and needs the
program console switch on.

With the switch off, the debugger starts a Java program itself. A program that
prints a lot then gets its output to the console well after it writes it; the
session ends, and the exit line shows, once the last of it has arrived. Until
then the Debug window's state reads *Program ended, output still arriving*. The
console still reports the program's exit code on Linux and macOS when the
program runs on this machine. On Windows, and on a remote host, in WSL or in a container, it
says "The program ended." instead. A JavaScript program's output, including
`process.stdout.write`, still reaches the console, but the program cannot read
input.

### Debugging Java

Compact `.java` sources and extensionless Java `--source 25+` shebang files can
be debugged with breakpoints, stepping, and local variables. Editora maps
breakpoints and stack frames to the original file and removes its temporary
compiled files after the session. Java debugging needs the Java language server
and debug adapter described below. In a folder you have not
[trusted](/docs/workspace#trusted-folders), the Java language server runs
without the project's Maven or Gradle build, so trust the folder before
debugging a project's code.

JUnit tests are debugged from their gutter icon, with *Debug Test at Caret*, or
with Debug in the test file (see [Starting a Java session](#starting-a-java-session)),
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

In a JUnit test file of a Maven or Gradle project, Debug debugs the test at the
caret, or the whole class when the caret is outside a test, the way Debug Test
does. The caret on a test's annotations, such as `@Test`, counts as in that
test. A file with a `main` of its own is launched as a program instead.

In a Maven or Gradle project, Debug builds first, so the program runs the code
you see. Debug on a file builds the project with the Java language server: if
that build finds compile errors, nothing is launched, the Debug console lists
the errors as links to their lines (with paths relative to the project), the
status bar gives their number, and the Debug window reads "Not started: the
build failed". If the project could not be imported, the console says so
instead; when its errors name a Java version, choose another Default JDK in
**Settings → Build Tools**. If the build cannot run, the program starts anyway,
and the console warns you when a class file is missing or older than its
source.

*Debug Main Class* and Debug on a saved configuration compile with the build
tool when **Build before Run and Debug** is on (the default): Maven `compile`,
or `test-compile` for a class under `src/test`; Gradle `compileJava` or
`compileTestJava`. The build runs in the Debug console, through the same Maven
or Gradle the tasks tree runs, and a failed build aborts the launch. With the
setting off, they build with the Java language server, as Debug on a file does.
In a Gradle project the Java language server builds after Gradle as well, since
the program runs from the class files it writes, so the program runs the code
you edited. The Gradle daemon that this build starts stops after 10 minutes
without work. Stop during either build cancels the launch. A run configuration
with a [before-launch step](#a-step-before-the-launch) uses that step as its
build instead. A Java configuration's step runs in the project root, whatever the
configuration's working directory.

A class with a `package` line in a folder that has no `pom.xml` or Gradle build
can be debugged too, and such a class has **Debug File** in the editor's
right-click menu. In a project window for such a folder, *Debug Main Class* and
Debug on a saved Java configuration work, and the program runs in the project
root.

A loose `.java` file, or a project folder without Maven or Gradle, is built by
the Java language server before every Debug, so the program always runs the
code you saved. When that build reports errors or cannot run (a folder with
several packages can be more than the language server works out without a
build tool), Editora compiles the file with `javac` from the source root its
`package` line names. `javac` compiles only the code the program uses, so only
errors there stop the launch, listed in the Debug console as in a Maven
project; a broken file the program never uses, or a JUnit test without JUnit,
does not. Code that does not compile never runs. A configuration's
before-launch step runs first, and the build after it. The program runs on the
Default JDK, else on the `java` on your `PATH` (see
[Which runtime a program runs under](#which-runtime-a-program-runs-under)).
When the Default JDK's folder no longer exists, the Debug console says so and
the `java` on your `PATH` runs the program. A compact source file is always
compiled by `javac`.

While the language server builds before a launch, the Debug window reads
*Building…* from the moment you press Debug, and Stop cancels the launch.

**Start / Continue** (and the Debug window's green ▶), **Debug File** in the
editor's right-click menu and Debug Test make one check before they launch.
Start / Continue on a paused session continues it. For another file, a session
that is paused, running or still starting (building, compiling, waiting for a
test to start, or waiting for you to choose a main class) is replaced only
after you agree, for example to *Main.java is paused in the debugger. Stop it
and debug App.java instead?* For the same file, the status bar says that it is
already being debugged and whether it is running, paused or still starting. Esc
on the main-class chooser cancels the launch.

**Debug: Attach to Running Program…** (`debug.attach`) on a Java file attaches
to a JVM listening for a debugger. The first time in a project, it asks for the
`host:port`, offering `localhost:5005`. After that it lists the addresses this
project attached to, at most eight and the most recent first, then **Another
address…**, which asks for one, offering the most recent, and last
**A program on this machine…**, which opens
[Attach to Process](#attach-to-process). The list is kept
with the project, so it is there after a restart. When nothing listens at the
address, the Debug console shows the `-agentlib:jdwp` option that starts a JVM
listening on it.

**Debug: Reattach** (`debug.reattach`, `C-c C-d S-a`) attaches again to the
address this project attached to last, without asking. Before the first attach,
the status bar says to use Attach to Running Program. Reattach is in the
**Run** menu, under Attach.

When Java debugging can't start, the status bar names the missing
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

**Debug: Smart Step Into…** (`C-c C-d s`, also in Run ▸ Debugger) lists the
calls on the current line in the order they run, for example `twice(1)`,
`twice(2)` and `add(twice(1), twice(2))`. Pick one to step into it. On a line
with a single call it steps straight in. The list closes when the program runs
on.

**Debug: Restart Frame** (`C-c C-d f`, the call stack's right-click menu, or
Run ▸ Debugger) runs the method of the selected frame again from its first line
and pauses there. It is greyed out on the bottom frame.

If the connection to the debugger is lost, Editora ends the program at once and
says so in the Debug console and the status bar.

### Threads in a Java program

**Pause** stops the whole Java program: every thread is suspended, not only the
one on screen. **Continue** after a Pause resumes every thread. A step between
the two runs only the thread you step, and the next Continue still resumes them
all.

By default, a breakpoint, an exception or a step stops only the thread that
hit it, and the program's other threads keep running. To stop them all, turn on
**Suspend all threads when one stops** on the Java card of **Settings →
Debugging** (off by default). Continue then resumes every thread. The setting
takes effect at the next debug session.

To pause or resume one thread and leave the others as they are, use the
[Threads view](#threads-view).

### Editing during a session

The running program keeps the code it was started with. When you edit a file
during a session, Editora matches breakpoints and the stopped line to the code
the program runs, so they stay on the right lines. At a stop in an edited file,
the status bar says that the program still runs the code it started with. A
breakpoint on a line you change in place, re-indent or wrap in a block (such as
an `if`) keeps working and stopping there; when you split a line in two, the
breakpoint stays on its first half. A breakpoint on a line you added during
the session is kept, and its hover says that it takes effect in the next
session. To run the new code, restart the session, or let
[hot code replace](#hot-code-replace) load a change to a method body.

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
- for a file without Maven or Gradle, the program runs an older Java than the
  code was built for ("the program runs Java N, but the code was built for
  Java M"): choose the program's JDK as the Default JDK in **Settings → Build
  Tools**, or restart the session;
- the Java language server could not build the project;
- the session runs classes compiled with `javac`, because the Java language
  server cannot build the folder: restart the session to run your edits;
- the program already runs this code.

A change to a method that is running when you save is applied too. That method
goes on with its old code until it returns, and its frame in the call stack
reads "old code (replaced while it ran)". To run it again with the new code,
select its frame and use **Debug: Restart Frame**. The first method of a
thread, such as `main`, has no caller to run it again, so it keeps its old code
until the program restarts, and the console says so.

Choose when this happens with **Apply code changes to a running Java program**
on the Java card of **Settings → Debugging**:

| Choice | What a save does |
| --- | --- |
| **On save** (default) | loads the change when you save a file yourself; autosave does not |
| **Only with Debug: Apply Code Changes** | shows a hint in the status bar; run **Debug: Apply Code Changes** (`C-c C-d h`, Run ▸ Debugger) to load the saved changes |
| **Never** | nothing; Debug: Apply Code Changes says that it is off |

Debug: Apply Code Changes loads only what is saved. When the active Java tab
has unsaved edits, the console says so.

Hot code replace also runs in an attached session and in Debug Test and Debug
via Build Tool sessions. There the program runs the classes your build tool
compiled, so a change can be refused that a session Editora started would
accept; the console then tells you to restart.

### Debugging JavaScript and TypeScript

**Debug** on a JavaScript or TypeScript file (`.js`, `.mjs`, `.cjs`, `.ts`,
`.mts`, `.cts`, or a `node` shebang script) runs it under vscode-js-debug,
started exactly as Run starts it: the same Node.js, flags, environment and
folder, and for TypeScript the project's tsx or the compiled file (see
[Node.js scripts](#nodejs-scripts)). That Node.js is chosen as described in
[Which Node.js runs](#nodejs-scripts): a configuration's own, then a project's
`.nvmrc` or `.node-version`, then the **Node.js** card under **Settings → Build
Tools**, then `node` on your `PATH`. The Debug console's first line is the command that runs. When that
is a compiled file older than its source, a red line says so: build the project
first to debug your latest changes. The **JavaScript** card under **Settings →
Debugging** has a link to the Node.js card, and its **Detected** line names the
vscode-js-debug version and the Node.js it runs on, such as *found (1.117.0 ·
Node.js v24.18.1)*.

- **TypeScript** files without tsx or a compiled file are debugged on plain
  Node.js 22.18 or newer. With an older Node.js, Debug says so. In a project
  that compiles its TypeScript, a breakpoint in a `.ts` source stops there when
  the compiled JavaScript runs, through its source maps.
- **JSX and TSX** files can't be run by Node.js. Debug the compiled JavaScript
  instead; a breakpoint in the `.jsx` or `.tsx` file still stops there through
  its source map.
- **Output and input.** All output reaches the Debug console in the order the
  program wrote it, with its colours, including `process.stdout.write` and
  `readline` prompts. Output to stderr is not shown in red, because it shares
  stdout's stream to keep that order. The program can read lines you type there
  (see
  [Program input in the Debug console](#program-input-in-the-debug-console)).
- Node.js's own modules are skipped, so Step Into doesn't enter them. In the
  call stack, Node.js's own frames fold into one row, such as *… 7 frames of
  runtime or library code (click to show)*; click it to see them.
- A breakpoint condition that isn't valid JavaScript marks the breakpoint as
  rejected, and the console says why, once.
- Child processes and worker threads show all their threads. The status line
  names the file a thread stopped in, such as *Suspended in worker.js —
  parent.js*.
- **Debug Test at Caret** (`test.debugAtCaret`) on a `node:test` test runs that
  test under `node --test` and stops at its breakpoints.
- **Node.js script** and **npm script** [run configurations](#run-configurations)
  can be debugged, started the way Run starts them.

**Attach to a running program.** Start Node.js with `--inspect` (for example
`node --inspect server.js`), then run **Debug: Attach to Running Program…**
(`debug.attach`) on a JavaScript or TypeScript file. It asks for the
inspector's `host:port`, such as `localhost:9229`. Once attached, the console
names the address, such as *Attached to 127.0.0.1:9229.* A program started with `--inspect-brk` waits
before its first line; attaching does not start it, so press Continue. The
list of earlier addresses is for a JVM only: on a JavaScript or TypeScript
file, Attach and **Debug: Reattach** both ask for the inspector's address.
[Attach to Process](#attach-to-process) lists the Node.js programs started with
`--inspect` instead, so you don't need the address.

vscode-js-debug itself needs Node.js 18 or newer to run. An older Node.js
chosen for a project still runs the program, while vscode-js-debug runs on the
newest Node.js 18 or newer that nvm has, else `node` on your `PATH`. When
vscode-js-debug can't start, or no Node.js new enough is found, Debug says so
instead of stopping without a word. **Install…** on the JavaScript card
installs only vscode-js-debug v1.117.0, a version tested with Editora.

### Managing breakpoints

A breakpoint's glyph shows its kind by shape as well as colour: a dot for a
plain breakpoint, a diamond for a logpoint, two "=" bars for a breakpoint with
a condition or a hit count, a "1" for a
[temporary breakpoint](#temporary-breakpoints), a λ for a
[lambda breakpoint](#lambda-breakpoints), purple with a ring for a
[recording breakpoint](#recording-breakpoints), and grey for a disabled one. Java
[method breakpoints and field watchpoints](#method-breakpoints-and-field-watchpoints)
have marks of their own on their member's line.
Its hover names the condition, log message and hit count, and says when it is
disabled, removed once hit, or recorded in Debug History. A hit
count stops on that hit in Java too: `3` stops the third time the line runs.

Right-click a glyph for **Edit Breakpoint…**, **Enable Breakpoint** or
**Disable Breakpoint**, **Remove Breakpoint** and **Breakpoints…**; on a Java
line that starts a lambda, the menu also has **Stop on the Line** and
**Stop in Lambda n** (see [Lambda breakpoints](#lambda-breakpoints)). The
editor's right-click menu has Toggle Breakpoint, Edit Breakpoint, Toggle
Temporary Breakpoint and Toggle Recording Breakpoint, Toggle Lambda Breakpoint on a Java line that starts a
lambda, and while the program is paused also Run to Cursor,
[Force Run to Cursor](#debugging), Show Value at Caret and Add to Watches.

A breakpoint can't be set on a blank line or on a line that holds only a
comment; the status bar says so. Removing one is always possible. A line with
only a closing `}` takes a breakpoint.

**Debug: Breakpoints…** (`C-c C-d l`; Ctrl+Shift+F8 in the IntelliJ keymap,
⇧⌘F8 on macOS) lists every breakpoint. Each row has the gutter's glyph for its kind, its file,
line and code, and below them what it does (its condition or log message) and
its folder, relative to the project (none for a file at the project root), or with
your home folder shortened to `~`. With no breakpoints, the list says how to set
one. Type to filter it; Enter jumps to the selected breakpoint, Alt+E enables
or disables it, Alt+D removes it, and Alt+M mutes them all. Java
[method breakpoints and field watchpoints](#method-breakpoints-and-field-watchpoints)
are listed after the line breakpoints.

**Debug: Mute Breakpoints** (`C-c C-d m`) turns every breakpoint off without
removing it, until you run it again. It also stops exception breakpoints from
stopping the program; your caught and uncaught choices come back when you turn
it off. While breakpoints are muted, every glyph is faded, a glyph's hover says
that none of them stops the program, Mute Breakpoints is checked in
Run ▸ Debugger, and the Debug window's state line says "breakpoints muted".
Editora keeps the choice for each project, so a muted project is still muted
when you open it again. **Debug: Enable / Disable Breakpoint** (`C-c C-d t`)
switches the breakpoint on the caret line. These commands are in
**Run ▸ Debugger**.

**Debug: Break on Caught Exceptions** stops wherever an exception is thrown,
even one the program catches. **Debug: Break on Uncaught Exceptions** stops on
an exception that nothing catches. Both are in Run ▸ Debugger, checked when on,
and in the menu of the ⚠ button in the Debug window's toolbar. While either is
on, the ⚠ is drawn in the accent colour, and its tooltip says which are on.
They act on the running session's language, or else on the active file's, and
Editora keeps your choice for each project. To stop only on some Java exception
classes, list them, separated by commas, in **Exception breakpoints: only these
classes** on the Java card of **Settings → Debugging** (for example
java.lang.IllegalStateException). Left blank, both stop on every exception.

At an exception stop, the line that threw is drawn red, the
[exception card](#exception-card) opens under it, and the exception's type and
message are the top row of Variables.

### Temporary breakpoints

A temporary breakpoint is removed once the program stops on it, so it stops
the program only once. **Debug: Toggle Temporary Breakpoint**
(`C-c C-d 1`; Ctrl+Alt+Shift+F8 in the IntelliJ keymap, ⌥⇧⌘F8 on macOS) works
on the caret line:

- on a line without a breakpoint, it adds a temporary one;
- on a line with an ordinary breakpoint, it makes that breakpoint temporary,
  keeping its condition and hit count;
- on a line with a temporary breakpoint, it removes it.

The command is also in **Run ▸ Debugger** and the editor's right-click menu.
In **Edit Breakpoint**, **Remove once hit** turns any breakpoint into a
temporary one, or back.

A temporary breakpoint's dot carries a "1" (one with a condition or a hit count
shows the "=" bars instead), and its hover and its row in **Debug: Breakpoints…**
say *Removed once hit*. When the program stops on it, Editora removes it, also
from the running session, and the status bar says *Temporary breakpoint on line
N hit and removed.* A temporary breakpoint with a condition or a hit count is
removed only when it actually stops the program. A logpoint never stops the
program, so it is never removed.

### Recording breakpoints

A recording breakpoint keeps a stop instead of staying stopped. Each time it
is hit, [Debug History](#debug-history) keeps that stop, with its call stack,
the stopped frame's local values, your watches and the line's inline values,
and the program goes on at once. The editor does not jump to the line, and the
Debug console notes each one as *Recorded stop n:* followed by where it was.
Afterwards, Back, Forward and Show Value History read the recorded stops as
any other, so you can follow how a value changed over many passes of a loop
without stepping through each one. This is what Visual Studio's tracepoints and
IntelliJ's non-suspending breakpoints do, with the values kept instead of
logged.

**Debug: Toggle Recording Breakpoint** (`debug.toggleRecordingBreakpoint`, in
**Run ▸ Debugger** and the editor's right-click menu) works on the caret line:

- on a line without a breakpoint, it adds a recording one;
- on a line with an ordinary breakpoint, it makes that breakpoint record,
  keeping its condition and hit count;
- on a line with a recording breakpoint, it removes it.

In **Edit Breakpoint**, **Record and continue** turns any breakpoint into a
recording one, or back. A recording breakpoint is drawn purple with a ring,
and its hover says *Recorded in Debug History; the program goes on*. A
condition or a hit count still decides when it records. A logpoint never stops
the program, so it records nothing.

A session records at most 500 stops. After that, a recording breakpoint stops
the program as an ordinary one does, and the status bar says why. It also
stops as usual when Debug History is turned off in **Settings → Debugging →
History**, since there is nowhere to keep the stop.

### Lambda breakpoints

In Java, a line breakpoint stops once, when its line runs, and never inside a
lambda on that line. On `items.forEach(x -> System.out.println(x));` it
stops before `forEach` is called, not each time the lambda's body runs, because the body
runs later, as code of its own. A lambda breakpoint stops there.

**Debug: Toggle Lambda Breakpoint** (`debug.toggleLambdaBreakpoint`) sets a
breakpoint that stops inside the lambda the caret is in, or the first one on
the caret's line, each time its body runs, with the lambda's parameters in
Variables. Its keys are Shift+F9 (⇧F9 on macOS) in the VS Code, CUA and Sublime
Text keymaps and `C-c C-d S-l` in the Emacs keymap. It is in
**Run ▸ Debugger**, and in the editor's right-click menu on a line that starts
a lambda.

- On a line without a breakpoint, it adds one that stops only in that lambda.
- On a line with a breakpoint, it adds that lambda to the places it stops.
- On a lambda the breakpoint already stops in, it takes the lambda away, and
  removes the breakpoint when it stops nowhere else.

The status bar says which, such as *Breakpoint in lambda 1 on line 12.* On a
line that starts no lambda, it says so, and outside Java it says lambda
breakpoints are for Java files.

Right-click a breakpoint's dot on a line with lambdas to choose where it
stops, in any combination: **Stop on the Line**, and **Stop in Lambda 1**,
**Stop in Lambda 2**… for each lambda the line starts, numbered from the left,
each followed by the start of its lambda.
At least one stays on: with no lambda checked, the breakpoint stops on the line
again.

![Shelf.java with a breakpoint on line 56, .filter(item -> item.price() < 1.0), its dot carrying a λ, and the dot's right-click menu: Edit Breakpoint…, Disable Breakpoint, Remove Breakpoint, Stop on the Line unchecked, Stop in Lambda 1: item -> item.price() < 1.0) checked, and Breakpoints…](/screenshots/docs/debug-lambda-breakpoint.png "A lambda breakpoint: the λ on its dot, and the menu that chooses where it stops.")

Such a breakpoint carries a λ on its dot, and its hover says *Also stops in
lambda 1* or *Stops only in lambda 1*. It keeps its condition, hit count and
log message for every place it stops, and is saved with the project's
breakpoints. Lambdas are kept by their number on the line, so editing earlier
in the line doesn't lose them; when the line no longer has that lambda, the
breakpoint stops on the line instead. Only the Java debugger has lambda
breakpoints; the other JVM languages and the other debuggers use line
breakpoints.

### Method breakpoints and field watchpoints

In Java, a breakpoint can be on a method or a field instead of a line.

**Debug: Toggle Method Breakpoint** (`debug.toggleMethodBreakpoint`,
`C-c C-d S-m`) stops the program when the method the caret is in is entered,
before its body runs. It covers every overload of that name in that class, but
not the method in a subclass. **Debug: Toggle Field Watchpoint**
(`debug.toggleFieldWatchpoint`, `C-c C-d S-w`) stops the program when the
field declared on the caret line is assigned, in any instance of the class.
Run either again on the same method or field to remove it; the status bar says
what was added or removed. Both are in **Run ▸ Debugger**. The IntelliJ IDEA
keymap has no keys for them.

Away from a method or field, they ask for one by name: the class, `#`, and the
member, such as `com.example.Order#total` or `com.example.Order#count`. The
prompt starts with the caret's class filled in. A nested class is written with
`$`, as in `com.example.Order$Line#price`. `com.example.Order::total` and
`com.example.Order.total()` are read the same way.

While the program is paused, **Break When Value Changes** in the Variables
tree's right-click menu sets a field watchpoint on the field a row shows, such
as a field of `this` or of an object you unfolded. A local variable can't be
watched, and the status bar says so.

Method breakpoints and field watchpoints are saved per project with the line
breakpoints, and every Java session gets them. **Debug: Breakpoints…** lists
them after the line breakpoints, as *Method breakpoint* or *Field watchpoint,
stops on assignment*. There Alt+E enables or disables one, Alt+D removes it,
and Enter opens the method or field, found through the Java language server.
**Mute Breakpoints** mutes them with the others, and
[Force Run to Cursor](#debugging) runs past them. When one stops the program,
the Debug window's state line says *Suspended on entering a method* or
*Suspended on a field access*.

Each one is marked in the gutter on its member's declaration line, in an open
Java file: a dot with an arrow into it for a method breakpoint, on the line of
every overload it stops in, and a dot with an eye for a field watchpoint. A
mark is gold when the breakpoint has a condition or a hit count, grey when it
is disabled, and faded while breakpoints are muted. Hover it to see what it is,
such as the method's name and *Method breakpoint*; click it to remove it.
Right-click it for **Disable Breakpoint** or **Enable Breakpoint**,
**Remove Method Breakpoint** or **Remove Field Watchpoint**, and
**Breakpoints…**. The mark follows its member as you edit. When a line has a
line breakpoint too, the gutter shows the line breakpoint, and a click on an
unmarked line still sets a line breakpoint.

![Shelf.java with a field watchpoint's mark, a dot with an eye, beside line 13, private final List<Item> items;, and a method breakpoint's mark, a dot with an arrow into it, beside line 39, Receipt checkout(int limit) {](/screenshots/docs/debug-member-marks.png "A field watchpoint marked on its field and a method breakpoint on its method.")

The marks are placed from the Java language server's outline of the file, so
without it there are none; the breakpoints still stop the program. Only the
Java debugger has method breakpoints and field watchpoints.

### Inspecting values

The line the program is stopped on is amber, with an arrow in the gutter, and
it is scrolled into view below any sticky header. Inline values appear only on
the lines up to the stopped line. Hovering a name shows its value once the
mouse rests; names in comments and strings are skipped. **Debug: Show Value at
Caret** (`C-c C-d v`) does the same from the keyboard.

A plain value, such as a number, shows on one line. An object, array or
collection shows as a tree that opens with its first level unfolded; unfold a
row to see its fields or elements. A Java `String` shows its whole text,
wrapped, under a line that gives its length, with a **Copy Value** button. A
big array comes a page at a time: its last row is "Show N more…". Right-click a
row for **Copy Value**, **Add to Watches** and **Pin to Line**, which
[pins](#pinned-values) its expression to the line the popup opened for; a
one-line value's menu has Copy Value and Pin to Line. The popup closes with Esc or when the mouse leaves it. When you open
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

After a step or any other stop, a value in Variables or Watches that changed
since the previous stop is **highlighted**: its row is tinted and the value is
bold, keeping its colours. Each frame is compared with what that same frame
showed at the previous stop, so after you step out of a call, the caller's
variables are compared with what they were before the call. A variable that
was not there before, such as one just declared, is not highlighted, and a new
session starts with nothing to compare. Choosing another frame and coming back,
or setting a value, keeps the highlights of the current stop.

In the Variables tree, the right-click menu has **Copy Value** (a string is
copied without its quotes and escapes), **Copy Name**, **Add to Watches**,
which adds the row's whole expression, such as `order.items`, **Set Value…**,
[Go to Index…](#long-lists-and-arrays) on a long list or array, in Java
[Break When Value Changes](#method-breakpoints-and-field-watchpoints) on a
field, and **Show Numbers as Hexadecimal**. Ctrl+C (⌘C on
macOS) copies the selected value, and Delete removes the selected watch. In
the evaluate field, Up and Down bring back the expressions you typed before.
An expression that throws says what it threw, such as *The expression threw
NullPointerException in length()*. After an evaluation, Variables, watches and
inline values are fetched again, so an assignment such as `count = 5` in the
console shows at once.

In Java, **Set Value…** sets a number, boolean, character or String directly,
and an `Integer`, a record or an enum variable by assigning what you type as an
expression, such as `Color.RED`.

In Java, an object whose class has its own `toString()`, such as a record, an
enum or a date, shows that text after its type, for example
`Main$Point@17 "Point[x=3, y=4]"`, and still unfolds to its fields. A boxed
number, such as an `Integer`, shows just its value: `42`. A lambda reads
`lambda@19`. Variables, watches, inline values and the hover popup all show
values this way. A Variables row shows at most 200 characters of a value; Show
Value and Copy Value give the whole text.

Each `toString()` is your program's own code, run once per object at each stop,
so a stop gives them 1.5 seconds in all. A row whose `toString()` has not
answered by then reads *(toString() not loaded yet: click to load)*, and loads
when you click it, press Enter on it or unfold it. After Pause, objects read
`Type@id` with *(toString() not available after Pause)*. While a `toString()`
has not returned, the debugger takes no other request, and the status bar says
so: wait, or press Stop. **Show toString() values** on the Java card of
**Settings → Debugging** (on by default) turns these calls off: objects then
read `Type@id`, from the next debug session.

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
stopped thread first, marked "(paused)". The [Threads view](#threads-view)
shows every thread at once. After the program ends, the state line
keeps "Exited with code N".

**Copy Call Stack**, in the call stack's right-click menu, copies the shown
thread's whole stack as text, ready to paste into a bug report or a chat.
Ctrl+C (⌘C on macOS) in the call stack does the same. Frames folded into one
row are copied too. The text reads like a Java stack trace: the thread's name,
then one line per frame, top frame first:

```text
Thread "main"
    at Main.total(Main.java:12)
    at Main.main(Main.java:5)
```

A frame without a source file is copied with its name only.

### Pinned values

A pinned value is an expression kept on a line and shown at the end of that
line at every stop, as Visual Studio's pinned DataTips and IntelliJ's inline
watches are, so you can follow it next to the code instead of in Watches.

**Debug: Pin Value to Line** (`debug.pinValue`, in **Run ▸ Debugger**) pins the
selection, or the expression at the caret, to the caret's line. With neither,
it asks for an expression. Running it again on the same expression unpins it.
**Pin to Line** in the [value popup](#inspecting-values)'s right-click menu,
shown as *Pin `total` to Line 12*, pins the popup's expression to the line it
opened for. A line holds at most four pinned values. **Debug: Unpin Values on
Line** (`debug.unpinLine`) removes every value pinned to the caret's line. The
status bar says what was pinned or unpinned.

At each stop, and when you pick another frame, every pinned value in the open
files is evaluated in the selected frame and painted after the values the
editor shows by itself, in the info colour:

```
◆ total = 3  ·  items.size() = 2
```

A value is cut to 60 characters. One that can't be evaluated there, such as a
variable not in scope, shows *unavailable:* and the debugger's message. The
values are cleared while the program runs and when the session ends.

![Shelf.java paused on line 52 in checkout, with total, subtotal and item.name() pinned to line 54, the loop's closing brace, which shows ◆ total = 4.560000 · subtotal = 8.250000 · item.name() = "Notebook", and the Debug window below with the call stack and Variables](/screenshots/docs/debug-pinned-values.png "Three values pinned to one line, evaluated at the stop.")

Pins are kept when no session runs, though they show only while the program is
paused. They follow their line as you edit above it, are saved with the
project's breakpoints, and come back on their line when the file opens again.

### Debug History

The Debug window keeps each stop of a session as it was shown: the call stack,
the values you looked at in Variables, your watches, the inline values and the
pinned values. After the program has run on, you can go back to an earlier
stop and read them again. Editora keeps the stops itself and asks the debugger
for nothing, so this works with every language's debugger.

- **Back** and **Forward**, the ‹ and › buttons in the Debug window's toolbar
  (or **Debug: Back** and **Debug: Forward**), go to the stop before or after
  the one shown.
- **Debug: Show History…** (`debug.showHistory`) lists the stops, each with
  its number, time, why it stopped and where. Pick one to go back to it.

An earlier stop is shown read-only. A banner above the Debug window says which
stop it is, when and where it stopped, and that the values are as they were
then; its **Back to the live stop** button returns to the program as it is now.
The stop's call stack and values fill the Debug window, values that changed
since the stop before are [highlighted](#inspecting-values) as usual, and the
editor marks the stop's line grey, with its inline and pinned values. Hovering
a name evaluates nothing there. Forward from the last stop, a step, Continue or
a new stop also return to the live stop.

Only values that were fetched while the stop was live are kept. A row nobody
unfolded at that stop reads *(not opened at that stop)*, since its children
were never asked for and the program has moved on.

**Show Value History**, in the right-click menu of a Variables or Watches row,
lists that row's value at every kept stop of the same frame and marks the ones
that changed. Pick one to go back to that stop. At an earlier stop, the menu
also has Copy Value and Copy Name, since nothing there can be changed.

The stops are kept in memory only, never on disk. A session keeps 200 by
default and forgets the oldest past that; **Stops kept** under **Settings →
Debugging → History** sets it to Off, 50, 200, 1,000 or 5,000. A new session
starts an empty history, but after the program ends you can still read the
last session's stops until you start the next one.
[Recording breakpoints](#recording-breakpoints) add stops to the history
without stopping the program.

### Threads view

The call stack shows one thread at a time, the one picked in the thread
dropdown above it. The **Threads** switch beside that dropdown replaces the
frames and the dropdown with every thread of the program. **Debug: Threads**
(`debug.threads`, also in **Run ▸ Debugger**) opens the Debug window on it.
Turn the switch off to see the frames again.

Each row names a thread and its state: **Running**, or suspended and why:

- **Suspended at a breakpoint**
- **Suspended after a step**
- **Suspended on an exception**
- **Paused**
- **Suspended at its start**
- **Suspended on entering a method**
- **Suspended on a field access**

The thread on screen, whose frames and variables the Debug window shows, is in
bold and marked ▶. The JVM's own housekeeping threads are left out, unless one
of them is suspended.

Right-click a thread to act on it alone:

- **Pause Thread**, on a running thread, suspends that thread alone; the others
  keep running. Its stop is shown, as any stop is.
- **Resume Thread**, on a suspended thread, resumes that thread alone; the
  others stay suspended. If it was the thread on screen, the next suspended
  thread is shown.
- **Show Frames**, on a suspended thread, turns the switch off and shows that
  thread's call stack and variables. Enter or a double-click on the row does the
  same.

The list follows each stop and Continue, and threads that start or end while
the program runs update it.

With Python (debugpy), Pause Thread and Resume Thread pause or resume the whole
program, not one thread.

### Filtering Variables

The **Filter** field beside the Variables heading narrows each scope to the
variables whose name contains what you type, ignoring case: `id` keeps `id`,
`userId` and `ID_PREFIX`. Rows that arrive later, such as the next page of a
scope, are filtered as they come. Watches are never filtered. In the field, Esc
clears it and Down moves into the Variables tree.

### Long lists and arrays

A list or array opens 100 elements at a time in Variables and Watches; the last
row reads *Show N more… (M remaining)*. Once a list or array with more than
100 elements is open, its right-click menu has **Go to Index…**, which asks for
an index from 0 to the last one and shows the elements from there, still 100 at
a time. The index can be written `250`, `[250]`, `1_000` or `1,000`. Go to
Index 0 goes back to the start. While you look at a part of a list this way,
its other children, such as a Java list's fields, are left out.

### Hexadecimal values

**Debug: Show Numbers as Hexadecimal** (`debug.toggleHex`), also in
**Run ▸ Debugger** and as a check item in the Variables tree's right-click
menu, shows
integer values in hexadecimal in Variables, watches, the hover popup and inline
values, so `255` reads `0xff`. Run it again to go back to decimal. The values
are fetched again at once, and the switch holds for later sessions until you
turn it off or quit Editora. Switching does not mark every number as
[changed](#inspecting-values).

The debugger formats the numbers: the Java and Python debuggers do,
JavaScript's does not, so a JavaScript program's numbers stay decimal.

### Disassembly and Memory

For C, C++, Rust and Go, the Debug window's third area shows the machine code
and the memory of the program as well as its console, as VS Code, Visual
Studio and IntelliJ do. Three toggles in the area's header switch between
**Console**, **Disassembly** and **Memory**. **Debug: Show Disassembly**
(`debug.showDisassembly`) and **Debug: Show Memory** (`debug.showMemory`), also
in **Run ▸ Debugger**, open the Debug window on them. Both views need a
debugger that offers them: CodeLLDB, which debugs Rust, C and C++, and Delve,
which debugs Go, do. With another debugger, the view says it can't.

**Disassembly** shows the machine instructions around the selected frame,
about 120 of them, with their addresses. The instruction the frame is at is
marked and selected, and each run of instructions is labelled with the source
line it came from. Double-click an instruction to open its source line. The
list is filled again at each stop and when you pick another frame in the call
stack.

While the Disassembly list has the keyboard, **Step Over** and **Step Into**
step one instruction at a time instead of one line, as in VS Code's
Disassembly view. **Debug: Step Over Instruction**
(`debug.stepOverInstruction`) and **Debug: Step Into Instruction**
(`debug.stepIntoInstruction`), in **Run ▸ Debugger**, do it from anywhere; Step
Into Instruction follows a call into the called function. With a debugger that
steps only by line, they say so in the status bar.

**Memory** shows the program's memory as rows of 16 bytes: the address, the
bytes in hexadecimal, and the same bytes as text. It reads 256 bytes at a time;
the ◀ and ▶ buttons show the 256 bytes before or after. Type an address
(`0x…`) or an expression, such as a variable's name, in its field and press
Enter: an expression shows the memory where its value is. **View Memory**,
in a variable's right-click menu in Variables, opens the view at that variable.
Memory that can't be read, or an expression with no address, is said in the
view.

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
  thrown. This is on by default for Groovy; **Break on Caught Exceptions** also
  stops on exceptions your own code catches. Exceptions Groovy throws and catches inside
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
  thrown, on by default; **Break on Caught Exceptions** also stops on
  exceptions your code catches.

For a Gradle project, open a Kotlin file and use
[Debug via Build Tool](#debug-via-the-build-tool): Gradle starts the app under
a suspended JVM and Editora's Kotlin debugger attaches to it, so breakpoints in
`.kt` files bind. **Debug: Attach to Running Program…** (`debug.attach`) from a Kotlin file attaches the same
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

### Debugging Go

Go is debugged with [Delve](https://github.com/go-delve/delve), the debugger
VS Code's Go extension uses. Debug a Go file with **Debug: Start / Continue**
(`debug.start`, F5 in the VS Code, CUA and Sublime Text keymaps), or with
**Debug File** in the editor's right-click menu:

- A file of a module (a `go.mod` in its folder or above) debugs its
  **package**, as `go run .` runs it.
- A file outside any module is debugged alone, as `go run file.go` runs it.
- In a `_test.go` file, Debug and **Debug Test at Caret**
  (`test.debugAtCaret`) run the test function the caret is in on its own
  (a `Benchmark…` function runs alone as a benchmark), and every test of the
  package when the caret is outside a test function.

**Restart** runs the same program or test again, wherever the caret is by
then. The arguments set by **File: Run with Arguments…** (`file.runWithArgs`)
are passed to the program, or to the test binary.

Delve builds the program itself, with optimizations off, using the `go` that the
[Go build tool](/docs/build-tools) runs. It builds from the files on disk, so
Debug and Restart save the package's edited Go files first. Its build goes to
the temporary folder, so no `__debug_bin` file is left in your package. The
program's output reaches the Debug console.

- **Breakpoints** take conditions, hit counts and log messages. A line that
  holds only a comment takes none.
- **Stepping**, **Variables**, **Watches**, hover values and inline values
  work. A struct, slice or map unfolds to its fields or items.
- **Goroutines are threads.** The call stack's thread list and the
  [Threads view](#threads-view) name each one, such as
  `[Go 6] main.restock.func1`. Delve lists at most 1024 goroutines.
- **A panic** stops the program where it happened. Delve does that by itself,
  so Go has no [exception breakpoints](#exception-breakpoints) to choose.

![restock.go paused on line 16, restocked++, inside a goroutine that restock starts, with inline values on the lines above, and the Debug window below: the Threads view listing the program's goroutines, such as [Go 1] sync.runtime_SemacquireWaitGroup, runtime.gopark ones and the suspended main.restock.func1, Variables with w, mu, restocked and wg, and the program's output in the console](/screenshots/docs/debug-go-goroutines.png "A Go program stopped in a goroutine: each goroutine is a thread in the Threads view.")

[Attach to Process](#attach-to-process) attaches Delve to a Go program that is
already running, on Linux and macOS. Stop leaves that program running.

Delve isn't bundled. Editora finds `dlv` as set on the **Go (Delve)** card of
**Settings → Debugging**, else on the PATH, else where `go install` puts it
(`$GOBIN`, `$GOPATH/bin` or `~/go/bin`), so a Delve installed while Editora runs
is found too. The card's **Install…** runs
`go install github.com/go-delve/delve/cmd/dlv@latest`, which needs Go. Without
Delve, Debug says *Delve (dlv) was not found* and how to get it. The card's
switch, **Enable Go debugging (Delve)**, is on by default, and the
[Doctor](/docs/troubleshooting#doctor) checks for Delve.

### Debugging Rust

Rust is debugged with [CodeLLDB](https://github.com/vadimcn/codelldb), LLDB
with Rust's formatters, as VS Code debugs it with rust-analyzer. A Rust file is
debugged from its Cargo package, the nearest `Cargo.toml` above it; outside a
package, Debug says so. **Debug: Start / Continue** (`debug.start`, F5), or
**Debug File** in the editor's right-click menu, debugs, depending on where the
caret is:

- in a `#[test]` function (or `#[tokio::test]` and the like): that test alone,
  in its test binary. **Debug Test at Caret** (`test.debugAtCaret`) does the
  same.
- in an integration test file (`tests/*.rs`), outside a test: all its tests.
- in a file of `examples/`: that example.
- anywhere else: the package's program. In a library with no program, the
  file's tests.

Cargo builds first, in the Debug console, after Editora saves the package's
edited Rust files. Stop ends the build, and a build that fails launches
nothing. CodeLLDB then debugs the program or test binary Cargo built, with the
arguments set by **File: Run with Arguments…**. **Restart** builds and runs
the same program or test again.

- **Values** show as Rust values: a `String` or a `&str` reads as its text, and
  a `Vec` or a `HashMap` unfolds to its items.
- **A panic** stops the program where it happened: the **Rust: on panic**
  [exception breakpoint](#exception-breakpoints) is on by default.

![shelf.rs paused on line 49, total += subtotal, with inline values such as item: {sku:"A-2", name:"Notebook", quantity:3, …} on the lines above, and the Debug window below: the call stack at shelf::shelf::checkout, and Variables with lines, a Vec<String> of size 2, unfolded to "A-1  Pencil …" and "A-2  Notebook …", then total, item and discount = None](/screenshots/docs/debug-rust-values.png "A Rust program at a breakpoint: a Vec unfolds to its items, and each String reads as its text.")
- **Breakpoints** take conditions, hit counts and log messages, and
  **stepping**, **Watches** and the [Threads view](#threads-view) work.
- With [program input](#program-input-in-the-debug-console) on, Editora starts
  the program for CodeLLDB, so the program reads lines typed in the Debug
  console.
- The [Disassembly and Memory views](#disassembly-and-memory) show the
  program's machine code and memory, and step by instruction.

[Attach to Process](#attach-to-process) attaches CodeLLDB to a Rust program
that is already running, on Linux, when it was built with debug information.
Stop leaves that program running.

CodeLLDB isn't bundled. Editora finds it as set on the **Rust, C and C++
(CodeLLDB)** card of **Settings → Debugging** (the `codelldb` program, or the
folder of an unpacked CodeLLDB extension), else in its own install, else in
VS Code's CodeLLDB extension (`vadimcn.vscode-lldb`), else on the PATH. The
card's **Install…** downloads CodeLLDB 1.12.3, the release tested with Editora,
for Linux, macOS or Windows, checks its SHA-256 checksum, and unpacks it into
your config folder. Cargo and Rust come from rustup; Editora doesn't install
them. The card's switch, **Enable Rust debugging (CodeLLDB)**, is on by
default, and the [Doctor](/docs/troubleshooting#doctor) checks for CodeLLDB.

### Debugging C and C++

C and C++ are debugged with the same [CodeLLDB](#debugging-rust) as Rust, or
with GDB if you choose it. **Debug: Start / Continue** (`debug.start`, F5) or
**Debug File** in the editor's right-click menu builds the program first, in
the Debug console, after Editora saves the project's edited C and C++ files:

- **In a CMake project** (a `CMakeLists.txt` in the file's folder or above),
  it configures a Debug build in `build/editora-debug` and builds it, then
  debugs the program whose sources hold the file, else the project's only
  program.
- **In a Make project** (the nearest makefile above the file), it runs `make`
  in that folder, then debugs the program it built: the one named after the
  file, in the folder or in its `bin/` or `build/`, else the only one, else the
  newest.
- **A file outside a project** is compiled with `cc`, or `c++` for C++, with
  `-g -O0`, into Editora's cache folder. A header outside a project says to
  open its source file instead.

CMake and Make run the project's own code, so the first build asks you to
[trust the folder](/docs/workspace#trusted-folders), as Maven and Cargo do. A
lone file compiled with `cc` asks nothing. A build that fails launches nothing.
**Restart** builds again, and the arguments set by **File: Run with
Arguments…** are passed to the program.

- **Values:** `std::string` reads as its text, and `std::vector` and
  `std::map` unfold to their items.
- **A thrown C++ exception** stops the program where it was thrown: the
  **C++: on throw** [exception breakpoint](#exception-breakpoints) is on by
  default.
- **Breakpoints**, **stepping**, **Watches**, the
  [Threads view](#threads-view) and the
  [Disassembly and Memory views](#disassembly-and-memory) work.

[Attach to Process](#attach-to-process) lists running C and C++ programs built
with debug information, on Linux, and attaches CodeLLDB to the one you pick.

The **Rust, C and C++ (CodeLLDB)** card of **Settings → Debugging** has the
switch **Enable C and C++ debugging (CodeLLDB)**, on by default, and **C and
C++ debugger**, which picks **CodeLLDB** or **GDB**. GDB debugs through its own
Debug Adapter Protocol mode (`gdb -i dap`), which needs GDB 14 or later. Editora
finds `gdb` as set in **GDB path** on the same card, else on the PATH; without
it, Debug says GDB was not found. GDB isn't installed by Editora.

### Debugging C#

C# is debugged with [netcoredbg](https://github.com/Samsung/netcoredbg), the
open-source .NET debugger. **Debug: Start / Continue** (`debug.start`, F5) or
**Debug File** in the editor's right-click menu builds the program with
`dotnet build` in the Debug console, after Editora saves the project's edited
C# files, then debugs it:

- **In a project**, the nearest `.csproj` in the file's folder or above it (of
  several, the one named after the folder) is built. MSBuild runs the
  project's own build steps, so the first build asks you to
  [trust the folder](/docs/workspace#trusted-folders).
- **A lone `.cs` file** is built and debugged as a .NET 10 file-based app. No
  trust is asked.

The program runs on `dotnet` in the project's folder, with the arguments set by
**File: Run with Arguments…**; its output reaches the Debug console. An
exception your code does not handle stops it, as in Visual Studio: the **C#:
user-unhandled exceptions** [exception breakpoint](#exception-breakpoints) is on
by default, and **C#: all exceptions** stops on every throw.

Debugging a test at the caret and attaching to a running .NET program aren't
offered yet; a [launch.json](#launchjson-configurations) `coreclr` configuration
can attach by process id.

netcoredbg isn't bundled. Editora finds it as set on the **C# (netcoredbg)**
card of **Settings → Debugging**, else in its own install, else in the free C#
extension for VS Code-compatible editors (`muhammad-sammy.csharp`), else on the
PATH. Microsoft's own C# extension ships a debugger licensed only for
Microsoft's tools, so Editora doesn't use it. The card's **Install…** downloads
netcoredbg 3.2.0-1092 and checks its SHA-256 checksum; Samsung builds none for
Intel Macs. The .NET SDK isn't installed by Editora; it is found on the PATH
and in `~/.dotnet`, where Microsoft's install script puts it.

### Debugging Ruby

Ruby is debugged with rdbg, the debugger of Ruby's `debug` gem, which Ruby 3.1
and later ship. **Debug: Start / Continue** (`debug.start`, F5) or **Debug
File** runs the file under rdbg with `ruby`, with the arguments set by **File:
Run with Arguments…**, and stops at your breakpoints. The program's output
reaches the Debug console. Ruby's
[exception breakpoints](#exception-breakpoints), **Ruby: any raised exception**
and **Ruby: RuntimeError**, are off by default, as in VS Code.

Editora finds `rdbg` as set on the **Ruby (rdbg)** card of **Settings →
Debugging**, else on the PATH, else in your own gem folders (such as
`~/.local/share/gem/ruby/*/bin`). The card's **Install…** runs
`gem install --user-install debug`, which puts it in your gem folder, since a
system Ruby's own gem folder belongs to root.

A [launch.json](#launchjson-configurations) `rdbg` configuration can also run a
script under `bundle exec`, or attach to a program started with `rdbg --open`.

### Debugging PHP

PHP is debugged with [Xdebug](https://xdebug.org), through PHP Debug
(vscode-php-debug), the adapter VS Code uses. **Debug: Start / Continue**
(`debug.start`, F5) or **Debug File** starts `php` on the file with Xdebug
turned on for that run, with the arguments set by **File: Run with
Arguments…**, and stops at your breakpoints. The program's output reaches the
Debug console. PHP's [exception breakpoints](#exception-breakpoints)
(exceptions, errors, warnings, notices, deprecations, or everything) are off by
default.

PHP needs the Xdebug extension installed (on Debian and Ubuntu, the
`php-xdebug` package), and the adapter runs on Node.js 20.18 or later. Editora
finds PHP Debug as set on the **PHP (Xdebug)** card of **Settings → Debugging**
(its `phpDebug.js`, or the folder of the unpacked extension), else in its own
install, else in VS Code's PHP Debug extension (`xdebug.php-debug`). The card's
**Install…** downloads PHP Debug 1.40.2. Editora doesn't install PHP or Xdebug.

PHP has no Attach to Process: Xdebug connects to a debugger that listens for
it. A [launch.json](#launchjson-configurations) `php` configuration without a
`program`, VS Code's *Listen for Xdebug*, starts PHP Debug listening, and PHP
run elsewhere, such as by a web server, with Xdebug set to connect to it stops
at your breakpoints.

### launch.json configurations

**Debug: Start Configuration…** (`debug.startConfiguration`, in **Run ▸
Debugger**) lists the configurations of the project's `.vscode/launch.json`, or
of the nearest one above the active file, and starts the one you pick as VS
Code would. Type to filter the list; the configuration you started last is
listed first. With no `launch.json`, the status bar says so.

| `type` | Debugger |
| --- | --- |
| `lldb` | CodeLLDB, for Rust, C or C++. Its `cargo` field runs Cargo first and debugs the program Cargo built |
| `gdb` | GDB's own debug adapter mode (GDB 14 or later); attaches by `pid` |
| `cppdbg` | GDB, or CodeLLDB when `MIMode` is `lldb`; attaches by `processId` |
| `go` | Delve, with `exec` for a program you built yourself; `mode: remote` isn't supported |
| `debugpy`, `python` | debugpy; an attach connects to its `connect` address |
| `node`, `pwa-node` | vscode-js-debug; an attach connects to its `address` and `port` |
| `java` | the Java debugger: `mainClass`, `projectName`, `args`, `vmArgs`, `cwd` and `env` |
| `coreclr` | netcoredbg, `program` being the built assembly; attaches by `processId` |
| `rdbg` | rdbg, with `useBundler` for `bundle exec`; an attach connects to a local `debugPort` |
| `php` | PHP Debug; without a `program`, it listens for Xdebug |

A [debug adapter of your own](#debug-adapters-of-your-own) is used by its
`type` too. Any other type, such as C# Dev Kit's `dotnet`, is refused with a
message.

The file is read as VS Code reads it: comments and trailing commas are allowed,
and a configuration's `linux`, `osx` and `windows` parts apply on that system.

- **Variables:** `${workspaceFolder}`, `${file}` and the rest of the `${file…}`
  family, `${relativeFile}`, `${lineNumber}`, `${selectedText}`, `${env:NAME}`,
  `${userHome}` and `${pathSeparator}` are filled in. `${input:id}` asks for
  its value once per start, as a list to pick from (`pickString`) or a prompt
  (`promptString`), before anything runs. `${command:…}` and `${config:…}` name
  VS Code's own commands and settings, so they are refused with a message.
- **`preLaunchTask`:** a task of `.vscode/tasks.json` that is a `shell` or
  `process` command runs first, in the Debug console. Any other kind of task,
  or one with `dependsOn`, isn't run: the status bar says to run it yourself
  first.

### Debug adapters of your own

Any program that speaks the Debug Adapter Protocol can debug in Editora, such
as `lldb-dap` or a language's own debugger. **Debug: Edit Debug Adapters**
(`debug.editCustomAdapters`, in **Run ▸ Debugger**) opens
`debug-adapters.json` in your [config folder](/docs/configuration#config-directory),
written with a commented example the first time. Each adapter says:

- `type`: the name a launch.json configuration uses for it. It can't be one of
  Editora's own types.
- `command`: the program and its arguments, as a list.
- `transport`: how Editora talks to it: `stdio` (the default), `port` (Editora
  picks a free port and puts it where the command says `{port}`), or `announce`
  (the adapter prints the port it listens on; `announce` is a pattern whose
  first group is the port).
- `languages`: the file types F5 debugs with it, such as `["zig"]`.
- `name` and `adapterID`, optional.
- `launch`: what F5 sends it, with VS Code's variables filled in. An adapter
  without one is started only from a launch.json configuration.

```json
{
  "adapters": [
    {
      "type": "lldb-dap",
      "name": "LLDB (lldb-dap)",
      "command": ["lldb-dap"],
      "transport": "stdio",
      "languages": ["zig"],
      "launch": { "program": "${fileDirname}/${fileBasenameNoExtension}", "cwd": "${fileDirname}" }
    }
  ]
}
```

A [trusted](/docs/workspace#trusted-folders) project can bring its own in
`.editora/debug-adapters.json`; they replace yours of the same type. An entry
that can't be read is named in the status bar, and the others still work.

### Debugging on a remote host

A file in a [remote folder](/docs/remote) is debugged on its host, when that
host [may run commands](/docs/remote#allowing-a-host-to-run-commands), with
breakpoints, stepping, variables and the call stack as for a local file.
Breakpoints of a remote file are kept across reconnects. Remote is in Beta.

- **Python** runs debugpy on the host, which needs its own
  (`pip install debugpy`). The program itself runs as Run runs it there: under
  the project's virtual environment on the host, else the host's **Python for
  Run and tests**, with the project root and `src/` on `PYTHONPATH`. debugpy
  runs under the host's **Python for debugging**. The program's output reaches
  the Debug console, but it can't read typed input there.
- **JavaScript** runs vscode-js-debug and `node` on the host, from the
  script's package folder there, as Run does. The program's output reaches the
  Debug console, but it can't read typed input there.
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
[Ports and debuggers](/docs/remote#ports-and-debuggers)). Containers and WSL
are part of [Remote](/docs/remote), which is in Beta.

### Installing adapters

Adapters are not bundled. Each language's card on **Settings → Debugging** has
an **Install…** button that downloads what is missing into your config folder:
for Java and Python, the adapter together with its language server. The same
installs are three palette commands:

| Command | Installs | Needs |
| --- | --- | --- |
| **Install: Java Language Support** (`install.javaSupport`) | jdtls and the Microsoft java-debug plugin | `tar`, and a JDK to run `jdtls` |
| **Install: Python Language Support (Pyright, debugpy)** (`install.pythonSupport`) | Pyright and debugpy | Python with `pip` for debugpy; Node.js with `npm` for Pyright |
| **Install: TypeScript / JavaScript (Node.js) Language Support** (`install.jsSupport`) | `typescript-language-server` 5.3.0 with TypeScript 5.9.3, and vscode-js-debug v1.117.0 | Node.js with `npm`, and `tar` |

The Java and Python cards on the **Settings → Debugging** page have the same
**Install…** button; the JavaScript card's **Install…** installs only
vscode-js-debug. The **Go (Delve)** card's **Install…** runs `go install` for
Delve, the **Rust, C and C++ (CodeLLDB)** card's downloads CodeLLDB, the **C#
(netcoredbg)** card's downloads netcoredbg, the **Ruby (rdbg)** card's runs
`gem install` for the debug gem, and the **PHP (Xdebug)** card's downloads PHP
Debug; see [Debugging Go](#debugging-go), [Debugging Rust](#debugging-rust),
[Debugging C#](#debugging-c), [Debugging Ruby](#debugging-ruby) and
[Debugging PHP](#debugging-php). An install fetches only what is missing. When one tool's
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

Each card on **Settings → Debugging** has a **Detected** line, for example
*debugpy: found (/usr/bin/python3)* (the interpreter that has debugpy) or
*js-debug: not found*, also while that language's debugging is switched off.
The Java, JavaScript, Go, Rust, C#, Ruby and PHP cards also have the adapter's
path field with **Browse…** (C and C++ use Rust's CodeLLDB path, or the **GDB
path**), and every card but Java's a switch that turns that language's
debugging on or off. While **Enable debugging (DAP)** is off, the cards are greyed and
a note says to turn it on. Python has no path field there: debugpy runs under
a Python interpreter, and the **Python interpreter (fallback for Run, Debug and
code intelligence)** is on the Python card of **Settings → LSP**, which stays
editable with debugging off. It is the Python used when a project has no
interpreter of its own, and the one that runs debugpy when the project's
interpreter cannot import it. A link at the top of the Debugging page leads
there.

![The Go (Delve) and Rust (CodeLLDB) cards of Settings → Debugging: Enable Go debugging (Delve) on, an empty Delve (dlv) path field with Browse…, Detected dlv: found (/home/ada/go/bin/dlv); Enable Rust debugging (CodeLLDB) on, an empty CodeLLDB (codelldb) path field, and Detected codelldb: found (/home/ada/.vs…-lldb-1.12.3/adapter/codelldb); both Install buttons read Installed](/screenshots/docs/debug-settings-go-rust.png "The Go and Rust cards: Delve found where go install puts it, CodeLLDB found in VS Code's extension.")

For sending HTTP requests from `.http` files, see the
[HTTP client](/docs/http-client). For Maven, Gradle, npm, Cargo, Go, sbt,
Mill, Scala CLI, Clojure CLI, Leiningen and Babashka tasks, see
[Build tools](/docs/build-tools). The first Maven or Gradle build in a folder,
wrapper or not, asks you to
[trust the folder](/docs/workspace#trusted-folders) first, because the build
runs code the repository supplies; the other tools are not asked.
