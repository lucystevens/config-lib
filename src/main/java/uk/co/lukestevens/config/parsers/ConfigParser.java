package uk.co.lukestevens.config.parsers;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.exceptions.ParseException;

public interface ConfigParser<T> {
	
	T parseConfig(Config config) throws ParseException;

}
