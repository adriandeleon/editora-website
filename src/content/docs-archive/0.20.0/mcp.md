---
title: MCP server
description: Let an LLM agent observe editor state and run commands through an embedded Model Context Protocol server.
category: Customization
order: 5
beta: true
---

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

### Editing and saving

`edit_buffer` replaces `old_text` with `new_text` in an open buffer, through the
editor's undo history. Nothing is written to disk until `save_buffer`.

- `old_text` must be the exact text to replace and must occur once in the
  buffer, unless `replace_all` is `true`.
- To replace the buffer's entire text, the agent passes
  `replace_whole_buffer: true` and no `old_text`. An edit with a missing or empty
  `old_text` is rejected, never widened to the whole buffer. A whole-buffer
  replacement is also refused when the buffer changed since the agent last read
  it with `read_buffer`; it reads the buffer again and retries.

`save_buffer` waits for the write. Its result is `saved` only when the file is
on disk, and an error when the save was refused, failed, or is still pending.
`execute_command` reports only that a command ran, so an agent that needs to
know a save reached the disk uses `save_buffer` rather than running `file.save`.

### Argument checks

Every tool validates its arguments before doing anything:

- An argument the tool doesn't define is rejected, with the accepted names in the
  error, so a misspelled argument can't be silently ignored.
- A `path` must be absolute. An empty or relative path is rejected. Tools that
  take an optional `path` use the active buffer when it is left out.

## Enabling and connecting

1. Turn it on in **Settings → MCP Server** (or run **View: Toggle MCP Server**,
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

`mcp-endpoint.json` holds the endpoint URL **and the bearer token**. Where the
filesystem supports POSIX permissions (so not on Windows), Editora restricts the
file to your user account. It deletes the file when the server stops and leaves
it out of Export Configuration. The token is the only thing that stops another
program on your computer from driving the editor, so any program that can read
that file can connect. The port and token are new each time the server starts:
after restarting Editora or toggling the server, copy the connection command
again.

Commands: `view.toggleMcp`, `mcp.copyEndpoint`.
