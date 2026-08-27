---
title: Language servers (LSP)
description: Go to definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics, and completion from 22 language servers.
category: Code intelligence
order: 2
beta: true
---

Editora speaks the **Language Server Protocol**, so with a server installed you
get real language smarts. LSP is **off by default**; turn it on in
**Settings → LSP**.

## Navigation

| Feature | Command | Default key |
| --- | --- | --- |
| Go to definition | `lsp.gotoDefinition` | `M-.` |
| Go to implementation | `lsp.gotoImplementation` | (palette) |
| Go to type definition | `lsp.gotoTypeDefinition` | (palette) |
| Go to declaration | `lsp.gotoDeclaration` | (palette) |
| Find references | `lsp.findReferences` | `M-?` |
| Go to Symbol in Workspace | `lsp.gotoSymbol` | (palette) |
| Call hierarchy | `lsp.callHierarchy` | (palette) |
| Type hierarchy | `lsp.typeHierarchy` | (palette) |

The goto, references and hover commands are also in the editor right-click menu
while a server is active, under a single **LSP** submenu, and you can
**Ctrl/Cmd-click** a symbol to jump to its definition. **Go to Implementation**
and **Go to Type Definition** join that menu only when the server actually
supports them, so it never offers a dead entry.

The submenu is only a container — which actions a server contributes is
unchanged. A fully-featured server offers up to eight, which flat in the menu
pushed cut, copy, paste and the spelling suggestions far enough down to hunt
for, and a bare "Go to Definition" sitting between "Paste" and a spelling
suggestion named nothing about where it had come from.

**Find references** and Go to Implementation list multiple results in a browsable
**References** tool window (`tool.references`), grouped by file with a line and
preview; a single result jumps straight there. **Go to Symbol in Workspace**
opens a live search over every symbol in the project.

**Definitions inside libraries work too.** A Java definition that lives in the
JDK or a dependency is fetched from the server and opened in a read-only,
highlighted tab at the right line, and `M-.` keeps working *from inside* that
tab, either onward into another library class or back into your own code.

**Call and type hierarchy** open in a **Hierarchy** tool window. Call hierarchy
shows who calls the method under the caret, or what it calls (flip with the
Callers / Callees toggle); type hierarchy shows a type's supertypes and
subtypes. Each level is fetched from the server as you expand it, and Enter or a
double-click jumps to any entry.

## Reading the code

| Feature | Command | Default key |
| --- | --- | --- |
| Hover docs | `lsp.hover` | `C-c h` |
| Signature help | `lsp.signatureHelp` | (palette, automatic) |
| Toggle inlay hints | `view.toggleInlayHints` | (palette) |

**Signature help** pops the server's overload list when you type `(` or `,` in a
call: the active signature with the current parameter highlighted, an *n/m*
overload counter, and the signature's documentation. It follows your typing as
arguments are entered and closes when the caret leaves the call, on Escape, or on
scroll.

**Inlay hints** are the server's parameter-name and inferred-type annotations,
drawn in grey italics after each line. They are **off by default**; turn them on
in Settings → Code Completion or with `Toggle Inlay Hints`. They cost nothing
while off.

**Occurrence highlighting** needs no command: rest the caret on a symbol and
every occurrence in the file gets a subtle wash, with writes (assignments) shaded
warmer than reads. It is semantic rather than textual, so a local `x` does not
light up an unrelated field `x`, and it clears the moment the caret moves.

