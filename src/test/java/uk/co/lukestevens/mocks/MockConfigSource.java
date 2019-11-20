package uk.co.lukestevens.mocks;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.config.models.ConfigSource;

public class MockConfigSource implements ConfigSource{
	
	final Properties props;
	
	public MockConfigSource(Properties props) {
		this.props = props;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return this.props.entrySet();
	}

	@Override
	public Object get(String key) {
		return this.props.get(key);
	}

	@Override
	public void load() throws IOException { }

}
