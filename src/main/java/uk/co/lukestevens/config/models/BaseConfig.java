package uk.co.lukestevens.config.models;

import java.util.ArrayList;
import java.util.List;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.KeyParser;
import uk.co.lukestevens.config.exceptions.ConfigException;

/**
 * A base implementation of the Config interface that defines 
 * the advanced config parsing method, and relies on further 
 * implementations to provide only the basic 'get' method for
 * a property.
 * 
 * @author Luke Stevens
 */
public abstract class BaseConfig implements Config {
	
	static final KeyParser<Integer> INT_PARSER = Integer::parseInt;
	static final KeyParser<Double> DOUBLE_PARSER = Double::parseDouble;
	static final KeyParser<Boolean> BOOLEAN_PARSER = s -> s.equalsIgnoreCase("true");
	static final KeyParser<List<String>> LIST_PARSER = BaseConfig::parseList;
	

	/**
	 * Get a property value given the key
	 * @param key The key to retrieve a property for
	 * @return The value that corresponds with the given key
	 */
	public abstract String get(String key);
	
	@Override
	public String getAsString(String key){
		String value = this.get(key);
		if(value == null) {
			throw new ConfigException(key);
		} else {
			return value;
		}
	}
	
	@Override
	public String getAsStringOrDefault(String key, String def){
		String value = this.get(key);
		if(value == null) {
			return def;
		} else {
			return value;
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
		String value = this.get(key);
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
	public int getAsInt(String key) {
		return this.getParsedValue(key, INT_PARSER);
	}
	
	@Override
	public int getAsIntOrDefault(String key, int def) {
		return this.getParsedValueOrDefault(key, INT_PARSER, def);
	}
	
	@Override
	public double getAsDouble(String key) {
		return this.getParsedValue(key, DOUBLE_PARSER);
	}
	
	@Override
	public double getAsDoubleOrDefault(String key, double def) {
		return this.getParsedValueOrDefault(key, DOUBLE_PARSER, def);
	}
	
	@Override
	public boolean getAsBoolean(String key) {
		return this.getParsedValue(key, BOOLEAN_PARSER);
	}
	
	@Override
	public boolean getAsBooleanOrDefault(String key, boolean def) {
		return this.getParsedValueOrDefault(key, BOOLEAN_PARSER, def);
	}
	
	@Override
	public List<String> getAsList(String key) {
		return this.getParsedValue(key, LIST_PARSER);
	}
	
	@Override
	public List<String> getAsListOrDefault(String key, List<String> def) {
		return this.getParsedValueOrDefault(key, LIST_PARSER, def);
	}
	
	/*
	 * Helper method for parsing a list from a string
	 */
	static List<String> parseList(String value){
		List<String> result = new ArrayList<>();
		for(String part : value.split(",")) {
			result.add(part.trim());
		}
		return result;
	}
	
}
