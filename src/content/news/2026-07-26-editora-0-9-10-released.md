---
title: "Editora 0.9.10: the language-server release"
description: "Code actions, rename, signature help, inlay hints, call and type hierarchy, occurrence highlighting and server-provided folding; run and debug a real project's main class; the Emacs model completed with a kill ring, narrowing, rectangles and query-replace; and Doctor, a health screen for every external tool."
date: 2026-07-26
version: "0.9.10"
---

**Editora 0.9.10** is out, and it is the largest release so far: 51 new features,
7 performance fixes and 18 bug fixes. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/tag/v0.9.10).

## Language servers grew up

Editora has spoken LSP since 0.9.1, but only the easy half of it: definitions,
references, hover, diagnostics, completion. The whole second half landed in this
release, so a language server is now worth turning on for more than squiggles.
See the [LSP guide](/docs/lsp).

- **Code actions and quick fixes.** `LSP: Code Actions` (palette, the editor
  right-click menu, `Ctrl-.` / `Cmd-.` in the VS Code, Sublime and IntelliJ
  keymaps) asks the server what it can do at the caret: fixes for the diagnostics
  there, organize imports, generate methods, extract and inline refactorings.
  Every action shape works, including a server-side command whose edits arrive
  back over `workspace/applyEdit`.
- **Rename symbol.** `F2`, the right-click menu, or `LSP: Rename Symbol…` renames
  across the whole workspace. Renaming a public Java class also moves its `.java`
  file, and the tab, session state and language server follow it.
- **Signature help.** Typing `(` or `,` pops the overload list with the current
  parameter highlighted, an *n/m* counter and the signature's docs, and it
  follows your typing as you fill arguments in.
- **Inlay hints.** The server's parameter-name and inferred-type hints, drawn as
  grey italic annotations after each line. Off by default; turn them on in
  Settings → Code Completion.
- **Call and type hierarchy.** `LSP: Call Hierarchy` shows who calls the method
  under the caret, or what it calls, in a new **Hierarchy** tool window, each
  level fetched as you expand it. `LSP: Type Hierarchy` does supertypes and
  subtypes.
- **Occurrence highlighting.** Rest the caret on a symbol and every occurrence in
  the file gets a subtle wash, with writes shaded warmer than reads. It is
  semantic, so a local `x` does not light up an unrelated field `x`.
- **Go to Implementation, Type Definition and Declaration**, beside Go to
  Definition, and only offered in the menu when the server actually supports
  them.
- **Definitions inside libraries.** `M-.` on `String` or any dependency symbol
  used to claim there was no definition. The class source is now fetched from the
  server and opened read-only at the right line, and you can keep chaining `M-.`
  from inside it.
- **Server-provided folding and selection.** Where a server is running, folds come
  from its understanding of the file (an import block folds as one region, javadoc
  folds) instead of brace scanning, and expand/shrink selection uses the same
  source. Both fall back to the built-in behaviour when there is no server.
