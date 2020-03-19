package uk.co.lukestevens.mocks;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import uk.co.lukestevens.config.models.BaseConfig;
import uk.co.lukestevens.config.models.ConfigSource;
import uk.co.lukestevens.encryption.IgnoredEncryptionService;

public class MockConfigSource extends BaseConfig implements ConfigSource{
	
	final Properties props;
	
	public MockConfigSource(Properties props) {
		super(new IgnoredEncryptionService());
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
