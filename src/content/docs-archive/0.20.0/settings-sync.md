---
title: Settings sync
description: Keep snippets, abbreviations, templates, and your personal dictionary the same on every computer, through a private Git repository you own.
category: Customization
order: 8
beta: true
---

Settings sync keeps four kinds of personal data the same on every computer you
use Editora on. The data travels through a **Git repository you own**; there is
no Editora account and no Editora server. It is **off by default**.

## What is synced

| Data | File in the [config folder](/docs/configuration) |
| --- | --- |
| Snippets | `snippets/<language>.json` |
| Abbreviations | `abbreviations.json` |
| Templates | `templates/<id>.json` |
| Personal dictionary | `dictionary.txt` |

Each of the four has its own checkbox under **What to sync**, so you can leave
one out.

Nothing else is synced. Preferences (`settings.json`), keymaps and key
rebinds, macros, themes, plugins, and session state stay on the computer they
were made on.

## Setting it up

You need `git` installed and a Git repository that is either empty or already
used for sync by another of your computers. Use a **private** repository:
snippets and templates can contain private text.

1. Open **Settings → Sync**, or run **Settings Sync: Set Up…** (`sync.setup`).
2. Paste the repository's URL into **Repository**, for example
   `git@github.com:you/editora-sync.git`, and check the **Branch**.
   When the [GitHub CLI](/docs/github) is installed and signed in, **Create on
   GitHub…** asks for a name, creates a private repository, and fills the URL
   in.
3. Click **Connect**.

Connect looks at the repository before anything changes. If it already holds
data, a dialog states how many entries would arrive on this computer, how many
this computer would send, and how many differ on both. Nothing is removed on
either side by connecting.

Repeat the same steps on each other computer, with the same URL.
**Disconnect** stops syncing on this computer and leaves the repository and
your local files as they are.

## When it syncs

With **Sync automatically** on, Editora syncs:

- a few seconds after startup,
- about half a minute after you change a snippet, an abbreviation, a template,
  or the dictionary, and
- at the interval set on the page (15 minutes by default), while an Editora
  window has the focus.

**Settings Sync: Sync Now** (`sync.now`) and the **Sync Now** button run one on
demand. A file you edit by hand in the config folder is picked up by the next
timed sync.

The **Status** row shows when the last sync ran and what it did, for example
"3 received, 1 sent". Changes that arrive are applied to every open window
without a restart.

## How changes merge

Changes merge **entry by entry**, not file by file: one snippet, one
abbreviation, one dictionary word. Two computers that each added a snippet to
the same language both end up with both. A template is merged as a whole file.

When the **same entry** was changed on two computers, the computer that syncs
keeps its own version. The other version is not lost: it stays in the
repository's Git history. The status row names the entries this happened to.

A file that cannot be read on either side, or that was written by a newer
Editora, is left alone on both sides and named in the status row.

A sync that would remove more than half of a category that holds ten or more
entries stops and asks first: the Sync page shows **Sync Anyway**. This keeps a
damaged file on one computer from emptying the others.

## Backups

Before a sync replaces or deletes a file in your config folder, it copies that
file to `sync/backups/<timestamp>/` in the config folder. The last ten sets are
kept. **Open Sync Folder** on the Sync page opens the folder.

## Credentials and failures

Editora signs in to the repository with **your own Git credentials or SSH
key**, the same ones `git` uses in a terminal, and stores none of its own.

An automatic sync never asks for a password or a passphrase and never opens a
dialog. If it fails, the reason appears once in the status bar and a
**Sync ⚠** segment stays there until a sync succeeds; clicking it opens
Settings → Sync. A sync you start yourself with Sync Now may use your Git
credential helper.

If a sync cannot reach the repository, nothing changes locally and the next
sync tries again. A sync interrupted part-way is repeated in full by the next
one.

When two Editora processes share one config folder (see
[`--new-instance`](/docs/cli#one-editor-not-two)), only the first one syncs.

## Commands

| Action | Command | Default key |
| --- | --- | --- |
| Open Settings on the Sync page | `sync.setup` | (palette) |
| Sync once | `sync.now` | (palette) |
| Turn automatic syncing on or off | `sync.toggleAuto` | (palette) |

## Related

- [Snippets & templates](/docs/snippets-templates) and
  [Configuration](/docs/configuration) describe the files that are synced.
- **Export Configuration…** makes a one-off archive of the whole config
  folder; see [Export and reset](/docs/configuration#export-and-reset).
