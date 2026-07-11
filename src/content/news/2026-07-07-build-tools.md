---
title: "Build-tool integration for Maven, npm, Cargo, Go, and Gradle"
description: "Each detected build tool gets a toolbar icon with an IntelliJ-style actions popup that streams the chosen task to a per-tool console."
date: 2026-07-07
---

Editora now integrates with five build tools. Each one gets its own **toolbar
icon**, shown only when its marker file is present, that opens an IntelliJ-style
**actions popup** and streams the chosen task to a per-tool console.

- **Maven** (`pom.xml`): the lifecycle phases, declared profiles (checkable,
  composing via `-P`), each plugin's bound goals, and a *Run custom…* box.
- **npm** (`package.json`): one entry per `scripts` name plus `install` / `ci`,
  using the detected package manager (npm / yarn / pnpm / bun).
- **Cargo** (`Cargo.toml`): the standard subcommands, `[[bin]]` / `[[example]]`
  targets, and a `--release` toggle.
- **Go** (`go.mod`): the standard subcommands over the whole module.
- **Gradle** (`build.gradle[.kts]`): the common tasks plus *Load all tasks…*.

Maven and Gradle prefer the project's own `./mvnw` / `./gradlew` wrapper.
Discovery parses the marker file directly, with no shell-out and no new
dependency, so it's instant and offline. It's on by default, and each tool stays
inert until its marker file is found. See the [build tools guide](/docs/build-tools).
