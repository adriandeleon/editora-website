---
title: "Debugging (DAP)"
group: "Run & debug"
order: 3
beta: true
summary: "Full debugging for Java, Python, and JavaScript: breakpoints, step in/over/out, watches, set-value, run-to-cursor, inline values, and an interactive console."
---

Full debugging for **Java**, **Python**, and **JavaScript** through the Debug Adapter Protocol, with an IntelliJ-style Debug tool window:

- Breakpoints (conditional, with a hit count, and logpoints), step in / over / out, **run-to-cursor**, and **jump-to-line**
- A threads + call-stack view and a lazy variables tree with **set-value**
- **Watches** and an evaluate console
- **Exception breakpoints** picked from what the debugger offers (Python: raised, uncaught, user-uncaught; uncaught exceptions stop from the start)
- **Debug File** next to Run File in the editor's right-click menu
- A debugged Java or Python program can read **standard input** typed in the Debug console
- Tests inside JUnit `@Nested` classes run and debug from the gutter

For Java it goes beyond single files: *Debug Main Class…* debugs any `main` in the active file's Maven or Gradle project (with saved run configurations carrying program and VM arguments and environment variables), and *Debug via Build Tool* launches a Gradle or Spring Boot app under a suspended JVM and attaches when it is listening.

A Python program is debugged the way Run runs it: under the project's virtual environment (or the interpreter picked for the project), in the script's own folder, with the project root and `src/` on `PYTHONPATH`. A pytest test is debugged from its test line, *Debug Test at Caret*, or its row in Test Results; without pytest, a `unittest` test runs under `unittest`. The console shows the command that runs and, at the end, the exit code.

Files in a [remote folder](/features/remote-sftp) are debugged on their host: debugpy or vscode-js-debug there for Python and JavaScript (a Python program under the project's virtual environment on that host), and the host's `jdtls` for Java (Debug Main Class, Attach, Debug Test and saved configurations too). Breakpoints survive a reconnect.

While suspended, **inline values** appear after each line and hovering a variable shows its value. The adapters (java-debug, debugpy, vscode-js-debug) are not bundled: **Install…** on each language's card in Settings → Debugging fetches them, and a `jdtls` that already bundles java-debug is detected as-is. Off by default. Enable it under Settings → Debugging. Java debugging runs through the Java language server, so it also needs [LSP](/features/lsp) switched on. Shell scripts can't be debugged; *Run with Trace* (see [Run files](/features/run-files)) prints each line as it runs, with a link to its source.
