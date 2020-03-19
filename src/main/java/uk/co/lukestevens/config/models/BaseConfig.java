package uk.co.lukestevens.config.models;

import java.util.ArrayList;
import java.util.List;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.ConfigException;
import uk.co.lukestevens.config.KeyParser;
import uk.co.lukestevens.encryption.EncryptionService;

/**
 * A base implementation of the Config interface that defines 
 * the advanced config parsing method, and relies on further 
 * implementations to provide only the basic 'get' method for
 * a property.
 * 
 * @author Luke Stevens
 */
public abstract class BaseConfig implements Config {
	
	protected final EncryptionService service;
	
	/**
	 * @param service The service used to decrypt encrypted properties
	 */
	protected BaseConfig(EncryptionService service) {
		this.service = service;
	}
	
	@Override
	public String getApplicationName() {
		return this.getAsString("application.name");
	}
	
	@Override
	public String getApplicationVersion() {
		return this.getAsString("application.version");
	}

	/**
	 * Get a property value given the key
	 * @param key The key to retrieve a property for
	 * @return The value that corresponds with the given key
	 */
	public abstract Object get(String key);
	
	@Override
	public String getAsString(String key){
		Object value = this.get(key);
		if(value == null) {
			throw new ConfigException(key);
		} else {
			return value.toString();
		}
	}
	
	@Override
	public String getAsStringOrDefault(String key, String def){
		Object value = this.get(key);
		if(value == null) {
			return def;
		} else {
			return value.toString();
		}
	}
	
	@Override
	public <T> T getParsedValue(String key, KeyParser<T> parser) {
		String value = this.getAsString(key);
		try {
			return parser.parse(value);
		} catch(Exception e) {
			throw new ConfigException(key, e);
		}
	}
	
	@Override
	public <T> T getParsedValueOrDefault(String key, KeyParser<T> parser, T def) {
		String value = this.getAsStringOrDefault(key, null);
		if(value == null) {
			return def;
		}
		
		try {
			return parser.parse(value);
		} catch(Exception e) {
			throw new ConfigException(key, e);
		}
	}
	
	@Override
	public String getEncrypted(String key) {
		return this.getParsedValue(key, service::decrypt);
	}
	
	@Override
	public int getAsInt(String key) {
		return this.getParsedValue(key, Integer::parseInt);
	}
	
	@Override
	public int getAsIntOrDefault(String key, int def) {
		return this.getParsedValueOrDefault(key, Integer::parseInt, def);
	}
	
	@Override
	public double getAsDouble(String key) {
		return this.getParsedValue(key, Double::parseDouble);
	}
	
	@Override
	public double getAsDoubleOrDefault(String key, double def) {
		return this.getParsedValueOrDefault(key, Double::parseDouble, def);
	}
	
	@Override
	public boolean getAsBoolean(String key) {
		return this.getParsedValue(key, val -> val.equalsIgnoreCase("true"));
	}
	
	@Override
	public boolean getAsBooleanOrDefault(String key, boolean def) {
		return this.getParsedValueOrDefault(key, val -> val.equalsIgnoreCase("true"), def);
	}
	
	@Override
	public List<String> getAsList(String key) {
		return this.getParsedValue(key, this::parseList);
	}
	
	@Override
	public List<String> getAsListOrDefault(String key, List<String> def) {
		return this.getParsedValueOrDefault(key, this::parseList, def);
	}
	
	/*
	 * Helper method for parsing a list from a string
	 */
	List<String> parseList(String value){
		List<String> result = new ArrayList<>();
		for(String part : value.split(",")) {
			result.add(part.trim());
		}
		return result;
	}
	
}
