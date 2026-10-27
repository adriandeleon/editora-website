// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Java refactorings with commands", detail: "Generate, Refactor This, a command per refactoring, and Undo Last Refactoring that takes a rename or move back in every file it changed." },
  { title: "Completion that knows the type", detail: "Smart Type completion, chain completion, Go to Super Method, and Lombok, null analysis and your formatter profile reaching the Java server." },
  { title: "A lightbulb and a problem counter", detail: "A gutter bulb when the line has a fix or quick action, and the file's errors and warnings at the top right of the editor." },
  { title: "Doctor update checks", detail: "Check for Newer Versions asks each server Editora installs whether a newer release is out, and About says whether Editora is up to date." },
  { title: "Report a Problem", detail: "A redacted bug report from the Debug Log, shown and editable before it goes into a GitHub issue you submit; editora --report when it will not start." },
  { title: "Shortcuts in Settings sync", detail: "Key bindings you changed sync across your computers, in every keymap, merged chord by chord." },
];
