#!/usr/bin/env bash
# Cross-editor startup benchmark for the /compare page.
#
# One metric for all three editors: the wall-clock gap between launching the
# binary and the window server reporting an on-screen window, owned by that
# editor, whose title names the file or project. Waiting for *any* window would
# be easier and wrong — IntelliJ shows a splash and a welcome window titled
# "IntelliJ IDEA" long before an editor exists, and VS Code paints its shell
# before the file. The title is the only readiness signal that means the same
# thing across a JavaFX app, an Electron app, and a JetBrains IDE.
#
# Usage: measure-editors.sh -n RUNS -m NEEDLE -- PATH
set -uo pipefail

SELF_DIR=$(cd "$(dirname "$0")" && pwd)

# The watcher is compiled, not a shell loop: `osascript` costs ~1.6 s per poll,
# which is coarser than every figure this benchmark reports and silently rounded
# the July 2026 run up to the next poll. Built on demand into the system temp
# dir so no binary is committed.
WAITWIN="${TMPDIR:-/tmp}/editora-waitwin"
if [ ! -x "$WAITWIN" ] || [ "$SELF_DIR/waitwin.swift" -nt "$WAITWIN" ]; then
  swiftc -O -o "$WAITWIN" "$SELF_DIR/waitwin.swift" || {
    echo "could not build the window watcher (needs swiftc from the Xcode tools)" >&2
    exit 2
  }
fi

runs=5; needle=""
while [ $# -gt 0 ]; do
  case "$1" in
    -n) runs=$2; shift 2 ;;
    -m) needle=$2; shift 2 ;;
    --) shift; break ;;
    *) break ;;
  esac
done
target=${1:?need a path to open}

now_ms() { perl -MTime::HiRes=time -e 'printf "%d\n", time() * 1000'; }

EDITORA_BIN=/Applications/Editora.app/Contents/MacOS/Editora
VSCODE_BIN="/Applications/Visual Studio Code.app/Contents/MacOS/Code"
# JetBrains Toolbox installs into ~/Applications, a direct download into
# /Applications; take whichever exists.
IDEA_BIN="$HOME/Applications/IntelliJ IDEA.app/Contents/MacOS/idea"
[ -x "$IDEA_BIN" ] || IDEA_BIN="/Applications/IntelliJ IDEA.app/Contents/MacOS/idea"

# Editora names the window after the active file, and after the project only when
# the folder is opened as one: a bare folder argument leaves the title at plain
# "Editora", with no project in it, so the project run has to pass --project.
# --single-window keeps it to the one window under test instead of also
# restoring the no-project one.
EDITORA_PROJECT_ARGS=""
if [ "${MODE:-file}" = "project" ]; then
  EDITORA_PROJECT_ARGS="--project=$target --single-window=$needle"
fi

# key|binary|window-owner|app name for `quit`|extra args
EDITORS=(
  "Editora|$EDITORA_BIN|Editora|Editora|--no-session --new-instance $EDITORA_PROJECT_ARGS"
  "VS Code|$VSCODE_BIN|Code|Visual Studio Code|-n --disable-workspace-trust"
  "IntelliJ IDEA|$IDEA_BIN|IntelliJ IDEA|IntelliJ IDEA|"
)

quit_all() {
  for app in "Editora" "Visual Studio Code" "IntelliJ IDEA"; do
    osascript -e "tell application \"$app\" to quit" >/dev/null 2>&1 || true
  done
  sleep 2
  pkill -f "Editora.app/Contents/MacOS/Editora"        2>/dev/null || true
  pkill -f "Visual Studio Code.app/Contents/MacOS/Code" 2>/dev/null || true
  pkill -f "IntelliJ IDEA.app/Contents/MacOS/idea"      2>/dev/null || true
  sleep 3   # let the machine settle, so a run is not timed against the last teardown
}

echo "target : $target"
echo "needle : $needle"
echo "runs   : $runs timed (plus one discarded warm-up)"
echo

for spec in "${EDITORS[@]}"; do
  IFS='|' read -r key bin owner appname extra <<<"$spec"
  if [ ! -x "$bin" ]; then echo "$key: no executable at $bin" >&2; continue; fi
  read -r -a extra_args <<<"$extra"

  echo "### $key"
  times=()
  for i in $(seq 0 "$runs"); do
    quit_all
    t0=$(now_ms)
    "$bin" ${extra_args+"${extra_args[@]}"} "$target" >/dev/null 2>&1 &
    if t1=$("$WAITWIN" "$needle" 120000 "$owner"); then
      ms=$(( t1 - t0 ))
      if [ "$i" -eq 0 ]; then
        echo "  warm-up : ${ms} ms (discarded)"
      else
        echo "  run $i   : ${ms} ms"
        times+=("$ms")
      fi
    else
      echo "  run $i   : TIMEOUT — no window owned by '$owner' titled *${needle}*" >&2
    fi
  done

  if [ "${#times[@]}" -gt 0 ]; then
    printf '%s\n' "${times[@]}" | sort -n | awk -v k="$key" '
      { v[NR] = $1 }
      END { m = (NR % 2) ? v[(NR+1)/2] : (v[NR/2] + v[NR/2+1]) / 2
            printf "  == %-14s n=%d  min=%d  median=%d  max=%d ms\n", k, NR, v[1], m, v[NR] }'
  fi
  echo
done

quit_all
