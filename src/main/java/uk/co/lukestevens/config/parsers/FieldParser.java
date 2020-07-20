package uk.co.lukestevens.config.parsers;

import java.lang.reflect.Field;

public interface FieldParser {
	
	String parse(Field field);

}
