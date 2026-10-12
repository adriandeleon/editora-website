---
title: Plugins
description: Install plugins from the registry, and write your own to extend Editora.
category: Customization
order: 4
---

Editora can be extended with **plugins**, which add commands, keybindings, tool
windows, editor-menu items, and status-bar segments. Browse the
[plugin catalog](/plugins) for what's available, or write your own.

> Plugins are **off by default** and run with full access (no sandbox). Only
> install plugins you trust.

## Installing

1. Enable plugins in **Settings → Plugins**.
2. **Browse plugins…** (or the `plugins.browse` command) installs from the
   official registry; **Install from file…** installs a local `.zip`.
3. Confirm the **Enable this plugin?** dialog, which lists what the plugin will
   do. If you decline, the plugin stays installed but not enabled; its checkbox
   in Settings → Plugins enables it later, behind the same dialog.
4. Restart Editora to load newly installed plugins.

Installed plugins live in your config folder under `plugins/<id>/`, and the
enabled set is tracked in `plugins.json`. (Plugins are loaded at startup, so
enabling/disabling takes effect on the next launch.)

Updating a plugin replaces its folder but keeps `plugins/<id>/data/`, where a
plugin stores its own files. Removing the plugin deletes that folder too.

## Security

Plugins are not sandboxed: an enabled plugin runs with the same access to your
files and system as Editora itself. What Editora checks is where a plugin came
from, and that you agreed to it.

- **Signed registry.** The registry's `index.json` is verified against an
  Ed25519 public key bundled with Editora. **Require signed plugins**
  (Settings → Plugins, on by default; `plugins.toggleRequireSignature`) blocks
  browsing and installing from a registry whose signature is missing or does
  not verify. A registry of your own (`plugins.setRegistryUrl`) cannot be
  signed with that key, so using one means turning the requirement off.
- **Checksum.** A plugin downloaded from the registry must match the SHA-256
  listed in the index, and the registry and its downloads are HTTPS-only. A
  `.zip` installed from a file has no checksum to match.
- **Consent.** Before a plugin is enabled, a dialog lists whether it runs
  executable code (a Java plugin), which external commands it declares, and
  which keyboard shortcuts it remaps. Each plugin has its own enable switch,
  and a plugin whose capabilities cannot be shown stays disabled.

A valid signature tells you who published the registry. It does not make a
plugin safe.

## What a plugin can do

A plugin can:

- **Register commands** (they appear in the palette) and **bind keys** to them.
- **Add a tool window** (left / right / bottom) with custom UI.
- **Add editor right-click menu items** and **status-bar segments**.
- **Ship snippets and file templates** that merge into the built-in sets. A
  plugin's entry overrides a bundled one with the same name or id, and your own
  overrides both.
- **Run external tools** (formatters, task runners) via a subprocess.

## Writing a plugin

A plugin is a folder with a `plugin.json` manifest plus, optionally, a Java jar
and `snippets/` / `templates/` directories. The simplest plugins are purely
**declarative** (commands that run a shell command, keybindings, asset folders);
richer ones implement the `Plugin` interface against the exported
`com.editora.plugin` API.

Start from the **[Example Plugin](https://github.com/adriandeleon/editora-plugins/tree/main/plugins/example)**,
which exercises every extension point, and read the full authoring guide in
[`docs/plugins.md`](https://github.com/adriandeleon/Editora/blob/master/docs/plugins.md).

## Publishing

The official registry is
[adriandeleon/editora-plugins](https://github.com/adriandeleon/editora-plugins),
a curated `index.json` plus each plugin's source and a downloadable `.zip`
(verified by sha-256 on install). Open a pull request there to add yours.
