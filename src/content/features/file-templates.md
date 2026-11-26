---
title: "File templates"
group: "Workspace & files"
order: 6
beta: false
summary: "New File From Template: single- and multi-file scaffolds that fill in the author, date, file name and package for you, with 37 bundled and room for your own."
description: "New File From Template: 37 bundled single- and multi-file templates that ask for their variables, plus templates of your own."
alt: "The New File From Template picker listing Java, HTML, Markdown, Python and shell templates"
---

Start a file from a template instead of a blank page or a copy of the last one. **Template: New File From Template…**, `C-c C-n` in the Emacs keymap or the toolbar button, asks only for what it cannot fill in, and every field starts filled, so **Create** alone gives a working file.

## What ships

Editora ships 37 templates:

- **Code**: a Java class, a JUnit 5 test, Python, Node.js, shell and Zsh scripts, an HTML page, and a Maven `pom.xml`.
- **XML**: a schema, an XSLT stylesheet, and an XML file bound to a schema so it validates from the start.
- **Files Editora previews or runs**: Markdown, Typst, Mermaid, Markwhen and HTTP requests with environment files.
- **Writing**: a README, a changelog, a decision record, a blog post and meeting notes.
- **Project files**: MIT and Apache 2.0 licenses, `.gitignore`, `.editorconfig`, GitHub issue and PR templates, and a Java Dockerfile.
- **Whole projects**: multi-file templates start a project; see [Starting a project](/features/starting-a-project/).

## How it fills in

- **Variables** such as the author, the date, the file name and the package are filled in for you, and the caret lands where the template says.
- **Nothing overwritten**: before writing, a template lists any files that already exist.
- **Fits the project**: files follow its `.editorconfig`, and a script that starts with `#!` is made executable.

## Your own templates

Add templates under `~/.editora/templates/`, or use **Template: Edit User Templates…** to copy a bundled one and change it.

See the [File templates guide](/docs/snippets-templates/#file-templates).
