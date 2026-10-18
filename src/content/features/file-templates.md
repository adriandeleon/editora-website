---
title: "File templates"
group: "Workspace & files"
order: 6
beta: false
summary: "New File From Template: single- or multi-file scaffolds that ask for their variables (author, date, file name, …)."
---

**New File From Template** (`C-c C-n`) scaffolds a file (or a whole set of files) from a reusable template, prompting for any variables (author, date, file name, package…) in a small wizard.

Template bodies use `${variable}`, `${variable:default}` and `${cursor}` for where the caret lands; anything else, such as `$1` or `$HOME`, is written as it is. Before a template writes anything it lists the files that already exist. Editora ships 33 of them: code (Java class, JUnit 5 test, Java shebang script, Python script, pytest module, Node.js script, shell and Zsh scripts, HTML), the formats it previews or runs (Markdown, Typst, Mermaid, Markwhen, HTTP requests with environment files), writing (README, changelog, decision record, blog post, meeting notes) and project files (MIT and Apache 2.0 licenses, `.gitignore`, `.editorconfig`, GitHub issue and PR templates, a Java Dockerfile with optional Compose). Every field starts filled in, so **Create** alone gives a working file. Created files follow the project's `.editorconfig` line endings and indentation (a shell script is always written with LF), and a file that starts with `#!` is made executable; where the file system keeps no executable bit (Windows), the status bar says how to run it. Add your own under `~/.editora/templates/`, or use **Edit User Templates** to copy a bundled one and change it.

A multi-file template can also scaffold a **whole project**: **New Project From Template** asks for a project name and a location, creates that folder, registers it as a [project](/features/projects) and opens it on the template's main file. **Python Project** and **Node.js TypeScript Project** templates ship with it, each with a package layout, a test, a build file, a README and a `.gitignore`.
