---
title: Configuration
description: The config folder, settings.json, session state, and how to export or reset it.
category: Customization
order: 1
---

Editora keeps preferences and session state in a config folder. Most settings
have a UI in the Settings window; everything underneath is plain text you can
edit by hand.

## The Settings window

Open it with **View: Settings** (`view.settings`, `Ctrl+,` in the CUA, Sublime
and VS Code keymaps). The sidebar groups the pages; a page whose feature is
still in beta (AI, AI Agent, AI Actions, LSP, Debugging, Remote, Sync, MCP
Server) carries a **Beta** pill in the sidebar and beside its title. The window
fits its 900 px minimum width in every language.

- Changes apply as you make them. There is no OK or Save button, and the footer
  says so. Every setting is also a palette command.
- A text field applies when you press Enter, when you leave it, and when you
  close the window.
- A number field takes a whole number in its range. Any other text stays in the
  field, marked red, with a tooltip that gives the range; the setting keeps its
  value until you fix the text or use the arrows.
- Rows that only matter while a switch is on are greyed out while it is off,
  with the note "Turn on “…” to change these settings."
- A new **Language** on the Appearance page applies after a restart. Until then
  the row says "Restart Editora to apply the new language."

### Search

The search box at the top of the sidebar finds a setting by its title, its
description, its card and page names, the choices of a list, and extra
keywords. Every word you type must appear, in any order. Pages without a match
are greyed out in the sidebar, and rows whose title or description matches are
tinted.

The page with the best match opens. A match in a row's title counts more than
one in its description or keywords, and a whole word more than part of one;
words that name a page (*keymaps*, *build tools*) open that page. Some settings
are found by other names too: *zoom* finds Font size, *line endings* and *crlf*
the EditorConfig row (end of line), *encoding* its charset, *hotkey* and
*keybinding* the shortcuts, *colour scheme* the themes, *soft wrap* the wrapping
rows, and *gutter* the line numbers. A command name finds the Keymaps page and
puts your words in its shortcut filter.

- `Ctrl+F` (`Cmd+F` on macOS) goes to the search box from anywhere in the
  window. Typing a letter or digit while the sidebar has the focus starts a
  search, and Down in the search box goes back to the sidebar.
