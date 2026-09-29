---
title: "Editora 0.18.6: compact Java debugging and more AI choices"
description: "Editora 0.18.6 adds debugging for compact and shebang Java sources, Codex AI Actions, an LM Studio/OpenCode preset, clearer Git output, and experimental native archives."
date: 2026-09-28T22:00:53-06:00
version: "0.18.6"
---

**Editora 0.18.6** is out. You can [download the release](https://github.com/adriandeleon/Editora/releases/tag/v0.18.6) for macOS, Windows, and Linux.

## Run and debug small Java programs

Compact `.java` sources and extensionless Java files with a `java --source 25+`
shebang can now be debugged with breakpoints, stepping, and local variables.
Editora maps the debug session back to the original file, including an
extensionless script's original line numbers. The selected JDK is used for
standalone Java Run and Debug, including the Default JDK choice. Compact Java
and Python files can also run while language services are disabled.

## More AI choices

AI Actions can use the Codex ACP adapter and an existing Codex login for
explanations, rewrites, and commit messages. Their text-only sessions leave an
open Agent chat alone. A separate LM Studio / Bionic provider can power AI
Actions and an OpenCode ACP agent preset with your local endpoint and model.
Both options remain opt-in. When AI Actions are enabled and connected, Explain
and Rewrite are available from the editor's context menu.

## Clearer output and previews

Git output now gives added and deleted diffstat counts distinct colors while
keeping file paths clickable. Markdown preview tables give compact columns room
for cell padding, and anchored popups stay within the window. Local File History
also closes a race between writing its index and cleaning up revision bodies.

## Experimental native archives

Alongside the regular JVM installers, the release includes experimental
GraalVM Native Image archives for Linux x64 and macOS x64/arm64. These use
separate settings and still have measured performance and plugin limitations;
the regular installers remain the recommended downloads. See the
[release assets](https://github.com/adriandeleon/Editora/releases/tag/v0.18.6)
and [What's New](/whats-new) for the full list of changes.
