package uk.co.lukestevens.config;

/**
 * 
 * An exception to be thrown if there are any errors
 * when fetching keys from {@link Config}
 * 
 * @author luke.stevens
 *
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = -1449823977473131486L;
	
	/**
	 * Constructs a new AppConfigException for errors
	 * where the key is missing a required value.
	 * @param missingConfig The key that is missing a value
	 */
	public ConfigException(String missingConfig){
		super("Cannot find value for property: " + missingConfig+ ".");
	}
	
	/**
	 * Constructs an AppConfigException for errors where
	 * the value exists for a key, but some other root exception
	 * prevented it from being parsed correctly.
	 * @param brokenConfig The key for which the value could not be parsed
	 * @param root The root exception causing the error
	 */
	public ConfigException(String brokenConfig, Exception root){
		super("Could not load key: " + brokenConfig + ". Root cause: " + root.getMessage());
	}

}
