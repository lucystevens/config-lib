package uk.co.lukestevens.config.models;

import java.util.Set;
import java.io.IOException;
import java.util.Map.Entry;

public interface DerivedConfig {
	
	public Set<Entry<Object, Object>> entrySet();

	public Object get(String key);
	
	public void load() throws IOException;

}
