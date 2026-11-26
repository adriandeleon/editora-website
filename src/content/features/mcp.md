---
title: "MCP server"
group: "Customization & extensibility"
order: 8
beta: true
summary: "Lets an AI agent such as Claude Code read what is open in Editora and act through its commands. Loopback only, protected by a token, off by default."
description: "A loopback-only, token-authenticated MCP server inside Editora that lets an agent such as Claude Code read editor state and run commands. Off by default."
alt: "Settings → MCP Server with the loopback MCP server turned on"
---

The MCP server lets an AI agent such as Claude Code work with the editor you have open. The agent sees your files, problems and Git status, and acts through Editora's own commands rather than behind its back.

- **Reads live state.** Open files and tabs, buffer text and the selection, diagnostics, document symbols, Git status, TODOs, find in files, and the list of commands.
- **Edits you can undo.** The agent changes buffers with undoable edits, saves them, and opens files inside the window's project.
- **Runs commands** from the same list the palette uses. Trusting a folder and installing or enabling plugins stay your decisions: the agent cannot run those commands.
- **Stays on this computer.** The server listens on loopback only and requires a token. The port and token change every time Editora starts.
- **Keeps out of private places.** Edits and saves refuse files in Editora's config folder and in version-control folders such as `.git/`.

While it runs, a status-bar **MCP** item copies a ready-to-paste `claude mcp add` command. **Settings → MCP Server** has the same **Copy Endpoint Command**, and **New Token** locks out every agent set up with the old token.

**Off by default.** Turn it on in **Settings → MCP Server** or with **View: Toggle MCP Server**, and accept the security notice. See the [MCP server guide](/docs/mcp/#enabling-and-connecting).
