package uk.co.lukestevens.testing.mocks;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.config.models.DatabaseConfig;

public class MockDatabaseConfig extends DatabaseConfig{
	
	final Properties props;
	
	public MockDatabaseConfig(Properties props) {
		super(null, null);
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
