/// Generator for src/lib/roadmap.ts and src/lib/whatsnew.ts.
/// A JDK 25 compact source file (JEP 512): run with `java scripts/GenRoadmap.java`
/// from the website repo root (or the scripts/ dir). It reads the Editora app's
/// TODO.md (the roadmap) and CHANGELOG.md ([Unreleased] = What's New) and writes
/// committed, static data modules, so the site build needs no filesystem access
/// to the Editora repo (it isn't checked out in CI).
/// The Editora checkout is found as a sibling directory (Editora-V2 or Editora);
/// override with an argument or the EDITORA_REPO environment variable.
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record RmItem(boolean done, String text) {}
record RmSection(String title, List<RmItem> items) {}
record News(String title, String detail) {}

// Shared inline markdown -> safe HTML (entities escaped, code spans kept).
String esc(String s) {
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
}

String inlineRoadmap(String s) {
    s = esc(s);
    s = s.replaceAll("`([^`]+)`", "<code>$1</code>");
    s = s.replaceAll("\\*\\*([^*]+)\\*\\*", "<strong>$1</strong>");
    s = s.replaceAll("\\[([^\\]]+)\\]\\([^)]+\\)", "$1");
    return s;
}

// What's New flattens bold (it's already shown as a bold title).
String inlineNews(String s) {
    s = esc(s);
    s = s.replaceAll("`([^`]+)`", "<code>$1</code>");
    s = s.replaceAll("\\*\\*([^*]+)\\*\\*", "$1");
    s = s.replaceAll("\\[([^\\]]+)\\]\\([^)]+\\)", "$1");
    return s;
}

String truncate(String s, int n) {
    s = s.strip();
    if (s.length() > n) {
        return s.substring(0, n).replaceAll("\\s+\\S*$", "") + "…";
    }
    return s;
}

String stripCr(String s) {
    return s.endsWith("\r") ? s.substring(0, s.length() - 1) : s;
}

// --- Roadmap (TODO.md) ---------------------------------------------------

List<RmSection> parseRoadmap(String md) {
    Pattern head = Pattern.compile("^##\\s+(.*)$");
    Pattern li = Pattern.compile("^- \\[([ xX])\\]\\s+(.*)$");
    List<RmSection> sections = new ArrayList<>();
    RmSection cur = null;
    RmItem item = null;
    for (String raw : md.split("\n", -1)) {
        raw = stripCr(raw);
        Matcher hm = head.matcher(raw);
        Matcher lm = li.matcher(raw);
        if (hm.matches()) {
            if (cur != null && item != null) cur.items().add(item);
            item = null;
            if (cur != null) sections.add(cur);
            String t = hm.group(1).trim();
            cur = t.equalsIgnoreCase("recently shipped") ? null : new RmSection(t, new ArrayList<>());
        } else if (lm.matches() && cur != null) {
            if (item != null) cur.items().add(item);
            item = new RmItem(lm.group(1).equalsIgnoreCase("x"), inlineRoadmap(lm.group(2).trim()));
        } else if (item != null && raw.matches("\\s+\\S.*")) {
            item = new RmItem(item.done(), item.text() + " " + inlineRoadmap(raw.trim())); // wrapped line
        }
    }
    if (cur != null && item != null) cur.items().add(item);
    if (cur != null) sections.add(cur);
    List<RmSection> out = new ArrayList<>();
    for (RmSection s : sections) if (!s.items().isEmpty()) out.add(s);
    return out;
}

// --- What's New (CHANGELOG.md [Unreleased]) ------------------------------

void flushNews(List<News> items, String cur, Pattern title) {
    if (cur == null) return;
    Matcher m = title.matcher(cur);
    if (m.matches()) {
        items.add(new News(inlineNews(m.group(1)), truncate(inlineNews(m.group(2)), 150)));
    } else {
        items.add(new News(truncate(inlineNews(cur), 90), ""));
    }
}

