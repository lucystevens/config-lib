package uk.co.lukestevens.config;

import java.util.Map.Entry;

import uk.co.lukestevens.utils.InternalFileReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertyConfig extends AppConfig {
	
	final Properties props = new Properties();
	
	public void load(String file) throws IOException {
		InputStream input = InternalFileReader.class.getResourceAsStream(file);
		props.load(input);
	}
	
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	@Override
	public Object get(String key) {
		return props.get(key);
	}

}
