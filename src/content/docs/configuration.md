---
title: Configuration
description: The config folder, settings.json, session state, and how to export or reset it.
category: Customization
order: 1
---

Editora keeps preferences and session state in a config folder. Most settings
have a UI in the Settings window; everything underneath is plain text you can
edit by hand.

## Config directory

The folder is chosen by this precedence:

| Source | Location |
| --- | --- |
| `--config-dir <path>` (CLI) | that path |
| `EDITORA_CONFIG_DIR` (env var) | that path |
| `--dev` flag | `~/.editora-dev/` |
| default | `~/.editora/` |

`~` is your home directory; on Windows that is `%USERPROFILE%`, so the default
folder is `%USERPROFILE%\.editora`.

Use `--dev` to run a development instance that never touches your everyday
settings or session. The live path is shown in **About Editora**. See the
[command-line reference](/docs/cli) for all the flags.

### Two editors on one config folder

A second launch normally goes to the editor that is already running on the same
config folder, so one folder has one process. Starting another with
[`--new-instance`](/docs/cli#one-editor-not-two) gives two processes that share
the folder. That is safe: each one writes only what it changed, so settings,
sessions, projects, notes, bookmarks, breakpoints, macros, the dictionary, and
Local History from both are kept.

The second process shows a one-time notice in its status bar. A change made in
one process appears in the other only after that one restarts, and when both
change the same setting or entry, the one saved last wins.
[Settings sync](/docs/settings-sync) runs only in the first process.

## Files

| File | Holds |
| --- | --- |
| `settings.json` | Preferences: font, theme, keymap, tab size, view options, auto-save, keybinding overrides |
| `workspace-state.json` | Session of the no-project window: open files, folds, tool-window layout, window geometry |
| `windows/<id>.json` | Session of each extra no-project window |
| `recent-files.json` | Recent files list |
| `projects.json` + `projects/<id>.json` | Project index and each project's session |
| `bookmarks.json` | Bookmarks (per project) |
| `notes.json` | Personal notes (per project) |
| `breakpoints.json` | Debugger breakpoints (per project) |
| `connections.json` | Saved SFTP connections (no secrets) |
| `plugins.json` + `plugins/<id>/` | Enabled plugins and their folders |
| `dictionary.txt` | Your added spell-check words |
| `macros.json` | Saved keyboard macros (format version 2; an older file is converted when it is read, and its key bindings are kept) |
| `abbreviations.json` | Your abbreviations |
| `trusted-folders.json` | Folders you have trusted to run their build wrappers |
| `snippets/<lang>.json`, `templates/*.json` | Your snippets and file templates |
| `history/` | [Local file history](/docs/workspace#local-file-history) |

Preferences, sessions, and list files are **JSON**. If `settings.json` is absent,
Editora converts an existing `settings.toml` automatically. Once the JSON
replacement has been written safely, the old file is renamed to
`settings.toml.migrated` and kept, because the JSON file does not carry over its
comments.

### Safety copies

These folders and files hold copies Editora makes before it replaces something.
They are created only when needed.

| Path | Holds |
| --- | --- |
| `recovery/` | The text of tabs with unsaved changes, for [crash recovery](/docs/undo-history#crash-recovery). Removed as you save or close |
| `save-backups/` | The previous contents of a file that is being overwritten in place, until that save finishes. One left by an interrupted save is offered at the next launch |
| `line-ending-originals/` | The original bytes of the last 30 files whose [mixed line endings](/docs/editorconfig#files-with-mixed-line-endings) a save rewrote |
| `sync/` | The [Settings sync](/docs/settings-sync) working copy (`sync/repo/`) and copies of files a sync replaced (`sync/backups/`) |
| `settings.json.before-reset-<date>.bak` | The preferences as they were before **Reset to Defaults** |
| `<name>.v<n>.bak` | A file written by a newer Editora, set aside by an older one (see [Schema versioning](#schema-versioning)) |
| `<name>.corrupt.bak` | A config file that was empty, damaged, or not valid UTF-8 when it was read |

A config file that cannot be read is never silently replaced by defaults: the
original is copied to `<name>.corrupt.bak` and the problem is reported in the
status bar at startup.

### Inside a project

Two files live in the project itself rather than in your config directory, so
they can be committed and shared:

| File | Holds |
| --- | --- |
| `.editora/settings.json` | Toolchain overrides for this project: which language server to run for a language, and whether to run it |
| `.editora/run-configurations.json` | [Run configurations](/docs/run-debug) exported for the team |

Only toolchain settings can be overridden this way; appearance, keymap and fonts
stay personal, because checking out a repository should not rearrange somebody
else's editor. **Project: Edit Project Settings…** creates the first file with an
example. An existing project TOML file remains readable and is converted when
you open **Edit Project Settings**. See
[projects](/docs/workspace#settings-a-project-can-commit).

## Preferences (settings.json)

Edit in the Settings window, or directly:

```json
{
  "fontFamily": "JetBrains Mono",
  "fontSize": 14,
  "theme": "Editora Dark",
  "tabSize": 4,
  "autoSave": "afterDelay"
}
```

`autoSave` accepts `off`, `afterDelay`, or `onFocusChange`. Text zoom is stored
separately as `fontZoom` (adjusted with `C-=` / `C--` / `C-0` or Ctrl+wheel) and
isn't shown in the Settings window.

## Schema versioning

Each structured config file carries a `schemaVersion`; in Editora 0.20.0
`settings.json` is at version **117**. Editora migrates older files forward
automatically on read. A file written by a **newer** Editora than you're running
is backed up to `<name>.v<n>.bak` and defaults are loaded, so an older build
never clobbers a newer config. When you return to the newer build, it puts that
copy back, provided the file left in its place still holds only defaults.

## Export and reset

- **Export Configuration…** (Settings → Advanced, or `config.export`) zips the
  active config folder into a timestamped archive in your home directory. Local
  history, `.bak` backups, and installed plugins are included. Runtime files are
  not: downloaded language servers and debug adapters, `jdtls` workspaces, the
  `sync/` folder, the lock files, the MCP endpoint file with its live token, and
  the session log.
- **Reset to Defaults** (Settings → Advanced) resets every preference, including
  the keymap choice, which goes back to Emacs. Text zoom is kept, and your key
  rebinds stay stored with the keymap you made them in, so they apply again when
  you switch back to it. Before anything is reset, the current preferences are
  saved beside `settings.json` as `settings.json.before-reset-<date>.bak`, and
  the status bar names that file; if the copy cannot be written, nothing is
  reset. A Local History limit that is higher than the default is kept, so a
  reset never deletes revisions.

## More

- Keymaps and rebinding: [Keymaps & keybindings](/docs/keymaps).
- Themes, fonts, and zoom: [Themes & fonts](/docs/themes-fonts).
- Snippets and templates: [Snippets & templates](/docs/snippets-templates).
- The same snippets, abbreviations, templates, and dictionary on every
  computer: [Settings sync](/docs/settings-sync).

Full internals live in the
[README](https://github.com/adriandeleon/Editora/blob/master/README.md).
