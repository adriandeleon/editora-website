---
title: "Crash recovery"
group: "Workspace & files"
order: 13
beta: false
summary: "Unsaved edits, including untitled buffers, are kept while you work and offered back on the next launch if Editora did not close normally. Restoring never writes to your files."
description: "Editora keeps a recovery copy of unsaved edits, untitled buffers included, and offers them back as unsaved tabs after a crash."
alt: "Settings → Workspace with \"Keep a recovery copy of unsaved edits\" turned on"
---

---
---
Editora keeps a recovery copy of every buffer with unsaved edits, untitled ones included, in every window. The copies live in the config folder and are written as you work.

If Editora did not close normally (a crash, a kill, a logout, a power loss), the next launch offers the edits back.

- Restoring opens the text as **unsaved tabs**. It never writes to your files, so you decide what to save.
- A file that changed on disk in the meantime is flagged.
- A copy is removed when you save, revert or close the buffer.
- If a refactoring that moves or deletes files is interrupted, the next time the project is opened Editora offers to put the files back.

Buffers over about 16.8 million characters are not covered. **File: Recover Unsaved Edits…** shows what is kept.

**On by default.** Turn it off in **Settings → Workspace** (**Keep a recovery copy of unsaved edits**) or with **View: Toggle Crash Recovery**. See the [Crash recovery guide](/docs/undo-history/#crash-recovery).
