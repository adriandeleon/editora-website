---
title: "MCP server"
group: "Customization & extensibility"
order: 5
beta: true
summary: "Embed a Model Context Protocol server in the running editor so an LLM agent (Claude Code, …) can observe live state and drive the command registry. Loopback-only, token-authed, off by default."
---

Editora can run a small **Model Context Protocol** server inside the editor, so an LLM agent like Claude Code can see what you're working on and act through Editora's own commands.

It's a **loopback-only** HTTP/JSON-RPC server with **bearer-token auth**, exposing six tools:

- `list_open_files`, `read_buffer`, `get_diagnostics`
- `find_in_files`, `list_commands`, `execute_command`

The endpoint is written to `mcp-endpoint.json` in your config folder for discovery, and a status-bar **MCP** indicator shows when it's running (click to copy the connection command). It uses the JDK's built-in HTTP server, so there's no new dependency.

It's **off by default** and guarded by a security-notice dialog. Enable it under Settings → MCP Server, or with the **Toggle MCP Server** command.
