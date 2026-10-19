---
title: Terminal
description: Shells in a Terminal tool window, on this machine, a remote host, a container or WSL, with shell integration, find in the scrollback, clickable links, type-ahead over slow connections, and Run and Debug in a terminal tab.
category: Workspace
order: 8
---

The **Terminal** tool window runs shells inside Editora, one per tab, at the
bottom of the window: on your computer, or where a project's commands run,
on a [remote host](#terminals-on-a-host-in-a-container-or-in-wsl), in a dev
container or in a WSL distribution. A focused terminal owns the keyboard, so
Ctrl+C, Escape, and your shell's own shortcuts work as they do in any terminal.
Its colors follow the editor theme and its font is the editor's, and a
Ctrl+click (⌘-click on macOS) on a file name in the output opens that file in
the editor.

## Open a terminal

**Tool Window: Terminal** (`tool.terminal`) shows the tool window and starts a
shell if none is running. Run it again to hide the window; the shells keep
running, and the next toggle brings them back.

| Action | Command | Emacs | IntelliJ IDEA | VS Code, CUA, Sublime Text |
| --- | --- | --- | --- | --- |
| Show or hide the Terminal | `tool.terminal` | `M-0` | Alt+F12 (⌥F12 on macOS) | Ctrl+\` |
| Open another terminal | `terminal.new` | (palette) | (palette) | Ctrl+Shift+\` |
| Close the selected terminal | `terminal.close` | (palette) | (palette) | (palette) |
| Find in the selected terminal | `terminal.find` | (palette) | (palette) | (palette) |

Ctrl+\` is the Control key on macOS too, as in VS Code. The Emacs keymap binds
only the first, to `M-0`: run the others from `M-x`, which also works from
inside a terminal (see [Keys](#keys)). The **+** at the right end of the tab strip opens another
terminal as well.

Each tab carries a chip saying where its shell runs (**Local**, or the host's,
container's or distribution's name), and its title follows the title the shell
sets, or else its current folder. A tab closes when you click its **✕**, run
**Terminal: Close Terminal**, or the shell exits (`exit`, or Ctrl+D at an empty
prompt). Closing the window ends its shells.

## Open Terminal Here

**Open Terminal Here** opens a new terminal tab in a particular folder. It is in
a tab's right-click menu, the Project tree's right-click menu, and the
breadcrumb's menu, and it is the **File: Open Terminal Here**
(`file.openTerminal`) command, which uses the active file's folder. On a folder
in the Project tree it uses that folder; on a file, the file's folder. If the
Terminal tool window had no shell yet, it opens with just that tab, not with a
second shell in the project folder beside it.

To use your system's terminal app instead, as earlier versions did, turn on
**Settings → Terminal → Open Terminal Here uses the system terminal**. Open Terminal Here then starts your system's
terminal application, and the Terminal tool window is left alone.

On a file or folder of a [remote project](/docs/remote), Open Terminal Here
opens a terminal on that host, in that folder, whatever the system-terminal
setting says. In [Simple UI mode](/docs/workspace#focus-modes) the trimmed
right-click menus leave it out; the command still works.

## Where a shell starts

A new shell started from the Terminal tool window, rather than by Open
Terminal Here, starts in the first of these that applies:

1. The project's folder: on this computer, or on its host for a
   [remote project](/docs/remote).
2. The folder of the active file.
3. Your home folder.

On this computer the shell is your login shell unless **Settings → Terminal →
Shell** names another. It runs with the same `PATH` additions as programs
Editora runs, and with `TERM`, `COLORTERM`, and `TERM_PROGRAM=Editora` set.

## Terminals on a host, in a container or in WSL

A folder whose commands run somewhere else gets its terminal there too: a
remote project's terminal is a shell on its SSH host, and a folder whose tools
run in a [dev container](/docs/remote#tools-in-a-container) or a
[WSL distribution](/docs/remote#tools-in-wsl-windows) gets a shell inside it.
The tab's chip names the host, container or distribution, so you can tell at a
glance where a command will run.

- **It is a real terminal.** vim, htop, less and resizing work, as in `ssh`.
- **It is the account's login shell there**: `$SHELL`, else bash, else sh.
  Settings → Terminal → Shell names a program on this computer, so it is never
  sent to a host.
- **Allowed places only.** A terminal runs commands there, so it opens only
  where Editora may run commands: a host you
  [allowed](/docs/remote#allowing-a-host-to-run-commands), or an allowed
  [container](/docs/remote#allowing-a-container) or
  [distribution](/docs/remote#allowing-a-distribution). It's the same permission
  Run, Git and language servers use.
- **It opens without freezing the window.** The tab says *Connecting to …*
  until the shell is there.
- **Each terminal on an SSH host uses one of the connection's channels for
  long-running programs**, as a Run or a language server does. When all are in
  use, opening another says so; close a terminal or stop a program first.

**When the connection drops**, the host ends the shell and the programs in its
foreground, as it does when an `ssh` session drops. Programs started with
`nohup` or inside tmux or screen survive. The tab keeps what it printed under a
**Connection lost** banner with **Reconnect**, which starts a new shell in the
same folder once the connection is back.

In a container or WSL there is no connection to lose: when the container stops,
the shell ends and its tab closes. Closing a terminal ends the shell inside the
container too.

## Shell integration

With bash or zsh, the terminal knows where the shell is and where each command
starts and ends. Editora starts the shell with a small script of its own that
first runs your usual startup files (`~/.bash_profile`, `~/.zshrc` and the
rest), so your prompt, aliases and `PATH` are as always, and none of your
files are changed. It works the same on a host, in a container and in WSL,
where the script is kept in `~/.cache/editora`.

What it gives you:

- **The tab follows the shell's folder**, on any machine, and relative paths
  in the output open the right file (see
  [Open files and links](#open-files-and-links-from-the-output)).
- **New Terminal Here** in the terminal's right-click menu opens another tab in
  the shell's current folder.
- **Jump between prompts** with **Ctrl+Shift+Up** and **Ctrl+Shift+Down**
  (**⌘↑** and **⌘↓** on macOS), to scroll back to where each command started.
- **Copy Last Command Output** in the right-click menu copies what the last
  command printed.

It is on by default; turn it off with **Settings → Terminal → Shell
integration**. Other shells, such as fish, start without it.

## Find in the terminal

**Ctrl+Shift+F** (**⌘F** on macOS), **Terminal: Find** (`terminal.find`), or
**Find…** in the terminal's right-click menu opens a find bar over the
terminal. Every match in the scrollback and on the screen is highlighted, and
the current one starts at the most recent match:

- **Enter** goes to the next match up, toward older output, and **Shift+Enter**
  down. Both wrap around.
- **Aa** matches case.
- **Escape** closes the bar and gives the keyboard back to the shell.

Matches stay on their text as more output scrolls in. A selected word, when
there is one, is what the bar starts with.

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

The terminal's own shortcuts never reach Editora: Copy and Paste (below),
Find (**Ctrl+Shift+F**, **⌘F** on macOS), and the prompt jumps (**Ctrl+Shift+Up**
and **Ctrl+Shift+Down**, **⌘↑** and **⌘↓** on macOS).

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
and, on a link, Open Link and Copy Link Address (see
[Open files and links](#open-files-and-links-from-the-output)).

## Open files and links from the output

Hold **Ctrl** (**⌘** on macOS) and point at a file name, a path, or a URL in the
terminal's output: it is underlined when it can be opened. Click to open it:

- **A file** opens in the editor. A position after it, as compilers, test
  runners and stack traces print it (`src/Main.java:42` or
  `src/Main.java:42:7`), puts the caret on that line and column.
- **A folder** is revealed in the Project tree.
- **A URL** (`http`, `https`, `ftp`, `ftps`, or `mailto`) opens in your browser
  or mail app. A `file://` URL is treated as the path it names.

A relative path is read against the shell's current folder, so a bare name from
`ls` or a path from `grep -n` works as printed. On a host, it is a path on the
host, and the file opens from there. Only a path that exists is
underlined, so ordinary words never turn into links. On a link, the terminal's
right-click menu has **Open Link**, which does the same as the click, and
**Copy Link Address**.

**Hyperlinks programs print** work the same way. Tools such as
`ls --hyperlink`, gcc, and git can mark text as a link to an address of their
choosing; Ctrl+click opens that address. When the text isn't the address
itself, pointing at it with Ctrl held shows the real address first. Only `http`,
`https`, `ftp`, `mailto` and `file` links open.

A plain click never opens a link: it starts a selection, or goes to a program
such as vim or tmux that reads the mouse.

## Typing over a slow connection

On a slow link to a host, what you type appears at once, dimmed, before the
shell echoes it back, and is replaced by the real echo when it arrives. This is
**type-ahead**. It starts only once echoes take longer than 100 ms, so a shell on
this computer is never affected, and it stays out of full-screen programs such
as vim. Turn it off with **Settings → Terminal → Type-ahead**.

## Run and debug programs in a terminal

Programs that draw on the screen, read single keys or ask for a password need
a real terminal, which the Run and Debug consoles are not. For those:

- A [run configuration](/docs/run-debug#run-configurations)'s **Console:
  Run in a Terminal tab** runs its program in the Terminal tool window.
- **Settings → Debugging → Java → Run the program in a Terminal tab instead**
  does the same for the program you debug (see
  [Program input](/docs/run-debug#program-input-in-the-debug-console)).

The program runs where its configuration or file runs: on this computer, on
the project's host, or in its container or WSL distribution. Its tab is named
after the configuration or file. When the program ends, the tab stays, with
what it printed and its exit code in the corner. Closing the tab while the
program is still running asks first.

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
ends the program with the shell. On a host, a container or WSL, Editora asks
that machine which programs the shell is running. A tab running a program from
Run or Debug asks the same way while the program runs. Turn the question off with
**Settings → Terminal → Ask before closing a terminal that is running a
program**.

## Settings

**Settings → Terminal** (under Languages & Tools):

| Setting | Default | What it does |
| --- | --- | --- |
| Shell | (blank) | The shell to start on this computer. Blank uses your login shell. |
| Shell integration | On | Start bash and zsh with Editora's [shell integration](#shell-integration). |
| Type-ahead | On | Show typing before a slow shell echoes it; see [Typing over a slow connection](#typing-over-a-slow-connection). |
| Open Terminal Here uses the system terminal | Off | Off: [Open Terminal Here](#open-terminal-here) opens a tab in the Terminal tool window. On: it starts your system's terminal application. |
| Text size | 0 | The terminal's text size, 6 to 72. 0 uses the editor's size. |
| Cursor | Block | Block, Underline, or Bar. |
| Flash on the bell character | On | Flash the terminal when a program rings the bell. |
| Scrollback lines | 5,000 | Lines of history kept above the screen, up to 100,000. |
| Commands that reach Editora | `tool.*`, `palette.show`, `search.everywhere`, `terminal.*` | See [Keys](#keys). |
| Ask before closing a terminal that is running a program | On | See [Closing a busy terminal](#closing-a-busy-terminal). |

Text size, cursor, bell, type-ahead, and the command list apply to open
terminals. Shell, shell integration, and scrollback apply from the next terminal
you open.

## Known limitations

- **Prompts built for dark terminals are hard to read on light themes.** Some
  prompt themes, such as Oh My Bash's *agnoster*, draw text in the default
  color on a black segment, which is dark on dark under a light editor theme.
  No terminal palette fixes that; use a prompt theme that suits light
  backgrounds, or a dark editor theme.
- Without [shell integration](#shell-integration) (another shell, or the
  setting off), Editora reads a shell's current folder on Linux and macOS only.
  On Windows, tab titles then don't follow the folder, and only absolute paths
  and URLs in the output become links, not relative paths. A terminal on a host
  needs shell integration for the folder at all.
- macOS's own `/bin/bash` is version 3.2, which can't mark where a command's
  output starts: Copy Last Command Output needs zsh or bash 4.4 or later.
- Find matches within one line of the screen. Text that wrapped onto the next
  line is two lines to it.
