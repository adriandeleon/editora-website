---
title: HTTP client
description: Send HTTP requests from .http and .rest files, with environments, variables, and request chaining.
category: Run & debug
order: 2
beta: true
---

Open a `.http` or `.rest` file and click the green ▶ next to a request to send
it. It uses the JDK's built-in HTTP client, so there's no external tool to
install. The HTTP client is **off by default**; enable it in
**Settings → HTTP Client**.

## Writing requests

Separate requests with a `###` line. A request is a method and URL followed by
headers and an optional body:

```http
### Get a user
GET https://api.example.com/users/42
Accept: application/json

### Create one
POST https://api.example.com/users
Content-Type: application/json

{ "name": "Ada" }
```

## Variables and environments

Substitute `{{var}}` and file-local `@var = value` declarations, plus **dynamic
variables**: `{{$random.*}}`, `{{$datetime}}` (with date math), `{{$dotenv.X}}`,
and more. Define named **environments** in `http-client.env.json` alongside the
file (with a `$shared` section for common values) and pick one in the tool
window; the choice is remembered per workspace.

## Chaining, bodies, and auth

The client is close to IntelliJ's HTTP Client:

- **Request chaining** references an earlier request's response, so a login can
  feed a token into the next call.
- **Multipart** and external-file bodies are supported, with automatic URL
  encoding.
- **Basic / Digest auth** shorthand, per-request directives, and
  response-to-file redirects.

## Running and the response

| Action | Command | Default key |
| --- | --- | --- |
| Run the request at the caret | `http.runRequest` | (gutter ▶) |
| Run every request in the file | `http.runFile` | (palette) |
| Select environment | `http.selectEnvironment` | (palette) |

The response appears as the `.http` file's own **preview**, in the same
Editor / Split / Preview view every other rich file type uses, so the response
sits beside the request that produced it. Running a request from the gutter ▶
opens the split automatically, and the view mode is remembered per file. Switch
views with `view.togglePreview` and `view.toggleSplitPreview`, or the floating
3-mode toggle at the top right.

The response shows the status line, headers, timing, size, and a
content-type-highlighted, pretty-printed body. You can **Copy as cURL**,
**Import cURL** from the clipboard (`http.importCurl`), open a response in its
own editor tab (`http.openResponseInTab`), and save the response to a file.
