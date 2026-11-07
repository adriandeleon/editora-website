---
title: "Project Map"
group: "Workspace & files"
order: 0
beta: false
summary: "See a project as a <strong>map</strong> in a window of its own: folders as columns on a canvas, code cards that open the way the map flows, callers and references as connected cards, recent-change heat, and trails that walk a team through the code."
alt: "The Project Map: folder columns joined by curves, a Java file selected and its Structure column"
---

The Project Map is a second way to look at a [project](/features/projects), beside the file tree. It opens in a **window of its own** (place it next to the editor window, or on another screen) and lays the project out on a canvas you can pan and zoom. Open it with the **Map** button in the Project tool window, `Alt-Shift-1` (`Cmd-Shift-1` on macOS, `C-c o m` in Emacs), or *Project: Open / Close Project Map Window*. `Alt-F1` reveals the file you are editing on it.

## Columns on a canvas

Select a folder and its children open in the next column, Miller-style, while the path that led there stays on screen. Several branches sit side by side without mixing. A chain of single-child folders such as `src/main/java/com/acme` opens with one click as **one column**. The map flows left to right by default; ⋯ → **Flow** turns it right to left, top to bottom or bottom to top, and the connectors and arrow keys follow.

Drag to pan and use the wheel to zoom around the pointer, from 15% to 225%. Below 60% the columns become labelled blocks, so even a big map fits on screen and the matches still stand out. **Fit**, **Center selection**, the corner overview and **Collapse All** bring you back to the path you are on. Ignored, generated and vendored files are drawn dimmed, and one option takes them off the map.

A local folder is listed whole, up to 50,000 entries, so a folder of thousands of files scrolls end to end. The Map remembers what you explored across restarts: the open folders, the selection, Back and Forward, the zoom and the code cards.

## Cards for a glance

Click a file's **eye** for a read-only, syntax-highlighted **preview card** over the canvas, in the editor's font and with any unsaved text. A card opens the way a new column does, past its row's column the way the map flows, and the map pans to centre it with its row still in sight. A live connector keeps it tied to its row as you pan, zoom, move or resize it. **Open** takes the file to the editor.

![A read-only preview card for Shelf.java on the Project Map, opened past the file's column and joined to its row by a connector, showing syntax-highlighted Java, with the file's last change and the card's buttons in its header](/screenshots/docs/project-map-card.png "A preview card opens past the file's column, joined to its row.")

A file's **Structure** icon opens a column of its classes, methods or headings, and clicking a declaration opens a card of **only its lines**, with **‹ ›** to step through the file's declarations. Up to eight cards stay open at once. **Pin** the ones you want to keep, find text inside a card with `Ctrl-F`, and read who changed the code last on the card's status line. Select lines in a card to add a [Personal Note](/features/bookmarks-notes); a card's notes stick beside it as small editable sheets, each tied to its line.

## Follow the code without leaving the map

![Show Callers as Cards on Pricing.apply: the callers in Shelf.checkout and two ShelfTest tests open as cards around Pricing.java, each joined to it by a dashed edge labelled by name](/screenshots/map/references.png "With no language server running, the callers are found by name, and the edges say so.")

Right-click a declaration, or a name in a card, and choose **Show Callers as Cards** or **Show References as Cards**. The places found open as cards around it, joined by edges that say how: *calls*, *references* or *by name*. A [language server](/features/lsp) that is already running answers, even for files that are not open; with none running, Editora matches the name across the project instead and draws those edges dashed. **Related Test / Subject** jumps between a class and its test, and Go to Definition, Find Usages and the hierarchies work from a focused card.

## Search and filter

![Git changed with Hide unmatched on: only the changed files and the folders that lead to them stay, with a 65 rows hidden badge, shown/total counts on each column and an N hidden line under each](/screenshots/map/filters.png "Git changed with Hide unmatched: only the changed files and their folders stay, and every column counts what it hid.")

The Map window has its own search over **the whole project**, however large: type a name, part of a path (`core/v1/types.go`), `#` for files only or `@` for declarations. A results strip counts the matches and steps through them, and clearing the search keeps your place. The filter row narrows the map to open, unsaved, Git-changed, bookmarked or noted files and by file type, fading everything else; **Hide unmatched** takes the rest off the map and counts what it hid.

## See where the work happens

![Show recent changes on the Editora repo: rows tinted and underlined by how often they changed, with a tooltip reading 5 commits in 90 days, last: Adrian De Leon, 2 weeks ago, and Owners: @adriandeleon from .github/CODEOWNERS](/screenshots/map/recent-changes.png "Recent-change heat, with the commit count, the last author and the owners in the tooltip.")

⋯ → **Show recent changes** warms each row by how often it changed in the last 90 days, and its tooltip says how many commits touched it and who changed it last. With a `CODEOWNERS` or Kubernetes-style `OWNERS` file, every row's tooltip names its owners and reviewers, read from the project's own files with no network. The first time you open a project, a **Project overview** card shows its README, build system and modules, languages, likely entry points, where the tests live, the most-changed folders and the owners.

## Working sets and trails

![A trail playing: the bar reads Stop 2 of 4, Pricing.discountFor, the card highlights that method's lines and the stop's note is stuck beside it](/screenshots/map/trail.png "A trail at its second stop: the card shows the stop's lines, with its note beside it.")

Save the map as a **working set** (open folders, selection, zoom, cards and pins) and pick it to come back to exactly that view. A **trail** is a guided tour of the code: add cards as stops, write a note at each, then play it stop by stop with `Ctrl-Right` / `Ctrl-Left`. Trails live in the project's `.editora/map-trails/` folder, so the team can commit them and everyone gets the same tour of a new codebase.

## Keyboard, print and export

Everything works from the keyboard: arrows follow the flow, typing a name jumps to it, `/` filters a column, `Alt-Left` / `Alt-Right` step back and forward through the places you visited, and `Alt`+arrows move a focused card. **Print…** and **PDF…** output the whole map, split across pages when it is large, and the PDF is searchable; **Export** saves it as a PNG or an SVG. Screen readers read the rows on screen as a tree.

See the [Project Map docs](/docs/workspace#project-map) for every option and key.
