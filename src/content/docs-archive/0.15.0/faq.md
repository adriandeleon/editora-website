---
title: FAQ
description: Licensing, telemetry, platforms, signing, which features are on by default, external tools, startup speed, and customization.
category: Help
order: 1
---

## Is Editora free and open source?

Yes, it's free and released under the MIT License. Source is on
[GitHub](https://github.com/adriandeleon/Editora).

## Does it phone home or collect telemetry?

There are no accounts, no telemetry, and no analytics. Nothing about you or your
code is collected or sent anywhere as a matter of course.

Two honest exceptions, so you don't have to discover them yourself:

- **The update check.** Once a day at startup Editora asks GitHub whether a
  newer release exists. It sends nothing about you or your files, and it's a
  checkbox in **Settings → Workspace** if you'd rather it didn't.
- **The AI features**, which are off by default. If you turn them on, the text
  you act on is sent to whichever endpoint you configure (a local model, or a
  provider you supply a key for). That's the whole point of the feature, but it
  is the one part of Editora that transmits your code, so it stays opt-in.

Plugins you install run with full access and can do their own thing (see below).

## Which platforms are supported?

macOS (Intel & Apple Silicon), Windows (x64), and Linux (x64 & arm64).
Windows-on-ARM runs the x64 installer under emulation.

## Do I need Java installed?

Not for the native installers; they bundle their own Java runtime. The portable
fat jar does need JDK 25 (`java -jar`). Running a Java file from the gutter ▶
also needs JDK 25 on your `PATH`.

## The installer says it's from an unidentified developer.

Installers are currently unsigned. On macOS, right-click the app and choose
*Open* the first time; on Windows, click "More info → Run anyway" in SmartScreen.
Signing is on the roadmap. See [Troubleshooting](/docs/troubleshooting).

## Which features are off by default?

Fewer than you might expect, because there are two different things going on.

**Off until you ask for them** are the features that run code, reach the
network, or change how the editor works: language servers, debugging, plugins,
the AI features, the MCP server, and projects. Turn on what you want in
Settings; each has its own page in these docs.

**On, but invisible until their tool exists** is the larger group. Git, GitHub,
Mermaid, the diagram tools, Typst and the rest are enabled out of the box, and
they self-gate: the button, panel or gutter icon simply isn't there until
Editora finds the command it needs. So a fresh install stays uncluttered
without you having to switch anything on, and installing `git` or `mmdc` later
lights the feature up on its own. [Doctor](/features/doctor) shows you exactly
what was found.

## How do I add a language, snippet, or dictionary word?

Snippets: drop a `snippets/<language>.json` in your config folder (they override
the bundled ones). Spell-check: "Add to Dictionary" saves to `dictionary.txt`,
and you pick a language per file with "Spell Check: Set Language…". See
[Configuration](/docs/configuration) and
[Languages & highlighting](/docs/languages).

## Do I need to install anything else?

No, for editing, previews, and everything that works on plain text. Yes, for
the features that drive an external tool, and by design: Editora shells out to
the `git`, `ripgrep`, language servers and diagram tools already on your
machine instead of bundling its own copies, so you get your versions and your
configuration.

Nothing breaks when a tool is missing. The feature stays hidden until it
appears. Run **Doctor** (`view.doctor`) to see what was found, what's only
half-configured, and what's missing, with an install button on the ones Editora
can fetch for you. See [Doctor](/features/doctor).

## Is this an Emacs clone? Do I need to know Emacs?

No, and no. Emacs is the default keymap because the editing model is genuinely
inherited: a kill ring, a mark ring, `C-x r` rectangles, narrowing,
query-replace, `C-u` prefix arguments. If that's your muscle memory, most of it
is already here, and [Emacs heritage](/features/emacs-heritage) lists what
carried over and what deliberately didn't.

If it isn't, pick CUA, Sublime Text, VS Code or IntelliJ IDEA in
**Settings → Keymaps** and you'll never meet a `C-x` chord. There's no Elisp
and no modal editing, and every command is discoverable by name in the palette
rather than something you have to know in advance.

## How does it compare to VS Code or IntelliJ?

There's a page for that: [how Editora compares](/compare). It covers startup
measurements taken on one machine with one method, where each editor is
stronger, and a section on when Editora is probably the wrong choice for you.

## Can I rebind keys or use a different keymap?

Yes. In **Settings → Keymaps** you can switch between Emacs (default), CUA,
Sublime Text, VS Code, and IntelliJ IDEA, and rebind any command with the
built-in editor. Changes apply live and are saved in `settings.toml`. See
[Keymaps & keybindings](/docs/keymaps).

## Are plugins safe?

Plugins are off by default and run with full access (no sandbox), like a VS Code
or IntelliJ extension. The registry adds defenses (a capability disclosure
before you enable one, sha-256 verification of downloads, and an Ed25519-signed
index), but the honest rule is to only install plugins you trust. See
[Plugins](/docs/plugins).

## Where are my settings and how do I reset them?

In a config folder (`~/.editora/` by default). Its live path is in **About
Editora**. You can export it or reset to defaults from **Settings → Advanced**,
and run a throwaway config with `--dev` or `--config-dir`. See
[Configuration](/docs/configuration).

## Is it slow to start? It's a Java app.

That's the usual assumption, so it's measured rather than asserted. On one
Apple M5, opening the same file, Editora and VS Code were level at about 1.5
seconds, and IntelliJ IDEA took about 5.6. Opening a real project folder,
Editora reached its window in about 2.3 seconds against 2.9 for VS Code and 7.3
for IntelliJ. The method, the caveats, and the run-to-run spread are all on
[how Editora compares](/compare).

The native builds bake in an AOT class cache, which is worth roughly a third of
the startup time on its own. If startup or editing ever feels slow, see
[Troubleshooting](/docs/troubleshooting#performance).

## Why JavaFX?

It gives one native-feeling, cross-platform codebase with a fast text surface
(RichTextFX), and lets Editora ship a single app per platform without a browser
engine.

Didn't find your answer?
[Open an issue](https://github.com/adriandeleon/Editora/issues).
