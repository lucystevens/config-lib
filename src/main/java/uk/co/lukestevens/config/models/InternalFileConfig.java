package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.encryption.EncryptionService;

/**
 * A config implementation that loads properties from within the classpath
 * 
 * @author Luke Stevens
 */
public class InternalFileConfig extends BaseConfig implements ConfigSource {

	private final Properties props = new Properties();
	private final String file;
	
	/**
	 * Creates a new configuration by loading properties from an internal file
	 * @param file The internal properties file to load from
	 * @param encryption The service to use to decrypt encrypted files
	 */
	public InternalFileConfig(String file, EncryptionService encryption) {
		super(encryption);
		this.file = file;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	@Override
	public Object get(String key) {
		return props.get(key);
	}

	@Override
	public void load() throws IOException {
		InputStream input = InternalFileConfig.class.getResourceAsStream(file);
		if(input != null) {
			this.props.load(input);
		}
	}

}
