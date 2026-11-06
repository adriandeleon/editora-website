// Every screenshot the site shows off: the home page's slideshow, the Project
// Map spotlight and the /screenshots gallery read from here.
//
// Captures follow the docs figures' convention (src/lib/rehype-doc-figures.mjs):
// 2x, in the app's light theme, with a `.dark` twin the dark site themes show
// instead. scripts/gen-thumbs.mjs builds the small WebP thumbnails the gallery
// grid uses (`<dir>/thumbs/<name>.webp`).
import { createHash } from "node:crypto";
import { existsSync, readFileSync } from "node:fs";
import { resolve } from "node:path";
import { docsBase } from "./editora";
import { groupedFeatures, groupId } from "./features";

export type Shot = {
  /** Full-size capture, `/`-rooted. The light one when there is a twin. */
  src: string;
  /** Its dark-theme twin, or null. */
  dark: string | null;
  thumb: string;
  thumbDark: string | null;
  /** A few words, for the slideshow's tabs and the gallery grid. */
  title: string;
  /** One sentence under the picture. */
  caption: string;
  alt: string;
  /** A page that says more about what the picture shows. */
  more?: { href: string; label: string };
};

export type ShotSection = { id: string; title: string; lead?: string; shots: Shot[] };

const pub = (p: string) => resolve("public", p.slice(1));
const has = (p: string) => existsSync(pub(p));
const twin = (p: string, make: (base: string, ext: string) => string) => {
  const m = p.match(/^(.*?)(\.[a-z]+)$/)!;
  return make(m[1], m[2]);
};
const darkOf = (src: string) => {
  const d = twin(src, (b, e) => `${b}.dark${e}`);
  return has(d) ? d : null;
};
const thumbOf = (src: string) => {
  const t = twin(src, (b) => b.replace(/\/([^/]+)$/, "/thumbs/$1") + ".webp");
  return has(t) ? t : src;
};

function shot(src: string, s: Omit<Shot, "src" | "dark" | "thumb" | "thumbDark">): Shot {
  if (!has(src)) throw new Error(`screenshot missing: public${src}`);
  const dark = darkOf(src);
  return { src, dark, thumb: thumbOf(src), thumbDark: dark && thumbOf(dark), ...s };
}

/** The home page's slideshow, in the order it plays. */
export const tourShots: Shot[] = [
  shot("/screenshots/hero/editor.webp", {
    title: "Editor",
    caption: "A Java file with folding markers, the minimap, the Project tool window and a status bar that stays out of the way.",
    alt: "The Editora editor: a Java file with syntax highlighting, a line-number gutter with folding markers, a minimap, the Project tool window and a status bar",
  }),
  shot("/screenshots/docs/project-map.png", {
    title: "Project Map",
    caption: "The Project Map lays the path to a file out as columns on a canvas, with the file's Structure past it.",
    alt: "The Project Map window: folder columns from the project root down to a Java package, the selected Shelf.java, its Structure column and a column of the Shelf class's members, joined by connectors",
    more: { href: "/features/project-map", label: "More about the Project Map" },
  }),
  shot("/screenshots/docs/http-response.png", {
    title: "HTTP Client",
    caption: "Run a request from an .http file and the response opens beside it, with a test handler checking the result.",
    alt: "An .http file with a GET request and its response handler, and the response pane beside it: a Run button, a 200 OK status pill, the response picker, Body, Headers, Request and Notes tabs, and the formatted JSON body",
    more: { href: "/features/http-client", label: "More about the HTTP Client" },
  }),
  shot("/screenshots/docs/markdown-preview.png", {
    title: "Markdown",
    caption: "Markdown in Split mode: the source on the left, the live preview on the right.",
    alt: "A Markdown file in Split mode: the source on the left and the rendered preview on the right with a heading, a task list, a table and a code block",
    more: { href: "/features/markdown-preview", label: "More about the Markdown preview" },
  }),
  shot("/screenshots/docs/git-log.png", {
    title: "Git Log",
    caption: "The Git Log with its commit graph, and the selected commit's details and files underneath.",
    alt: "The Git Log maximized: a commit graph beside each row's hash, branch and tag labels, subject, author and date, and below it the selected merge commit's details and changed files",
    more: { href: "/features/git", label: "More about Git" },
  }),
  shot("/screenshots/docs/terminal.png", {
    title: "Terminal",
    caption: "The integrated terminal, started in the project folder, under the file you are editing.",
    alt: "The Terminal tool window under the editor, running git status --short and git log --oneline in the project folder",
  }),
  shot("/screenshots/docs/maven.png", {
    title: "Maven",
    caption: "The Maven tool window beside a pom.xml: the lifecycle phases and the profiles the pom declares.",
    alt: "The Maven tool window beside an open pom.xml: a toolbar, the build root marked Trusted, a search field, the Lifecycle phases from clean to deploy, and the project's profiles",
    more: { href: "/features/build-tools", label: "More about build tools" },
  }),
];

