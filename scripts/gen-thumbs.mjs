#!/usr/bin/env node
// Builds small WebP thumbnails for the feature cards and the /screenshots
// gallery.
//
// The cards render at roughly 260px wide, but they were pointing straight at
// the full-resolution captures in public/screenshots/features — 9.5 MB across
// 19 PNGs, one of them 2.4 MB, for images displayed at a tenth of that size.
// Lazy loading hid the cost on first paint but anyone scrolling the grid paid
// it, on a page whose next section benchmarks startup time.
//
// Output goes to public/screenshots/<dir>/thumbs/<name>.webp at THUMB_W wide.
// Feature pages and the full-size viewer keep using the originals.
//
//   node scripts/gen-thumbs.mjs           # only missing/outdated thumbs
//   node scripts/gen-thumbs.mjs --force   # rebuild all
import { readdirSync, existsSync, mkdirSync, statSync } from "node:fs";
import { join, dirname, extname, basename } from "node:path";
import { fileURLToPath } from "node:url";
import sharp from "sharp";

const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const force = process.argv.includes("--force");

// 2x the ~260px card so it stays sharp on retina.
const THUMB_W = 560;
const RASTER = new Set([".png", ".jpg", ".jpeg", ".webp"]);

// Folders under public/screenshots that get a thumbs/ folder. The feature
// cards use the light capture only; the gallery follows the site theme, so the
// docs, map and hero captures get their `.dark` twins thumbed too.
const DIRS = [
  { dir: "features", dark: false },
  { dir: "docs", dark: true },
  { dir: "map", dark: true },
  { dir: "hero", dark: true },
  { dir: "", dark: false },
];

let built = 0, skipped = 0, srcBytes = 0, outBytes = 0;

for (const { dir, dark } of DIRS) {
  const srcDir = join(root, "public/screenshots", dir);
  const outDir = join(srcDir, "thumbs");
  mkdirSync(outDir, { recursive: true });
  // A picture can ship as both .jpg and .webp; one thumbnail covers both.
  const done = new Set();

  for (const file of readdirSync(srcDir).sort()) {
    const ext = extname(file).toLowerCase();
    if (!RASTER.has(ext)) continue; // SVG stand-ins are already tiny
    const name = basename(file, ext);
    // A `<name>.dark.<ext>` capture is the dark-theme twin.
    if (!dark && name.endsWith(".dark")) continue;
    if (done.has(name)) continue;
    done.add(name);
    const src = join(srcDir, file);
    const out = join(outDir, name + ".webp");

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
}

const mb = (b) => (b / 1024 / 1024).toFixed(2) + " MB";
console.log(
  `Thumbnails: built ${built}, skipped ${skipped}. ` +
    `Originals ${mb(srcBytes)} -> thumbs ${mb(outBytes)} ` +
    `(${Math.round((1 - outBytes / srcBytes) * 100)}% smaller).`,
);
