---
title: Remote files (SFTP)
description: Browse, edit, search, and save files on a remote host over SSH/SFTP, and run Git, builds, language servers, and debuggers on that host.
category: Workspace
order: 3
beta: true
---

Editora can edit files on a remote host over **SSH/SFTP**. A remote folder
opens as a project in its own window, and from there editing, syntax
highlighting, search, bookmarks, notes, and preview all work over the wire. Save
writes straight back, with no dialog when the server copy is unchanged. If it
changed since you opened or last saved the file, Editora asks before overwriting
it.

On a host you allow to run commands, the tools Editora drives run **on that
host**, over the same SSH connection: Git and `gh`, ripgrep, builds, tests and
Run, language servers, debuggers, and the watch for outside changes. See
[What works remotely, and what doesn't](#what-works-remotely-and-what-doesnt).

## Connecting

| Action | Command |
| --- | --- |
| Connect to a host | `remote.connect` |
| Manage saved connections | `remote.manageConnections` |
| Open a remote file by URI | `remote.openFile` |
| Disconnect | `remote.disconnect` |

`remote.connect` opens a form for host, port, user, and authentication. On
success the remote folder opens as a [project in its own
window](#remote-folders-as-projects), with the Project tool window showing its
tree. The window you connected from is left as it was. With projects turned off
(Settings → Workspace), the folder is mounted as the Project tree of the
current window instead. Connections are remembered in `connections.json`.

**Disconnect** closes every connection of the window, not only the mounted one,
and closes the SSH connection itself rather than only its file channel. When
remote tabs have unsaved changes it names them and asks first; their tabs stay
open but cannot be saved until you reconnect.

## Remote folders as projects

A remote folder is a project like a local one: it is listed with your other
projects in the project switcher (`project.switch`), and it has its own window,
its own saved tabs and layout, and its own [trust](/docs/workspace#trusted-folders).
A folder on a server and a local folder with the same path are trusted
separately.

Reopening a remote project, from the project switcher or when Editora restores
its windows at startup, opens its window and brings up the connect form filled
in for its host, since your password or passphrase is never stored. Until it
connects, the Project panel reads **Not connected to** the host, with a
**Reconnect** button. The tabs you had open come back when the connection does,
with their carets.

The same panel appears when the connection is lost and after **Disconnect**.
Editora asks the server whether it is still there every 30 seconds and gives up
after three unanswered asks, so a network that silently drops the connection is
noticed in about a minute and a half rather than whenever the system's TCP
timeout fires.

## Saved sites

Saved sites have three surfaces beyond the palette picker:

- A **Remote Sites** tool window (`tool.remote`, `M-g r`) listing every saved
  site, with New, Connect, and Remove.
- A **Settings → Remote** page (`remote.settings`) to add, edit, and remove
  sites: label, host, port, user, auth method, and key path, and, under **This
  Host**, whether the host may [run commands](#allowing-a-host-to-run-commands)
  and which tool commands to use there.
- A **Remote Sites** quick-connect list on the [Welcome page](/docs/workspace).

Picking a site opens the connection form pre-filled; your password or key
passphrase is still prompted each time and is never stored.

## Authentication

Each connection uses the one method you pick in the form: your default `~/.ssh`
keys, a specific private key file, or a password. Only that method is tried;
there is no fallback from one to the next. Secrets are never stored, only
the connection details (host, user, last path, and which method to use). The
password or passphrase you type is never written to disk, and a password is
removed from the SSH session as soon as authentication finishes.

Keys of every type `ssh-keygen` makes work, ed25519 (its default) included, and
so does a server that offers only an ed25519 host key.

## Host-key verification

Editora verifies the server's host key against `~/.ssh/known_hosts`, the same
file `ssh` uses, so a host you've already accepted at the terminal connects with
no prompt. A host you've never connected to shows its fingerprint and asks you
first. A host whose key has **changed** is refused outright, without any button to
wave it through, since a changed key is how an impersonation attempt looks.

## What works remotely, and what doesn't

The file features always work on a remote folder: editing, highlighting, find
and replace, Find in Files, bookmarks, notes, preview, and PDF or print. Recent
files keep remote files and reopen them once the connection is open.

Everything that runs a program needs the host's permission to run commands.
Once a host has it, those tools run **on the host**, as the account you signed
in with, each on its own channel of the same SSH connection. Nothing is
installed there.

### Allowing a host to run commands

The first time you open a folder on a host, Editora asks once: **Run commands
on** the host? The question names the host and the account, and says that the
Java language server keeps its index in `~/.cache/editora` there.

- **Allow** is remembered for that host (user, host and port), alongside the
  saved sites.
- **Not Now** is respected until you restart Editora; the file features keep
  working.

To withdraw a host's permission, open **Settings → Remote**
(`remote.settings`), pick the site, and untick **Allow running commands on this
host** under **This Host**. That is also where you can allow a host you said
Not Now to. Changes reach every open window at once: Git, language servers and
the outside-change watch start or stop accordingly. Removing a saved site keeps
its answer and tool commands, so adding it back neither asks again nor loses
them.

The switch for all hosts is **Run commands on the remote host** at the top of
Settings → Remote, also **Remote: Toggle Running Commands on the Host**
(`remote.toggleExecution`). It is on by default; turned off, no host runs
anything, whatever it was allowed. Turning it back on with a remote folder open
asks about that host if it has not been allowed yet.

What the permission covers is the tools Editora already drives, listed below.
Editora does not open a terminal or start an AI agent on the host, and has no
box for typing arbitrary commands to run there. It writes on the host only
where those tools write, plus `~/.cache/editora` (the Java language server's
index, the java-debug plugin, and classes compiled for debugging).

### What runs on the host

| Feature | On a host that may run commands |
| --- | --- |
| [Git](/docs/git) | Status, change bars, log, blame, staging (also single hunks), commit, branches, stashes, fetch / pull / push, clone and init, all by the host's `git` with the host's credentials. Automatic fetch works as for a local repository. |
| [GitHub](/docs/github) | The host's own `gh`, signed in there. Links open in your local browser. |
| [Find in Files](/docs/navigation#find-in-files) | The host's ripgrep searches the folder in one command, when *Use ripgrep when available* is on and the host has `rg`. Otherwise every file is read over SFTP. |
| [Search Everywhere](/docs/navigation#search-everywhere) files | The host lists the project's files, skipping ignored ones as for a local project. There are no symbols for a remote project. |
| [Run, builds and tests](/docs/run-debug#running-files) | Run on a file, a main class, script / make / npm configurations, and Maven, Gradle, npm, Cargo and Go builds and test runs, with output, input and Stop as locally. |
| [Language servers](/docs/lsp) | Each server runs on the host: completion, hover, diagnostics, go to definition, rename and the rest. |
| [Debugging](/docs/run-debug#debugging) | Python (debugpy), JavaScript (vscode-js-debug) and Java (java-debug in the host's `jdtls`). |
| Outside changes | The project folder is watched on the host, so a `git pull` or a build there updates the Project tree, Git, language servers and an open file that changed. A host with `inotifywait` reports at once; others are checked every few seconds. |

The tools are the ones the host's own `PATH` finds. When it finds the wrong one,
or none, set the command for that host in **Settings → Remote → This Host**:
**Git**, **GitHub CLI**, **ripgrep**, **JDK home**, **Node.js**, **Python for
debugging**, **js-debug server**, **java-debug plugin jar**, and **Language
servers**, one `server = command` line each (for example
`python = basedpyright-langserver --stdio`). Commands and paths are as the host
knows them; a change applies to the next command or server started there.
Settings that name programs on this machine, such as your local Git command or
Default JDK, are never sent to a host.

Commands run with the account's login environment: the `PATH` and locale of its
login shell, and every variable its login profile (`~/.bash_profile`,
`~/.zprofile` or `~/.profile`) exports, such as `JAVA_HOME`, `MAVEN_OPTS`,
`GOPATH` or a version manager's. Variables that belong to the login session
itself (the prompt, history, `SSH_*`, the display) are left out.

### Still local only

These work only on files on this machine, whatever a host allows:

- [Local History](/docs/workspace#local-file-history)
- the [HTTP client](/docs/http-client) and HTML live preview
- [external tools](/docs/external-tools)
- *Reveal in File Manager*, *Open Terminal Here*, and *Save as Administrator*
- following a growing [log file](/docs/log-viewer)
- `.editorconfig` files, a project's committed `.editora/settings.json`, and a
  Markdown lint configuration file
- Go to Symbol in Project, which would have to read every source file over the
  network
- the Astro language server, and debugging languages other than Python,
  JavaScript and Java

### Hosts it works with

Remote commands work with **OpenSSH** servers, on Linux and on macOS (where
Remote Login is one), and with **dropbear**. The account's login shell must be
a POSIX shell (`sh`, `bash`, `dash`, `zsh`); Windows hosts and fish or csh login
shells are not supported. Stop ends a program together with everything it
started (on dropbear, when the host has `setsid`), and a lost connection ends it
on the host too.

## Saving

- **Save** writes back to the server, asking first when the server copy changed
  since you opened or last saved it.
- A file with **no write bit** on the server opens in
  [View mode](/docs/editing#read-only-and-view-mode). It is never replaced
  silently: Save asks first, and auto-save does not write it.
- **Save As** on a remote tab writes a **local copy**, and the tab becomes that
  local file. This also works after the connection is gone, so unsaved remote
  work can always be kept.
- An `.editorconfig` is not read for remote files, so a remote file in an
  encoding other than UTF-8 needs its
  [encoding chosen](/docs/workspace#file-encodings) with *Buffer: Reopen with
  Encoding…* or *Save with Encoding…*. The choice is remembered for that file,
  as for a local one.

Personal notes on a remote file are stored under the path you opened it by, not
the target of a symlink. Notes made in an earlier version on a file opened
through a symlink may no longer be found.

## Behind the scenes

A remote path is a real `java.nio.file.Path` on an SFTP filesystem, so the same
open, save, list, and search code paths work unchanged. The transport is Apache
MINA SSHD. Saves use a server-side atomic rename when the host supports it. If a
safe replacement cannot be completed, Editora retains the previous remote copy
instead of risking a partial file.
