package uk.co.lukestevens.config.parsers;

import java.lang.reflect.Field;
import javax.inject.Inject;

import org.apache.commons.cli.ParseException;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.annotations.ConfigOption;
import uk.co.lukestevens.config.annotations.SetupClass;
import uk.co.lukestevens.utils.ReflectionUtils;

public class ReflectiveConfigParser<T> implements ConfigParser<T> {
	
	final PropertyParserProvider parserProvider;
	final ReflectionUtils reflection;
	final Class<T> c;

	@Inject
	public ReflectiveConfigParser(PropertyParserProvider parserProvider, ReflectionUtils reflection, @SetupClass Class<T> c) {
		this.parserProvider = parserProvider;
		this.reflection = reflection;
		this.c = c;
	}

	@Override
	public T parseConfig(Config config) throws ParseException {
		T result;
		try {
			result = c.newInstance();
			
			for(Field field : reflection.getAllFieldsWithAnnotation(c, ConfigOption.class)) {
				String key = this.getConfigOptionName(field);
				String value = config.getAsStringOrDefault(key, field.getAnnotation(ConfigOption.class).defaultValue());
				if(!value.equals(ConfigOption.DEFAULT_VALUE_DEFAULT)) {
					PropertyParser<?> argParser = parserProvider.getParser(field.getType());
					if(argParser == null) {
						throw new ParseException("No parser found for class " + field.getType());
					}
					else {
						Object fieldValue = argParser.parse(value);
						field.setAccessible(true);
						field.set(result, fieldValue);
					}
				}
			}
			
		} catch(InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	String getConfigOptionName(Field field) {
		String name = field.getAnnotation(ConfigOption.class).name();
		return name == null? field.getName() : name;
	}

}
