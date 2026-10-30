---
title: "HTML live preview"
group: "Docs & diagrams"
order: 4
beta: false
summary: "Click the globe on any HTML file to open it in a detected browser (Safari, Chrome, Firefox, Edge…), served over a loopback web server with live-as-you-type reload. The whole site's CSS, JS, and images included."
---

A floating browser-globe button on any `.html`/`.htm`/`.xhtml` file opens it in a **detected browser**: Safari, Chrome, Firefox, Edge, or the system default.

The page's whole site is served over a tiny embedded web server bound to **loopback only** (the project folder, or the nearest folder with an `index.html`), so its CSS, JS, images and `/…` links load, and a small injected script **reloads the page live as you type** (it serves the buffers' in-memory text, so you don't have to save). Editing a stylesheet of the site swaps it in without a reload. Hidden files and private keys are never served. No external tool: it uses the JDK's built-in HTTP server.

On by default. Toggle it and choose the browser under Settings → Web → HTML Preview. See the [HTML docs](/docs/html#live-preview), or read the [deep-dive](/blog/html-live-preview).
