---
title: "Editora 0.9.6: a security and correctness sweep"
description: "A systematic security and correctness sweep across nearly every feature, with SSH host-key verification, per-provider AI keys, owner-only config, and dozens of audit-driven fixes."
date: 2026-07-17
version: "0.9.6"
---

**Editora 0.9.6** is the largest correctness release so far: a per-feature audit
that closed a long list of security holes and sharp edges across almost every
part of the editor. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**Security.** SFTP now verifies the server's host key against your
`known_hosts`, so a machine on your network can no longer impersonate a server
and collect your password and files; a changed key is refused outright. Your AI
API key is stored per-provider and never sent to another provider's endpoint,
never falls back to `ANTHROPIC_API_KEY` for a provider it doesn't belong to, and
is never attached to a plain-`http://` host on another machine. Config files,
the MCP token, and the exported configuration archive are all owner-only now. An
untrusted repo can no longer run commands through "Open Terminal Here" on
Windows, and the HTML preview server no longer follows a symlink out of its
folder.

**The audit.** Behind those headlines are dozens of quieter fixes, each from
walking through a feature and asking what could go wrong: language servers that
stayed cached after crashing, a Typst path with a space in it, a PlantUML
diagram that reported a successful export while writing nothing, external-tool
output landing in the wrong tab, a personal note jumping to the wrong line, a
crontab preview that read cron's day rule backwards. The
[What's New](/whats-new) page has the complete list.

**What you'll notice.** The five build tools now stream into **one tabbed Build
Output window** instead of a console each (see [Build tools](/docs/build-tools)).
**Edit Breakpoint** is a real form now, so you can finally set a **log message**
(a logpoint) or **disable** a breakpoint, not just a condition
([Debugging](/docs/run-debug#debugging)). **Format Document** works for JSON,
CSS, and HTML. And **Reset to Defaults** actually resets your settings, all of
them, rather than the 23-out-of-181 it used to.

There's a [0.9.6 blog post](/blog/editora-0-9-6) on what a per-feature audit
turns up, and the full changelog is on [What's New](/whats-new).
