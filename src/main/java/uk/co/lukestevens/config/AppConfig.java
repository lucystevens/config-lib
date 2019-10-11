package uk.co.lukestevens.config;

import uk.co.lukestevens.encryption.EncryptionService;

/**
 * @author luke.stevens
 */
public abstract class AppConfig implements Config {
	
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
	public String getEncrypted(String key, EncryptionService service) {
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
	
}
