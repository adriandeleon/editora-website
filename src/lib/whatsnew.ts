// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Tools on the host, in a container, or in WSL", detail: "Git, builds, tests, language servers and debuggers run on the host of a remote folder once you allow it, or inside the Docker or Podman container a project is mounted into, or its WSL distribution." },
  { title: "Choose a file's encoding", detail: "Reopen a file in the encoding it is really in, or save it in another: Shift_JIS, GBK, windows-1251 and every other encoding the JDK knows. The choice is remembered per file." },
  { title: "More from multiple cursors", detail: "Split a selection into lines, put a cursor at every line end, skip an occurrence or take back the last cursor; line commands and text tools run at every cursor." },
  { title: "HTTP Client scripts", detail: "Response handlers, tests and pre-request scripts in the JetBrains format run on your Node.js, with crypto and JWT for signing, and Debug Request steps through them." },
  { title: "Shell scripts, end to end", detail: "Scripts run as their shebang says, with no language server needed. Run with Trace links each traced line to its source, and ShellCheck and shfmt install in one click." },
  { title: "Markdown wiki links and backlinks", detail: "[[note]] links resolve, complete, follow renames and are checked by lint, and a Backlinks window lists what links to the open note." },
];
