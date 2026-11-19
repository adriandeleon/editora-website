// Checks the internal links of the built site (dist/): every <a href>, <img
// src>, srcset entry and <link href> that points into the site must reach a
// file, and a #fragment must name an id on the page it points at.
//
// Covers every page except the archived docs versions and the per-command
// pages (about 22,000 copies of the same templates, checked through the
// current version's pages). Script blocks are skipped, since their template
// literals hold hrefs that are not links. External URLs are not fetched.
//
//   npm run build && npm run check:links
import { readFileSync, readdirSync, statSync, existsSync } from "node:fs";
import { join, relative, posix } from "node:path";

const root = new URL("..", import.meta.url).pathname;
const dist = join(root, "dist");
const version = /version = "([^"]+)"/.exec(readFileSync(join(root, "src/lib/version.ts"), "utf8"))[1];
const current = `docs/v-${version}`;

if (!existsSync(dist)) {
  console.error("check-links: dist/ not found; run `npm run build` first");
  process.exit(2);
}

function skip(rel) {
  if (rel.startsWith("pagefind") || rel.startsWith("_astro")) return true;
  if (rel.startsWith("docs/v-") && !rel.startsWith(current)) return true;
  return rel.startsWith(`${current}/commands/`);
}

const pages = [];
(function walk(dir) {
  for (const name of readdirSync(dir)) {
    const path = join(dir, name);
    const rel = relative(dist, path);
    if (statSync(path).isDirectory()) {
      if (!skip(rel)) walk(path);
    } else if (name.endsWith(".html")) {
      pages.push(path);
    }
  }
})(dist);

const ID = /\sid="([^"]+)"/g;
const idCache = new Map();
function idsOf(file) {
  if (!idCache.has(file)) {
    idCache.set(file, new Set([...readFileSync(file, "utf8").matchAll(ID)].map((m) => m[1])));
  }
  return idCache.get(file);
}

function target(path) {
  const p = decodeURIComponent(path).replace(/^\/+/, "");
  for (const c of [p, join(p, "index.html"), `${p}.html`]) {
    const f = join(dist, c);
    if (existsSync(f) && statSync(f).isFile()) return f;
  }
  return null;
}

const ATTR = /<(a|img|link|source)\b[^>]*?\s(href|src|srcset)="([^"]*)"/gi;
const broken = [];
let checked = 0;
for (const file of pages) {
  const html = readFileSync(file, "utf8");
  const page = "/" + relative(dist, file).replace(/index\.html$/, "");
  const body = html.replace(/<script\b[\s\S]*?<\/script>/gi, "");
  for (const [, , attr, value] of body.matchAll(ATTR)) {
    const urls = attr === "srcset" ? value.split(",").map((s) => s.trim().split(/\s+/)[0]) : [value];
    for (const raw of urls) {
      if (!raw || /^(https?:|mailto:|data:|javascript:|tel:)/i.test(raw)) continue;
      checked++;
      const [pathPart, fragment] = raw.split("#", 2);
      if (!pathPart) {
        if (fragment && !idsOf(file).has(decodeURIComponent(fragment))) broken.push([page, raw, "no such anchor"]);
        continue;
      }
      const abs = pathPart.startsWith("/") ? pathPart : posix.join(page, pathPart);
      const hit = target(abs.split("?")[0]);
      if (!hit) {
        broken.push([page, raw, "missing"]);
      } else if (fragment && hit.endsWith(".html") && !idsOf(hit).has(decodeURIComponent(fragment))) {
        broken.push([page, raw, "no such anchor"]);
      }
    }
  }
}

console.log(`check-links: ${pages.length} pages, ${checked} links, ${broken.length} broken`);
for (const [page, url, why] of broken) console.log(`  ${page}  ${url}  (${why})`);
process.exit(broken.length ? 1 : 0);
