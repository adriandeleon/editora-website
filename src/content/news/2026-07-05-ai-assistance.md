---
title: "AI comes to Editora: actions and an embedded agent"
description: "Explain, rewrite, and generate commit messages, an inline ghost completion, and a chat agent over ACP. Anthropic or a local model, off by default."
date: 2026-07-05
beta: true
---

Editora now has optional AI, off by default and yours to configure.

**AI actions** call the model directly and stream the result:

- **Generate a commit message** from the staged diff into the Commit window.
- **Explain the selection** in a new Markdown buffer.
- **Rewrite the selection** per an instruction, as a single undoable edit.
- **Inline completion**: after a typing pause, a muted one-line ghost suggestion
  at the caret that Tab accepts (its own fast model).

**AI Agent** is a chat with an embedded coding agent over the
[Agent Client Protocol](https://agentclientprotocol.com), defaulting to Claude
Code's `claude-code-acp` adapter (any ACP agent works). Its reads see your
unsaved buffers, and its edits to open files apply as **undoable buffer edits**
you review and save, with a permission dialog for each action.

Pick your **provider** in Settings: the Anthropic API (key from
`ANTHROPIC_API_KEY` or a Settings override), or **Local (OpenAI-compatible)** to
run everything against LM Studio, Ollama, or any local server, with no API key.
See the [AI guide](/docs/ai).
