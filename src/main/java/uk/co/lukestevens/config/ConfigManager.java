package uk.co.lukestevens.config;

import java.io.IOException;
import uk.co.lukestevens.config.models.AppConfig;
import uk.co.lukestevens.config.models.SimpleConfig;
import uk.co.lukestevens.config.services.ApplicationPropertyService;
import uk.co.lukestevens.config.services.NoDatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.config.services.SitePropertyService;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;

public class ConfigManager {
	
	private static final String CONF_FILE = "/conf/default.conf";
	private SimpleConfig config;
	
	private final String environment;
	private final EncryptionService encryption;
	
	public ConfigManager(String environment, EncryptionService encryption) {
		this.environment = environment;
		this.encryption = encryption;
	}

	private SimpleConfig getConfig() throws IOException {
		if(this.config == null) {
			this.config = new SimpleConfig(encryption);
			config.load(CONF_FILE);
			config.load("/conf/" + environment + ".conf");
		}
		return config;
	}
	
	public AppConfig getAppConfig() throws IOException {
		SimpleConfig config = this.getConfig();
		
		boolean useDb = config.getAsBoolean("config.db.enabled");
		PropertyService propertyService = new NoDatabasePropertyService();
		if(useDb) {
			String applicationName = config.getAsString("application.name");
			Database configDb = new ConfiguredDatabase(config, "config");
			propertyService = new ApplicationPropertyService(configDb, applicationName);
		}

		return new AppConfig(propertyService, config.getProperties(), encryption);
	}
	
	public SiteConfigService getSiteConfigService() throws IOException {
		SimpleConfig config = this.getConfig();
		Database db = new ConfiguredDatabase(config, "site");
		
		return new DatabaseSiteConfigService((site) -> new AppConfig(new SitePropertyService(db, site), this.encryption));
	}


}
