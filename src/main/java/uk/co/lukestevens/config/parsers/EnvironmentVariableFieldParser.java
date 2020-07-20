package uk.co.lukestevens.config.parsers;

import java.lang.reflect.Field;

import uk.co.lukestevens.cli.CommandLineOption;

public class EnvironmentVariableFieldParser implements FieldParser {

	@Override
	public String parse(Field field) {
		CommandLineOption option = field.getAnnotation(CommandLineOption.class);
		return System.getenv(option.envVar());
	}

}
