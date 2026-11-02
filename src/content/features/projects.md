---
title: "Project Map navigation"
group: "Workspace & files"
order: 1
beta: false
summary: "Explore a project as a file tree or, in a window of its own, a <strong>spatial canvas</strong>: focused columns, pan and zoom, live filters, and connected code and Personal Notes cards."
---

Projects are VS Code-style single-folder workspaces: a root folder plus its **own saved session**, open files (with carets and pins), the active tab, folds, [editor-group layout](/features/editor-groups) and tool-window layout. Each project opens in its own window, and everything in a window acts on its own project. A folder on a remote host over SFTP is a project too, with its own window, tabs and trust (a server folder is trusted separately from a local one with the same path); reopening it asks to connect.

Open one with `C-x C-p`, switch with `C-x p`, and close to return to the global session. Bookmarks, notes and [run configurations](/features/run-configurations) are scoped per project. **On by default** since 0.10.0; Settings → Workspace turns it off.

## Navigate on a canvas

Beside the familiar file tree, the **Map** button opens a **visual Project Map** in a window of its own, which you can place beside the editor window or on another screen; the Project tool window keeps the tree. The map lays the active path out as focused Miller-style columns on a canvas: expand a folder and its children appear in the next column, while the ancestor path stays visible. Choose left-to-right, right-to-left, top-to-bottom, or bottom-to-top flow from the ⋯ menu; the connectors and arrow keys follow the direction you choose.

Pan and pointer-centered zoom make room for large projects, with Fit, Center, and a compact overview when you want your bearings back. Default-on **Keep current zoom** and **Focus new column** options preserve the scale and center each newly opened column; either can be turned off, and the choice is remembered per workspace. Each column sizes itself to its content, can be repositioned and locked, and has its own name filter and "Show hidden" checkbox. On a local project a long folder is listed whole, so a folder of thousands of files scrolls end to end; a remote folder loads 300 rows at a time, with a "+N more…" row for the next chunk. **Collapse All** tidies the map down to the path you are on. Global filters narrow by open, unsaved, Git-changed, bookmarked, or Personal Notes status and by file type without throwing away the surrounding path; **Hide unmatched** takes the rest off the map, with a badge and per-column counts saying how much is hidden.

Click a file to open it in an editor tab, or use the row's eye icon for a movable, resizable **syntax-highlighted preview** over the canvas, including current unsaved text when that file is already open. Common bitmap images preview too, with zoom kept separately from text. A file's Personal Notes badge opens its preview with the notes stuck beside it, and a folder's opens an editable note card; a default-off option temporarily hides all open note cards. Live connectors keep every preview and note card tied to its source row as the canvas pans or zooms and as cards move or resize. The map reuses the tree's file icons and context menu, so New, rename, delete, reveal, terminal, Local History, bookmarks, Personal Notes, Maven, and Git actions work in either view.

Cards are for a glance beside the map. Pin the ones you want to keep, find text inside a card, step a declaration card through its file's declarations, and see who changed the code last. **Show Callers as Cards** and **Show References as Cards** open the places found as cards around the one you asked from, joined by labelled edges, using a language server that is already running or matching the name when none is.

**Show recent changes** warms each row by how often it changed in the last 90 days, and tooltips name a file's owners from `CODEOWNERS` or `OWNERS` files. Save a view of the map as a **working set** to come back to it, or record a **trail**: a guided tour of the code, stop by stop with a note at each, kept in the project so the team can commit it.

The whole map is keyboard-navigable: arrows follow the selected flow and move among siblings, `Ctrl-N` / `Ctrl-P` step through a column, `Backspace` goes to the parent, `Alt-Left` / `Alt-Right` step back and forward through the places you visited, typing a name jumps to it, `/` focuses the column filter, and `Home` / `End` go to the column's first and last rows. The Map window has its own search over the whole project, with a results strip to step through the matches. Opening a file from the map brings the editor window forward; **Project: Open / Close Project Map Window** opens or closes the map from the palette, and a workspace that last had it open reopens it. **Reveal in Map** on an editor tab or a Project tree row finds that file on the map. Print… and PDF… output the whole map, split across pages when it is large, and the PDF is searchable; **Export** saves it as a PNG or an SVG. The map is another view of the same project and does not replace the tree.

## Start from a template

**New Project From Template** scaffolds a whole project and opens it in its own window, rather than pointing Editora at a folder you made yourself. Pick a multi-file [template](/features/file-templates), give the project a name and a location, and the new folder is created, registered as a project and opened on the template's main file. **Python Project** and **Node.js TypeScript Project** templates ship with it, and every other multi-file template, yours included, appears in the same picker.

## Settings you can commit

A project can carry `.editora/settings.json` with values that override yours for anyone who opens that project: indentation, tab size, rulers, wrapping, trimming trailing whitespace and the final newline, the spell-check language, the command each build tool runs, the Java, Python and JavaScript debugger programs, and **which language server to run and whether to run it**. One repository can then use two-space indentation and a JDK 17 server while another uses tabs and a JDK 25 one, and nobody has to flip a global preference between them. The values apply once the folder is trusted, and `.editorconfig` still comes first. In a project window, Settings has a **User | Project** switch to edit them on the usual rows, and **Project: Edit Project Settings…** creates the file with an example.

Appearance, keymap and fonts stay personal, because checking out a repository should not rearrange somebody's editor.
