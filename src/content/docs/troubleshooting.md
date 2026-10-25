---
title: Troubleshooting
description: Doctor, unsigned installers, finding external tools, the debug log, recovering after a crash, performance, and resetting config.
category: Help
order: 3
---

## Doctor

**`View: Doctor`** (in the Tools menu and the command palette, linked from the
Welcome page and from the message log's **Open Doctor** button) opens a full-tab
health report of every external command Editora's features rely on, in the
spirit of `flutter doctor`. It covers Git and the GitHub CLI (including whether
`gh` is signed in), ripgrep, GnuPG and its passphrase prompt for encrypted
files, the preview and diagram tools (`mmdc`, `maid`, Graphviz, PlantUML, the
Typst CLI), every enabled language server, the Python formatter, ShellCheck and
shfmt, the debug adapters (java-debug, debugpy, js-debug), the Run interpreters
(with a JDK 25 check for Java, and the Python interpreter and Node.js that Run,
Debug and the language servers share), the Node.js for HTTP scripts, the build
tools, the AI provider and agent, the container engine (and WSL on Windows),
elevated save, browsers, the in-app installer's prerequisites, and plugins that
failed to load or start.

A missing tool is a problem only when the window needs it: a file of that
language is open, the project has the tool's marker file, or you set its command.
Anything else shows as **Not installed** and is not counted. Problems and warnings
are gathered under **Needs attention** at the top; sections where everything is
OK fold to one line.

Each row shows the resolved command and its version or path. When something is
off, the row carries a plain-language tip and its fix: **Install…** (the in-app
installer), **Turn on** for a switched-off feature, **How to install**, and
**Settings…** straight to the right page. All probes run off the UI thread, and
only when you open or refresh the screen.

Start here when something that should work doesn't: it answers "is the tool
installed, is it the version I think, and is Editora looking at the right one" in
one pass. **Copy Report** puts the whole report on the clipboard, ready to paste
into a bug report.

### Update… for servers older than Editora's

Editora installs some servers and tools at the exact release it is tested with:
jdtls (Java), the JavaScript debugger (js-debug), the Maven `pom.xml` server
(lemminx-maven), gls (Groovy), Metals (Scala), the Kotlin server, clojure-lsp,
ShellCheck and shfmt. A newer Editora can move to a newer release of one of
them. When the copy you have is older than the one this Editora is tested with,
its Doctor row is a warning that names both versions, and its **Install…**
button reads **Update…**.

**Update…** downloads the release Editora is tested with, replaces the old copy,
and restarts the server on it. If the download fails, the server starts again on
the old copy. Install… in Settings also replaces Editora's own copy when it is
known to be older.

What the warning says depends on whose copy it is:

- **Editora installed it.** For example *Editora installed 0.10.0; this release
  of Editora is tested with 0.11.0.* Update replaces it.
- **Installed by an earlier Editora.** Editora now records the version of each
  copy it installs. For a copy from before that, Doctor reads the version from
  its files, or from what the program prints, where it can. Where it can't tell
  (a Kotlin server from an earlier Editora, for example), the row says the copy
  may be older and offers Update, which installs the tested release.
- **Your own copy is older.** A language server you installed yourself, or a
  ShellCheck or shfmt found on your `PATH`: Update installs Editora's own copy
  and uses it instead, and your copy stays where it is. Doctor warns about your
  own copy only when it can tell its version; otherwise the row stays OK.
- **A ShellCheck or shfmt path set in Settings.** You get the warning but no
  Update, since Settings names that copy on purpose. Update that copy yourself,
  or clear the path in **Settings → LSP** to use Editora's own.

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
nvm/fnm/asdf per-version bins). A Python environment you activated before
starting Editora (`$VIRTUAL_ENV`, `$CONDA_PREFIX`) stays first on that `PATH`.
If a tool still isn't found:

- Set its absolute path in the relevant Settings page (LSP, Debugging, Mermaid).
- Confirm it runs from a plain login shell, since that's the `PATH` Editora reads.
- On Windows there's no login-shell convention, so prefer setting absolute paths.

