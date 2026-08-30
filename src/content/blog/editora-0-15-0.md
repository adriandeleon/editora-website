---
title: "Editora 0.15.0: fourteen pages for a 382-page document"
description: "A print path whose failure the on-screen preview structurally clips away, an SVG that renders everywhere except in an actual installation, and a menu that decided it wasn't in a Git repository once and then never asked again. Four bugs that each worked fine on the path I happened to take."
date: 2026-08-30
author: Adrian De Leon
tags: [release, printing, performance]
---

I printed this repository's `CLAUDE.md` and got **fourteen pages**. Most were
nearly blank. A few were shrunk past the point of legibility.

It is a 382-page document.

That is the headline fix in 0.15.0, and chasing it turned up three more bugs
with the same shape — which is what this post is actually about. The
[release note](/news/2026-08-30-editora-0-15-0-released) lists what changed.
This is about a property they shared: **each of them worked fine on the path I
happen to take.**

## One block, however long it is

Pagination splits on whole blocks, so nothing breaks across a page edge. That is
the right model. The failure was the escape hatch: a block **taller than a
single page** was given a page of its own and scaled uniformly to fit.

For an oversized image, that is exactly right. For text it is a catastrophe, and
the reason is a fact about Markdown that is obvious once stated and easy to miss
while writing a paginator: **a list is one top-level block however long it is.**
So is a paragraph. The document I was printing has 19 top-level blocks, six of
them over-tall, and one of those was a 296-page list — rendered onto a single
page at **0.3% scale**.

The fix is to stop treating "too tall" as "shrink it". An over-tall *container*
is regrouped into copies of itself, each holding as many children as fit: a long
list becomes several lists, a long paragraph several paragraphs, each carrying
the original's styling so it renders identically. The text stays vector rather
than being sliced into an image, so it is crisp on paper. The cost is a seam
that does not hang-indent, which is a real but small price against 0.3% scale.

Two things I had to get right rather than approximately right:

**Uniform scaling still has to exist.** A genuinely *atomic* over-tall block — an
image — cannot be regrouped, because it has no children to distribute. Deleting
the scale path entirely would have traded a text bug for an image bug.

**A plain paragraph has nothing to split at.** It is a single run with no
emphasis boundaries, so there is no child list to regroup. Those get cut at
whitespace instead, and no word is broken.

And then, found alongside it, an older overflow that had been there the whole
time: the page container's own padding and the gaps between blocks were not
counted, so blocks summing to exactly the page height spilled over the edge. Six
of eight pages on a 200-item list, the worst by 31 pixels.

Nobody had ever seen it, because **the on-screen preview clips**. The preview is
the thing you look at to check pagination, and it is structurally incapable of
showing you the one failure mode that matters most. That is the first of the
four.

## The height that was already known

Fixing the output meant looking at the timing, and printing that file took
**17.1 seconds**, of which 16.1 was layout. Not rendering. Not measuring text.
Layout, 94% of it.

The splitter asks "how many of these blocks fit on a page?" by binary search,
and every step of every search laid out a candidate group to measure it. Per
step, per piece emitted: **1.31 million cumulative nodes** for one document.

The insight is that the question had already been answered. A vertical group
stacks its children at their preferred heights — that is the entire contract —
so its height is the *sum* of theirs. Measure each child once and every prefix is
arithmetic. No layout required.

    CLAUDE.md      409 pages   17.1s → 9.2s     (nodes laid out 1.31M → 643K)
    CHANGELOG      333 pages    3.4s → 2.4s
    README          46 pages    1.6s → 1.2s

Two guardrails, because this is precisely the kind of shortcut that is right
until it isn't:

**It applies to one container type.** Wrapped text and side-by-side rows are
still measured for real, because summing their children would be meaningless.

**The arithmetic is a hint, not an answer.** Every emitted page is still verified
once against a real layout. A page that overflows is the exact bug the splitter
exists to prevent, so it is not something to take on trust from an optimization —
and one verification per emitted page is cheap next to one layout per binary
search step. That is why the numbers above are a halving rather than the order
of magnitude the node count suggests, and I would rather have the halving.

## It works on the classpath

The SVG preview — the vector one that shipped in 0.14.0 — drew a **correctly
sized checkerboard and no artwork** in packaged builds.

The symptom is the clue, and it took me too long to read it. The checkerboard is
the transparency backdrop, drawn immediately *before* the artwork. It came out at
exactly the size the drawing should have been. So the geometry was right, the
document had parsed, the canvas was sized, and the layout had happened. Only the
paint had failed — silently, mid-frame.

