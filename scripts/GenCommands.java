/// Generator for src/lib/commands.ts (the Commands and Keybindings pages).
/// A JDK 25 compact source file (JEP 512): run with `java scripts/GenCommands.java`
/// from the website repo root (or the scripts/ dir). It reads the Editora app's
/// real sources:
///   - command ids from the Command.of(...) registrations in com/editora/ui
///   - titles + descriptions from i18n messages.properties (command.<id>[.desc])
///   - keys from every bundled keymap (emacs/cua/sublime/vscode/intellij): the base
///     Win/Linux file and, where the app ships one, its macOS variant (<id>.mac.json),
///     as raw keymap tokens (src/lib/chord-format.ts writes them out as the app does).
/// The Editora checkout is found as a sibling directory (Editora-V2 or Editora).
/// Override with an argument (`java scripts/GenCommands.java /path/to/Editora`)
/// or the EDITORA_REPO environment variable.
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Every bundled keymap, in display order: {id, label}. On macOS the app loads <id>.mac.json
// instead of <id>.json when the variant exists (KeymapManager.loadNamed), whole-file, not as
// an overlay; Emacs has no variant, so macOS uses emacs.json.
static final String[][] KEYMAPS = {
    {"emacs", "Emacs"},
    {"cua", "CUA"},
    {"sublime", "Sublime Text"},
    {"vscode", "VS Code"},
    {"intellij", "IntelliJ IDEA"},
};

static final String[] ORDER = {
    "Files", "Editing", "Navigation", "Search", "Buffers & tabs",
    "View & display", "Folding", "Splits & zoom", "Markdown", "Diagrams",
    "Previews", "Typst", "CSV", "Log viewer",
    "Code intelligence (LSP)", "Language support", "Run", "Build tools", "Debugging",
    "Git & diff", "Local history", "HTTP client", "Terminal", "Bookmarks", "Personal notes",
    "TODO highlighting", "Snippets & templates", "Macros & external tools", "Projects",
    "Project Map", "Remote (SFTP)", "Deployments & containers", "Encrypted files",
    "AI & agents", "Spell check", "Appearance", "Tool windows", "Window", "Plugins",
    "Settings & sync", "Application",
};

// The build tools' command prefixes (BuildTool ids in com.editora.build).
static final String[] BUILD_TOOLS = {
    "maven", "npm", "cargo", "go", "gradle", "sbt", "mill", "scalacli", "clojure", "lein", "babashka",
};

// `\s*` spans the newline in a wrapped registration:
//     registry.register(Command.of(
//             "view.toggleTestRunner",
// Requiring the literal on the same line as `Command.of(` missed 96 of MainController's 492
// registrations. Calls whose id is an expression rather than a literal (`id + ".stop"` per build
// tool, `ExternalTool.commandIdFor(...)`, plugin ids) are not matched here.
// The trailing `,` requires the literal to be the *whole* first argument, so a concatenation like
// `Command.of("tool." + id, …)` is rejected instead of contributing a truncated id ("tool.").
// Ids this pattern cannot see are recovered from the i18n catalog — see the completeness backstop
// in main(). Truly runtime-generated ids (macro.run.<slug>, externalTool.run.<slug>, plugin ids)
// carry an explicit title and no catalog key, so they stay out of both passes.
static final Pattern COMMAND_OF = Pattern.compile("Command\\.of\\(\\s*\"([^\"]+)\"\\s*,");
// Flat JSON "key": "value" pairs (escape-aware), scanned in file order.
static final Pattern JSON_PAIR =
    Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");

boolean in(String x, String... opts) {
    for (String o : opts) if (o.equals(x)) return true;
    return false;
}

