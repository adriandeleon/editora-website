// The docs are versioned, and each version serves the docs it actually shipped
// with — not the current text under an old URL. The app deep-links
// /docs/v-<appVersion>/… and /docs/v-<appVersion>/commands/<id>, so those URLs
// have to keep resolving after a release, and they have to describe the version
// the reader is running.
//
// How a release is archived (see README "Versioned docs"):
//
//   1. Before regenerating for the new release, snapshot the outgoing version:
//        V=0.9.10   # the version being archived
//        mkdir -p src/content/docs-archive/$V
//        cp src/content/docs/*.md src/content/docs-archive/$V/
//        cp src/lib/commands.ts src/lib/commands-archive/v${V//./-}.ts
//   2. Add an ARCHIVED entry below, importing that commands snapshot.
//
// The snapshot is a copy on purpose. Docs get corrected over time, and a
// correction should not silently rewrite what a shipped version claimed; an
// archived page is a record of that release.
import { version as currentVersion } from "./version";
import {
  commandGroups as currentCommandGroups,
  keymaps as currentKeymaps,
  type CmdGroup,
} from "./commands";
import {
  commandGroups as commandGroups_0_10_0,
  keymaps as keymaps_0_10_0,
} from "./commands-archive/v0-10-0";
import {
  commandGroups as commandGroups_0_9_10,
  keymaps as keymaps_0_9_10,
} from "./commands-archive/v0-9-10";

export type Keymap = { id: string; name: string };

export type DocVersion = {
  /** "0.10.0" */
  version: string;
  /** "v-0.10.0" — the :version route param */
  slug: string;
  /** "/docs/v-0.10.0" */
  base: string;
  /** The version the site currently documents; /docs redirects here. */
  current: boolean;
  /**
   * Directory under src/content/docs-archive holding this version's Markdown,
   * which is also the id prefix in the `docsArchive` collection. Null for the
   * current version, whose pages live in the live `docs` collection.
   */
  archive: string | null;
  commandGroups: CmdGroup[];
  keymaps: Keymap[];
};

type Spec = Pick<DocVersion, "version" | "archive" | "commandGroups" | "keymaps">;

// Newest first. Add an entry here when a release is archived.
const ARCHIVED: Spec[] = [
  {
    version: "0.10.0",
    archive: "0.10.0",
    commandGroups: commandGroups_0_10_0,
    keymaps: keymaps_0_10_0,
  },
  {
    version: "0.9.10",
    archive: "0.9.10",
    commandGroups: commandGroups_0_9_10,
    keymaps: keymaps_0_9_10,
  },
];

const decorate = (s: Spec): DocVersion => ({
  ...s,
  slug: `v-${s.version}`,
  base: `/docs/v-${s.version}`,
  current: s.archive === null,
});

export const currentDocVersion: DocVersion = decorate({
  version: currentVersion,
  archive: null,
  commandGroups: currentCommandGroups,
  keymaps: currentKeymaps,
});

export const archivedDocVersions: DocVersion[] = ARCHIVED.map(decorate);

/** Current first, then archived newest-first. */
export const docVersions: DocVersion[] = [currentDocVersion, ...archivedDocVersions];

export const docVersionBySlug: Map<string, DocVersion> = new Map(
  docVersions.map((v) => [v.slug, v]),
);
