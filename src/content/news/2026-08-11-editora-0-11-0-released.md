---
title: "Editora 0.11.0: start a project, not just open one"
description: "A New Maven Project wizard, git init from inside the editor, inlay hints that sit where they belong, and typing that stops getting slower the longer you leave the editor open."
date: 2026-08-11
version: "0.11.0"
---

**Editora 0.11.0** is out. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**New Maven Project.** Until now Editora could only ever *open* an existing
folder as a project, which made it a poor place to begin one. There is now a
wizard: pick an archetype, fill in the coordinates, and the generated project is
registered and opened in its own window. Archetypes come from a curated list that
ships with the editor, with an option to pull Maven Central's full catalog when
you need something unusual. Where both list the same archetype the curated pin
wins, because the published catalog is often years out of date. The package name
is derived the way IntelliJ does it, so a project called `2048` still produces a
legal Java identifier.

The project arrives ready to run. A run configuration is seeded for its main
class with `mvn -q compile` as its before-launch step, and that class is opened,
so Run works on the first press rather than failing on an empty `target/classes`.

**Git: Initialize Repository.** You could clone a repository but not start one,
so a folder opened as a project had to be taken to a terminal for `git init`.
Now it is a command. Creating a repository inside an existing one is refused and
names the enclosing root, since that is nearly always a mistake and awkward to
undo.

**Create any kind of file from the Project tree.** Right-clicking a folder opens
a single **New** submenu covering about fifty file types, grouped by family and
each carrying the icon the file will have once it exists. A new Java file takes
its package declaration from where you put it, so "New Class" in
`src/main/java/demo` writes `package demo;`. The name you type always wins over
the type's extension, so `notes.json` under Text File gives you JSON.

**Inlay hints sit where they belong.** They used to be parked at the end of the
line, so a call with several arguments produced a run of grey text whose only
clue to which hint went with which argument was left-to-right order. Each hint
now renders inline at the position it describes, gently pushing the code after it
aside. They are still annotations rather than text, so selecting or copying a
line gives you exactly what is in the file.

They also stopped labelling everything. `System.out.println("Hello")` used to
earn a grey `x:`, because the JDK declares `println(String x)`, which tells you
nothing and is the common case rather than an edge one. Names that explain
nothing and names that merely repeat their argument are now always hidden, and
you can choose whether the rest appear on literal arguments only, which is the
new default, or on all of them.

**Typing no longer gets slower the longer you leave Editora open.** Every
keystroke was permanently leaking two animation timers through the 80-column
ruler, which measured the caret in a way that made the editor allocate a
throwaway caret whose blink timer nothing ever stopped. They accumulated and were
never released, so the median keystroke went from 6.4 ms at 500 keystrokes to
28.4 ms at 2,000 and never recovered. It now stays flat at about 2 ms however
long you type, and a benchmark counts the timers so it cannot come back.

**The packaged app uses about a third less memory.** It shipped with the serial
garbage collector, picked on the reasoning that a mostly-idle editor would get
very short pauses and a smaller footprint from it. Measured on a real session,
none of that held: it sized a 548 MB young generation it never handed back
against a live set of roughly 72 MB, and had all of that to copy on every
collection. Switching to G1 with periodic uncommit took resident memory from
1130 MB to 738 MB and the longest pause from 138 ms to 34 ms, with startup
unchanged. A 138 ms pause is eight dropped frames on the typing path.

**Java completion stopped leaving placeholder text in your files.** A language
server can send a completion as a snippet, and Editora was flattening every one
of them into literal characters. Accepting `java.util` produced
`import java.util.*;` with the caret past the semicolon, and accepting a method
left its arguments as text to delete by hand. Placeholders are now real, so
typing replaces them and Tab steps through the rest.

**Filter and keyboard-navigate the tool windows.** The Commit window and the
Maven, Gradle, npm, Cargo and Go task trees now open with a filter box focused,
the way Bookmarks and Personal Notes already did. `C-n` and `C-p` move the
results without your hand leaving the filter field, in every filterable tool
window. The Commit window's file list also became multi-select, so you can stage,
unstage or discard a run of files in one action, and one `git` invocation.

Also: copying the Markdown preview now puts formatted HTML on the clipboard
alongside the plain text, so one Copy pastes properly into Word or Gmail. Each
tool window remembers its own size instead of sharing one number per side.
Sliding along the menu bar switches menus again on Linux, where a menu's own
drop shadow was covering the bar. And a long status bar message no longer
squashes everything beside it into ellipses.

The complete list is on the [What's New](/whats-new) page.
