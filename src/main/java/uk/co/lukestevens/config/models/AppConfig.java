package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Set;
import uk.co.lukestevens.encryption.EncryptionService;

public class AppConfig extends BaseConfig {
	
	private final ConfigSource base;
	private final ConfigSource db;
	
	public AppConfig(EncryptionService service, ConfigSource base) {
		this(service, base, null);
	}
	
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
