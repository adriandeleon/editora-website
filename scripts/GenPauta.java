/// Generator for src/content/docs/pauta.md, the user guide's Pauta conformance page.
/// A JDK 25 compact source file (JEP 512): run with `java scripts/GenPauta.java`
/// from the website repo root (or the scripts/ dir). It reads the Editora app's
/// pauta.json (the rules of its Pauta edition it does not meet, each with a
/// reason and a link) and the pauta.version of its pom.xml, and writes the page,
/// so the page cannot list a different set from the app (pauta-project.dev,
/// Conformance: PAUTA-13, PAUTA-14). The output is committed, like commands.ts:
/// the site build needs no access to the Editora repo (it isn't checked out in CI).
/// The Editora checkout is found as a sibling directory (Editora-V2 or Editora);
/// override with an argument or the EDITORA_REPO environment variable.
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

static final String PAUTA_JSON = "src/main/resources/com/editora/pauta.json";

/// One rule the app does not meet as written, as pauta.json records it.
record Entry(String rule, String level, String url, String kind, String reason, String link) {}

/// The kinds in the order the app's Pauta window lists them: what is still owed first.
static final List<String> KINDS = List.of("pending", "deviation", "not-applicable");

// ---- a JSON reader just big enough for pauta.json (objects, arrays, strings, literals) ----

static final class Json {
    private final String s;
    private int i;

    Json(String s) {
        this.s = s;
    }

    Object value() {
        ws();
        char c = s.charAt(i);
        if (c == '{') return object();
        if (c == '[') return array();
        if (c == '"') return string();
        int start = i;
        while (i < s.length() && ",}] \t\r\n".indexOf(s.charAt(i)) < 0) i++;
        String literal = s.substring(start, i);
        return switch (literal) {
            case "true" -> Boolean.TRUE;
            case "false" -> Boolean.FALSE;
            case "null" -> null;
            default -> Double.valueOf(literal);
        };
    }

    private Map<String, Object> object() {
        Map<String, Object> map = new LinkedHashMap<>();
        i++; // {
        ws();
        if (s.charAt(i) == '}') {
            i++;
            return map;
        }
        while (true) {
            ws();
            String key = string();
            ws();
            expect(':');
            map.put(key, value());
            ws();
            if (s.charAt(i) == ',') {
                i++;
                continue;
            }
            expect('}');
            return map;
        }
    }

    private List<Object> array() {
        List<Object> list = new ArrayList<>();
        i++; // [
        ws();
        if (s.charAt(i) == ']') {
            i++;
            return list;
        }
        while (true) {
            list.add(value());
            ws();
            if (s.charAt(i) == ',') {
                i++;
                continue;
            }
            expect(']');
            return list;
        }
    }

    private String string() {
        expect('"');
        StringBuilder out = new StringBuilder();
        while (s.charAt(i) != '"') {
            char c = s.charAt(i++);
            if (c != '\\') {
                out.append(c);
                continue;
            }
            char e = s.charAt(i++);
            switch (e) {
                case 'n' -> out.append('\n');
                case 't' -> out.append('\t');
                case 'r' -> out.append('\r');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'u' -> {
                    out.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                    i += 4;
                }
                default -> out.append(e); // \" \\ \/
            }
        }
        i++; // closing quote
        return out.toString();
    }

    private void ws() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
    }

    private void expect(char c) {
        if (s.charAt(i) != c) {
            throw new IllegalStateException("pauta.json: expected '" + c + "' at offset " + i);
        }
        i++;
    }
}

@SuppressWarnings("unchecked")
List<Entry> readExceptions(String json) {
    Map<String, Object> root = (Map<String, Object>) new Json(json).value();
    List<Entry> entries = new ArrayList<>();
    for (Object o : (List<Object>) root.getOrDefault("exceptions", List.of())) {
        Map<String, Object> e = (Map<String, Object>) o;
        Entry entry = new Entry(
            text(e, "rule"), text(e, "level"), text(e, "url"), text(e, "kind"), text(e, "reason"), (String) e.get("link"));
        if (!KINDS.contains(entry.kind())) {
            // app-only is a template repository's alone; anything else is a typo. Either way the
            // page has no heading for it, so say so instead of leaving the rule out.
            throw new IllegalStateException(entry.rule() + ": this page has no section for the kind \"" + entry.kind() + "\"");
        }
        entries.add(entry);
    }
    return entries;
}

