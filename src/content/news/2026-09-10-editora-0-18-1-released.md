---
title: "Editora 0.18.1: safer saves, history, and Git workflows"
description: "Editora 0.18.1 hardens local and remote saves, file history, Project-tree deletion, Git operations, agent edits, and LSP workspace changes."
date: 2026-09-10
version: "0.18.1"
---

**Editora 0.18.1** is out. This patch release focuses on protecting your work
when saves, history, Git operations, remote changes, or background edits overlap.
Download it from the
[0.18.1 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.1).

## Safer local and remote writes

Existing local files are now saved through a staged replacement. If Editora
cannot create that safe staging file, it leaves the original bytes untouched
instead of falling back to an in-place write.

SFTP saves now compare the server's exact bytes with the copy you opened or last
saved and ask before overwriting a remote change. When the server supports it,
the final replacement uses an atomic rename; when a safe replacement is not
available or is interrupted, the previous remote copy is retained.

ACP agent writes to closed files use the same staged approach and respect
read-only buffers. LSP workspace edits also reject stale, closed, or newly
read-only buffers and preserve recovery files if a rollback encounters another
filesystem change.

## History and deletion protect the current file

Local History keeps newly written revision data alive until its complete index
is durable. Restoring a file validates the stored revision and refuses to
replace content that changed while the revision was loading.

Deleting files from the Project tree now resolves dirty-buffer
Save/Discard/Cancel choices before the batch starts, waits for recoverable
history, rejects changed preimages together, and prevents an older pending save
from recreating a deleted path.

## Git history meets the working tree

Double-click a revision in a file's Git history—or press Enter—to compare it
with the editable working file in the full diff viewer. From the repository-wide
Git Log, **Compare with Working Tree** provides the same workflow for an
individual changed file.

Branch switches, discard, and stash now coordinate with pending saves before
and after they modify the working tree. Clean buffers reload in order, while
dirty or deleted open copies remain recoverable.

The release also fixes distributed multi-cursor paste undo, Replace in Files
undo isolation and conflict handling, cross-window Save As coordination,
read-only loading-state flashes, and sticky headers on extremely long lines.

The complete list is on the [What's New](/whats-new) page.
