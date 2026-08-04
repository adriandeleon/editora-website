---
title: "File templates"
group: "Workspace & files"
order: 5
beta: true
summary: "New File From Template: single- or multi-file scaffolds with interactive placeholders (author, date, file name, …)."
---

**New File From Template** (`C-c C-n`) scaffolds a file (or a whole set of files) from a reusable template, prompting for any variables (author, date, file name, package…) in a small wizard.

Templates use the same `${var}` / `$0` syntax as snippets; bundled ones cover a Java class, an HTML page/bundle, a Markdown doc, and a Python script. Add your own under `~/.editora/templates/`.

A multi-file template can also scaffold a **whole project**: **New Project From Template** writes it to a folder of your choosing, registers that folder as a [project](/features/projects) and opens it in its own window. A **Python Project** template ships with it — package layout, a test, `pyproject.toml`, README and `.gitignore`.
