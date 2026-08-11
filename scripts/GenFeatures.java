/// Generator for the feature pages (src/content/features/*.md).
/// A JDK 25 compact source file (JEP 512): run with `java gen-features.java`
/// from the repo root or the scripts/ dir. Edit the Markdown directly after
/// generating; re-running overwrites every feature page.
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

record Feature(String slug, String group, int order, boolean beta,
               String title, String summary, String body) {}

// Group labels (must match the order the cards render in).
static final String KB = "Keyboard & commands";
static final String ED = "Editing";
static final String CI = "Code intelligence";
static final String RD = "Run & debug";
static final String GD = "Git & diff";
static final String DD = "Docs & diagrams";
static final String WF = "Workspace & files";
static final String CE = "Customization & extensibility";

static final List<Feature> FEATURES = List.of(
    new Feature("command-driven-core", KB, 1, false,
        "Command-driven core",
        "Hunting through menus? Every action is a registered <code>Command</code>, bound to a chord or one <kbd>M-x</kbd> search away. 600+ commands, nothing buried.",
        """
Editora has no hidden actions. Every capability (save, toggle a bookmark, start the debugger, switch a theme) is a registered `Command` with an id and a title. That one decision powers four things at once:

- The **command palette** (`M-x`) fuzzy-searches all 600+ commands, each with a one-line description.
- **Keybindings** are just a map from a chord to a command id, so anything can be bound, or rebound.
- **Toolbar buttons** dispatch the same commands, so the UI and the keyboard never drift apart.
- The **[menu bar](/features/menu-bar)** is a curated view of the same registry, so each item shows its live keybinding and can never name an action that doesn't exist.

If you can describe it, you can find it by typing a few letters. Browse the full [command list](/commands).
"""),
    new Feature("keymaps", KB, 2, false,
        "Keymaps your way",
        "Pick <strong>Emacs</strong>, <strong>CUA</strong>, <strong>Sublime Text</strong>, <strong>VS Code</strong>, or <strong>IntelliJ IDEA</strong>. Switch live, no restart. Or rebind any command yourself in the built-in keybinding editor (multi-key chords like <kbd>C-x C-s</kbd> supported).",
        """
Editora ships five complete keymaps (**Emacs** (default), **CUA**, **Sublime Text**, **VS Code**, and **IntelliJ IDEA**) selectable in **Settings → Keymaps** and switchable **live, with no restart**. Each is a chord→command map over the same command ids, so switching changes accelerators without stranding any functionality.

Prefer your own bindings? The built-in keybinding editor records multi-key chords (like `C-x C-s`), rebinds any command, and resets to defaults. Overrides are saved in `settings.toml`, layered on top of the active keymap, so you only specify what you change.

On macOS the non-Emacs keymaps use ⌘ wherever the [keybindings reference](/keybindings) shows Ctrl.
"""),
    new Feature("jump-to-popups", KB, 3, false,
        "Jump-to popups",
        "Lost in a big project? Fuzzy-jump to recent files, symbols, open tabs, and tool windows, plus an Emacs <code>find-file</code>-style path finder.",
        """
Keyboard-first navigation: fuzzy pickers that get you anywhere without the mouse.

- **Recent files**: `C-x C-r`
- **Symbols / file structure**: `M-g i`
- **Open tabs**: `C-x b`
- **Tool windows**: `M-g t`
- **Bookmarks**: `M-g b`, **Notes**, `M-g n`

There's also an Emacs `find-file`-style **path finder** (`C-x C-f`) with prefix autocomplete, type and Tab to complete, Enter to descend a folder or open (or create) a file. Every picker shows a footer legend of its navigation keys.
"""),
    new Feature("multiple-cursors", KB, 4, false,
        "Multiple cursors",
        "Add a caret at the next occurrence, above/below, or on every occurrence at once, or <kbd>Alt</kbd>-drag a column/box selection to edit many places at once, VS Code-style.",
        """
Edit many places at once, VS Code-style. Add a caret at the **next occurrence** of the selection, or **above / below** the current line, or **Alt-drag** a column/box selection. Type, and the edit fans out to every caret; `Esc` collapses back to one.

**Select all occurrences** (`Ctrl+Shift+L` in the VS Code and Sublime keymaps) puts a cursor on every occurrence of the selection, or of the word under the caret, in one step. From the Find bar, **Alt+Enter** does the same for every match of the current query, so the query's case, regex and whole-word toggles decide what gets a cursor.

It's powered by Editora's RichTextFX fork, which adds multiple cursors and column selection as a layered input map that's completely transparent when there's a single caret.

Movement chords fan out too: `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and `M-b` move every caret, like the arrow keys. Document, paragraph, sentence and page motions stay on the primary caret.
"""),
    new Feature("macros", KB, 5, false,
        "Keyboard macros",
        "Record a sequence of edits and replay it: <kbd>F3</kbd> to start, <kbd>F4</kbd> to stop, <kbd>C-x e</kbd> to replay. Name and save macros, and bind them to keys.",
        """
Record a sequence of editor actions and replay it, Emacs-style. Recording captures the faithful interleaved stream of invoked commands and literally typed text, and replay reproduces the exact sequence, so replayed typing runs through the same auto-close and auto-indent assists as live typing.

- **F3** starts recording, **F4** stops, and **C-x e** replays the last macro.
- The palette adds **Replay Last N Times**, **Name and Save Last**, **Run Saved**, and **Delete Saved**.
- Saved macros persist across sessions, and each becomes its own palette command, so you can bind it to a shortcut in Settings → Keymaps like any other command.

The recording hooks are inert when you're not recording, so there's no idle cost. See the [macros guide](/docs/macros).
"""),
    new Feature("emacs-heritage", KB, 6, false,
        "Emacs heritage",
        "The Emacs editing <em>model</em>, not just its keybindings: a kill ring and a mark ring, <code>C-x r</code> rectangles, narrowing, query-replace, <kbd>C-u</kbd> prefix arguments, and structural sexp motion. Emacs is the default keymap.",
        """
Plenty of editors ship an "Emacs keymap" that maps a handful of chords onto their own commands. Editora implements the model underneath: the concepts an Emacs user reaches for without thinking, built as real features rather than shortcut aliases. Emacs is the default keymap, and everything here works in the other four too, through the palette or a binding of your own.

## Killing and yanking

A real kill ring, not a clipboard with extra steps. It holds the last 120 kills (Emacs' own `kill-ring-max`), consecutive kills accumulate into one entry, and the ring is shared with the system clipboard in both directions, so text copied in another application is yankable and a kill is pasteable elsewhere.

- **C-k** kill line, **M-d** kill word, **M-DEL** backward kill word, **C-S-DEL** kill whole line
- **M-z** zap to char, **C-M-k** kill sexp
- **C-y** yank, **M-y** yank-pop to cycle back through the ring

## The mark

- **C-SPC** sets the mark, **C-x C-SPC** pops back through the ring, **C-x h** marks the whole buffer.
- The ring is per buffer, as in Emacs, and marks are tracked through edits: type above a mark and it stays on its text rather than its old offset.

## Rectangles

The full **C-x r** family over the columns between point and mark: kill, copy, yank, delete, clear, open, string-rectangle, and number-lines. Each is a single undo step. Note these are character columns, so a tab counts as one, and rectangles are distinct from the Alt-drag multiple-cursor selection.

## Narrowing

**C-x n** narrows to the region, the current defun, or a fold region, and widening restores everything. It is true narrowing: search, replace, Select All and macros all see only the region, which is the point of it. Writing the file still writes the whole file.

## Search, replace, and motion

- **C-s** / **C-r** incremental search, repeating the chord to cycle matches.
- **M-%** query-replace and **C-M-%** query-replace-regexp, with the usual `y`, `n`, `!`, `.` and `q` answers.
- **M-s o** occur, listing every match in the buffer.
- **C-M-f** / **C-M-b** move by sexp, **C-M-SPC** marks one, and **C-M-a** / **C-M-e** jump to the start and end of a defun.
- **M-g j** and **M-g L** jump to any visible character or line by typing its label, in the spirit of avy.

## Prefix arguments

**C-u** takes a numeric argument the way Emacs does: `C-u 5 C-n`, `C-u 40 -` to type a row of dashes, `C-u C-SPC` to pop the mark. It is a repeat model rather than a full numeric argument, so a negative argument does not reverse motion and `C-u 0` specials are not honoured.

## Keyboard macros

The `C-x (`, `C-x )`, `C-x e` habit, on the function keys: **F3** starts recording, **F4** stops, **C-x e** replays. Recording captures the interleaved stream of commands and literally typed characters, so a replay reruns the same auto-indent and auto-close behaviour a human keystroke would, rather than pasting text in.

Past that it goes somewhere Emacs needs a `defun` to reach. A macro can be named and saved, it persists across sessions, and each saved macro **becomes a command of its own**, so it shows up in the palette and can be bound to a key in Settings like anything else. There is also a replay-N-times prompt, and a run-saved picker. See [keyboard macros](/features/macros).

## M-x, and a palette that explains itself

**M-x** does what you expect: fuzzy-match a command by name and run it. The difference is what the list can tell you, because every command is a registered object with an id, a title and a description rather than a symbol you have to already know.

- Each result shows its **description** and its current **keybinding**, so the palette doubles as the way you learn the chords.
- A command that cannot run right now is still **listed**, greyed out, and **says why**: the feature is switched off, or there is no file open, or you are not in a git repository. It points at the setting that would enable it instead of failing silently.
- The same list drives keybindings and the toolbar, so the palette can never drift out of step with what the editor can actually do.

## The rest of the muscle memory

Transpose (**C-t**, **M-t**, **C-x C-t**), fill paragraph (**M-q**) with a fill column and optional auto-fill, case commands (**M-u**, **M-l**, **M-c**), whitespace and line surgery (**M-\\**, **M-SPC**, **C-x C-o**, **C-o**, **M-^**), comment-dwim (**M-;**), abbreviations (**C-x a e**, **C-x a g**), and **C-g** to back out of anything.

## What we don't copy

Some of Emacs is inheritance, and some of it is a different philosophy that Editora deliberately does not follow.

- **No Elisp, and no editor-as-operating-system.** Emacs answers "can it do X?" by reimplementing X in Lisp. Editora answers it by [running the tool you already have](/features/doctor): your `git`, your `ripgrep`, your language servers. Extension is through [plugins](/features/plugins) and the [command system](/features/command-driven-core), so nothing here replaces a tuned Emacs configuration, and it is not trying to.
- **Discoverability is not opt-in.** Emacs rewards knowing the name of the function. Here every action carries a title and a description, Settings is a real window with checkboxes, and the palette explains why something is unavailable. You should not need to have read the manual to find a feature.
- **Emacs is a default, not a requirement.** Four other complete keymaps ship (CUA, Sublime, VS Code, IntelliJ), and any command can be rebound. Nobody is asked to convert.
- **Modern window conventions.** Tabs, tool windows, a project tree, drag and drop, and a working mouse, rather than the Emacs buffer and window model.

## Not yet

Registers, a global cross-buffer mark ring, and dabbrev are genuinely missing rather than rejected. A few of the ports are also approximations, and the page says so where it matters: `C-u` repeats rather than passing a true numeric argument, and rectangles count character columns rather than display columns.

Browse the [full command list](/commands) or the [keybindings reference](/keybindings).
"""),
    new Feature("menu-bar", KB, 7, false,
        "A menu bar, over the same commands",
        "Prefer to browse rather than recall? <strong>File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help</strong>, built over the command registry, so every item shows its live keybinding. Hide it in one keystroke.",
        """
The command palette is complete but unbrowsable: it answers "what is this called?" and not "what can this thing do?". The menu bar answers the second question.

**File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help.** Every item names a registered [command](/features/command-driven-core) — the same objects the palette lists and the keymap binds — so nothing in it can drift out of step with what Editora can actually do.

- Each entry shows its **current keybinding**, and updates when you [switch keymaps](/features/keymaps).
- A command whose feature is switched off appears **greyed rather than vanishing**, so the menu stays a stable map instead of rearranging itself as you toggle features.
- On **macOS** it sits in the system menu bar, where it belongs.

It is deliberately a curated subset. Editora registers over six hundred commands and a menu that listed all of them would be a worse palette; the palette remains the complete index.

Hide it from **Settings → Interface** or with **View: Toggle Menu Bar**, and it hides itself in Zen, Expert and [Simple](/features/simple-ui-mode) modes.
"""),
    new Feature("snippets", ED, 1, false,
        "Snippets",
        "Retyping the same boilerplate? Expand VS Code / TextMate templates with tab stops, mirrors, choices, and variables, all from a prefix + <kbd>Tab</kbd>. Ships for all 21 languages.",
        """
Expand boilerplate with interactive templates. Type a prefix and press **Tab**, or pick from the **Snippet: Insert…** list (`C-c i`).

Placeholders are pre-selected to overtype, **Tab / Shift-Tab** cycle the fields, mirrors update live, choice fields show a dropdown, and `$0` is the final caret. Bodies use the standard VS Code / TextMate syntax, `$1`, `${1:default}`, mirrors, `${1|a,b|}` choices, and variables (`$TM_FILENAME`, `$CLIPBOARD`, date/time, the selection…).

Snippets ship for all 21 highlighted languages; add your own in `~/.editora/snippets/<language>.json` (user snippets override the bundled ones).
"""),
    new Feature("smart-indentation", ED, 2, false,
        "Smart indentation",
        "Per-language auto-indent on <kbd>Enter</kbd> (block openers, matching-pair stanzas, closer re-alignment), plus smart backspace that clears a whole indent level in one press.",
        """
Enter does the right thing per language: it keeps the current line's indent, adds a level after a block opener (braces, `:` in Python/YAML, `do`/`then` in shell, an open tag in XML/HTML…), and splits a matching pair into an indented stanza with the closer dropped below. Typing a closer (`)]}` or a keyword like `end`/`fi`/`done`) re-aligns the line to its opener.

**Smart backspace** removes a whole indent level in one press, and on a blank, auto-indented line, a single Backspace jumps back to the end of the previous line. The indent unit (tabs vs spaces) is inferred from the file.
"""),
    new Feature("auto-close-brackets", ED, 3, false,
        "Auto-close & bracket matching",
        "Typing <code>([{</code> or quotes inserts the closer, type-over to skip, wrap a selection, and Backspace clears an empty pair. The bracket beside the caret is highlighted with its match.",
        """
Typing `(`, `[`, `{`, `"`, `'`, or `` ` `` inserts the matching closer and keeps the caret between them. Type the closer when it's already next to the caret and Editora types over it; type an opener with a selection and it wraps the selection; Backspace inside an empty pair deletes both halves.

Quotes aren't auto-paired next to a word character, so the apostrophe in `don't` is left alone. And whenever the caret sits next to a bracket, both it and its match are highlighted.
"""),
    new Feature("comment-toggling", ED, 4, false,
        "Comment toggling",
        "<kbd>M-;</kbd> comments or uncomments the line or selection using the language's syntax (<code>//</code>, <code>#</code>, <code>&lt;!-- --&gt;</code>, <code>/* */</code>, <code>--</code>, …).",
        """
`M-;` (Emacs comment-dwim) toggles comments using the language's own syntax. A single line toggles a line comment; a multi-line selection toggles a block/region comment, `//` and `/* */` for Java and C-likes, `#` for Python/shell/YAML, `<!-- -->` for XML/HTML/Markdown, `--` for SQL, and so on.

It preserves indentation, falls back gracefully for line-only or block-only languages, and is a no-op for languages without comments.
"""),
    new Feature("spell-checking", ED, 5, false,
        "Spell checking",
        "Red wavy underlines with right-click suggestions, Add-to-Dictionary, and Ignore: full text for prose, comments &amp; strings for code. Pure-Java Hunspell; English, Spanish, French.",
        """
Misspelled words get a red wavy underline; right-click for **suggestions** (click one to replace), **Add to Dictionary**, or **Ignore**. In source files only comments and string literals are checked (identifiers aren't flagged); plaintext and Markdown are checked in full.

It's powered by Apache Lucene's pure-Java **Hunspell** engine, no native dependency. Ships **English (en_US, en_GB)**, **Spanish**, and **French**; pick a dictionary per file with *Spell Check: Set Language…*, or set a default in Settings. Your added words live in `dictionary.txt`.
"""),
    new Feature("editorconfig", ED, 6, false,
        "EditorConfig",
        "Honors a project's <code>.editorconfig</code>: indent style/size, tab width, end-of-line, charset, max line length, and on-save trim / final-newline. On by default.",
        """
Editora reads a project's `.editorconfig` so your files follow the project's conventions automatically. On opening a file it resolves the nearest config, walking up the directory tree to a `root = true` file, with the closest directory winning.

It honors the common keys:

- **`indent_style`** / **`indent_size`** / **`tab_width`**: Tab and Enter follow them.
- **`end_of_line`** and **`charset`** (utf-8, utf-8-bom, latin1, utf-16le/be), round-tripped on read and save.
- **`max_line_length`**, which drives the column ruler.
- On save, **`trim_trailing_whitespace`** and **`insert_final_newline`**.

It's **on by default**; toggle it in Settings → Editor or with *View: Toggle EditorConfig*. Without an `.editorconfig`, a global **Indent style** preference (Detect / Spaces / Tabs) still applies.
"""),
    new Feature("undo-history", ED, 7, false,
        "Undo History",
        "An in-session timeline of document checkpoints (one per typing burst). Jump back to any recent state from a filterable popup (<kbd>M-g v</kbd>) or the tool window (<kbd>M-g u</kbd>).",
        """
Editora keeps an in-session **timeline of checkpoints** as you edit, one per typing burst, finer-grained than save-based [local file history](/docs/workspace#local-file-history).

- The **popup** (`undoHistory.jump`, `M-g v`) lists the active buffer's checkpoints, each with a caret-line preview and capture time, and filters as you type. Pick one to jump back to that state (a single undoable restore). It's the fast, keyboard-driven path.
- The **Undo History tool window** (`M-g u`) shows the same timeline; double-click or Enter to jump back.

It's session-only and disabled for very large files. The tool-window stripe is off by default (the popup is the primary entry point); enable it in Settings → Tool Windows if you want it docked. This complements the **word/line-level undo** granularity, where one `C-z` undoes a word or line rather than a whole burst.
"""),
    new Feature("syntax-highlighting", CI, 1, false,
        "Syntax highlighting",
        "TextMate grammars (via tm4e) for 21 languages: Java, Python, Rust, Go, Kotlin, C/C++, C#, Ruby, SQL, Markdown, and more.",
        """
Highlighting uses **TextMate grammars** (via tm4e) for 21 languages (Java, XML, shell, PowerShell, DOS batch, Python, Groovy, Kotlin, Ruby, C, C++, Rust, Go, C#, Markdown, JSON, CSS, HTML, YAML, INI, and SQL) plus TypeScript/JavaScript, PHP, Lua, Dockerfile, Terraform, TOML, and more added alongside their language servers.

Tokenization is **stateful** (it carries grammar state across lines, so block comments and heredocs highlight correctly) and **incremental**, an edit re-tokenizes only from the changed line, off the UI thread. Token colors are themed per editor theme.

## Bracket-pair colorization

Each `()`, `[]` and `{}` is tinted by how deeply it is nested, so "how far in am I?" is readable without counting. It answers a different question from the matching-bracket highlight — which is "where does *this* one close?" — and the two combine on the same character.

Brackets inside strings and comments are skipped. That is not a nicety: a stray `{` in a string would otherwise shift the colour of every bracket below it, which reads as the feature being broken rather than as one bracket being wrong. The depth pass rides the tokenize that was happening anyway, so it costs no extra repaint. On by default; Settings → Editor turns it off.
"""),
    new Feature("lsp", CI, 2, true,
        "Language servers (LSP)",
        "Go-to-definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics and completions via 22 language servers (Java, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled.",
        """
Editora speaks the **Language Server Protocol**, both halves of it: the requests that read your code, and the ones that change it.

- **Go to definition**: `M-.`, or Ctrl/Cmd-click a symbol, and it works from inside an opened JDK or dependency source tab too
- **Go to implementation / type definition / declaration**, offered only where the server supports them
- **Find references**: `M-?`, listed in a browsable **References** tool window
- **Call and type hierarchy** in a **Hierarchy** tool window, each level fetched as you expand it
- **Go to Symbol in Workspace**: search any symbol across the project and jump
- **Code actions and quick fixes**: `Ctrl-.` / `Cmd-.`, opening at the caret with the server's preferred fix preselected, including organize imports and extract/inline refactorings
- **Java code generation** from the same menu: toString(), hashCode()/equals(), constructors, and override/implement methods, each with a checkbox picker
- **Re-indent as you type** (`;`, `}`, Enter snap the line to the server's convention — indentation only, off by default) and a **whole-project Problems** view with a Build Project command
- **Rename symbol**: `F2`, across the whole workspace, moving a public Java class's file with it — and showing you every affected file first, with its change count, so you can untick any of them before applying
- **Pasted Java code imports itself**, and a `;` typed mid-expression moves to the end of the statement
- **Signature help** as you type `(` or `,`, with the current parameter highlighted
- **Inlay hints** (off by default), **occurrence highlighting**, and **hover docs** (`C-c h`)
- **Format Document**: reformat the whole file via the server (palette or right-click)
- Server-provided **folding regions** and **expand/shrink selection**
- Inline **diagnostics** (with a Problems tool window and minimap/scrollbar marks) and **completions**

Twenty-two servers are supported, Java, TypeScript/JavaScript, Python, Go, Rust, C/C++, C#, Ruby, PHP, Kotlin, HTML, CSS, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, Dockerfile and Typst (tinymist). Servers are **auto-detected on your PATH, never bundled** (and configurable in Settings → LSP). A [project can commit](/features/projects) which server it wants and whether to run it, so a repository needing a different JDK doesn't mean flipping a global preference every time you switch.

Document sync is incremental, semantic highlighting transfers only what changed where the server supports token deltas, a crashed server restarts itself, and a server shuts down a few minutes after its last file closes.

**One-click install** covers every server: an **Install…** button per server in Settings, an in-editor banner when a file's server is missing, and the **Install: Language Server…** picker. Editora fetches each via the right channel (npm, the language's own toolchain, or a per-OS binary release), and the server activates without a restart.

Off by default. Enable it under Settings → LSP.
"""),
    new Feature("autocomplete", CI, 3, false,
        "Autocomplete",
        "As-you-type completion: a popup for code (LSP + snippets) and inline ghost text for prose. Trigger with <kbd>C-M-i</kbd> / <kbd>M-/</kbd>.",
        """
Completion appears as you type, debounced and off the hot path.

**Code** buffers get a popup that merges LSP results and snippets, Enter or Tab to accept. Accepting a snippet starts a full tab-stop session; accepting an LSP item can auto-add its import.

**Prose** buffers get inline **ghost text**, a muted suffix you accept with Tab.

Trigger manually with `C-M-i` or `M-/`. Per-source toggles (words, snippets) live in Settings → Editor.
"""),
    new Feature("run-files", RD, 1, false,
        "Run files & main classes",
        "Run a script, or a Maven/Gradle project's <code>main</code> class, from a gutter ▶. Output and <code>stdin</code> go to the Run console, with clickable stack traces and saved run configurations.",
        """
A green ▶ in the gutter runs the current file, a **Java compact source file** (JEP 512), a **Python** script, or a **shell** script.

It also runs a **project's main class**. *Run Main Class…* picks any `main` in the active file's **Maven or Gradle** project, and a ▶ sits beside every `public static void main` (the right-click menu offers *Run '….main()'*). With the Java language server set up, Editora asks it for the main classes and the resolved classpath; without it, Run falls back to the build tool, with Maven resolving the classpath and Gradle delegating to `run` or `bootRun`.

For anything you run more than once, save it as a [run configuration](/features/run-configurations) instead.

Output streams to the Run console, which also accepts **stdin** so `readln`-style programs work, and **stack-trace lines are clickable** (Java, Python, and Node frames) to jump to the file and line. Pass per-file **program arguments** (remembered across runs), and re-run the last file with *Rerun Last Run*. Bind it to `C-c r` or run from the palette.
"""),
    new Feature("run-configurations", RD, 2, false,
        "Run configurations",
        "Save how a thing is launched — Java main class, Python or shell script, or a make target — with a before-launch build step, a toolbar selector, and a file you can commit so your team gets the same ones.",
        """
A run configuration is a saved answer to "how is this launched": the main class or script, program and VM arguments, environment variables and a working directory. Pick one in the toolbar and hit **Run** or **Debug**, with **Stop** beside them; the choice is remembered across restarts.

## Four kinds, not just Java

Choose a **Type** in Settings → Run Configurations:

- a **Java main class** (resolved through the language server, or through your Maven/Gradle build)
- a **Python script**
- a **shell script**
- a **make target**

Script configurations need no project and no language server at all. Debugging remains Java-only, and says so rather than reporting a confusing Java error.

## A step before the launch

A configuration can name a command to run first — a build, a codegen step. A **non-zero exit aborts the launch**, so a stale binary is never run by accident.

## Shareable

**Export Configurations to Project** writes them to `.editora/run-configurations.json` inside the project, where they can be committed alongside [per-project settings](/features/projects). **Import** merges them back **by name**, so importing twice doesn't duplicate and a colleague's edit updates the configuration rather than doubling it.

## It stays out of your way

- Each configuration **becomes a real command**, so it appears in the palette by name and can be given its own keyboard shortcut, the same way [saved macros](/features/macros) and [external tools](/features/external-tools) already work.
- **Add** prefills from the file you are looking at: the main class from the active Java file (or the one your Gradle build declares), the name from that class, and the cursor in whichever field still needs you.
- The toolbar group **only appears where you could actually launch something**, so a project of Markdown notes doesn't carry a dropdown that can never fill. Anything you have already saved keeps it visible regardless.
- Running an incomplete configuration **opens its form** at the field you need to fill in, rather than naming the problem and leaving you to find it.
"""),
    new Feature("debugging", RD, 3, true,
        "Debugging (DAP)",
        "Full debugging for Java, Python, and JavaScript: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, and an interactive console.",
        """
Full debugging for **Java**, **Python**, and **JavaScript** through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window:

- Breakpoints (including conditional and logpoints), step in / over / out, **run-to-cursor**, and **jump-to-line**
- A threads + call-stack view and a lazy variables tree with **set-value**
- **Watches** and an evaluate console

For Java it goes beyond single files: *Debug Main Class…* debugs any `main` in the active file's Maven or Gradle project (with saved run configurations carrying program and VM arguments and environment variables), and *Debug via Build Tool* launches a Gradle or Spring Boot app under a suspended JVM and attaches when it is listening.

While suspended, **inline values** appear after each line and hovering a variable shows its value. The adapters (java-debug, debugpy, vscode-js-debug) are user-installed, not bundled, and a `jdtls` that already bundles java-debug is detected as-is. Off by default. Enable it under Settings → Debugging.
"""),
    new Feature("http-client", RD, 4, true,
        "HTTP client",
        "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶, with environments, variables, request chaining, and a formatted response view. Built on the JDK HTTP client.",
        """
Open a `.http` or `.rest` file and click the green ▶ next to a request to send it, no external tool, it uses the JDK's built-in HTTP client.

Define multiple requests separated by `###` and the feature reaches for IntelliJ-style parity: `{{var}}` / `@var` substitution, dynamic variables (`{{$random.*}}`, `{{$datetime}}` with date math, `{{$dotenv.X}}`), **request chaining** that references an earlier response, **multipart** and external-file bodies, **environment files** (`http-client.env.json` with a `$shared` section) and a picker, and Basic/Digest auth shorthand.

The response is the `.http` file's own **preview**, in the same Editor / Split / Preview view every other rich file type uses, so it sits beside the request that produced it (and the view mode is remembered per file). It shows status, headers, timing, and a pretty-printed, content-type-highlighted body, with **Copy as cURL** / **Import cURL**, open-in-editor, and Save-response. Run one request or the whole file. Off by default. Enable it under Settings → HTTP Client.
"""),
    new Feature("build-tools", RD, 5, false,
        "Build tools",
        "Maven, npm, Cargo, Go, and Gradle each get an IntelliJ-style tasks tool window and a streaming console.",
        """
Each detected build tool gets its own **tasks tool window** (its stripe appears when the tool's marker file is found): a browsable tree of the tool's goals, scripts, or targets with a mini toolbar (Run / Reload / Stop / Run custom…). Double-click or Enter runs a task, and the output streams to a separate per-tool console window. A searchable actions popup is also available from the command palette (*Maven: Show Actions*, and so on).

- **Maven** (`pom.xml`): lifecycle phases, the pom's declared profiles (checkable, composing via `-P`), and each plugin's bound goals, plus a *Run custom…* box. Prefers `./mvnw`, else `mvn`.
- **npm** (`package.json`): one entry per `scripts` name (run as `<pm> run <name>`) plus `install` / `ci`. Uses the detected package manager (npm/yarn/pnpm/bun).
- **Cargo** (`Cargo.toml`): the standard subcommands, any `[[bin]]` / `[[example]]` targets, and a `--release` toggle.
- **Go** (`go.mod`): the standard subcommands over the whole module.
- **Gradle** (`build.gradle[.kts]`): the common tasks plus *Load all tasks…*. Prefers `./gradlew`.

Discovery parses the marker file directly (no shell-out, no new dependency), so it's instant and offline. **On by default**, each inert until its marker is found. See the [build tools guide](/docs/build-tools).
"""),
    new Feature("git", GD, 1, true,
        "Git integration",
        "Native Git: status-bar branch, gutter change bars vs HEAD, a Commit tool window, fetch / pull / push + branches, plus a history/log view, inline blame, and stash.",
        """
Native Git that shells out to your installed `git`, no bundled library.

- The **status bar** shows the current branch with ahead/behind counts and a dropdown to switch/create branches, pull, fetch, and push.
- **Gutter change bars** mark added/modified/deleted lines vs HEAD (hover for the hunk diff).
- The **Commit** tool window groups staged / changed / untracked files with stage, unstage, discard, and a commit box.
- The **Project tree colors files by Git status** (added, modified, deleted, renamed, untracked), IntelliJ-style, with changed folders tinted.
- Plus a **history / log** view, **inline blame**, and **stash**.
- **A transcript of what it ran.** The **Output** console has a **Git** tab holding every `git` command Editora ran on your behalf, with its output, exit code and duration. It logs the ones you asked for (commit, push, pull, checkout, stash, clone…) and deliberately not the `status`/`diff` reads it re-runs on every tab switch, which would bury them. It never steals focus — the transcript is waiting when you open the window.

Off by default. Enable it under Settings → Git.
"""),
    new Feature("github", GD, 2, true,
        "GitHub integration",
        "Review and check out pull requests, submit reviews, open a file on GitHub at the caret line, and jump from a failed CI log straight to the offending line. Uses your own <code>gh</code> CLI, so Editora never handles a token.",
        """
GitHub, through the [`gh` CLI](https://cli.github.com) you already have signed in. **Editora never handles a token**, it shells out to `gh` the same way the Git support shells out to `git`, so GitHub Enterprise works with no extra setup.

- **Review a pull request in the editor.** A *Files changed* tab lists every file with its status and per-file `+` / `−` counts; click one for a read-only diff. The description renders as Markdown above the list, and `n` / `p` step through changes.
- **Submit a review**, approve, request changes, or comment, without leaving the editor.
- **Check out a PR**, **create a PR**, and **open the current file on GitHub** at the caret line.
- A **pull request / issue / Actions-runs tool window**, with one filter box across all three that matches anything a row shows — number, title, author, branch, state, labels — and a leading `#` optional, so `42` and `#42` both find PR 42. It opens with focus in the filter and the first row selected; `C-n` / `C-p` move without leaving the box, Down enters the list, Enter opens. Plus a **status-bar CI checks** indicator for the current branch.
- **A failed CI run's log opens in the Output console with clickable stack frames.** Runner paths are mapped back onto your local checkout, so a red build takes you straight to the line. A **GitHub** tab beside it keeps a transcript of the `gh` commands Editora ran, with their exit codes and durations.

On by default, and completely invisible until `gh` is signed in and the repo actually has an open PR, issue, or workflow run. See the [GitHub guide](/docs/github).
"""),
    new Feature("diff-merge", GD, 3, true,
        "Diff & merge",
        "Side-by-side and unified diff (vs HEAD, a commit, or another file) with word-level highlights and apply-hunk arrows, plus a merge-conflict resolver.",
        """
Compare files in a dedicated tab (**side-by-side** or **unified**) with per-line backgrounds and intra-line **word-level** highlights.

Diff a file against **HEAD** (`C-x v =`), a **commit**, or **another file**, and apply changes hunk-by-hunk with gutter arrows (or apply-all), all undoable. Open diffs **refresh live** when the underlying files change.

When a file has Git conflict markers, the **merge resolver** lists each conflict with Accept Ours / Theirs / Both and writes the result back.
"""),
    new Feature("markdown-preview", DD, 1, false,
        "Markdown preview",
        "IntelliJ-style 3-mode view rendered natively with CommonMark + GFM: task lists, code pills, images, LaTeX math, a heading outline, linting, and Export to HTML. Live and theme-matched.",
        """
An IntelliJ-style 3-mode view (**Editor**, **Editor + Preview** (split), and **Preview**) via a floating control at the top-right of any Markdown file.

It renders **natively** (no WebView) from CommonMark + GFM, GitHub-style: real task-list checkboxes, inline-code pills, underlined headings, tables, and **images** (local and remote, including SVG badges). It updates live as you type, follows the active theme (or its own light/dark toggle), and remembers its mode per file. Extra CommonMark extensions render too: YAML front matter, footnotes, heading anchors, and `++inserted++` text.

Markdown files get a full editing kit:

- **Linting** with a broad markdownlint rule set, shown as inline squiggles, scrollbar/minimap stripes, and a Markdown Lint tool window, with **auto-fix**, per-rule config, inline disable comments, and `.markdownlint.json` discovery.
- **LaTeX math**: inline `$…$` and display `$$…$$` (off by default).
- **Image paste & drag-drop** into a sibling `assets/` folder, and **smart link paste** to wrap a selection.
- **Table editing**: insert a table, add/delete rows and columns, Tab between cells, and reflow; convert to and from **CSV** or export the table to Excel/ODF.
- **Table of contents** and **task-list** insertion, plus a **heading outline** in the Structure tool window.
- **Export** the preview to PDF, **HTML**, **Word (`.docx`)**, or **ODF (`.odt`)**.

Zoom with the −/+ control or Ctrl+wheel; right-click to copy, or **export to PDF, HTML, or print**. See the [Markdown guide](/docs/markdown).
"""),
    new Feature("mermaid", DD, 2, false,
        "Mermaid diagrams",
        "Render Mermaid diagrams inline in Markdown and in standalone <code>.mmd</code> files, with live linting and export to SVG / PNG / PDF.",
        """
Mermaid diagrams render inline. A fenced ` ```mermaid ` block in Markdown becomes a diagram in the preview, and standalone `.mmd` files get the same 3-mode preview.

Rendering uses the `mmdc` CLI (rasterized faithfully and cached per diagram), with **live linting** via `maid` that underlines errors with precise line/column messages as you type. Export a diagram to **SVG / PNG / PDF**.

Off by default. Enable it under Settings → Mermaid (point it at your `mmdc`/`maid`, or use `npx`).
"""),
    new Feature("typst", DD, 5, false,
        "Typst",
        "A 3-mode preview for <code>.typ</code> files rendered by the Typst CLI, a tinymist language server, Markdown-style editing, and export to PDF / PNG / SVG.",
        """
Standalone `.typ` files get the same 3-mode view (Editor / Split / Preview) as Markdown, rendered off-thread by the external **`typst`** CLI as a **multi-page** stack. The last good render stays on screen while you edit (no flicker), and a compile error keeps the pages visible under a small banner.

- **Editing** has Markdown-style ergonomics: Enter continues a `-` / `+` / `N.` list, and selecting text pops a format bar (bold, emphasis, raw, link, bullet, heading) with matching right-click and palette actions. Bundled snippets cover figures, tables, and more.
- **Code intelligence** comes from the **tinymist** language server.
- **Export** to PDF (a native single file), PNG, or SVG (`typst.export`); print paginates the pages.

**On by default**, self-gating on detection, so it's inert until `typst` is found. Install it with your package manager (`brew install typst`, `cargo install typst-cli`) or the in-app **Install…** button. See the [Typst guide](/docs/typst).
"""),
    new Feature("diagrams", DD, 6, false,
        "Diagrams as code",
        "A 3-mode preview for Graphviz DOT (<code>.dot</code>/<code>.gv</code>) and PlantUML (<code>.puml</code>) files via the <code>dot</code> / <code>plantuml</code> CLIs, with export to SVG / PNG / PDF.",
        """
Standalone `.dot`/`.gv` (Graphviz) and `.puml`/`.plantuml` files get the same 3-mode preview as Markdown and Mermaid, rendered off-thread via the external **`dot`** and **`plantuml`** CLIs. Both rasterize to PNG natively, so there's no headless browser, and results are cached by source hash. Zoom resizes the image, and you can export a diagram to **SVG / PNG / PDF** (`diagram.export`).

**On by default**, self-gating on detection, so it's inert until the tool is found (install via your package manager, e.g. `brew install graphviz plantuml`). Tool paths live under Settings → Languages & Tools → Diagrams.
"""),
    new Feature("previews", DD, 7, false,
        "Smart file previews",
        "Open a data or config file and get a rendered or plain-English preview: JSON/YAML/TOML/XML trees, OpenAPI docs, and decoded systemd, ssh, Dockerfile, fstab, crontab, and GitHub Actions files.",
        """
Many file types get the same 3-mode preview as Markdown, turning raw config and data into something readable.

- **Structured data**: `.json` / `.yaml` / `.toml` render a collapsible, type-colored **data tree**, and `.xml` renders a faithful DOM tree. A JSON/YAML file recognized as an **OpenAPI 3 / Swagger 2** spec instead renders as browsable **API docs** (endpoints, method badges, params, responses, schemas), with a tree ⇄ docs toggle.
- **Config files, decoded to plain English**: **crontab** (`30 2 * * 1-5` becomes "At 02:30, Monday through Friday", with next fire times), **fstab** mounts, **systemd** units (with `OnCalendar=` next triggers), **SSH config** (a one-line connection summary per host), **Dockerfile** (a per-stage digest), and **GitHub Actions** workflows (triggers and jobs). Malformed lines are flagged.
- **Viewers**: `.pdf` files open in a read-only page viewer, `.svg` files stay editable XML but gain a live rendered-image preview, and binaries open as a hex dump.

All on by default, each toggled under Settings → Editor. See the [previews guide](/docs/previews).
"""),
    new Feature("export-pdf-print", DD, 3, false,
        "Export & print",
        "Export code or the Markdown preview to a syntax-highlighted PDF, HTML, MS Word, or ODF, or print with a preview. Light-themed and generated off-thread.",
        """
Export **code** to a syntax-highlighted PDF (with optional line numbers), or the **Markdown / Mermaid preview** to a richly-formatted PDF, headings, lists, tables, code blocks, and images rendered as native vector text.

The Markdown preview also exports to **standalone HTML**, **MS Word (`.docx`)**, and **OpenDocument Text (`.odt`)**, embedding tables, code, math, Mermaid diagrams, and images.

Or **print** either, with a page-by-page preview first (what you preview is what prints). Output is always light-themed and generated off the UI thread, via Apache PDFBox / Apache POI / `javafx.print`. Page size and options live in Settings → Editor → Export & Print.
"""),
    new Feature("html-live-preview", DD, 4, true,
        "HTML live preview",
        "Click the globe on any HTML file to open it in a detected browser (Safari, Chrome, Firefox, Edge…), served over a loopback web server with live-as-you-type reload. Sibling CSS, JS, and images included.",
        """
A floating browser-globe button on any `.html`/`.htm`/`.xhtml` file opens it in a **detected browser**, Safari, Chrome, Firefox, Edge, or the system default.

The file is served over a tiny embedded web server bound to **loopback only**, so its sibling CSS, JS, and images load, and a small injected script **reloads the page live as you type** (it serves the buffer's in-memory text, so you don't have to save). No external tool, it uses the JDK's built-in HTTP server.

Off by default. Enable it under Settings → HTML Preview. Read the [deep-dive](/blog/html-live-preview).
"""),
    new Feature("projects", WF, 1, false,
        "Projects",
        "VS Code single-folder workspaces: a root folder plus its own saved session (open files, layout, folds), shown as a filterable file tree. Scaffold one from a template, and commit settings the whole team gets.",
        """
Projects are VS Code-style single-folder workspaces: a root folder plus its **own saved session**, open files (with carets and pins), the active tab, folds, [editor-group layout](/features/editor-groups) and tool-window layout. Each project opens in its own window.

Open one with `C-x C-p`, switch with `C-x p`, and close to return to the global session. The **Project tool window** shows the tree with keyboard navigation and a filter that runs a bounded project-wide filename search. Bookmarks, notes and [run configurations](/features/run-configurations) are scoped per project. **On by default** since 0.10.0; Settings → Workspace turns it off.

## Start from a template

**New Project From Template** scaffolds a whole project and opens it in its own window, rather than pointing Editora at a folder you made yourself. Pick a multi-file [template](/features/file-templates), fill in its variables, choose where it goes, and the new folder is registered as a project and opened. A **Python Project** template ships with it (package layout, a test, `pyproject.toml`, README and `.gitignore`), and your own multi-file templates appear in the same picker.

## Settings you can commit

A project can carry `.editora/settings.toml` saying **which language server to run and whether to run it**, overriding your global preferences for anyone who opens that project. The case it's for: one repository needs a JDK 17 server and another a JDK 25 one, and nobody should have to remember to flip a global preference between them. **Project: Edit Project Settings…** creates the file with a commented example.

Only toolchain settings can be overridden. Appearance, keymap and fonts stay personal, because checking out a repository should not rearrange somebody's editor.
"""),
    new Feature("starting-a-project", WF, 2, false,
        "Starting a project",
        "Generate a Maven project from an archetype, <code>git init</code> a folder without a terminal, and create any of about fifty file types from the Project tree with the right package declaration already written.",
        """
Editora used to only ever *open* an existing folder, which made it a poor place to begin something.

## New Maven Project

A wizard: pick an archetype, fill in the coordinates, and the generated project is registered and opened in its own window. Reachable from the File menu, the Project tree's **New ▸** submenu, and the palette.

Archetypes come from a curated list that ships with the editor, with **Load full catalog…** to pull Maven Central's when you need something unusual. Where both list the same archetype the curated pin wins, because the published catalog is often years out of date. The package name is derived the way IntelliJ does it, so `my-app` becomes `my_app` and `2048` becomes `_2048`.

The project arrives ready to run: a run configuration is seeded for its main class with `mvn -q compile` as its before-launch step, and that class is opened, so [Run](/features/run-files) works on the first press instead of failing on an empty `target/classes`. Generation shells out to `mvn archetype:generate` rather than writing a pom by hand, so Maven has to be on your `PATH`, which is checked before the wizard opens rather than after five fields have been typed.

## Git: Initialize Repository

Starts version control without dropping to a terminal. It prompts for a folder, defaults to the project root, and brings the [Git](/features/git) UI up the usual way: status bar, Commit window, gutter change bars. Creating a repository inside an existing one is refused, naming the enclosing root, since that is nearly always a mistake and awkward to undo.

## New ▸ on any folder

About fifty file types, grouped by family (Java, Web, Scripts, Languages, Data & Config, Docs & Diagrams, Build & Ops), each carrying the icon the file will have once it exists.

A new Java file takes its package from where you create it, so "New ▸ Class" in `src/main/java/demo` writes `package demo;`, and a qualified name like `text.Slug` creates the sub-package to match. The name you type always wins over the type's extension, so `notes.json` under Text File gives you JSON rather than `notes.json.txt`, and a dotfile stays a dotfile. A name that tries to climb out of the folder creates nothing at all, and an existing file is never overwritten.
"""),
    new Feature("editor-groups", WF, 3, false,
        "Editor groups",
        "Two files on screen at once. Split the editor into independent groups with their own tabs, nest the splits, drag a tab between them, and get the whole arrangement back on the next launch.",
        """
The editor area splits into independent **editor groups**, each with its own tabs and its own selection, so a header can sit beside its implementation or a test beside what it tests.

- **Split Editor Group Right** and **Split Editor Group Down** move the current file into a new group.
- **Move File to Next Editor Group** shifts it along, **Focus Next Editor Group** moves the keyboard between them, and **Merge Editor Groups** puts everything back.
- Closing the last file in a group **collapses it**, so you never end up staring at an empty pane.

All five are in the command palette and bindable like anything else.

## Nesting

Splits nest: a side-by-side pair can hold a stacked pair, so an L-shaped layout is reachable. Splitting the **same** direction twice widens the existing row instead of chaining, which gives you three even columns rather than one column and a shrinking remainder.

## Drag and drop

Drag a tab **onto another group** to move it there, or onto a group's **edge** to split that group and drop the file on that side. A translucent highlight shows where it will land before you let go.

## It comes back

The layout is saved with the [session](/features/projects), so the arrangement you left is what you get on the next launch. A file that has since disappeared no longer leaves a blank pane behind.

## Not the same as Split Editor

The older **Split Editor** commands show two views of the *same* file, for reading one part while editing another. Those work exactly as before, and the two can be combined.
"""),
    new Feature("bookmarks-notes", WF, 4, false,
        "Bookmarks & notes",
        "Line bookmarks (gutter markers, cross-file jump, per-project), plus Personal Notes attached to a word/line/range, stored outside the file, surviving renames, with Markdown bodies.",
        """
Two ways to mark up code.

**Bookmarks** toggle on a line (`C-c m`) with a gutter marker and an optional note; the Bookmarks tool window lists them across files, `C-c ]` / `C-c [` cycle within a file, and `M-g b` is a cross-file picker, reorderable and scoped per project.

**Personal Notes** attach an annotation to a word, line, or range, stored *outside* the file (great for read-only or generated code). They survive edits and renames via content-hash identity and text anchoring, render Markdown, and have their own tool window and `M-g n` picker. See the [deep-dive](/blog/personal-notes-that-survive-edits).
"""),
    new Feature("find-in-files", WF, 5, false,
        "Find in files",
        "Project-wide search and replace with a results panel, plus AceJump to leap the caret to any visible spot by typing a label.",
        """
Project-wide search with a results panel (`C-S-f`): matches grouped by file, with case / regex / whole-word options, and it searches open buffers' unsaved text too. **Replace-in-files** rewrites matches across the project (undoable in open buffers).

In-file find (`C-s` / `C-r`) is incremental with highlight-all and a match count. And **AceJump** (`M-g j`) lets you leap the caret to any visible spot by typing the label that appears over it.
"""),
    new Feature("file-templates", WF, 6, true,
        "File templates",
        "New File From Template: single- or multi-file scaffolds with interactive placeholders (author, date, file name, …).",
        """
**New File From Template** (`C-c C-n`) scaffolds a file (or a whole set of files) from a reusable template, prompting for any variables (author, date, file name, package…) in a small wizard.

Templates use the same `${var}` / `$0` syntax as snippets; bundled ones cover a Java class, an HTML page/bundle, a Markdown doc, and a Python script. Add your own under `~/.editora/templates/`.

A multi-file template can also scaffold a **whole project**: **New Project From Template** writes it to a folder of your choosing, registers that folder as a [project](/features/projects) and opens it in its own window. A **Python Project** template ships with it — package layout, a test, `pyproject.toml`, README and `.gitignore`.
"""),
    new Feature("read-only-view-mode", WF, 7, false,
        "Read-only / View mode",
        "Toggle a buffer read-only to browse without editing; pager-style <kbd>Space</kbd>/<kbd>Backspace</kbd> paging and a Word-style View Mode banner.",
        """
Toggle a buffer read-only with `C-x C-q` so it can't be edited by accident, typing and edit commands are blocked while highlighting, folding, search, and copy keep working.

A file that isn't writable on disk opens read-only automatically, and the per-file state is remembered. A Word-style **View Mode banner** docks above the editor with an *Enable Editing* button (when the file is writable), and while read-only, **Space pages down / Backspace pages up** like a pager.
"""),
    new Feature("remote-sftp", WF, 8, true,
        "Remote files (SFTP)",
        "Browse, edit, search, and save files on a remote host over SSH/SFTP. The project tree, search, bookmarks, and notes all work over the wire.",
        """
Edit files on a remote host over **SSH/SFTP**. *Remote: Connect to SFTP…* mounts the remote folder as the Project tree, and from there editing, syntax highlighting, search, bookmarks, notes, and preview all work over the wire, Save writes straight back.

Saved sites have three surfaces beyond the palette: a **Remote Sites** tool window (`M-g r`) with New / Connect / Remove, a **Settings → Remote** page to manage them, and a quick-connect list on the Welcome page. Picking a site opens the form pre-filled.

Auth supports your default `~/.ssh` keys, a key file, or a password; connections are remembered (without secrets). Off by default; local-only features (running, LSP, Git) are gated off for remote files.
"""),
    new Feature("local-file-history", WF, 9, false,
        "Local file history",
        "IntelliJ-style snapshots of your files over time, on save, auto-save, and before an external reload, so you can diff or restore an earlier version with no Git required.",
        """
Editora quietly snapshots your local files over time, on save, on auto-save, and before it reloads a file that changed outside the editor. It's independent of any version control, so you get a safety net even on files that aren't in Git.

Open a file's timeline from the **File History** tool window (`M-g l`). Each revision shows its date, the reason it was taken, and its size, with the latest tagged *Current*. Double-click one for a diff against the current file, then **restore the whole revision** or use the **apply-chevrons to copy individual fragments** back in (undoable).

It's grown closer to IntelliJ:

- **Named snapshots** with *Put Label* (`history.putLabel`), shown bold in the list.
- A **filter** over the revisions plus a project-wide **Recent Changes** picker (`history.recentChanges`).
- A **folder view**: right-click a folder in the Project tree to list every file under it with history, **deleted files badged**, and restore a revision to recreate a file. Deleting a file in Editora snapshots it first, so an accidental delete is recoverable.

Snapshots are deduped by content and stored gzip-compressed under your config folder, pruned by configurable limits (revisions per file, age, size per project). On by default, local-only, and off in Simple UI mode.
"""),
    new Feature("todo-highlighting", WF, 10, false,
        "TODO highlighting",
        "Configurable regex patterns (TODO, FIXME, and your own) are highlighted in the editor and collected in a TODO tool window, with scrollbar and minimap stripes.",
        """
Editora highlights **TODO / FIXME-style patterns** everywhere they appear, IntelliJ-style, and collects them in a **TODO** tool window (`M-g o`).

- Matches are highlighted inline and listed in the tool window, grouped by file. It scans the open project's tree when a project is open, else the open files; double-click a result to jump.
- Matches also show as **overview stripes** over the scrollbar and on the minimap edge, each in its pattern's color. Click to jump, hover for the line.
- Jump between matches in the active file with `M-g ]` / `M-g [` (`todo.next` / `todo.previous`), wrapping around.
- Patterns are fully configurable in **Settings → Editor → TODO Highlighting**: name, regex, a color picker, case sensitivity, and enabled. TODO and FIXME ship by default.

On by default. Highlighting runs off the UI thread and is debounced; the project scan is lazy. See the [TODO highlighting guide](/docs/todo).
"""),
    new Feature("log-viewer", WF, 11, false,
        "Server log viewer",
        "Open a <code>.log</code> file for severity highlighting, a <code>tail -f</code> Follow toggle, open-at-the-tail for huge logs, and live level + regex filtering.",
        """
`.log` files open in a dedicated log mode built for reading server output.

- **Severity highlighting**: ERROR / WARN / INFO / DEBUG / TRACE, both inline and as a left-edge bar that works even on huge logs. It recognizes Logback/Log4j, `java.util.logging`, syslog, nginx, structured/JSON, zerolog, and access logs.
- **Follow** (`tail -f`): a floating toggle streams new lines as the file grows and auto-scrolls. Very large logs **open at the tail** (read-only at the end).
- **Live filtering**: filter as you type by a level floor and a regex (or a literal substring when it isn't valid regex). A stack trace inherits its record's level so it stays visible.

Logs open in **View mode** (read-only with an *Enable Editing* banner) by default, and follow keeps streaming while read-only. On by default (Settings → Editor → Logs). Commands: `log.toggleFollow`, `log.setLevelFilter`, `log.setRegexFilter`, `log.clearFilter`, `log.viewAsLog`, and `view.toggleLogViewer`. See the [log viewer guide](/docs/log-viewer).
"""),
    new Feature("csv", WF, 12, false,
        "CSV & TSV support",
        "Rainbow per-column coloring, a field readout, and an editable CSV Grid with sort/filter and export to Excel/ODF, plus align/shrink and Markdown-table interop.",
        """
`.csv` and `.tsv` files get first-class, spreadsheet-style tooling.

- **Rainbow columns**: each column is colored distinctly in the editor (cycling every eight), so rows line up at a glance. On by default.
- **Field readout**: the status bar shows *Field N of M* for the caret's column.
- **CSV Grid** tool window: the file as a spreadsheet, with content-fit columns, a filter box, column sort, inconsistent-row highlighting, **editable cells and headers**, and a right-click export to **PDF / Print / Excel (`.xlsx`) / ODF (`.ods`)**.
- **Align / shrink**: *CSV: Align Columns* pads fields so delimiters line up in the editor; *CSV: Shrink Columns* reverses it. Both preserve quoted fields.
- **Markdown interop**: *CSV: Copy as Markdown Table*, and from a Markdown table, export to CSV/Excel/ODF or convert to and from CSV.

It builds on proper CSV/TSV syntax highlighting. Toggle rainbow and the grid in **Settings → Editor → CSV**.
"""),
    new Feature("ai", CE, 0, true,
        "AI assistance",
        "One-shot AI actions (explain, rewrite, commit message, inline completion) and an embedded coding agent over ACP. Anthropic or a local model. Off by default.",
        """
Editora has optional AI, off by default and yours to configure.

**AI actions** call the model directly (streamed):

- **Generate a commit message** from the staged diff into the Commit window.
- **Explain the selection** in a new Markdown buffer.
- **Rewrite the selection** per an instruction, as a single undoable edit.
- **Inline completion**: after a typing pause, a muted one-line ghost suggestion at the caret, accepted with Tab (its own fast model).

**AI Agent** is a chat with an embedded coding agent over the [Agent Client Protocol](https://agentclientprotocol.com), the default being Claude Code's `claude-code-acp` adapter (any ACP agent works). Its reads see your unsaved buffers, and its edits to open files apply as **undoable buffer edits** you review and save, with a permission dialog for each action. The agent is a user-installed external tool, never bundled.

**Provider**: use the **Anthropic API** (key from `ANTHROPIC_API_KEY` or Settings; models configurable) or switch to **Local (OpenAI-compatible)** to run everything against LM Studio, Ollama, or any local server, with no API key. Enable it under Settings → AI. See the [AI guide](/docs/ai).
"""),
    new Feature("themes-fonts", CE, 1, false,
        "Themes & fonts",
        "<strong>Editora Light</strong> and <strong>Editora Dark</strong>, plus 26 more (Primer, Nord, Cupertino, Dracula and the community set). Five bundled monospace fonts, no install needed, and you can drop in a theme of your own.",
        """
**Editora Light** and **Editora Dark** are the app's own pair, drawn from the palette in its icon: a teal accent, an ink-navy ground, and a periwinkle reserved for one thing — a keybinding. They are what a fresh install starts in.

Twenty-eight themes ship in total: the Editora pair, **Primer**, **Nord** and **Cupertino** (each light and dark), **Dracula**, and a community set of nineteen (Army, Autumn, Blacky, Blue, Browny, Fall, Navy, News, Spring, Summer, Winter, Yacht).

Each one themes the syntax tokens, the editor surface, the gutter and the project tree together. The **editor** theme follows the app theme until you pick one explicitly, after which the two are independent.

## One vocabulary for state

Colour means the same thing everywhere. **Amber** is "not saved yet" — the tab, the Switcher, the file tree, the pickers — and it follows the theme rather than being one fixed value that only suited a light background. **Red** is broken, **green** is verified, **olive** and **violet** are git's untracked and renamed, and **periwinkle** is only ever a keybinding.

## Fonts

Five monospace fonts ship with the app (**JetBrains Mono**, **Cascadia Code**, **Fira Code**, **IBM Plex Mono** and **Source Code Pro**), so nothing needs installing, and there is per-editor **text zoom**. The interface itself is set in **Inter** on every platform, so Editora looks the same wherever you run it.

## Bring your own

Drop a stylesheet into `themes/` in your config directory for a full app theme, or into `editor-themes/` for syntax colours only. It appears in the picker under its filename, and **Reload User Themes** re-scans without a restart.
"""),
    new Feature("plugins", CE, 2, false,
        "Plugins",
        "Extend the editor with commands, tool windows, and integrations. Install from a built-in registry of 19 plugins, or write your own (Java or a simple manifest).",
        """
Extend Editora without forking it. A plugin adds commands, keybindings, tool windows, editor-menu items, and status-bar segments, written in Java against a small exported API, or declared in a simple `plugin.json` manifest (with snippets and templates folders).

Install from the built-in **registry** of 19 plugins (*Browse plugins…*), or from a `.zip` on disk; downloads are sha-256-verified and the registry index is Ed25519-signed. See the [plugins catalog](/plugins) and the [docs](/docs/plugins).

Off by default. And plugins aren't sandboxed, so only install ones you trust.
"""),
    new Feature("simple-ui-mode", CE, 3, false,
        "Simple UI mode",
        "One toggle strips the editor to the essentials, hiding the extra toolbar groups, tool-window stripe, breadcrumb, gutter, and minimap for a calm, minimal surface.",
        """
One toggle strips the editor to the essentials (hiding the extra toolbar groups, the tool-window stripe, the breadcrumb, the **entire gutter** (line numbers, fold chevrons, and all markers), and the minimap) and turns off the heavier features (LSP, debugging, Git, multiple cursors) for a calm, minimal surface.

Toggle it from Settings → Application, the toolbar, the palette, or the `--simple` CLI flag (session-only). Toggling off restores everything exactly.
"""),
    new Feature("localized-ui", CE, 4, false,
        "Localized UI",
        "The whole interface is translated into English, Italian, Spanish, French, Portuguese, and German, selectable in Settings.",
        """
Editora's entire interface is translated (**English, Italian, Spanish, French, Portuguese, and German**) covering the command palette, toolbar tooltips, tool windows, Settings, the status bar, dialogs, and menus.

Pick a language in Settings → Appearance (or let it follow your OS locale); the choice applies on restart. A key-parity test keeps every translation complete.
"""),
    new Feature("mcp", CE, 5, true,
        "MCP server",
        "Embed a Model Context Protocol server in the running editor so an LLM agent (Claude Code, …) can observe live state and drive the command registry. Loopback-only, token-authed, off by default.",
        """
Editora can run a small **Model Context Protocol** server inside the editor, so an LLM agent like Claude Code can see what you're working on and act through Editora's own commands.

It's a **loopback-only** HTTP/JSON-RPC server with **bearer-token auth**, exposing fourteen tools:

- **Reads**: `list_open_files`, `list_tabs`, `read_buffer`, `get_selection`, `get_diagnostics`, `document_symbols`, `git_status`, `todo_scan`, `find_in_files`, `list_commands`.
- **Writes**: `edit_buffer` (undoable str-replace edits) and `save_buffer`.
- **Actions**: `open_file` and `execute_command`.

So an agent can observe live state, make undoable edits, and drive the editor. The endpoint is written to `mcp-endpoint.json` in your config folder for discovery, and a status-bar **MCP** indicator shows when it's running (click to copy the connection command). It uses the JDK's built-in HTTP server, so there's no new dependency.

It's **off by default** and guarded by a security-notice dialog. Enable it under Settings → MCP Server, or with the **Toggle MCP Server** command.
"""),
    new Feature("external-tools", CE, 6, false,
        "External tools",
        "Define your own CLI commands and run them on the current file or buffer, with <code>$Name$</code> macros, stdin piping, and output to a console or back into the text. IntelliJ-style.",
        """
Define your own command-line tools in **Settings → External Tools** and run them on the current file or buffer, IntelliJ-style.

- Command and arguments support `$Name$` macros: `$FilePath$`, `$FileDir$`, `$FileName$`, `$FileNameWithoutExtension$`, `$SelectedText$`, `$LineNumber$`, `$ColumnNumber$`, `$ProjectFileDir$`.
- A tool can pipe the **selection** or the **whole buffer** to the command's stdin.
- Each tool chooses what to do with the output: show it in a read-only **console**, **replace the selection**, **replace the whole buffer** (undoable), or **insert at the caret**. That covers both "run and see the output" and text transforms with filters like `jq`, `sort`, or `sed`.

Every tool you define becomes its own palette command (and is bindable to a key), plus there's **External Tools: Run…** (a picker) and **Rerun Last**. Tools run off the UI thread with a timeout. Available by default (the list starts empty) and off in Simple UI mode. See the [external tools guide](/docs/external-tools).
"""),
    new Feature("doctor", CE, 7, false,
        "Doctor",
        "A health screen for every external tool Editora can use: what was found, which version, where it lives, what's only half-configured, and what's missing, with an <strong>Install…</strong> button where Editora can fetch it for you.",
        """
Editora leans on the tools already on your machine rather than bundling its own copies, so **Doctor** tells you exactly what it found. Run it from the palette with `view.doctor`, or from the Welcome page.

Every integration gets a row, grouped by area:

- **Version control**: `git`, and `gh` for pull requests, issues, and CI runs.
- **Search**: `ripgrep`, which accelerates Find in Files (the built-in walker is used when it's absent).
- **Language servers**: one row per enabled server, showing the resolved path.
- **Debugging**: the Java debug plugin, `debugpy`, and `js-debug`.
- **Preview & diagrams**: `mmdc` and `maid` for Mermaid, `dot` and `plantuml`, and `typst`.
- **Run & build**: the JDK used to run a file, plus `python3`, `bash`, `make`, and the build tools.
- **System**: the browsers available for HTML preview, and the installer prerequisites.

Each row is green (found, with its version and path), amber (found but not usable yet, like a `gh` that isn't signed in or a JDK too old to run a file), red (missing), or grey (the feature is switched off, so nothing is probed). A missing tool that Editora knows how to fetch gets an **Install…** button right in the row, and a **Settings…** link jumps to the page that configures it.

Probes run fresh each time, off the UI thread, so Doctor reflects the machine as it is now rather than a cached answer from startup, and the results re-check live after an install — no restart.
""")
);

String yaml(String s) {
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
}

Path outputDir() {
    Path a = Path.of("src/content/features");
    if (Files.isDirectory(a)) return a;
    Path b = Path.of("..", "src", "content", "features");
    if (Files.isDirectory(b)) return b;
    return a; // will be created relative to CWD
}

void main() throws IOException {
    Path dir = outputDir();
    Files.createDirectories(dir);
    for (Feature f : FEATURES) {
        String fm = "---\n"
            + "title: " + yaml(f.title()) + "\n"
            + "group: " + yaml(f.group()) + "\n"
            + "order: " + f.order() + "\n"
            + "beta: " + f.beta() + "\n"
            + "summary: " + yaml(f.summary()) + "\n"
            + "---\n\n";
        Files.writeString(dir.resolve(f.slug() + ".md"),
            fm + f.body().strip() + "\n", StandardCharsets.UTF_8);
    }
    IO.println("Wrote " + FEATURES.size() + " feature pages to " + dir.toAbsolutePath());
}

