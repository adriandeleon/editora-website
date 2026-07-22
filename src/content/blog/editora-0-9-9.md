---
title: "Editora 0.9.9: asking before it builds"
description: "Why opening a repository shouldn't be enough to run its code, what it takes for a dimmed command to be useful rather than annoying, and a class cache that quietly went missing on Apple silicon for eight releases."
date: 2026-07-21
author: Adrian De Leon
tags: [release]
---

Three things in 0.9.9 are worth explaining rather than just listing: a security
prompt that had to be designed carefully to be worth anything, a palette feature
that was shipped but barely wired up, and a performance bug that was invisible
on every machine except the ones that built the release.

## Opening a repository shouldn't run its code

Cloning a repository and opening it in an editor feels like a read-only act. It
mostly is, until you hit build.

Maven and Gradle projects conventionally ship their own wrapper, `./mvnw` or
`./gradlew`, checked into the repository. It's a convenience: contributors don't
need the right Maven version installed. It's also a shell script that the
repository controls, and Editora prefers it over the one on your `PATH`, exactly
as the convention intends. So the first build in a freshly cloned repository
runs code that came with the repository, with your privileges.

Editora now asks first, once per folder, with trust inherited by subfolders so a
multi-module repository doesn't nag. Two details mattered more than the prompt
itself.

**Declining has to mean no build.** The tempting fallback is "fine, use the
`mvn` on your `PATH` instead". That would be security theater. A hostile
`pom.xml` or `build.gradle` executes code through your own Maven too, because
build plugins and Gradle scripts run in-process. If you don't trust the
repository enough to run its wrapper, you don't trust it enough to build it at
all, so "no" means no.

**The gate has to be exactly the wrapper case.** There's one function that
answers "would this run repository-controlled code as `argv[0]`?", and the same
function decides whether the wrapper is preferred in the first place. A test
pins those two in agreement, so a Settings command override can't silently
disarm the prompt. npm, Cargo and Go launch your own toolchain and are never
gated; Run and Debug always invoke a `PATH` interpreter, so they don't prompt
either.

The honest limit: this closes the `argv[0]` hole, not the broader one. Running
`mvn` on a hostile build file is still executing repository-controlled content.
Gating *that* means trusting a folder before any build at all, which is a much
bigger behavior change than this release wanted to make.

## A feature that was on, but barely

Editora already dimmed palette commands whose feature was switched off. In
principle. In practice only 17 features were wired into the gate, so on a fresh
install roughly 19 of about 550 commands were ever affected. Debugging, which is
off by default, showed its entire command family as fully available. The feature
existed and looked broken.

Two things fixed it. First, the missing features got wired up: debugging, AI, the
agent, TODO highlighting, spell check, the CSV grid, structured previews,
Markdown lint, EditorConfig. Second, and more useful day to day, commands are now
dimmed when they have **nothing to act on**, not only when their feature is off.
Markdown commands outside a Markdown file. CSV commands outside a CSV. Git
commands outside a repository. Debugger steps with nothing suspended.

That second half needed a deliberate bias. Dimming a command that would in fact
have worked is a worse failure than leaving one lit, so the context rules are
conservative: they only cover families where every member genuinely needs the
same context, with explicit carve-outs (Clone works fine outside a repository).

The part that turns this from annoying into useful is the tooltip. A dimmed row
now says *why*, and what to do: "Unavailable, this feature is turned off. Run
'Toggle Debugging Support' to enable it." It names a command you can type into
the same palette you're already looking at. And when Simple UI mode is what's
forcing a feature off, the tooltip points at Simple mode rather than the
feature's own toggle, which wouldn't have helped.

Two invariants keep it from eating itself: the toggle that re-enables a feature
is never dimmed, and the "why" is derived from the same logic that decides the
dimming, so a command can't be dimmed for a reason the tooltip can't state.

## Consistent titles are a search feature

