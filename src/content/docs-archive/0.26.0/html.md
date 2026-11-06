---
title: HTML
description: Tag auto-close and rename, comments and indentation inside style and script, links you can follow, markup checks, templates, the HTML language server, and a live preview in your browser.
category: Editing
order: 3.5
---

Files ending in `.html`, `.htm` and `.xhtml` open as HTML. Most of what this
page describes works out of the box: highlighting, tag editing, comments,
folding, Structure, spell checking, snippets, following links, markup checks
and Format Document need nothing installed. The
[HTML language server](#the-html-language-server) adds completion, hover,
problems in embedded CSS and JavaScript, and its own formatter. The
[live preview](#live-preview) opens the page in your browser and reloads it as
you edit.

## Opening HTML files

Besides the three extensions, a file whose name says nothing (no extension)
opens as HTML when it starts with `<!doctype html` or an `<html>` tag, in any
case, after an optional byte-order mark, blank lines and `<!-- -->` comments.
A `.txt` file stays plain text.

**Templates** open as HTML too:

- Jinja, Nunjucks, Twig, Liquid, Mustache, ERB and EJS files (`.html.erb`,
  `.erb`, `.ejs`, `.njk`, `.jinja`, `.jinja2`, `.j2`, `.twig`, `.liquid`,
  `.mustache`) are **HTML Template** in the status bar.
- Handlebars files (`.hbs`, `.handlebars`) are **Handlebars**.

Both get the HTML colours, tag auto-close and rename, folding, Structure,
spell checking, the HTML snippets and `<!-- -->` comments. The HTML language
server is never started for an HTML Template, so template syntax is not
reported as errors or reformatted; Handlebars files do get the server's
completion, hover and formatting. A template named after the file it renders,
such as `nginx.conf.j2`, `config.yml.erb` or `theme.css.liquid`, opens in that
file's language instead, so an Ansible or Chef template is not called HTML.
`page.html.erb` stays an HTML Template. To pick a language yourself, click the
language in the status bar (see [Languages](/docs/languages#when-the-file-name-says-nothing)).

## Editing tags

- **Auto-close.** Typing the `>` that ends a start tag inserts its end tag
  after the caret: `<section>` gives `<section>|</section>`. Void elements
  such as `<br>`, `<img>` and `<input>` get none.
- **Typing the end tag anyway.** If you type `</section>` by habit, the `/`
  joins the end tag that is already there and the rest steps over it, so you
  do not get a second one. Only the end tag of the element you are closing is
  joined: typing `</b>` inside `<p>Hello <b>world|</p>` writes `</b>` before the
  `</p>`. Tag names match in any case. No completion list pops up while you
  type over it.
- **Auto-rename.** Editing a start tag's name renames its end tag, and the
  other way round.
- **Attribute quotes.** In a start tag, typing `=` straight after an attribute
  name inserts `""` with the caret inside: `<a class=` gives `<a class="|">`. If
  you type the `"` yourself next, it is taken as the opening quote, so typing
  `class="x"` still gives `class="x"`. The quotes are always double. This is
  part of auto-close, and HTML only.

Auto-close and auto-rename are on by default. Turn them off with **Auto-close
HTML/XML tags** and **Auto-rename paired HTML/XML tag** in Settings → Editor →
Editing, or with *View: Toggle Auto Close Tags* (`view.toggleAutoCloseTags`)
and *View: Toggle Auto Rename Tag* (`view.toggleAutoRenameTag`).

## Comments and indentation

**Edit: Toggle Comment** (`edit.toggleComment`; `M-;` in the Emacs keymap,
Ctrl+/ in the others, ⌘/ on macOS) uses the syntax of where the selection is:

- `<!-- -->` in the markup,
- `/* */` inside a `<style>`,
- `//` inside a `<script>`.

On a line that holds the tags as well, such as `<script>init()</script>`, only
the code is commented, with `/* */`. A selection that runs from the markup into
a script or style is refused, and the status bar says *The selection runs from
the HTML into a script, style or PHP block: comment each part on its own*. The
content of `<script type="text/x-template">` counts as markup. In a PHP file
the HTML part is commented with `<!-- -->`.

**Enter** after a start tag indents the next line. Inside a `<script>` or
`<style>`, Enter follows the brace rules instead: it indents after `{`, and a
typed `}` lines up with its opener.

## Folding and Structure

Elements that span several lines fold. Without the language server, Editora
folds them itself: tag names match in any case (`<DIV>`…`</div>`), void
elements open nothing, and the content of `<script>` and `<style>` is skipped,
so a script holding `a < b` does not break the folds below it. With the server
running, its folding ranges are used (see
[Folding and selection](/docs/lsp#folding-and-selection)).

**Structure** outlines the page:

- **With the server**, Structure, **Structure: Jump…** and the breadcrumbs list
  every element, repeated siblings included (every link of a `<nav>`, a second
  `<script>`), and the functions of every inline script.
- **Without it**, Editora labels elements the way the server does
  (`header#hero.card`, tag then `#id` then `.class`) and lists the elements that
  span several lines, plus one-line headings (`<h1>` to `<h6>`) and elements
  with an `id`. Omitted end tags, void elements and script content are handled.
  The XML server's **Structure element limit** (see
  [XML settings](/docs/lsp#xml-settings), 5,000 by default) caps how many are
  listed.

## Spell checking

In an HTML page the text visitors read is checked, and comments. Tags and
attribute names are not, and neither are most attribute values. The values
visitors do read are checked: `alt`, `title`, `placeholder`, `aria-label` and
`aria-description`. The content of `<script>`, `<style>`, `<pre>`, `<code>`,
`<kbd>`, `<samp>` and `<var>` is skipped, while the rest of a line that holds
an inline `<script>` is still checked. A tag mentioned inside a comment does
not turn checking off. See [Spell checking](/docs/languages#spell-checking).

## Snippets and new files

The HTML snippets cover the elements by name (`div`, `ul`, `table`, `form`,
`video`…), with `div.`, `div#` and `div.#` (and the same for `p`, `ul` and `ol`)
for an element with a class, an id or both. `!` and `html5` insert a whole
page, with the `lang` value (`en`) selected first (`html5` also links a
stylesheet); `doctype` inserts `<!DOCTYPE html>`; `link` is a stylesheet link.
Obsolete elements are not offered.

Which snippets are offered follows where you are typing: CSS snippets inside a
`<style>`, JavaScript snippets inside a `<script>`, the HTML snippets in the
markup, and none inside a tag, so `col` in a stylesheet offers `color` rather
than `<col>`. HTML Templates and Handlebars files get the HTML snippets. See
[Snippets](/docs/snippets-templates#html-snippets).

**New File From Template** (`template.new`) has **HTML Page** (a minimal
HTML5 page) and **HTML Page + CSS** (the page and a stylesheet it links), both
with `<!DOCTYPE html>`, `UTF-8` and the viewport meta. **File: New File of
Type…** (`file.newFileOfType`) → **HTML** starts the same kind of page.

## Following links

**Go to Definition** (`lsp.gotoDefinition`: `M-.` in the Emacs keymap, F12 in
the VS Code, CUA and Sublime keymaps, Ctrl+B in IntelliJ's) or **Ctrl/Cmd-click**
on an `href`, `src`, `poster`, `action` or similar attribute opens the file it
names. This works with or without the HTML language server, and with LSP off.

- A link with a `#fragment` opens the file at the element whose `id` (or `<a
  name>`) matches. A link that is only `#top` moves within the page. A fragment
  that names no element says *No element with id "top"*.
- A link to a folder opens its `index.html`. The page's `<base href>` is
  followed, a `?query` is dropped, `%20` and other escapes are decoded, and a
  root-relative link (`/css/site.css`) is resolved against the project (or the
  server's workspace root).
- A web address asks **Open https://… in the browser?** with **Open**, every
  time, and then opens in your browser.
- A link to a file that does not exist says *File not found: missing.html*.
  `mailto:`, `javascript:` and template values (`{{ url }}`) say *Nothing to
  open for …*.

## Markup checks

Editora checks an HTML page's markup itself and marks three things as
warnings:

- an element that is never closed: *Element \<div\> is not closed*;
- an end tag that closes nothing: *End tag \</span\> closes no open element*;
- a link to a local file that does not exist: *File not found: missing.html*.

Elements whose end tag is optional (`<li>`, `<p>`, `<td>`, `<option>` and the
others HTML allows), void elements, self-closing tags, comments and the content
of `<script>` and `<style>` never count. The missing-file check looks only at
links whose last part has an extension, so routes like `/about` and `docs/`
are not flagged, and skips web addresses, form `action`s and `<base>`. It runs
only for pages on this machine.

With the HTML language server, these warnings join the server's own
diagnostics in the Problems window and the problem counter, with `editora` as
their source. Without it, they show as squiggles in the page, with the message
on hover, but not in the Problems window.

The checks apply to `.html` and `.htm` files, not to `.xhtml` or templates. They
are on by default; turn them off with **HTML markup checks** in Settings →
Editor → Editing.

## Related File

**Go: Related File** (`nav.relatedFile`) from a page offers the stylesheets and
scripts it loads (`<link href>`, `<script src>`) first, the ones that exist.
From a `.css`, `.js` or `.mjs` file it offers the project's pages that load it.
That direction reads the project's indexed pages, so it needs the
[symbol index](/docs/code-navigation#go-to-symbol-in-project). See
[Go to Related File](/docs/code-navigation#go-to-related-file).

## Formatting

**Format Document** (`lsp.formatDocument`: `C-c l f` in the Emacs keymap,
Shift+Alt+F in the VS Code, CUA and Sublime keymaps, Ctrl+Alt+L in IntelliJ's)
formats an HTML file with or without the language server.

**With the server**, its formatter is used, with the settings on the
[HTML card](#settings--lsp--html):

- The content of `<pre>`, `<code>` and `<textarea>` is left exactly as written,
  so code samples and a textarea's default value keep their spaces. Scripts are
  always kept.
- The file's final newline is kept, unless its `.editorconfig` says
  `insert_final_newline = false`.
- Long lines wrap at the file's EditorConfig `max_line_length` (never, for
  `off`), else at **Wrap lines at column** (120 by default).
- The indentation is the file's own: EditorConfig's `indent_style` and
  `indent_size`, else what the file already uses.

**Format Selection** formats only the selected lines. When the server times
out, stops or refuses, the status bar says *Format failed: …* with the reason
rather than *Already formatted*.

**Without the server** (LSP off, or no HTML server running), Editora's own
formatter puts one block element per line and indents by nesting, in the
file's indent unit. It keeps the content of `<pre>`, `<textarea>`, `<script>`
and `<style>` exactly, keeps your line breaks around text, and keeps up to one
blank line in a row. It says *Formatted with the built-in HTML formatter*, or,
when a tag, comment, script or `<pre>` is left open, *Not formatted: a tag,
comment, script or \<pre\> is left open* and changes nothing. It formats plain
HTML only, not templates, whose syntax it would reflow.

## The HTML language server

The HTML server is **vscode-html-language-server**, from the
**vscode-langservers-extracted** npm package: the HTML, CSS and JSON servers
taken out of VS Code. One install gives all three
(`vscode-css-language-server` and `vscode-json-language-server` too). It is a
Node.js program, so it needs **Node.js**.

Language servers are off by default. The first HTML (or CSS) file a window
opens asks **Turn on HTML code intelligence?**, with **Turn on** and **Not
now**. To turn them on yourself, use **Enable LSP (language servers)** in
Settings → LSP. See [Language servers](/docs/lsp).

The server reads `.html`, `.htm` and `.xhtml` files and Handlebars files, not
HTML Templates. It runs once per workspace root: the project, else the nearest
folder with a `package.json` or `.git`. A loose page of a static site with
neither is rooted at the highest folder above it that holds an `index.html`
(stopping below your home folder), so its `/css/…` links resolve. HTML files
keep their server up to 50,000 lines.

### Installing it

**Install** on the banner of an HTML file, **Install…** on the HTML card in
Settings → LSP, Doctor's **Install…**, or **Install: Language Server…**
(`install.languageServer`) runs npm to put **vscode-langservers-extracted
4.10.0** into Editora's own folder, `plugins/lsp/web` under the
[config folder](/docs/configuration), not `npm install -g`. 4.10.0 is the
version Editora is tested with, not npm's latest. Only Node.js and npm are
needed. The banner says what Install does before you click:

> The HTML language server isn’t installed. Install adds
> vscode-langservers-extracted 4.10.0 with npm (it needs Node.js) to Editora’s
> own folder; the one package brings the HTML, CSS and JSON servers.

When npm is not found, the banner says so before you click, and asks you to
install Node.js first (or start Editora where it is on your `PATH`).

The install also links the TypeScript libraries the HTML server looks for, so
JavaScript in a page gets the DOM and the standard library. A copy installed
with npm on its own (`npm install -g vscode-langservers-extracted`) cannot load
them: nothing completes after `document.` and there is no parameter help.
[Doctor](/docs/troubleshooting#doctor)'s HTML row warns about such a copy and
offers **Update**.

With the HTML, CSS and JSON commands left blank in Settings → LSP, Editora
runs its own copy first, then one on your `PATH`. A command you set always
wins. The server runs on the Node.js chosen in Settings → Debugging →
JavaScript (Node.js) → **Node.js**; left blank, that is the version a
project's `.nvmrc` or `.node-version` names, else `node` on your `PATH`.

### What the server adds

- **Completion** of elements, attributes and their values, with each item's
  MDN documentation beside the list as formatted text with a working **MDN
  Reference** link. CSS in a `<style>` or `style=""` completes like a
  stylesheet, and JavaScript in a `<script>` or `on…=""` attribute gets the DOM
  and standard library: completion after `document.`, parameter help and
  hovers. With auto-close on, the list does not offer the end tag Editora has
  already inserted. **Enter** and **Tab** accept the selected item while the
  documentation shows; links in it open with a click.
- **Two-step completions.** Accepting a CSS property opens the list of its
  values, and accepting a folder in a path opens the list of its files.
- **Hover** documentation for elements, attributes and CSS properties, opening
  at the start of its text.
- **Problems** in embedded scripts and styles (each can be switched off, below),
  with Editora's [markup checks](#markup-checks) added.
- **Colour swatches.** A colour in a `<style>`, a `style=""` attribute or a CSS
  file shows a small square in its colour before it. There is no colour picker.
- **Structure** of every element, as [above](#folding-and-structure).
- **Format Document** and **Format Selection**, as [above](#formatting).

### Settings → LSP → HTML

The HTML card in Settings → LSP has the server's switch, its command and
**Detected** line, and two groups of the server's own settings:

| Setting | Default | What it does |
| --- | --- | --- |
| **Formatting (HTML server)** | | |
| Wrap lines at column | 120 | Format Document wraps lines longer than this; 0 never wraps. A file's EditorConfig `max_line_length` wins |
| Attributes when formatting | Wrap only when the line is too long | How an element's attributes are laid out: also *One per line after the first*, *One per line, aligned with the first*, *One per line, each on a new line*, *Wrap when too long, aligned with the first*, *Keep as written* and *Keep as written, aligned with the first* |
| Keep content as written in | `pre`, `code`, `textarea` | The elements whose content Format Document leaves exactly as written, separated by commas. Scripts are always kept |
| **Completion and embedded code (HTML server)** | | |
| Quotes for completed attributes | Double quotes | What an attribute chosen from completion gets after its `=`: also *Single quotes* or *No quotes*. Typing `=` yourself always inserts double quotes |
| Check embedded scripts | On | Report problems in the `<script>` elements of an HTML file |
| Check embedded styles | On | Report problems in the `<style>` elements of an HTML file |

A project can set them for everyone who opens it, with an `html` object in its
`.editora/settings.json` (see
[Configuration](/docs/configuration#inside-a-project)). It uses the names of
VS Code's `html.*` settings, without the `html.` prefix:

```json
{
  "html": {
    "format": { "wrapAttributes": "force-aligned", "wrapLineLength": 100 },
    "validate": { "scripts": false }
  }
}
```

The keys are a `format` object (`wrapLineLength`, `wrapAttributes`,
`contentUnformatted`, `unformatted`, `indentInnerHtml`, `preserveNewLines`,
`maxPreserveNewLines`, `indentHandlebars`, `extraLiners`, `templating`,
`unformattedContentDelimiter`, `endWithNewline`, `wrapAttributesIndentSize`,
`indentScripts`), `validate` (`scripts`, `styles`), `suggest` (`html5`),
`hover` (`documentation`, `references`), `completion`
(`attributeDefaultValue`) and `autoClosingTags`. A key Editora does not know,
or a value of the wrong type, is ignored, and a key the project leaves out keeps
your own setting. `customData` and `dataPaths` are not supported. Nothing in
the object runs a program, so it applies in untrusted folders too. It applies
only to the project's own files: a page from another folder opened in the same
window keeps your settings. A file's EditorConfig `max_line_length` and
`insert_final_newline` still win.

### What it does not do

- **No markup validation.** The server reports no unknown elements or
  attributes, wrong nesting or broken anchors; its only problems come from
  embedded `<style>` and `<script>`. Editora's own
  [markup checks](#markup-checks) cover unclosed elements, stray end tags and
  missing local files.
- **No Emmet.** Abbreviations such as `ul>li*3` are not expanded. Use the
  [snippets](#snippets-and-new-files) instead.
- **No templates.** HTML Templates (Jinja, Twig, ERB…) are not sent to the
  server.

## Live preview

On an HTML file, a floating **globe** button at the top right of the editor
opens the page in a browser, served by a small web server inside Editora that
only this machine can reach. The page reloads as you edit.

- **HTML Preview: Open in Browser** (`htmlPreview.open`) opens the page in the
  browser you used last, or the one chosen in Settings.
- **HTML Preview: Open in Browser…** (`htmlPreview.openIn`) picks one of the
  detected browsers.
- The preview is **on by default**. Turn it off with **Enable HTML Live
  Preview** on the **HTML Preview** card in Settings → Web, or with *View:
  Toggle HTML Live Preview* (`view.toggleHtmlPreview`). **Open the preview in**
  on the same card chooses the browser (the system default until you choose
  one).

The globe shows on `.html`, `.htm` and `.xhtml` files and on a file that opens
as HTML from its first lines or its modeline, not on templates. A new, untitled
page must be saved once, so its stylesheets and images have a folder to load
from: *Save the HTML file first so its assets can load.*

### What is served

The preview serves the page's whole **site**, so `../css/site.css` and
root-relative `/img/logo.png` links load:

- for a file in the project, the project folder;
- otherwise, the nearest folder above the file that holds an `index.html`;
- otherwise, the file's own folder.

Some files are never served, whatever the page asks for: hidden files and
folders (`.env`, `.git/`, `.ssh/`, `.npmrc`…, except `.well-known`) and private
keys (`*.pem`, `*.key`, and `id_rsa`-style files without an extension or ending
in `.pub`). A page sitting directly in your home folder, Downloads, Desktop,
Documents or the temporary folder, or at the top of a drive, is refused, because
the whole folder would be served: *Not serving …: the preview server exposes the
file's whole folder. Move the HTML file into its own folder first.* When the
project is your home folder, the rules for a loose file apply instead.

Video and audio play and seek (Safari included), because files are streamed
with byte ranges; big files are not loaded into Editora's memory. Each page
keeps its own character encoding.

### Live reload

The page reloads when you edit it, when you save it, and when you edit or save
any other file of its site that is open in Editora: a stylesheet, a script,
another page. Unsaved edits are served as you type. An edit to a stylesheet
only is applied without a reload, so the page keeps its scroll position and
state. Closing a file serves it from disk again. Files changed outside Editora,
such as a build tool writing `dist/`, are not watched.

Several sites can be previewed at once, each under its own address, and
previewing a page of a site already served reuses it. Each **Open** opens a new
browser tab. The tabs keep working until the server stops: when the preview is
turned off or the window closes. The address changes every time the server
starts. A tab whose server has stopped backs off and then says *Editora's
preview server has stopped. Open the preview again from Editora.*

### Browsers

Editora detects Safari, Google Chrome, Chromium, Firefox, Firefox Developer
Edition, Microsoft Edge, Brave, Vivaldi, Opera and Arc. On Linux it also finds
Flatpak and Snap installs and any other browser with a desktop launcher; on
Windows, per-user installs too. A browser installed later shows up without
reopening the window. A remembered browser that is no longer installed shows as
*Brave (not found)* in Settings → Web. Doctor's **Web Browsers** row lists what
was found.

### Status messages

The status bar says what happened:

- *Opening in Firefox…*, then *HTML preview at http://127.0.0.1:…*.
- *Couldn't start the preview server: …* for a server problem.
- *Couldn't open the browser: …* for a browser that could not be started.
- *Brave wasn't found, so the default browser opened the preview at …* when the
  chosen browser has gone.
- *Firefox quit right away (exit code 1). The preview is at …*, with the
  address to open by hand.

### Remote files

The preview serves local files only. A file on an [SFTP host](/docs/remote) has
no globe, and the HTML Preview commands say *HTML Live Preview serves local
files only; this file is on a remote host.*
