---
title: "Saved remote sites, front and center"
description: "Your saved SFTP connections now have a tool window, a Settings page, and a quick-connect list on the Welcome page."
date: 2026-06-24
---

Editing files over SSH/SFTP got friendlier. Your saved sites (kept in
`connections.json`) now have three visible surfaces beyond the palette picker:

- A **Remote Sites** tool window (`M-g r`) listing every saved site, with New,
  Connect, and Remove.
- A **Settings → Remote** page to add, edit, and remove sites (label, host,
  port, user, auth method, key path).
- A **Remote Sites** quick-connect list on the [Welcome page](/docs/workspace).

Picking a site opens the connection form pre-filled. As before, your password or
key passphrase is prompted each time and never stored. Commands: **Remote Sites**
(`tool.remote`) and **Manage Remote Sites…** (`remote.settings`).

See the [remote files guide](/docs/remote) for the full SFTP story.