/** The Project Map spotlight's big picture. */
export const mapHero: Shot = tourShots[1];

/** Extra Map captures: the /features spotlight's gallery. */
export const mapShots: Shot[] = [
  shot("/screenshots/map/references.png", {
    title: "Callers as cards",
    caption: "Show Callers as Cards: every call site opens around the declaration, one edge each.",
    alt: "Show Callers as Cards on Pricing.apply: the callers in Shelf.checkout and two ShelfTest tests open as cards around Pricing.java, each joined to it by an edge labelled by name",
  }),
  shot("/screenshots/map/recent-changes.png", {
    title: "Recent changes",
    caption: "Show recent changes warms the rows where the work happens; tooltips name the owners.",
    alt: "Show recent changes on the Editora repo: rows tinted and underlined by how often they changed, with a tooltip reading 5 commits in 90 days, last: Adrian De Leon, 2 weeks ago, and Owners: @adriandeleon from .github/CODEOWNERS",
  }),
  shot("/screenshots/map/trail.png", {
    title: "Trails",
    caption: "A trail walks the team through the code, a note at every stop.",
    alt: "A trail playing: the bar reads Stop 2 of 4, Pricing.discountFor, the card highlights that method's lines and the stop's note is stuck beside it",
  }),
  shot("/screenshots/map/filters.png", {
    title: "Filters",
    caption: "Filters narrow the map and count what they hide.",
    alt: "Git changed with Hide unmatched on: only the changed files and the folders that lead to them stay, with a 65 rows hidden badge and shown/total counts on each column",
  }),
].map((s) => ({ ...s, more: { href: "/features/project-map", label: "More about the Project Map" } }));

const mapCard = shot("/screenshots/docs/project-map-card.png", {
  title: "Preview cards",
  caption: "A preview card opens the way the map flows, past the file's column.",
  alt: "A read-only preview card for Shelf.java on the Project Map, opened past the file's column and joined to its row by a connector, showing syntax-highlighted Java",
  more: { href: "/features/project-map", label: "More about the Project Map" },
});

