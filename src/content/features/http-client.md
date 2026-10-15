---
title: "HTTP client"
group: "Run & debug"
order: 4
beta: false
summary: "Run <code>.http</code> / <code>.rest</code> requests from a gutter ▶ or a key, with environments, variables, request chaining, and a response view that shows exactly what was sent. Built on the JDK HTTP client."
---

Open a `.http` or `.rest` file and click the green ▶ next to a request to send it, or press `C-c C-c` (`Ctrl+Alt+R` in the other keymaps). No external tool is needed: it uses the JDK's built-in HTTP client.

Requests are written in JetBrains' HTTP request format, separated by `###`, with some VS Code REST Client syntax as well. That covers `{{var}}` and `@var` substitution, directives such as `# @no-redirect` and `# @timeout 5 s`, dynamic variables (`{{$uuid}}`, `{{$random.*}}`, `{{$datetime}}` with date math, `{{$dotenv.X}}`), **request chaining** from an earlier response, **multipart** and external-file bodies, **environment files** (`http-client.env.json` and a private one, with a `$shared` section) and a picker remembered per file, and Basic/Digest auth shorthand. A variable that can't be resolved is underlined as you type and listed in the response's warnings, typing `{{` offers the variables available there, and a request that reads your environment variables or a `.env` file asks before it runs from a folder you haven't trusted.

The response is the `.http` file's own **preview**, in the same Editor / Split / Preview view every other rich file type uses, so it sits beside the request that produced it. It shows the status (`200 OK · 32 ms · 44 B`), the request as it was sent, warnings, headers, and a pretty-printed, content-type-highlighted body; compressed responses are decoded and images shown. The last 10 responses of each file are kept, with credential headers masked. **Copy as cURL** gives a bash/zsh command that is safe to paste, **Import cURL** takes a browser's copy, and Save response keeps the bytes as received. Run one request or the whole file, from a saved or an untitled file. On by default. Toggle it under Settings → Languages & Tools → Web.
