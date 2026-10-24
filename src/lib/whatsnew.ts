// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Maven, reworked", detail: "Parent chains and profiles in the tree, builds from the reactor root, Run and Debug that build first, Debug Test that attaches, and Update Versions that compares versions as Maven does." },
  { title: "Narrow from the Structure window", detail: "One toggle narrows the editor to a symbol's block, and a narrowed editor shows an amber banner with Widen and keeps the file's line numbers." },
  { title: "The Project Map in its own window", detail: "The Map opens in a separate, resizable window beside the editor or on another screen, with its own search, while the tool window keeps the tree." },
  { title: "A pom.xml server that answers", detail: "The Maven-aware lemminx-maven install now fetches the libraries it was missing and is tested before it is kept." },
  { title: "Settings icons and project names", detail: "Every Settings page has an icon, and the project scope names the project it edits." },
  { title: "Null checking in the build", detail: "Every package is checked for nulls when Editora is built, which found and fixed about ten crashes." },
];
