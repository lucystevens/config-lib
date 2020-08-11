package uk.co.lukestevens.config.providers;

import java.lang.reflect.Field;

import org.apache.commons.cli.CommandLine;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class CommandLineConfigProvider implements ConfigValueProvider {
	
	final CommandLine commandLine;
	
	public CommandLineConfigProvider(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	@Override
	public String getConfigValue(Field field) {
		ConfigOption option = field.getAnnotation(ConfigOption.class);
		boolean hasArg = !(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class));
		return hasArg? 
				commandLine.getOptionValue(option.opt()) : 
				String.valueOf(commandLine.hasOption(option.opt()));
	}

}
