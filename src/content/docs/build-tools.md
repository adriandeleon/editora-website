---
title: Build tools
description: Maven, npm, Cargo, Go, and Gradle each get a toolbar icon with an IntelliJ-style actions popup that streams the chosen task to a console.
category: Run & debug
order: 3
---

Each detected build tool gets its own **toolbar icon** (shown only when its
marker file is present) that opens an IntelliJ-style **actions popup**, streaming
the chosen task to a per-tool console. It's **on by default**, and each tool is
inert until its marker file is found.

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