The cause is a broken module boundary in jsvg 2.1.0: its JavaFX bridge reads a
logging class that the main jsvg module does not export to it. So the first paint
throws, and what survives on screen is whatever was drawn just before.

Here is the part worth writing down. **It reproduces only on the module path.**
On the classpath there are no module boundaries to violate, so the fat jar was
fine, `mvn javafx:run` was fine, and every test in the repository passed —
because the test suite runs on the classpath. The module path is every real
installation. My entire development loop sat on the one path where the bug does
not exist, and four other explanations got measured and ruled out before I
questioned that.

The fix is a one-line `--add-exports`. What is irritating is *where* it has to
go: the dev runner, the packaged launcher, and the ahead-of-time cache trainer —
the same three places, kept in step by hand, that
[0.14.0's preview-feature flag](/blog/editora-0-14-0) needed. Same trio, second
release running, for a completely unrelated reason. There is a test that fails
the build if the trainer's option list drifts from the others, which is the only
reason I am not expecting a third.

## Asked once, at the only moment the answer was wrong

The VCS menu came up almost entirely greyed out *inside a Git repository* —
Commit, Push, Pull, Fetch, Switch and New Branch, Git Log, file history, both
stash entries — while the status bar an inch below cheerfully displayed the
branch name and its ahead/behind counts.

The gate was correct. It was simply never re-run.

Repository detection is asynchronous, which is right: it shells out to `git`, and
that does not belong on the UI thread. But it means that at the moment a window
is built, the answer to "are we in a repository?" is *always* no, because nothing
has come back yet. The menu asked once, at construction, at the only moment in
the window's life when the answer was guaranteed wrong — and then never asked
again.

Which froze far more than Git. The preview, CSV, `.http` and Typst entries never
followed a tab switch. The debug step commands never followed a session
suspending. Every one of those had a correct gate evaluated exactly once against
an empty world.

The state is now re-evaluated when a menu opens. That alone is not enough,
though, and the reason is a nice piece of platform trivia: on macOS the system
menu bar is owned by AppKit, which may simply not tell you it is opening a menu.
So the state is *also* pushed from the three places the context genuinely moves.
Belt and braces, because the belt is missing on one platform.

## The same behaviour, by two different paths

The last one is small and I like it as a specimen. Re-enabling a build tool in
Settings — Maven, npm, Cargo, Go, Gradle — left it dead: no stripe button, no
tasks tree. And the status row *on the very same Settings page* went on reading
"No Maven project detected", which is what made it look like something you could
not fix from the interface.

Detection is cleared when a tool is switched off, and re-running it is the job of
"every settings apply", which is the documented behaviour. The palette command
did that. The Settings checkbox did not. One feature, two paths to reach it, and
only one of them honoured the contract — so whether the feature worked depended
on which control you used, which is not a distinction any user should have to
discover.

The status row had a second, independent version of the same mistake: it asked
for the detection result *synchronously*, immediately, which always answers "not
detected" for exactly the reason the menu bug existed. It is read when detection
finishes now.

## Designing one of them out

Worth ending on the opposite case, because 0.15.0 also has a feature where this
class of bug was avoided by construction rather than fixed afterwards.

Typst files now fold and outline by section, and the hard part is not the
outline — it is deciding what counts as a section marker. A `=` in a Typst file
might be a heading, or an assignment in `#let x = 1`, or an equality inside math,
or a rule of `=` characters, or any of those written inside a raw block, a line
comment, or one of Typst's *nesting* block comments.

Folding needs that answer. The outline needs that answer. The obvious build gives
each of them a copy, and the copies drift — and the failure mode is a document
that outlines one way and folds another, which reads as both features being
broken.

They share one parser. Not as an optimization; as the thing that makes the two
surfaces unable to disagree.

## The common thread

Four bugs, one property: each worked on the path I take.

I develop on the classpath, so a module-boundary violation cannot reach me. I
check pagination in a preview that clips, so an overflow cannot reach me. I open
menus in windows whose asynchronous state has long since settled, so a
construction-time evaluation looks correct. I toggle features from the palette,
so the Settings path went untested by habit rather than by decision.

None of that is a testing failure exactly — the tests were real, and they passed
for accurate reasons. It is narrower: **the environment I develop in is a
sample of one, and it is not a random one.** It is specifically the environment
where things are most convenient, which correlates uncomfortably well with where
things work.

The useful move is not "test more". It is to notice when you are about to trust a
result that only holds on your side of a boundary — a packaging boundary, an
async boundary, a clipping viewport, a second code path to the same feature — and
go stand on the other side of it once.
