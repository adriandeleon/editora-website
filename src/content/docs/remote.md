---
title: Remote files (SFTP)
description: Browse, edit, search, and save files on a remote host over SSH/SFTP.
category: Workspace
order: 3
beta: true
---

Editora can edit files on a remote host over **SSH/SFTP**. A remote folder
mounts as the Project tree, and from there editing, syntax highlighting, search,
bookmarks, notes, and preview all work over the wire. Save writes straight back,
with no dialog when the server copy is unchanged. If it changed since you opened or
last saved the file, Editora asks before overwriting it.

## Connecting

| Action | Command |
| --- | --- |
| Connect to a host | `remote.connect` |
| Manage saved connections | `remote.manageConnections` |
| Open a remote file by URI | `remote.openFile` |
| Disconnect | `remote.disconnect` |

`remote.connect` opens a form for host, port, user, and authentication. On
success it mounts the remote folder as the Project tree and opens the Project
tool window. Connections are remembered in `connections.json`.

**Disconnect** closes every connection of the window, not only the mounted one.
When remote tabs have unsaved changes it names them and asks first.

## Saved sites

Saved sites have three surfaces beyond the palette picker:

- A **Remote Sites** tool window (`tool.remote`, `M-g r`) listing every saved
  site, with New, Connect, and Remove.
- A **Settings → Remote** page (`remote.settings`) to add, edit, and remove
  sites: label, host, port, user, auth method, and key path.
- A **Remote Sites** quick-connect list on the [Welcome page](/docs/workspace).

Picking a site opens the connection form pre-filled; your password or key
passphrase is still prompted each time and is never stored.

## Authentication

Each connection uses the one method you pick in the form: your default `~/.ssh`
keys, a specific private key file, or a password. Only that method is tried;
there is no fallback from one to the next. Secrets are never stored, only
the connection details (host, user, last path, and which method to use). The
password or passphrase you type is never written to disk, and a password is
removed from the SSH session as soon as authentication finishes.

## Host-key verification

Editora verifies the server's host key against `~/.ssh/known_hosts`, the same
file `ssh` uses, so a host you've already accepted at the terminal connects with
no prompt. A host you've never connected to shows its fingerprint and asks you
first. A host whose key has **changed** is refused outright, without any button to
wave it through, since a changed key is how an impersonation attempt looks.

## What works remotely, and what doesn't

Remote files stay fully text-capable: editing, highlighting, search, bookmarks,
notes, preview, and PDF or print.

Features that run a local process or read sibling files on the local disk are
gated off for remote files: language servers, debugging, running, the HTTP
client, Git, HTML live preview, and external-change polling. Recent files
round-trip remote URIs and reconnect once the connection is open.

## Saving

- **Save** writes back to the server, asking first when the server copy changed
  since you opened or last saved it.
- A file with **no write bit** on the server opens in
  [View mode](/docs/editing#read-only-and-view-mode). It is never replaced
  silently: Save asks first, and auto-save does not write it.
- **Save As** on a remote tab writes a **local copy**, and the tab becomes that
  local file. This also works after the connection is gone, so unsaved remote
  work can always be kept.

Personal notes on a remote file are stored under the path you opened it by, not
the target of a symlink. Notes made in an earlier version on a file opened
through a symlink may no longer be found.

## Behind the scenes

A remote path is a real `java.nio.file.Path` on an SFTP filesystem, so the same
open, save, list, and search code paths work unchanged. The transport is Apache
MINA SSHD. Saves use a server-side atomic rename when the host supports it. If a
safe replacement cannot be completed, Editora retains the previous remote copy
instead of risking a partial file.
