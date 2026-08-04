---
title: "Git integration"
group: "Git & diff"
order: 1
beta: true
summary: "Native Git: status-bar branch, gutter change bars vs HEAD, a Commit tool window, fetch / pull / push + branches, plus a history/log view, inline blame, and stash."
---

Native Git that shells out to your installed `git`, no bundled library.

- The **status bar** shows the current branch with ahead/behind counts and a dropdown to switch/create branches, pull, fetch, and push.
- **Gutter change bars** mark added/modified/deleted lines vs HEAD (hover for the hunk diff).
- The **Commit** tool window groups staged / changed / untracked files with stage, unstage, discard, and a commit box.
- The **Project tree colors files by Git status** (added, modified, deleted, renamed, untracked), IntelliJ-style, with changed folders tinted.
- Plus a **history / log** view, **inline blame**, and **stash**.
- **A transcript of what it ran.** The **Output** console has a **Git** tab holding every `git` command Editora ran on your behalf, with its output, exit code and duration. It logs the ones you asked for (commit, push, pull, checkout, stash, clone…) and deliberately not the `status`/`diff` reads it re-runs on every tab switch, which would bury them. It never steals focus — the transcript is waiting when you open the window.

Off by default. Enable it under Settings → Git.
