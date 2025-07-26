#!/bin/bash
set -e

echo "host replication replica_user 0.0.0.0/0 md5" >> "$PGDATA/pg_hba.conf"

cat >> "$PGDATA/postgresql.conf" <<EOF
wal_level = replica
max_wal_senders = 10
wal_keep_size = 512MB
listen_addresses = '*'
EOF

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  CREATE ROLE replica_user WITH REPLICATION LOGIN ENCRYPTED PASSWORD 'root';
EOSQL

