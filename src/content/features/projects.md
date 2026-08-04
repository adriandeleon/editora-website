---
title: "Projects"
group: "Workspace & files"
order: 1
beta: false
summary: "VS Code single-folder workspaces: a root folder plus its own saved session (open files, layout, folds), shown as a filterable file tree. Scaffold one from a template, and commit settings the whole team gets."
---

Projects are VS Code-style single-folder workspaces: a root folder plus its **own saved session**, open files (with carets and pins), the active tab, folds, [editor-group layout](/features/editor-groups) and tool-window layout. Each project opens in its own window.

Open one with `C-x C-p`, switch with `C-x p`, and close to return to the global session. The **Project tool window** shows the tree with keyboard navigation and a filter that runs a bounded project-wide filename search. Bookmarks, notes and [run configurations](/features/run-configurations) are scoped per project. **On by default** since 0.10.0; Settings → Workspace turns it off.

## Start from a template

**New Project From Template** scaffolds a whole project and opens it in its own window, rather than pointing Editora at a folder you made yourself. Pick a multi-file [template](/features/file-templates), fill in its variables, choose where it goes, and the new folder is registered as a project and opened. A **Python Project** template ships with it (package layout, a test, `pyproject.toml`, README and `.gitignore`), and your own multi-file templates appear in the same picker.

## Settings you can commit

A project can carry `.editora/settings.toml` saying **which language server to run and whether to run it**, overriding your global preferences for anyone who opens that project. The case it's for: one repository needs a JDK 17 server and another a JDK 25 one, and nobody should have to remember to flip a global preference between them. **Project: Edit Project Settings…** creates the file with a commented example.

Only toolchain settings can be overridden. Appearance, keymap and fonts stay personal, because checking out a repository should not rearrange somebody's editor.
