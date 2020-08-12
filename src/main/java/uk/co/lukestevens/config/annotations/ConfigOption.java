package uk.co.lukestevens.config.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation to define when a field should be set from the argument of
 * a command line option. A class with fields annotated with this annotation
 * can be used to build a set of command line options.
 * 
 * Order of priority when building the application config is (lower number = lower priority):
 * 1) Fetch options from environment variables
 * 2) Override these variables with any specified on the command line
 * 3) Override these with any specified in a config file (if specified on the command line)
 * 
 * @author Luke Stevens
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ConfigOption {
	
	
	/**
	 * The default value for configName, should be replaced with <variable-name>
	 */
	public static final String CONFIG_NAME_DEFAULT = "CONFIG_NAME_DEFAULT";
	
	public static final String DEFAULT_VALUE_DEFAULT = "DEFAULT_VALUE_DEFAULT";
	
	
	/**
	 * @return The config key for this config, if overridden in a local config file.
	 * This defaults to <variable-name>.
	 */
	public String name() default CONFIG_NAME_DEFAULT;
	
	public String defaultValue() default DEFAULT_VALUE_DEFAULT;
	
	public boolean optional() default false;
	
}
