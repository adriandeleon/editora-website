// The Editora release version, generated from the project's pom.xml into
// version.ts by scripts/GenRoadmap.java and committed, so the build never reads
// the Editora repo (it isn't checked out in CI). Re-exported here so callers
// keep importing { version } from "../lib/editora".
import { version } from "./version";
import { appDocs } from "./app-docs";

export { version };

// Where templates link the user guide. On the website that is the stable
// /docs/<slug>/ (each page's canonical URL; src/pages/docs/[slug].astro); the
// copy inside the app serves only /docs/v-<version>/. Markdown links (authored
// as plain /docs/...) get the same treatment at build time (astro.config.mjs).
// End every link built from it with a slash: GitHub Pages answers a slashless
// directory URL with a 301.
export const docsBase = appDocs ? `/docs/v-${version}` : "/docs";

// Per-command pages exist only under the version: /docs/v-<version>/commands/<id>/.
export const commandsBase = `/docs/v-${version}/commands`;

export const repo = "https://github.com/adriandeleon/Editora";
export const latestRelease = `${repo}/releases/latest`;

// The language servers the app registers: one entry per ServerDef in
// LspServerRegistry in the Editora repo, in its order, with the languages each
// one serves. MAVEN_POM is left out: it is the Maven-aware variant of the XML
// server for pom.xml, not a language of its own. Checked against the
// ServerDef enum in v0.29.0 (29 servers). Update this list when a server is
// added or removed; the count and the home page's language chips both come
// from it, so they cannot disagree.
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
  { id: "dart", languages: ["Dart"] },
  { id: "elixir", languages: ["Elixir"] },
  { id: "powershell", languages: ["PowerShell"] },
];

// How many language servers the app registers. One number for every page that
// quotes it, so they cannot disagree.
export const lspServerCount = lspServers.length;
