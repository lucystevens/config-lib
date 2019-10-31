package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.lukestevens.config.Property;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.encryption.EncryptionService;

public class DatabaseConfig extends BaseConfig implements DerivedConfig {
	
	private final Map<String, Property> config = new HashMap<>();
	private final PropertyService service;
	
	public DatabaseConfig(PropertyService service, EncryptionService encryption) {
		super(encryption);
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
	public Object get(String key) {
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
