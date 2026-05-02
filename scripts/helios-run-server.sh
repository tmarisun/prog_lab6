#!/usr/bin/env bash
# Запуск TCP-сервера на helios (из корня репозитория после git clone).
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

export PG_HOST="${PG_HOST:-pg}"
export PG_PORT="${PG_PORT:-5432}"
export PG_DATABASE="${PG_DATABASE:-studs}"
export SERVER_PORT="${SERVER_PORT:-5555}"
export AUTO_CREATE_DATABASE="${AUTO_CREATE_DATABASE:-true}"

if [[ -z "${PG_USER:-}" ]]; then
  export PG_USER="${USER:?задайте USER или PG_USER}"
fi
if [[ -z "${PG_PASSWORD:-}" ]]; then
  export PG_PASSWORD="${PG_USER}"
fi

PORT="${1:-$SERVER_PORT}"
exec ./gradlew --no-daemon runServer --args="$PORT"
