---
title: Maven support
description: Run Maven lifecycle phases, profiles, and plugin goals from a toolbar popup that reads your pom.xml.
category: Run & debug
order: 3
---

When the active project has a `pom.xml`, Editora shows a **Maven** button on the
toolbar. It stays hidden until a `pom.xml` is actually detected for the active
file or project, and refreshes on tab switch, window focus, and save. Maven
support is **on by default** (inert until a pom is found); it's disabled in
[Simple UI mode](/docs/workspace) and for remote (SFTP) files.

## The actions popup

Click the Maven button (or run **Show Maven Actions** from the palette) to open a
searchable popup, read straight from the pom, offline and instant. There's no
call to `mvn help:effective-pom`; the pom is parsed directly with the JDK's own
hardened XML parser (no third-party XML dependency, XXE disabled).

The popup lists:

- **Run custom goal(s)…** at the top, a freeform prompt for anything the popup
  doesn't list (e.g. `dependency:tree`, or several goals at once).
- **Lifecycle**: the standard phases `clean`, `validate`, `compile`, `test`,
  `package`, `verify`, `install`, `site`, `deploy`.
- **Profiles**: every `<profile>` the pom declares, shown as checkable rows.
  Checking one composes it into the next run via `-P<id>` (it doesn't run on its
  own, and checking it keeps the popup open); any `activeByDefault` profile is
  marked.
- **Plugins**: each plugin's explicitly-bound `<executions>` goals as
  `<prefix>:<goal>` rows (for example `spotless:check`, `jacoco:report`), using
  Maven's own plugin-prefix naming convention. A plugin with no bound executions
  is omitted (use the freeform prompt for those). Plugins declared inside a
  profile's `<build>` nest under that profile once it's checked.

Type to filter, use the arrow keys, and press Enter (or click) to run.

## How runs execute

Editora prefers the project's own wrapper, `./mvnw` (or `mvnw.cmd` on Windows),
when it's present, so you get the exact Maven version the project pins. Otherwise
it falls back to `mvn` resolved on your `PATH`, or a command you set in
**Settings → Languages & Tools → Maven**. Output streams to a dedicated **Maven
console** tool window, which is hidden by default and opens automatically on a
run.

## Settings and commands

Enable or disable the feature in **Settings → Languages & Tools → Maven**, where
you can also set the Maven command override. The palette exposes:

- **Toggle Maven Support** (`view.toggleMavenSupport`)
- **Show Maven Actions** (`maven.showActions`)
- **Run Custom Maven Goal…** (`maven.runCustom`)
- **Stop Maven** (`maven.stop`)
- **Rerun Last Maven Goal** (`maven.rerunLast`)
- **Refresh Maven** (`maven.refresh`)
- **Toggle Maven Console** (`tool.maven`)

Discovery is `pom.xml`-only: there's no full effective-pom resolution (default
lifecycle bindings by packaging, parent-POM inheritance beyond a direct
`<parent>`, or `<pluginManagement>`), which keeps it fast and dependency-free.
