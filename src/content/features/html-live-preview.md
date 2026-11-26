---
title: "HTML live preview"
group: "Docs & diagrams"
order: 4
beta: false
summary: "Click the globe on an HTML file to open it in your browser, served with the rest of its site and reloaded as you type, before you save."
description: "Open an HTML file in your browser from a loopback-only server that serves the whole site and reloads the page as you type, before you save."
alt: "An HTML file in the editor, with the globe button at the top right that opens its live preview in a browser"
---

See your page in a real browser while you write it: click the globe and the page opens, then follows every keystroke without a save or a manual refresh.

- **Open in your browser.** The globe at the top right of any `.html`, `.htm` or `.xhtml` file opens it in Safari, Chrome, Firefox, Edge or the system default. **HTML Preview: Open in Browser…** picks another.
- **Reload as you type.** The browser shows the text in the editor, saved or not. Editing a stylesheet swaps it in without a reload.
- **The whole site loads.** Stylesheets, scripts, images, root-relative `/…` links and folder links all work, served from the project folder or the page's site folder.
- **Private by default.** The server answers only this machine, each site gets its own address, and hidden files, private keys, credential stores, databases and encrypted files are never served.
- **Nothing to install.** The server is built into Editora.

**On by default.** Turn it off, or choose the browser, in **Settings → Web → HTML Preview**. See the [HTML live preview guide](/docs/html/#live-preview).
