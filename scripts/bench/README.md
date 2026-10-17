# Startup benchmark

Produces the two tables on `/compare` and the bar chart in "Built for speed" on
the home page. Both read their numbers from `src/pages/compare.astro` and
`src/components/SpeedChart.astro`; nothing is generated at build time, so a
re-run means editing those two files by hand.

## Running it

    # single file, 5 timed runs
    scripts/bench/measure-editors.sh -n 5 -m bench-710 -- /path/to/a-710-line.md

    # project folder, 3 timed runs
    MODE=project scripts/bench/measure-editors.sh -n 3 -m editora-src -- /path/to/clone

`-m` is the substring the ready window's title must contain. It needs `swiftc`
(Xcode command-line tools) to build the watcher on first use, and a real
display, since it waits on actual windows.

## The metric

Launch of the binary → the window server reports an on-screen window, owned by
that editor, whose title names the file or project. It is deliberately not "any
window": IntelliJ shows a splash and then a welcome window titled `IntelliJ
IDEA` long before an editor exists.

It is also **not** the first frame showing the file's text. The window arrives
first. On an M5, Editora's own instrumentation (`scripts/measure-startup.sh` in
the Editora repo) put window-shown at 475 ms and first paint at 890 ms for the
same 710-line fixture. VS Code and IntelliJ have a gap of their own that cannot
be measured from outside, which is why all three are reported on the earlier
event and the pages say so.

## Things that will bite you

- **Poll resolution.** The July 2026 run used `osascript` per poll at ~1.6 s a
  call, so every figure was rounded up to the next poll — that is why Editora
  and VS Code came out 2 ms apart then. The watcher here polls every 5 ms.
  Numbers from before October 2026 are not comparable with numbers after.
- **Editora needs `--project` for the folder test.** `updateWindowTitle()` names
  the window `<file> — <project> — Editora`; with no active buffer and no
  registered project the title is just `Editora`, so a bare folder argument
  gives the watcher nothing to match. `MODE=project` adds `--project` and
  `--single-window`.
- **IntelliJ's trust dialog.** A folder it has not seen blocks on "Trust
  Project?" — four buttons, and the window has no title, so it looks like a
  hang. Add the path to
  `~/Library/Application Support/JetBrains/IntelliJIdea<ver>/options/trusted-paths.xml`
  first, and take it out again afterwards.
- **IntelliJ reads `.idea/.name`.** A repo with a stale one is titled after
  that, not after its folder, so `-m <folder-name>` will never match. Benchmark
  a clean clone.
- **Discard the warm-up.** It is not a formality. In the October 2026 project
  run VS Code's warm-up was 3.08 s against the 0.66 s it then settled to.
