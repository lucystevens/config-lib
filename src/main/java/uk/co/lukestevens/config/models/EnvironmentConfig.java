package uk.co.lukestevens.config.models;

import java.util.Map;
import java.io.IOException;
import java.util.Map.Entry;

import javax.inject.Singleton;

import uk.co.lukestevens.utils.EnvironmentVariables;

import java.util.Properties;

/**
 * A config implementation that fetches properties from the system environment
 * variables.
 * 
 * @author Luke Stevens
 */
@Singleton
public class EnvironmentConfig extends PropertiesConfig {
	
	/**
	 * Constructs a new EnvironmentConfig. Any changes to environment variables
	 * will require a reload to view.
	 */
	public EnvironmentConfig() {
		super(new Properties());
	}

	@Override
	public void load() throws IOException {
		init(EnvironmentVariables.get());
	}
	
	void init(Map<String, String> environmentVariables) {
		for(Entry<String, String> prop : environmentVariables.entrySet()) {
			props.put(normalise(prop.getKey()), prop.getValue());
		}
	}
	
	String normalise(String key) {
		return key.toLowerCase().replace("_", ".");
	}

}
 