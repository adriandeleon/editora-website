---
title: "GitHub integration"
group: "Git & diff"
order: 2
beta: true
summary: "Review and check out pull requests, submit reviews, open a file on GitHub at the caret line, and jump from a failed CI log straight to the offending line. Uses your own <code>gh</code> CLI, so Editora never handles a token."
---

GitHub, through the [`gh` CLI](https://cli.github.com) you already have signed in. **Editora never handles a token**, it shells out to `gh` the same way the Git support shells out to `git`, so GitHub Enterprise works with no extra setup.

- **Review a pull request in the editor.** A *Files changed* tab lists every file with its status and per-file `+` / `−` counts; click one for a read-only diff. The description renders as Markdown above the list, and `n` / `p` step through changes.
- **Submit a review**, approve, request changes, or comment, without leaving the editor.
- **Check out a PR**, **create a PR**, and **open the current file on GitHub** at the caret line.
- A **pull request / issue / Actions-runs tool window**, plus a **status-bar CI checks** indicator for the current branch.
- **A failed CI run's log opens in the Build Output console with clickable stack frames.** Runner paths are mapped back onto your local checkout, so a red build takes you straight to the line.

On by default, and completely invisible until `gh` is signed in and the repo actually has an open PR, issue, or workflow run. See the [GitHub guide](/docs/github).