String category(String cid) {
    int dot = cid.indexOf('.');
    String p = dot == -1 ? cid : cid.substring(0, dot);
    if (in(cid, "editor.exportPdf", "editor.print", "preview.exportPdf", "preview.print",
            "editor.exportSelectionPdf", "editor.printSelection", "editor.setPdfPageSize")) return "Files";
    if (in(cid, "file.run", "file.runWithArgs")) return "Run";
    if (in(p, "file", "recent")) return "Files";
    if (in(p, "edit", "editor", "abbrev", "editorConfig")) return "Editing";
    if (in(p, "nav", "index") || cid.equals("structure.jump")) return "Navigation";
    if (cid.equals("structure.narrowFold")) return "Folding";
    if (in(p, "find", "search")) return "Search";
    if (in(p, "buffer", "switcher")) return "Buffers & tabs";
    if (p.equals("markdown") || p.equals("markdownLint") || cid.startsWith("view.markdown") || p.equals("preview")
        || cid.equals("view.toggleMarkdownPreviewTheme")) return "Markdown";
    if (in(cid, "view.fold", "view.foldAll", "view.unfold", "view.unfoldAll", "view.toggleFold")) return "Folding";
    if (in(cid, "view.splitHorizontal", "view.splitVertical", "view.unsplit",
            "view.textZoomIn", "view.textZoomOut", "view.textZoomReset")) return "Splits & zoom";
    if (cid.equals("view.settings")) return "Settings & sync";
    if (in(cid, "view.welcome", "view.messageLog", "view.debugLog")) return "Application";
    if (p.equals("view")) return "View & display";
    if (in(p, "git", "github", "diff", "merge")) return "Git & diff";
    if (p.equals("debug")) return "Debugging";
    if (in(p, "lsp", "eslint")) return "Code intelligence (LSP)";
    if (cid.equals("install.typstCli") || p.equals("typst")) return "Typst";
    if (cid.equals("install.mermaidSupport")) return "Diagrams";
    if (in(p, "install", "python")) return "Language support";
    if (p.equals("http")) return "HTTP client";
    if (in(p, "mermaid", "diagram")) return "Diagrams";
    if (in(p, "htmlPreview", "structured", "markwhen")) return "Previews";
    if (p.equals("csv")) return "CSV";
    if (p.equals("log")) return "Log viewer";
    if (in(p, "run", "test")) return "Run";
    if (in(p, BUILD_TOOLS) || p.equals("pom")) return "Build tools";
    if (in(p, "history", "localHistory", "undoHistory")) return "Local history";
    if (p.equals("terminal")) return "Terminal";
    if (p.equals("bookmarks")) return "Bookmarks";
    if (p.equals("notes")) return "Personal notes";
    if (p.equals("todo")) return "TODO highlighting";
    if (in(p, "snippets", "template")) return "Snippets & templates";
    if (in(p, "macro", "externalTool")) return "Macros & external tools";
    if (in(p, "project", "workspace")) return "Projects";
    if (p.equals("projectMap")) return "Project Map";
    if (p.equals("remote")) return "Remote (SFTP)";
    if (in(p, "deploy", "container")) return "Deployments & containers";
    if (p.equals("crypto")) return "Encrypted files";
    if (in(p, "ai", "agent", "mcp")) return "AI & agents";
    if (p.equals("spell")) return "Spell check";
    if (in(p, "theme", "appearance")) return "Appearance";
    if (p.equals("tool")) return "Tool windows";
    if (p.equals("window")) return "Window";
    if (p.equals("plugins")) return "Plugins";
    if (in(p, "config", "sync", "keymap", "toolbar")) return "Settings & sync";
    return "Application";
}

// ---- Which chord the app advertises: KeymapManager.chordFor + ChordFormat.typable. The chords stay
// raw keymap tokens (C-S-p, Cmd-S-p); the pages write them out in the app's notation through
// src/lib/chord-format.ts, as ChordFormat does.

static final String[] PREFIXES = {"C-", "M-", "Cmd-", "S-"};
static final int CMD = 1 << 2;

int modifierMask(String token) {
    int mask = 0, at = 0;
    for (int i = 0; i < PREFIXES.length; i++) {
        String p = PREFIXES[i];
        // A prefix only counts while a key is left after it: "C--" is Ctrl + the minus key.
        if (token.startsWith(p, at) && token.length() > at + p.length()) {
            mask |= 1 << i;
            at += p.length();
        }
    }
    return mask;
}

/** ChordFormat.typable: a Cmd- chord can't be typed on a Windows/Linux keyboard. */
boolean typable(String sequence, boolean mac) {
    if (mac) return true;
    for (String token : sequence.split(" ")) {
        if ((modifierMask(token) & CMD) != 0) return false;
    }
    return true;
}

String js(String s) {
    return s.replace("\\", "\\\\").replace("\"", "\\\"");
}

String jsonUnescape(String s) {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c == '\\' && i + 1 < s.length()) {
            char n = s.charAt(++i);
            switch (n) {
                case '"' -> b.append('"');
                case '\\' -> b.append('\\');
                case '/' -> b.append('/');
                case 'b' -> b.append('\b');
                case 'f' -> b.append('\f');
                case 'n' -> b.append('\n');
                case 'r' -> b.append('\r');
                case 't' -> b.append('\t');
                case 'u' -> {
                    b.append((char) Integer.parseInt(s.substring(i + 1, i + 5), 16));
                    i += 4;
                }
                default -> b.append(n);
            }
        } else {
            b.append(c);
        }
    }
    return b.toString();
}

