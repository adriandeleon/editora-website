---
title: "Editora 0.9.10: the half of LSP that writes to your files"
description: "Reading from a language server is easy. Acting on what it says means editing your code, and that is where the interesting bugs live: an ordering rule in the spec, a capability nobody declared, a feature the server ships switched off, and an off-by-one that made a whole feature look like it had nothing to say."
date: 2026-07-26
author: Adrian De Leon
tags: [release]
---

Editora has spoken the Language Server Protocol since 0.9.1, and until this
release it spoke the comfortable half of it: go to definition, find references,
hover, diagnostics, completion. Those are all reads. The server answers, Editora
draws something, and if the answer is wrong you get a bad jump or a missing
squiggle.

0.9.10 implements the other half, the half that writes to your files: code
actions and quick fixes, rename across the workspace, organize imports, extract
and inline refactorings. Along the way it also picked up signature help, inlay
hints, occurrence highlighting, call and type hierarchy, and folding that comes
from the server's parse rather than from counting braces. Four of the bugs found
on the way are worth writing down, because each one failed in a way that looked
like the feature simply had nothing to offer.

## Edits have an order, and it is not the obvious one

A code action rarely comes back as one edit. Re-indenting a file comes back as
dozens, one per line, each with the range it applies to.

Editora applied them top to bottom, which is how a human would read them. That
is wrong, and the specification says so: every edit's range is computed against
the *original* document, so the first edit that changes a line's length shifts
every later edit onto the wrong place. Applying bottom to top means each edit
lands before any offset it depends on has moved.

The failure mode is nasty because it is proportional. A format that only changes
whitespace on one line works perfectly. A format that re-indents a whole file,
which is exactly what you reach for Format Document to do, mangles it. The same
code path backs quick fixes and multi-file rename, so both were affected, and
both are now covered by the fix.

## A capability you do not declare is a feature that does not exist

Renaming a public Java class should also rename `Foo.java`. Editora asked jdtls
to rename the symbol, jdtls renamed the symbol, and the file stayed put.

The reason is a single line in the `initialize` handshake. jdtls only emits the
file-rename operation when the client declares that it supports create, rename
*and* delete resource operations. Editora declared rename alone, which reads as
the obviously sufficient subset, so the server quietly downgraded what it sent
back. No error, no warning: just a rename that did less than you asked.

The same shape, from the other direction, hid signature help. jdtls advertises
the capability and ships the feature disabled, so `(` in a Java call produced
nothing until Editora set `java.signatureHelp.enabled` in its initialization
options. Editora already carried a per-server initialization-options table for
exactly this reason, because the VS Code JSON, CSS and HTML servers also hide
their formatter behind a flag.

This is a whole class of defect with no natural detection: a dropped or missing
capability fails no build, throws no exception, and produces a feature that is
merely absent. The client-side half now has tests that assert what Editora
declares at `initialize`, so a capability cannot go missing without a red test.

## The empty answer that meant "you asked wrong"

Inlay hints never worked. Not "worked badly": the request came back with an
empty list every single time, on every server, for every file.

Covering line *n* of a document normally means an exclusive end position of
`(n+1, 0)`. For the last line, that names a line the document does not have.
jdtls answers an out-of-range range with an empty list rather than an error,
which is indistinguishable from "this file has no hints". On a 27-line file,
asking to `(27, 0)` returned zero hints and asking to `(26, 0)` returned six.
The same off-by-one was in the semantic-token range request, where it had been
hiding as "this server just does not send many tokens".

The investigation is the part worth keeping. An audit pass ruled the range out
as a cause after testing nine live-server configurations, all of which read
zero. Its in-range control was itself out of range, because
`split("\n", -1).length` is the line *count* and the last index is one less. Nine
careful measurements, all agreeing, all wrong, and the conclusion they supported
was "the servers do not implement this".

## Nothing was checking what went on the wire