Three quarters of command titles already read `Family: Action` (`Edit: Cut`,
`Git: Push`). The `view.*` family was a coin flip: 46 prefixed, 48 bare. That
sounds cosmetic. It isn't, because the palette is a search box. Scanning for
view-related commands surfaced half of them and scattered the rest.

All 48 are prefixed now, in all six languages, along with `tool.*` (now
`Tool Window: <name>`, reusing each window's already-translated name), the
`markdown.*` formatting commands, and `lsp.*`. One title was actively harmful:
`tool.go` was the bare word "Go", which collided with the 33-command `Go:`
navigation family in palette search.

A new test pins the convention: a command family is either fully prefixed or
fully bare, across all six catalogs. It can't drift back into the half-and-half
state that caused this.

## Eight releases without a class cache

Editora trains an ahead-of-time class cache at build time, worth 300 to 480 ms
of cold start. Every tagged release from 0.9.1 through 0.9.8 shipped macOS arm64
without it.

The training run launches the real GUI. When JavaFX 26 made Metal the default
pipeline on macOS, the arm64 CI runner's paravirtual GPU aborted the trainer with
an Objective-C exception, a process-level `abort()` that JavaFX's own pipeline
fallback chain can't catch. The x64 runner has a different virtual GPU and
trained fine every time, so it never looked like a systemic failure.

It never reproduced on real Apple silicon, which has a proper Metal device. That
is precisely why device testing missed it for eight releases: the machines that
could reproduce it were the CI runners, and the only symptom there was one
absent file in a ten-thousand-line build log.

The fix is two-part. Training pins a pipeline any runner can render, while the
shipped app still uses Metal (the cache's value is mostly scene graph, controls
and CSS, which are pipeline-independent). And a missing cache now fails loudly,
with a per-target annotation, instead of silently costing 28% of startup.

## Starting up, and staying put

Opening a file from the file manager used to restore your entire previous
session first, then show the file you asked for. With eight tabs that's a second
or two of the wrong file on screen, plus a language server starting for each
restored tab. Now the requested file is shown first, servers start per tab as
you visit it, and `--no-session` skips the saved tabs entirely. Measured on an
8-file session opening one file: five child processes down to one, 19 seconds of
CPU down to 8, 917 MB down to 696.

Two related jumps are gone as well. A restored file no longer flashes to the top
of the document, which turned out to be two separate causes: applying syntax
highlighting collapsed the viewport a few frames after the file had already
painted at its saved caret, and the virtual flow re-anchored itself during a
plain layout pass. Both are corrected in the same frame they happen. Correcting
a frame later, the obvious approach, only converts a jump into a bounce, which
is what two earlier attempts shipped and reverted.

And in Split view, a progressively rendered preview (typst, Mermaid, image-heavy
Markdown) no longer drags the editor around. The preview pane re-anchoring as
its content grew was indistinguishable from you scrolling it, worth about 100
lines of drift over two seconds.

## Smaller things

Case-insensitive search now matches case pairs that change length, so `STRASSE`
finds `Straße` and `FI` finds `ﬁ`. That's literal search in open buffers; regex
and the ripgrep-accelerated half of Find in Files keep their own engines' simpler
folding, which is a real limit rather than an oversight.

The HTTP client became the `.http` file's preview instead of a separate tool
window, so the response sits beside the request in the same three-mode view
everything else uses. Console panels finally scroll with your configured
keybindings, and scrolling back through a running build stays put instead of
being yanked to the tail by the next line of output. Test Results tracks the
running test and shows a passing test's captured output, which was being dropped
entirely.

And adding a bookmark moved off the gutter click. The breakpoint strip and the
Run arrow are narrow targets inside that same gutter, so a slightly-off click
added a stray bookmark instead of setting a breakpoint. It's `C-c m` or the
editor right-click menu now.

## Get it

Download from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest). The
full changelog is on the [What's New](/whats-new) page.
