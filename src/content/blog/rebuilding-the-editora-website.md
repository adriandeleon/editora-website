---
title: "Rebuilding the website, and two bugs it turned up"
description: "The old home page described a keyboard-driven editor without ever showing one. Fixing that surfaced a CSS trap that froze the new search, and 71 commands missing from the docs."
date: 2026-07-24
author: Adrian De Leon
tags: [website, css]
---

Editora's home page had a problem I had been ignoring: it described the editor
instead of showing it. One screenshot, a download button, and a list of feature
cards written as text. The single thing that makes the editor worth using, that
every action is a command you can find by typing a few letters, was invisible on
a static page.

So the site got rebuilt. Most of that work is unremarkable (write components,
pick spacing, argue with yourself about headings) but two things went wrong in
ways worth writing down.

## Show the verb, not the noun

A screenshot of a text editor looks like every other text editor. What separates
Editora is a motion: you press `M-x`, type four letters, and the thing you
wanted runs.

The hero now animates exactly that. It is not a video or a GIF, just CSS
keyframes over real markup: a query reveals itself one character at a time, the
non-matching rows collapse, and the match lights up. It costs nothing to
download, stays sharp on any display, recolours with whichever of the six site
themes you pick, and freezes on a tidy frame if you have asked your system to
reduce motion.

The same reasoning drove the mode ladder further down the page. Editora has four
levels of interface, from Zen (nothing but text) to the full IDE, and describing
them in a paragraph never landed. Now each is a small mockup that shows what it
hides. Building those honestly meant reading `ui/Chrome.java` rather than
trusting my own docs, which is how I noticed a distinction the page now states
outright: Zen and Expert only *hide* the interface, with the language server
still running behind it. Simple UI is the one that genuinely turns features off.

## The modal that ate the page

The site had no search. For a documentation site with 700-odd pages, 588 of them
command references, that is a real gap, so I added a `Cmd-K` palette backed by
[Pagefind](https://pagefind.app/), which builds a static index at build time and
runs entirely in the browser.

I tested it, it worked, I moved on. Then I clicked the button like a user would
and the page locked up. The modal opened, typing did nothing, Escape did
nothing, and the page would not scroll.

The cause is a CSS rule that is easy to forget:

> An ancestor with `filter`, `transform`, `perspective`, or **`backdrop-filter`**
> becomes the containing block for any `position: fixed` descendant.

The site header is a translucent blurred bar:

```css
.site-nav {
  position: sticky;
  backdrop-filter: blur(8px);
}
```

I had put the search trigger in the nav and the modal markup right next to it.
So the modal, `position: fixed; inset: 0`, was not sized against the viewport at
all. It was sized against the header. Measured in the browser:

```text
viewport:   1280 x 720
modalRect:  1280 x 88     <- the height of the nav bar
```

Everything followed from that. The input rendered outside the visible strip, so
it never took focus, so every keystroke went to `<body>`. My Escape handler was
bound to the input, which no longer had focus, so Escape was dead. And
`openModal()` had already set `body { overflow: hidden }` to stop background
scrolling. Locked page, no way out.

The fix is one line, moving the modal out from under the blurred ancestor before
anything else runs:

```js
if (modal.parentElement !== document.body) document.body.appendChild(modal);
```

Two smaller repairs went in beside it, because the first bug only became
*unrecoverable* through them: Escape is now handled globally rather than on the
input, so a stray focus can never trap the page, and the field is focused both
immediately and after paint.

The part I keep thinking about is why my own testing missed it. I had verified
the search by driving it from the console:

```js
input.value = 'git blame';
input.dispatchEvent(new Event('input', { bubbles: true }));
```

That exercises the search pipeline beautifully and skips the entire click,
layout, and focus path where the bug lived. It told me the query engine worked,
and I read it as "search works." Synthetic events are a fine way to test a
function and a poor way to test a feature.

## Seventy-one commands that were never there

While testing search I looked up `rectangle`, expecting Editora's Emacs-style
rectangle commands. One result, a blog post. `kill rectangle` returned nothing.

Search was right. The pages did not exist. The site's command reference is
generated from the app source by a script that finds registrations with a
regular expression:

```java
Pattern.compile("Command\\.of\\(\\s*\"([^\"]+)\"\\s*,");
```

That only sees ids written as string literals, and Editora registers plenty of
commands other ways. The build tools build theirs by concatenation, one set per
tool:

```java
registry.register(Command.of("tool." + id, ops::openTasks));
registry.register(Command.of(id + ".showActions", this::showActionsPopup));
```

Others pass a constant (`Command.of(KeyDispatcher.UNIVERSAL_ARGUMENT, ...)`), and
`BuildTool` keeps its own toggle ids in a package the script never scanned. Three
different shapes, all invisible to a literal match, and the result was 36 missing
commands on top of 35 the script had already been failing to pick up. The entire
Maven, npm, Cargo, Go, and Gradle integration was absent from the site, so
searching "gradle" found nothing that could run Gradle.

I could have taught the pattern about each shape. Instead the generator now
falls back to the thing that is already authoritative: the translation
catalogue. Every statically registered command has a `command.<id>` title there,
because that is how the app resolves its own titles, while runtime-generated
commands pass an explicit title and have no key. So after the source scan, any
titled id the scan missed is adopted:

```java
for (String key : props.keySet()) {
    if (!key.endsWith(".desc")) ids.add(key);
}
```

The count went from 517 to 588, which matches the app exactly, and it stays
correct as commands are added because nobody has to remember to update a regular
expression.

Both bugs have the same shape, now that I look at them together. A check that
was *nearly* right, believed because it produced a plausible answer. A regular
expression that found most commands. A test that exercised most of the feature.
The gap in each case was quiet, and neither would have surfaced without doing
the boring thing: clicking the button, and searching for something I already
knew should be there.
