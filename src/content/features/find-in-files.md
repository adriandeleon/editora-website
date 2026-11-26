---
title: "Find in files"
group: "Workspace & files"
order: 5
beta: false
summary: "Project-wide search and replace with results grouped by file, unsaved text included, fast with ripgrep, plus incremental find in the file and AceJump."
description: "Project-wide search and replace with results grouped by file, using ripgrep when installed and on the host for remote folders, plus AceJump."
alt: "Find in Files searching the project for \"themeFor\" with ripgrep: 10 matches in 6 files, grouped by file"
---

Search and replace across a whole project, unsaved edits included, and get answers quickly even in a large repository or on a server.

## Search the project

- **Find in Files** opens with `C-S-f` in the Emacs keymap and `Ctrl+Shift+F` in the others (`Cmd+Shift+F` on macOS), with matches grouped by file.
- **Case, regex and whole-word** options, plus include and exclude globs, narrow what it finds.
- **Unsaved text counts**: open files are searched as they are in the editor, not as they are on disk.
- **Skips the noise**: what `.gitignore` excludes, every `node_modules` folder, binary files and files over 2 MB.
- **Fast with ripgrep** when it is installed. In a [remote folder](/features/remote-sftp/), the host's own ripgrep searches in one command.
- **Find in Files (Popup)** is a quick, keyboard-first search when you do not want the tool window.

## Replace across files

**Replace in files** rewrites the matches after a confirmation. Open files change in the editor, where Undo works, and closed files keep their line endings.

## Find in the current file

- **Incremental search** with `C-s` and `C-r` in the Emacs keymap, `Ctrl+F` in the others (`Cmd+F` on macOS), highlights every match and counts them.
- **Go: AceJump**, `M-g j` in the Emacs keymap, labels each visible spot so you can jump the caret there by typing its label.

See the [Find in files guide](/docs/navigation/#find-in-files).
