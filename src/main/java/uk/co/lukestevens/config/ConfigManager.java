package uk.co.lukestevens.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import uk.co.lukestevens.config.annotations.ConfigFile;
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
@Singleton
public class ConfigManager {
	
	private final ConfigFileSource config;
	private final EncryptionService encryption;
	
	/**
	 * Creates a new ConfigManager
	 * @param configFile The external config file to use
	 * @param encryption The service to use to decrypt any encrypted configs
	 */
	@Inject
	public ConfigManager(@ConfigFile File configFile, EncryptionService encryption) {
		this.encryption = encryption;
		this.config = new ConfigFileSource(configFile, encryption);
		
		// Load primary config
		try {
			this.config.load();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		// Load properties from maven
		try (InputStream input = ConfigManager.class.getResourceAsStream("/conf/application.properties")){
			this.config.load(input);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Gets the database config (if applicable) from the base config
	 * @return A database config instance, or null if not enabled
	 */
	private DatabaseConfig parseDatabaseConfig() {
		boolean databaseEnabled = config.getAsBooleanOrDefault("config.database.enabled", false);
		if(!databaseEnabled) {
			return null;
		}
		
		String databaseAlias = config.getAsStringOrDefault("config.database.alias", "config");
		Database configDb = new ConfiguredDatabase(config, databaseAlias);
		
		String applicationName = config.getApplicationName();
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
