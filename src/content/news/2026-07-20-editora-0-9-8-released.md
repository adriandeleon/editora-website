---
title: "Editora 0.9.8: GitHub, without a token"
description: "Review and check out pull requests, submit reviews, and jump from a failed CI log straight to the line, all through your own gh CLI. Plus a smarter test runner and gdb-style debug controls."
date: 2026-07-20
version: "0.9.8"
---

**Editora 0.9.8** brings GitHub into the editor. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**GitHub, via your own `gh` CLI.** Editora never handles a token. It shells out
to the authenticated [`gh`](https://cli.github.com) you already have, the same
way the Git support shells out to `git`, so GitHub Enterprise works with no
extra setup. You can **review a pull request** in a *Files changed* tab (per-file
`+`/`−` counts, the description rendered as Markdown, `n`/`p` to step through
changes), **submit a review**, **check out** or **create** a pull request, and
**open the current file on GitHub** at the caret line. There's a **pull request /
issue / Actions-runs tool window** (`M-g p`) and a **status-bar CI checks**
indicator, both of which stay invisible until the repo actually has something to
look at.

**A red build takes you to the line.** A failed CI run's log opens in the Build
Output console with **clickable stack frames**. Runner paths like
`/home/runner/work/repo/repo/src/…` are mapped back onto your local checkout, so
clicking a frame opens the real file. See the [GitHub guide](/docs/github).

**Test runner.** An unfiltered `test` run now pre-seeds the whole expected test
list greyed-out and flips each entry green or red as results land, instead of
classes popping in already finished. JUnit classes and methods get a **run icon
in the gutter**, and running tests brings up the Test Results window with a tree
that updates live.

**Debugging.** The Debug tool window gained **gdb-style single-key controls**
(`n`, `s`, `f`, and friends) with a focus highlight, and stepping no longer
yanks focus back to the editor and swallows your next key press.

**Also worth knowing:** the per-type preview toggles are replaced by one pair of
commands, `view.togglePreview` and `view.toggleSplitPreview`, that act on
whatever the active file previews as. And development builds now carry a
`-SNAPSHOT` suffix and a `snapshot` toolbar badge, so a build made from source is
never mistaken for a release.

The complete list is on the [What's New](/whats-new) page, and there's a
[0.9.8 blog post](/blog/editora-0-9-8). If you're upgrading from further back,
[0.9.7](/blog/editora-0-9-7) was a broad performance pass and
[0.9.6](/blog/editora-0-9-6) a large security and data-safety one.
