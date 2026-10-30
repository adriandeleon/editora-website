---
title: "Editora 0.26.0: a reworked Project Map, a terminal that keeps up with modern programs, and XML, HTML and Bash reviews"
description: "Editora 0.26.0 reworks the Project Map (compact folder chains, Reveal Active File, Hide unmatched, notes on preview cards), gives the terminal a theme of its own, bundled Nerd Font icons, the kitty keyboard protocol, notifications and clipboard copy, and closes the gaps four reviews found in XML, HTML and Bash support. Java library classes now open with their sources."
date: 2026-10-30
version: "0.26.0"
---

**Editora 0.26.0** is out. A review pass reworked the Project Map. The
integrated terminal picked up much of what its standalone sibling, Termina,
has learned. XML, HTML and the Bash language server each had their own
review, and every finding was fixed. Download it from the
[0.26.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.26.0).

## Project Map

- **The first column is your project's files**, listed under the project's
  name, instead of a one-row "Project" column.
- **Compact folder chains.** A chain of single-child folders such as
  `src/main/java/com/acme` opens with one click as one column, titled
  `src / main / java / com / acme`. Each folder stays in the breadcrumb.
- **Reveal Active File** opens or raises the Map on the file you are editing,
  and **Always select opened file** follows every file you open.
- **Hide unmatched** takes rows that don't match the filters off the map,
  leaving the matches and the folders that lead to them. A badge and each
  column's counts say how much is hidden.
- **Personal Notes on preview cards.** Select text in a preview card to add a
  note. The card's **Notes** button sets its notes beside it as sticky notes,
  each tied by a connector to its line.
- **More of it works from the keyboard.** Shift+Enter opens a preview card,
  Alt+arrows move cards and notes, and Alt+Shift+arrows resize them. The **?**
  popover lists every gesture and key.
- Structure columns have their own violet tint, every workspace starts left to
  right, numbered files sort by number, and the Modified filter is now
  **Unsaved**.

[More about the Project Map](/docs/workspace#project-map).

## Terminal

- **A theme of its own.** Settings ▸ Terminal ▸ Theme picks the terminal's
  colours apart from the editor's: Solarized, Dracula, Nord, Gruvbox, One Half,
  Tango, Campbell or the Editora pair. **Editor theme** keeps the colours
  derived from your syntax theme.
- **Nerd Font icons without installing a Nerd Font.** Editora bundles Symbols
  Nerd Font Mono, so the icons eza, lsd and powerline prompts print show on
  any machine.
- **The kitty keyboard protocol**, for programs that ask for it (Neovim,
  Helix, fish, nushell): Escape on its own, Ctrl+I apart from Tab, and
  Shift+Enter as a key.
- **Programs can talk back.** A build's "done" notification marks its tab and
  reaches the desktop. A program can copy to your clipboard, as tmux does over
  SSH, but never read it. Tabs name the program running in them.
- **Better selection.** Alt+drag selects a block of columns, Copy on select is
  available, and on Linux a middle-click pastes the PRIMARY selection. A
  selection survives typing and resizing, and it highlights exactly what it
  copies.

[More about the terminal](/docs/terminal).

## XML and pom.xml

- **Generate Grammar…** makes an XSD, DTD or RELAX NG grammar from the current
  file and binds the file to it. **Bind to Grammar…** and lemminx's own quick
  fixes now work too.
- **Safer by default.** Opening a file no longer downloads every schema it
  names from any host. Downloaded schemas are kept under Editora's config
  folder, and Doctor shows the cache with **Clear**. A `pom.xml` in a folder
  you have not trusted is edited with the plain XML server, and Maven-aware
  editing starts when you trust the folder.
- **Bigger files and better tools.** Files up to 100,000 lines keep the
  language server. Structure outlines every element. Format Document works
  without the server and no longer wraps at a fixed 100 columns. Completion
  lists the schema's elements first.
- **New File** has XML Schema and XSLT entries, and DTD declarations are
  highlighted.

[More about XML](/docs/lsp#xml).

## HTML

- **Links work.** Go to Definition or Ctrl-click on an `href` or `src` opens
  the file it names, at the `#fragment`, with or without the HTML server.
- **Markup checks.** An element that is never closed, a stray end tag and a
  link to a missing file are marked as warnings.
- **Colour swatches** in front of CSS colours, and Related File goes between
  a page and the stylesheets and scripts it loads.
- **Live Preview serves the whole site**, so root-relative links and shared
  stylesheets load. Format Document leaves `<pre>` and `<textarea>` as written.

[More about HTML](/docs/html).

## Java

- **Library sources.** A Maven library class opens with its real source when
  a repository has it. A decompiled one says so in its tab, with **Download
  Sources** and **Choose Sources…** to attach a `-sources.jar` from disk. A
  setting downloads every dependency's sources at import.
- Go to Super Method reads generics and anonymous classes right, and paste
  with imports is more reliable.

[More about Java library sources](/docs/lsp#the-java-server-and-your-project).

## Shell scripts

- Setting up the Bash language server is one step, and Doctor reports its
  state accurately. ShellCheck and shfmt can each be switched off, and
  libraries changed on disk are read again.

[More about shell scripts](/docs/lsp#shell-scripts).

## Smaller things

- The Simple-mode toggle sits at the right end of the toolbar, beside
  Settings, and never moves into the overflow menu.
- In the VS Code keymap, Shift+Alt+F (Shift+Option+F on macOS) formats the
  document.
- Jumps leave two lines of context under the sticky-scroll rows, and each
  sticky row is exactly one line tall.

The full list is in the
[changelog](https://github.com/adriandeleon/Editora/blob/master/CHANGELOG.md)
and on [What's new](/whats-new).
