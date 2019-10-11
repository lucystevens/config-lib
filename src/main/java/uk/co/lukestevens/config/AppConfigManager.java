package uk.co.lukestevens.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A static class to manage the loading of application
 * and environmental properties
 * 
 * @author luke.stevens
 */
public class AppConfigManager implements ConfigManager {
	
	private static final String CONF_FILE = "/conf/default.conf";
	
	/**
	 * Loads properties from both the default file and the 
	 * environment file
	 * @params environment The environment to load config for
	 * @return The AppConfig loaded from the properties files.
	 * @throws IOException If either of the conf files does not exist,
	 * or there are any other errors loading properties from them
	 */
	public static AppConfig load(String environment) throws IOException {
		PropertyConfig properties = new PropertyConfig();
		properties.load(CONF_FILE);
		properties.load("/conf/" + environment + ".conf");
		
		return properties;
	}
	
	final Config baseConfig;
	final Map<String, Config> siteConfigs = new HashMap<>();
	
	AppConfigManager(Config baseConfig) {
		this.baseConfig = baseConfig;
	}

	@Override
	public Config getBaseConfig() {
		return baseConfig;
	}

	@Override
	public Config getSiteConfig(String site) {
		return siteConfigs.get(site);
	}
	

}
