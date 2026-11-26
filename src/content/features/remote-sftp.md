---
title: "Remote files (SFTP)"
group: "Workspace & files"
order: 8
beta: true
summary: "Edit files on a remote host over SSH/SFTP, each remote folder a project in its own window, and run Git, builds, tests, language servers and debuggers on the host, in a container, or in a WSL distribution."
description: "Edit a remote folder over SSH and SFTP as a project, and run Git, builds, tests, language servers and debuggers on the host, in a container or in WSL."
alt: "The Connect to SFTP form with host, port, user, authentication, key file and path fields"
---

---
---
Open a folder on a server and work in it as if it were on your machine: Editora edits the files over SSH and runs your tools on the host, so nothing has to be copied back and forth.

## Connect

- **Remote: Connect to SFTP…** opens the remote folder as a [project](/features/projects/) in its own window. Reopening it asks to connect, and your tabs come back.
- **Saved sites** live in the **Remote Sites** tool window (`M-g r` in the Emacs keymap), in **Settings → Remote** and on the Welcome page.
- **Logs in like `ssh`**: your SSH agent's keys first, so security keys and smartcards work, then the keys `~/.ssh/config` names, then your default keys. A key file or a password works too.
- **Reads `~/.ssh/config`**: its hosts are offered in the form, and ProxyJump and Include are followed.
- **Asks while it logs in** for a password, passphrase or 2FA code, and never stores secrets.

## Work on the host

Editing, highlighting, search, bookmarks, notes and preview all work on remote files, and Save writes straight back. On a host you allow to run commands, which Editora asks once per host, the tools run there over the same connection:

- **Git and `gh`**, with the host's own repository.
- **Find in Files** with the host's ripgrep, in one command instead of reading every file.
- **Run, builds and tests**, with output and Stop as for a local build.
- **Language servers and debuggers** for Python, JavaScript and Java.
- **A [terminal](/features/terminal/)** on the host, in the project folder.
- **Outside changes**, such as a `git pull` on the host, update the tree, Git and open files.

## Tools in a container or WSL

A project on this machine can have its tools run elsewhere while you edit its files here:

- **A container**: the Docker or Podman container the folder is bind-mounted into, found from its dev container or picked with **Containers: Run This Project's Tools in a Container…**.
- **WSL**: on Windows, the distribution a `\\wsl.localhost\…` folder lives in.

Git, Run, builds, tests, language servers and debuggers then run there, with its own PATH, environment and user. Editora asks once per container or distribution, and **Containers: Run This Project's Tools Here** goes back.

See the [Remote files guide](/docs/remote/).
