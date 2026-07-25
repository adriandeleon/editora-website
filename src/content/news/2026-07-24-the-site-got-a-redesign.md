---
title: "The site got a redesign"
description: "A rebuilt home page, a live command-palette demo, side-by-side keymaps, and search across every page with Cmd-K."
date: 2026-07-24
---

The website has been rebuilt around a simple complaint: it was underselling the
editor. The old home page led with one screenshot and a download button, then
listed features as text. If you had never run Editora, nothing on the page
showed you what a keyboard-driven editor actually feels like.

**The home page now demonstrates instead of describes.** The hero runs a live
command palette: a query types itself in, the list narrows, a command lights up.
Below it, feature cards carry thumbnails rather than prose alone.

**Four new sections** cover ground the site never had:

- **One editor, four ways to run it.** Zen, Expert, Simple UI, and the full IDE,
  each drawn as a small mockup so you can see what each mode hides and keeps.
  Worth knowing: Zen and Expert only hide the interface, and everything keeps
  running underneath. Simple UI is the one that actually switches features off.
- **Works with your stack.** The 21 language servers and every live preview
  format, in one wall.
- **Your keymap, your keys.** The same twelve everyday actions across Emacs,
  CUA, Sublime, VS Code, and IntelliJ, side by side. The chords come straight
  from the shipped keymaps, so the table cannot drift from the app.
- **Uses the tools you already have.** Editora shells out to the git, ripgrep,
  language servers, and diagram tools already on your machine instead of
  bundling copies. That section explains why that is a feature, and introduces
  [Doctor](/features/doctor), the health screen that reports what was found and
  offers to install what is missing.

**Search arrived.** Press <kbd>Cmd-K</kbd> (or <kbd>Ctrl-K</kbd>, or just
<kbd>/</kbd>) anywhere on the site to search every page: documentation, every
command, features, blog posts, and news. It runs entirely in your browser
against an index built with the site. No accounts, no third-party service, and
nothing is sent anywhere.

**The command reference is complete again.** Regenerating it turned up 71
commands that had never made it onto the site, including the whole Maven, npm,
Cargo, Go, and Gradle family and the Emacs rectangle commands. The generator now
cross-checks against the app's own catalogue, so a command can no longer be
registered in Editora and quietly missing here.

There is more on how it was built, and on two bugs found along the way, in
[Rebuilding the website](/blog/rebuilding-the-editora-website).
