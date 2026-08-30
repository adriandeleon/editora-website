---
title: "Editora 0.15.0: printing, Typst structure, and menus that keep up"
description: "A long Markdown file printed as fourteen pages, most of them nearly blank. Plus Typst files that fold and outline by section, every goal a Maven plugin offers, and a VCS menu that stops being greyed out inside a Git repository."
date: 2026-08-30
version: "0.15.0"
---

**Editora 0.15.0** is out — **4 new features, 5 changes and 4 fixes**, plus a
printing path that is roughly twice as fast. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

The theme, if there is one, is things that were wrong somewhere you could not
easily look: a print path whose failure the on-screen preview clips away, a menu
whose greyed-out state was decided once and never revisited, and an SVG preview
that worked everywhere except in an actual installation.

## A document that prints

Printing this repository's own `CLAUDE.md` produced **fourteen pages**, most of
them nearly blank and a few shrunk past legibility. It is a 382-page document.

Pagination splits on whole blocks so nothing breaks across a page edge, and a
block taller than a page was given a page of its own and scaled uniformly to fit.
That is right for an oversized image and catastrophic for text, because a
Markdown list is **one** top-level block however long it is. Of that file's 19
top-level blocks, six were over-tall — one of them a 296-page list rendered onto
a single page at **0.3% scale**.

An over-tall container is now regrouped into copies of itself holding as many
children as fit: a long list becomes several lists, a long paragraph several
paragraphs, each carrying the original's styling so it renders identically. The
text stays vector rather than being sliced into an image, so it is crisp on
paper; the cost is a seam that does not hang-indent. Uniform scaling survives
only for a genuinely atomic over-tall block, where it is the right answer. Same
file, after: **382 pages, none scaled.**

A second, older overflow surfaced alongside it. The page container's own padding
and the gaps between blocks were not being counted, so blocks summing to exactly
the page height spilled over the edge — six of eight pages on a 200-item list,
the worst by 31 pixels. Invisible on screen, because the preview clips.

And it is **about twice as fast**, which turned out to be the same investigation.
Pagination was 94% layout: the search for how many blocks fit a page laid out a
candidate group per step of a binary search, per piece emitted — 16.1 of 17.1
seconds on that file, laying out 1.31 million cumulative nodes. A vertical
group's height is *arithmetic* rather than something to measure, since it stacks
its children at their preferred heights, so measuring each child once makes
every prefix a sum.

    CLAUDE.md      409 pages   17.1s → 9.2s     (nodes laid out 1.31M → 643K)
    CHANGELOG      333 pages    3.4s → 2.4s
    README          46 pages    1.6s → 1.2s

That shortcut is a property of one container, so wrapped text and side-by-side
rows are still measured for real — summing them would be meaningless — and the
arithmetic is treated as a *hint*, with every emitted page verified once against
a real layout. A page that overflows is the exact bug the splitter exists to
prevent, so it is not taken on trust.

## Typst files get a shape

A `.typ` file folded at its brace pairs and nowhere else, and the Structure
window listed a single entry — `#align()` — for a report with three sections.
Typst was being routed through the generic paths, which know nothing about
`= Introduction`.

**Headings now drive both**, so the outline reads as the document's own table of
contents and a section collapses like one. Folding at `#align` / `#table`
brackets still works alongside it, and a long embedded listing collapses at its
fence — usually the thing a reader most wants out of the way.

**`#let` and `#show` bindings join the outline** in document order, each nested
under the section it is written in. A template file can be almost entirely
bindings, and outlining headings alone left exactly those files with an empty
Structure window. Three forms are deliberately left out, because each would be
noise rather than navigation: `#set`, which configures the document rather than
defining anything (a run of `#set page` / `#set text` would top most files); an
indented `#let` inside a function body, naming something that means nothing
outside it; and destructuring or `#show:` forms, which name nothing you could
jump to.

The hard part is telling a section marker from every other use of `=` in a Typst
file — an assignment in `#let x = 1`, an equality inside math, a rule of `=`
characters — and from anything written inside a raw block, a line comment or one
of Typst's nesting block comments. Folding and the outline **share one parser**
rather than each carrying a copy of that rule, so the two surfaces cannot
disagree about where a section begins.

## Every goal a Maven plugin offers

