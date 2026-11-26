---
title: "Run configurations"
group: "Run & debug"
order: 2
beta: false
summary: "Save how a Java main class, a Python, Node.js, shell, Groovy, Kotlin, Clojure or Scala script, a C, C++, Go, Rust or Dart program, a make target, or a named npm script is launched, with a before-launch step and a shareable project file."
description: "Save how a main class, script, make target or npm script runs, with arguments, environment and a before-launch step, and share it in the project."
alt: "A Python script run configuration: script, interpreter, arguments, working folder, console and Run on"
---

A run configuration saves how something is launched: the main class or script, program and VM arguments, environment variables (`$NAME` expands, and `@.env` loads a `.env` file), an optional JDK override, a shell or Python script's interpreter, and a working directory (relative to the project root; a Python script runs in its own folder when it is blank). Pick one in the toolbar and hit **Run** or **Debug**, with **Stop** beside them; the choice is remembered across restarts. Starting it always opens the Run console, and trying again while it is active brings that console back into focus.

## Kinds

Choose a **Type** in the project's **Run Configurations** window:

- a **Java main class** (resolved through the language server, or through your Maven/Gradle build)
- a **Python script**, or a **Python module** (`python -m`), run by the project's Python unless you set **Interpreter** and **Interpreter options**
- a **Node.js script** (with Node.js arguments; a blank **Interpreter** uses the same Node.js as Run)
- a **Scala** file or script, run with Scala CLI
- a **shell script** (its shebang picks the interpreter unless you set **Interpreter** and **Interpreter options**)
- a **Groovy script** (run with `groovy` on the configuration's JDK, its VM arguments in `JAVA_OPTS`)
- a **Kotlin file**: a `.kts` script run with `kotlinc -script`, or a `.kt` program compiled and run with `java -jar`, on the configuration's JDK
- a **Clojure script** (run with the Clojure CLI, Leiningen or Babashka, as its project says, on the configuration's JDK)
- a **C program** or **C++ program**, built the way Debug builds it: its CMake or Make project, or the file alone with `cc` or `c++`
- a **Go program** (the file's package, or the file alone outside a module; a test file runs all its tests)
- a **Rust program**, built by Cargo from the file's package (a test file runs all its tests)
- a **Dart program**, run with `dart run`, or `flutter run` for a Flutter app
- a **make target**
- a named **npm script**, run with the project's own package manager (npm, pnpm, yarn or bun), with arguments passed after `--`

Run configurations belong to a [project](/features/projects): each project window keeps its own, and the No Project window has none. There, a script or a compact Java file runs from its gutter ▶ or **File: Run** (see [Run files](/features/run-files)). Script configurations need no language server. In a [remote project](/features/remote-sftp), every kind but the C, C++, Go, Rust and Dart programs (which build and run on this computer only) runs on the host, with the host's own Java (or the JDK home set for that host) and, for Python, the project's venv there (or the host's Python for Run and tests). While a configuration runs, a spinner turns in the Run window, and on the Run stripe button when the window is closed. Every type but shell and make can be debugged, several configurations together as a compound: Java, Groovy, Kotlin, Clojure, Scala, Node.js, npm, Python, C, C++, Go, Rust and Dart (see [Debugging](/features/debugging)). Debug on a shell configuration points you to its Bash script, which is debugged from its own tab, or to *Run with Trace* (see [Run files](/features/run-files)).

## A step before the launch

A configuration can name a command to run first, such as a build or a codegen step. A **non-zero exit aborts the launch**, so a stale binary is never run by accident. A Java configuration's step runs in the project root, and Debug uses it as its build.

## Shareable

**Export Configurations to Project** writes them to `.editora/run-configurations.json` inside the project, where they can be committed alongside [per-project settings](/features/projects). **Import** merges them back **by name**, so importing twice doesn't duplicate and a colleague's edit updates the configuration rather than doubling it.

## Smaller conveniences

- Each configuration **becomes a real command**, so it appears in the palette by name and can be given its own keyboard shortcut, the same way [saved macros](/features/macros) and [external tools](/features/external-tools) already work.
- **Add** prefills from the file you are looking at: a Python, Node.js, shell, Groovy or Clojure script becomes a configuration for that script; a Java file gives the main class (or the one your Gradle build declares), the name from that class, and the cursor in whichever field still needs you. *Save Run Configuration* works for Python, Node.js, shell, Groovy and Clojure scripts too. A script's folder goes into Working directory (a Node.js script's stays blank, so it runs from its package folder), so the saved configuration runs as its gutter ▶ does.
- The toolbar group **only appears in a project where you could launch something**, so a project of Markdown notes doesn't carry a dropdown that can never fill. Anything you have already saved keeps it visible regardless.
- The form **shows only the fields the selected type uses**, and a hidden field keeps its value. Python, Node.js, shell, Groovy and Clojure scripts can be picked with **Browse…**, a Java configuration's Main class **Browse…** takes a `.java` file and fills in its fully qualified class name, and an npm configuration's **Browse…** lists the `package.json` scripts.
- Running an incomplete configuration **opens its form** at the field you need to fill in, rather than naming the problem and leaving you to find it.
