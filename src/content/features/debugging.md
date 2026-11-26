---
title: "Debugging (DAP)"
group: "Run & debug"
order: 3
beta: false
summary: "Full debugging for Java, Python, JavaScript, TypeScript, Groovy, Kotlin, Clojure, Scala, Go, Rust, C, C++, C#, Ruby, PHP, Bash, PowerShell, Elixir, Dart and Lua: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, Debug History, and an interactive console."
description: "Debug 20 languages over the Debug Adapter Protocol: breakpoints of every kind, stepping, watches, inline values, Debug History and memory views."
alt: "A Java program paused at a breakpoint, with inline values and the Debug window's call stack and variables"
---

---
---
Stop a program where it goes wrong, read its values and step through it, in one Debug window that works the same way in every language and never takes the keyboard from the editor.

## Start a session

**Debug: Start / Continue** is `C-c C-d d` in the Emacs keymap and `F5` in the CUA, Sublime Text and VS Code keymaps (see the [keybindings](/keybindings/) for IntelliJ IDEA). It builds first and stops on compile errors.

- **Debug File** in the editor's right-click menu debugs the file you are in.
- **Debug Test at Caret** debugs one test, in JUnit, pytest, Go, Rust, Jest, RSpec, PHPUnit and most other frameworks.
- **Web apps** debug as they are served: Django, Flask, FastAPI, Rails, PHP and Electron.
- **Debug in Browser…** opens a page in Chrome, Edge or Firefox under the debugger.
- **VS Code's `launch.json`** starts as it does in VS Code, compounds and `preLaunchTask` included.
- **Several sessions** run at once, each in its own tab.
- **Attach to Process…** lists the programs on this machine you can debug, and Stop leaves the one you pick running.
- **Attach to Remote Program…** debugs a C, C++ or Rust program under gdbserver or lldb-server elsewhere.
- **Open Core Dump…** opens what a crashed Go, C, C++ or Rust program left.

## Set breakpoints

**Debug: Toggle Breakpoint** is `C-c C-b` in the Emacs keymap and `F9` in the CUA, Sublime Text and VS Code keymaps.

- **Conditions and hit counts** decide when a breakpoint stops, and a **temporary** one removes itself once hit.
- **Logpoints** print a message without stopping; Shift+click the gutter to add one.
- **Triggered** breakpoints wait for another one, and **recording** ones note the stop and let the program go on.
- **Lambda, method and field breakpoints** stop inside a lambda, on entering a Java method, or when a field changes.
- **Exception breakpoints** stop on the exceptions you name, such as `java.io.*`, and a card under the line that threw shows what was thrown.
- **Filters** limit a breakpoint to some threads, callers or one marked object.
- **Groups** switch a set of breakpoints together, and **export and import** share them.

## Step through the code

- **Step Into, Over and Out**, with the time each step took shown after the line.
- **Run to Cursor**, or **Force Run to Cursor** past every breakpoint.
- **Don't Step Into This…** makes Step Into go over a class, package, module or file.
- **Smart Step Into…** picks the call to enter, and **Restart Frame** runs a function again.
- **Hot code replace** loads a saved Java change into the running program.
- **Step Back** runs C and C++ backward on GDB once **Debug: Record Execution** is on.
- **Pause or resume one thread** while the others keep going.

## Inspect values

- **Inline values** follow each line while paused, and a hover unfolds a value as a tree.
- **The Variables tree** highlights what the last step changed and edits a value in place.
- **Evaluate Expression** runs code in the stopped frame, in its own window or with Alt+click.
- **Watches and pinned values** update at every stop; a pinned value sits at the end of its line.
- **Autos** lists the values the stopped line uses, and a step out shows the return value where the debugger reports it.
- **Show Value As** shows a number in hexadecimal, binary, octal or as a character.
- **Type renderers** show a type your way, such as a `Money` as `12.50 EUR`.
- **View as Table** opens a list or map as a sortable table, and a pandas DataFrame or numpy array as its rows.
- **Mark Object** labels an object, and **View as JSON or XML** lays out a string.

## Look back and look deeper

- **Debug History** shows an earlier stop as it was, with its stack, values and watches.
- **Thread Dump** shows every thread's stack, even in a program that hangs, and **Parallel Stacks** draws them as one tree.
- **Disassembly and Memory** views step by instruction and edit bytes in C, C++, Rust and Go.
- **Loaded Sources** opens code with no file, such as `eval` code, where it stops.

## Languages and adapters

Each language's debugger is installed separately. Its card in **Settings → Debugging** says whether it is found and offers **Install…** where Editora can fetch it. Java debugging also needs the [language server](/features/lsp/), and **Debug: Edit Debug Adapters** adds any other debugger that speaks the Debug Adapter Protocol. Each page under [Languages](/docs/languages/) says what its debugger supports.

**On by default.** Turn it off in **Settings → Debugging**. See the [Debugging guide](/docs/debugging/#start-debugging).
