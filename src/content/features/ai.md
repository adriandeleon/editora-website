---
title: "AI assistance"
group: "Customization & extensibility"
order: 0
beta: true
summary: "One-shot AI actions (explain, rewrite, commit message, inline completion) and an embedded coding agent over ACP. Anthropic, Codex, or a local model. Off by default."
---

Editora has optional AI, off by default and yours to configure.

**AI actions** call the model directly (streamed):

- **Generate a commit message** from the staged diff into the Commit window.
- **Explain the selection** in a new Markdown buffer.
- **Rewrite the selection** per an instruction, as a single undoable edit.
- **Inline completion**: after a typing pause, a muted one-line ghost suggestion at the caret, accepted with Tab (its own fast model).

**AI Agent** is a chat with an embedded coding agent over the [Agent Client Protocol](https://agentclientprotocol.com), the default being Claude Code's `claude-code-acp` adapter (any ACP agent works). Its reads see your unsaved buffers, and its edits to open files apply as **undoable buffer edits** you review and save, with a permission dialog for each action. It works in the project of its own window, and its file reads and writes stay inside that folder. The agent is a user-installed external tool, never bundled.

**Provider**: use the **Anthropic API** (key from `ANTHROPIC_API_KEY` or Settings), **Local (OpenAI-compatible)**, a separate **LM Studio / Bionic** local provider, or **Codex** through a user-installed ACP adapter and existing login. The LM Studio provider can also power an OpenCode ACP agent preset. Codex AI Actions use separate text-only sessions and do not provide inline completion. Each provider keeps its own key, endpoint and model (Codex has its own model too), so switching provider never sends one provider's key to another's server. Keys typed into Settings are kept in the system keychain (macOS Keychain, Windows Credential Manager, Secret Service), or in `settings.json` where there is none. Enable these options under Settings → AI. See the [AI guide](/docs/ai).
