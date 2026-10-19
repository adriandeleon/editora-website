---
title: "Build tools"
group: "Run & debug"
order: 5
beta: false
summary: "Maven, npm, Cargo, Go, Gradle, the Clojure CLI, Leiningen and Babashka each get a tasks tool window and a streaming console."
---

Each detected build tool gets its own **tasks tool window** (its stripe appears when the tool's marker file is found): a browsable tree of the tool's goals, scripts, or targets with a mini toolbar (Run / Reload / Stop / Run custom…). Double-click or Enter runs a task, and the output streams to a separate per-tool console window. A searchable actions popup is also available from the command palette (*Maven: Show Actions*, and so on).

- **Maven** (`pom.xml`): lifecycle phases, the pom's declared profiles (checkable, composing via `-P`), and each plugin's bound goals, plus a *Run custom…* box. Prefers `./mvnw`, else `mvn`. A discovered-JDK selector controls Maven tasks, classpath resolution, Run, Debug, and before-launch steps; a run configuration can override it.
- **npm** (`package.json`, the **Package Scripts** window): one entry per `scripts` name (run as `<pm> run <name>`) plus `install` / `ci`. Uses the project's package manager (npm/yarn/pnpm/bun), found at the workspace root for a monorepo member. A missing manager says how to get it, a failed script with no `node_modules` suggests installing first, and the Output console shows the script's colours and its Node.js version.
- **Cargo** (`Cargo.toml`): the standard subcommands, any `[[bin]]` / `[[example]]` targets, and a `--release` toggle.
- **Go** (`go.mod`): the standard subcommands over the whole module.
- **Gradle** (`build.gradle[.kts]`): the common tasks plus *Load all tasks…*. Prefers `./gradlew`.
- **Clojure CLI** (`deps.edn`): the aliases that run something (`-M:test`, `-X:build`, `-T:build`), plus `-P`, the dependency tree and the classpath.
- **Leiningen** (`project.clj`): the common `lein` tasks (`run`, `test`, `uberjar`, …).
- **Babashka** (`bb.edn`): one entry per task in `:tasks`.

Discovery parses the marker file directly (no shell-out, no new dependency), so it's instant and offline. **On by default**, each inert until its marker is found. See the [build tools guide](/docs/build-tools).
