/// Generator for the feature pages (src/content/features/*.md).
/// A JDK 25 compact source file (JEP 512): run with `java scripts/GenFeatures.java`
/// from the repo root (or `java GenFeatures.java` from scripts/). This file is the
/// source of truth: edit the content here, because re-running overwrites every
/// feature page and discards edits made directly in the Markdown.
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

// summary is HTML (the card text); description is plain text of 160 characters or fewer, no HTML
// entities: the page's meta description for search results and link previews.
record Feature(String slug, String group, int order, boolean beta,
               String title, String summary, String description, String body) {
    Feature {
        if (description.length() > 160 || description.contains("&") || description.contains("<")) {
            throw new IllegalArgumentException("Bad description for " + slug + ": plain text, 160 characters at most");
        }
    }
}

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
        "Every action is a registered command, bound to a chord or one <kbd>M-x</kbd> search away. There are 1,600+ commands and no hidden actions.",
        "Every Editora action is a registered command with an id, title and description: 1,600+ of them, one M-x search away, bindable to any key.",
        """
Editora has no hidden actions. Every capability (save, toggle a bookmark, start the debugger, switch a theme) is a registered command with an id and a title. Four things are built on that registry:

- The **command palette** (`M-x`) fuzzy-searches all 1,600+ commands, each with a one-line description. A command is also found by the first part of its id, so "markdown table" finds the `Table: …` commands.
- **Keybindings** are a map from a chord to a command id, so anything can be bound, or rebound.
- **Toolbar buttons** dispatch the same commands, so the UI and the keyboard never drift apart.
- The **[menu bar](/features/menu-bar/)** is a curated view of the same registry, so each item shows its live keybinding and can never name an action that doesn't exist.

If you can describe it, you can find it by typing a few letters. In the palette, the docs key (`C-h` in the Emacs keymap; the hint line names it in the others) opens the highlighted command's page in the user guide that ships with Editora, the one **Help ▸ Documentation** opens: the guide for the version you run, readable offline. Browse the full [command list](/commands/).

See the [Navigation guide](/docs/navigation/#command-palette).
"""),
    new Feature("keymaps", KB, 2, false,
        "Keymaps your way",
        "Pick <strong>Emacs</strong>, <strong>CUA</strong>, <strong>Sublime Text</strong>, <strong>VS Code</strong>, or <strong>IntelliJ IDEA</strong>. Switch live with no restart, or rebind any command yourself in the built-in keybinding editor (multi-key chords like <kbd>C-x C-s</kbd> supported).",
        "Five complete keymaps (Emacs, CUA, Sublime Text, VS Code, IntelliJ IDEA), switched live with no restart, plus a keybinding editor for multi-key chords.",
        """
Bring the keys your hands know. Editora ships five complete keymaps, Emacs by default, plus CUA, Sublime Text, VS Code and IntelliJ IDEA, and every one reaches every command.

## Pick a keymap

- **Switch live**: choose one in **Settings → Keymaps** or with **Keymap: Select…**, with no restart.
- **Nothing stranded**: each keymap binds the same commands, so switching changes the keys, not what you can do.
- **Debugger keys everywhere**: every keymap binds the debugger. CUA and Sublime Text use VS Code's function keys, where `F11` steps into while a program is paused and switches full screen otherwise.
- **Familiar code chords**: the VS Code keymap keeps VS Code's own, such as `Shift+Alt+O` to organize imports and `Ctrl+Shift+Space` for parameter hints.
- **macOS**: the non-Emacs keymaps use ⌘ wherever the [keybindings reference](/keybindings/) shows Ctrl.

## Make it yours

- **Record any chord**: the keybinding editor records multi-key chords like `C-x C-s` in the active keymap's notation.
- **Rebind, remove or reset** any command, and find a command by name or by typing a chord in any notation.
- **Your keys travel**: shortcuts for macros, external tools and run configurations follow you to another keymap.
- **Only what you change**: your overrides are saved in `settings.json` on top of the active keymap.

See the [Keymaps guide](/docs/keymaps/#the-five-keymaps).
"""),
    new Feature("search-everywhere", KB, 3, false,
        "Search Everywhere",
        "One picker over <strong>commands, files and symbols</strong>. Type the name of the thing instead of first choosing which finder it lives in. A <code>&gt;</code>, <code>#</code> or <code>@</code> prefix narrows it when you already know.",
        "One picker over commands, project files and symbols, grouped by source. A > (commands), # (files) or @ (symbols) prefix narrows it to one kind.",
        """
Every other picker asks you to decide what kind of thing you want before you can type its name. Search Everywhere skips that step: type the name, and commands, project files and symbols are searched together.

- **Open it**: `M-S-x` in the Emacs keymap, `Ctrl+Shift+E` in the others (`Cmd+Shift+E` on macOS).
- **Narrow when you know**: start with `>` for commands only, `#` for files only, or `@` for symbols only.
- **Grouped results**: commands, files and symbols each keep their own group and a fair share of rows, so thousands of symbols never push the one command you wanted off the list.
- **Best group first**: groups are ordered by their best match, not by how many matches they have.
- **Teaches like the palette**: an empty query lists every command, a switched-off feature's command is greyed with the setting that enables it, and the highlighted row's description sits under the list.
- **No server needed**: symbols come from Editora's own [project symbol index](/features/code-navigation/), so they work with no language server installed.
- **One chord for all of it**: **Palette shortcut opens Search Everywhere** in **Settings → Interface → Pickers** gives the palette's chord to Search Everywhere.

In a [remote project](/features/remote-sftp/) it lists files from the host and has no symbols.

See the [Navigation guide](/docs/navigation/#search-everywhere).
"""),
    new Feature("jump-to-popups", KB, 4, false,
        "Jump-to popups",
        "Fuzzy-jump to recent files, symbols, open tabs, and tool windows, plus an Emacs <code>find-file</code>-style path finder.",
        "Fuzzy pickers for recent files, symbols, open tabs, tool windows, bookmarks and notes, plus an Emacs find-file style path finder.",
        """
Get anywhere in a project without touching the mouse. Each picker opens on a chord, filters as you type, and shows a footer legend of its keys.

- **Recent Files: Jump…** (`C-x C-r` in the Emacs keymap) reopens a file you had open.
- **Structure: Jump…** (`M-g i`) lists the current file's symbols.
- **Open Files: Jump…** (`C-x b`) switches between open tabs.
- **Tool Windows: Jump…** (`M-g t`) opens any tool window by name.
- **Bookmarks: Jump…** (`M-g b`) and **Jump to Note…** (`M-g n`) reach your bookmarks and notes across files.
- **File: Find File…** (`C-x C-f`) is an Emacs-style path finder: Tab completes, Enter goes into a folder or opens a file, creating it if needed, and `C-d` opens a folder as a project.

**Smart ranking**: every picker scores what you typed by contiguity, word and camelCase boundaries and exact case, and bolds the matched characters, so `mcon` finds `MainController` first.

The other keymaps bind several of these in their own editors' style; see the [keybindings](/keybindings/). When you don't know which picker holds something, use [Search Everywhere](/features/search-everywhere/).

See the [Navigation guide](/docs/navigation/#jump-pickers).
"""),
    new Feature("multiple-cursors", KB, 5, false,
        "Multiple cursors",
        "Add a caret at the next occurrence, above/below, on every occurrence or at every selected line's end, split a selection into lines, or <kbd>Alt</kbd>-drag a column/box selection to edit many places at once.",
        "Add carets at the next or every occurrence, above or below, or at line ends, split a selection into lines, or Alt-drag a column selection.",
        """
Change ten places at once instead of making the same edit ten times. Put a caret everywhere the edit belongs, type once, and the edit lands at every caret; `Esc` collapses back to one.

## Add carets

- **Alt-drag** draws a column or box selection with a caret on every line, in every keymap.
- **Edit: Add Caret at Next Occurrence** adds a caret at the next match of the selection.
- **Edit: Add Caret Above** and **Edit: Add Caret Below** stack carets on the neighbouring lines.
- **Edit: Select All Occurrences** puts a caret on every occurrence of the selection, or of the word under the caret.
- **Alt+Enter in the find bar** puts a caret on every match, so the query's case, regex and whole-word toggles decide what gets one.
- **Edit: Add Carets to Line Ends** puts a caret at the end of every selected line.
- **Edit: Split Selection into Lines** turns a selection into one selection per line it covers.

## Shape the set

- **Edit: Skip Occurrence** moves the newest selection on to the next match instead of adding one.
- **Edit: Remove Last Added Caret** takes back the most recent caret, one at a time.

## Edit at every caret

- **Typing, Tab and Shift+Tab** happen at every caret, and the [line commands](/features/line-and-text-tools/) run at each one as a single undo step.
- **Movement fans out**: the arrow keys and `C-f`, `C-b`, `C-n`, `C-p`, `C-a`, `C-e`, `M-f` and `M-b` move every caret. Page and document motions move the main caret only.

In the Emacs keymap the caret commands are in the palette. The VS Code, Sublime Text and IntelliJ IDEA keymaps bind them as those editors do, such as `Ctrl+D` for the next occurrence in VS Code and Sublime Text (`Cmd+D` on macOS); see the [keybindings](/keybindings/).

**On by default.** Turn it off in **Settings → Editor**. See the [Editing guide](/docs/editing/#multiple-cursors).
"""),
    new Feature("macros", KB, 6, false,
        "Keyboard macros",
        "Record a sequence of edits and replay it: in the Emacs keymap, <kbd>F3</kbd> to start, <kbd>F4</kbd> to stop, <kbd>C-x e</kbd> to replay. Name and save macros, and bind them to keys.",
        "Record keyboard macros of commands and typed text, replay them as one undo step, and save named macros that become bindable commands.",
        """
When the same edit has to happen forty times, do it once and let Editora repeat it. A macro records the commands you run and the text you type, and replays them exactly, auto-indent and auto-close included.

## Record and replay

- **Record**: `F3` starts recording and `F4` stops in the Emacs keymap; `Esc` cancels and keeps the previous macro.
- **Replay**: `C-x e` replays the last macro, and **Macro: Replay Last N Times** repeats it as often as you ask.
- **Everything you do counts**: text typed into the find bar, a prompt or a picker is recorded too, so a search replays as a search.
- **One undo step**: a whole replay undoes at once.

## Keep the good ones

- **Macro: Name and Save Last** keeps a macro across sessions.
- **Each saved macro is a command** of its own, so it appears in the palette and takes a shortcut in **Settings → Keymaps**.
- **Macro: Run Saved** and **Macro: Delete Saved** pick from your saved macros.

In the CUA and Sublime Text keymaps `F3` is Find, so CUA starts and stops recording with `Ctrl+Shift+R` (`Cmd+Shift+R` on macOS) and Sublime Text replays with `Ctrl+Shift+Q`. In every keymap the macro commands are in the palette.

See the [Macros guide](/docs/macros/#record-and-replay).
"""),
    new Feature("emacs-heritage", KB, 7, false,
        "Emacs heritage",
        "The Emacs editing <em>model</em>, not just its keybindings: a kill ring and a mark ring, registers, <code>C-x r</code> rectangles, narrowing, query-replace, <kbd>C-u</kbd> prefix arguments, and structural sexp motion. Emacs is the default keymap.",
        "The Emacs editing model in Editora: kill ring, mark ring, registers, C-x r rectangles, narrowing, query-replace, C-u prefix arguments and sexp motion.",
        """
Plenty of editors ship an "Emacs keymap" that maps a handful of chords onto their own commands. Editora builds the model underneath, so the concepts an Emacs user reaches for without thinking are real features, not shortcut aliases. Every chord below is in the Emacs keymap, the default; the same commands are in the palette and bindable in the other four keymaps.

## Kill and yank

The kill ring holds the last 120 kills, consecutive kills join into one entry, and the ring is shared with the system clipboard both ways.

- **Kill**: `C-k` line, `M-d` word, `M-backspace` word backward, `C-S-backspace` whole line, `M-z` zap to char, `C-M-k` sexp.
- **Yank**: `C-y` yanks, and `M-y` cycles back through the ring.

## Mark, rectangles and registers

- **The mark**: `C-space` sets it, `C-x C-space` pops back through the ring, `C-x h` marks the whole buffer. The ring is per buffer, and marks stay on their text as you edit.
- **Rectangles**: the full `C-x r` family works on the columns between point and mark: kill, copy, yank, delete, clear, open, string-rectangle and number-lines, each one undo step.
- **Text registers**: `C-x r s` copies the region into a one-character register, and `C-x r i` inserts it.
- **Position registers**: `C-x r space` saves point, and `C-x r j` jumps back, opening the file if it is closed. The position follows edits to its file.
- **Edit: List Registers** shows them all. Registers last for the session, one set per window.

## Narrow, search and move

- **Narrowing**: `C-x n n`, `C-x n d` and `C-x n f` narrow to the region, the defun or a fold region, and `C-x n w` widens. Search, replace and macros see only the narrowed part; saving writes the whole file. See [Structure narrowing](/features/structure-narrowing/).
- **Incremental search**: `C-s` and `C-r`, repeating the chord to step through matches.
- **Query-replace**: `M-S-5` (Emacs' `M-%`) and `C-M-S-5` for the regexp form, with the usual `y`, `n`, `!`, `.` and `q` answers.
- **Occur**: `M-s o` lists every matching line in the buffer.
- **Sexps and defuns**: `C-M-f` and `C-M-b` move by sexp, `C-M-space` marks one, `C-M-a` and `C-M-e` jump to a defun's start and end.
- **Jump by label**: `M-g j` and `M-g S-l` jump to any visible character or line by typing its label, in the spirit of avy.

## Repeat and record

- **Prefix arguments**: `C-u` repeats the next command, as in `C-u 5 C-n`, or `C-u 40 -` for a row of dashes.
- **Keyboard macros**: `F3` records, `F4` stops, `C-x e` replays, and a saved macro becomes a command you can bind. See [Keyboard macros](/features/macros/).
- **M-x** finds any command by name, with its description and key, and a greyed row says why a command cannot run. See the [command-driven core](/features/command-driven-core/).

## The rest of the muscle memory

- **Transpose**: `C-t`, `M-t` and `C-x C-t`.
- **Fill**: `M-q` fills a paragraph to the fill column, with optional auto-fill.
- **Case**: `M-u`, `M-l` and `M-c`.
- **Whitespace and lines**: `M-\\`, `M-space`, `C-x C-o`, `C-o` and `M-S-6` (Emacs' `M-^`).
- **Comments and abbreviations**: `M-;` comments, `C-x a g` defines an abbreviation and `C-x a e` expands it.
- **Back out**: `C-g` cancels anything.

## What Editora does differently

- **No Elisp.** Editora answers "can it do X?" by [running the tools you have installed](/features/doctor/): your `git`, your `ripgrep`, your language servers. You extend it with [plugins](/features/plugins/) and the command system, so it does not replace a tuned Emacs configuration.
- **Discoverable by default.** Every action has a title and a description, Settings is a real window with checkboxes, and you should not need the manual to find a feature.
- **A default, not a requirement.** Four other complete [keymaps](/features/keymaps/) ship, and any command can be rebound.
- **Modern windows.** Tabs, tool windows, a project tree, drag and drop and a working mouse replace the Emacs buffer and window model.

## Limits of the port

- `C-u` repeats rather than passing a true numeric argument, so a negative argument does not reverse a motion.
- Rectangles count character columns, so a tab counts as one.
- Number, rectangle and window-configuration registers, a global cross-buffer mark ring and dabbrev are not supported; `M-/` opens completion instead.

Browse the [command list](/commands/) or the [keybindings](/keybindings/). See the [Emacs editing model guide](/docs/editing/#the-emacs-editing-model).
"""),
    new Feature("menu-bar", KB, 8, false,
        "A menu bar, over the same commands",
        "<strong>File / Edit / Find / View / Navigate / Code / Run / Git / Tools / Window / Help</strong> menus, built over the command registry, so every item shows its live keybinding. Hide it in one keystroke.",
        "File to Help menus built over the command registry, each item with its live keybinding. On Windows and Linux the menu bar shares the title bar by default.",
        """
The command palette finds anything you can name, but it is hard to browse. The menu bar is for seeing what the editor can do: eleven menus, from **File** to **Help**, each item a registered [command](/features/command-driven-core/), so a menu never names an action that does not exist.

## Browse what Editora can do

- **Live keys**: each item shows its current keybinding and updates when you [switch keymaps](/features/keymaps/).
- **A stable map**: a command whose feature is switched off is greyed rather than removed, so the menus never rearrange themselves.
- **The same icons everywhere**: an item carries the glyph that action has in right-click menus, so Save looks the same wherever you reach it.
- **Short menus**: daily items stay at the top and the rest sit one level down. Git groups its commands by what they act on, Run keeps the debugger in four submenus, and **File ▸ Export** holds the export formats.
- **A curated subset**: Editora has over 1,600 commands, so the menus hold the useful ones and the palette stays the complete index.

## Keep it out of the way

- **From the keyboard**: **View: Focus Menu Bar** (`F10` in the Emacs keymap, `Ctrl+Shift+F10` in CUA, VS Code and Sublime Text) opens the first menu.
- **One row of chrome**: on Linux and Windows the menus share the window's title bar, giving the editor a full row back. Turn off **Merge the menu bar into the title bar** in **Settings → Interface** for the system title bar; it applies after a restart.
- **On macOS** the menus sit in the system menu bar.
- **Hide it** with **View: Toggle Menu Bar**. It hides itself in Zen and Expert mode, and [Simple mode](/features/simple-ui-mode/) keeps a reduced menu bar, because a first-time user is the person most likely to browse.

**On by default.** Turn it off in **Settings → Interface → Window chrome**. See the [Workspace guide](/docs/workspace/#window-chrome).
"""),
    new Feature("snippets", ED, 1, false,
        "Snippets",
        "Type a short prefix, press <kbd>Tab</kbd>, and fill in the blanks. VS Code and TextMate style templates, bundled for 35 languages.",
        "VS Code and TextMate style snippets with tab stops, mirrors, choices and variables, bundled for 35 languages, plus your own in Settings.",
        """
Boilerplate takes a few keystrokes: type a short trigger such as `fori` or `main`, press **Tab**, and fill in the blanks the snippet leaves for you.

## Use a snippet

- **Expand** a trigger with **Tab**, or pick one by name from **Snippet: Insert…** (`C-c i` in the Emacs keymap, `Ctrl+J` in the IntelliJ keymap, `Cmd+J` on macOS).
- **Fill the fields.** Placeholders are selected so you can type over them, **Tab** and **Shift+Tab** move between fields, and repeated fields update together.
- **Choose from a list** where a field offers choices, and land on the final caret position when you finish.
- **Know where you are.** Fields are outlined, and the status bar shows "Snippet 2/3".

## Expands only where you mean it

- **Not in comments or strings**, and not on a member name after a `.`, so Tab indents or completes as usual there.
- **Not mid-sentence in prose.** In Markdown and plain text a word-like trigger expands only alone on its line; the completion popup still offers it.
- **Ahead of other suggestions.** A trigger typed in full wins over a dictionary or AI inline suggestion, and leads the language server's completion list.

## What ships

Snippets ship for 35 languages, plus a set for every file. They use each language's idioms: shell snippets write `[ ]` tests under `sh` or `dash` and `[[ ]]` under bash, and the Java set covers declarations named after the file, loggers, streams, text blocks and JUnit 5 tests.

## Your own snippets

- **Add or change snippets** for any language in **Settings → Snippets**, or in `~/.editora/snippets/<language>.json`. Your snippet replaces a bundled one with the same trigger.
- **Disable** a bundled snippet you never want.

See the [Snippets guide](/docs/snippets-templates/#snippets).
"""),
    new Feature("smart-indentation", ED, 2, false,
        "Smart indentation",
        "<kbd>Enter</kbd> puts the next line where the language expects it, closers line up with their openers, and Backspace clears an indent in one press.",
        "Per-language indentation on Enter and on typing a closer, PEP 8 hanging indents for Python, and a Backspace that clears the indent in one press.",
        """
You spend less time fixing whitespace: each new line starts at the right depth for the language you are writing, and Backspace takes an indent away in one press.

- **Enter keeps the indent** of the current line, and adds a level after a block opener such as `{`, a trailing `:` in Python or YAML, `do` or `then` in shell, or an open tag.
- **Pairs split into a stanza.** Enter between `{` and `}` puts the caret on an indented line with the closer dropped below.
- **Closers line up.** Typing `)`, `]`, `}` or a closing keyword such as `end`, `fi` or `done` moves the line back to its opener.

## In each language

- **Python** follows PEP 8: `else`, `except` and `finally` line up with their `if` or `try`, Enter after `return` or `pass` steps out a level, and an open bracket gives a hanging indent. **Tab** gives a line the indent Enter would.
- **Shell scripts** indent the way shfmt lays them out: `case` bodies indent, `fi`, `done` and `esac` line up, and a here-document's closing word stays at the margin so bash still finds it.
- **Brace languages** such as Java, JavaScript and TypeScript indent a `case` body, the one statement under a brace-less `if`, the next argument of an open call, and a `.` call chain. Enter inside `/** … */` continues it with ` * `.

## Backspace and indent style

- **Smart Backspace** clears a line's whole indent in one press, and on a blank auto-indented line jumps back to the end of the line above. In Python and YAML, where indentation is syntax, it removes one level at a time.
- **Tabs or spaces** are read from the file. A Python file that mixes them the way Python rejects says "mixed indent" in the status bar.

See the [Editing guide](/docs/editing/#auto-indent).
"""),
    new Feature("auto-close-brackets", ED, 3, false,
        "Auto-close & bracket matching",
        "Typing <code>([{</code> or quotes inserts the closer, type-over to skip, wrap a selection, and Backspace clears an empty pair. The bracket beside the caret is highlighted with its match.",
        "Brackets and quotes close as you type, type over their closer, wrap a selection, and delete as a pair. The bracket at the caret shows its match.",
        """
Brackets and quotes take care of themselves: the closer appears as you type the opener, and you never end up with a stray or missing one.

- **Closes as you type.** Typing `(`, `[`, `{`, `"`, `'` or `` ` `` adds the closer and keeps the caret between them.
- **Types over the closer** when it sits next to the caret, so typing it yourself does no harm.
- **Wraps a selection** when you type an opener with text selected.
- **Deletes as a pair.** Backspace inside an empty pair removes both halves.
- **Stays out of the way.** A bracket pairs only where a closer can go, so `(` typed before `foo` gives `(foo`, and the apostrophe in `don't` is left alone.
- **Knows Python strings.** A quote after a prefix such as `f` or `rb` pairs, and the third quote of `\"""` adds the closing triple quote.
- **Shows the match.** When the caret sits next to a bracket, it and its partner are highlighted.

See the [Brackets and quotes guide](/docs/editing/#brackets-and-quotes).
"""),
    new Feature("comment-toggling", ED, 4, false,
        "Comment toggling",
        "One key comments or uncomments the line or selection in the language's own syntax: <code>//</code>, <code>#</code>, <code>&lt;!-- --&gt;</code>, <code>--</code> and the rest.",
        "One key comments or uncomments the line or selection in the language's own syntax, keeps the indentation, and never touches a shebang line.",
        """
Comment out a line or a block with one key, in whatever language you are in, and press it again to bring the code back.

- **One key** for every language: **Edit: Toggle Comment** is `M-;` in the Emacs keymap and `Ctrl+/` in the other keymaps (`Cmd+/` on macOS).
- **The right syntax**: `//` and `/* */` in Java and C-like languages, `#` in Python, shell and YAML, `<!-- -->` in HTML, XML and Markdown, `--` in SQL, and so on.
- **Lines or blocks.** A single line gets a line comment and a multi-line selection a block comment, or whichever form the language has.
- **Tidy markers.** Indentation is kept, and the markers of several lines line up in one column.
- **Scripts stay runnable.** A `#!` shebang line is never commented, and a shell here-document is left alone unless you select the line that opens it.
- **Follows the file.** In an INI file such as `setup.cfg`, the comment character is the one the file uses.

See the [Comments guide](/docs/editing/#comments).
"""),
    new Feature("spell-checking", ED, 5, false,
        "Spell checking",
        "Red wavy underlines in prose, comments and strings, never in your identifiers, with suggestions a right-click away. English, Spanish and French.",
        "Spell checking for prose, comments and strings in English, Spanish and French, with suggestions, a personal dictionary and a technical dictionary.",
        """
Typos get a red wavy underline wherever people will read the text, and your code's identifiers are left alone, so the underlines that remain are worth fixing.

## What is checked

- **Prose** in plain text, Markdown, HTML and Typst is checked in full, apart from code, link targets and markup.
- **Source code** has only its comments and string literals checked.
- **Data and configuration formats** such as JSON, YAML and TOML are off by default. Tick any file type on or off in **Settings → Spell Check → File Types**.

## Fix a word

- **Right-click** a flagged word for suggestions, **Add to Dictionary** or **Ignore**.
- **Work from the keyboard** with **Spell Check: Next Misspelling** and **Spell Check: Correct Word at Caret…** (`M-S-4` in the Emacs keymap). The add and ignore commands are in the palette too.

## Dictionaries

- **English** (US and UK), **Spanish** and **French** ship with the app, with no native library to install. Pick one per file with **Spell Check: Set Language…**, or set the default in **Settings → Spell Check**.
- **Your words** live in `dictionary.txt`, kept across sessions.
- **A technical dictionary** of over 2,000 terms, such as `async`, `kubernetes` and `worktree`, keeps programming words from being flagged, plurals and possessives included.

**On by default.** Turn it off in **Settings → Spell Check**. See the [Spell checking guide](/docs/languages/#spell-checking).
"""),
    new Feature("editorconfig", ED, 6, false,
        "EditorConfig",
        "Honors a project's <code>.editorconfig</code>: indent style/size, tab width, end-of-line, charset, max line length, and on-save trim / final-newline. On by default.",
        "Editora follows a project's .editorconfig: indent style and size, tab width, line endings, charset, max line length, trimming and final newline.",
        """
Editora reads a project's `.editorconfig` so your files follow the project's conventions automatically. On opening a file it resolves the nearest config, walking up the directory tree to a `root = true` file, with the closest directory winning.

It honors the common keys:

- **`indent_style`** / **`indent_size`** / **`tab_width`**: Tab and Enter follow them, and so do new files from templates.
- **`end_of_line`** and **`charset`** (utf-8, utf-8-bom, latin1, utf-16le/be), round-tripped on read and save. An encoding you choose for a file yourself ([Reopen or Save with Encoding](/features/file-encodings/)) takes precedence.
- **`max_line_length`**, which sets the first [ruler](/features/rulers-indent-guides/) (`off` hides them all).
- On save, **`trim_trailing_whitespace`** and **`insert_final_newline`**.

Shell scripts keep LF line endings and no byte-order mark: `end_of_line = crlf` and `charset = utf-8-bom` are not applied to them, because bash cannot run such a script.

Without an `.editorconfig`, a global **Indent style** preference (Detect / Spaces / Tabs) still applies. **View: Toggle EditorConfig Support** turns EditorConfig on or off from the palette.

**On by default.** Turn it off in **Settings → Editor**. See the [EditorConfig guide](/docs/editorconfig/#supported-keys).
"""),
    new Feature("line-and-text-tools", ED, 8, false,
        "Line commands and text tools",
        "Insert, delete, select and indent whole lines at every cursor; sort, filter and de-duplicate lines; encode, decode and escape the selection; increment the number at each cursor.",
        "Line commands that run at every cursor, plus sorting, filtering, case conversion, Base64 and URL encoding, and number increments.",
        """
The small edits you make all day, as commands that work at **every cursor** and undo in one step.

## Line commands

- **Insert Line Below** (`Ctrl+Enter`) opens a new, indented line under the current one wherever the cursor is on it; **Insert Line Above** (`Ctrl+Shift+Enter`) opens one above.
- **Delete Line** (`Ctrl+Shift+K`, `Ctrl+Y` in the IntelliJ keymap) deletes the line, or every line a selection touches, and keeps the cursor's column. It leaves the clipboard and the kill ring alone.
- **Select Line** (`Ctrl+L`) selects the whole line; press it again to take the next one.
- **Indent Lines** / **Outdent Lines** (`Ctrl+]` / `Ctrl+[`) shift the line by the file's indent unit, wherever the cursor sits.
- **Duplicate Line** and **Move Line Up / Down** work on the line or the selected block.

A collapsed fold is opened before a line command changes it, and an ordered Markdown list it changes is numbered in sequence again. The chords above are the VS Code keymap's (`Cmd` on macOS). Sublime Text uses the same ones, CUA shares Insert Line and Delete Line, and IntelliJ binds its own chords for those two. In the default Emacs keymap these line commands run from the palette; every chord is on the [keybindings](/keybindings/) page.

## String manipulation

**Edit: String Manipulation…** (`C-c x` in the Emacs keymap) is one filterable picker over all of these:

- **Case**: camelCase, PascalCase, snake_case, SCREAMING_SNAKE_CASE, kebab-case, dot.case, swap case, and a cycle through the styles.
- **Lines**: sort ascending or descending in natural order (numbers by value, case-insensitive), case-sensitive sort, sort keeping one of each, sort by length, reverse, shuffle, remove duplicates or empty lines, trim trailing whitespace, and **keep** or **remove the lines matching** a regular expression. They act on the selected lines, or on the whole file with nothing selected.
- **Encode and decode the selection**: Base64, URL encoding, JSON string escapes, HTML entities and `\\uXXXX` escapes. With several selections each one is converted, in one undo step; a decode that cannot read one of them changes none.
- **Increment / Decrement Number** change the number at each cursor, or the next one on its line: negatives, zero padding (`007` → `008`) and hexadecimal (`0x0f` → `0x10`) keep their form. In the Emacs keymap they are `C-c =` and `C-c -`, and `C-u 10` steps by ten.

See the [String manipulation guide](/docs/editing/#string-manipulation).
"""),
    new Feature("rulers-indent-guides", ED, 9, false,
        "Rulers and indent guides",
        "Rulers at any columns, with their own set per language, and faint guides at every indentation level, carried through blank lines.",
        "Column rulers set globally or per language, and indent guides at every level that run through blank lines and stay aligned in any font.",
        """
See your line-length limit and which block a line belongs to at a glance, in any file, font or tab size.

## Rulers

- **Any columns.** One ruler at column 80 by default, or as many as you like, such as `80, 120`, set in **Settings → Editor → Display** or with **Editor: Set Rulers…**.
- **Per language.** Give a file type its own columns, such as `java: 100; python: 79, 88`, with **Editor: Set Rulers for This Language…**. A language with no columns shows no rulers.
- **From the project.** An [`.editorconfig`](/features/editorconfig/) `max_line_length` replaces the first ruler, and `off` hides them all.

## Indent guides

- **A line at each level** of indentation, so nested blocks are easy to follow. The guides run through blank lines inside a block, follow the theme's colours, and skip folded lines.
- **Always on their columns**, measured from the laid-out text, so they stay aligned with tabs at any width, in a proportional font, and when the line numbers widen.
- **Cheap at any size.** Their cost depends on the lines on screen, not on the length of the file.

Both hide in [Zen mode](/features/zen-mode/) and [Simple UI mode](/features/simple-ui-mode/), and stay in [Expert mode](/features/expert-mode/). Show or hide them with **View: Toggle Rulers** and **View: Toggle Indent Guides**.

**On by default.** Turn them off in **Settings → Editor → Display**. See the [Rulers and indent guides guide](/docs/configuration/#rulers-and-indent-guides).
"""),
    new Feature("undo-history", ED, 7, false,
        "Undo History",
        "A timeline of the file as you edit it, one checkpoint per burst of typing. Jump back to any recent state from a filterable popup, without pressing undo forty times.",
        "A timeline of in-session checkpoints, one per typing burst. Jump back to any earlier state of the buffer from a popup or a tool window.",
        """
Get back to how the file looked a few minutes ago without pressing undo over and over. Editora keeps a checkpoint for each burst of typing, so you can pick the state you want from a list.

- **Pick a checkpoint** with **Undo History: Pick Checkpoint…** (`M-g v` in the Emacs keymap). Each entry shows the caret line and the time, and the list filters as you type.
- **Undo the jump** if you picked the wrong one: restoring a checkpoint is a single undoable step.
- **Keep it docked** in the **Undo History** tool window (`M-g u` in the Emacs keymap), where double-click or Enter jumps back. Its side-stripe button is off until you turn it on in **Settings → Tool Windows**.
- **Up to 50 checkpoints** per file, within a shared memory budget, kept for the session.

Undo History is finer-grained than [Local History](/features/local-file-history/), which keeps saved versions across sessions. It is off for very large files. The other keymaps run both commands from the palette.

See the [Undo History guide](/docs/undo-history/#the-popup-recommended).
"""),
    new Feature("syntax-highlighting", CI, 1, false,
        "Syntax highlighting",
        "TextMate grammars for dozens of languages, from Java, Python, Rust and Go to Astro, SQL and Markdown, with bracket-pair colours and language detection from a shebang.",
        "Accurate highlighting for dozens of languages that never makes typing wait, with the language found from a shebang or modeline, and bracket-pair colours.",
        """
Accurate colours make code faster to read and mistakes easier to spot. Editora highlights dozens of languages with TextMate grammars, the format VS Code and Sublime Text use, and keeps highlighting in step with every keystroke without slowing your typing.

## The right language for every file

- **Dozens of languages**: Java, Python, Rust, Go, C and C++, C#, Kotlin, Ruby, PHP, TypeScript, Elixir, Dart, SQL, Markdown and many more, plus config formats such as YAML, TOML, INI and Dockerfile.
- **Mixed files** highlight each part in its own language: Astro's frontmatter, HTML and CSS use the official Astro grammar.
- **A file with no telling name** takes its language from a shebang such as `#!/usr/bin/env bash` or a vim or Emacs modeline.
- **The status-bar language picker** sets it by hand, and the language server, Run and the tab's icon follow.
- **Familiar file names** open in the right language without an extension, such as `.bashrc`, `.envrc`, `requirements.txt` and Bazel's `BUILD` files.

## Correct and fast

- **Stateful**: grammar state carries from line to line, so block comments, heredocs and multi-line strings colour correctly.
- **Incremental**: an edit re-tokenizes only from the changed line, in the background, so a large file stays responsive.
- **Themed**: token colours follow the editor theme you pick.

## Bracket-pair colours

Each `()`, `[]` and `{}` is tinted by how deeply it is nested, so "how far in am I?" is readable without counting. The matching-bracket highlight answers "where does *this* one close?", and the two combine on the same character. Brackets inside strings and comments are skipped, so a stray `{` in a string does not shift the colour of every bracket below it.

**On by default.** Turn bracket-pair colours off in **Settings → Editor**. See the [Languages guide](/docs/languages/#syntax-highlighting).
"""),
    new Feature("lsp", CI, 2, false,
        "Language servers (LSP)",
        "Go-to-definition, code actions, rename, signature help, inlay hints, hierarchy, diagnostics and completions via 29 language servers (Java, Astro, TypeScript, Python, Go, Rust, C/C++, and more), auto-detected, never bundled.",
        "Definitions, references, rename, code actions, hover, inlay hints and diagnostics from 29 language servers that Editora finds on your PATH or installs.",
        """
A language server is a language's own analysis engine, the one its community maintains. Editora works with 29 of them, so definitions, fixes, refactorings and errors come from the same tools you would run on the command line, in every language you use.

## Read code

- **Go to Definition** (`M-.` in the Emacs keymap, or Ctrl-click, Cmd-click on macOS) jumps to a symbol's definition, also from inside an opened JDK or library source tab.
- **Find References** (`M-S-/`) lists every use in the **References** tool window, each line with its syntax highlighting.
- **Go to implementation, type definition and declaration** appear wherever the server supports them.
- **Call and type hierarchy** opens as a tree in the **Hierarchy** tool window, each level fetched as you expand it.
- **Go to Symbol in Workspace** (`M-g s`) searches every symbol in the project and jumps to it.
- **Hover docs** (`C-c h`), **signature help** as you type `(` or `,`, occurrence highlighting and inlay hints explain the code in front of you.
- **Library sources** open with their real source when a Maven repository has it, or decompiled, with **Download Sources** in the tab.

## Change code

- **Code actions and quick fixes** (`C-c l a`, or `Ctrl+.` in the other keymaps, `Cmd+.` on macOS) open at the caret with the server's preferred fix first.
- **Organize imports, extract and inline** come from the same menu. For Python, Editora adds fixes Pyright lacks: import a missing name, remove an unused import, ignore on this line.
- **Rename Symbol** (`C-c l r`, `F2` in most other keymaps) renames across the workspace and lists every affected file first, so you can untick any.
- **Format Document** reformats the file through the server and keeps the file's own indentation. Python files use Ruff or Black, also with language servers off.
- **Java code generation** fills in toString, equals and hashCode, constructors, getters and setters, delegates and overrides, each from a checkbox picker.
- **Java refactorings** that need an answer ask for it: Move, Extract Interface and Change Signature.
- **Pasted Java code imports itself**, and a `;` typed mid-expression moves to the end of the statement.

## See problems

- **Inline diagnostics** underline errors, fade unused code and strike through deprecated calls, with marks on the scrollbar and minimap.
- **The Problems tool window** shows each problem's rule code, such as `[SC2086]`, and opens that rule's documentation. **LSP: Build Project (refresh all problems)** checks the whole project.
- **Code lenses** show reference and implementation counts after a declaration; click one to open what it counts.
- **Project tools join in**: a trusted JavaScript or TypeScript project's own ESLint, Prettier or Biome check and format its files, and shell scripts get ShellCheck and shfmt.

## Servers

Editora supports servers for Java, Astro, TypeScript and JavaScript, Python, Go, Rust, C and C++, C#, Ruby, PHP, Kotlin, Groovy, Clojure, Scala, HTML, CSS, XML, YAML, JSON, Bash, Lua, SQL, Terraform, TOML, Dockerfile, Typst, PowerShell, Elixir and Dart.

- **Found, never bundled**: Editora uses the server on your PATH, and each server's card in **Settings → LSP** shows whether its program is found.
- **One-click install**: an **Install…** button per server, a banner when a file's server is missing, or **Install: Language Server…** fetches it, and it starts with no restart.
- **Python** uses Pyright with the project's own interpreter, shown in the status bar and changed with **Python: Select Interpreter**.
- **Remote folders** run the server on the host, found on the host's PATH or set per host.
- **Per project**: a [project](/features/projects/) can commit which server it uses and whether it runs.
- **Safe in a folder you have not trusted**: a server that would run the project's build waits until you choose **Trust Folder**.
- **Looks after itself**: a crashed server restarts, and an idle one shuts down a few minutes after its last file closes.

**On by default.** Turn it off in **Settings → LSP**; each server starts once it is installed. See the [Language servers guide](/docs/lsp/#servers-are-auto-detected-never-bundled).
"""),
    new Feature("code-navigation", CI, 3, false,
        "Code navigation",
        "Go to symbol <strong>with no language server</strong>, sticky scroll, Peek Definition, preview tabs, and related-file jumps. Move around code without losing your place.",
        "Go to Symbol and Go to Definition without a language server, Ctrl or Cmd hover links, sticky scroll, Peek Definition, preview tabs and related files.",
        """
Navigation needs two things: a symbol has to be **findable**, and you have to be able to jump to it **without losing your place**.

## Find it

- **Go to Symbol in Project** searches every declaration in the project with Editora's own index, so it works on a first run and in 19 languages with no server installed.
- **Go to Definition** uses the same index when no server is running: it looks for the name at the caret in the file, then in the project.
- **A running [language server](/features/lsp/)** gives better answers and takes over when it starts.
- **Trustworthy results**: the index leaves out what it is unsure of, so it never sends you to a place that does not exist.

## Keep your place

- **Sticky scroll** pins the enclosing scope headers above the code, so deep in a long method you can still see what it belongs to.
- **Ctrl-hover** (Cmd on macOS) underlines the symbol under the mouse when Go to Definition can follow it; click to go there.
- **LSP: Peek Definition** shows a definition over the editor and leaves your place, scroll and tabs as they were. Enter makes the real jump; it needs a language server.
- **Preview tabs** reuse one tab while you browse instead of opening one per file. Its title is italic, and editing the file or opening it for real makes it a normal tab.
- **Go: Recent Locations…** lists the session's trail with the line you were on, and previews each as you arrow through it.
- **Go: Related File** (`C-c S-t` in the Emacs keymap) jumps between a test and its subject, a header and its implementation, or a component and its stylesheet.

See the [Code navigation guide](/docs/code-navigation/#go-to-symbol-in-project).
"""),
    new Feature("autocomplete", CI, 4, false,
        "Autocomplete",
        "As-you-type completion: a popup for code (LSP + snippets) and inline ghost text for prose. Trigger with <kbd>C-M-i</kbd> / <kbd>M-/</kbd>.",
        "A completion popup that merges language-server results and snippets for code, and inline ghost-text suggestions for prose.",
        """
Completion saves keystrokes and the trip to the docs: Editora suggests what comes next as you type, from your language server and snippets in code, and from a word list in prose.

- **One popup in code** merges language-server results and snippets; Enter or Tab accepts.
- **The whole list**: everything the server offers is listed, so `os.` in Python shows all of the module's members.
- **Docs beside the list** show the highlighted item's signature and documentation, widening to fit a long signature.
- **Snippets** start a tab-stop session, so Tab moves through the fields to fill in.
- **Imports come along**: accepting a Java item adds its import, and one undo takes back both.
- **A slow server says so**: a row such as "Waiting for Pyright…" shows until its answer arrives.
- **Ghost text in prose** finishes the word you are typing in plain text and Markdown as a muted suffix; Tab accepts it.

Completion appears as you type. To ask for it, press `C-M-i` or `M-/` in the Emacs keymap, or `Ctrl+Space` in the others.

**On by default.** Turn it off in **Settings → Code Completion**, where words, snippets and Mermaid keywords each have a switch of their own. See the [Languages guide](/docs/languages/#autocomplete).
"""),
    new Feature("structure-narrowing", CI, 5, false,
        "Structure narrowing",
        "Walk a file one function at a time: a toggle in the Structure tool window narrows the editor to the symbol you click, and a click on the next one moves there.",
        "Read a file one declaration at a time: the Structure window narrows the editor to the symbol you pick, Emacs-style, with a Widen banner.",
        """
A long file is easier to read one function at a time. Turn on narrowing in the **Structure** tool window, and each symbol you pick fills the editor on its own, with the rest of the file out of the way until you widen.

## Walk a file

- **The toggle**: **Narrow to fold region**, at the end of the Structure window's sort row, narrows the editor to the picked symbol's block.
- **Next symbol**: while the toggle is on, a click or Enter on another symbol narrows to that one instead of only scrolling there.
- **Markdown** narrows a heading to its section.
- **From the keyboard**: `f` flips the toggle while the outline has focus, and **Structure: Toggle Narrow to Fold Region** can take a key in any keymap.
- **From the [Project Map](/features/project-map/)**: a declaration card's **Open** opens its file narrowed to that declaration.

## What narrowing means

- **Real narrowing, as in Emacs**: search, replace, macros and Select All see only the region, and saving still writes the whole file.
- **You always know**: an amber banner names the lines shown, such as *Narrowed to lines 32–36 of 81*, with a **Widen** button.
- **The file's own line numbers** stay in the gutter, the status bar and the outline, and the outline mutes the rows outside the region.
- **Widen any way** to end it: the banner, the status-bar badge or `C-x n w` in the Emacs keymap. The `C-x n` chords from [Emacs heritage](/features/emacs-heritage/) narrow too.
- **While narrowed**, language-server features pause, and bookmarks, notes and breakpoints are not saved until you widen.

See the [Emacs editing model guide](/docs/editing/#the-emacs-editing-model).
"""),
    new Feature("run-files", RD, 1, false,
        "Run files & main classes",
        "Run the file you are looking at from a gutter ▶: a script, a program or a project's <code>main</code> class, in most of the languages Editora supports. Output, input and clickable stack traces are in the Run console.",
        "Run a Java, Python, Node.js, shell, Kotlin, Ruby, PHP, Go or Rust file, and many more, from a gutter button, with input and clickable stack traces.",
        """
---
---
A green ▶ in the gutter runs the file you are looking at, with nothing to set up first. It picks the runtime the project asks for, such as its `.venv`, its `.nvmrc` or its JDK, and works with language services turned off.

## What the ▶ runs

- **Scripts**: Python, Node.js and TypeScript, shell, Ruby, PHP, Deno, Elixir and PowerShell.
- **JVM files**: a single-file or compact Java program, Groovy, Kotlin, Scala and Clojure, each through its own build when it has one.
- **Compiled programs**: C, C++, Go, Rust and Dart, built the way Debug builds them, then run.
- **Tests**: a test file runs under its own runner, such as pytest, Jest, Vitest, RSpec, PHPUnit or GoogleTest, with each test listed in Test Results.
- **Web apps**: Django, Flask, FastAPI, Rails, PHP and Electron start their server.

Each language's page under [Languages](/docs/languages/) says exactly what its ▶ runs.

**File: Run** is `C-c r` in the Emacs keymap and `Ctrl+Shift+B` in the VS Code keymap (`Cmd+Shift+B` on macOS). **Run: Rerun Last** runs the last file again with the same arguments.

## A project's main class

In a Maven or Gradle project, a ▶ sits beside every `public static void main`, and **Run: Run Main Class…** picks any of them. The classpath comes from the Java language server, or from the build tool when the server is not set up.

## The Run console

- **Input**: the console takes `stdin`, so programs that read input work, and **Run: End Program Input** ends it.
- **Clickable stack traces**: Java, Python and Node.js frames, shell errors and pytest locations open the file at that line.
- **Colours and progress bars** show as the program prints them.
- **Program arguments** are kept per file across runs.
- **The runtime** the program runs under is named beside the console, such as *Python 3.13.5*, read from the program that ran.
- **Stop** gives a Python program the chance to run its `finally` blocks on macOS and Linux.

## Trace, remote and saved runs

- **Run: Run with Trace** runs a shell script with `bash -x`, and each traced line links to its source.
- A file in a [remote folder](/features/remote-sftp/) runs on its host, with the same output, input and Stop.
- For anything you run more than once, save a [run configuration](/features/run-configurations/).

See the [Running programs guide](/docs/run-debug/#running-files).
"""),
    new Feature("run-configurations", RD, 2, false,
        "Run configurations",
        "Save how a main class, a script, a program, a make target or an npm script launches, with its arguments, environment and a before-launch step, then run or debug it from the toolbar and share it with the project.",
        "Save how a main class, script, make target or npm script runs, with arguments, environment and a before-launch step, and share it in the project.",
        """
---
---
A run configuration saves how something launches, so you pick it in the toolbar and press **Run** or **Debug** instead of retyping arguments. The choice is remembered across restarts, and a configuration can be shared with the project.

## What a configuration holds

- **What to launch**: a main class, a script, a program, a make target or an npm script.
- **Arguments and environment**: program and VM arguments, and environment variables, where `$NAME` expands and `@.env` loads a `.env` file.
- **Runtime**: a JDK for this configuration, or a script's interpreter and its options.
- **Working folder**, relative to the project root.
- **Run on**: this machine, or one of the project's [deployments](/features/deployments/).

## Kinds

Choose a **Type** in **Run: Edit Run Configurations…**:

- a **Java main class**, found through the language server or your Maven or Gradle build
- a **Python script** or **Python module** (`python -m`)
- a **Node.js script**, or a named **npm script** run with the project's package manager
- a **shell script**, run by its shebang
- a **Groovy**, **Kotlin**, **Clojure** or **Scala** script or program, on the configuration's JDK
- a **C**, **C++**, **Go**, **Rust** or **Dart** program, built the way Debug builds it
- a **make target**

Every kind but shell and make can be debugged, and a [compound](/docs/debugging/#compounds) starts several configurations together. A Bash script is debugged from its own tab instead.

## Run a step first

A configuration can name a command to run first, such as a build or a code generator. A **non-zero exit stops the launch**, so a stale binary never runs by accident. For Java, Debug uses that step as its build.

## Share with the team

**Run: Export Configurations to Project** writes them to `.editora/run-configurations.json`, to commit beside the [project settings](/features/projects/). **Run: Import Configurations from Project** merges them back **by name**, so importing twice never duplicates and a colleague's edit updates the one you have.

## Conveniences

- **Each configuration is a command** in the palette, so it can have its own keyboard shortcut.
- **Run: Add Run Configuration** fills the form from the open file: its script, its main class, and a name.
- **The form shows only the fields** the chosen type uses, and **Browse…** picks the script, class or npm script.
- **An incomplete configuration opens its form** at the field you still need to fill in.
- **The toolbar selector** appears only in a project with something to launch.

Run configurations belong to a [project](/features/projects/). Outside one, run a file from its gutter ▶ (see [Run files](/features/run-files/)).

See the [Running programs guide](/docs/run-debug/#run-configurations).
"""),
    new Feature("debugging", RD, 3, false,
        "Debugging (DAP)",
        "Full debugging for Java, Python, JavaScript, TypeScript, Groovy, Kotlin, Clojure, Scala, Go, Rust, C, C++, C#, Ruby, PHP, Bash, PowerShell, Elixir, Dart and Lua: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, Debug History, and an interactive console.",
        "Debug 20 languages over the Debug Adapter Protocol: breakpoints of every kind, stepping, watches, inline values, Debug History and memory views.",
        """
---
---
Stop a program where it goes wrong, read its values and step through it, in one Debug window that works the same way in every language and never takes the keyboard from the editor.

## Start a session

**Debug: Start / Continue** is `C-c C-d d` in the Emacs keymap and `F5` in the CUA, Sublime Text and VS Code keymaps (see the [keybindings](/keybindings/) for IntelliJ IDEA). It builds first and stops on compile errors.

- **Debug File** in the editor's right-click menu debugs the file you are in.
- **Debug Test at Caret** debugs one test, in JUnit, pytest, Go, Rust, Jest, RSpec, PHPUnit and most other frameworks.
- **Web apps** debug as they are served: Django, Flask, FastAPI, Rails, PHP and Electron.
- **Debug in Browser…** opens a page in Chrome, Edge or Firefox under the debugger.
- **VS Code's `launch.json`** starts as it does in VS Code, compounds and `preLaunchTask` included.
- **Several sessions** run at once, each in its own tab.
- **Attach to Process…** lists the programs on this machine you can debug, and Stop leaves the one you pick running.
- **Attach to Remote Program…** debugs a C, C++ or Rust program under gdbserver or lldb-server elsewhere.
- **Open Core Dump…** opens what a crashed Go, C, C++ or Rust program left.

## Set breakpoints

**Debug: Toggle Breakpoint** is `C-c C-b` in the Emacs keymap and `F9` in the CUA, Sublime Text and VS Code keymaps.

- **Conditions and hit counts** decide when a breakpoint stops, and a **temporary** one removes itself once hit.
- **Logpoints** print a message without stopping; Shift+click the gutter to add one.
- **Triggered** breakpoints wait for another one, and **recording** ones note the stop and let the program go on.
- **Lambda, method and field breakpoints** stop inside a lambda, on entering a Java method, or when a field changes.
- **Exception breakpoints** stop on the exceptions you name, such as `java.io.*`, and a card under the line that threw shows what was thrown.
- **Filters** limit a breakpoint to some threads, callers or one marked object.
- **Groups** switch a set of breakpoints together, and **export and import** share them.

## Step through the code

- **Step Into, Over and Out**, with the time each step took shown after the line.
- **Run to Cursor**, or **Force Run to Cursor** past every breakpoint.
- **Don't Step Into This…** makes Step Into go over a class, package, module or file.
- **Smart Step Into…** picks the call to enter, and **Restart Frame** runs a function again.
- **Hot code replace** loads a saved Java change into the running program.
- **Step Back** runs C and C++ backward on GDB once **Debug: Record Execution** is on.
- **Pause or resume one thread** while the others keep going.

## Inspect values

- **Inline values** follow each line while paused, and a hover unfolds a value as a tree.
- **The Variables tree** highlights what the last step changed and edits a value in place.
- **Evaluate Expression** runs code in the stopped frame, in its own window or with Alt+click.
- **Watches and pinned values** update at every stop; a pinned value sits at the end of its line.
- **Autos** lists the values the stopped line uses, and a step out shows the return value where the debugger reports it.
- **Show Value As** shows a number in hexadecimal, binary, octal or as a character.
- **Type renderers** show a type your way, such as a `Money` as `12.50 EUR`.
- **View as Table** opens a list or map as a sortable table, and a pandas DataFrame or numpy array as its rows.
- **Mark Object** labels an object, and **View as JSON or XML** lays out a string.

## Look back and look deeper

- **Debug History** shows an earlier stop as it was, with its stack, values and watches.
- **Thread Dump** shows every thread's stack, even in a program that hangs, and **Parallel Stacks** draws them as one tree.
- **Disassembly and Memory** views step by instruction and edit bytes in C, C++, Rust and Go.
- **Loaded Sources** opens code with no file, such as `eval` code, where it stops.

## Languages and adapters

Each language's debugger is installed separately. Its card in **Settings → Debugging** says whether it is found and offers **Install…** where Editora can fetch it. Java debugging also needs the [language server](/features/lsp/), and **Debug: Edit Debug Adapters** adds any other debugger that speaks the Debug Adapter Protocol. Each page under [Languages](/docs/languages/) says what its debugger supports.

**On by default.** Turn it off in **Settings → Debugging**. See the [Debugging guide](/docs/debugging/#start-debugging).
"""),
    new Feature("http-client", RD, 4, false,
        "HTTP client",
        "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶ or a key, with environments, variables, request chaining, and a response view that shows exactly what was sent. Built on the JDK HTTP client.",
        "Run .http and .rest requests from the editor or with editora http on the command line, with environments, variables, chaining and tests.",
        """
---
---
Try an API without leaving the editor. Write requests in a `.http` or `.rest` file, send one with the green ▶ beside it, and read the response next to the request that produced it. Nothing else to install.

**HTTP: Run Request at Caret** is `C-c C-c` in the Emacs keymap and `Ctrl+Alt+R` in the other keymaps (see the [keybindings](/keybindings/) for macOS). **HTTP: Run All Requests in File** is `C-c C-r` in the Emacs keymap.

## Write requests

- **JetBrains' HTTP request format**, requests separated by `###`, with some VS Code REST Client syntax too.
- **Variables and environments**: `{{name}}` comes from the file, an `http-client.env.json` environment picked from the Env pill, a `.env` file or a dynamic value such as `{{$uuid}}`.
- **Chaining**: a request reads a value from an earlier response, so a sign-in feeds the token to the next call.
- **Bodies and auth**: multipart and file bodies, and Basic and Digest shorthand.
- **Help as you type**: `{{` offers the variables in scope, and an unknown variable is underlined, with a fix that switches to an environment that defines it.

## Test the response

- **`# @assert` lines** check the status, a header or a JSON value without any code.
- **JavaScript response handlers** run JetBrains-style `client.test(…)` scripts on your own Node.js.
- **Pre-request scripts** sign a request or set its variables before it is sent.
- **Trusted folders**: scripts run, and requests read your environment, only in folders you trust, or after you agree.

## Read the response

- **Beside the request**: the response opens as the `.http` file's preview, with the status, time and size in its header.
- **Tabs** hold the body, pretty-printed and highlighted, the headers, the test results, the request exactly as sent, and warnings.
- **Results on each request line**, such as `200 OK · 83 ms · ✗ 1 of 3 tests`, and a clear message when there was no response.
- **History**: the last 10 responses of each file are kept, with credential headers masked.
- **cURL**: **HTTP: Copy Request as cURL** gives a command that is safe to paste, and **HTTP: Import cURL from Clipboard** takes a browser's copy.

## Run from the command line

`editora http` (`editora-cli http` on Windows) runs every request of a file in order, with the same chaining, checks and handlers, for CI and scripts. It takes an environment and variables, writes a JUnit XML report with `--report`, and exits 1 when a request or a test fails. `editora http --help` lists every option.

**On by default.** Turn it off in **Settings → Web**. See the [HTTP client guide](/docs/http-client/#writing-requests).
"""),
    new Feature("build-tools", RD, 5, false,
        "Build tools",
        "Maven, Gradle, npm, Cargo, Go, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka each get a tasks tool window and a streaming console.",
        "Tasks windows and streaming consoles for Maven, Gradle, npm, Cargo, Go, sbt, Mill, Scala CLI, the Clojure CLI, Leiningen and Babashka.",
        """
Run a project's builds without memorising its commands. Each build tool Editora finds gets a **tasks tool window**: a tree of its goals, scripts or targets, where a double-click or Enter runs one and the output streams to that tool's console. The window's stripe appears as soon as the tool's marker file is found, and the list loads instantly, offline.

The mini toolbar has **Run**, **Reload**, **Stop** and **Run custom…**, and each tool's actions are in the palette as a searchable popup, such as **Maven: Show Actions**.

## What each tool lists

- **Maven** (`pom.xml`): lifecycle phases, the pom's profiles as checkboxes, and each plugin's goals. It prefers `./mvnw`.
- **Gradle** (`build.gradle`, `build.gradle.kts`): the common tasks, plus **Load all tasks…**. It prefers `./gradlew`.
- **npm** (`package.json`, the **Package Scripts** window): every script, plus `install` and `ci`, run with the project's own npm, pnpm, yarn or bun.
- **Cargo** (`Cargo.toml`): the standard subcommands, each `[[bin]]` and `[[example]]`, and a `--release` toggle.
- **Go** (`go.mod`): the standard subcommands over the whole module.
- **sbt** (`build.sbt`): the common tasks, plus **Load all tasks…** with each subproject's.
- **Mill** (`build.mill`, `build.sc`): every module's compile and test, plus **Load all tasks…**. It prefers `./mill`.
- **Scala CLI** (`project.scala`): its commands over the project folder.
- **Clojure CLI** (`deps.edn`): the aliases that run something, such as `-M:test` or `-X:build`, plus the dependency tree and classpath.
- **Leiningen** (`project.clj`): the common `lein` tasks, such as `run`, `test` and `uberjar`.
- **Babashka** (`bb.edn`): one entry per task.

A missing package manager says how to get it, and a failed npm script with no `node_modules` suggests installing first.

## The project's JDK, Maven and Gradle

A Java project builds, runs, debugs and is analysed on the versions it names, so everyone on the team uses the same ones. Editora reads them from the project's settings or from the files [jkit](https://jkit-project.dev/) reads: `jkit.toml`, `.sdkmanrc`, `.tool-versions` and `.java-version`.

**Tools ▸ Java Toolchain** sets the project's JDK, Maven, Gradle, sbt and GraalVM. With jkit installed, it also installs, syncs, upgrades and removes them.

**On by default.** Turn any tool off in **Settings → Build Tools**. See the [Build tools guide](/docs/build-tools/).
"""),
    new Feature("deployments", RD, 6, true,
        "Deployments",
        "Keep the project here and run it on an SSH host. Before each run, Editora copies what changed to a folder there, then runs or debugs the program in that copy.",
        "Edit a project locally and run or debug it on an SSH host: Editora copies only what changed to a folder there before each run.",
        """
---
---
A **deployment** lets you edit a project on this machine and run its programs on another one: a server with the database, a GPU box, or a machine with the right operating system. The sources, language servers, Git and Local History stay here. It is the reverse of a [remote folder](/features/remote-sftp/), where the project itself lives on the host.

- **Run on.** In the Run Configurations window, **Run on** picks **This machine** or one of the project's deployments. Run then saves the project's edited files, copies what changed to the deployment's folder, and runs the program there, with the host's interpreters and tools, and with output, input and Stop over the same connection.
- **Java** is built on the host with its own Maven or Gradle. **Debug** starts the program there and attaches through the SSH connection, so breakpoints and stepping work on the local files.
- **Only what changed is sent**, as one compressed stream into the host's `tar`. Folders such as `.git/`, `target/` and `node_modules/` stay behind, so do files Git ignores (with the default options), and **Leave out** adds patterns of your own. **Upload on save** deploys after every save without running anything.
- **Careful with the host's files.** A deploy removes only files it sent itself, asks before replacing a file changed on the host, asks before a first deploy into a folder that already has files, and refuses `/`, system folders and the home folder.

**Run ▸ Deployment** has **Deploy Now**, **Compare with Deployed** (the active file beside its copy, in the diff viewer), **Open Terminal in Deployment** and **Edit Deployments…**. A deployment uses the same SSH logins and host keys as a remote folder, and the same [permission to run commands](/docs/remote/#allowing-a-host-to-run-commands) on the host. Nothing is installed on the host, and passwords are never stored.

See the [Deployments guide](/docs/deployments/#setting-one-up).
"""),
    new Feature("git", GD, 1, false,
        "Git integration",
        "Native Git: status-bar branch, gutter change bars vs HEAD, a Commit tool window, merges and rebases with a conflict resolver, branches, remotes, tags, stashes and patches, a paged log with a commit graph, and blame.",
        "Git through your own git: branch in the status bar, gutter change bars, a Commit window, merge conflict resolver, paged log with a graph, and blame.",
        """
Do your everyday Git work without leaving the editor. Editora runs your own installed `git`, so it behaves exactly like the command line, with your own config and credentials.

## See what changed

- **The branch in the status bar**, with ahead and behind counts and a menu to switch or create branches, pull, fetch and push.
- **Change bars in the gutter** mark added, changed and deleted lines. Click one to see the old lines and revert or stage that hunk.
- **The Project tree is coloured by status**: added, modified, deleted, renamed and untracked files, with changed folders tinted.

## Commit

- **The Commit window** (`M-4` in the Emacs keymap) groups staged, changed and untracked files, with stage, unstage, discard and a message box.
- **Commit options**: Amend, Commit and Push, Sign off, and Undo Last Commit.

## Merge, rebase and sync

- **Operations in progress**, such as a merge, rebase, cherry-pick or revert, show in the status bar and the Commit window with Continue, Skip and Abort.
- **Conflicts** get their own group and open in the [three-way resolver](/features/diff-merge/). Finishing a resolution stages the file.
- **Pull your way**, fast-forward only, rebase or merge, and push with lease or to another remote.
- **Manage branches** from the status-bar menu: rename, merge, rebase onto, set upstream, compare and delete.
- **Remotes, worktrees, tags, stashes and patches** each have their own commands.

## Explore history

- **The Git Log** (`C-x v S-l` in the Emacs keymap) pages through the whole history with a commit graph. Search by message, author, content, date or path, and press Enter on a commit to review everything it changed.
- **Blame** (`C-x v g` in the Emacs keymap) shows who last changed each line. It can ignore whitespace, follow moved lines, and walk a line back through earlier revisions.

## Know what it ran

- **A Git transcript** in the Output console lists every command you asked for, with its output, exit code and duration. Clone, fetch, pull and push show progress there, with a Stop button.
- **Remote folders work too.** In a [remote folder](/features/remote-sftp/), the host's own `git` runs over the same SSH connection.

Nothing appears until `git` is found. The other keymaps' chords are on the [keybindings](/keybindings/) page.

**On by default.** Turn it off in **Settings → Version Control → Git**. See the [Git guide](/docs/git/#git-integration).
"""),
    new Feature("github", GD, 2, false,
        "GitHub integration",
        "Review and check out pull requests, submit reviews, open a file on GitHub at the caret line, and jump from a failed CI log straight to the offending line. Uses your own <code>gh</code> CLI, so Editora never handles a token.",
        "Review and check out pull requests, submit reviews, follow CI checks and open failed logs at the line, all through your own signed-in gh CLI.",
        """
Review pull requests, follow CI and jump to a failing line without opening a browser. Editora works through the [`gh` CLI](https://cli.github.com) you have signed in, so it never handles a token, and GitHub Enterprise works with no extra setup.

## Review pull requests

- **Read a pull request in the editor.** **GitHub: Review Pull Request Diff…** opens a Files changed tab with the description rendered as Markdown and per-file `+` and `−` counts. Click a file for its diff.
- **Step through changes** with `n` and `p` inside any read-only diff.
- **Submit a review**, to approve, request changes or comment, with **GitHub: Submit Review…**.
- **Check out a pull request** to work on it locally.

## Open and share

- **Create a pull request** from the branch's commits and the repository's template, with a base-branch chooser and an offer to push the branch first.
- **Open the current file on GitHub** at the caret line.

## Find what you need

- **One tool window** for pull requests, issues and Actions runs (`M-g p` in the Emacs keymap). A single filter box matches anything a row shows, so `42` and `#42` both find PR 42.
- **Narrow the lists** by state, such as open, closed or merged, or to your own items, and load more on demand.

## Follow CI

- **CI checks in the status bar** for the current branch's pull request, updated while checks run, with a list of checks and their logs a click away.
- **Failed logs open at the line.** A failed run's log opens in the Output console with clickable stack frames mapped onto your local checkout.
- **A transcript** of the `gh` commands Editora ran, with exit codes and durations, sits in the console's GitHub tab.

Nothing appears until Git support is on, `gh` is signed in, and the repository has an open pull request, issue or workflow run.

**On by default.** Turn it off in **Settings → Version Control → GitHub**. See the [GitHub guide](/docs/github/).
"""),
    new Feature("diff-merge", GD, 3, false,
        "Diff & merge",
        "Compare files, folders, patches and Git changes side by side or unified, apply changes hunk by hunk, and resolve merge conflicts three ways.",
        "Side-by-side and unified diffs of files, folders, patches and Git changes, with word emphasis, hunk actions and a three-way merge resolver.",
        """
See exactly what changed, take the parts you want, and settle merge conflicts without hand-editing conflict markers.

## Compare anything

- **A file against Git**: **Diff: Compare with HEAD** (`C-x v =` in the Emacs keymap), or against a commit, branch or tag. Unsaved edits count.
- **Two files or two folders**, or a file against the clipboard or empty text.
- **A whole change set**: every staged or working-tree change, or a `.patch` file, opens as one review you step through file by file.

## Read the changes

- **Side by side or unified**, with the changed words highlighted inside each line and an overview of the whole file at the right edge.
- **Cut the noise**: ignore whitespace or case, collapse unchanged lines, wrap long lines, or swap sides.
- **Line up related lines** with smart alignment, so a changed line sits beside its old version even when lines were inserted or removed around it.

## Apply what you want

- **Take a change** into your file with the gutter actions, each one undoable, or edit a result draft and save it.
- **Work with Git** from the diff: stage, unstage or revert a hunk or a single line, copy a hunk, or open the changed line in the editor.
- **Export** the comparison as a patch.

## Resolve conflicts

**Merge: Resolve Conflicts** opens a three-way resolver from Git's base, ours and theirs. Compatible edits combine on their own, each remaining conflict offers Ours, Theirs, Both or Base, and a complete resolution saves and stages the file. Edits on neighbouring lines count as one conflict, as in Git, so nothing is merged out of sight.

See the [Diff and merge guide](/docs/diff-merge/).
"""),
    new Feature("markdown-preview", DD, 1, false,
        "Markdown preview",
        "A live, GitHub-style preview beside your Markdown, with math, alerts, wiki links and backlinks, 43 lint rules, table editing, and export to PDF, HTML, Word and ODF.",
        "A native CommonMark and GFM preview with math, alerts and wiki links, plus markdownlint, table editing, and export to PDF, HTML, Word and ODF.",
        """
Write Markdown and see it as GitHub would show it, updated as you type, with the tools for tables, links, images and linting in the same editor.

## Preview as you write

- **Three views** from the floating control at the top right of any Markdown file: Editor, Split and Preview. Each file remembers its view.
- **GitHub-style rendering** of task lists, tables, alerts, footnotes, front matter, emoji shortcodes and LaTeX math, drawn natively with no browser engine.
- **Images**, local and remote, including SVG badges. Remote images load once you allow them for the document.
- **Theme-matched**, or switched with its own light and dark toggle, and it can open in a tab of its own beside the file.
- **Work in the preview.** Select and copy text as formatted text, tick a task box to update the source, and enlarge images and diagrams.
- **Terminal sessions.** A `console` block shows a muted prompt, the command highlighted, and plain output.

## Edit faster

- **Tables**: insert one, add, move and sort rows and columns, Tab between cells, reflow, and convert to and from CSV.
- **Pasting**: drop or paste images into an assets folder you choose, paste HTML or spreadsheet cells as Markdown, and paste a link over a selection to wrap it.
- **Links and wiki links**: follow `#heading` and file links, complete `[[wiki links]]`, see a **Backlinks** tool window, and keep links right when a file is renamed or moved.
- **Structure**: a heading outline, a table of contents, heading numbering and **Markdown: Format Document**.
- **Writing aids**: a word count and reading time in the status bar, and typewriter mode.

## Lint and export

- **Linting** with 43 markdownlint rules plus broken-link checks, shown as squiggles and in a **Markdown Lint** tool window, with auto-fix that can also run on save. Your project's `.markdownlint` config is read.
- **Export** the preview to PDF, self-contained HTML, Word or ODF, or print it; see [Export and print](/features/export-pdf-print/).
- **Diagrams** in a `mermaid` fence render in place; see [Mermaid diagrams](/features/mermaid/).

See the [Markdown guide](/docs/markdown/#live-preview).
"""),
    new Feature("mermaid", DD, 2, false,
        "Mermaid diagrams",
        "Render Mermaid diagrams inline in Markdown and in standalone <code>.mmd</code> files, with live linting and export to SVG / PNG / PDF.",
        "Mermaid diagrams render in Markdown previews and .mmd files through mmdc, with live linting by maid and export to SVG, PNG and PDF.",
        """
Draw flowcharts, sequence diagrams and charts as text, and see them rendered as you write, in Markdown documents and in their own files.

- **In Markdown**, a ` ```mermaid ` fence becomes a diagram in the [preview](/features/markdown-preview/), drawn for the preview's light or dark theme.
- **In `.mmd` files**, the diagram gets the Editor, Split and Preview views.
- **Live linting** underlines mistakes as you type, with the exact line and column, when the `maid` linter is installed.
- **Completion** offers Mermaid keywords inside a fence.
- **Export** a diagram to SVG, PNG or PDF with **Mermaid: Export Diagram (SVG/PNG/PDF)**. HTML exported from Markdown includes the drawn diagrams.

Diagrams are drawn by the `mmdc` command, and nothing appears until it is found. Install it with the **Install…** button in **Settings → Mermaid**, which shows whether each tool is found, and add the linter with `npm i -g @probelabs/maid`.

**On by default.** Turn it off in **Settings → Mermaid**. See the [Mermaid guide](/docs/markdown/#mermaid-diagrams).
"""),
    new Feature("typst", DD, 5, false,
        "Typst",
        "A 3-mode preview for <code>.typ</code> files rendered by the Typst CLI, a tinymist language server, Markdown-style editing, and export to PDF / PNG / SVG.",
        "A multi-page preview for .typ files rendered by the Typst CLI, the tinymist language server, list and format helpers, and export to PDF, PNG or SVG.",
        """
Write Typst documents with the typeset pages beside your source, and the editing comforts of Markdown. The preview keeps its last good render while you type, so it never flickers, and a compile error shows as a small banner over pages that stay visible.

- **See every page.** `.typ` files get the Editor, Split and Preview views, rendered by the `typst` CLI as a stack of pages.
- **Edit like Markdown.** Enter continues a list, and selecting text pops a format bar for bold, emphasis, raw, link, bullet and heading, also on the right-click menu.
- **Insert the usual blocks**: tables, images and a table of contents, plus bundled snippets for figures and more.
- **Navigate by section.** Headings drive the outline and the folding, and `#let` and `#show` bindings appear in the Structure window under their section.
- **Code intelligence** comes from the tinymist language server, which Editora can install for you.
- **Export** to PDF, PNG or SVG with **Typst: Export (PDF/PNG/SVG)**, or print the pages.

Nothing appears until `typst` is found. Install it with your package manager, such as `brew install typst` or `cargo install typst-cli`, or with the **Install…** button in **Settings → Typst**.

**On by default.** Turn it off in **Settings → Typst**. See the [Typst guide](/docs/typst/).
"""),
    new Feature("diagrams", DD, 6, false,
        "Diagrams as code",
        "A 3-mode preview for Graphviz DOT (<code>.dot</code>/<code>.gv</code>) and PlantUML (<code>.puml</code>) files via the <code>dot</code> / <code>plantuml</code> CLIs, with export to SVG / PNG / PDF.",
        "Graphviz DOT and PlantUML files get an Editor, Split and Preview view rendered by dot and plantuml, with export to SVG, PNG and PDF.",
        """
Keep your architecture and UML diagrams as plain text in the repository, and see them drawn beside the source as you edit.

- **Graphviz and PlantUML.** `.dot` and `.gv` files and `.puml` and `.plantuml` files get the Editor, Split and Preview views, drawn by the `dot` and `plantuml` tools.
- **Quick to redraw.** An unchanged diagram is not drawn again, and zoom resizes the image.
- **Export** a diagram to SVG, PNG or PDF with **Diagram: Export (SVG/PNG/PDF)**, or print it.
- **Safe to open.** PlantUML runs sandboxed, so a diagram cannot pull in local files or URLs.

Nothing appears until the tools are found. Install them with your package manager, such as `brew install graphviz plantuml`; **Settings → Diagrams** shows whether each one is found.

**On by default.** Turn it off in **Settings → Diagrams**. See the [Diagrams guide](/docs/diagrams/).
"""),
    new Feature("previews", DD, 7, false,
        "Smart file previews",
        "Open a data or config file and read it at a glance: JSON, YAML, TOML and XML as trees, Maven poms as summaries, OpenAPI as API docs, Markwhen as timelines, and crontab, systemd or Dockerfile in plain English.",
        "Readable previews of JSON, YAML, TOML and XML trees, Maven poms, OpenAPI docs, Markwhen timelines, and crontab, systemd or Dockerfile in plain English.",
        """
Raw data and config files are hard to read. Editora gives each one a preview that shows what the file means, in the same Editor, Split and Preview views as Markdown.

## Data and specs

- **Data trees** for JSON, YAML and TOML, collapsible and coloured by type, and an element tree for XML.
- **Maven poms as a summary**: coordinates, dependencies, plugins, properties and profiles in aligned columns, instead of the generic XML tree. **Maven: Toggle POM Summary / XML View** switches back.
- **API docs** for an OpenAPI 3 or Swagger 2 file: endpoints, method badges, parameters, responses and schemas, with a toggle back to the tree.
- **Markwhen timelines** for `.mw` and `.markwhen` files, with a month calendar view and JSON export.

## Config files in plain English

- **crontab**: `30 2 * * 1-5` reads "At 02:30, Monday through Friday", with the next run times.
- **systemd units**, with the next triggers of an `OnCalendar=` timer.
- **SSH config**, one connection summary per host, and **fstab** mounts.
- **Dockerfile**, a digest per build stage, and **GitHub Actions** workflows, their triggers and jobs.
- **Malformed lines are flagged**, so the preview also catches mistakes.

## Viewers

- **PDF files** open in a read-only page viewer.
- **SVG files** stay editable and gain a live rendered preview.
- **Binary files** open as a hex dump.

**On by default.** Turn the data, pom, SVG and config previews off one by one in **Settings → Editor → File previews**. See the [Previews guide](/docs/previews/).
"""),
    new Feature("export-pdf-print", DD, 3, false,
        "Export & print",
        "Turn code or a Markdown document into a clean PDF, HTML, Word or ODF file, or print it after a page-by-page preview. Output is always light-themed.",
        "Export code or the Markdown preview to a highlighted PDF, HTML, Word or ODF, or print it with a page preview. Output is always light-themed.",
        """
Share your work in the format the reader wants: a highlighted PDF of your code, or a Markdown document as a polished PDF, web page, Word or ODF file, always light-themed and readable on paper.

## Export code

- **Syntax-highlighted PDF** of the current file, with line numbers if you want them: **File: Export to PDF…**.
- **Only the lines you need**: **File: Export Selection to PDF…** and **File: Print Selection…** output the selected lines, numbered as in the file.

## Export documents

- **PDF** from the Markdown or Mermaid preview, with headings, lists, tables, code blocks, math, images and coloured GitHub alerts drawn as sharp vector text.
- **Self-contained HTML** with images embedded and code highlighted, following the reader's light or dark preference.
- **Word** (`.docx`) and **OpenDocument** (`.odt`), with tables, code, math, Mermaid diagrams and images.
- **Long documents paginate well**: a heading stays with what follows it, and a table split across pages repeats its header row.
- **Ready to navigate**: exported PDFs have clickable links, bookmarks from the headings, and a page footer.

## Print

- **Preview first.** **File: Print…**, in the palette or `Ctrl+P` in the CUA keymap (`Cmd+P` on macOS), opens a page-by-page preview at paper size, with zoom, Page Setup and keyboard paging. What you preview is what prints.
- **More than text.** Image tabs, data previews and the whole [Project Map](/features/project-map/) print and export too.

Find both under the **File** menu, with the export formats under **File ▸ Export**, and in the tab and editor right-click menus. Page size, margins, code font size and the footer are set in **Settings → Editor → Export & Print**.

See the [Print and PDF guide](/docs/print-pdf/).
"""),
    new Feature("html-live-preview", DD, 4, false,
        "HTML live preview",
        "Click the globe on an HTML file to open it in your browser, served with the rest of its site and reloaded as you type, before you save.",
        "Open an HTML file in your browser from a loopback-only server that serves the whole site and reloads the page as you type, before you save.",
        """
See your page in a real browser while you write it: click the globe and the page opens, then follows every keystroke without a save or a manual refresh.

- **Open in your browser.** The globe at the top right of any `.html`, `.htm` or `.xhtml` file opens it in Safari, Chrome, Firefox, Edge or the system default. **HTML Preview: Open in Browser…** picks another.
- **Reload as you type.** The browser shows the text in the editor, saved or not. Editing a stylesheet swaps it in without a reload.
- **The whole site loads.** Stylesheets, scripts, images, root-relative `/…` links and folder links all work, served from the project folder or the page's site folder.
- **Private by default.** The server answers only this machine, each site gets its own address, and hidden files, private keys, credential stores, databases and encrypted files are never served.
- **Nothing to install.** The server is built into Editora.

**On by default.** Turn it off, or choose the browser, in **Settings → Web → HTML Preview**. See the [HTML live preview guide](/docs/html/#live-preview).
"""),
    new Feature("project-map", WF, 0, false,
        "Project Map",
        "See a project as a <strong>map</strong>: folders as columns on a canvas, code you follow as connected cards, branch review, dependencies and hotspots, and trails that walk a team through the code.",
        "See a project as a map in its own window: folder columns on a canvas, code cards, callers and hierarchies as connected cards, hotspots and trails.",
        """
---
---
The Project Map lays a [project](/features/projects/) out as folders on a canvas, with the code open in cards beside them, so you can explore a codebase, follow a call chain or review a branch without losing your place. It opens in a **window of its own**: `C-c o m` in the Emacs keymap, `Alt+Shift+1` in the others (`Cmd+Shift+1` on macOS), or the **Map** button in the Project tool window.

## Browse the folders

- **Columns** open a folder's children in the next column, and the path that led there stays on screen.
- **Single-child chains** such as `src/main/java/com/acme` open as one column.
- **Pan and zoom** with a drag and the wheel. Zoomed out, columns turn into labelled blocks, so a big project still fits.
- **Reveal** the file you are editing with `C-c o r` in the Emacs keymap, `Alt+F1` in the others.
- **Remembered places**: open folders, selection, zoom and cards come back after a restart.

## Read code in cards

![A read-only preview card for Shelf.java on the Project Map, opened past the file's column and joined to its row by a connector, showing syntax-highlighted Java, with the file's last change and the card's buttons in its header](/screenshots/docs/project-map-card.png "A preview card opens past the file's column, joined to its row.")

- **Preview cards** open from a file's **eye**: read-only, highlighted like the editor, unsaved text included, and joined to their row.
- **Declaration cards** show one class or method, picked from a file's **Structure** column.
- **Like the editor**: semantic colours, inlay hints, problems and usage counts, and a gutter that sets breakpoints and runs tests.
- **Personal notes** sit beside the lines they belong to; select lines in a card to add a [Personal Note](/features/bookmarks-notes/).

## Follow the code

![Show Callers as Cards on Pricing.apply: the callers in Shelf.checkout and two ShelfTest tests open as cards around Pricing.java, each joined to it by a dashed edge labelled by name](/screenshots/map/references.png "With no language server running, the callers are found by name, and the edges say so.")

- **Go to Definition and Find Usages** inside a card answer with more cards, each joined by a labelled edge.
- **Callers, callees and references** open as cards around a declaration.
- **Hierarchies and the Symbol Graph** show a method's callers, callees, supertypes and subtypes as cards to walk.
- **No language server needed**: a running [language server](/features/lsp/) answers; without one, Editora matches names and draws those edges dashed.

## Review and understand a codebase

![Show recent changes on the Editora repo: rows tinted and underlined by how often they changed, with a tooltip reading 5 commits in 90 days, last: Adrian De Leon, 2 weeks ago, and Owners: @adriandeleon from .github/CODEOWNERS](/screenshots/map/recent-changes.png "Recent-change heat, with the commit count, the last author and the owners in the tooltip.")

- **Review Changes** shows every changed file and its diff in one card, or everything your branch changes, as a pull request does.
- **Dependencies** shows what a file or folder uses and what uses it, read from import lines with no build.
- **Code Metrics** scores hotspots: code that is both complex and often changed.
- **Recent changes** tints each row by how often it changed in 90 days, and names its owners from `CODEOWNERS`.
- **Project overview** sums up the README, build, languages, entry points and tests on the first Map of a session.

## Search and filter

![Git changed with Hide unmatched on: only the changed files and the folders that lead to them stay, with a 65 rows hidden badge, shown/total counts on each column and an N hidden line under each](/screenshots/map/filters.png "Git changed with Hide unmatched: only the changed files and their folders stay, and every column counts what it hid.")

- **Search the whole project** by name or path, `@` for declarations or `%` for text.
- **Filters** keep only open, unsaved, Git-changed, bookmarked, noted or problem files. **Hide unmatched** takes the rest away.

## Save views and share tours

![A trail playing: the bar reads Stop 2 of 4, Pricing.discountFor, the card highlights that method's lines and the stop's note is stuck beside it](/screenshots/map/trail.png "A trail at its second stop: the card shows the stop's lines, with its note beside it.")

- **Working sets** bring back exactly the view you saved.
- **Trails** are guided tours: cards as stops, a note at each, played with `Ctrl+Right` (`Cmd+Right` on macOS). They live in `.editora/map-trails/`, so the team can commit them.
- **Print, PDF and Export** output the whole Map.
- **Keyboard**: every row and button is a command you can bind to a key.

See the [Project Map guide](/docs/project-map/).
"""),
    new Feature("projects", WF, 1, false,
        "Projects",
        "Single-folder workspaces, each in its <strong>own window</strong> with its own saved session, explored as a file tree or on the Project Map, with settings the team can commit.",
        "Single-folder projects, each in its own window with a saved session, a file tree and Project Map, and settings a team can commit.",
        """
A project is a folder that opens in its own window and remembers how you left it, so switching between repositories never mixes their files, tabs or settings.

## One window per project

- **Open a folder** with `C-x C-p` in the Emacs keymap, or **Project: Open Folder…** in the palette; switch with `C-x p` or **Project: Switch…**.
- **Its own session**: open files with their carets and pins, the active tab, folds, the [editor-group](/features/editor-groups/) layout and the tool windows all come back.
- **Its own scope**: everything in a window acts on its project, and bookmarks, notes and [run configurations](/features/run-configurations/) belong to it.
- **Remote folders** are projects too: a [folder on a server](/features/remote-sftp/) gets its own window, tabs and trust.
- **Closing** a project returns you to the global session.

## Tree and map

The Project tool window shows the folder as a tree, with multi-select, drag-to-move and the file actions you expect. Its **Map** button opens the same project on the [Project Map](/features/project-map/), and both share one context menu, so New, rename, Local History, bookmarks, Git and the rest work in either.

To start a project from an archetype or a template, see [Starting a project](/features/starting-a-project/).

## Settings you can commit

A project can carry `.editora/settings.json`, whose values override yours for anyone who opens it:

- **Editing**: indentation, tab size, rulers, wrapping, trailing whitespace, the final newline and the spell-check language.
- **Tools**: the command each build tool runs, the Java, Python and JavaScript debugger programs, and the JDK and build tools the project uses.
- **Language servers**: which one runs for a language, and whether it runs at all.

One repository can then use two-space indentation and a JDK 17 server while another uses tabs and JDK 25, with no global setting to flip. The values apply once the folder is trusted, and `.editorconfig` still comes first. In a project window, Settings switches between **All projects** and **This project** at the top, and **Project: Edit Project Settings…** creates the file with an example. Appearance, keymap and fonts stay personal.

**On by default.** Turn it off in **Settings → Workspace** (**Enable projects**). See the [Projects guide](/docs/workspace/#projects).
"""),
    new Feature("starting-a-project", WF, 2, false,
        "Starting a project",
        "Start a project without a terminal: a Maven project from an archetype, a whole project from a template, a Git repository, and any of about sixty file types with the right package already written.",
        "Start a Maven project from an archetype or a project from a template, git init a folder, and add any of about sixty file types with the right package.",
        """
Editora creates projects as well as opening them, so a new idea goes from nothing to a project that runs without a trip to the terminal.

## New Maven Project

**Maven: New Maven Project…**, in the File menu and on a folder's **New ▸** submenu, builds a project from an archetype and opens it in its own window.

- **Pick an archetype** from eight that ship with the editor and build on a current JDK, or **Load full catalog…** for anything on Maven Central.
- **Ready to run**: a run configuration for the main class is seeded and that class is opened, so [Run](/features/run-files/) works on the first press.
- **Advanced** sets the Java release from the JDKs you have installed, and can update the generated pom's dependencies and plugins to their latest versions.
- **Inside another project's folder** works too: an ordinary jar project there is left alone, and an aggregator pom gains the new project as a module.

## New Project From Template

**Project: New Project From Template…** writes a multi-file [template](/features/file-templates/) into a new folder and opens it as a project, on the template's main file.

- **Python Project** gives a `src` layout, a test, a build file and a README that walks through a virtual environment and `pip install -e .`.
- **Node.js TypeScript Project** gives a `package.json`, `tsconfig.json`, a test and a README.
- **Your own** multi-file templates appear in the same picker.

## Git: Initialize Repository

**Git: Initialize Repository…** puts a folder under version control, defaulting to the project root, and the [Git](/features/git/) status bar, Commit window and change bars appear at once. A repository inside another one is refused, since that is nearly always a mistake.

## New files on any folder

A folder's **New ▸** submenu offers about sixty file types, grouped by family, from Java and web to data, docs and build files.

- **The right package**: **New ▸ Java ▸ Class** in `src/main/java/demo` writes `package demo;`, and `text.Slug` creates the sub-package.
- **The type you meant**: `notes.json` under Text File gives you JSON, not `notes.json.txt`.
- **Safe**: an existing file is never overwritten.

See the [Build tools guide](/docs/build-tools/#new-maven-project).
"""),
    new Feature("editor-groups", WF, 3, false,
        "Editor groups",
        "Split the editor into independent groups with their own tabs to see two files at once, nest the splits, drag a tab between them, and get the whole arrangement back on the next launch.",
        "Split the editor into groups with their own tabs, nest the splits, drag tabs between them, and get the same layout back on the next launch.",
        """
---
---
Put two or more files side by side, each group with its own tabs, so a header sits beside its implementation or a test beside what it tests.

- **Split** with **View: Split Editor Group Right** or **View: Split Editor Group Down**, which move the current file into a new group.
- **Nest** the splits: a side-by-side pair can hold a stacked pair. Splitting the same way twice gives three even columns.
- **Drag a tab** onto another group to move it there, or onto a group's edge to split that group; a highlight shows where it will land.
- **Move between them** with **View: Move File to Next Editor Group** and **View: Focus Next Editor Group**, and **View: Merge Editor Groups** puts everything back in one.
- **No empty panes**: closing a group's last file closes the group.
- **Restored on launch**: the layout is saved with the [project](/features/projects/) session.

All five commands are in the palette and can be bound to keys. To see two parts of the *same* file, **View: Split Editor — Side by Side** (`C-x 3` in the Emacs keymap) splits one file into two views, and it combines with groups.

See the [Editor groups guide](/docs/workspace/#editor-groups-two-files-at-once).
"""),
    new Feature("bookmarks-notes", WF, 4, false,
        "Bookmarks & notes",
        "Bookmarks to come back to and Markdown Personal Notes on code, both kept outside your source files and listed across a project.",
        "Line bookmarks with a gutter marker, and Markdown personal notes on words, lines, ranges, files and folders, kept outside your source files.",
        """
Mark the places you want to come back to, and keep notes on code without writing them into the file. Both follow the code as it changes and belong to the [project](/features/projects/) you are in.

## Bookmarks

- **Toggle** a bookmark on a line with `C-c m` in the Emacs keymap, or **Add / Remove Bookmark** in the editor's right-click menu. A gutter marker shows it, and it can carry a short note.
- **Jump** with `C-c ]` and `C-c [` within a file, or `M-g b` to pick one from any file. The other keymaps have **Bookmarks: Jump…** in the palette.
- **Mnemonics**: **Bookmarks: Set Mnemonic** gives a bookmark one character, and each digit has a command you can bind to a single key.
- **The Bookmarks window** lists them across files in an order you choose, by drag or Alt+Up and Alt+Down.

## Personal Notes

- **Add a note** to a word, a line, a range, a file or a folder with `C-c n` in the Emacs keymap, or **Add Personal Note** in the right-click menu.
- **Outside the file**, so notes work on read-only, generated or shared code, and on context that does not belong in source.
- **Markdown**: a noted span is tinted, and hovering it shows the note rendered.
- **Follow the code** through edits, renames and outside changes.
- **Find them again** with `M-g n` in the Emacs keymap, **Search Notes**, or the Personal Notes window. Folder notes show as tooltips in the Project tree.
- **Resolve** a note with **Toggle Note Resolved** when it is done, or reopen it the same way.
- **On the [Project Map](/features/project-map/)**, notes sit beside the card lines they belong to.

**On by default.** Turn Personal Notes off in **Settings → Workspace → Features** (**Enable Personal Notes**). See the [Bookmarks & notes guide](/docs/bookmarks-notes/).
"""),
    new Feature("find-in-files", WF, 5, false,
        "Find in files",
        "Project-wide search and replace with results grouped by file, unsaved text included, fast with ripgrep, plus incremental find in the file and AceJump.",
        "Project-wide search and replace with results grouped by file, using ripgrep when installed and on the host for remote folders, plus AceJump.",
        """
Search and replace across a whole project, unsaved edits included, and get answers quickly even in a large repository or on a server.

## Search the project

- **Find in Files** opens with `C-S-f` in the Emacs keymap and `Ctrl+Shift+F` in the others (`Cmd+Shift+F` on macOS), with matches grouped by file.
- **Case, regex and whole-word** options, plus include and exclude globs, narrow what it finds.
- **Unsaved text counts**: open files are searched as they are in the editor, not as they are on disk.
- **Skips the noise**: what `.gitignore` excludes, every `node_modules` folder, binary files and files over 2 MB.
- **Fast with ripgrep** when it is installed. In a [remote folder](/features/remote-sftp/), the host's own ripgrep searches in one command.
- **Find in Files (Popup)** is a quick, keyboard-first search when you do not want the tool window.

## Replace across files

**Replace in files** rewrites the matches after a confirmation. Open files change in the editor, where Undo works, and closed files keep their line endings.

## Find in the current file

- **Incremental search** with `C-s` and `C-r` in the Emacs keymap, `Ctrl+F` in the others (`Cmd+F` on macOS), highlights every match and counts them.
- **Go: AceJump**, `M-g j` in the Emacs keymap, labels each visible spot so you can jump the caret there by typing its label.

See the [Find in files guide](/docs/navigation/#find-in-files).
"""),
    new Feature("file-templates", WF, 6, false,
        "File templates",
        "New File From Template: single- and multi-file scaffolds that fill in the author, date, file name and package for you, with 37 bundled and room for your own.",
        "New File From Template: 37 bundled single- and multi-file templates that ask for their variables, plus templates of your own.",
        """
Start a file from a template instead of a blank page or a copy of the last one. **Template: New File From Template…**, `C-c C-n` in the Emacs keymap or the toolbar button, asks only for what it cannot fill in, and every field starts filled, so **Create** alone gives a working file.

## What ships

Editora ships 37 templates:

- **Code**: a Java class, a JUnit 5 test, Python, Node.js, shell and Zsh scripts, an HTML page, and a Maven `pom.xml`.
- **XML**: a schema, an XSLT stylesheet, and an XML file bound to a schema so it validates from the start.
- **Files Editora previews or runs**: Markdown, Typst, Mermaid, Markwhen and HTTP requests with environment files.
- **Writing**: a README, a changelog, a decision record, a blog post and meeting notes.
- **Project files**: MIT and Apache 2.0 licenses, `.gitignore`, `.editorconfig`, GitHub issue and PR templates, and a Java Dockerfile.
- **Whole projects**: multi-file templates start a project; see [Starting a project](/features/starting-a-project/).

## How it fills in

- **Variables** such as the author, the date, the file name and the package are filled in for you, and the caret lands where the template says.
- **Nothing overwritten**: before writing, a template lists any files that already exist.
- **Fits the project**: files follow its `.editorconfig`, and a script that starts with `#!` is made executable.

## Your own templates

Add templates under `~/.editora/templates/`, or use **Template: Edit User Templates…** to copy a bundled one and change it.

See the [File templates guide](/docs/snippets-templates/#file-templates).
"""),
    new Feature("read-only-view-mode", WF, 7, false,
        "Read-only / View mode",
        "Toggle a buffer read-only to browse without editing, with a View Mode banner and pager-style <kbd>Space</kbd>/<kbd>Backspace</kbd> paging.",
        "Toggle a buffer read-only with a View mode banner and pager keys. Files of 200,000 lines or 16 MB, very long lines and minified code open in View mode.",
        """
Read a file without the risk of changing it by accident. In View mode typing and edit commands are blocked, while highlighting, folding, search and copy keep working.

- **Toggle it** with `C-x C-q` in the Emacs keymap, or **View: Toggle Read-Only (View Mode)** in the palette.
- **A banner** above the editor says the file is for viewing, with an **Enable Editing** button when the file is writable.
- **Page like a pager**: Space pages down and Backspace pages up.
- **Files you cannot write** open read-only, and Save asks before replacing one.
- **Huge files open in View mode**: 200,000 lines or 16 MB or more, a line of a million characters, or minified code. The banner says why.
- **Remembered per file**, so a file you set read-only stays that way.

See the [Read-only and View mode guide](/docs/editing/#read-only-and-view-mode).
"""),
    new Feature("remote-sftp", WF, 8, true,
        "Remote files (SFTP)",
        "Edit files on a remote host over SSH/SFTP, each remote folder a project in its own window, and run Git, builds, tests, language servers and debuggers on the host, in a container, or in a WSL distribution.",
        "Edit a remote folder over SSH and SFTP as a project, and run Git, builds, tests, language servers and debuggers on the host, in a container or in WSL.",
        """
---
---
Open a folder on a server and work in it as if it were on your machine: Editora edits the files over SSH and runs your tools on the host, so nothing has to be copied back and forth.

## Connect

- **Remote: Connect to SFTP…** opens the remote folder as a [project](/features/projects/) in its own window. Reopening it asks to connect, and your tabs come back.
- **Saved sites** live in the **Remote Sites** tool window (`M-g r` in the Emacs keymap), in **Settings → Remote** and on the Welcome page.
- **Logs in like `ssh`**: your SSH agent's keys first, so security keys and smartcards work, then the keys `~/.ssh/config` names, then your default keys. A key file or a password works too.
- **Reads `~/.ssh/config`**: its hosts are offered in the form, and ProxyJump and Include are followed.
- **Asks while it logs in** for a password, passphrase or 2FA code, and never stores secrets.

## Work on the host

Editing, highlighting, search, bookmarks, notes and preview all work on remote files, and Save writes straight back. On a host you allow to run commands, which Editora asks once per host, the tools run there over the same connection:

- **Git and `gh`**, with the host's own repository.
- **Find in Files** with the host's ripgrep, in one command instead of reading every file.
- **Run, builds and tests**, with output and Stop as for a local build.
- **Language servers and debuggers** for Python, JavaScript and Java.
- **A [terminal](/features/terminal/)** on the host, in the project folder.
- **Outside changes**, such as a `git pull` on the host, update the tree, Git and open files.

## Tools in a container or WSL

A project on this machine can have its tools run elsewhere while you edit its files here:

- **A container**: the Docker or Podman container the folder is bind-mounted into, found from its dev container or picked with **Containers: Run This Project's Tools in a Container…**.
- **WSL**: on Windows, the distribution a `\\\\wsl.localhost\\…` folder lives in.

Git, Run, builds, tests, language servers and debuggers then run there, with its own PATH, environment and user. Editora asks once per container or distribution, and **Containers: Run This Project's Tools Here** goes back.

See the [Remote files guide](/docs/remote/).
"""),
    new Feature("file-encodings", WF, 14, false,
        "File encodings",
        "Reopen a file in the encoding it is really in, or save it in another one: Shift_JIS, GBK, windows-1251 and every other encoding the JDK knows. The choice is remembered per file.",
        "Reopen a file in the encoding it is really in, or save it in another, from Shift_JIS to windows-1251. The choice is remembered per file.",
        """
---
---
Open a file in the encoding it was really written in, or save it in the one another tool expects, without a byte lost on the way. Editora reads UTF-8 unless a byte-order mark or [`.editorconfig`](/features/editorconfig/) says otherwise, and a file that is not valid UTF-8 still opens with every byte kept, its encoding drawn in the warning colour in the status bar because it is a guess.

Click the encoding in the status bar, or use the palette:

- **Buffer: Reopen with Encoding…** reads the file again in the encoding you pick. If the bytes are not valid in it, nothing changes and the status bar names the first place that failed.
- **Buffer: Save with Encoding…** writes the file in another encoding from its next save. Text that encoding cannot hold is refused, naming the character and its line, rather than written as question marks.
- **Every encoding the runtime knows**: the common ones come first, grouped by script, from Shift_JIS and GBK to windows-1251, and the list is the same on every system.
- **Remembered per file**: the choice holds after a restart and a rename, and works on a [remote host](/features/remote-sftp/), where `.editorconfig` is not read.
- **Safe**: unsaved changes are confirmed before a reopen, and Local History keeps a copy.

See the [File encodings guide](/docs/workspace/#file-encodings).
"""),
    new Feature("crash-recovery", WF, 13, false,
        "Crash recovery",
        "Unsaved edits, including untitled buffers, are kept while you work and offered back on the next launch if Editora did not close normally. Restoring never writes to your files.",
        "Editora keeps a recovery copy of unsaved edits, untitled buffers included, and offers them back as unsaved tabs after a crash.",
        """
---
---
Editora keeps a recovery copy of every buffer with unsaved edits, untitled ones included, in every window. The copies live in the config folder and are written as you work.

If Editora did not close normally (a crash, a kill, a logout, a power loss), the next launch offers the edits back.

- Restoring opens the text as **unsaved tabs**. It never writes to your files, so you decide what to save.
- A file that changed on disk in the meantime is flagged.
- A copy is removed when you save, revert or close the buffer.
- If a refactoring that moves or deletes files is interrupted, the next time the project is opened Editora offers to put the files back.

Buffers over about 16.8 million characters are not covered. **File: Recover Unsaved Edits…** shows what is kept.

**On by default.** Turn it off in **Settings → Workspace** (**Keep a recovery copy of unsaved edits**) or with **View: Toggle Crash Recovery**. See the [Crash recovery guide](/docs/undo-history/#crash-recovery).
"""),
    new Feature("local-file-history", WF, 9, false,
        "Local History",
        "Snapshots of your files over time, taken on save, auto-save, and before an external reload, so you can diff or restore an earlier version with no Git required.",
        "Snapshots of your files on save, auto-save and external reloads, to diff or restore an earlier version, with labels and deleted-file recovery. No Git needed.",
        """
---
---
Editora keeps earlier versions of your files on this machine, so a bad edit, a lost afternoon or an accidental delete can be undone even in a folder that is not in Git.

## What it keeps

- **Every save and auto-save** becomes a revision, and the version you started the session from is kept too.
- **Before anything replaces your text**: an outside change, a revert, Replace in Files, an AI agent's write or a Git discard each keep a copy first.
- **Deleted files**: deleting a file in Editora snapshots it first, so it can come back.

## Look back and restore

- **The Local History window** opens with `M-g l` in the Emacs keymap, `Alt+Shift+H` in the others (`Ctrl+Cmd+H` on macOS), and lists the file's revisions with when and why each was recorded.
- **Compare** any revision with the current file side by side, and the diff follows your edits.
- **Restore** the whole revision, or copy back single changes with the arrows in the diff. Both are undoable, and nothing is written until you save.
- **Labels**: **Local History: Put Label…** names a moment, such as "before the refactor", and labelled versions are kept longer.
- **Local History: Recent Changes…** lists the project's latest revisions across files.
- **A folder's history**: right-click a folder in the Project tree to see every file under it, deleted ones struck through, and bring one back.

History follows a file when you rename or move it, and old revisions are pruned by limits you set.

**On by default.** Turn it off in **Settings → Workspace → Local History**. See the [Local History guide](/docs/workspace/#local-file-history).
"""),
    new Feature("todo-highlighting", WF, 10, false,
        "TODO highlighting",
        "Configurable regex patterns (TODO, FIXME, and your own) are highlighted in the editor and collected in a TODO tool window, with scrollbar and minimap stripes.",
        "TODO, FIXME and your own regex patterns are highlighted in the editor, listed in a TODO window, and marked on the scrollbar and minimap.",
        """
---
---
Every TODO, FIXME and pattern of your own stands out where it is written and is collected in one list, so the notes you leave in code get picked up again.

## See them

- **Highlighted inline**, each pattern in its own colour.
- **Stripes** on the scrollbar and the minimap edge show where they are; click one to jump.
- **The TODO window** opens with `M-g o` in the Emacs keymap, or **Tool Window: TODO** in the palette, and lists every match in the project, grouped by file.
- **Next and previous** match in the file with `M-g ]` and `M-g [` in the Emacs keymap, or **TODO: Next Match** in the palette.

## Organize them

- **Tags and priorities**: write `TODO [auth] (high) fix token refresh`, and the TODO window groups matches by file, priority, tag or keyword.
- **Edit from the list**: right-click a match to mark it done, set its priority or change its text, and the source changes to match.

## Choose the patterns

**Settings → TODO → TODO highlighting** sets each pattern's name, regex, colour and case. Six ship with Editora: TODO, FIXME, HACK, NOTE, XXX and DONE. A new pattern's regex fills in from its name.

**On by default.** Turn it off in **Settings → TODO → TODO highlighting** (**Highlight TODO/FIXME patterns**). See the [TODO highlighting guide](/docs/todo/).
"""),
    new Feature("log-viewer", WF, 11, false,
        "Server log viewer",
        "Open a log file, a <code>.gz</code> log or a live stream from Docker, Kubernetes, journald or SSH for severity highlighting, Follow, filters by level, pattern and time, a summary, and logs merged by time.",
        "A log mode with severity colours, Follow, filters by level, pattern and time, summaries, merged logs, and live streams from Docker, Kubernetes or SSH.",
        """
---
---
Editora reads server logs as records, not lines: each level in its own colour, new lines streaming in as they are written, and filters that cut a long log down to the part that matters. `.log` files open this way, and so do rotated logs, `catalina.out`, `syslog` and files whose content looks like a log.

## Read it

- **Severity colours** mark FATAL, ERROR, WARN, INFO, DEBUG and TRACE inline and in a bar down the left edge, for the common Java, syslog, web-server, .NET and JSON formats.
- **Warnings and errors on the scrollbar**, in amber and red; hover to read one, click to go there.
- **Compressed logs** such as `app.log.2.gz` open directly.

## Follow it live

- **Follow** streams new lines as the file grows, like `tail -f`, and keeps going when the log is rotated. Toggle it with `C-c C-l t` in the Emacs keymap, `Ctrl+Alt+Shift+L` in the others (`Cmd+Option+Shift+L` on macOS).
- **Very large logs** open at the tail, so the newest lines are there at once.
- **Log: Open Log Stream…** follows `docker logs -f`, `kubectl logs -f`, `journalctl -f`, a file over SSH or any command, with the same filters.

## Narrow it down

- **Level and pattern** filters keep only what you want, as you type. `C-c C-l f` in the Emacs keymap focuses them, `Ctrl+Alt+Shift+F` in the others (`Cmd+Option+Shift+F` on macOS).
- **Hide** drops records you never want to see, such as health checks.
- **Whole records**: a matched line brings its stack trace, lines keep their real numbers, and the bar counts what is showing.
- **Times**: **Log: Go to Time…** jumps to a moment, and **Log: Filter by Time…** keeps the records between two times.
- **Remembered filters**: a log opens with the filters you left on it.

## Make sense of it

- **Log: Summarize** reports the levels, records over time, the most frequent messages and the longest silences.
- **Log: Merge Logs…** opens several logs as one, in the order their records were written.

Logs open in View mode, read-only with an **Enable Editing** banner, and Follow keeps streaming.

**On by default.** Turn it off in **Settings → Editor → Logs**. See the [Server log viewer guide](/docs/log-viewer/).
"""),
    new Feature("csv", WF, 12, false,
        "CSV & TSV support",
        "Rainbow per-column coloring, a field readout, and an editable CSV grid with sort/filter, print, and export to PDF/Excel/ODF, plus align/shrink and Markdown-table interop.",
        "Rainbow CSV and TSV columns, a field readout, an editable grid with sort and filter, column alignment, and export to PDF, Excel and ODF.",
        """
---
---
Work with CSV and TSV files as a table when you want to read or edit the data, or as text with every column in its own colour when you want to see the raw file.

## In the editor

- **Rainbow columns** give each column its own colour, cycling through eight, so rows line up at a glance.
- **Field readout**: the status bar says which field the caret is in, as *Field N of M*.
- **CSV: Align Columns** pads fields so the delimiters line up, and **CSV: Shrink Columns** takes the padding out again. Quoted fields are kept intact.

## As a grid

- **The CSV Grid** shows the file as a spreadsheet in its preview, beside the text or instead of it.
- **Edit cells and headers**, and the changes go straight back to the file.
- **Sort and filter** by column, and spot rows with the wrong number of fields, which are highlighted.

## Print, export and Markdown

- **Print, PDF, Excel and ODF**: the grid's right-click menu outputs exactly what it shows, the filtered rows in the sorted order.
- **CSV: Copy as Markdown Table** copies the file as a table for a README or an issue, and a Markdown table converts back to CSV.

**On by default.** Turn rainbow columns and the grid off in **Settings → Editor → CSV**. See the [CSV & TSV guide](/docs/csv/).
"""),
    new Feature("terminal", WF, 15, false,
        "Terminal",
        "Shells in a Terminal tool window, on this machine or where the project runs: an SSH host, a container or WSL. Shell integration, find, and clickable paths.",
        "Shells in a Terminal tool window, on this machine, an SSH host, a container or WSL, with shell integration, find and clickable paths.",
        """
---
---
The **Terminal** tool window runs shells inside Editora, one per tab, below the editor. It opens with `M-0` in the Emacs keymap, ``Ctrl+` `` in the CUA, Sublime Text and VS Code keymaps and `Alt+F12` in IntelliJ IDEA's (`Option+F12` on macOS), and **Open Terminal Here** on a tab, a folder in the Project tree or the breadcrumb starts a shell in that folder. A focused terminal owns the keyboard, so `Ctrl+C`, `Escape` and your shell's own shortcuts work as in any terminal, and `Shift+Escape` goes back to the editor.

- **Where the project runs.** A [remote project](/features/remote-sftp/)'s terminal is a shell on its SSH host, and a folder whose tools run in a dev container or a WSL distribution gets a shell inside it. Each tab's chip names where its shell runs. When a connection drops, the output stays under a **Reconnect** banner.
- **Shell integration** for bash and zsh: the tab follows the shell's folder, `Ctrl+Shift+Up` and `Ctrl+Shift+Down` jump between prompts, and **Copy Last Command Output** copies what the last command printed. Your own startup files run as usual and are never changed.
- **Open what the output names.** `Ctrl`-click (`Cmd`-click on macOS) a file name, a `path:line:column` from a compiler or a stack trace, a folder or a URL. Hyperlinks that programs print (OSC 8) work the same way.
- **Find** in the scrollback with `Ctrl+Shift+F` (`Cmd+F` on macOS).
- Full-screen programs such as vim, htop and less work, and programs that speak the **kitty keyboard protocol**, such as Neovim and Helix, get unambiguous keys.
- **Type-ahead** shows what you type at once on a slow link, before the host echoes it.
- Programs can post **desktop notifications** (OSC 9 and OSC 777) and copy to the clipboard (OSC 52), but can never read it.
- A [run configuration](/features/run-configurations/) can run its program in a terminal tab, for programs that draw on the screen or read single keys.

Colours follow the editor theme, or a fixed terminal scheme such as Solarized, Dracula or Nord. The font is the editor's, and the icons of `eza` and powerline prompts show without installing a Nerd Font. **Settings → Terminal** holds the shell, the scrollback and the rest.

See the [Terminal guide](/docs/terminal/).
"""),
    new Feature("encrypted-files", WF, 16, false,
        "Encrypted files",
        "Open, edit and save GnuPG-encrypted <code>.gpg</code>, <code>.pgp</code> and <code>.asc</code> files as the text inside them, with your own <code>gpg</code> and keyring. The plain text never reaches the disk.",
        "Edit GnuPG-encrypted .gpg, .pgp and .asc files as their plain text, with your own gpg and keyring. The plain text never reaches the disk.",
        """
---
---
Open `notes.md.gpg` and you edit a Markdown file, with Markdown highlighting and preview. Save encrypts it again, for the same people and in the same format, before anything reaches the disk; a save that can't be encrypted writes nothing and says why. Editora uses the `gpg` you already have, with your keyring, trust settings, smartcards and passphrase cache, and never asks you to export a key.

- **Encrypt File…** writes the active file encrypted beside the original (`notes.md` → `notes.md.gpg`, or `.asc` for ASCII armor), and Save As to a `.gpg` or `.asc` name does the same. The plain original stays until you choose **Move Original to Trash** or **Keep It**.
- The **recipient picker** chooses who can open the file: people's public keys from your keyring, your own included by default, or a passphrase. A key you haven't verified shows its fingerprint, and a folder's `.gpg-id` file, as the `pass` password manager writes it, sets the default recipients.
- The tab has a lock, and the status bar says who the file is encrypted for. **Lock File** takes the text out of memory until you decrypt it again, and tabs restored at startup stay locked until you press Enter, so startup never asks for a run of passphrases.
- **Encrypt Selection…** and **Decrypt Block at Caret** handle an armored message inside an otherwise plain text, and **Clear-Sign Text**, **Sign File (Detached)** and **Verify Signature** sign and check text and files.

While an encrypted file is open, Editora keeps its text out of crash recovery, Local History, personal notes, Git change bars and blame, the HTML Live Preview, agents and AI actions, and language servers unless you allow them. Export, print and external tools ask first. Files in a [remote project](/features/remote-sftp/) are decrypted on your computer, where your keys are.

See the [Encrypted files guide](/docs/encrypted-files/).
"""),
    new Feature("ai", CE, 0, true,
        "AI assistance",
        "One-shot AI actions (explain, rewrite, commit message, inline completion) and an embedded coding agent over ACP. Anthropic, Codex, or a local model. Off by default.",
        "Optional AI actions (explain, rewrite, commit messages, inline completion) and an ACP coding agent, on Anthropic, Codex or a local model. Off by default.",
        """
AI in Editora stays out of the way until you ask for it: quick actions on the code in front of you, or a coding agent whose edits wait for your review.

## Quick actions

- **AI: Generate Commit Message** writes a message from the staged diff into the Commit window.
- **AI: Explain Selection** explains the selected code in a Markdown buffer of its own.
- **AI: Rewrite Selection…** rewrites the selection to your instruction, as one edit you can undo.
- **Inline completion** shows a muted one-line suggestion at the caret after a pause in typing, and Tab accepts it. It has its own switch and its own fast model.

## A coding agent beside your code

- **Your choice of agent.** Claude Code is the default, and Gemini CLI, GitHub Copilot CLI, Codex, Qwen Code and OpenCode also work, over the [Agent Client Protocol](https://agentclientprotocol.com). You install the agent; Editora never bundles one.
- **It sees what you see.** The agent's reads include your unsaved changes.
- **You review every edit.** Its edits to open files arrive as undoable buffer edits that you save yourself, and each action asks your permission first.
- **It stays in the project.** Its file reads and writes stay inside the project folder of its window.

## Your model, your keys

- **Anthropic API**, with the key from `ANTHROPIC_API_KEY` or Settings.
- **A local model** through any OpenAI-compatible server, or through LM Studio or Bionic, which can also run the agent through OpenCode.
- **Codex**, through your existing Codex login. It runs the quick actions but not inline completion.
- **Keys stay where they belong.** Each provider keeps its own key, endpoint and model, so switching provider never sends a key to another provider's server. Keys typed into Settings go in the system keychain, never in `settings.json`.

**Off by default.** Turn it on in **Settings → AI**. See the [AI guide](/docs/ai/#turning-it-on).
"""),
    new Feature("themes-fonts", CE, 1, false,
        "Themes & fonts",
        "<strong>Editora Light</strong> and <strong>Editora Dark</strong>, plus 28 more (High Contrast, Primer, Nord, Cupertino, Dracula and the community set). Five bundled monospace fonts, no install needed, and you can drop in a theme of your own.",
        "Thirty themes, including Editora Light and Dark and a High Contrast pair, five bundled monospace fonts, and themes of your own without a restart.",
        """
Editora looks at home on any system and can follow it between light and dark. Thirty themes ship with it, and you can add your own without a restart.

## Pick a theme

- **Editora Light and Editora Dark** are the app's own pair, drawn from its icon: a teal accent, an ink-navy ground, and periwinkle kept for keybindings. A fresh install starts in Editora Light.
- **System**, first in the picker, shows Editora Light while your system is light and Editora Dark while it is dark, and switches as soon as the system does.
- **High Contrast Light and Dark** put text, accents and syntax colours at 7:1 or better. While Windows' high-contrast mode is on, Editora switches to the matching one by itself.
- **Primer, Nord and Cupertino** come in light and dark, alongside **Dracula** and nineteen community themes in twelve families.
- **Syntax colours to match.** Each theme colours the syntax, the editor surface, the gutter and the project tree together. The editor theme is **Automatic** and follows the app theme until you pick one of its own.

## Colours that mean one thing

**Amber** means "not saved yet" on the tab, the Switcher, the file tree and the pickers, in every theme. **Red** is broken, **green** is verified, and **periwinkle** is only ever a keybinding.

## Fonts

Five monospace fonts ship with the app, so nothing needs installing: **JetBrains Mono**, **Cascadia Code**, **Fira Code**, **IBM Plex Mono** and **Source Code Pro**. Each editor has its own **text zoom**. The interface is set in **Inter** on every platform, so Editora looks the same wherever you run it.

## Bring your own

Drop a stylesheet into `themes/` in your config directory for a full app theme, or into `editor-themes/` for syntax colours only. It appears under **Your themes** in the picker. **Reload themes** in **Settings → Appearance**, or **Theme: Reload User Themes** in the palette, picks it up without a restart.

See the [Themes and fonts guide](/docs/themes-fonts/#themes).
"""),
    new Feature("plugins", CE, 2, false,
        "Plugins",
        "Extend the editor with commands, tool windows, and integrations. Install from the built-in signed registry, or write your own (Java or a simple manifest).",
        "Add commands, keybindings and tool windows with plugins written in Java or declared in a manifest, installed from a signed registry after you consent.",
        """
Plugins extend Editora without forking it. A plugin adds commands, keybindings, tool windows, editor-menu items and status-bar segments.

- **Write one in Java** against a small API, or **declare one** in a `plugin.json` manifest with folders of snippets and templates.
- **Install from the registry** with **Plugins: Browse Plugins**. The registry index is signed, and every download is checked against its SHA-256 hash. Browse it on the web in the [plugins catalog](/plugins/).
- **Install from a file** with **Plugins: Install from File…** for a local `.zip`.

## You stay in control

Plugins are not sandboxed, so install only ones you trust. Editora helps you decide:

- **It asks first.** The consent dialog lists a plugin's code, the external commands it runs and the chords it replaces. Nothing is installed until you agree.
- **It asks again** when an update changes the code, commands or chords you agreed to.
- **Built-ins stay safe.** A plugin's commands live in its own namespace, so it cannot replace a built-in command.
- **Trouble is easy to rule out.** Start with `--disable-plugins` for a session without any, and [Doctor](/features/doctor/) lists a plugin that failed to load or start, or started slowly.

**Off by default.** Turn it on in **Settings → Plugins**. See the [Plugins guide](/docs/plugins/#installing).
"""),
    new Feature("zen-mode", CE, 3, false,
        "Zen mode",
        "Hides every bar, the line numbers, and the minimap, leaving only the text. Everything you have switched on keeps running.",
        "Zen mode hides every bar, the line numbers and the minimap, leaving only the text, while every feature and keybinding keeps working.",
        """
Zen mode leaves only the text of the file. It hides the menu bar, toolbar, tab bar, breadcrumb, tool-window stripes, and status bar, along with the line numbers, the minimap, the rulers, and the indent guides. Fold markers stay in the margin.

Nothing is switched off. Language servers, Git, and every command and keybinding keep working, so the palette (`M-x` in the Emacs keymap) and your chords still reach everything while the interface is hidden.

- Toggle it with `C-c z` in the Emacs keymap, with **View: Toggle Zen Mode** from the palette, from **Settings → Interface → Modes**, or start in it with the `--zen` flag.
- A small floating **Z** button in the corner turns it off again.
- It applies to one window and never changes your saved settings, so the interface comes back as you left it.
- Zen and [Expert mode](/features/expert-mode/) are mutually exclusive: turning one on turns the other off.

The other three ways to run the editor are [Expert mode](/features/expert-mode/), [Simple UI mode](/features/simple-ui-mode/), and the [full IDE](/features/full-ide/).

See the [Focus modes guide](/docs/workspace/#focus-modes).
"""),
    new Feature("expert-mode", CE, 4, false,
        "Expert mode",
        "A lighter focus mode than Zen: the menu bar, toolbar, tabs, breadcrumb, and tool stripes go, while line numbers, the minimap, and the status bar stay.",
        "A lighter focus mode: the menu bar, toolbar, tabs, breadcrumb and stripes go, while line numbers, minimap and status bar stay.",
        """
Expert mode removes the window chrome around the editor and keeps the editor itself complete. The [menu bar](/features/menu-bar/), toolbar, tab bar, breadcrumb, tool-window stripes, and whitespace guides are hidden; where the menus share the title bar, the title and the window buttons stay. Line numbers, the minimap, the rulers, the indent guides, the current-line highlight, and the status bar stay.

It suits people who drive the editor from the keyboard and want the room back without losing their bearings in the file. Nothing is switched off, so language servers, Git, and every command keep working.

- Toggle it with `C-c C-e` in the Emacs keymap, with **View: Toggle Expert Mode** from the palette, from **Settings → Interface → Modes**, or start in it with the `--expert` flag.
- A small floating **E** button in the corner turns it off again.
- It applies to one window and never changes your saved settings.
- Expert and [Zen mode](/features/zen-mode/) are mutually exclusive: turning one on turns the other off.

The other three ways to run the editor are [Zen mode](/features/zen-mode/), [Simple UI mode](/features/simple-ui-mode/), and the [full IDE](/features/full-ide/).

See the [Focus modes guide](/docs/workspace/#focus-modes).
"""),
    new Feature("simple-ui-mode", CE, 5, false,
        "Simple UI mode",
        "One switch turns Editora into a plain editor: no gutter, minimap or extra toolbars, and language servers, debugging, Git and AI off until you leave it.",
        "One switch hides the gutter, minimap and extra toolbars and turns off language servers, debugging, Git and AI for a calm, minimal editor.",
        """
Simple UI mode keeps only what you need to open, write and save files. It suits writing, and anyone starting out with code editors who wants a calm window.

- **Less on screen.** It hides the extra toolbar groups, the project selector, the tool-window stripe, the breadcrumb, the whole gutter, the minimap and most status-bar segments.
- **Fewer features running.** Language servers, debugging, Git, AI, multiple cursors, external tools and plugins are off while the mode is on.
- **A short menu bar.** The [menu bar](/features/menu-bar/) keeps File, Edit, Find, View and Help. The menus for switched-off features go, rather than sitting greyed out.
- **Trimmed right-click menus.** The tab, editor, Project tree and preview menus keep saving, editing, printing, closing and file management.
- **An easy way back.** The reduced View menu keeps the toggle, and turning the mode off restores everything exactly as it was.

Toggle it with **View: Toggle Simple UI Mode** in the palette, the toolbar button beside Settings, or **Settings → Interface → Modes**. The `--simple` flag starts one session in it.

The other three ways to run the editor are [Zen mode](/features/zen-mode/), [Expert mode](/features/expert-mode/) and the [full IDE](/features/full-ide/).

**Off by default.** Turn it on in **Settings → Interface → Modes**. See the [Focus modes guide](/docs/workspace/#focus-modes).
"""),
    new Feature("full-ide", CE, 6, false,
        "Full IDE",
        "The default layout: menu bar, toolbar, tabs, tool windows, gutter, minimap, and status bar, with every feature available.",
        "Editora's default layout: menu bar, toolbar, tabs, tool windows, gutter, minimap and status bar, with every feature available.",
        """
The full IDE is the layout Editora starts in. Nothing is hidden: the menu bar, toolbar, tab bar, breadcrumb, gutter, minimap, and status bar are all on screen, and tool windows such as Project, Git Log, TODO, and Output dock around the editor.

Some features work as soon as you open a folder, and some wait for their tool or for you to trust the folder:

- **Available from the start:** [build tools](/features/build-tools/), [previews](/features/previews/), [projects](/features/projects/), and [Local History](/features/local-file-history/).
- **Once their tool is installed:** [Git](/features/git/) and [GitHub](/features/github/), which use `git` and `gh`, [language servers](/features/lsp/) and [debugging](/features/debugging/). A file whose server is missing offers to install it, and servers that would run a project's build wait until you trust the folder.

Every piece of the interface can be shown or hidden on its own from the **View** menu or **Settings → Interface**. For a whole-window change, use one of the other three ways to run the editor: [Zen mode](/features/zen-mode/) and [Expert mode](/features/expert-mode/) hide parts of the interface and leave every feature running, and [Simple UI mode](/features/simple-ui-mode/) hides more and also switches the heavier features off.

See the [Window chrome guide](/docs/workspace/#window-chrome).
"""),
    new Feature("localized-ui", CE, 7, false,
        "Localized UI",
        "The whole interface in English, Italian, Spanish, French, Portuguese or German, chosen in Settings or taken from your system.",
        "Editora's interface in English, Italian, Spanish, French, Portuguese and German, picked in Settings or taken from your system locale.",
        """
Work in the language you think in. Every part of the interface is translated: the menus, the command palette, toolbar tooltips, tool windows, Settings, dialogs and the status bar.

- **Six languages:** English, Italian, Spanish, French, Portuguese and German. Every string exists in all six, so no screen is half translated.
- **Pick one** in **Settings → Appearance → Language**, or leave it on **Automatic (system)** to follow your system language.
- **Restart to apply.** The Language row says the change is waiting until you restart Editora.

See the [Settings guide](/docs/configuration/#the-settings-window).
"""),
    new Feature("mcp", CE, 8, true,
        "MCP server",
        "Lets an AI agent such as Claude Code read what is open in Editora and act through its commands. Loopback only, protected by a token, off by default.",
        "A loopback-only, token-authenticated MCP server inside Editora that lets an agent such as Claude Code read editor state and run commands. Off by default.",
        """
The MCP server lets an AI agent such as Claude Code work with the editor you have open. The agent sees your files, problems and Git status, and acts through Editora's own commands rather than behind its back.

- **Reads live state.** Open files and tabs, buffer text and the selection, diagnostics, document symbols, Git status, TODOs, find in files, and the list of commands.
- **Edits you can undo.** The agent changes buffers with undoable edits, saves them, and opens files inside the window's project.
- **Runs commands** from the same list the palette uses. Trusting a folder and installing or enabling plugins stay your decisions: the agent cannot run those commands.
- **Stays on this computer.** The server listens on loopback only and requires a token. The port and token change every time Editora starts.
- **Keeps out of private places.** Edits and saves refuse files in Editora's config folder and in version-control folders such as `.git/`.

While it runs, a status-bar **MCP** item copies a ready-to-paste `claude mcp add` command. **Settings → MCP Server** has the same **Copy Endpoint Command**, and **New Token** locks out every agent set up with the old token.

**Off by default.** Turn it on in **Settings → MCP Server** or with **View: Toggle MCP Server**, and accept the security notice. See the [MCP server guide](/docs/mcp/#enabling-and-connecting).
"""),
    new Feature("external-tools", CE, 9, false,
        "External tools",
        "Define your own CLI commands and run them on the current file or buffer, with <code>$Name$</code> macros, stdin piping, and output to a console or back into the text.",
        "Run your own command-line tools on the current file or selection, with $Name$ macros, stdin piping, and output to a console or back into the text.",
        """
Turn any command-line tool into an Editora command: run a checker on the current file, or pipe the selection through `jq`, `sort` or `sed` and put the result back.

- **Define a tool** in **Settings → External Tools** with its command, arguments, input and output. The list starts empty.
- **Fill in the context** with macros such as `$FilePath$`, `$SelectedText$` and `$LineNumber$`, eight in all, for the current file, selection, caret and project.
- **Send it text.** A tool can pipe the selection or the whole buffer to the command's standard input.
- **Choose where the output goes:** a read-only console, the selection, the whole buffer as one undoable edit, or the caret.
- **Run it like any command.** Each tool is its own palette command that you can bind to a key, and **External Tools: Run…** and **External Tools: Rerun Last** pick or repeat one.

Tools run in the background with a time limit, so a slow one never freezes the editor. External tools are off in [Simple UI mode](/features/simple-ui-mode/).

See the [External tools guide](/docs/external-tools/#macros).
"""),
    new Feature("settings-sync", CE, 11, true,
        "Settings sync",
        "Keeps your snippets, abbreviations, templates, personal dictionary and shortcuts the same on every computer, through a private Git repository you own. Off by default.",
        "Sync snippets, abbreviations, templates, your dictionary and shortcuts between computers through a private Git repository you own. Off by default.",
        """
Make a snippet on your laptop and find it on your desktop. There is no Editora account or server: sync runs through a private Git repository you own and signs in with your own Git credentials.

## Set it up

- **Connect** in **Settings → Sync** by pasting the repository URL, or use **Create on GitHub…** when `gh` is signed in. Connect first shows what the repository would bring.
- **Your own credentials.** Sync uses your Git credential helper or SSH key, and Editora never sees a token. A repository URL with a password or token in it is refused.
- **Several GitHub accounts.** Sync finds the `gh` account that can read the repository and keeps using it across `gh auth switch`.

## When it syncs

- **On its own** after startup, shortly after you change something, and every 15 minutes. Automatic syncs never prompt.
- **On demand** with **Settings Sync: Sync Now**. You can cancel a running sync until it starts writing files on this computer.

## When two computers disagree

- **Entry by entry.** Two computers that each added a snippet keep both. If the same entry changed on both, the one that syncs keeps its version and the other stays in the repository history.
- **Backups first.** Files a sync replaces are copied to a backups folder in the config directory.
- **Failures in plain sight.** A failing sync shows "Sync ⚠" in the status bar, and the Sync page shows a Connected, Not connected or Sync failing pill and says why Connect was refused.

## What stays on each computer

Shortcuts you changed sync for every keymap, but which keymap each computer uses does not. Other preferences in `settings.json`, macros and themes stay local.

**Off by default.** Turn it on in **Settings → Sync** by connecting a repository. See the [Settings sync guide](/docs/settings-sync/#setting-it-up).
"""),
    new Feature("doctor", CE, 10, false,
        "Doctor",
        "A health screen for every external tool Editora uses: what it found, which version, and where. Problems come first, with an <strong>Install…</strong> or <strong>Turn on</strong> button where Editora can fix it.",
        "Doctor checks every external tool Editora can use, shows what was found, its version and path, and installs or turns on what you need.",
        """
Editora uses the tools installed on your machine instead of bundling its own copies. Doctor shows in one tab what it found, which version it is and where it lives, and helps you fix what is missing.

Open it with **View: Doctor** from the **Tools** menu or the palette, from the Welcome page, or from the **Open Doctor** button in the message log.

## What it checks

Doctor checks Git and the GitHub CLI, ripgrep, GnuPG, the preview and diagram tools, every language server you turned on, the formatters and linters, every debug adapter, the interpreters Run uses, the build tools, the AI provider and agent, the container engine and WSL, and any plugin that failed to load. Rows are grouped by area, and an area where everything is OK folds to one line.

## Only what you use counts

- **Problems first.** **Needs Attention** at the top gathers what is missing and what is found but not usable yet, like a `gh` that is not signed in or a JDK too old to run a file.
- **No false alarms.** A missing tool counts only when the window needs it: a file of that language is open, the project has the tool's marker file, such as `pom.xml` or `go.mod`, or you set its command. Otherwise the row says **Not installed**.
- **Always current.** Every check probes the machine afresh, and the header says when it ran. In a project on an SSH host, in WSL or in a container, the header says the rows describe this computer.

## Fix it from the row

- **Install…** fetches the tool, **Turn on** switches on a feature that is off, **How to install** explains the steps where Editora cannot, and **Settings…** opens the page that configures it.
- **Update…** appears when a server Editora installs at a tested release is older than that release.
- **Check for Newer Versions** looks up the latest releases of the other tools, only when you click it, and lists them under **Updates available**.
- **Copy Report** puts the whole report, with the Editora version, OS and Java, on the clipboard for a bug report.

See the [Doctor guide](/docs/doctor/#reading-the-report).
""")
);

