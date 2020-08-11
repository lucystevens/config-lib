package uk.co.lukestevens.config.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A config source implementation for resolving the base config source from file
 * 
 * @author Luke Stevens
 */
public class MavenConfig extends PropertiesConfig {
	
	private static final String MAVEN_PROPERTIES = "/mvn.properties";
	public static final String NAME = "application.name";
	public static final String VERSION = "application.version";
	public static final String GROUP = "application.group";

	public MavenConfig() {
		super(new Properties());
	}
	
	/**
	 * Loads additional config into this source from an inputstream
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

}
