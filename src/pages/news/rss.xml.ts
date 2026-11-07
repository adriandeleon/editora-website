import rss from "@astrojs/rss";
import { getCollection } from "astro:content";
import type { APIContext } from "astro";

// Newest first. Several items can share a calendar day (0.12.1 and 0.12.3, 0.18.2
// and 0.18.3), and dates are date-only, so ties are broken by release version
// (higher first, a versioned item ahead of an unversioned one) and then by id.
const versionParts = (v?: string) => (v ? v.split(".").map((n) => Number.parseInt(n, 10) || 0) : []);
const byNewest = (
  a: { id: string; data: { date: Date; version?: string } },
  b: { id: string; data: { date: Date; version?: string } },
) => {
  const byDate = b.data.date.valueOf() - a.data.date.valueOf();
  if (byDate !== 0) return byDate;
  const va = versionParts(a.data.version);
  const vb = versionParts(b.data.version);
  for (let i = 0; i < Math.max(va.length, vb.length); i++) {
    const d = (vb[i] ?? -1) - (va[i] ?? -1);
    if (d !== 0) return d;
  }
  return b.id.localeCompare(a.id);
};

// Posts are dated by day only, so same-day posts would share one midnight pubDate and
// readers could list them in either order. Each one gets a minute per same-day post
// listed after it (older in the order above), so the newest of the day sorts first.
const MINUTE = 60_000;

export async function GET(context: APIContext) {
  const items = (await getCollection("news")).sort(byNewest);
  const origin = context.site ?? new URL("https://editora-project.dev");
  const channel = new URL("/news/", origin).href;
  const self = new URL("/news/rss.xml", origin).href;
  return rss({
    title: "Editora News",
    description: "Release announcements and updates from the Editora project.",
    // The channel's <link> is the page the feed mirrors.
    site: channel,
    xmlns: { atom: "http://www.w3.org/2005/Atom" },
    customData: `<language>en-us</language><atom:link href="${self}" rel="self" type="application/rss+xml"/>`,
    items: items.map((p, i) => {
      const day = p.data.date.valueOf();
      const laterSameDay = items.slice(i + 1).filter((q) => q.data.date.valueOf() === day).length;
      return {
        title: p.data.title,
        description: p.data.description ?? "",
        pubDate: new Date(day + laterSameDay * MINUTE),
        link: new URL(`/news/${p.id}/`, origin).href,
      };
    }),
  });
}
