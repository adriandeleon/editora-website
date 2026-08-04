// @ts-check
import { defineConfig } from "astro/config";
import sitemap from "@astrojs/sitemap";
import pagefind from "astro-pagefind";
import { version } from "./src/lib/version.ts";
import { docVersions } from "./src/lib/doc-versions.ts";

// Served at the custom apex domain editora-project.dev (www. redirects to it).
// No `base` — the site lives at the domain root, so asset/link paths are "/".

// Docs are versioned under /docs/v-<version>/. Authored Markdown links use plain
// /docs/... paths; this rewrites them to the versioned base at build time, so
// content never hardcodes the version (bumping it just regenerates version.ts).
const docsBase = `/docs/v-${version}`;

// An archived page under src/content/docs-archive/<version>/ resolves its links
// against *its own* version. Otherwise a 0.9.10 page would link out into the
// current docs and quietly stop being a record of that release. The version
// comes from the file being processed, so nothing needs threading through.
const ARCHIVE_DIR = /[\\/]src[\\/]content[\\/]docs-archive[\\/]([^\\/]+)[\\/]/;

function baseForFile(file) {
  const path = file?.path ?? file?.history?.[0] ?? "";
  const m = ARCHIVE_DIR.exec(String(path));
  return m ? `/docs/v-${m[1]}` : docsBase;
}

function rehypeVersionDocs() {
  return (tree, file) => {
    const base = baseForFile(file);
    const rewrite = (node) => {
      if (node.type === "element" && node.tagName === "a" && node.properties) {
        const h = node.properties.href;
        if (typeof h === "string") {
          if (h === "/docs") node.properties.href = base;
          else if (h.startsWith("/docs/") && !h.startsWith("/docs/v-"))
            node.properties.href = base + h.slice("/docs".length);
        }
      }
      if (node.children) for (const c of node.children) rewrite(c);
    };
    rewrite(tree);
  };
}

export default defineConfig({
  site: "https://editora-project.dev",
  // pagefind() runs the Pagefind indexer inside `astro build` (so it works no
  // matter how CI invokes the build) and serves the index in `astro dev`.
  integrations: [sitemap(), pagefind()],
  markdown: {
    rehypePlugins: [rehypeVersionDocs],
  },
  // Bare /docs lands on the current version; every served version's command
  // index points at the searchable command list. Unversioned doc/command URLs
  // are not served — each app version pins its own /docs/v-<version>/ links,
  // and past versions stay served from their archived copy.
  redirects: {
    "/docs": docsBase,
    ...Object.fromEntries(
      docVersions.map((v) => [`${v.base}/commands`, "/commands"]),
    ),
  },
});
