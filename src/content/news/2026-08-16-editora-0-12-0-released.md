---
title: "Editora 0.12.0: panels that go where you want, poms you can read"
description: "Tool windows maximize, re-dock, share a side, and float into their own window. A pom.xml previews as a summary that resolves the versions it can and admits the ones it can't, and Maven can bring an existing project's versions up to date — showing you the diff first."
date: 2026-08-16
version: "0.12.0"
---

**Editora 0.12.0** is out. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

**Tool windows go where you want them.** Until now a tool window lived on the
stripe it was assigned to, at whatever size that side happened to be. Now it can
be **maximized** over its split, **re-docked to another stripe by dragging its
button**, **shared with a second window on the same side**, and **floated** into
a window of its own. Maximize and float have a header button each, all three are
palette commands, and the header's right-click menu carries the set.

A side holds at most two windows — Project over Structure, say. Left and right
split vertically; the bottom splits *horizontally*, because two consoles side by
side is the only arrangement where both stay readable. Floating windows are
owned by the editor, so they float above it and minimize with it, and they
remember their bounds — but reuse them only when they still overlap a screen, so
a window last saved on a monitor you have since unplugged is re-centred rather
than opened somewhere you can't reach.

Maximize is expressed as divider positions rather than by moving the panel, so
focus, the stripe button's state and every remembered size survive it. It is
also deliberately never saved: a maximized divider persisted as that window's
size would have it reopen next session covering the editor, and then do it again
every launch after that.

**A `pom.xml` reads like a summary, not a tree.** The generic XML rendering
spreads every dependency over four nested rows, which is a faithful view of the
file and a poor view of the project. A pom now previews as coordinates and
parent, then modules, properties, dependencies, managed dependencies, plugins,
managed plugins, and each profile's own set — the artifact and its version in
aligned columns, so the versions read as a column instead of a hunt.

The point is the two indirections that otherwise send you back up the file. A
`${property}` version is shown **resolved**, with the reference kept beside it,
and a blank version is filled in from the file's own `<dependencyManagement>` and
tagged *managed*. What the file cannot answer, it says: no parent pom is read, so
a version inherited from a parent reads *inherited* rather than a number Editora
would be inventing. It is a reading of one file, not an effective pom. The XML
tree is one toggle away.

**Maven can update an existing project's versions.** `maven.updateVersions`
checks the nearest pom's dependencies and plugins against Maven Central and
**shows you what would change before writing anything** — a row per artifact,
current → latest, behind a dialog you can decline. The update is applied through
the open buffer as a single edit, so one `C-z` takes the whole thing back.

Maven Central's "latest release" marker is deliberately not trusted, because it
means "newest non-snapshot published" and for `maven-surefire-plugin` that was a
milestone build for years. A version is never walked *backwards*, which an
unguarded set-to-latest does to a pom that was pinned on purpose. And a
property-driven version is skipped rather than rewritten, since replacing
`${junit.version}` with a number throws away the indirection you chose.

The same option is offered up front in the **New Maven Project** wizard, which
gained a collapsed **Advanced** section: the project `<url>`, the Java release
(a combo of the JDK majors you actually have installed), and the
update-to-latest checkbox. Empty means "keep what the archetype wrote".

**A Maven submenu**, on a `pom.xml` in the editor and on a folder or pom row in
the Project tree. One builder serves every surface, so they can't drift into
offering different actions, and it offers nothing at all when there's no pom.

**An LSP submenu.** A fully-featured language server contributes up to eight
actions to the editor's right-click menu, which flat pushed cut, copy, paste and
the spelling suggestions far enough down to hunt for — and a bare "Go to
Definition" sitting between "Paste" and a spelling suggestion named nothing
about where it had come from. Which actions a server offers is unchanged; the
submenu is only their container.

**Fixes.** Recent files that no longer exist are no longer offered — an entry
that can't open is worse than no entry, though remote entries are never checked,
since that is a network round trip per entry every time the menu is built and it
answers "gone" for a host that is merely asleep. Creating a Maven project beside
an existing one no longer fails outright. Launching `Editora notes.txt:42` no
longer opens the file *and* reports a failure, which is what happened when macOS
delivered the same argument twice and the two paths parsed it differently.
Find in Files stopped showing two different icons for one feature. The tool
stripe's drop-zone highlight no longer sticks for the rest of the session after
a drag. And every debug launch stopped logging a parse failure for a reply that
was correct — nothing was broken except the log, which is the problem, since a
routine failure firing every time trains you to ignore the one that matters.

The complete list is on the [What's New](/whats-new) page.
