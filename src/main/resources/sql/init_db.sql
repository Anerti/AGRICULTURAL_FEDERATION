create database agricultural_federation_db;
\c agricultural_federation_db;
CREATE ROLE agricultural_federation_manager LOGIN ENCRYPTED PASSWORD 'AgrZ27U&ml!' NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT;
CREATE SCHEMA agricultural_federation_app AUTHORIZATION agricultural_federation_manager;
GRANT USAGE ON SCHEMA agricultural_federation_app TO agricultural_federation_manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA agricultural_federation_app TO agricultural_federation_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA agricultural_federation_app GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO agricultural_federation_manager;