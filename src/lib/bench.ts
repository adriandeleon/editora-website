// The startup benchmark: the one copy of every figure the home page's
// SpeedChart and the /compare page show, so the two cannot disagree.
//
// Every number here must come from a real run of scripts/bench/measure-editors.sh,
// which times all three editors with one metric on one machine. Do not add an
// editor you have not measured yourself that way: a startup comparison is only
// meaningful if every entry was timed identically, and a figure taken from
// someone else's blog post measures a different thing on different hardware.
// When the benchmark is re-run, replace the whole set (figures, versions,
// `measured` and `machine`) in one change.
//
// The metric is when the editor's window for the file appears: the moment its
// title names the file. That is earlier than the first frame showing the
// file's text, and both pages say so.

/** When, on what, and with which Editora the figures were taken. */
export const measured = {
  editoraVersion: "0.22.0",
  month: "October 2026",
  /** Short form for captions; `machine` below has it part by part. */
  machineShort: "an M5 MacBook Pro",
};

export type BenchRow = {
  name: string;
  ver: string;
  /** Median, in milliseconds. */
  ms: number;
  /** Fastest and slowest timed run. */
  lo: number;
  hi: number;
  self?: boolean;
};

export const startup: { file: BenchRow[]; project: BenchRow[] } = {
  // Five timed runs each, opening a 710-line Markdown file, no project.
  file: [
    { name: "Editora", ver: measured.editoraVersion, ms: 521, lo: 511, hi: 529, self: true },
    { name: "VS Code", ver: "1.142.0", ms: 589, lo: 575, hi: 594 },
    { name: "IntelliJ IDEA", ver: "2026.2.3", ms: 2183, lo: 2139, hi: 2293 },
  ],
  // Three timed runs each, opening a 117 MB, 3,680-file source repository.
  project: [
    { name: "Editora", ver: measured.editoraVersion, ms: 578, lo: 545, hi: 614, self: true },
    { name: "VS Code", ver: "1.142.0", ms: 664, lo: 654, hi: 664 },
    { name: "IntelliJ IDEA", ver: "2026.2.3", ms: 3156, lo: 2191, hi: 3973 },
  ],
};

/** Editora's own instrumentation (scripts/measure-startup.sh in the Editora
    repo) on the single-file fixture: window shown, and first frame with the
    file's text painted. */
export const editoraInternal = { windowMs: 475, firstPaintMs: 890 };

/** The machine every figure was measured on, part by part. */
export const machine: [string, string][] = [
  ["Model", "14-inch MacBook Pro, M5 generation (Mac17,2)"],
  ["Chip", "Apple M5, 10 cores (4 performance, 6 efficiency)"],
  ["Memory", "16 GB unified"],
  ["Storage", "Apple 1 TB internal SSD (APPLE SSD AP1024Z), 255 GB free"],
  ["OS", "macOS 27.0 (build 26A428)"],
  [
    "Displays",
    "AORUS FI32U at 5120×2880 (main, where the windows opened), alongside the built-in 3024×1964 Liquid Retina XDR",
  ],
  ["Power", "AC, battery at 80%"],
];

/** A duration in seconds, two decimals: 521 → "0.52s". */
export const secs = (ms: number) => (ms / 1000).toFixed(2) + "s";
