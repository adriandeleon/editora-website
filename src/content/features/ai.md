---
title: "AI assistance"
group: "Customization & extensibility"
order: 0
beta: true
summary: "One-shot AI actions (explain, rewrite, commit message, inline completion) and an embedded coding agent over ACP. Anthropic, Codex, or a local model. Off by default."
description: "Optional AI actions (explain, rewrite, commit messages, inline completion) and an ACP coding agent, on Anthropic, Codex or a local model. Off by default."
alt: "Settings → AI Agent with the agent on, Claude Code as the active client, and editor context included in prompts"
---

AI in Editora stays out of the way until you ask for it: quick actions on the code in front of you, or a coding agent whose edits wait for your review.

## Quick actions

- **AI: Generate Commit Message** writes a message from the staged diff into the Commit window.
- **AI: Explain Selection** explains the selected code in a Markdown buffer of its own.
- **AI: Rewrite Selection…** rewrites the selection to your instruction, as one edit you can undo.
- **Inline completion** shows a muted one-line suggestion at the caret after a pause in typing, and Tab accepts it. It has its own switch and its own fast model.

## A coding agent beside your code

- **Your choice of agent.** Claude Code is the default, and Gemini CLI, GitHub Copilot CLI, Codex, Qwen Code and OpenCode also work, over the [Agent Client Protocol](https://agentclientprotocol.com). You install the agent; Editora never bundles one.
- **It sees what you see.** The agent's reads include your unsaved changes.
- **You review every edit.** Its edits to open files arrive as undoable buffer edits that you save yourself, and each action asks your permission first.
- **It stays in the project.** Its file reads and writes stay inside the project folder of its window.

## Your model, your keys

- **Anthropic API**, with the key from `ANTHROPIC_API_KEY` or Settings.
- **A local model** through any OpenAI-compatible server, or through LM Studio or Bionic, which can also run the agent through OpenCode.
- **Codex**, through your existing Codex login. It runs the quick actions but not inline completion.
- **Keys stay where they belong.** Each provider keeps its own key, endpoint and model, so switching provider never sends a key to another provider's server. Keys typed into Settings go in the system keychain, never in `settings.json`.

**Off by default.** Turn it on in **Settings → AI**. See the [AI guide](/docs/ai/#turning-it-on).
