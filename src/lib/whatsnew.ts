// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "A reworked Project Map", detail: "The project's own files first, compact folder chains, Reveal Active File, Hide unmatched, and Personal Notes on preview cards." },
  { title: "A terminal theme of its own", detail: "Solarized, Dracula, Nord, Gruvbox and more for the terminal alone, plus Nerd Font icons bundled so prompts show on any machine." },
  { title: "Terminal programs talk back", detail: "The kitty keyboard protocol, desktop notifications, clipboard copy over SSH, and tabs that name the program running in them." },
  { title: "XML that is safe by default", detail: "Generate Grammar, no schema downloads from any host, a schema cache in Doctor, and Maven-aware pom editing only in trusted folders." },
  { title: "HTML links and markup checks", detail: "Go to Definition on href and src, warnings for unclosed elements and missing files, colour swatches, and a preview that serves the whole site." },
  { title: "Java library sources", detail: "A library class opens with its real source when a repository has it; a decompiled one offers Download Sources and Choose Sources." },
];
