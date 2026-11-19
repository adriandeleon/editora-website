// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Debug eight more languages", detail: "Go, Rust, C and C++ (CodeLLDB or GDB), C#, Ruby, PHP and Bash, JavaScript in the browser, and VS Code's launch.json configurations." },
  { title: "Go back in the debugger", detail: "Debug History keeps each stop, recording breakpoints collect stops without stopping, and GDB steps back over what it recorded." },
  { title: "Several programs at once", detail: "Debug a second file while one runs, or a compound of several, each with its own tab, console, call stack and variables." },
  { title: "Read and follow code on the Project Map", detail: "Go to Definition, Find Usages and the hierarchies answer with cards, Symbol Graph shows callers and callees, and Back and Forward step through them." },
  { title: "Review and measure on the Map", detail: "Review a branch or your changes, and find the hotspots: code that is both complex and often changed." },
  { title: "Every command on the keyboard", detail: "Every right-click row, button and setting is a command, a card lists the keys after a prefix key, and the whole interface zooms from 80% to 200%." },
];
