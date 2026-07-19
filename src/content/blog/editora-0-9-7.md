---
title: "Editora 0.9.7: keeping the UI thread sacred"
description: "A performance release about one rule — never make the user wait on the thread that draws the screen — and the handful of places Editora was quietly breaking it."
date: 2026-07-18
author: Adrian De Leon
tags: [release]
---

A desktop app has one thread that draws the screen and handles your keystrokes.
In JavaFX it's the Application Thread. The single most important rule for a
responsive editor is that this thread never does anything slow. Every hitch you
feel while typing, every beat where a click doesn't register, is that rule being
broken somewhere.

0.9.7 is mostly the result of going looking for those places. None of them were
obvious, because a stall of a few dozen milliseconds doesn't show up as a bug
report, it shows up as a vague sense that the editor is heavier than it should
be. But they add up, and they're findable.

## The one you felt without knowing why

Open the first `.py` or `.java` file in a project and, for a moment, the editor
would freeze. That pause was Editora starting the language server: forking the
process, scanning your `PATH` to find it, writing a small ledger file. All of it
ran on the UI thread, so the editor couldn't repaint or accept input until the
process was up.

The fix is to do that work on a background thread. The loading indicator in the
status bar already covered the wait, so there was never a reason for the UI to
block on it. Now it doesn't, and opening that first file is instant even though
the server still takes the same second or two to come up in the background.

## Death by a thousand small scans

The subtler ones share a shape: some panel or readout doing an O(whole-document)
operation on every keystroke, and often doing it even when nobody could see the
result.

The status bar showed the file size. Computing it meant scanning and copying the
entire document, and it happened on every caret move and every keystroke, in a
large file, on the UI thread. The File Information panel re-counted every word in
the document on every caret move, and re-materialized the whole document on every
keystroke, even when the panel was closed. The Structure outline re-split the
document and rebuilt its tree on every edit, closed or not.

The pattern for fixing all of these is the same:

- Only recompute when the thing being shown actually changed. The file size only
  changes when the text does, not when the caret moves.
- Do nothing while the panel is closed. Defer the work and do it once when it
  reopens.
- Ride the debounced change stream instead of firing on every raw keystroke.

Individually, none of these was dramatic. Together, they were most of why typing
in a big file felt sticky.

## The zoom that redetected your compilers

My favorite one, because of how far the cause was from the symptom. Zooming the
text with `Ctrl`+wheel felt slow. The reason: each notch re-ran the *entire*
settings-apply cascade. That meant swapping the editor theme's stylesheet (a
full-scene CSS reapply) and running about twenty feature `applySupport()` calls,
re-detecting every language server, re-checking Git, rebuilding previews, none of
which change when you change the font size.

A font zoom now re-applies exactly two things: the fonts and the per-buffer view.
A `Ctrl`+wheel gesture no longer churns the scene stylesheet or goes looking for
your compilers. It just resizes the text, which is all you asked for.

## Persistence that blocked on disk

One more, because it's a good example of the category. Holding Enter above a
bookmark shifts the bookmark's line down, once per newline. Each shift wrote
`bookmarks.json` to disk and rebuilt the entire tool-window tree, synchronously,
on the UI thread. So a burst of edits above a marker turned into a burst of
blocking disk writes.

Now that persistence is coalesced: the writes collapse into a single save after
editing settles. Deliberate actions, toggling a bookmark, adding a note,
reordering, still save immediately, because there the save *is* the action. It's
only the incidental line-shifting that waits.

## And some things you can see

Not all of 0.9.7 is invisible. The Run, External Tools, and Debug consoles tint
error output now, so a stack trace stands out from ordinary logging. The Git Log
window is colored and uses the real file-type icons, matching the rest of the
app. The command palette shows commands that can't run right now, grayed out,
instead of hiding them, so you can tell a feature exists and why it's unavailable.
Bookmarks and Notes group by project, with a toggle to see all of them at once.
And the gutter fold arrow got 35% bigger, which is a small thing you'll be glad of
every time you aim for it.

## Why bother with milliseconds

Because responsiveness isn't a feature you add, it's a property you either protect
or slowly lose. Every one of these regressions got in the same way: a reasonable
line of code that did a little too much, on the wrong thread, a little too often.
The only defense is to keep going back and asking, of the thread that draws the
screen, what is it doing that it doesn't need to.

Get 0.9.7 from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The full
list of changes is on the [What's New](/whats-new) page.
