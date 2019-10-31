package uk.co.lukestevens.config.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map.Entry;

import uk.co.lukestevens.utils.Dates;

import java.util.Properties;
import java.util.Set;

public class ExternalFileConfig implements DerivedConfig {

	private final Properties props = new Properties();
	private final File file;
	private final long refreshRate;
	
	private Date expiry = Dates.now();
	
	public ExternalFileConfig(File file, long refreshRate) {
		this.file = file;
		this.refreshRate = refreshRate;
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	@Override
	public Object get(String key) {
		if(this.expiry.before(Dates.now())){
			safeLoad();
		}
		return props.get(key);
	}
	
	void safeLoad() {
		try {
			this.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() throws IOException {
		this.props.clear();
		InputStream input = new FileInputStream(file);
		this.props.load(input);
		this.expiry = new Date(Dates.millis() + this.refreshRate);
	}

}
