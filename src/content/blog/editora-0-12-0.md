---
title: "Editora 0.12.0: moving a panel is not moving a panel"
description: "Letting a tool window maximize, re-dock, share a side and float looks like one layout feature. It is really six separate encounters with what a SplitPane, an unmanaged node, a drag gesture, a fresh Scene, and the order you restore things in will each quietly do instead. Plus: a pom preview that admits what it doesn't know."
date: 2026-08-16
author: Adrian De Leon
tags: [release]
---

Editora's tool windows — Project, Structure, Git, Problems, the build consoles —
have always lived wherever they were assigned, at whatever size that side
happened to be. 0.12.0 lets you **maximize** one over its split, **re-dock** it
to another stripe by dragging its button, **share a side** between two of them,
and **float** one into a window of its own.

That reads like a single afternoon of layout code. It was six separate
discoveries about what the toolkit does when you stop using it the easy way, and
every one of them had a version that compiled, ran, demoed fine, and was wrong.

## Maximize is a divider position, not a move

The obvious implementation of "give this panel the whole area" is to take the
panel out of the split and put it somewhere bigger. Don't. Re-parenting a node
drops focus, resets the stripe button's state, and throws away the remembered
size you have to restore afterwards.

The panel is already in the right `SplitPane`. Maximizing is just pushing every
divider to one end:

```java
// Divider i sits between items i and i+1: everything before the target
// collapses to 0, everything from it onwards goes to 1.
double[] pos = new double[itemCount - 1];
for (int i = 0; i < pos.length; i++) {
    pos[i] = i < index ? 0.0 : 1.0;
}
```

With the editor and both side panels open — `[left, editor, right]` — maximizing
the left window is `[1, 1]` and the right one `[0, 0]`. Nothing moves in the
scene graph, focus survives, and restoring is one array assignment.

Then it stopped part-way, and looked like a bug rather than a feature.

**A `SplitPane` honours its items' minimum widths.** The editor area's computed
minimum is wide enough that pushing the divider to 1 gets you most of the way and
then stops, leaving a stubborn strip of editor. So the other items' minimums are
zeroed for the duration and put back on restore. A test drives both splits and
asserts the panel actually reaches the full width; mutating the zeroing out makes
it fail, which is the point of writing it.

The second way this ships broken is worse, because it survives a restart.

Closing a tool window records the live divider as that window's remembered size.
So does persisting the layout. If either runs **while maximized**, the size
written down is 0 or 1 — and then the window reopens next session covering the
editor, records that as its size again, and does it every launch after that. It
is not a glitch you notice and shrug at; it is a layout that has eaten itself and
will not come back without deleting your session. Both paths un-maximize first.
Any change to the split's contents cancels a maximize too, since the saved
positions were captured against divider indices that no longer mean the same
thing.

## The empty stripe nobody could drop on

Re-docking is a drag: pick up a stripe button, drop it on another stripe. If
there is already a button over there you can aim at it, and inserting before or
after it is straightforward — as long as you remove the source from the list
*before* asking the target for its index, or you are off by one every time the
source sat ahead of it.

The interesting case is a side with **nothing on it**, where there is no
neighbour to aim at and the only non-arbitrary landing spot is "the end". So the
stripe `Pane` itself became a drop target, which is about eight lines, and it did
not work at all. Not intermittently — the empty side was simply unreachable, and
re-docking silently only ever worked between sides that already had a button.

An empty stripe is `setManaged(false)`. **An unmanaged, invisible node receives
no drag events whatsoever.** The drop target was there, correct, and never once
consulted.

So a drag now reveals the empty stripes for its duration:

```java
static boolean stripeShown(boolean stripesEnabled, boolean zenHidesStripes,
                           boolean stripeEmpty, boolean dragging) {
    return stripesEnabled && !zenHidesStripes && (!stripeEmpty || dragging);
}
```

Note what it deliberately does *not* override: stripes switched off, and Zen
mode. Those are the two hides the user asked for — and in both there is no stripe
UI to drag *from*, so a drag cannot be in progress anyway.

## The highlight that would not go away

The stripe paints a dashed border while you hover a button over it. It shipped
stuck on: reorder a button by dropping it on a neighbour and the drop zone stayed
painted for the rest of the session.

Two independent bugs, both worth knowing.

`DRAG_OVER` fires continuously while the cursor is over a node — tens of times a
second — and the handler was calling `getStyleClass().add(...)` on each one. So
the class stacked up dozens deep, while both clear paths called `remove`, which
drops only the **first** occurrence. One clear, and the stripe stays lit.

But the clears were not running anyway, for the commonest gesture there is.
Dropping onto a *button* is consumed by that button, so the stripe's own
`DRAG_DROPPED` never fires. And sliding from stripe space onto one of its own
child buttons is not an *exit* from the stripe, so `DRAG_EXITED` doesn't fire
either. Between them they covered exactly the case nobody does.

The fix is an idempotent setter using `removeAll`, plus clearing on any
end-of-drag signal — with the clear placed *ahead* of the "did the flag actually
change?" guard, so a stop always clears whether or not the state moved.

## A SplitPane forgets its dividers when you touch its items

