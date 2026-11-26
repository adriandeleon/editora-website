---
title: "Projects"
group: "Workspace & files"
order: 1
beta: false
summary: "Single-folder workspaces, each in its <strong>own window</strong> with its own saved session, explored as a file tree or on the Project Map, with settings the team can commit."
description: "Single-folder projects, each in its own window with a saved session, a file tree and Project Map, and settings a team can commit."
alt: "A Java file open in the Editora project, with the project's folders in the Project tree on the right and the Git menu in the menu bar"
---

A project is a folder that opens in its own window and remembers how you left it, so switching between repositories never mixes their files, tabs or settings.

## One window per project

- **Open a folder** with `C-x C-p` in the Emacs keymap, or **Project: Open Folder…** in the palette; switch with `C-x p` or **Project: Switch…**.
- **Its own session**: open files with their carets and pins, the active tab, folds, the [editor-group](/features/editor-groups/) layout and the tool windows all come back.
- **Its own scope**: everything in a window acts on its project, and bookmarks, notes and [run configurations](/features/run-configurations/) belong to it.
- **Remote folders** are projects too: a [folder on a server](/features/remote-sftp/) gets its own window, tabs and trust.
- **Closing** a project returns you to the global session.

## Tree and map

The Project tool window shows the folder as a tree, with multi-select, drag-to-move and the file actions you expect. Its **Map** button opens the same project on the [Project Map](/features/project-map/), and both share one context menu, so New, rename, Local History, bookmarks, Git and the rest work in either.

To start a project from an archetype or a template, see [Starting a project](/features/starting-a-project/).

## Settings you can commit

A project can carry `.editora/settings.json`, whose values override yours for anyone who opens it:

- **Editing**: indentation, tab size, rulers, wrapping, trailing whitespace, the final newline and the spell-check language.
- **Tools**: the command each build tool runs, the Java, Python and JavaScript debugger programs, and the JDK and build tools the project uses.
- **Language servers**: which one runs for a language, and whether it runs at all.

One repository can then use two-space indentation and a JDK 17 server while another uses tabs and JDK 25, with no global setting to flip. The values apply once the folder is trusted, and `.editorconfig` still comes first. In a project window, Settings switches between **All projects** and **This project** at the top, and **Project: Edit Project Settings…** creates the file with an example. Appearance, keymap and fonts stay personal.

**On by default.** Turn it off in **Settings → Workspace** (**Enable projects**). See the [Projects guide](/docs/workspace/#projects).
