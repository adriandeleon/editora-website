---
title: "Editora 0.23.0: Kotlin, Scala, Clojure and Groovy, a terminal, Node.js Run and Debug, deployments and encrypted files"
description: "Editora 0.23.0 brings Kotlin, Scala, Clojure and Groovy with language servers, Run and Debug, a Terminal tool window, Run and Debug for Node.js and TypeScript, one Python interpreter for everything with a pytest runner, deployments that copy a project to an SSH host and run it there, and GnuPG-encrypted files in the core. Java debugging gains hot code replace, SSH logins follow your SSH agent and ~/.ssh/config, Settings shows what you changed and takes project settings, and plugins ask before anything changes."
date: 2026-10-19
version: "0.23.0"
---

**Editora 0.23.0** is out. Four more JVM languages get the whole treatment,
from highlighting to the debugger, there is a terminal inside the editor,
Node.js and Python scripts run, test and debug like Java does, and a project
can now be deployed to another machine and run there. Download it from the
[0.23.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.23.0).

## Kotlin, Scala, Clojure and Groovy

Each language gets a language server installed in one click, Run File and the
gutter ▶, a run configuration type, and Debug. Kotlin, Groovy, Clojure and Scala
scripts debug through the JVM debugger built into Editora, so there is no debug
adapter to install; a file of a Scala build uses Metals' own.

- **Kotlin** uses JetBrains' Kotlin LSP, which imports the Gradle build. A
  `.kts` script runs with `kotlinc -script`, and a `.kt` file with a `main`
  is compiled once into a cached jar. Debug stops in programs, scripts and
  lambdas and steps over the standard library and coroutines.
- **Scala** uses Metals, and sbt, Mill and Scala CLI projects get a toolbar
  button and task list like Gradle's. Run picks an `@main def`, an
  `object … extends App`, a `def main` or a `.sc` script and runs it through
  the build or Scala CLI.
- **Clojure** uses clojure-lsp with clj-kondo's diagnostics. `deps.edn`,
  `project.clj` and `bb.edn` projects get Clojure CLI, Leiningen or Babashka
  tasks, and an uncaught exception prints its whole trace with linked frames.
- **Groovy** uses the gvy language server; scripts, classes and closures can
  be debugged.

