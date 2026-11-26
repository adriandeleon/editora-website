---
title: "Project Map"
group: "Workspace & files"
order: 0
beta: false
summary: "See a project as a <strong>map</strong>: folders as columns on a canvas, code you follow as connected cards, branch review, dependencies and hotspots, and trails that walk a team through the code."
description: "See a project as a map in its own window: folder columns on a canvas, code cards, callers and hierarchies as connected cards, hotspots and trails."
alt: "The Project Map: folder columns joined by curves, a Java file selected and its Structure column"
---

---
---
The Project Map lays a [project](/features/projects/) out as folders on a canvas, with the code open in cards beside them, so you can explore a codebase, follow a call chain or review a branch without losing your place. It opens in a **window of its own**: `C-c o m` in the Emacs keymap, `Alt+Shift+1` in the others (`Cmd+Shift+1` on macOS), or the **Map** button in the Project tool window.

## Browse the folders

- **Columns** open a folder's children in the next column, and the path that led there stays on screen.
- **Single-child chains** such as `src/main/java/com/acme` open as one column.
- **Pan and zoom** with a drag and the wheel. Zoomed out, columns turn into labelled blocks, so a big project still fits.
- **Reveal** the file you are editing with `C-c o r` in the Emacs keymap, `Alt+F1` in the others.
- **Remembered places**: open folders, selection, zoom and cards come back after a restart.

## Read code in cards

![A read-only preview card for Shelf.java on the Project Map, opened past the file's column and joined to its row by a connector, showing syntax-highlighted Java, with the file's last change and the card's buttons in its header](/screenshots/docs/project-map-card.png "A preview card opens past the file's column, joined to its row.")

- **Preview cards** open from a file's **eye**: read-only, highlighted like the editor, unsaved text included, and joined to their row.
- **Declaration cards** show one class or method, picked from a file's **Structure** column.
- **Like the editor**: semantic colours, inlay hints, problems and usage counts, and a gutter that sets breakpoints and runs tests.
- **Personal notes** sit beside the lines they belong to; select lines in a card to add a [Personal Note](/features/bookmarks-notes/).

## Follow the code

![Show Callers as Cards on Pricing.apply: the callers in Shelf.checkout and two ShelfTest tests open as cards around Pricing.java, each joined to it by a dashed edge labelled by name](/screenshots/map/references.png "With no language server running, the callers are found by name, and the edges say so.")

- **Go to Definition and Find Usages** inside a card answer with more cards, each joined by a labelled edge.
- **Callers, callees and references** open as cards around a declaration.
- **Hierarchies and the Symbol Graph** show a method's callers, callees, supertypes and subtypes as cards to walk.
- **No language server needed**: a running [language server](/features/lsp/) answers; without one, Editora matches names and draws those edges dashed.

## Review and understand a codebase

![Show recent changes on the Editora repo: rows tinted and underlined by how often they changed, with a tooltip reading 5 commits in 90 days, last: Adrian De Leon, 2 weeks ago, and Owners: @adriandeleon from .github/CODEOWNERS](/screenshots/map/recent-changes.png "Recent-change heat, with the commit count, the last author and the owners in the tooltip.")

- **Review Changes** shows every changed file and its diff in one card, or everything your branch changes, as a pull request does.
- **Dependencies** shows what a file or folder uses and what uses it, read from import lines with no build.
- **Code Metrics** scores hotspots: code that is both complex and often changed.
- **Recent changes** tints each row by how often it changed in 90 days, and names its owners from `CODEOWNERS`.
- **Project overview** sums up the README, build, languages, entry points and tests on the first Map of a session.

## Search and filter

![Git changed with Hide unmatched on: only the changed files and the folders that lead to them stay, with a 65 rows hidden badge, shown/total counts on each column and an N hidden line under each](/screenshots/map/filters.png "Git changed with Hide unmatched: only the changed files and their folders stay, and every column counts what it hid.")

- **Search the whole project** by name or path, `@` for declarations or `%` for text.
- **Filters** keep only open, unsaved, Git-changed, bookmarked, noted or problem files. **Hide unmatched** takes the rest away.

## Save views and share tours

![A trail playing: the bar reads Stop 2 of 4, Pricing.discountFor, the card highlights that method's lines and the stop's note is stuck beside it](/screenshots/map/trail.png "A trail at its second stop: the card shows the stop's lines, with its note beside it.")

- **Working sets** bring back exactly the view you saved.
- **Trails** are guided tours: cards as stops, a note at each, played with `Ctrl+Right` (`Cmd+Right` on macOS). They live in `.editora/map-trails/`, so the team can commit them.
- **Print, PDF and Export** output the whole Map.
- **Keyboard**: every row and button is a command you can bind to a key.

See the [Project Map guide](/docs/project-map/).
