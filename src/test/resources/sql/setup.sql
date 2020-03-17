CREATE SCHEMA IF NOT EXISTS config;

DROP TABLE IF EXISTS config.application_config;
DROP TABLE IF EXISTS config.site_config;

CREATE TABLE config.application_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	application VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);

CREATE TABLE config.site_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	site VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);