- Escape clears the search; a second Escape closes the window.
- A command that opens Settings on a page, such as **Snippet: Manage…** or
  **Settings Sync: Set Up…**, clears the search first. Opening the window again
  keeps the last search, selected so that typing replaces it.

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
| `connections.json` | Saved SFTP connections (no secrets), with whether each host may [run commands](/docs/remote) and its tool commands, and which containers and WSL distributions may run a project's tools |
| `plugins.json` + `plugins/<id>/` | Enabled plugins and their folders |
| `dictionary.txt` | Your added spell-check words |
| `macros.json` | Saved keyboard macros (format version 2; an older file is converted when it is read, and its key bindings are kept) |
| `abbreviations.json` | Your abbreviations |
| `trusted-folders.json` | Folders you have trusted to run their build wrappers |
| `snippets/<lang>.json`, `templates/*.json` | Your snippets and file templates |
| `history/` | [Local History](/docs/workspace#local-file-history) |
| `http-history/` | The [HTTP client](/docs/http-client)'s last responses and chosen environment for each `.http` file |

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
| `settings.json.before-reset-<date>.bak` | The preferences as they were before **Reset to Defaults** or **Restore from Backup…** |
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
| `.editora/settings.json` | Toolchain overrides for this project: which language server to run for a language, whether to run it, and the [Bash server's options](/docs/lsp#bash-server-options) |
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
separately as `fontZoom` (adjusted with `C-=` / `C--` / `C-0` or Ctrl+wheel).
While it is not 100%, the Appearance page says so, with a
**Reset zoom** link.

### Editing settings.json by hand

The **Path** link on the Advanced page opens `settings.json` in an editor tab.
Saving it there applies it at once in every window, Settings windows included,
and the status bar says "Applied the saved settings.json." A file that is not
valid JSON applies nothing; fix it and save again. Changes made with another
program apply the next time Editora starts.

Under the path, the Advanced page lists the values that are not used as
written:

- a key that is not a setting: it is ignored and removed at the next save;
- a value of the wrong type: the default is used;
- a value that was changed to fit, such as a font size of -5, used as 6.

### Containers

`containerEngine` is the command Editora runs for a project whose
[tools run in a container](/docs/remote#tools-in-a-container): blank (the
default) means `docker`; `podman` works too, and so does a command line such as
`flatpak-spawn --host docker`. It is the **Container engine**
field in Settings → Remote. `containerBindings` remembers the container you
picked for each project folder with *Containers: Run This Project's Tools in a
Container…*, from folder path to container name; *Containers: Run This
Project's Tools Here* removes the folder's entry. Whether a container or WSL
distribution may run a project's tools is kept in `connections.json`, with the
hosts' answers.

### Rulers and indent guides

These are the rows under **Settings → Editor → Display**:

| Key | Settings row | Default |
| --- | --- | --- |
| `showColumnRuler` | Show rulers | `true` |
| `rulers` | Rulers, such as `80, 120` (blank for none) | `[80]` |
| `languageRulers` | Rulers by language, such as `java: 100; python: 79, 88` | none |
| `showIndentGuides` | Show indent guides | `true` |

`languageRulers` maps a language to its own columns, used instead of `rulers`
for files in that language; an empty list means that language has no rulers.
Columns run from 1 to 1000. An `.editorconfig` `max_line_length` replaces the
first ruler (see [EditorConfig](/docs/editorconfig#rulers-and-max_line_length)).

The palette sets the same values: *Editor: Set Rulers…* (`editor.setRulers`),
*Editor: Set Rulers for This Language…* (`editor.setLanguageRulers`, for the
current file's language; leave it blank to go back to the global rulers),
*View: Toggle Rulers* (`view.toggleColumnRuler`) and *View: Toggle Indent
Guides* (`view.toggleIndentGuides`). Indent guides are faint vertical lines at
each indentation level, carried through blank lines and coloured by the theme;
they are hidden in Zen and Simple UI modes and in
[large-file mode](/docs/troubleshooting#performance).

```json
{
  "rulers": [80, 120],
  "languageRulers": { "java": [100], "python": [79, 88], "markdown": [] },
  "showIndentGuides": true
}
```

### Word wrap

Two rows under **Settings → Editor → Display**, one for code and one for prose
(see [Word wrap](/docs/editing#word-wrap)):

| Key | Settings row | Default |
| --- | --- | --- |
| `wordWrap` | Wrap long lines in code | `false` |
| `wordWrapProse` | Wrap long lines in Markdown and plain text | `true` |

*View: Toggle Word Wrap* (`view.toggleWordWrap`) flips whichever of the two
governs the file in front.

## Schema versioning

Each structured config file carries a `schemaVersion`; in Editora 0.22.0
`settings.json` is at version **126**. Editora migrates older files forward
automatically on read. A file written by a **newer** Editora than you're running
is backed up to `<name>.v<n>.bak` and defaults are loaded, so an older build
never clobbers a newer config. When you return to the newer build, it puts that
copy back, provided the file left in its place still holds only defaults.

## Export and reset

- **Export Configuration…** (Settings → Advanced, or `config.export`) zips the
  active config folder into a timestamped archive in your home directory.
  `.bak` backups and installed plugins are included. Local History is left out,
  because it holds the text of your files rather than configuration. A dialog
  asks before exporting when there is something sensitive to leave out:
  - AI API keys are blank in the exported `settings.json` and its backups
    unless you tick **Include AI API keys**. A key is a billable credential.
  - The HTTP client's response history is left out unless you tick **Include
    HTTP response history**: saved responses hold response bodies and full
    request URLs, which can contain tokens or keys.

  A password or token in the [Settings sync](/docs/settings-sync) repository URL
  is always removed from the export. Runtime files are never included:
  downloaded language servers and debug adapters, the installed ShellCheck and
  shfmt, the Typst CLI, `jdtls` workspaces, the `sync/` folder, the lock files,
  the MCP endpoint file with its live token, and the session log.
- **Reset to Defaults** (the window's footer, or Settings → Advanced) asks
  first, and **Cancel** is the default button. The dialog lists what is reset
  and what is kept, and names the backup it is about to write.
  - Reset: appearance, editor and language settings, the keymap choice (back to
    Emacs), tool and server commands, AI and language-server options, external
    tools, TODO patterns, the toolbar layout, and plugins (turned off).
  - Kept: AI API keys, the Sync connection, saved Remote sites, text zoom, your
    custom shortcuts (stored with the keymap you made them in; shortcuts of
    macros, external tools and run configurations come along to the default
    keymap), snippets, templates, abbreviations, macros, and the personal
    dictionary. A Local History limit that is higher than the default is kept
    too, so a reset never deletes revisions.

  Before anything is reset, the current preferences are saved beside
  `settings.json` as `settings.json.before-reset-<date>.bak`, and the status bar
  names that file. If the copy cannot be written, nothing is reset.
- **Restore from Backup…** (Settings → Advanced, beside **Reset to Defaults**)
  lists those backups, newest first, and puts the one you pick back. The
  settings in force are saved as a new backup first, so a restore can be undone
  the same way.

## More

- Keymaps and rebinding: [Keymaps & keybindings](/docs/keymaps).
- Themes, fonts, and zoom: [Themes & fonts](/docs/themes-fonts).
- Snippets and templates: [Snippets & templates](/docs/snippets-templates).
- The same snippets, abbreviations, templates, and dictionary on every
  computer: [Settings sync](/docs/settings-sync).

Full internals live in the
[README](https://github.com/adriandeleon/Editora/blob/master/README.md).