The four defects above have something in common: none of them is a logic bug
inside Editora. Each is a detail of what Editora *sends*, and none of them could
be caught by any test that existed, because the two classes that build every
request were reachable only by forking a real language server. Their line
coverage was 3.2% and 17%. The pure mappers sitting right beside them, at 91% to
100%, had produced no defects at all.

So the classes got in-process test seams, a recording fake server, and 121
tests: the wire format of every request, the capabilities declared at
`initialize`, session lifetime (idle eviction, crash recovery, per-project
workspaces), per-buffer gating, diagnostics routing, and the rename path that
writes and moves files. Each of the four defects has a regression test. There is
no behaviour change in any of it, which is the point.

One more found by the same sweep, and my favourite: the Maven-aware `pom.xml`
language server had been enabled by default, offered by the in-app installer,
shown as configured in Settings and reported as found by Doctor, while every
`pom.xml` silently fell back to the plain XML server. The map of server ids to
commands was hand-written and had drifted to 22 entries against 23 ids. The one
it omitted resolved to an empty command line, so that server could never start,
no matter what you did in the UI. It is derived from the id list now, and a test
pins the two together.

## Run now means your project

The other half of the release is that Run stopped meaning "this file".

Editora could run a single-file script: a Java compact source file, a Python
script, a shell script. Real work is a project, so **Run Main Class…** and
**Debug Main Class…** now pick any main class in the active file's Maven or
Gradle project, and a green ▶ sits in the gutter beside every
`public static void main`.

There are deliberately two paths under that. The fast one asks the Java language
server for the project's main classes and its resolved classpath, which is also
the only way to debug, since a debugger needs the exact classpath and a JVM it
controls. When the language server is not set up, plain Run falls back to the
build tool: Maven resolves the classpath through
`mvn compile dependency:build-classpath` and Gradle delegates to its `run` task,
or `bootRun` for a Spring Boot project. The fallback learned one thing the hard
way, which is that a multi-module build has to run from the reactor root with
`-pl <module> -am`, or a submodule that depends on an uninstalled sibling fails
on a dependency that is sitting right there in the same repository.

Run configurations are saved per project with program and VM arguments,
environment variables and a working directory, and there is a Settings page to
edit them rather than only a palette prompt. **Debug via Build Tool** covers the
Spring Boot and Gradle applications where the build tool is how the app is meant
to start: launch it under a suspended JVM, watch the output for the JDWP banner,
attach when it appears.

## The Emacs model, not the Emacs keymap

The third thread in this release is smaller to describe and was the most
enjoyable to build. Plenty of editors ship an Emacs keymap that maps a handful of
chords onto their existing commands. 0.9.10 implements the concepts underneath:
a kill ring, a mark ring, narrowing, rectangles, query-replace, the `C-u` prefix
argument, abbreviations, auto-fill.

Two of them have a detail worth stealing if you ever build the same thing.

**Consecutive kills have to be judged before the edit, not after.** In Emacs,
`C-k C-k C-k` then `C-y` gives you all three lines back, because consecutive
kills accumulate into one ring entry. Deciding "was this a consecutive kill?"
inside the function that pushes onto the ring is too late, because the kill's own
edit has already advanced the document version the comparison uses. The check
never matches, accumulation silently never happens, and every unit test still
passes because each kill individually is correct.

**Narrowing is safe by inversion, not by auditing.** Narrowing the buffer to a
region really does replace the editor's text with that region, which is the whole
point: search, replace, macros and Select All must see only the region. That
leaves about twenty-five callers that mean "the whole file", including save,
autosave, the language server sync, diff and local history. Rather than find and
fix each one, the accessor they all already call returns the whole document, and
the narrowed portion is what you have to ask for specially. The dangerous default
became the safe one and none of those callers changed.

## The rest of 0.9.10

More than fits an essay, so here it is grouped.

