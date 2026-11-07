---
title: "Editora 0.27.0: signed downloads and a Project Map for exploring code"
description: "A signed and notarized macOS app, signed Linux downloads, Project Map working sets and trails, ESLint and Prettier, Python quick fixes, Java toString() values."
date: 2026-11-05
version: "0.27.0"
---

**Editora 0.27.0** is out. It is the first release with signed downloads.
The Project Map gets a round of tools for finding your way around code you
don't know yet. JavaScript and TypeScript, Python and Java debugging each had
their own review, and every finding was fixed. Download it from the
[0.27.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.27.0).

## Signed downloads

- **The macOS app is signed and notarized by Apple**, for Apple silicon and
  Intel. It opens on first launch with no Gatekeeper warning and no trip to
  System Settings → Privacy & Security.
- **The Linux downloads are signed.** Each `.deb`, `.rpm`, `.AppImage` and
  `.tar.gz` has an OpenPGP signature beside it, made with the release key
  attached to the release. The `.rpm` and `.deb` are signed inside too, for
  `rpm -K`, dnf and `debsig-verify`.
- **Every release file has a build provenance attestation**, so
  `gh attestation verify` can confirm a download was built by the release
  workflow from the tagged commit.

The Windows installer is still unsigned. [Checking a
download](/docs/getting-started#checking-a-download) has the commands.

## Project Map

- **Working sets** keep the Map's view under a name: open folders, the
  selected row, zoom and pan, and the open cards. Pick one to go straight back
  to it.
- **Trails** are guided tours of a project, kept in `.editora/map-trails/` so
  the team can commit them. Add stops with notes from any card, then **Play**
  steps through them, opening each card at its lines.
- **Show recent changes** tints files and folders by how many commits touched
  them in the last 90 days. Tooltips and the Project overview also name owners
  from CODEOWNERS or OWNERS files.
- **Show References as Cards** and **Show Callers as Cards** open the places
  that use a declaration as cards around it, joined by labelled edges. The
  language server answers for files with no editor tab open.
- **A Structure column on each preview card.** Click a method or type to open
  its own card beside the column, and that card's **Open** opens the file
  narrowed to the declaration. Cards open the way the map flows.
- **More from a card:** find inside it, pin it so it never closes to make
  room, step a declaration card through its file's declarations, and see the
  last change to its lines. ⋯ → **Export** saves the map as a PNG or an SVG.
- **Finding things in a big project** got a review of its own: search ranks
  the name in the case you typed first, a long local folder is listed whole,
  and clearing the search keeps your place.

[More about the Project Map](/docs/workspace#project-map).

## JavaScript and TypeScript

- **ESLint, from the project.** A project's own ESLint checks files as you
  work and on save. Its problems join the TypeScript server's, its fixes are in
  Code Actions, and **ESLint: Fix All Problems** applies every automatic fix.
- **Prettier or Biome formats**, when the project uses one, with Format
  Document or a selection.
- **Actions on save:** organize imports, ESLint fixes and format can run after
  an explicit save, as one undo step. All of this runs only in trusted
  folders.
- A review of the whole JavaScript, TypeScript, Node.js and npm support fixed
  every finding. A remote host, WSL or a container now honours the project's
  `.nvmrc`, and a long word in a file no longer slows its colouring.

[More about TypeScript and JavaScript](/docs/lsp#typescript-and-javascript).

## Python

- **Quick fixes:** the lightbulb on an error offers Import X, Remove unused
  import and Ignore on this line.
- **Format on save** with Ruff or Black (Settings → LSP → Python, off by
  default), next to the interpreter setting and its picker.
- **Sub-projects get their own Pyright.** A folder with its own
  `pyproject.toml` is analysed with its own `.venv` and settings, and a uv
  workspace member shares its workspace's.
- Starlark, Cython and SCons files open with Python highlighting, and
  completion shows the server's whole list.

[More about Python](/docs/lsp#python).

## Java debugging

- **toString() values.** Variables, watches, inline values and hover show an
  object's `toString()` text. Each stop has a 1.5-second budget; past it, a row
  loads on a click. Settings → Debugging → Java can switch it off.
- **Plain folders debug again.** When the Java language server can't build a
  file without Maven or Gradle, Editora compiles it with `javac`, and only
  errors in code the program uses stop the launch.
- **One start check.** F5, Debug ▶, Debug File and Debug Test all ask before
  replacing a session that is paused, running or still building.
- **A tidier Run menu.** Stepping, watches and breakpoint items moved into
  **Run ▸ Debugger**, and Mute Breakpoints mutes exception stops too.

[More about debugging](/docs/run-debug#debugging).

## Also in this release

- **XML and HTML reviews:** a fourth XML and pom.xml review and two more HTML
  reviews, all fixed.
- **Find File opens a folder as a project:** C-d in the Find File popup
  (C-x C-f in Emacs).
- **Every language server says when it is ready**, even one that leaves some
  work unfinished.
- **Sticky-scroll headers keep their height** while the top line is partly
  scrolled off.
- **"Debug Log" is now Application Log**, since it is Editora's own log.

## Security

- **HTML Live Preview never serves an encrypted file's text.** After Save As,
  Encrypt File or a rename, the preview kept serving the editor's text at the
  old address until it was turned off; after Encrypt File, that was the
  plaintext, including edits never saved. The old address now answers from
  disk, encrypted pages get no preview, and `.gpg`, `.asc` and `.pgp` files are
  never served. Each previewed site also has its own address, so a page
  previewed later can't read another site's files or unsaved edits.

The full list is in [What's New](/whats-new/0.27/#0270---2026-11-05) and the
[changelog](https://github.com/adriandeleon/Editora/blob/v0.27.0/CHANGELOG.md).
