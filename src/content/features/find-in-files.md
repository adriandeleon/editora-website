---
title: "Find in files"
group: "Workspace & files"
order: 5
beta: false
summary: "Project-wide search and replace with a results panel, plus AceJump to leap the caret to any visible spot by typing a label."
---

Project-wide search with a results panel (`C-S-f`): matches grouped by file, with case / regex / whole-word options, and it searches open buffers' unsaved text too. **Replace-in-files** rewrites matches across the project (undoable in open buffers). It uses ripgrep when installed, and in a [remote folder](/features/remote-sftp) the host's own ripgrep searches it in one command instead of reading every file over the connection.

In-file find (`C-s` / `C-r`) is incremental with highlight-all and a match count. And **AceJump** (`M-g j`) lets you leap the caret to any visible spot by typing the label that appears over it.
