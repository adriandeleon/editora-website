// Hand-written highlights of the current release for the home page's
// "What's new" block. Update it with each release, from that release's news
// post; scripts/GenRoadmap.java no longer writes this file.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Incremental highlighting", detail: "An edit re-tokenizes only the lines it affects. In a 79,000-line Java file, an edit near the top settles in about half a second instead of five to seven." },
  { title: "Large files open without freezing", detail: "A 49 MB file that blocked the window for about three seconds now stays under one, and Find in a very large file runs in the background." },
  { title: "Fast output keeps the window responsive", detail: "200,000 build lines that froze the window for about a minute now stream in about 13 seconds." },
  { title: "Opening a file does less on its own", detail: "A folder's .git/config can no longer run programs when you open a file in it, and links go through one check before anything is handed to the system." },
  { title: "Program input in the Debug console", detail: "A debugged Java program that reads System.in gets its input from the Debug console instead of waiting forever." },
];
