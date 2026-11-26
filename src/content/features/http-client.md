---
title: "HTTP client"
group: "Run & debug"
order: 4
beta: false
summary: "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶ or a key, with environments, variables, request chaining, and a response view that shows exactly what was sent. Built on the JDK HTTP client."
description: "Run .http and .rest requests from the editor or with editora http on the command line, with environments, variables, chaining and tests."
alt: "An .http file with two requests and a test script, beside the JSON response of a 200 OK"
---

Open a `.http` or `.rest` file and click the green ▶ next to a request to send it, or press `C-c C-c` (`Ctrl+Alt+R` in the other keymaps). No external tool is needed: it uses the JDK's built-in HTTP client.

Requests are written in JetBrains' HTTP request format, separated by `###`, with some VS Code REST Client syntax as well. That covers `{{var}}` and `@var` substitution, directives such as `# @no-redirect` and `# @timeout 5 s`, dynamic variables (`{{$uuid}}`, `{{$random.*}}`, `{{$datetime}}` with date math, `{{$dotenv.X}}`), **request chaining** from an earlier response, **multipart** and external-file bodies, **environment files** (`http-client.env.json` and a private one, with a `$shared` section) chosen per file from an Env pill beside the editor or the status bar, and Basic/Digest auth shorthand. Responses can be tested with `# @assert status == 200` lines or JetBrains-style JavaScript **response handlers** (`> {% client.test(…) %}`) that run on your own Node.js from trusted folders, **pre-request scripts** (`< {% … %}`) can sign a request with the `crypto` and `jwt` helpers and set its variables, and `client.global.set` or `# @capture` keeps a value for later requests as `{{name}}`. A variable that can't be resolved is underlined as you type (with a quick fix to switch to an environment that defines it) and listed in the response's warnings, typing `{{` offers the variables available there, and a request that reads your environment variables or a `.env` file asks before it runs from a folder you haven't trusted.

The response is the `.http` file's own **preview**, in the same Editor / Split / Preview view every other rich file type uses, so it sits beside the request that produced it. A header row gives the status (`200 OK · 32 ms · 44 B`), and tabs hold the body (pretty-printed, content-type-highlighted JSON, HTML and XML; compressed responses decoded; images shown with their type and size), the headers, the tests (`2/3`), the request as it was sent, and warnings. Each request line in the editor shows its last result (`200 OK · 83 ms · ✗ 1 of 3 tests`), a Run All lists every request with its status and tests, and a request that got no response shows what went wrong (Connection refused, Timed out…) with Run again. The last 10 responses of each file are kept, with credential headers masked. **Copy as cURL** gives a bash/zsh command that is safe to paste, **Import cURL** takes a browser's copy, and Save response keeps the bytes as received. Run one request or the whole file, from a saved or an untitled file.

**From the command line**, `editora http` runs every request of a file in order, as Run All does (same cookie jar, chained responses, `# @assert` checks and JavaScript handlers), and prints each response and test, for CI and scripts. It takes an environment (`--env`, `--env-file`, `--private-env-file`), variables (`-V name=value`, or `-P` for one masked in the output), `--report` for a JUnit XML report and `--no-scripts`, and exits 1 when a request or a test fails. On Windows it is `editora-cli http`. `editora http --help` lists every option.

On by default. Toggle it under Settings → Languages & Tools → Web.
