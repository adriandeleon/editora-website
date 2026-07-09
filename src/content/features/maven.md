---
title: "Maven support"
group: "Run & debug"
order: 4
beta: false
summary: "A toolbar icon + searchable actions popup that reads the active project's <code>pom.xml</code>, IntelliJ-style: lifecycle phases, profiles, and plugin goals, streamed to a Maven console."
---

When a project has a `pom.xml`, a **Maven** toolbar button appears (hidden until one is actually detected). Click it for a searchable popup that reads the pom directly, offline and instant, with no `mvn help:effective-pom`:

- The standard **lifecycle** phases (clean, validate, compile, test, package, verify, install, site, deploy).
- The pom's declared **profiles**, checkable to compose with a run via `-P<id>` (and marking any `activeByDefault` one).
- Each plugin's explicitly-bound **goals** as `<prefix>:<goal>` rows (e.g. `spotless:check`, `jacoco:report`), using Maven's own plugin-prefix naming; profile-scoped plugins nest under their profile once checked.
- A **"Run custom goal(s)…"** freeform prompt for anything else.

Runs prefer the project's own `./mvnw` wrapper when present (falling back to `mvn` on PATH, or a Settings override) and stream output to a dedicated **Maven console** tool window. Parsing uses the JDK's own hardened XML parser, no third-party dependency. On by default (inert until a `pom.xml` is found); off in Simple UI mode and for remote files.
