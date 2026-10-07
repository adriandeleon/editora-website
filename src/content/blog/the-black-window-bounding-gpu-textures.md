---
title: "The black window: bounding GPU textures in JavaFX"
description: "A bug that only showed up in the packaged build: open enough files and the window goes black. The cause was GPU texture exhaustion."
date: 2026-06-05
author: Adrián De León
tags: [performance, javafx]
---

This one only reproduced in the *packaged* build, which made it especially fun:
open enough files, and eventually the whole editor window would go **black**. There
was no exception in the echo area and no crash, only a black rectangle where the
UI used to be.

## The clue: it scaled with open files

The black window got likelier the more files you had open. That pointed away
from any single document and toward something that *accumulated*. Digging into
the render thread turned up NullPointerExceptions deep in JavaFX's Prism
pipeline, on a null `RTTexture` / mask texture.

That explained it. JavaFX's Prism renderer keeps a **texture pool with a fixed
ceiling** (512 MB by default). When you exhaust it, texture allocation starts
returning null, and the render thread NPEs trying to use it, so the screen goes
black. It only showed up packaged because the dev run had different VRAM
headroom.

## The fix: don't let GPU resources grow with open files

The root problem was that GPU-backed resources scaled linearly with the number
of open buffers. There were two big offenders.

- **Minimaps.** Every open buffer kept a rendered minimap snapshot, a pinned
  GPU texture. Now a background (non-selected) tab drops its snapshot via
  `setRenderingActive(false)`, driven off the tab-selection listener, and
  regenerates it when shown again. Only the visible buffer holds a minimap
  texture.
- **Image caches.** The Markdown preview's image loader and the Mermaid renderer
  both cache `Image` objects, each a pinned texture. Those caches are now
  **LRU-bounded** instead of unbounded maps, so they can't grow without limit.

As a safety net, the packaged launcher (and `mvn javafx:run`, so dev matches
prod) also raises the caps: `-Dprism.maxvram=2G -Dprism.maxTextureSize=16384`.

## Bounding per-buffer GPU resources

This is now a checklist item: **any per-buffer `Canvas` or
`Image` must be released or bounded.** GPU memory isn't garbage-collected the way
heap is. A cached `Image` is a texture you're holding until you let go. If a
resource's lifetime is "one per open file" and files can pile up, that's a leak,
and it will surface as a black window in a user's packaged build rather than in
your dev run.
