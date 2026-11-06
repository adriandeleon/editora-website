---
title: "Language servers (LSP)"
group: "Code intelligence"
order: 2
beta: false
summary: "Go-to-definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics and completions via 26 language servers (Java, Astro, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled."
---

Editora supports the **Language Server Protocol**, both the requests that read your code and the ones that change it.

- **Go to definition**: `M-.`, or Ctrl/Cmd-click a symbol, and it works from inside an opened JDK or dependency source tab too
- **Go to implementation / type definition / declaration**, offered only where the server supports them
- **Find references**: `M-?`, listed in a browsable **References** tool window that shows each line with the editor's syntax highlighting
- **Call and type hierarchy** in a **Hierarchy** tool window, each level fetched as you expand it
- **Go to Symbol in Workspace**: search any symbol across the project and jump
- **Code actions and quick fixes**: `Ctrl+.` (`Cmd+.` on macOS) in the CUA, VS Code, Sublime and IntelliJ keymaps (also `Alt+Enter` in IntelliJ), `C-c l a` in Emacs, opening at the caret with the server's preferred fix preselected, including organize imports and extract/inline refactorings. Python gets Editora's own fixes, which Pyright does not give: **Import** for a missing name, **Remove unused import**, and **Ignore on this line** (`# pyright: ignore[rule]`)
- **Java code generation** from the same menu: toString(), hashCode()/equals(), constructors, getters and setters, delegate methods, and override/implement methods, each with a checkbox picker
- **Java refactorings that ask where to, or what**: Move, Extract Interface, and Change Signature, where the signature is edited as one line
- **Code lenses**: reference and implementation counts after a declaration; click one to open what it counts, a references lens always in the **References** tool window (off by default)
- **Re-indent as you type** (typing `;`, `}` or Enter snaps the line's indentation to the server's convention, off by default) and a **whole-project Problems** view with a Build Project command
- **Rename symbol**: `F2` in the CUA, VS Code and Sublime keymaps, `Shift+F6` in IntelliJ, `C-c l r` in Emacs, across the whole workspace, moving a public Java class's file with it. Every affected file is shown first with its change count, so you can untick any of them before applying
- **Pasted Java code imports itself**, and a `;` typed mid-expression moves to the end of the statement
- **Library sources**: a Maven library class opens with its real source when a repository has it; one that has none is shown decompiled, with **Download Sources** and **Choose Sources…** in its tab
- **Signature help** as you type `(` or `,`, with the current parameter highlighted
- **Inlay hints** (off by default), **occurrence highlighting**, and **hover docs** (`C-c h`). Turning on inlay hints, semantic highlighting or code lenses for a file whose server gives none says so
- **Format Document**: reformat the whole file via the server (palette or right-click), keeping the file's own indentation from `.editorconfig` or the file itself; a Python file is formatted with **Ruff** or **Black** (the project environment's first), also with language servers off, and after each explicit save with **Format on save (Ruff/Black)** on the Python card of Settings → LSP (off by default). Ruff and Black do not format a file on a remote host, and a narrowed or read-only Python file says why it is not formatted
- Server-provided **folding regions** and **expand/shrink selection**
- Inline **diagnostics** (with a Problems tool window and minimap/scrollbar marks; a row shows the rule code, such as `[SC2086]`, and opens the rule's documentation; unused code is faded and deprecated calls are struck through) and **completions**

Twenty-six servers are supported: Java, Astro (`astro-ls`), TypeScript/JavaScript, Python, Go, Rust, C/C++, C#, Ruby, PHP, Kotlin, Groovy, Clojure, Scala (Metals), HTML, CSS, XML, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, Dockerfile and Typst (`tinymist`). Servers are **auto-detected on your PATH, never bundled** (and configurable in Settings → LSP, where each server's card shows whether its program is found). For a [remote folder](/features/remote-sftp), the server runs on the host, found on its PATH or set per host, and Java's jdtls keeps its index there. TypeScript and JavaScript use typescript-language-server with TypeScript 5.9, installed in Editora's own folder (TypeScript 6 leaves out `@types/node` unless the tsconfig lists it, and TypeScript 7 has no `tsserver`); a folder's own `node_modules/typescript` runs only once the folder is trusted, type definitions are not downloaded unless you turn that on, and Organize Imports, inlay hints and code lenses work there too. Astro support finds the TypeScript SDK required by `astro-ls`, including a workspace-hoisted or Editora-installed SDK. A [project can commit](/features/projects) which server it wants and whether to run it, so a repository needing a different JDK doesn't mean flipping a global preference every time you switch.

**Shell scripts** use bash-language-server, which finds problems with ShellCheck and formats with shfmt. Settings and Doctor show whether both are found, and **Install: ShellCheck and shfmt** downloads the official releases, checked against SHA-256 hashes built into Editora. The Bash card in Settings → LSP sets ShellCheck's arguments, shfmt's style, how many scripts the server reads in the background, and an explainshell server for option help; a project can set the same options in its `.editora/settings.json`.

In a trusted **JavaScript or TypeScript** project, the project's own **ESLint** checks each file when you stop typing and on save, with its fixes in Code Actions and **ESLint: Fix All Problems**; Format Document uses the project's **Prettier** or **Biome** when it has one; and organize imports, ESLint fixes and formatting can run on save.

**Python** uses Pyright, told the project's interpreter: an activated environment, a `.venv` or `venv` in the project, or the one you pick. A status-bar item on Python files shows the interpreter Pyright analyses with and why (`Python 3.13.5 (.venv)`); click it or run **Python: Select Interpreter** to choose another per project. The fallback for a project with no environment is set on the **Python** card of Settings → LSP, whose **Choose…** opens the same picker. The same interpreter is used by Run, Debug and the tests, also on SSH hosts, in WSL and in containers.

A Python sub-project (a folder with its own `pyproject.toml`, `setup.py`, `setup.cfg` or `pyrightconfig.json`) gets its own Pyright, with its own `.venv`, `src/` and `[tool.pyright]`; a uv workspace member shares its workspace's. A virtual environment created after the project is open, and packages added with `pip install`, are picked up without a restart, and a remote project picks up files changed on the host when the window comes back to the front, or within a minute. **Whole project** in Problems checks every Python file, and Build Project on a Python file does the same. Go to Definition into the standard library opens it read-only, as does any file in the project's `.venv`. Organize Imports sorts a file's imports, untitled Python buffers are analysed too, and Python keeps its server up to 50,000 lines; completion says "Waiting for Pyright…" until a slow answer arrives. Pyright gives no inlay hints or semantic highlighting; basedpyright, set as the **Python server command**, gives both.

Document sync is incremental, semantic highlighting transfers only what changed where the server supports token deltas, a crashed server restarts itself, and a server shuts down a few minutes after its last file closes. Startup ends with a "<server>: Loaded and ready" line in the status bar (for jdtls, once the project import is done). **LSP: Clear Java Index Caches** deletes the Java indexes no running server is using and reports the space freed. A Java script without a `.java` name (a `java --source 25` shebang file) gets completion, hover, the outline and syntax errors from jdtls too.

**One-click install** covers every server: an **Install…** button per server in Settings, an in-editor banner when a file's server is missing, and the **Install: Language Server…** picker. Editora fetches each via the right channel (npm, the language's own toolchain, or a per-OS binary release), and the server activates without a restart.

Off by default. Enable it under Settings → LSP; the first Python file you open offers to turn it on.
