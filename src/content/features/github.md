---
title: "GitHub integration"
group: "Git & diff"
order: 2
beta: false
summary: "Review and check out pull requests, submit reviews, open a file on GitHub at the caret line, and jump from a failed CI log straight to the offending line. Uses your own <code>gh</code> CLI, so Editora never handles a token."
description: "Review and check out pull requests, submit reviews, follow CI checks and open failed logs at the line, all through your own signed-in gh CLI."
alt: "The GitHub tool window listing a repository's pull requests, with Issues and Runs tabs"
---

Review pull requests, follow CI and jump to a failing line without opening a browser. Editora works through the [`gh` CLI](https://cli.github.com) you have signed in, so it never handles a token, and GitHub Enterprise works with no extra setup.

## Review pull requests

- **Read a pull request in the editor.** **GitHub: Review Pull Request Diff…** opens a Files changed tab with the description rendered as Markdown and per-file `+` and `−` counts. Click a file for its diff.
- **Step through changes** with `n` and `p` inside any read-only diff.
- **Submit a review**, to approve, request changes or comment, with **GitHub: Submit Review…**.
- **Check out a pull request** to work on it locally.

## Open and share

- **Create a pull request** from the branch's commits and the repository's template, with a base-branch chooser and an offer to push the branch first.
- **Open the current file on GitHub** at the caret line.

## Find what you need

- **One tool window** for pull requests, issues and Actions runs (`M-g p` in the Emacs keymap). A single filter box matches anything a row shows, so `42` and `#42` both find PR 42.
- **Narrow the lists** by state, such as open, closed or merged, or to your own items, and load more on demand.

## Follow CI

- **CI checks in the status bar** for the current branch's pull request, updated while checks run, with a list of checks and their logs a click away.
- **Failed logs open at the line.** A failed run's log opens in the Output console with clickable stack frames mapped onto your local checkout.
- **A transcript** of the `gh` commands Editora ran, with exit codes and durations, sits in the console's GitHub tab.

Nothing appears until Git support is on, `gh` is signed in, and the repository has an open pull request, issue or workflow run.

**On by default.** Turn it off in **Settings → Version Control → GitHub**. See the [GitHub guide](/docs/github/).
