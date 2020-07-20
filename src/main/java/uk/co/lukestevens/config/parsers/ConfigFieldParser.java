package uk.co.lukestevens.config.parsers;

import java.lang.reflect.Field;

import uk.co.lukestevens.cli.CommandLineOption;
import uk.co.lukestevens.config.Config;

public class ConfigFieldParser implements FieldParser {
	
	final Config config;
	
	public ConfigFieldParser(Config config) {
		this.config = config;
	}

	@Override
	public String parse(Field field) {
		CommandLineOption option = field.getAnnotation(CommandLineOption.class);
		String configKey = option.configName().equals(CommandLineOption.CONFIG_NAME_DEFAULT)?
				config.getAsStringOrDefault("app.name", "core") + "." + field.getName() :
				option.configName();
		return config.getAsStringOrDefault(configKey, null);
	}

}
