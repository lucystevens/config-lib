package uk.co.lukestevens.config.models;

import java.util.Map.Entry;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.utils.InternalFileReader;

public class SimpleConfig extends BaseConfig {
	
	private final Properties props = new Properties();

	public SimpleConfig(EncryptionService service) {
		super(service);
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	@Override
	public Object get(String key) {
		return props.get(key);
	}
	
	public void load(String file) throws IOException {
		InputStream input = InternalFileReader.class.getResourceAsStream(file);
		if(input != null) {
			this.props.load(input);
		}
	}
	
	public Properties getProperties() {
		return this.props;
	}

}
