package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.encryption.EncryptionService;

public class InternalFileConfig extends BaseConfig implements DerivedConfig {

	private final Properties props = new Properties();
	private final String file;
	
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
