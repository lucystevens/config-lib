package uk.co.lukestevens.config;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import uk.co.lukestevens.config.models.AppConfig;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.DerivedConfig;
import uk.co.lukestevens.config.models.ExternalFileConfig;
import uk.co.lukestevens.config.models.InternalFileConfig;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;

public class ConfigManager {
	
	private final InternalFileConfig config;
	private final EncryptionService encryption;
	
	public ConfigManager(String environment, EncryptionService encryption) {
		this.encryption = encryption;
		this.config = new InternalFileConfig("/conf/" + environment + ".conf", encryption);
		
		try {
			this.config.load();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private DerivedConfig parseConfig(String configName) {
		if(configName.equals("this")) {
			return config;
		}
		
		String configType = config.getAsString(configName + ".config.type");
		if(configType.equals("database")) {
			return this.parseDatabaseConfig(configName);
		}
		else if(configType.equals("external")) {
			return this.parseExternalConfig(configName);
		}
		else if(configType.equals("internal")) {
			return this.parseInternalConfig(configName);
		}
		
		return null;
	}
	
	private DatabaseConfig parseDatabaseConfig(String configName) {
		String applicationName = config.getAsString("application.name");
		Database configDb = new ConfiguredDatabase(config, configName + ".config");
		PropertyService propertyService = DatabasePropertyService.forApplication(configDb, applicationName);
		return new DatabaseConfig(propertyService, encryption);
	}
	
	private ExternalFileConfig parseExternalConfig(String configName) {
		int refreshRate = config.getAsIntOrDefault(configName + ".config.refresh.rate", 300);
		File file = config.getParsedValue(configName + ".config.file", File::new);
		return new ExternalFileConfig(file, refreshRate);
	}
	
	private InternalFileConfig parseInternalConfig(String configName) {
		String file = config.getAsString(configName + ".config.file");
		return new InternalFileConfig(file, encryption);
	}
	
	public AppConfig getAppConfig() {
		List<String> configs = config.getAsListOrDefault("config.sources", Collections.singletonList("this"));
		List<DerivedConfig> parsedConfigs = configs.stream().map(this::parseConfig).filter(Objects::nonNull).collect(Collectors.toList());
		
		return new AppConfig(encryption, parsedConfigs);
	}
	
	public SiteConfigService getSiteConfigService() {
		Database db = new ConfiguredDatabase(config, "site.config");
		return new DatabaseSiteConfigService((site) -> new DatabaseConfig(DatabasePropertyService.forSite(db, site), this.encryption));
	}


}
