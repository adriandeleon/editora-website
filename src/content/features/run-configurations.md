---
title: "Run configurations"
group: "Run & debug"
order: 2
beta: false
summary: "Save how a thing is launched — Java main class, Python or shell script, or a make target — with a before-launch build step, a toolbar selector, and a file you can commit so your team gets the same ones."
---

A run configuration is a saved answer to "how is this launched": the main class or script, program and VM arguments, environment variables and a working directory. Pick one in the toolbar and hit **Run** or **Debug**, with **Stop** beside them; the choice is remembered across restarts.

## Four kinds, not just Java

Choose a **Type** in Settings → Run Configurations:

- a **Java main class** (resolved through the language server, or through your Maven/Gradle build)
- a **Python script**
- a **shell script**
- a **make target**

Script configurations need no project and no language server at all. Debugging remains Java-only, and says so rather than reporting a confusing Java error.

## A step before the launch

A configuration can name a command to run first — a build, a codegen step. A **non-zero exit aborts the launch**, so a stale binary is never run by accident.

## Shareable

**Export Configurations to Project** writes them to `.editora/run-configurations.json` inside the project, where they can be committed alongside [per-project settings](/features/projects). **Import** merges them back **by name**, so importing twice doesn't duplicate and a colleague's edit updates the configuration rather than doubling it.

## It stays out of your way

- Each configuration **becomes a real command**, so it appears in the palette by name and can be given its own keyboard shortcut, the same way [saved macros](/features/macros) and [external tools](/features/external-tools) already work.
- **Add** prefills from the file you are looking at: the main class from the active Java file (or the one your Gradle build declares), the name from that class, and the cursor in whichever field still needs you.
- The toolbar group **only appears where you could actually launch something**, so a project of Markdown notes doesn't carry a dropdown that can never fill. Anything you have already saved keeps it visible regardless.
- Running an incomplete configuration **opens its form** at the field you need to fill in, rather than naming the problem and leaving you to find it.
