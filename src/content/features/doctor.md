---
title: "Doctor"
group: "Customization & extensibility"
order: 10
beta: false
summary: "A health screen for every external tool Editora can use: what was found, which version, where it lives, and what's missing that you actually need, with problems first and an <strong>Install…</strong> or <strong>Turn on</strong> button where Editora can fix it for you."
---

Editora leans on the tools already on your machine rather than bundling its own copies, so **Doctor** tells you what it found. Open it with **Tools → View: Doctor**, `view.doctor` in the palette, the Welcome page, or the **Open Doctor** button in the message log.

Every integration gets a row, grouped by area:

- **Version control**: `git`, and `gh` for pull requests, issues, and CI runs.
- **Search**: `ripgrep`, which accelerates Find in Files (the built-in walker is used when it's absent).
- **Encryption**: GnuPG and its passphrase prompt, for [encrypted files](/docs/encrypted-files).
- **Preview & diagrams**: `mmdc` and `maid` for Mermaid, Graphviz and PlantUML (with their versions), and the Typst CLI.
- **Language servers**: one row per enabled server, showing the resolved path (and Pyright's version).
- **Formatting & linting**: the **Python formatter** (Ruff or Black) that Format Document uses, and ShellCheck and shfmt, which the Shell server lints and formats with.
- **Debugging**: the Java debug plugin, `debugpy`, and `js-debug`.
- **Run**: the JDK used to run a file, the **Python interpreter** that Run, Debug and Pyright share (its version, command and why it was chosen), the Node.js that Run, Debug, npm scripts and the TypeScript server share, `bash`, and `make`.
- **HTTP Client**: the Node.js that runs response handlers and pre-request scripts.
- **Build tools**: Maven, npm, Cargo, Go, Gradle, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka, checked by the command set in Settings or the project's own wrapper.
- **AI & agents**: the AI provider (is a key set) and the selected agent's command.
- **Remote & containers**: the container engine, and WSL on Windows.
- **System**: the installer prerequisites, elevated save, and the browsers available for HTML preview.
- **Plugins**: how many plugins run, and a row for each one that failed to load or start, or started slowly.

A missing tool is a **problem** only when something in the window needs it: a file of that language is open, the project has the tool's marker file (`pom.xml`, `go.mod`, `Cargo.toml`, `package.json`…), or you set its command. Otherwise the row says **Not installed**, keeps its **Install…** button, and isn't counted, so turning on language servers doesn't paint every language you never use red.

Problems and warnings (amber: found but not usable yet, like a `gh` that isn't signed in or a JDK too old to run a file) are gathered under **Needs attention** at the top. A section where everything is OK folds to one line, and the not-installed tools of a section fold together; **Show** opens them. The summary counts link to the attention group.

Each row has the fix it needs: **Install…** where Editora can fetch the tool, **Turn on** for a feature that is switched off, **How to install** where it can't, and **Settings…** for the page that configures it. A server or tool Editora installs at the exact release it is tested with (jdtls, js-debug, the Maven `pom.xml` server, gls, Metals, the Kotlin server, clojure-lsp, ShellCheck and shfmt) gets an **Update…** button when the copy you have is older, or was installed by an earlier Editora that didn't record its version: it installs the tested release and restarts the server on it. See [Update… for servers older than Editora's](/docs/troubleshooting#update-for-servers-older-than-editoras). Right-click a row to copy it or its path, or to reveal the path in the file manager. **Copy Report** puts the whole report, with the Editora version, OS and Java, on the clipboard for a bug report.

Probes run fresh each time, off the UI thread, so Doctor reflects the machine as it is now rather than a cached answer from startup. **Refresh** and installs keep the last results on screen while checking again, and the header says when the check ran. In a project on an SSH host, in WSL or in a container, the header says that the rows describe this computer.
