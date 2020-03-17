package uk.co.lukestevens.config.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.encryption.EncryptionService;

/**
 * A config source implementation for resolving the base config source from file
 * 
 * @author Luke Stevens
 */
public class ConfigFileSource extends BaseConfig implements ConfigSource {
	
	private final File file;
	private final Properties props = new Properties();

	/**
	 * Creates a new config file source
	 * @param file The config file to load properties from
	 * @param service The encryption service to use to decrypt sensitive files
	 */
	public ConfigFileSource(File file, EncryptionService service) {
		super(service);
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
	
	/**
	 * Loads an additional config file into this source
	 * @param configFile The config file to load properties from
	 * @throws IOException
	 */
	public void load(File configFile) throws IOException {
		try(InputStream input = new FileInputStream(configFile)){
			if(input != null) {
				this.props.load(input);
			}
		}
	}

	@Override
	public void load() throws IOException {
		this.load(this.file);
	}

}