// The newest *released* "## [x.y.z]" section (its header through just before the
// next "## ["), skipping a leading "## [Unreleased]" header whether or not it has
// content — the teaser reflects what shipped, not pending work.
String newestReleaseSection(String md) {
    int i = md.indexOf("## [");
    while (i >= 0) {
        int lineEnd = md.indexOf('\n', i);
        String header = lineEnd >= 0 ? md.substring(i, lineEnd) : md.substring(i);
        if (!header.startsWith("## [Unreleased]")) break;
        i = md.indexOf("## [", i + 3);
    }
    if (i < 0) return md;
    int next = md.indexOf("## [", i + 3);
    return next >= 0 ? md.substring(i, next) : md.substring(i);
}

// The version number out of the newest released section's "## [x.y.z] - date" header, or null if
// the changelog has no released section yet. Used when the pom sits on a -SNAPSHOT/-rc.
String newestReleasedVersion(String md) {
    Matcher m = Pattern.compile("^## \\[([^\\]]+)\\]", Pattern.MULTILINE)
        .matcher(newestReleaseSection(md));
    if (!m.find()) return null;
    String v = m.group(1).trim();
    return v.equalsIgnoreCase("Unreleased") ? null : v;
}

List<News> parseWhatsNew(String md) {
    // Scope to the newest *released* version's section, so a release with no
    // "### Added" block doesn't fall through to an older release's additions
    // (and unshipped [Unreleased] notes never leak into the teaser).
    String section = newestReleaseSection(md);
    // Within that section, lead with "### Added" (features/highlights) when
    // present, so a release's leading "### Fixed"/"### Performance" block doesn't
    // dominate the landing page; otherwise start at the top of the section.
    int start = section.indexOf("### Added");
    String body = start >= 0 ? section.substring(start) : section;
    Pattern title = Pattern.compile("^\\*\\*(.+?)\\*\\*\\s*(?:[—–-]+\\s*)?(.*)$");
    List<News> items = new ArrayList<>();
    String cur = null;
    for (String raw : body.split("\n", -1)) {
        raw = stripCr(raw);
        if (raw.matches("- .*")) {
            flushNews(items, cur, title);
            cur = raw.replaceFirst("^- ", "").trim();
        } else if (cur != null && raw.matches("\\s+\\S.*") && !raw.matches("\\s*[-*].*")) {
            cur = cur + " " + raw.trim(); // wrapped continuation
        } else if (raw.matches("#{2,3}\\s.*") || raw.startsWith("[Unreleased]:")) {
            flushNews(items, cur, title);
            cur = null;
        }
        if (items.size() >= 8) break;
    }
    flushNews(items, cur, title);
    return items.size() > 8 ? items.subList(0, 8) : items;
}

// --- Emit ----------------------------------------------------------------

String ts(String s) {
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
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
        candidates.add(sib);
    }
    for (Path c : candidates) {
        if (c != null && Files.isRegularFile(c.resolve("TODO.md")) && Files.isRegularFile(c.resolve("CHANGELOG.md"))) {
            return c.toAbsolutePath().normalize();
        }
    }
    throw new IllegalStateException(
        "Could not find the Editora repo (with TODO.md + CHANGELOG.md). Pass its path as an argument or set EDITORA_REPO. Tried: " + candidates);
}

