package uk.co.lukestevens.config.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

/**
 * A config implementation for resolving the properties from file
 * 
 * @author Luke Stevens
 */
@Singleton
public class FileConfig extends PropertiesConfig {
	
	private final File file;

	/**
	 * Creates a new FileConfig
	 * @param file The config file to load properties from
	 */
	public FileConfig(File file) {
		super(new Properties());
		this.file = file;
	}
	
	/**
	 * Loads additional config into this config from an inputstream
	 * @param configFile The config stream to load properties from
	 * @throws IOException
	 */
	public void load(InputStream input) throws IOException {
		if(input != null) {
			this.props.load(input);
		}
		else {
			throw new FileNotFoundException(file.getName());
		}
	}

	@Override
	public void load() throws IOException {
		try(InputStream input = new FileInputStream(this.file)){
			this.load(input);
		}
	}

}
