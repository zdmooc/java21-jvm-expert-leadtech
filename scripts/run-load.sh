#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAR="$ROOT/tools/loadgen/target/loadgen-1.0.0.jar"

URL="${1:-http://localhost:8080/alloc?mb=5}"
CONC="${2:-200}"
DUR="${3:-30}"
MODE="${4:-virtual}"

if [[ ! -f "$JAR" ]]; then
  echo "[INFO] loadgen jar not found -> building..."
  (cd "$ROOT" && mvn -q -pl tools/loadgen -am package)
fi

exec java -jar "$JAR" "$URL" "$CONC" "$DUR" "$MODE"