## A language server starts but does nothing

The status bar shows an **LSP: \<server\>** segment and a loading bar while a
server initializes. If it never settles, restart servers with *LSP: Restart
Language Servers* (`lsp.restartServers`). For Java specifically, a stale `jdtls` process from a
previous run can hold its workspace lock; the loading bar clearing is the sign
the handshake completed. A server that starts but then fails its requests is
announced in the status bar (`lemminx-maven: internal error on hover — see the
log`), stopped after three failures in a row, and shown as a warning in Doctor;
the Maven `pom.xml` server whose folder is missing jars is reported as
*incomplete* in Settings → LSP and Doctor, and its **Install…** repairs it.

## Python imports show as errors

`Import "requests" could not be resolved` on a package you have installed
means Pyright is looking at a different Python than the one the package is in.

- Look at the interpreter item in the status bar, for example
  `Python 3.13.5 (PATH)`. `(PATH)` means no project environment was found.
- Click it, or run **Python: Select Interpreter** (`python.selectInterpreter`),
  and pick the project's environment, or **Browse…** to the environment's
  `python`. The server picks the change up at once.
- An environment named something other than `.venv`, `venv`, `env` or `.env`, or
  one outside the project, is not found by itself: pick it once, and the project
  keeps it.
- A `pyrightconfig.json` or `[tool.pyright]` section that sets `venv` wins over
  the pick inside Pyright.
- Doctor's **Python interpreter** row shows the same interpreter, its version
  and why it was chosen.

