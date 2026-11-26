---
title: "Editora 0.29.0: web apps and tests in more languages, a log viewer for real logs, and the guide inside the app"
seoTitle: "Editora 0.29.0: web apps, logs, in-app guide"
description: "Run and debug Django, Flask, FastAPI, Rails, PHP and Electron apps; PowerShell, Elixir, Dart and Lua; tests at the caret; a better log viewer; an offline guide."
date: 2026-11-25
version: "0.29.0"
---

**Editora 0.29.0** is out. Run and Debug now start web apps and test
runners the way their own tools do, in more languages than before. The log
viewer learns to read times, follow live sources and sum a log up. And the
user guide ships inside the app.
Download it from the [0.29.0 release page](https://github.com/adriandeleon/Editora/releases/tag/v0.29.0).

## Web apps and tests

- **Web apps start as they are served.** Run or Debug a Django project's
  `manage.py`, a Flask or FastAPI app, a Rails app, a PHP app (on PHP's
  built-in server or as `artisan serve`) or an Electron app, and Editora
  starts the server or the app. Breakpoints stop on each request, and Python
  apps keep stopping after the reloader restarts the server.
- **Tests at the caret, almost everywhere.** Run Test at Caret and Debug Test
  at Caret run the one test under the caret in Go, Rust, Dart, Kotlin, Groovy,
  Jest, Vitest, Deno, RSpec, Minitest, PHPUnit, ExUnit, C# and C or C++
  (GoogleTest, Catch2, doctest). Test Results now lists each test of Jest,
  Vitest, Deno, RSpec, Minitest, PHPUnit, ExUnit, `dart test` and the C and
  C++ frameworks, with Rerun Failed.
- **launch.json `serverReadyAction`** opens the server in the browser, or
  debugs it there, as soon as it says where it listens.

[More about debugging](/docs/debugging).

## More languages

- **PowerShell**, with PowerShell Editor Services for diagnostics, completion
  and debugging.
- **Elixir**, with ElixirLS: Mix tests, scripts and projects run and debug.
- **Dart and Flutter**, with Dart's analysis server and debugger.
- **Lua** and **Deno** debugging, and **Firefox** for web pages.
- **Run File** runs C, C++, Go and Rust programs and single-file Java
  programs (`java Hello.java`), not only scripts.

## The debugger

- **Autos** shows the values used on the line where the program stopped.
- **Return values** appear after a step leaves a function.
- **Instruction breakpoints** in the Disassembly view, with conditions, and
  **Edit Bytes…** in the Memory view, on CodeLLDB, GDB and Delve.
- **Attach to Remote Program** debugs a C, C++ or Rust program under
  gdbserver on another machine.

## The log viewer

- **Live logs** from `docker logs`, `kubectl logs`, `journalctl` or `ssh …
  tail -F` open as a log tab with filters and Follow.
- **Times:** go to a time, or show only the records between two times.
- **Log: Summarize** writes a report of a log's levels, busiest messages and
  longest silences; **Log: Merge Logs…** interleaves several logs by time.
- A second filter hides records, warnings and errors are marked on the
  scrollbar, `.gz` logs open directly, and a log opens filtered as you left it.

[More about the log viewer](/docs/log-viewer).

## Java toolchains

A project's `.sdkmanrc`, or [jkit](https://jkit-project.dev/)'s pins, now
choose the JDK, Maven and Gradle it builds, runs and debugs with. The Java
Toolchain commands install, pin and update them through jkit.

[More about Java](/docs/java).

## Looks and help

- A **System** theme follows your light or dark setting, and **High Contrast
  Light** and **High Contrast Dark** read at 7:1 or better.
- On Windows and Linux the menu bar sits in the title bar by default.
- **Help ▸ Documentation** opens the user guide that ships with Editora, for
  the version you run, without a network connection.

[More about themes](/docs/themes-fonts).

## Faster

Each keystroke allocates about two thirds less and takes about half the
UI-thread time, background tabs no longer hold large textures, and start-up
builds menus and the Project Map when they are first opened. Big files keep
highlighting up to 200,000 lines.

## And more

- Hold Ctrl (Cmd on macOS) over code to see what Ctrl/Cmd-click would follow.
- `editora http api.http` runs a `.http` file from the command line, and the
  Windows install has `editora-cli.exe` on the PATH.
- Git Rollback and Discard keep a Local History copy first.
- Without a system keychain, AI API keys are kept in an encrypted file instead
  of `settings.json`.

The full list is in [What's New](/whats-new/0.29/#0290---2026-11-25).