String text(Map<String, Object> e, String field) {
    Object v = e.get(field);
    if (!(v instanceof String s) || s.isBlank()) {
        throw new IllegalStateException("pauta.json: an exception has no " + field + ": " + e);
    }
    return s;
}

// ---- the conformance, as the app's own helper computes it (PautaConformance.of) ----

/// Partial when any MUST is pending, Core when any SHOULD is pending, Complete otherwise.
String conformance(List<Entry> exceptions) {
    boolean should = false;
    for (Entry e : exceptions) {
        if (!e.kind().equals("pending")) continue;
        if (e.level().equals("MUST")) return "Partial";
        if (e.level().equals("SHOULD")) should = true;
    }
    return should ? "Core" : "Complete";
}

long pending(List<Entry> exceptions, String level) {
    return exceptions.stream().filter(e -> e.kind().equals("pending") && e.level().equals(level)).count();
}

// ---- the page ----

String cell(String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("|", "\\|");
}

/// "#212" for an issue, "Decision 0007" for a decision record, else "Details".
String linkText(String link) {
    Matcher issue = Pattern.compile("/issues/(\\d+)$").matcher(link);
    if (issue.find()) return "#" + issue.group(1);
    Matcher decision = Pattern.compile("/docs/decisions/(\\d+)-").matcher(link);
    if (decision.find()) return "Decision " + decision.group(1);
    return "Details";
}

String plural(long n, String one, String many) {
    return n == 1 ? "1 " + one : n + " " + many;
}

String kindTitle(String kind) {
    return switch (kind) {
        case "pending" -> "Pending";
        case "deviation" -> "Deviations";
        default -> "Not applicable";
    };
}

String kindIntro(String kind) {
    return switch (kind) {
        case "pending" -> "Rules Editora does not meet yet. Each one links the issue that tracks it.";
        case "deviation" ->
            "Recommendations Editora follows differently on purpose. Each one links the decision record that says why.";
        default -> "Rules that read as applying to every app but whose subject does not exist in Editora.";
    };
}

