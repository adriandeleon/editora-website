---
title: HTTP client
description: Send HTTP requests from .http and .rest files, with environments and variables.
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

Use `{{variables}}` and dynamic built-ins like `{{$uuid}}`, `{{$timestamp}}`,
`{{$isoTimestamp}}`, and `{{$randomInt}}`. Declare file-local variables with
`@name = value`, and define named **environments** in `http-client.env.json`
alongside the file. The environment is picked in the tool window and remembered
per workspace.

## Running and the response

| Action | Command | Default key |
| --- | --- | --- |
| Run the request at the caret | `http.runRequest` | (gutter ▶) |
| Run every request in the file | `http.runFile` | (palette) |
| Select environment | `http.selectEnvironment` | (palette) |
| Open the tool window | `tool.http` | `M-0` |

The response shows in the **HTTP Client** tool window with the status line,
headers, timing, size, and a pretty-printed JSON body, plus a Save-response
action.

## Limits

Response-handler scripts (`> {% %}`) and cross-request captured variables aren't
supported yet, so a request that needs a token from an earlier one can't run in
isolation. Multipart and file bodies are also deferred.
