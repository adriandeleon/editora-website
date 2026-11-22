#!/usr/bin/env node
// Turns the raw 2x captures from Editora's screenshot runner into what the site
// embeds. Editora's `java scripts/screenshots/Shoot.java --out <this repo>/public/screenshots …`
// writes lossless full-window PNGs; this script then, under public/screenshots/:
//
//   - crops the docs pictures that only show part of the window (CROPS below,
//     in 2x pixels; one capture can feed two crops, and such a source is deleted),
//   - palette-compresses every docs/, features/ and map/ PNG (about 3x smaller),
//   - makes hero/editor{,.dark}.{jpg,webp} from hero/editor{,.dark}.png, copies
//     the dark pair to editor.{jpg,webp} (the README's old URL) and deletes the PNGs,
//   - makes file-information.{jpg,webp} from features/file-information.png (deleted),
//   - refreshes the features/ pictures that are copies of a docs picture (COPIES).
//
// It only touches raw captures: a PNG that is already palette-compressed is left
// alone, so running it twice changes nothing. Run it after a Shoot run, then
// `npm run build` (gen-thumbs rebuilds the thumbnails of what changed):
//
//   npm run optimize:shots
//   SKIP=debug-settings-cpp,debug-settings-csharp-ruby-php npm run optimize:shots   # leave those crops alone
import { readdirSync, statSync, renameSync, unlinkSync, copyFileSync, existsSync, openSync, readSync, closeSync } from "node:fs";
import { join, dirname } from "node:path";
import { fileURLToPath } from "node:url";
import sharp from "sharp";

const root = join(dirname(fileURLToPath(import.meta.url)), "..", "public/screenshots");

// docs/<out> <- docs/<source> cropped (2x pixels). The Debugging settings crops
// need a Settings capture scrolled to those sections (docs5/ and docs6/settings.txt).
const CROPS = {
  "debug-attach-to-process": ["debug-attach-to-process", { left: 0, top: 0, width: 2560, height: 730 }],
  "debug-member-marks": ["debug-member-marks", { left: 0, top: 172, width: 1760, height: 1163 }],
  "debug-settings-go-rust": ["debug-settings-go-rust", { left: 505, top: 1772, width: 1650, height: 888 }],
  "debug-history-list": ["debug-history-list", { left: 640, top: 170, width: 1280, height: 600 }],
  "debug-settings-cpp": ["debug-settings-cpp-csharp", { left: 533, top: 950, width: 1592, height: 782 }],
  "debug-settings-csharp-ruby-php": ["debug-settings-cpp-csharp", { left: 533, top: 1744, width: 1592, height: 1294 }],
  "debug-triggered-breakpoint": ["debug-triggered-breakpoint", { left: 0, top: 0, width: 2560, height: 1270 }],
  "debug-breakpoint-groups": ["debug-breakpoint-groups", { left: 0, top: 0, width: 2560, height: 1270 }],
  "debug-add-logpoint": ["debug-add-logpoint", { left: 0, top: 0, width: 2560, height: 1270 }],
  "spell-check": ["spell-check", { left: 0, top: 0, width: 1640, height: 460 }],
};

// features/<name> is the same picture as docs/<name> (the gallery dedupes by content).
const COPIES = {
  deployments: "deployments",
  "encrypt-recipients": "encrypted-files",
  "git-commit": "git",
  "http-response": "http-client",
  maven: "build-tools",
  "project-map": "project-map",
  "remote-connect": "remote-sftp",
  "run-configurations": "run-configurations",
  "run-file": "run-files",
  terminal: "terminal",
};

const SKIP = new Set((process.env.SKIP || "").split(",").filter(Boolean));
const SUFFIXES = ["", ".dark"];

/** A raw capture: a PNG that is not palette-compressed yet (IHDR colour type 3). */
function raw(file) {
  if (!existsSync(file)) return false;
  const fd = openSync(file, "r");
  const head = Buffer.alloc(26);
  readSync(fd, head, 0, 26, 0);
  closeSync(fd);
  return head[25] !== 3;
}

const palette = (img) => img.png({ palette: true, quality: 90, effort: 10, compressionLevel: 9 });
let inBytes = 0, outBytes = 0, count = 0;

async function compress(src, out, crop) {
  let img = sharp(src);
  if (crop) img = img.extract(crop);
  const tmp = out + ".tmp.png";
  const info = await palette(img).toFile(tmp);
  inBytes += statSync(src).size;
  outBytes += info.size;
  count++;
  return [tmp, info];
}

const docs = join(root, "docs");
const sources = new Set();
const pending = [];
for (const [out, [src, crop]] of Object.entries(CROPS)) {
  if (SKIP.has(out)) continue;
  for (const sfx of SUFFIXES) {
    const s = join(docs, src + sfx + ".png");
    if (!raw(s)) continue;
    const [tmp, info] = await compress(s, join(docs, out + sfx + ".png"), crop);
    pending.push([tmp, join(docs, out + sfx + ".png")]);
    console.log(`crop ${out + sfx} ${info.width}x${info.height}`);
    if (src !== out) sources.add(s);
  }
}
for (const [tmp, out] of pending) renameSync(tmp, out);
for (const s of sources) unlinkSync(s);

const cropped = new Set(Object.keys(CROPS));
for (const dir of ["docs", "features", "map"]) {
  for (const f of readdirSync(join(root, dir)).filter((f) => f.endsWith(".png")).sort()) {
    const base = f.replace(/(\.dark)?\.png$/, "");
    const p = join(root, dir, f);
    if (SKIP.has(base) || (dir === "docs" && cropped.has(base)) || !raw(p)) continue;
    if (dir === "features" && base === "file-information") continue; // becomes a jpg/webp below
    const [tmp] = await compress(p, p, null);
    renameSync(tmp, p);
  }
}

for (const sfx of SUFFIXES) {
  const png = join(root, "hero", "editor" + sfx + ".png");
  if (!existsSync(png)) continue;
  await sharp(png).flatten().jpeg({ quality: 85, progressive: true, mozjpeg: true }).toFile(join(root, "hero", `editor${sfx}.jpg`));
  await sharp(png).webp({ quality: 85 }).toFile(join(root, "hero", `editor${sfx}.webp`));
  if (sfx === ".dark") {
    copyFileSync(join(root, "hero/editor.dark.jpg"), join(root, "editor.jpg"));
    copyFileSync(join(root, "hero/editor.dark.webp"), join(root, "editor.webp"));
  }
  unlinkSync(png);
  console.log(`hero ${sfx || "light"}: jpg + webp`);
}

const info = join(root, "features", "file-information.png");
if (existsSync(info)) {
  await sharp(info).flatten().jpeg({ quality: 85, progressive: true, mozjpeg: true }).toFile(join(root, "file-information.jpg"));
  await sharp(info).webp({ quality: 85 }).toFile(join(root, "file-information.webp"));
  for (const sfx of SUFFIXES) {
    const p = join(root, "features", `file-information${sfx}.png`);
    if (existsSync(p)) unlinkSync(p);
  }
  console.log("file-information: jpg + webp");
}

for (const [from, to] of Object.entries(COPIES)) {
  for (const sfx of SUFFIXES) {
    const src = join(docs, from + sfx + ".png");
    const dst = join(root, "features", to + sfx + ".png");
    if (existsSync(src) && existsSync(dst) && statSync(src).mtimeMs > statSync(dst).mtimeMs) copyFileSync(src, dst);
  }
}

const mb = (b) => (b / 1048576).toFixed(1) + " MB";
console.log(`Compressed ${count} PNGs: ${mb(inBytes)} -> ${mb(outBytes)}.`);
