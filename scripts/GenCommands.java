/// Generator for src/lib/commands.ts (the Commands and Keybindings pages).
/// A JDK 25 compact source file (JEP 512): run with `java scripts/GenCommands.java`
/// from the website repo root (or the scripts/ dir). It reads the Editora app's
/// real sources:
///   - command ids from MainController.registerCommands()
///   - titles + descriptions from i18n messages.properties (command.<id>[.desc])
///   - keys from every bundled keymap (emacs/cua/sublime/vscode/intellij), the
///     base Win/Linux (Ctrl) variant; macOS substitutes Cmd.
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

// Every bundled keymap, in display order: {id, label}. Base (Win/Linux) files.
static final String[][] KEYMAPS = {
    {"emacs", "Emacs"},
    {"cua", "CUA"},
    {"sublime", "Sublime Text"},
    {"vscode", "VS Code"},
    {"intellij", "IntelliJ IDEA"},
};

static final String[] ORDER = {
    "Files", "Editing", "Navigation", "Search", "Buffers & tabs",
    "View & display", "Folding", "Splits & zoom", "Markdown",
    "Code intelligence (LSP)", "Run", "Debugging", "Git & diff",
    "HTTP client", "Diagrams", "Bookmarks", "Personal notes",
    "Snippets & templates", "Projects", "Remote (SFTP)", "Spell check",
    "Appearance", "Tool windows", "Window", "Application",
};

static final Pattern COMMAND_OF = Pattern.compile("Command\\.of\\(\"([^\"]+)\"");
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
    if (in(cid, "editor.exportPdf", "editor.print", "preview.exportPdf", "preview.print")) return "Files";
    if (in(cid, "file.run", "file.runWithArgs")) return "Run";
    if (in(p, "file", "recent")) return "Files";
    if (p.equals("edit")) return "Editing";
    if (p.equals("nav")) return "Navigation";
    if (in(p, "find", "search")) return "Search";
    if (in(p, "buffer", "switcher")) return "Buffers & tabs";
    if (p.equals("markdown") || cid.startsWith("view.markdown") || p.equals("preview")
        || cid.equals("view.toggleMarkdownPreviewTheme")) return "Markdown";
    if (in(cid, "view.fold", "view.foldAll", "view.unfold", "view.unfoldAll", "view.toggleFold")) return "Folding";
    if (in(cid, "view.splitHorizontal", "view.splitVertical", "view.unsplit",
            "view.textZoomIn", "view.textZoomOut", "view.textZoomReset")) return "Splits & zoom";
    if (in(cid, "view.settings", "view.welcome", "view.messageLog", "view.debugLog")) return "Application";
    if (p.equals("view")) return "View & display";
    if (in(p, "git", "diff", "merge")) return "Git & diff";
    if (p.equals("debug")) return "Debugging";
    if (p.equals("lsp")) return "Code intelligence (LSP)";
    if (p.equals("http")) return "HTTP client";
    if (p.equals("mermaid")) return "Diagrams";
    if (p.equals("run")) return "Run";
    if (p.equals("bookmarks")) return "Bookmarks";
    if (p.equals("notes")) return "Personal notes";
    if (in(p, "snippets", "template")) return "Snippets & templates";
    if (p.equals("project")) return "Projects";
    if (p.equals("remote")) return "Remote (SFTP)";
    if (p.equals("spell")) return "Spell check";
    if (p.equals("theme")) return "Appearance";
    if (p.equals("tool")) return "Tool windows";
    if (p.equals("window")) return "Window";
    return "Application";
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

