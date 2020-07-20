package uk.co.lukestevens.cli.args;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to provide correct parsers for specific classes. This can either be instantiated
 * with no parsers for full customisability, or a default can be accessed using {@link getDefault()}
 * providing parsers for all primitives, String, Object and File.
 * 
 * @author Luke Stevens
 */
public final class ArgumentParserProvider {
	
	/**
	 * @return The default provider, with preset parsers for
	 * all primitives, String, Object and File classes.
	 */
	public static ArgumentParserProvider getDefault() {
		ArgumentParserProvider provider = new ArgumentParserProvider();
		provider.setParser(int.class, Integer::parseInt);
		provider.setParser(Integer.class, Integer::parseInt);
		
		provider.setParser(short.class, Short::parseShort);
		provider.setParser(Short.class, Short::parseShort);
		
		provider.setParser(long.class, Long::parseLong);
		provider.setParser(Long.class, Long::parseLong);
		
		provider.setParser(float.class, Float::parseFloat);
		provider.setParser(Float.class, Float::parseFloat);
		
		provider.setParser(double.class, Double::parseDouble);
		provider.setParser(Double.class, Double::parseDouble);
		
		provider.setParser(char.class, s -> s.charAt(0));
		provider.setParser(Character.class, s -> s.charAt(0));
		
		provider.setParser(boolean.class, Boolean::parseBoolean);
		provider.setParser(Boolean.class, Boolean::parseBoolean);
		
		provider.setParser(Object.class, s -> s);
		provider.setParser(String.class, s -> s);
		provider.setParser(File.class, File::new);
		
		return provider;
	}

	// Although not type checked, the helper methods ensure that the
	// key types are the same as the value types
	private final Map<Class<?>, ArgumentParser<?>> parsers = new HashMap<>();
	
	/**
	 * Sets a class and parser pair for this provider
	 * @param <T> The type to be returned when parsed
	 * @param c The class representing the parsed type
	 * @param parser The parser to parse a String into the given type
	 */
	public <T> void setParser(Class<T> c, ArgumentParser<T> parser) {
		this.parsers.put(c, parser);
	}
	
	/**
	 * Get the parser for a class
	 * @param <T> The type to be returned when parsed
	 * @param c The class representing the parsed type
	 * @return A parser to parse a String into the given type
	 */
	@SuppressWarnings("unchecked")
	public <T> ArgumentParser<T> getParser(Class<T> c){
		return (ArgumentParser<T>) this.parsers.get(c);
	}

}
