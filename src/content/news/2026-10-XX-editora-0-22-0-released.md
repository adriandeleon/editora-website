---
title: "Editora 0.22.0: remote development on the host, and a round of editing commands"
description: "Editora 0.22.0 runs Git, builds, language servers and debuggers on the host of a remote folder, lets you choose a file's encoding, adds line commands, text tools and more multiple-cursor commands, rulers per language and indent guides, Save All and Reopen Closed Tab, and Emacs registers."
date: 2026-10-31
version: "0.22.0"
---

<!-- DRAFT (website-next): set the date, file name and version on release day, and fill the sections marked
TODO from CHANGELOG.md's 0.22.0 section. The editing sections below are complete. -->

**Editora 0.22.0** is out. Remote folders now work like local ones: the tools
Editora drives run on the host. The rest of the release is a round of everyday
editing commands. Download it from the
[0.22.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.22.0).

## Remote development

<!-- TODO from CHANGELOG: remote execution per host, remote debugging, language servers, outside changes,
run/build/test, Find in Files and Go to File, Git, a window per remote project. -->

## File encodings

A file that is not valid UTF-8 used to open through a lossless stand-in, and you
could not say what it really was. Click the encoding in the status bar:

- **Reopen with Encoding…** reads the file again in the encoding you pick, from
  Shift_JIS, GBK, Big5 and windows-1251 to every other encoding the JDK knows. If
  the file is not valid in it, nothing changes and the status line says where.
- **Save with Encoding…** writes the file in another encoding from its next save,
  and refuses text the encoding cannot hold instead of writing question marks.

The choice is remembered for the file, also for files on a remote host. An
encoding Editora had to guess is shown in the warning colour.
[More about file encodings](/features/file-encodings).

## Editing

- **Line commands** that work at every cursor: Insert Line Below and Above,
  Delete Line, Select Line, and Indent and Outdent Lines.
- **Multiple cursors**: Split Selection into Lines, Add Carets to Line Ends,
  Skip Occurrence and Remove Last Added Caret.
- **Text tools** in the String Manipulation picker: encode and decode Base64, URL,
  JSON, HTML and `\uXXXX` text, increment and decrement the number at each cursor,
  keep or remove the lines matching a pattern, and two more sorts.
- **Rulers** at any columns, with their own set per language, and **indent
  guides**, on by default.
- **Save All**, **Revert File** and **Reopen Closed Tab** (`Ctrl+Shift+T`). The
  File menu keeps its export formats under **Export**.
- **Emacs registers**: `C-x r s` and `C-x r i` for text, `C-x r SPC` and
  `C-x r j` for places.

See [line commands and text tools](/features/line-and-text-tools),
[multiple cursors](/features/multiple-cursors),
[rulers and indent guides](/features/rulers-indent-guides) and
[Emacs heritage](/features/emacs-heritage).

## Markdown

<!-- TODO from CHANGELOG: wiki links and backlinks, more syntax in preview and exports, preview selection,
link updates on rename, 29 more lint rules, format document, completion, navigation, settings. -->

## HTTP Client

<!-- TODO from CHANGELOG: closer to the JetBrains format, response view, per-file history and environments,
secrets not sent without asking, Copy as cURL safe to paste, the fixes. -->

## Also

<!-- TODO from CHANGELOG: run/test spinners, Recent Files dropdown, abbreviation previews, Clear Java Index
Caches, smaller Linux app, word wrap per kind of file, the remaining fixes. -->
