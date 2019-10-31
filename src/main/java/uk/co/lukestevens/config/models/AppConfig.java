package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.lukestevens.encryption.EncryptionService;

public class AppConfig extends BaseConfig {
	
	private final List<DerivedConfig> configs;
	
	public AppConfig(EncryptionService service, List<DerivedConfig> configs) {
		super(service);
		this.configs = configs;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return this.configs.stream().map(DerivedConfig::entrySet).flatMap(Set::stream).collect(Collectors.toSet());
	}

	@Override
	public Object get(String key) {
		Object result = null;
		for(DerivedConfig config : configs) {
			result = config.get(key);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

}
