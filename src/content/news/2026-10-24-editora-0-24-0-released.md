---
title: "Editora 0.24.0: Maven reworked, narrowing from the Structure window, and the Project Map in its own window"
description: "Editora 0.24.0 reworks Maven support: the tree reads the parent chain and profiles, a module builds from its reactor root, Run and Debug build first, Debug Test attaches again, Update Versions compares versions as Maven does, and the pom.xml server answers again. The Structure window narrows the editor to a symbol's block, the Project Map opens in a window of its own, Settings pages get icons and say which project they edit, and new null checking in the build fixed about ten crashes."
date: 2026-10-24
version: "0.24.0"
---

**Editora 0.24.0** is out. Most of it is Maven: two review passes went through
the tool window, the New Project wizard, Update Versions, the `pom.xml` server
and Run, Debug and Test on Maven projects. The Structure window can now narrow
the editor to a symbol, and the Project Map moves to a window of its own.
Download it from the
[0.24.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.24.0).

## Maven

- **The tree reads the whole project.** A module's tree merges the parent
  chain found beside it on disk: profiles, plugins, managed goal groups and
  `${property}` versions. Each profile is off, `-P` or `-P!id` and shows its
  activation condition, and the ones your `settings.xml` turns on are shown
  locked.
- **A module builds from its reactor root.** In a multi-module project the
  *Reactor* toggle runs a task as `mvn -f <root>/pom.xml -pl <module> -am`, so
  `compile` in a module that depends on a sibling no longer fails with
  "Could not find artifact".
- **Run and Debug build first.** Main Class in a Maven or Gradle project runs
  `compile` (or `test-compile`) before the launch, so a fresh clone no longer
  stops with `ClassNotFoundException` and an edited file no longer runs the old
  binary. Settings → Build Tools → **Build before Run and Debug** turns it off.
- **Debug Test attaches again** with Surefire 3.
- **Versions are compared the way Maven compares them.** Update Versions no
  longer proposes a downgrade such as `guava 33.0.0-jre → r09`, it lets you pick
  rows one by one, and the New Project wizard no longer writes milestones and
  betas into a new project. **Load full catalog…** works again.
- **The `pom.xml` server works.** The lemminx-maven install was missing three
  libraries, so it could not answer any request. Install… now fetches them and
  tests the server before keeping it. **If you installed it before, click
  Install… once.**
- **Safer first builds.** The first build in a Maven or Gradle folder that is
  not trusted yet asks once and lists what will run.
- The Output console keeps Maven's colours under every theme, makes
  `App.java:[5,18]` clickable, says **Stopped** for a build you stopped, and keeps
  the previous run.

[More about Maven](/docs/build-tools#maven).

## Narrow from the Structure window

The Structure window's **Narrow to fold region** toggle narrows the editor to
the picked symbol's block; while it is on, a click on another symbol narrows to
that one, and a second click shows the whole file again. A narrowed editor now
says so where you are looking: an amber banner names the lines you can see
(*Narrowed to lines 120–180 of 950*) with a **Widen** button, and the gutter
keeps the file's line numbers instead of starting again at 1.
[More about narrowing](/docs/editing#the-emacs-editing-model).

## The Project Map in its own window

The Project tool window's **Map** button now opens the Project Map in a
separate, resizable window, which can sit beside the editor or on another
screen, while the tool window keeps the tree. The Map window has its own
search field, opening a file from it brings the editor forward, and a workspace
that last had it open reopens it.
[More about the Project Map](/docs/workspace#project-map).

## Settings

- **An icon for every page** in the sidebar, the same one the page uses on the
  tool stripe or toolbar where it has one.
- **Project settings say which project.** In a project window the switch reads
  *Project: name*, the bar is tinted while Project is chosen, and the window
  title names the project.

[More about Settings](/docs/configuration#the-settings-window).

## Fewer crashes

Every package of Editora is now checked for nulls when it is built, and a null
that is not declared fails the build. Turning that on found about ten bugs that
would have failed an action with a `NullPointerException`, in Terminal Find, the
debugger, folder watching, the plugin registry, the test runner, SSH config
`Include` lines, Local History, and Scala and Node.js Run.

## Also fixed

- **Remote:** a 2FA code asked after the password no longer says "That was not
  accepted. Try again.", and a login or host-key question left unanswered for
  two minutes now closes its dialog.
- **Deployments:** a second Run while a deployed configuration was getting
  ready no longer moved the program to your own machine.
- **Markdown:** switching to Preview right after a highlight pass keeps your
  line instead of jumping to the top.
- Opening a root such as `/` or `D:\` as a file says it is a folder instead of
  throwing.

For trying the debugger, the repository now has a small
[Java debugging sample](https://github.com/adriandeleon/Editora/tree/v0.24.0/samples/debug/java)
with a `break here` comment at every spot worth a breakpoint.

The full list is in the
[changelog](https://github.com/adriandeleon/Editora/blob/v0.24.0/CHANGELOG.md).
