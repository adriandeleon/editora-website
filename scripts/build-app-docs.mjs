// Builds the copy of the user guide that ships inside the Editora app.
//
// Editora owns the guide (its user-docs/ folder); this site is the one thing
// that turns it into HTML, so the app shows exactly the pages the website does.
// The app serves this folder from 127.0.0.1 and opens it in the browser from
// Help > Documentation; anything the folder lacks (News, Features, archived
// versions) the app sends on to editora-project.dev.
//
//   node scripts/build-app-docs.mjs --editora /path/to/Editora --out /path/to/folder
//
// It works on a copy of this repo in a temporary folder, so the checkout's own
// generated files are left alone:
//   1. the generators read the given Editora tree: SyncDocs (the guide),
//      GenCommands (the command pages), GenPauta, GenRoadmap;
//   2. src/lib/version.ts is set to that tree's own version, -SNAPSHOT dropped,
//      the same number the app puts in its docs links (AppInfo.releaseVersion);
//   3. `astro build` runs with EDITORA_APP_DOCS=1 (src/lib/app-docs.ts): only that
//      version, no analytics;
//   4. the docs, the /commands and /keybindings pages, the styles, the icons and
//      the pictures those pages use are copied to --out, Pagefind indexes them
//      there, and app-docs.json records the version.
// Needs JDK 25 (`java` on the PATH) and this repo's node_modules (`npm ci`).
import { execFileSync } from "node:child_process";
import {
  cpSync,
  existsSync,
  mkdirSync,
  mkdtempSync,
  readFileSync,
  readdirSync,
  rmSync,
  statSync,
  symlinkSync,
  writeFileSync,
} from "node:fs";
import { tmpdir } from "node:os";
import { dirname, join, relative, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const site = resolve(dirname(fileURLToPath(import.meta.url)), "..");

function arg(name) {
  const at = process.argv.indexOf(name);
  return at >= 0 ? process.argv[at + 1] : undefined;
}

const editora = arg("--editora") ?? process.env.EDITORA_REPO;
const outArg = arg("--out");
if (!editora || !outArg) {
  console.error("usage: node scripts/build-app-docs.mjs --editora <Editora checkout> --out <folder>");
  process.exit(2);
}
const src = resolve(editora);
const out = resolve(outArg);
if (!existsSync(join(src, "user-docs"))) {
  console.error(`build-app-docs: ${src} has no user-docs/ folder`);
  process.exit(2);
}
if (!existsSync(join(site, "node_modules"))) {
  console.error("build-app-docs: run `npm ci` in the website repo first");
  process.exit(2);
}

// The project's own <version>, the first in the POM, as the app reads it.
const pomVersion = /<version>([^<]+)<\/version>/.exec(readFileSync(join(src, "pom.xml"), "utf8"))?.[1]?.trim();
if (!pomVersion) {
  console.error(`build-app-docs: no <version> in ${join(src, "pom.xml")}`);
  process.exit(2);
}
const version = pomVersion.replace(/-SNAPSHOT$/i, "");

const SKIP = new Set(["node_modules", "dist", ".git", ".astro", ".claude"]);
const work = mkdtempSync(join(tmpdir(), "editora-app-docs-"));
const run = (cmd, args, env = {}) =>
  execFileSync(cmd, args, { cwd: work, stdio: "inherit", env: { ...process.env, ...env } });

try {
  for (const name of readdirSync(site)) {
    if (!SKIP.has(name)) cpSync(join(site, name), join(work, name), { recursive: true });
  }
  symlinkSync(join(site, "node_modules"), join(work, "node_modules"), "junction");

  for (const gen of ["SyncDocs.java", "GenCommands.java", "GenPauta.java", "GenRoadmap.java"]) {
    run("java", [join("scripts", gen), src]);
  }
  writeFileSync(
    join(work, "src/lib/version.ts"),
    `// Written by scripts/build-app-docs.mjs for the copy inside the app.\nexport const version = ${JSON.stringify(version)};\n`,
  );

  run("node", ["scripts/gen-thumbs.mjs"]);
  // The symlinked node_modules is shared with the checkout; keep Astro's cache
  // out of it (see `cacheDir` in astro.config.mjs).
  run(join(site, "node_modules/.bin/astro"), ["build"], { EDITORA_APP_DOCS: "1" });

  const dist = join(work, "dist");
  rmSync(out, { recursive: true, force: true });
  mkdirSync(out, { recursive: true });
  const copy = (rel) => {
    const from = join(dist, rel);
    if (existsSync(from)) cpSync(from, join(out, rel), { recursive: true });
  };
  for (const rel of ["docs", "commands", "keybindings", "_astro", "favicon.png", "editora-icon.svg", "apple-touch-icon.png", "icon-512.png"]) {
    copy(rel);
  }

  // Every site file the copied pages point at by an absolute path, apart from
  // pages: the screenshots and their thumbnails.
  const wanted = new Set();
  const walk = (dir) => {
    for (const name of readdirSync(dir)) {
      const p = join(dir, name);
      if (statSync(p).isDirectory()) walk(p);
      else if (name.endsWith(".html")) {
        const html = readFileSync(p, "utf8");
        for (const m of html.matchAll(/(?:src|href|srcset)="([^"]+)"/g)) {
          for (const part of m[1].split(",")) {
            const url = part.trim().split(/\s+/)[0];
            if (url.startsWith("/screenshots/")) wanted.add(decodeURI(url.split(/[?#]/)[0]));
          }
        }
      }
    }
  };
  walk(out);
  let pictures = 0;
  for (const url of wanted) {
    if (existsSync(join(dist, url))) {
      copy(url.slice(1));
      pictures++;
    }
  }

  // Search over just these pages; the website's index covers news and features too.
  // Pages only: a command id such as edit.escape.html names a folder, which
  // Pagefind's default *.html glob would try to read as a page.
  execFileSync(join(site, "node_modules/.bin/pagefind"), ["--site", out, "--glob", "**/index.html"], {
    stdio: "inherit",
  });

  writeFileSync(
    join(out, "app-docs.json"),
    JSON.stringify({ version, docs: `/docs/v-${version}/`, built: new Date().toISOString() }, null, 2) + "\n",
  );

  let files = 0;
  let bytes = 0;
  const count = (dir) => {
    for (const name of readdirSync(dir)) {
      const p = join(dir, name);
      const s = statSync(p);
      if (s.isDirectory()) count(p);
      else {
        files++;
        bytes += s.size;
      }
    }
  };
  count(out);
  console.log(
    `build-app-docs: Editora ${version} docs in ${relative(process.cwd(), out) || out}: ` +
      `${files} files, ${(bytes / 1048576).toFixed(1)} MB (${pictures} pictures)`,
  );
} finally {
  rmSync(work, { recursive: true, force: true });
}
