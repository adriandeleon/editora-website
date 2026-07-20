---
title: Build tools
description: Maven, npm, Cargo, Go, and Gradle each get an IntelliJ-style tasks tool window that streams to a shared Build Output console, plus a test runner with live results.
category: Run & debug
order: 3
---

Each detected build tool gets its own **tasks tool window** (its stripe appears
when the tool's marker file is present, docked on the right by default): a
browsable tree of the tool's goals, scripts, or targets with a mini toolbar
(Run / Reload / Stop / Run custom…).
Double-click or Enter runs a task, and the output streams to the shared **Build
Output** window, which has **one tab per tool** (created on first run and
selected while it runs), so two builds running at once stay in separate tabs
instead of interleaving. A searchable actions popup is also available from the
palette (`<tool>.showActions`, e.g. *Maven: Show Actions*). `tool.<tool>` opens
the tasks window; `tool.buildOutput` opens the Build Output console. It's **on by
default**, and each tool is inert until its marker file is found.

| Tool | Marker | Actions |
| --- | --- | --- |
| Maven | `pom.xml` | Lifecycle phases, declared profiles (checkable, composing via `-P`), each plugin's bound goals, and *Run custom…* |
| npm | `package.json` | One entry per `scripts` name, plus `install` / `ci` |
| Cargo | `Cargo.toml` | Standard subcommands, `[[bin]]` / `[[example]]` targets, and a `--release` toggle |
| Go | `go.mod` / `go.work` | Standard subcommands over the whole module |
| Gradle | `build.gradle[.kts]` | Common tasks, plus *Load all tasks…* |

Maven and Gradle prefer the project's own `./mvnw` / `./gradlew` wrapper, falling
back to `mvn` / `gradle` on your `PATH`. npm uses the detected package manager
(npm / yarn / pnpm / bun) from the `packageManager` field or the lockfile.

Discovery parses the marker file directly, with no shell-out and no new
dependency, so it's instant and offline. Toggle each under **Settings →
Languages & Tools → Build Tools**.

## Test runner

Running a build tool's **test** task, from the tasks tree, the palette
(`test.run`), or a gutter icon, is intercepted and shown in a dedicated **Test
Results** tool window (`M-g e`) instead of only as raw console text. The same
single process still runs, and its raw output still streams to Build Output.

The window shows a status header with pass / fail / skip counts, a progress bar,
and elapsed time, over a tree of suites and tests. Selecting a test shows its
failure message, stack trace, and captured output in the detail pane, with
**clickable stack frames**. Double-click or `Enter` on a test jumps to its
source.

Results are parsed per tool: Maven and Gradle read the JUnit XML reports as they
are written, Go uses `go test -json`, npm reads TAP when the reporter emits it,
and Cargo parses libtest output. Where a structured format isn't available, the
window says so and points at Build Output.

For an unfiltered JVM run, Editora scans the project's test sources up front and
**pre-seeds the whole expected list greyed-out**, flipping each entry green or
red as results land, so a long run reads like a progress bar rather than classes
appearing already finished. Parameterized and dynamic tests are left out of the
seed, since their case ids are generated at runtime.

JUnit test classes and methods also get a **run icon in the gutter** (with a
tooltip naming what it runs), and a test file's right-click menu offers **Run
Tests** for the whole class. `test.runAtCaret` and `test.runClassAtCaret` do the
same from the keyboard, and `test.rerun`, `test.rerunFailed`, and `test.stop`
control the run. Gutter icons are JVM-only and need a detected Maven or Gradle
project.

Toggle the whole feature under **Settings → Languages & Tools → Build Tools**
(`view.toggleTestRunner`). It's off in [Simple UI mode](/docs/workspace).
