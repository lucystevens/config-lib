package uk.co.lukestevens.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import uk.co.lukestevens.cli.CommandLineOption;
import uk.co.lukestevens.cli.CommandLineUsage;
import uk.co.lukestevens.cli.args.ArgumentParser;
import uk.co.lukestevens.cli.args.ArgumentParserProvider;
import uk.co.lukestevens.config.models.CompoundConfig;
import uk.co.lukestevens.config.models.FileConfig;
import uk.co.lukestevens.config.models.MavenConfig;
import uk.co.lukestevens.config.parsers.CommandLineFieldParser;
import uk.co.lukestevens.config.parsers.ConfigFieldParser;
import uk.co.lukestevens.config.parsers.EnvironmentVariableFieldParser;
import uk.co.lukestevens.config.parsers.FieldParser;
import uk.co.lukestevens.utils.ReflectionUtils;

/**
 * A class used to parse command line arguments into setup objects
 * 
 * @author Luke Stevens
 */
public class SetupProvider<T> {
	
	private static final String CONFIG_OPTION = "c";
	
	final ArgumentParserProvider parserProvider;
	final ReflectionUtils reflection;
	final Class<T> c;

	/**
	 * Creates a new SetupProvider given a specific ArgumentParserProvider
	 * @param parserProvider The ArgumentParserProvider used to get the
	 * required parser for each class.
	 */
	public SetupProvider(ArgumentParserProvider parserProvider, ReflectionUtils reflection, Class<T> c) {
		this.parserProvider = parserProvider;
		this.reflection = reflection;
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
		
		// Get all fields annotated with CommandLineOption for this class (and superclasses)
		List<Field> fields = reflection.getAllFieldsWithAnnotation(c, CommandLineOption.class);
		
		// Create options object and add all fields to it
		Options options = new Options();
		for(Field field : fields) {
			this.addFieldToOptions(options, field);
		}
		
		// If usage has been specified, add the help command
		CommandLineUsage usage = c.getAnnotation(CommandLineUsage.class);
		if(usage != null) {
			options.addOption(usage.helpShortOpt(), "help", false, "Print script usage information");
		}
		
		// Add config option
		options.addOption(CONFIG_OPTION, "config", true, "A local config file used to override environment variable setup");
		
		// Parse commandline args
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		// If usage is specified, and help command provided, then print help and exit
		if(usage != null && cmd.hasOption(usage.helpShortOpt())) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(usage.value(), options);
			return null; // Don't continue with application if help specified
		}
		
		// Set up parsers
		List<FieldParser> parsers = new ArrayList<>();
		parsers.add(new EnvironmentVariableFieldParser());
		parsers.add(new CommandLineFieldParser(cmd));
		if(cmd.hasOption(CONFIG_OPTION)) {
			File configFile = new File(cmd.getOptionValue(CONFIG_OPTION));
			Config fileConfig = new FileConfig(configFile);
			Config mavenConfig = new MavenConfig();
			Config compoundConfig = new CompoundConfig(mavenConfig, fileConfig);
			compoundConfig.load();
			parsers.add(new ConfigFieldParser(compoundConfig));
		}
		
		// Create and populate result instance
		T result;
		try {
			result = c.newInstance();
			for(FieldParser fieldParser : parsers) {
				parseValues(result, fields, fieldParser);
			}
		} catch(InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	void parseValues(T setup, List<Field> fields, FieldParser parser) throws IllegalArgumentException, IllegalAccessException, ParseException {
		for(Field field : fields) {
			String value = parser.parse(field);
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
	 * Use the annotations and typing for this field to add a 
	 * new option value to the options object
	 */
	void addFieldToOptions(Options options, Field field) {
		CommandLineOption cli = field.getAnnotation(CommandLineOption.class);
		Builder builder = Option
				.builder(cli.opt())
				.hasArg(!(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)))
				.desc(cli.description());
		if(hasLongOpt(cli)) {
			builder.longOpt(cli.longOpt());
		}
		options.addOption(builder.build());
	}
	
	/*
	 * Checks whether this option has a long option value specified
	 */
	boolean hasLongOpt(CommandLineOption cli) {
		return !cli.longOpt().equals(CommandLineOption.LONG_OPT_DEFAULT);
	}

}
