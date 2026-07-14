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

`--single-window` opens exactly one window instead of restoring every window
that was open at last quit: bare, it opens the no-project window; with a name
(`--single-window=MyProject`), that project's window (falling back to no-project
if no project matches). It's session-only, so your saved multi-window layout is
untouched and the next normal launch restores everything.

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

## Examples

```bash
# A throwaway instance that won't touch your real config
editora --dev

# Open a folder as a project and jump to a line
editora --project ~/code/app src/main/java/App.java:88

# Start a quick scratch buffer in Zen mode
editora --new-file=scratch.md --zen
```

`--zen` and `--simple` only affect the current session; they don't change your
saved preferences. The config-folder flags are documented in
[Configuration](/docs/configuration).
