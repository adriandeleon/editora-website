// The blog was folded into News (2026-11-03), and its feed lived here. Feed
// readers do not follow an HTML redirect, so this URL keeps serving the News
// feed itself, and anyone still subscribed to the old address keeps getting
// posts.
export { GET } from "../news/rss.xml.ts";
