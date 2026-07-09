---
title: "Editora 0.9.2: Maven, an AI switch, and a Windows fix"
description: "The second release adds IntelliJ-style Maven support, consolidates AI behind one master switch, makes preview links clickable, and fixes a Windows launch failure."
date: 2026-07-08
author: Adrian De Leon
tags: [release]
---

**Editora 0.9.2** is here, the second tagged release, with native installers for
macOS, Windows, and Linux. It's a focused update: one substantial new feature, a
cleanup of how AI is turned on, a couple of Markdown-preview touches, and an
important fix for Windows users.

## Maven support

The headline addition is **Maven support**. When the active project has a
`pom.xml`, a Maven button appears on the toolbar (hidden until a pom is actually
detected). Click it for a searchable popup, IntelliJ-style, that reads the pom
directly:

- the standard **lifecycle** phases (clean, validate, compile, test, package,
  verify, install, site, deploy),
- the pom's declared **profiles**, checkable to compose into a run with `-P<id>`,
- each plugin's explicitly-bound **goals** as `<prefix>:<goal>` rows (e.g.
  `spotless:check`, `jacoco:report`), and
- a **"Run custom goal(s)…"** freeform prompt for everything else.

Discovery is `pom.xml`-only and parsed with the JDK's own hardened XML parser, so
it's instant and offline, with no call out to `mvn help:effective-pom` and no
third-party XML dependency. Runs prefer the project's own `./mvnw` wrapper when
present (falling back to `mvn` on PATH, or a Settings override) and stream to a
dedicated Maven console tool window. It's on by default, inert until a pom is
found, and disabled in Simple UI mode and for remote files. Read the
[Maven guide](/docs/maven).

## AI, behind one switch

The AI Agent and AI Actions settings, previously scattered across separate
groups, now live together under a new **AI** sidebar group with a single master
**Enable/Disable AI** switch. It's **off by default**, and turning it off
disables every AI feature at once, the agent chat, commit-message generation,
explain and rewrite, and inline completion, regardless of the individual pages'
settings below it. (The [MCP server](/docs/mcp) is a separate feature and is
unaffected.) There's a palette command for it too. A fresh install now has no AI
usage until you explicitly turn it on.

## Markdown preview

Two smaller touches: a **link in the live preview is now clickable**, showing a
hand cursor and opening in your system browser, and there's a **loading spinner**
while an AI explanation streams into a preview buffer (the re-render is debounced,
so a fast stream could otherwise leave the preview blank until it finished).

## The Windows fix

If you tried a Windows build recently and it wouldn't launch, this release is for
you. Since the AOT startup cache landed, every Windows install failed with
"Failed to find JVM in ...\runtime directory". The cause: the build strips the
bundled runtime's `bin/` after AOT training to reclaim space, which is safe on
macOS and Linux, where `bin/` holds only launcher executables. But the Windows
JDK layout puts the JVM itself *inside* `bin/` (`jvm.dll` and the bootstrap
libraries), which `Editora.exe` loads from there, so deleting `bin/` removed the
JVM. The strip is now guarded to non-Windows only; the Windows `bin/` is kept
intact.

## Get it

Download an installer from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). They're
unsigned for now, so macOS wants a right-click → *Open* and Windows wants "More
info → Run anyway" the first time. The full list is on the
[What's New](/whats-new) page.

Editora is built in the open, and with the help of AI coding tools. Issues and
discussion are on [GitHub](https://github.com/adriandeleon/Editora).
