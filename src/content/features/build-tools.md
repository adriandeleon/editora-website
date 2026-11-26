---
title: "Build tools"
group: "Run & debug"
order: 5
beta: false
summary: "Maven, Gradle, npm, Cargo, Go, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka each get a tasks tool window and a streaming console."
description: "Tasks windows and streaming consoles for Maven, Gradle, npm, Cargo, Go, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka."
alt: "A pom.xml beside the Maven tool window, listing its lifecycle phases and profiles"
---

Run a project's builds without memorising its commands. Each build tool Editora finds gets a **tasks tool window**: a tree of its goals, scripts or targets, where a double-click or Enter runs one and the output streams to that tool's console. The window's stripe appears as soon as the tool's marker file is found, and the list loads instantly, offline.

The mini toolbar has **Run**, **Reload**, **Stop** and **Run custom…**, and each tool's actions are in the palette as a searchable popup, such as **Maven: Show Actions**.

## What each tool lists

- **Maven** (`pom.xml`): lifecycle phases, the pom's profiles as checkboxes, and each plugin's goals. It prefers `./mvnw`.
- **Gradle** (`build.gradle`, `build.gradle.kts`): the common tasks, plus **Load all tasks…**. It prefers `./gradlew`.
- **npm** (`package.json`, the **Package Scripts** window): every script, plus `install` and `ci`, run with the project's own npm, pnpm, yarn or bun.
- **Cargo** (`Cargo.toml`): the standard subcommands, each `[[bin]]` and `[[example]]`, and a `--release` toggle.
- **Go** (`go.mod`): the standard subcommands over the whole module.
- **sbt** (`build.sbt`): the common tasks, plus **Load all tasks…** with each subproject's.
- **Mill** (`build.mill`, `build.sc`): every module's compile and test, plus **Load all tasks…**. It prefers `./mill`.
- **Scala CLI** (`project.scala`): its commands over the project folder.
- **Clojure CLI** (`deps.edn`): the aliases that run something, such as `-M:test` or `-X:build`, plus the dependency tree and classpath.
- **Leiningen** (`project.clj`): the common `lein` tasks, such as `run`, `test` and `uberjar`.
- **Babashka** (`bb.edn`): one entry per task.

A missing package manager says how to get it, and a failed npm script with no `node_modules` suggests installing first.

## The project's JDK, Maven and Gradle

A Java project builds, runs, debugs and is analysed on the versions it names, so everyone on the team uses the same ones. Editora reads them from the project's settings or from the files [jkit](https://jkit-project.dev/) reads: `jkit.toml`, `.sdkmanrc`, `.tool-versions` and `.java-version`.

**Tools ▸ Java Toolchain** sets the project's JDK, Maven, Gradle, sbt and GraalVM. With jkit installed, it also installs, syncs, upgrades and removes them.

**On by default.** Turn any tool off in **Settings → Build Tools**. See the [Build tools guide](/docs/build-tools/).
