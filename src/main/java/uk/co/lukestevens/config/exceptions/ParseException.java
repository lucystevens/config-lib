package uk.co.lukestevens.config.exceptions;

/**
 * An exception to be thrown when there are errors parsing
 * a class or value.
 * 
 * @author Luke Stevens
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 5129224712320296284L;
	
	/**
	 * Constructs a new ParseException for errors
	 * where the parser is missing an implementation for a class.
	 * @param missingClass The class that is missing a parser
	 */
	public ParseException(Class<?> missingClass){
		super(String.format("No parser found for class %s.", missingClass));
	}
	
	/**
	 * Constructs an ParseException for errors where
	 * the setup class could not be created or populated with values
	 * @param setupClass The setup class that could not be parsed
	 * @param root The root exception causing the error
	 */
	public ParseException(Class<?> setupClass, Exception root){
		super(String.format("Could not parse class: %s. Root cause: [%s] %s.", setupClass, root.getClass(), root.getMessage()));
	}

}
