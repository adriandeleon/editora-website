---
title: Build tools
description: Maven, npm, Cargo, Go, and Gradle each get an IntelliJ-style tasks tool window that streams to a shared Output console, plus a test runner with live results.
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

The console is not only for builds — it was called "Build Output" until 0.10.0,
and the rename reflects what it now holds. Alongside a tab per build tool it
carries a **CI** tab for a [failed GitHub Actions log](/docs/github), and a
**Git** and **GitHub** tab holding a transcript of the `git` / `gh` commands
Editora ran on your behalf, each with its output, exit code and duration. Only
the window's label changed; its stripe placement and any keybinding you gave
`tool.buildOutput` are unaffected.

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

## Maven

### The Maven submenu

A **Maven** submenu appears wherever there's a pom in hand: on a `pom.xml` in the
editor's right-click menu, and on a folder or a `pom.xml` row in the Project
tree. It carries Update Versions, Actions, Run…, Re-run, and Stop.

One builder serves all three surfaces, so they can't drift into offering
different actions, and it offers nothing at all when Maven is off, when there's
no pom above the folder, or on a file that isn't itself a `pom.xml`.

### Update the versions in an existing project

**`maven.updateVersions`** checks the nearest `pom.xml`'s dependencies and
plugins against Maven Central and **shows what would change before writing
anything** — a row per artifact, current → latest, behind a dialog you can
decline. Nothing is written until you accept.

The update is applied **through the open buffer as a single edit**, so one
`C-z` takes the whole thing back and the buffer is left dirty for you to save.
Only a pom that isn't open is written to disk.

Three deliberate limits, each because the obvious behaviour is wrong:

- Maven Central's `<release>` marker is **not trusted**. It means "newest
  non-snapshot published", which for `maven-surefire-plugin` was a milestone for
  years; the full version list is filtered for stability instead.
- A version is **never walked backwards**, which an unguarded set-to-latest
  would do to a pom pinned on purpose.
- A **property-driven version** (`${junit.version}`) is skipped. Rewriting the
  reference would replace the indirection you chose, and rewriting the property
  is a different edit with a different blast radius.

Each artifact is resolved independently and best-effort, so one unreachable
coordinate leaves that version alone rather than failing the rest. A plugin with
no `<version>` is left alone too — its version comes from a parent, and writing
one would change resolution rather than update it.

The same option is offered up front when [generating a new
project](/features/starting-a-project).

## Test runner

Running a build tool's **test** task, from the tasks tree, the palette
(`test.run`), or a gutter icon, is intercepted and shown in a dedicated **Test
Results** tool window (`M-g e`) instead of only as raw console text. The same
single process still runs, and its raw output still streams to the Output console.

The window shows a status header with pass / fail / skip counts, a progress bar,
and elapsed time, over a tree of suites and tests. Selecting a test shows its
failure message, stack trace, and captured output in the detail pane, with
**clickable stack frames**. Double-click or `Enter` on a test jumps to its
source.

Results are parsed per tool: Maven and Gradle read the JUnit XML reports as they
are written, Go uses `go test -json`, npm reads TAP when the reporter emits it,
and Cargo parses libtest output. Where a structured format isn't available, the
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
control the run. Gutter icons are JVM-only and need a detected Maven or Gradle
project.

Toggle the whole feature under **Settings → Languages & Tools → Build Tools**
(`view.toggleTestRunner`). It's off in [Simple UI mode](/docs/workspace).
