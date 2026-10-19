// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Kotlin, Scala, Clojure and Groovy", detail: "Language servers installed in one click, Run File and the gutter ▶, sbt, Mill, Leiningen and Babashka tasks, and Debug through the JVM debugger built into Editora." },
  { title: "A terminal inside Editora", detail: "Shells in tabs, in the project's folder or on its host, container or WSL distribution, with shell integration, clickable paths and Find." },
  { title: "Node.js and TypeScript, run and debugged", detail: "Run File runs JS and TS scripts, node:test fills Test Results, TypeScript debugs through source maps, and one Node.js setting serves everything." },
  { title: "One Python for everything", detail: "Run, Debug, Pyright and the formatter use the project's own venv, picked from the status bar, and a pytest runner fills Test Results." },
  { title: "Deployments", detail: "Develop locally, run on another machine: a run configuration copies what changed to an SSH host and builds and runs it there." },
  { title: "Encrypted files", detail: "GnuPG is built in: a .gpg file opens as its text, saves encrypted again, and stays out of crash recovery, Local History and AI tools." },
];
