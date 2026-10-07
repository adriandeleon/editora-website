---
title: "Editora 0.12.2 and 0.12.3: finishing what the single-instance handoff started"
description: "Opening a file from your file manager now raises the editor and gives the file a window of its own. Both are fixes to the handoff 0.12.1 introduced."
date: 2026-08-17
version: "0.12.3"
---

**Editora 0.12.3** is out, and it comes with **0.12.2**. They are two fixes to
the same thing, so this note covers both. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/latest).

[0.12.1](/news/2026-08-17-editora-0-12-1-released) made a file opened from your
file manager reuse the editor you already have running, instead of starting a
second one. That was the right change to the process, since a second JVM and
a second set of language servers is a lot to pay for a click. But it got two
things wrong about the window, and you notice both on the first try.

**The editor now comes to the front (0.12.2).** The file opened, and the window
stayed behind the file manager with a notification you had to click. The cause
is the compositor rather than Editora. When a file manager launches a **new** process it
hands that process an activation token, so the window it maps counts as
user-initiated and gets focused. Forwarding breaks that chain: the token goes to
the forwarding process, which delivers the file and exits *without ever mapping
a window*, while the process that owns the window is an older one you have not
touched recently. Its request to raise itself is therefore an unsolicited focus
request from a background application, which GNOME refuses, marking the window
as demanding attention instead.

The fix is to briefly pin the window above the others and release it straight
after. A compositor honours that, because "always on top" is a window *state*
rather than a focus request, and nothing is left pinned. The raise also happens
*before* the file is opened rather than after, so it is immediate instead of
waiting on the file to load, and it still happens if opening fails.

**A file from the file manager gets a window of its own (0.12.3).** It was
landing as a tab in whatever window you were working in. With the "Editora
Expert Mode" desktop entry it also restyled that window's chrome. Before
the handoff existed, such a click started its own process and so its own window;
0.12.1 was meant to stop duplicating the process, not to change what the
click does. The requested focus mode now applies to the new window, where it is
unambiguous, rather than being imposed on one already in use.

**Unless the file is already open**, in which case the window holding it is
brought forward instead. Two independent buffers over one file lose edits,
because saving one leaves the other stale behind an external-change prompt, and
re-opening a file you already have open is an ordinary thing to do.

These windows are left out of your saved layout, so a file opened
from the file manager doesn't come back as an empty window on the next launch.

Everything else about the handoff is unchanged: it applies only to a launch that
is purely "open these files", `--new-instance` still forces a separate editor,
and an instance is scoped to its config directory. The
[command line](/docs/cli#one-editor-not-two) page describes the whole behaviour.

The complete list is on the [What's New](/whats-new) page.
