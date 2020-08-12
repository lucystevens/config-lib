package uk.co.lukestevens.config.parsers;

import org.apache.commons.cli.ParseException;

import uk.co.lukestevens.config.Config;

public interface ConfigParser<T> {
	
	T parseConfig(Config config) throws ParseException;

}
