CREATE SCHEMA IF NOT EXISTS core;

DROP TABLE IF EXISTS core.application_config;
DROP TABLE IF EXISTS core.site_config;

CREATE TABLE core.application_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	application VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);

CREATE TABLE core.site_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	site VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);