#!/usr/bin/env node
// Builds a social card per key page, so a shared Editora link shows what it
// points at instead of the same generic image every time.
//
// Cards are drawn as SVG (brand background, wordmark, page title, kicker) and
// rasterised with sharp, which Astro already depends on. Output lands in
// public/og/<slug>.png; a page opts in with <Layout ogImage="og/<slug>.png">.
//
//   node scripts/gen-og-images.mjs [--force]
import { mkdirSync, existsSync, writeFileSync } from "node:fs";
import { join, dirname } from "node:path";
import { fileURLToPath } from "node:url";
import sharp from "sharp";

const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const outDir = join(root, "public/og");
const force = process.argv.includes("--force");
mkdirSync(outDir, { recursive: true });

// Primer Dark, matching the site's default theme.
const C = { bg: "#0d1117", panel: "#161b22", border: "#30363d", text: "#c9d1d9", accent: "#58a6ff", faint: "#6e7681" };

const cards = [
  { slug: "compare", kicker: "Comparison", title: "How Editora compares" },
  { slug: "features", kicker: "Features", title: "Every feature, one keystroke away" },
  { slug: "docs", kicker: "Documentation", title: "Editora documentation" },
  { slug: "blog", kicker: "Blog", title: "Notes from building Editora" },
  { slug: "news", kicker: "News", title: "What's new in Editora" },
  { slug: "plugins", kicker: "Plugins", title: "Extend Editora with plugins" },
  // No command count here: this string is baked into a PNG, so it cannot track
  // the generated total the way the pages do, and would quietly go stale.
  { slug: "commands", kicker: "Reference", title: "The command reference" },
];

const esc = (s) => s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");

// Rough character-count wrap: the card font is fixed, so this is good enough
// and avoids pulling in a text-measurement dependency.
function wrap(text, max = 26, maxLines = 2) {
  const words = text.split(/\s+/);
  const lines = [];
  let cur = "";
  for (const w of words) {
    if ((cur + " " + w).trim().length > max && cur) { lines.push(cur); cur = w; }
    else cur = (cur + " " + w).trim();
  }
  if (cur) lines.push(cur);
  return lines.slice(0, maxLines);
}

const svg = ({ kicker, title }) => {
  const lines = wrap(title);
  const startY = 330 - (lines.length - 1) * 34;
  const tspans = lines
    .map((l, i) => `<tspan x="80" y="${startY + i * 68}">${esc(l)}</tspan>`)
    .join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="1200" height="630" viewBox="0 0 1200 630" font-family="Helvetica, Arial, sans-serif">
  <rect width="1200" height="630" fill="${C.bg}"/>
  <circle cx="1010" cy="120" r="300" fill="${C.accent}" opacity="0.10"/>
  <rect x="80" y="70" width="52" height="52" rx="13" fill="${C.panel}" stroke="${C.border}"/>
  <text x="106" y="104" text-anchor="middle" fill="${C.accent}" font-size="21" font-weight="700" font-family="ui-monospace, Menlo, monospace">{E}</text>
  <text x="150" y="105" fill="${C.text}" font-size="30" font-weight="700">Editora</text>
  <text x="80" y="196" fill="${C.accent}" font-size="21" font-weight="700" letter-spacing="2.5">${esc(kicker.toUpperCase())}</text>
  <text fill="${C.text}" font-size="58" font-weight="700">${tspans}</text>
  <rect x="80" y="536" width="132" height="4" rx="2" fill="${C.accent}"/>
  <text x="80" y="590" fill="${C.faint}" font-size="23">editora-project.dev</text>
</svg>`;
};

let built = 0, skipped = 0;
for (const card of cards) {
  const out = join(outDir, `${card.slug}.png`);
  if (!force && existsSync(out)) { skipped++; continue; }
  await sharp(Buffer.from(svg(card))).png().toFile(out);
  built++;
}
console.log(`OG images: built ${built}, skipped ${skipped} (in public/og/).`);
