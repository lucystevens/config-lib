package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.lukestevens.config.services.PropertyService;


/**
 * A config implementation that loads properties from a database
 * 
 * @author Luke Stevens
 */
public class DatabaseConfig extends BaseConfig{
	
	private final Map<String, Property> config = new HashMap<>();
	private final PropertyService service;
	
	/**
	 * Creates a new configuration by loading properties from a database using a service
	 * @param service the PropertyService too use to load properties from the database
	 */
	@Inject
	public DatabaseConfig(PropertyService service) {
		this.service = service;	
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return this.config.values()
				.stream()
				.map(prop -> new AbstractMap.SimpleEntry<Object, Object>(prop.getKey(), prop.getValue()))
				.collect(Collectors.toSet());
	}

	@Override
	public String get(String key) {
		Property prop = this.config.get(key);
		if(prop == null || prop.isExpired()) {
			prop = this.service.get(key);
		}
		
		if(prop == null) {
			return null;
		}
		else {
			this.config.put(key, prop);
			return prop.getValue();
		}
	}

	@Override
	public void load() throws IOException {
		for(Property prop : service.load()) {
			this.config.put(prop.getKey(), prop);
		}
	}

}
