#!/usr/bin/env bash
set -euo pipefail

# Trouve le PID du serveur (Main class).
# Usage: ./scripts/jcmd-pid.sh
jcmd | awk '/io.zd.jvm.server.Main/ {print $1; exit}'
