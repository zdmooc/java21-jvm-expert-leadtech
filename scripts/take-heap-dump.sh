#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID="$("$ROOT/scripts/jcmd-pid.sh" || true)"
if [[ -z "${PID:-}" ]]; then
  echo "PID not found. Is the server running?"
  exit 2
fi
OUT="$ROOT/dumps/heap_$(date +%Y%m%d_%H%M%S).hprof"
jcmd "$PID" GC.heap_dump "$OUT"
echo "Saved: $OUT"
