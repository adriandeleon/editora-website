import { defineCollection, z } from "astro:content";
import { glob } from "astro/loaders";

// Documentation pages — Markdown, organized by category + order for the sidebar.
const docsSchema = z.object({
  title: z.string(),
  description: z.string().optional(),
  category: z.string().default("Guide"),
  order: z.number().default(100),
  beta: z.boolean().default(false),
});

const docs = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/docs" }),
  schema: docsSchema,
});

// Frozen copies of the docs as a past release shipped them, one directory per
// version, so /docs/v-0.9.10/… keeps describing 0.9.10 rather than whatever the
// docs say today. Entry ids are "<version>/<slug>" (e.g. "0.9.10/lsp"); the
// version registry in src/lib/doc-versions.ts maps them onto routes.
const docsArchive = defineCollection({
  // The id must stay the literal "<version>/<slug>". The loader's default id
  // generation slugifies, which eats the dots in a version number ("0.9.10/lsp"
  // became "0910/lsp") and silently produced zero matching pages.
  loader: glob({
    pattern: "**/*.md",
    base: "./src/content/docs-archive",
    generateId: ({ entry }) => entry.replace(/\.md$/, ""),
  }),
  schema: docsSchema,
});

// Short, release-style announcements.
const news = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/news" }),
  schema: z.object({
    title: z.string(),
    description: z.string().optional(),
    date: z.coerce.date(),
    version: z.string().optional(),
    beta: z.boolean().default(false),
  }),
});

// Longer-form articles.
const blog = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/blog" }),
  schema: z.object({
    title: z.string(),
    description: z.string().optional(),
    date: z.coerce.date(),
    author: z.string().default("Adrian De Leon"),
    tags: z.array(z.string()).default([]),
    beta: z.boolean().default(false),
  }),
});

// One page per feature — drives both the home grid and /features/<slug>.
const features = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/features" }),
  schema: z.object({
    title: z.string(),
    group: z.string(),
    order: z.number().default(100),
    beta: z.boolean().default(false),
    summary: z.string(), // HTML — the card body on the home page
  }),
});

// Optional long-form docs for individual commands. A file named after a command
// id (e.g. `view.toggleReadOnly.md`) is rendered as the "Details" section on
// that command's page (/docs/commands/<id>). Commands without one still get a
// generated reference page. English-only for now; locales will be added later.
const commandDocs = defineCollection({
  // Keep the entry id exactly equal to the command id (e.g. view.toggleReadOnly),
  // dots and camelCase preserved, so it matches the ids in src/lib/commands.ts.
  loader: glob({
    pattern: "**/*.md",
    base: "./src/content/command-docs",
    generateId: ({ entry }) => entry.replace(/\.md$/, ""),
  }),
  schema: z.object({
    title: z.string().optional(),
  }),
});

export const collections = { docs, docsArchive, news, blog, features, commandDocs };
