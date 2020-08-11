package uk.co.lukestevens.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import uk.co.lukestevens.config.annotations.ConfigOption;
import uk.co.lukestevens.config.annotations.SetupClass;
import uk.co.lukestevens.config.models.CompoundConfig;
import uk.co.lukestevens.config.models.FileConfig;
import uk.co.lukestevens.config.models.MavenConfig;
import uk.co.lukestevens.config.parsers.ArgumentParser;
import uk.co.lukestevens.config.parsers.ArgumentParserProvider;
import uk.co.lukestevens.config.providers.CommandLineConfigProvider;
import uk.co.lukestevens.config.providers.PropertiesConfigProvider;
import uk.co.lukestevens.config.providers.EnvironmentVariableConfigProvider;
import uk.co.lukestevens.config.providers.ConfigValueProvider;
import uk.co.lukestevens.utils.ReflectionUtils;

/**
 * A class used to parse configs from several sources into a setup object
 * 
 * @author Luke Stevens
 */
public class SetupProvider<T> {
	
	private static final String CONFIG_OPTION = "c";
	
	final ArgumentParserProvider parserProvider;
	final ReflectionUtils reflection;
	final List<Field> fields;
	final Class<T> c;

	/**
	 * Creates a new SetupProvider given a specific ArgumentParserProvider
	 * @param parserProvider The ArgumentParserProvider used to get the
	 * required parser for each class.
	 */
	@Inject
	public SetupProvider(ArgumentParserProvider parserProvider, ReflectionUtils reflection, @SetupClass Class<T> c) {
		this.parserProvider = parserProvider;
		this.reflection = reflection;
		this.fields = reflection.getAllFieldsWithAnnotation(c, ConfigOption.class);
		this.c = c;
	}

	/**
	 * @return The ArgumentParserProvider this parser is currently using. This is
	 * the preferred method for adding custom parsers to the provider.
	 */
	public ArgumentParserProvider getParserProvider() {
		return parserProvider;
	}
	
	/**
	 * Parse a set of command line arguments into an object of a specific type
	 * @param <T> The type of object to parse the arguments into
	 * @param args A String array of command line arguments
	 * @param c The class of the object to parse the arguments into
	 * @return An instance of the given object type, with fields values matching
	 * the provided arguments, or null if a valid 'help' command was selected.
	 * @throws ParseException If the command line arguments are malformed or missing
	 * @throws IOException 
	 */
	public T parseCommandLine(String[] args) throws ParseException, IOException {
		Options options = createCommandLineOptions();
		
		// Parse commandline args
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		// Set up parsers
		List<ConfigValueProvider> configProviders = setupConfigProviders(cmd);
		
		// Create and populate result instance
		T setup = createSetupFromFields(configProviders);
		try {
			checkSetupComplete(setup);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return setup;
	}
	
	/*
	 * Create the set of config providers to be used when creating this setup, in descending order of priority
	 */
	List<ConfigValueProvider> setupConfigProviders(CommandLine cmd) throws IOException {
		List<ConfigValueProvider> configProviders = new ArrayList<>();
		configProviders.add(new EnvironmentVariableConfigProvider());
		configProviders.add(new CommandLineConfigProvider(cmd));
		if(cmd.hasOption(CONFIG_OPTION)) {
			File configFile = new File(cmd.getOptionValue(CONFIG_OPTION));
			Config fileConfig = new FileConfig(configFile);
			Config mavenConfig = new MavenConfig();
			Config compoundConfig = new CompoundConfig(mavenConfig, fileConfig);
			compoundConfig.load();
			configProviders.add(new PropertiesConfigProvider(compoundConfig));
		}
		return configProviders;
	}
	
	/*
	 * Create a setup object from the class fields, and the set of config providers used
	 * to fetch the class values
	 */
	T createSetupFromFields(List<ConfigValueProvider> configProviders) throws IllegalArgumentException, ParseException {
		T result;
		try {
			result = c.newInstance();
			for(ConfigValueProvider configProvider : configProviders) {
				setupConfigValuesFromProvider(result, configProvider);
			}
		} catch(InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/*
	 * Given a setup object and a list of all class fields, this uses a specific configProvider to fetch
	 * the required config values
	 */
	void setupConfigValuesFromProvider(T setup, ConfigValueProvider configProvider) throws IllegalArgumentException, IllegalAccessException, ParseException {
		for(Field field : fields) {
			String value = configProvider.getConfigValue(field);
			if(value != null) {
				ArgumentParser<?> argParser = parserProvider.getParser(field.getType());
				if(argParser == null) {
					throw new ParseException("No parser found for class " + field.getType());
				}
				else {
					Object fieldValue = argParser.parse(value);
					field.setAccessible(true);
					field.set(setup, fieldValue);
				}
			}
		}
	}
	
	/*
	 * Check that setup is complete, and all fields have been populated
	 */
	void checkSetupComplete(T setup) throws IllegalArgumentException, IllegalAccessException {
		for(Field field : fields) {
			field.setAccessible(true);
			Object value = field.get(setup);
			if(value == null) {
				throw new ConfigException(c.getSimpleName() + "." + field.getName());
			}
		}
	}
	
	Options createCommandLineOptions() {
		// Create options object and add all fields to it
		Options options = new Options();
		for(Field field : fields) {
			this.addFieldToOptions(options, field);
		}
		
		// Add config option
		options.addOption(Option
				.builder(CONFIG_OPTION)
				.hasArg()
				.longOpt("config")
				.build());
		// options.addOption(CONFIG_OPTION, true, "config");
		return options;
	}
	
	
	/*
	 * Use the annotations and typing for this field to add a 
	 * new option value to the options object
	 */
	void addFieldToOptions(Options options, Field field) {
		ConfigOption cli = field.getAnnotation(ConfigOption.class);
		Builder builder = Option
				.builder(cli.opt())
				.hasArg(!(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)));
		if(hasLongOpt(cli)) {
			builder.longOpt(cli.longOpt());
		}
		options.addOption(builder.build());
	}
	
	/*
	 * Checks whether this option has a long option value specified
	 */
	boolean hasLongOpt(ConfigOption cli) {
		return !cli.longOpt().equals(ConfigOption.LONG_OPT_DEFAULT);
	}

}
