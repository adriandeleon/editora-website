/// Copies the user guide from the Editora repo, which owns it, into this site.
/// A JDK 25 compact source file (JEP 512): run with `java scripts/SyncDocs.java`
/// from the website repo root (or the scripts/ dir).
///
/// The guide is written in Editora's `user-docs/` folder, so a change to the app
/// and the page that describes it land in one pull request, and the app can ship
/// the same pages for reading offline. This script mirrors three folders:
///
///   user-docs/guide/*.md               -> src/content/docs/
///   user-docs/commands/*.md            -> src/content/command-docs/
///   user-docs/screenshots/docs/*       -> public/screenshots/docs/
///
/// "Mirror" means a file removed in Editora is removed here too. Two things here
/// are not Editora's and are kept: src/content/docs/pauta.md (GenPauta.java
/// writes it) and public/screenshots/docs/thumbs/ (gen-thumbs.mjs writes it).
/// Like every generator, the output is committed and check:generated holds it to
/// the Editora release the site documents. Never edit the copies here: change
/// them in Editora.
///
/// The Editora checkout is found as a sibling directory (Editora-V2 or Editora);
/// override with an argument or the EDITORA_REPO environment variable.
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

static final String USER_DOCS = "user-docs";

/// One mirrored folder: Editora's source, this site's copy, which files belong to
/// the mirror, and which names in the copy are someone else's and must stay.
record Mirror(String from, String to, Predicate<Path> files, Set<String> keep) {}

static final List<Mirror> MIRRORS = List.of(
    new Mirror("user-docs/guide", "src/content/docs", p -> p.toString().endsWith(".md"), Set.of("pauta.md")),
    new Mirror("user-docs/commands", "src/content/command-docs", p -> p.toString().endsWith(".md"), Set.of()),
    new Mirror("user-docs/screenshots/docs", "public/screenshots/docs", Files::isRegularFile, Set.of("thumbs")));

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
    // A path given explicitly only has to be an Editora tree: one from before the
    // guide moved (0.28.x and older) has no user-docs/, and main() says so.
    if (args.length > 0 && Files.isRegularFile(Path.of(args[0]).resolve("pom.xml"))) {
        return Path.of(args[0]).toAbsolutePath().normalize();
    }
    for (Path c : candidates) {
        if (c != null && Files.isRegularFile(c.resolve("pom.xml")) && Files.isDirectory(c.resolve(USER_DOCS))) {
            return c.toAbsolutePath().normalize();
        }
    }
    throw new IllegalStateException("Could not find an Editora checkout with pom.xml and " + USER_DOCS
        + "/. Pass its path as an argument or set EDITORA_REPO. Tried: " + candidates);
}

/// The top-level files of {@code dir} that {@code accept} takes, by name.
List<Path> files(Path dir, Predicate<Path> accept) throws IOException {
    if (!Files.isDirectory(dir)) return List.of();
    try (Stream<Path> s = Files.list(dir)) {
        return s.filter(Files::isRegularFile).filter(accept).sorted().toList();
    }
}

void main(String[] args) throws IOException {
    Path web = websiteRoot();
    Path root = editoraRoot(args, web);
    if (!Files.isDirectory(root.resolve(USER_DOCS))) {
        // Releases before the guide moved into Editora: the copies here are the
        // guide as written for them, so there is nothing to sync.
        System.out.println("SyncDocs: " + root + " has no " + USER_DOCS + "/ (a release from before the guide"
            + " moved into Editora); the guide here is left as it is.");
        return;
    }
    for (Mirror m : MIRRORS) {
        Path from = root.resolve(m.from());
        Path to = web.resolve(m.to());
        if (!Files.isDirectory(from)) throw new IllegalStateException(from + " is missing");
        Files.createDirectories(to);
        List<Path> sources = files(from, m.files());
        Set<String> names = new java.util.HashSet<>();
        int copied = 0;
        for (Path src : sources) {
            String name = src.getFileName().toString();
            names.add(name);
            Path dst = to.resolve(name);
            if (Files.isRegularFile(dst) && Files.mismatch(src, dst) == -1) continue;
            Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            copied++;
        }
        int removed = 0;
        try (Stream<Path> s = Files.list(to)) {
            for (Path old : s.sorted().toList()) {
                String name = old.getFileName().toString();
                if (names.contains(name) || m.keep().contains(name) || !Files.isRegularFile(old)) continue;
                Files.delete(old);
                removed++;
            }
        }
        System.out.println("Synced " + web.relativize(to) + ": " + sources.size() + " files (" + copied
            + " changed, " + removed + " removed) from " + root.resolve(m.from()));
    }
}
