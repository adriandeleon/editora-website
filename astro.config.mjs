// @ts-check
import { defineConfig } from "astro/config";
import sitemap from "@astrojs/sitemap";
import pagefind from "astro-pagefind";
import { version } from "./src/lib/version.ts";

// Served at the custom apex domain editora-project.dev (www. redirects to it).
// No `base` — the site lives at the domain root, so asset/link paths are "/".

// Docs are versioned under /docs/v-<version>/. Authored Markdown links use plain
// /docs/... paths; this rewrites them to the versioned base at build time, so
// content never hardcodes the version (bumping it just regenerates version.ts).
const docsBase = `/docs/v-${version}`;

function rehypeVersionDocs() {
  const rewrite = (node) => {
    if (node.type === "element" && node.tagName === "a" && node.properties) {
      const h = node.properties.href;
      if (typeof h === "string") {
        if (h === "/docs") node.properties.href = docsBase;
        else if (h.startsWith("/docs/") && !h.startsWith("/docs/v-"))
          node.properties.href = docsBase + h.slice("/docs".length);
      }
    }
    if (node.children) for (const c of node.children) rewrite(c);
  };
  return (tree) => rewrite(tree);
}

export default defineConfig({
  site: "https://editora-project.dev",
  // pagefind() runs the Pagefind indexer inside `astro build` (so it works no
  // matter how CI invokes the build) and serves the index in `astro dev`.
  integrations: [sitemap(), pagefind()],
  markdown: {
    rehypePlugins: [rehypeVersionDocs],
  },
  // Bare /docs lands on the current version; the versioned command index points
  // at the searchable command list. Unversioned doc/command URLs are not served
  // (each app version pins its own /docs/v-<version>/ links).
  redirects: {
    "/docs": docsBase,
    [`${docsBase}/commands`]: "/commands",
  },
});