// Alt text for each feature's screenshot (public/screenshots/features/<slug>.*): what the
// picture shows, kept short. A feature with none falls back to "<Title> in Editora".
static final Map<String, String> ALT = Map.ofEntries(
    Map.entry("autocomplete", "A stand-in illustration of an editor, with faint lines of code and the word Autocomplete"),
    Map.entry("ai", "Settings → AI Agent with the agent on, Claude Code as the active client, and editor context included in prompts"),
    Map.entry("auto-close-brackets", "A Python script whose last line has auto-closed brackets and quotes around the caret"),
    Map.entry("bookmarks-notes", "The Personal Note dialog over a Java file, with bookmark markers in the gutter"),
    Map.entry("build-tools", "A pom.xml beside the Maven tool window, listing its lifecycle phases and profiles"),
    Map.entry("code-navigation", "The Go to Symbol in Project picker over a Java file, listing classes with their files and lines"),
    Map.entry("command-driven-core", "The command palette filtered by \"git\", listing Git commands with the highlighted one's description"),
    Map.entry("comment-toggling", "A Java method commented out as one block comment, its lines highlighted"),
    Map.entry("crash-recovery", "Settings → Workspace with \"Keep a recovery copy of unsaved edits\" turned on"),
    Map.entry("csv", "A CSV file shown as a table in Preview, with a filter box and a first-row-is-header switch"),
    Map.entry("debugging", "A Java program paused at a breakpoint, with inline values and the Debug window's call stack and variables"),
    Map.entry("diagrams", "A Graphviz DOT file in Split view, with the rendered graph beside its source"),
    Map.entry("diff-merge", "A Java file side by side with its HEAD version, the changed line and words highlighted"),
    Map.entry("doctor", "Doctor listing Git, the GitHub CLI, ripgrep and the diagram tools as found, with their versions"),
    Map.entry("editor-groups", "Two editor groups side by side: a CSV file on the left and a Markdown file on the right"),
    Map.entry("editorconfig", "An .editorconfig file with sections for all files, Java, XML, JSON and YAML"),
    Map.entry("emacs-heritage", "The command palette filtered by \"kill\", listing the Emacs kill commands with their chords"),
    Map.entry("expert-mode", "Expert mode: a Java file with only line numbers, the minimap and the status bar around it, and the E button that leaves the mode"),
    Map.entry("export-pdf-print", "Print preview of a Java file, page 1 of 2, with Page Setup and Print buttons"),
    Map.entry("external-tools", "The External Tools: Run picker listing three tools and what each does with its output"),
    Map.entry("file-templates", "The New File From Template picker listing Java, HTML, Markdown, Python and shell templates"),
    Map.entry("find-in-files", "Find in Files searching the project for \"themeFor\" with ripgrep: 10 matches in 6 files, grouped by file"),
    Map.entry("full-ide", "The full IDE: a Java file with the Project tree on the right and the Git Log window below, its commit graph topped by v0.29.0 on master"),
    Map.entry("git", "The Commit window beside a Java file: one changed file, two untracked, and a commit message typed"),
    Map.entry("github", "The GitHub tool window listing a repository's pull requests, with Issues and Runs tabs"),
    Map.entry("html-live-preview", "An HTML file in the editor, with the globe button at the top right that opens its live preview in a browser"),
    Map.entry("http-client", "An .http file with two requests and a test script, beside the JSON response of a 200 OK"),
    Map.entry("jump-to-popups", "The Jump to Structure picker listing a Java class and its methods"),
    Map.entry("keymaps", "The Keymap: Select picker offering Emacs, CUA, Sublime Text, Visual Studio Code and IntelliJ IDEA"),
    Map.entry("local-file-history", "The Local History window: a Java file's saved versions, with one compared against the current text"),
    Map.entry("localized-ui", "Editora in Spanish, with translated menus, Project tree and status bar"),
    Map.entry("log-viewer", "A .log file in View mode with levels coloured, a filter box, and an error's stack trace highlighted"),
    Map.entry("lsp", "Quick fixes for an unresolved ArrayList in a Java file, above the Problems window listing two errors"),
    Map.entry("macros", "The command palette filtered by \"macro\", listing the macro commands with F3, F4 and C-x e"),
    Map.entry("markdown-preview", "A Markdown file in Split view, with headings, a task list and a table rendered beside the source"),
    Map.entry("mcp", "Settings → MCP Server with the loopback MCP server turned on"),
    Map.entry("menu-bar", "The Git menu open, listing its commands with their keybindings and submenus such as Branches and Stash"),
    Map.entry("mermaid", "A Mermaid flowchart in Split view, with the rendered diagram beside its source"),
    Map.entry("multiple-cursors", "Five carets in a column of Java lines, the same word typed at each"),
    Map.entry("plugins", "The Browse Plugins picker listing registry plugins with their versions and descriptions"),
    Map.entry("previews", "A YAML config file in Split view: the source on the left and its data tree on the right, with the name, version, features and authors keys and their typed values"),
    Map.entry("project-map", "The Project Map: folder columns joined by curves, a Java file selected and its Structure column"),
    Map.entry("projects", "A Java file open in the Editora project, with the project's folders in the Project tree on the right and the Git menu in the menu bar"),
    Map.entry("read-only-view-mode", "A Java file in View mode, with an amber banner and an Enable Editing button"),
    Map.entry("remote-sftp", "The Connect to SFTP form with host, port, user, authentication, key file and path fields"),
    Map.entry("run-configurations", "A Python script run configuration: script, interpreter, arguments, working folder, console and Run on"),
    Map.entry("run-files", "A Python script with a gutter ▶, and its output in the Run window below"),
    Map.entry("search-everywhere", "Search Everywhere for \"theme\", with matching files and symbols in separate groups"),
    Map.entry("settings-sync", "Settings → Sync with a repository URL, Connect and Create on GitHub buttons, and what to sync"),
    Map.entry("simple-ui-mode", "Simple UI mode: a Java file with a short menu bar, a small toolbar, and no gutter or tool windows"),
    Map.entry("smart-indentation", "A Python file whose new line under a for statement is indented automatically"),
    Map.entry("snippets", "The Insert Snippet picker listing Java snippets such as main, class, record, enum and method"),
    Map.entry("spell-checking", "A Markdown paragraph with four misspelled words underlined in red"),
    Map.entry("starting-a-project", "The New Project From Template picker listing seven bundled templates, from HTML Page + CSS and Python Project to GitHub Issue + PR Templates"),
    Map.entry("syntax-highlighting", "A Java class in the Editora Light theme, with keywords, types, strings and comments in their own colours"),
    Map.entry("themes-fonts", "Settings → Appearance with font, size, theme and editor theme, and a live preview of Java code"),
    Map.entry("todo-highlighting", "TODO comments highlighted in a Java file, with the TODO window counting 1,309 matches in 163 files"),
    Map.entry("typst", "A Typst document in Split view, with the typeset page, headings and maths beside the source"),
    Map.entry("undo-history", "The Undo History window beside a Python file, listing each edit with its time"),
    Map.entry("zen-mode", "Zen mode: a Java file with no menus, tabs, toolbars or status bar around it"),
    Map.entry("file-encodings", "A Shift_JIS text file opened as ISO-8859-1, its encoding shown in the warning colour in the status bar, with Reopen with Encoding filtered to Shift_JIS"),
    Map.entry("line-and-text-tools", "Twelve selected Map.entry lines in a Java file with the String Manipulation picker filtered to sort"),
    Map.entry("rulers-indent-guides", "A Rust file with rulers at columns 60 and 80 and indent guides down each nested block"),
    Map.entry("structure-narrowing", "A Java method narrowed: an amber banner says lines 39 to 62 of 74 are in reach, with Widen, and the Structure window beside it"),
    Map.entry("terminal", "The Terminal tool window under a Java file, a local shell tab showing a git log graph in the project folder"),
    Map.entry("deployments", "The Deployments window with a staging deployment: host, user, authentication, folder on the host, Leave out and options"),
    Map.entry("encrypted-files", "Encrypting README.md: the recipient picker with two people chosen, a Format menu and an Encrypt for 2 People button")
    // No alt text (and no real screenshot) for autocomplete: its completion popup does not show in
    // a headless capture, so the page keeps a drawn stand-in.
);

