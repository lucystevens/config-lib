package uk.co.lukestevens.config.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.encryption.EncryptionService;

public class ConfigFileSource extends BaseConfig implements ConfigSource {
	
	private final File file;
	private final Properties props = new Properties();

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
