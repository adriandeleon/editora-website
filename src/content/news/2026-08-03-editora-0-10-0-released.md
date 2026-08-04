---
title: "Editora 0.10.0: a look of its own, and two files on screen at once"
description: "Editora Light and Dark, a rebuilt Settings and a flatter interface; a menu bar over the command registry; independent editor groups that nest, drag and persist; run configurations for scripts and make targets, shareable with your team; per-project settings you can commit; bracket-pair colorization, the four missing fold commands, and Java paste auto-import."
date: 2026-08-03
version: "0.10.0"
---

**Editora 0.10.0** is out: 15 new features, 15 changes, a performance fix and 4
bug fixes. Grab it from the
[releases page](https://github.com/adriandeleon/Editora/releases/tag/v0.10.0).

This one is mostly about the two things you look at all day — the interface
itself, and how many files it can show you at once — plus run configurations
growing up into something you can share with a team.

## Editora has its own look

Until now Editora borrowed its appearance from whichever theme you picked.
**Editora Light** and **Editora Dark** are its own: a teal accent, an ink-navy
ground and a periwinkle taken from the app's own icon. They ship as a matched
pair and are what a fresh install starts in.

**Nothing changes for an existing install.** Your saved theme is kept, and the
new pair simply joins Primer, Nord, Dracula and the community themes in
Settings → Appearance. See [Themes & fonts](/docs/themes-fonts).

- **One vocabulary for state, everywhere.** Amber means "not saved yet" in every
  place it can appear — the tab, the Switcher, the file tree, the pickers — and
  it now follows the theme instead of being one fixed ochre that suited a light
  background and little else. Red is broken, green is verified, olive and violet
  are git's untracked and renamed, and periwinkle is only ever a keybinding.
- **The interface is set in Inter.** Editora already bundled it for the Markdown
  preview and PDF export; the whole interface now uses it, on every platform,
  instead of whatever default font each system supplied.
- **Settings has been rebuilt.** Every page reads the same way: one background,
  each group of settings on a card, and every setting stated as a name, a
  plain-English line saying what it does, and its control on the right — instead
  of a stack of bare checkboxes whose labels had to carry the whole explanation.
  A tool's detection result is a green or red pill on its own row, and a setting
  whose feature has a keybinding shows that chord beside its switch, so Settings
  teaches the keyboard instead of hiding it.
- **Less line, more space.** Editora used to draw a hairline at nearly every
  seam, and in several places two at once — a panel's frame plus the frame the
  tree inside it drew for itself. Those are gone; surfaces are told apart by
  shade and spacing. Menus, tab strips and toolbars are tighter, tabs are
  shorter, and the rounded corners are consistent from the command palette down
  to a spinner in Settings.
- **Menu keybindings line up.** Each menu's shortcuts share one column instead of
  trailing after their labels at whatever width each happened to be, so a menu
  can be read down its right edge. (On macOS the menu bar belongs to the system,
  which draws text only, so they stay beside the label.)
- **Language, Tab Size and Line Endings open a picker, not a dialog.** Clicking
  those status-bar segments used to open a separate window with a dropdown. They
  now use the same in-scene picker as everything else, which you can type into —
  the language list is around a hundred grammars, and it was not searchable
  before.

## A menu bar

File / Edit / Find / View / Navigate / Code / Run / VCS / Tools / Window / Help,
so the things Editora can do are *browsable* rather than only findable by name.

Every item is built over the same command registry the palette uses, so each one
shows its current keybinding and updates when you switch keymaps. A command whose
feature is switched off appears greyed rather than vanishing, which keeps the
menu a stable map instead of a shifting one. On macOS it sits in the system menu
bar.

The palette is still the complete index — the menu is a deliberately curated
subset. Hide it from Settings → Interface or with **View: Toggle Menu Bar**; it
is hidden automatically in Zen, Expert and Simple modes.

## Two files on screen at once

The editor area can now be split into independent **editor groups**, each with
its own tabs and its own selection, so you can put a header beside its
implementation or a test beside what it tests.

- **Split Editor Group Right** and **Split Editor Group Down** move the current
  file into a new group.
- **Move File to Next Editor Group** shifts it along, **Focus Next Editor Group**
  moves the keyboard between them, and **Merge Editor Groups** puts everything
  back.
- Splits **nest**, so a side-by-side pair can hold a stacked pair and you can
  build an L-shaped layout — while splitting the same direction twice widens the
  existing row instead, giving you three columns rather than a lopsided chain.
- **Drag a tab onto another group** to move it there, or onto a group's edge to
  split that group and drop the file on that side; a translucent highlight shows
  where it will land.
- **The layout is saved with the session**, so the arrangement you left comes
  back on the next launch.

Closing the last file in a group collapses it, so you never end up staring at an
empty pane. All five commands are in the palette and bindable.

This is distinct from the older **Split Editor** commands, which show two views
of the *same* file. Those work exactly as before, and the two can be combined.

## Run configurations grew up

- **They are no longer Java-only.** A saved configuration can launch a **Python
  script, a shell script or a make target** as well as a Java main class, chosen
  with a Type field in Settings → Run Configurations. Script configurations need
  no project and no language server at all.
- **A selector in the toolbar.** Pick a configuration and hit Run or Debug, with
  Stop beside them; the choice is remembered across restarts. Each configuration
  also becomes a real command, so it appears in the palette by name and can be
  given its own keyboard shortcut — the same way saved macros and external tools
  already work.
- **A before-launch step.** A configuration can name a command to run first (a
  build, a codegen step); a non-zero exit aborts the launch, so a stale binary is
  never run by accident.
- **Share them with your team.** **Export Configurations to Project** writes them
  to `.editora/run-configurations.json` inside the project, where they can be
  committed. **Import** merges them back by name, so importing twice doesn't
  duplicate and a colleague's edit updates rather than doubles.
- **Adding one starts from the file you are looking at.** **Add** used to create a
  blank entry called "New Configuration". It now prefills the main class from the
  active Java file (or the one your Gradle build declares), names the
  configuration after that class, and puts the cursor in the field that still
  needs you. Adding twice from the same file gets you "App" and "App (2)" rather
  than two entries with the same name, which previously collided into a single
  palette command.
- **Edit one from the dropdown.** It now ends with **Edit Configurations…**,
  which opens Settings on the Run Configurations page with the configuration you
  had selected already picked out. It is offered even with nothing saved, which
  is one way to create your first.
- **Running an incomplete configuration takes you to it.** It used to name the
  missing field and leave you to go and find it; pressing Run is a request to run
  it, so it now opens that configuration's form with the field you need to fill
  in.
- **The toolbar group only appears where it applies.** The selector and its Run /
  Debug / Stop buttons used to sit on every toolbar, so a project of Markdown
  notes carried a dropdown that could never fill. They now show where you could
  actually launch something, and anything you have already saved keeps the group
  visible regardless. The palette commands are unchanged and still work anywhere.

Also: a saved configuration no longer refuses to run because a README happens to
be the front tab — any open Java file in the project serves. Debugging remains
Java-only, and now says so instead of reporting a confusing Java error. See
[Run & debug](/docs/run-debug).

## Projects

- **Projects are on by default.** The feature shipped behind a checkbox most
  people never found, and much of what Editora has grown since is anchored to a
  project: the file tree, per-project bookmarks and notes, find-in-files scope,
  run configurations, per-project settings. Existing installs are switched on
  when their settings are upgraded — including installs that had turned it off,
  since a saved setting cannot say which of the two it was. Settings → Workspace
  turns it back off.
- **Per-project settings you can commit.** A project can carry
  `.editora/settings.toml` saying which language server to run and whether to run
  it, overriding your global preferences for anyone who opens that project. The
  case it's for: one repository needs a JDK 17 server and another a JDK 25 one,
  and you no longer have to remember to flip a global preference between them.
  **Project: Edit Project Settings…** creates the file with a commented example.
  Only toolchain settings can be overridden — appearance, keymap and fonts stay
  personal, because checking out a repository shouldn't rearrange somebody's
  editor.
- **New Project From Template.** Scaffold a whole project and open it in its own
  window, rather than pointing Editora at a folder you made yourself. Pick a
  multi-file template, fill in its variables, choose where it goes, and the new
  folder is registered as a project and opened. Ships with a **Python Project**
  template (package layout, a test, `pyproject.toml`, README and `.gitignore`);
  your own multi-file templates appear in the same picker.

## Editing and code intelligence

- **Bracket-pair colorization.** Each `()`, `[]` and `{}` is tinted by how deeply
  it is nested, so "how far in am I?" is readable without counting. It answers a
  different question from the matching-bracket highlight ("where does *this* one
  close?"), and the two combine on the same character. Brackets inside strings
  and comments are skipped — a stray `{` in a string would otherwise shift the
  colour of every bracket below it and read as the feature being broken. On by
  default.
- **Folding gained the four things it was missing**: fold a **selection** into a
  range of your own choosing (which survives your edits and comes back on the
  next launch), fold **all block comments**, fold **all `#region` markers**
  (`//#region`, `#region`, `#pragma region`, `<!-- #region -->` and friends), and
  fold **everything except the block you are in**. All four are palette commands
  and bindable, and they merge with the folds Editora detects — or the ones your
  language server reports — rather than replacing them.
- **Pasted Java code imports itself.** Paste a snippet into a Java file and the
  language server is asked which imports it needs; they are added for you. This
  is the one paste behaviour nothing else in the stack can approximate, because
  it needs the type resolver.
- **Smart semicolon placement for Java.** Typing `;` part-way through an
  expression puts it at the end of the statement: `compute(1, 2|)` becomes
  `compute(1, 2);|`. The semicolon is never held back waiting for the server, so
  typing never stalls.
- **Rename shows you what it will change.** A rename that reaches beyond the
  current file lists every affected file first, with its change count and where
  it moves to, and you can untick any file before applying. A rename confined to
  the file you're looking at applies straight away as before: it's on screen and
  one undo away, so there'd be nothing to confirm.
- **Quick fixes appear at the caret.** The code-action list used to open as a card
  centred near the top of the window, the same surface used for picking a project.
  It now drops just below the cursor, where you are actually looking, with the
  server's preferred fix preselected so Enter usually does the right thing.

## The status bar tells you more

- **Long-running work now says it's running.** A find-in-files sweep over a big
  tree used to announce itself once and then go quiet, which looks identical to a
  hang. The status bar shows what's in progress with a spinner, and a count when
  several things run at once; it disappears entirely when nothing is happening.
- **Failures no longer slip past you.** The status bar shows one message at a time
  and the next replaces it, so an error that appeared while you were typing used
  to vanish without trace. Errors are now shown in red, recorded as errors in the
  message log, and leave a small count beside the status line that stays until you
  open the log. Warnings are marked too, without the count.

## Git, GitHub and the Output console

- **The command lines Editora runs are now visible.** The shared output console
  gained a **Git** and a **GitHub** tab holding a transcript of the `git` / `gh`
  commands run on your behalf: the command, its output, its exit code and its
  duration. Git logs the ones you asked for (commit, push, pull, checkout, stash,
  clone…) and deliberately not the `status`/`diff` reads it re-runs on every tab
  switch, which would bury them. Neither steals focus — the transcript is waiting
  when you open the window.
- **The GitHub tool window is filterable and keyboard-driven.** Pull Requests,
  Issues and Runs share one filter box matching everything a row shows (number,
  title, author, branch, state, labels), with a leading `#` optional so `42` and
  `#42` both find PR 42. The window opens with focus in the filter and the first
  row selected; `C-n`/`C-p` move without leaving the box, Down enters the list,
  Enter opens.
- **"Build Output" is now just "Output"** — it holds build tools, Git, GitHub and
  CI logs, so the old name undersold it. Your stripe placement and any keybinding
  are unaffected.

## Performance and fixes

- **Closing a large tab no longer stalls highlighting everywhere else.** Files of
  the same language share one grammar and only one can be tokenized at a time, so
  a background pass over a very large document held that lock for its whole
  duration while every other file of that language waited. Two paths let such a
  pass run when nothing needed it any more — a superseded pass ran to completion
  instead of stopping, and a closed buffer could still dispatch a fresh one. Both
  are now refused.
- **A run configuration with no main class says so**, instead of reporting a Java
  language server stack trace about a null `SearchPattern`.
- **A stray command-line flag no longer opens its value as a file.** Any two-token
  option Editora doesn't define had its *value* treated as a path, so launching
  could greet you with `Failed to open: …/javafx.graphics/com.sun.glass.ui=com.editora`.
- **Doctor no longer crushes the tool name.** A long resolved path shrank
  everything beside it, reducing the name identifying the row to "…" or
  "Docker…".
- **The Split buttons are disabled where they would do nothing** — on the Welcome
  and Doctor pages and the image, hex, PDF and diff viewers, which aren't text
  buffers.

The complete list is on the [What's New](/whats-new) page, and there is a
[0.10.0 blog post](/blog/editora-0-10-0) on what a design system actually has to
decide.
