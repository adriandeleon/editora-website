---
title: "Language servers (LSP)"
group: "Code intelligence"
order: 2
beta: true
summary: "Go-to-definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics and completions via 23 language servers (Java, Astro, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled."
---

Editora supports the **Language Server Protocol**, both the requests that read your code and the ones that change it.

- **Go to definition**: `M-.`, or Ctrl/Cmd-click a symbol, and it works from inside an opened JDK or dependency source tab too
- **Go to implementation / type definition / declaration**, offered only where the server supports them
- **Find references**: `M-?`, listed in a browsable **References** tool window that shows each line with the editor's syntax highlighting
- **Call and type hierarchy** in a **Hierarchy** tool window, each level fetched as you expand it
- **Go to Symbol in Workspace**: search any symbol across the project and jump
- **Code actions and quick fixes**: `Ctrl-.` / `Cmd-.` in the VS Code, Sublime and IntelliJ keymaps (from the palette in Emacs and CUA), opening at the caret with the server's preferred fix preselected, including organize imports and extract/inline refactorings
- **Java code generation** from the same menu: toString(), hashCode()/equals(), constructors, getters and setters, delegate methods, and override/implement methods, each with a checkbox picker
- **Java refactorings that ask where to, or what**: Move, Extract Interface, and Change Signature, where the signature is edited as one line
- **Code lenses**: reference and implementation counts after a declaration; click one to open what it counts, a references lens always in the **References** tool window (off by default)
- **Re-indent as you type** (typing `;`, `}` or Enter snaps the line's indentation to the server's convention, off by default) and a **whole-project Problems** view with a Build Project command
- **Rename symbol**: `F2` in the VS Code, Sublime and IntelliJ keymaps (from the palette in Emacs and CUA), across the whole workspace, moving a public Java class's file with it. Every affected file is shown first with its change count, so you can untick any of them before applying
- **Pasted Java code imports itself**, and a `;` typed mid-expression moves to the end of the statement
- **Signature help** as you type `(` or `,`, with the current parameter highlighted
- **Inlay hints** (off by default), **occurrence highlighting**, and **hover docs** (`C-c h`)
- **Format Document**: reformat the whole file via the server (palette or right-click), keeping the file's own indentation from `.editorconfig` or the file itself
- Server-provided **folding regions** and **expand/shrink selection**
- Inline **diagnostics** (with a Problems tool window and minimap/scrollbar marks; a row shows the rule code, such as `[SC2086]`, and opens the rule's documentation) and **completions**

Twenty-six servers are supported: Java, Astro (`astro-ls`), TypeScript/JavaScript, Python, Go, Rust, C/C++, C#, Ruby, PHP, Kotlin, Groovy, Clojure, Scala (Metals), HTML, CSS, XML, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, Dockerfile and Typst (`tinymist`). Servers are **auto-detected on your PATH, never bundled** (and configurable in Settings → LSP, where each server's card shows whether its program is found). For a [remote folder](/features/remote-sftp), the server runs on the host, found on its PATH or set per host, and Java's jdtls keeps its index there. TypeScript and JavaScript use typescript-language-server with TypeScript 6, because TypeScript 7 has no `tsserver`; a folder's own `node_modules/typescript` runs only once the folder is trusted, type definitions are not downloaded unless you turn that on, and Organize Imports, inlay hints and code lenses work there too. Astro support finds the TypeScript SDK required by `astro-ls`, including a workspace-hoisted or Editora-installed SDK. A [project can commit](/features/projects) which server it wants and whether to run it, so a repository needing a different JDK doesn't mean flipping a global preference every time you switch.

**Shell scripts** use bash-language-server, which finds problems with ShellCheck and formats with shfmt. Settings and Doctor show whether both are found, and **Install: ShellCheck and shfmt** downloads the official releases, checked against SHA-256 hashes built into Editora. The Bash card in Settings → LSP sets ShellCheck's arguments, shfmt's style, how many scripts the server reads in the background, and an explainshell server for option help; a project can set the same options in its `.editora/settings.json`.

Document sync is incremental, semantic highlighting transfers only what changed where the server supports token deltas, a crashed server restarts itself, and a server shuts down a few minutes after its last file closes. **LSP: Clear Java Index Caches** deletes the Java indexes no running server is using and reports the space freed. A Java script without a `.java` name (a `java --source 25` shebang file) gets completion, hover, the outline and syntax errors from jdtls too.

**One-click install** covers every server: an **Install…** button per server in Settings, an in-editor banner when a file's server is missing, and the **Install: Language Server…** picker. Editora fetches each via the right channel (npm, the language's own toolchain, or a per-OS binary release), and the server activates without a restart.

Off by default. Enable it under Settings → LSP.