String page(String edition, List<Entry> exceptions) {
    String conformance = conformance(exceptions);
    String base = "https://pauta-project.dev/" + edition + "/";
    long musts = pending(exceptions, "MUST");
    long shoulds = pending(exceptions, "SHOULD");
    String opening = conformance.equals("Partial") ? "is working toward" : "is built to";
    String meaning = switch (conformance) {
        case "Partial" ->
            "Editora does not meet every MUST of this edition yet: " + (musts == 1 ? "1 is" : musts + " are") + " pending.";
        case "Core" ->
            "Editora meets every MUST of this edition; " + (shoulds == 1 ? "1 SHOULD is" : shoulds + " SHOULDs are")
                + " pending.";
        default -> "Editora meets every MUST and SHOULD of this edition.";
    };

    StringBuilder md = new StringBuilder();
    md.append("---\n");
    md.append("title: Pauta conformance\n");
    md.append("description: The edition of Pauta, the guidelines Editora's family of apps follows, that Editora ")
        .append(conformance.equals("Partial") ? "is working toward" : "is built to")
        .append(", how fully it conforms, and every rule it does not meet as written.\n");
    md.append("category: Help\n");
    md.append("order: 4\n");
    md.append("---\n\n");
    md.append("<!-- AUTO-GENERATED by scripts/GenPauta.java from Editora's pauta.json and pom.xml. Do not edit by hand. -->\n\n");

    md.append("[Pauta](https://pauta-project.dev/) is the set of guidelines Editora and its sibling apps follow, so they\n")
        .append("look and work the same on Windows, macOS and Linux. Each rule has an ID and a level: a **MUST** is\n")
        .append("required, a **SHOULD** is expected unless there is a stated reason.\n\n");
    md.append("Editora ").append(opening).append(" **[Pauta ").append(edition).append("](").append(base).append(")**.\n\n");
    md.append("**Pauta conformance: ").append(conformance).append(".** ").append(meaning).append("\n\n");
    md.append("| Conformance | Means |\n|---|---|\n");
    md.append("| Complete | Every MUST and every SHOULD is met, does not apply, or is a recorded deviation. |\n");
    md.append("| Core | Every MUST is met; one or more SHOULDs are still pending. |\n");
    md.append("| Partial | One or more MUSTs are still pending. The app says *Working toward*, not *Built to*. |\n\n");
    md.append("The conformance is worked out from the list below, never typed in, so it cannot claim more than the list\n")
        .append("shows. The same list is in the app: **Help → Pauta Conformance** opens it, and **About** names the edition\n")
        .append("and the conformance.\n");

    if (exceptions.isEmpty()) {
        md.append("\n## Exceptions\n\nNone: Editora meets every MUST and SHOULD of Pauta ").append(edition).append(".\n");
        return md.toString();
    }

    Comparator<Entry> order = Comparator.<Entry>comparingInt(e -> e.level().equals("MUST") ? 0 : e.level().equals("SHOULD") ? 1 : 2)
        .thenComparing(Entry::rule);
    for (String kind : KINDS) {
        List<Entry> group = exceptions.stream().filter(e -> e.kind().equals(kind)).sorted(order).toList();
        if (group.isEmpty()) continue;
        long m = group.stream().filter(e -> e.level().equals("MUST")).count();
        md.append("\n## ").append(kindTitle(kind)).append("\n\n");
        md.append(kindIntro(kind)).append(' ');
        md.append(plural(group.size(), "rule", "rules")).append(": ").append(m).append(" MUST, ")
            .append(group.size() - m).append(" SHOULD.\n\n");
        boolean linked = group.stream().anyMatch(e -> e.link() != null);
        md.append(linked ? "| Rule | Level | What is different | Link |\n|---|---|---|---|\n" : "| Rule | Level | What is different |\n|---|---|---|\n");
        for (Entry e : group) {
            // The ID in monospace, as in the app's Pauta window. An HTML link rather than a Markdown one,
            // for the nowrap: the narrow first column otherwise breaks every ID at its hyphen.
            md.append("| <a href=\"").append(e.url()).append("\" style=\"white-space: nowrap\"><code>").append(e.rule())
                .append("</code></a> | ").append(e.level()).append(" | ")
                .append(cell(e.reason()));
            if (linked) {
                md.append(" | ");
                if (e.link() != null) md.append('[').append(linkText(e.link())).append("](").append(e.link()).append(')');
            }
            md.append(" |\n");
        }
    }
    return md.toString();
}

// ---- finding the two repositories, as GenRoadmap.java does ----

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
        candidates.add(sib);
    }
    for (Path c : candidates) {
        if (c != null && Files.isRegularFile(c.resolve("pom.xml")) && Files.isRegularFile(c.resolve(PAUTA_JSON))) {
            return c.toAbsolutePath().normalize();
        }
    }
    throw new IllegalStateException(
        "Could not find an Editora checkout with pom.xml and " + PAUTA_JSON
            + ". Pass its path as an argument or set EDITORA_REPO. Tried: " + candidates);
}

void main(String[] args) throws IOException {
    Path web = websiteRoot();
    Path root = editoraRoot(args, web);

    Matcher m = Pattern.compile("<pauta\\.version>([^<]+)</pauta\\.version>")
        .matcher(Files.readString(root.resolve("pom.xml"), StandardCharsets.UTF_8));
    if (!m.find()) throw new IllegalStateException(root.resolve("pom.xml") + " defines no pauta.version");
    String edition = m.group(1).strip();

    List<Entry> exceptions = readExceptions(Files.readString(root.resolve(PAUTA_JSON), StandardCharsets.UTF_8));
    for (Entry e : exceptions) {
        // Every rule links its own edition's page: an entry at another address is a stale copy.
        if (!e.url().startsWith("https://pauta-project.dev/" + edition + "/")) {
            throw new IllegalStateException(e.rule() + " links " + e.url() + ", not Pauta " + edition);
        }
    }

    Path out = web.resolve("src/content/docs/pauta.md");
    Files.writeString(out, page(edition, exceptions), StandardCharsets.UTF_8);
    System.out.println("Wrote " + web.relativize(out) + ": Pauta " + edition + ", " + conformance(exceptions) + ", "
        + exceptions.size() + " exceptions (from " + root + ")");
}
