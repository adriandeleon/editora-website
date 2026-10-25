---
title: Language servers (LSP)
description: Go to definition, code actions, Java refactorings, rename, signature help, inlay hints, code lenses, hierarchy, diagnostics, and completion from 23 language servers.
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
| Peek definition | `lsp.peekDefinition` | (palette) |
| Go to definition in a split | `lsp.gotoDefinitionInSplit` | (palette) |
| Go to implementation | `lsp.gotoImplementation` | `C-c l i` |
| Go to type definition | `lsp.gotoTypeDefinition` | `C-c l d` |
| Go to declaration | `lsp.gotoDeclaration` | (palette) |
| Find references | `lsp.findReferences` | `M-S-/` |
| Go to Symbol in Workspace | `lsp.gotoSymbol` | (palette) |
| Call hierarchy | `lsp.callHierarchy` | `C-c l c` |
| Type hierarchy | `lsp.typeHierarchy` | `C-c l h` |
| Next / previous problem | `lsp.nextProblem`, `lsp.previousProblem` | `M-g M-n`, `M-g M-p` |
| Show problem at caret | `lsp.problemAtCaret` | `C-c l e` |

The goto, references and hover commands are also in the editor right-click menu
while a server is active, under a single **LSP** submenu, and you can
**Ctrl/Cmd-click** a symbol to jump to its definition. **Go to Implementation**
and **Go to Type Definition** join that menu only when the server
supports them, so it never offers a dead entry.

The submenu does not change which actions a server contributes. A
fully-featured server offers up to eight. Grouping them keeps cut, copy, paste
and the spelling suggestions near the top of the menu, and labels entries such
as "Go to Definition" as coming from the language server.

When an implementation lives in the JDK or a dependency, Go to Implementation
lists every implementation in a picker, each with its package, and opens the
one you choose as read-only source.

**Find references** and Go to Implementation list multiple results in a browsable
**References** tool window (`tool.references`), grouped by file with a line and
preview; a single result jumps straight there. When the file has an open tab,
the preview is the reference's line with the editor's syntax highlighting, in
the active editor color theme; for a file with no tab the line is read from disk
in the background and filled in a moment later. Each file's header names its
folder, so two `Main.java` in different packages can be told apart, the summary
names what was searched ("5 references to area in 4 files"), and a reference
that writes the symbol (an assignment, `+=`, `++`) is marked **(write)**. The
declaration itself is not listed, so Find References on an unused symbol says
there are none. A request that fails or times out says so instead of "No
references".

