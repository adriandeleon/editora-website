// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Merges, rebases and branches in the editor", detail: "An operation in progress shows in the status bar and the Commit window with Continue, Skip and Abort. Branches, remotes, worktrees, tags, stashes and patches have their own commands." },
  { title: "Crash recovery", detail: "Unsaved edits, including untitled buffers, are kept while you work and offered back on the next launch if Editora did not close normally." },
  { title: "Settings sync (Beta)", detail: "Snippets, abbreviations, templates and your personal dictionary stay the same on every computer, through a private Git repository you own." },
  { title: "Print and PDF, reworked", detail: "Print and Export to PDF are in the File menu. PDFs have clickable links, bookmarks and a page footer, and the Print Preview has zoom and Page Setup." },
  { title: "Java refactorings that ask where to", detail: "Move, Extract Interface and Change Signature run from the Code Actions menu, and tests in @Nested classes run from the gutter." },
];
