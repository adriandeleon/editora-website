---
title: Troubleshooting
description: Doctor, unsigned installers, finding external tools, the debug log, performance, and resetting config.
category: Help
order: 3
---

## Doctor

**`View: Doctor`** in the command palette (also linked from the Welcome page)
opens a full-tab health report of every external command Editora's features rely
on, in the spirit of `flutter doctor`. It covers Git and the GitHub CLI
(including whether `gh` is signed in), ripgrep, the preview and diagram tools
(`mmdc`, `maid`, Graphviz, PlantUML, Typst), every enabled language server, the
debug adapters (java-debug, debugpy, js-debug), the Run interpreters (with a
JDK 25 check for Java), the build tools, the selected AI agent CLI, elevated
save, browsers, and the in-app installer's prerequisites.

Each row shows the resolved command and its version where available. When
something is off, the row carries a plain-language tip plus an **Install…**
button (the in-app installer) or a **Settings…** link straight to the right page.
A feature you have switched off shows as a gray informational row and is never
probed, so Doctor doesn't nag you about tools you don't want. All probes run off
the UI thread, and only when you open or refresh the screen.

Start here when something that should work doesn't: it answers "is the tool
installed, is it the version I think, and is Editora looking at the right one" in
one pass.

## A launcher is blocked on first run

Installers are currently unsigned, so the operating system stops the first
launch until you allow it. You only do this once.

- **macOS** (15 and later): open Editora once and dismiss the warning, then go
  to **System Settings → Privacy & Security**, scroll to the Security section,
  and click **Open Anyway** next to the Editora message. Confirm, and it
  launches normally from then on. On macOS 14 and earlier, Control-click the app
  in Finder and choose *Open* instead.
- **Windows**: in the SmartScreen dialog, click **More info**, then **Run
  anyway**.

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
server initializes. If it never settles, restart servers with *LSP: Restart
Language Servers* (`lsp.restartServers`). For Java specifically, a stale `jdtls` process from a
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
with a capped load. A file of 5 MB or more that would not fit in the memory that
is left (typically a second or third very large file) opens the same way, as a
read-only slice of its first part; close other large tabs and reopen it to edit
it. Lines longer than 20,000 characters are not syntax-highlighted, though the
rest of the file is. Find in a very large file searches in the background and
holds at most 100,000 matches around the current one, so the count can read
"N of 100,000+"; Next, Previous, Replace, and Replace All still reach every
match. If editing or scrolling feels heavy:

- Check whether a language server is busy (the LSP loading bar).
- Try **Simple UI mode** (`view.toggleSimpleMode`), which drops the gutter,
  minimap, and the heavier features.
- The native builds set conservative heap and GC options and bake in an AOT
  class cache for faster startup; a missing cache just falls back to a normal
  start.

## Reset or isolate your config

- **Reset to Defaults** in **Settings → Advanced** resets every preference,
  including the keymap choice, which goes back to Emacs. Text zoom is kept, and
  your key rebinds stay stored with the keymap you made them in, so they apply
  again when you switch back to it.
- **Export Configuration…** zips the active config folder for a backup or bug
  report.
- Run with `--dev` or `--config-dir <path>` to use a separate config without
  touching your everyday one. See [Configuration](/docs/configuration) and the
  [command-line reference](/docs/cli).

## Editora 0.13.0 quits instantly on startup

If you are on **0.13.0**, upgrade to **0.13.1** or later. 0.13.0 crashed a
fraction of a second into startup, intermittently, on any CPU without AVX-512
(most consumer Intel from the 12th generation onward, and every AMD before
Zen 4). It is fixed in 0.13.1, and there is no workaround worth applying on the
old build.

Still stuck? [Open an issue](https://github.com/adriandeleon/Editora/issues) and
attach the Debug Log.
