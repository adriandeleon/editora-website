// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Remote development, on the host", detail: "Git, ripgrep, builds, tests, language servers and debuggers run on the host of a remote folder, once you allow that host. Each remote folder is a project with its own window." },
  { title: "Choose a file's encoding", detail: "Reopen a file in the encoding it is really in, or save it in another: Shift_JIS, GBK, windows-1251 and every other encoding the JDK knows. The choice is remembered per file." },
  { title: "More from multiple cursors", detail: "Split a selection into lines, put a cursor at every line end, skip an occurrence or take back the last cursor; line commands and text tools run at every cursor." },
  { title: "Rulers and indent guides", detail: "Rulers at any columns, with their own set per language, and faint guides at every indentation level." },
  { title: "Shell scripts, end to end", detail: "Scripts run as their shebang says, with no language server needed. Run with Trace links each traced line to its source, and ShellCheck and shfmt install in one click." },
  { title: "Markdown wiki links and backlinks", detail: "[[note]] links resolve, complete, follow renames and are checked by lint, and a Backlinks window lists what links to the open note." },
];
