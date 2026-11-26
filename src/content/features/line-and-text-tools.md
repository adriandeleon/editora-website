---
title: "Line commands and text tools"
group: "Editing"
order: 8
beta: false
summary: "Insert, delete, select and indent whole lines at every cursor; sort, filter and de-duplicate lines; encode, decode and escape the selection; increment the number at each cursor."
description: "Line commands that run at every cursor, plus sorting, filtering, case conversion, Base64 and URL encoding, and number increments."
alt: "Twelve selected Map.entry lines in a Java file with the String Manipulation picker filtered to sort"
---

The small edits you make all day, as commands that work at **every cursor** and undo in one step.

## Line commands

- **Insert Line Below** (`Ctrl+Enter`) opens a new, indented line under the current one wherever the cursor is on it; **Insert Line Above** (`Ctrl+Shift+Enter`) opens one above.
- **Delete Line** (`Ctrl+Shift+K`, `Ctrl+Y` in the IntelliJ keymap) deletes the line, or every line a selection touches, and keeps the cursor's column. It leaves the clipboard and the kill ring alone.
- **Select Line** (`Ctrl+L`) selects the whole line; press it again to take the next one.
- **Indent Lines** / **Outdent Lines** (`Ctrl+]` / `Ctrl+[`) shift the line by the file's indent unit, wherever the cursor sits.
- **Duplicate Line** and **Move Line Up / Down** work on the line or the selected block.

A collapsed fold is opened before a line command changes it, and an ordered Markdown list it changes is numbered in sequence again. The chords above are the VS Code keymap's (`Cmd` on macOS). Sublime Text uses the same ones, CUA shares Insert Line and Delete Line, and IntelliJ binds its own chords for those two. In the default Emacs keymap these line commands run from the palette; every chord is on the [keybindings](/keybindings/) page.

## String manipulation

**Edit: String Manipulation…** (`C-c x` in the Emacs keymap) is one filterable picker over all of these:

- **Case**: camelCase, PascalCase, snake_case, SCREAMING_SNAKE_CASE, kebab-case, dot.case, swap case, and a cycle through the styles.
- **Lines**: sort ascending or descending in natural order (numbers by value, case-insensitive), case-sensitive sort, sort keeping one of each, sort by length, reverse, shuffle, remove duplicates or empty lines, trim trailing whitespace, and **keep** or **remove the lines matching** a regular expression. They act on the selected lines, or on the whole file with nothing selected.
- **Encode and decode the selection**: Base64, URL encoding, JSON string escapes, HTML entities and `\uXXXX` escapes. With several selections each one is converted, in one undo step; a decode that cannot read one of them changes none.
- **Increment / Decrement Number** change the number at each cursor, or the next one on its line: negatives, zero padding (`007` → `008`) and hexadecimal (`0x0f` → `0x10`) keep their form. In the Emacs keymap they are `C-c =` and `C-c -`, and `C-u 10` steps by ten.

See the [String manipulation guide](/docs/editing/#string-manipulation).
