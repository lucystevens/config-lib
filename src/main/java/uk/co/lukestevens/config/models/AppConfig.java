package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.utils.Functions;

/**
 * A config implementation that takes a prioritised list of config sources
 * and checks each one in turn when fetching a config.
 * 
 * @author Luke Stevens
 */
public class AppConfig extends BaseConfig {
	
	private final List<ConfigSource> configs;
	
	/**
	 * Creates a new configuration by fetching configs from a prioritised list of config sources
	 * @param service The service to use to decrypt encrypted files
	 * @param configs A prioritised list of config sources
	 */
	public AppConfig(EncryptionService service, List<ConfigSource> configs) {
		super(service);
		this.configs = configs;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return this.configs.stream().map(ConfigSource::entrySet).flatMap(Set::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Functions::pickFirst)).entrySet();
	}

	@Override
	public Object get(String key) {
		Object result = null;
		for(ConfigSource config : configs) {
			result = config.get(key);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

}