**Diagnostics** appear as inline squiggles, in the **Problems** tool window
(`M-8`), and as marks on the minimap and scrollbar. **Completion** is merged into
the [autocomplete](/docs/languages#autocomplete) popup, and the **Structure**
tool window builds its outline from the server's document symbols, with real
kinds, per-kind icons and method signatures.

The Problems window has an **Open files / Whole project** selector
(`lsp.toggleProjectProblems`), plus a **Build Project** command
(`lsp.buildWorkspace`) that recompiles the Java project and fills it. Until now
problems only ever came from files you had open, so a compile error in a file you
hadn't touched was invisible. The default is unchanged, open files only, so
nothing gets noisier unless you ask for it.

## Changing the code

| Feature | Command | Default key |
| --- | --- | --- |
| Code actions / quick fixes | `lsp.codeActions` | `Ctrl-.` / `Cmd-.` |
| Rename symbol | `lsp.rename` | `F2` |
| Format Document | `lsp.formatDocument` | (palette) |
| Organize imports | `lsp.organizeImports` | (palette) |
| Copy fully qualified name | `lsp.copyQualifiedName` | (palette) |
| Reload project configuration | `lsp.reloadProject` | (palette) |
| Re-indent as you type | `view.toggleOnTypeFormatting` | (palette) |
| Add imports for pasted Java | `view.togglePasteImports` | (palette) |
| Smart semicolon placement | `view.toggleSmartSemicolon` | (palette) |

**Code actions** ask the server what it can do at the caret or selection: quick
fixes for the diagnostics there, organize imports, generate methods, extract and
inline refactorings. The list **opens at the caret**, where you are looking,
with the server's preferred fix preselected so Enter usually does the right
thing; arrow keys or `C-n` / `C-p` move, Enter applies, Escape or `C-g`
dismisses. Every action shape is supported, including a server-side command whose
edits
arrive back over `workspace/applyEdit`. Edits land as normal undoable edits, one
undo step per file, and a multi-file fix opens the untouched files in background
tabs. `Ctrl-.` / `Cmd-.` is bound in the VS Code, Sublime and IntelliJ keymaps;
the command is in the palette and the right-click menu in every keymap.

**Java code generation** rides that same menu: **Generate toString()**, **Generate
hashCode() and equals()**, **Generate Constructors** and **Override/Implement
Methods**. Each opens a checkbox list so you choose which fields or methods to
include, with Space to toggle and Enter to generate. The server withholds these
unless the editor says it can drive the picker, which is why they were absent
before.

Three Java commands sit outside the menu: **Organize Imports** sorts and prunes a
file's imports directly, **Copy Fully Qualified Name** puts the full name of the
symbol at the caret on the clipboard, and **Reload Project Configuration** makes
the server re-read `pom.xml` or `build.gradle` when a dependency change hasn't
been picked up.

**Rename symbol** renames across the whole workspace. The prompt comes pre-filled
with the current name, and the server validates the spot first, so you are told
up front when something cannot be renamed. Renaming a public Java class also
**moves its `.java` file**, and the tab, session state and language server all
follow to the new path. `F2` is bound in the VS Code, Sublime and IntelliJ
keymaps.

A rename that reaches **beyond the file you're looking at** shows you what it
will do first: every affected file, its change count, and where it moves to, with
a tick beside each one so you can leave a file out. A rename confined to the
current file applies straight away — it's on screen and one undo away, so there
would be nothing to confirm.

**Format Document** reformats the whole file through the server when it advertises
formatting (undoable, palette or right-click menu), including `.json`, `.css` and
`.html`. In a language whose server supports range formatting, **Tab** also snaps
the current line's indentation to the server's convention.

### Two Java-only assists

**Pasted code imports itself.** Paste a snippet into a Java file and the server
is asked which imports the pasted text needs; they are added for you. This is the
one paste behaviour nothing else in the stack can approximate, because it needs
the type resolver. An answer that lost a race with your typing is dropped rather
than applied to text it wasn't computed for.

**Smart semicolon placement.** Typing `;` part-way through an expression puts it
at the end of the statement: `compute(1, 2|)` becomes `compute(1, 2);|`. The
semicolon is never held back waiting for the server — making a keystroke wait on
a round trip is exactly the latency an editor cannot afford — so it lands where
you typed it and moves afterwards if the server disagrees, as a single undo step.
Type straight on through (the very common `;` then Enter) and the document has
moved past the answer, so the correction is skipped rather than applied late.

Both need the Java language server running, and both can be turned off in
Settings → Code Completion.

**Re-indent as you type** does the same on `;`, `}` and Enter. It is indentation
only, never a reformat of the line under you, and the local auto-indent still
acts first, so nothing feels slower and the server only corrects the cases the
heuristic gets wrong. Off by default: turn it on in Settings → Code Completion or
with `view.toggleOnTypeFormatting`.

## Folding and selection

Where a server is running, **code folding comes from the server's own
understanding of the file** instead of brace and indent scanning: an import block
folds as one region, javadoc and block comments fold, and multi-line expressions
no longer nest wrongly. [Expand and shrink
selection](/docs/editing#expand-and-shrink-selection) uses the same source, so it
respects strings and comments rather than guessing at brackets.

Both fall back to the built-in behaviour whenever LSP is off, the file is remote,
or the server offers neither, so nothing is ever *less* foldable than it was.

## While the server works

Servers that report long-running work through the LSP progress channel (jdtls
importing and indexing a project, gopls loading packages, rust-analyzer's first
check) drive the status-bar loading bar and show what they are doing, with a
title and percentage, in the echo area.

External file changes are forwarded to the running servers, so a `git checkout`,
a branch switch, a CLI build or an external editor no longer leaves a server's
project model quietly stale. A server that dies mid-session (a crash, an
OOM-kill, a failed handshake) is announced in the status bar, has its stale
diagnostics cleared, and is restarted for the affected files, with a cap so a
server that keeps crashing is not relaunched forever.

## Servers are auto-detected, never bundled

Editora doesn't ship language servers. It looks for each one on your `PATH` (a
Settings field can override the command per server) and uses it if present.
Twenty-two servers are supported, plus a Maven-aware `pom.xml` server:

- **Java** (`jdtls`), **TypeScript / JavaScript** (`typescript-language-server`),
  **Python** (`pyright-langserver`), **Go** (`gopls`), **Rust**
  (`rust-analyzer`), **C / C++** (`clangd`), **C#** (`csharp-ls`)
- **Ruby** (`ruby-lsp`), **PHP** (`phpactor`), **Kotlin**
  (`kotlin-language-server`), **Lua** (`lua-language-server`)
- **HTML**, **CSS**, **JSON**, **YAML**, **XML** (`lemminx`), **Bash**
  (`bash-language-server`), **Dockerfile**, **SQL** (`sqls`), **Terraform**
  (`terraform-ls`), **TOML** (`taplo`), **Typst** (`tinymist`)

There is also a **Maven-aware `pom.xml` server** (JVM lemminx plus the
lemminx-maven extension). It is routed by file name, so a `pom.xml` gets
dependency, plugin and GAV completion while every other XML file keeps the fast
native lemminx.

Each server has its own enable checkbox and a live found/not-found status on the
**Settings → LSP** page, plus a per-server command field with a Browse button.
[Doctor](/docs/troubleshooting#doctor) reports the same detection for every
enabled server in one place.

### Per-project overrides

A project can commit a `.editora/settings.toml` naming **which server to run for
a language and whether to run it**, which overrides your global preference for
anyone who opens that project. That is the answer to one repository needing a
JDK 17 server and another a JDK 25 one: neither of you has to remember to flip a
setting when switching between them. See
[projects](/docs/workspace#settings-a-project-can-commit).

## One-click install

If a server isn't installed, Editora can fetch it for you. **Every server** is
covered through four channels, picked per server:

- **npm** packages (JSON/HTML/CSS, Bash, YAML, Dockerfile, TOML, TypeScript,
  Python/Pyright), needing Node.
- **Toolchain** installs that use the language's own package manager (Go, Ruby,
  C#, Rust, PHP), which run only when that toolchain is already on your `PATH`.
- **Binary** releases downloaded per OS/architecture (clangd, lemminx, Kotlin,
  terraform-ls, Lua), extracted into your config folder.
- A **JVM zip with its dependencies** for the Maven-aware `pom.xml` server, which
  needs a JDK.

Three entry points: an **Install…** button per server in **Settings → LSP**, an
in-editor **banner** when you open a file whose server is missing (turn the nudge
off with `view.toggleInstallPrompts`), and the **Install: Language Server…**
picker (`install.languageServer`). After installing, the server is auto-detected
and activates without a restart. Editora never installs the underlying runtimes
(Node, Python, Go, …); if one is missing it tells you which to install first.

## Workspace roots and lifetime

A server runs once per workspace root. The root is the active project, else the
nearest build marker (`pom.xml`, `package.json`, `go.mod`, `Cargo.toml`, and so
on), else the file's directory. Files of the same language in one project share a
single server.

Servers start **per tab, as you visit it**, rather than all at once while a
session restores, and they **shut down when their last file closes**, after a
three-minute grace period so reopening a file keeps the server warm. Each window
gets its own jdtls workspace directory, so two windows on the same Java project
can't contend for one Eclipse workspace lock.

## Finding the server's binary

A GUI-launched app inherits a stripped `PATH` that often misses Homebrew, npm,
and version-manager directories. Editora rebuilds the `PATH` from your login
shell plus the usual install locations before launching a server, which recovers
things like nvm's per-version `node` bin. If a server still isn't found, set its
absolute path in Settings. See [Troubleshooting](/docs/troubleshooting#external-tools-arent-found)
for details.

## Notes and limits

Document sync is **incremental** for servers that accept it (most of them), with
a periodic full resync as a divergence safety net, and semantic highlighting
transfers only what changed on servers that support token deltas. Diagnostics for
files that aren't open are dropped by default, to keep the Problems window
focused on what you're editing, and the whole-project selector above lifts that
when you want it. Still deferred: format-on-save.
