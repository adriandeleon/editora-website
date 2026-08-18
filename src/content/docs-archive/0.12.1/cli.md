---
title: Command line
description: Command-line flags for opening files, projects, config folders, and the focus modes.
category: Help
order: 2
---

Editora takes a few command-line arguments. With the native installers the
launcher binary accepts them too; from source, pass them after `javafx:run` or
the jar.

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

`--single-window` opens exactly one window instead of restoring every window
that was open at last quit: bare, it opens the no-project window; with a name
(`--single-window=MyProject`), that project's window (falling back to no-project
if no project matches). It's session-only, so your saved multi-window layout is
untouched and the next normal launch restores everything.

`--no-session` skips the saved session's files entirely and opens only what you
named on the command line. It's for launching from a file manager or a script,
where restoring the last session is pure cost — every restored file is a buffer
to load and highlight and, once shown, a language server to run, for files you
didn't ask to see. Also session-only: your saved tabs are left as they were
rather than replaced by whatever you happened to open.

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

If Editora is already running, a launch that just opens files **hands them to
the running editor and exits** rather than starting a second one. The files open
in the focused window, and a focus mode you asked for (`--expert`, say) is
applied to it — so a desktop entry like "Editora Expert Mode" still means Expert
Mode.

This is what you want when opening a file from a file manager. On Linux and
Windows a file manager passes the path as a command-line argument, which by
definition starts a new process, so clicking a file used to pay a full cold
start *and* leave a second editor resident. (macOS never had the problem —
Finder delivers an event to the running app — and the handoff routes into that
same code path.)

It is deliberately narrow. Only a launch that is purely *"open these files"* is
handed over; these always get their own editor:

- `--project`, `--new-file`, `--config-dir` and `--dev`, which shape how a
  *process* starts and have no honest meaning inside a window that's already
  running
- a launch with **no files at all**
- anything passing **`--new-instance`**

An instance is scoped to its **config directory**, so a `--dev` launch can never
hand off to your real editor, and two `--config-dir` sessions stay independent.
If the handoff fails for any reason, the launch simply starts its own editor.

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

`--zen` and `--simple` only affect the current session; they don't change your
saved preferences. The config-folder flags are documented in
[Configuration](/docs/configuration).