// command id -> first chord this keymap binds to it (file order, first wins).
Map<String, String> firstChordPerCommand(String json) {
    Map<String, String> out = new LinkedHashMap<>();
    Matcher m = JSON_PAIR.matcher(json);
    while (m.find()) {
        String chord = jsonUnescape(m.group(1));
        String cmd = jsonUnescape(m.group(2));
        out.putIfAbsent(cmd, chord);
    }
    return out;
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

    // Command ids from MainController.
    String mc = Files.readString(root.resolve("src/main/java/com/editora/ui/MainController.java"),
        StandardCharsets.UTF_8);
    TreeSet<String> ids = new TreeSet<>();
    Matcher m = COMMAND_OF.matcher(mc);
    while (m.find()) ids.add(m.group(1));

    // command.<id>[.desc] -> text, from the English i18n catalog (naive, like the app reads it).
    Map<String, String> props = new HashMap<>();
    for (String line : Files.readAllLines(
            root.resolve("src/main/resources/com/editora/i18n/messages.properties"), StandardCharsets.UTF_8)) {
        if (line.startsWith("command.") && line.contains("=")) {
            int eq = line.indexOf('=');
            props.put(line.substring("command.".length(), eq), line.substring(eq + 1));
        }
    }

    // Per-keymap chord lookup.
    Map<String, Map<String, String>> keyForKm = new LinkedHashMap<>();
    for (String[] km : KEYMAPS) {
        String json = Files.readString(
            root.resolve("src/main/resources/com/editora/keymaps/" + km[0] + ".json"), StandardCharsets.UTF_8);
        keyForKm.put(km[0], firstChordPerCommand(json));
    }
    Map<String, String> keyForEmacs = keyForKm.get("emacs"); // `keys` = the default keymap's chord.

    // Bucket commands into groups, sorted by id within each group.
    Map<String, List<String>> groups = new LinkedHashMap<>();
    for (String g : ORDER) groups.put(g, new ArrayList<>());
    for (String cid : ids) groups.get(category(cid)).add(cid);

    List<String> out = new ArrayList<>();
    out.add("// AUTO-GENERATED by scripts/GenCommands.java — do not edit by hand.");
    out.add("// Sources: MainController.registerCommands(), messages.properties, keymaps/*.json.");
    out.add("// Re-run the script after commands or keybindings change.");
    out.add("");
    out.add("// `keys` is the Emacs (default) chord; `bindings` maps each keymap id to its chord");
    out.add("// (base Win/Linux variant — macOS substitutes Cmd). Only keymaps that bind it appear.");
    out.add("export type Cmd = { title: string; id: string; keys?: string; bindings?: Record<string, string>; desc?: string };");
    out.add("export type CmdGroup = { title: string; commands: Cmd[] };");
    out.add("");
    out.add("export const keymaps: { id: string; name: string }[] = [");
    for (String[] km : KEYMAPS) {
        out.add("  { id: \"" + js(km[0]) + "\", name: \"" + js(km[1]) + "\" },");
    }
    out.add("];");
    out.add("");
    out.add("export const commandGroups: CmdGroup[] = [");
    for (String g : ORDER) {
        List<String> cmds = groups.get(g);
        if (cmds.isEmpty()) continue;
        out.add("  {");
        out.add("    title: \"" + js(g) + "\",");
        out.add("    commands: [");
        for (String cid : cmds) {
            String title = js(props.getOrDefault(cid, cid));
            String key = keyForEmacs.getOrDefault(cid, "");
            String desc = js(props.getOrDefault(cid + ".desc", ""));
            List<String> parts = new ArrayList<>();
            parts.add("title: \"" + title + "\"");
            parts.add("id: \"" + cid + "\"");
            if (!key.isEmpty()) parts.add("keys: \"" + js(key) + "\"");
            List<String> binds = new ArrayList<>();
            for (String[] km : KEYMAPS) {
                String c = keyForKm.get(km[0]).get(cid);
                if (c != null) binds.add("\"" + km[0] + "\": \"" + js(c) + "\"");
            }
            if (!binds.isEmpty()) parts.add("bindings: { " + String.join(", ", binds) + " }");
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
    IO.println("Wrote " + outFile + ": " + total + " commands in " + nonEmpty + " groups");
}
