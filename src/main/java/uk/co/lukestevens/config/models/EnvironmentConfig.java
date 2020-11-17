package uk.co.lukestevens.config.models;

import java.util.Map;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

public class EnvironmentConfig extends PropertiesConfig {
	
	public EnvironmentConfig() {
		super(new Properties());
	}

	@Override
	public void load() throws IOException {
		init(System.getenv());
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
 