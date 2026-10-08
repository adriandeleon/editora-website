---
title: Command line
description: Command-line flags for opening files, projects, config folders, and the focus modes.
category: Help
order: 2
---

Editora takes a few command-line arguments. With the native installers the
launcher binary accepts them; from source, build the jar and pass them to it:
`java -jar target/Editora-*.jar <flags>`.

## Where is the launcher?

The examples on this page write the launcher as `editora`. Whether that command
exists depends on how you installed:

| Package | Launcher |
| --- | --- |
| **Linux `.deb`** | `editora` on your `PATH` (a `/usr/bin/editora` link to `/opt/editora/bin/Editora`) |
| **Linux `.tar.gz`** | `editora` on your `PATH` after `./install.sh` (`~/.local/bin/editora`, or `/usr/local/bin/editora` with `sudo`); or run `./Editora/bin/Editora` in place |
| **Linux `.rpm`** | `/opt/editora/bin/Editora`; no `editora` command is added, so add your own symlink if you want one |
| **Linux `.AppImage`** | The `.AppImage` file itself; nothing is added to your `PATH` |
| **macOS `.dmg`**, **Windows `.msi`** | The installed application's own launcher; no `editora` command is added to your `PATH` |
| **Fat jar** | `java -jar Editora-<version>-<platform>.jar` (needs JDK 25) |

Where there is no `editora` command, substitute the launcher in the examples.

## Flags

| Flag | Effect |
| --- | --- |
| `--version`, `-V` | Print the version and exit (no GUI) |
| `--help`, `-h` | Print usage and exit (no GUI) |
| `--config-dir <path>` | Use this config folder |
| `--dev` | Use an isolated `~/.editora-dev/` config |
| `--project <dir>` | Open this folder as a project (if projects are enabled) |
| `--new-file[=name]` | Open a new untitled buffer (optionally named) |
| `--zen` | Start in Zen mode (session-only) |
| `--expert` | Start in Expert mode, a lighter focus mode (session-only) |
| `--simple` | Start in Simple UI mode (session-only) |
| `--single-window[=project]` | Open just one window, not the whole saved set (session-only) |
| `--no-session` | Open only the files given here; don't restore the saved session |
| `--new-instance` | Start a separate editor instead of handing the files to the running one |
| `--diff-ui LEFT RIGHT` | Open files or folders in an isolated comparison workspace |

`--single-window` opens exactly one window instead of restoring every window
that was open at last quit: bare, it opens the no-project window; with a name
(`--single-window=MyProject`), that project's window (falling back to no-project
if no project matches). It's session-only, so your saved multi-window layout is
untouched and the next normal launch restores everything.

`--no-session` skips the saved session's files entirely and opens only what you
named on the command line. It's for launching from a file manager or a script,
where restoring the last session only costs time: every restored file is a
buffer to load and highlight and, once shown, a language server to run, for
files you didn't ask to see. It is also session-only, so your saved tabs are
left as they were and are not replaced by the files you opened.

## Opening files

Pass one or more file targets, each optionally with a line and column:

```
editora path/to/file.txt
editora src/Main.java:42
editora notes.md:10:5
```

Each target opens in its own focused tab and jumps to the given position. File
targets, `--project`, and `--new-file` combine, so you can open a project and
jump into a file in one command.

## One editor, not two

If Editora is already running, a launch that only opens files **hands them to
the running editor and exits**. The running process is reused, with its language
servers and its memory, and no second editor is started.

The files open in a **new window**, brought to the front, with any focus mode
you asked for (`--expert`, say) applied to it, so a desktop entry like "Editora
Expert Mode" still opens in Expert Mode. The window you were working in is left
alone: the files do not arrive as tabs in it, and its chrome is not restyled.

The exception is a file that is **already open**. The window holding it is
brought forward instead of opening it twice, because two independent buffers
over one file lose edits: saving one leaves the other stale.

These windows are left out of your saved layout, so a file opened from the file
manager doesn't come back as an empty window on the next launch.

The handoff exists because on Linux and Windows a file manager passes the path
as a command-line argument, which starts a new process. Without the handoff,
clicking a file pays a full cold start and leaves a second editor resident.
macOS does not have the problem, because Finder delivers an event to the running
app, and the handoff routes into that same code path.

The handoff is narrow. Only a launch that does nothing but open files is handed
over; these always get their own editor:

- `--project`, `--new-file`, `--config-dir`, `--dev` and `--diff-ui`, which shape how a
  *process* starts and cannot apply to an editor that's already running
- a launch with **no files at all**
- anything passing **`--new-instance`**

An instance is scoped to its **config directory**, so a `--dev` launch can never
hand off to your real editor, and two `--config-dir` sessions stay independent.
If the handoff fails for any reason, the launch starts its own editor.

## Opening files from the file manager

Editora registers itself as a text editor with the desktop, so it shows up where
your file manager offers a choice of application. What the installer can claim
differs by platform:

| Package | Registration |
| --- | --- |
| **Windows `.msi`** | Editora appears under Explorer's *Open with* for the text and source types it edits |
| **Linux `.deb`** | An *Open With* entry and an `editora` command on your `PATH`; it also sets *Editora Expert Mode* as the system default text editor, which your own per-user choice still overrides |
| **macOS `.dmg`** | Finder's *Open With*, via the app bundle's declared document types |
| **Linux `.rpm`** | No association — run `/opt/editora/bin/Editora`, or open files from inside Editora |
| **Linux `.tar.gz`** | `install.sh` adds an application-menu entry and the `editora` command |
| **Linux `.AppImage`** | Nothing is registered — the file runs in place |

Windows hands a file manager's chosen file to an application as a command-line
argument, and nothing maps an extension to Editora unless the installer says
so. Before 0.13.0 the MSI registered nothing, so no file manager could hand a
file to the installed editor.

**It doesn't take over your existing defaults.** On Windows 8+ the shell's
per-user choice wins over anything an installer writes, so a type you already
open with something else keeps opening with it and Editora is offered
alongside. An extension nothing else claims does open with Editora, which is the
intended result for types such as `.tfvars`.

The list is every extension Editora resolves to a language, minus `.html`,
`.htm`, `.xhtml` and `.svg`, which are left to the browser and the image viewer.
The Linux package makes the same choice, so the two installers claim the same
types.

## Examples

```bash
# A throwaway instance that won't touch your real config
editora --dev

# Open a folder as a project and jump to a line
editora --project ~/code/app src/main/java/App.java:88

# Start a quick scratch buffer in Zen mode
editora --new-file=scratch.md --zen

# Open a file in a second editor rather than the running one
editora --new-instance notes.md

# What a desktop "Open With" entry passes: just these files, no saved session
editora --expert --single-window --no-session README.md
```

`--zen`, `--expert` and `--simple` only affect the current session; they don't change your
saved preferences. The config-folder flags are documented in
[Configuration](/docs/configuration).
