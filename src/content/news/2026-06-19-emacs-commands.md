---
title: "More Emacs editing and movement commands"
description: "Backward-kill-word, word and region case commands, balanced-expression navigation, zap-to-char, and more fill the gaps versus a standard Emacs global-map."
date: 2026-06-19
---

Editora's Emacs keymap got a lot closer to a standard `global-map`. A batch of
editing and movement commands landed, all palette-discoverable and rebindable
like any other command.

Text surgery:

- **backward-kill-word** (`M-DEL`)
- case commands **upcase / downcase / capitalize-word** (`M-u` / `M-l` / `M-c`)
  and **upcase / downcase-region** (`C-x C-u` / `C-x C-l`)
- **join-line** (`M-^`), **delete-horizontal-space** (`M-\`),
  **just-one-space** (`M-SPC`), **delete-blank-lines** (`C-x C-o`),
  **open-line** (`C-o`), **kill-whole-line** (`C-S-DEL`), and
  **zap-to-char** (`M-z`)

Navigation:

- **forward / backward-sexp** (`C-M-f` / `C-M-b`), **mark-sexp** (`C-M-SPC`),
  **kill-sexp** (`C-M-k`)
- **beginning / end-of-defun** (`C-M-a` / `C-M-e`), **mark-paragraph** (`M-h`),
  **mark-whole-buffer** (`C-x h`), and **move-to-window-line** (`M-r`)

A kill ring (yank-pop and consecutive-kill accumulation) is still deferred, so
these "kill" commands delete, and cut/copy/paste keep using the system
clipboard. See the [keybindings reference](/keybindings) for the full Emacs map.
