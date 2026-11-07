// The macOS half of ChordFormat in the Editora repo
// (src/main/java/com/editora/command/ChordFormat.java), beside the
// Windows/Linux half in ./chord-format.ts: the Emacs keymap keeps its own
// tokens on every platform, and every other keymap writes modifier glyphs in
// Apple's order (Control Option Shift Command) before the key, as in ⇧⌘P.
// The chords it formats are the raw tokens of a keymap's macOS variant
// (`mac` / `macAlt` in src/lib/commands.ts).

/** Modifier prefixes in the order the app's KeyDispatcher emits them. */
const PREFIXES = ["C-", "M-", "Cmd-", "S-"];
/** Glyph per prefix index: ⌃ ⌥ ⌘ ⇧. */
const GLYPHS = ["⌃", "⌥", "⌘", "⇧"];
/** Indices into PREFIXES in the order Apple writes modifiers: ⌃ ⌥ ⇧ ⌘. */
const MAC_ORDER = [0, 1, 3, 2];

const KEYS: Record<string, string> = {
  enter: "↩",
  tab: "⇥",
  backspace: "⌫",
  delete: "⌦",
  escape: "⎋",
  left: "←",
  right: "→",
  up: "↑",
  down: "↓",
  home: "↖",
  end: "↘",
  pageup: "⇞",
  pagedown: "⇟",
  "back-quote": "`",
  quote: "'",
};

const macKey = (key: string): string =>
  // p → P, space → Space, f5 → F5; punctuation is unchanged.
  KEYS[key] ?? (/^[a-z]/i.test(key) ? key[0].toUpperCase() + key.slice(1) : key);

const formatToken = (token: string): string => {
  let at = 0;
  let mask = 0;
  PREFIXES.forEach((p, i) => {
    // A prefix only counts while a key is left after it: "C--" is Ctrl + minus.
    if (token.startsWith(p, at) && token.length > at + p.length) {
      mask |= 1 << i;
      at += p.length;
    }
  });
  const mods = MAC_ORDER.filter((i) => mask & (1 << i))
    .map((i) => GLYPHS[i])
    .join("");
  return mods + macKey(token.slice(at));
};

/**
 * A macOS chord sequence in the notation the app uses for `keymap`: Emacs
 * tokens for the Emacs keymap, glyphs for the others. Keys stay separated by
 * single spaces, so callers can still split a sequence into its keys.
 */
export function formatChordMac(sequence: string, keymap: string): string {
  if (keymap === "emacs") return sequence;
  return sequence
    .split(" ")
    .filter((t) => t !== "")
    .map(formatToken)
    .join(" ");
}
