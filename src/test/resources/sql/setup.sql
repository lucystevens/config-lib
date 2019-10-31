DROP TABLE IF EXISTS application_config;
DROP TABLE IF EXISTS site_config;

CREATE TABLE application_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	application VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);

CREATE TABLE site_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	site VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);