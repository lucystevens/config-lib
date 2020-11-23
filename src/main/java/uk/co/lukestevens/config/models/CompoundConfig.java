package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.util.Map.Entry;

import java.util.Properties;

import uk.co.lukestevens.config.Config;

/**
 * A config implementation that combines several other config
 * implementations. 
 * 
 * @author Luke Stevens
 */
public class CompoundConfig extends PropertiesConfig {
	
	final Config[] configs;

	/**
	 * Create a new CompoundConfig with a list of configs. These will be resolved in order of
	 * priority e.g. If a config exists in the first config in the list, it will be resolved
	 * there first.
	 * It should also be noted that this will only combine properties available when this
	 * object is constructed; if changes are made to the configs these will not be available
	 * until this config is reloaded.
	 * @param configs
	 */
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
