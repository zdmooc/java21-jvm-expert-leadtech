#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAR="$ROOT/apps/jvm-lab-server/target/jvm-lab-server-1.0.0.jar"

if [[ ! -f "$JAR" ]]; then
  echo "[INFO] jar not found -> building..."
  (cd "$ROOT" && mvn -q -pl apps/jvm-lab-server -am package)
fi

mkdir -p "$ROOT/profiles" "$ROOT/dumps" "$ROOT/logs"

# Paramètres par défaut orientés labs.
PORT="${PORT:-8080}"
SERVER_EXECUTOR="${SERVER_EXECUTOR:-virtual}"  # virtual|fixed
HEAP="${HEAP:-512m}"

# GC logs + heapdump on OOM + JFR on demand
JAVA_OPTS=(
  "-Xms$HEAP" "-Xmx$HEAP"
  "-XX:+HeapDumpOnOutOfMemoryError"
  "-XX:HeapDumpPath=$ROOT/dumps"
  "-Xlog:gc*:file=$ROOT/logs/gc.log:time,uptime,level,tags"
)

export PORT SERVER_EXECUTOR
echo "[INFO] PORT=$PORT SERVER_EXECUTOR=$SERVER_EXECUTOR HEAP=$HEAP"
echo "[INFO] Logs: $ROOT/logs/gc.log"
echo "[INFO] Dumps: $ROOT/dumps"

exec java "${JAVA_OPTS[@]}" -jar "$JAR"
