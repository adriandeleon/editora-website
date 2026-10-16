---
title: "Run files & main classes"
group: "Run & debug"
order: 1
beta: false
summary: "Run a script, or a Maven/Gradle project's <code>main</code> class, from a gutter ▶. Output and <code>stdin</code> go to the Run console, with clickable stack traces and saved run configurations."
---

A green ▶ in the gutter runs the current file, a **Java compact source file** (JEP 512), an extensionless Java file with a `java --source 25+` shebang, a **Python** script, or a **shell** script (run with the interpreter its shebang names, else `bash`). All of them run with language services disabled; Java uses the selected Default JDK or `java` on your `PATH`. Shell startup files such as `.bashrc` and `.xinitrc` get no ▶.

It also runs a **project's main class**. *Run Main Class…* picks any `main` in the active file's **Maven or Gradle** project, and a ▶ sits beside every `public static void main` (the right-click menu offers *Run '….main()'*). With the Java language server set up, Editora asks it for the main classes and the resolved classpath; without it, Run falls back to the build tool, with Maven resolving the classpath and Gradle delegating to `run` or `bootRun`.

For anything you run more than once, save it as a [run configuration](/features/run-configurations) instead.

Output streams to the Run console, which also accepts **stdin** so `readln`-style programs work, and **stack-trace lines are clickable** (Java, Python and Node frames, and shell errors) to jump to the file and line. The console draws the colours programs print, keeps a `` progress bar on one line, and has **End Input** for programs that read until the end of input. Pass per-file **program arguments** (remembered across runs), and re-run the last file with *Rerun Last*. *Run with Trace* runs a shell script with `bash -x`, and each traced line links to its source line. A file in a [remote folder](/features/remote-sftp) runs on its host, with output, input and Stop as for a local one. A spinner turns in the Run window, and on the Run stripe button when the window is closed, while the program runs. Bind it to `C-c r` or run from the palette.
