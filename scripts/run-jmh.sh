#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAR="$ROOT/benchmarks/jmh-suite/target/jmh-suite.jar"

FILTER="${1:-}"

if [[ ! -f "$JAR" ]]; then
  echo "[INFO] building JMH suite..."
  (cd "$ROOT" && mvn -q -pl benchmarks/jmh-suite -am package)
fi

CMD=(java -jar "$JAR" -wi 3 -i 5 -f 2)
if [[ -n "$FILTER" ]]; then
  CMD+=("$FILTER")
fi
exec "${CMD[@]}"