Scala and Clojure files are highlighted, fold, indent (Lisp-style for
Clojure) and have an outline without a language server too. The Run window
shows which Kotlin, Scala or Groovy version ran, beside the Java one. [More about the language servers](/docs/lsp#kotlin) and
[running files](/docs/run-debug).

## A terminal inside Editora

The new **Terminal** tool window opens shells in tabs, in the project's folder
(Alt+F12 in the IntelliJ keymap, Ctrl+` in VS Code, CUA and Sublime). A focused
terminal owns the keyboard, so Ctrl+C, Ctrl+W and Escape reach the shell and
vim, while the keys a shell never uses still reach Editora.

- **Open Terminal Here** from the Project tree, a tab or the breadcrumb.
- **Terminals on a remote host, in a container or in WSL** open where the
  project's tools run.
- **Shell integration** (bash and zsh) keeps the tab in the shell's folder and
  lets you jump between prompts.
- File paths, `file:line` locations and URLs in the output open with a click,
  and Find searches the scrollback.

[More about the terminal](/docs/terminal).

## Node.js and TypeScript

- **Run File runs scripts.** A `.js`, `.mjs`, `.cjs`, `.ts`, `.mts` or `.cts`
  file, or a `node`, `deno`, `bun` or `tsx` shebang, runs with source maps and
  colours. TypeScript runs on plain Node 22.18 or later.
- **Tests:** the ▶ of a test file runs `node --test` into Test Results, with
  Rerun Failed and Rerun This Test.
- **Debugging** for TypeScript and for a compiled project's `.ts` sources,
  **Attach** to a Node started with `--inspect`, and **Debug Test at Caret**.
- **One Node.js for everything:** Run, Debug, npm scripts, the TypeScript
  server and HTTP scripts use the same Node, from the setting, the project's
  `.nvmrc` or `.node-version`, or PATH. Doctor says which one and why.

[More about Node.js scripts](/docs/run-debug#nodejs-scripts).

## Python

- **One interpreter for Run, Debug and code intelligence.** Editora finds the
  project's own Python: an activated environment, then a `.venv`, `venv` or
  `env` folder, then the setting, then the system Python. Pyright is told
  which one, so a venv's packages resolve with no config file. The status bar
  shows the interpreter and lets you pick another per project, also on SSH
  hosts, in containers and in WSL.
- **A pytest runner.** Test files get a ▶ on every test, results fill Test
  Results, and Debug Test works from a test line or a result row. Without
  pytest, `unittest` tests run instead.
- **Format Document runs Ruff or Black**, even with language servers off.
- **Run configurations** take an interpreter, and a **Python module** type runs
  `python -m`.

[More about Python](/docs/lsp#python).

## Debugging

- **Java hot code replace.** Saving a `.java` file while debugging loads the new
  code into the running program, and the Debug console says what changed.
- **Restart Frame** and **Smart Step Into…** for Java.
- **Debug: Breakpoints…** lists every breakpoint to jump to, switch off or
  remove; breakpoints get hit counts, and **Mute Breakpoints** is kept per
  project.
- **Completion while debugging** in the evaluate field, watches and breakpoint
  conditions.
- **The value popup unfolds** objects and collections as a tree, and variables
  use the editor's syntax colours.
- **Caught and Uncaught** exception breakpoints are two toggles, and Java's step
  filters skip JDK and synthetic code.
- JDK and library frames show their source, and every keymap has the usual
  debugger keys.

[More about debugging](/docs/run-debug#debugging).

## Deployments

Develop locally, run on another machine. A run configuration's **Run on**
setting names a deployment, an SSH host and a folder there. Before the run,
Editora copies only what changed to that folder in one compressed stream over
its own connection (no rsync or ssh needed here), then builds and runs the
program there with the host's tools. Files Editora put there and the project no
longer has are removed, and nothing else on the host ever is.
[More about deployments](/docs/deployments).

## Encrypted files

GnuPG encryption is built in (the `gpg-tools` plugin is retired). An encrypted
file opens as the text inside it, in that text's language, with a lock on its
tab and a status-bar item that says who can read it. **Encrypt File…** picks the
recipients or a passphrase, and saving encrypts it again for the same people.
While it is open its text gets no crash-recovery copy or Local History,
language servers do not read it, and agents, MCP clients and AI actions are
refused. A tab that cannot be opened stays locked and says why.
[More about encrypted files](/docs/encrypted-files).

## SSH logins

- **Logins ask what they need, while they run:** a password, a passphrase, a
  2FA code, as `ssh` does.
- **Your SSH agent's keys** are used first: `ssh-agent`, 1Password, Secretive,
  gpg-agent or Windows' agent, so keys on a security key or a smartcard work.
- **`~/.ssh/config` is followed**: the connect form offers its hosts, and
  logins use its `IdentityFile`, `IdentitiesOnly`, `ProxyJump` and `Include`
  lines.
- A failed connect says why, and connecting a remote folder logs in once.

[More about authentication](/docs/remote#authentication).

## Settings

- **Settings shows what you changed**, with a marker, a count per page,
  **Reset to default** on each row and a **Modified** filter.
- **Project settings:** in a project window, a **User | Project** switch sets
  indentation, rulers, wrap, the dictionary, build commands, debugger paths and
  language servers for that project.
- **Import Configuration…** reads an Export Configuration zip, with a backup
  first. Settings that apply at startup offer **Restart Now**.
- **AI API keys live in the system keychain** and stay out of exports, and the
  Sync page shows whether it is connected.
- Search ranks its results, every page links to its documentation, and
  the pages were reworked so they look and behave the same.

[More about the Settings window](/docs/configuration#the-settings-window).

## Plugins

Plugins ask before anything changes: consent belongs to a plugin's folder and
to what you were shown, an install or update is staged and only put in place
once you agree, and MCP clients cannot turn plugins on or install them.
`--disable-plugins` starts Editora without them, and Doctor → Plugins lists any
that failed or started slowly.
[More about plugins](/docs/plugins).

## Also

- **The HTTP Client's response view** gives the body the room, with a tab strip
  and one header row; Run All lists every request, and results show on the
  request lines.
- **23 more file templates**, 32 in all, and 28 more Java snippets.
- **The Run and Debug windows show the runtime**, such as `Java 25.0.4
  (Temurin)` or `Python 3.13.5`.
- **The Welcome page and Doctor were reworked**, and Doctor puts problems
  first.
- **Simple UI mode trims the right-click menus too.**
- **Run Configurations → Main class** has a Browse… button.
- The technical dictionary knows over 900 more terms, and possessives of any
  word are accepted.
- Changing tab size with many files open is about nine times faster.
- Fixes: window buttons are visible on dark themes, File Information and the
  status bar agree on the word count, and Settings no longer loses text typed
  while another window saves.

The full list is in the
[changelog](https://github.com/adriandeleon/Editora/blob/v0.23.0/CHANGELOG.md).
