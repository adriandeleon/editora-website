# Editora website

The marketing + documentation site for [Editora](https://github.com/adriandeleon/Editora),
a keyboard-driven, cross-platform programmer's text editor. Live at
**[editora-project.dev](https://editora-project.dev)**.

Built with [Astro](https://astro.build) and deployed to GitHub Pages via
`.github/workflows/pages.yml` on every push to `master`.

## Develop

```bash
npm install
npm run dev       # http://localhost:4321
npm run build     # production build to dist/
npm run preview   # serve the build
```

## Content

Most content is Markdown under `src/content/`:

- `docs/` — documentation pages (sidebar grouped by `category` + `order`)
- `blog/` — blog posts
- `news/` — release-style announcements
- `features/` — one page per feature (also drives the home-page grid)

Add a Markdown file with the right frontmatter and it appears automatically;
lists, the sitemap, and RSS regenerate on the next build.

Feature screenshots go in `public/screenshots/features/<slug>.png` (the slug
matches the page URL, e.g. `/features/git` → `git.png`).

## Generated data

`src/lib/commands.ts` (the Commands and Keybindings pages) is generated from
Editora's source by `scripts/gen-commands.py`, and the feature pages were
scaffolded by `scripts/gen-features.py`. Re-running the command generator
requires a local checkout of the [Editora](https://github.com/adriandeleon/Editora)
repository alongside this one (it reads the app's `MainController`,
`messages.properties`, and keymaps).
