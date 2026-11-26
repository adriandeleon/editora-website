---
title: "Run files & main classes"
group: "Run & debug"
order: 1
beta: false
summary: "Run the file you are looking at from a gutter ▶: a script, a program or a project's <code>main</code> class, in most of the languages Editora supports. Output, input and clickable stack traces are in the Run console."
description: "Run a Java, Python, Node.js, shell, Kotlin, Ruby, PHP, Go or Rust file, and many more, from a gutter button, with input and clickable stack traces."
alt: "A Python script with a gutter ▶, and its output in the Run window below"
---

---
---
A green ▶ in the gutter runs the file you are looking at, with nothing to set up first. It picks the runtime the project asks for, such as its `.venv`, its `.nvmrc` or its JDK, and works with language services turned off.

## What the ▶ runs

- **Scripts**: Python, Node.js and TypeScript, shell, Ruby, PHP, Deno, Elixir and PowerShell.
- **JVM files**: a single-file or compact Java program, Groovy, Kotlin, Scala and Clojure, each through its own build when it has one.
- **Compiled programs**: C, C++, Go, Rust and Dart, built the way Debug builds them, then run.
- **Tests**: a test file runs under its own runner, such as pytest, Jest, Vitest, RSpec, PHPUnit or GoogleTest, with each test listed in Test Results.
- **Web apps**: Django, Flask, FastAPI, Rails, PHP and Electron start their server.

Each language's page under [Languages](/docs/languages/) says exactly what its ▶ runs.

**File: Run** is `C-c r` in the Emacs keymap and `Ctrl+Shift+B` in the VS Code keymap (`Cmd+Shift+B` on macOS). **Run: Rerun Last** runs the last file again with the same arguments.

## A project's main class

In a Maven or Gradle project, a ▶ sits beside every `public static void main`, and **Run: Run Main Class…** picks any of them. The classpath comes from the Java language server, or from the build tool when the server is not set up.

## The Run console

- **Input**: the console takes `stdin`, so programs that read input work, and **Run: End Program Input** ends it.
- **Clickable stack traces**: Java, Python and Node.js frames, shell errors and pytest locations open the file at that line.
- **Colours and progress bars** show as the program prints them.
- **Program arguments** are kept per file across runs.
- **The runtime** the program runs under is named beside the console, such as *Python 3.13.5*, read from the program that ran.
- **Stop** gives a Python program the chance to run its `finally` blocks on macOS and Linux.

## Trace, remote and saved runs

- **Run: Run with Trace** runs a shell script with `bash -x`, and each traced line links to its source.
- A file in a [remote folder](/features/remote-sftp/) runs on its host, with the same output, input and Stop.
- For anything you run more than once, save a [run configuration](/features/run-configurations/).

See the [Running programs guide](/docs/run-debug/#running-files).
