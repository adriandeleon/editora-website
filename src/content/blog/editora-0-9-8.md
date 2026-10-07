---
title: "Editora 0.9.8: GitHub without a token"
description: "Pull request review, CI logs that link back to your own checkout, and a test runner that shows you the whole run up front. Plus why the GitHub integration never asks you to paste a token."
date: 2026-07-20
author: Adrián De León
tags: [release]
---

The headline of 0.9.8 is GitHub support, and it never asks you for a token.

## Borrowing someone else's trust boundary

The obvious way to build a GitHub integration is to add an API client, ask the
user for a personal access token, and store it somewhere. That means Editora
would be holding a credential that can read your private repositories and push
to them. It also means writing token storage, refresh, and revocation, picking a
place to keep the secret and getting the file permissions right, and making
every GitHub Enterprise user tell the editor about their host.

Editora does none of that. It shells out to the
[`gh` CLI](https://cli.github.com), the same pattern the Git support already uses
for `git`, and the same one the Mermaid, diagram, and Typst features use for
their own tools. `gh` already holds your credentials, knows your hosts, and
handles enterprise. Editora runs it and parses the JSON.

In practice, GitHub Enterprise works with no configuration in Editora at all,
because `gh` resolves the host from the repository's own remote. Authentication
is `gh auth login`, outside the editor. And there is no credential in Editora's
config to leak.

The cost is a ceiling on what it can do. Inline review comments and GitHub's "suggested
changes" have no `gh` subcommand, so they aren't supported. Pull request diffs
come whole rather than per file. Those are limits of the approach, and
they seem like a fair trade for not being in the token business.

## Staying out of the way

The GitHub surfaces only appear when they apply. They are gated on five
independent conditions: the integration is enabled, `gh` is installed,
`gh auth status` succeeds, the remote is a GitHub host, and the repository has
an open pull request, issue, or workflow run.

That last one matters. A trunk-based repository with no open
pull requests and no issues shouldn't grow a GitHub tool window just because it
happens to be hosted on GitHub. But if it has a red CI run, that's when you want
the tab. So workflow runs count toward the check, evaluated last so the
cheaper conditions short-circuit first.

## Reviewing without leaving

Pull request review opens as a *Files changed* tab modeled on GitHub's own view:
each file with its status and per-file `+` / `−` counts, the description rendered
as Markdown in a card above the list, and a click opening that file's read-only
diff. In any read-only diff, `n` and `p` step through the changes, so you can
read a file end to end from the keyboard.

Getting the per-file counts right took a small correction. A diff hunk's line
lists include context lines, so their sizes are *not* the additions and deletions
GitHub shows. The counts come from the tagged `+` and `−` lines only.

## CI logs that link back to your checkout

When CI fails, the log tells you where, but in the runner's paths. A GitHub
Actions log prints paths like
`/home/runner/work/your-repo/your-repo/src/main/java/Foo.java`, and a path
beginning `/home/runner` doesn't exist on your machine, so a clickable stack
trace would resolve to nothing.

Editora maps them back. It generates progressively shorter repository-relative
suffixes of the logged path, longest first, and tries each under your project
root until one resolves. A runner prefix is about five segments and a deep Java
package path is eight or more, which is why the cap is generous. Any suffix
containing `..` is rejected outright, so a hostile log line can't walk out of the
project root.

As a result, a failed run's log opens in the Build Output console on its own CI
tab, and clicking a frame in the stack trace opens your local file at that line. Logs are fetched once rather than streamed, because a finished run's log
doesn't grow.

## Showing the whole test run up front

The test runner had a subtle presentation problem. Results arrived per class, so
a run looked like nothing was happening and then classes appeared already
finished. You couldn't tell progress from a stall.

Now an unfiltered run walks the project's test sources first, scans them for test
methods, and seeds the entire expected list greyed-out. Each entry flips green or
red as its result lands, so the run reads like a progress bar.

Seeding only covers plain `@Test` methods. A `@ParameterizedTest` or
`@TestFactory` generates case ids at runtime that don't match the method name, so
seeding those would leave placeholders that never resolve. Anything still pending
when the run finishes is pruned.

JUnit classes and methods also picked up a **run icon in the gutter**, with a
tooltip naming exactly what it runs.

## Small things

Stepping in the debugger used to reveal the stopped line and yank focus to the
editor, so the next key press went to your source instead of the debugger. Focus
now stays in the Debug panel, which also gained gdb-style single-key controls
(`n`, `s`, `f`) and a highlight showing when it has the keyboard. A fresh
breakpoint hit while you're editing still takes you to the code.

The per-type preview toggles are gone, replaced by one pair of commands,
`view.togglePreview` and `view.toggleSplitPreview`, that act on whatever the
active file previews as. Markdown, CSV, SVG, diagrams, and the rest all share
them now.

And the gutter no longer nudges your text sideways when a file crosses nine
lines. It reserves two digits minimum.

## Build honesty

Between releases the project version now carries a `-SNAPSHOT` suffix, and a
`snapshot` badge sits in the toolbar next to the `dev mode` one. The suffix shows
in `--version`, the About dialog, and the Welcome footer. A release build shows
no badge. It's a small thing, but it answers "is this the release or my build
from yesterday?" at a glance.

## Get it

Download from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The
full changelog is on the [What's New](/whats-new) page, and the GitHub
integration is documented in the [GitHub guide](/docs/github).
