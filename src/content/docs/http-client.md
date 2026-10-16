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

The file format is JetBrains' HTTP request format, plus some syntax from the VS
Code REST Client extension (`$dotenv`, `$processEnv`, `$datetime`, `$shared`, and
`{{name.response…}}` references).

## Writing requests

Separate requests with a `###` line. A request is a method and URL followed by
headers and an optional body, separated from the headers by a blank line:

```http
### Get a user
GET https://api.example.com/users/42
Accept: application/json

### Create one
POST https://api.example.com/users
Content-Type: application/json

{ "name": "Ada" }
```

- **The request line.** The method can be any case (`get` is sent as `GET`), and
  custom methods such as `PROPFIND` or `QUERY` are sent as written. A line with
  only a URL is a `GET`. A URL without a scheme gets `http://`, and
  `GET /path` with a `Host:` header takes its host from that header. A query can
  continue on indented lines starting with `?` or `&`. `HTTP/1.1` or `HTTP/2` at
  the end of the line chooses the protocol; without it, `http://` requests use
  HTTP/1.1 and `https://` requests negotiate HTTP/2.
- **Headers.** An indented line under a header continues its value. A `Host`
  header can be set to reach a virtual host by IP.
- **Comments.** Lines starting with `#` or `//` are comments, inside a body too:
  they are not sent. To send such lines, put the body in a file and use
  `< ./file` (see [Chaining, bodies, and auth](#chaining-bodies-and-auth)).
- **A literal `{{`.** Write `\{{` to send `{{` as text (a template body, say);
  Import cURL writes every `{{` of a pasted command this way, so pasted text
  never reads your variables.
- **Not supported.** `GRAPHQL`, `WEBSOCKET` and `GRPC` requests are reported as
  not supported and not sent. JetBrains pre-request scripts (`< {% … %}`) are
  skipped, with a warning in the response. Response handlers (`> {% … %}`) run:
  see [Tests and response handlers](#tests-and-response-handlers).

### Directives

Comment lines above the request line set options for that request:

| Directive | Effect |
| --- | --- |
| `# @name login` | Names the request, for [chaining](#naming-a-request) |
| `# @no-redirect` | Don't follow redirects |
| `# @no-cookie-jar` | Don't send or keep cookies |
| `# @no-auto-encoding` | Send the URL exactly as written |
| `# @no-log` | Keep the response out of the saved [history](#history) |
| `# @timeout 5 s` | Give up when the server is silent this long |
| `# @connection-timeout 2 s` | Give up connecting after this long |
| `# @assert status == 200` | Test the response (see [Checks without JavaScript](#checks-without-javascript)) |
| `# @capture token = $.token` | Keep a value of the response as `{{token}}` |

Timeouts take `ms`, `s` or `m`; a number on its own is seconds. Without them a
connection gets 15 s, the response headers 60 s, and the whole body 5 minutes.

## Variables and environments

`{{name}}` is replaced by a variable's value, in the URL, headers, body, and
`>>` paths. Variables come from:

- **`@name = value` lines** in the file. A declaration applies to the requests
  below it; a later one with the same name takes over from there.
- **Environments**, defined in `http-client.env.json`, with secrets in
  `http-client.private.env.json` (keep that one out of Git). Editora reads them
  from the request file's folder, or the nearest folder above it, up to the open
  project's root, else the file's Git (or Mercurial, Subversion) repository root;
  outside a repository only the file's own folder counts. Pick one from the environment dropdown in the response view
  (`http.selectEnvironment` takes you to it); the choice is remembered per
  `.http` file, and the list includes environments defined only in the private
  file. A `$shared` section applies to every environment. The private file wins
  over the public one, and a named environment wins over `$shared`. Nested
  objects are reached with dots (`{{client.host.url}}`) or indexes
  (`{{users[0].name}}`), and a value may refer to another environment value
  (not to the file's `@var` lines). The files may hold `//` comments and trailing
  commas; one that can't be read is named in the response's warnings and in the
  editor marks.
- **Dynamic variables**: `{{$uuid}}`, `{{$timestamp}}`, `{{$isoTimestamp}}`,
  `{{$randomInt}}` (or `{{$randomInt 1 10}}`), `{{$random.integer(1, 100)}}`,
  `{{$random.float}}`, `{{$random.alphabetic(8)}}`,
  `{{$random.alphanumeric(8)}}`, `{{$random.hexadecimal(8)}}`,
  `{{$random.email}}`, and `{{$datetime iso8601}}` / `{{$localDatetime
  "DD-MM-YYYY" -1 d}}` with a format (`iso8601`, `rfc1123`, or a Day.js or Java
  pattern; in Day.js, put literal text in `[brackets]`, as an unknown letter is
  reported) and an optional offset (`y`, `M`, `w`, `d`, `h`, `m`, `s`, `ms`).
  `{{$timestamp}}`, `{{$isoTimestamp}}` and `{{$datetime}}` are in UTC wherever
  you are; `{{$localDatetime}}` uses your time zone.
- **Your system**: `{{$env.NAME}}` or `{{$processEnv.NAME}}` reads an
  environment variable, and `{{$dotenv.NAME}}` reads a `.env` file beside the
  request file (a regular file of at most 1 MB inside that folder; a link that
  leads elsewhere is not read).

A variable that can't be resolved (a typo, no environment selected, a request
the file doesn't have) is underlined as you type; hover it for the reason. If you
send it anyway it goes out as an empty value, and the response lists it under
its warnings. Typing `{{` offers the variables available at that point, the
dynamic variables, the keys of the `.env` file, and `name.response.` for each
request in the file.

### Secrets and trusted folders

A request that reads `$processEnv`, `$env` or `$dotenv` (in any form: directly,
through an `@var` line or an environment value, in a header, a `<@` body file or
a multipart `<@` part) only runs from a **trusted** folder. In a folder you
haven't trusted, Editora asks first, naming the variables as written (never their
values) and the hosts the request goes to, or that a host is chosen by an earlier
response; a redirect may lead to another host. **Cancel** is the default button.
If you decline, nothing that reads a secret is sent; in a Run All, the requests
that read none still go out.
This is the same trust that lets a project's build scripts run, so a project
you've trusted is not asked again. Requests that read no secrets are never
asked about.

## Chaining, bodies, and auth

- **Request chaining** references an earlier request's response, so a login can
  feed a token into the next call. See [Naming a request](#naming-a-request).
- **Bodies** can be inline, or come from a file: `< ./body.json` sends the file
  as is, and `<@ ./body.json` (or `<@latin1 ./body.json`) fills in its
  `{{variables}}` first. The path may use variables too (`< ./fixtures/{{env}}.json`). Text bodies are encoded in the charset the
  `Content-Type` header names (UTF-8 if none).
- **Multipart** form bodies take inline parts and `< ./file` parts, and fill in
  variables in part headers and file paths. File parts are streamed from disk,
  so a large upload isn't read into memory. URLs are percent-encoded automatically, emoji and
  non-ASCII host names included. Body files and `>>` targets stay inside the
  request's folder; a symlink that leads out of it is refused.
- **Basic / Digest auth** shorthand: `Authorization: Basic user password` is
  encoded for you, and `Authorization: Digest user password` answers the
  server's challenge (MD5, SHA-256 or SHA-512-256). The password may contain
  spaces.

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

Each `.http` file keeps the responses of its runs while it is open, so you can
run `Sign in` once and then run `Fetch the profile` on its own. Running the
whole file (`http.runFile`) sends the requests in order, so each one can refer
to those before it. The captured responses are cleared when you pick another
environment or close the file. A reference to a request that hasn't run, or that
failed, is listed in the response's warnings.

### Saving a response to a file

A `>>` line after a request writes the response body to a file in the request's
folder; the path can use `{{variables}}`. The two forms differ in what they do
when the file already exists:

- `>> file.json` never touches an existing file: it writes `file-1.json`, then
  `file-2.json`, and so on.
- `>>! file` replaces an existing file, but only with a complete, successful
  response. An error status, or a body that was cut short, leaves the file as it
  is. Before replacing, Editora records the previous content in
  [Local History](/docs/undo-history), and it leaves the file alone when the file
  is open in the editor with unsaved changes.

The response view reports what happened to every target: which files were
written or replaced, and which were not and why.

## Tests and response handlers

A request can test its response and keep values from it for later requests,
with `# @assert` and `# @capture` lines or with a JavaScript response handler.
Each test shows in the response view's **Tests** block, with ✓ or ✗ and why it
failed, and a count such as `✓ 3 passed · ✗ 1 failed` sits beside the status
line.

### Checks without JavaScript

`# @assert` and `# @capture` lines go above the request line, with the other
[directives](#directives). They need neither Node.js nor a trusted folder:

```http
### Sign in
# @assert status == 200
# @assert header Content-Type contains json
# @assert $.user.id == 7
# @assert $.token exists
# @capture token = $.token
POST https://api.example.com/login
Content-Type: application/json

{ "user": "ada", "password": "{{password}}" }

### Fetch the profile
GET https://api.example.com/me
Authorization: Bearer {{token}}
```

An `@assert` reads one of these from the response:

- `status`, or `time` (milliseconds);
- `header Name` (the first value; names are case-insensitive);
- `body` (the text);
- a JSONPath into a JSON body: `$.user.id`, `$.items[0].name`, `$["a b"]`.

It compares it with `==`, `!=`, `<`, `<=`, `>` or `>=` (as numbers when both
sides are numbers, otherwise as text), `contains` (text, or an element of an
array), `matches` (a regular expression found anywhere; anchor it with `^…$`),
or checks `exists` / `!exists`. The value is written as it is: `200`, `true`,
`null`, `"quoted text"` or bare text, never a `{{variable}}`.

`# @capture name = subject` keeps `status`, `header Name`, `body` or a JSONPath
value as `{{name}}`. A capture that finds nothing clears the name and says so in
the warnings.

### Response handlers

A JavaScript response handler runs after the response arrives, on your own
[Node.js](https://nodejs.org). Write it after the request, inline or in a file
next to the `.http` file:

```http
### Sign in
POST https://api.example.com/login
Content-Type: application/json

{ "user": "ada", "password": "{{password}}" }

> {%
  client.test("signed in", () => {
    client.assert(response.status === 200, "status " + response.status);
    client.assert(response.body.token, "no token");
  });
  client.global.set("token", response.body.token);
  client.log("token for", response.body.user.name);
%}

> ./checks/login.js
```

Handlers use the JetBrains API:

| Object | What it has |
| --- | --- |
| `client` | `test(name, fn)`, `assert(condition, message)`, `log(…)` (also `console.log`), `exit()`, `global.get/set/clear/clearAll/isEmpty` |
| `response` | `body` (parsed when the response is JSON, otherwise text), `status`, `headers.valueOf(name)` / `valuesOf(name)`, `contentType.mimeType` / `.charset`, `cookies()`, `cookiesByName(name)` |
| `request` | `method`, `url`, `body` and `headers` as sent (`tryGetSubstituted()`, and `getRaw()` as written), `environment.get(name)`, `variables.get(name)` |
| `jsonPath(value, path)` | A value from an object or JSON text: `jsonPath(response.body, "$.items[0].id")` |

A test whose function returns a promise is waited for, so `fetch` can be used
with `await`. An error stops the handler and shows in the Tests block with its
line in the `.http` file (or the handler file), and `client.log` lines show
there too. `xpath`, streamed responses, `client.global.headers` and the
`crypto` helpers are not supported yet.

### Session variables

What `client.global.set` or `@capture` keeps is used by later requests as
`{{name}}`: by the next requests of a **Run All**, or when you run them one by
one, for as long as the file is open. It takes precedence over an environment
variable of the same name, and an `@name = value` line in the file takes
precedence over it. Its value is sent as it is: a `{{…}}` inside it is not
filled in. Session variables are cleared when you pick another environment or
close the file. A `{{name}}` the file sets this way is not underlined as
unresolved.

### What a handler may do

A handler is code that came with the folder, so it only runs from a
**trusted** folder: Editora asks once per run, with **Cancel** as the default
button, and the answer is the same trust that
[secrets](#secrets-and-trusted-folders) and build scripts use. In a folder you
don't trust, handlers are skipped with a warning while `@assert` and `@capture`
still run. A handler file must be in a trusted folder too, and at most 1 MB.

Node.js is started without your environment variables, so a handler can't read a
token from `process.env`. On Node.js 20 or later it runs under Node's permission
model: it can't write files, start processes or use worker threads. A value the
request read from `$processEnv`, `$env` or `$dotenv` reaches the handler masked.
A handler **may** make network requests. It is stopped after 10 seconds.
Handlers in a remote (SFTP) file don't run.

**Settings → Languages & Tools → Web** turns handlers off, picks the Node.js to
use (empty means `node` on the `PATH`) and sets the timeout. The
[Doctor](/docs/troubleshooting#doctor) shows which Node.js runs them and whether the permission
model is on.

## Running and the response

| Action | Command | Default key |
| --- | --- | --- |
| Run the request at the caret | `http.runRequest` | `C-c C-c` (or the gutter ▶) |
| Run every request in the file | `http.runFile` | `C-c C-r` |
| Cancel the running request | `http.cancelRequest` | `C-c C-k` |
| Select environment | `http.selectEnvironment` | (palette) |
| Copy the request as a curl command | `http.copyAsCurl` | (Copy as cURL button) |
| Turn a curl command on the clipboard into a request | `http.importCurl` | (palette) |
| Open the response body in its own tab | `http.openResponseInTab` | (Open in tab button) |

In the other keymaps, Run Request is `Ctrl+Alt+R`, Run All is
`Ctrl+Alt+Shift+Enter` and Cancel is `Ctrl+Alt+K` (`Option+Cmd+R`,
`Option+Cmd+Shift+Enter` and `Option+Cmd+K` on macOS in the CUA, Sublime and VS
Code keymaps). The caret can be anywhere in a request's section, its
`###` line included. **Run File** and the editor's right-click menu on a `.http`
file run its requests too. While a request runs, its ▶ turns into a ■; click it
to cancel.

A `.http` file doesn't have to be saved to send requests. An untitled one uses
the project's environment files, and its responses are kept when you save it. A
request that needs the file's folder (a body file, a `>>` target, `$dotenv`) is
not sent until the file is saved. Requests from a remote (SFTP) file can't run
from this machine yet; Editora says so instead of sending them.

The response appears as the `.http` file's own **preview**, in the same
Editor / Split / Preview view every other rich file type uses, so the response
sits beside the request that produced it. Running a request from the gutter ▶
opens the split automatically, and the view mode is remembered per file. Switch
views with `view.togglePreview` and `view.toggleSplitPreview`, or the floating
3-mode toggle at the top right. Escape in the response view goes back to the
editor, and the response, headers and Request text answer your keymap's
movement and copy keys (`M-w`, `C-n`, `M->` in Emacs).

The response view shows:

- a status line such as `200 OK · 32 ms · 44 B`, coloured by outcome, and,
  while a request runs, its request line with variables filled in and the
  elapsed time. Run All shows its progress ("2 of 5"), says how many requests'
  tests failed, and ends on the first failed request, or else on the first
  whose tests failed.
- a **Request** section with the request as it was sent, variables filled in,
  and the address a redirect ended at.
- a **Tests** block with the request's [tests](#tests-and-response-handlers), the
  handlers' log and any error.
- warnings in their own block, with a count beside the status line (unresolved
  variables, skipped scripts, headers that couldn't be sent, a redirect to
  another host that carried custom headers) and the files `>>` wrote.
- the protocol version, the headers, and the body: JSON pretty-printed,
  one-line XML indented, images shown as pictures, other binary bodies
  summarised. gzip and deflate responses are decoded, and **Wrap** wraps long
  lines.

The **Run** button sends the request the selected response came from again,
found by its name (or its method and URL) even if the file changed since; it
turns into **Cancel** while a request runs. Cancelling a Run All keeps the
responses that had already arrived. A request that was refused or couldn't be
sent shows **Not sent**, and Copy as cURL is off for it. Errors read as sentences, such as "Connection refused — is
a server listening on …?" or "Host not found". Responses over 50 MB are cut, and
the view says so.

**Copy as cURL** produces a command for bash or zsh that is safe to paste and
reproduces what was sent, including file bodies, forms, Digest auth and
redirects, multipart part types and the HTTP version, also for a response
restored from history. **Import cURL** reads a curl command from the clipboard
(a browser's "Copy as cURL", `curl.exe` or a `$ curl` line from a README) into the active `.http` file, or into a new tab when
no `.http` file is active; body lines the format would not send (from a `>`,
`<>` or `###` line on) become comments, so a pasted command never adds a
handler. **Save response** writes the body exactly as
received (an image saves as the image); the status-and-headers text is the
second choice in the dialog.

### History

The **History** dropdown holds the file's recent responses, each with its
request's name and time. The last 10 are saved in Editora's config folder and
come back when you reopen the file. Saved bodies are capped at 1 MB, and the
values of credential headers (Authorization, cookies, and headers named like
tokens, secrets, API keys and passwords) are masked, and so is any value a run
read from `$processEnv`, `$env` or `$dotenv`. A request marked
`# @no-log` is never saved. **Clear** empties the file's history, saved copy
included. The history follows a renamed or moved file, is removed with a deleted
one, and is shared by every window showing the file. **Export Configuration**
includes it only when you tick "Include HTTP response history".

Cookies last for one run: Run All shares them between its requests, and a
request run on its own starts without any.
