package uk.co.lukestevens.config.services;

import java.io.IOException;
import java.util.List;

import uk.co.lukestevens.config.Property;

/**
 * An interface defining a service that provides Properties from an external
 * source such as a database
 * 
 * @author Luke Stevens
 */
public interface PropertyService {
	
	/**
	 * Loads a list of available properties
	 * @return A list of properties applicable for this service
	 * @throws IOException
	 */
	List<Property> load() throws IOException;
	
	/**
	 * Fetches a single property based on the given key
	 * @param key The property key
	 * @return The property that matches this key, or null if none exists
	 */
	Property get(String key);

}