See [the Python interpreter](/docs/lsp#the-python-interpreter).

## A project's tools don't run in its container

A project's tools [run in a container](/docs/remote#tools-in-a-container) only
when all of these hold:

- **Run commands on the remote host** is on in Settings → Remote.
- The container is running, and one of its bind mounts covers the project
  folder. A dev container is found only when the project folder itself has a
  `.devcontainer` folder or a `.devcontainer.json` file.
- You allowed that container. **Not Now** holds until you restart Editora, and
  so does going back with *Containers: Run This Project's Tools Here*. Picking
  the container with *Containers: Run This Project's Tools in a Container…*
  allows it at once.

Editora looks for a dev container when the project opens and when its window
comes back to the front, at most every 30 seconds, so one started meanwhile is
picked up once you switch back to the window. If the picker says no containers
are running, check that the **Container engine** in Settings → Remote names the
engine you use (`docker` or `podman`) and that it lists your container in a
terminal.

A debug session that cannot reach its port in the container or WSL distribution
needs `nc`, `bash` or `python3` in it; see
[Ports and debuggers](/docs/remote#ports-and-debuggers).

## A WSL project misses outside changes

Windows cannot watch a folder on the `\\wsl.localhost` share. Editora watches a
WSL project from inside the distribution, but only while the project's
[tools run there](/docs/remote#tools-in-wsl-windows). Allow the distribution
when asked, and install `inotify-tools` in it for changes to show at once
rather than within a few seconds. See
[Outside changes in WSL](/docs/remote#outside-changes-in-wsl).

## Shell scripts show no problems or don't format

The Bash language server needs two helper programs: **ShellCheck** for
diagnostics and **shfmt** for Format Document. Without them a script looks
clean and Format Document changes nothing. The status bar says so when you open
a script or format one, and the **Bash / Shell** card in **Settings → LSP**
and the Doctor show whether each is found. Install them with the Install…
buttons there, or with **Install: ShellCheck and shfmt** (`install.shellTools`)
in the palette. See [ShellCheck and shfmt](/docs/lsp#shellcheck-and-shfmt).

If ShellCheck runs but ignores a `.shellcheckrc`, the file is probably in a
subfolder of the project; ShellCheck reads it only from the project folder or
above. See [Where ShellCheck finds .shellcheckrc](/docs/lsp#where-shellcheck-finds-shellcheckrc).

The server's own warnings, such as a helper it could not find, are in the
Debug Log (below).

## Seeing errors in a packaged build

A packaged app has no visible console. Editora captures its logging and uncaught
exceptions into a **Debug Log**:

- Open it with **Settings → Advanced → Show Debug Log…** or `view.debugLog`.
- It also mirrors to `editora-session.log` in your config folder, so it survives
  a crash for a bug report.

The **message log** (click the status-bar message, or `view.messageLog`) keeps a
session history of the transient status-bar messages.

## After a crash or an interrupted save

- **Unsaved edits.** If Editora did not close normally (a crash, a kill, a
  logout, a power cut), the next launch opens **Recover Unsaved Edits** with the
  text of every tab that had unsaved changes. See
  [crash recovery](/docs/undo-history#crash-recovery). Reopen the list any time
  with `file.recoverUnsavedEdits`.
- **A save that did not finish.** Some files are overwritten in place rather
  than replaced. While that happens their previous contents are held in
  `save-backups/` in the config folder. If Editora stops in the middle, the next
  launch shows an **Unfinished Save** dialog that names the backup and offers
  **Restore Previous Contents**, **Keep Current File**, or **Decide Later**.
  Keep Current File deletes the backup. When the file itself is gone, the
  backup is the only copy left and that button reads **Delete Backup**.
- **Save as Administrator.** The file is backed up beside itself
  (`<name>.editora-backup`) before it is overwritten, and put back if the write
  fails. If it cannot be put back either, the status bar names the backup, and
  the next administrator save of that file is refused until you restore or
  remove it.
- **A refactoring that moves or deletes files.** If Editora is interrupted in
  the middle of one, opening the project again shows an **Interrupted
  Refactoring** dialog that lists the files that were moved aside and offers to
  put them back.
- **A damaged config file.** A config file that is empty or unreadable at
  startup is copied to `<name>.corrupt.bak` and reported in the status bar
  rather than reset silently. See
  [Configuration](/docs/configuration#safety-copies).

## Two editors show different settings

Two Editora processes on the same config folder (the second started with
`--new-instance`) do not overwrite each other's data, but each one reads the
other's changes only when it starts. Restart the one that looks stale. See
[Configuration](/docs/configuration#two-editors-on-one-config-folder).

## Settings sync shows "Sync ⚠"

The status-bar segment means the last automatic sync failed; click it to open
**Settings → Sync**, where the Status row gives the reason. Automatic syncs
never prompt for credentials, so a repository that needs a password or an SSH
passphrase fails until a credential helper or an SSH agent can supply it. Run
**Settings Sync: Sync Now** (`sync.now`) to try with your credential helper. See
[Settings sync](/docs/settings-sync#credentials-and-failures).

## A file shows the wrong characters

Editora reads a file as UTF-8 unless a byte-order mark or an `.editorconfig`
`charset` rule says otherwise. A file in some other encoding still opens without
losing any bytes, but its accented or non-Latin text can look garbled, and the
encoding in the status bar is drawn in the warning colour. Click it and choose **Reopen with Encoding…** with the file's
real encoding (Shift_JIS, windows-1251, GBK and so on). Editora remembers the
choice for that file. See [File encodings](/docs/workspace#file-encodings).

## Performance

Editora aims to stay responsive on large files. Highlighting and the minimap
turn off automatically at about 5 MB, and files of about 50 MB open read-only
with a capped load. A file of 5 MB or more that would not fit in the memory that
is left (typically a second or third very large file) opens the same way, as a
read-only slice of its first part; close other large tabs and reopen it to edit
it. Lines longer than 20,000 characters are not syntax-highlighted, though the
rest of the file is. A file with a line of 10,000 characters or a long run
without spaces (minified code, an embedded image) opens unwrapped even where
[word wrap](/docs/editing#word-wrap) is on; *View: Toggle Word Wrap* wraps it
anyway. Find in a very large file searches in the background and
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
  The previous preferences are first saved as
  `settings.json.before-reset-<date>.bak` in the config folder, and the status
  bar names the file; copy it back over `settings.json` while Editora is closed
  to undo the reset.
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

If you are still stuck, [open an issue](https://github.com/adriandeleon/Editora/issues) and
attach the Debug Log.
