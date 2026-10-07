---
title: A faster cold start with a GUI-trained AOT cache
description: "How Editora's installers shave ~300–480 ms (~28%) off the time to first frame using JDK 25's AOT cache, trained against a real GUI run."
date: 2026-06-13
author: Adrián De León
tags: [performance, packaging]
---

Editora is a JavaFX app, and JavaFX apps have a reputation for a slow *first*
frame: the moment you launch, the JVM has to load and link a mountain of
scene-graph, control, and CSS classes before anything appears. Once it's warm
it's fast, but cold start is the first impression, and it was slower than I
wanted.

The newest installers cut the time to first frame by **~28%, roughly
300–480 ms on macOS**, using JDK 25's new **AOT cache** (the first piece of
Project Leyden to ship). This post covers how it works, and the one non-obvious step that
made it pay off.

## What the AOT cache does

JDK 25 can record the work the JVM normally repeats on every launch into a cache
file, then memory-map it on the next start instead of redoing it. That work is
loading and linking classes, plus some profile data. You point the launcher at a file with
`-XX:AOTCache=...` and the JVM does the rest.

A cache only helps for classes that were *loaded* when it was recorded, and that
matters for a GUI app.

## A headless trainer buys you almost nothing

My first instinct was to train the cache in CI the cheap way: run the app
headless, let it initialize, capture the cache. That gave **basically zero
speedup.**

The expensive part of JavaFX startup is loading
the scene/control/CSS classes, and **those don't load until a window actually
renders.** A headless run never paints a frame, so it never touches the classes
that matter. To capture them, the training run has to be a *real GUI run.*

So that's what the build does now. Under a `-Deditora.aotTrainExit` flag,
`WindowManager` builds and renders the first real window, lets it settle briefly
(long enough to pull in the highlighting classes too), and then calls
`System.exit`. The cache it records reflects an actual launch, which is
what we want to replay.

On Linux there's no display in CI, so the training run happens under `xvfb` (the
release workflow installs it); macOS and Windows runners use their session.

## Baking it into the installer

The tricky part is making a recorded cache valid inside the *shipped* app, which
lives at a different path on every user's machine. The build solves it with a
two-phase `jpackage`:

1. Build an `APP_IMAGE`: the app plus a jlinked runtime.
2. Run `scripts/aot_build.java` (via the build JDK), which trains a full-GUI
   cache **against the image's own runtime**, writes `editora.aot` into the app
   dir, and strips the now-unneeded `bin/`.
3. Wrap that image into the real installer with `jpackage --app-image`.

Because the archived classes live in the jlinked `lib/modules` (keyed to the
runtime image, not absolute paths), the cache stays valid after the installer
drops the app wherever the user puts it. The launcher's `.cfg` just carries
`-XX:AOTCache=$APPDIR/editora.aot`.

## It can't make things worse

A performance optimization that can break a launch isn't worth it, so this one
is **fully failure-tolerant.** If training fails (no display on a runner, any
error at all), the build ships without the cache. And at runtime, a
missing or invalid `-XX:AOTCache` file degrades gracefully to a normal start
under JDK 25's default `AOTMode=auto`. Worst case, you get today's startup; best
case, you get a noticeably quicker one.

The cost is disk space. The cache is about **60 MB**, though installers compress
it well, and the macOS DMG stayed around 81 MB.

## While I was in there: footprint

The same change tuned the packaged runtime for a single-user desktop editor:

- **`-Xmx2g` + `-XX:+UseSerialGC`**: predictable RSS and sub-millisecond GC
  pauses, with none of the parallel-GC thread overhead a mostly-idle editor
  doesn't need.
- **Runtime stripping** at jlink time (`--strip-debug`, `--no-man-pages`,
  `--no-header-files`, `--compress=zip-6`): a smaller download and a smaller
  mapped footprint.

Both the `dist` launcher options and the `mvn javafx:run` dev options use the
same flags, so development matches production.

---

If you
grab a [fresh installer](/#download), the first window should be up noticeably
quicker than before. As always, the gory details are in the
[changelog](https://github.com/adriandeleon/Editora/blob/master/CHANGELOG.md).
I'm happy to talk about it in
[Discussions](https://github.com/adriandeleon/Editora/discussions).

---

**Update (October 2026).** Several claims above did not hold up in later
releases, including the heading's claim that the cache can't make things worse.

- **Windows.** Stripping the runtime's `bin/` after training removed the JVM
  itself on Windows, where it lives in that folder, so Windows installs failed
  to launch. [0.9.2](/news/2026-07-08-editora-0-9-2-released) limits the strip
  to macOS and Linux.
- **Apple silicon.** macOS arm64 shipped without the cache from 0.9.1 through
  0.9.8, because the training run crashed on the CI runner's virtual GPU. The
  macOS figure above was not what those builds delivered until
  [0.9.9](/news/2026-07-21-editora-0-9-9-released) fixed the training run.
- **The garbage collector.** The serial collector did not give the short pauses
  and small footprint described above. Measured on a real session it kept a
  548 MB young generation against a live set of about 72 MB, and its longest
  pause was 138 ms. [0.11.0](/news/2026-08-11-editora-0-11-0-released) moved
  the packaged app to G1.
- **Some CPUs.** 0.13.0 crashed at startup on processors without AVX-512,
  because the cache also archived machine code compiled for the CPU that built
  the release. [0.13.1](/news/2026-08-27-editora-0-13-1-released) leaves that
  code out of the cache and keeps the startup gain.
