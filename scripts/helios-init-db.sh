#!/usr/bin/env bash
# Создание БД studs на кластере PostgreSQL кафедры (если ещё нет).
set -euo pipefail
HOST="${PG_HOST:-pg}"
DB="${PG_DATABASE:-studs}"
USER="${PG_USER:-${USER:?}}"
export PGPASSWORD="${PG_PASSWORD:-$USER}"

if command -v createdb >/dev/null 2>&1; then
  if createdb -h "$HOST" -U "$USER" "$DB" 2>/dev/null; then
    echo "База $DB создана."
  else
    echo "createdb не выполнен (возможно, БД уже есть или нет прав). Проверьте: psql -h $HOST -d $DB -U $USER -c 'SELECT 1'"
  fi
else
  echo "Утилита createdb не найдена; создайте БД вручную или положитесь на AUTO_CREATE_DATABASE=true в приложении."
fi
