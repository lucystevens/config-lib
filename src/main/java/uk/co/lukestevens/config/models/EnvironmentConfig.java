package uk.co.lukestevens.config.models;

import java.util.AbstractMap;
import java.util.Set;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EnvironmentConfig extends BaseConfig {
	
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return System.getenv()
				.entrySet()
				.stream()
				.map(e -> new AbstractMap.SimpleEntry<Object, Object>(
						e.getKey(),
						e.getValue()))
				.collect(Collectors.toSet());
	}
	
	@Override
	public String get(String key) {
		return System.getenv(key);
	}

	@Override
	public void load() throws IOException { }

}
 