**Go to Symbol in Workspace** opens a live search over every symbol in the
project, methods included, with JDK and library types after the project's own,
labelled with their module or jar. With a Java server running,
[Search Everywhere](/docs/navigation#search-everywhere) lists those library
classes too. Where no server is running, Editora's own
[project symbol index](/docs/code-navigation#go-to-symbol-in-project) answers the
same question.

**Peek Definition** shows the definition over the editor instead of taking you
to it, leaving your place, scroll position and tab count where they were; Enter
commits to the real jump. **Go to Definition in a Split** puts the definition in
a new editor group beside the code that referenced it. Both are covered in
[Code navigation](/docs/code-navigation).

**Definitions inside libraries work too.** A Java definition that lives in the
JDK or a dependency is fetched from the server and opened in a read-only,
highlighted tab on the declaration, not the start of its Javadoc. Inside that
tab the language server keeps working: Go to Definition, hover, Find References
(which finds your project's calls), Go to Implementation, Type Definition and
Declaration, Peek Definition and both hierarchies, onward into another library
class or back into your own code. Peek Definition on a library member shows its
source in the peek window rather than opening a tab.

**Call and type hierarchy** open in a **Hierarchy** tool window. Call hierarchy
shows who calls the method under the caret, or what it calls (flip with the
Callers / Callees toggle); a member called more than once is one row with its
count, as in `append(String) ×2`. Type hierarchy shows a type's supertypes and
subtypes, and on an interface it opens on the subtypes, its implementations.
Each level is fetched from the server as you expand it, and Enter or a
double-click jumps to any entry, a JDK or library one included.

**Next Problem** and **Previous Problem** move the caret to the file's next or
previous error, wrapping at the end (its warnings when it has no errors), and say
what it is; **Show Problem at Caret** says the problem under the caret, or the
worst one on its line.

## Reading the code

| Feature | Command | Default key |
| --- | --- | --- |
| Hover docs | `lsp.hover` | `C-c h` |
| Signature help | `lsp.signatureHelp` | `C-c l s` (also automatic) |
| Toggle inlay hints | `view.toggleInlayHints` | (palette) |
| Toggle code lenses | `view.toggleCodeLens` | (palette) |

**Hover docs** open in a popup at the caret. Long documentation, such as the
man page bash-language-server shows for a command like `ls`, scrolls inside a
popup no taller than part of the window.

**Signature help** pops the server's overload list when you type `(` or `,` in a
call: the active signature with the current parameter highlighted, an *n/m*
overload counter, and the signature's documentation (for Java, the method's
Javadoc, led by the current parameter's own `@param` text). It follows your typing as
arguments are entered, stays open across multiline calls, and lets you move
between overloads with `lsp.nextSignature` and `lsp.previousSignature`. It
closes when the caret leaves the call, on Escape, or on scroll. Finished
zero-argument calls and method references do not open an irrelevant signature
popup.

**Inlay hints** are the server's parameter-name and inferred-type annotations,
drawn in grey italics inline, at the position they describe. They are **off by
default**; turn them on in the **Language server features** card of Settings →
Code Completion or with `Toggle Inlay Hints`. A hint keeps the space the server
asks for, so a lambda's type reads `String s ->`. By default a parameter-name hint
appears only where the argument is a literal; **LSP: Set Inlay Hint Filter**
(`lsp.setInlayHintMode`) switches between that and hints on all arguments. Type hints are never filtered; in Java
they show the inferred type of a `var` and of a lambda's parameters. Arguments
named only by number (`List.of(e1: 1, e2: 2)`) get no hint. They cost nothing
while off.

**Code lenses** are the server's reference and implementation counts, drawn
after a declaration (`3 references`, `2 implementations`). A lens lights up
under the pointer, and a click opens what it counts. A references lens always
opens the **References** tool window with its first row selected, also for a
single reference and for `0 references`, which shows the empty list. An
implementations lens runs Go to Implementation. When a declaration has more
than one lens, a short list asks which one. They are **off by default**,
because each count is a search on the server; turn them on in Settings → Code
Completion ("Code lenses") or with `view.toggleCodeLens` (**View: Toggle Code
Lenses**).

**Occurrence highlighting** needs no command: rest the caret on a symbol and
every occurrence in the file gets a subtle wash, with writes (assignments) shaded
warmer than reads. It is semantic rather than textual, so a local `x` does not
light up an unrelated field `x`, and it clears the moment the caret moves.

**Diagnostics** appear as inline squiggles, in the **Problems** tool window
(`M-8`), and as marks on the minimap and scrollbar. A hint (ShellCheck's
"style" level, for example) is drawn as a faint dotted line rather than a full
squiggle, code the server marks as unused, such as a variable nothing reads,
is drawn faded, and a call the server marks as deprecated is struck through.
Those unused and deprecated marks are not listed in the Problems window.
**Completion** is merged into
the [autocomplete](/docs/languages#autocomplete) popup, and the **Structure**
tool window builds its outline from the server's document symbols, with real
kinds, per-kind icons and method signatures. A server that lists its symbols
flat, as bash-language-server does, has them nested under the function that
contains them, and a variable assigned several times in one scope is listed
once. In a `pom.xml` the outline names what the XML server lists only by
element: a `dependency`, `plugin`, `exclusion` or `extension` by its
`groupId:artifactId`, and a `profile`, `execution`, `repository` or
`pluginRepository` by its `id`, so five "dependency" rows become a list you can
pick from.

Each row in the Problems window ends with the rule's code when the server gives
one, for example `[SC2086]` (but not jdtls's numeric problem ids, which link
nowhere). Right-click a row for **Open Rule Documentation**,
which opens the rule's page (ShellCheck links every code to its wiki), and
**Copy Code**, which puts the code on the clipboard.

Java completion reacts immediately after member access and begins identifier
completion after a short pause. Its documentation popup shows the item's Javadoc;
the **Excluded types** in the Language server features card (vscode-java's list,
`sun.*`, `com.sun.*`, `jdk.*` and the like, plus `java.awt.*` by default) are not
offered, imported or listed by Go to Symbol and Search Everywhere; JUnit and Mockito
statics such as `assertEquals` and `mock` are offered without an import, which
is then added; and in an argument or assignment it also offers a call chain that
gives the expected type, such as `Collectors.toList()` inside `collect(`. The
list matches what you type against the name, not the parameter list. Enter and Tab accept the selected item without
also inserting a newline or indentation; complete result lists filter locally,
and obsolete requests are cancelled as you continue typing. Overloads remain
distinct, commit characters and server ranges are honored, and method
completion reuses parentheses already in the file before requesting signature
help. Auto-imports can safely follow continued typing and remain grouped with
the completion for undo and redo; an answer that can no longer be rebased is
dropped instead of touching newer text.

**Smart Type completion** (Edit → Smart Type Completion; Ctrl+Shift+Space in the
IntelliJ keymaps) opens the list with only what fits the type the code expects
at the caret, and the status bar names that type. Editora reads the type from:

- a declaration (`String s = `);
- an assignment, using the type the name was declared with;
- `return`, using the method's return type;
- a condition (`if (`, `while (`, after `&&`, `||` or `!`), which expects
  `boolean`;
- a call's argument, using the parameter that signature help shows.

The list keeps the fields, variables and methods of a fitting type, `null` for
an object, and `true` and `false` for a boolean. After `new`, it keeps the
classes that implement the type (`ArrayList` for a `List`). A class of your own
that implements or extends the expected type stays too, because jdtls ranks it
as a match. Where the code expects no particular type, the full list opens and
the status bar says so.

The Problems window has an **Open files / Whole project** selector
(`lsp.toggleProjectProblems`), plus a **Build Project** command
(`lsp.buildWorkspace`) that recompiles the Java project and fills it; it says
when the build found errors. With **Open files** selected, problems come only
from files you have open, so a compile error in a file you haven't touched is
not listed. Open files is the default, so the window lists more only when you ask
for it. With **Whole project** selected, saving a Java file runs a quiet
incremental build, so an error in a closed file clears once its cause is fixed.

Problems of the project rather than of a file, such as a JDK version the build
expects and the machine does not have, are reported after a Maven or Gradle
project is imported: said in the status line, and listed in Problems in either
scope.

## Changing the code

| Feature | Command | Default key |
| --- | --- | --- |
| Code actions / quick fixes | `lsp.codeActions` | `C-c l a` |
| Generate (Java) | `lsp.generate` | `C-c l g` |
| Refactor This (Java) | `lsp.refactorThis` | `C-c l t` |
| Undo Last Refactoring | `lsp.undoRefactoring` | `C-c l u` |
| Rename symbol | `lsp.rename` | `C-c l r` |
| Format Document (or the selection) | `lsp.formatDocument` | `C-c l f` |
| Organize imports | `lsp.organizeImports` | `C-c l o` |
| Clean Up File (Java) | `lsp.cleanUp` | (palette) |
| Smart Type completion (Java) | `lsp.smartTypeCompletion` | (palette) |
| Copy fully qualified name | `lsp.copyQualifiedName` | (palette) |
| Reload project configuration | `lsp.reloadProject` | (palette) |
| Re-indent as you type | `view.toggleOnTypeFormatting` | (palette) |
| Add imports for pasted Java | `view.togglePasteImports` | (palette) |
| Smart semicolon placement | `view.toggleSmartSemicolon` | (palette) |

**Code actions** ask the server what it can do at the caret or selection: quick
fixes for the diagnostics there, assists, and extract and inline refactorings.
The list **opens at the caret**, where you are looking, with the fixes first,
then assists, refactorings and source actions, each row labelled **Fix**,
**Assist**, **Refactor** or **Generate** in words, and a title the server offers twice listed
once. The fixes for a line's problem are offered from anywhere on that line, not
only on the squiggle. Typing narrows the list to titles containing what you type
(Backspace widens it again) instead of editing the code behind it; arrow keys or
`C-n` / `C-p` move, Enter applies, Escape or `C-g` dismisses. Every action shape is supported, including a server-side command whose
edits
arrive back over `workspace/applyEdit`. An edit to the open file is one undo
step; one that changes other files is applied and saved as one
[refactoring](#java-refactorings-that-ask-a-question), undone together. `Ctrl-.` / `Cmd-.` is bound in the VS Code, Sublime and IntelliJ keymaps;
the command is in the palette and the right-click menu in every keymap.

**Organize Imports** works the way the file's server does it: jdtls's own
request, Pyright's organize-imports command, or the server's organize-imports
code action. If the server has none of these, the status bar says so. On a
Python file **Format Document** uses Ruff or Black; see
[Formatting Python](#formatting-python).

A multi-file edit is applied **completely or not at all**. An action that would
delete a folder or replace an existing file asks first and names the paths, and
whatever an edit deletes or replaces is copied to
[local file history](/docs/workspace#local-file-history) beforehand. If Editora
is interrupted in the middle of an edit that moves or deletes files, the next
time the project is opened an **Interrupted Refactoring** dialog offers to put
the files back.

**Java code generation** has its own list, **LSP: Generate…** (`lsp.generate`,
Alt+Insert in the IntelliJ keymap): **Generate toString()**, **Generate
hashCode() and equals()**, **Generate Constructors**, **Generate Getters and
Setters**, **Generate Delegate Methods** and **Override/Implement Methods**
(`lsp.overrideMethods`, Ctrl+O in the IntelliJ keymap). For Java they are not
repeated in the quick-fix list. Each opens a checkbox list so you choose which
fields or methods to include, with Space to toggle and Enter to generate;
hashCode() and equals() starts with every field ticked, and Enter with nothing
ticked says so. Delegate Methods asks for the field to delegate to first, then
lists that field's methods. The server offers these only when the editor
declares that it can drive the picker, and Editora does.

**Refactorings have commands of their own**, in the **Code ▸ Refactor** submenu
and the palette: **Refactor This…** (`lsp.refactorThis`) lists the ones at the
caret, and **Extract Variable**, **Extract Constant**, **Extract Field**,
**Extract Method**, **Introduce Parameter**, **Inline**, **Move…** and **Change
Signature…** (`lsp.extractVariable`, `lsp.extractConstant`, `lsp.extractField`,
`lsp.extractMethod`, `lsp.introduceParameter`, `lsp.inline`, `lsp.move`,
`lsp.changeSignature`) each run the one jdtls offers there.

The extract refactorings work **at a bare caret**: with nothing selected Editora
asks the server which expressions are there and lets you pick one when there are
several. Extract to Field and Convert Local to Field ask where to initialize the
field when there is a choice. After an extraction, **Rename opens on the name the
server picked** (`d`, `getTotal`), so you can name it at once.

### Java refactorings that ask a question

Three refactorings in the Code Actions menu need more than the caret position,
so each one asks before it runs. They need the Java language server.

**Move** depends on what the caret is on:

| On | Moves it to |
| --- | --- |
| A class | another package, chosen from a list |
| A nested class | its own new file, or another class |
| A static member | another class |
| An instance method | one of its parameters or fields |

Moving a class lists the main source folders' packages before the test ones, and
ends with **New package…**: type a package that does not exist yet and its folder
is created under the current source folder. A Move Instance Method that jdtls
finds problems with shows them first and moves only if you continue.

**Extract Interface** shows the class's methods as a checkbox list, then asks
for the interface's name and its package.

**Change Signature** presents the method's signature as one line of text to
edit:

```java
public String greet(Helper helper, int n)
```

In that line you can rename the method, change its visibility or return type,
and reorder, rename, retype or remove parameters. To **add** a parameter, write
it as `type name = value`; the value is what existing callers pass for it:

```java
public String greet(Helper helper, int n, boolean loud = false)
```

A line that cannot be read as a method signature is rejected and nothing
changes. When Change Signature or Move changes other files, they are listed
first, ticked, the way a [rename](#rename-preview) is, and a file you untick is
left alone.

**A refactoring is one change across files.** Every file a refactoring renames,
moves or creates is saved, so the project compiles straight away, and a file it
opened only to edit is saved and its tab closed. Renaming or moving a `.java`
file in the Project tree is such a refactoring too: the class is renamed with its
file and the files that use it are updated. **Undo** in one of those files
offers to undo the refactoring in all of them, and **LSP: Undo Last
Refactoring** (`lsp.undoRefactoring`) does that from anywhere, moving renamed
files back and removing created ones. A rename onto a name that is already taken
is reported after it lands, with the way back.

Three commands sit outside the menu: **Organize Imports** sorts and prunes a
file's imports directly (in Java, JavaScript and TypeScript), **Copy Fully Qualified Name** puts the full name of the
symbol at the caret on the clipboard, and **Reload Project Configuration** makes
the server re-read `pom.xml` or `build.gradle`, also from the `pom.xml` tab
itself. A build file you save is picked up on its own, so Reload is rarely
needed. In Java, Organize Imports asks which class an ambiguous name such as
`Date` or `List` means.

**Rename symbol** renames across the whole workspace. The prompt comes pre-filled
with the current name, and the server validates the spot first, so you are told
up front when something cannot be renamed. Renaming a public Java class also
**moves its `.java` file**, and the tab, session state and language server all
follow to the new path. Renaming a `.java` file or a package folder in the
**Project** tree renames the class or package too, so references, `package`
lines and imports follow. `F2` is bound in the VS Code and Sublime keymaps, and
`Shift+F6` in the IntelliJ keymap.

<a id="rename-preview"></a>A rename that reaches **beyond the file you're looking at** shows you what it
will do first: every affected file, its change count, and where it moves to, with
a tick beside each one so you can leave a file out. The preview also lists any
file or folder the rename **would delete** and any existing file it **would
replace**, each marked as such. A rename confined to the current file applies
straight away, because the result is on screen and one undo away.

**Format Document** reformats the whole file through the server when it advertises
formatting (undoable, palette or right-click menu), including `.json`, `.css` and
`.html`. With a selection it formats only the selected lines. It tells the server the file's own indentation: the `indent_style` and
`indent_size` from [EditorConfig](/docs/editorconfig) when there are any, else
what the file already uses, not the global tab size. A two-space script stays
two-space. In a language whose server supports range formatting, **Tab** also snaps
the current line's indentation to the server's convention.

### Java-only assists

**Pasted code imports itself.** Paste a snippet into a Java file and the server
is asked which imports the pasted text needs; they are added for you, and only
for names the pasted text uses. Only the
language server can do this, because it needs the type resolver. An answer that
lost a race with your typing is dropped rather than applied to text it wasn't
computed for.

**Smart semicolon placement.** Typing `;` part-way through an expression puts it
at the end of the statement: `compute(1, 2|)` becomes `compute(1, 2);|`. The
semicolon is never held back waiting for the server, since that would make a
keystroke wait on a round trip. It lands where you typed it and moves afterwards
if the server disagrees, as a single undo step. If you keep typing (the very
common `;` then Enter), the document has moved past the answer, so the
correction is skipped rather than applied late.

Both need the Java language server running. Pasted-code imports are **on by
default**; smart semicolon placement is **off by default**. Switch either in
Settings → Code Completion, or with `view.togglePasteImports` and
`view.toggleSmartSemicolon`.

**Re-indent as you type** does the same on `;`, `}` and Enter. It is indentation
only, never a reformat of the line under you, and the local auto-indent still
acts first, so nothing feels slower and the server only corrects the cases the
heuristic gets wrong. Off by default: turn it on in Settings → Code Completion or
with `view.toggleOnTypeFormatting`.

**A Javadoc for the declaration below.** Typing `/**` above a method or class
closes the comment and offers the server's **Javadoc comment** template, with
`@param`, `@return` and `@throws` for that declaration. Pressing Enter instead
splits the comment as usual.

**Organize imports and format on save.** **Organize imports on save (Java)** and
**Format on save (Java)** in Settings → Code Completion → Language server
features tidy a Java file after you save it, and save it again when that changed
it, as IntelliJ's Actions on Save do. Both are off by default. Autosave never
runs them, since organizing imports while you type would remove one you have
just added, and an ambiguous import is left out rather than asked about.

**Clean-ups.** **Code → Clean Up File** applies the jdtls clean-ups you tick
under **Java clean-ups → Choose…** on the same Settings page: add `@Override`
and `@Deprecated`, remove redundant modifiers and `super()` calls, qualify
members, add `final`, use pattern-matching `instanceof`, switch expressions,
lambdas, try-with-resources and text blocks, and more (19 in all). The first
four are on by default; the others rewrite code, so they are yours to pick.
**Clean up on save (Java)** runs them after an explicit save, before Organize
Imports and Format.

### The Java server and your project

- **Which jdtls.** Install fetches jdtls **1.63.0**. [Doctor](/docs/troubleshooting#doctor)
  names the installed version and warns when it is older, with an
  [Update…](/docs/troubleshooting#update-for-servers-older-than-editoras)
  button that installs 1.63.0 and restarts the server on it.
- **Which JDK.** jdtls itself needs Java 21 or newer, and is given one Editora
  finds even when `JAVA_HOME` is older; when there is none, it says so. The
  default JDK for your projects, which Gradle also runs on, is Settings → Build
  Tools → Default JDK, else `JAVA_HOME`, else the newest JDK installed. A running
  jdtls keeps the JDK it started with, so changing it says to run **Restart
  Language Servers**.
- **A failed import says why.** When jdtls cannot import the project (a Gradle
  build that does not run, say), the status bar names the reason it logged and
  Problems lists the build file's own errors.
- **Your project folder stays clean.** jdtls keeps Eclipse's `.project`,
  `.classpath` and `.settings/` in its own workspace, not in your project.
- **Build files sync on save.** A change to `pom.xml` or `build.gradle` reaches
  the classpath when you save it.
- **One server per build.** Files of the modules of one Maven reactor or Gradle
  build share a server even without a project open. A plain folder of `.java`
  files with packages, and no build file, gets the right source folder as soon as
  a file opens. Java files keep their server up to 50,000 lines.
- **The project's own code style.** IntelliJ's import settings in the project's
  `.editorconfig` (`ij_java_imports_layout`,
  `ij_java_class_count_to_use_import_on_demand`,
  `ij_java_names_count_to_use_import_on_demand`) set the import order and when
  imports become `*`. The Eclipse formatter profile your build formats with
  (Spotless's `<eclipse><file>` or `eclipse().configFile(…)`, else
  `eclipse-formatter.xml` at the project root) is what Format Document uses.
- **Lombok.** For a project whose build declares Lombok, jdtls runs with it as
  its agent, using the newest Lombok jar already in your local Maven or Gradle
  cache, so the members Lombok generates are seen. When none has been downloaded
  yet, Editora offers to download Lombok from Maven Central and restarts the
  server with it.
- **Null analysis.** In a project with JSpecify, `javax.annotation`, Spring or
  Eclipse null annotations, jdtls checks `@Nullable` and `@NonNull` use.
- **A fresh start.** **LSP: Clean Java Workspace and Restart**
  (`lsp.cleanJavaWorkspace`) deletes the project's jdtls index and the
  classpath it worked out, then imports the project again: the cure for a stale
  or corrupt index.

## Folding and selection

Where a server is running, **code folding comes from the server's own
understanding of the file** instead of brace and indent scanning: an import block
folds as one region, javadoc and block comments fold, and multi-line expressions
nest correctly. [Expand and shrink
selection](/docs/editing#expand-and-shrink-selection) uses the same source, so it
respects strings and comments rather than guessing at brackets.

Both fall back to the built-in behaviour whenever LSP is off, the file is on a
[remote host](/docs/remote) that may not run commands, or the server offers neither, so a file is never less foldable with a server
than without one.

## While the server works

Servers that report long-running work through the LSP progress channel (jdtls
importing and indexing a project, gopls loading packages, rust-analyzer's first
check) drive the status-bar loading bar and show what they are doing, with a
title and percentage, in the echo area. Each message starts with the name of the
server it comes from, for example `bash-language-server: …`.

Startup ends with a line that says it is done, such as
`jdtls: Loaded and ready`. It appears once the server is ready and has had no work outstanding for
a moment; for jdtls that is after the project import, not at its own earlier
"Ready". After that, background work (diagnostics after an edit, a search) is
shown only when it is still running after that moment, and work that was shown
ends with another ready line, so the last line never reads as a job left
unfinished. A line that repeats the one just shown is skipped.

External file changes are forwarded to the running servers, so a server's
project model stays current after a `git checkout`, a branch switch, a CLI build
or an edit in an external editor. In a window without a project, and for a
server whose folder is outside the project, Editora watches the server's folder
itself (hidden folders, `node_modules`, `__pycache__` and virtual environments
aside), so a module created by `git` or a terminal resolves without a restart.
A server that dies mid-session (a crash, an
OOM-kill, a failed handshake) is announced in the status bar, has its stale
diagnostics cleared, and is restarted for the affected files, with a cap so a
server that keeps crashing is not relaunched forever.

A server that *answers* a request with an internal error is not mistaken for
one with nothing to say. The error is logged with the server's own detail, and
the first failure of each request kind is announced in the status bar, such as
`lemminx-maven: internal error on hover — see the log`. Hover then says
*Hover failed: …* with the reason instead of "No documentation at the caret".
Three failures in a row with no good answer between them stop the server: the
status bar says so, Doctor shows the server as a warning with the reason, and
*LSP: Restart Language Servers* (`lsp.restartServers`) tries it again.

## Servers are auto-detected, never bundled

Editora doesn't ship language servers. It looks for each one on your `PATH` (a
Settings field can override the command per server) and uses it if present.
Twenty-six servers are supported:

- **Java** (`jdtls`), **TypeScript / JavaScript** (`typescript-language-server`),
  **Python** (`pyright-langserver`), **Go** (`gopls`), **Rust**
  (`rust-analyzer`), **C / C++** (`clangd`), **C#** (`csharp-ls`)
- **Ruby** (`ruby-lsp`), **PHP** (`phpactor`), **Kotlin**
  (JetBrains' Kotlin LSP, `kotlin-lsp`), **Groovy** (gvy), **Clojure** (`clojure-lsp`),
  **Scala** (Metals), **Lua** (`lua-language-server`)
- **Astro** (`astro-ls`), **HTML**, **CSS**, **JSON**, **YAML**, **XML** (`lemminx`), **Bash**
  (`bash-language-server`), **Dockerfile**, **SQL** (`sqls`), **Terraform**
  (`terraform-ls`), **TOML** (`taplo`), **Typst** (`tinymist`)

There is also a **Maven-aware `pom.xml` server** (JVM lemminx plus the
lemminx-maven extension), shown as **lemminx-maven** in Settings, Doctor and
the status bar where the XML server is **lemminx**. It is routed by file name,
so a `pom.xml` gets dependency, plugin and GAV completion while every other XML
file keeps the fast native lemminx. It runs on the **Default JDK** chosen
under Settings → Languages & Tools → Build Tools (else the `java` on your
`PATH`), for Install… as well as for the server itself. Its root is the
outermost pom of a contiguous chain, so a multi-module reactor is served by one
JVM, also when it is opened without a project. The status bar says *Loaded and
ready* only once the server reports its own Maven initialization done, since it
answers the handshake long before it can answer a request. Its diagnostics are
named after the server, the schema's "resource is downloading" notice is shown
as information rather than an error, and `${…}` property items are left out of
an artifact completion unless you typed `$`.

Completion lists the artifacts in your local repository (`~/.m2`) only.
**Use the Maven Central index for pom.xml completion**, on the server's card
under Settings → LSP and off by default, lets lemminx-maven download Maven
Central's index, a large one-time download it then keeps up to date; changing
the setting restarts the server.

Its **Install…** downloads lemminx-maven's bundle and the three libraries the
bundle leaves out (Guava, Commons Lang and jsoup, pinned by version and
checksum, from Maven Central) into one folder, then starts the server and asks
it one hover request before keeping the install. A server that cannot answer is
refused with the reason, and the previous install stays in place. A folder that
lacks any of the jars (an install made with an earlier release, or a wiped
folder) does not count as installed: `pom.xml` falls back to the XML server,
the Settings → LSP card reads *lemminx-maven: incomplete — Install… repairs
it*, and [Doctor](/docs/troubleshooting#doctor) names the missing jars beside
an **Install…** button. If you installed this server with an earlier release,
click **Install…** once: that bundle lacked the three libraries and could not
answer a request.

Each server has a card on the **Settings → LSP** page: its enable switch, its
command field with **Browse…**, and a **Detected** line such as *jdtls: found
(/usr/bin/jdtls)* or *rust-analyzer: not found*, with an **Install…** button
while it is missing. A command left blank uses the program on your `PATH`. While
**Enable LSP (language servers)** is off, the cards are greyed and a note says to
turn it on. The switches for inlay hints, code lenses and the other server
features are in the **Language server features** card on Settings → Code
Completion; the LSP page links there.
[Doctor](/docs/troubleshooting#doctor) reports the same detection for every
enabled server in one place.

### Per-project overrides

A project can commit a `.editora/settings.json` naming **which server to run for
a language and whether to run it**, which overrides your global preference for
anyone who opens that project. It can also set the
[Bash server's options](#bash-server-options). This covers one repository needing a
JDK 17 server and another a JDK 25 one, where nobody has to remember to flip a
setting when switching between them. The same file can also set indentation,
rulers, build commands and debugger programs; see
[projects](/docs/workspace#settings-a-project-can-commit) and the full list in
[Configuration](/docs/configuration#inside-a-project).

Because those commands are programs that run on your machine, they apply only
to a **trusted folder**. In an untrusted folder the project's server commands
(and any attempt to re-enable a server you switched off) are ignored until you
run *Project: Trust This Project's Settings* (`lsp.trustProjectSettings`).

Settings → LSP shows your own commands. When the project in the window
overrides a server, that server's card says so: which command the project runs,
which one it asks to run until you trust it, or that it turns the server on or
off.

A remote project's `.editora/settings.json` is not read; set a server's command
for a host in Settings → Remote instead (see
[Language servers on a remote host](#language-servers-on-a-remote-host)).

## One-click install

If a server isn't installed, Editora can fetch it for you. **Every server** is
covered through one of these channels, picked per server:

- **npm** packages (Astro, JSON/HTML/CSS, Bash, YAML, Dockerfile, TOML, TypeScript,
  Python/Pyright), needing Node.
- **Toolchain** installs that use the language's own package manager (Go, Ruby,
  C#, Rust, PHP), which run only when that toolchain is already on your `PATH`.
- **Binary** releases downloaded per OS/architecture (clangd, lemminx,
  terraform-ls, Lua), extracted into your config folder.
- A **pinned release** for the Java server, Eclipse JDT LS 1.62.0, unpacked
  into your config folder. A server installed earlier keeps its version until
  you install it again.
- A **pinned distribution** for the Kotlin server, checked against its published
  checksum. It is about 370 MB to download and about 1.2 GB once installed,
  because it brings its own Java runtime, so Editora asks before it starts.
- A **JVM zip with its dependencies** for the Maven-aware `pom.xml` server, which
  needs a JDK, plus three jars from Maven Central that the zip leaves out, each
  checked against its published checksum. The server is started and asked one
  request before the install is kept.
- A **single jar** for the Groovy server (gvy), pinned to one release and checked
  against its published checksum. It runs on the JDK chosen for builds and runs
  (Java 17 or later).
- A **pinned native binary** for clojure-lsp, one per OS and architecture, each
  checked against its SHA-256 before it is unpacked. It needs no JVM.
- **Coursier** for Metals, the Scala server, which is published only as Maven
  artifacts: Editora downloads coursier's launcher (pinned, and checked against
  its published checksum), which fetches Metals' jars into your config folder.
  It runs on the JDK chosen for builds and runs (Java 17 or later).

There are three entry points: an **Install…** button per server in **Settings → LSP**, an
in-editor **banner** when you open a file whose server is missing (turn the nudge
off with `view.toggleInstallPrompts`), and the **Install: Language Server…**
picker (`install.languageServer`). After installing, the server is auto-detected
and activates without a restart. Editora never installs the underlying runtimes
(Node, Python, Go, …); if one is missing it tells you which to install first.

The Bash server lints with ShellCheck and formats with shfmt, which it does not
install itself. Editora installs those two separately; see
[ShellCheck and shfmt](#shellcheck-and-shfmt).

Installing over an existing version is safe to retry. A downloaded server is
unpacked beside the installed one and replaces it only once it is complete and
is the expected tool, so a failed or interrupted install leaves the working
version in place.

For Astro, startup also locates the TypeScript SDK required by `astro-ls`,
whether it is hoisted in the workspace or installed alongside the server by
Editora (TypeScript 6, as for the
[TypeScript / JavaScript server](#typescript-and-javascript)). A TypeScript SDK inside the project is loaded only from a trusted
folder; otherwise Editora uses the SDK beside the server, or does not start the
server until you trust the folder. The trust prompt lists the SDK.

## Groovy

The Groovy server is gvy. Install it from its Settings → LSP card, the banner
on a `.groovy` file, or **Install: Language Server…**. It has no command until
it is installed. The installer sets its command to `java -jar` with the
downloaded jar, and Editora runs that with the JDK chosen for Maven, Gradle and
Run, not the first `java` on your `PATH`.

It reports Groovy compiler errors, and offers completion, hover, go to
definition, references, rename, the outline and formatting. CodeNarc's style
rules are off: they flag nearly every line of ordinary Groovy.

A `Jenkinsfile` at a project's root makes [Doctor](/docs/troubleshooting#doctor)
expect the Groovy server, as an open `.groovy` file does. A `build.gradle` alone
does not, because most Gradle projects with a Groovy build script are Java
projects.

## Kotlin

The Kotlin server is JetBrains' Kotlin LSP. Install it from its Settings → LSP
card, the banner on a `.kt` file, or **Install: Language Server…**; Editora says
how large the download is and waits for your answer. The installer sets the
server's command to the downloaded launcher. Left blank, the command is
`kotlin-lsp --stdio`, the name Homebrew's `kotlin-lsp` gives it. A command you
set yourself is kept, so fwcd's `kotlin-language-server` still works there.

The server imports the project's Gradle or Maven build, so its root is the
folder with `settings.gradle.kts` (or `settings.gradle`), else the nearest
`build.gradle.kts`, `build.gradle` or `pom.xml`: every module of a multi-module
build shares one server. Once the import ends (a few seconds for a small
project, longer the first time Gradle fetches its plugins) it reports
compiler errors and warnings and offers completion, hover, go to definition,
references, rename, the outline, formatting and code actions.

The server is an alpha. It checks the `.kt` files in the build's source
folders; a `.kts` script, `build.gradle.kts`, and a `.kt` file outside a build
get no diagnostics or hover from it. A folder without a build file is not
imported at all.

## Clojure

The Clojure server is clojure-lsp. Editora uses the `clojure-lsp` on your `PATH`,
or installs its own copy from the server's Settings → LSP card, the banner on a
Clojure file, or **Install: Language Server…**. The install is the native build
for your platform (Linux and macOS on x64 and arm64, Windows on x64), pinned to
one release and checked against its SHA-256.

It serves `.clj`, `.cljs`, `.cljc` and `.edn` files with clj-kondo's diagnostics,
completion, hover, go to definition, references, rename, code actions, the
outline and formatting. To resolve the project's dependencies it asks the
project's own tool for the classpath (`clojure -Spath`, `lein classpath`,
`bb print-deps` or `npx shadow-cljs classpath`), which it finds on the same
`PATH` Editora gives every language server. Without that tool it still works on
the project's own files.

A server runs per project root: the folder with a `deps.edn`, `project.clj`,
`bb.edn`, `shadow-cljs.edn` or `build.boot`, else the repository. Any of those
files at a project's root also makes [Doctor](/docs/troubleshooting#doctor)
expect the Clojure server.

## Scala

The Scala server is [Metals](https://scalameta.org/metals/) 1.6.9. Install it
from its Settings → LSP card, the banner on a `.scala` file, or **Install:
Language Server…**. It has no command until it is installed: the installer
sets it to `java` with Metals' JVM options and the fetched jars, and Editora
runs that with the JDK chosen for Maven, Gradle and Run, not the first `java`
on your `PATH`.

Metals works on the build it finds at the workspace root: an sbt build
(`build.sbt`), a Mill build (`build.mill`, or a Mill 0.x `build.sc`), a Scala
CLI project (`project.scala`), a build already exported over BSP (`.bsp/`), or a
Gradle or Maven build. The first time it sees one it asks whether to **import
the build**; the question appears as a dialog, and the editor stays usable while
it is open. An import runs the build tool and compiles the project, which takes
a minute or more the first time. Metals shows its progress in the status bar.

It reports compiler errors, and offers completion, hover, go to definition,
references, rename, the outline and formatting (with a `.scalafmt.conf`). When
Metals asks for a value or a choice of its own, a text field or a picker opens.

A `build.sbt`, `build.mill`, `build.sc` or `project.scala` at a project's root
makes [Doctor](/docs/troubleshooting#doctor) expect Metals.

Any server's question with buttons gets the same dialog; before, such a
question went unanswered.

## Workspace roots and lifetime

A server runs once per workspace root. The root is the active project, else the
nearest build marker (`pom.xml`, `package.json`, `go.mod`, `Cargo.toml`, and so
on), else the file's directory. Files of the same language in one project share a
single server. Python looks for its own markers first; see [Python](#python).

A file directly in your home folder, the temporary-files folder or at the top of
a drive (`~/.bashrc`, `/tmp/x.sh`) has that whole folder as its root. Editora
tells the Bash server not to scan such a folder for other scripts, so opening
one loose file does not walk your whole home folder. The file itself still gets
diagnostics, completion and navigation.

Servers start **per tab, as you visit it**, rather than all at once while a
session restores, and they **shut down when their last file closes**, after a
three-minute grace period so reopening a file keeps the server warm.

**Two windows on one Java project share one Java server.** The second window
joins the jdtls the first one runs, so the project isn't imported twice and the
second window costs no extra memory (a jdtls takes about 1.5 GB). Both windows
show its diagnostics. A question the server asks, such as which class to import,
goes to the window you are working in. Closing one window leaves the server to
the other, and it stops when the last window closes. **Restart Language
Servers** in either window restarts it for both, and the other window reopens
its files on the new server. Projects on a remote host, in a container or in
WSL, and other language servers, still get one server per window.

jdtls keeps an index for every Java folder you have opened, under
`jdtls-workspaces` in your config folder: about 40 MB each, and often far more.
**LSP: Clear Java Index Caches** (`lsp.clearJavaIndexCaches`) deletes every
index that no running server is using and reports the space it freed; the ones
in use are kept and counted. jdtls rebuilds a project's index the next time the
project opens. It works with LSP turned off too.

A Java file without a `.java` name (an extension-less `java --source 25`
shebang script, or a file set to Java in the status bar) is shown to jdtls as a
temporary `.java` copy, since jdtls works only on files named `.java`. It gets
completion, hover, the outline and syntax errors, and an import a completion
adds goes below the `#!` line. The copy sits outside any project, so type
errors are not reported. A script in a remote folder is not given a copy.

## TypeScript and JavaScript

JavaScript and TypeScript files use **typescript-language-server**, which runs
TypeScript's own `tsserver`. **Install…** on its card (or *Install: TypeScript
/ JavaScript (Node.js) Language Support*, `install.jsSupport`) installs the
server with **TypeScript 6** beside it. The server runs on the same Node.js as
[Run](/docs/run-debug#nodejs-scripts).

**Why TypeScript 6.** TypeScript 7 is a new compiler written in Go. It has no
`tsserver`, so typescript-language-server cannot run on it. npm's newest
TypeScript is now 7, so Editora asks for TypeScript 6, the last version with a
`tsserver`. When the only TypeScript found has no `tsserver`, or there is none,
Editora does not start the server and says why, with the fix:
`npm install -g typescript@6`, or **Install…** on the card. It does not try
again each time you open another tab. [Doctor](/docs/troubleshooting#doctor)
and the card's **TypeScript** line show the version in use, or that it has no
`tsserver`.

**A folder's own TypeScript runs only once you trust the folder.** Many projects
have their own `node_modules/typescript`. That is code from the folder, so
Editora uses it only in a [trusted folder](/docs/workspace#trusted-folders),
and the trust prompt lists it. Until then, the TypeScript installed beside the
server is used. When the folder's own copy is the only one, the server waits
until you run *Project: Trust This Project's Settings*
(`lsp.trustProjectSettings`). On a remote host, the server still picks its
TypeScript itself.

**Type definitions are not downloaded.** Left to itself, the server downloads
`@types` packages from the npm registry for the libraries your JavaScript uses.
Editora turns that off. To turn it on, use **Download type definitions
automatically** on the TypeScript / JavaScript card in **Settings → LSP**. When
a folder has no Node.js types, the status bar suggests
`npm install --save-dev @types/node`, so hover and completion know Node's APIs.

**What it adds:**

- **Organize Imports** (`lsp.organizeImports`) sorts the imports and removes
  unused ones. The code-action menu also offers **Fix all**, **Add missing
  imports** and **Remove unused**, and quick fixes follow the file's own
  indentation.
- **Inlay hints** for parameter names, return types and enum values, when
  [inlay hints](#reading-the-code) are on.
- **Code lenses**: a references count on each function, when code lenses are
  on.

**Workspace roots.** Without a project open, a file's server root is the
nearest folder with a `tsconfig.json` or `jsconfig.json`, then one with a
`package.json`, then the Git repository. This gives each package in a monorepo
its own root. Scripts with none of these above them share one server, wherever
they are, instead of starting one each.

## Shell scripts

Shell scripts use **bash-language-server** (`bash-language-server start`).
It serves `.sh`, `.bash`, `.zsh` and `.ksh` files, shell dotfiles such as
`.bashrc` and `.profile`, and files whose shebang names a shell.

zsh startup files (`.zshrc`, `.zshenv`, `.zprofile`, `.zlogin`, `.zlogout`)
are not opened on the Bash server. ShellCheck has no zsh mode and would report
errors in valid zsh, so these files get highlighting and folding but no
language server. A `.zsh` script stays on the server, which does not run
ShellCheck on it.

### ShellCheck and shfmt

The Bash server finds problems with **ShellCheck** and formats with **shfmt**.
Without ShellCheck a script gets no diagnostics; without shfmt **Format
Document** changes nothing. Editora shows whether each one is there:

- The **Bash / Shell** card in **Settings → LSP** has a command field and a
  Detected line for each, for example *shellcheck: found (/usr/bin/shellcheck)*
  or *shfmt: not found*.
- [Doctor](/docs/troubleshooting#doctor) has a **ShellCheck** and an **shfmt**
  row under the Shell server.
- Opening a script without ShellCheck says so in the status bar, once per
  window. Format Document without shfmt says that shfmt was not found.

To install them, use the **Install…** button beside either line in Settings,
the Install… button in Doctor, or **Install: ShellCheck and shfmt**
(`install.shellTools`) in the palette, which installs whichever is missing.
Editora downloads the official release for your system (ShellCheck 0.11.0 and
shfmt 3.14.1, for Linux x64 and arm64, macOS Intel and Apple silicon, and
Windows x64). It checks each download against a SHA-256 hash built into
Editora and refuses one that does not match, so nothing is installed from a
changed or damaged file. The programs go into `plugins/tools` in your config
folder.

The **ShellCheck command** and **shfmt command** fields on the Bash card choose
which copy the server runs. Left empty, they use the copy Editora installed,
else the program of that name on your `PATH`. A path you enter always wins.
On a [remote host](#language-servers-on-a-remote-host), ShellCheck and shfmt
are the ones the host's `PATH` finds.

### Bash server options

The Bash / Shell card also holds the server's own settings:

| Setting | What it does |
| --- | --- |
| ShellCheck arguments | Added to every ShellCheck run, for example `-x` to follow sourced files or `--severity=warning` to hide notes and style hints |
| shfmt dialect | The shell syntax Format Document reads: Automatic (from the shebang), Bash, POSIX sh, mksh or Bats |
| Indent case branches, Put && and \|\| at the start of a line, Put a function's opening brace on its own line, Space after redirections, Simplify code when formatting, Keep column alignment | shfmt's style switches, all off by default |
| Ignore .editorconfig shfmt options | Use only these options, even where an `.editorconfig` sets shfmt properties |
| Scripts read in the background | How many workspace scripts the server reads for Go to Symbol and cross-file completion (500 by default; 0 turns it off) |
| Symbols from every workspace script | Complete, find and rename functions and variables from all scripts, not only from sourced ones |
| explainshell server | The address of an [explainshell](https://github.com/idank/explainshell) server, for hover help on a command's options. It is sent the command under the caret. Empty (the default) turns it off |

A project can set the same options for everyone who opens it, with a `bashIde`
object in its `.editora/settings.json` (see
[Per-project overrides](#per-project-overrides)). It uses the server's own key
names, the same ones VS Code's Bash IDE extension uses:

```json
{
  "bashIde": {
    "shellcheckArguments": "-x",
    "backgroundAnalysisMaxFiles": 2000,
    "shfmt": { "caseIndent": true, "languageDialect": "bash" }
  }
}
```

The keys are `shellcheckPath`, `shellcheckArguments`, `explainshellEndpoint`,
`backgroundAnalysisMaxFiles`, `includeAllWorkspaceSymbols`, and a `shfmt`
object with `path`, `languageDialect`, `simplifyCode`, `binaryNextLine`,
`caseIndent`, `funcNextLine`, `spaceRedirects`, `keepPadding` and
`ignoreEditorconfig`. A key the project leaves out keeps your own setting.
The ShellCheck path, the shfmt path and the explainshell server name a program
to run or a place to send your code, so they apply only in a trusted folder,
like a project's server commands; the other keys always apply.

### Where ShellCheck finds .shellcheckrc

ShellCheck looks for a `.shellcheckrc` file in the server's workspace root (the
project or repository folder, or for a loose script the folder it is in) and
the folders above it. A `.shellcheckrc` in a subfolder of the project is not
read, because the server hands ShellCheck the script's text rather than its
path. Put shared settings in the root
`.shellcheckrc`, and use `# shellcheck disable=SC2086` (or other
`# shellcheck` directives) in the script itself for anything that applies to
one folder or file.

## Python

Python files use **Pyright** (`pyright-langserver --stdio`). Language servers
are off by default, so the first Python file a window opens asks **Turn on
Python code intelligence?** with **Turn on** and **Not now**. The offer comes
once per window and follows the same switch as the install banners
(`view.toggleInstallPrompts`).

### The Python interpreter

Pyright analyses your code with a Python interpreter, and it can only resolve
the packages installed in that interpreter. Editora picks one interpreter per
file and uses it for Run, Debug, the tests, Pyright and Format Document alike.
The first match wins:

1. the **Interpreter** field of a [run configuration](/docs/run-debug), for that
   configuration;
2. the interpreter picked for this project (see below);
3. the environment Editora was started from (`$VIRTUAL_ENV`, then
   `$CONDA_PREFIX`), so activating a virtual environment before starting
   Editora works;
4. a virtual environment in the project: a `.venv`, `venv`, `env` or `.env`
   folder with a `pyvenv.cfg`, looked for from the file's folder up to the
   project root;
5. **Settings → Debugging → Python interpreter (fallback for Run, Debug and code
   intelligence)**;
6. the system Python on `PATH` (`python3`, then `python`; on Windows `py -3`,
   then `python`, never the Microsoft Store stub).

A status-bar item on Python files shows the result and why, for example
`Python 3.13.5 (.venv)`, `Python 3.12.4 (PATH)` or `Python 3.13.5 (picked)`.
Hover it for the full command. Click it, or run **Python: Select Interpreter**
(`python.selectInterpreter`), to choose another one: **Detect automatically**
(the order above), the one in use, the project's environments, every Python on
`PATH`, or **Browse…** for any file. The pick is kept per project; inside the
project folder it is stored as a relative path. In a window without a project
it applies to every loose Python file in that window.

Pyright is told the interpreter, so the packages in a project's `.venv` resolve
with no `pyrightconfig.json`. A changed pick reaches the running server at once.
A `pyrightconfig.json` or a `[tool.pyright]` section with `venvPath` and `venv`
still wins inside Pyright, and settings such as `pythonVersion` or
`typeCheckingMode` belong there too.
[Doctor](/docs/troubleshooting#doctor) shows the same interpreter in its
**Python interpreter** row.

### What Pyright adds

- **Unused names are faded** (an import or a variable nothing reads) and
  **calls to a deprecated function are struck through**. These marks are drawn
  in the editor but not listed in the Problems window, where they would bury
  the real errors.
- **Organize Imports** (`lsp.organizeImports`) sorts and groups the imports
  with Pyright's own command. It leaves unused ones in place; they are the
  faded ones.
- **Go to Definition** into the standard library, site-packages or Pyright's
  bundled stubs opens that file in View mode, on the project's server, and
  without the library's own type errors. **Enable Editing** in the View mode
  bar makes it editable.
- An **untitled** buffer set to Python is analysed too. In a project window it
  sees the project's modules. Once you save it, its file takes over.
- A Python file keeps its server up to **50,000 lines**, even above the
  **Disable minimap & LSP above** limit (10,000 lines by default), which then
  turns off only the minimap.

The Python server's root is the nearest folder with a `pyproject.toml`,
`setup.py` or `setup.cfg`, else the repository (`.git`), else the nearest
`requirements.txt` or `Pipfile`. A `docs/requirements.txt` therefore does not
split the documentation off from the package it documents. In a project window
the project root is the root.

To use another server, set the Python command in **Settings → LSP**, for example
`basedpyright-langserver --stdio`. If that command is not found, the banner
names it and offers **Settings…**, rather than installing Pyright.

### Formatting Python

Pyright does not format. **LSP: Format Document** (`lsp.formatDocument`) on a
Python file runs **Ruff** (`ruff format`) or, failing that, **Black**, giving
them the editor's text with the file's name so they read the project's
`pyproject.toml`. Each is looked for first in the interpreter's environment
(the project's `.venv`), then on `PATH`, in this order: the environment's Ruff,
the environment's Black, Ruff on `PATH`, Black on `PATH`. The result is one
undoable edit, and the status bar names the tool, for example
`Formatted with ruff`. It works with language servers off too.

If neither is installed, the status bar says so; install one into the project's
environment (`pip install ruff`). A Python server that formats by itself, such
as `ruff server`, keeps the job. Doctor's **Python formatter** row shows which
tool Format Document will use.

## Language servers on a remote host

A file in a [remote folder](/docs/remote) is served by a language server running
on its host, once that host
[may run commands](/docs/remote#allowing-a-host-to-run-commands): completion,
hover, diagnostics, go to definition, rename and the rest work as for a local
file. The server is the one the host's `PATH` finds. To use another, add a
`server = command` line for that host under **Language servers** in Settings →
Remote, for example `python = basedpyright-langserver --stdio`. A command set in
Settings → LSP is used on the host only when it is a bare program name rather
than a path on this machine.

- All files on the project's host share the project's server, including a file
  outside the project folder, such as a library source reached by Go to
  Definition.
- Renames and other cross-file edits create, move and delete files on the host.
- **Python**: Pyright on the host is told the project's virtual environment as
  the host names it, found there the way [Run](/docs/run-debug) finds it, else
  the host's own `python` tool. The status-bar item names it, for example
  `python (.venv)`, without running it on this machine. If the connection
  drops, the host's own `python3` is used.
- **Java** works too: jdtls runs on the host with the host's JDKs and keeps its
  index in `~/.cache/editora` there.
  *LSP: Clear Java Index Caches* clears only the indexes on this machine.
- Losing the connection stops the host's servers without reporting a crash, and
  reconnecting starts them again.
- The Astro server does not run on a host yet.

## Language servers in a container or WSL

A project whose tools [run in a container](/docs/remote#tools-in-a-container)
or, on Windows, [in a WSL distribution](/docs/remote#tools-in-wsl-windows) has
its language servers run there, each the one the container's or distribution's
`PATH` finds, and told about the files by the names they have inside. A command
set in Settings → LSP is used there only when it is a bare program name.

- When the project's tools move into a container or back out, a server started
  on the other side is stopped and its files reopen on the right one.
- **Java**: jdtls runs inside with the JDKs found there, and keeps its index in
  `~/.cache/editora` inside, so it is kept across restarts of Editora for as long
  as the container lasts. *LSP: Clear Java Index Caches* clears only the indexes
  on this machine.
- **Python**: Pyright inside is told the project's virtual environment by the
  name it has inside, as on a remote host.
- The Astro server does not run in a container or distribution yet.

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
when you want it. Java can [organize imports and format on
save](#java-only-assists); other languages cannot yet.

The protocol library is **LSP 3.18 / DAP 1.70**. Two fields it widened affect
Editora. A diagnostic's message may be markup rather than plain text;
the markup's text is used, so a squiggle always carries its explanation. A
document edit may be a **snippet** edit carrying `${1:name}` placeholders.
Editora **refuses** one rather than applying it, since inserting it as plain
text would write the placeholder markup into your file. That is the same
all-or-nothing rule that already covers file creates and deletes, and in
practice no server sends one, because Editora does not advertise the
capability.
