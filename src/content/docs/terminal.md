---
title: Terminal
description: Shells on this machine in a Terminal tool window, in tabs, with the editor's theme colors and font, and a keyboard that belongs to the shell.
category: Workspace
order: 8
---

The **Terminal** tool window runs shells on your computer inside Editora, one
per tab, at the bottom of the window. A focused terminal owns the keyboard, so
Ctrl+C, Escape, and your shell's own shortcuts work as they do in any terminal.
Its colors follow the editor theme and its font is the editor's.

## Open a terminal

**Tool Window: Terminal** (`tool.terminal`) shows the tool window and starts a
shell if none is running. Run it again to hide the window; the shells keep
running, and the next toggle brings them back.

| Action | Command | Emacs | IntelliJ IDEA | VS Code, CUA, Sublime Text |
| --- | --- | --- | --- | --- |
| Show or hide the Terminal | `tool.terminal` | (palette) | Alt+F12 (⌥F12 on macOS) | Ctrl+\` |
| Open another terminal | `terminal.new` | (palette) | (palette) | Ctrl+Shift+\` |
| Close the selected terminal | `terminal.close` | (palette) | (palette) | (palette) |

Ctrl+\` is the Control key on macOS too, as in VS Code. The Emacs keymap binds
none of the three: run them from `M-x`, which also works from inside a terminal
(see [Keys](#keys)). The **+** at the right end of the tab strip opens another
terminal as well.

Each tab carries a **Local** chip, and its title follows the title the shell
sets, or else its current folder. A tab closes when you click its **✕**, run
**Terminal: Close Terminal**, or the shell exits (`exit`, or Ctrl+D at an empty
prompt). Closing the window ends its shells.

*File: Open Terminal Here* (`file.openTerminal`) still opens your system's
terminal app.

## Where a shell starts

A new shell starts in the first of these that applies:

1. The project's folder, when the project is on this computer.
2. The folder of the active file.
3. Your home folder.

A [remote project](/docs/remote) gets a shell on your own computer for now, not
on the remote host.

The shell is your login shell unless **Settings → Terminal → Shell** names
another. It runs with the same `PATH` additions as programs Editora runs, and
with `TERM`, `COLORTERM`, and `TERM_PROGRAM=Editora` set.

## Keys

While a terminal has focus, keys go to the shell, not to Editora:

- **Ctrl+C** interrupts the running program, **Ctrl+W** deletes a word, and
  **Ctrl+R** searches the shell's history.
- **Escape** reaches vim, less, and other full-screen programs.
- Alt works as the shell's Meta key, and key sequences belong to the shell: in
  the Emacs keymap, `C-x` inside the terminal is Emacs's or readline's, not
  Editora's.

Only a few commands still reach Editora from a terminal. By default they are:

- the tool-window toggles (`tool.*`), so you can open the Project tree or hide
  the Terminal without leaving it;
- the command palette (`palette.show`);
- Search Everywhere (`search.everywhere`);
- the terminal commands (`terminal.*`).

A command gets through only when its shortcut is a single chord. In the Emacs
keymap that means `M-x`, `M-S-x`, and the `M-1` … `M-9` tool windows work from a
terminal, while the `M-g …` tool-window sequences go to the shell. On macOS,
every ⌘ shortcut reaches Editora too, since a shell has no use for ⌘.

Change the list in **Settings → Terminal → Commands that reach Editora**: one
command id per line, where an entry ending in `.*` covers every command that
starts with it. Every other key stays with the shell.

### Copy and paste

The terminal's own Copy and Paste are **Ctrl+Shift+C** and **Ctrl+Shift+V**
(**⌘C** and **⌘V** on macOS). Plain Ctrl+C and Ctrl+V belong to the shell.
The terminal's right-click menu has Copy, Paste, Select All, Clear Scrollback,
and, on a link, Open Link and Copy Link Address.

## Colors and font

The terminal takes its colors from the active
[editor theme](/docs/themes-fonts): the theme's keyword color becomes the
terminal's red, its number color the blue, and so on, with the editor's
background and text colors behind and in front. Switching themes recolors
open terminals at once.

The font is always the editor's. **Settings → Terminal → Text size** sets the
terminal's size on its own; 0 keeps the editor's size.

## Closing a busy terminal

Closing a terminal whose shell is running a program, such as a build or a
server, asks first and names the program. **Cancel** is the default; **Close**
ends the program with the shell. Turn the question off with **Settings →
Terminal → Ask before closing a terminal that is running a program**.

## Settings

**Settings → Terminal** (under Languages & Tools):

| Setting | Default | What it does |
| --- | --- | --- |
| Shell | (blank) | The shell to start. Blank uses your login shell. |
| Text size | 0 | The terminal's text size, 6 to 72. 0 uses the editor's size. |
| Cursor | Block | Block, Underline, or Bar. |
| Flash on the bell character | On | Flash the terminal when a program rings the bell. |
| Scrollback lines | 5,000 | Lines of history kept above the screen, up to 100,000. |
| Commands that reach Editora | `tool.*`, `palette.show`, `search.everywhere`, `terminal.*` | See [Keys](#keys). |
| Ask before closing a terminal that is running a program | On | See [Closing a busy terminal](#closing-a-busy-terminal). |

Text size, cursor, bell, and the command list apply to open terminals. Shell
and scrollback apply from the next terminal you open.

## Known limitations

- **Prompts built for dark terminals are hard to read on light themes.** Some
  prompt themes, such as Oh My Bash's *agnoster*, draw text in the default
  color on a black segment, which is dark on dark under a light editor theme.
  No terminal palette fixes that; use a prompt theme that suits light
  backgrounds, or a dark editor theme.
- Terminals run on this computer only. A remote project's terminal is a local
  shell.
