---
title: AI assistance
description: One-shot AI actions and an embedded coding agent, using the Anthropic API or a local OpenAI-compatible model. Off by default.
category: Customization
order: 7
beta: true
---

<span class="beta-pill">Beta</span>

Editora has optional AI, **off by default** and entirely yours to configure. It
comes in two parts: quick one-shot actions, and a full embedded agent.

## AI actions

These call the model directly (streamed), enabled under **Settings → AI
Actions**:

| Action | Command |
| --- | --- |
| Generate a commit message from the staged diff | `ai.generateCommitMessage` |
| Explain the selection (into a new Markdown buffer) | `ai.explainSelection` |
| Rewrite the selection per an instruction (undoable) | `ai.rewriteSelection` |
| Test the provider connection | `ai.testConnection` |

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
- Each action that needs permission pops a dialog.

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

Each provider has its **own** stored key: the Settings key field shows only the
selected provider's, and a key set for one provider is never sent to another's
endpoint. The `ANTHROPIC_API_KEY` fallback applies only to the Anthropic
provider. And Editora won't attach a key to a plain-`http://` endpoint on another
machine, refusing before it connects and asking you to use `https` or a loopback
address, so your key never crosses the network in the clear. Plain-http on
`127.0.0.1` (the usual local-inference path) is unaffected.

## Related

For letting an external agent drive Editora (rather than Editora calling a
model), see the [MCP server](/docs/mcp).
