---
title: "Editora 0.13.0: whether it matched, or how well"
description: "Every picker in the editor filtered on a boolean. Replacing it with a real score turned out to touch dynamic programming, a Turkish dotted capital I, and an optimization that quietly reintroduced the exact bug it was written to avoid. Plus: a symbol scanner that under-reports on purpose."
date: 2026-08-26
author: Adrián De León
tags: [release, search]
---

Editora had five keyboard pickers, and all five asked the same question of a
candidate: *does this match?* A boolean. Yes or no.

That is not the question. The question is **how well**, and the difference
between the two is most of what makes a picker feel intelligent or feel broken.
0.13.0 is the release where I went and fixed it, and then discovered how much
else was sitting on top.

## What "unranked" actually looked like

The command palette had *some* ranking: five coarse buckets — exact, whole word,
word start, substring anywhere, scattered subsequence — tie-broken by title
length. Everything that matched only as a scattered subsequence landed in one
undifferentiated pile ordered by how short it happened to be.

Everything else had none at all:

- The generic picker behind recent files, open buffers, bookmarks, snippets,
  projects, themes and LSP references showed matches in **source order**.
- The project tree's filter was a lowercased `contains`, sorted **alphabetically**.
- The path finder was a *directory browser* on `startsWith`.

Which means that until this release there was no project-wide fuzzy "go to file"
anywhere in the product, and typing `mcon` could not find `MainController`.

## Greedy takes the first fit. You want the best one.

The obvious way to match a query against a candidate is a forward scan: walk the
candidate, and every time you see the next query character, take it. It is O(n),
it is four lines, and it is wrong.

Match `mc` against `MyMacController`. Greedy takes the `M` at index 0, then the
first `c` it finds — the one inside "Mac" — and stops. It has matched. It has
also missed the `C` at the hump, which is the alignment a human means by `mc`
and the one that should score far higher.

So the match is computed by dynamic programming over the candidate instead: for
each query character, the best score achievable with that character at each
position, plus a back-pointer to reconstruct the alignment. Characters earn a
base score, plus a bonus for landing on a word boundary or a camelCase hump, plus
a larger one for continuing a run; gaps are penalized, and so is the total spread
of the match.

This is not a novel algorithm — it is roughly what fzf does — but writing it
yourself is where you find out which parts are load-bearing.

## The optimization that put the bug back

A matrix per candidate per keystroke is too much, so a non-matching candidate has
to be rejected before it allocates anything. That part is easy: run the cheap
greedy subsequence scan first, and if the characters are not even *present*, stop.

Then the tempting next step. The greedy scan already found a matching span — why
not restrict the matrix to that window and save the rest?

Because greedy finds the **earliest** end, and the best alignment frequently ends
later. Fencing the matrix to greedy's span against `MyMacController` cuts off the
`C` at the hump — reintroducing the exact bug the matcher exists to avoid, inside
the optimization, where it looks like a performance tweak rather than a
correctness change.

The sound bounds are different, and unrelated to greedy's answer:

```java
// The first place the FIRST query character could sit...
int lo = 0;
while (lo < n && !eq(s.charAt(lo), query.charAt(qStart))) lo++;
// ...and the last place the LAST one could.
int hi = n;
while (hi > lo && !eq(s.charAt(hi - 1), query.charAt(qEnd - 1))) hi--;
```

Measured over the 1451 paths in Editora's own `src/`, scoring *every*
candidate on every keystroke: 0.26 ms for a one-character query, peaking at
1.16 ms for a seven-character one, and 0.29 ms for a query matching almost
nothing. It plateaus rather than growing with query length — a one-character
query matches nearly everything but has a one-row matrix, a long query has a tall
matrix but almost no survivors. Comfortably inside a 16 ms frame, with the whole
list rescored from scratch each time.

## A Turkish capital I will corrupt your highlights

Case-insensitive matching has an obvious implementation: lowercase both strings
once, match against the copy, return the indices.

The indices are wrong. `String.toLowerCase` is **not length-preserving**. `İ`
(U+0130, the Turkish dotted capital I) lowercases to *two* characters. Every
index after that point in the copy is shifted relative to the original — so the
ranges you hand back overrun the string the caller then substrings to draw the
bold runs.

The fix is to never make the copy. Fold per character, against the original:

```java
private static boolean eq(char a, char b) {
    return a == b || Character.toLowerCase(a) == Character.toLowerCase(b);
}
```

The completion popup's highlighter had already learned this one. It is the kind
of thing you fix once per codebase and then write down, because nothing about the
symptom points at Unicode.

## The bonus that was measuring the wrong thing

The first query character anchors the match more than any later one, so its bonus
counts double. My first version of that gave the bonus for sitting at **index 0**
of the candidate.

Which is fine, until you remember that command palette titles are `Category:
Verb`. For the query `undo`, "Undo History" outranked "Edit: Undo" — because the
first title happens to *begin* with the word while the second merely contains it
as its entire verb. There is no reason at all to think the user meant the tool
window.

The multiplier now applies to that character wherever it lands. Emphatically not
a bonus for being first in the string.

