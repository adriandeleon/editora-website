---
title: "Debugging (DAP)"
group: "Run & debug"
order: 3
beta: true
summary: "Full debugging for Java, Python, JavaScript, TypeScript, Groovy, Kotlin, Clojure and Scala: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, and an interactive console."
---

Full debugging for **Java**, **Python**, **JavaScript and TypeScript** (Node.js), **Groovy**, **Kotlin**, **Clojure** and **Scala** (through Metals for a build's file, Editora's own debugger for a script) through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window:

- Breakpoints (conditional, with a hit count, and logpoints), step in / over / out, **run-to-cursor**, and **jump-to-line** (Python)
- A threads + call-stack view and a lazy variables tree with **set-value**, names, values and types coloured like code in the editor theme
- **Watches** and an evaluate console, with **completion** from the debugger while the program is paused
- A hover **value popup** that unfolds objects, arrays and collections as a tree
- **Exception breakpoints** picked from what the debugger offers (Python: raised, uncaught, user-uncaught; uncaught exceptions stop from the start)
- **Debug File** next to Run File in the editor's right-click menu
- A debugged Java, Python or JavaScript program can read **standard input** typed in the Debug console
- The Debug window names the **runtime** the program runs under: its JDK, Python or Node.js version
- Tests inside JUnit `@Nested` classes run and debug from the gutter

For Java it goes beyond single files: *Debug Main Class…* debugs any `main` in the active file's Maven or Gradle project (with saved run configurations carrying program and VM arguments and environment variables), and *Debug via Build Tool* launches a Gradle or Spring Boot app under a suspended JVM and attaches when it is listening. A project launch builds the project first and stops on compile errors, which the Debug console lists as links. Saving a file during a session loads the change into the running program (**hot code replace**), and *Restart Frame* and *Smart Step Into* are there when you need them. Step Into skips JDK classes, and JDK or library frames open their source. Breakpoints differ by shape, are listed in one picker you can filter, and can be muted; **Caught** and **Uncaught** exception toggles are kept per project. Every keymap binds the debugger, and the Debug window never takes the keyboard from the editor.

JavaScript runs under the same Node.js as Run. `.ts` files debug on Node.js 22.18 or newer, and a breakpoint in a compiled project's `.ts` source stops there through source maps. *Attach* connects to a Node.js started with `--inspect`, *Debug Test at Caret* debugs a `node:test` test, and Node.js and npm run configurations can be debugged.

A Python program is debugged the way Run runs it: under the project's virtual environment (or the interpreter picked for the project), in the script's own folder, with the project root and `src/` on `PYTHONPATH`. A pytest test is debugged from its test line, *Debug Test at Caret*, or its row in Test Results; without pytest, a `unittest` test runs under `unittest`. The console shows the command that runs and, at the end, the exit code.

Groovy is debugged by a debugger built into Editora, with nothing to install: breakpoints in scripts, classes and closures, stepping that skips Groovy's own runtime, locals and fields, and a stop where an uncaught exception is thrown. It reads variables and field paths but does not evaluate expressions, and breakpoint conditions and log messages are not supported yet.

Clojure uses the same debugger, under the Clojure CLI or Leiningen: breakpoints in a namespace's functions and a script's top-level forms, frames named as Clojure names them (`my-app.core/greet`), locals kept visible for the whole function, and stepping that skips Clojure's runtime. Babashka, a native program, can't be debugged, and says so.

Files in a [remote folder](/features/remote-sftp) are debugged on their host: debugpy or vscode-js-debug there for Python and JavaScript (a Python program under the project's virtual environment on that host), and the host's `jdtls` for Java (Debug Main Class, Attach, Debug Test and saved configurations too). Breakpoints survive a reconnect.

While suspended, **inline values** appear after each line and hovering a variable shows its value. The adapters (java-debug, debugpy, vscode-js-debug) are user-installed, not bundled: each language's card in Settings → Debugging shows whether its adapter is found and has an **Install…** button that fetches it, and a `jdtls` that already bundles java-debug is detected as-is. Off by default. Enable it under Settings → Debugging. Java debugging runs through the Java language server, so it also needs [LSP](/features/lsp) switched on. Shell scripts can't be debugged; *Run with Trace* (see [Run files](/features/run-files)) prints each line as it runs, with a link to its source.
