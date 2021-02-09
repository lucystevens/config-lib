package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

import uk.co.lukestevens.config.Config;

/**
 * A config implementation that combines several other config
 * implementations. 
 * 
 * @author Luke Stevens
 */
public class CompoundConfig extends BaseConfig {
	
	final Config[] configs;

	/**
	 * Create a new CompoundConfig with a list of configs. These will be resolved in order of
	 * priority e.g. If a config exists in the first config in the list, it will be resolved
	 * there first.
	 * @param configs A list of config objects to combine
	 */
	public CompoundConfig(Config...configs) {
		this.configs = configs;
	}
	
	@Override
	public void load() throws IOException {
		for(Config config : this.configs) {
			config.load();
		}
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		Map<Object, Object> entryMap = new HashMap<>();
		for(Config config : configs) {
			for(Entry<Object, Object> entry : config.entrySet()) {
				entryMap.putIfAbsent(entry.getKey(), entry.getValue());
			}
		}
		return entryMap.entrySet();
	}

	@Override
	public String get(String key) {
		for(Config config : configs) {
			String value = config.getAsStringOrDefault(key, null);
			if(value != null) {
				return value;
			}
		}
		return null;
	}

}
