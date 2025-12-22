#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID="$("$ROOT/scripts/jcmd-pid.sh" || true)"
if [[ -z "${PID:-}" ]]; then
  echo "PID not found. Is the server running?"
  exit 2
fi
OUT="$ROOT/profiles/thread_dump_$(date +%Y%m%d_%H%M%S).txt"
jcmd "$PID" Thread.print -l > "$OUT"
echo "Saved: $OUT"
