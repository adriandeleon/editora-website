---
title: GitHub
description: "Review and check out pull requests, submit reviews, open files on GitHub, and read failed CI logs, all through your own gh CLI."
category: Version control
order: 3
beta: true
---

Editora talks to GitHub through the [`gh` CLI](https://cli.github.com) you
already have installed and signed in. **Editora never handles a token.** It
shells out to `gh`, exactly the way the [Git integration](/docs/git) shells out
to `git`, so whatever `gh` can reach, Editora can reach, including GitHub
Enterprise, with no host configuration of its own.

It's **on by default** but self-gating, so it stays completely invisible until
all of these are true:

1. The GitHub integration is enabled in **Settings → Version Control → GitHub**.
2. `gh` is on your `PATH` (or its location is set on that Settings page).
3. `gh auth status` succeeds, meaning you're signed in.
4. The current repository's remote points at a GitHub host.
5. The repository actually has an open pull request, issue, or workflow run.

If any of those fail, the tool window and status-bar indicator simply don't
appear, and each command reports the precise reason rather than failing quietly.

## Signing in

Authentication is deliberately outside the editor. Run:

```bash
gh auth login
```

Editora reads the result but never stores, prompts for, or transmits a
credential of its own. Once `gh` is authenticated, use
**GitHub: Refresh** (`github.refresh`) to re-detect it without restarting.

## Reviewing a pull request

**GitHub: Review Pull Request Diff…** (`github.viewPrDiff`) lists the open pull
requests and opens the one you pick as a **Files changed** tab, modeled on
GitHub's own review view:

- Every changed file is listed with its status letter and per-file `+` / `−`
  counts.
- The pull request description renders as **Markdown** in a card above the list.
  Long descriptions collapse behind a *Show more* toggle.
- Clicking a file opens that file's **read-only diff** (base against head).
- **Open all** opens every file at once. Past a dozen files it confirms first.

A single-file pull request skips the list and opens the diff directly. Re-running
the command on a pull request you already have open re-selects and refreshes that
tab rather than opening a second one.

Inside any read-only diff, `n` and `p` step to the next and previous change, so
you can read a file end to end without reaching for the mouse.

### Submitting a review

**GitHub: Submit Pull Request Review…** (`github.submitReview`), or the *Submit
review…* link in the review tab, opens a small form: pick **approve**, **request
changes**, or **comment**, and add a body. The body is optional for an approval
and required for the other two.

## Working with pull requests

- **GitHub: Check Out Pull Request…** (`github.checkoutPr`) picks an open pull
  request and checks out its branch. Open files that are unmodified reload from
  disk, and the Git surfaces refresh, the same choreography as switching branches.
- **GitHub: Create Pull Request…** (`github.createPr`) creates one from the
  current branch. Fill in title, body, base branch, and whether it's a draft.
  Leaving the base blank uses the repository's default branch.
- **GitHub: Open File on GitHub** (`github.openOnGitHub`) opens the active file
  in your browser at the caret line, on the current branch. This routes through
  `gh browse`, so it stays correct on GitHub Enterprise and on repositories whose
  default branch has been renamed.

## The tool window

The **GitHub** tool window (`M-g p`, or `tool.github`) has three segments:

- **Pull requests** and **Issues**, where a double-click or `Enter` reviews the
  pull request's diff or opens the issue in your browser. Each row's right-click
  menu offers check out, review diff, open on GitHub, and copy URL.
- **Runs**, listing recent GitHub Actions workflow runs with a state glyph. The
  row menu offers view failure log, rerun, rerun failed jobs, cancel, open, and
  copy URL, each enabled only where it makes sense for that run's state.

The window is **repo-scoped, not file-scoped**, so it stays put as you switch
tabs, including onto the Welcome tab.

## Reading a failed CI run

This is the part that saves the most time. Double-clicking a failed run, or
running **GitHub: View CI Failure Log…** (`github.viewRunLog`), pulls that run's
failure log into a **CI** tab of the shared
[Build Output](/docs/build-tools) console, with errors and warnings colored.

The useful bit: **the stack frames are clickable.** A CI log prints paths as they
existed on the runner, like
`/home/runner/work/your-repo/your-repo/src/main/java/…`, which doesn't exist on
your machine. Editora maps those back onto your local checkout by matching
progressively shorter repository-relative suffixes, so clicking a frame in a red
build opens the actual file at the actual line.

Logs are fetched once, not streamed, because a finished run's log doesn't grow.
Very large logs are trimmed to their last few thousand lines.

## CI checks in the status bar

When the current branch has a pull request, the status bar shows a compact checks
indicator (a check, a cross, or a circle, plus a failure count). It refreshes on
**GitHub: Refresh** and after checking out a pull request. There is **no
background polling**, so it never surprises you with network activity.

## Commands

| Command | Id |
| --- | --- |
| GitHub: Review Pull Request Diff… | `github.viewPrDiff` |
| GitHub: Submit Pull Request Review… | `github.submitReview` |
| GitHub: Check Out Pull Request… | `github.checkoutPr` |
| GitHub: Create Pull Request… | `github.createPr` |
| GitHub: Open File on GitHub | `github.openOnGitHub` |
| GitHub: View CI Failure Log… | `github.viewRunLog` |
| GitHub: Refresh | `github.refresh` |
| Tool Window: GitHub | `tool.github` (`M-g p`) |
| GitHub: Toggle Support | `view.toggleGithub` |

## Limitations

`gh` sets the ceiling on what's possible here. Inline and threaded review
comments, and GitHub's "suggested changes", have no `gh` subcommand, so they
aren't supported yet. Neither are notifications, job-level drill-down into a run,
or artifact downloads. Pull request diffs are fetched whole rather than per file,
and binary files appear as stubs.

The integration is also disabled in [Simple UI mode](/docs/workspace), along with
the other heavier features.