String yaml(String s) {
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
}

// The house shape every feature page keeps, so pages stay readable as features are added (a page that gains one
// clause per release turns into a list nobody reads). A feature page sells and orients; the user guide holds the
// detail. The rules:
// - the body is 800 words at most, no bullet is over 60 words and no paragraph over 120 (split it, or move the
//   detail to the guide);
// - the body ends with a link to its guide page: "See the [X guide](/docs/page/#anchor).";
// - no command id appears in backticks: name the command by its palette title instead;
// - the card summary is 40 words at most.
static final int MAX_BODY_WORDS = 800, MAX_BULLET_WORDS = 60, MAX_PARAGRAPH_WORDS = 120, MAX_SUMMARY_WORDS = 40;
static final java.util.regex.Pattern GUIDE_LINK_END =
    java.util.regex.Pattern.compile("See the \\[[^\\]]+\\]\\(/docs/[^)\\s]+\\)\\.$");
static final java.util.regex.Pattern CODE = java.util.regex.Pattern.compile("`([^`]+)`");

static int words(String s) {
    String t = s.strip();
    return t.isEmpty() ? 0 : t.split("\\s+").length;
}

static List<String> houseShape(Feature f, java.util.Set<String> commandIds) {
    List<String> out = new java.util.ArrayList<>();
    String body = f.body().strip();
    if (words(body) > MAX_BODY_WORDS) {
        out.add(f.slug() + ": body is " + words(body) + " words (at most " + MAX_BODY_WORDS + ")");
    }
    for (String block : body.split("\n\\s*\n")) {
        String b = block.strip();
        if (b.startsWith("#") || b.startsWith("|") || b.startsWith("```")) continue;
        if (b.startsWith("- ") || b.startsWith("* ") || b.matches("(?s)\\d+\\. .*")) {
            for (String item : b.split("\n(?=[-*] |\\d+\\. )")) {
                if (words(item) > MAX_BULLET_WORDS) {
                    out.add(f.slug() + ": a bullet is " + words(item) + " words (at most " + MAX_BULLET_WORDS + "): "
                        + item.substring(0, Math.min(60, item.length())) + "…");
                }
            }
        } else if (words(b) > MAX_PARAGRAPH_WORDS) {
            out.add(f.slug() + ": a paragraph is " + words(b) + " words (at most " + MAX_PARAGRAPH_WORDS + "): "
                + b.substring(0, Math.min(60, b.length())) + "…");
        }
    }
    if (!GUIDE_LINK_END.matcher(body).find()) {
        out.add(f.slug() + ": the body does not end with \"See the [X guide](/docs/page/).\"");
    }
    var m = CODE.matcher(body);
    while (m.find()) {
        if (commandIds.contains(m.group(1))) {
            out.add(f.slug() + ": command id `" + m.group(1) + "` in the text (use its palette title)");
        }
    }
    int summaryWords = words(f.summary().replaceAll("<[^>]+>", ""));
    if (summaryWords > MAX_SUMMARY_WORDS) {
        out.add(f.slug() + ": summary is " + summaryWords + " words (at most " + MAX_SUMMARY_WORDS + ")");
    }
    return out;
}

