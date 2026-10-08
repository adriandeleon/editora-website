---
title: AI assistance
description: One-shot AI actions and an embedded coding agent, using the Anthropic API or a local OpenAI-compatible model. Off by default.
category: Customization
order: 7
beta: true
---

Editora has optional AI, **off by default** and yours to configure. It
comes in two parts: quick one-shot actions, and a full embedded agent.

## Turning it on

1. Turn on **Enable AI** in **Settings → AI** (or `view.toggleAiEnabled`). This
   master switch gates everything on this page: while it is off, every AI
   feature is disabled whatever its own setting says.
2. Turn on the part you want: **AI Actions** (`view.toggleAi`), the **AI
   Agent** (`view.toggleAgent`), or both.
3. For AI Actions, choose a [provider](#providers), give it a key or an
   endpoint, and run **AI: Test Connection** (`ai.testConnection`). For the
   agent, install an ACP agent and check its command under Settings → AI Agent.
4. Inline completion has its own switch under AI Actions
   (`view.toggleAiCompletion`) and stays off until you turn it on.

## AI actions

These call the model directly (streamed), enabled under **Settings → AI
Actions**:

| Action | Command |
| --- | --- |
| Generate a commit message from the staged diff | `ai.generateCommitMessage` |
| Explain the selection (into a new Markdown buffer) | `ai.explainSelection` |
| Rewrite the selection per an instruction (undoable) | `ai.rewriteSelection` |
| Stop the running generation | `ai.cancel` |
| Test the provider connection | `ai.testConnection` |

**Rewrite Selection** and **Generate Commit Message** put the model's text into
your work, so they apply a reply only when it is **complete**. A reply that was
cut off at the model's output limit, ended because the connection closed, came
back empty, or was declined is discarded, and the status bar says which. A
rewrite is also discarded if the selected text changed while the reply was
being generated. Explain
writes to a buffer of its own, so a partial explanation is kept and marked as
incomplete.

Rewrite refuses a selection longer than 120,000 characters instead of sending
part of it, because the reply replaces the whole selection.

There's also **inline completion**: after a typing pause, a muted one-line ghost
suggestion appears at the caret, and **Tab** accepts it. It uses its own fast
model (default `claude-haiku-4-5`), separate from the action model (default
`claude-opus-4-8`).

## AI Agent

The **AI Agent** is a chat with an embedded coding agent over the
[Agent Client Protocol](https://agentclientprotocol.com) (ACP). The default is
Claude Code's `claude-code-acp` adapter, but any ACP agent works
(Settings → AI Agent). The agent is a **user-installed external tool, never
bundled**.

- Its file reads see your open buffers' **unsaved** text.
- Its edits to open files apply as **undoable buffer edits** that you review and
  save.
- A write to a file that is **not open** goes to disk. The file's previous text
  is saved to [local file history](/docs/workspace#local-file-history) first,
  and the write is refused if that copy cannot be made. The file keeps its
  encoding, byte-order mark and line endings.
- It **cannot replace text it has not seen**. A write is refused when the file
  or buffer changed since the agent last read it, including text you typed in
  the meantime; the agent is told to read the file again.
- Each action that needs permission pops a dialog. The dialog opens with the
  focus on the **rejecting** choice, has no default button, and ignores key
  presses for a moment after it appears, so a keystroke meant for the editor
  cannot approve a request.
- Its file reads and writes are confined to the session folder. It can never
  touch Editora's own configuration, or version-control metadata such as
  `.git/`.
- In a [large file that has no undo](/docs/undo-history#files-without-undo), an
  agent edit is preceded by a Local History copy, or refused when none can be
  taken.
- Replies appear as they stream. Remote images in a reply are not loaded; the
  alt text and URL are shown instead.

Commands: `tool.agent` (the tool window), `agent.newSession`,
`agent.resumeSession`, `agent.selectClient`, `agent.selectMode`,
`agent.selectModel`, `agent.stop`. AI is gated behind a master **Enable AI**
switch (off by default) in **Settings → AI**.

## Providers

Pick a provider in Settings:

- **Anthropic API**: the API key comes from the `ANTHROPIC_API_KEY` environment
  variable or a Settings override; models are configurable.
- **Local (OpenAI-compatible)**: point every AI feature at **LM Studio**,
  **Ollama**, or any local OpenAI-compatible server, with no API key and a
  configurable endpoint.
- **LM Studio / Bionic**: a separate local provider with its own endpoint,
  action model, inline model, and optional token. It can also supply the model
  for the **OpenCode** ACP agent preset without writing OpenCode config files.
- **Codex**: use the user-installed `codex-acp` adapter and an existing Codex
  login for Explain, Rewrite, and commit-message generation. These actions use
  separate text-only sessions and leave your Agent chat intact. Inline
  completion is unavailable with this provider; its API key and endpoint
  settings are ignored.

When AI Actions are enabled and connected, the editor right-click menu includes
**AI Actions → Explain** and **Rewrite**. Generated `explanation.md` buffers
show the provider and response model when the server reports it.

Each provider has its **own** stored key: the Settings key field shows only the
selected provider's, and a key set for one provider is never sent to another's
endpoint. The `ANTHROPIC_API_KEY` fallback applies only to the Anthropic
provider. And Editora won't attach a key to a plain-`http://` endpoint on another
machine, refusing before it connects and asking you to use `https` or a loopback
address, so your key never crosses the network in the clear. Plain-http on
`127.0.0.1` (the usual local-inference path) is unaffected.

A key typed into Settings, for any provider, is saved in `settings.json` in your
[config folder](/docs/configuration) as plain text. On a shared machine, leave
the Anthropic field empty and set `ANTHROPIC_API_KEY` in your environment
instead.

## What leaves your machine

AI Actions send the following to the provider you selected: Anthropic's API,
the endpoint you configured for a local or LM Studio provider, or the
`codex-acp` adapter running on your machine for Codex.

- **Generate commit message** sends the staged diff (`git diff --cached`).
- **Explain** sends the selected text and the file's language name.
- **Rewrite** sends the selected text, the language name, and your instruction.
- A staged diff, or a selection sent to Explain, longer than 120,000 characters
  is cut at that length. Rewrite does not send a selection over that length at
  all.
- **Inline completion** sends the language name and the text around the caret,
  up to 4,000 characters before it and 1,000 after, once per typing pause. It
  only asks when the caret is at the end of a line.

The **AI Agent** gets your prompt. With **Include editor context in prompts**
on (the default; `view.toggleAgentContext`), each prompt is also prefixed with
the active file's path, the cursor line, and the first 200 characters of any
selection. Anything else the agent reads, it requests itself, within the limits
listed under [AI Agent](#ai-agent).

## Related

For letting an external agent drive Editora (rather than Editora calling a
model), see the [MCP server](/docs/mcp).
