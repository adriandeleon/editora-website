---
title: "Settings sync"
group: "Customization & extensibility"
order: 11
beta: true
summary: "Keeps your snippets, abbreviations, templates, personal dictionary and shortcuts the same on every computer, through a private Git repository you own. Off by default."
description: "Sync snippets, abbreviations, templates, your dictionary and shortcuts between computers through a private Git repository you own. Off by default."
alt: "Settings → Sync with a repository URL, Connect and Create on GitHub buttons, and what to sync"
---

Make a snippet on your laptop and find it on your desktop. There is no Editora account or server: sync runs through a private Git repository you own and signs in with your own Git credentials.

## Set it up

- **Connect** in **Settings → Sync** by pasting the repository URL, or use **Create on GitHub…** when `gh` is signed in. Connect first shows what the repository would bring.
- **Your own credentials.** Sync uses your Git credential helper or SSH key, and Editora never sees a token. A repository URL with a password or token in it is refused.
- **Several GitHub accounts.** Sync finds the `gh` account that can read the repository and keeps using it across `gh auth switch`.

## When it syncs

- **On its own** after startup, shortly after you change something, and every 15 minutes. Automatic syncs never prompt.
- **On demand** with **Settings Sync: Sync Now**. You can cancel a running sync until it starts writing files on this computer.

## When two computers disagree

- **Entry by entry.** Two computers that each added a snippet keep both. If the same entry changed on both, the one that syncs keeps its version and the other stays in the repository history.
- **Backups first.** Files a sync replaces are copied to a backups folder in the config directory.
- **Failures in plain sight.** A failing sync shows "Sync ⚠" in the status bar, and the Sync page shows a Connected, Not connected or Sync failing pill and says why Connect was refused.

## What stays on each computer

Shortcuts you changed sync for every keymap, but which keymap each computer uses does not. Other preferences in `settings.json`, macros and themes stay local.

**Off by default.** Turn it on in **Settings → Sync** by connecting a repository. See the [Settings sync guide](/docs/settings-sync/#setting-it-up).
