---
title: Configuration
description: The config folder, settings.toml, session state, and how to export or reset it.
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

Use `--dev` to run a development instance that never touches your everyday
settings or session. The live path is shown in **About Editora**. See the
[command-line reference](/docs/cli) for all the flags.

## Files

| File | Holds |
| --- | --- |
| `settings.toml` | Preferences: font, theme, keymap, tab size, view options, auto-save, keybinding overrides |
| `workspace-state.json` | Session: open files, folds, tool-window layout, window geometry |
| `recent-files.json` | Recent files list |
| `projects.json` + `projects/<id>.json` | Project index and each project's session |
| `bookmarks.json` | Bookmarks (per project) |
| `notes.json` | Personal notes (per project) |
| `breakpoints.json` | Debugger breakpoints (per project) |
| `connections.json` | Saved SFTP connections (no secrets) |
| `plugins.json` + `plugins/<id>/` | Enabled plugins and their folders |
| `dictionary.txt` | Your added spell-check words |
| `snippets/<lang>.json`, `templates/*.json` | Your snippets and file templates |

Preferences are **TOML**; session and list files are **JSON**.

### Inside a project

Two files live in the project itself rather than in your config directory, so
they can be committed and shared:

| File | Holds |
| --- | --- |
| `.editora/settings.toml` | Toolchain overrides for this project: which language server to run for a language, and whether to run it |
| `.editora/run-configurations.json` | [Run configurations](/docs/run-debug) exported for the team |

Only toolchain settings can be overridden this way; appearance, keymap and fonts
stay personal, because checking out a repository should not rearrange somebody
else's editor. **Project: Edit Project Settings…** creates the first file with a
commented example. See
[projects](/docs/workspace#settings-a-project-can-commit).

## Preferences (settings.toml)

Edit in the Settings window, or directly:

```toml
fontFamily = "JetBrains Mono"
fontSize = 14
theme = "Editora Dark"
tabSize = 4
autoSave = "afterDelay"
```

`autoSave` accepts `off`, `afterDelay`, or `onFocusChange`. Text zoom is stored
separately as `fontZoom` (adjusted with `C-=` / `C--` / `C-0` or Ctrl+wheel) and
isn't shown in the Settings window.

## Schema versioning

Each structured config file carries a `schemaVersion`. Editora migrates older
files forward automatically on read. A file written by a **newer** Editora than
you're running is backed up to `<name>.v<n>.bak` and defaults are loaded, so an
older build never clobbers a newer config.

## Export and reset

- **Export Configuration…** (Settings → Advanced, or `config.export`) zips the
  active config folder into a timestamped archive in your home directory.
- **Reset to Defaults** (Settings → Advanced) restores defaults while keeping
  your text zoom and keybinding overrides.

## More

- Keymaps and rebinding: [Keymaps & keybindings](/docs/keymaps).
- Themes, fonts, and zoom: [Themes & fonts](/docs/themes-fonts).
- Snippets and templates: [Snippets & templates](/docs/snippets-templates).

Full internals live in the
[README](https://github.com/adriandeleon/Editora/blob/master/README.md).
