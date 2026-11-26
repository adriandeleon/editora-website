---
title: "Starting a project"
group: "Workspace & files"
order: 2
beta: false
summary: "Start a project without a terminal: a Maven project from an archetype, a whole project from a template, a Git repository, and any of about sixty file types with the right package already written."
description: "Start a Maven project from an archetype or a project from a template, git init a folder, and add any of about sixty file types with the right package."
alt: "The New Project From Template picker listing seven bundled templates, from HTML Page + CSS and Python Project to GitHub Issue + PR Templates"
---

Editora creates projects as well as opening them, so a new idea goes from nothing to a project that runs without a trip to the terminal.

## New Maven Project

**Maven: New Maven Project…**, in the File menu and on a folder's **New ▸** submenu, builds a project from an archetype and opens it in its own window.

- **Pick an archetype** from eight that ship with the editor and build on a current JDK, or **Load full catalog…** for anything on Maven Central.
- **Ready to run**: a run configuration for the main class is seeded and that class is opened, so [Run](/features/run-files/) works on the first press.
- **Advanced** sets the Java release from the JDKs you have installed, and can update the generated pom's dependencies and plugins to their latest versions.
- **Inside another project's folder** works too: an ordinary jar project there is left alone, and an aggregator pom gains the new project as a module.

## New Project From Template

**Project: New Project From Template…** writes a multi-file [template](/features/file-templates/) into a new folder and opens it as a project, on the template's main file.

- **Python Project** gives a `src` layout, a test, a build file and a README that walks through a virtual environment and `pip install -e .`.
- **Node.js TypeScript Project** gives a `package.json`, `tsconfig.json`, a test and a README.
- **Your own** multi-file templates appear in the same picker.

## Git: Initialize Repository

**Git: Initialize Repository…** puts a folder under version control, defaulting to the project root, and the [Git](/features/git/) status bar, Commit window and change bars appear at once. A repository inside another one is refused, since that is nearly always a mistake.

## New files on any folder

A folder's **New ▸** submenu offers about sixty file types, grouped by family, from Java and web to data, docs and build files.

- **The right package**: **New ▸ Java ▸ Class** in `src/main/java/demo` writes `package demo;`, and `text.Slug` creates the sub-package.
- **The type you meant**: `notes.json` under Text File gives you JSON, not `notes.json.txt`.
- **Safe**: an existing file is never overwritten.

See the [Build tools guide](/docs/build-tools/#new-maven-project).
