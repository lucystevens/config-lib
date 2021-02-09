package uk.co.lukestevens.config.parsers;

/**
 * An interface defining a method to parse a String value
 * into an Object of a given type
 * 
 * @author Luke Stevens
 *
 * @param <T> The type this parses a String into
 */
@FunctionalInterface
public interface PropertyParser<T> {

	/**
	 * Parse a String into a given type
	 * @param value The String value to parse
	 * @return The parsed object
	 */
	public T parse(String value);
}
