package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

import uk.co.lukestevens.config.ApplicationProperties;

/**
 * A config implementation for resolving properties from the local maven file
 * 
 * @author Luke Stevens
 */
@Singleton
public class MavenConfig extends PropertiesConfig implements ApplicationProperties {
	
	private static final String MAVEN_PROPERTIES = "/mvn.properties";
	public static final String NAME = "application.name";
	public static final String VERSION = "application.version";
	public static final String GROUP = "application.group";

	public MavenConfig() {
		super(new Properties());
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
	}

	@Override
	public void load() throws IOException {
		try(InputStream input = MavenConfig.class.getResourceAsStream(MAVEN_PROPERTIES)){
			this.load(input);
		}
	}

	@Override
	public String getApplicationVersion() {
		return this.getAsString(VERSION);
	}

	@Override
	public String getApplicationName() {
		return this.getAsString(NAME);
	}

	@Override
	public String getApplicationGroup() {
		return this.getAsString(GROUP);
	}

}
