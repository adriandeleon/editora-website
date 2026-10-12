---
title: HTTP client
description: Send HTTP requests from .http and .rest files, with environments, variables, and request chaining.
category: Run & debug
order: 2
---

Open a `.http` or `.rest` file and click the green ▶ next to a request to send
it. It uses the JDK's built-in HTTP client, so there's no external tool to
install. The HTTP client is **on by default**; its switch is the HTTP Client
section of **Settings → Languages & Tools → Web** (or
`view.toggleHttpClient`).

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
file (with a `$shared` section for common values) and pick one from the
environment dropdown in the response preview (`http.selectEnvironment` takes
you to it); the choice is remembered per workspace.

## Chaining, bodies, and auth

The client is close to IntelliJ's HTTP Client:

- **Request chaining** references an earlier request's response, so a login can
  feed a token into the next call. See [Naming a request](#naming-a-request).
- **Multipart** and external-file bodies are supported, with automatic URL
  encoding. Body files and `>>` response targets stay inside the request's
  folder; a symlink that leads out of it is refused.
- **Basic / Digest auth** shorthand, per-request directives, and
  response-to-file redirects. See [Saving a response to a
  file](#saving-a-response-to-a-file).

### Naming a request

A later request refers to an earlier one by name, as
`{{name.response.body.$.path}}`, `{{name.response.headers.Header-Name}}` or
`{{name.response.status}}`. A request gets its name in one of two ways:

- A `# @name login` comment (or `// @name login`) anywhere in the request's
  section before the request line, whether or not the `###` separator above it
  has a title.
- Its `### Title`, when the request has no `@name`. A `@name` takes precedence
  over the title.

```http
### Sign in
# @name login
POST https://api.example.com/login
Content-Type: application/json

{ "user": "ada", "password": "{{password}}" }

### Fetch the profile
GET https://api.example.com/me
Authorization: Bearer {{login.response.body.$.token}}
```

Responses are captured for the length of one run. Running the whole file
(`http.runFile`) sends the requests in order, so each one can refer to those
before it. A request run on its own starts with nothing captured, and a
reference to a request that hasn't run resolves to an empty string.

### Saving a response to a file

A `>>` line after a request writes the response body to a file in the request's
folder. The two forms differ in what they do when the file already exists:

- `>> file` creates the file and never touches an existing one.
- `>>! file` replaces an existing file, but only with a complete, successful
  response. An error status, or a body that was cut short, leaves the file as it
  is. Before replacing, Editora records the previous content in
  [Local History](/docs/undo-history), and it leaves the file alone when the file
  is open in the editor with unsaved changes.

The response view reports what happened to every target: which files were
written or replaced, and which were not and why.

## Running and the response

| Action | Command | Default key |
| --- | --- | --- |
| Run the request at the caret | `http.runRequest` | (gutter ▶) |
| Run every request in the file | `http.runFile` | (palette) |
| Cancel the running request | `http.cancelRequest` | (Cancel button) |
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
