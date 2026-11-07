// The Editora release version, generated from the project's pom.xml into
// version.ts by scripts/GenRoadmap.java and committed, so the build never reads
// the Editora repo (it isn't checked out in CI). Re-exported here so callers
// keep importing { version } from "../lib/editora".
import { version } from "./version";

export { version };

// Docs are versioned: the canonical docs URLs live under /docs/v-<version>/.
// Templates build links from this; Markdown content links (authored as plain
// /docs/...) are rewritten to this base at build time (see astro.config.mjs).
export const docsBase = `/docs/v-${version}`;

export const repo = "https://github.com/adriandeleon/Editora";
export const latestRelease = `${repo}/releases/latest`;

// The language servers the app registers: one entry per ServerDef in
// LspServerRegistry in the Editora repo, in its order, with the languages each
// one serves. MAVEN_POM is left out: it is the Maven-aware variant of the XML
// server for pom.xml, not a language of its own. Matches "Twenty-six servers"
// in docs/lsp.md. Update this list when a server is added or removed; the
// count and the home page's language chips both come from it, so they cannot
// disagree.
export const lspServers: { id: string; languages: string[] }[] = [
  { id: "java", languages: ["Java"] },
  { id: "typescript", languages: ["JavaScript", "TypeScript"] },
  { id: "python", languages: ["Python"] },
  { id: "xml", languages: ["XML"] },
  { id: "json", languages: ["JSON"] },
  { id: "bash", languages: ["Shell"] },
  { id: "yaml", languages: ["YAML"] },
  { id: "go", languages: ["Go"] },
  { id: "rust", languages: ["Rust"] },
  { id: "php", languages: ["PHP"] },
  { id: "ruby", languages: ["Ruby"] },
  { id: "clangd", languages: ["C", "C++"] },
  { id: "html", languages: ["HTML", "Handlebars"] },
  { id: "css", languages: ["CSS"] },
  { id: "kotlin", languages: ["Kotlin"] },
  { id: "groovy", languages: ["Groovy"] },
  { id: "scala", languages: ["Scala"] },
  { id: "clojure", languages: ["Clojure"] },
  { id: "lua", languages: ["Lua"] },
  { id: "dockerfile", languages: ["Dockerfile"] },
  { id: "sql", languages: ["SQL"] },
  { id: "terraform", languages: ["Terraform"] },
  { id: "toml", languages: ["TOML"] },
  { id: "csharp", languages: ["C#"] },
  { id: "typst", languages: ["Typst"] },
  { id: "astro", languages: ["Astro"] },
];

// How many language servers the app registers. One number for every page that
// quotes it, so they cannot disagree.
export const lspServerCount = lspServers.length;
