---
title: "Editora 0.10.0: what a design system actually has to decide"
description: "Giving an editor a look of its own is not picking colours. It is naming what each colour means, then discovering that the toolkit you build on has no last-child selector, shrinks every label in a row by an equal share, and silently refuses one flavour of SVG arc. Plus: the third time a language server shipped a feature switched off."
date: 2026-08-03
author: Adrian De Leon
tags: [release]
---

Editora has looked like whichever theme you picked. Primer, Nord, Dracula, one of
the nineteen bundled community themes — all good themes, none of them Editora's.
0.10.0 gives it a pair of its own, **Editora Light** and **Editora Dark**, drawn
from the palette in the app's own icon.

The colours were the quick part. What took the work was everything a palette
implies but does not state, and then four places where JavaFX declines to do what
a design system assumes every toolkit does.

## A palette is not a set of colours, it's a vocabulary

The first draft of any theme is a list of hexes. The useful version is a list of
*meanings*, because a colour that means two things is worse than no colour at
all.

Editora had accumulated the second kind. Amber marked an unsaved tab. A different
amber marked a read-only buffer. The file tree's "modified" marker was a third
one, and it was `#bf8700` — a fixed ochre, chosen against a light background,
still `#bf8700` on a dark theme where it reads as brown.

So the palette is now declared once, as meanings, on `.root`:

```css
-state-amber:  /* not durable yet — unsaved, read-only, narrowed, a snapshot */
-state-red:    /* broken */
-state-green:  /* verified */
-state-accent: /* the app is offering you something */
-state-olive:  /* git: untracked */
-state-violet: /* git: renamed */
-state-key:    /* a keybinding, and nothing else */
```

Every surface — tabs, status pills, trees, pickers, Settings — references those
instead of restating a hex. The first four chain to the theme's own semantic
roles, so they track whatever theme you are in for free. That single change is
why dirty markers now follow the theme: nobody went and fixed the tab, the
Switcher, the tree and the pickers one at a time.

Periwinkle is the interesting one. It has no semantic role in the underlying
theme system to chain to, because "this is a keyboard shortcut" is not a concept
any general-purpose theme has. So it carries a compromise value that works on
both grounds, and the two flagship themes override it with their exact pair. It
is also the rule with the sharpest edge: periwinkle is *only* ever a keybinding.
The moment it also means "selected", the reader has to work out which one it is,
and the vocabulary is back to where it started.

## Four things JavaFX does not do

### `:last-child` parses, and matches nothing

The rebuilt Settings pages put each group of settings on a card, with a hairline
between rows and no hairline under the last one. Every CSS author writes that
rule the same way, and it parsed without complaint and did nothing, because
JavaFX has no structural pseudo-classes at all. A selector it does not understand
is not an error; it simply never matches.

The fix is to know the thing CSS was going to tell you: a `ListChangeListener` on
the card's children tags the last row with a `settings-row-last` class and
retags it when the list changes. It works, and it is a listener where a selector
should have been.

### An author stylesheet beats the theme, regardless of specificity

Rounding the controls looked like a handful of `-fx-background-radius` rules.
Then editable cells and spinners grew corners in the wrong places.

JavaFX resolves author stylesheets above the user-agent stylesheet *per property*,
without weighing specificity between the two. The control theme sets a radius
reset on `.cell .text-input` and `.combo-box-base > .text-field` precisely so a
nested field stays square; a single flat rule in the app's own sheet outranked
every one of those resets at once, no matter how specific they were. Each reset
has to be restated beside the override. Not difficult — but "my rule is less
specific, so theirs wins" is the assumption you carry in from the web, and it is
not true here.

### An `HBox` shrinks every label by an equal share

Doctor lists each external tool as a row: the name, the command, the resolved
path, a state pill. Rows whose command was already an absolute path printed a
long path twice and pushed the row over its width — and what got cut was the
**tool name**. `Sh…`. `Docker…`. `…`.

That is not a bug in the layout; it is the layout doing what it says. An `HBox`
over its width distributes the shortfall across its children, and a `Label`'s
minimum width is roughly its ellipsis, so the name shrank exactly as far as the
path did. The path is the part that can afford to lose characters; the name is
the entire reason the row exists.

So the name (and the action buttons) pin `minWidth = USE_PREF_SIZE`, and only the
two path columns shrink — ellipsized from the *left*, because the binary at the
end of a path is the informative half, with the full text on hover. Worth
recording: no model test could have caught this. The row data was always correct.
It took laying the row out and measuring what came back, which is now what
`DoctorPaneLayoutFxTest` does.

### One flavour of SVG arc renders as nothing

The UI Kit authors its icons as 16-unit outline glyphs mixing `<path>`, `<circle>`
and `<rect>`. JavaFX's `SVGPath` takes path data only, so circles and rounded
rectangles become arc commands — which is exactly the construct its parser is
strictest about. Optimised SVG packs an arc's two boolean flags together,
`a1 1 0 000-.5`, and JavaFX rejects it as an invalid boolean flag. The glyph does
not fail loudly. It renders as an empty shape.

Editora had already met this once, when three build-tool brand logos came out of
an icon set with exactly that packing and drew nothing at all in the tool stripe.
The guard is a test that renders every converted glyph and asserts it has
non-empty bounds — the only assertion that distinguishes "this icon is subtle"
from "this icon is nothing".

