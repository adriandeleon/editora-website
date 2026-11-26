---
title: "Plugins"
group: "Customization & extensibility"
order: 2
beta: false
summary: "Extend the editor with commands, tool windows, and integrations. Install from the built-in signed registry, or write your own (Java or a simple manifest)."
description: "Add commands, keybindings and tool windows with plugins written in Java or declared in a manifest, installed from a signed registry after you consent."
alt: "The Browse Plugins picker listing registry plugins with their versions and descriptions"
---

Plugins extend Editora without forking it. A plugin adds commands, keybindings, tool windows, editor-menu items and status-bar segments.

- **Write one in Java** against a small API, or **declare one** in a `plugin.json` manifest with folders of snippets and templates.
- **Install from the registry** with **Plugins: Browse Plugins**. The registry index is signed, and every download is checked against its SHA-256 hash. Browse it on the web in the [plugins catalog](/plugins/).
- **Install from a file** with **Plugins: Install from File…** for a local `.zip`.

## You stay in control

Plugins are not sandboxed, so install only ones you trust. Editora helps you decide:

- **It asks first.** The consent dialog lists a plugin's code, the external commands it runs and the chords it replaces. Nothing is installed until you agree.
- **It asks again** when an update changes the code, commands or chords you agreed to.
- **Built-ins stay safe.** A plugin's commands live in its own namespace, so it cannot replace a built-in command.
- **Trouble is easy to rule out.** Start with `--disable-plugins` for a session without any, and [Doctor](/features/doctor/) lists a plugin that failed to load or start, or started slowly.

**Off by default.** Turn it on in **Settings → Plugins**. See the [Plugins guide](/docs/plugins/#installing).
