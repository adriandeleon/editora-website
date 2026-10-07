---
title: "Editora 0.14.0: bugs that had no symptom"
description: "Typing got slower the longer you typed, and nothing pointed at why. Chasing it turned up a measurement that allocated a timer nobody stopped, a probe that moved the thing it was probing, and four tests that could only ever pass. Plus a preview feature that throws, and a window that never appears."
date: 2026-08-29
author: Adrián De León
tags: [release, performance, testing]
---

0.14.0 is a chrome release. The [release note](/news/2026-08-29-editora-0-14-0-released)
lists what changed: a menu bar that can share the title bar, three bars of
chrome reconsidered, focus modes that finally remove the chrome they always
claimed to.

This post is about the other half: the bugs I found while in there, which had
one thing in common. **None of them had a symptom.** There was no crash, no
stack trace and no wrong pixel you could point at in a screenshot, only an
editor that was slightly worse than it should have been, in ways that show up
only if you go looking with an instrument.

## Typing got slower the longer you typed

The first one was barely noticeable. Typing latency in a fresh window was
fine. Typing latency after a few minutes was not, and it never came back.

The fix for this half shipped earlier, in
[0.11.0](/news/2026-08-11-editora-0-11-0-released). It is told here because
0.14.0 found the same fault a second time, in the popups, and that find only
makes sense next to the first.

Measured over 2,000 keystrokes, the median keystroke went from **6.4 ms** at
500 to **28.4 ms** at 2,000, climbing monotonically, with no recovery.
Restarting fixed it. Nothing in the editing path allocates per keystroke in a
way that would explain a degradation like that, and a profiler
pointed at the JavaFX frame loop rather than at any of my code, which suggests
the problem is the amount of work per frame rather than any one piece of it.

It was the 80-column ruler, and specifically this:

```java
area.getCharacterBoundsOnScreen(caret, caret)
```

That is an empty range. Ask RichTextFX for the bounds of a zero-width span and it has
nothing to measure, so it allocates a throwaway `CaretNode` and measures that
instead. A `CaretNode` starts a 500 ms blink timer (`restartableTicks`) on
construction, and **nothing ever stops it**. The node is discarded and the
timer is not. It stays registered with JavaFX's pulse loop as a receiver, ticking,
for the life of the process.

The ruler did that two or three times per measurement, and re-measured on every
edit. So the real number was **+2.02 leaked timers per keystroke**, each one a
permanent addition to the work the frame loop does before it can draw anything.

The fix is one character wide:

```java
area.getCharacterBoundsOnScreen(caret, caret + 1)
```

A non-empty range takes a completely different path and allocates nothing.
Receivers went to **+0.03 per keystroke** and latency to a flat ~2 ms.

Then, in 0.14.0, the same fault turned up somewhere I had not thought to look.
There are
about thirty `getCharacterBoundsOnScreen` call sites, since every overlay uses
one, and most pass a real range. But the completion popup, its LSP variant and the
quick-fix list all anchored themselves to the caret the leaking way, and leaked
**exactly one timer per open**. A session that leans on autocomplete accumulated
them all day.

Those now go through a shared `caretAnchorBounds`, and the hard part of writing
it was the fallback chain rather than the measurement. The leaking form
*always answered*, because it measured through a caret it had just created. A
real character is only measurable once its paragraph has been laid out, so it
comes back empty whenever the flow has no cell for that line, and an empty
answer means the popup **silently does not open**. So it falls back to the
rendered caret's bounds, and then to the area's top-left corner.

Both failure modes are order-dependent: the popup test passes alone and fails
only in a full suite run. The regression guard is
`TypingLatencyBenchmarkTest`, which counts pulse receivers per keystroke and now
covers the popups as well.

## The probe moved the thing it was probing

Chasing the ruler meant instrumenting the viewport, and the obvious way to ask
"what is on screen right now" is:

```java
area.firstVisibleParToAllParIndex()
```

That call **forces a `VirtualFlow` layout**, and the layout it forces *itself
moves* `estimatedScrollY`. Poll it every pulse to watch the scroll position and
you are perturbing the exact quantity you are measuring. I spent a while
believing a scroll drift that my instrument was creating.

The probe that measures without touching anything is a plain property read,
`estimatedScrollYProperty().getValue()`.

There is a second trap next to it. A per-pulse poll can only notice a reset **one
frame late**, which converts a jump into a bounce. If you want to catch the frame
something happens on, you have to hook the change, not sample the timeline. Two
earlier attempts at a related fix shipped and were reverted for exactly that:
they turned a visible jump into a visible bounce and called it fixed.

## The ruler was doing its work at the wrong time

With the leak gone, the ruler was still measuring far too often, and the code
doing the measuring was fine. The bug was the **subscription**.

It listened to `viewportDirtyEvents()`, which fires on every vertical scroll and
on every edit. A ruler's x position depends on three things: glyph advance,
gutter width, and horizontal scroll. Neither a vertical scroll nor an edit can
move it. It was recomputing, forcing two layout passes each time, in response to
events that cannot affect it. That came to six or seven full measures per chrome
toggle, and one per keystroke.

It now recomputes on a viewport **width** change, on horizontal scroll, and when
something that does move it says so explicitly. That takes six or seven measures
down to one or two.

This is not a performance bug in the usual sense. Nothing was slow and
everything was correct. The code ran in response to the wrong signal, and the
only way that surfaces is if you ask how many times it ran.

## A settings apply that redid unchanged work

The next one is the same category with a wider reach. Toggling Simple UI mode froze the interface
for **230–550 ms**, which for a chrome toggle is somewhere between noticeable and
broken.

