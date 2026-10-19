---
title: "Remote files (SFTP)"
group: "Workspace & files"
order: 8
beta: true
summary: "Edit files on a remote host over SSH/SFTP, each remote folder a project in its own window, and run Git, builds, tests, language servers and debuggers on the host, in a container, or in a WSL distribution."
---

Edit files on a remote host over **SSH/SFTP**. *Remote: Connect to SFTP…* opens the remote folder as a project in its own window, listed with your other projects; reopening it asks to connect, and the tabs you had open come back when it does. From there editing, syntax highlighting, search, bookmarks, notes, and preview all work over the wire, and Save writes straight back.

Saved sites have three surfaces beyond the palette: a **Remote Sites** tool window (`M-g r`) with New / Connect / Remove, a **Settings → Remote** page to manage them, and a quick-connect list on the Welcome page. Picking a site opens the form pre-filled. On the Settings page each field saves as you leave it and a value that cannot work (a port above 65535, a host with spaces) is refused with the reason; removing a site asks first and can also forget the host's permission to run commands.

Logins work the way `ssh` does: **Automatic (like ssh)** uses your SSH agent's keys (`ssh-agent`, 1Password, Secretive, gpg-agent or Windows' OpenSSH agent, so keys on a security key or a smartcard work), then the keys `~/.ssh/config` names, then your default `~/.ssh` keys; or pick one key file, or a password. A password, passphrase or 2FA code is asked for while the login runs. The connect form offers the hosts in `~/.ssh/config` and follows its HostName, User, Port, IdentityFile, IdentitiesOnly, ProxyJump and Include lines. Connecting a remote folder from another window logs in once, and a failed connect says why. Connections are remembered without secrets. Nothing connects until you add a site. See [Authentication](/docs/remote#authentication).

On a host you allow to run commands (Editora asks once per host; withdraw it in Settings → Remote), the tools Editora drives run on the host over the same SSH connection: Git and `gh`, ripgrep for Find in Files, Run, builds and tests, language servers, debuggers for Python, JavaScript and Java, and a watch for outside changes. Each host can name its own tool commands. It works with OpenSSH (Linux, macOS) and dropbear servers, and with ed25519 keys. See [Remote files](/docs/remote).

A project on this machine can have its tools run elsewhere while you edit its files here: in the Docker or Podman container it is bind-mounted into (its dev container, found automatically, or one you pick with *Containers: Run This Project's Tools in a Container…*), or, on Windows, inside the WSL distribution it lives in (`\\wsl.localhost\…`). Git and `gh`, Run, builds and tests, language servers and debuggers then run there, with its own PATH, environment and user. Editora asks once per container or distribution, and *Containers: Run This Project's Tools Here* goes back. See [Tools in a container](/docs/remote#tools-in-a-container).
