---
title: "Editora 0.13.1: a startup crash on most non-AVX-512 CPUs"
description: "0.13.0 aborted a fraction of a second into startup on most consumer Intel from 12th gen onward and every AMD before Zen 4. If you installed it, upgrade — and here is exactly what happened."
date: 2026-08-27
version: "0.13.1"
---

**Editora 0.13.1** is out, and if you installed
[0.13.0](/news/2026-08-26-editora-0-13-0-released) you should take it. Grab it
from the [releases page](https://github.com/adriandeleon/Editora/releases/latest).

0.13.0 **crashed on startup on any CPU without AVX-512** — most consumer Intel
from the 12th generation onward, and every AMD before Zen 4. The JVM aborted with
`SIGILL` a fraction of a second in, on whichever thread happened to get there
first, so the crash landed somewhere different each time and sometimes not at
all: on a 13th-generation Core i7, four launches in six.

There is no workaround worth applying to the old build. Upgrade.

## What actually happened

The cause is the ahead-of-time cache that makes cold start about 40% faster. It
archives generated machine code as well as class metadata, and the **call
adapters** among that code are compiled for the CPU that built the release.

GitHub's Linux runners are Xeons with AVX-512. So the shipped adapters spilled
the first sixteen vector registers using ordinary AVX instructions and then
carried on into AVX-512 encodings for registers that only exist on such a chip.
The JVM maps an archive like that without complaint — its CPU-feature validation
does not extend to cached adapters — and the first thread to execute one hits an
instruction its processor refuses.

Adapter caching is now off, in the packaged launcher and in the training step
both, so a cache built from here on contains no such code and anyone still
holding a poisoned one no longer executes it.

**The startup win is kept in full.** On the affected machine, first paint measured
1024 ms with the adapters off against 1704 ms with the whole cache disabled —
they were contributing nothing to the improvement they broke.

## What this says about a green build

Worth stating plainly, because it shapes how much a passing CI run is worth:
**this was a lottery on which runner built the release, not a bug in Editora's
own code.**

Every test passed, because the archived instructions are perfectly legal on the
machine that generated them. No test could have caught it, because catching it
means running the built artifact on different silicon than built it — and the
release matrix builds each platform on its own runner precisely because
jpackage and JavaFX are host-specific.

A guard now fails the build if either flag is dropped. That is the only defence
available here: the symptom would otherwise reappear only on other people's
hardware, which is where it appeared this time.

Nothing else changed — no feature, no command, no configuration. 0.13.1 is
[0.13.0](/news/2026-08-26-editora-0-13-0-released) with the adapters left out of
the cache.

The complete list is on the [What's New](/whats-new) page.
