package uk.co.lukestevens.config.models;

import java.util.Set;

import uk.co.lukestevens.config.Config;

import java.io.IOException;
import java.util.Map.Entry;

/**
 * An interface defining a config stub that exposes the basic methods
 * to retrieve a config value, retrieve all config values, and load
 * initial values from a source. This should not be used instead of {@link Config} 
 * for configurations.
 * 
 * @author Luke
 */
public interface ConfigSource {
	
	/**
	 * @return A list of the key value entries this configuration currently
	 * stores in memory. This will not update any expired
	 * values from their source.
	 */
	public Set<Entry<Object, Object>> entrySet();

	/**
	 * Gets the value for a key from this config.
	 * If the currently stored value has expired, 
	 * this will update it from the source
	 * @param key The key to retrieve a value for
	 * @return The value associated with this key
	 */
	public Object get(String key);
	
	/**
	 * Loads current configuration from the source
	 * @throws IOException
	 */
	public void load() throws IOException;

}
