---
title: "Auto-close & bracket matching"
group: "Editing"
order: 3
beta: false
summary: "Typing <code>([{</code> or quotes inserts the closer, type-over to skip, wrap a selection, and Backspace clears an empty pair. The bracket beside the caret is highlighted with its match."
description: "Brackets and quotes close as you type, type over their closer, wrap a selection, and delete as a pair. The bracket at the caret shows its match."
alt: "A Python script whose last line has auto-closed brackets and quotes around the caret"
---

Brackets and quotes take care of themselves: the closer appears as you type the opener, and you never end up with a stray or missing one.

- **Closes as you type.** Typing `(`, `[`, `{`, `"`, `'` or `` ` `` adds the closer and keeps the caret between them.
- **Types over the closer** when it sits next to the caret, so typing it yourself does no harm.
- **Wraps a selection** when you type an opener with text selected.
- **Deletes as a pair.** Backspace inside an empty pair removes both halves.
- **Stays out of the way.** A bracket pairs only where a closer can go, so `(` typed before `foo` gives `(foo`, and the apostrophe in `don't` is left alone.
- **Knows Python strings.** A quote after a prefix such as `f` or `rb` pairs, and the third quote of `"""` adds the closing triple quote.
- **Shows the match.** When the caret sits next to a bracket, it and its partner are highlighted.

See the [Brackets and quotes guide](/docs/editing/#brackets-and-quotes).
