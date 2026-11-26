---
title: "HTTP client"
group: "Run & debug"
order: 4
beta: false
summary: "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶ or a key, with environments, variables, request chaining, and a response view that shows exactly what was sent. Built on the JDK HTTP client."
description: "Run .http and .rest requests from the editor or with editora http on the command line, with environments, variables, chaining and tests."
alt: "An .http file with two requests and a test script, beside the JSON response of a 200 OK"
---

---
---
Try an API without leaving the editor. Write requests in a `.http` or `.rest` file, send one with the green ▶ beside it, and read the response next to the request that produced it. Nothing else to install.

**HTTP: Run Request at Caret** is `C-c C-c` in the Emacs keymap and `Ctrl+Alt+R` in the other keymaps (see the [keybindings](/keybindings/) for macOS). **HTTP: Run All Requests in File** is `C-c C-r` in the Emacs keymap.

## Write requests

- **JetBrains' HTTP request format**, requests separated by `###`, with some VS Code REST Client syntax too.
- **Variables and environments**: `{{name}}` comes from the file, an `http-client.env.json` environment picked from the Env pill, a `.env` file or a dynamic value such as `{{$uuid}}`.
- **Chaining**: a request reads a value from an earlier response, so a sign-in feeds the token to the next call.
- **Bodies and auth**: multipart and file bodies, and Basic and Digest shorthand.
- **Help as you type**: `{{` offers the variables in scope, and an unknown variable is underlined, with a fix that switches to an environment that defines it.

## Test the response

- **`# @assert` lines** check the status, a header or a JSON value without any code.
- **JavaScript response handlers** run JetBrains-style `client.test(…)` scripts on your own Node.js.
- **Pre-request scripts** sign a request or set its variables before it is sent.
- **Trusted folders**: scripts run, and requests read your environment, only in folders you trust, or after you agree.

## Read the response

- **Beside the request**: the response opens as the `.http` file's preview, with the status, time and size in its header.
- **Tabs** hold the body, pretty-printed and highlighted, the headers, the test results, the request exactly as sent, and warnings.
- **Results on each request line**, such as `200 OK · 83 ms · ✗ 1 of 3 tests`, and a clear message when there was no response.
- **History**: the last 10 responses of each file are kept, with credential headers masked.
- **cURL**: **HTTP: Copy Request as cURL** gives a command that is safe to paste, and **HTTP: Import cURL from Clipboard** takes a browser's copy.

## Run from the command line

`editora http` (`editora-cli http` on Windows) runs every request of a file in order, with the same chaining, checks and handlers, for CI and scripts. It takes an environment and variables, writes a JUnit XML report with `--report`, and exits 1 when a request or a test fails. `editora http --help` lists every option.

**On by default.** Turn it off in **Settings → Web**. See the [HTTP client guide](/docs/http-client/#writing-requests).
