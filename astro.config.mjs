// @ts-check
import { defineConfig } from "astro/config";
import sitemap from "@astrojs/sitemap";
import pagefind from "astro-pagefind";
import { version } from "./src/lib/version.ts";
import { docVersions } from "./src/lib/doc-versions.ts";
import rehypeDocFigures from "./src/lib/rehype-doc-figures.mjs";

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
  // `astro dev` watches the whole project folder. The built site (dist/, about
  // 19,000 folders) and Claude Code worktrees under .claude/ are not sources,
  // and watching them can exhaust the system's file-watch limits.
  vite: {
    server: {
      watch: { ignored: ["**/dist/**", "**/.claude/**"] },
    },
  },
  markdown: {
    rehypePlugins: [rehypeVersionDocs, rehypeDocFigures],
  },
  // Bare /docs lands on the current version; every served version's command
  // index points at the searchable command list. Unversioned doc/command URLs
  // are not served — each app version pins its own /docs/v-<version>/ links,
  // and past versions stay served from their archived copy.
  redirects: {
    // The blog was folded into News (2026-11-03). Old post URLs land on the
    // release's news post or the closest feature page, or on /news.
    "/blog": "/news",
    "/blog/building-a-keyboard-first-editor": "/news",
    "/blog/editora-0-10-0": "/news/2026-08-03-editora-0-10-0-released",
    "/blog/editora-0-12-0": "/news/2026-08-16-editora-0-12-0-released",
    "/blog/editora-0-13-0": "/news/2026-08-26-editora-0-13-0-released",
    "/blog/editora-0-14-0": "/news/2026-08-29-editora-0-14-0-released",
    "/blog/editora-0-15-0": "/news/2026-08-30-editora-0-15-0-released",
    "/blog/editora-0-9-1": "/news/2026-07-06-editora-0-9-1-released",
    "/blog/editora-0-9-10": "/news/2026-07-26-editora-0-9-10-released",
    "/blog/editora-0-9-2": "/news/2026-07-08-editora-0-9-2-released",
    "/blog/editora-0-9-3": "/news/2026-07-10-editora-0-9-3-released",
    "/blog/editora-0-9-4": "/news/2026-07-13-editora-0-9-4-released",
    "/blog/editora-0-9-5": "/news/2026-07-15-editora-0-9-5-released",
    "/blog/editora-0-9-6": "/news/2026-07-17-editora-0-9-6-released",
    "/blog/editora-0-9-7": "/news/2026-07-18-editora-0-9-7-released",
    "/blog/editora-0-9-8": "/news/2026-07-20-editora-0-9-8-released",
    "/blog/editora-0-9-9": "/news/2026-07-21-editora-0-9-9-released",
    "/blog/extending-editora-with-plugins": "/features/plugins",
    "/blog/faster-cold-start-with-an-aot-cache": "/news/2026-06-13-faster-cold-start",
    "/blog/going-headless-to-stay-alive": "/news",
    "/blog/html-live-preview": "/features/html-live-preview",
    "/blog/keeping-the-ui-thread-sacred": "/news",
    "/blog/one-dap-client-three-debuggers": "/news",
    "/blog/orphaned-language-servers-and-symlinks": "/news",
    "/blog/personal-notes-that-survive-edits": "/features/bookmarks-notes",
    "/blog/rebuilding-the-editora-website": "/news/2026-07-24-the-site-got-a-redesign",
    "/blog/rendering-markdown-like-github": "/news",
    "/blog/shipping-javafx-to-five-platforms": "/news",
    "/blog/signing-the-plugin-registry": "/news",
    "/blog/the-black-window-bounding-gpu-textures": "/news",
    "/blog/why-your-app-cant-find-git": "/news",
    "/docs": docsBase,
    ...Object.fromEntries(
      docVersions.map((v) => [`${v.base}/commands`, "/commands"]),
    ),
  },
});
