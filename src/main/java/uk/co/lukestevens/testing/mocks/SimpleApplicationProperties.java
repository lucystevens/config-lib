package uk.co.lukestevens.testing.mocks;

import uk.co.lukestevens.config.ApplicationProperties;

public class SimpleApplicationProperties implements ApplicationProperties {
	
	String version;
	String name;
	String group;
	
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
