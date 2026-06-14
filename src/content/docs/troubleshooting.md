---
title: Troubleshooting
description: Unsigned installers, finding external tools, the debug log, performance, and resetting config.
category: Help
order: 3
---

## A launcher is blocked on first run

Installers are currently unsigned.

- **macOS**: right-click the app and choose *Open*, then confirm. After the first
  time it launches normally.
- **Windows**: in the SmartScreen dialog, click "More info → Run anyway".

## External tools aren't found

Language servers, debug adapters, `git`, `mmdc`, and the file runner are
external programs Editora launches. A GUI-launched app inherits a stripped
`PATH` that often misses Homebrew, npm, and version-manager directories, so a
tool that works in your terminal may look "not found" in the app.

Editora rebuilds the `PATH` from your login shell plus the usual install
locations before launching anything, which recovers most setups (including
nvm/fnm/asdf per-version bins). If a tool still isn't found:

- Set its absolute path in the relevant Settings page (LSP, Debugging, Mermaid).
- Confirm it runs from a plain login shell, since that's the `PATH` Editora reads.
- On Windows there's no login-shell convention, so prefer setting absolute paths.

## A language server starts but does nothing

The status bar shows an **LSP: \<server\>** segment and a loading bar while a
server initializes. If it never settles, restart servers with
`lsp.restartServers`. For Java specifically, a stale `jdtls` process from a
previous run can hold its workspace lock; the loading bar clearing is the sign
the handshake completed.

## Seeing errors in a packaged build

A packaged app has no visible console. Editora captures its logging and uncaught
exceptions into a **Debug Log**:

- Open it with **Settings → Advanced → Show Debug Log…** or `view.debugLog`.
- It also mirrors to `editora-session.log` in your config folder, so it survives
  a crash for a bug report.

The **message log** (click the status-bar message, or `view.messageLog`) keeps a
session history of the transient status-bar messages.

## Performance

Editora aims to stay responsive on large files. Highlighting and the minimap
turn off automatically at about 5 MB, and files of about 50 MB open read-only
with a capped load. If editing or scrolling feels heavy:

- Check whether a language server is busy (the LSP loading bar).
- Try **Simple UI mode** (`view.toggleSimpleMode`), which drops the gutter,
  minimap, and the heavier features.
- The native builds set conservative heap and GC options and bake in an AOT
  class cache for faster startup; a missing cache just falls back to a normal
  start.

## Reset or isolate your config

- **Reset to Defaults** in **Settings → Advanced** restores defaults (keeping
  text zoom and keybinding overrides).
- **Export Configuration…** zips the active config folder for a backup or bug
  report.
- Run with `--dev` or `--config-dir <path>` to use a separate config without
  touching your everyday one. See [Configuration](/docs/configuration) and the
  [command-line reference](/docs/cli).

Still stuck? [Open an issue](https://github.com/adriandeleon/Editora/issues) and
attach the Debug Log.