## Two icon families, which must not touch

Thirty-four glyphs are specified by the kit as outlines. Editora has about a
hundred and sixty, so the rest stay as the filled Material glyphs until a full
line set exists. That means two families in one app for a while, and they are
coloured by different properties: an outline glyph by `-fx-stroke`, a filled one
by `-fx-fill`.

Which makes mixing them worse than merely inconsistent. Any existing
`X .toolbar-icon { -fx-fill: … }` rule outranks a bare `.icon-line`, so an
outline glyph that also carries the filled family's style class gets painted
solid — a filled blob where a line drawing should be. The rule is that they never
share a class, and every context an outline glyph appears in carries its own
stroke rule.

## Migrating a control without migrating its wiring

The kit draws on/off settings as toggle switches. Editora had checkboxes —
dozens, each with listeners, each written to by a palette command, each read by
the routine that keeps an open Settings window in step with a setting changed
from elsewhere.

Swapping the control type means touching all of that. So the switch is a *view*:
its selected state is bound bidirectionally to the existing `CheckBox`, which
stays alive and keeps every listener it had, but is never added to the scene.
Nothing downstream can tell the difference. A row migrates by handing its
checkbox to `switchFor(…)`, and its title is the checkbox's own localized label,
so the change needs no new translations in any of the six catalogs.

The description line beside it is worth a note on its own. It uses an existing
hint string wherever one existed, which is the difference between "add a card
layout" and "write and translate two hundred new sentences" — and it is why the
rebuilt Settings shipped in one release rather than three.

## The font nobody had set

Once the palette landed, the interface still did not look like the mockups, and
the reason turned out to be the least interesting possible one: no font had ever
been chosen for it.

AtlantaFX does not bundle or reference Inter, and none of the bundled themes set
a family on `.root`, so the whole interface had been falling back to JavaFX's
default logical "System" font since day one. Editora already ships Inter — for
the Markdown preview and for PDF export, where a predictable font matters.
Pointing the interface at the same one is a four-line stylesheet.

It is a *scene* stylesheet rather than a rule in the main sheet, for two reasons
that only show up later: dialogs and popups each live in their own scene and
would not inherit it, and a scene sheet survives the runtime call that swaps the
theme.

There was a detour here. macOS got the system font first, on the reasonable
theory that a Mac app should look like a Mac app. It made the interface differ by
platform for no design reason — and JavaFX cannot cleanly rasterize the bold
weight of the macOS system font, so it fakes it and mangles the glyphs. Which is
the same reason the Markdown preview had been pinned to Inter years earlier. The
lesson had already been learned once in this codebase and got learned again.

## The failure a stylesheet cannot have

The flagship themes are hand-written where it matters and vendored where it does
not: the palette block is authored, and the ~4,800-line component body is the
existing Blue theme's, because a light/dark pair's bodies differ by about forty
hover-state swaps and nothing a new palette needs.

That split has one hazard. If the body references a token the palette forgot to
define, **nothing fails**. Not the build, not a test, not a warning at startup.
JavaFX leaves the property unresolved and the control renders some default
colour, somewhere, in one state — a button's pressed background, say, which you
will find in three months.

So there is a test that reads the vendored theme, extracts every token it
references, and asserts the flagship palette defines all of them. It is a strange
test to write. It is also the only thing standing between a themed app and a
button that turns the wrong colour when you hold it down.

## Postscript: the feature your server ships switched off

Elsewhere in this release, pasting Java code now adds the imports it needs, and a
semicolon typed mid-expression moves to the end of the statement. Both are jdtls
delegate commands, and both took longer than they should have for the same
reason.

The parameters have to go over the wire as **stringified JSON**, not as a JSON
object. Sending the object — the obvious shape, the one every other request in
the protocol uses — deserializes to null on the server, which answers with an
internal error naming a getter on a class you have never heard of.

And smart semicolon detection answers `null` until you push
`java.edit.smartSemicolonDetection.enabled` through a configuration change. Before
that push, *every* argument shape answers null — which is indistinguishable from
"there is nothing to do at this position", and reads as wrong parameters. You can
spend a long time fixing a request that was never the problem.

This is the third instance of the same pattern in a year: `signatureHelp.enabled`,
then `provideFormatter`, now this. A capability advertised unconditionally in the
handshake, with the feature behind it inert until a preference is pushed. The
protocol has no way to express "supported, but off", so the server says
"supported" and means "ask again later". Both shapes here were established by
probing a real server and are pinned by tests, because the failure mode when they
drift is not an exception — it is a feature that quietly stops doing anything.

One design note on the semicolon, since it is a keystroke. It is never held back
waiting for the server. Making a character wait on a round trip is exactly the
latency an editor cannot afford, and a cold server would let it land after
whatever you typed next. So it inserts where you typed it, and the server's answer
moves it afterwards, as a single ranged edit so it is one undo step. The
consequence is stated rather than hidden: type straight through — the very common
`;` then Enter — and the document has moved past the answer, so the correction is
dropped rather than applied to text it was not computed for.

---

The full list is on the [What's New](/whats-new) page, and the
[0.10.0 release notes](/news/2026-08-03-editora-0-10-0-released) cover editor
groups, the menu bar, run configurations and per-project settings.
