#!/usr/bin/env node
// Generates stand-in feature screenshots so the redesigned home grid + feature
// pages have imagery everywhere while real captures are still being recorded.
//
//   node scripts/gen-placeholder-shots.mjs          # only features missing a shot
//   node scripts/gen-placeholder-shots.mjs --force   # rewrite every .svg stand-in
//
// A raster screenshot (png/jpg/…) at public/screenshots/features/<slug>.<ext>
// always wins over the generated <slug>.svg (see [slug].astro / index.astro),
// so dropping in a real capture automatically retires its placeholder. This
// script never touches raster files.
import { readdirSync, readFileSync, writeFileSync, existsSync } from "node:fs";
import { resolve, join, dirname } from "node:path";
import { fileURLToPath } from "node:url";

const root = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const featuresDir = join(root, "src/content/features");
const shotsDir = join(root, "public/screenshots/features");
const RASTER = ["png", "jpg", "jpeg", "webp", "gif", "avif"];
const force = process.argv.includes("--force");

// Primer Dark palette (the app + site default). Fixed on purpose: an <img> SVG
// can't read the page's CSS vars, and a neutral dark placeholder reads fine
// under every site theme.
const C = {
  bg: "#0d1117", soft: "#161b22", border: "#30363d", text: "#c9d1d9",
  faint: "#6e7681", accent: "#58a6ff", kw: "#ff7b72", str: "#a5d6ff",
  fn: "#d2a8ff", ty: "#ffa657",
};

const esc = (s) =>
  s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");

const titleOf = (md) => {
  const m = md.match(/^title:\s*(.+)$/m);
  if (!m) return null;
  return m[1].trim().replace(/^["']|["']$/g, "");
};

// Greedy word-wrap to <= max chars per line, up to 3 lines.
const wrap = (text, max = 20, maxLines = 3) => {
  const words = text.split(/\s+/);
  const lines = [];
  let cur = "";
  for (const w of words) {
    if ((cur + " " + w).trim().length > max && cur) {
      lines.push(cur);
      cur = w;
    } else {
      cur = (cur + " " + w).trim();
    }
    if (lines.length === maxLines - 1 && cur.length > max) break;
  }
  if (cur) lines.push(cur);
  if (lines.length > maxLines) {
    lines.length = maxLines;
    lines[maxLines - 1] = lines[maxLines - 1].replace(/.{1}$/, "…");
  }
  return lines;
};

// Deterministic faux-code line widths per slug, so a stand-in doesn't reshuffle
// on every regen (no Math.random).
const codeLines = (slug) => {
  let seed = 0;
  for (const ch of slug) seed = (seed * 31 + ch.charCodeAt(0)) & 0xffff;
  const rand = () => ((seed = (seed * 1103515245 + 12345) & 0x7fffffff) / 0x7fffffff);
  const toks = [C.kw, C.fn, C.str, C.ty, C.text, C.text];
  const out = [];
  for (let i = 0; i < 8; i++) {
    const indent = 24 + (i % 3) * 16;
    const w = 60 + Math.floor(rand() * 260);
    out.push({ y: 70 + i * 30, x: indent, w, c: toks[Math.floor(rand() * toks.length)] });
  }
  return out;
};

const svgFor = (slug, title) => {
  const lines = wrap(title, 20, 3);
  const lineH = 30;
  const startY = 208 - ((lines.length - 1) * lineH) / 2;
  const codeRects = codeLines(slug)
    .map(
      (l) =>
        `<rect x="${l.x}" y="${l.y}" width="${l.w}" height="8" rx="4" fill="${l.c}" opacity="0.26"/>`,
    )
    .join("");
  const titleTspans = lines
    .map(
      (ln, i) =>
        `<tspan x="320" y="${startY + i * lineH}">${esc(ln)}</tspan>`,
    )
    .join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 360" width="640" height="360" font-family="ui-sans-serif, -apple-system, Segoe UI, Roboto, sans-serif">
  <rect width="640" height="360" fill="${C.bg}"/>
  ${codeRects}
  <rect width="640" height="36" fill="${C.soft}"/>
  <line x1="0" y1="36" x2="640" y2="36" stroke="${C.border}"/>
  <circle cx="20" cy="18" r="5" fill="#ff5f56"/>
  <circle cx="38" cy="18" r="5" fill="${C.ty}"/>
  <circle cx="56" cy="18" r="5" fill="#27c93f"/>
  <text x="82" y="22" fill="${C.faint}" font-size="12" font-family="ui-monospace, Menlo, monospace">editora</text>
  <rect x="150" y="150" width="340" height="120" rx="14" fill="${C.soft}" stroke="${C.border}"/>
  <text x="320" y="184" text-anchor="middle" fill="${C.accent}" font-size="13" font-family="ui-monospace, Menlo, monospace" font-weight="700">{ E }</text>
  <text text-anchor="middle" fill="${C.text}" font-size="21" font-weight="700">${titleTspans}</text>
  <rect x="548" y="52" width="76" height="22" rx="11" fill="${C.accent}"/>
  <text x="586" y="67" text-anchor="middle" fill="${C.bg}" font-size="11" font-weight="700" letter-spacing="0.5">STAND-IN</text>
</svg>
`;
};

const hasRaster = (slug) =>
  RASTER.some((e) => existsSync(join(shotsDir, `${slug}.${e}`)));

let written = 0,
  skipped = 0;
for (const file of readdirSync(featuresDir).filter((f) => f.endsWith(".md"))) {
  const slug = file.replace(/\.md$/, "");
  if (hasRaster(slug)) {
    skipped++;
    continue;
  }
  const svgPath = join(shotsDir, `${slug}.svg`);
  if (existsSync(svgPath) && !force) {
    skipped++;
    continue;
  }
  const title = titleOf(readFileSync(join(featuresDir, file), "utf8")) || slug;
  writeFileSync(svgPath, svgFor(slug, title));
  written++;
}
console.log(
  `Placeholder shots: wrote ${written}, skipped ${skipped} (raster/existing).`,
);
