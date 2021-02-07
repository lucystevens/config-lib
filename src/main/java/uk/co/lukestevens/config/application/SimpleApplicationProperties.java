package uk.co.lukestevens.config.application;

import uk.co.lukestevens.config.ApplicationProperties;

/**
 * A simple ApplicationProperties implementation for testing
 * 
 * @author Luke Stevens
 */
public class SimpleApplicationProperties implements ApplicationProperties {
	
	String version;
	String name;
	String group;
	
	/**
	 * Create a new ApplicationProperties implementation using given values
	 * @param version The application version
	 * @param name The application name
	 * @param group The application group
	 */
	public SimpleApplicationProperties(String version, String name, String group) {
		this.version = version;
		this.name = name;
		this.group = group;
	}

	@Override
	public String getApplicationVersion() {
		return version;
	}

	@Override
	public String getApplicationName() {
		return name;
	}

	@Override
	public String getApplicationGroup() {
		return group;
	}

}
