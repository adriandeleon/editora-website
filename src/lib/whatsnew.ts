// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "A faster editor", detail: "Typing, switching tabs, opening files and restoring a session all do less work, and a large file takes less memory. A restored session draws its first frame before building the tabs you are not looking at." },
  { title: "Local History, reworked", detail: "A compact panel with keyboard actions and a diff that follows your edits. Restoring goes into the open tab as an edit you can undo, and one large file cannot push other files' history out." },
  { title: "Clickable code lenses", detail: "A click on a lens opens what it counts. A references lens opens the References tool window, whose previews are colored like the editor." },
  { title: "Git history and branches", detail: "Enter on a commit in a file's Git history opens what it changed in that file, and the branch dropdown's sections fold." },
  { title: "MCP open_file stays in the project", detail: "An MCP client can open only files inside the window's project, and cannot edit the editor's configuration or version-control metadata." },
];
