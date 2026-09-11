---
title: "Editora 0.18.2: better Emacs window control and clearer actions"
description: "Editora 0.18.2 expands Emacs window-management keys, fixes C-x 1 across editor groups, clarifies Save and Discard actions, and stabilizes editable tab typography."
date: 2026-09-11
version: "0.18.2"
---

**Editora 0.18.2** is out. This patch release makes window management more
natural for Emacs users and sharpens several everyday interface details.
Download it from the
[0.18.2 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.18.2).

## More Emacs window-management keys

Emacs mode now supports familiar commands for arranging Editora's editor and
tool windows:

- `C-x 0` closes the current window.
- `C-x ^` grows the bottom window; `C-u C-x ^` shrinks it.
- `C-x >` and `C-x <` resize side windows toward or away from the editor.
- Repeating `C-x f` toggles maximize and restore.

`C-x 1` also now collapses independent editor groups, in addition to closing a
second view of the active document.

## Clearer Save and Discard choices

Save buttons are now green and Discard buttons red throughout the application,
making the consequence of each choice easier to recognize at a glance. Neutral
actions such as Cancel keep their existing appearance.

Ordinary editable tab labels now consistently use the upright Inter UI face.
They no longer inherit an editor font or retain the temporary italic styling
shown while read-only content is loading.

The complete list is on the [What's New](/whats-new) page.
