// GitHub Pages refuses a published site over 1 GB. Every release archives a
// version under /docs/v-<version>/: a copy of the guide and ~1,700 small
// command stubs (src/components/ArchivedCommand.astro), about 5 MB. This fails
// well before the limit (and warns earlier) so trimming the archive is a
// planned change, not a failed deploy.
//
//   npm run check:size   (after npm run build)
import { readdirSync, statSync } from "node:fs";
import { join } from "node:path";

const FAIL_MB = 900;
const WARN_MB = 750;

function bytes(dir) {
  let total = 0;
  for (const d of readdirSync(dir, { withFileTypes: true })) {
    const p = join(dir, d.name);
    total += d.isDirectory() ? bytes(p) : statSync(p).size;
  }
  return total;
}

const mb = Math.round(bytes("dist") / 1024 / 1024);
if (mb > FAIL_MB) {
  console.error(`check-size: dist/ is ${mb} MB, over the ${FAIL_MB} MB budget (GitHub Pages stops at 1 GB).`);
  process.exit(1);
}
const note = mb > WARN_MB ? ` — past ${WARN_MB} MB, plan to trim the archived versions` : "";
console.log(`check-size: dist/ is ${mb} MB (budget ${FAIL_MB} MB)${note}`);
if (mb > WARN_MB && process.env.GITHUB_ACTIONS) console.log(`::warning::dist/ is ${mb} MB${note}`);
