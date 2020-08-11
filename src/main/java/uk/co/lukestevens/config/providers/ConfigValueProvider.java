package uk.co.lukestevens.config.providers;

import java.lang.reflect.Field;

public interface ConfigValueProvider {
	
	String getConfigValue(Field field);

}
