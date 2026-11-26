---
title: "File encodings"
group: "Workspace & files"
order: 14
beta: false
summary: "Reopen a file in the encoding it is really in, or save it in another one: Shift_JIS, GBK, windows-1251 and every other encoding the JDK knows. The choice is remembered per file."
description: "Reopen a file in the encoding it is really in, or save it in another, from Shift_JIS to windows-1251. The choice is remembered per file."
alt: "A Shift_JIS text file opened as ISO-8859-1, its encoding shown in the warning colour in the status bar, with Reopen with Encoding filtered to Shift_JIS"
---

---
---
Open a file in the encoding it was really written in, or save it in the one another tool expects, without a byte lost on the way. Editora reads UTF-8 unless a byte-order mark or [`.editorconfig`](/features/editorconfig/) says otherwise, and a file that is not valid UTF-8 still opens with every byte kept, its encoding drawn in the warning colour in the status bar because it is a guess.

Click the encoding in the status bar, or use the palette:

- **Buffer: Reopen with Encoding…** reads the file again in the encoding you pick. If the bytes are not valid in it, nothing changes and the status bar names the first place that failed.
- **Buffer: Save with Encoding…** writes the file in another encoding from its next save. Text that encoding cannot hold is refused, naming the character and its line, rather than written as question marks.
- **Every encoding the runtime knows**: the common ones come first, grouped by script, from Shift_JIS and GBK to windows-1251, and the list is the same on every system.
- **Remembered per file**: the choice holds after a restart and a rename, and works on a [remote host](/features/remote-sftp/), where `.editorconfig` is not read.
- **Safe**: unsaved changes are confirmed before a reopen, and Local History keeps a copy.

See the [File encodings guide](/docs/workspace/#file-encodings).
