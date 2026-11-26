---
title: "Run configurations"
group: "Run & debug"
order: 2
beta: false
summary: "Save how a main class, a script, a program, a make target or an npm script launches, with its arguments, environment and a before-launch step, then run or debug it from the toolbar and share it with the project."
description: "Save how a main class, script, make target or npm script runs, with arguments, environment and a before-launch step, and share it in the project."
alt: "A Python script run configuration: script, interpreter, arguments, working folder, console and Run on"
---

---
---
A run configuration saves how something launches, so you pick it in the toolbar and press **Run** or **Debug** instead of retyping arguments. The choice is remembered across restarts, and a configuration can be shared with the project.

## What a configuration holds

- **What to launch**: a main class, a script, a program, a make target or an npm script.
- **Arguments and environment**: program and VM arguments, and environment variables, where `$NAME` expands and `@.env` loads a `.env` file.
- **Runtime**: a JDK for this configuration, or a script's interpreter and its options.
- **Working folder**, relative to the project root.
- **Run on**: this machine, or one of the project's [deployments](/features/deployments/).

## Kinds

Choose a **Type** in **Run: Edit Run Configurations…**:

- a **Java main class**, found through the language server or your Maven or Gradle build
- a **Python script** or **Python module** (`python -m`)
- a **Node.js script**, or a named **npm script** run with the project's package manager
- a **shell script**, run by its shebang
- a **Groovy**, **Kotlin**, **Clojure** or **Scala** script or program, on the configuration's JDK
- a **C**, **C++**, **Go**, **Rust** or **Dart** program, built the way Debug builds it
- a **make target**

Every kind but shell and make can be debugged, and a [compound](/docs/debugging/#compounds) starts several configurations together. A Bash script is debugged from its own tab instead.

## Run a step first

A configuration can name a command to run first, such as a build or a code generator. A **non-zero exit stops the launch**, so a stale binary never runs by accident. For Java, Debug uses that step as its build.

## Share with the team

**Run: Export Configurations to Project** writes them to `.editora/run-configurations.json`, to commit beside the [project settings](/features/projects/). **Run: Import Configurations from Project** merges them back **by name**, so importing twice never duplicates and a colleague's edit updates the one you have.

## Conveniences

- **Each configuration is a command** in the palette, so it can have its own keyboard shortcut.
- **Run: Add Run Configuration** fills the form from the open file: its script, its main class, and a name.
- **The form shows only the fields** the chosen type uses, and **Browse…** picks the script, class or npm script.
- **An incomplete configuration opens its form** at the field you still need to fill in.
- **The toolbar selector** appears only in a project with something to launch.

Run configurations belong to a [project](/features/projects/). Outside one, run a file from its gutter ▶ (see [Run files](/features/run-files/)).

See the [Running programs guide](/docs/run-debug/#run-configurations).