Two tool windows on one side means the side contributes an inner `SplitPane`
instead of a bare panel, and the outer split's item has to be swapped. Swapping
in place — `getItems().set(i, next)` — keeps the slot, so the dividers after it
aren't renumbered. Good.

**A `SplitPane` rebuilds its dividers on any items-list change, a `set`
included.** The position is not preserved across it. So every time a side split
or unsplit, it sprang back to an even share with the editor, discarding a width
the user had dragged. The position is now captured before the swap and re-applied
after — immediately, *and* on the next pulse, so the re-apply survives the layout
pass the list change schedules.

Sizes had a quieter version of the same problem. A side's remembered size is
measured against its slot in the outer split — but once a side is split, its two
panels are not outer-split items at all. `indexOf(panel)` answers `-1`, and the
size simply stops being remembered, with nothing to indicate anything went wrong.
It is measured against the side's *container* now.

## A fresh Scene has no styles, and a node has one parent

Floating a panel into its own `Stage` is where a design system finds out how it
is delivered. The panel came out looking like raw JavaFX — default grey buttons,
no palette, none of the app's look.

Editora's appearance is an **author** stylesheet, not the user-agent theme. A new
`Scene` starts with an empty stylesheet list, so it inherits nothing:

```java
scene.getStylesheets().setAll(owner.getScene().getStylesheets());
```

And docking it back has a smaller trap. A JavaFX node has exactly one parent, so
handing the panel to the split while the floating scene still holds it throws —
the scene has to be given a throwaway root to own first.

The floating stage is **owned by the editor window**, which is what makes it feel
like part of the app rather than a second one: it floats above its owner,
minimizes with it, and JavaFX closes it when the owner closes, which is why there
is no teardown code for these anywhere. Floating is a *state of an open window*,
not a mode it keeps — closing the stage closes the tool window — so it can never
be reopened into a stage you have forgotten is out there.

Bounds are remembered, and reused only if they still overlap a screen; a window
last saved on a monitor you have since unplugged is re-centred rather than opened
somewhere you cannot reach. They are rewritten on every move and resize but
**never saved from the listener** — a config write per pixel of a drag is a
blocking disk write on the UI thread. The next ordinary save flushes it.

## The order you restore things in

The last one has no toolkit in it at all, and cost the most time.

Restoring the floating windows read the saved id list, then opened the docked
sides, then floated the ones on the list. Nothing came back.

Every open **persists**. Persisting rewrites the floating set from the live one —
which is empty until the deferred floating pass runs. Reading the list after the
docked sides restore hands you back a list `restore()` has just cleared itself,
so the read succeeds, returns empty, and the windows are silently gone.

The list is read first, before a single window opens. One line moved; the comment
above it is longer than the fix, because the next person to tidy this method will
otherwise move it back.

Persistence had one more decision worth stating. Open state moved from three
single-id fields to a map of side → ids, so the schema bumped — and the migration
is a real **seeding** step rather than the identity step most additive bumps get.
From the new version the map is the source of truth, so an un-seeded upgrade
would have restored your session with every tool window closed. The three legacy
fields are still written, each carrying its side's primary window, so an older
Editora reading a newer config gets a sensible single-window layout instead of an
empty one.

## Testing a gesture you cannot construct

None of the drag behaviour is tested through drag-and-drop. A `Dragboard` cannot
be constructed outside a real gesture, so a DnD-level test would be exercising a
mock of the very thing that was broken — and every bug above lived in what
JavaFX does, not in what the handlers compute.

So the *decisions* are pure and unit-tested — where a drop lands, which dividers
maximize, whether a stripe shows — and the parts that must touch the toolkit are
tested through the methods the handlers call, in a real window. That split is
roughly where the bugs were, too: the arithmetic was right the first time, and
everything that went wrong was an assumption about the toolkit's behaviour.

## Postscript: a preview that admits what it doesn't know

The other half of 0.12.0 is a **`pom.xml` summary preview**, and it ran into a
different kind of question: what do you do about a fact the file doesn't contain?

A pom is full of indirection. A version can be `${junit.version}`, or absent and
supplied by `<dependencyManagement>`, or absent and inherited from a parent pom.
The first two the file can answer, and the preview resolves them — showing
`5.10.2  ${junit.version}` and `2.0.17  managed`, the resolved value with its
source kept beside it, because hiding the indirection is its own kind of lying.

The third it cannot. Reading the parent means finding it in the local repository
or walking a reactor, and Editora is rendering *one file*. So an inherited version
reads **inherited**, and an unresolvable property reports itself. The temptation
is to guess — the number is usually obvious, and a guess looks more finished than
a word. But a version number is exactly the thing you open this view to check,
and a plausible wrong one is worse than an honest gap.

The same principle shows up in `maven.updateVersions`, which brings an existing
pom up to date. It refuses to trust Maven Central's "latest release" marker
(which means "newest non-snapshot published", and for `maven-surefire-plugin` was
a milestone build for years), it never walks a version *backwards*, and it skips
property-driven versions rather than inlining a number over the indirection you
chose. And it shows you every change before writing anything, because the
alternative — an upgrade you cannot preview or undo — is the version of this
feature I shipped first, and the first real use found it immediately.

The full list is on the [What's New](/whats-new) page, and 0.12.0 is on the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).
