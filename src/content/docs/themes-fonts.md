---
title: Themes & fonts
description: Editor color themes, bundled monospace fonts, and text zoom.
category: Customization
order: 3
---

Appearance settings live in **Settings → Appearance**, which includes a live
preview that recolors as you change things.

## Themes

The app chrome is themed by AtlantaFX. The editor surface, gutter, syntax
tokens, and project tree are themed separately, with six editor color themes:

- **Primer** (the default, GitHub-style)
- **Nord**
- **Cupertino**
- **Dracula**
- **Islands** (JetBrains-style)

Each comes in **light and dark**. By default the editor theme follows the
AtlantaFX app theme; pick one explicitly and it sticks. A theme colors the syntax
tokens, editor surface, gutter, and the Project tree together.

The Markdown preview can be themed independently of the app with its own
light/dark toggle (a sun/moon button on the preview's zoom control), so you can
read a GitHub-style light page inside a dark editor.

## Fonts

Five monospace families ship with the app, so nothing needs a system install:

- **JetBrains Mono** (default)
- **Cascadia Code**
- **Fira Code**
- **IBM Plex Mono**
- **Source Code Pro**

They're listed ahead of your system monospace families in the font picker. The
Markdown preview and PDF prose use **Inter** on every platform.

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
