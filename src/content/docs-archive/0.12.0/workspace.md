---
title: Projects, windows & files
description: Projects and multi-window, tabs and splits, Zen and Simple UI modes, local file history, and external-change detection.
category: Workspace
order: 1
---

## Projects

A project is a VS Code-style single-folder workspace: a root folder plus its
**own saved session** (open files with carets and pins, the active tab, folds,
the editor-group layout, and tool-window layout). Projects are **on by default**
since 0.10.0; Settings → Workspace turns them off.

| Action | Command | Default key |
| --- | --- | --- |
| Open a folder as a project | `project.open` | `C-x C-p` |
| Switch project | `project.switch` | `C-x p` |
| New project from a template | `project.newFromTemplate` | (palette) |
| Edit this project's settings | `project.editSettings` | (palette) |

The **Project** tool window shows the folder tree with keyboard navigation, a
filter that runs a bounded project-wide filename search, per-file-type icons, and
right-click actions: new file, **new folder**, new from template, rename, reveal,
open terminal, local history, and Git stage/revert. The **project root has its
own menu** too (with rename omitted so you can't move the whole project).
Bookmarks and notes are scoped per project. Closing a project returns you to the
global, no-project session.

It also works like a **mini file manager**: multi-select files and folders with
Ctrl/Cmd- and Shift-click, then **drag them onto a folder** (or the root) to move
them, with open tabs following to the new path; a name conflict is skipped rather
than overwritten, and a folder can't be moved into its own subtree. **Delete**
acts on the whole selection at once.

With no project open, the Project tool window doesn't sit empty: it becomes a
**Current Folder** explorer rooted at the active file's parent directory, and
follows the focused tab as you switch files.

### Starting a project from a template

**New Project From Template** (`project.newFromTemplate`) scaffolds a whole
project rather than pointing Editora at a folder you made yourself: pick a
multi-file [template](/docs/snippets-templates), fill in its variables, choose
where it goes, and the new folder is registered as a project and opened in its
own window. A **Python Project** template ships with it (package layout, a test,
`pyproject.toml`, README and `.gitignore`), and your own multi-file templates
appear in the same picker.

### Settings a project can commit

A project can carry a `.editora/settings.toml` file saying **which language
server to run for a language, and whether to run it**. It overrides your global
preferences for anyone who opens that project, which is what you want when one
repository needs a JDK 17 server and another a JDK 25 one — nobody has to
remember to flip a global preference when switching between them.

**Project: Edit Project Settings…** (`project.editSettings`) creates the file
with a commented example and opens it.

Only **toolchain** settings can be overridden this way. Appearance, keymap and
fonts stay personal, because checking out a repository should not rearrange
somebody else's editor. Run configurations have their own shared file — see
[Run & debug](/docs/run-debug).

## Multiple windows

When projects are enabled, **each project opens in its own window**, with its own
tabs, tool windows, and session. The window's project picker acts as a window
switcher: choosing a project focuses or opens that window. The set of open
windows is remembered and restored on the next launch. With projects disabled,
Editora stays a single window.

## Tabs

Tabs are draggable to reorder and can be **pinned**. The tab strip, the
switcher, and the Open Files picker all show the same unsaved-file marker. Close
the last tab and the editor is left empty (it doesn't recreate an Untitled
buffer).

## Editor groups: two files at once

The editor area splits into independent **editor groups**, each with its own
tabs and its own selection, so a header can sit beside its implementation or a
test beside what it tests.

| Action | Command | Default key |
| --- | --- | --- |
| Split the group to the right | `view.splitEditorRight` | (palette) |
| Split the group downwards | `view.splitEditorDown` | (palette) |
| Move the file to the next group | `view.moveToNextGroup` | (palette) |
| Focus the next group | `view.focusNextGroup` | (palette) |
| Merge every group back into one | `view.unsplitEditorGroups` | (palette) |

All five are bindable in Settings → Keymaps like any other command.

- Splits **nest**, so a side-by-side pair can hold a stacked pair and you can
  build an L-shaped layout. Splitting the **same** direction twice widens the
  existing row instead of chaining, which gives you three even columns rather
  than one column and a shrinking remainder.
- **Drag a tab onto another group** to move it there, or onto a group's **edge**
  to split that group and drop the file on that side; a translucent highlight
  shows where it will land.
- Closing the last file in a group **collapses** it, so you never end up looking
  at an empty pane.
- The layout is **saved with the session** and restored on the next launch. A
  file that has since disappeared no longer leaves a blank pane behind.

### Two views of one file

Separately from editor groups, you can split the *current file* into two views
of the same buffer — useful for reading one part while editing another. The two
can be combined.

| Split | Command | Default key |
| --- | --- | --- |
| Side by side | `view.splitVertical` | `C-x 3` |
| Stacked | `view.splitHorizontal` | `C-x 2` |
| Unsplit | `view.unsplit` | (palette) |

These buttons are disabled on tabs that aren't text buffers — the Welcome and
Doctor pages and the image, hex, PDF and diff viewers.

## Tool window layout

Tool windows live on three stripes — left, right, and bottom — and you can
rearrange them without leaving the keyboard or reaching for a preference.

| Action | Command | Default key |
| --- | --- | --- |
| Maximize / restore the tool window | `view.maximizeToolWindow` | (palette) |
| Float it into its own window | `view.floatToolWindow` | (palette) |
| Open a second window on one side | `view.splitToolWindow` | (palette) |
| Close a focused tool window | (any tool window) | `M-g` |

Maximize and float also have a button each in the tool window's header, and the
header's right-click menu carries all three. Each acts on the focused tool
window, or on the only open one; with two open and focus elsewhere, the command
says so rather than guessing.

- **Maximize** expands a tool window over its split and toggles back. It is
  session-only and deliberately never saved, so a maximized window can't reopen
  next launch covering the editor.
- **Re-dock by dragging its stripe button** onto another stripe. Dropping it on
  an existing button inserts it before or after; dropping it on empty stripe
  space appends it to that side. A window that was open stays open on its new
  side.
- **Two windows can share a side** — Project over Structure, say. Use **Open in
  Split** from the stripe button's right-click menu, or `view.splitToolWindow`,
  which lists only the windows that could actually join a side right now. Left
  and right split vertically; the bottom splits **horizontally**, which is the
  only way two consoles side by side both stay readable. A third window evicts
  the companion, not the primary. Opening a tool window normally still
  *replaces*, so the stripe buttons, keybindings, and palette behave as before.
- **Floating** puts a tool window in its own window, owned by the editor — it
  floats above it, minimizes with it, and closes with it. Bounds are remembered,
  and reused only when they still overlap a screen, so a window saved on a
  since-detached monitor is re-centred rather than opened somewhere you can't
  reach. Closing a floating window closes the tool window; reopening it from the
  stripe docks it again.

Each tool window remembers **its own size**, rather than sharing one number per
side, and the whole layout is saved with the session.

## The Welcome page

With no session to restore, Editora opens a **Welcome** page (a real tab) with
New / Open / Open Folder / Clone actions (each labeled with its shortcut), your
recent files, and version and license info. Reopen it with `view.welcome`.

## Focus modes

- **Zen mode** hides the chrome for distraction-free writing, with a small
  floating "Z" to exit. Toggle it from the palette or start with the `--zen`
  flag.
- **Expert mode** is a lighter focus mode than Zen: it strips only the window
  chrome (toolbar, tab bar, breadcrumb, tool stripes, whitespace guides) but
  **keeps the full editor view**, line numbers, status bar, minimap, column
  ruler, and current-line highlight, so you stay oriented. Toggle it from the
  palette (`view.toggleExpert`), `C-c C-e`, Settings → Interface → Modes, the
  floating "E" button, or the `--expert` flag. Expert and Zen are mutually
  exclusive, and like Zen it's per-window and never changes your saved settings.
- **Simple UI mode** strips the editor to the essentials: it hides the extra
  toolbar groups, the tool-window stripe, the breadcrumb, the gutter, and the
  minimap, and turns off the heavier features (LSP, debugging, Git, multiple
  cursors) for a calm surface. Toggle it from Settings, the toolbar, the palette
  (`view.toggleSimpleMode`), or the `--simple` flag (session-only). Toggling off
  restores everything.

You can also hide the toolbar, the tool stripe, the breadcrumb, and the minimap
individually in Settings.

## Local file history

Editora snapshots your local files over time, independent of any version
control, so you have a safety net even outside Git. A snapshot is taken on save,
on auto-save, and before a file is reloaded after an external change.

The **File History** tool window (`M-g l`) lists each revision with its date,
the reason it was taken, and its size (the latest tagged *Current*).
Double-click a revision for a diff against the current file, then restore the
whole revision or use the **apply-chevrons to copy individual fragments** back in
(undoable).

It mirrors more of IntelliJ's Local History:

- **Named snapshots** with *Put Label* (`history.putLabel`), shown bold in the
  list even when the content is unchanged.
- A **filter** over the revision list, plus a project-wide **Recent Changes**
  picker (`history.recentChanges`).
- A **folder view**: right-click a folder in the Project tree to list every file
  under it that has history, with **deleted files badged**; restore a revision to
  recreate the file. Deleting a file in Editora snapshots it first, so an
  accidental delete is recoverable (for files Editora had opened or edited).

Snapshots are deduped by content and stored gzip-compressed in your config
folder, pruned by configurable limits. It's on by default, local-only, and off in
Simple UI mode.

## External-change detection

When a file changes on disk under you, Editora notices on window focus and tab
switch and prompts to reload or keep your version. The Project tree also
re-scans on focus so files added or removed outside the editor show up, keeping
your expanded folders and selection.

## Trusted folders

A repository can ship its own build wrapper, `./mvnw` or `./gradlew`, and those
scripts live in the repository rather than on your machine. Triggering a Maven
or Gradle build runs one with your privileges, so a repository you merely cloned
and opened could execute its own code on the first build.

Editora asks first. The first time you build in a folder it hasn't been told to
trust, it shows what would run and waits. Trust is remembered **per folder and
inherited by subfolders**, so a multi-module repository asks once, and the
default is always untrusted, including when the trust file can't be read.

Declining means **no build**, not a quieter one. Falling back to the `mvn` or
`gradle` on your `PATH` would not be safer: a hostile `pom.xml` or
`build.gradle` executes code through those too, since build plugins and Gradle
scripts run in-process.

Only the wrapper case is gated. Every other build tool (npm, Cargo, Go) launches
your own toolchain, and Run and Debug always invoke a `PATH` interpreter, so
none of them prompt.

Review or revoke trust in **Settings → Workspace → Trusted Folders**, or from
the palette with `workspace.manageTrust` and `workspace.revokeTrust`. Revoking a
folder that is still covered by a trusted parent tells you so, rather than
claiming a revoke that the parent would override.
