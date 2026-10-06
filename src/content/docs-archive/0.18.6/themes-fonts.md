---
title: Themes & fonts
description: Editor color themes, bundled monospace fonts, and text zoom.
category: Customization
order: 3
---

Appearance settings live in **Settings → Appearance**, which includes a live
preview that recolors as you change things.

## Themes

**Editora Light** and **Editora Dark** are the app's own pair, drawn from the
palette in its icon: a teal accent, an ink-navy ground, and a periwinkle reserved
for one job — a keybinding. They are what a fresh install starts in. An existing
install keeps whatever theme it was already on; the pair simply joins the list.

Twenty-eight themes ship in total:

- **Editora** (light and dark) — the default
- **Primer** (GitHub-style), **Nord** and **Cupertino** — each light and dark
- **Dracula**
- A community set of nineteen: Army, Autumn, Blacky, Blue, Browny, Fall, Navy,
  News, Spring, Summer, Winter and Yacht

The app chrome is themed by AtlantaFX; the editor surface, gutter, syntax tokens
and project tree are themed separately. By default the **editor** theme follows
the app theme; pick one explicitly and it sticks.

### What the colors mean

Colors carry a fixed meaning across the whole interface, and they follow the
theme rather than being fixed values that only suit one background:

| Color | Means |
| --- | --- |
| Amber | Not saved yet — an unsaved tab, a read-only or narrowed buffer, a snapshot |
| Red | Broken |
| Green | Verified |
| Olive / violet | Git's untracked / renamed |
| Periwinkle | A keybinding, and only ever a keybinding |

The Markdown preview can be themed independently of the app with its own
light/dark toggle (a sun/moon button on the preview's zoom control), so you can
read a GitHub-style light page inside a dark editor.

**User themes**: drop your own theme CSS in a `themes/` (app + editor theme) or
`editor-themes/` (editor only) folder in your [config directory](/docs/configuration),
and it joins the picker. *Theme: Reload User Themes* (`theme.reloadUserThemes`)
picks up changes without a restart.

## Fonts

Five monospace families ship with the app, so nothing needs a system install:

- **JetBrains Mono** (default)
- **Cascadia Code**
- **Fira Code**
- **IBM Plex Mono**
- **Source Code Pro**

They're listed ahead of your system monospace families in the font picker.

The **interface itself** is set in **Inter** on every platform, so Editora looks
the same wherever you run it rather than inheriting each system's default UI
font. The Markdown preview and PDF prose use Inter too.

## Text zoom

Zoom the editor text independently of the configured font size:

| Action | Default key |
| --- | --- |
| Zoom in | `C-=` |
| Zoom out | `C--` |
| Reset | `C-0` |

Ctrl+wheel zooms too, and the status bar has `−` / `+` buttons. When the active
buffer is a Markdown preview, the same gestures zoom the preview instead of the
editor text. The zoom level is saved as `fontZoom` and isn't shown in Settings.
