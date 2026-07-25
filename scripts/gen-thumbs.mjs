#!/usr/bin/env node
// Builds small WebP thumbnails for the home-page feature grid.
//
// The cards render at roughly 260px wide, but they were pointing straight at
// the full-resolution captures in public/screenshots/features — 9.5 MB across
// 19 PNGs, one of them 2.4 MB, for images displayed at a tenth of that size.
// Lazy loading hid the cost on first paint but anyone scrolling the grid paid
// it, on a page whose next section benchmarks startup time.
//
// Output goes to public/screenshots/features/thumbs/<slug>.webp at THUMB_W
// wide. The feature detail pages keep using the full-size original.
//
//   node scripts/gen-thumbs.mjs           # only missing/outdated thumbs
//   node scripts/gen-thumbs.mjs --force   # rebuild all
import { readdirSync, existsSync, mkdirSync, statSync } from "node:fs";
import { join, dirname, extname, basename } from "node:path";
import { fileURLToPath } from "node:url";
import sharp from "sharp";

const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const srcDir = join(root, "public/screenshots/features");
const outDir = join(srcDir, "thumbs");
const force = process.argv.includes("--force");

// 2x the ~260px card so it stays sharp on retina.
const THUMB_W = 560;
const RASTER = new Set([".png", ".jpg", ".jpeg", ".webp"]);

mkdirSync(outDir, { recursive: true });

let built = 0, skipped = 0, srcBytes = 0, outBytes = 0;

for (const file of readdirSync(srcDir)) {
  const ext = extname(file).toLowerCase();
  if (!RASTER.has(ext)) continue; // SVG stand-ins are already tiny
  const src = join(srcDir, file);
  const out = join(outDir, basename(file, ext) + ".webp");

  const sStat = statSync(src);
  srcBytes += sStat.size;

  if (!force && existsSync(out) && statSync(out).mtimeMs >= sStat.mtimeMs) {
    outBytes += statSync(out).size;
    skipped++;
    continue;
  }

  await sharp(src)
    .resize({ width: THUMB_W, withoutEnlargement: true })
    .webp({ quality: 78 })
    .toFile(out);

  outBytes += statSync(out).size;
  built++;
}

const mb = (b) => (b / 1024 / 1024).toFixed(2) + " MB";
console.log(
  `Thumbnails: built ${built}, skipped ${skipped}. ` +
    `Originals ${mb(srcBytes)} -> thumbs ${mb(outBytes)} ` +
    `(${Math.round((1 - outBytes / srcBytes) * 100)}% smaller).`,
);
