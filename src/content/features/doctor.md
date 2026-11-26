---
title: "Doctor"
group: "Customization & extensibility"
order: 10
beta: false
summary: "A health screen for every external tool Editora uses: what it found, which version, and where. Problems come first, with an <strong>Install…</strong> or <strong>Turn on</strong> button where Editora can fix it."
description: "Doctor checks every external tool Editora can use, shows what was found, its version and path, and installs or turns on what you need."
alt: "Doctor listing Git, the GitHub CLI, ripgrep and the diagram tools as found, with their versions"
---

Editora uses the tools installed on your machine instead of bundling its own copies. Doctor shows in one tab what it found, which version it is and where it lives, and helps you fix what is missing.

Open it with **View: Doctor** from the **Tools** menu or the palette, from the Welcome page, or from the **Open Doctor** button in the message log.

## What it checks

Doctor checks Git and the GitHub CLI, ripgrep, GnuPG, the preview and diagram tools, every language server you turned on, the formatters and linters, every debug adapter, the interpreters Run uses, the build tools, the AI provider and agent, the container engine and WSL, and any plugin that failed to load. Rows are grouped by area, and an area where everything is OK folds to one line.

## Only what you use counts

- **Problems first.** **Needs Attention** at the top gathers what is missing and what is found but not usable yet, like a `gh` that is not signed in or a JDK too old to run a file.
- **No false alarms.** A missing tool counts only when the window needs it: a file of that language is open, the project has the tool's marker file, such as `pom.xml` or `go.mod`, or you set its command. Otherwise the row says **Not installed**.
- **Always current.** Every check probes the machine afresh, and the header says when it ran. In a project on an SSH host, in WSL or in a container, the header says the rows describe this computer.

## Fix it from the row

- **Install…** fetches the tool, **Turn on** switches on a feature that is off, **How to install** explains the steps where Editora cannot, and **Settings…** opens the page that configures it.
- **Update…** appears when a server Editora installs at a tested release is older than that release.
- **Check for Newer Versions** looks up the latest releases of the other tools, only when you click it, and lists them under **Updates available**.
- **Copy Report** puts the whole report, with the Editora version, OS and Java, on the clipboard for a bug report.

See the [Doctor guide](/docs/doctor/#reading-the-report).