/** Docs captures that are not in the slideshow. */
const aroundShots: Shot[] = [
  shot("/screenshots/docs/welcome.png", {
    title: "Welcome",
    caption: "The Welcome page, with the Project tool window open beside it.",
    alt: "The Welcome tab with the Editora logo, a Start list (New File, Open File, Open Folder as a Project, Clone Git Repository, Command Palette) with shortcuts, and Recent Projects and Recent Files lists",
    more: { href: `${docsBase}/workspace/`, label: "Read the workspace docs" },
  }),
  shot("/screenshots/command-palette.webp", {
    title: "Command palette",
    caption: "The M-x command palette: fuzzy-search any command, with its key binding beside it.",
    alt: "The M-x command palette over the editor, filtered to bookmark commands, each row showing its key binding",
    more: { href: "/features/command-driven-core", label: "More about commands" },
  }),
  shot("/screenshots/bookmarks-panel.webp", {
    title: "Bookmarks",
    caption: "The Bookmarks tool window lists bookmarks across every file.",
    alt: "The Bookmarks tool window beside the editor, listing bookmarks grouped by project and file with their lines",
    more: { href: "/features/bookmarks-notes", label: "More about bookmarks and notes" },
  }),
  shot("/screenshots/file-information.webp", {
    title: "File information",
    caption: "File Information: dates, size, encoding, line endings, counts and the character under the caret.",
    alt: "The File Information tool window beside a Java file, with File, Text Settings, Count and Character sections",
  }),
  shot("/screenshots/doctor.webp", {
    title: "Doctor",
    caption: "The Doctor lists every external tool Editora can use, and whether it was found.",
    alt: "The Doctor tab: a summary of what is OK, missing or off, then checks grouped by area (version control, search, encryption, preview and diagram tools), each with its status and version",
    more: { href: "/features/doctor", label: "More about the Doctor" },
  }),
  shot("/screenshots/docs/git-commit.png", {
    title: "Commit",
    caption: "The Commit window lists changes and untracked files with their line counts; the editor gutter shows the same changes.",
    alt: "The Commit tool window beside the editor: Shelf.java under Changes with +3 −2 line counts, two untracked entries, Amend and Sign off checkboxes and a commit message being typed; the editor gutter marks the changed lines",
    more: { href: "/features/git", label: "More about Git" },
  }),
  shot("/screenshots/docs/git-blame.png", {
    title: "Blame",
    caption: "Blame annotations in the gutter; lines you have not committed yet say so.",
    alt: "A Java file with the blame gutter on: each line shows its author and date, and the three changed lines show You · Uncommitted changes",
    more: { href: "/features/git", label: "More about Git" },
  }),
  shot("/screenshots/docs/run-file.png", {
    title: "Run a file",
    caption: "A Python script after a run: the ▶ in the gutter, the runtime chip, and the command line above the output.",
    alt: "hello.py with a green run arrow in the gutter on its __main__ guard, and the Run tool window below showing the Python 3.13 runtime chip, Process finished with exit code 0, the muted command line and the script's output",
    more: { href: "/features/run-files", label: "More about running files" },
  }),
  shot("/screenshots/docs/run-configurations.png", {
    title: "Run configurations",
    caption: "Add starts from the open file: here a Python script configuration, prefilled from hello.py.",
    alt: "The Run Configurations window with a hello.py entry in the list and its form: Name, Type (Python script), Script, Interpreter, Interpreter options, Program arguments, Working directory, Environment, Before launch, Console and Run on",
    more: { href: "/features/run-configurations", label: "More about run configurations" },
  }),
  shot("/screenshots/docs/remote-connect.png", {
    title: "SFTP",
    caption: "Connecting to a host over SFTP.",
    alt: "The Connect to SFTP dialog over the editor, with Host, Port, User, Authentication, Key file, Secret and Path fields",
    more: { href: "/features/remote-sftp", label: "More about remote files" },
  }),
  shot("/screenshots/docs/settings.png", {
    title: "Settings",
    caption: "The Settings window: pages in the sidebar, each with its icon, and a live preview.",
    alt: "The Settings window on its Appearance page: User and Project scope buttons, a search field, a sidebar of pages with icons, and the page's language, font, theme and editor theme settings above a live preview",
    more: { href: "/features/themes-fonts", label: "More about themes and fonts" },
  }),
  shot("/screenshots/docs/settings-sync.png", {
    title: "Settings Sync",
    caption: "Settings → Sync before connecting: a Git repository carries your settings between machines.",
    alt: "The Sync page of the Settings window: a Repository URL field, a Branch field with Connect and Create on GitHub buttons, a Status row, switches for snippets, abbreviations, templates and the personal dictionary, and the automatic sync interval",
    more: { href: "/features/settings-sync", label: "More about Settings Sync" },
  }),
];

const stripTags = (html: string) => html.replace(/<[^>]+>/g, "");
const hashOf = (src: string) => createHash("sha1").update(readFileSync(pub(src))).digest("hex");

/** The /screenshots gallery: everything above, then each feature's capture. */
export async function gallerySections(): Promise<ShotSection[]> {
  const sections: ShotSection[] = [
    {
      id: "highlights",
      title: "Highlights",
      lead: "The pictures the home page plays through.",
      shots: tourShots,
    },
    {
      id: "project-map",
      title: "Project Map",
      lead: "Your project as a map, in a window of its own.",
      shots: [mapCard, ...mapShots],
    },
    {
      id: "around-the-editor",
      title: "Around the editor",
      lead: "More of the app: tool windows, Git, running code, remote files and settings.",
      shots: aroundShots,
    },
  ];
  // A feature whose picture is one of the above (the Project Map's is the
  // docs capture) is not shown twice.
  const seen = new Set(sections.flatMap((s) => s.shots.map((x) => hashOf(x.src))));
  for (const g of await groupedFeatures()) {
    const shots = g.items.flatMap((f) => {
      const src = ["png", "jpg", "jpeg", "webp"]
        .map((e) => `/screenshots/features/${f.id}.${e}`)
        .find(has);
      if (!src || seen.has(hashOf(src))) return [];
      return [
        shot(src, {
          title: f.data.title,
          caption: stripTags(f.data.summary),
          alt: `${f.data.title} in Editora`,
          more: { href: `/features/${f.id}`, label: `More about ${f.data.title}` },
        }),
      ];
    });
    if (shots.length) sections.push({ id: groupId(g.title), title: g.title, shots });
  }
  return sections;
}
