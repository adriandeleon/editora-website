---
title: Code navigation
description: The project symbol index, sticky scroll, Peek Definition, preview tabs, related files, and jumping without losing your place.
category: Navigation
order: 2
---

Moving around code is two questions. **Reach**: can the thing be found at all?
**Flow**: can you jump without losing your place? This page covers both — the
server-free symbol index that answers the first, and the peek, preview and
related-file moves that answer the second.

For pickers, find, and project-wide search, see
[Navigation & search](/docs/navigation).

## Go to Symbol in Project

**Go to Symbol in Project** (`index.gotoSymbol`) finds a class, method or
function anywhere in the project **with no language server installed**. Editora
carries its own declaration scanner, so the question works on a first run and in
every language that ships a grammar but has no server.

Sixteen language ids are covered: Java, Kotlin, C#, Python, JavaScript,
JSX, TypeScript, TSX, Go, Rust, C, C++, Ruby, PHP, shell and Lua. The symbols it
records are the kinds a pattern can identify honestly — type, interface, enum,
method, function, field, variable, module — rather than LSP's twenty-six, most
of which need real type resolution to tell apart.

The same index backs the file and symbol halves of
[Search Everywhere](/docs/navigation#search-everywhere) and
[Go to Related File](#go-to-related-file).

### It is built lazily

The index is **not** built when a project opens. Walking every file the moment
you open a folder spends real work on behalf of someone who may never ask it
anything; asking for a symbol is the justification. So the first query pays for
the walk — under a second on a 650-file project, announced in the status bar so
it doesn't look like a hang — and after that it is incremental: saving a file
rescans exactly that file, from the text already in memory.

There is no second filesystem watcher; the [project tree](/docs/workspace)
already runs one. For files changed outside the editor, **Rebuild Symbol Index**
(`index.rebuild`) scans the project again from scratch.

It honours your `.gitignore` the same way Find in Files and the project tree do,
skips files over 2 MB, and is bounded (20,000 files, 400,000 symbols) — an index
that can grow without limit in the background is a memory leak with a feature
attached.

### It under-reports on purpose

A missing declaration costs you one fallback to search; an invented one sends
you somewhere that doesn't exist and teaches you not to trust the feature. When
the scanner isn't sure, it says nothing.

The index is a **floor**, not a competitor. Where a
[language server](/docs/lsp) is running it is better at this in every respect,
and **LSP: Go to Symbol in Workspace** (`lsp.gotoSymbol`) remains the better
answer there.

Turn it off in **Settings → Editor → Display** ("Project symbol index"), or with
`view.toggleSymbolIndex`. Disabling it releases the project's symbols.

## Sticky scroll

**Sticky scroll** pins the enclosing scope headers above the viewport, so deep
in a long method you can still see what it belongs to — the class, the method,
the block. Clicking a pinned row jumps to it, which is the other half of the
feature: the header tells you where you are, and it is also the way back.

Two rules make it stay out of the way:

- **A header already on screen is not pinned.** Pinning it would cover the real
  line with a copy of itself.
- **When the chain is deeper than the cap (5 rows), the *outermost* entries are
  kept.** The innermost scope is the one you can infer from the code in front of
  you; the file-level type has been off screen longest.

Rows are rebuilt from the highlighting the editor has already applied, so they
match what's on screen and cost no extra tokenizing. It's on by default —
**Settings → Editor → Display**, or `view.toggleStickyScroll` — and off in
[large-file mode](/docs/troubleshooting#performance) along with the other per-viewport work.

## Peek Definition

**LSP: Peek Definition** (`lsp.peekDefinition`) shows a definition *over* the
editor and leaves your place, scroll position and tab count exactly where they
were.

Go-to-definition answers "what is this" by moving you, and most of the time the
question is only a signature and a few lines of body — an answer that doesn't
justify the move. The peek shows a couple of lines of context and a dozen of the
definition, syntax-highlighted. **Enter** commits to the real jump, so nothing is
taken away; **Esc** dismisses it.

A library target with no file to read degrades to the full go-to-definition
rather than reporting a failure.

Needs a [language server](/docs/lsp) for the file.

## Go to Definition in a Split

**LSP: Go to Definition in Split** (`lsp.gotoDefinitionInSplit`) puts the
definition in a new [editor group](/docs/workspace#editor-groups-two-files-at-once) and leaves the
origin where it was — the pair you usually want on screen together is the call
and the thing called.

A definition in the *same file* jumps without splitting: there's only one tab, so
the split would move it and leave the group it came from empty.

## Preview tabs

Browsing costs one tab, not one per glance. Arrowing through a picker, or
following a definition to see what something is, reuses a single slot whose title
is **italic** — the same "not durable yet" convention the unsaved marker uses.

It becomes a permanent tab when you commit to it:

- **editing the file** promotes it, and
- **opening it explicitly** (from the file finder, the project tree, anywhere
  that isn't a glance) promotes it.

An already-open file is **never demoted** into the slot. It earned its place by
an earlier deliberate open, and quietly making it disposable would lose it on the
next glance at something else. A file with unsaved changes is never reused
either.

## Go to Related File

**Go: Related File** (`nav.relatedFile`) pairs a file with its counterpart — a
test and the class it tests, a header and its implementation, a component and its
stylesheet. It works in both directions, and coming from a test the subject
leads.

| From | Offers |
| --- | --- |
| `Foo.java` | `FooTest.java`, `FooTests.java`, `FooSpec.java`, `FooIT.java` |
| `FooTest.java` | `Foo.java` |
| `test_foo.py` | `foo.py` |
| `foo.c` | `foo.h` |
| `foo.cpp` | `foo.hpp`, `foo.hxx`, `foo.h` |
| `Button.tsx` | `Button.css`, `Button.scss` |
| `app.js` | `app.css`, `app.scss`, `app.html` |
| `x_test.go`, `x.test.ts`, `x.spec.ts` | `x.go`, `x.ts` |

These are conventions rather than facts, so Editora proposes candidate names in
preference order and offers only the ones that **exist** — a convention being
wrong therefore costs nothing, which is what lets the list be generous. A
counterpart rarely sits beside its partner (a test lives under `src/test`, a
header under `include`), so the sibling directory is only the first place looked;
the [symbol index](#go-to-symbol-in-project) knows every file in the project and
finds the rest.

One match opens. Several offer a picker, since `Foo.css` and `Foo.scss` can both
exist and only you know which was meant.

## Keeping your place

Three things work together here, and each is documented where it lives:

- [**Back and forward**](/docs/navigation#back-and-forward) steps through the
  jump list.
- [**Recent Locations**](/docs/navigation#recent-locations) lists the session's
  trail with the line you were on, and previews as you arrow through it.
- [**Bookmarks**](/docs/bookmarks-notes) mark places you want to return to
  deliberately, and a **mnemonic** turns one into a single chord.

None of these commands has a default chord — bind the ones you use from
**Settings → Keymap**.
