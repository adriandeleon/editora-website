---
title: "HTTP client"
group: "Run & debug"
order: 3
beta: true
summary: "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶, with environments, variables, request chaining, and a formatted response view. Built on the JDK HTTP client."
---

Open a `.http` or `.rest` file and click the green ▶ next to a request to send it, no external tool, it uses the JDK's built-in HTTP client.

Define multiple requests separated by `###` and the feature reaches for IntelliJ-style parity: `{{var}}` / `@var` substitution, dynamic variables (`{{$random.*}}`, `{{$datetime}}` with date math, `{{$dotenv.X}}`), **request chaining** that references an earlier response, **multipart** and external-file bodies, **environment files** (`http-client.env.json` with a `$shared` section) and a picker, and Basic/Digest auth shorthand.

The response shows status, headers, timing, and a pretty-printed, content-type-highlighted body in a tool window (`M-0`), with an in-session history, **Copy as cURL** / **Import cURL**, open-in-editor, and Save-response. Run one request or the whole file. Off by default. Enable it under Settings → HTTP Client.
