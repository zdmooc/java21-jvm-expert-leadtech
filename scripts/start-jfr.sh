#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID="$("$ROOT/scripts/jcmd-pid.sh" || true)"
if [[ -z "${PID:-}" ]]; then
  echo "PID not found. Is the server running?"
  exit 2
fi

NAME="${1:-lab}"
DUR="${2:-60s}"
OUT="$ROOT/profiles/${NAME}_$(date +%Y%m%d_%H%M%S).jfr"
jcmd "$PID" JFR.start name="$NAME" settings=profile duration="$DUR" filename="$OUT"
echo "Started JFR: $OUT"