- **Real progress reporting.** Servers that report long-running work through the
  LSP progress channel (jdtls importing a project, gopls loading packages,
  rust-analyzer's first check) now drive the status-bar loading bar and say what
  they are doing, instead of a bare spinner that only covered startup.
- **Servers track external file changes.** A `git checkout`, a CLI build or an
  external editor is forwarded to the running servers, so their project models no
  longer go stale until a restart.
- **Java code generation** from that same code-action menu: **Generate
  toString()**, **hashCode() and equals()**, **Constructors** and
  **Override/Implement Methods**, each with a checkbox list so you pick the
  fields or methods to include. The server withholds these unless the editor says
  it can drive the picker, which is why they were absent entirely.
- **Three Java commands**: **Organize Imports** (directly, without the code-action
  menu), **Copy Fully Qualified Name**, and **Reload Project Configuration**, for
  when a dependency change hasn't been picked up.
- **Project-wide problems.** The Problems window gained an **Open files / Whole
  project** selector and a **Build Project** command that recompiles the Java
  project and fills it, so a compile error in a file you hadn't opened is no
  longer invisible. The default is unchanged, open files only.
- **Re-indent as you type.** Typing `;`, `}` or Enter snaps the line to the
  server's own indentation convention. Indentation only, never a reformat, with
  the local auto-indent still acting first. Off by default.
- **A crashed server restarts itself**, servers **shut down when their last file
  closes** (after a three-minute grace), and two windows on the same Java project
  no longer contend for one jdtls workspace.

Three performance fixes came with it: typing pauses now send only the changed
splice of the document to servers that accept incremental sync, semantic
highlighting transfers only what changed on servers that support token deltas
(so large Java files pay for their edits, not their size), and a diagnostics
burst costs map lookups instead of filesystem syscalls on the UI thread.

## Run and debug a real project

Until now Run meant a single-file script. It now means your project.

**Run Main Class…** and **Debug Main Class…** pick any main class in the active
file's Maven or Gradle project, and a green ▶ appears in the gutter beside every
`public static void main` (the right-click menu offers *Run '….main()'* and
*Debug '….main()'*). Debugging and the fast project-wide Run use the Java
language server; without it, Run falls back to the build tool, with Maven
resolving the classpath and Gradle delegating to `run` or `bootRun`.

**Run configurations** are saved per project with main class, program and VM
arguments, environment variables and working directory, re-runnable from the
palette and editable in the new **Settings → Run Configurations** page. In a
Gradle project, saving one pre-fills the main class the build itself declares,
so the configuration matches what `gradle run` would launch rather than whichever
file is open. **Debug via Build Tool** covers the projects where the build tool
is the natural way to start the app: it launches Gradle `run`/`bootRun
--debug-jvm` or Maven `spring-boot:run` under a suspended JVM and attaches when
it is ready. See [Running and debugging](/docs/run-debug).

Clicking a **Java stack-trace frame** in the Run, Test or Build console now asks
the language server to resolve it, so a frame inside a dependency or the JDK
opens its source instead of reporting "not found", and the ▶ beside a JUnit class
is confirmed against the project's real test source folders, so a class in
`src/main/java` that happens to carry a `@Test`-shaped annotation no longer gets
a stray one.

Two fixes here matter as much as the features. Java debugging stopped hiding from
people whose jdtls already bundles the java-debug plugin (Homebrew's does), which
had been reporting debugging as unavailable and pushing Run onto the slower
fallback. And the Maven fallback now resolves sibling-module dependencies in a
reactor build, running from the reactor root with `-pl <module> -am`, so a
submodule that depends on an uninstalled sibling runs correctly.

## The Emacs model, finished

Editora has always had Emacs keybindings. 0.9.10 fills in the model underneath
them, as real features rather than shortcut aliases. See
[Emacs heritage](/features/emacs-heritage).

- **A kill ring.** `C-k`, `M-d`, `M-DEL`, `C-M-k`, `M-z` and friends used to just
  delete, so `C-k C-y` pasted whatever unrelated thing was on the system
  clipboard. They now feed a 120-entry ring that `C-y` yanks from and **`M-y`**
  cycles back through, with consecutive kills accumulating into one entry.
- **A mark ring.** `C-SPC` records the spot; **`C-x C-SPC`** pops back through
  older marks and around to where you started. Marks follow their text as you
  edit.
- **Narrowing** (`C-x n n` / `d` / `f` / `w`). Real narrowing, not a display
  filter: search, replace, macros and Select All see only the region. An amber
  **Narrowed** badge in the status bar widens on click, and saving always writes
  the whole file.
- **Rectangles** (`C-x r …`): kill, copy, yank, delete, clear, open,
  string-rectangle and number-lines over the columns between point and mark, each
  a single undo step.
- **Query-replace** (`M-%`, `C-M-%` for regexp), the replace-with-confirmation
  loop with `y`, `n`, `!`, `.` and `q`.
- **The `C-u` prefix argument.** `C-u 5 C-n` moves down five lines, `C-u 40 -`
  inserts forty dashes, `C-u C-u` is 16. Only the Emacs keymap binds it.
- **Abbreviations**, a text-replacement dictionary expanded on demand with
  `C-x a e` or automatically in Abbrev Mode, carrying your typed case onto the
  expansion.
- **Auto Fill mode**, breaking prose at the fill column as you type. Prose only,
  so it never wraps code.
- **Occur** (`M-s o`), **tabify / untabify**, and **align-regexp**.

## Everything else

- **Doctor**, a `flutter doctor` for Editora: one full-tab report of every
  external tool a feature depends on, from Git and `gh` through the language
  servers, debug adapters, preview tools and build tools, each row with the
  resolved command, its version, and an **Install…** button or a **Settings…**
  link when something is off. `View: Doctor`, or the Welcome page.
- **Select all occurrences** (`Ctrl+Shift+L` in the VS Code and Sublime keymaps)
  turns every occurrence of the selection into a cursor; **Alt+Enter** in the Find
  bar does it for every match of the query.
- **Multi-caret movement chords** now move every caret, matching what the arrow
  keys already did.
- **Copy with syntax highlighting**, so code pasted into Slack, an email or a
  document keeps its colors instead of arriving as a grey block. Plain text is
  always on the clipboard too.
- **Deeper folding**: fold by nesting level (Fold Level 1 to 7), fold or unfold
  the region at the caret and everything inside it, and jump to the parent, next
  or previous fold.
- **Subword navigation**, moving and deleting by camelCase and snake_case parts.
- **Expand and shrink selection** outward through syntactic levels and back.
- **Go to matching bracket** and **Select to Bracket**.
- **Convert indentation** between tabs and spaces across the whole file.
- **Preserve case when replacing** (the `AB` toggle) and **find in selection**
  (the `Sel` toggle).
- **Snippet regex transforms** (`${1/regex/format/flags}`), the VS Code
  `FormatString` in full.
- Smaller UI work: filter boxes in the Git Log and TODO windows, a clear (`✕`)
  button on the Find bar and Find in Files fields, `n` / `p` navigation in the
  TODO window, right-click Select All / Copy in the Build Output and Test Runner
  consoles, tooltips on the Test Runner's status icons, labelling an existing
  Local File History revision, and popping the AI Agent chat out into its own
  window.

## Performance

- **Incremental document sync.** Servers that accept incremental changes (most of
  them) now get only the changed splice of the document per typing pause instead
  of the whole text, with a periodic full resync as a divergence safety net.
- **Semantic highlighting transfers only what changed** on servers that support
  token deltas, so large Java files pay for their edits rather than their size.
- **Cheaper diagnostics bursts.** Matching a project-wide publish to its open tab
  used to re-resolve every tab's canonical path with filesystem syscalls on the UI
  thread; successful resolutions are now cached.
- **Replace All can no longer freeze the editor** on a valid but pathological
  regular expression. The incremental search was already time-budgeted; the
  replace walk now shares the same budget, abandons a runaway pattern and leaves
  the buffer untouched.
- **Auto Rename Tag** no longer builds the whole document into a string on every
  keystroke in an HTML or XML buffer, only when an edit really touches a tag name.
- **TODO and keyword highlighting** scans off the UI thread.
- **Abbrev expansion** reads a bounded window before the caret instead of the
  whole buffer.

## Fixes

Language servers:

- **The Maven-aware `pom.xml` server now actually starts.** It was enabled by
  default, offered by the installer, shown as configured and reported found by
  Doctor, while every `pom.xml` silently fell back to the plain XML server.
- **Inlay hints appear.** The request asked for a range ending one line past the
  end of the file, which a server answers with an empty result rather than an
  error. The same off-by-one applied to semantic-token range requests.
- **Signature help works with Java** (jdtls advertises the capability and ships
  the feature disabled), **is triggered correctly when bracket auto-close inserts
  `()` as one edit**, and now **tells the server which character triggered it**
  and whether it is a re-trigger.
- **Renaming a Java class moves its file again.** jdtls only emits the file
  rename when the client declares create, rename *and* delete resource
  operations.
- **Format Document no longer mangles the file**: a multi-edit format was applied
  top to bottom using offsets computed against the original text, which is
  exactly what re-indenting breaks. It also **formats what is on screen** rather
  than a copy up to 300 ms stale.
- **Go to Definition reaches JDK and dependency classes** instead of claiming
  there is no definition.
- **A crashed server restarts itself**, **servers shut down when their last file
  closes**, and **two windows on one Java project no longer share a jdtls
  workspace**.

Editing and search:

- **Multi-caret movement chords** move every caret, matching the arrow keys.
- **Regex capture groups work in the Find bar's replace.** `$1` was being
  inserted literally, while the same query in Find in Files substituted
  correctly. A replacement naming a group the pattern doesn't have now reports
  the error and leaves the buffer untouched.
- **Replace All no longer rewrites the whole document**, only the span from the
  first to the last match, so the untouched remainder stays out of the undo entry.
- **Snippet transforms are no longer discarded**, which had been quietly breaking
  the bundled PowerShell snippets, and **a snippet's value is no longer stolen by
  a leading mirror**.

Under the hood, the two classes that build every request Editora sends to a
language server gained in-process test seams and **121 new tests**: the wire
format, the capabilities declared at `initialize`, session lifetime, diagnostics
routing, and the rename path that writes and moves files, with a regression test
for each defect above.

The complete list is on the [What's New](/whats-new) page, and there is a
[0.9.10 blog post](/blog/editora-0-9-10) on the ones worth explaining rather than
listing.
