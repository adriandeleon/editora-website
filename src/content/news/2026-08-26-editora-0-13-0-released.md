---
title: "Editora 0.13.0: a navigation release"
description: "Search Everywhere, symbol navigation with no language server, ranked pickers, sticky scroll, Peek Definition and preview tabs — plus 255 MB less peak memory."
date: 2026-08-26
version: "0.13.0"
---

**Editora 0.13.0** is out. It is a code-navigation release: four gaps that
mattered, closed together. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

Navigation is judged on two things. **Reach** — can the thing be found at all?
**Flow** — can you jump without losing your place? Editora's plumbing was in
good shape on both, and the fundamentals underneath them were not: nothing was
ranked, symbols existed only if you had installed a language server, five
separate finders each asked you to decide *what kind* of thing you wanted before
you could type its name, and every jump cost you your place.

## One picker instead of five

**[Search Everywhere](/docs/navigation#search-everywhere)** searches commands,
project files and symbols at once, so you can type the *name* of the thing
instead of first choosing which finder it lives in. A leading `>`, `#` or `@`
restricts it to commands, files or symbols for when you do already know — VS
Code's sigils rather than invented ones, since the muscle memory already exists.

Results stay **grouped by source** rather than interleaved on raw score, which
sounds like a presentation detail and isn't. The sources differ in size by
orders of magnitude — tens of thousands of symbols, thousands of files, a few
hundred commands — so a flat merge hands the whole list to whichever is biggest
and the other two effectively disappear. Each source gets a guaranteed share,
the groups compete on their *best* result rather than their bulk, and the overall
cap trims a group's tail instead of dropping a source outright.

## Symbols without a language server

LSP is off by default and needs a server you install yourself, so for a first-run
user — or for any of the twenty-odd languages that ship a grammar but have no
server — "go to symbol" simply did not exist.

**[Go to Symbol in Project](/docs/code-navigation#go-to-symbol-in-project)** now
works on a first run, from Editora's own declaration scanner, across sixteen
language ids. A running language server remains the better answer and still
takes precedence; this is what there is when there isn't one.

The index is built **lazily, on first use**. Walking every file the moment a
project opens spends real work for someone who may never ask it anything, and
asking for a symbol is the justification. The first query pays for the walk —
under a second on a 650-file project — and after that it is incremental,
rescanning exactly the file you saved from the text already in memory.

It **under-reports on purpose**. A missing declaration costs you one fallback to
search; an invented one sends you somewhere that does not exist and teaches you
not to trust the feature.

## Everything is ranked now

The pickers only ever asked whether a candidate matched, never *how well*, so a
scattered accidental hit ranked alongside a clean prefix one — and recent files,
open buffers, bookmarks, snippets, projects, themes and LSP references were not
ranked at all, showing matches in source order.

[The new matcher](/docs/navigation#how-matching-works) scores contiguity, word
and camelCase boundaries, exact case and compactness, matches whitespace-separated
terms in any order (`toggle git` finds *Git: Toggle Blame*), and knows a path's
basename from its directories. The alignment is computed by dynamic programming
rather than greedily, which is the difference between `mc` meaning the two humps
of `MainController` and meaning whichever `c` happens to come first.

Rows now embolden the characters actually responsible for the match. The command
palette previously ranked with one algorithm and emboldened with another, so the
highlight could point at characters the ranking had never considered.

## Jumping without losing your place

- **[Sticky scroll](/docs/code-navigation#sticky-scroll)** pins the enclosing
  scope headers above the viewport. A header already on screen is not pinned —
  that would cover the real line with a copy of itself — and when the chain is
  deeper than the cap the *outermost* entries are kept, because the innermost
  scope is the one you can infer from the code in front of you.
- **[Peek Definition](/docs/code-navigation#peek-definition)** shows a definition
  over the editor and leaves your place, scroll position and tab count where they
  were. Most of the time the question is only a signature and a few lines of
  body — an answer that does not justify the move. Enter still commits to the
  real jump.
- **[Preview tabs](/docs/code-navigation#preview-tabs)** make browsing cost one
  tab, not one per glance. The reused slot's title is italic — the same "not
  durable yet" convention the unsaved marker uses. An already-open file is never
  demoted into it: it earned its place by an earlier deliberate open.
- **[Recent Locations](/docs/navigation#recent-locations)** lists the session's
  trail newest-first with **the line you were on**, which is the thing you
  actually lost when a jump took you elsewhere. It reads the same trail Back
  walks, so the two can never disagree.
- **Picker preview** shows the highlighted row in the editor as the selection
  moves, and dismissing the picker puts everything back. The undo is half the
  feature — without it, arrowing through a list and pressing Escape leaves you
  wherever the cursor stopped.
- **[Go to Related File](/docs/code-navigation#go-to-related-file)** pairs a file
  with its counterpart — a test and the class it tests, a header and its
  implementation, a component and its stylesheet. These are conventions rather
  than facts, so Editora proposes candidates in preference order and offers only
  the ones that exist; a convention being wrong therefore costs nothing, which is
  what lets the list be generous.
- **[Go to Definition in a Split](/docs/code-navigation#go-to-definition-in-a-split)**
  puts the definition in a new editor group and leaves the origin where it was.
- **[Bookmark mnemonics](/docs/bookmarks-notes#mnemonics)** give a bookmark a
  single character that jumps to it from anywhere in the project. A mnemonic is
  unique within a project — `3` means one place, not one place per file — because
  a name that resolves to several locations is a menu, not a shortcut.

## Windows finally shows up in "Open with"

Windows hands a file manager's chosen file to an application as a command-line
argument, and nothing maps an extension to Editora unless the installer says so
— so until now the MSI installed an editor that no file manager could hand a
file to. It now registers for the
[text and source types it edits](/docs/cli#opening-files-from-the-file-manager).

It does not take over your existing defaults: on Windows 8+ the shell's per-user
choice wins over anything an installer writes, so a type you already open with
something else keeps opening with it. An extension nothing else claims does fall
to Editora, which is the wanted outcome for the likes of `.tfvars`.

The Linux `.deb` also now **describes itself properly in GNOME Software** —
name, summary, description, MIT licence, homepage and release notes — instead of
"Unknown License", "No details for this release" and a generic gear icon. None of
that came from the desktop entry, which was already correct: a software centre
reads AppStream metadata, and the package shipped none.

## Less memory, faster start

**Peak memory is down from ~908 MB to ~653 MB** on a four-file session, measured
on the installed package against a copy of a real configuration. The initial heap
was never pinned, and the JVM's default for it is 1/64 of physical RAM *clamped
up to* the maximum heap — so on a large-RAM machine the whole 2 GB heap was
committed before the application started. Stop-the-world pauses got *better*
rather than worse (19.6 → 4.2 ms median), because a small young generation copies
far less per collection.

The [undo history](/docs/undo-history) and the decoded-image caches are now
bounded **by size, not by count**: keeping 50 whole-document snapshots is not a
memory bound when the entries differ in size by three orders of magnitude. On a
472 KB file, 50 edits cost 50 MB before and 34 MB after.

Startup also reaches its first frame **~46 ms sooner**, by entering through a
plain launcher so the configuration can load on a background thread while the
JavaFX toolkit starts.

The complete list is on the [What's New](/whats-new) page.
