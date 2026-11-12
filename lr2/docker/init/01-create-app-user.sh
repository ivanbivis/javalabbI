#!/bin/bash

# Создаёт отдельного пользователя приложения и его схему.
set -euo pipefail

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE ROLE ${APP_DB_USER} WITH LOGIN PASSWORD '${APP_DB_PASSWORD}';
    GRANT CONNECT ON DATABASE ${POSTGRES_DB} TO ${APP_DB_USER};
    CREATE SCHEMA ${APP_DB_SCHEMA} AUTHORIZATION ${APP_DB_USER};
    ALTER ROLE ${APP_DB_USER} SET search_path TO ${APP_DB_SCHEMA};
EOSQL
