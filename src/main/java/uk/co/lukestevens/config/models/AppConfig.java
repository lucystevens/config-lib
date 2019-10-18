package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import uk.co.lukestevens.config.Property;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.encryption.EncryptionService;

public class AppConfig extends BaseConfig {
	
	private final Map<String, Property> config;
	private final PropertyService propertyService;
	
	public AppConfig(PropertyService propertyService, EncryptionService service) {
		this(propertyService, new Properties(), service);
	}

	public AppConfig(PropertyService propertyService, Properties baseProps, EncryptionService service) {
		super(service);
		this.propertyService = propertyService;
		this.config = baseProps.entrySet()
				.stream()
				.map(this::parse)
				.collect(Collectors.toMap(Property::getKey, Function.identity()));
		
		this.propertyService.load().stream().forEach(property -> this.config.put(property.getKey(), property));
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
			prop = this.propertyService.get(key);
		}
		
		if(prop == null) {
			return null;
		}
		else {
			this.config.put(key, prop);
			return prop.getValue();
		}
	}
	
	Property parse(Map.Entry<Object, Object> entry) {
		return new Property(entry.getKey().toString(), entry.getValue().toString());
	}

}
