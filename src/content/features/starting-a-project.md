---
title: "Starting a project"
group: "Workspace & files"
order: 2
beta: false
summary: "Generate a Maven project from an archetype, <code>git init</code> a folder without a terminal, and create any of about fifty file types from the Project tree with the right package declaration already written."
---

Editora used to only ever *open* an existing folder, which made it a poor place to begin something.

## New Maven Project

A wizard: pick an archetype, fill in the coordinates, and the generated project is registered and opened in its own window. Reachable from the File menu, the Project tree's **New ▸** submenu, and the palette.

Archetypes come from a curated list that ships with the editor, with **Load full catalog…** to pull Maven Central's when you need something unusual. Where both list the same archetype the curated pin wins, because the published catalog is often years out of date. The package name is derived the way IntelliJ does it, so `my-app` becomes `my_app` and `2048` becomes `_2048`.

The project arrives ready to run: a run configuration is seeded for its main class with `mvn -q compile` as its before-launch step, and that class is opened, so [Run](/features/run-files) works on the first press instead of failing on an empty `target/classes`. Generation shells out to `mvn archetype:generate` rather than writing a pom by hand, so Maven has to be on your `PATH`, which is checked before the wizard opens rather than after five fields have been typed.

## Git: Initialize Repository

Starts version control without dropping to a terminal. It prompts for a folder, defaults to the project root, and brings the [Git](/features/git) UI up the usual way: status bar, Commit window, gutter change bars. Creating a repository inside an existing one is refused, naming the enclosing root, since that is nearly always a mistake and awkward to undo.

## New ▸ on any folder

About fifty file types, grouped by family (Java, Web, Scripts, Languages, Data & Config, Docs & Diagrams, Build & Ops), each carrying the icon the file will have once it exists.

A new Java file takes its package from where you create it, so "New ▸ Class" in `src/main/java/demo` writes `package demo;`, and a qualified name like `text.Slug` creates the sub-package to match. The name you type always wins over the type's extension, so `notes.json` under Text File gives you JSON rather than `notes.json.txt`, and a dotfile stays a dotfile. A name that tries to climb out of the folder creates nothing at all, and an existing file is never overwritten.
