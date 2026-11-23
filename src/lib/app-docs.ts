// True when this build makes the copy of the user guide that ships inside the
// Editora app (scripts/build-app-docs.mjs sets EDITORA_APP_DOCS=1), not the
// website. That build serves only the current version's docs and command pages
// from 127.0.0.1, so it leaves out the archived versions and the analytics
// beacon; every page it does not have is sent on to editora-project.dev.
// Read from process.env because both astro.config.mjs and the pages run in Node
// at build time.
export const appDocs: boolean =
  (globalThis as { process?: { env?: Record<string, string | undefined> } }).process?.env
    ?.EDITORA_APP_DOCS === "1";
