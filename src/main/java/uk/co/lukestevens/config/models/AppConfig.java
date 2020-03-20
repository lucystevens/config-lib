package uk.co.lukestevens.config.models;

import java.util.Map.Entry;

import javax.inject.Singleton;

import java.util.HashMap;
import java.util.Set;
import uk.co.lukestevens.encryption.EncryptionService;

/**
 * A config implementation that stores configs from both file and database.
 * 
 * Properties are first attempted to be loaded from the database, before then using
 * the base file as backup
 * 
 * @author Luke Stevens
 */
@Singleton
public class AppConfig extends BaseConfig {
	
	private final ConfigSource base;
	private final ConfigSource db;
	
	/**
	 * Creates a new application config from a base config source
	 * @param service The encryption service used to decrypt sensitive configs
	 * @param base The base config source, resolved from file
	 */
	public AppConfig(EncryptionService service, ConfigSource base) {
		this(service, base, null);
	}
	
	/**
	 * Creates a new application config from a base config source and a database config source
	 * @param service The encryption service used to decrypt sensitive configs
	 * @param base The base config source, resolved from file
	 * @param db The database config source
	 */
	public AppConfig(EncryptionService service, ConfigSource base, ConfigSource db) {
		super(service);
		this.base = base;
		this.db = db;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		HashMap<Object, Object> entryMap = new HashMap<>();
		for(Entry<Object, Object> entry : base.entrySet()) {
			entryMap.put(entry.getKey(), entry.getValue());
		}
		
		if(db != null) {
			for(Entry<Object, Object> entry : db.entrySet()) {
				entryMap.put(entry.getKey(), entry.getValue());
			}
		}

		return entryMap.entrySet();
	}

	@Override
	public Object get(String key) {
		Object result = null;
		if(db != null) {
			result = db.get(key);;
		}
		
		if(result == null) {
			result = base.get(key);
		}

		return result;
	}

}
