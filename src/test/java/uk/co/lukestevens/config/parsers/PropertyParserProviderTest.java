package uk.co.lukestevens.config.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;
import uk.co.lukestevens.config.exceptions.ParseException;


public class PropertyParserProviderTest {
	
	PropertyParserProvider provider = PropertyParserProvider.getDefault();
	
	// ------------ ints ------------
	
	@Test
	public void testParsePrimitiveInt_valid() throws ParseException {
		PropertyParser<Integer> parser = provider.getParser(int.class);
		assertNotNull(parser);
		
		int value = parser.parse("2147483647");
		assertEquals(Integer.MAX_VALUE, value);
	}
	
	@Test
	public void testParsePrimitiveInt_invalid() throws ParseException {
		PropertyParser<Integer> parser = provider.getParser(int.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notanint"));
	}
	
	@Test
	public void testParseInteger_valid() throws ParseException {
		PropertyParser<Integer> parser = provider.getParser(Integer.class);
		assertNotNull(parser);
		
		Integer value = parser.parse("2147483647");
		assertEquals(Integer.MAX_VALUE, value);
	}
	
	@Test
	public void testParseInteger_invalid() throws ParseException {
		PropertyParser<Integer> parser = provider.getParser(Integer.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notanint"));
	}
	
	// ------------ shorts ------------
	
	@Test
	public void testParsePrimitiveShort_valid() throws ParseException {
		PropertyParser<Short> parser = provider.getParser(short.class);
		assertNotNull(parser);
		
		short value = parser.parse("32767");
		assertEquals(Short.MAX_VALUE, value);
	}
	
	@Test
	public void testParsePrimitiveShort_invalid() throws ParseException {
		PropertyParser<Short> parser = provider.getParser(short.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notashort"));
	}
	
	@Test
	public void testParseShort_valid() throws ParseException {
		PropertyParser<Short> parser = provider.getParser(Short.class);
		assertNotNull(parser);
		
		Short value = parser.parse("32767");
		assertEquals(Short.MAX_VALUE, value);
	}
	
	@Test
	public void testParseShort_invalid() throws ParseException {
		PropertyParser<Short> parser = provider.getParser(Short.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notashort"));
	}
	
	// ------------ longs ------------
	
	@Test
	public void testParsePrimitiveLong_valid() throws ParseException {
		PropertyParser<Long> parser = provider.getParser(long.class);
		assertNotNull(parser);
		
		long value = parser.parse("9223372036854775807");
		assertEquals(Long.MAX_VALUE, value);
	}
	
	@Test
	public void testParsePrimitiveLong_invalid() throws ParseException {
		PropertyParser<Long> parser = provider.getParser(long.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notalong"));
	}
	
	@Test
	public void testParseLong_valid() throws ParseException {
		PropertyParser<Long> parser = provider.getParser(Long.class);
		assertNotNull(parser);
		
		Long value = parser.parse("9223372036854775807");
		assertEquals(Long.MAX_VALUE, value);
	}
	
	@Test
	public void testParseLong_invalid() throws ParseException {
		PropertyParser<Long> parser = provider.getParser(Long.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notalong"));
	}
	
	// ------------ floats ------------
	
	@Test
	public void testParsePrimitiveFloat_valid() throws ParseException {
		PropertyParser<Float> parser = provider.getParser(float.class);
		assertNotNull(parser);
		
		float value = parser.parse("9223372036854775807");
		assertEquals(Long.MAX_VALUE, value);
	}
	
	@Test
	public void testParsePrimitiveFloat_invalid() throws ParseException {
		PropertyParser<Float> parser = provider.getParser(float.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notafloat"));
	}
	
	@Test
	public void testParseFloat_valid() throws ParseException {
		PropertyParser<Float> parser = provider.getParser(Float.class);
		assertNotNull(parser);
		
		Float value = parser.parse("9223372036854775807");
		assertEquals(Long.MAX_VALUE, value);
	}
	
	@Test
	public void testParseFloat_invalid() throws ParseException {
		PropertyParser<Float> parser = provider.getParser(Float.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notafloat"));
	}
	
	// ------------ doubles ------------
	
	@Test
	public void testParsePrimitiveDouble_valid() throws ParseException {
		PropertyParser<Double> parser = provider.getParser(double.class);
		assertNotNull(parser);
		
		double value = parser.parse("37.4");
		assertEquals(37.4, value);
	}
	
	@Test
	public void testParsePrimitiveDouble_invalid() throws ParseException {
		PropertyParser<Double> parser = provider.getParser(double.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notadouble"));
	}
	
	@Test
	public void testParseDouble_valid() throws ParseException {
		PropertyParser<Double> parser = provider.getParser(Double.class);
		assertNotNull(parser);
		
		Double value = parser.parse("-17.5");
		assertEquals(-17.5, value);
	}
	
	@Test
	public void testParseDouble_invalid() throws ParseException {
		PropertyParser<Double> parser = provider.getParser(Double.class);
		assertNotNull(parser);
		
		assertThrows(NumberFormatException.class, () -> parser.parse("notadouble"));
	}
	
	// ------------ chars ------------
	
	@Test
	public void testParsePrimitiveCharacter_valid() throws ParseException {
		PropertyParser<Character> parser = provider.getParser(char.class);
		assertNotNull(parser);
		
		char value = parser.parse("s");
		assertEquals('s', value);
	}
	
	@Test
	public void testParsePrimitiveCharacter_invalid() throws ParseException {
		PropertyParser<Character> parser = provider.getParser(char.class);
		assertNotNull(parser);
		
		assertThrows(IndexOutOfBoundsException.class, () -> parser.parse(""));
	}
	
	@Test
	public void testParseCharacter_valid() throws ParseException {
		PropertyParser<Character> parser = provider.getParser(Character.class);
		assertNotNull(parser);
		
		Character value = parser.parse("#~");
		assertEquals('#', value);
	}
	
	@Test
	public void testParseCharacter_invalid() throws ParseException {
		PropertyParser<Character> parser = provider.getParser(Character.class);
		assertNotNull(parser);
		
		assertThrows(IndexOutOfBoundsException.class, () -> parser.parse(""));
	}
	
	// ------------ booleans ------------
	
	@Test
	public void testParsePrimitiveBoolean() throws ParseException {
		PropertyParser<Boolean> parser = provider.getParser(boolean.class);
		assertNotNull(parser);
		
		boolean value = parser.parse("true");
		assertTrue(value);
	}
	
	@Test
	public void testParseBoolean() throws ParseException {
		PropertyParser<Boolean> parser = provider.getParser(Boolean.class);
		assertNotNull(parser);
		
		Boolean value = parser.parse("false");
		assertFalse(value);
	}
	
	
	// ------------ string ------------
	
	@Test
	public void testParseString() throws ParseException {
		PropertyParser<String> parser = provider.getParser(String.class);
		assertNotNull(parser);
		
		{
			String value = parser.parse("iamastring!");
			assertEquals("iamastring!", value);
		}
		
		{
			String value = parser.parse(null);
			assertNull(value);
		}
	}
	
	// ------------ object ------------
	
	@Test
	public void testParseObject() throws ParseException {
		PropertyParser<Object> parser = provider.getParser(Object.class);
		assertNotNull(parser);
		
		{
			Object value = parser.parse("iamastring!");
			assertEquals("iamastring!", value);
		}
		
		{
			Object value = parser.parse(null);
			assertNull(value);
		}
	}
	
	// ------------ file ------------
	
	@Test
	public void testParseFile() throws ParseException {
		PropertyParser<File> parser = provider.getParser(File.class);
		assertNotNull(parser);
		
		File value = parser.parse("src/main/resources");
		assertEquals(new File("src/main/resources"), value);
		
	}
	

}
