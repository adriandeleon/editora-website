// Checks the command ids and Emacs chords quoted in the docs and feature pages
// against the generated command data in src/lib/commands.ts, so a chord that
// moves in the app (or an id that never existed) fails here instead of going
// stale on the site. Run with `npm run check:docs`; exits non-zero on an error.
//
// What it checks, in src/content/docs/*.md and src/content/features/*.md:
//   1. A backticked token that looks like a command id (`<namespace>.<name>`,
//      where <namespace> is one commands.ts uses) names a real command.
//   2. In a table row that holds a command id and a key cell, the key is not
//      bound to a *different* command in the Emacs keymap, and a "(palette)"
//      cell is not used for a command that does have an Emacs chord.
// commands.ts keeps one chord per keymap, so a chord it doesn't know (an
// alternate binding) is reported as a note, not an error.
import { readFileSync, readdirSync } from "node:fs";
import { join } from "node:path";

const root = new URL("..", import.meta.url).pathname;
const src = readFileSync(join(root, "src/lib/commands.ts"), "utf8");

const emacsById = new Map(); // id -> Emacs chord, or undefined
const idByEmacs = new Map(); // Emacs chord -> id
for (const m of src.matchAll(/\{ title: "(?:[^"\\]|\\.)*", id: "([^"]+)"(?:, keys: "([^"]+)")?/g)) {
  emacsById.set(m[1], m[2]);
  if (m[2]) idByEmacs.set(m[2], m[1]);
}
if (emacsById.size < 100) {
  console.error(`check-doc-commands: parsed only ${emacsById.size} commands from commands.ts`);
  process.exit(2);
}
const namespaces = new Set([...emacsById.keys()].map((id) => id.split(".")[0]));

const FILE_EXT = /\.(json|md|xml|yml|yaml|toml|properties|java|ts|js|mjs|css|html|txt|log|sh|py|typ|csv|http|env|png|svg|jar|exe|app|d|mod|work|sum|scss|lock)$/i;
const ID_SHAPE = /^[a-z][A-Za-z]*(\.[A-Za-z][A-Za-z0-9]*)+$/;
const CHORD_SHAPE = /^(?:(?:[CMS]|Cmd)-)*[^\s-]+(?: (?:(?:[CMS]|Cmd)-)*[^\s-]+)*$/;

const errors = [];
const notes = [];
for (const dir of ["src/content/docs", "src/content/features"]) {
  for (const file of readdirSync(join(root, dir)).filter((f) => f.endsWith(".md")).sort()) {
    const rel = `${dir}/${file}`;
    const lines = readFileSync(join(root, rel), "utf8").split("\n");
    let fenced = false;
    lines.forEach((line, i) => {
      if (/^\s*(```|~~~)/.test(line)) fenced = !fenced;
      if (fenced) return;
      const at = `${rel}:${i + 1}`;
      const ticks = [...line.matchAll(/`([^`]+)`/g)].map((m) => m[1]);
      const ids = ticks.filter(
        (t) => ID_SHAPE.test(t) && namespaces.has(t.split(".")[0]) && !FILE_EXT.test(t),
      );
      for (const id of ids) {
        if (!emacsById.has(id)) errors.push(`${at}  unknown command id \`${id}\``);
      }

      // Table rows: exactly one known command id, then a key cell.
      if (!line.trimStart().startsWith("|")) return;
      const known = ids.filter((id) => emacsById.has(id));
      if (known.length !== 1) return;
      const id = known[0];
      const cells = line.split("|").map((c) => c.trim());
      const keyCell = cells[cells.findIndex((c) => c.includes(`\`${id}\``)) + 1] ?? "";
      const bound = emacsById.get(id);
      if (/^\(palette\)$/i.test(keyCell)) {
        if (bound) errors.push(`${at}  \`${id}\` is listed as (palette) but is \`${bound}\` in the Emacs keymap`);
        return;
      }
      const chords = [...keyCell.matchAll(/`([^`]+)`/g)].map((m) => m[1]).filter((c) => CHORD_SHAPE.test(c));
      // A row may cover a pair of commands ("next / previous") with both chords.
      if (bound && chords.some((c) => c.toLowerCase() === bound.toLowerCase())) return;
      for (const chord of chords) {
        const owner = idByEmacs.get(chord);
        if (owner && owner !== id) {
          errors.push(`${at}  \`${chord}\` is listed for \`${id}\` but is \`${owner}\` in the Emacs keymap`);
        } else if (!owner) {
          notes.push(`${at}  \`${chord}\` for \`${id}\` is not its primary Emacs chord (${bound ? `\`${bound}\`` : "unbound"})`);
        }
      }
    });
  }
}

for (const n of notes) console.log(`note   ${n}`);
for (const e of errors) console.log(`error  ${e}`);
console.log(`check-doc-commands: ${emacsById.size} commands, ${errors.length} error(s), ${notes.length} note(s)`);
process.exit(errors.length ? 1 : 0);
