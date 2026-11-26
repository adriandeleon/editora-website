// Checks that every generated page still matches the app: runs each generator
// in scripts/ against Editora at the release the site documents (the tag
// v<version> of the committed src/lib/version.ts) and fails when the output
// differs from what is committed. So the user guide (copied from Editora's
// user-docs/), the Commands and Keybindings data, What's new, the
// version, the Pauta conformance page and the feature pages cannot drift from the app without the build saying so
// (pauta-project.dev, Documentation: DOCS-04; Conformance: PAUTA-14).
//
// Where the app comes from, first match wins:
//   1. --editora <dir> (or EDITORA_SRC): a tree already at the right release.
//   2. A local Editora clone (EDITORA_REPO, or a sibling Editora-V2/Editora of
//      this repo or of its worktrees folder) that has the tag: `git archive`
//      of the tag into a temporary folder, so the clone's own branch is untouched.
//   3. A shallow clone of the tag from GitHub.
// The user guide (SyncDocs) comes from src/lib/guide-ref.ts's branch instead of
// the tag when one is named there: a guide fix made after the release. That
// branch must descend from the tag and change nothing outside user-docs/.
// --allow-unreleased: when the tag does not exist yet (a draft for the next
// release, as on website-next) say so and pass instead of failing.
//
// It rewrites the generated files in place, so after a failure `git diff`
// shows what changed; commit the regenerated files, or fix the generator.
//
//   npm run check:generated [-- --allow-unreleased] [-- --editora /path/to/Editora]
import { execFileSync, spawnSync } from "node:child_process";
import { existsSync, mkdtempSync, readFileSync, rmSync } from "node:fs";
import { tmpdir } from "node:os";
import { dirname, join, resolve } from "node:path";

const root = resolve(new URL("..", import.meta.url).pathname);
const GITHUB = "https://github.com/adriandeleon/Editora.git";

// Each generator and the paths it writes, relative to the repo root.
const GENERATORS = [
  // First: the guide copied from Editora's user-docs/, before GenPauta adds its
  // page to the same folder.
  ["SyncDocs.java", ["src/content/docs", "src/content/command-docs", "public/screenshots/docs"]],
  ["GenCommands.java", ["src/lib/commands.ts"]],
  [
    "GenRoadmap.java",
    ["src/lib/version.ts", "src/lib/release-series.ts", "src/pages/whats-new.md", "src/pages/whats-new"],
  ],
  ["GenPauta.java", ["src/content/docs/pauta.md"]],
  ["GenFeatures.java", ["src/content/features"]],
];

const args = process.argv.slice(2);
const allowUnreleased = args.includes("--allow-unreleased");
const at = args.indexOf("--editora");
const given = at >= 0 ? args[at + 1] : process.env.EDITORA_SRC;

const version = /version = "([^"]+)"/.exec(readFileSync(join(root, "src/lib/version.ts"), "utf8"))?.[1];
if (!version) {
  console.error("check-generated: no version in src/lib/version.ts");
  process.exit(2);
}
const tag = `v${version}`;
const guideRef = /guideRef: string \| null = "([^"]+)"/.exec(
  readFileSync(join(root, "src/lib/guide-ref.ts"), "utf8"),
)?.[1];

function git(cwd, ...rest) {
  return spawnSync("git", ["-C", cwd, ...rest], { encoding: "utf8" });
}

function localClone() {
  const candidates = [];
  if (process.env.EDITORA_REPO) candidates.push(process.env.EDITORA_REPO);
  for (const base of [dirname(root), dirname(dirname(root))]) {
    candidates.push(join(base, "Editora-V2"), join(base, "Editora"));
  }
  return candidates.find((c) => existsSync(join(c, "pom.xml")) && git(c, "rev-parse", "--git-dir").status === 0);
}

