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
import { featureShotAlt, groupedFeatures, groupId } from "./features";

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
  shot("/screenshots/docs/command-palette.png", {
    title: "Command palette",
    caption: "The palette after typing fold: every matching command, its key, and what the highlighted one does.",
    alt: "The command palette over a Java file with fold typed: View: Fold, Fold All, Toggle Fold and Fold Level 1 to 5, each with its chord, the highlighted row's description below and a hint line naming the keys",
    more: { href: `${docsBase}/navigation/`, label: "Read the navigation docs" },
  }),
  shot("/screenshots/docs/bookmarks.png", {
    title: "Bookmarks",
    caption: "The Bookmarks tool window groups bookmarks by project and file.",
    alt: "The Bookmarks tool window beside Shelf.java: bookmarks grouped under the current project and then by file, each with its line text and number; the editor gutter marks the bookmarked line",
    more: { href: `${docsBase}/bookmarks-notes/`, label: "Read the bookmarks notes docs" },
  }),
  shot("/screenshots/file-information.webp", {
    title: "File information",
    caption: "File Information: dates, size, encoding, line endings, counts and the character under the caret.",
    alt: "The File Information tool window beside a Java file, with File, Text Settings, Count and Character sections",
  }),
  shot("/screenshots/docs/doctor.png", {
    title: "Doctor",
    caption: "The Doctor puts what needs attention first and folds the sections where everything is OK.",
    alt: "The Doctor tab: a summary line of 1 warning, 23 OK, 9 not installed and 3 off, a Needs Attention section with the GitHub CLI not signed in, then Version Control, Search and Encryption folded to one line and the Preview & Diagrams rows",
    more: { href: `${docsBase}/troubleshooting/`, label: "Read the troubleshooting docs" },
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

/** Docs captures for the pages that show one feature each. */
const docsShots: Shot[] = [
  shot("/screenshots/docs/multiple-cursors.png", {
    title: "Multiple cursors",
    caption: "Select all occurrences turns every Item in the file into a cursor.",
    alt: "Shelf.java with every occurrence of Item selected at once: each occurrence highlighted with its own caret, down the sample list and in the methods below",
    more: { href: `${docsBase}/editing/`, label: "Read the editing docs" },
  }),
  shot("/screenshots/docs/hex-viewer.png", {
    title: "Hex viewer",
    caption: "A binary file opens as an offset, hex and ASCII dump.",
    alt: "sample.bin open read-only in the hex viewer: an offset column, sixteen bytes per row in hex, and the printable characters on the right",
    more: { href: `${docsBase}/editing/`, label: "Read the editing docs" },
  }),
  shot("/screenshots/docs/theme-picker.png", {
    title: "Themes",
    caption: "Set App Theme lists every theme; type to filter.",
    alt: "The Set App Theme picker over the editor: a filter field above a list that starts with Editora Light, Editora Dark, Primer, Nord, Cupertino, Dracula and the Army and Autumn themes",
    more: { href: `${docsBase}/themes-fonts/`, label: "Read the themes fonts docs" },
  }),
  shot("/screenshots/docs/snippet-picker.png", {
    title: "Snippets",
    caption: "Snippet: Insert… lists the snippets for the file's language, each with its trigger and a preview.",
    alt: "The Insert Snippet picker over a Java file: each trigger (main, psvm, cmain, class, interface, record, enum, sealed, package, import, ctor, method) with its description and a preview of its body",
    more: { href: `${docsBase}/snippets-templates/`, label: "Read the snippets templates docs" },
  }),
  shot("/screenshots/docs/template-picker.png", {
    title: "File templates",
    caption: "New File From Template shows where each template comes from and what it makes.",
    alt: "The New File From Template picker: Java Class, Java Compact Source, HTML Page, Markdown Document, Python Script, Node.js Script, Shell Script, Zsh Script, JUnit 5 Test and more, each marked Bundled with a description",
    more: { href: `${docsBase}/snippets-templates/`, label: "Read the snippets templates docs" },
  }),
  shot("/screenshots/docs/notes.png", {
    title: "Personal notes",
    caption: "A personal note on a line: tinted in the editor, listed in the Personal Notes tool window.",
    alt: "The Personal Notes tool window beside Shelf.java listing a note on line 48, Check the discount rounding before the release; the noted line is tinted in the editor and marked in the gutter",
    more: { href: `${docsBase}/bookmarks-notes/`, label: "Read the bookmarks notes docs" },
  }),
  shot("/screenshots/docs/html-structure.png", {
    title: "HTML structure",
    caption: "Structure without the language server: elements labelled tag, then #id, then .class.",
    alt: "widget.html with the Structure tool window open and no language server: html, head, body, main.widget, h1, p#count.count and div.buttons, each with its line",
    more: { href: `${docsBase}/html/`, label: "Read the HTML docs" },
  }),
  shot("/screenshots/docs/spell-check.png", {
    title: "Spell checking",
    caption: "In code, spell checking reads comments and strings, not identifiers.",
    alt: "A Java file whose comments and string contain misspelled words underlined in red, while identifiers and keywords are left alone",
    more: { href: `${docsBase}/languages/`, label: "Read the languages docs" },
  }),
  shot("/screenshots/docs/openapi-preview.png", {
    title: "API docs preview",
    caption: "An OpenAPI spec previews as API docs beside its source.",
    alt: "petstore.yaml in Split view: the YAML on the left and, on the right, API docs titled Pet Store 1.0.0 with its server and GET and POST endpoint cards listing parameters and responses",
    more: { href: `${docsBase}/previews/`, label: "Read the previews docs" },
  }),
  shot("/screenshots/docs/crontab-preview.png", {
    title: "Crontab preview",
    caption: "A crontab decoded: each schedule in plain English with its next runs, and a malformed line flagged.",
    alt: "deploy.crontab in Split view: each schedule decoded on the right, such as Every 15 minutes and At 02:30, Monday through Friday, with its next run times, and a bad line flagged with minute 99 out of range",
    more: { href: `${docsBase}/previews/`, label: "Read the previews docs" },
  }),
  shot("/screenshots/docs/encrypt-recipients.png", {
    title: "Encryption",
    caption: "The recipient picker with two people chosen; a key that can only sign is listed last.",
    alt: "The Encrypt README.md picker in Public keys mode: Ada Lovelace and Sam Rivera ticked and shown as chips, Release Team unticked, a signing-only key greyed at the bottom, a Format menu and an Encrypt for 2 People button",
    more: { href: `${docsBase}/encrypted-files/`, label: "Read the encrypted files docs" },
  }),
  shot("/screenshots/docs/settings-keymaps.png", {
    title: "Keymaps",
    caption: "Settings → Keymaps: pick a keymap, then rebind any command in the list below.",
    alt: "Settings on the Keymaps page: the keymap menu set to Emacs, and below it a filterable list of every command with its chord or Unbound",
    more: { href: `${docsBase}/keymaps/`, label: "Read the keymaps docs" },
  }),
  shot("/screenshots/docs/settings-ai.png", {
    title: "AI settings",
    caption: "Settings → AI: the master switch for every AI feature.",
    alt: "Settings on the AI page: the Enable AI master switch turned on, with its description, and the AI Agent and AI Actions pages listed under AI in the sidebar",
    more: { href: `${docsBase}/ai/`, label: "Read the AI docs" },
  }),
  shot("/screenshots/docs/settings-mcp.png", {
    title: "MCP server",
    caption: "Settings → MCP Server while the server runs, with the button that copies the connect command.",
    alt: "Settings on the MCP Server page: the Enable MCP server switch on, and the Server row reading Running at an http://127.0.0.1 endpoint with Copy Endpoint Command and New Token buttons",
    more: { href: `${docsBase}/mcp/`, label: "Read the MCP server docs" },
  }),
  shot("/screenshots/docs/settings-external-tools.png", {
    title: "External tools",
    caption: "A tool that runs Prettier on the current file.",
    alt: "Settings on the External Tools page with one tool, Prettier: Command npx, Arguments prettier --write $FilePath$, a blank working directory, Standard input None, Output Show in console and Enabled on, with the list of macros below",
    more: { href: `${docsBase}/external-tools/`, label: "Read the external tools docs" },
  }),
  shot("/screenshots/docs/settings-plugins.png", {
    title: "Plugins",
    caption: "Settings → Plugins with plugins turned on and none installed yet.",
    alt: "Settings on the Plugins page: Enable plugins on, the Installed Plugins section with Reload Plugins and the plugins folder, Browse plugins and Install from file buttons, and the Registry URL",
    more: { href: `${docsBase}/plugins/`, label: "Read the plugins docs" },
  }),
  shot("/screenshots/docs/lsp-references.png", {
    title: "Find references",
    caption: "Find references in a Java project, grouped by file.",
    alt: "Shelf.java with the caret on discountFor and the References tool window below: 2 references to discountFor in 2 files, one in Shelf.java at 48:41 with its line, and one in ShelfTest.java",
    more: { href: `${docsBase}/lsp/`, label: "Read the language server docs" },
  }),
  shot("/screenshots/docs/lsp-hover.png", {
    title: "Hover docs",
    caption: "Hover docs: the signature and Javadoc at the caret.",
    alt: "A hover popup over discountFor in Shelf.java showing the signature Discount com.example.shelf.Pricing.discountFor(Item item), its Javadoc and its parameter",
    more: { href: `${docsBase}/lsp/`, label: "Read the language server docs" },
  }),
  shot("/screenshots/docs/deployments.png", {
    title: "Deployments",
    caption: "A deployment to a staging host, filled in.",
    alt: "The Deployments window with one deployment, staging: Host staging.example.com, port 22, User ada, Automatic authentication, Folder on host /srv/apps/shelf, an empty Leave out list and the Options checkboxes, with Deploy Now and Close buttons",
    more: { href: `${docsBase}/deployments/`, label: "Read the deployments docs" },
  }),
  shot("/screenshots/docs/find-in-files.png", {
    title: "Find in Files",
    caption: "Find in Files: matches grouped by file, with a ripgrep badge when it did the search.",
    alt: "The Find in Files tool window beside a Java file: the query discountFor, case, regex and whole-word toggles, replace and include and exclude fields, then 4 matches in 4 files grouped under Pricing.java, Shelf.java, ShelfTest.java and README.md",
    more: { href: `${docsBase}/navigation/`, label: "Read the navigation docs" },
  }),
  shot("/screenshots/docs/git-branches.png", {
    title: "Branches",
    caption: "The branch popup: actions first, then local and remote branches, each section foldable.",
    alt: "The Branches popup over a Java file: a search field, an Actions section with New Branch, Update Project (Pull), Fetch, Push, Stash Changes, Unstash and Commit with their chords, then Local with master ticked and its origin/master upstream, and Remote with origin/master",
    more: { href: `${docsBase}/git/`, label: "Read the git docs" },
  }),
  shot("/screenshots/docs/git-peek-change.png", {
    title: "Peek a change",
    caption: "Peek Change at Caret shows one hunk against HEAD with the buttons to revert or stage it.",
    alt: "Shelf.java with two changed lines marked in the gutter and the peek card for the one at the caret: Added, change 2 of 2, the working-tree line in green, and Previous, Next, Revert Hunk, Stage Hunk, Copy Old Text and Open Diff buttons",
    more: { href: `${docsBase}/git/`, label: "Read the git docs" },
  }),
  shot("/screenshots/docs/macro-recording.png", {
    title: "Macros",
    caption: "While you record, the status bar shows REC; click it to stop.",
    alt: "Shelf.java while a macro records: a comment just typed at the end of a line, Recording macro… at the left of the status bar and a red REC chip at its right",
    more: { href: `${docsBase}/macros/`, label: "Read the macros docs" },
  }),
  shot("/screenshots/docs/markdown-format-bar.png", {
    title: "Markdown format bar",
    caption: "Select text and the format bar appears above it.",
    alt: "A Markdown file with three words selected and the format bar above them: bold, italic, strikethrough, code, link, bulleted, numbered and task lists, block quote, table, and a heading level picker reading Normal",
    more: { href: `${docsBase}/markdown/`, label: "Read the markdown docs" },
  }),
  shot("/screenshots/docs/markdown-lint.png", {
    title: "Markdown lint",
    caption: "Lint problems as squiggles, a count in the status bar, and the Markdown Lint window with the rule behind each one.",
    alt: "lint.md with squiggles under its deliberate problems and the Markdown Lint tool window below: lint.md 18 issues, then rows such as Ln 1:1 MD041 First line in a file should be a top-level heading and Ln 6:1 MD001 Heading levels should increment by one level at a time; the status bar counts 18 lint issues",
    more: { href: `${docsBase}/markdown/`, label: "Read the markdown docs" },
  }),
  shot("/screenshots/docs/markdown-backlinks.png", {
    title: "Backlinks",
    caption: "The Backlinks window lists every note that links to the open one.",
    alt: "ideas.md open with the Backlinks tool window below: 6 links to this file, grouped under archive/old-plan.md and index.md, each with its line number and the link text",
    more: { href: `${docsBase}/markdown/`, label: "Read the markdown docs" },
  }),
  shot("/screenshots/docs/http-environments.png", {
    title: "HTTP environments",
    caption: "The Env pill's menu: pick an environment, or open the files that define them.",
    alt: "environments.http open with the environment menu dropped from its Env pill: dev, prod and local, No environment ticked, and Open http-client.env.json and Open http-client.private.env.json",
    more: { href: `${docsBase}/http-client/`, label: "Read the HTTP client docs" },
  }),
  shot("/screenshots/docs/editor-groups.png", {
    title: "Editor groups",
    caption: "Split Editor Right: a test beside what it tests, each group with its own tabs.",
    alt: "Two editor groups side by side: Shelf.java on the left and ShelfTest.java on the right, each with its own tab strip, caret and scrollbar",
    more: { href: `${docsBase}/workspace/`, label: "Read the workspace docs" },
  }),
  shot("/screenshots/docs/terminal-find.png", {
    title: "Find in the terminal",
    caption: "Find in the terminal highlights every match in the scrollback and counts them.",
    alt: "The Terminal tool window after git log --oneline, with the find bar open over it: map typed, every match highlighted in the commit subjects, 4 of 4, an Aa toggle, up and down arrows and a close button",
    more: { href: `${docsBase}/terminal/`, label: "Read the terminal docs" },
  }),
  shot("/screenshots/docs/test-results.png", {
    title: "Test Results",
    caption: "Test Results after a Maven test run: counts in the header, each test in the tree.",
    alt: "The Test Results tool window after a Maven run: 9 of 9 tests passed with a full green bar and 2.0 s, a filter field, the tree of ShelfTest's methods each ticked with its time, and the run summary on the right: 9 passed, 0 failed, 0 skipped",
    more: { href: `${docsBase}/build-tools/`, label: "Read the build tools docs" },
  }),
  shot("/screenshots/docs/package-scripts.png", {
    title: "Package Scripts",
    caption: "Package Scripts lists the scripts of the project's package.json, plus install and ci; double-click runs one.",
    alt: "package.json open beside the Package Scripts tool window: Build root npm, a filter field, the Scripts section with start, build, test and lint, and a Common section with install and ci",
    more: { href: `${docsBase}/build-tools/`, label: "Read the build tools docs" },
  }),
  shot("/screenshots/docs/lsp-code-actions.png", {
    title: "Code actions",
    caption: "Code actions open at the caret: fixes first, then assists and refactorings.",
    alt: "Shelf.java with the caret on a line whose type is wrong and the code actions list open at the caret: two Fix rows, Change type of label to double and Change type of total to String, then an Assist row, Add final modifier for label, each labelled in words",
    more: { href: `${docsBase}/lsp/`, label: "Read the language server docs" },
  }),
  shot("/screenshots/docs/lsp-problems.png", {
    title: "Problems",
    caption: "Diagnostics: a squiggle in the editor, a counter at the top right, and the Problems window with the list.",
    alt: "Shelf.java with a red squiggle under a type error on line 44, a red marker in the gutter, the problem counter at the top right of the editor reading 1, and the Problems tool window below: 1 problem in 1 file, grouped under Java and Shelf.java",
    more: { href: `${docsBase}/lsp/`, label: "Read the language server docs" },
  }),
  shot("/screenshots/docs/lsp-rename.png", {
    title: "Rename",
    caption: "Rename symbol: the prompt comes pre-filled, and the server has already checked the spot.",
    alt: "The Rename Symbol prompt over Shelf.java: a New name field pre-filled with discountFor, the symbol under the caret, with Cancel and OK buttons",
    more: { href: `${docsBase}/lsp/`, label: "Read the language server docs" },
  }),
  shot("/screenshots/docs/edit-breakpoint.png", {
    title: "Edit Breakpoint",
    caption: "Edit Breakpoint: a condition, a hit count, or a log message that turns it into a logpoint.",
    alt: "The Edit Breakpoint form for Shelf.java line 50 over the editor: the line's code above the fields, a Condition field with item.price() > 1.0 typed, an empty Log message, a Hit count field whose hint reads Every hit, or a number: 5 stops on the fifth hit, Enabled ticked, Remove once hit, and Cancel and OK buttons",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-attach-to-process.png", {
    title: "Attach to Process",
    caption: "Attach to Process: programs listening for a debugger come first, then those started without debugging.",
    alt: "The Attach to Process list over Main.java: com.example.inventory.InventoryServer (pid 11), Java · listening on localhost:5005; server.js (pid 13), Node.js · listening on localhost:9229; worker.py (pid 15), Python · listening on localhost:5678; then com.example.inventory.ReportJob (pid 12) and watch.js (pid 14), not started for debugging, each with what to start it with",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-lambda-breakpoint.png", {
    title: "Lambda breakpoints",
    caption: "A lambda breakpoint: the λ on its dot, and the menu that chooses where it stops.",
    alt: "Shelf.java with a breakpoint on line 56, .filter(item -> item.price() < 1.0), its dot carrying a λ, and the dot's right-click menu: Edit Breakpoint…, Disable Breakpoint, Remove Breakpoint, Stop on the Line unchecked, Stop in Lambda 1: item -> item.price() < 1.0) checked, and Breakpoints…",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-member-marks.png", {
    title: "Method and field marks",
    caption: "A field watchpoint marked on its field and a method breakpoint on its method.",
    alt: "Shelf.java with a field watchpoint's mark, a dot with an eye, beside line 13, private final List<Item> items;, and a method breakpoint's mark, a dot with an arrow into it, beside line 39, Receipt checkout(int limit) {",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-pinned-values.png", {
    title: "Pinned values",
    caption: "Three values pinned to one line, evaluated at the stop.",
    alt: "Shelf.java paused on line 52 in checkout, with total, subtotal and item.name() pinned to line 54, the loop's closing brace, which shows ◆ total = 4.560000 · subtotal = 8.250000 · item.name() = \"Notebook\", and the Debug window below with the call stack and Variables",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-go-goroutines.png", {
    title: "Debugging Go",
    caption: "A Go program stopped in a goroutine: each goroutine is a thread in the Threads view.",
    alt: "restock.go paused on line 16, restocked++, inside a goroutine that restock starts, with inline values on the lines above, and the Debug window below: the Threads view listing the program's goroutines, such as [Go 1] sync.runtime_SemacquireWaitGroup, runtime.gopark ones and the suspended main.restock.func1, Variables with w, mu, restocked and wg, and the program's output in the console",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-rust-values.png", {
    title: "Debugging Rust",
    caption: "A Rust program at a breakpoint: a Vec unfolds to its items, and each String reads as its text.",
    alt: "shelf.rs paused on line 49, total += subtotal, with inline values such as item: {sku:\"A-2\", name:\"Notebook\", quantity:3, …} on the lines above, and the Debug window below: the call stack at shelf::shelf::checkout, and Variables with lines, a Vec<String> of size 2, unfolded to \"A-1  Pencil …\" and \"A-2  Notebook …\", then total, item and discount = None",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-settings-go-rust.png", {
    title: "Go and Rust debugger settings",
    caption: "The Go and Rust cards: Delve found where go install puts it, CodeLLDB found in VS Code's extension.",
    alt: "The Go (Delve) and Rust (CodeLLDB) cards of Settings → Debugging: Enable Go debugging (Delve) on, an empty Delve (dlv) path field with Browse…, Detected dlv: found (/home/ada/go/bin/dlv); Enable Rust debugging (CodeLLDB) on, an empty CodeLLDB (codelldb) path field, and Detected codelldb: found (/home/ada/.vs…-lldb-1.12.3/adapter/codelldb); both Install buttons read Installed",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-history.png", {
    title: "Debug History",
    caption: "An earlier stop, read back from Debug History: a recording breakpoint kept six stops while the program ran on.",
    alt: "shelf.go during a Go debug session, shown at stop 4 of 7: line 42, total += subtotal, is marked grey with its inline values, total: 19.919 and subtotal: 4.8; the Debug window below has the banner Stop 4 at 22:33:59, breakpoint: main.checkout (shelf.go:42). Values as they were then; the program has run on, with a Back to the live stop link, the call stack at main.checkout, Variables with the values that changed since stop 3 highlighted, and the console listing Recorded stop 1 to Recorded stop 6",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-history-list.png", {
    title: "Debug History list",
    caption: "Debug: Show History… lists the stops of the session, newest first.",
    alt: "The Debug History list: Stop 7, breakpoint, main.checkout — shelf.go:46, then Stops 6 to 1, each a breakpoint in main.checkout at shelf.go:42, with the time of each stop",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-disassembly.png", {
    title: "Disassembly view",
    caption: "The Disassembly view: the instructions around the stopped line, each run labelled with its source line.",
    alt: "shelf.go paused on line 39 in a Go program, with the Debug window's Disassembly view open beside Variables: machine instructions such as CALL main.discountFor(SB) and MOVSD_XMM X0, 0x58(SP) with their addresses, runs labelled shelf.go:37, shelf.go:38 and shelf.go:39, and the instruction the frame is at highlighted",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-settings-cpp.png", {
    title: "C and C++ debugger settings",
    caption: "The Rust, C and C++ card: C and C++ can switch from CodeLLDB to GDB.",
    alt: "The Rust, C and C++ (CodeLLDB) card of Settings → Debugging: Enable Rust debugging (CodeLLDB) on, Enable C and C++ debugging (CodeLLDB) on, C and C++ debugger set to CodeLLDB with the hint that GDB debugs through its own DAP mode (GDB 14 or later), an empty GDB path, an empty CodeLLDB (codelldb) path with Browse…, and Detected codelldb: found",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-settings-csharp-ruby-php.png", {
    title: "C#, Ruby and PHP debugger settings",
    caption: "The C#, Ruby and PHP cards, before their debuggers are installed.",
    alt: "The C# (netcoredbg), Ruby (rdbg) and PHP (Xdebug) cards of Settings → Debugging, each with its Enable switch on, its path field (netcoredbg path, rdbg path, PHP Debug (phpDebug.js) path) with Browse…, a Detected line reading not found, and an Install… button",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-sessions.png", {
    title: "Several debug sessions",
    caption: "A program and one of its tests, each in a session of its own, with a tab for each in the Debug window.",
    alt: "The Debug window with two Go sessions: tabs main.go · suspended and TestCheckoutTotals · suspended above the toolbar, Stop All at the right, and the test's session shown, paused at line 8 of shelf_test.go with lines and total in Variables",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-both-question.png", {
    title: "Debug Both",
    caption: "Debugging another file while a session runs asks first: Debug Both, Stop and Debug, or Cancel.",
    alt: "The question Debug Another File?: main.go is being debugged. Debug shelf_test.go as well, in a session of its own, or stop main.go and debug shelf_test.go instead? with Stop and Debug, Cancel and Debug Both buttons",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-compounds.png", {
    title: "Debug compounds",
    caption: "A compound of two saved run configurations, made in Debug: Edit Compounds….",
    alt: "The Compounds window: a compound named Server and worker on the left; on the right its Name, the list Run configurations to debug together with API server and Restock worker ticked and Web client and Price check not, Stopping one stops them all ticked, and Add, Remove, Debug and Close buttons",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-triggered-breakpoint.png", {
    title: "Triggered breakpoints",
    caption: "A breakpoint that waits until another is hit, chosen with Debug: Add Triggered Breakpoint….",
    alt: "shelf.js with breakpoints on lines 12 and 13 and an hourglass breakpoint on line 21, under the Debug: Add Triggered Breakpoint… list for line 21 with shelf.js:12 ticked and shelf.js:13 below it",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-breakpoint-groups.png", {
    title: "Breakpoint groups",
    caption: "Breakpoints in named groups, turned on and off together; Alt+G in the list flips a group.",
    alt: "The Breakpoints list: shelf.js:12 and shelf.js:13 disabled in group checkout, and shelf.js:21 waiting until shelf.js:12 is hit, in group report",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-add-logpoint.png", {
    title: "Add Logpoint",
    caption: "Shift+click the gutter or run Add Logpoint: the form asks only for the message, and braces include a value.",
    alt: "The Add Logpoint form for shelf.js line 13: the line's code above a Log message field holding {item.name}: total = {total}, a hint that the program does not suspend, and Cancel and OK",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-logpoint-console.png", {
    title: "Logpoint output",
    caption: "A logpoint logs a line in the Debug console on every pass and never stops the program.",
    alt: "A paused JavaScript session with a logpoint's blue diamond on line 13 of shelf.js and the Debug console listing one logged line per item, from Pencil: total = 0 to Marker",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-loaded-sources.png", {
    title: "Loaded Sources",
    caption: "The scripts a Node.js program has loaded: the project's file first, then Node.js's own modules, read-only.",
    alt: "The Debug: Loaded Sources… list over a paused JavaScript session: shelf.js marked project folder, then <node_internals> modules marked read-only, from the debugger",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-modules.png", {
    title: "Modules",
    caption: "The assemblies a C# program has loaded: its own first with symbols loaded, then the .NET runtime's.",
    alt: "The Debug: Modules… list over a paused C# session: Shelf.dll with Symbols loaded, then System.Collections.dll, System.Console.dll, System.Linq.dll and other .NET assemblies with Symbols not found, each with its path",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-eval-source.png", {
    title: "Code without a file",
    caption: "Code run through eval opens read-only where the program stopped in it.",
    alt: "A read-only tab named <eval>/VM46947589 with a report function, its debugger; line marked as the current line, and the call stack global.report:3, VM46947589 above shelf.js",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-mark-object.png", {
    title: "Mark Object",
    caption: "An object labelled pencil, recognised in a watch and in a variable.",
    alt: "Variables in a paused Java session: the watch items.get(0) and the local item both read [pencil] Item@11 followed by the item's text, Item[sku=A-1, name=Pencil, quantity=12, price=0.4]",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/debug-view-as-json.png", {
    title: "View As JSON",
    caption: "A string opened with View As ▸ JSON: laid out, highlighted and read-only.",
    alt: "A read-only tab named order (JSON) showing indented JSON with lines, sku, qty and total, and Variables filtered to the string order",
    more: { href: `${docsBase}/run-debug/`, label: "Read the run debug docs" },
  }),
  shot("/screenshots/docs/trust-folder.png", {
    title: "Trusted folders",
    caption: "Before the first build in a folder, Editora lists what would run and asks.",
    alt: "The Build in the folder java? dialog: a build in this folder runs code the folder supplies itself, the pom.xml plugins it would run listed by coordinate, a warning that a build can read, change or delete your files, the folder's path, and Cancel and Trust folder and run buttons",
    more: { href: `${docsBase}/workspace/`, label: "Read the workspace docs" },
  }),
  shot("/screenshots/docs/github-pr-review.png", {
    title: "Pull request review",
    caption: "Review Pull Request Diff: the description, the file list, and a read-only diff a click away.",
    alt: "A Files changed tab for a pull request: the description rendered as Markdown in a card, Open on GitHub, Submit review… and Refresh links, and the changed files listed with their status letters and plus and minus counts",
    more: { href: `${docsBase}/github/`, label: "Read the GitHub docs" },
  }),
  shot("/screenshots/docs/settings-tool-windows.png", {
    title: "Tool Windows settings",
    caption: "Settings → Tool Windows: show or hide each window's button, pick its side, and order the stripes.",
    alt: "Settings on the Tool Windows page: a Layout row with Restore Default Layout, then the windows listed under Left and Right, each row with a switch, a side menu and up and down arrows",
    more: { href: `${docsBase}/workspace/`, label: "Read the workspace docs" },
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
    {
      id: "from-the-docs",
      title: "From the docs",
      lead: "Editing, previews, language servers, encryption and the settings pages, as the user docs show them.",
      shots: docsShots,
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
          alt: featureShotAlt(f),
          more: { href: `/features/${f.id}`, label: `More about ${f.data.title}` },
        }),
      ];
    });
    if (shots.length) sections.push({ id: groupId(g.title), title: g.title, shots });
  }
  return sections;
}