The tasks tree is built from a literal reading of `pom.xml`, so it could only
show a goal written inside an explicit `<execution>`. A plugin declared for
**direct invocation** — javafx-maven-plugin, spring-boot-maven-plugin,
exec-maven-plugin — carries only `<configuration>`, so it contributed no row at
all. **`javafx:run` could be reached only by typing it into "Run custom…"**,
even though running it is the entire reason the plugin is in the pom.

Each plugin now gets a collapsed section listing the goals it actually offers,
read from the plugin's own descriptor, with each goal's own description as its
tooltip. Reading the descriptor rather than keeping a table of well-known goals
means it covers whatever is in your pom instead of whatever somebody remembered.

It reads the **local repository only, never the network**, so opening a project
never fetches anything and a plugin you have not downloaded simply contributes
nothing. Results are cached, since this re-runs on every save and tab switch.

## Menus that keep up

**The main menu's greyed-out items never un-greyed.** The VCS menu came up
almost entirely disabled *inside* a Git repository — Commit, Push, Pull, Fetch,
Switch and New Branch, Git Log, file history and both stash entries — while the
status bar an inch below showed the branch and its ahead/behind counts.

The gate was right. It was simply never re-run. Repository detection is
asynchronous, so at the moment a window is built the answer is always "not in a
repo", and nothing re-evaluated it for the life of that window. That froze every
context-dependent item, not only the Git ones: the preview, CSV, `.http` and
Typst entries never followed a tab switch, and the debug step commands never
followed a session suspending. The enabled state is now re-evaluated when a menu
opens, and pushed from the three places the context actually moves — which is
what keeps the macOS system menu bar honest, since AppKit owns the popup there
and may not tell us it is opening.

Three smaller things in the same area:

- **Each markup language's actions sit under one submenu** in the editor's
  right-click menu. A Typst buffer spliced eight flat `Typst: …` entries into it
  and a Markdown one seven, which pushed cut, copy, paste and the spelling
  suggestions far enough down to hunt for, and made file-type actions read as
  ordinary editing ones. Run and Debug deliberately stay at the top level — they
  are the two entries reached without reading the menu.
- **The branch dropdown is drawn like the VCS menu it duplicates.** It offers
  the same commands and drew them as bare text at a smaller size while the menu
  drew each with a glyph, so one set of actions read as two unrelated lists. New
  Branch also gets an icon of its own; it had been drawn identically to Switch
  Branch, and had been spelling its icon as a fullwidth `＋` inside the label
  text in all six translations.
- **The Help menu links to the documentation** — the one entry that helps
  someone who does not yet know what the editor can do, and it was missing. The
  link is versioned, so a build lands on the docs for what it actually is rather
  than on whatever is current. It is in Simple UI mode's shorter Help menu too,
  where it belongs most, and in the palette as *Documentation*.

## Also fixed

- **The SVG preview showed a correctly-sized checkerboard and no artwork** in
  packaged builds. jsvg 2.1.0 ships a broken module boundary: its JavaFX bridge
  reads a logging class the main module does not expose to it, so the first
  paint fails and what survives is the transparency pattern drawn just before it
  — a checkerboard the exact size the drawing should have been. It reproduces
  **only** on the module path, which is every real installation, and not on the
  classpath, so the fat jar was fine and so was every test in the repository.
  Four other explanations were measured and ruled out first.

- **Switching a build tool back on in Settings left it dead.** Re-enabling Maven
  — or npm, Cargo, Go, Gradle — gave no stripe button and no tasks tree, and the
  status row on the very same Settings page went on reading "No Maven project
  detected", which is what made it look unfixable from the interface. Detection
  had been cleared while the tool was off and only the palette command re-ran
  it.

## Also

- **Recent Files moved into the customizable part of the toolbar**, between Save
  As and Undo. It is a file action — one of the ways to get a file on screen —
  and sitting beside the project switcher it read as a project control. It is a
  real catalog item now, so it can be dragged or removed like any other. A
  toolbar you have already customized keeps the button.
- **The editor's right-click icons match the rest of the application again.**
  The icon-family migration moved Undo, Redo, Cut, Copy, Paste and others to the
  outline family on one side only, so `Edit ▸ Undo` and right-click ▸ Undo showed
  two different icons for the same command. The two sets are now checked against
  each other rather than by eye.
- **Eclipse LSP4J 0.23.1 → 1.0.0**, which implements LSP 3.18 and DAP 1.70.
  Language-server and debug-adapter behaviour is unchanged; this is the protocol
  library catching up.

The complete list is on the [What's New](/whats-new) page.
