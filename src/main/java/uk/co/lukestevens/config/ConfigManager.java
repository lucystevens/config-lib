package uk.co.lukestevens.config;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import uk.co.lukestevens.config.models.AppConfig;
import uk.co.lukestevens.config.models.ConfigFileSource;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.ConfigSource;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;

/**
 * A class to parse the environment config file and create
 * access to application and site configurations
 * 
 * @author Luke Stevens
 */
public class ConfigManager {
	
	private final ConfigFileSource config;
	private final EncryptionService encryption;
	
	/**
	 * Creates a new ConfigManager
	 * @param configFile The config file to use
	 * @param encryption The service to use to decrypt any encryptyed configs
	 */
	public ConfigManager(File configFile, EncryptionService encryption) {
		this.encryption = encryption;
		this.config = new ConfigFileSource(configFile, encryption);
		
		try {
			this.config.load();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private DatabaseConfig parseDatabaseConfig() {
		boolean databaseEnabled = config.getAsBooleanOrDefault("config.database.enabled", false);
		if(!databaseEnabled) {
			return null;
		}
		
		String databaseAlias = config.getAsStringOrDefault("config.database.alias", "config");
		Database configDb = new ConfiguredDatabase(config, databaseAlias);
		
		String applicationName = config.getAsString("application.name");
		PropertyService propertyService = DatabasePropertyService.forApplication(configDb, applicationName);
		return new DatabaseConfig(propertyService, encryption);
	}
	
	/**
	 * @return The application config for this application
	 */
	public AppConfig getAppConfig() {
		ConfigSource db = this.parseDatabaseConfig();
		return new AppConfig(encryption, this.config, db);
	}
	
	/**
	 * @return The SiteConfigService for accessing site-specific configs
	 */
	public SiteConfigService getSiteConfigService() {
		String databaseAlias = config.getAsStringOrDefault("config.database.alias", "config");
		Database configDb = new ConfiguredDatabase(config, databaseAlias);
		return new GenericSiteConfigService((site) -> new DatabaseConfig(DatabasePropertyService.forSite(configDb, site), this.encryption));
	}


}
