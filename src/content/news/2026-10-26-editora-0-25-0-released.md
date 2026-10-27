---
title: "Editora 0.25.0: Java support closer to an IDE, a gutter lightbulb, and Report a Problem"
description: "Editora 0.25.0 is mostly Java: refactorings with commands of their own, Generate and Refactor This, Undo Last Refactoring across files, Go to Super Method, Smart Type and chain completion, Lombok, clean-ups and format on save, one server per project across windows, and navigation inside library source. Also a code-action lightbulb and a problem counter in the editor, Doctor update checks, Settings sync for shortcuts, file structure in the Project Map, and a redacted Report a Problem."
date: 2026-10-26
version: "0.25.0"
---

**Editora 0.25.0** is out. Most of it is Java: ten review passes compared
Editora with IntelliJ IDEA on everyday Java work and closed the gaps they
found. Around that come a lightbulb and a problem counter in the editor,
Doctor update checks, and a way to report a problem without sharing anything
private. Download it from the
[0.25.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.25.0).

## Java

- **Refactorings have commands of their own.** **Generate…** lists the
  generators (getters and setters, constructors, `toString`, `equals` and
  `hashCode`, overrides, delegates), **Refactor This…** the refactorings at the
  caret, and Extract Variable, Constant, Field and Method, Introduce Parameter,
  Inline, Move, Change Signature and Override/Implement Methods each have a
  command, in a new **Code ▸ Refactor** submenu. The extractions work with
  nothing selected, and Rename opens on the name they picked.
- **Undo Last Refactoring** takes back a rename or move in every file it
  changed, moving renamed files back. Renaming a `.java` file or a package
  folder in the Project tree now renames the class or package, and Change
  Signature and Move list the files they will change before applying.
- **Go to Super Method** goes from a method to the one it overrides or
  implements, from a constructor to the super constructor, and from a type to
  its supertypes.
- **Completion that knows the type.** Smart Type completion lists only what
  fits the type expected at the caret, chain completion offers calls that
  produce it (`Collectors.toList()` inside `collect(`), and JUnit and Mockito
  statics such as `assertEquals` complete with their import.
- **Your project's setup reaches the server.** Lombok works, from your build
  cache or downloaded on request. Null annotations are checked. The import
  order in `.editorconfig` and the Eclipse formatter profile your build uses
  are followed.
- **On save, if you want it:** clean-ups (19 to choose from), organize imports
  and format. All are off by default.
- **One server per project.** A second window on a Java project joins the
  server the first one runs instead of importing the project again with
  another 1.5 GB.
- **Library source is navigable.** In a JDK or dependency tab, hover, Find
  References, the hierarchies and the other Go to commands now work, and Go to
  Symbol and Search Everywhere find JDK and library classes.
- The **IntelliJ IDEA keymap** uses IntelliJ's Java chords (Alt+Enter,
  Shift+F6, Alt+Insert, Ctrl+Alt+L and the rest), and the command palette finds
  commands by their IntelliJ and VS Code names, so "Reformat Code" or
  "Go to Test" finds the right one.

[More about Java](/docs/lsp#changing-the-code).

## In the editor

- **A code-action lightbulb** shows in the gutter when the line at the caret
  has a fix (red) or another quick action (amber). Click it for the list
  Alt+Enter shows.
- **A problem counter** at the top right of the editor shows the file's errors
  and warnings, or a green check, with arrows that go through them.
- **Complete Statement** (Ctrl+Shift+Enter in the IntelliJ keymaps) closes the
  line's brackets and adds the `;` or `{ }` it needs, in Java, C, C++, C#,
  JavaScript, TypeScript, PHP and Kotlin.
- **Surround With** wraps the selection in an `if`, `for`, `try` or another
  snippet that wraps.
- **Run Context at Caret** runs the test at the caret, else the class's
  `main`, else the file.

## Doctor and updates

- **Check for Newer Versions** asks each language server and tool Editora
  installs at its latest release whether a newer one is out, and offers
  **Update…** for those that are.
- Servers Editora pins (jdtls, Metals, the Kotlin server and others) get an
  **Update…** when a new Editora moves to a newer release.
- **About Editora** says whether you are up to date, with a **Check now**
  link.

[More about Doctor](/docs/troubleshooting#doctor).

## Report a Problem

**Help → Report a Problem…** prepares a bug report from the Debug Log, after
replacing home-folder paths, your user name, e-mail addresses and tokens. You
can read and edit it, and **Open GitHub Issue** opens a new issue with it filled
in. Editora sends nothing itself. When Editora will not start, `editora --report`
writes the same report from the command line. The log of the session before is
now kept, so a crash's log is still there when you reopen Editora to report it.
[More about reporting a problem](/docs/troubleshooting#reporting-a-problem).

## Also new

- **Settings sync carries your shortcuts:** key bindings you changed, in every
  keymap, merged chord by chord.
  [More about sync](/docs/settings-sync#shortcuts).
- **File structure in the Project Map.** A file row opens a column of its
  declarations, and a declaration opens a preview card of just its lines.
  Preview cards now pan and zoom with the map.
  [More about the Project Map](/docs/workspace#project-map).

For trying the Clojure debugger, the repository now has a small
[Clojure debugging sample](https://github.com/adriandeleon/Editora/tree/v0.25.0/samples/debug/clojure)
with a `break here` comment at every spot worth a breakpoint.

The full list is in the
[changelog](https://github.com/adriandeleon/Editora/blob/v0.25.0/CHANGELOG.md).