void main(String[] args) throws IOException {
    Path web = websiteRoot();
    Path root = editoraRoot(args, web);

    List<RmSection> roadmap = parseRoadmap(Files.readString(root.resolve("TODO.md"), StandardCharsets.UTF_8));
    String changelogMd = Files.readString(root.resolve("CHANGELOG.md"), StandardCharsets.UTF_8);
    List<News> news = parseWhatsNew(changelogMd);

    StringBuilder rm = new StringBuilder();
    rm.append("// AUTO-GENERATED by scripts/GenRoadmap.java — do not edit by hand.\n");
    rm.append("// Source: Editora's TODO.md. Re-run after the roadmap changes.\n\n");
    rm.append("export type RoadmapItem = { done: boolean; text: string };\n");
    rm.append("export type RoadmapSection = { title: string; items: RoadmapItem[] };\n\n");
    rm.append("export const roadmap: RoadmapSection[] = [\n");
    for (RmSection s : roadmap) {
        rm.append("  {\n");
        rm.append("    title: ").append(ts(s.title())).append(",\n");
        rm.append("    items: [\n");
        for (RmItem i : s.items()) {
            rm.append("      { done: ").append(i.done()).append(", text: ").append(ts(i.text())).append(" },\n");
        }
        rm.append("    ],\n");
        rm.append("  },\n");
    }
    rm.append("];\n");
    Files.writeString(web.resolve("src/lib/roadmap.ts"), rm.toString(), StandardCharsets.UTF_8);

    StringBuilder wn = new StringBuilder();
    wn.append("// AUTO-GENERATED by scripts/GenRoadmap.java — do not edit by hand.\n");
    wn.append("// Source: the newest released section of Editora's CHANGELOG.md. Re-run after the changelog changes.\n\n");
    wn.append("export type NewsItem = { title: string; detail: string };\n\n");
    wn.append("export const whatsNew: NewsItem[] = [\n");
    for (News n : news) {
        wn.append("  { title: ").append(ts(n.title())).append(", detail: ").append(ts(n.detail())).append(" },\n");
    }
    wn.append("];\n");
    Files.writeString(web.resolve("src/lib/whatsnew.ts"), wn.toString(), StandardCharsets.UTF_8);

    // The release version is the project's own <version> (the first in the POM).
    String pom = Files.readString(root.resolve("pom.xml"), StandardCharsets.UTF_8);
    Matcher vm = Pattern.compile("<version>([^<]+)</version>").matcher(pom);
    String pomVersion = vm.find() ? vm.group(1).trim() : "1.0.0";
    // Between releases the pom carries a pre-release suffix (`0.9.9-SNAPSHOT`, or `-rcN`), so
    // generating from a checkout sitting on master would pin every docs URL to a version that was
    // never released (/docs/v-0.9.9-SNAPSHOT/...). The site documents what shipped, so fall back to
    // the newest released section of the changelog and say so loudly.
    String version = pomVersion;
    if (pomVersion.contains("-")) {
        String released = newestReleasedVersion(changelogMd);
        String chosen = released != null ? released : pomVersion.substring(0, pomVersion.indexOf('-'));
        IO.println("WARNING: pom <version> is " + pomVersion + ", which is not a released version."
            + " Using " + chosen + (released != null ? " (newest released section of CHANGELOG.md)." : ".")
            + " Generate from the release tag to be sure (git worktree add --detach <dir> v" + chosen + ").");
        version = chosen;
    }
    Files.writeString(
        web.resolve("src/lib/version.ts"),
        "// AUTO-GENERATED by scripts/GenRoadmap.java — do not edit by hand.\n"
            + "// Source: Editora's pom.xml <version> (falling back to the newest released\n"
            + "// CHANGELOG.md section when the pom sits on a -SNAPSHOT/-rc between releases).\n"
            + "// Committed so the build needs no access to the Editora repo (it isn't\n"
            + "// checked out in CI).\n\n"
            + "export const version = " + ts(version) + ";\n",
        StandardCharsets.UTF_8);

    // The full "What's New" page: the entire changelog (every ## [version]
    // section) as a Markdown page, so nothing the home teaser truncates is lost.
    int firstVer = changelogMd.indexOf("## [");
    String unreleased = (firstVer >= 0 ? changelogMd.substring(firstVer) : changelogMd).strip();
    // Drop a leading empty [Unreleased] header (present right after a release).
    if (unreleased.startsWith("## [Unreleased]")) {
        int nl = unreleased.indexOf('\n');
        String rest = nl >= 0 ? unreleased.substring(nl + 1).strip() : "";
        if (rest.startsWith("## [")) unreleased = rest;
    }
    Files.writeString(
        web.resolve("src/pages/whats-new.md"),
        "---\n"
            + "layout: ../layouts/WhatsNewLayout.astro\n"
            + "title: What's New\n"
            + "---\n\n"
            + "<!-- AUTO-GENERATED by scripts/GenRoadmap.java from Editora's CHANGELOG.md. Do not edit by hand. -->\n\n"
            + unreleased + "\n",
        StandardCharsets.UTF_8);

    int rmItems = roadmap.stream().mapToInt(s -> s.items().size()).sum();
    IO.println("Wrote roadmap.ts: " + rmItems + " items in " + roadmap.size() + " sections");
    IO.println("Wrote whatsnew.ts: " + news.size() + " items");
    IO.println("Wrote whats-new.md: " + unreleased.length() + " chars");
    IO.println("Wrote version.ts: " + version);
}
