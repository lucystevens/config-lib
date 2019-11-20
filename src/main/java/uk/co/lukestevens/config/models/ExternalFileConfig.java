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

/**
 * A derived config implementation that loads properties
 * from an external file outside of the application
 * 
 * @author Luke Stevens
 */
public class ExternalFileConfig implements ConfigSource {

	private final Properties props = new Properties();
	private final File file;
	private final long refreshRate;
	
	private Date expiry = Dates.now();
	
	/**
	 * Creates a new configuration from an external file
	 * @param file The external file to load properties from
	 * @param refreshRate The time, in milliseconds, to wait
	 * before reloading the configuration from file
	 */
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
	
	/*
	 * Try to load the config file, and silently fail
	 * if an exception is thrown
	 */
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
		try(InputStream input = new FileInputStream(file)){
			this.props.load(input);
		}
		this.expiry = new Date(Dates.millis() + this.refreshRate);
	}

}
