package uk.co.lukestevens.config.models;

import javax.inject.Inject;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;

public class ConfiguredApplicationProperties implements ApplicationProperties {
	
	public static final String NAME = "application.name";
	public static final String VERSION = "application.version";
	public static final String GROUP = "application.group";
	
	final Config config;
	
	@Inject
	public ConfiguredApplicationProperties(Config config) {
		this.config = config;
	}

	@Override
	public String getApplicationVersion() {
		return this.config.getAsString(VERSION);
	}

	@Override
	public String getApplicationName() {
		return this.config.getAsString(NAME);
	}

	@Override
	public String getApplicationGroup() {
		return this.config.getAsString(GROUP);
	}

}