// command id -> every chord this keymap binds to it, in keymap (file) order.
// A chord listed twice in one file is bound to its LAST command, as the app's
// JSON parser keeps the last duplicate key; the earlier entry binds nothing.
Map<String, List<String>> chordsPerCommand(String json) {
    Map<String, String> commandByChord = new LinkedHashMap<>();
    Matcher m = JSON_PAIR.matcher(json);
    while (m.find()) {
        commandByChord.put(jsonUnescape(m.group(1)), jsonUnescape(m.group(2)));
    }
    Map<String, List<String>> out = new LinkedHashMap<>();
    commandByChord.forEach((chord, cmd) -> out.computeIfAbsent(cmd, k -> new ArrayList<>()).add(chord));
    return out;
}

/**
 * The chords one keymap shows for a command on one platform: the primary first, then the alternates. The primary is KeymapManager.chordFor: the first binding in keymap order that can be
 * typed on this platform, else the first binding of any kind. An untypable alternate (a Cmd- chord
 * on Windows/Linux) is left out, as the app never advertises one.
 */
List<String> shownChords(List<String> chords, boolean mac) {
    List<String> out = new ArrayList<>();
    if (chords == null || chords.isEmpty()) return out;
    String primary = null;
    for (String c : chords) {
        if (typable(c, mac)) {
            primary = c;
            break;
        }
    }
    if (primary == null) primary = chords.get(0);
    out.add(primary);
    for (String c : chords) {
        if (!c.equals(primary) && typable(c, mac)) out.add(c);
    }
    return out;
}

String jsList(List<String> xs) {
    List<String> q = new ArrayList<>();
    for (String x : xs) q.add("\"" + js(x) + "\"");
    return "[" + String.join(", ", q) + "]";
}

Path websiteRoot() {
    Path cwd = Path.of("").toAbsolutePath();
    if (Files.isDirectory(cwd.resolve("src/lib"))) return cwd;
    Path parent = cwd.getParent();
    if (parent != null && Files.isDirectory(parent.resolve("src/lib"))) return parent;
    return cwd;
}

Path editoraRoot(String[] args, Path web) {
    List<Path> candidates = new ArrayList<>();
    if (args.length > 0) candidates.add(Path.of(args[0]));
    String env = System.getenv("EDITORA_REPO");
    if (env != null && !env.isBlank()) candidates.add(Path.of(env));
    Path sib = web.getParent();
    if (sib != null) {
        candidates.add(sib.resolve("Editora-V2"));
        candidates.add(sib.resolve("Editora"));
        candidates.add(sib); // legacy: website/ inside the Editora repo
    }
    for (Path c : candidates) {
        if (c != null && Files.isRegularFile(c.resolve("src/main/java/com/editora/ui/MainController.java"))) {
            return c.toAbsolutePath().normalize();
        }
    }
    throw new IllegalStateException(
        "Could not find the Editora repo. Pass its path as an argument or set EDITORA_REPO. Tried: " + candidates);
}

