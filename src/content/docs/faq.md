---
title: FAQ
description: Licensing, telemetry, platforms, signing, default-off features, and customization.
category: Help
order: 1
---

## Is Editora free and open source?

Yes, it's free and released under the MIT License. Source is on
[GitHub](https://github.com/adriandeleon/Editora).

## Does it phone home or collect telemetry?

No. There are no accounts, no telemetry, and no analytics calls. It's a local
editor. Plugins you install can do their own thing (see below).

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

## Why are so many features off by default?

Anything that reaches outside the editor or adds chrome ships off so a fresh
install stays light and predictable. That includes language servers, debugging,
Git, the HTTP client, Mermaid, HTML live preview, personal notes, projects,
remote files, and plugins. Turn on what you want in Settings; each has its own
page in these docs.

## How do I add a language, snippet, or dictionary word?

Snippets: drop a `snippets/<language>.json` in your config folder (they override
the bundled ones). Spell-check: "Add to Dictionary" saves to `dictionary.txt`,
and you pick a language per file with "Spell Check: Set Language…". See
[Configuration](/docs/configuration) and
[Languages & highlighting](/docs/languages).

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

## Is it slow to start?

Cold start is tuned, and the native builds bake in an AOT class cache for a
faster first frame. If startup or editing ever feels slow, see
[Troubleshooting](/docs/troubleshooting#performance).

## Why JavaFX?

It gives one native-feeling, cross-platform codebase with a fast text surface
(RichTextFX), and lets Editora ship a single app per platform without a browser
engine.

Didn't find your answer?
[Open an issue](https://github.com/adriandeleon/Editora/issues).
