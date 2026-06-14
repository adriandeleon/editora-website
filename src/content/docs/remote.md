---
title: Remote files (SFTP)
description: Browse, edit, search, and save files on a remote host over SSH/SFTP.
category: Workspace
order: 3
---

Editora can edit files on a remote host over **SSH/SFTP**. A remote folder
mounts as the Project tree, and from there editing, syntax highlighting, search,
bookmarks, notes, and preview all work over the wire. Save writes straight back,
no dialog.

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

## Authentication

Three methods, tried in this order when set to default: your default `~/.ssh`
keys, then a specific key file, then a password. Secrets are never stored, only
the connection details (host, user, last path, and which method to use). The
secret you type is wiped from memory after the connection is made.

## What works remotely, and what doesn't

Remote files stay fully text-capable: editing, highlighting, search, bookmarks,
notes, preview, and PDF or print.

Features that run a local process or read sibling files on the local disk are
gated off for remote files: language servers, debugging, running, the HTTP
client, Git, HTML live preview, and external-change polling. Recent files round-
trip remote URIs and reconnect once the connection is open.

## Behind the scenes

A remote path is a real `java.nio.file.Path` on an SFTP filesystem, so the same
open, save, list, and search code paths work unchanged. The transport is Apache
MINA SSHD. Saving over SFTP is just a normal save.
