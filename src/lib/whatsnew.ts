// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Signed downloads", detail: "The macOS app is signed and notarized by Apple, the Linux downloads carry OpenPGP signatures, and every release file has a build provenance attestation." },
  { title: "Explore code on the Project Map", detail: "Working sets, guided trails you can commit, recent-change heat with owners, and references and callers opened as cards." },
  { title: "A Structure column on every card", detail: "Click a method or type in a preview card's outline to open it beside the card; its Open opens the file narrowed to it." },
  { title: "ESLint, Prettier and Biome", detail: "The project's own ESLint checks JavaScript and TypeScript as you work, Prettier or Biome formats, and fixes can run on save." },
  { title: "Python quick fixes and format on save", detail: "Import X and Remove unused import from the lightbulb, Ruff or Black on save, and each sub-project analysed by its own Pyright." },
  { title: "Java debugging shows toString()", detail: "Objects show their toString() text within a per-stop budget, and plain-folder projects with several packages debug again." },
];
