---
title: "Projects"
group: "Workspace & files"
order: 1
beta: false
summary: "Single-folder workspaces, each in its <strong>own window</strong> with its own saved session, explored as a file tree or on the Project Map, started from a template, and carrying settings the team can commit."
alt: "A Java file open in a project, with the project's folders in the Project tree on the right"
---

Projects are VS Code-style single-folder workspaces: a root folder plus its **own saved session**, open files (with carets and pins), the active tab, folds, [editor-group layout](/features/editor-groups) and tool-window layout. Each project opens in its own window, and everything in a window acts on its own project. A folder on a remote host over SFTP is a project too, with its own window, tabs and trust (a server folder is trusted separately from a local one with the same path); reopening it asks to connect.

Open one with `C-x C-p`, switch with `C-x p`, and close to return to the global session. Bookmarks, notes and [run configurations](/features/run-configurations) are scoped per project. **On by default** since 0.10.0; Settings → Workspace turns it off.

## Tree and map

The Project tool window shows the project as a tree, with multi-select, drag-to-move and the file actions you expect. Its **Map** button opens the same project as a **[Project Map](/features/project-map)** in a window of its own: folders as columns on a canvas you can pan and zoom, code cards that stay tied to their rows, callers and references as connected cards, recent-change heat and trails. **Reveal in Map** on an editor tab or a tree row finds that file there. Both views share one context menu, so New, rename, delete, reveal, terminal, Local History, bookmarks, Personal Notes, Maven and Git actions work in either.

## Start from a template

**New Project From Template** scaffolds a whole project and opens it in its own window, rather than pointing Editora at a folder you made yourself. Pick a multi-file [template](/features/file-templates), give the project a name and a location, and the new folder is created, registered as a project and opened on the template's main file. **Python Project** and **Node.js TypeScript Project** templates ship with it, and every other multi-file template, yours included, appears in the same picker.

## Settings you can commit

A project can carry `.editora/settings.json` with values that override yours for anyone who opens that project: indentation, tab size, rulers, wrapping, trimming trailing whitespace and the final newline, the spell-check language, the command each build tool runs, the Java, Python and JavaScript debugger programs, and **which language server to run and whether to run it**. One repository can then use two-space indentation and a JDK 17 server while another uses tabs and a JDK 25 one, and nobody has to flip a global preference between them. The values apply once the folder is trusted, and `.editorconfig` still comes first. In a project window, Settings has a **User | Project** switch to edit them on the usual rows, and **Project: Edit Project Settings…** creates the file with an example.

Appearance, keymap and fonts stay personal, because checking out a repository should not rearrange somebody's editor.