// The command ids the app registers, read from the generated src/lib/commands.ts.
static java.util.Set<String> commandIds(Path featuresDir) throws IOException {
    Path commands = featuresDir.resolve("../../lib/commands.ts").normalize();
    var ids = new java.util.HashSet<String>();
    var m = java.util.regex.Pattern.compile("\\bid: \"([^\"]+)\"").matcher(Files.readString(commands));
    while (m.find()) ids.add(m.group(1));
    if (ids.size() < 100) throw new IllegalStateException("Read only " + ids.size() + " command ids from " + commands);
    return ids;
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
    for (String slug : ALT.keySet()) {
        if (FEATURES.stream().noneMatch(f -> f.slug().equals(slug))) {
            throw new IllegalStateException("Alt text for an unknown feature: " + slug);
        }
    }
    List<String> problems = new java.util.ArrayList<>();
    java.util.Set<String> commandIds = commandIds(dir);
    for (Feature f : FEATURES) {
        if (!ALT.containsKey(f.slug())) problems.add(f.slug() + ": no alt text for its screenshot");
        problems.addAll(houseShape(f, commandIds));
    }
    if (!problems.isEmpty()) {
        throw new IllegalStateException("Feature pages break the house shape (see the comment above houseShape):\n  "
            + String.join("\n  ", problems));
    }
    for (Feature f : FEATURES) {
        String fm = "---\n"
            + "title: " + yaml(f.title()) + "\n"
            + "group: " + yaml(f.group()) + "\n"
            + "order: " + f.order() + "\n"
            + "beta: " + f.beta() + "\n"
            + "summary: " + yaml(f.summary()) + "\n"
            + "description: " + yaml(f.description()) + "\n"
            + (ALT.containsKey(f.slug()) ? "alt: " + yaml(ALT.get(f.slug())) + "\n" : "")
            + "---\n\n";
        Files.writeString(dir.resolve(f.slug() + ".md"),
            fm + f.body().strip() + "\n", StandardCharsets.UTF_8);
    }
    IO.println("Wrote " + FEATURES.size() + " feature pages to " + dir.toAbsolutePath());
}
