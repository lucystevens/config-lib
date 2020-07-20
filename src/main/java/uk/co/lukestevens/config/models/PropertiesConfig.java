package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * A config source implementation for resolving the base config source from file
 * 
 * @author Luke Stevens
 */
public class PropertiesConfig extends BaseConfig {
	
	protected final Properties props;

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
