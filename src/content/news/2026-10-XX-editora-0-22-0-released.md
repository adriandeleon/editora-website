---
title: "Editora 0.22.0: remote development on the host, Markdown wiki links, and a round of editing commands"
description: "Editora 0.22.0 runs Git, builds, language servers and debuggers on the host of a remote folder, lets you choose a file's encoding, adds line commands, text tools and more multiple-cursor commands, rulers per language and indent guides, Save All and Reopen Closed Tab, and Emacs registers. Markdown gains wiki links, backlinks and 29 lint rules, and the HTTP Client asks before sending your secrets. Shell scripts run as their shebang says, with Run with Trace and one-click ShellCheck and shfmt."
date: 2026-10-31
version: "0.22.0"
---

<!-- DRAFT (website-next): set the date, file name and version on release day, and check the sections
against CHANGELOG.md's 0.22.0 section (written from master on 2026-10-15). -->

**Editora 0.22.0** is out. Remote folders now work like local ones: the tools
Editora drives run on the host. Markdown gets wiki links, backlinks and a
much larger lint, the HTTP Client asks before it sends a secret, shell scripts
get a full pass from highlighting to Run, and there is a round of everyday
editing commands. Download it from the
[0.22.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.22.0).

## Remote development

Until now a remote (SFTP) folder was a place to open and save files. The
first time you open a folder on a host, Editora asks once whether it may run
commands there. Once you allow it, the tools Editora drives run on that host,
over the same SSH connection:

- **Git and GitHub**: branch, change bars, log and blame, staging hunks,
  commits, stashes, fetch, pull and push, using the host's own `git`,
  credentials and `gh`. Clone and Init work on the host too.
- **Language servers**: pyright, gopls, rust-analyzer, the TypeScript server,
  jdtls and the rest run on the host, with completion, diagnostics, go to
  definition and renames that create and move files there.
- **Run, builds and tests**: Run, script, make and npm configurations, and
  Maven, Gradle, npm, Cargo and Go builds and tests. Output, input and Stop work
  as they do locally.
- **Debugging**: debugpy, vscode-js-debug and java-debug, with breakpoints,
  stepping and the call stack.
- **Search**: Find in Files uses the host's ripgrep, and Go to File lists the
  project's files there.
- **Outside changes**: a `git pull` or a build on the host updates the Project
  tree, Git, the language servers and open files.

Each remote folder is a project with its own window, its own tabs and its own
trust. Reopening it asks to connect, and your tabs come back once it does.
Settings → Remote lists which hosts may run commands, and lets you set the
command for each tool on a host whose PATH does not find the right one. It
works with OpenSSH servers on Linux and macOS, and with dropbear.
[More about remote folders](/docs/remote).

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

Three passes over Markdown since 0.21.0:

- **Wiki links**: `[[note]]`, `[[note|label]]`, `[[note#heading]]` and
  `![[image.png]]` resolve, complete after `[[`, are followed from the source
  and the preview, and are checked by lint. A **Backlinks** window lists what
  links to the current note.
- **Links follow a renamed or moved file**, kept up to date through an index of
  the project's links, including files open in other windows.
- **More syntax**, in the preview and in every export: GitHub alerts, emoji
  shortcodes, highlight, superscript and subscript, definition lists and math.
- **The preview** can be selected and copied, opened to the side or in its own
  tab, searched with Find, and its task boxes ticked with a click.
- **Lint** has 29 more rules, including checks for broken relative links and
  heading anchors. You can act on an issue from the editor, fix issues on save
  and lint the whole project.
- **Editing**: Format Document, numbered headings, table and link tools, Paste
  as Markdown, completion of paths, anchors and emoji, and a search over every
  heading in the project.
- **Exports**: self-contained HTML, Word documents with real heading styles and
  lists, and emoji in PDFs.

[More about Markdown](/docs/markdown).

## HTTP Client

- **Your secrets stay put unless you say so.** A request that reads
  `$processEnv`, `$env` or `$dotenv` in a folder you have not trusted asks
  first. The prompt names the variables and the hosts, never the values.
- **Copy as cURL is safe to paste** into a shell, and reproduces uploads, forms
  and Digest logins.
- **The response view shows what happened**: status, time and size on one line,
  the request as it was sent, and the warnings.
- **History and environments are remembered per file**, with credential
  headers masked.
- **Closer to the JetBrains format**: comment lines in a body, directives,
  timeouts with units, and HTTP/1.1 by default.
- **Keys** to run a request (`C-c C-c`, or `Ctrl+Alt+R` in the GUI keymaps),
  run the file and cancel, and unresolved variables are underlined as you type.

[More about the HTTP Client](/docs/http-client).

## Shell scripts

A full pass over shell support, from highlighting to Run:

- **Scripts run as their shebang says.** A shell script runs with the default
  settings, with no language server needed, using the interpreter and flags its
  `#!` line names (`#!/bin/sh`, `#!/usr/bin/env -S bash -e`), or `bash` when
  there is none. A run configuration can set the interpreter instead.
- **The Run console** shows a script's `read -p` prompts, has **End Input** for
  scripts that read until the end of input, draws colours, keeps a progress bar
  on one line, and links bash errors to their line.
- **Run with Trace** runs a bash script with `-x`, and each traced line links to
  the line that ran. Startup files such as `.bashrc` have no Run ▶.
- **ShellCheck and shfmt in one click.** Settings and Doctor show whether they
  are installed, and Editora can download the official releases, checked
  against hashes built into it. The Bash card in Settings sets ShellCheck's
  arguments and shfmt's style, and Problems shows each ShellCheck rule with a
  link to its page.
- **Highlighting** stays right after `$((x << 2))` and `<<\EOF` here-docs;
  `.envrc`, `/etc/zshrc` and `.ksh` files open as shell, and vim and emacs
  modelines set the language.
- **Editing**: `case` statements and here-docs indent correctly, scripts fold,
  Toggle Comment leaves the `#!` line alone, spell check skips command
  arguments, and a script with Windows line endings gets a notice that converts
  it.

[More about running scripts](/docs/run-debug) and
[ShellCheck and shfmt](/docs/lsp#shellcheck-and-shfmt).

## Also

- The Run, Output, Test Results, Debug and External Tools windows show a
  **spinner** while something runs, so a quiet build no longer looks stalled.
- **Word wrap is set per kind of file**: on for Markdown and plain text, off
  for code.
- **Abbreviations show what they expand to** as muted text after the caret, and
  Tab expands them.
- `LSP: Clear Java Index Caches` frees the disk space jdtls's per-folder indexes
  take.
- The Recent Files dropdown fits long names and asks before removing one.
- The installed Linux app is 22 MB smaller.
- Fixes: each project window acts on its own project, right-click menus close
  when you click outside them, the window drags by its title, and Java
  completions show the whole signature.

The full list is in the
[changelog](https://github.com/adriandeleon/Editora/blob/v0.22.0/CHANGELOG.md).
