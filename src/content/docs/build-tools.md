---
title: Build tools
description: Maven, npm, Cargo, Go, Gradle, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka each get an IntelliJ-style tasks tool window that streams to a shared Output console, plus a test runner with live results.
category: Run & debug
order: 3
---

Each detected build tool gets its own **tasks tool window** (its stripe appears
when the tool's marker file is present, docked on the right by default): a
browsable tree of the tool's goals, scripts, or targets with a mini toolbar
(Run / Reload / Stop / Run custom…).
Double-click or Enter runs a task, and the output streams to the shared **Output**
window, which has **one tab per tool** (created on first run and
selected while it runs), so two builds running at once stay in separate tabs
instead of interleaving. A searchable actions popup is also available from the
palette (`<tool>.showActions`, e.g. *Maven: Show Actions*). `tool.<tool>` opens
the tasks window; `tool.buildOutput` opens the Output console. It's **on by
default**, and each tool is inert until its marker file is found.

The console holds more than build output, and was called "Build Output" until
0.10.0. Alongside a tab per build tool it
carries a **CI** tab for a [failed GitHub Actions log](/docs/github), and a
**Git** and **GitHub** tab holding a transcript of the `git` / `gh` commands
Editora ran on your behalf, each with its output, exit code and duration. Only
the window's label changed; its stripe placement and any keybinding you gave
`tool.buildOutput` are unaffected.

While a build runs, a spinner turns beside its status and in its tab's title,
so a build in a tab you are not looking at still shows it is running; a **Git**
tab spins while a clone streams. In **Test Results** the spinner also covers
the compile step before the first test reports.

| Tool | Marker | Actions |
| --- | --- | --- |
| Maven | `pom.xml` | Lifecycle phases, declared profiles (checkable, composing via `-P`), each plugin's bound goals and its full goal list, and *Run custom…* |
| npm (the **Package Scripts** window) | `package.json` | One entry per `scripts` name, plus `install` / `ci` |
| Cargo | `Cargo.toml` | Standard subcommands, `[[bin]]` / `[[example]]` targets, and a `--release` toggle |
| Go | `go.mod` / `go.work` | Standard subcommands over the whole module |
| Gradle | `build.gradle[.kts]` | Common tasks, plus *Load all tasks…* |
| sbt | `build.sbt` | Common tasks (`compile`, `test`, `run`, `clean`, `package`, …), plus *Load all tasks…*: each subproject's `compile`, `test` and `run` |
| Mill | `build.mill` (or a Mill 0.x `build.sc`) | `__.compile`, `__.test`, `clean` and `resolve _`, plus *Load all tasks…*: each module's `compile` and `run`, and each test module |
| Scala CLI | `project.scala` | `compile`, `run`, `test`, `package`, `fmt` and `clean` over the project folder |
| Clojure CLI | `deps.edn` | The aliases that run something (`-M:test`, `-X:build`, `-T:build`), plus prepare (`-P`), `deps tree`, `deps list` and the classpath |
| Leiningen | `project.clj` | `run`, `test`, `check`, `compile`, `jar`, `uberjar`, `install`, `deps`, `clean` |
| Babashka | `bb.edn` | Each task in `:tasks` (`bb run <task>`), plus `bb tasks` |

Maven, Gradle and Mill prefer the project's own `./mvnw` / `./gradlew` /
`./mill` launcher, falling back to `mvn` / `gradle` / `mill` on your `PATH`. sbt
runs `sbt` and Scala CLI `scala-cli` from your `PATH`; sbt runs each task as one
batch command (`sbt compile`), which with sbt 2 starts sbt's server in the
background and keeps it for the next task. *Load all tasks…* runs the tool once
to list the build (`sbt projects`, `mill resolve __.compile`), which takes a
while the first time. A `package.json` project uses its own package manager
(npm, pnpm, yarn or bun); see
[npm and other package managers](#npm-and-other-package-managers).

A wrapper is a script the repository ships, and it runs with your privileges.
The first time a task (or Gradle's *Load all tasks…*) would run one, Editora
shows a **Workspace Trust** notice naming the script and waits for your answer.
Trust is remembered for the folder that ships the wrapper and the folders
inside it. Declining cancels the run; it does not fall back to the `mvn` or
`gradle` on your `PATH`. A project without a wrapper is not asked. Review or
revoke trust under [Trusted folders](/docs/workspace#trusted-folders).

Choose the JDK used for Maven under **Settings → Languages & Tools → Build
Tools → Default JDK**. The selector finds installed JDKs in standard platform
locations and through SDKMAN, asdf, mise, Jabba, and JetBrains-managed installs;
**System default** keeps using `JAVA_HOME` / `PATH`. The choice supplies both
`JAVA_HOME` and the matching `java` executable to Maven tasks, Maven classpath
resolution, Java Run and Debug, and before-launch commands, and the same JDK
runs sbt, Mill, Scala CLI and Scala's Run and Debug. A saved run
configuration can override it for one project or launch.
A Java run configuration's before-launch command runs in the project root,
whatever the configuration's working directory, so a `mvn compile` or
`gradle classes` step finds the build file.

In a [remote project](/docs/remote#allowing-a-host-to-run-commands), Maven,
Gradle, npm, Cargo and Go builds and test runs start on the host, over the same
SSH connection, with output, input and Stop as for a local build. The wrappers
are looked for on the host, and Java is the host's own (or the **JDK home** set
for that host in Settings → Remote), never the local Default JDK.

A project whose tools [run in a container](/docs/remote#tools-in-a-container)
or, on Windows, [in a WSL distribution](/docs/remote#tools-in-wsl-windows) runs
its builds and test runs there the same way, with the wrappers, Java and
environment found inside. The wrapper run there is the POSIX `./mvnw` or
`./gradlew`, also when Editora itself runs on Windows.

Discovery parses the marker file directly, with no shell-out and no new
dependency, so it's instant and offline. Toggle each under **Settings →
Languages & Tools → Build Tools**.

Each tool's card there has a **Command override** field with **Browse…** and a
**Detected** line for the program a run uses when the project has no wrapper:
the override, or else the tool's own command. For example *mvn: found
(/usr/bin/mvn)* or *gradle: not found*. A misspelled override is still saved;
the line shows that it is not found. Below it, **Project** says whether the
active file's project uses the tool. The command row is greyed while that tool
is turned off.

The same page has **Build before Run and Debug** (on): in a Maven or Gradle
project, Run and Debug on a main class first compile with the build tool, as
described under
[Running a project's main class](/docs/run-debug#running-a-projects-main-class).
Turn it off to launch whatever `target/classes` holds.

## Clojure

A `deps.edn` gets a **Clojure CLI** tasks window. An alias with `:main-opts` is
listed as `-M:<alias>`, one with `:exec-fn` as `-X:<alias>`, and one with only
`:ns-default` as the tool `-T:<alias>`; an alias that only adds dependencies or
paths runs nothing on its own and is not listed. The CLI's own commands follow:
download the dependencies (`clojure -P`), print the dependency tree and list,
and print the classpath.

A `project.clj` gets a **Leiningen** window with the common `lein` tasks. A
`project.clj` is Clojure code, so its aliases and its plugins' tasks are not
read; run them with *Run custom…*.

A `bb.edn` gets a **Babashka** window with one entry per task in its `:tasks`,
in file order, run as `bb run <task>`. Private tasks (a name starting with `-`)
are left out, as `bb tasks` leaves them out.

The three tools run `clojure`, `lein` and `bb` from your `PATH`, or the command
override in their Settings cards. Their test tasks stream to the Output console;
the Test Results window does not read Clojure test runners' output. Running and
debugging a single Clojure file is covered in
[Clojure files](/docs/run-debug#clojure-files).

## Maven

### Every goal a plugin offers

A literal reading of `pom.xml` can show only a goal written inside an explicit
`<execution>`. A plugin declared for **direct invocation**, such as
javafx-maven-plugin, spring-boot-maven-plugin or exec-maven-plugin, carries only
`<configuration>` and no executions, so that reading gives it no row, and
`javafx:run` would be reachable only by typing it into *Run custom…*.

Each plugin therefore gets a **collapsed section listing every goal it offers**,
read from the plugin's own descriptor, with each goal's description as its
tooltip. Because the goals come from the descriptor and not from a built-in
table of well-known goals, the section covers whatever plugins are in your pom.

The flat *Plugins* section and these per-plugin groups overlap by design and
answer different questions: the flat one is *what this build runs* (with the
phase and execution id in its tooltip), a group is *what this plugin can run*.

It reads your **local repository only, never the network**, so opening a project
never fetches anything and a plugin you have not downloaded contributes
nothing. Results are cached, since detection re-runs on every save and tab
switch. A goal belonging to a plugin declared only inside a profile is not
listed, even when that profile is checked.

### Building a module from its reactor root

In a multi-module project, the Maven tool window of a module starts with a
**Reactor** section holding one toggle, *Build from \<root\> (-pl \<module\> -am)*.
With it on, every task runs from the reactor root as
`mvn -f <root>/pom.xml -pl <module> -am <task>`, so `compile` or `test` in a
module that depends on a sibling no longer fails with "Could not find artifact"
until the sibling is installed. The toggle is offered only when the root pom
lists the module and the root is inside the open project; a top-level project
has no Reactor section.

### The Maven submenu

A **Maven** submenu appears wherever there's a pom in hand: on a `pom.xml` in the
editor's right-click menu, and on a folder or a `pom.xml` row in the Project
tree. It carries **Update Versions**, **Show Actions** and **Run custom…**, and
each acts on the pom it was opened on: right-click `moduleB/pom.xml` (or its
folder) and they read, list and run that module, not the one the active editor
tab is in. When that folder is not the active tab's, the status line names it.
*Rerun Last* and *Stop* are window-wide, so they are not in the submenu; the
palette and the tool window keep them.

One builder serves all three surfaces, so they always offer the same actions.
It offers nothing when Maven is off, when there's no pom above the folder, or on
a file that isn't itself a `pom.xml`.

### New Maven Project

**Maven: New Project…** (`maven.newProject`) creates a project from an
archetype and opens it in its own window. It is in the File menu, in the
Project tree's **New ▸** submenu on a folder, and in the palette, where
**Maven: New Project in Folder…** (`maven.newProjectHere`) asks for the parent
folder first. It runs your own Maven (the command override, else `mvn` on your
`PATH`), never a wrapper found in the target folder, and says so before the
wizard opens when there is none.

The archetype picker starts with a **curated list of eight** that generate and
build on a current JDK: Apache's `maven-archetype-quickstart`, `-webapp`,
`-simple`, `-site` and `-plugin` (1.5), OpenJFX's `javafx-archetype-simple` and
`javafx-archetype-fxml`, and Apache Camel's `camel-archetype-java`. Below them,
**Custom archetype…** takes a `groupId:artifactId:version`, and **Load full
catalog…** fetches Maven Central's catalog. That file is about 18 MB and lists
every published version of every archetype; Editora streams it into one row per
archetype at its newest version (about 3,800 rows) and caches the result under
`~/.editora/cache/archetype-catalog.json` for a day, after which the next load
asks Central whether the file changed before downloading it again. A fetch that
fails falls back to the cached rows, or to the curated list, with the error
shown. Every row reads `groupId:artifactId:version — description`, typing
filters on both, and where the catalog lists a curated archetype the curated
pin wins. A curated archetype runs at once; one you typed or loaded is
third-party code, so the wizard asks before Maven downloads and runs it.
**Maven: Set Archetype Catalog URL…** (`maven.setArchetypeCatalogUrl`) points
*Load full catalog…* elsewhere (`https` only).

The form then takes the coordinates, with a collapsed **Advanced** section for
the project `<url>`, the Java release and the **Update dependencies and plugins
to the latest versions** option described below. The generated project gets a
run configuration for its main class whose
[before-launch step](/docs/run-debug#a-step-before-the-launch) is
`maven:compile`, so the first Run builds before it launches.

### Update the versions in an existing project

**`maven.updateVersions`** checks the nearest `pom.xml`'s dependencies and
plugins against Maven Central and **shows what would change before writing
anything**. A dialog lists a row per artifact, current → latest, and you can
decline it. Nothing is written until you accept.

The update is applied **through the open buffer as a single edit**, so one
`C-z` takes the whole thing back and the buffer is left dirty for you to save.
Only a pom that isn't open is written to disk.

It has three limits:

- Maven Central's `<release>` marker is **not trusted**. It means "newest
  non-snapshot published", which for `maven-surefire-plugin` was a milestone for
  years; the full version list is filtered for stability instead.
- A version is **never walked backwards**, which an unguarded set-to-latest
  would do to a pom pinned on purpose. Versions are ordered by Maven's own
  rules (a port of its `ComparableVersion`), not as semver, so a number beats a
  qualifier and `guava 33.0.0-jre` is never "updated" to `r09`; a candidate whose
  leading number is below the current one is never offered either.
- A **property-driven version** (`${junit.version}`) is skipped, for a
  dependency and for a plugin alike, and so are a range (`[2.0,3.0)`) and
  `LATEST` or `RELEASE`. Rewriting the reference would replace the indirection
  you chose, and rewriting the property is a different edit that can reach
  other artifacts.

Each artifact is resolved independently and best-effort, so one unreachable
coordinate leaves that version alone rather than failing the rest. A plugin with
no `<version>` is left alone too, because its version comes from a parent and
writing one would change how the plugin is resolved.

The same option is offered up front when
[generating a new project](#new-maven-project): the wizard's **Update
dependencies and plugins to the latest versions** resolves both through this
same Maven Central path and stability filter, so a brand-new project never
starts on a milestone or beta, and the status reports both counts (*Versions
updated: 3 dependencies, 2 plugins*).

## npm and other package managers

The `package.json` scripts are in the **Package Scripts** tool window
(`tool.npm`). Each row shows a script's name and its command; run one with
double-click or Enter. Scripts run as `<manager> run <name>`, where the manager
is the one the project uses:

1. the `packageManager` field of `package.json` (for example `"pnpm@9.12.0"`);
2. else the lockfile: `pnpm-lock.yaml` → pnpm, `yarn.lock` → yarn, a `bun`
   lockfile → bun;
3. else npm.

In a **workspace** (a monorepo), a member package has no lockfile of its own, so
Editora looks upward to the workspace root that has the `packageManager` field,
the lockfile or a `pnpm-workspace.yaml`, and stops at the project root. A member
of a pnpm workspace therefore runs pnpm, not npm, and is never offered
`npm install`. Run configurations of the npm type use the same manager.

The npm card in **Settings → Languages & Tools → Build Tools** names the manager
the active project uses and why, for example *pnpm (pnpm-lock.yaml)*. Its
**Command override** applies only when it names that same manager, or is a
wrapper script of your own; an override of `npm` no longer makes a pnpm or yarn
project run npm.

The window always shows the project's own `package.json`: opening a file inside
`node_modules` does not switch it to that dependency's scripts.

**When something is missing,** the Output console says how to fix it instead of
showing an error from Java:

- a missing npm says to install Node.js (npm comes with it);
- a missing pnpm or yarn says which file asked for it, and to run
  `corepack enable` (corepack comes with Node.js) or install it;
- a missing bun says to install it from bun.sh;
- a script that fails while the package has no `node_modules` suggests running
  `<manager> install` first;
- a `package.json` that can't be read says where the mistake is.

The Output tab is named after the manager, and it shows the colours the script
prints and the Node.js it runs under, such as **Node.js 24.18.1**. Scripts use
the same Node.js as [Run](/docs/run-debug#nodejs-scripts). Stop first sends
Ctrl+C (SIGINT), so the script and its child processes can clean up.

## Test runner

Running a build tool's **test** task, from the tasks tree, the palette
(`test.run`), or a gutter icon, is intercepted and shown in a dedicated **Test
Results** tool window (`M-g e`) instead of only as raw console text. The same
single process still runs, and its raw output still streams to the Output console.

The window shows a status header with pass / fail / skip counts, a progress bar,
and elapsed time, over a tree of suites and tests. Selecting a test shows its
failure message, stack trace, and captured output in the detail pane, with
**clickable stack frames**. Double-click or `Enter` on a test jumps to its
source. A right-click on a test row offers **Go to Test**, **Rerun This Test**
and **Debug This Test**; on a class row, **Go to Test Class**, **Rerun This
Class** and **Debug This Class**. The Debug entry appears where the Java
debugger is available. A test's output, failure message and stack trace are
kept up to 200,000 characters; past that the start and the end are shown, with
a line between them saying how much was left out.

Results are parsed per tool: Maven and Gradle read the JUnit XML reports as they
are written, Go uses `go test -json`, npm reads TAP when the reporter emits it,
and Cargo parses libtest output. For `node --test`, Test Results also reads
node:test's own default output, so `"test": "node --test"` fills the tree with no
reporter flag. In TAP output a `describe` block is a group, not a failed test,
and a failure shows its error and stack. sbt, Mill and Scala CLI tests are not
read yet: their output stays in the Output console. Where a structured format isn't available, the
window says so and points at the Output console.

For an unfiltered JVM run, Editora scans the project's test sources up front and
**pre-seeds the whole expected list greyed-out**, flipping each entry green or
red as results land, so a long run reads like a progress bar rather than classes
appearing already finished. Parameterized and dynamic tests are left out of the
seed, since their case ids are generated at runtime.

JUnit test classes and methods also get a **run icon in the gutter** (with a
tooltip naming what it runs), and a test file's right-click menu offers **Run
Tests** for the whole class. `test.runAtCaret` and `test.runClassAtCaret` do the
same from the keyboard, and `test.rerun`, `test.rerunFailed`, and `test.stop`
control the run. These JUnit gutter icons need a detected Maven or Gradle
project. Python test files get their own, one per test (see
[Python tests](#python-tests)). A Node.js test file has one ▶ for the whole
file, which runs `node --test` (see
[Node.js scripts](/docs/run-debug#nodejs-scripts)); its results can be rerun one
test at a time or failed-only.

Tests in **nested classes** have their own icons: a method inside a JUnit
`@Nested` class, or inside a static nested test class, gets a ▶, and so does
the nested class itself. Run and Debug address the nested class as
`Outer$Inner`, so only that class's tests run. A class whose tests are all in
nested classes is marked the same way.

**Debug Test at Caret** (`test.debugAtCaret`) and the test-results context menu
launch the selected JUnit method or class through Maven or Gradle with its JVM
suspended, then attach Editora's Java debugger automatically. This needs the
same Java debug adapter as ordinary Java debugging. On Maven, Editora picks a
free loopback port before the launch, passes it in `-Dmaven.surefire.debug`,
and attaches as soon as the forked JVM listens on it, because Surefire 3 prints
the JDWP "Listening for transport" banner only after the test has run. While it
waits, the Test Results header reads *Debug: waiting for the test JVM to open
port N…*, and **Debug ▸ Stop** ends the Maven run before a session exists. After
60 s with no listener the status says so and points at the Output window. A
further fork of the same run (several forks, reruns) is attached after the
first has finished; forks are debugged one at a time. The test JVM prints one
"Debugger failed to attach: handshake failed" line in the Output console: that
is Editora's port check, not an error. Gradle keeps its own banner. In a Node.js test file,
Debug Test at Caret debugs the `node:test` test at the caret (see
[Debugging JavaScript and TypeScript](/docs/run-debug#debugging-javascript-and-typescript)).

### Python tests

Python tests need no build tool. A test file (`test_*.py` or `*_test.py`) gets
a ▶ on every test pytest would collect: each `test` function, each class named
`Test…` or deriving from `unittest.TestCase`, and each of its `test` methods.
The first line gets one more ▶, **Run the tests in this file**. Run File
(`file.run`) on a test file runs its tests instead of the file. The right-click
menu offers **Run This Test** or **Run Tests**, and **Run Test at Caret**
(`test.runAtCaret`) and **Run Test Class at Caret** (`test.runClassAtCaret`)
work from the keyboard. **Run Tests** (`test.run`) runs every test of the
project when a Python file is in front, or when no build tool is found and the
project root has a `pyproject.toml`, `setup.py`, `setup.cfg`, `pytest.ini`,
`tox.ini` or `conftest.py`.

The tests run with the same Python as Run (see
[Python scripts](/docs/run-debug#python-scripts)), with the project root and its
`src/` folder on `PYTHONPATH`:

- **With pytest** installed for that Python, Editora runs `python -m pytest`
  from the project root (the file's folder when there is no project) and reads
  its JUnit XML report into Test Results. Failure locations such as
  `tests/test_cart.py:14` are clickable.
- **Without pytest**, tests written with `unittest.TestCase` run with Python's
  own `python -m unittest`, from the test file's folder (a whole-project run
  from the project's `tests` folder, else its root), and the status line says
  so. Their results fill Test Results as they are printed.
- **Otherwise** the status line names the Python and how to install pytest
  there, for example `.venv/bin/python -m pip install pytest`.

The command's output streams to the **Run** console, which starts with the
command line; Stop there or in Test Results stops the tests. **Rerun Failed
Tests** (`test.rerunFailed`) runs only the tests that failed, and
**Rerun This Test**, **Go to Test** and **Rerun Tests** (`test.rerun`) work as
for JUnit; **Rerun Last** in the Run window repeats the test run too.
**Debug This Test** and Debug Test at Caret are described under
[Debugging](/docs/run-debug#debugging). With the test runner turned off, Python
tests still run, in the Run console only.

Toggle the whole feature under **Settings → Languages & Tools → Build Tools**
(`view.toggleTestRunner`). It's off in [Simple UI mode](/docs/workspace).