let src;
let temp;
if (given) {
  src = resolve(given);
  console.log(`check-generated: Editora from ${src} (taken to be ${tag})`);
} else {
  temp = mkdtempSync(join(tmpdir(), "editora-src-"));
  const local = localClone();
  if (local && git(local, "rev-parse", "--verify", "--quiet", `refs/tags/${tag}`).status === 0) {
    execFileSync("sh", ["-c", `git -C "$1" archive "$2" | tar -x -C "$3"`, "sh", local, tag, temp]);
    src = temp;
    console.log(`check-generated: Editora ${tag} from ${local}`);
  } else {
    const remote = spawnSync("git", ["ls-remote", "--exit-code", "--tags", GITHUB, `refs/tags/${tag}`], {
      encoding: "utf8",
    });
    if (remote.status === 2) {
      rmSync(temp, { recursive: true, force: true });
      const what = `Editora has no tag ${tag}: src/lib/version.ts names a release that is not out yet`;
      if (allowUnreleased) {
        console.log(`check-generated: ${what}; skipped (--allow-unreleased).`);
        process.exit(0);
      }
      console.error(`check-generated: ${what}.`);
      process.exit(1);
    }
    if (remote.status !== 0) {
      rmSync(temp, { recursive: true, force: true });
      console.error(`check-generated: could not reach ${GITHUB}: ${remote.stderr.trim()}`);
      process.exit(2);
    }
    execFileSync("git", ["clone", "--quiet", "--depth", "1", "--branch", tag, GITHUB, temp], { stdio: "inherit" });
    src = temp;
    console.log(`check-generated: Editora ${tag} from ${GITHUB}`);
  }
}

// The tree the guide is copied from: the release itself, or guideRef's branch.
let guideSrc = src;
let guideTemp;
if (guideRef) {
  guideTemp = mkdtempSync(join(tmpdir(), "editora-guide-"));
  guideSrc = guideTree(guideRef, guideTemp);
}

try {
  for (const [script] of GENERATORS) {
    const from = script === "SyncDocs.java" ? guideSrc : src;
    execFileSync("java", [join("scripts", script), from], { cwd: root, stdio: "inherit" });
  }
} finally {
  if (temp) rmSync(temp, { recursive: true, force: true });
  if (guideTemp) rmSync(guideTemp, { recursive: true, force: true });
}

// Extracts guideRef's tree into `into` after checking that it is the release
// plus guide changes only. Uses the local clone when it has the branch (as
// origin/<ref> or <ref>), else a shallow clone from GitHub.
function guideTree(ref, into) {
  let repo = localClone();
  let rev = null;
  for (const cand of repo ? [`refs/remotes/origin/${ref}`, `refs/heads/${ref}`] : []) {
    if (git(repo, "rev-parse", "--verify", "--quiet", cand).status === 0) {
      rev = cand;
      break;
    }
  }
  if (!rev || git(repo, "rev-parse", "--verify", "--quiet", `refs/tags/${tag}`).status !== 0) {
    repo = join(into, ".clone");
    const clone = spawnSync(
      "git",
      ["clone", "--quiet", "--bare", "--depth", "200", "--branch", ref, GITHUB, repo],
      { encoding: "utf8" },
    );
    if (clone.status !== 0) {
      console.error(`check-generated: src/lib/guide-ref.ts names ${ref}, which Editora does not have: ${clone.stderr.trim()}`);
      process.exit(1);
    }
    execFileSync("git", ["-C", repo, "fetch", "--quiet", "--depth", "200", "origin", `refs/tags/${tag}:refs/tags/${tag}`]);
    rev = "HEAD";
  }
  if (git(repo, "merge-base", "--is-ancestor", `refs/tags/${tag}`, rev).status !== 0) {
    console.error(`check-generated: ${ref} does not descend from ${tag}; cut it from the release tag.`);
    process.exit(1);
  }
  const outside = git(repo, "diff", "--name-only", `refs/tags/${tag}`, rev)
    .stdout.split("\n")
    .filter((f) => f && !f.startsWith("user-docs/"));
  if (outside.length > 0) {
    console.error(`check-generated: ${ref} changes files outside user-docs/ since ${tag}:\n  ${outside.join("\n  ")}`);
    process.exit(1);
  }
  execFileSync("sh", ["-c", `git -C "$1" archive "$2" | tar -x -C "$3"`, "sh", repo, rev, into]);
  console.log(`check-generated: user guide from ${ref} (${tag} plus guide fixes)`);
  return into;
}

const paths = GENERATORS.flatMap(([, out]) => out);
const changed = git(root, "status", "--porcelain", "--", ...paths).stdout.trim();
if (changed) {
  console.error(`\ncheck-generated: these generated files differ from Editora ${tag}:\n${changed}`);
  console.error(
    "Re-run the generators against the release tag and commit their output (README, Generated data); " +
      "for a feature page, edit scripts/GenFeatures.java instead of the Markdown.",
  );
  process.exit(1);
}
console.log(`check-generated: every generated page matches Editora ${tag}${guideRef ? ` (guide from ${guideRef})` : ""}.`);
