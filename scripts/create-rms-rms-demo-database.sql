-- ==========================================
-- Create rms-rms-demo tenant database
-- Naming: 3-letter platform prefix + hyphen + tenant_key = rms-rms-demo
-- Run as Postgres superuser (e.g. postgres) on the host rms-service/TMS use (e.g. db:5432)
-- ==========================================

-- Drop if re-creating (optional; comment out to avoid data loss)
-- DROP DATABASE IF EXISTS "rms-rms-demo";
-- DROP USER IF EXISTS "rms-rms-demo";

-- Create role (user) for the tenant DB. Use quoted identifier for hyphen.
CREATE USER "rms-rms-demo" WITH PASSWORD 'AzBy791833!';

-- Create database with hyphen; owner = same user
CREATE DATABASE "rms-rms-demo"
    WITH
    OWNER = "rms-rms-demo"
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Connect to the new database (psql only; omit if running from pgAdmin or as single statements)
\c "rms-rms-demo"

-- Ensure the owner has full rights on schema public
GRANT ALL ON SCHEMA public TO "rms-rms-demo";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO "rms-rms-demo";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO "rms-rms-demo";
GRANT CREATE ON SCHEMA public TO "rms-rms-demo";

-- Verify
\echo 'Database "rms-rms-demo" and user "rms-rms-demo" created.'
\echo 'Next: Run Liquibase against jdbc:postgresql://<host>:5432/rms-rms-demo with user rms-rms-demo'
\echo 'Then: Update TMS tenant row for rms-demo to use database_name rms-rms-demo and this user.'
