// Writes a keymap chord the way the app shows it. A port of ChordFormat in the
// Editora repo (src/main/java/com/editora/command/ChordFormat.java): the Emacs
// keymap keeps its own tokens (C-x C-s, M-x); every other keymap uses platform
// key names joined by "+" on Windows/Linux (Ctrl+Shift+P, a multi-key sequence
// as Ctrl+K Ctrl+W) and modifier glyphs in Apple's order on macOS (⇧⌘P).
//
// The chords in src/lib/commands.ts are the raw keymap tokens, so a page that
// shows a non-Emacs keymap's chord passes it through here first.

/** Modifier prefixes in the order the app's KeyDispatcher emits them. */
const PREFIXES = ["C-", "M-", "Cmd-", "S-"];
const PLATFORM_NAMES = ["Ctrl", "Alt", "Meta", "Shift"];
const MAC_GLYPHS = ["⌃", "⌥", "⌘", "⇧"];
/** Indices into PREFIXES in the order Apple writes modifiers: ⌃ ⌥ ⇧ ⌘. */
const MAC_ORDER = [0, 1, 3, 2];

const platformKey = (key: string): string => {
  switch (key) {
    case "escape":
      return "Esc";
    case "pageup":
      return "PageUp";
    case "pagedown":
      return "PageDown";
    case "back-quote":
      return "`";
    case "quote":
      return "'";
    default:
      // p → P, space → Space, f5 → F5; punctuation is unchanged.
      return /^[a-z]/i.test(key) ? key[0].toUpperCase() + key.slice(1) : key;
  }
};

const MAC_KEYS: Record<string, string> = {
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
};

const formatToken = (token: string, mac: boolean): string => {
  let at = 0;
  const held: boolean[] = [];
  PREFIXES.forEach((p, i) => {
    // A prefix only counts while a key is left after it: "C--" is Ctrl + minus.
    held[i] = token.startsWith(p, at) && token.length > at + p.length;
    if (held[i]) at += p.length;
  });
  const key = token.slice(at);
  if (mac) {
    return MAC_ORDER.filter((i) => held[i]).map((i) => MAC_GLYPHS[i]).join("") +
      (MAC_KEYS[key] ?? platformKey(key));
  }
  return PREFIXES.map((_, i) => (held[i] ? `${PLATFORM_NAMES[i]}+` : "")).join("") + platformKey(key);
};

/**
 * A chord sequence in the notation the app uses for `keymap`: Emacs tokens for
 * the Emacs keymap, else Windows/Linux names (or macOS glyphs when `mac`). Keys
 * stay separated by single spaces, so callers can still split a sequence.
 */
export function formatChord(sequence: string, keymap: string, mac = false): string {
  if (keymap === "emacs") return sequence;
  return sequence
    .split(" ")
    .filter((t) => t !== "")
    .map((t) => formatToken(t, mac))
    .join(" ");
}
