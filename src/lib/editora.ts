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
