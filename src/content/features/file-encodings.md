---
title: "File encodings"
group: "Workspace & files"
order: 14
beta: false
summary: "Reopen a file in the encoding it is really in, or save it in another one: Shift_JIS, GBK, windows-1251 and every other encoding the JDK knows. The choice is remembered per file."
---

Editora reads a file as UTF-8 unless a byte-order mark or the project's [`.editorconfig`](/features/editorconfig) says otherwise. A file that is not valid UTF-8 opens through a single-byte stand-in (Windows-1252 or ISO-8859-1) that keeps every byte, and its encoding in the status bar is drawn in the warning colour, because it is a guess.

Click the encoding in the status bar, or use the palette:

- **Reopen with Encoding…** reads the file again in the encoding you pick. If the bytes are not valid in it, nothing changes and the status line names the first line and column that could not be read. Unsaved changes are confirmed first, and Local History keeps a copy.
- **Save with Encoding…** sets the encoding the file is written in from its next save, whatever `.editorconfig` says. The tab shows as modified until you save, and choosing the file's current encoding again makes it clean. Text the encoding cannot hold is refused, naming the character and its line, rather than written as question marks or quietly switched to UTF-8.

The list starts with the common encodings grouped by script (Unicode, Western, Central European, Cyrillic, Greek, Turkish, Hebrew, Arabic, Baltic, Vietnamese, Thai, Japanese, Chinese, Korean, DOS) and goes on with every other encoding the runtime supports, the same on every operating system.

The choice is **remembered for the file**: it is used when the file opens again, after a restart, and after a rename, and it works for files on a [remote host](/features/remote-sftp), where `.editorconfig` is not read. File Information shows the encoding in use.

A shell script that opens with Windows (CRLF) line endings or a byte-order mark gets a notice above the editor with **Convert to LF** (or **Remove BOM**), since bash cannot run it as it is. Saving writes the fix.
