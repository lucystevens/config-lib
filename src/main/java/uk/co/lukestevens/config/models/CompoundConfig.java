package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.util.Map.Entry;

import java.util.Properties;

import uk.co.lukestevens.config.Config;

public class CompoundConfig extends PropertiesConfig {
	
	final Config[] configs;

	// configs in order of priority (first arg being highest priority)
	public CompoundConfig(Config...configs) {
		super(new Properties());
		this.configs = configs;
	}
	
	@Override
	public void load() throws IOException {
		for(Config config : this.configs) {
			config.load();
			for(Entry<Object, Object> property : config.entrySet()) {
				props.putIfAbsent(property.getKey(), property.getValue());
			}
		}
	}

}
