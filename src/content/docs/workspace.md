---
title: Projects, windows & files
description: Projects and multi-window, the Project tree and Project Map, tabs and splits, Zen and Simple UI modes, Local History, and external-change detection.
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
open terminal, Show Local History, and Git stage/revert. Typing in the filter
highlights the first match, so Enter opens it and Down moves on from there. The **project root has its
own menu** too (with rename omitted so you can't move the whole project).
From the keyboard, **F2** renames and **Delete** deletes the selected file, and
the **Menu key** or **Shift+F10** opens the selected row's context menu (also in
the Bookmarks, Notes, TODO, Git Log, and Structure tool windows). Renaming or
moving a file or folder keeps its bookmarks, personal notes, breakpoints, fold
state, and other per-file settings.
Bookmarks and notes are scoped per project. Closing a project returns you to the
global, no-project session.

A folder on another machine can be a project too: connecting to it over SFTP
opens it as a project in its own window. See
[Remote folders as projects](/docs/remote#remote-folders-as-projects).

**Delete** moves files to the system trash on Linux and macOS, and the
confirmation says which will happen. Windows deletes permanently, as does any
file the trash cannot take; a permanent delete asks with **Cancel** as the
default button.

Drag files and folders onto a folder to move them. Dragging a single file moves
it at once; dragging a **folder** or **several items** asks first. **Undo Move**
in the tree's right-click menu puts the last drag-move back.

Files open in any editor tab carry an accent marker in the tree; the active file
uses a stronger marker. A file or folder menu can also add a first-line bookmark
or Personal Note directly, with compact indicators showing both annotations.

### Project Map

The Project Map shows the project on a spatial canvas, in a **window of its
own**. Click **Map** beside the Project tool window's search field, or run
*Project: Open / Close Project Map Window* (`project.toggleMapView`), to open
it; if it is open already it comes forward. The command works even with the
Project tool window closed, and closes the Map window when that is the window
in use. The Project tool window keeps showing the tree.

The map shows the project as Miller-style columns: selecting a folder reveals
its children in the next column, so the path to the current file stays visible
without mixing unrelated expanded branches together.

The Map window is resizable and can sit beside the editor window or on another
screen. It does not stay above the editor window: opening a file from the Map
brings the editor window forward. It closes with its editor window or project.
Whether the window was open, and where, is remembered per workspace, so a
workspace that last had the Map open reopens it.

- **Click a file** to open it in an editor tab. **Click a folder** to select it
  and show its column; clicking a folder that is already open selects it and
  brings its column into view. A folder's **chevron** expands or collapses it,
  and a column's **×** closes that branch.
- Choose left-to-right (the default), right-to-left, top-to-bottom, or
  bottom-to-top flow. Connector direction and arrow-key navigation change
  together, and the flow is remembered per workspace.
- Drag empty canvas space to pan and use the mouse wheel to zoom around the
  pointer; `Shift` or `Alt` with the wheel pans instead. **Fit**, **Center
  selection**, **Reset**, and the overview recenter the layout.
- Drag a column header to reposition it and use its lock to prevent accidental
  movement. Columns size themselves to the longest loaded name.
- Filter globally by open, modified, Git-changed, bookmarked, or Personal Notes
  state and by file type. Matches and the folders that contain them stay
  prominent while everything else fades.
  Every non-root column also has a fuzzy name filter and a **Show hidden**
  checkbox.
- Click a file's **eye** for a movable, resizable, syntax-highlighted read-only
  preview card. The preview uses unsaved buffer text when available and follows
  later edits; **Open** promotes it to a normal editor tab.
- Click a file's **Structure icon**, just before the eye, to open a column of
  its classes, methods or headings. See [File structure](#file-structure).
- A Personal Notes badge opens an independent editable note card for a file or
  folder. Note cards can coexist with code previews.
- Right-click a node for the same New, Maven, rename, delete, reveal, terminal,
  Local History, and Git actions available in the tree. Rename, New Folder,
  Delete, Undo Move and Reveal stay in the Map window and ask there; the other
  actions bring the editor window forward.
- The **?** button lists the mouse and keyboard controls in a popover.

The **options (⋯) menu** holds **Keep current zoom**, **Focus new column**,
**Hide all open Personal Notes**, **Print…** and **PDF…**. Keep current zoom and
Focus new column are on by default and remembered per workspace, so opening a
folder preserves the current scale and brings its new column into view. Turn
either off independently; turning zoom preservation off restores fit-to-content
when the map expands.

Hidden (dot) files follow **Settings → Workspace → Show hidden files in the
project tree**. A column's **Show hidden** checkbox overrides the setting for
that folder only.

Preview and note cards sit on the map like columns: panning carries them along,
zooming scales them with the columns around them, and you can drag a card past
the edge of the window and pan back to it later. A card's resize corner stays
under the pointer at any zoom. Each card also keeps a live connector to its
source row. Image previews support common bitmap formats. A text or image card's
own **−** and **+** buttons size its content on top of the map's zoom, so a card
can stay readable when the map is zoomed out. At most **eight cards** are open
at once, previews and notes together; opening a ninth replaces the one used
least recently.

The Map window has its own search field at the top (the Project tool window's
field filters the tree only). A search reveals all the ancestor columns needed
to reach its matches and selects the first one, and clearing the query restores
the folders you had open.

#### File structure

A file whose language the **Structure** tool window can outline (Java, Python,
JavaScript and TypeScript, Kotlin, Markdown headings, Typst, `.http` requests,
Clojure, Maven poms and more) has a **Structure icon**, three stepped lines,
just before its eye. Click it to open a **structure column** next to the file:
its declarations, in the order they are written, as the Structure tool window
lists them. A declaration that holds others, such as a class's members or a
heading's subsections, has its own chevron and opens a further column, like a
folder. A file with a single top-level class opens that class's members right
away. Click the icon again, or the column's **×**, to close it.

**Click a declaration** to open a preview card of **only its lines**: its block,
with the doc comment and annotations written above it. Its line numbers are the
file's own, in the amber the editor uses for a narrowed file, and the card
title names the declaration, for example *App.java › add()*. In the card:

- **Open** opens the file at the declaration.
- **Open Narrowed**, in the card's right-click menu, opens the file narrowed
  to the declaration's block, as the Structure tool window's
  [Narrow to fold region](/docs/editing#the-emacs-editing-model) toggle does.

A declaration row's own right-click menu has **Preview**, **Open at
Declaration** and **Open Narrowed**.

Several declarations of one file can be open side by side, within the
eight-card limit. The columns and cards follow your edits. A file that is open
in a tab is read from its unsaved text, using the language server's symbols
when the Structure tool window has them. A closed file is read again when it
changes on disk. A card whose declaration was removed keeps its last lines and
says so. Structure columns are included when the map is printed, and they are
not remembered when Editora restarts.

#### Large folders

A column shows up to **300 rows** of a folder and ends in a **+N more…** row
that loads the next 300. Its header reads *shown/total* whenever a filter or a
limit hides rows. The map as a whole shows at most **1,200 items**; when that
limit is reached the status bar says so, and closing a column makes room. An
empty or unreadable folder opens a column that says so.

#### Keyboard

The canvas is fully keyboard-navigable.

| Key | Result |
| --- | --- |
| Arrow keys | Move along the flow (into a folder, a file's structure or a declaration; back to the parent) and among siblings |
| `Ctrl-N` / `Ctrl-P` | Select the next or previous sibling |
| `Page Down` / `Page Up` | Move ten rows, stopping at the end of the column |
| `Enter` or `Space` | Open the selected file or a declaration's card, or expand or collapse the selected folder |
| `Backspace` | Select the parent |
| `Home` | Select the project root |
| `Alt-Left` / `Alt-Right` | Go back or forward through earlier selections |
| `/` | Focus the current column's filter; `Enter` there returns to the map on the first match |
| `Ctrl-0` (`Cmd-0` on macOS) | Fit the whole map |
| `F2` | Rename the selected file or folder |
| `Delete` | Delete the selection, as in the tree |
| Menu key or `Shift-F10` | Open the selected row's context menu |
| `Escape` | Close open cards, then clear the search, then return to the editor window |

These keys reach the map in every keymap while it has focus, including the
chords a keymap binds to something else, such as `Ctrl-N` or `Ctrl-0`.

#### File management

The map also works like a **mini file manager**: multi-select files and folders with
Ctrl/Cmd- and Shift-click, then **drag them onto a folder** (or the root) to move
them, with open tabs following to the new path; a name conflict is skipped rather
than overwritten, and a folder can't be moved into its own subtree. **Delete**
acts on the whole selection at once. Dirty files receive one
Save/Discard/Cancel decision before the batch begins, and Editora records
recoverable Local History before removing anything it has opened or edited.

#### Printing the map

**Print…** and **PDF…** in the options menu, or *Project Map: Print…*
(`projectMap.print`) and *Project Map: Export to PDF…*
(`projectMap.exportPdf`), output the **whole map**, not only the part on screen,
with the current flow, filters, open branches and column positions. The output
is light whatever the theme. A map that is wider than tall goes on a landscape
page. A map too large to fit one page with readable labels is split across
several pages, and the status bar says when the map was scaled down or split.
Labels are drawn as an image, so the PDF's text is not searchable. See
[Print & PDF export](/docs/print-pdf) for the preview and the Save dialog.

With no project open, the Project tool window becomes a
**Current Folder** explorer rooted at the active file's parent directory, and
follows the focused tab as you switch files.

### Starting a project from a template

**New Project From Template** (`project.newFromTemplate`) scaffolds a whole
project rather than pointing Editora at a folder you made yourself: pick a
multi-file [template](/docs/snippets-templates), enter a **project name** and a
**location**, and fill in the template's variables. The dialog shows the folder
it will create; a folder that already exists and is not empty is refused.
Editora creates the folder, registers it as a project, and opens it in its own
window on the template's main file. Two project templates ship with Editora:
**Python Project** (package layout, a test, `pyproject.toml`, README and
`.gitignore`) and **Node.js TypeScript Project** (a module, a `node:test` test,
`tsconfig.json`, README and `.gitignore`). Every other multi-file template, yours
included, appears in the same picker.

### Settings a project can commit

A project can carry a `.editora/settings.json` file with values that override
your own for anyone who opens that project, so they travel with the repository:

- how its files are edited: indent style, tab size, rulers (also by language),
  wrapping for code and for prose, trimming trailing whitespace and ending
  files with a newline on save, and the spell-check language;
- the command each build tool runs, and the program of the Java, Python and
  JavaScript debuggers;
- **which language server to run for a language, and whether to run it**, and
  the Bash server's options such as ShellCheck's arguments and shfmt's style
  (see [Bash server options](/docs/lsp#bash-server-options)).

One repository can then use two-space indentation and a JDK 17 language
server while another uses tabs and a JDK 25 one, and nobody has to flip a
global preference when switching between them.

The values apply only once the folder is trusted: run *Project: Trust This
Project's Settings* (`lsp.trustProjectSettings`), or use **Trust Folder…** in
Settings. A file's `.editorconfig` rules still come before the project's
values.

In a project window, Settings has a **User | Project** switch at the top, and
its Project side names the project. Choose **Project** (the window's title then
names the project too) to change the project's values on the same rows you use for
your own; the change is written to `.editora/settings.json`. In **User**, a row
the project sets says "Overridden by this project", and in **Project** it says
"Set for this project" and offers **Use my setting**.

**Project: Edit Project Settings…** (`project.editSettings`) creates the file
with an example and opens it. Appearance, keymap and fonts stay personal,
because checking out a repository should not rearrange somebody else's editor.
The whole list, the file's keys and the trust rules are in
[Configuration](/docs/configuration#inside-a-project). Run configurations have
their own shared file, described in [Run & debug](/docs/run-debug).

## Multiple windows

When projects are enabled, **each project opens in its own window**, with its own
tabs, tool windows, and session. The window's project picker acts as a window
switcher: choosing a project focuses or opens that window. The set of open
windows is remembered and restored on the next launch. With projects disabled,
Editora stays a single window.

Everything in a window acts on that window's project, whichever window you used
last: Git, *Project: Edit Project Settings…*, exporting and importing run
configurations, where a new file is offered and where the file finder starts, a
template's `${projectName}`, snippet variables such as `$WORKSPACE_FOLDER`, the
AI agent's working folder, and finding the files of a stack trace. A recent
file that belongs to another open project opens in that project's window.

Launching Editora again while it is running hands the launch to the running
editor instead of starting a second process. Start with `--new-instance` when
you want a separate one; see the [command line](/docs/cli).

## Tabs

Tabs are draggable to reorder and can be **pinned**. The tab strip, the
switcher, and the Open Files picker all show the same unsaved-file marker. Close
the last tab and the editor is left empty (it doesn't recreate an Untitled
buffer). Two open files with the same name show their folder in the tab
(`pom.xml · core`, `pom.xml · web`); the suffix appears when the second opens
and goes when it closes.

A file you're only *glancing* at, for example while arrowing through a picker
or following a definition, lands in a single reusable **preview tab**, shown in italic, so
browsing costs one tab rather than one per glance. Editing it, or opening it
explicitly, makes it permanent. See
[preview tabs](/docs/code-navigation#preview-tabs).

Closed a tab by mistake? **Buffer: Reopen Closed Tab** (`buffer.reopenClosed`)
brings back the file whose tab you closed last, with the caret where it was.
Run it again for the one before, up to the last 20 tabs closed in the window
during this session. A file deleted since is skipped, and a file that is open
again is simply focused. It counts the tabs you close yourself (the close
command, *Close Others* and the other tab-menu closes, or a tab's **×**), not the
ones that go when a window closes or you switch projects, and it brings back
files only, not diff or preview tabs. It is `Ctrl+Shift+T` (`⇧⌘T` on macOS) in
the VS Code, Sublime Text and CUA keymaps, and it is in the **File** menu
under Open Project. IntelliJ IDEA's `Ctrl+Shift+T` stays Go to Test, so there it is a
palette command.

## Saving and reverting

| Action | Command | Default key |
| --- | --- | --- |
| Save every modified file in the window | `file.saveAll` | `C-x s` |
| Throw away unsaved changes and read the file again | `file.revert` | (palette) |

**File: Save All** saves every modified file open in the window, in all its
editor groups, exactly as *File: Save* would for each one, so a file that needs
administrator rights, the Markdown on-save fixes and the check for a file changed
on disk all apply. Untitled buffers are skipped. When it is done, one line in the
status bar gives the count: how many were saved, how many could not be, and how
many untitled buffers were skipped. In VS Code's keymap it is `Ctrl+K S` (`⌥⌘S`
on macOS), and it sits in the **File** menu under Save As.

**File: Revert File** reads the file again from disk. It asks first only when
that would discard unsaved changes, and [Local History](#local-file-history)
keeps a copy of them. The caret, scroll position and folds stay where they were,
and the file is read in the [encoding chosen for it](#file-encodings), if any. An
untitled buffer or a file that no longer exists on disk has nothing to revert to,
and the status bar says so. It is in the **File** menu under Save as
Administrator.

## File encodings

Editora reads a file as UTF-8 unless a byte-order mark or a `charset` rule in an
[`.editorconfig`](/docs/editorconfig) says otherwise. A file that is not valid in
that encoding still opens without losing a byte: it is shown through a
single-byte stand-in (Windows-1252 or ISO-8859-1), and the status bar draws its
encoding in the **warning colour**, with a tooltip saying the real encoding is
unknown.

Click the encoding in the status bar (*Buffer: Change Encoding…*,
`buffer.chooseEncoding`) to fix it, or use the two commands it offers from the
palette:

| Action | Command |
| --- | --- |
| Read the file again in another encoding | `buffer.reopenWithEncoding` |
| Choose the encoding the file is written in | `buffer.saveWithEncoding` |

The list puts about 45 common encodings first, grouped by script (Unicode,
Western, Central European, Cyrillic, Greek, Turkish, Hebrew, Arabic, Baltic,
Vietnamese, Thai, Japanese, Chinese, Korean and DOS), with Shift_JIS, EUC-JP,
ISO-2022-JP, GB18030, GBK, Big5, EUC-KR, windows-1251 and KOI8-R among them,
followed by every other encoding available. The list is the same on every
operating system.

- **Reopen with Encoding…** reads the file from disk again in the encoding you
  pick. If the bytes are not valid in it, nothing changes, and the status bar
  names the first line and column that could not be read. With unsaved changes
  it asks first, and Local History keeps a copy of them.
- **Save with Encoding…** sets the encoding the file is written in from its next
  save, whatever the `.editorconfig` says. The tab shows as modified until you
  save; picking the encoding the file already has on disk makes it clean again.
  If the text holds a character the encoding cannot store, the command refuses
  and names the character and its line, and a save that meets such a character
  later refuses too, rather than quietly writing UTF-8. For a file whose
  encoding Editora could not detect, reopen it in the right encoding first.

The encoding you choose is **remembered for that file**: it is used the next time
you open it, after a restart and when a session is restored, it follows the file
when you rename it, and it works for [remote files](/docs/remote) too. If the
file later stops being valid in that encoding, Editora forgets the choice and
says so in the status bar. A crash-recovery copy keeps a choice that had not been
saved yet.

The **File Information** tool window (`tool.fileInformation`) shows the file's
encoding.

None of the three has a default key or a place in the **File** menu; reach them
from the status bar or the palette, and bind them in Settings → Keymaps if you
use them often.

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
  file that has since disappeared does not leave a blank pane behind.

### Two views of one file

Separately from editor groups, you can split the *current file* into two views
of the same buffer, which is useful for reading one part while editing another. The two
can be combined.

Both views share **one undo history**: an edit made in one pane can be undone
from the other, exactly once, and undo moves the caret of the pane it was issued
in.

| Split | Command | Default key |
| --- | --- | --- |
| Side by side | `view.splitVertical` | `C-x 3` |
| Stacked | `view.splitHorizontal` | `C-x 2` |
| Unsplit | `view.unsplit` | `C-x 1` |

These commands are disabled on tabs that aren't text buffers: the Welcome and
Doctor pages and the image, hex, PDF and diff viewers.

## Tool window layout

Tool windows live on three stripes (left, right, and bottom), and you can
rearrange them without leaving the keyboard or reaching for a preference.

| Action | Command | Default key |
| --- | --- | --- |
| Maximize / restore the tool window | `view.maximizeToolWindow` | `C-x f` |
| Float it into its own window | `view.floatToolWindow` | (palette) |
| Open a second window on one side | `view.splitToolWindow` | (palette) |
| Close a focused tool window | `view.closeFocusedToolWindow` | `C-x 0`, or `M-g` |

Maximize and float also have a button each in the tool window's header, and the
header's right-click menu carries all three. Each acts on the focused tool
window, or on the only open one; with two open and focus elsewhere, the command
says so rather than guessing.

- **Maximize** expands a tool window over its split and toggles back. It is
  session-only and never saved, so a maximized window can't reopen
  next launch covering the editor.
- **Re-dock by dragging its stripe button** onto another stripe. Dropping it on
  an existing button inserts it before or after; dropping it on empty stripe
  space appends it to that side. A window that was open stays open on its new
  side.
- **Two windows can share a side**, for example Project over Structure. Use **Open in
  Split** from the stripe button's right-click menu, or `view.splitToolWindow`,
  which lists only the windows that could join a side right now. Left
  and right split vertically; the bottom splits **horizontally**, so that two
  consoles side by side both stay readable. A third window evicts
  the companion, not the primary. Opening a tool window normally still
  *replaces*, so the stripe buttons, keybindings, and palette behave as before.
- **Floating** puts a tool window in its own window, owned by the editor. It
  floats above the editor, minimizes with it, and closes with it. Bounds are remembered,
  and reused only when they still overlap a screen, so a window saved on a
  since-detached monitor is re-centred rather than opened somewhere you can't
  reach. Closing a floating window closes the tool window; reopening it from the
  stripe docks it again.

Each tool window remembers **its own size**, rather than sharing one number per
side, and the whole layout is saved with the session.

**Settings → Tool Windows** lists the windows under **Left**, **Right** and
**Bottom**, in the order their buttons stand on each stripe. Each row has a
switch that shows or hides the window's stripe button, the side it docks on,
and ▲ ▼ to move it one place among the buttons shown on that side; the list
follows every change. The side of a hidden window can be set too. A window
whose feature is off (Project, Commit, Personal Notes, Problems, Debug) has its
row greyed out, with the Settings page that turns it on. The **Layout** row
says that the layout is saved per project (or per window when no project is
open), so other projects keep theirs, and that **Reset to Defaults** does not
change it. Its **Restore Default Layout** button puts every window back on its
own side, shown or hidden as on a fresh install, in the default order.

## The Welcome page

With no session to restore, Editora opens a **Welcome** page (a real tab).
Reopen it with `view.welcome`. From top to bottom it has:

- **Start**: New File, Open File, Open Folder as a Project, Clone Git
  Repository and the Command Palette, each with its shortcut when the keymap
  binds one.
- **Recent Projects**, newest first. Picking one opens or focuses that
  project's window. A project whose folder no longer exists is left out.
- **Recent Files**. Each row shows the file name, the project it belongs to
  (or the host of a remote file) and its folder: relative to the project, or
  starting with `~`. A long folder is shortened from its start. Right-click a
  row to copy its path or remove it from the list.
- **Remote Sites**: your saved SFTP sites; picking one opens the connect form
  filled in.
- **Customize**: the current keymap and theme; click either to change it. With
  the Emacs keymap it also explains the notation (`C-` is Ctrl, `M-` is Alt).
  On a first run, before there is anything recent, this block sits right under
  Start.
- **Help**: Documentation, What's New (the release notes of the version you
  run) and Doctor.

The first nine projects and files are numbered: press **1**–**9** to open one.
When the page opens, the first entry has the focus, so Enter opens it. Long
lists show five entries and a "Show N more" link.

Closing the last tab does not bring the Welcome page back. The editor area
shows a short list instead (Open File, Open Folder, Command Palette, Welcome
Page) with their shortcuts.

## Focus modes

- **Zen mode** hides the chrome for distraction-free writing, with a small
  floating "Z" to exit. Toggle it from the palette (`view.toggleZen`), `C-c z`,
  or start with the `--zen` flag.
- **Expert mode** is a lighter focus mode than Zen: it strips only the window
  chrome (toolbar, tab bar, breadcrumb, tool stripes, whitespace guides) but
  **keeps the full editor view**, line numbers, status bar, minimap, rulers,
  indent guides, and current-line highlight, so you stay oriented. Toggle it
  from the palette (`view.toggleExpert`), `C-c C-e`, Settings → Interface → Modes, the
  floating "E" button, or the `--expert` flag. Expert and Zen are mutually
  exclusive, and like Zen it's per-window and never changes your saved settings.
- **Simple UI mode** strips the editor to the essentials: it hides the extra
  toolbar groups, the tool-window stripe, the breadcrumb, the gutter, and the
  minimap, and turns off the heavier features (LSP, debugging, Git, multiple
  cursors) for a calm surface. Toggle it from Settings → Interface → Modes, the
  toolbar, the palette
  (`view.toggleSimpleMode`), or the `--simple` flag (session-only). Toggling off
  restores everything.

  It **keeps a menu bar**, simplified to File, Edit, Find, View and Help. The
  menus removed are the ones for features the mode switches off (code
  intelligence, running and debugging, version control, tool windows), which
  would otherwise be entirely greyed out. The menu bar stays because it shows
  what the editor can do, and people new to the editor need that most. The
  Simple UI mode toggle stays in the reduced View menu, so anyone who entered
  the mode from there can leave it the same way.

  The right-click menus are trimmed the same way:

  - **Tab:** Save, Save As, Encrypt… (Lock File on an encrypted file), Print,
    Export to PDF, Close, Close Others, Close All, Reveal in File Manager, Copy
    Path and Rename. Unpin stays on a tab that was pinned before.
  - **Editor:** Comment / Uncomment, the spelling suggestions, Cut, Copy,
    Paste, Undo, Redo, Select All, Print and Export to PDF.
  - **Project tree:** New (a file or a folder), Rename, Undo Move, Delete and
    Reveal in File Manager.
  - **Preview** (Markdown, Typst, Markwhen, diagrams): the copy actions, Go to
    Source, Enlarge, Export to PDF and Print. Copy HTML and the other export
    formats are hidden.

  Git, Local History, the compare actions, Open Terminal, Pin, bookmarks and
  notes, External Tools and plugin items, and the Run, Debug, language-server,
  build, AI, Markdown and Typst submenus are left out. Turning Simple mode off brings every entry back.

## Window chrome

Each row of chrome can be hidden on its own under **Settings → Interface →
Window chrome**: the menu bar, toolbar, status bar, tab bar, breadcrumb and
tool-window stripe. (Hiding the tool stripe hides every tool window's button,
whatever the Tool Windows page says; the windows still open from their
keyboard shortcuts and the palette.)

**Merge the menu bar into the title bar** goes one further: the window is drawn
without a system title bar, and the menus, the window title and the system
buttons share that single row, which gives the editor a full bar of vertical
space back. Minimise, maximise, close, drag-to-move, double-click-to-maximise,
edge resize and snapping all stay the platform's own, so the window behaves as
before and only the position of the menus changes. The window drags from the
title in the middle of the bar as well as from its empty parts.

It is **experimental**, off by default, and **applies on restart**, because a
window's style is fixed once it has been shown. **Linux and Windows only.** macOS is
excluded because the menu belongs to the system menu bar at the top of the
screen there, so drawing it inside the window would not make the window any
taller. The checkbox is disabled rather than hidden, so its note still explains
why. Also on the palette as
`view.toggleExtendedWindow`.

The **breadcrumb** sits directly under the editor area and is as wide as the
editor, rather than spanning the whole window above the status bar. It writes your home directory as a single `~` crumb, which still
navigates there. In a Markdown file it continues with the path of headings at
the caret.

In the **status bar**, indent, line endings and encoding are one segment
(`Tab 4 · LF · UTF-8`), since all three describe how the file is written to disk.
They remain three separate click targets with three tooltips, because they run
three different commands, and they appear only when a file is open. The encoding
is drawn in the warning colour when Editora had to guess it; see
[File encodings](#file-encodings). A file whose
lines do not all end the same way reads **Mixed** with its dominant ending, for
example `Mixed (LF)`. Saving such a file unchanged leaves it as it is; saving an
edit writes the dominant ending on every line and says so.

### The toolbar

**Settings → Toolbar** adds, removes and reorders the icons; right-click the
toolbar and choose *Customize Toolbar…* to drag them directly on the bar (drag
one off to remove it). *Restore Default Layout* puts it back.

The bar is two containers on one row. The **icon cluster** is what you
customize, and it is what collapses into the overflow chevron when the window
gets narrow. The **tail** is pinned to the right end and never overflows, the way an
IDE pins its run widget. It holds the run-configuration selector with its Run,
Debug and Stop buttons, the project switcher, Open Folder, the build badges and
Settings. As a result, the selector and its Run, Debug and Stop buttons are not draggable items in
Settings → Toolbar. They are pinned so that a narrow window cannot hide the
control that starts a run, whereas every icon in the cluster is also reachable
from the menus and the palette.

**Recent Files sits in the cluster**, between Save As and Undo. It is a file
action, one of the ways to get a file on screen, and beside the project
switcher it read as a project control. As a catalog item, it can be dragged
elsewhere or removed like any other. A toolbar you had already customized keeps
the button. Because a saved layout is used verbatim, the button is inserted
after Save As so that it does not disappear from both halves.

Its dropdown is as wide as the longest file name, up to a limit, with each
file's project label lined up in one column. Hover a row for the file's full
path. A row's trash icon asks before removing that file from the list, and the
question shows the full path, so two files with the same name are easy to tell
apart.

Cut, copy and paste are not in the default layout, because in a keyboard-driven
editor they are rarely reached for with the mouse. They remain catalog items
and can be dragged back onto the bar. A bar you have already arranged is otherwise
untouched.

## Local file history

**Local History** snapshots your local files over time, independent of any
version control, so you have a safety net even outside Git. A revision is
recorded on save, on auto-save, and before a file is reloaded after an external
change. The first save of a session also keeps the text the file held before
it, listed as *Before first save*, so the version you started from is in the
history too. A save that changes nothing adds no revision, and auto-saves less
than five minutes apart collapse into one, so a run of them does not push
earlier revisions out. [Encrypted files](/docs/encrypted-files) get no
revisions: their text stays out of the history.

The **Local History** tool window lists the active file's revisions, newest
first, under day captions. Each row shows its label if it has one, the time,
and the reason it was recorded; the size and full date are in the tooltip. The
newest row is tagged *Current* when it equals the editor's text and *Latest*
when the editor has moved on (unsaved edits, an undo, or a restore of an older
row).

| Action | Command | Emacs | CUA, Sublime Text, VS Code, IntelliJ IDEA |
| --- | --- | --- | --- |
| Show or hide Local History | `tool.fileHistory` | `M-g l` | Alt+Shift+H (⌃⌘H on macOS) |
| Put Label | `history.putLabel` | (palette) | (palette) |
| Recent Changes | `history.recentChanges` | (palette) | (palette) |

The same three are under **VCS ▸ Local History**, and *Show Local History* is in
a tab's right-click menu and in the Project tree. Git's per-file log is a
separate command, [Show Git History](/docs/git#git-log).

Select a revision for a side-by-side diff against the current file; the diff
follows edits you make in the editor. **Restore** replaces the file's text with
that revision, or use the **apply-chevrons to copy individual fragments** back
in. Either way the change goes into the open tab as an undoable edit and
nothing is written to disk until you save. Restore asks first only when the
tab has unsaved edits.

| Key | Result |
| --- | --- |
| `Enter` | Move into the selected revision's diff |
| `F2` | Set or edit the revision's label |
| `F5` | Reload the list |
| Menu key or `Shift-F10` | Open the row's menu (*Restore This Version*, *Set Label…*) |
| `Tab` | Leave the diff for the filter and the list |
| `Escape` | Clear a typed filter, then return to the editor |

It mirrors more of IntelliJ's Local History:

- **Named snapshots** with *Put Label* (`history.putLabel`), shown bold in the
  list even when the content is unchanged.
- A **filter** over the revision list that matches the words the rows show
  (label, reason, time, and day), plus a project-wide **Recent Changes** picker
  (`history.recentChanges`) that opens the revision you pick, including one of a
  deleted file.
- A **folder view**: right-click a folder in the Project tree and choose *Show
  Local History* to list every file under it that has history, with **deleted
  files struck through and badged**. Selecting a revision shows its diff, and
  Enter or **Restore** puts it back, recreating a deleted file; a file that
  still exists asks before it is overwritten. The view stays up while you
  restore several files, and a file row's menu has *Delete History…* for that
  one file. Deleting a file in Editora snapshots it first, so an accidental
  delete is recoverable (for files Editora had opened or edited).
- History **follows the file**. Renaming or moving a file from the Project tree
  keeps its revisions, and a file created with **Save As** starts with a copy of
  the original's history once its first save is recorded (the original keeps its
  own). The copied revisions count towards the project's size limit.
- **Deleting history for good**: *Local History: Delete History of Current
  File…* (`localHistory.purgeFile`) and *Local History: Delete History of
  Project…* (`localHistory.purgeProject`) remove snapshots permanently, after
  a confirmation that states how many snapshots go.

Restore validates the stored revision and verifies that the file has not
changed while it was loading. If either check fails, the current file and editor
contents are left untouched, and the status bar says which check failed. A copy
of what a restore replaces is stored first.

A file's Local History is the same in every window, whichever project's window
recorded it, and an open panel follows saves, labels, and deletions made in
another window.

Snapshots are deduped by content and stored gzip-compressed in your config
folder, pruned by configurable limits (**Settings → Workspace → Local History**,
or the three commands):

| Limit | Command | Default |
| --- | --- | --- |
| Max revisions per file | `history.setMaxPerFile` | 50 |
| Max age in days; 0 means no age limit | `history.setMaxAgeDays` | 30 |
| Max size per project, in MB of uncompressed text | `history.setMaxTotalMb` | 50 |

The size limit applies to each project and counts content shared by several
revisions once. When a project is over it, a file above its fair share loses
its own older revisions first, so one large file does not push the other files'
history out. A file's newest revision, labelled revisions, and copies taken
before a delete are never removed for size, and are kept six times as long as
the age limit (at least 180 days). Lowering a limit asks first when it would
delete existing history, stating how many snapshots from how many files would
go. It's on by default, local-only, and off in Simple UI mode.

## External-change detection

When a file changes on disk under you, Editora notices on window focus and tab
switch and prompts to reload or keep your version. When the buffer has unsaved
edits, the prompt says so and **Keep Mine** is the default, so pressing Enter
keeps your version; without unsaved edits **Reload** is the default. The Project tree also
re-scans on focus so files added or removed outside the editor show up, keeping
your expanded folders and selection, and a folder is listed afresh each time you
open it.

A file **deleted** outside Editora keeps its tab and its text: the tab is marked
unsaved, the status bar says the file no longer exists on disk, and auto-save
does not write it back. Save it yourself to recreate the file.

A [remote project](/docs/remote)'s folder is watched on its host when that host
[may run commands](/docs/remote#allowing-a-host-to-run-commands), so a
`git pull` or a build there updates the Project tree, Git, language servers and
an open file that changed, as for a local folder. A host with `inotifywait`
reports changes at once; others are checked every few seconds.

A project whose tools run in a container is watched here, since a bind-mounted
folder is this machine's own. A project inside a WSL distribution is watched
from inside the distribution while its tools [run there](/docs/remote#outside-changes-in-wsl),
because Windows cannot watch the `\\wsl.localhost` share.

## Trusted folders

A Maven or Gradle build runs code that lives in the repository rather than on
your machine: a shipped wrapper (`./mvnw`, `./gradlew`), the pom's plugins and
extensions, the files under `.mvn/`, every Gradle build script. A repository
you merely cloned and opened could execute its own code on the first build,
with your privileges, whether or not it ships a wrapper.

Editora asks first. The **first build of any kind** in a folder it hasn't
been told to trust shows what would run and waits: the wrapper, the pom's
plugin coordinates, the `.mvn/` files, the Gradle scripts, and a
`.editora/project.json` build-command override. Trust is remembered for the
folder that ships the wrapper, else the project you opened, and **inherited by
subfolders**, so a multi-module repository asks once and a module never
silently covers a script one level up. The default is always untrusted,
including when the trust file can't be read. The same question fronts
Gradle's *Load all tasks…*, the build before Run and Debug, and Maven's
classpath fallback.

Declining means **no build** runs at all. Editora does not fall back to the
`mvn` or `gradle` on your `PATH`, because that would not be safer: a hostile `pom.xml` or
`build.gradle` executes code through those too, since build plugins and Gradle
scripts run in-process.

Only Maven and Gradle are gated. Every other build tool (npm, Cargo, Go) launches
your own toolchain, and Run and Debug always invoke a `PATH` interpreter, so
none of them prompt.

Review or revoke trust in **Settings → Workspace → Trusted folders**, with the
**Revoke** button in the Maven or Gradle window's header (which shows
*Trusted* or *Not trusted yet*), or from the palette with
`workspace.manageTrust` and `workspace.revokeTrust`. Revoking a folder that is
still covered by a trusted parent tells you so, rather than claiming a revoke
that the parent would override.

A folder on a [remote host](/docs/remote) is trusted on its own: trusting
`/srv/app` on a server does not trust a local `/srv/app`, or the other way round.
