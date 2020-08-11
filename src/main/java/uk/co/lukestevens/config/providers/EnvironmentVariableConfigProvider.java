package uk.co.lukestevens.config.providers;

import java.lang.reflect.Field;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class EnvironmentVariableConfigProvider implements ConfigValueProvider {

	@Override
	public String getConfigValue(Field field) {
		ConfigOption option = field.getAnnotation(ConfigOption.class);
		return System.getenv(option.envVar());
	}

}
