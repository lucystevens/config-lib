package uk.co.lukestevens.cli;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation to be specified when a help option
 * should be available to the user
 * 
 * @author Luke Stevens
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface CommandLineUsage {
	
	/**
	 * @return The usage text for this application e.g. 'java -jar SomeApp.jar
	 */
	String value();
	
	/**
	 * @return The short option key for help, defaults to 'h'
	 */
	String helpShortOpt() default "h";

}