**More from the language server.** Occurrence highlighting, which washes every
use of the symbol under the caret and shades writes warmer than reads. Call and
type hierarchy in a new Hierarchy tool window, each level fetched as you expand
it. Go to Implementation, Type Definition and Declaration, offered in the
right-click menu only where the server supports them. Definitions inside JDK and
dependency classes, opened as read-only source you can keep navigating from.
Folding regions and the expand/shrink selection ladder taken from the server's
parse instead of brace counting. Real progress reporting, so jdtls importing a
project or gopls loading packages says what it is doing instead of showing a bare
spinner. External file changes (a `git checkout`, a CLI build) forwarded to the
running servers so their project models do not go stale. And a server that
crashes now restarts itself, servers shut down a few minutes after their last
file closes, and two windows on one Java project no longer contend for a single
jdtls workspace.

**The rest of the Emacs model.** A per-buffer mark ring (`C-SPC`, `C-x C-SPC`)
whose marks follow their text through edits. The `C-x r` rectangle family, each
command one undo step. Query-replace (`M-%`) and query-replace-regexp with the
usual `y`, `n`, `!`, `.` and `q` answers. The `C-u` prefix argument. An
abbreviation dictionary that carries your typed case onto the expansion. Auto
Fill mode, prose-only so it never wraps code. Occur (`M-s o`), tabify and
untabify, and align-regexp.

**Editing and search.** Select all occurrences, which turns every occurrence of
the selection into a cursor at once (`Alt+Enter` in the Find bar does it for
every match of the query), and the movement chords now move every caret rather
than just the primary one. Copy with syntax highlighting, so code pasted into
Slack or a document keeps its colors instead of arriving as a grey block. Folding
by nesting level, plus fold and unfold recursively and jump to the parent, next
or previous fold. Subword navigation through camelCase and snake_case parts.
Convert a file's indentation between tabs and spaces (leading whitespace only, so
alignment and string contents survive). Go to matching bracket and select to
bracket. Snippet regex transforms, the VS Code `FormatString` in full. Preserve
case when replacing, so a rename across mixed-case usages takes one pass instead
of three. Find in selection, picked up automatically from a multi-line selection.

**Smaller things.** Doctor, the health screen for every external tool a feature
depends on. Filter boxes in the Git Log and TODO windows, a clear button on the
Find bar and Find in Files fields, `n`/`p` navigation in the TODO window,
right-click Select All and Copy in the Build Output and Test Runner consoles,
tooltips on the Test Runner's status icons, labelling an existing Local File
History revision, and popping the AI Agent chat out into its own window.

**Performance.** Three of these are language-server work: incremental document
sync, so a typing pause sends the changed splice rather than the whole file;
semantic token deltas, so a large Java file pays for its edits and not its size;
and diagnostics bursts that no longer re-resolve every tab's canonical path with
filesystem syscalls on the UI thread. Elsewhere, Replace All can no longer be
frozen by a pathological regular expression (the incremental search was already
time-budgeted; the replace walk was not), Auto Rename Tag stopped building the
whole document into a string on every keystroke in an HTML or XML buffer, TODO
highlighting scans off the UI thread, and abbrev expansion reads a bounded window
before the caret rather than the whole buffer.

**And the fixes that did not fit above.** Regex capture groups now work in the
Find bar's replace, where `$1` had been inserted literally while the same query
in Find in Files substituted correctly; Replace All rewrites only the span from
the first to the last match, keeping the untouched remainder out of the undo
entry; a snippet's value is no longer stolen by a leading mirror, and snippet
transforms are no longer discarded, which had been quietly breaking the bundled
PowerShell snippets; Java debugging no longer reports itself unavailable when
your jdtls already bundles the java-debug plugin; and a Gradle run configuration
pre-fills the main class the build declares rather than whichever file is open.

The [release note](/news/2026-07-26-editora-0-9-10-released) has the organized
list and the [What's New](/whats-new) page has all of it.