Simple mode goes through the ordinary settings-apply path, and that path pushes
about forty settings into *every open buffer* and re-runs twenty-odd feature
`applySupport()` calls. It is the path behind every toggle, whether a checkbox
or a theme change. So any setter that does real work when handed **the value it
already holds** pays that cost once per open buffer on every toggle.

Sampling the UI thread every 2 ms and attributing each sample to its deepest
application frame found four:

- Collapsing every fold cleared the fold shading on **every region in the file**,
  unconditionally. That is a styled-document rewrite, not a property set, so
  thousands of paragraphs were rebuilt in a large source file, almost none of
  which had ever been shaded. 442 samples out of 758.
- The ruler again, re-measured for every open buffer **including background
  tabs**.
- The gutter rebuilt **twice** per buffer per apply, because neither the
  gutter-visible nor the line-numbers-visible setter compared its argument.
- Applying the editor theme removed and re-added **the same stylesheet**, forcing
  a full restyle of the window.

All four are now one-line guards, and the result is **48–108 ms** (median 233 →
55). Leaving Simple mode went from a median of 57 ms to 18.

There are two traps if you add guards like these. First, **a field's declared
default is an assumption about the widget, not a fact about it**. The font
setter needs a separate "has this ever been applied" flag, because the area's inline style and
the overlays' fonts are set by that very call, so the first one must always run
even though the value matches. Second, some setters are re-invoked *deliberately*
to re-derive an effective value from other state; the minimap one is called again
after a large-file or preview-mode change. That has to guard on the **effective**
result or not at all. Guarding it on the raw argument makes it a no-op precisely
when it was needed.

## Four tests that could only ever pass

The rest of the release is about tests, and specifically whether each test has a
way to fail.

**The toolbar.** A narrow window was putting the project switcher and Settings
into the overflow chevron while fourteen icons stayed. The obvious test asserts
the pinned group is still in the scene, or still has a width. Both pass against
the broken arrangement, because a `ToolBar` does not remove an overflowed item.
It leaves it parented, sized, and *stranded wherever it last laid out*. It
measured at x=297 of a 620-pixel bar, against x=619 when correctly pinned. Only
an assertion about its **right edge** can tell the two apart.

**The SVG canvas.** The vector preview draws through JSVG's JavaFX canvas, and
the test asserts it is present, holds a parsed document, and is sized correctly.
All three pass for a canvas that renders **nothing at all**, which is what the
first implementation did, twice, for two different reasons. So the test
snapshots the canvas and asserts it painted pixels.

Neither reason is visible from the API. `setViewBox` is the **target** rectangle, not the source: set it to the
document's own size and the canvas draws at natural size in the corner of
whatever box it is given, so the control grows with the zoom and the artwork does
not. Measured: a 200×50 SVG in a 400×100 canvas painted exactly 25% of its box.
And the size has to be pinned including `setMaxSize`, because the skin computes
no preferred size. Unsized it lays out at zero, and pinning only the preferred
size lets the centering parent stretch it and **distort the drawing**. Measured:
800×600 for a box that should have been 800×200.

**Zen and Expert mode**, which had been leaving half the toolbar on screen. The
bar is two containers on one row and only the first was ever hidden. The fix is
easy, and the test took more thought. Asserting "container X is hidden" bakes
in today's layout and will pass again the next time that row is restructured
wrongly. The assertion is that a tail button is invisible **through its
ancestors**, which is the property the user sees and which survives the next
change to how the row is built.

**Search Everywhere's bounded heap.** Both big sources were scoring every
candidate, sorting *all* the matches, and discarding everything past the
fortieth. A one- or two-character query matches most of a corpus, so that sort
ran over nearly everything, on the UI thread, between one keypress and the next.
Keeping a bounded heap of the best forty as it scans took it from 7.2 ms to 2.0
at roughly this project's size, and from 316 ms to 59 at the index's cap.

But the entire justification for that change is that **the results are
identical**. So the test does not assert properties of the new algorithm. It
runs the old one alongside and compares, over corpora built to be full of ties,
since a tie is the only place a bounded selection can legitimately diverge
from a full sort. A property-based test would have been prettier and would not
have been checking the thing the change rests on.

## And then the ones that are somebody else's

Two of this release's near-misses were not in Editora's code at all.

**The title-bar menu bar** uses JavaFX's `StageStyle.EXTENDED`, which is a
**preview feature**. `Stage.initStyle` *throws* without
`-Djavafx.enablePreview=true`, and because it is thrown out of window
construction, the application starts with **no window**. Measured: the first run left a live,
windowless process. So the flag has to be present in three places that can drift
apart (the dev runner, the packaged launcher, and the ahead-of-time cache
trainer), and there is now a test that fails the build if the trainer's option
list stops matching. The call is also wrapped, because a *chrome preference*
should not be able to prevent a window from existing.

**The SVG canvas ships as `jsvg-javafx`**, whose module declaration requires
`javafx.swing`, which Editora itself never touches. Left alone, that resolves to
JavaFX **17.0.15**, for which OpenJFX publishes no Linux ARM64 build at all. So
the Linux ARM64 package could not be built, and the other four would have linked
a JavaFX 17 module into an otherwise-26 runtime without any error. That is the
worse half of the same problem, because the wrong build ships. One line pins it to the
version everything else uses.

## What they had in common

Every one of these was invisible until something counted.

A leaked timer per keystroke looks like an editor that feels a bit heavy today.
A ruler measuring on the wrong event looks like correct code. A test that cannot
fail looks like coverage. A canvas that renders nothing passes every assertion
you would naturally write about it.

Chrome is the part of the editor you are not looking at, which is why this
release found so many of these at once. I do not take that to mean "instrument
everything". When you cannot describe the symptom, a count is more useful than a
profiler: how many times did this run, and how many of these exist now that did
not exist a minute ago? Both of the worst bugs here were found by a number going
up that nobody had thought to watch.
