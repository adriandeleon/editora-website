// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Web apps run and debug as they are served", detail: "Django, Flask, FastAPI, Rails and PHP apps start from Run or F5, and breakpoints stop on each request. Electron apps debug their main and renderer processes together." },
  { title: "Tests at the caret in almost every language", detail: "Run or debug the one test under the caret in Go, Rust, Dart, Kotlin, Groovy, Jest, Vitest, Deno, RSpec, Minitest, PHPUnit, ExUnit, C# and C/C++." },
  { title: "PowerShell, Elixir, Dart and Lua", detail: "PowerShell, Elixir and Dart get language servers and debuggers; Lua, Deno and Firefox get debugging." },
  { title: "A log viewer for real logs", detail: "Live logs from Docker, Kubernetes, journald and SSH, filters by time, a summary of a log, and several logs merged by time." },
  { title: "System and High Contrast themes", detail: "A theme that follows your light or dark setting, and High Contrast Light and Dark at 7:1 or better." },
  { title: "The guide comes with the app", detail: "Help ▸ Documentation opens the user guide for the version you run, without a network connection." },
];
