---
title: "Editora 0.14.0: a chrome release"
description: "The menu bar can share the title bar, three bars of chrome are reconsidered, Search Everywhere grows up into a real palette replacement, and toggling Simple UI mode got about four times faster."
date: 2026-08-29
version: "0.14.0"
---

**Editora 0.14.0** is out. It is a chrome release: the window gives rows back to
the editor, the chrome it keeps stops disagreeing with itself, and the two focus
modes finally remove the chrome they always claimed to. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

Chrome is the part of an editor you are not looking at. That makes it easy to
add and easy to leave wrong — every bar is a row of pixels charged to the file
you actually opened, and every icon is something the eye has to read and then
discount. This release went through it row by row.

## The menu bar can share the title bar

Turn on *Merge the menu bar into the title bar* under **Settings → Interface**
and the window loses its system title bar; the menus, the window title and the
system buttons share that one row. A full bar of vertical space, back.

What does **not** change is how the window behaves. Minimise, maximise, close,
drag-to-move, double-click-to-maximise, edge resize and snapping all remain the
platform's own — only where the menus sit is different. That is the whole reason
it is feasible: the alternative was an undecorated window, which means
reimplementing every one of those by hand and getting them subtly wrong on three
operating systems.

It is **experimental**, off by default, applies on restart, and is **Linux and
Windows only**. macOS is excluded and not for want of API — the menu belongs to
the system bar at the top of the screen there, so drawing it inside the window
would be a worse window rather than a taller one. The checkbox is disabled on
macOS rather than hidden, so its note still explains why.

## Three bars, reconsidered

The **breadcrumb** names the active file, and it lived in the bottom bar stack,
spanning the whole window between the tool stripe and the status bar — where it
read as a second status bar describing the window rather than a label for the
file. It now hangs under the editor area itself: as wide as the editor, directly
under the text it names. It also collapses your home directory to a single `~`
crumb that still navigates there. Spelled out, every file under `$HOME` opened
with two crumbs identical on every file you will ever have open, so the segments
that actually distinguish *this* one began a third of the way along a bar that
scrolls.

In the **status bar**, indent, line endings and encoding are now one segment —
`Tab 4 · LF · UTF-8`. They are three facts about how the same file is written to
disk, and they were three separately divided segments of an eleven-segment bar.
They remain three click targets with three tooltips, because they run three
different commands, and they now appear only when a file is open at all: the
Welcome tab no longer reports a tab size and a charset describing nothing.

The echo line starts **empty** rather than on a standing "Ready". A placeholder
present on every frame that says nothing trains the eye to skip the one region
real messages land in.

The **toolbar** loses cut, copy and paste from the default layout — in a
keyboard-driven editor they are the three actions nobody reaches for with the
mouse — and About and Quit entirely. Both of those are in the menus and the
palette, neither is a mid-edit action, and Quit sat in the top-right corner
directly beneath the window's own close button. Cut, copy and paste remain
catalog items, one drag away in **Settings → Toolbar**; a bar you have already
customized is untouched.

The run-configuration group moved to the **right end**, beside the project
switcher, the way an IDE pins its run widget — and it is pinned there, which
turns out to matter more than the placement.

## A narrow window took the wrong things away

A toolbar overflows from its end, and the fixed group — project switcher,
Recent, the build badges, Settings — was the last thing on the bar. So on a
narrow window those went into the overflow chevron while fourteen icons stayed
put. Measured at about 1250 logical pixels: an ordinary 1280-wide laptop, where
the whole group collapsed to a `»`.

That is exactly backwards. Every icon in the cluster is also in the menus and
the palette; the project switcher and Settings are not. The tail is pinned now,
and a window short of space takes the icons instead. It is also why the run
controls are no longer draggable items — the control that starts a run is the
wrong thing to lose to a window width.

## Icons that mean one thing

The **debug icon was a sun**. A circle with eight rays, and that is how it read
in the toolbar's run cluster, the Run menu and the test panel — in every case
beside a style class named for a bug, and beside the editor's own right-click
Debug item, which already showed one. The application disagreed with itself
about what debugging looks like. It is a bug now.

The default toolbar is also **one icon family** again. Open, Open Folder and
Save As were still filled glyphs in a row of outlines, the Open folder in
particular being the single solid shape there. And New From Template had been
borrowing the generic document icon, which is the New File glyph minus its
little plus: two adjacent buttons, both meaning "new", differing by one cross.

**Main-menu items now carry icons** too — the same glyph you see for that action
in a right-click menu, so Save looks like Save wherever you reach it from. It is
partial on purpose: about ninety of the roughly 135 entries have one, and the
rest are left blank rather than given an invented glyph. A picture that does not
depict the thing beside it has to be read and *then* discounted, which is worse
than an empty space. The File menu's export block is the clearest case, where
one tray-and-arrow would have meant "PDF", "Word" and "HTML" equally, and so
meant nothing.

## Search Everywhere can replace the palette now

