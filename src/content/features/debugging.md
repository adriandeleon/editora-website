---
title: "Debugging (DAP)"
group: "Run & debug"
order: 3
beta: true
summary: "Full debugging for Java, Python, and JavaScript: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, and an interactive console."
---

Full debugging for **Java**, **Python**, and **JavaScript** through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window:

- Breakpoints (including conditional and logpoints), step in / over / out, **run-to-cursor**, and **jump-to-line**
- A threads + call-stack view and a lazy variables tree with **set-value**
- **Watches** and an evaluate console, with **completion** from the debugger while the program is paused
- A hover **value popup** that unfolds objects, arrays and collections as a tree
- A debugged Java program can read **standard input** typed in the Debug console
- The Debug window names the **runtime** the program runs under: its JDK, Python or Node.js version
- Tests inside JUnit `@Nested` classes run and debug from the gutter

For Java it goes beyond single files: *Debug Main Class…* debugs any `main` in the active file's Maven or Gradle project (with saved run configurations carrying program and VM arguments and environment variables), and *Debug via Build Tool* launches a Gradle or Spring Boot app under a suspended JVM and attaches when it is listening. A project launch builds the project first and stops on compile errors, which the Debug console lists as links. Saving a file during a session loads the change into the running program (**hot code replace**), and *Restart Frame* and *Smart Step Into* are there when you need them. Step Into skips JDK classes, and JDK or library frames open their source. Breakpoints differ by shape, are listed in one picker you can filter, and can be muted; **Caught** and **Uncaught** exception toggles are kept per project. Every keymap binds the debugger, and the Debug window never takes the keyboard from the editor.

Files in a [remote folder](/features/remote-sftp) are debugged on their host: debugpy or vscode-js-debug there for Python and JavaScript, and the host's `jdtls` for Java (Debug Main Class, Attach, Debug Test and saved configurations too). Breakpoints survive a reconnect.

While suspended, **inline values** appear after each line and hovering a variable shows its value. The adapters (java-debug, debugpy, vscode-js-debug) are user-installed, not bundled, and a `jdtls` that already bundles java-debug is detected as-is. Off by default. Enable it under Settings → Debugging. Java debugging runs through the Java language server, so it also needs [LSP](/features/lsp) switched on. Shell scripts can't be debugged; *Run with Trace* (see [Run files](/features/run-files)) prints each line as it runs, with a link to its source.
