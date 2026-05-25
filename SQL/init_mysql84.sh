#!/bin/sh
set -eu

# MySQL 8.4 official image already runs SQL files placed in
# /docker-entrypoint-initdb.d/ on first startup, so no custom
# init logic is needed here.
exec "$@"
