---
title: "MCP server"
group: "Customization & extensibility"
order: 8
beta: true
summary: "Embed a Model Context Protocol server in the running editor so an LLM agent (Claude Code, …) can observe live state and drive the command registry. Loopback-only, token-authed, off by default."
---

Editora can run a small **Model Context Protocol** server inside the editor, so an LLM agent like Claude Code can see what you're working on and act through Editora's own commands.

It's a **loopback-only** HTTP/JSON-RPC server with **bearer-token auth**, exposing fourteen tools:

- **Reads**: `list_open_files`, `list_tabs`, `read_buffer`, `get_selection`, `get_diagnostics`, `document_symbols`, `git_status`, `todo_scan`, `find_in_files`, `list_commands`.
- **Writes**: `edit_buffer` (undoable str-replace edits) and `save_buffer`.
- **Actions**: `open_file` (files inside the window's project, or already open there) and `execute_command`.

So an agent can observe live state, make undoable edits, and drive the editor. `edit_buffer` and `save_buffer` refuse files in Editora's config folder and in version-control metadata such as `.git/`. The endpoint is written to `mcp-endpoint.json` in your config folder for discovery, and a status-bar **MCP** indicator shows when it's running (click to copy the connection command), and Settings → MCP Server shows the endpoint, with **Copy Endpoint Command** and **New Token**. It uses the JDK's built-in HTTP server, so there's no new dependency.

It's **off by default** and guarded by a security-notice dialog. Enable it under Settings → MCP Server, or with the **Toggle MCP Server** command.
