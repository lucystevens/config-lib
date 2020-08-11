package uk.co.lukestevens.config.providers;

import java.lang.reflect.Field;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.annotations.ConfigOption;
import uk.co.lukestevens.config.models.MavenConfig;

public class PropertiesConfigProvider implements ConfigValueProvider {
	
	final Config config;
	
	public PropertiesConfigProvider(Config config) {
		this.config = config;
	}

	@Override
	public String getConfigValue(Field field) {
		ConfigOption option = field.getAnnotation(ConfigOption.class);
		String configKey = option.configName().equals(ConfigOption.CONFIG_NAME_DEFAULT)?
				config.getAsStringOrDefault(MavenConfig.NAME, "core") + "." + field.getName() :
				option.configName();
		return config.getAsStringOrDefault(configKey, null);
	}

}
