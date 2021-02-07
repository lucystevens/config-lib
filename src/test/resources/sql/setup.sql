CREATE SCHEMA IF NOT EXISTS core;

DROP TABLE IF EXISTS core.config;

CREATE TABLE core.config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	application_name VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);
