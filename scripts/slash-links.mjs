// Adds the trailing slash to internal links that point at a page folder.
//
// Astro emits every page as <path>/index.html, and GitHub Pages answers a
// slashless folder URL (/features) with a 301 to /features/. A link written
// without the slash, in a template or in Markdown, so costs every visitor a
// redirect. After the build this rewrites href="/x" (and "/x#y", "/x?q") to
// "/x/" wherever dist/x/index.html exists; files (/og/a.png) and URLs that
// already end in a slash are left alone. Run by `npm run build`.
//
//   node scripts/slash-links.mjs [dist]
import { existsSync, readdirSync, readFileSync, statSync, writeFileSync } from "node:fs";
import { join } from "node:path";

const dist = process.argv[2] ?? "dist";
const isFolder = new Map();
function folder(path) {
  let hit = isFolder.get(path);
  if (hit === undefined) {
    hit = existsSync(join(dist, path, "index.html"));
    isFolder.set(path, hit);
  }
  return hit;
}

const HREF = /href="(\/(?!\/)[^"#?]*?)([#?][^"]*)?"/g;
let files = 0;
let links = 0;
(function walk(dir) {
  for (const name of readdirSync(dir)) {
    const p = join(dir, name);
    if (statSync(p).isDirectory()) {
      if (name !== "_astro" && name !== "pagefind") walk(p);
    } else if (name.endsWith(".html")) {
      const html = readFileSync(p, "utf8");
      let changed = 0;
      const out = html.replace(HREF, (m, path, rest = "") => {
        if (path.endsWith("/") || /\.[a-z0-9]+$/i.test(path) || !folder(path)) return m;
        changed++;
        return `href="${path}/${rest}"`;
      });
      if (changed) {
        writeFileSync(p, out);
        files++;
        links += changed;
      }
    }
  }
})(dist);
console.log(`slash-links: ${links} links in ${files} pages now end in a slash`);
