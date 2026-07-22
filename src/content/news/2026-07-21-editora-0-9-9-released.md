---
title: "Editora 0.9.9: asks before it builds"
description: "Workspace trust for repo-supplied build wrappers, a much smarter command palette that explains what it can't run, the HTTP client as a real preview, and a 28% faster start on Apple silicon."
date: 2026-07-21
version: "0.9.9"
---

**Editora 0.9.9** is out. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**Workspace trust.** A repository that ships its own `./mvnw` or `./gradlew`
gets that script run with your privileges the moment you trigger a Maven or
Gradle build. Editora now asks before the first build in a folder it hasn't been
told to trust. Trust is per folder and inherited by subfolders, so a
multi-module repository asks once, and it's revocable from **Settings →
Workspace → Trusted Folders**. Declining means no build rather than a quieter
one, because a hostile `pom.xml` executes code through the `PATH` `mvn` too.
Every other build tool launches your own toolchain and is never gated. See
[Trusted folders](/docs/workspace).

**The command palette got much more honest.** Commands that can't run were
already dimmed, but only 17 features were wired up, so on a fresh install just
19 of roughly 550 commands were ever affected and the feature looked broken.
Missing features are now covered (debugging especially, which is off by
default), and commands are dimmed when they have **nothing to act on** as well
as when their feature is off: Markdown commands outside a Markdown file, Git
commands outside a repository, debugger steps with nothing suspended. Hover a
dimmed row and it says which, and names the command that would fix it.

**Command titles are consistent.** Three quarters of titles already read
`Family: Action`, but the `view.*` family was a coin flip, 46 prefixed and 48
bare, so related commands scattered when you scanned the list. All of them are
prefixed now, in all six languages, along with `tool.*`, `markdown.*` and
`lsp.*`. A test pins the convention so a family can't drift back.

**The HTTP client is now the `.http` file's preview**, not a separate tool
window. Running a request from the gutter opens the response beside the source,
in the same Editor / Split / Preview view every other rich file type uses, and
the view mode is remembered per file. See the
[HTTP client guide](/docs/http-client).

**macOS on Apple silicon starts about 28% faster.** Every tagged release from
0.9.1 through 0.9.8 shipped macOS arm64 without its ahead-of-time class cache,
costing 300 to 480 ms of cold start, because the build's cache-training run
crashed on the CI runner's virtual GPU once JavaFX 26 made Metal the default.
None of it was visible on real Apple silicon, which is why device testing never
caught it.

**Opening a file from the file manager** is much faster and no longer jumps
around: the requested file is shown first instead of after the whole previous
session restores, and language servers start per tab as you visit it. Measured
on an 8-file session opening one file: 5 to 1 child processes, 19 s to 8 s of
CPU, 917 to 696 MB.

Also: case-insensitive search now matches case pairs that change length, so
`STRASSE` finds `Straße`. Console panels scroll with your configured
keybindings. Test Results tracks the running test and shows a passing test's
real output. And adding a bookmark moved off the gutter click, onto `C-c m` and
the editor right-click menu.

The complete list is on the [What's New](/whats-new) page, and there's a
[0.9.9 blog post](/blog/editora-0-9-9).
