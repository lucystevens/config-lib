package uk.co.lukestevens.cli;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * TODO Should this be merged with config lib?
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
public @interface CommandLineOption {
	
	/**
	 * The default value for longOpt(), should be replaced with null when found
	 */
	public static final String LONG_OPT_DEFAULT = "V2Or8Jtyd7rHkmZ17LIc";
	
	/**
	 * The default value for configName, should be replaced with <application-name>.<variable-name>
	 */
	public static final String CONFIG_NAME_DEFAULT = "31UhiG1ZQd3TRk0Qcn4C";
	
	/**
	 * @return The short, single character code for this option
	 */
	public String opt();
	
	/**
	 * @return The long key code for this option
	 */
	public String longOpt() default LONG_OPT_DEFAULT;
	
	public String envVar();
	
	public String configName() default CONFIG_NAME_DEFAULT;
	
	/**
	 * @return The description for this option
	 */
	public String description() default "";
	

}
