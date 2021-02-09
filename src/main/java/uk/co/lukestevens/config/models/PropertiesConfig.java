package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * A config implementation for resolving properties from a Properties object
 * 
 * @author Luke Stevens
 */
public class PropertiesConfig extends BaseConfig {
	
	protected final Properties props;

	/**
	 * Create a new PropertiesConfig
	 * @param props The properties to back with config with.
	 * External changes to this properties object will affect this config instance
	 */
	public PropertiesConfig(Properties props) {
		this.props = props;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	@Override
	public String get(String key) {
		return props.getProperty(key);
	}
	

	@Override
	public void load() throws IOException { }

}
