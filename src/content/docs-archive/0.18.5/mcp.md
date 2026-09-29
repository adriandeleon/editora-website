---
title: MCP server
description: Let an LLM agent observe editor state and run commands through an embedded Model Context Protocol server.
category: Customization
order: 5
beta: true
---

<span class="beta-pill">Beta</span>

Editora can run a small [Model Context Protocol](https://modelcontextprotocol.io)
server inside the editor, so an LLM agent (Claude Code, for example) can see what
you're working on and act through Editora's own commands. It's **off by default**
and guarded by a security-notice dialog; enable it in **Settings → MCP Server**.

## What it exposes

A **loopback-only** HTTP/JSON-RPC server with **bearer-token auth** exposes
fourteen tools, in three groups:

| Group | Tools |
| --- | --- |
| **Reads** | `list_open_files`, `list_tabs`, `read_buffer`, `get_selection`, `get_diagnostics`, `document_symbols`, `git_status`, `todo_scan`, `find_in_files`, `list_commands` |
| **Writes** | `edit_buffer` (undoable str-replace edits), `save_buffer` |
| **Actions** | `open_file`, `execute_command` |

So an agent can observe live state, make **undoable edits**, and drive the editor
through the same command registry the palette uses. It runs on the JDK's built-in
HTTP server, so there's no new dependency.

## Enabling and connecting

1. Turn it on in **Settings → MCP Server** (or run **Toggle MCP Server**,
   `view.toggleMcp`), and accept the security notice.
2. A status-bar **MCP** indicator shows while it's running. Click it, or run
   **MCP: Copy Endpoint Command** (`mcp.copyEndpoint`), to copy a ready-to-paste
   `claude mcp add` command with the endpoint URL and token.
3. The endpoint is also written to `mcp-endpoint.json` in your
   [config folder](/docs/configuration) for discovery.

## Security

The server binds to loopback only (never the network), requires the bearer
token, stays off until you enable it, and shows a security notice first. Even so,
`execute_command` can run any command, so only connect agents you trust. Simple
UI mode turns it off.

Commands: `view.toggleMcp`, `mcp.copyEndpoint`.
