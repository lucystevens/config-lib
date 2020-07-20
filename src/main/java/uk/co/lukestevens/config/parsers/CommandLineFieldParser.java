package uk.co.lukestevens.config.parsers;

import java.lang.reflect.Field;

import org.apache.commons.cli.CommandLine;

import uk.co.lukestevens.cli.CommandLineOption;

public class CommandLineFieldParser implements FieldParser {
	
	final CommandLine commandLine;
	
	public CommandLineFieldParser(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	@Override
	public String parse(Field field) {
		CommandLineOption option = field.getAnnotation(CommandLineOption.class);
		boolean hasArg = !(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class));
		return hasArg? 
				commandLine.getOptionValue(option.opt()) : 
				String.valueOf(commandLine.hasOption(option.opt()));
	}

}