## Ranking with one algorithm and emboldening with another

The palette already bolded the matched characters. It computed those runs with a
*different* matcher than the one it ranked with — so the highlight could point at
characters the ranking had never considered, and the two were free to disagree
about what had matched.

Now the score and the ranges come out of the same call. That sounds like tidying;
it is really the difference between a UI that explains its ordering and one that
decorates it.

## Merging three sources on score deletes two of them

**Search Everywhere** is the other half of the release: one picker over commands,
project files and symbols, so you can type the *name* of the thing instead of
first choosing which of five pickers it lives in.

Once every source can rank itself, merging them looks trivial — concatenate,
sort by score, cap the list. Do that and you ship a picker that is *only* a symbol
search.

The sources differ in size by orders of magnitude: tens of thousands of symbols,
thousands of files, a few hundred commands. With that many symbols, some of them
will score well against almost anything, so the top 24 rows of a flat merge are
24 symbols and the other two sources have silently ceased to exist.

So results stay **grouped by source**, and three rules keep it honest:

- Each source gets a guaranteed share (8 rows), so no source can be crowded out.
- The **groups** are ordered by their best single result, not by their bulk — a
  perfect command match puts commands on top even though there are five hundred
  of them and forty thousand symbols.
- The overall cap trims a group's *tail* rather than dropping a group outright.
  Losing a source entirely because another scored well is the failure the whole
  design exists to prevent.

A leading `>`, `#` or `@` restricts it to commands, files or symbols for when you
do already know. Those are VS Code's sigils rather than invented ones; the muscle
memory already exists and there is nothing to gain by being original about it.

## A scanner that under-reports on purpose

Search Everywhere needs symbols, and until this release symbols meant a language
server. LSP is off by default and needs one you install yourself, so for a
first-run user — or for any of the twenty-odd languages that ship a grammar but
have no server — "go to symbol" did not exist at all.

Editora now carries its own declaration scanner across sixteen language ids. Two
decisions did the work.

**It under-reports on purpose.** When a pattern is not sure, it says nothing. A
missing declaration costs you one fallback to search. An invented one sends you
somewhere that does not exist and teaches you not to trust the feature — and a
navigation tool you have learned to double-check is worth less than no tool,
because you now pay for it twice.

**It is not built on the TextMate grammars**, which are the obvious source: they
already tag `entity.name.function` and friends, and Editora already ships them.
But tm4e grammars are not thread-safe and the registry is *shared with the
editor's background highlighters*. An indexer walking a project would race the
thing that colors the file you are looking at. The regex rules are less elegant
and cannot lose that race.

The index is also built **lazily, on first use** — not when a project opens.
Walking every file the moment you open a folder spends real work on behalf of
someone who may never ask it anything, and asking for a symbol is the
justification. The first query pays for the walk (under a second on a 650-file
project, announced in the status bar so it does not read as a hang); after that
it is incremental, rescanning exactly the file you saved, from the text already
in memory.

## Reach is half of it

Everything above is **reach**: can the thing be found. The other half is
**flow**: can you jump without losing your place. Three findings there, each
smaller than the ranking work and each with a version that demoed fine and was
wrong.

**A preview without an undo is worse than no preview.** Recent Locations shows
the highlighted row in the editor as you arrow through it. Ship just that, and
pressing Escape leaves you wherever the cursor happened to stop — you have
converted a cancel into a jump. The undo is not a nicety attached to the feature;
it is half of it, and the feature should not exist without it.

**Never demote an already-open file.** Browsing now costs one reusable *preview
tab* instead of one tab per glance. The tempting generalization is that any file
you land on lands in that slot — which quietly makes a file you deliberately
opened an hour ago disposable, and loses it on your next glance at something
else. An open tab earned its place; only new arrivals are disposable.

**Keep the outermost, not the innermost.** Sticky scroll pins the enclosing scope
headers above the viewport, and when the chain is deeper than the cap something
has to go. My instinct was to keep the innermost — it is the nearest, surely the
most relevant. It is exactly the one you can infer from the code in front of you.
The file-level type is the one that has been off screen longest, so that is what
survives the trim.

## Also in 0.13.0: two gigabytes committed before `main`

One more, because it surprised me. Peak memory on a four-file session dropped
from ~908 MB to ~653 MB by setting a single flag.

The maximum heap was pinned; the *initial* heap never was. The JVM's default for
it is 1/64 of physical RAM — **clamped up to the maximum heap**. On a large-RAM
machine those two rules meet, and the entire 2 GB heap is committed before the
application starts, sitting there for the best part of a minute until the
periodic collector hands it back. The live heap when idle is 63–75 MB.

Pinning it to 64 MB also made pauses *better* rather than worse — 19.6 → 4.2 ms
median — because a small young generation copies far less per collection. And it
only ever affected large-RAM machines, which is why a 16 GB laptop never saw it
and why it survived this long.

---

The [release notes](/news/2026-08-26-editora-0-13-0-released) have the full list,
and the docs cover [Search Everywhere](/docs/navigation#search-everywhere) and
[code navigation](/docs/code-navigation).