void main(String[] args) throws IOException {
    Path web = websiteRoot();
    Path root = editoraRoot(args, web);

    // Command ids from every class in com.editora.ui that registers one. MainController holds most,
    // but the feature coordinators (Todo, Macro, Build, ExternalTool, Csv, Plugin) register their own,
    // and scanning MainController alone silently dropped them from the site.
    Path uiDir = root.resolve("src/main/java/com/editora/ui");
    TreeSet<String> ids = new TreeSet<>();
    List<Path> sources = new ArrayList<>();
    try (var stream = Files.list(uiDir)) {
        stream.filter(p -> p.getFileName().toString().endsWith(".java")).sorted().forEach(sources::add);
    }
    for (Path src : sources) {
        Matcher m = COMMAND_OF.matcher(Files.readString(src, StandardCharsets.UTF_8));
        while (m.find()) ids.add(m.group(1));
    }

    // command.<id>[.desc] -> text, from the English i18n catalog (naive, like the app reads it).
    // Key and value are stripped: the catalog mixes `command.x=Text` and `command.x = Text`, and
    // java.util.Properties treats whitespace around the `=` as a separator. Slicing at the `=` index
    // without stripping keyed the spaced entries as "x " (and valued them " Text"), so every lookup of
    // the real id missed and those commands fell back to rendering their raw id with no description.
    Map<String, String> props = new HashMap<>();
    for (String line : Files.readAllLines(
            root.resolve("src/main/resources/com/editora/i18n/messages.properties"), StandardCharsets.UTF_8)) {
        if (line.startsWith("command.") && line.contains("=")) {
            int eq = line.indexOf('=');
            props.put(line.substring("command.".length(), eq).strip(), line.substring(eq + 1).strip());
        }
    }

    // Completeness backstop. The source scan only sees ids written as string literals inside
    // `Command.of(...)`, and three real registration shapes defeat that: a concatenation
    // (`Command.of("tool." + id, …)`, one set per build tool), a constant reference
    // (`Command.of(KeyDispatcher.UNIVERSAL_ARGUMENT, …)`), and literals that live outside
    // com/editora/ui (BuildTool's own `view.toggle<Tool>Support` ids). Together those silently
    // dropped 36 real commands — the entire Maven/npm/Cargo/Go/Gradle family among them — so the
    // whole build-tool feature was missing from the site and unsearchable.
    // Every *documentable* command has a `command.<id>` title in the catalog, because that is how
    // the app resolves a title for a statically registered command; the runtime-generated ones pass
    // an explicit title and have no key. So the catalog is the authoritative list: adopt any titled
    // id the scan missed. This stays correct as new commands (and new build tools) are added.
    for (String key : props.keySet()) {
        if (!key.endsWith(".desc")) ids.add(key);
    }

    // Bookmark mnemonics: one command per digit, `bookmarks.gotoMnemonic0` … `9`, registered in a loop
    // as `Command.of("bookmarks.gotoMnemonic" + key, tr("bookmarks.mnemonic.gotoTitle", key), …)`. The id
    // is a concatenation and the title template deliberately sits outside the `command.*` namespace, so
    // neither the source scan nor the catalog backstop sees them. They are real, bindable commands that
    // the docs name, so expand the template here. They carry no description in the app.
    String mnemonicTitle = null;
    for (String line : Files.readAllLines(
            root.resolve("src/main/resources/com/editora/i18n/messages.properties"), StandardCharsets.UTF_8)) {
        if (line.startsWith("bookmarks.mnemonic.gotoTitle") && line.contains("=")) {
            mnemonicTitle = line.substring(line.indexOf('=') + 1).strip();
        }
    }
    if (mnemonicTitle != null) {
        for (char digit : "0123456789".toCharArray()) {
            String cid = "bookmarks.gotoMnemonic" + digit;
            ids.add(cid);
            props.put(cid, mnemonicTitle.replace("{0}", String.valueOf(digit)));
        }
    }

    // Per-keymap chord lookup, for each platform. macOS reads <id>.mac.json when the app ships one.
    Path keymapDir = root.resolve("src/main/resources/com/editora/keymaps");
    Map<String, Map<String, List<String>>> pcChords = new LinkedHashMap<>();
    Map<String, Map<String, List<String>>> macChords = new LinkedHashMap<>();
    for (String[] km : KEYMAPS) {
        String base = Files.readString(keymapDir.resolve(km[0] + ".json"), StandardCharsets.UTF_8);
        Path macFile = keymapDir.resolve(km[0] + ".mac.json");
        String mac = Files.isRegularFile(macFile) ? Files.readString(macFile, StandardCharsets.UTF_8) : base;
        pcChords.put(km[0], chordsPerCommand(base));
        macChords.put(km[0], chordsPerCommand(mac));
    }

    // Bucket commands into groups, sorted by id within each group.
    Map<String, List<String>> groups = new LinkedHashMap<>();
    for (String g : ORDER) groups.put(g, new ArrayList<>());
    for (String cid : ids) groups.get(category(cid)).add(cid);

    List<String> out = new ArrayList<>();
    out.add("// AUTO-GENERATED by scripts/GenCommands.java — do not edit by hand.");
    out.add("// Sources: Command.of(...) registrations in com/editora/ui, messages.properties, keymaps/*.json.");
    out.add("// Re-run the script after commands or keybindings change.");
    out.add("");
    out.add("// Chords are raw keymap tokens (C-S-p, Cmd-S-p, C-x C-s); src/lib/chord-format.ts writes them in the");
    out.add("// app's notation. `keys` is the Emacs (default) keymap's chord. `bindings` maps each keymap id to the");
    out.add("// chord the app shows for the command on Windows/Linux, `mac` to the one it shows on macOS (read from");
    out.add("// <keymap>.mac.json where the app ships one); `alt` / `macAlt` list that keymap's other chords for it.");
    out.add("// Only keymaps that bind the command appear.");
    out.add("export type Cmd = {");
    out.add("  title: string;");
    out.add("  id: string;");
    out.add("  keys?: string;");
    out.add("  bindings?: Record<string, string>;");
    out.add("  mac?: Record<string, string>;");
    out.add("  alt?: Record<string, string[]>;");
    out.add("  macAlt?: Record<string, string[]>;");
    out.add("  desc?: string;");
    out.add("};");
    out.add("export type CmdGroup = { title: string; commands: Cmd[] };");
    out.add("");
    out.add("export const keymaps: { id: string; name: string }[] = [");
    for (String[] km : KEYMAPS) {
        out.add("  { id: \"" + js(km[0]) + "\", name: \"" + js(km[1]) + "\" },");
    }
    out.add("];");
    out.add("");
    TreeSet<String> untitled = new TreeSet<>();
    out.add("export const commandGroups: CmdGroup[] = [");
    for (String g : ORDER) {
        List<String> cmds = groups.get(g);
        if (cmds.isEmpty()) continue;
        out.add("  {");
        out.add("    title: \"" + js(g) + "\",");
        out.add("    commands: [");
        for (String cid : cmds) {
            // A command with no `command.<id>` entry falls back to rendering its raw id as its
            // title, with no description, on both the command list and its own page. Collect those
            // so the run reports them instead of shipping them silently.
            if (!props.containsKey(cid)) untitled.add(cid);
            String title = js(props.getOrDefault(cid, cid));
            List<String> emacsShown = shownChords(pcChords.get("emacs").get(cid), false);
            String key = emacsShown.isEmpty() ? "" : emacsShown.get(0);
            String desc = js(props.getOrDefault(cid + ".desc", ""));
            List<String> parts = new ArrayList<>();
            parts.add("title: \"" + title + "\"");
            parts.add("id: \"" + cid + "\"");
            if (!key.isEmpty()) parts.add("keys: \"" + js(key) + "\"");
            List<String> binds = new ArrayList<>(), macBinds = new ArrayList<>();
            List<String> alts = new ArrayList<>(), macAlts = new ArrayList<>();
            for (String[] km : KEYMAPS) {
                List<String> pc = shownChords(pcChords.get(km[0]).get(cid), false);
                List<String> mac = shownChords(macChords.get(km[0]).get(cid), true);
                if (!pc.isEmpty()) binds.add("\"" + km[0] + "\": \"" + js(pc.get(0)) + "\"");
                if (!mac.isEmpty()) macBinds.add("\"" + km[0] + "\": \"" + js(mac.get(0)) + "\"");
                if (pc.size() > 1) alts.add("\"" + km[0] + "\": " + jsList(pc.subList(1, pc.size())));
                if (mac.size() > 1) macAlts.add("\"" + km[0] + "\": " + jsList(mac.subList(1, mac.size())));
            }
            if (!binds.isEmpty()) parts.add("bindings: { " + String.join(", ", binds) + " }");
            if (!macBinds.isEmpty()) parts.add("mac: { " + String.join(", ", macBinds) + " }");
            if (!alts.isEmpty()) parts.add("alt: { " + String.join(", ", alts) + " }");
            if (!macAlts.isEmpty()) parts.add("macAlt: { " + String.join(", ", macAlts) + " }");
            if (!desc.isEmpty()) parts.add("desc: \"" + desc + "\"");
            out.add("      { " + String.join(", ", parts) + " },");
        }
        out.add("    ],");
        out.add("  },");
    }
    out.add("];");
    out.add("");
    out.add("export const commandCount = commandGroups.reduce(");
    out.add("  (n, g) => n + g.commands.length,");
    out.add("  0,");
    out.add(");");
    out.add("");

    Path outFile = web.resolve("src/lib/commands.ts");
    Files.writeString(outFile, String.join("\n", out), StandardCharsets.UTF_8);

    int total = 0, nonEmpty = 0;
    for (List<String> v : groups.values()) {
        total += v.size();
        if (!v.isEmpty()) nonEmpty++;
    }
    IO.println("Wrote " + outFile + ": " + total + " commands in " + nonEmpty + " groups"
        + " (from " + sources.size() + " sources in com/editora/ui)");
    if (!untitled.isEmpty()) {
        IO.println("WARNING: " + untitled.size() + " command(s) have no `command.<id>` entry in"
            + " messages.properties and will render their raw id with no description:");
        for (String cid : untitled) IO.println("  - " + cid);
    }
}