[Search Everywhere](/docs/navigation#search-everywhere) shipped in 0.13.0 as one
picker over commands, files and symbols — and with two properties that quietly
made it a worse command palette than the command palette.

It had **no chord in any bundled keymap**. The only way to reach the picker that
replaces the other pickers was to open the command palette and type its name. It
is now `M-S-x` in Emacs, sitting beside `M-x` where the mnemonic explains
itself, and `Ctrl`/`Cmd`+`Shift`+`E` in the CUA, Sublime, VS Code and IntelliJ
keymaps.

And it **omitted** commands whose feature was switched off, where the palette
lists them greyed with an explanation. Omitting is tidier in a short mixed list,
which is why it was built that way — but it also means you never learn the
command exists, and a picker you cannot learn the product from is a worse
palette than the one it stands in for. Those rows are listed now, greyed, naming
the setting that would enable them, sorted after everything you can actually
run, with the cursor stepping over them.

Two more of the palette's habits came along: the **highlighted row's
description** under the list, and **`C-h`** to open that command's
documentation. The hint line names them, along with `C-n`/`C-p` — it had been
advertising three of the keys the picker accepts and staying quiet about the
rest, which in a keyboard-first editor is where those keys go to be discovered
or not at all.

An **empty query now lists every command** rather than showing a blank box, and
a single-source query is **no longer capped**. A `>` search used to return at
most eight commands. The cap exists so a large source cannot drown a small one;
with one source in play there is nothing to drown.

With all of that true, you can now let it **take over the palette's shortcut** —
*Settings → Interface → Pickers*. It is off by default, because which picker a
chord opens is muscle memory, and that is something you change rather than
something a release does to you.

## Focus modes that actually remove chrome

**Zen and Expert mode left half the toolbar on screen.** The bar is two
containers on one row, and only the first was ever hidden — so a focus mode
whose entire point is that the chrome goes away stripped the icons and left the
run controls, project switcher, Recent, badges and Settings sitting there, over
a bar-height strip of reserved empty space. The row that holds both is what gets
hidden now.

**Simple UI mode**, meanwhile, keeps a menu bar — simplified rather than hidden:
File, Edit, Find, View and Help. The menus that go are exactly the ones Simple
mode switches off, which would otherwise have stood there entirely greyed out,
worse than not being offered at all. A menu is the browsable map of what the
editor can do, and the mode aimed at someone new to the editor is the one that
needs a map most. Toggling Simple UI mode is deliberately kept in that reduced
View menu, so the mode is never a one-way door for anyone who entered it from
there. Its toolbar button also **reads as engaged** now, the same way the find,
palette and split toggles do.

## Faster

**Toggling Simple UI mode blocked the interface for 230–550 ms; it is now
48–108 ms** (median 233 → 55), over five open source files. None of the four
causes was work that needed doing: collapsing every fold rewrote the fold
shading on every region in the file, the 80-column ruler was re-measured for
every open buffer including background tabs, the gutter was rebuilt twice per
buffer, and applying the editor theme removed and re-added the same stylesheet.
All four are guarded now, which helps **every** settings apply — a theme change,
any checkbox — not just this one.

Then the ruler again from the other direction: it had been listening for
viewport changes, which fire on every vertical scroll and every *edit*, neither
of which can move a ruler whose position depends only on glyph width, gutter
width and horizontal scroll. Since every keystroke was paying for a measurement
it could never need, that one is a typing win as much as a chrome one.

**Search Everywhere did a corpus-sized sort on every keystroke.** Both of its
big sources scored every candidate, sorted *all* the matches, then threw away
everything past the fortieth — and a one-character query matches most of a
corpus, so that sort ran over nearly everything, on the UI thread, between one
keypress and the next. Each source now keeps a bounded heap of the best forty as
it scans. At roughly this project's size **7.2 ms → 2.0 ms**; at ten times it,
102 ms → 23 ms; at the index's cap, 316 ms → 59 ms. A frame is 16.7 ms, so the
first of those was about 40% of a frame per keystroke and is now nearer 12%.
Results are unchanged — same rows, same order, pinned by tests that compare
against the previous algorithm directly.

Typing in it also rebuilt the entire keymap **once per matching command**, which
for a one-character query against six hundred commands meant building the same
map hundreds of times before a single row was drawn. Once per query now, which
is what the palette had always done.

## Also fixed

- **Dragging the minimap moved the document in whole-line steps.** It scrolled
  to a paragraph, and a paragraph is the smallest thing it could land on, so on
  a long file the document lurched a line at a time with stalls between:
  consecutive pixels of travel gave deltas of `16, 0, 16, 16, 0, …` where the
  mapping wanted about two-thirds of a line per pixel. It scrolls in document
  pixels now, and the same travel gives `10, 9, 10, 9, …`. Pressing the viewport
  box also used to put its *top* under the cursor, throwing the document most of
  a viewport before it began tracking; it takes hold of the box now. And the
  **mouse wheel over the minimap did nothing at all** — the column is a sibling
  of the scroll area rather than a child, so the event reached nothing
  scrollable.

- **Opening the quick-fix or completion list leaked a timer, permanently.** Both
  anchored to the caret by asking for the bounds of an empty range, and for an
  empty range the editor allocates a throwaway caret to measure with — one whose
  500 ms blink timer nothing ever stops. Exactly one per open, for the life of
  the process, so a session that leans on completion accumulated them all day.

- **SVG files preview as vector art** instead of a fixed 2×-resolution bitmap,
  so zooming past 200% no longer goes soft on the one preview whose entire
  content is vector. Transparent files get a checkerboard behind them. Markdown
  badges and every export path still rasterize exactly as before.

- **The Git Log tool window was missing from the VCS menu**, which offered Show
  File History but never the whole-repository log.

- **The first Search Everywhere of a session opened with no row selected**, and
  every later one had scrolled the group header out of sight — so neither was
  quite right, and an open was only correct if you had typed something the time
  before.

- **Status messages write your home directory as `~`**, matching the breadcrumb
  and window title that had always done so.

Plus a dependency sweep — Jackson, Lucene, PDFBox, POI, commonmark and JSVG —
and the full list is in the [changelog](/whats-new).
